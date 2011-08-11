// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.modern.sha3.skein.algorithm;

import static org.jcryptool.crypto.modern.sha3.skein.algorithm.SkeinUtil.lsbArrayOfLongToBytes;
import static org.jcryptool.crypto.modern.sha3.skein.algorithm.SkeinUtil.lsbBytesToArrayOfLong;
import static org.jcryptool.crypto.modern.sha3.skein.algorithm.SkeinUtil.zeroPad;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.jcryptool.crypto.modern.sha3.skein.threefish.ThreefishImpl;
import org.jcryptool.crypto.modern.sha3.skein.threefish.ThreefishSecretKey;

/**
 * SkeinMain is used to test the SimpleSkein, UBI64 and Threefish implementations and to see that the test vectors,
 * including initial chaining vectors are correct. The output of the main() method has been used to successfully confirm
 * the old and new chaining values as created by the Skein team.
 * <P>
 * The initial implementation incorrectly used the blocksize as size of the configuration encoding instead of the real
 * size of the configuration encoding. You can simulate this incorrect behaviour by simply setting the private
 * USE_BLOCK_SIZE constant to true.
 * <P>
 * The Threefish part of the protocol is of course the most difficult part of the algorithm to implement. To test the
 * intermediate values of your own implementation, just change the logger settings below. For full logging to the
 * console, just set the logging level to Level.FINEST instead of Level.OFF.
 *
 * @author maartenb
 * @author $Author: $
 * @since 5 nov 2008
 * @version $Revision: $
 */
public class SkeinAction {
    private static final boolean USE_BLOCK_SIZE = false;
    private static final boolean INITIAL_CHAINING_VALUES_IN_JAVA = false;

    /**
     * Run method for Skein
     *
     * @return
     */

    public byte[] run(int blockSize, String str, int outputSize) {
        byte[] output;
        /*
         * if (outputSize == 512){ byte[] result = new byte[outputSize/8]; Skein512Small.hash(str.getBytes(),result);
         * output=result; }else
         */
        output = doSimpleSkein(blockSize, str.getBytes(), outputSize);
        return output;
    }

    static {
        Logger threefishLogger = Logger.getLogger(ThreefishImpl.class.getName());
        threefishLogger.setLevel(Level.OFF);
        Handler handler = new ConsoleHandler();
        Formatter myFormatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getMessage();
            }

        };

        handler.setFormatter(myFormatter);
        handler.setLevel(Level.FINEST);
        threefishLogger.addHandler(handler);
    }

    public SkeinAction() {
        super();
    }

    public static void testThreefish(final int blockSize, final int rounds) {
        final ThreefishImpl impl;
        if (rounds <= 0) {
            impl = new ThreefishImpl(blockSize);
        } else {
            impl = new ThreefishImpl(blockSize, rounds);
        }

        byte[] keyData = new byte[blockSize / Byte.SIZE]; // initialized to 00h values, used later on
        final long[] tweak;
        try {
            SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
            rnd.nextBytes(keyData);
            tweak = new long[] {rnd.nextLong(), rnd.nextLong()};
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        ThreefishSecretKey sk = new ThreefishSecretKey(keyData);

        impl.init(sk, tweak);
        final byte[] plain = "Maarten".getBytes();
        final byte[] plainPadded = zeroPad(plain, impl.getBlockSize());

        final long[] encryptedBlock = new long[impl.getBlockSize() / Long.SIZE];

        final long[] plainBlock = lsbBytesToArrayOfLong(plainPadded);

        // not needed: impl.init(sk, tweak);
        impl.blockEncrypt(plainBlock, encryptedBlock);

        long[] decryptedBlock = new long[encryptedBlock.length];
        impl.blockDecrypt(encryptedBlock, decryptedBlock);
    }

    public static byte[] doSimpleSkein(final int blockSize, final byte[] message, final int outputSize) {
        final int blockSizeBytes = blockSize / Byte.SIZE;

        if (message == null) {
            throw new IllegalArgumentException("Please provide a message, even one of 0 bytes to process");
        }

        if (outputSize <= 0 || outputSize % Byte.SIZE != 0) {
            throw new IllegalArgumentException(
                    "The output size N must fullfil N MOD 8 = 0 (a complete number of bytes)");
        }

        // create buffer
        byte[] blockBuffer = new byte[blockSizeBytes];

        // create cipher
        ThreefishImpl threefish = new ThreefishImpl(blockSize);

        // create and init UBI
        SkeinUbi64 ubi = new SkeinUbi64(threefish);
        ubi.init();

        // create configuration
        SkeinAlgorithm.Configuration config = new SkeinAlgorithm.Configuration(outputSize, 0, 0, 0);
        byte[] configEncoding = config.getEncoded();

        // padded automatically, block is still filled with 00h values
        System.arraycopy(configEncoding, 0, blockBuffer, 0, configEncoding.length);
        long[] blockWords = lsbBytesToArrayOfLong(blockBuffer);

        // create tweak for configuration
        // used configEncoding.length, but it seems the entire block should be in the tweak value (???) -> see question
        // on site
        int configSize = configEncoding.length;
        if (USE_BLOCK_SIZE) { // wrong, but whatever
            configSize = blockSizeBytes;
        }

        SkeinAlgorithm.Tweak tweak = new SkeinAlgorithm.Tweak(true, true, SkeinAlgorithm.Tweak.T_CFG, false, 0,
                configSize);

        final long configTweak[] = {tweak.getT0(), tweak.getT1()};

        // update UBI with configuration
        ubi.update(blockWords, configTweak);

        // process message in blocks
        int bytesProcessed = 0;

        int messageLength = message.length;
        while (bytesProcessed < messageLength) {
            final int available = messageLength - bytesProcessed;
            final int toblock = Math.min(blockSizeBytes, available);
            System.arraycopy(message, bytesProcessed, blockBuffer, 0, toblock);

            // pad block itself (not the bits)
            if (toblock != blockSizeBytes) {
                for (int i = toblock; i < blockSizeBytes; i++) {
                    blockBuffer[i] = 0;
                }
            }

            blockWords = lsbBytesToArrayOfLong(blockBuffer);

            // already update bytesProcessed (needed for tweak)
            bytesProcessed += toblock;

            tweak = new SkeinAlgorithm.Tweak(bytesProcessed == messageLength, bytesProcessed <= blockSizeBytes,
                    SkeinAlgorithm.Tweak.T_MSG, false, 0, bytesProcessed);

            // finally do the actual update
            ubi.update(blockWords, new long[] {tweak.getT0(), tweak.getT1()});
        }

        final int outputBlocks = (outputSize - 1) / blockSize + 1;

        // create a new set of longs of the same size (terrible hack, but whatever)
        long[] inputForOutput = new long[blockWords.length];
        for (int i = 0; i < outputBlocks; i++) {
            // create input for the OUTPUT function
            inputForOutput[0] = i;

            tweak = new SkeinAlgorithm.Tweak(i == outputBlocks - 1, i == 0, SkeinAlgorithm.Tweak.T_OUT, false, 0, 8);

            ubi.update(inputForOutput, new long[] {tweak.getT0(), tweak.getT1()});
        }

        final long[] outputWords = ubi.getOutput();

        final byte[] output = lsbArrayOfLongToBytes(outputWords);

        return output;
    }

    public static void showConfigurationInit(final int blockSize, final int outputSize) {

        final int blockSizeBytes = blockSize / Byte.SIZE;

        if (outputSize <= 0 || outputSize % Byte.SIZE != 0) {
            throw new IllegalArgumentException(
                    "The output size N must fullfil N MOD 8 = 0 (a complete number of bytes)");
        }

        // create buffer
        byte[] blockBuffer = new byte[blockSizeBytes];

        // create cipher
        ThreefishImpl threefish = new ThreefishImpl(blockSize);

        // create and init UBI
        SkeinUbi64 ubi = new SkeinUbi64(threefish);
        ubi.init();

        // create configuration
        SkeinAlgorithm.Configuration config = new SkeinAlgorithm.Configuration(outputSize, 0, 0, 0);
        byte[] configEncoding = config.getEncoded();
        // padded automatically, block is still filled with 00h values
        System.arraycopy(configEncoding, 0, blockBuffer, 0, configEncoding.length);
        long[] blockWords = lsbBytesToArrayOfLong(blockBuffer);

        int configSize = configEncoding.length;
        if (USE_BLOCK_SIZE) { // wrong, but whatever
            configSize = blockSizeBytes;
        }

        // create tweak for configuration
        // used configEncoding.length, but it seems the entire block should be in the tweak value (???) -> see question
        // on site
        SkeinAlgorithm.Tweak tweak = new SkeinAlgorithm.Tweak(true, true, SkeinAlgorithm.Tweak.T_CFG, false, 0,
                configSize);

        final long configTweak[] = {tweak.getT0(), tweak.getT1()};

        // update UBI with configuration
        ubi.update(blockWords, configTweak);
        long[] initialChainingValue = ubi.getOutput();

        if (INITIAL_CHAINING_VALUES_IN_JAVA) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < initialChainingValue.length; i++) {
                sb.append(String.format("0x%016XL", initialChainingValue[i]));
                if (i != initialChainingValue.length - 1) {
                    sb.append(", ");
                    if (i % 4 == 3) {
                        sb.append(String.format("%n"));
                    }
                }
            }
            sb.append(String.format("%n"));
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < initialChainingValue.length; i++) {
                sb.append(String.format("0x%016X", initialChainingValue[i]));
                if (i != initialChainingValue.length - 1) {
                    sb.append(", ");
                    if (i % 4 == 3) {
                        sb.append(String.format("%n"));
                    }
                }
            }

        }
    }
}
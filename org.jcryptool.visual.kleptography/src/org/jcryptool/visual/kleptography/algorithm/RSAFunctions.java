// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.kleptography.algorithm;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * A set of functions and basic functions necessary for RSAMain.
 * @author Patrick Vacek
 */
public class RSAFunctions {

	/**
	 * Can be freely changed, as long as it is a multiple of 2.
	 * SETUP requires that it be a multiple of 4.
	 * Recommended minimum for real-world application is 1024;
	 * Anything less is too easily factorable and 2048 is now often suggested.
	 * Note that this is really just a default; the user can change this at will.
	 * (Only valid input is accepted, though!)
	 */
	private int bitCount = 64;

	/**
	 * bitCount / 8, rounded down. This is the number of bytes that count as one
	 * block of the message and thus are calculated as one unit.
	 */
	private int blockSizeMessage = 8;

	/**
	 * bitCount / 8, rounded up. This is the number of bytes that count as one
	 * block of the ciphertext and thus are calculated as one unit.
	 */
	private int blockSizeCipher = 8;

	/**
	 * Defines the character encoding that will be used to convert the
	 * input message into bytes and the deciphered encrypted text back
	 * into plaintext. Changing this may require additional changes to
	 * the encryption/decryption methods. I have found UTF-16 to work
	 * best since it handles most special characters without a problem.
	 */
	public static final String charEncoding = "UTF-16BE"; //$NON-NLS-1$

	/** List of possible hex values. Nothing surprising here! */
	public static final String HEXES = "0123456789abcdef"; //$NON-NLS-1$

	/** General-purpose pseudo-random generator for all basic needs. */
	private Random random = new Random();

	/**
	 * Set the bitCount and also the blockSizes. Both should be bitCount / 8, but
	 * blockSizeMessage rounds down and blockSizeCipher rounds up.
	 * There is a good reason why the block size for the message and the cipher
	 * are not the same. It all has to do with bitCounts not evenly divisible by 8.
	 * Since all encryption and decryption work is done via bytes (8 bits), other bitCounts
	 * cannot be done in a straightforward style where message.length == ciphertext.length.
	 * If we do nothing, and let the blockSize be universal and rounded down, then many
	 * values will encrypt into values that require an extra byte (since N itself requires
	 * that extra byte, were it to be stored as a byte array), and this messes up the plan.
	 * If we keep the blockSize shared but round it up, then sometimes we will take a set
	 * of bytes that turn out to be greater than N - which means that in decryption, we
	 * will get the wrong value back. By rounding the message blockSize down but the ciphertext's
	 * up, we keep a constant and controllable message size and if the ciphertext requires
	 * an extra byte, we can handle it. This is not perfect, but it is functional.
	 * Downsides are that all bitCounts not divisble by 8 create ciphertexts that are
	 * longer than their messages, and bitCounts less than 8 should not be allowed,
	 * since the only possible prime pair is (5,7) and thus N = 35, so only characters less
	 * with ASCII values less than 35 can be encoded. Even with bitCount = 8, the only prime
	 * pair is (11,13) and N = 143, which will still cause problems for non ASCII-7 characters.
	 * To be able to encrypt all UTF-16 characters, the minimum bit length is 18.
	 * (For SETUP attacks, though, bitCount must be divisible by 4, thus the minimum is then 20.
	 * @param bitCount The key bit size.
	 */
	public void setBitCount(int bitCount) {
		this.bitCount = bitCount;
		blockSizeMessage = getBitCount() / 8;
		if(blockSizeMessage == 0)
			blockSizeMessage++;
		blockSizeCipher = (int) Math.ceil((double) getBitCount() / 8);
	}
	public int getBitCount() {
		return bitCount;
	}

	/**
	 * blockSize is defined as bitCount / 8, rounded up, since we will be working in
	 * bytes, not bits.
	 * @return The block size, i.e. how many bytes count as one unit for encrypting.
	 */
	public final int getBlockSizeMessage() {
		return blockSizeMessage;
	}
	public final int getBlockSizeCipher() {
		return blockSizeCipher;
	}

	public Random getRandom() {
		return random;
	}

	/**
	 * Calculates N = P * Q
	 * @param p A prime number.
	 * @param q Another prime number.
	 * @return The composite N = P * Q.
	 */
	public BigInteger calculateN(BigInteger p, BigInteger q) {
		return (new BigInteger(p.multiply(q).toString()));
	}

	/**
	 * Calculates Phi, Euler's totient function of P * Q.
	 * Since P and Q are primes, Phi = (P - 1) * (Q - 1).
	 * @param p A prime number.
	 * @param q Another prime number.
	 * @return Phi = (P - 1) * (Q - 1), Euler's totient function.
	 */
	public BigInteger calculatePhi(BigInteger p, BigInteger q) {
		return (new BigInteger(p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).toString()));
	}

	/**
	 * Generates E, where 1 < E < Phi and gcd(E, Phi) = 1
	 * @param phi Euler's totient of the source primes.
	 * @return The public key exponent E.
	 */
	public BigInteger generateE(BigInteger phi) {
		BigInteger temp;
		while(true) {
			temp = (new BigInteger(phi.bitLength(), getRandom()));
			if(temp.compareTo(phi) < 0 && temp.gcd(phi).equals(BigInteger.ONE)) {
				return temp;
			}
		}
	}

	/**
	 * Calculates D by means of solving for the modular inverse of E mod Phi.
	 * Can be solved via the extended Euclidean algorithm or the easier
	 * BigInteger.modInverse() method.
	 * D * E should be congruent to 1 mod Phi.
	 * @param e The public key exponent.
	 * @param phi Euler's totient of the source primes.
	 * @return The private key exponent D.
	 */
	public BigInteger calculateD(BigInteger e, BigInteger phi) {
		return e.modInverse(phi);
	}

	/**
	 * Converts a byte array to its hex representation in a string.
	 * @param bytes The original byte array to convert
	 * @return The converted string representing the byte array in hex.
	 */
	public static final String convertBytesToHex(byte[] bytes) {
		StringBuilder hex = new StringBuilder(2 * bytes.length);
		for (final byte b : bytes) {
			hex.append(RSAFunctions.HEXES.charAt((b & 0xF0) >> 4)).
			append(RSAFunctions.HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

	/**
	 * Note that last if X and Y are prime, then A*X + B*Y = 1 and thus B*Y is congruent to 1 mod A.
	 * This means that if you input E and Phi, lasty = D.
	 * @param a Should be the larger of the two numbers.
	 * @param b Another number.
	 * @return Solves for the gcd(A,B).
	 */
	public static final BigInteger extendedEuclidean(BigInteger a, BigInteger b) {
		BigInteger x = BigInteger.ZERO;
		BigInteger lastx = BigInteger.ONE;
		BigInteger y = BigInteger.ONE;
		BigInteger lasty = BigInteger.ZERO;
		BigInteger temp, quotient;

		while(!b.equals(BigInteger.ZERO))
		{
			temp = b;
			quotient = a.divide(b);
			b = a.remainder(b);
			a = temp;
			temp = x;
			x = lastx.subtract(quotient.multiply(x));
			lastx = temp;
			temp = y;
			y = lasty.subtract(quotient.multiply(y));
			lasty = temp;
		}
		return a;
	}

	/**
	 * Encrypts the plaintext message with the specified bit length.
	 * The ciphertext is accessible in a hex string from getCipherHex()
	 * and in a byte array from getCipherBytes().
	 * @param message The plaintext message to encrypt.
	 * @param e The public key exponent.
	 * @param n The key composite.
	 * @return The encrypted ciphertext of the original message.
	 */
	public byte[] encrypt(String message, BigInteger e, BigInteger n) {
		// Get the message and encode it into a byte array.
		byte[] messageBytes = Charset.forName(charEncoding).encode(message).array();
		// Initialize the encrypted byte array. Its length will be the message length
		// divided by the message block size, rounded up, times the cipher block size.
		// The rounding up is required because an incomplete block in the message
		// will probably become a full block in the ciphertext.
		// The division by the message size but multiplication by the cipher size is
		// necessary to account for bitCounts not divisible by 8; see the setBitCount() description.
		byte[] cipherBytes = new byte[(int) Math.ceil(
				(double) messageBytes.length / getBlockSizeMessage()) * getBlockSizeCipher()];
		// Loop through the message a block at a time.
		for(int i = 0; i < Math.ceil((double) messageBytes.length / getBlockSizeMessage()); i++) {
			// Read the next block into a buffer.
			byte[] messageBlock = new byte[getBlockSizeMessage()];
			for(int j = 0; j < getBlockSizeMessage(); j++) {
				// This if is necessary for the last block, in case it is not full.
				if(j + i * getBlockSizeMessage() < messageBytes.length) {
					messageBlock[j] = messageBytes[j + i * getBlockSizeMessage()];
				}
			}
			// The essential algorithmic equation of the encryption process.
			// First convert the byte block in a BigInteger.
			// Then take the message to the power of e mod n.
			// Finally convert this back into a byte array.
			byte[] cipherBlock = (new BigInteger(messageBlock)).modPow(e, n).toByteArray();
			// Essentially we just want to add the newly encrypted block to the main array.
			// This is sadly much more difficult that one might expect.
			// Because of the way Java handles bytes in two's complement, sometimes
			// the conversion from BigInteger to byte[] adds an extra empty element at
			// the beginning. This should be removed, but we must be careful:
			// this byte should only be removed if the block is longer than it should be.
			if(cipherBlock[0] == (byte) 0 && cipherBlock.length > getBlockSizeCipher()) {
				for(int j = 0; j < getBlockSizeCipher(); j++) {
					cipherBytes[j + i * getBlockSizeCipher()] = cipherBlock[j + 1];
				}
			}
			// Sometimes the result obtained from the modInverse() method is
			// a bit smaller than normal, and the converted byte array will be
			// too short. This must be corrected by adding one or more empty
			// elements at the beginning.
			else if(cipherBlock.length < getBlockSizeCipher()) {
				for(int j = 0; j < getBlockSizeCipher() - cipherBlock.length; j++) {
					cipherBytes[j + i * getBlockSizeCipher()] = (byte) 0;
				}
				for(int j = getBlockSizeCipher() - cipherBlock.length; j < getBlockSizeCipher(); j++) {
					cipherBytes[j + i * getBlockSizeCipher()] =
						cipherBlock[j - (getBlockSizeCipher() - cipherBlock.length)];
				}
			}
			// The normal case: we simply copy each element over. If only every
			// block was so easy to take care of!
			else {
				for(int j = 0; j < getBlockSizeCipher(); j++) {
					cipherBytes[j + i * getBlockSizeCipher()] = cipherBlock[j];
				}
			}
		}
		// The loop builds the master byte array on its own. The calling function
		// can do with the array what it wants.
		// I previously attempted to decode the bytes back to into a character array,
		// but the presence of unprintable characters in every existing character set
		// cause too many problems - and not because they can't be printed, but because
		// Java converts them all automatically to something else, screwing up the cipher.
		return cipherBytes;
	}

	/**
	 * Decrypts the encrypted ciphertext. Unless something went very wrong,
	 * it should be the same as the original message.
	 * @param cipherBytes The encrypted ciphertext.
	 * @param d The private key exponent.
	 * @param n The key composite.
	 * @return The deciphered plaintext.
	 */
	public String decrypt(byte[] cipherBytes, BigInteger d, BigInteger n) {
		// We need somewhere to store our decrypted byte blocks as we go along.
		byte[] decryptedBytes = new byte[cipherBytes.length];
		// Loop through the ciphertext a block at a time.
		for(int i = 0; i < Math.ceil((double) cipherBytes.length / getBlockSizeCipher()); i++) {
			// Read the next block into a buffer.
			byte[] cipherBlock = new byte[getBlockSizeCipher()];
			for(int j = 0; j < getBlockSizeCipher(); j++) {
				cipherBlock[j] = cipherBytes[j + i * getBlockSizeCipher()];
			}
			// The essential algorithmic equation of the decryption process,
			// but with one extra funny step.
			// First convert the byte block in a BigInteger.
			// Then check if the BigInteger is negative. This shouldn't happen,
			// but sometimes it does and it causes problems. The solution is just
			// to add 2^bitCount.
			// Next take the message to the power of d mod n.
			// Finally convert this back into a byte array.
			BigInteger tempBigInt = new BigInteger(cipherBlock);
			if(tempBigInt.compareTo(BigInteger.ZERO) < 0) {
				tempBigInt = tempBigInt.add(BigInteger.ONE.add(BigInteger.ONE).pow(getBitCount()));
			}
			byte[] decryptedBlock = tempBigInt.modPow(d, n).toByteArray();
			// Essentially we just want to add the newly decrypted block to the master byte array.
			// This is sadly more difficult that one might expect.
			// Because of the way Java handles bytes in two's complement, sometimes
			// the conversion from BigInteger to byte[] adds an extra empty element at
			// the beginning. This should be removed, but we must be careful:
			// this byte should only be removed if the block is longer than it should be.
			if(decryptedBlock[0] == (byte) 0 && decryptedBlock.length > getBlockSizeMessage()) {
				// Copy all the bytes after the first into the master array.
				System.arraycopy(decryptedBlock, 1, decryptedBytes, i * getBlockSizeMessage(),
						decryptedBlock.length - 1);
			}
			// Often the result obtained from the modInverse() method is
			// a bit small (since the first byte of UTF-16 pair for Latin characters
			// is often zero), and the converted byte array will be
			// too short. This must be corrected by adding one or more empty
			// elements at the beginning.
			else if(decryptedBlock.length < getBlockSizeMessage()) {
				// Add as many zeros as is necessary for fill the missing spaces.
				int j;
				for(j = 0; j < getBlockSizeMessage() - decryptedBlock.length; j++) {
					decryptedBytes[i * getBlockSizeMessage() + j] = (byte) 0;
				}
				// Copy the decrypted bytes into the master array right after the zeros.
				System.arraycopy(decryptedBlock, 0, decryptedBytes, i * getBlockSizeMessage() + j,
						decryptedBlock.length);
			}
			// The normal case: we simply copy the decryted byte array into the master.
			else {
				System.arraycopy(decryptedBlock, 0, decryptedBytes, i * getBlockSizeMessage(),
						decryptedBlock.length);
			}
		}
		// Wrap the bytes into a byte buffer and then decode that into a character buffer.
		ByteBuffer decryptedBuffer = ByteBuffer.wrap(decryptedBytes);
		CharBuffer textBuffer = Charset.forName(charEncoding).decode(decryptedBuffer);
		// Return the completed product as a string.
		return textBuffer.toString();
	}

	/**
	 * A fun little hack to check if a number is a power of two.
	 * Sadly, I didn't think of this idea. I adapted it from Wikipedia.
	 * @param number The number to check.
	 * @return True if the number is a power of 2.
	 */
	public boolean checkMultipleOfTwo(int number) {
		// For numbers > 1, the binary representation of a number ANDed with
		// that of the number - 1 should be all zeros. If not, it ain't a power of two.
		return number > 1 && (number & (number - 1)) == 0;
	}

	/**
	 * A fun little hack to check if a number is a power of two.
	 * This is my version. It is recursive and its use of division makes
	 * it somewhat slower than Wikipedia's version.
	 * @param number The number to check.
	 * @return True if the number is a power of 2.
	 */
	public boolean checkMultipleOfTwoSlow(int number) {
		// Keep dividing the number by two until we get either 2 (success)
		// or an odd number (fail).
		if(number == 2)
			return true;
		else if(number % 2 == 1 || number < 1)
			return false;
		else
			return checkMultipleOfTwo(number / 2);
	}

} // End RSAFunctions
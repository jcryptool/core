package org.jcryptool.crypto.modern.stream.arc4.algorithm;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jcryptool.core.operations.algorithm.modern.AbstractModernAlgorithm;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.modern.IModernDataObject;
import org.jcryptool.core.operations.dataobject.modern.symmetric.SymmetricDataObject;

/**
 * class which reads from input stream and creats cipher/plaintext.
 *
 * @author David
 *
 */
public class Arc4Algorithm extends AbstractModernAlgorithm {

    /**
     * The SymetricDataObject class.
     */
    protected SymmetricDataObject dataObject;

    /**
     * The InputStream.
     */
    protected BufferedInputStream fileInputStream;

    /**
     * The Arc4 key.
     */
    protected byte[] key;

    /**
     * the algorithm spritz or Arc4 true when arc4.
     */
    boolean algorithm;

    /**
     * operation's engine -> algorithm.
     */
    protected Arc4Engine engine;

    protected int W;

    /**
     * The Constructor.
     */
    public Arc4Algorithm() {
        this.engine = new Arc4Engine();
    }

    /**
     * Initialises the data object.
     *
     * @param input the InputStream of the input file editor
     * @param key the key
     * @param w
     */
    public void init(InputStream input, byte[] key, boolean algo, int w) {
        this.dataObject = new SymmetricDataObject();
        this.dataObject.setInputStream(input);
        this.dataObject.setSymmetricKey(key);
        this.algorithm = algo;
        this.key = key;
        this.W = w;
    }

    /**
     * Returns the data object of the current algorithm.
     *
     * @return the current algorithms data object
     */
    @Override
    public IModernDataObject getDataObject() {
        return dataObject;
    }

    /**
     * the execution of reading stream and change it to cipher
     */
    @Override
    public IDataObject execute() {

        if (dataObject.getInputStream() instanceof BufferedInputStream) {
            fileInputStream = (BufferedInputStream) dataObject.getInputStream();
        } else {
            fileInputStream = new BufferedInputStream(dataObject.getInputStream());
        }

        // read from InputStream and call decrypt/encrypt methods
        byte[] cipherInput = new byte[1024];
        int readFromStream = 0;
        byte[] cipherOutput = null;

        ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream();
        dataObject.setOutputStream(cipherOutputStream);

        try {
            readFromStream = fileInputStream.read(cipherInput);
        } catch (IOException e) {
        }

        engine.setKey(key);
        engine.setAlgo(this.algorithm);
        engine.setW(W);

        while (readFromStream != -1) {

            byte[] trimmedCipherInput = new byte[readFromStream];
            for (int i = 0; i < readFromStream; i++) {
                trimmedCipherInput[i] = cipherInput[i];
            }
            cipherOutput = engine.encryptDecrypt(trimmedCipherInput);

            try {
                cipherOutputStream.write(cipherOutput);
                dataObject.setOutputIS(new BufferedInputStream(new ByteArrayInputStream(
                        ((ByteArrayOutputStream) cipherOutputStream).toByteArray())));
                dataObject.setOutput(((ByteArrayOutputStream) cipherOutputStream).toByteArray());
            } catch (IOException e1) {
            }
            try {
                readFromStream = fileInputStream.read(cipherInput);
            } catch (IOException e) {
                readFromStream = -1;
            }
        }
        return dataObject;
    }

    /**
     * Name of Algorithm.
     */
    @Override
    public String getAlgorithmName() {
        return "ARC4/Spritz"; //$NON-NLS-1$
    }

}

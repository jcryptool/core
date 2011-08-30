// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.xor.algorithm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.classic.xor.XorPlugin;

/**
 * Class which provides the function to export/import chars by using a specified
 * charset or a default (UTF-8) one.
 *
 * @author Amro Al-Akkad
 */
public class FileTransferManager {
    /**
     * buffer size for im- and exporting files
     */
    public static final int BUFFER_SIZE = 2048;

    /**
     * auxiliary variable for checking if an input file contains a BOM
     * (byte-order mark)
     */
    public static final int BOM_VALUE = 65279;

    /**
     * stream for exporting data
     */
    private OutputStreamWriter out;

    /**
     * stream for importing data
     */
    private InputStreamReader in;

    /**
     * aux var
     */
    private int token;

    private int eof = 0;

    /**
     * Constructor
     *
     * @param inputPath of the to be imported data
     * @param outputPath of the to be exported data
     * @param dataObject contains information like the input charset, output
     *        charset, input path etc
     * @throws Exception if a transfer error occurs
     */
    public FileTransferManager(String inputPath, String outputPath) {

        try {

            in = new InputStreamReader(inputPath == null ? System.in : new FileInputStream(inputPath));

            out = new OutputStreamWriter(new FileOutputStream(outputPath));

        } catch (Exception e) {
            LogUtil
                    .logError(
                            XorPlugin.PLUGIN_ID,
                            "File Transfer Error: Check the following options: -i, -I, -o and -O for having the invalid argument", //$NON-NLS-1$
                            e, false);
        }

    }

    /**
     * Constructor
     *
     * @param inputPath of the to be imported data
     * @param outputPath of the to be exported data
     * @param dataObject contains information like the input charset, output
     *        charset, input path etc
     * @throws Exception if a transfer error occures
     */
    public FileTransferManager(String inputPath) {

        try {

            in = new InputStreamReader(inputPath == null ? System.in : new FileInputStream(inputPath));

        } catch (Exception e) {
            LogUtil
                    .logError(
                            XorPlugin.PLUGIN_ID,
                            "File Transfer Error: Check the following options: -i, -I, -o and -O for having the invalid argument", //$NON-NLS-1$
                            e, false);
        }

    }

    /**
     * writes out the specified char data
     *
     * @param chars to be exported data
     * @param numberOfChars the read in chars, smaller or equals the
     *        BUFFER_SIZE=2048 means 4 KB
     */
    public void writeOutput(char[] chars, int numberOfChars) {

        // write out string
        try {
            out.write(chars, 0, numberOfChars);
        } catch (Exception e) {
            LogUtil.logError(XorPlugin.PLUGIN_ID, "Exception while writing to the output stream", e, false); //$NON-NLS-1$
        }

    }

    /**
     * writes out the special token for Little Endian
     */
    public void writeOutSpecialToken() {
        try {
            out.write(BOM_VALUE);
        } catch (Exception e) {
            LogUtil.logError(XorPlugin.PLUGIN_ID, "Exception while writing to the output stream", e, false); //$NON-NLS-1$
        }
    }

    /**
     * Imports the data.
     *
     * @param plainBlock the imported chars by using a buffer of 4KB
     * @return the read in data
     * @throws Exception if an error occures during the process of importing
     *         chars
     */
    public char[] readInput() {

        char[] readArray = new char[FileTransferManager.BUFFER_SIZE];
        int charPos = 0;
        try {
            while (eof != -1 && charPos < FileTransferManager.BUFFER_SIZE) {
                eof = in.read(readArray, charPos, 1);
                charPos++;
            }
            if (charPos < FileTransferManager.BUFFER_SIZE - 1) {
                char[] lastArray = new char[charPos - 1];
                for (int i = 0; i < charPos - 1; i++) {
                    lastArray[i] = readArray[i];
                }
                readArray = lastArray;
            }

        } catch (IOException e) {
            LogUtil.logError(XorPlugin.PLUGIN_ID, "Exception while reading from the input stream", e, false); //$NON-NLS-1$
        }

        return readArray;
    }

    /**
     * checks if the input file contains a BOM at the beginning
     *
     * @return true if the input file contains a BOM
     */
    public boolean readInSpecialToken() {

        try {
            token = in.read();
        } catch (IOException e) {
            LogUtil.logError(XorPlugin.PLUGIN_ID, "Exception while reading from the input stream", e, false); //$NON-NLS-1$
        }

        if (token == BOM_VALUE) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * closes the output stream
     */
    public void closeOutputStream() {
        try {
            out.close();
        } catch (IOException e) {
            LogUtil.logError(XorPlugin.PLUGIN_ID, "Exception while closing the output stream", e, false); //$NON-NLS-1$
        }
    }

    /**
     * closes the input stream
     */
    public void closeInputStream() {
        try {
            in.close();
        } catch (IOException e) {
            LogUtil.logError(XorPlugin.PLUGIN_ID, "Exception while closing the input stream", e, false); //$NON-NLS-1$
        }
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getEof() {
        return eof;
    }

}

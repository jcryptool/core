//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.modern.stream.arc4.algorithm;

import java.io.InputStream;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithmHandler;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.modern.symmetric.SymmetricDataObject;
import org.jcryptool.crypto.modern.stream.arc4.ui.Arc4Wizard;

/**
 *
 * the handler for the plugin everythin starts from here.
 *
 * @author David
 *
 */
public class Arc4AlgorithmHandler extends AbstractAlgorithmHandler {

    /**
     * Constructor.
     */
    public Arc4AlgorithmHandler() {
        super();
    }

    /**
     * the wizard is created and the objects created and the key transfered.
     */
    @Override
    public Object execute(ExecutionEvent event) {
        Arc4Wizard wizard = new Arc4Wizard();
        WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);

        int result = dialog.open();

        if (result == Window.OK) {
            InputStream editorContent = getActiveEditorInputStream();
            String key = wizard.getKey();

            // if key is empty should never happen because the wizard already
            // can't be finished when key is empty :)
            if (key == "") {
                key = "0";
                if (wizard.getKeyFormatIsHexadecimal()) {

                    for (int i = 0; i < 255; i++) {
                        key += "0";
                    }
                } else {
                    for (int i = 0; i < 2048; i++) {
                        key += "0";
                    }
                }
            }

            byte[] keyArray;

            Arc4Algorithm algorithm = new Arc4Algorithm();

            if (wizard.getKeyFormatIsHexadecimal()) {
                keyArray = hexStringToByteArray(key);
            } else {
                keyArray = bintoByteArray(key);
            }

            algorithm.init(editorContent, keyArray, wizard.getAlgoIsArc4(), wizard.getFreeW());

            super.finalizeRun(algorithm);
        }
        return (null);
    }

    /**
     * the run().
     */
    @Override
    public void run(IDataObject dataobject) {
        Arc4Algorithm algorithm = new Arc4Algorithm();

        algorithm.dataObject = (SymmetricDataObject) dataobject;

        super.finalizeRun(algorithm);
    }

    /**
     *
     * converting a String of binaries (0 and 1) to a hex string this will be converted in the next.
     * methode to a byte arrray
     * <P>
     * same method is used in the wizard to perform the change from hex to binary when users swaps
     * between these two
     *
     * @param key -> the key string
     * @return -> byte array
     */
    private byte[] bintoByteArray(String key) {
        String t = "";

        while (key.length() % 4 != 0) {
            key += "0";
        }

        int[] a = new int[4];
        for (int i = 0; i < key.length(); i = i + 4) {
            if (key.charAt(i) == 48) {
                a[0] = 0;
            } else {
                a[0] = 1;
            }
            if (key.charAt(i + 1) == 48) {
                a[1] = 0;
            } else {
                a[1] = 1;
            }
            if (key.charAt(i + 2) == 48) {
                a[2] = 0;
            } else {
                a[2] = 1;
            }
            if (key.charAt(i + 3) == 48) {
                a[3] = 0;
            } else {
                a[3] = 1;
            }

            int calc = 0;

            if (a[0] == 1) {
                calc += 8;
            }

            if (a[1] == 1) {
                calc += 4;
            }

            if (a[2] == 1) {
                calc += 2;
            }

            if (a[3] == 1) {
                calc += 1;
            }

            switch (calc) {
            case 0:
                t += "0";
                break;
            case 1:
                t += "1";
                break;
            case 2:
                t += "2";
                break;
            case 3:
                t += "3";
                break;
            case 4:
                t += "4";
                break;
            case 5:
                t += "5";
                break;
            case 6:
                t += "6";
                break;
            case 7:
                t += "7";
                break;
            case 8:
                t += "8";
                break;
            case 9:
                t += "9";
                break;
            case 10:
                t += "a";
                break;
            case 11:
                t += "b";
                break;
            case 12:
                t += "c";
                break;
            case 13:
                t += "d";
                break;
            case 14:
                t += "e";
                break;
            case 15:
                t += "f";
                break;
            }

            calc = 0;

        }

        // call of the hex String to Byte array.
        byte[] out = hexStringToByteArray(t);
        return out;
    }

    /**
     *
     * creats a Byte Array out of a hex-string. -> commented code is if you don't have an input
     * validation in the wizard.
     *
     * @param s the string
     * @return data
     */
    private static byte[] hexStringToByteArray(String s) {
        byte[] data;
        // int len = Math.min(512, s.length());
        int len = s.length();

        // if (len % 2 == 1) {
        // s += 0;
        // data = new byte[(len / 2) + 1];
        // } else {
        data = new byte[len / 2];
        // }
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}

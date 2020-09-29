// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.modern.stream.dragon.algorithm;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithmHandler;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.modern.symmetric.SymmetricDataObject;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.crypto.modern.stream.dragon.DragonPlugin;
import org.jcryptool.crypto.modern.stream.dragon.ui.DragonWizard;
import org.jcryptool.crypto.modern.stream.dragon.ui.DragonWizardPage.DisplayOption;

/**
 * The DragonAlgorithmHandler class is a specific implementation of AbstractAlgorithmHandler
 *
 * @see org.jcryptool.core.operations.algorithm.AbstractAlgorithmHandler
 *
 * @author Tahir Kacak
 * @author Holger Friedrich (support for Commands, additional class based on DragonAlgorithmAction)
 * @version 0.1
 */
public class DragonAlgorithmHandler extends AbstractAlgorithmHandler{

    /**
     * Constructor
     */
    public DragonAlgorithmHandler() {
        super();
    }

    /**
     * This method performs the action
     */
    @Override
    public Object execute(ExecutionEvent event) {
        DragonWizard wizard = new DragonWizard();
        WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
        dialog.setHelpAvailable(true);

        int result = dialog.open();

        if (result == Window.OK) {
            InputStream editorContent = getActiveEditorInputStream();
            String key = wizard.getKey();
            String iv = wizard.getIV();
            byte[] keyArray;
            byte[] ivArray;

            if (wizard.getIs128Bit()) {
                if (wizard.getKeyFormatIsHexadecimal())
                    keyArray = hexTo128BitByteArray(key);
                else
                    keyArray = bigIntegerTo128BitByteArray(new BigInteger(key, 2));

                if (wizard.getIVFormatIsHexadecimal())
                    ivArray = hexTo128BitByteArray(iv);
                else
                    ivArray = bigIntegerTo128BitByteArray(new BigInteger(iv, 2));
            } else {
                if (wizard.getKeyFormatIsHexadecimal())
                    keyArray = hexTo256BitByteArray(key);
                else
                    keyArray = bigIntegerTo256BitByteArray(new BigInteger(key, 2));

                if (wizard.getIVFormatIsHexadecimal())
                    ivArray = hexTo256BitByteArray(iv);
                else
                    ivArray = bigIntegerTo256BitByteArray(new BigInteger(iv, 2));
            }

            if (wizard.getDisplayOption() == DisplayOption.KEYSTREAM_ONLY) {
                outputKeyStream(keyArray, ivArray, wizard);
                return(null);
            }

            DragonAlgorithm algorithm = new DragonAlgorithm();

            algorithm.init(editorContent, keyArray, ivArray);

            super.finalizeRun(algorithm);

            if (wizard.getDisplayOption() == DisplayOption.OUTPUT_AND_KEYSTREAM) {
                IEditorInput keyStream =
                        AbstractEditorService.createOutputFile(Messages.DragonAlgorithmAction_0, ".bin",
                                ((SymmetricDataObject) algorithm.getDataObject()).getInputStream());
                try {
                    getActiveWorkbenchWindow().getActivePage().openEditor(keyStream, IOperationsConstants.ID_HEX_EDITOR);
                } catch (PartInitException e) {
                    MessageDialog.openError(getActiveWorkbenchWindow().getShell(), Messages.DragonAlgorithmAction_1,
                            NLS.bind(Messages.DragonAlgorithmAction_2, IOperationsConstants.ID_HEX_EDITOR));
                }
            }
        }
        return(null);
    }

    @Override
    public void run(IDataObject dataobject) {
        DragonAlgorithm algorithm = new DragonAlgorithm();

        algorithm.dataObject = (SymmetricDataObject) dataobject;

        super.finalizeRun(algorithm);
    }

    /**
     * Opens an editor to display keystream of given length.
     *
     * @param keyArray the keystream generator's key as a byte array
     * @param ivArray the keystream generator's IV as a byte array
     * @param wizard used to retrieve the number of bytes of keystream to generate
     */
    private void outputKeyStream(byte[] keyArray, byte[] ivArray, DragonWizard wizard) {
        int keyStreamByteCount = Integer.valueOf(wizard.getKeystreamLengthValue());

        // check if key and IV are same length
        if (keyArray.length != ivArray.length)
            throw new Error("key and IV length must be equal"); //$NON-NLS-1$

        int[] dragonKey = DragonAlgorithm.byteArrayToIntArray(keyArray);
        int[] dragonIV = DragonAlgorithm.byteArrayToIntArray(ivArray);

        DragonKeyStreamGenerator keyStreamGenerator = new DragonKeyStreamGenerator();

        if (dragonKey.length == 4)
            keyStreamGenerator.dInit128(dragonKey, dragonIV);
        else if (dragonKey.length == 8)
            keyStreamGenerator.dInit256(dragonKey, dragonIV);
        else
            throw new Error("key and IV must be 128 bit or 256 bit"); //$NON-NLS-1$

        byte[] keyStream = generateKeyStream(keyStreamByteCount, keyStreamGenerator);
        ByteArrayOutputStream keyStreamOutputStream = new ByteArrayOutputStream();

        try {
            keyStreamOutputStream.write(keyStream);
        } catch (IOException e) {
            LogUtil.logError(DragonPlugin.PLUGIN_ID, e);
        }

        IEditorInput keyStreamEditor =
                AbstractEditorService.createOutputFile(Messages.DragonAlgorithmAction_0, ".bin",
                        new BufferedInputStream(new ByteArrayInputStream(keyStreamOutputStream.toByteArray())));

        try {
            getActiveWorkbenchWindow().getActivePage().openEditor(keyStreamEditor, IOperationsConstants.ID_HEX_EDITOR);
        } catch (PartInitException e) {
            MessageDialog.openError(getActiveWorkbenchWindow().getShell(), Messages.DragonAlgorithmAction_1,
                    NLS.bind(Messages.DragonAlgorithmAction_2, IOperationsConstants.ID_HEX_EDITOR));
        }
    }

    /**
     * Generates an amount of keystream of given length.
     *
     * @param keyStreamByteCount the number of bytes of keystream to generate
     * @param keyStreamGenerator the generator with which the keystream is produced
     * @return the keystream as a byte array
     */
    private byte[] generateKeyStream(int keyStreamByteCount, DragonKeyStreamGenerator keyStreamGenerator) {
        int[] intKeyStream;

        if (keyStreamByteCount % 4 == 0)
            intKeyStream = new int[keyStreamByteCount / 4];
        else
            intKeyStream = new int[(keyStreamByteCount / 4) + 1];

        if (intKeyStream.length % 2 == 0) {
            for (int i = 0; i < intKeyStream.length / 2; i++) {
                keyStreamGenerator.dGen();
                intKeyStream[i * 2] = keyStreamGenerator.getA();
                intKeyStream[(i * 2) + 1] = keyStreamGenerator.getE();
            }
        } else {
            int i;
            for (i = 0; i < (intKeyStream.length / 2); i++) {
                keyStreamGenerator.dGen();
                intKeyStream[i * 2] = keyStreamGenerator.getA();
                intKeyStream[(i * 2) + 1] = keyStreamGenerator.getE();
            }
            intKeyStream[i * 2] = keyStreamGenerator.getA();
        }

        return DragonEngine.intArrayToByteArray(intKeyStream);
    }

    /**
     * Converts a String representation of a hexadecimal number to an array of bytes with 16 elements. The most
     * significant bits are padded with 0's to fill 128 bits.
     *
     * @param hexadecimalNumberString the hexadecimal String to convert
     * @return a byte array of length 16, which represents the bytes of the String
     */
    private byte[] hexTo128BitByteArray(String hexadecimalNumberString) {
        byte[] byteOutputArray = new byte[16];
        ArrayList<Byte> byteArrayList = new ArrayList<Byte>();

        if (hexadecimalNumberString.length() % 2 == 0) {
            for (int i = 0; i < (hexadecimalNumberString.length() / 2); i++)
                byteArrayList.add(i,
                        Integer.valueOf(hexadecimalNumberString.substring(i * 2, (i * 2) + 2), 16).byteValue());

            for (int i = 0; i < (16 - (hexadecimalNumberString.length() / 2)); i++)
                byteArrayList.add(0, Byte.valueOf((byte) 0));
        } else {
            byteArrayList.add(0, Integer.valueOf("0" + hexadecimalNumberString.substring(0, 1), 16).byteValue()); //$NON-NLS-1$

            for (int i = 0; i < (hexadecimalNumberString.length() / 2); i++)
                byteArrayList.add(i + 1,
                        Integer.valueOf(hexadecimalNumberString.substring((i * 2) + 1, (i * 2) + 3), 16).byteValue());

            for (int i = 0; i < (16 - ((hexadecimalNumberString.length() / 2) + 1)); i++)
                byteArrayList.add(0, Byte.valueOf((byte) 0));
        }

        for (int i = 0; i < byteOutputArray.length; i++)
            byteOutputArray[i] = byteArrayList.get(i);

        return byteOutputArray;
    }

    /**
     * Converts a String representation of a hexadecimal number to an array of bytes with 32 elements. The most
     * significant bits are padded with 0's to fill 256 bits.
     *
     * @param hexadecimalNumberString the hexadecimal String to convert
     * @return a byte array of length 32, which represents the bytes of the String
     */
    private byte[] hexTo256BitByteArray(String hexadecimalNumberString) {
        byte[] byteOutputArray = new byte[32];
        ArrayList<Byte> byteArrayList = new ArrayList<Byte>();

        if (hexadecimalNumberString.length() % 2 == 0) {
            for (int i = 0; i < (hexadecimalNumberString.length() / 2); i++)
                byteArrayList.add(i,
                        Integer.valueOf(hexadecimalNumberString.substring(i * 2, (i * 2) + 2), 16).byteValue());

            for (int i = 0; i < (32 - (hexadecimalNumberString.length() / 2)); i++)
                byteArrayList.add(0, Byte.valueOf((byte) 0));
        } else {
            byteArrayList.add(0, Integer.valueOf("0" + hexadecimalNumberString.substring(0, 1), 16).byteValue()); //$NON-NLS-1$

            for (int i = 0; i < (hexadecimalNumberString.length() / 2); i++)
                byteArrayList.add(i + 1,
                        Integer.valueOf(hexadecimalNumberString.substring((i * 2) + 1, (i * 2) + 3), 16).byteValue());

            for (int i = 0; i < (32 - ((hexadecimalNumberString.length() / 2) + 1)); i++)
                byteArrayList.add(0, Byte.valueOf((byte) 0));
        }

        for (int i = 0; i < byteOutputArray.length; i++)
            byteOutputArray[i] = byteArrayList.get(i);

        return byteOutputArray;
    }

    /**
     * Converts a BigInteger to an array of bytes with 16 elements. The most significant bits are padded with 0's if the
     * byte array is too short.
     *
     * @param bigInt the BigInteger to convert
     * @return a byte array of length 16, which represents the bytes of the BigInteger
     */
    private byte[] bigIntegerTo128BitByteArray(BigInteger bigInt) {
        byte[] byteInputArray = bigInt.toByteArray();

        if (byteInputArray.length == 16)
            return byteInputArray;

        byte[] byteOutputArray = new byte[16];

        ArrayList<Byte> byteArrayList = new ArrayList<Byte>();
        for (int i = 0; i < byteInputArray.length; i++)
            byteArrayList.add(i, Byte.valueOf(byteInputArray[i]));

        for (int i = 0; i < (16 - byteInputArray.length); i++)
            byteArrayList.add(0, Byte.valueOf((byte) 0));

        for (int i = 0; i < byteOutputArray.length; i++)
            byteOutputArray[i] = byteArrayList.get(i);

        return byteOutputArray;
    }

    /**
     * Converts a BigInteger to an array of bytes with 32 elements. The most significant bits are padded with 0's if the
     * byte array is too short.
     *
     * @param bigInt the BigInteger to convert
     * @return a byte array of length 32, which represents the bytes of the BigInteger
     */
    private byte[] bigIntegerTo256BitByteArray(BigInteger bigInt) {
        byte[] byteInputArray = bigInt.toByteArray();

        if (byteInputArray.length == 32)
            return byteInputArray;

        byte[] byteOutputArray = new byte[32];

        ArrayList<Byte> byteArrayList = new ArrayList<Byte>();
        for (int i = 0; i < byteInputArray.length; i++)
            byteArrayList.add(i, Byte.valueOf(byteInputArray[i]));

        for (int i = 0; i < (32 - byteInputArray.length); i++)
            byteArrayList.add(0, Byte.valueOf((byte) 0));

        for (int i = 0; i < byteOutputArray.length; i++)
            byteOutputArray[i] = byteArrayList.get(i);

        return byteOutputArray;
    }
}

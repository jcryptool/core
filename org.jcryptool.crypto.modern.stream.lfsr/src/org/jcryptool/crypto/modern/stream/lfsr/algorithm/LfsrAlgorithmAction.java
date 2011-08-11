//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.modern.stream.lfsr.algorithm;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithmAction;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.modern.symmetric.LfsrDataObject;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.crypto.modern.stream.lfsr.LfsrPlugin;
import org.jcryptool.crypto.modern.stream.lfsr.ui.LfsrWizard;
import org.jcryptool.crypto.modern.stream.lfsr.ui.LfsrWizardPage.DisplayOption;

/**
 * The LfsrAlgorithmAction class is a specific implementation of AbstractAlgorithmAction
 *
 * @see org.jcryptool.core.operations.algorithm.AbstractAlgorithmAction
 *
 * @author Tahir Kacak, Daniel Dwyer
 * @version 0.1
 */
public class LfsrAlgorithmAction extends AbstractAlgorithmAction {

    /**
     * Constructor
     */
    public LfsrAlgorithmAction() {
        super();
    }

    /**
     * This method performs the action
     */
    @Override
    public void run() {
        LfsrWizard wizard = new LfsrWizard();
        WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setHelpAvailable(true);

        int result = dialog.open();

        if (result == Window.OK) {
            InputStream editorContent = getActiveEditorInputStream();
            boolean[] seed = wizard.getSeed();
            boolean[] tapSettings = wizard.getTapSettings();

            if (wizard.getDisplayOption() == DisplayOption.KEYSTREAM_ONLY) {
                outputKeyStream(seed, tapSettings, wizard);
                return;
            }

            LfsrAlgorithm algorithm = new LfsrAlgorithm();

            algorithm.init(editorContent, seed, tapSettings);

            super.finalizeRun(algorithm);

            if (wizard.getDisplayOption() == DisplayOption.OUTPUT_AND_KEYSTREAM) {
                IEditorInput keyStream = AbstractEditorService.createOutputFile(Messages.LfsrAlgorithmAction_2, ".bin",
                        ((LfsrDataObject) algorithm.getDataObject()).getInputStream());
                try {
                    getActiveWorkbenchWindow().getActivePage().openEditor(keyStream,
                            IOperationsConstants.ID_HEX_EDITOR);
                } catch (PartInitException e) {
                    MessageDialog.openError(getActiveWorkbenchWindow().getShell(),
                        Messages.LfsrAlgorithmAction_0,
                        NLS.bind(Messages.LfsrAlgorithmAction_1, IOperationsConstants.ID_HEX_EDITOR));
                }
            }
        }
    }

    @Override
    public void run(IDataObject dataobject) {
        LfsrAlgorithm algorithm = new LfsrAlgorithm();

        algorithm.dataObject = (LfsrDataObject) dataobject;

        super.finalizeRun(algorithm);
    }

    /**
     * Opens an editor to display keystream of given length.
     *
     * @param seed the keystream generator's seed
     * @param tapSettings the keystream generator's tap settings
     * @param wizard used to retrieve the number of bytes of keystream to generate
     */
    private void outputKeyStream(boolean[] seed, boolean[] tapSettings, LfsrWizard wizard) {
        int keyStreamByteCount = new Integer(wizard.getKeystreamLengthValue()).intValue();

        LfsrKeyStreamGenerator keyStreamGenerator = new LfsrKeyStreamGenerator(seed, tapSettings);

        byte[] keyStream = generateKeyStream(keyStreamByteCount, keyStreamGenerator);
        ByteArrayOutputStream keyStreamOutputStream = new ByteArrayOutputStream();

        try {
            keyStreamOutputStream.write(keyStream);
        } catch (IOException e) {
            LogUtil.logError(LfsrPlugin.PLUGIN_ID, e);
        }

        IEditorInput keyStreamEditor = AbstractEditorService.createOutputFile(Messages.LfsrAlgorithmAction_2, ".bin",
                new BufferedInputStream(new ByteArrayInputStream(keyStreamOutputStream.toByteArray())));

        try {
            getActiveWorkbenchWindow().getActivePage().openEditor(keyStreamEditor,
                    IOperationsConstants.ID_HEX_EDITOR);
        } catch (PartInitException e) {
            MessageDialog.openError(getActiveWorkbenchWindow().getShell(),
                Messages.LfsrAlgorithmAction_0,
                NLS.bind(Messages.LfsrAlgorithmAction_1, IOperationsConstants.ID_HEX_EDITOR));
        }
    }

    /**
     * Generates an amount of keystream of given length.
     *
     * @param keyStreamByteCount the number of bytes of keystream to generate
     * @param keyStreamGenerator the generator with which the keystream is produced
     * @return the keystream as a byte array
     */
    private byte[] generateKeyStream(int keyStreamByteCount,
            LfsrKeyStreamGenerator keyStreamGenerator) {
        boolean[] keyStream = new boolean[keyStreamByteCount * 8];

        for (int i = 0; i < keyStream.length; i++) {
            keyStream[i] = keyStreamGenerator.doRound();
        }
        return LfsrEngine.booleanArrayToByteArray(keyStream);
    }
}

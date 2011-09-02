// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.modern.sha3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.crypto.modern.sha3.echo.ECHOAction;
import org.jcryptool.crypto.modern.sha3.jh.JHAction;
import org.jcryptool.crypto.modern.sha3.skein.algorithm.SkeinAction;
import org.jcryptool.crypto.modern.sha3.ui.Messages;
import org.jcryptool.crypto.modern.sha3.ui.SHA3Wizard;

/**
 * Action class for sha3 plugin, managing the wizard and all algorithms
 *
 * @author Michael Starzer
 *
 */
public class SHA3Action extends AbstractAlgorithmAction {
    private static final int UNSIGNED_BYTE_MASK = 0xFF;

    public SHA3Action() {
        super();
    }

    /**
     * The run() method for the sha3 plugin
     */
    public void run() {
        /* Creates a new wizard and starts it */
        SHA3Wizard wizard = new SHA3Wizard();
        WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
        dialog.setHelpAvailable(true);

        int result = dialog.open();
        /* If the Wizard is finished */
        if (result == Window.OK) {
            /* Get all the important information of the wizard */
            int Bitlength = wizard.getBitlength();
            InputStream editorContent = getActiveEditorInputStream();
            IEditorInput filecontent = null;
            String SHA3Type = wizard.getSha3Type();
            String Mode = wizard.getMode();
            byte[] hashValue = null;
            boolean hexEditor = false;
            String Salt = wizard.getSalt();
            /* Check if the hash shall be created or verified */
            if (Mode.compareTo("CreateHash") == 0) {
                if (SHA3Type.compareTo("ECHO") == 0) {
                    ECHOAction runECHO = new ECHOAction();
                    if (Salt.compareTo("") == 0)
                        hashValue = runECHO.run(Bitlength, convertStreamToString(editorContent));
                    else
                        hashValue = runECHO.run(Bitlength, convertStreamToString(editorContent), Salt);
                } else if (SHA3Type.compareTo("JH") == 0) {
                    JHAction runJH = new JHAction();
                    hashValue = runJH.run(Bitlength, convertStreamToString(editorContent));
                } else if (SHA3Type.compareTo("Skein") == 0) {
                    SkeinAction runSkein = new SkeinAction();
                    hashValue = runSkein.run(Bitlength, convertStreamToString(editorContent), Bitlength);
                }
            } else if (Mode.compareTo("VerifyHash") == 0) {
                String Hash = wizard.getHashValue();
                String Buffer = "";
                if (SHA3Type.compareTo("Skein") == 0) {
                    SkeinAction runSkein = new SkeinAction();
                    Buffer = toHex(runSkein.run(Bitlength, convertStreamToString(editorContent), Bitlength));
                } else if (SHA3Type.compareTo("JH") == 0) {
                    JHAction runJH = new JHAction();
                    Buffer = toHex(runJH.run(Bitlength, convertStreamToString(editorContent)));
                } else if (SHA3Type.compareTo("ECHO") == 0) {
                    ECHOAction runECHO = new ECHOAction();
                    if (Salt.compareTo("") == 0)
                        Buffer = toHex(runECHO.run(Bitlength, convertStreamToString(editorContent)));
                    else
                        Buffer = toHex(runECHO.run(Bitlength, convertStreamToString(editorContent), Salt));
                }
                /* Checking if the hash was correct */
                if (Hash.compareTo(Buffer) == 0)
                    filecontent = AbstractEditorService.createOutputFile(Messages.WizardMessage11 + "\n\n"
                            + "Input Hash: \n" + Hash + "\n" + "OutputHash: \n" + Buffer);
                else
                    filecontent = AbstractEditorService.createOutputFile(Messages.WizardMessage12 + "\n\n"
                            + "Input Hash: \n" + Hash + "\n" + "OutputHash: \n" + Buffer);

            }
            /* Changes the format of the hashvalue against the chosen mode */
            if (Mode.compareTo("CreateHash") == 0) {
                switch (wizard.getOutputMode()) {
                    case 0:
                        filecontent = AbstractEditorService.createOutputFile(hashValue);
                        hexEditor = true;
                        break;
                    case 1:
                        filecontent = AbstractEditorService.createOutputFile(toFormattedHex(hashValue));

                        break;
                    case 2:
                        filecontent = AbstractEditorService.createOutputFile(toHex(hashValue));
                        break;
                }
            }
            if (hexEditor) {
                try {
                    getActiveWorkbenchWindow().getActivePage().openEditor(filecontent,
                            IOperationsConstants.ID_HEX_EDITOR);
                } catch (PartInitException e) {
                    MessageDialog.openError(getActiveWorkbenchWindow().getShell(), "Error!", NLS.bind("Error!",
                            IOperationsConstants.ID_HEX_EDITOR));
                }
            } else {
                try {
                    getActiveWorkbenchWindow().getActivePage().openEditor(filecontent,
                            IOperationsConstants.ID_TEXT_EDITOR);
                } catch (PartInitException e) {
                    MessageDialog.openError(getActiveWorkbenchWindow().getShell(), "Error!", NLS.bind("Error!",
                            IOperationsConstants.ID_TEXT_EDITOR));
                }
            }
        }
    }

    /**
     * Creates a string out of a stream
     *
     * @param stream InputSream
     * @return
     */
    public String convertStreamToString(InputStream stream) {
    	BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, IConstants.UTF8_ENCODING));
		} catch (UnsupportedEncodingException e1) {
			LogUtil.logError(SHA3Plugin.PLUGIN_ID, e1);
		}
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LogUtil.logError(e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                LogUtil.logError(e);
            }
        }

        return sb.toString();
    }

    /**
     * Creates a formatted hex
     *
     * @param bytes data, which shall be formatted
     * @return
     */
    public String toFormattedHex(final byte[] bytes) {
        final StringBuilder tabsSB = new StringBuilder(0);
        String tabsStr = tabsSB.toString();

        final StringBuilder sb = new StringBuilder(bytes.length * 4);
        for (int i = 0; i < bytes.length; i++) {
            if (i % 16 == 0) {
                sb.append(tabsStr);
            }
            sb.append(String.format("%02X", bytes[i] & UNSIGNED_BYTE_MASK));
            if (i != 0 && (i + 1) % 16 == 0) {
                sb.append(String.format("%n"));
            } else {
                sb.append(" ");
            }

        }
        return sb.toString();
    }

    /**
     * Changes the byte array to a hex string
     *
     * @param bytes
     * @return
     */
    public static String toHex(final byte[] bytes) {
        final StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%02X", bytes[i] & UNSIGNED_BYTE_MASK));
        }
        return sb.toString();
    }

    public void run(IDataObject dataobject) {
    }
}
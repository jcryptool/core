/*******************************************************************************
 * Copyright (c) 2011 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.security.core.sign.CreateSignature;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.sign.NewSignatureWizard;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithmAction;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.util.constants.IConstants;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

/**
 * <p>
 * Command used to start the <b>XML Signature</b> wizard for a new XML Signature for the selected
 * XML document. The signature process differs depending on whether editor content or a file via a
 * view should be signed.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class NewSignatureCommand extends AbstractAlgorithmAction {
    /** Selected text in the editor. */
    private ITextSelection textSelection = null;
    /** The data to sign. */
    private InputStream data = null;

    /**
     * Takes the resource (selected file or editor content) and starts the XML Signature Wizard. The
     * returned signed XML document is not pretty printed because this would change the hash value
     * of the signed content and verification would fail.
     */
    private void createSignature() {
        try {
            NewSignatureWizard wizard = new NewSignatureWizard();
            data = getActiveEditorInputStream();

            Object activeEditor =
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getAdapter(
                            ITextEditor.class);

            if (activeEditor != null) {
                textSelection = (ITextSelection) ((ITextEditor) activeEditor).getSelectionProvider().getSelection();
            }

            wizard.init(data, textSelection);

            signData(wizard);

            if (wizard.getModel().getLaunchEncryptionWizard()) {
                callEncryptionWizard();
            }
        } catch (SAXParseException ex) {
            LogUtil.logError(XSTUIPlugin.getId(), Messages.NewSignatureCommand_2, ex, true);
        } catch (FileNotFoundException ex) {
            LogUtil.logError(XSTUIPlugin.getId(), Messages.NewSignatureCommand_3, ex, true);
        } catch (IOException ex) {
            LogUtil.logError(XSTUIPlugin.getId(), Messages.NewSignatureCommand_4, ex, true);
        } catch (Exception ex) {
            LogUtil.logError(XSTUIPlugin.getId(), Messages.NewSignatureCommand_5, ex, true);
        }
    }

    /**
     * Signing an XML resource inside an opened editor (with or without a text selection) or via a
     * view.
     *
     * @param wizard The Signature Wizard
     * @throws Exception to indicate any exceptional condition
     */
    private void signData(final NewSignatureWizard wizard) throws Exception {
        final CreateSignature signature = new CreateSignature();

        WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
        dialog.create();
        dialog.open();

        if (dialog.getReturnCode() == Dialog.OK && wizard.getModel() != null) {
            Job job = new Job(Messages.NewSignatureCommand_0) {
                public IStatus run(final IProgressMonitor monitor) {
                    try {
                        monitor.beginTask(Messages.NewSignatureCommand_7, 5);

                        Document doc = null;

                        if (textSelection != null) {
                            doc = signature.sign(wizard.getModel(), textSelection.getText(), monitor);
                        } else {
                            doc = signature.sign(wizard.getModel(), null, monitor);
                        }

                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }

                        if (doc != null) {
                            IEditorInput output =
                                    AbstractEditorService.createOutputFile("signed",
                                            IConstants.XML_FILE_TYPE_EXTENSION,
                                            Utils.docToInputStram(doc));

                            performOpenEditor(output, IOperationsConstants.ID_TEXT_EDITOR);
                        }
                    } catch (final Exception ex) {
                        getActiveWorkbenchWindow().getShell().getDisplay().asyncExec(
                                new Runnable() {
                                    public void run() {
                                        LogUtil.logError(XSTUIPlugin.getId(),
                                                Messages.NewSignatureCommand_8, ex, true);
                                    }
                                });
                    } finally {
                        monitor.done();
                    }

                    return Status.OK_STATUS;
                }
            };
            job.setUser(true);
            job.schedule();
        }

        wizard.dispose();
    }

    /**
     * Calls the <i>XML Encryption Wizard</i> after successfully signing the selected resource if
     * the user selected the checkbox in the <i>XML Signature Wizard</i>.
     *
     * @throws ExecutionException
     */
    private void callEncryptionWizard() throws ExecutionException {
        NewEncryptionCommand encrypt = new NewEncryptionCommand();
        encrypt.encryptAfterSignature(data);
        encrypt.run();
    }

    /**
     * Signs the given XML document after successfully encrypting it.
     *
     * @param encryptedData The encrypted data, now used to sign
     */
    public void signAfterEncryption(final InputStream encryptedData) {
        this.data = encryptedData;

        createSignature();
    }

    @Override
    public void run() {
        run(null);
    }

    @Override
    public void run(IDataObject dataobject) {
        createSignature();
    }
}

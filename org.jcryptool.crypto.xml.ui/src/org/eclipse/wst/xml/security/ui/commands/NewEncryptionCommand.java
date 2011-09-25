/*******************************************************************************
 * Copyright (c) 2011 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.commands;

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
import org.eclipse.wst.xml.security.core.encrypt.CreateEncryption;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.encrypt.NewEncryptionWizard;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithmAction;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.util.constants.IConstants;
import org.w3c.dom.Document;

/**
 * <p>
 * Command used to start the <b>XML Encryption</b> wizard for a new XML Encryption for the selected
 * XML document. The encryption process differs depending on whether editor content or a file via a
 * view should be encrypted.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class NewEncryptionCommand extends AbstractAlgorithmAction {
    /** Selected text in the editor. */
    private ITextSelection textSelection = null;
    /** The data to encrypt. */
    private InputStream data = null;

    /**
     * Takes the resource (selected file or editor content) and starts the XML Encryption Wizard.
     * The returned encrypted XML document is pretty printed before the editor or the file is
     * updated.
     */
    private void createEncryption() {
        try {
            NewEncryptionWizard wizard = new NewEncryptionWizard();
            data = getActiveEditorInputStream();

            Object activeEditor =
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getAdapter(
                            ITextEditor.class);

            if (activeEditor != null) {
                textSelection =
                        (ITextSelection) ((ITextEditor) activeEditor).getSelectionProvider().getSelection();
            }

            wizard.init(data, textSelection);

            encryptData(wizard);

            if (wizard.getModel().getLaunchSignatureWizard()) {
                callSignatureWizard();
            }
        } catch (Exception ex) {
            LogUtil.logError(XSTUIPlugin.getId(), Messages.NewEncryptionCommand_1, ex, true);
        }
    }

    /**
     * Encrypting an XML resource inside an opened editor (with or without a text selection) or via
     * a view.
     *
     * @param wizard The Encryption Wizard
     * @throws Exception to indicate any exceptional condition
     */
    private void encryptData(final NewEncryptionWizard wizard) throws Exception {
        final CreateEncryption encryption = new CreateEncryption();

        WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
        dialog.create();
        dialog.open();

        if (dialog.getReturnCode() == Dialog.OK && wizard.getModel() != null) {
            Job job = new Job(Messages.NewEncryptionCommand_0) {
                public IStatus run(final IProgressMonitor monitor) {
                    try {
                        monitor.beginTask(Messages.NewEncryptionCommand_2, 3);

                        Document doc = null;

                        if (textSelection != null) {
                            doc = encryption.encrypt(wizard.getModel(), textSelection.getText(), monitor);
                        } else {
                            doc = encryption.encrypt(wizard.getModel(), null, monitor);
                        }

                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }

                        if (doc != null) {
                            IEditorInput output =
                                    AbstractEditorService.createOutputFile("encrypted",
                                            IConstants.XML_FILE_TYPE_EXTENSION,
                                            Utils.docToInputStram(doc));

                            performOpenEditor(output, IOperationsConstants.ID_TEXT_EDITOR);
                        }
                    } catch (final Exception ex) {
                        getActiveWorkbenchWindow().getShell().getDisplay().asyncExec(
                                new Runnable() {
                                    public void run() {
                                        LogUtil.logError(XSTUIPlugin.getId(),
                                                Messages.NewEncryptionCommand_1, ex, true);
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
     * Calls the <i>XML Signature Wizard</i> after successfully encrypting the selected resource if
     * the user selected the checkbox in the <i>XML Encryption Wizard</i>.
     *
     * @throws ExecutionException
     */
    private void callSignatureWizard() throws ExecutionException {
        NewSignatureCommand sign = new NewSignatureCommand();
        sign.signAfterEncryption(data);
        sign.run();
    }

    /**
     * Encrypts the given XML document after successfully signing it.
     *
     * @param signedData The signed data, now used to encrypt
     */
    public void encryptAfterSignature(final InputStream signedData) {
        this.data = signedData;

        createEncryption();
    }

    @Override
    public void run() {
        run(null);
    }

    @Override
    public void run(IDataObject dataobject) {
        createEncryption();
    }
}

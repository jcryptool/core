/*******************************************************************************
 * Copyright (c) 2011 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.commands;

import java.io.InputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.wst.xml.security.core.decrypt.CreateDecryption;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.decrypt.NewDecryptionWizard;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithmAction;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.util.constants.IConstants;
import org.w3c.dom.Document;

/**
 * <p>
 * Command used to start the <b>XML Decryption Wizard</b> for a new decryption in the selected XML document. The
 * decryption process differs depending on whether editor content or a file via a view should be decrypted.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class NewDecryptionCommand extends AbstractAlgorithmAction {
    /** The file to decrypt. */
    private InputStream data = null;

    private void createDecryption() {
        try {
            NewDecryptionWizard wizard = new NewDecryptionWizard();
            data = getActiveEditorInputStream();

            wizard.init(data);

            decryptData(wizard);
        } catch (Exception ex) {
            LogUtil.logError(XSTUIPlugin.getId(), Messages.NewDecryptionCommand_1, ex, true);
        }
    }

    /**
     * Called when decrypting an XML resource inside an opened editor or via a view. The output XML can not be pretty
     * printed since this would break an existing XML signature in the document.
     *
     * @param wizard The Decryption Wizard
     * @throws Exception to indicate any exceptional condition
     */
    private void decryptData(final NewDecryptionWizard wizard) throws Exception {
        final CreateDecryption decryption = new CreateDecryption();

        WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
        dialog.create();
        dialog.open();

        if (dialog.getReturnCode() == Dialog.OK && wizard.getModel() != null) {
            Job job = new Job(Messages.NewDecryptionCommand_0) {
                public IStatus run(final IProgressMonitor monitor) {
                    try {
                        monitor.beginTask(Messages.NewDecryptionCommand_2, 6);

                        Document doc = decryption.decrypt(wizard.getModel(), monitor);

                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }

                        if (doc != null) {
                            IEditorInput output = AbstractEditorService.createOutputFile("decrypted", IConstants.XML_FILE_TYPE_EXTENSION,
                                    Utils.docToInputStram(doc));

                            performOpenEditor(output, IOperationsConstants.ID_TEXT_EDITOR);
                        }
                    } catch (final Exception ex) {
                        getActiveWorkbenchWindow().getShell().getDisplay().asyncExec(new Runnable() {
                            public void run() {
                                LogUtil.logError(XSTUIPlugin.getId(), Messages.NewDecryptionCommand_1, ex, true);
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

    @Override
    public void run() {
        run(null);
    }

    @Override
    public void run(IDataObject dataobject) {
        createDecryption();
    }
}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.vigenere.algorithm;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithm;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithmAction;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.classic.ClassicDataObject;
import org.jcryptool.crypto.classic.vigenere.VigenerePlugin;
import org.jcryptool.crypto.classic.vigenere.ui.VigenereWizard;

/**
 * The VigenereAlgorithmAction class is a specific implementation of
 * AbstractClassicAlgorithmAction.
 *
 * @see org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithmAction
 *
 * @author amro
 * @version 0.6.0
 */
public class VigenereAlgorithmAction extends AbstractAlgorithmAction {
    /**
     * Constructor.
     */
    public VigenereAlgorithmAction() {
        super();
    }

    /**
     * This methods performs the action. Therefore the currentAlphabet input
     * is a standard input.
     */
    public void run() {
        final VigenereWizard wizard = new VigenereWizard();
        final AbstractClassicAlgorithm algorithm = new VigenereAlgorithm();
        WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
        dialog.setHelpAvailable(true);

        if (dialog.open() == Window.OK) {
            Job job = new Job(Messages.VigenereAlgorithmAction_0) {
                public IStatus run(final IProgressMonitor monitor) {
                    try {
                        String jobTitle = Messages.VigenereAlgorithmAction_1;

                        if (!wizard.encrypt()) {
                            jobTitle = Messages.VigenereAlgorithmAction_2;
                        }

                        monitor.beginTask(jobTitle, 4);

                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }

                        char[] key = wizard.getKey().toCharArray();

                        monitor.worked(1);

                        AbstractAlphabet alphabet = wizard.getSelectedAlphabet();

                        monitor.worked(2);

                        if (wizard.encrypt()) { // explicit encrypt
                            algorithm.init(AbstractAlgorithm.ENCRYPT_MODE, getActiveEditorInputStream(), alphabet, key, wizard.getTransformData());
                        } else { // implicit decrypt
                            algorithm.init(AbstractAlgorithm.DECRYPT_MODE, getActiveEditorInputStream(), alphabet, key, wizard.getTransformData());
                        }

                        monitor.worked(3);

                        if (!wizard.isNonAlphaFilter()) {
                            algorithm.setFilter(false);
                        }

                        monitor.worked(4);

                        VigenereAlgorithmAction.super.finalizeRun(algorithm);
                    } catch (final Exception ex) {
                        LogUtil.logError(VigenerePlugin.PLUGIN_ID, ex);
                    } finally {
                        monitor.done();
                    }

                    return Status.OK_STATUS;
                }
            };
            job.setUser(true);
            job.schedule();
        }
    }

    @Override
	public void run(IDataObject dataobject) {
		AbstractClassicAlgorithm algorithm = new VigenereAlgorithm();

		algorithm.init((ClassicDataObject) dataobject);

		super.finalizeRun(algorithm);
	}
}

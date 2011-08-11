//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.substitution.algorithm;

import java.io.InputStream;

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
import org.jcryptool.crypto.classic.substitution.SubstitutionPlugin;
import org.jcryptool.crypto.classic.substitution.ui.SubstitutionWizard;

public class SubstitutionAlgorithmAction extends AbstractAlgorithmAction {

	/**
	 * Constructor
	 *
	 */
	public SubstitutionAlgorithmAction() {
		super();
	}

	/**
	 * This methods performs the action
	 */
	public void run() {

		final SubstitutionWizard wizard = new SubstitutionWizard();
		final WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setHelpAvailable(true);

		if (dialog.open() == Window.OK) {
            Job job = new Job(Messages.SubstitutionAlgorithmAction_0) {
                public IStatus run(final IProgressMonitor monitor) {
                    try {
                        String jobTitle = Messages.SubstitutionAlgorithmAction_1;

                        if (!wizard.encrypt()) {
                            jobTitle = Messages.SubstitutionAlgorithmAction_2;
                        }

                        monitor.beginTask(jobTitle, 4);

                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }

						InputStream editorInputStream = null;
						editorInputStream = getActiveEditorInputStream();
						
						monitor.worked(1);
			
						char[] key = wizard.getKey().toCharArray();
						AbstractAlphabet alphabet = wizard.getSelectedAlphabet();
						
						monitor.worked(2);
						
						AbstractClassicAlgorithm algorithm = new SubstitutionAlgorithm();
						if (editorInputStream != null) {
							if (wizard.encrypt()) {
								// explicit encrypt
								algorithm.init(AbstractAlgorithm.ENCRYPT_MODE,
										editorInputStream, alphabet, key, wizard.getTransformData());
							} else {
								// implicit decrypt
								algorithm.init(AbstractAlgorithm.DECRYPT_MODE,
										editorInputStream, alphabet, key, wizard.getTransformData());
							}
						}
						
						monitor.worked(3);
			
						// check filter
						if (!wizard.isNonAlphaFilter()) {
							algorithm.setFilter(false);
						}
						
						monitor.worked(4);
						
						SubstitutionAlgorithmAction.super.finalizeRun(algorithm);
                    } catch (final Exception ex) {
                        LogUtil.logError(SubstitutionPlugin.PLUGIN_ID, ex);
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
    /**
     * Rewrite a byte array as a char array
     * method taken from class ByteArrayUtils
     * @param input -
     *            the byte array
     * @return char array
     */

    protected static char[] toCharArray(byte[] input) {

        char[] result = new char[input.length];

        for (int i = 0; i < input.length; i++) {

            result[i] = (char) input[i];

        }

        return result;

    }

    @Override
	public void run(IDataObject dataobject) {
		AbstractClassicAlgorithm algorithm = new SubstitutionAlgorithm();

		ClassicDataObject d = (ClassicDataObject)dataobject;
		algorithm.init(d);

		super.finalizeRun(algorithm);
	}

}

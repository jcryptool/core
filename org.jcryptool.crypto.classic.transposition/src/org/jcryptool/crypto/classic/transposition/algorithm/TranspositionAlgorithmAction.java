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
package org.jcryptool.crypto.classic.transposition.algorithm;


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
import org.jcryptool.crypto.classic.transposition.TranspositionPlugin;
import org.jcryptool.crypto.classic.transposition.ui.TranspositionWizard;

/**
 * The CaesarAlgorithmAction class is a specific
 * implementation of AbstractClassicAlgorithmAction.
 * @see org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithmAction
 *
 * @author SLeischnig
 * @version 0.1
 *
 */
public class TranspositionAlgorithmAction extends AbstractAlgorithmAction{

	/**
	 * Constructor
	 *
	 */
	public TranspositionAlgorithmAction() {
		super();
	}

	/**
	 * This methods performs the action
	 */
	public void run() {
		final TranspositionWizard wizard = new TranspositionWizard();
		final WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setHelpAvailable(true);

		if (dialog.open() == Window.OK) {
            Job job = new Job(Messages.TranspositionAlgorithmAction_0) {
                public IStatus run(final IProgressMonitor monitor) {
                    try {
                        String jobTitle = Messages.TranspositionAlgorithmAction_1;

                        if (!wizard.encrypt()) {
                            jobTitle = Messages.TranspositionAlgorithmAction_2;
                        }

                        monitor.beginTask(jobTitle, 4);

                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }

						InputStream editorContent = getActiveEditorInputStream();
						
						
						AbstractAlphabet alphabet = wizard.getSelectedAlphabet();
						
						char[] formattedKey1 = TranspositionAlgorithm.specification.keyInputStringToDataobjectFormat(wizard.getKey(), alphabet, wizard.getTransp1InOrder(), wizard.getTransp1OutOrder());
						char[] formattedKey2 = TranspositionAlgorithm.specification.keyInputStringToDataobjectFormat(wizard.getKey2(), alphabet, wizard.getTransp2InOrder(), wizard.getTransp2OutOrder());
						
						monitor.worked(1);
						
						AbstractClassicAlgorithm algorithm = new TranspositionAlgorithm();
						
						monitor.worked(2);
						
						if (wizard.encrypt()) {
							// explicit encrypt
							algorithm.init(AbstractAlgorithm.ENCRYPT_MODE,
									editorContent,
									alphabet,
									formattedKey1, formattedKey2, wizard.getTransformData());
						} else {
							// implicit decrypt
							algorithm.init(AbstractAlgorithm.DECRYPT_MODE,
									editorContent,
									alphabet,
									formattedKey1, formattedKey2, wizard.getTransformData());
						}
						
						monitor.worked(3);
						
						if(!wizard.isNonAlphaFilter()) {
							algorithm.setFilter(false);
						}
						
						monitor.worked(4);
						
						TranspositionAlgorithmAction.super.finalizeRun(algorithm);
                    } catch (final Exception ex) {
                        LogUtil.logError(TranspositionPlugin.PLUGIN_ID, ex);
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
		AbstractClassicAlgorithm algorithm = new TranspositionAlgorithm();

		ClassicDataObject d = (ClassicDataObject)dataobject;
		algorithm.init(d);

		super.finalizeRun(algorithm);
	}

}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008, 2014 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.xor.algorithm;

import java.io.InputStream;
import java.util.Observer;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithm;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithmHandler;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.classic.ClassicDataObject;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfiguration;
import org.jcryptool.crypto.classic.model.ui.wizard.ClassicWizardDialog;
import org.jcryptool.crypto.classic.xor.XorPlugin;
import org.jcryptool.crypto.classic.xor.ui.XorWizard;

public class XorAlgorithmHandler extends AbstractAlgorithmHandler{

	/**
	 * Constructor
	 *
	 */
	public XorAlgorithmHandler() {
		super();
	}

	/**
	 * This methods performs the action
	 */
	@Override
	public Object execute(ExecutionEvent event) {
		final XorWizard wizard = new XorWizard();
		final WizardDialog dialog = new ClassicWizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setHelpAvailable(true);

		if (dialog.open() == Window.OK) {
            Job job = new Job(Messages.XorAlgorithmHandler_0) {
                @Override
				public IStatus run(final IProgressMonitor monitor) {
                    try {
                        String jobTitle = Messages.XorAlgorithmHandler_1;

                        if (!wizard.encrypt()) {
                            jobTitle = Messages.XorAlgorithmHandler_2;
                        }

                        monitor.beginTask(jobTitle, 4);

                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }

						InputStream editorContent = getActiveEditorInputStream();
						char[] key = "".toCharArray(); //$NON-NLS-1$
						char[] pathToKeyFile = "".toCharArray(); //$NON-NLS-1$
						if (wizard.getKeyMethod()) {
							key = wizard.getKey().toCharArray();
						} else {
							pathToKeyFile = wizard.getPathToKeyFile().toCharArray();
						}
						
						monitor.worked(1);
						
						AbstractAlphabet alphabet = wizard.getSelectedAlphabet();
			
						AbstractClassicAlgorithm algorithm = new XorAlgorithm();
						
						monitor.worked(2);
			
						if (wizard.encrypt()) {
							// explicit encrypt
							algorithm.init(AbstractAlgorithm.ENCRYPT_MODE, editorContent, alphabet, key, pathToKeyFile, wizard.getTransformData());
			
						} else {
							// implicit decrypt
							algorithm.init(AbstractAlgorithm.DECRYPT_MODE, editorContent, alphabet, key, pathToKeyFile, wizard.getTransformData());
						}
						
						monitor.worked(3);
						
						//check filter
						if (!wizard.isNonAlphaFilter()) {
							algorithm.setFilter(false);
						}
						
						monitor.worked(4);
						
						XORConfiguration config = new XORConfiguration(
								wizard.encrypt(),
								wizard.getSelectedAlphabet(), 
								wizard.isNonAlphaFilter(), 
								wizard.getTransformData(), 
								wizard.getKeyMethod(),
								wizard.getKey(),
								wizard.getPathToKeyFile()
								);
						Observer editorOpenObserver = ClassicAlgorithmConfiguration.createEditorOpenHandler(algorithm, config);

						XorAlgorithmHandler.super.finalizeRun(algorithm, editorOpenObserver);
                    } catch (final Exception ex) {
                        LogUtil.logError(XorPlugin.PLUGIN_ID, ex);
                    } finally {
                        monitor.done();
                    }

                    return Status.OK_STATUS;
                }
            };
            job.setUser(true);
            job.schedule();
		}
		return(null);
	}

	@Override
	public void run(IDataObject dataobject) {
		AbstractClassicAlgorithm algorithm = new XorAlgorithm();

		ClassicDataObject d = (ClassicDataObject)dataobject;
		algorithm.init(d);

		super.finalizeRun(algorithm);
	}

}

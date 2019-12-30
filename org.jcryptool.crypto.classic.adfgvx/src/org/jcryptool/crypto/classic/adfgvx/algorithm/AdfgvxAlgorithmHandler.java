//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.adfgvx.algorithm;

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
import org.jcryptool.crypto.classic.adfgvx.AdfgvxPlugin;
import org.jcryptool.crypto.classic.adfgvx.ui.AdfgvxWizard;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfiguration;
import org.jcryptool.crypto.classic.model.ui.wizard.ClassicWizardDialog;

public class AdfgvxAlgorithmHandler extends AbstractAlgorithmHandler{
	/**
	 * Constructor
	 *
	 */
	public AdfgvxAlgorithmHandler() {
		super();
	}

	/**
	 * This methods performs the action
	 */
	@Override
	public Object execute(ExecutionEvent event) {
		final AdfgvxWizard wizard = new AdfgvxWizard();
		final WizardDialog dialog = new ClassicWizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setHelpAvailable(true);
		
		if (dialog.open() == Window.OK) {
            Job job = new Job(Messages.AdfgvxAlgorithmHandler_0) {
                @Override
				public IStatus run(final IProgressMonitor monitor) {
                    try {
                        String jobTitle = Messages.AdfgvxAlgorithmHandler_1;

                        if (!wizard.encrypt()) {
                            jobTitle = Messages.AdfgvxAlgorithmHandler_2;
                        }

                        monitor.beginTask(jobTitle, 4);

                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }

			            InputStream editorContent = getActiveEditorInputStream();
						//String editorContent = getActiveEditorContentAsString();
						char[] key = wizard.getSubstitutionKey().toCharArray();
						char[] key2 = wizard.getTranspositionKey().toCharArray();
						
						monitor.worked(1);						
						
						AbstractAlphabet alphabet = wizard.getSelectedAlphabet();
						
						monitor.worked(2);
			
						final AbstractClassicAlgorithm algorithm = new AdfgvxAlgorithm();
			
						if (wizard.encrypt()) {
							// explicit encrypt
							algorithm.init(AbstractAlgorithm.ENCRYPT_MODE, editorContent, alphabet, key, key2, wizard.getTransformData());
						} else {
							// implicit decrypt
							algorithm.init(AbstractAlgorithm.DECRYPT_MODE, editorContent, alphabet, key, key2, wizard.getTransformData());
						}
						
						monitor.worked(3);
			
						if(!wizard.isNonAlphaFilter()) {
							algorithm.setFilter(false);
						}
						
						monitor.worked(4);
						
						AdfgvxConfiguration config = new AdfgvxConfiguration(
								wizard.encrypt(),
								wizard.getSelectedAlphabet(), 
								wizard.isNonAlphaFilter(), 
								wizard.getTransformData(), 
								wizard.getSubstitutionKeyAsEntered(), 
								wizard.getTranspositionKey()
							);
						Observer editorOpenObserver = ClassicAlgorithmConfiguration.createEditorOpenHandler(algorithm, config);
						AdfgvxAlgorithmHandler.super.finalizeRun(algorithm, editorOpenObserver);
						
                    } catch (final Exception ex) {
                        LogUtil.logError(AdfgvxPlugin.PLUGIN_ID, ex);
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
		AbstractClassicAlgorithm algorithm = new AdfgvxAlgorithm();

		ClassicDataObject d = (ClassicDataObject)dataobject;
		algorithm.init(d);

		super.finalizeRun(algorithm);
	}



}

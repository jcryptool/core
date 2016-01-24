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
package org.jcryptool.crypto.classic.delastelle.algorithm;


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
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.classic.ClassicDataObject;
import org.jcryptool.crypto.classic.delastelle.DelastellePlugin;
import org.jcryptool.crypto.classic.delastelle.ui.DelastelleWizard;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfiguration;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfigurationWithKey;
import org.jcryptool.crypto.classic.model.ui.wizard.ClassicWizardDialog;

/**
 * The DelastelleAlgorithmHandler class is a specific
 * implementation of AbstractClassicAlgorithmAction.
 * @see org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithmAction
 *
 * @author SLeischnig
 * @author Holger Friedrich (support for Commands, based on DelastelleAlgorithmAction)
 * @version 0.2
 *
 */
public class DelastelleAlgorithmHandler extends AbstractAlgorithmHandler{

	/**
	 * Constructor
	 *
	 */
	public DelastelleAlgorithmHandler() {
		super();
	}

	/**
	 * This methods performs the action
	 */
	@Override
	public Object execute(ExecutionEvent event) {
		final DelastelleWizard wizard = new DelastelleWizard();
		final WizardDialog dialog = new ClassicWizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setHelpAvailable(true);

		if (dialog.open() == Window.OK) {
            Job job = new Job(Messages.DelastelleAlgorithmHandler_0) {
                @Override
				public IStatus run(final IProgressMonitor monitor) {
                    try {
                        String jobTitle = Messages.DelastelleAlgorithmHandler_1;

                        if (!wizard.encrypt()) {
                            jobTitle = Messages.DelastelleAlgorithmHandler_2;
                        }

                        monitor.beginTask(jobTitle, 4);

                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }
                        
						InputStream editorContent = getActiveEditorInputStream();
						char[] key = wizard.getKey().toCharArray();
						
						monitor.worked(1);
						
						AbstractAlphabet alphabet = wizard.getSelectedAlphabet();
						TransformData myTransformData = wizard.getTransformData();
						AbstractClassicAlgorithm algorithm = new DelastelleAlgorithm();
						
						monitor.worked(2);
			
						if (wizard.encrypt()) {
							// explicit encrypt
							algorithm.init(AbstractAlgorithm.ENCRYPT_MODE,
									editorContent,
									alphabet,
									key,
									myTransformData);
						} else {
							// implicit decrypt
							algorithm.init(AbstractAlgorithm.DECRYPT_MODE,
									editorContent,
									alphabet,
									key,
									myTransformData);
						}
						
						monitor.worked(3);
						
						if(!wizard.isNonAlphaFilter()) {
							algorithm.setFilter(false);
						}
						
						monitor.worked(4);
						
			            ClassicAlgorithmConfigurationWithKey config = new ClassicAlgorithmConfigurationWithKey(
			            		wizard.encrypt(),
			            		"Bifid",
			            		wizard.getSelectedAlphabet(), 
			            		wizard.isNonAlphaFilter(), 
			            		wizard.getTransformData(), 
			            		wizard.getKey()
			            		);
			            Observer editorOpenObserver = ClassicAlgorithmConfiguration.createEditorOpenHandler(algorithm, config);

						DelastelleAlgorithmHandler.super.finalizeRun(algorithm, editorOpenObserver);
                    } catch (final Exception ex) {
                        LogUtil.logError(DelastellePlugin.PLUGIN_ID, ex);
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
		AbstractClassicAlgorithm algorithm = new DelastelleAlgorithm();
		
		ClassicDataObject d = (ClassicDataObject)dataobject;
		algorithm.init(d);
		
		super.finalizeRun(algorithm);
	}

}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.doppelkasten.algorithm;


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
import org.jcryptool.crypto.classic.doppelkasten.DoppelkastenPlugin;
import org.jcryptool.crypto.classic.doppelkasten.ui.DoppelkastenWizard;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfiguration;
import org.jcryptool.crypto.classic.model.ui.wizard.ClassicWizardDialog;

/**
 * The DoppelkastenAlgorithmHandler class is a specific
 * implementation of AbstractClassicAlgorithmAction.
 * @see org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithmAction
 *
 * @author SLeischnig
 * @author Holger Friedrich (support for Commands, based on DoppelkastenAlgorithmAction)
 * @version 0.2
 *
 */
public class DoppelkastenAlgorithmHandler extends AbstractAlgorithmHandler{

	/**
	 * Constructor
	 *
	 */
	public DoppelkastenAlgorithmHandler() {
		super();
	}

	/**
	 * This methods performs the action
	 */
	@Override
	public Object execute(ExecutionEvent event) {
		final DoppelkastenWizard wizard = new DoppelkastenWizard();
		final WizardDialog dialog = new ClassicWizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setHelpAvailable(true);

		if (dialog.open() == Window.OK) {
            Job job = new Job(Messages.DoppelkastenAlgorithmHandler_0) {
                @Override
				public IStatus run(final IProgressMonitor monitor) {
                    try {
                        String jobTitle = Messages.DoppelkastenAlgorithmHandler_1;

                        if (!wizard.encrypt()) {
                            jobTitle = Messages.DoppelkastenAlgorithmHandler_2;
                        }

                        monitor.beginTask(jobTitle, 4);

                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }

						InputStream editorContent = getActiveEditorInputStream();
						char[] key = wizard.getKey().toCharArray();
						
						monitor.worked(1);
						
						AbstractAlphabet alphabet = wizard.getSelectedAlphabet();
						AbstractClassicAlgorithm algorithm = new DoppelkastenAlgorithm();
						
						monitor.worked(2);
			
						if (wizard.encrypt()) {
							// explicit encrypt
							algorithm.init(AbstractAlgorithm.ENCRYPT_MODE,
									editorContent,
									alphabet,
									key, wizard.getTransformData());
						} else {
							// implicit decrypt
							algorithm.init(AbstractAlgorithm.DECRYPT_MODE,
									editorContent,
									alphabet,
									key, wizard.getTransformData());
						}
						
						monitor.worked(3);
						
						if(!wizard.isNonAlphaFilter()) {
							algorithm.setFilter(false);
						}
						
						monitor.worked(4);
						
						String[] ungluedKeys = DoppelkastenAlgorithmSpecification.unglueKeys(wizard.getKey());
						if(ungluedKeys.length == 2) {
							String key1 = ungluedKeys[0];
							String key2 = DoppelkastenAlgorithmSpecification.unglueKeys(wizard.getKey())[1];
							DoppelkastenConfiguration config = new DoppelkastenConfiguration(
									wizard.encrypt(),
									wizard.getSelectedAlphabet(), 
									wizard.isNonAlphaFilter(), 
									wizard.getTransformData(), 
									key1, 
									key2
									);
							Observer editorOpenObserver = ClassicAlgorithmConfiguration.createEditorOpenHandler(algorithm, config);
							DoppelkastenAlgorithmHandler.super.finalizeRun(algorithm, editorOpenObserver);
						} else {
							LogUtil.logError(DoppelkastenPlugin.PLUGIN_ID, "Could not unglue keys for algorithm configuration storage.");
							DoppelkastenAlgorithmHandler.super.finalizeRun(algorithm);
						}
                    } catch (final Exception ex) {
                        LogUtil.logError(DoppelkastenPlugin.PLUGIN_ID, ex);
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
		AbstractClassicAlgorithm algorithm = new DoppelkastenAlgorithm();

		ClassicDataObject d = (ClassicDataObject)dataobject;
		algorithm.init(d);

		super.finalizeRun(algorithm);
	}
}

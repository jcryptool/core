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
package org.jcryptool.crypto.classic.transposition.algorithm;


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
import org.jcryptool.crypto.classic.transposition.TranspositionPlugin;
import org.jcryptool.crypto.classic.transposition.ui.TranspositionWizard;

/**
 * The TranspositionAlgorithmHandler class is a specific
 * implementation of AbstractClassicAlgorithmAction.
 * @see org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithmAction
 *
 * @author SLeischnig
 * @author Holger Friedrich (support of Commands, based on TranspositionAlgorithmAction)
 * @version 0.2
 *
 */
public class TranspositionAlgorithmHandler extends AbstractAlgorithmHandler{

	/**
	 * Constructor
	 *
	 */
	public TranspositionAlgorithmHandler() {
		super();
	}

	/**
	 * This methods performs the action
	 */
	@Override
	public Object execute(ExecutionEvent event) {
		final TranspositionWizard wizard = new TranspositionWizard();
		final WizardDialog dialog = new ClassicWizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setHelpAvailable(true);

		if (dialog.open() == Window.OK) {
            Job job = new Job(Messages.TranspositionAlgorithmHandler_0) {
                @Override
				public IStatus run(final IProgressMonitor monitor) {
                    try {
                        String jobTitle = Messages.TranspositionAlgorithmHandler_1;

                        if (!wizard.encrypt()) {
                            jobTitle = Messages.TranspositionAlgorithmHandler_2;
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
						
						String transpKey2EnteredString = wizard.getTranspKey2EnteredString();
						if(wizard.getKey2().length() == 0) transpKey2EnteredString = "";
						TranspositionConfiguration config = new TranspositionConfiguration(
								wizard.encrypt(),
								wizard.getSelectedAlphabet(), 
								wizard.isNonAlphaFilter(), 
								wizard.getTransformData(), 
								wizard.getTranspKey1EnteredString(),
								transpKey2EnteredString,
								wizard.getTransp1InOrder(),
								wizard.getTransp2InOrder(),
								wizard.getTransp1OutOrder(),
								wizard.getTransp2OutOrder()								
							);
						Observer editorOpenObserver = ClassicAlgorithmConfiguration.createEditorOpenHandler(algorithm, config);
						
						TranspositionAlgorithmHandler.super.finalizeRun(algorithm, editorOpenObserver);
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
		return(null);
	}

	@Override
	public void run(IDataObject dataobject) {
		AbstractClassicAlgorithm algorithm = new TranspositionAlgorithm();

		ClassicDataObject d = (ClassicDataObject)dataobject;
		algorithm.init(d);

		super.finalizeRun(algorithm);
	}

}

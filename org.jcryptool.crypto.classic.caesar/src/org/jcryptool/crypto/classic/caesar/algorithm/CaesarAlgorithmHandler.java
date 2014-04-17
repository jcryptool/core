// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.caesar.algorithm;

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
import org.jcryptool.crypto.classic.caesar.CaesarPlugin;
import org.jcryptool.crypto.classic.caesar.ui.CaesarWizard;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfiguration;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfigurationWithKey;

/**
 * The CaesarAlgorithmAction class is a specific implementation of AbstractClassicAlgorithmAction2.
 *
 * @see org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithmAction2
 *
 * @author amro
 * @version 0.5
 */
public class CaesarAlgorithmHandler extends AbstractAlgorithmHandler {
    public CaesarAlgorithmHandler() {
        super();
    }

    /**
     * This methods performs the action. Therefore the currentAlphabet input is a standard input.
     */
    @Override
	public Object execute(ExecutionEvent event) {
        final CaesarWizard wizard = new CaesarWizard();
        final WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setHelpAvailable(true);

		if (dialog.open() == Window.OK) {
            Job job = new Job(Messages.CaesarAlgorithmHandler_0) {
                @Override
				public IStatus run(final IProgressMonitor monitor) {
                    try {
                        String jobTitle = Messages.CaesarAlgorithmHandler_1;

                        if (!wizard.encrypt()) {
                            jobTitle = Messages.CaesarAlgorithmHandler_2;
                        }

                        monitor.beginTask(jobTitle, 4);

                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }
                        
			            char[] key = wizard.getKey().toCharArray();
			            
			            monitor.worked(1);
			            
			            AbstractAlphabet alphabet = wizard.getSelectedAlphabet();
			            AbstractClassicAlgorithm algorithm = new CaesarAlgorithm();
			            
			            monitor.worked(2);
			
			            if (wizard.encrypt()) {
			                // explicit encrypt
			                algorithm.init(AbstractAlgorithm.ENCRYPT_MODE, getActiveEditorInputStream(), alphabet, key, wizard.getTransformData());
			            } else {
			                // implicit decrypt
			                algorithm.init(AbstractAlgorithm.DECRYPT_MODE, getActiveEditorInputStream(), alphabet, key, wizard.getTransformData());
			            }
			            
			            monitor.worked(3);
			            
			            if (!wizard.isNonAlphaFilter()) {
			                algorithm.setFilter(false);
			            }
			            
			            monitor.worked(4);
			            
			            ClassicAlgorithmConfigurationWithKey config = new ClassicAlgorithmConfigurationWithKey(
								wizard.encrypt(),
								"Caesar",
								wizard.getSelectedAlphabet(), 
								wizard.isNonAlphaFilter(), 
								wizard.getTransformData(), 
								wizard.getKey()
							);
						Observer editorOpenObserver = ClassicAlgorithmConfiguration.createEditorOpenHandler(algorithm, config);
			            CaesarAlgorithmHandler.super.finalizeRun(algorithm, editorOpenObserver);
                    } catch (final Exception ex) {
                        LogUtil.logError(CaesarPlugin.PLUGIN_ID, ex);
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
		AbstractClassicAlgorithm algorithm = new CaesarAlgorithm();

		ClassicDataObject d = (ClassicDataObject)dataobject;
		algorithm.init(d);

		super.finalizeRun(algorithm);
	}
}

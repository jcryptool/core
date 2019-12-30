// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.model.algorithmconfig;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.crypto.classic.model.ClassicCryptoModelPlugin;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfiguration;
import org.jcryptool.crypto.classic.model.algorithmconfig.ui.ClassicAlgorithmConfigViewer;

public class ShowAlgorithmConfigAction implements IWorkbenchWindowActionDelegate {
    /**
     * The action has been activated. The argument of the method represents the 'real' action
     * sitting in the workbench UI.
     *
     * @see IWorkbenchWindowActionDelegate#run
     */
    @Override
	public void run(IAction action) {
        if(EditorsManager.getInstance().isEditorOpen()) {
    		IEditorPart currentEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    		if(currentEditor != null) {
    			ClassicAlgorithmConfiguration config = ClassicAlgorithmConfiguration.getAlgorithmConfigForEditor(currentEditor);

    			if(config != null) {
    				showConfiguration(currentEditor, config);
    			} else {
    				handleNoConfigToShow();
    			}
    		} else {
    			handleNoActiveEditor();
    		}
        } else {
        	handleNoEditorOpen();
        }
    }

    private void showConfiguration(IEditorPart currentEditor, ClassicAlgorithmConfiguration config) {
    	try {
			Display display = Display.getDefault();
			ClassicAlgorithmConfigViewer shell = new ClassicAlgorithmConfigViewer(
					display, currentEditor, config);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception ex) {
		    LogUtil.logError(ex);
		}
	}

	private void handleNoActiveEditor() {
    	//TODO: better handling
    	LogUtil.logInfo(Messages.ShowAlgorithmConfigAction_0, ClassicCryptoModelPlugin.PLUGIN_ID);
	}

	private void handleNoEditorOpen() {
    	//TODO: better handling
    	LogUtil.logInfo(Messages.ShowAlgorithmConfigAction_1, ClassicCryptoModelPlugin.PLUGIN_ID);
	}

	private void handleNoConfigToShow() {
		//TODO: better handling
		LogUtil.logInfo(Messages.ShowAlgorithmConfigAction_2, ClassicCryptoModelPlugin.PLUGIN_ID);
	}


	/**
     * Selection in the workbench has been changed. We can change the state of the 'real' action
     * here if we want, but this can only happen after the delegate has been created.
     *
     * @see IWorkbenchWindowActionDelegate#selectionChanged
     */
    @Override
	public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
     * We can use this method to dispose of any system resources we previously allocated.
     *
     * @see IWorkbenchWindowActionDelegate#dispose
     */
    @Override
	public void dispose() {
    }

    /**
     * We will cache window object in order to be able to provide parent shell for the message
     * dialog.
     *
     * @see IWorkbenchWindowActionDelegate#init
     */
    @Override
	public void init(IWorkbenchWindow window) {
    }
}
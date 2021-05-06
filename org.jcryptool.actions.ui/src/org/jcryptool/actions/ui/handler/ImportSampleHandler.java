// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.ui.handler;

import java.io.File;
import java.net.URL;
import java.security.InvalidParameterException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.actions.core.registry.ActionCascadeService;
import org.jcryptool.actions.core.utils.ImportUtils;
import org.jcryptool.actions.ui.ActionsUIPlugin;
import org.jcryptool.actions.ui.views.ActionView;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.editor.text.startup.StartUp;

/**
 * @author Anatoli Barski
 *
 */
public class ImportSampleHandler extends AbstractHandler {

    /* (non-Javadoc)
     * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

        String filename = getCascadeFilename(event);

        if (filename != null && filename.length() > 0) {

            ImportUtils importUtil = new ImportUtils(filename);
            boolean isValid = importUtil.validateActionCascade();

            if (isValid) {

            	openDefaultPerspective();
            	openActionView();
            	openSamplefileInEditor();

            	ActionCascadeService.getInstance().setCurrentActionCascade(importUtil.createActionCascade());
            } else {
                MessageDialog.openInformation(HandlerUtil.getActiveShell(event), Messages.ImportHandler_2,
                Messages.ImportHandler_3);
            }
        }

        return null;
    }

	private String getCascadeFilename(ExecutionEvent event) {

		String cascadeFilename = event.getParameter("cascadeFilename"); //$NON-NLS-1$
    	if(cascadeFilename == null)
    	{
    		throw new InvalidParameterException("command parameter cascadeFilename is required"); //$NON-NLS-1$
    	}

    	URL bundleUrl = null;
    	File cascadeFile = null;

        try {
        	bundleUrl =
                    FileLocator.toFileURL((ActionsUIPlugin.getDefault().getBundle().getEntry("/"))); //$NON-NLS-1$
            cascadeFile = new File(bundleUrl.getFile()
                    + "templates" + File.separatorChar //$NON-NLS-1$
                    + cascadeFilename); 

        } catch (Exception ex) {
            LogUtil.logError("Error loading sample file " + cascadeFilename + " from plugin.", ex); //$NON-NLS-1$ //$NON-NLS-2$
        }

    	ActionCascadeService service = ActionCascadeService.getInstance();

    	if (service.getCurrentActionCascade() != null && service.getCurrentActionCascade().getSize()>0){
            boolean confirmImport = MessageDialog
                    .openConfirm(HandlerUtil.getActiveShell(event), Messages.ImportHandler_0,
                            Messages.ImportHandler_1);
            if (!confirmImport) {
                return null;
            }
        }
		return cascadeFile.getAbsolutePath();
	}

	private void openSamplefileInEditor() {
		StartUp startUp = new StartUp();
    	startUp.earlyStartup();
	}

	private void openDefaultPerspective() {
		IWorkbench workbench = PlatformUI.getWorkbench();
    	IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

    	try {
    		// open the default perspective
			workbench.showPerspective(org.jcryptool.core.Perspective.PERSPECTIVE_ID, window);
		} catch (WorkbenchException e) {
			LogUtil.logError(e);
		}
	}

	private void openActionView() {

		IWorkbench workbench = PlatformUI.getWorkbench();
    	IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

    	try {
			// open the action view
			window.getActivePage().showView(ActionView.ID);
		} catch (WorkbenchException e) {
			LogUtil.logError(e);
		}
	}

}

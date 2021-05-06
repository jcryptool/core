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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.actions.core.registry.ActionCascadeService;
import org.jcryptool.actions.core.types.ActionCascade;
import org.jcryptool.actions.ui.ActionsUIPlugin;
import org.jcryptool.actions.ui.preferences.PreferenceConstants;

/**
 * <b>New cascade handler</b> for the Actions view. Creates a new action cascade. The user has to
 * confirm this action if there is unsaved data contained in the view.
 * 
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class NewCascadeHandler extends AbstractHandler {
    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        ActionCascadeService service = ActionCascadeService.getInstance();
    	
    	if (service.getCurrentActionCascade() != null && service.getCurrentActionCascade().getSize()>0){
            boolean confirmNew = MessageDialog.openConfirm(HandlerUtil.getActiveShell(event),
                    Messages.NewCascadeHandler_0, Messages.NewCascadeHandler_1);

            if (confirmNew) {
            	ActionCascade ac = new ActionCascade("actionCascade"); //$NON-NLS-1$
                ActionsUIPlugin.getDefault().getPreferenceStore()
                  .getBoolean(PreferenceConstants.P_STORE_PASSWORDS);
            	service.setCurrentActionCascade(ac); 
            }
        }

        ICommandService commandService = PlatformUI.getWorkbench().getService(
                ICommandService.class);
        Command command = commandService.getCommand("org.jcryptool.actions.recordCommand"); //$NON-NLS-1$
        State state = command.getState("org.jcryptool.actions.recordCommand.toggleState"); //$NON-NLS-1$

        boolean doRecord = (Boolean) state.getValue();

        if (doRecord) {
            state.setValue(!doRecord);
            commandService.refreshElements("org.jcryptool.actions.recordCommand", null); //$NON-NLS-1$
        }

        return null;
    }
}

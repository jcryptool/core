// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----

package org.jcryptool.core.operations;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

/**
 * Class containing an IAction and an AbstractHandler to support both Actions and Commands.
 * This is used in various lists and maps containing the Actions/Commands for various algorithms.
 * The understanding is that either the IAction is non-null and the command ID and the AbstractHandler are both null,
 * or the IAction is null and the command ID and the AbstractHandler are both null.
 * 
 * @author Holger Friedrich
 */
public class CommandOrAction {
	private IAction action;
	private String commandId;
	private AbstractHandler handler;	// or IHandler?
	
	public CommandOrAction(final String commandId, final AbstractHandler handler) {
		this.commandId = commandId;
		this.handler = handler;
	}
	
	public CommandOrAction(final IAction action) {
		this.action = action;
	}
	
	public String getCommandId() {
		return(commandId);
	}
	
	public AbstractHandler getHandler() {
		return(handler);
	}
	
	public IAction getAction() {
		return(action);
	}
	
	public void setEnabled(boolean active) {
		if(action != null) {
			action.setEnabled(active);
		}
		// TODO enable the Command somehow
		if(handler != null) {
			ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
			Command cmd = commandService.getCommand(commandId);
			if(active)
				cmd.setHandler(handler);
			else
				cmd.setHandler(null);
		}
	}
}

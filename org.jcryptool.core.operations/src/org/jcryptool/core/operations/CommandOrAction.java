package org.jcryptool.core.operations;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

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

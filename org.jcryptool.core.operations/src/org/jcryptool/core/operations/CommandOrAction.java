package org.jcryptool.core.operations;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.action.IAction;

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
		// if(handler != null) {
		//	handler.setBaseEnabled(active);
		// }
	}
}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----

package org.jcryptool.core.operations;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

public class CommandInfo {
	private String commandId;
	private AbstractHandler handler;
	
	private String text;
	private String tooltip;
	private ImageDescriptor icon;

	public CommandInfo(String commandId_, AbstractHandler handler_) {
		commandId = commandId_;
		handler = handler_;
	}
	
	public String getCommandId() {
		return(commandId);
	}
	
	public AbstractHandler getHandler() {
		return(handler);
	}
	
	public void setEnabled(boolean active) {
		if(handler != null) {
			ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
			Command cmd = commandService.getCommand(commandId);
			if(active)
				cmd.setHandler(handler);
			else
				cmd.setHandler(null);
		}
	}
	
	/** Does not define the command to be associated with the text.
	 * This should be called before the commands are defined.
	 *   
	 * @param value The text for the command, as it should appear e.g. in menu entries.
	 */
	public void setText(String value) {
		text = value;
	}
	
	public void setTooltip(String value) {
		tooltip = value;
	}
	
	public void setIcon(ImageDescriptor value) {
		icon = value;
	}
	
	public String getText() {
		return(text);
	}
	
	public String getTooltip() {
		return(tooltip);
	}
	
	public ImageDescriptor getIcon() {
		return(icon);
	}
}

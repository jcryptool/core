//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013 Florian Böhl <florian@boehl.name>
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.games.zudoku.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.games.zudoku.ZudokuPlugin;

public class HelpHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	    PlatformUI.getWorkbench().getHelpSystem().displayHelp(ZudokuPlugin.PLUGIN_ID + ".view"); //$NON-NLS-1$
		return null;
	}
}
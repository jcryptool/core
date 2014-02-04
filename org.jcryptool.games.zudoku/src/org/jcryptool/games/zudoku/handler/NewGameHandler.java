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
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.zudoku.views.ZudokuView;


public class NewGameHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	if (HandlerUtil.getActivePart(event) instanceof ZudokuView) {
            ZudokuView zv = (ZudokuView)HandlerUtil.getActivePart(event);
            zv.zudoku.verification_panel.newSudoku();
        }
    	return null;
    }
}
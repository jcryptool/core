// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.divide.logic.GameMachine;
import org.jcryptool.games.divide.views.DivideView;

public class UndoHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPart workbench = HandlerUtil.getActivePart(event);

        if (workbench != null && workbench instanceof DivideView) {
            DivideView view = (DivideView) HandlerUtil.getActivePart(event);
            GameMachine gameMachine = view.getGameMachine();
            if (gameMachine != null) {
                gameMachine.undo();
            }
        }

        return null;
    }
}

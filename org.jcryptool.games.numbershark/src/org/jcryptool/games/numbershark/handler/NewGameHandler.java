// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.numbershark.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.numbershark.dialogs.NewGameDialog;
import org.jcryptool.games.numbershark.views.NumberSharkView;

/**
 * This handler starts a new game.
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class NewGameHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof NumberSharkView) {
            NewGameDialog newGame = new NewGameDialog(HandlerUtil.getActiveShell(event));
            newGame.create();
            newGame.open();

            if (newGame.getReturnCode() == IDialogConstants.OK_ID) {
                NumberSharkView view = ((NumberSharkView) HandlerUtil.getActivePart(event));

                view.cleanPlayingField();
                view.createPlayingField(newGame.getNumberOfFields());
            }
        }

        return null;
    }
}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.games.numbershark.handler;

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.numbershark.util.CommandState;
import org.jcryptool.games.numbershark.util.CommandStateChanger;
import org.jcryptool.games.numbershark.views.Number;
import org.jcryptool.games.numbershark.views.NumberSharkView;

/**
 * This handler creates a shark meal.
 * 
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class SharkMealHandler extends AbstractHandler {
    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof NumberSharkView) {
            NumberSharkView view = ((NumberSharkView) HandlerUtil.getActivePart(event));

            ArrayList<Integer> sharkMealList = view.getSharkMealList();
            int[] lostNumbers;
            Number[] numberField = view.getNumberField();

            // calculate numbers to be eaten by the shark
            for (int k = 0; k < sharkMealList.size(); k++) {
                int i = sharkMealList.get(k);
                numberField[i - 1].setEnabled(false);

                int tabFolderIndex = view.getSelectedTabFolderIndex();

                if (tabFolderIndex * 40 < i && i < (tabFolderIndex + 1) * 40 + 1) {
                    view.setStatus(i - 1, false);
                }
            }

            lostNumbers = new int[sharkMealList.size()];
            for (int i = 0; i < lostNumbers.length; i++) {
                lostNumbers[i] = sharkMealList.get(i);
            }

            view.addMoveToTable(0, lostNumbers);

        }

        CommandStateChanger commandStateChanger = new CommandStateChanger();
        commandStateChanger.chageCommandState(CommandState.Variable.SHARKMEAL_STATE,
                CommandState.State.SHARKMEAL_DISABLED);

        return null;
    }
}

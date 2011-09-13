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

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.numbershark.views.Messages;
import org.jcryptool.games.numbershark.views.NumberSharkView;

/**
 * This handler undos the last step and enables the taken numbers.
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class UndoHandler extends AbstractHandler {
    // TODO disable undo icon in menu when score table is empty
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof NumberSharkView) {
            NumberSharkView view = ((NumberSharkView) HandlerUtil.getActivePart(event));

            Table scoreTable = view.getTable();

            int lastRow = scoreTable.getItemCount();

            if (lastRow < 1) {
                return null;
            }

            ArrayList<Integer> undoNumbers = new ArrayList<Integer>();
            TableItem row = scoreTable.getItem(lastRow - 1);
            String undoLostNumbers = row.getText(3);
            int iterator = undoLostNumbers.lastIndexOf(", "); //$NON-NLS-1$

            int tabFolderIndex = view.getSelectedTabFolderIndex();

            // reactivate numbers and buttons
            while (iterator != -1) {
                int toEnable = Integer.parseInt(undoLostNumbers.substring(iterator + 2, undoLostNumbers.length()));
                undoLostNumbers = undoLostNumbers.substring(0, iterator);
                undoNumbers.add(toEnable);
                view.setStatus(toEnable - 1, true);
                iterator = undoLostNumbers.lastIndexOf(", "); //$NON-NLS-1$
                if (tabFolderIndex * 40 < toEnable && toEnable < (tabFolderIndex + 1) * 40 + 1) {
                    view.enableNumber(toEnable - 1);
                }
            }

            int toEnable = Integer.parseInt(undoLostNumbers);
            view.setStatus(toEnable - 1, true);
            if (tabFolderIndex * 40 < toEnable && toEnable < (tabFolderIndex + 1) * 40 + 1) {
                view.enableNumber(toEnable - 1);
            }

            String takenNumberString = row.getText(1);

            if (!"-".equals(takenNumberString)) { //$NON-NLS-1$
                String temp = row.getText(1);
                if (temp.endsWith(Messages.NumberSharkView_0)) {
                    temp = temp.substring(0, temp.indexOf(Messages.NumberSharkView_0));
                }
                int takenNumber = Integer.parseInt(temp);
                view.setStatus(toEnable - 1, true);
                if (tabFolderIndex * 40 < takenNumber && takenNumber < (tabFolderIndex + 1) * 40 + 1) {
                    view.enableNumber(takenNumber - 1);
                }
            }

            scoreTable.getItem(lastRow - 1).dispose();

            // set the previous score
            if (scoreTable.getItemCount() > 0) {
                TableItem previousRow = scoreTable.getItem(scoreTable.getItemCount() - 1);
                view.setSharkScore(previousRow.getText(4));
                view.setPlayerScore(previousRow.getText(2));
            } else {
                view.setSharkScore(NumberSharkView.ZERO_SCORE);
                view.setPlayerScore(NumberSharkView.ZERO_SCORE);
            }
        }

        return null;
    }
}

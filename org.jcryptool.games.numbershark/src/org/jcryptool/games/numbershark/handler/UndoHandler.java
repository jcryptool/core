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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.numbershark.util.CommandState;
import org.jcryptool.games.numbershark.util.CommandStateChanger;
import org.jcryptool.games.numbershark.util.ScoreTableRow;
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

            Table scoreTableView = view.getTable();

            if (view.getActualPlayerMove() < 1) {
                return null;
            }

            ScoreTableRow scoreTableRow =  view.getScoreTableRowByActualPlayerPosition();
            
            String undoLostNumbers =  scoreTableRow.getLostNumbers(); // row.getText(3);
            int iterator = undoLostNumbers.lastIndexOf(", "); //$NON-NLS-1$

            // reactivate numbers and buttons
            while (iterator != -1) {
                int toEnable = Integer.parseInt(undoLostNumbers.substring(iterator + 2, undoLostNumbers.length()));
                undoLostNumbers = undoLostNumbers.substring(0, iterator);
                view.setStatus(toEnable - 1, true);
                iterator = undoLostNumbers.lastIndexOf(", "); //$NON-NLS-1$  
                view.enableNumber(toEnable - 1);               
            }

            int toEnable = Integer.parseInt(undoLostNumbers);
            view.setStatus(toEnable - 1, true);
            view.enableNumber(toEnable - 1);

            String takenNumberString = scoreTableRow.getTakenNumbers(); // row.getText(1);

            if (!"-".equals(takenNumberString)) { //$NON-NLS-1$
                String temp = scoreTableRow.getTakenNumbers(); // row.getText(1);
                if (temp.endsWith(Messages.NumberSharkView_0)) {
                    temp = temp.substring(0, temp.indexOf(Messages.NumberSharkView_0));
                }
                int takenNumber = Integer.parseInt(temp);
                view.setStatus(takenNumber - 1, true);
                view.enableNumber(takenNumber - 1);
            }

            scoreTableView.getItem(view.getLastPlayerMove()).dispose();

            // set the previous score
            if (scoreTableView.getItemCount() > 0) {
                TableItem previousRow = scoreTableView.getItem(scoreTableView.getItemCount() - 1);
                view.setSharkScore(previousRow.getText(4));
                view.setPlayerScore(previousRow.getText(2));
            } else {
                view.setSharkScore(NumberSharkView.ZERO_SCORE);
                view.setPlayerScore(NumberSharkView.ZERO_SCORE);
            }
        
            
            view.decreasePlayerMove();
            
            if (view.getActualPlayerMove() < 1){                
                CommandStateChanger commandStateChanger = new CommandStateChanger();
                commandStateChanger.chageCommandState(CommandState.Variable.UNDO_STATE, CommandState.State.UNDO_DISABLED);
            }
            
            if (view.hasScoreTableRowListNextEntry()){                
                CommandStateChanger commandStateChanger = new CommandStateChanger();
                commandStateChanger.chageCommandState(CommandState.Variable.REDO_STATE, CommandState.State.REDO_ENABLED);
            }      
            
        } 
        
        
        return null;
    }
}

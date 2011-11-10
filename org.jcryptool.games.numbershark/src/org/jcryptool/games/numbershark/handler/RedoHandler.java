package org.jcryptool.games.numbershark.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.numbershark.util.CommandState;
import org.jcryptool.games.numbershark.util.CommandStateChanger;
import org.jcryptool.games.numbershark.util.ScoreTableRow;
import org.jcryptool.games.numbershark.views.Messages;
import org.jcryptool.games.numbershark.views.NumberSharkView;


/**
 * This handler redo the last step and disables the taken numbers.
 *
 * @author Daniel Loeffler
 * @version 0.9.5
 */
public class RedoHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof NumberSharkView) {
            NumberSharkView view = ((NumberSharkView) HandlerUtil.getActivePart(event));
            
            Table scoreTableView = view.getTable();
            
            if (view.hasScoreTableRowListNextEntry() == false){
            	return null;
            }
           
            //gets the next ScoreTableRow from actual point of view
            ScoreTableRow scoreTableRow = view.getNextScoreTableRow();
            
            view.addScoreTableRow2ScoreTableView(scoreTableRow);
           
            scoreTableView.setSelection(view.getActualPlayerMove());
            view.setSharkScore(scoreTableRow.getLostScore());                     
            view.setPlayerScore(scoreTableRow.getPoints());
            
            //activate numbers an buttons            
            String redoLostNumbers =  scoreTableRow.getLostNumbers();
            int iterator = redoLostNumbers.lastIndexOf(", "); //$NON-NLS-1$

            // activate numbers and buttons
            while (iterator != -1) {
                int toDisable = Integer.parseInt(redoLostNumbers.substring(iterator + 2, redoLostNumbers.length()));
                redoLostNumbers = redoLostNumbers.substring(0, iterator);               
                view.setStatus(toDisable - 1, false);
                iterator = redoLostNumbers.lastIndexOf(", "); //$NON-NLS-1$
                view.disableNumber(toDisable - 1);
            }

            int toDisable = Integer.parseInt(redoLostNumbers);
            view.setStatus(toDisable - 1, true);
            view.disableNumber(toDisable - 1);

            String takenNumberString = scoreTableRow.getTakenNumbers(); // row.getText(1);

            if (!"-".equals(takenNumberString)) { //$NON-NLS-1$
                String temp = scoreTableRow.getTakenNumbers(); // row.getText(1);
                if (temp.endsWith(Messages.NumberSharkView_0)) {
                    temp = temp.substring(0, temp.indexOf(Messages.NumberSharkView_0));
                }
                int takenNumber = Integer.parseInt(temp);
                view.setStatus(toDisable - 1, true);
                view.disableNumber(takenNumber - 1);              
            }

            view.increasePlayerMove();
            
            //More than 1 Entry in ScorTableRowList set UndoState 2 enable
            if (view.getActualPlayerMove() >= 1){
            	CommandStateChanger commandStateChanger = new CommandStateChanger();
                commandStateChanger.chageCommandState(CommandState.Variable.UNDO_STATE, CommandState.State.UNDO_ENABLED);
            }

            if (view.hasScoreTableRowListNextEntry()){                
                CommandStateChanger commandStateChanger = new CommandStateChanger();
                commandStateChanger.chageCommandState(CommandState.Variable.REDO_STATE, CommandState.State.REDO_ENABLED);
            }
            else{
            	CommandStateChanger commandStateChanger = new CommandStateChanger();
                commandStateChanger.chageCommandState(CommandState.Variable.REDO_STATE, CommandState.State.REDO_DISABLED);
            }
            	
        }
        
        
        
		
        return null;
	}
}

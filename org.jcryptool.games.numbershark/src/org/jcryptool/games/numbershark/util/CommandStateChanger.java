package org.jcryptool.games.numbershark.util;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;
import org.jcryptool.games.numbershark.util.CommandState.State;
import org.jcryptool.games.numbershark.util.CommandState.Variable;


/**
 * The class enables and disable commands
 * 
 * @author Daniel Loeffler
 * @version 0.9.5
 */
public class CommandStateChanger {

	
	public CommandStateChanger(){		
	}
	
	public void chageCommandState(Variable stateVariabe, State state ){
		
        ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
        		.getWorkbench().getActiveWorkbenchWindow().getService(ISourceProviderService.class);
        
        CommandState commandStateService = (CommandState) sourceProviderService
        				.getSourceProvider(CommandState.variableVal(stateVariabe));    
                  
        
        if (state.equals(State.UNDO_ENABLED)){
        	commandStateService.setUndoEnabled();
        }
        else if(state.equals(State.REDO_ENABLED)){
        	commandStateService.setRedoEneabled();
        }
        else if(state.equals(State.UNDO_DISABLED)){
        	commandStateService.setUndoDisabled();
        }
        else if(state.equals(State.REDO_DISABLED)){
        	commandStateService.setRedoDisabled();
        }
        else if (state.equals(State.ENABLED)){
        	commandStateService.setUndoEnabled();
        	commandStateService.setRedoEneabled();
        }
        else if (state.equals(State.DISABLED)){
        	commandStateService.setUndoDisabled();
        	commandStateService.setRedoDisabled();
        }
	}
}

package org.jcryptool.games.numbershark.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;


/**
 * 
 * @author Daniel Löffler
 * @version 0.9.5
 */
public class CommandState extends AbstractSourceProvider    {
    	
	public static String stateVal(State state) {
	    String str ="";
	    switch (state) {	     
	      case UNDO_ENABLED: str = "UNDO_ENABLED";
	      		break;          
	      case UNDO_DISABLED: str = "UNDO_DISABLED";
	      		break;          
	      case REDO_ENABLED: str = "REDO_ENABLED";
	      		break;          
	      case REDO_DISABLED: str = "REDO_DISABLED";
	      		break;          
	      case DISABLED: str = "DISABLED";
	      		break;          
	      case ENABLED: str = "ENABLED";
	      		break;
	    }
	    return str;
	  }	
	
	public static String variableVal(Variable variable) {
	    String str ="";
	    switch (variable) {
	      case UNDO_STATE: str = "org.jcryptool.games.numbershark.commands.undoState";
	              break;
	      case REDO_STATE: str = "org.jcryptool.games.numbershark.commands.redoState";
	              break;
	    }
	    return str;
	  }	
	
	public enum Variable {
		UNDO_STATE, REDO_STATE,
	}
	
	public enum State {
		UNDO_ENABLED, UNDO_DISABLED, REDO_ENABLED, REDO_DISABLED, DISABLED, ENABLED
	 }
		
    private State curState = State.DISABLED;
   
	public void dispose() {
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map getCurrentState() {
		
		Map<String, String> map = new HashMap<String, String>(1);
		if (curState == State.UNDO_ENABLED)
			map.put(variableVal(Variable.UNDO_STATE) , stateVal(State.UNDO_ENABLED));
		else if (curState == State.UNDO_DISABLED)
		   	map.put(variableVal(Variable.UNDO_STATE), stateVal(State.UNDO_DISABLED));
		else if (curState == State.REDO_ENABLED)
		   	map.put(variableVal(Variable.REDO_STATE), stateVal(State.REDO_ENABLED));
		else if (curState == State.REDO_DISABLED)
		   	map.put(variableVal(Variable.REDO_STATE), stateVal(State.REDO_DISABLED));
		else if (curState == State.DISABLED) {
		   	map.put(variableVal(Variable.UNDO_STATE), stateVal(State.UNDO_DISABLED));
			map.put(variableVal(Variable.REDO_STATE), stateVal(State.REDO_DISABLED));
			}
		else if (curState == State.ENABLED) {
		   	map.put(variableVal(Variable.UNDO_STATE), stateVal(State.UNDO_ENABLED));
			map.put(variableVal(Variable.REDO_STATE), stateVal(State.REDO_ENABLED));
			}
	 return map;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { variableVal(Variable.UNDO_STATE), variableVal(Variable.REDO_STATE) };
	}
	
	public void setUndoEnabled() {		
		fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.UNDO_STATE), stateVal(State.UNDO_ENABLED));
	}
	
	public void setUndoDisabled() {		
		fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.UNDO_STATE), stateVal(State.UNDO_DISABLED));
	}
	
	public void setRedoEnabled() {
		fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.REDO_STATE), stateVal(State.REDO_ENABLED));
	}

	public void setRedoDisabled() {
		fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.REDO_STATE), stateVal(State.REDO_DISABLED));
	}
	
	public void setDisabled() {
		fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.UNDO_STATE), stateVal(State.DISABLED));
		fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.REDO_STATE), stateVal(State.DISABLED));
	}
	
}   



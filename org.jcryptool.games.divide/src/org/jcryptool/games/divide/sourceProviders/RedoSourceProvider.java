package org.jcryptool.games.divide.sourceProviders;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

public class RedoSourceProvider extends AbstractSourceProvider {

	public static final String REDO_COMMAND_STATE = "org.jcryptool.games.divide.redo";
	
	// instance vars
	private final String ENABLED = "enabled";
	private final String DISABLED = "disabled";
	private boolean isEnabled;
	
	// constructor
	public RedoSourceProvider() {
		super();
		isEnabled = false;
	}
	
	// methods
	@Override
	public void dispose() {
	
	}

	@Override
	public Map<String, String> getCurrentState() {
		Map<String, String> currentStateMap = new HashMap<String, String>(1);
		String currentState = isEnabled ? ENABLED : DISABLED;
		currentStateMap.put(REDO_COMMAND_STATE, currentState);
		
		return currentStateMap;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] {REDO_COMMAND_STATE}; 
	}
	
	public void setState(boolean isEnabled) {
		this.isEnabled = isEnabled;
		String currentState = isEnabled ? ENABLED : DISABLED;
		fireSourceChanged(ISources.WORKBENCH, REDO_COMMAND_STATE, currentState);
	}
}

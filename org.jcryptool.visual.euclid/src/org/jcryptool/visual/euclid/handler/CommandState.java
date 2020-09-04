package org.jcryptool.visual.euclid.handler;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

/**
 * 
 * @author Thorben Groos
 *
 */
public class CommandState extends AbstractSourceProvider {
	
	public enum Variable {
		TEX, CSV
	}
	
	public enum State {
		ENABLED, DISABLED
	}
	
	private State curState = State.DISABLED;

	@Override
	public void dispose() {

	}

	@Override
	public Map<String, String> getCurrentState() {
		Map<String, String> map = new HashMap<String, String>(1);
		if (curState == State.DISABLED) {
			map.put(variableVal(Variable.CSV), stateVal(State.DISABLED));
			map.put(variableVal(Variable.TEX), stateVal(State.DISABLED));
		} else if (curState == State.ENABLED) {
			map.put(variableVal(Variable.CSV), stateVal(State.ENABLED));
			map.put(variableVal(Variable.TEX), stateVal(State.ENABLED));
		}
		return map;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] {variableVal(Variable.CSV), variableVal(Variable.TEX)};
	}
	
	public static String variableVal(Variable variable) {
		String str = "";
		switch (variable) {
		case CSV:
			str = "org.jcryptool.visual.euclid.commands.CsvState";
			break;
		case TEX:
			str = "org.jcryptool.visual.euclid.commands.TexState";
			break;
		default:
			break;
		}
		return str;
	}
	
	public static String stateVal(State state) {
		String str = "";
		switch (state) {
		case DISABLED:
			str = "DISABLED";
			break;
		case ENABLED:
			str = "ENABLED";
			break;
		default:
			break;
		}
		return str;
	}
	
	public void setCSVExportEnabled() {
		fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.CSV), stateVal(State.ENABLED));
	}
	
	public void setTEXExportEnabled() {
		fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.TEX), stateVal(State.ENABLED));
	}
	
	public void setCSVExportDisabled() {
		fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.CSV), stateVal(State.DISABLED));
	}
	
	public void setTEXExportDisabled() {
		fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.TEX), stateVal(State.DISABLED));
	}

}

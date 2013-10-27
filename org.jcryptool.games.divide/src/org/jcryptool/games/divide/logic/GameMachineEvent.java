package org.jcryptool.games.divide.logic;

import java.util.List;


public class GameMachineEvent {

	// instance vars
	private GameMachineNotifyEvent eventType;
	private List<GameState> state;
	
	// constructor
	public GameMachineEvent(GameMachineNotifyEvent eventType, List<GameState> state) {
		super();
		this.eventType = eventType;
		this.state = state;
	}

	// methods
	public GameMachineNotifyEvent getEventType() {
		return eventType;
	}

	public List<GameState> getStateList() {
		return state;
	}
}

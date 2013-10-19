package org.jcryptool.games.divide.logic;


public class GameMachineEvent {

	// instance vars
	private GameMachineNotifyEvent eventType;
	private GameState state;
	
	// constructor
	public GameMachineEvent(GameMachineNotifyEvent eventType, GameState state) {
		super();
		this.eventType = eventType;
		this.state = state;
	}

	// methods
	public GameMachineNotifyEvent getEventType() {
		return eventType;
	}

	public GameState getState() {
		return state;
	}
}

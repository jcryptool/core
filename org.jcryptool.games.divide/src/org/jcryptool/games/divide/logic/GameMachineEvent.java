package org.jcryptool.games.divide.logic;

import java.util.List;

public class GameMachineEvent {

	// instance vars
	private GameMachineNotifyEvent eventType;
	private List<Integer> listOfNumbers;
	private List<Integer> eliminatedNumbers;
	private IPlayer currentPlayer;
	private IPlayer nextPlayer;
	private int turn;
	private int chosenNumber;
	
	
	// constructor
	public GameMachineEvent(GameMachineNotifyEvent eventType) {
		super();
		this.eventType = eventType;
	}
	
	// methods
	public void setData(List<Integer> listOfNumbers, IPlayer currentPlayer, IPlayer nextPlayer, int turn, int chosenNumber, List<Integer> eliminatedNumbers) {
		this.listOfNumbers = listOfNumbers;
		this.currentPlayer = currentPlayer;
		this.nextPlayer = nextPlayer;
		this.turn = turn;
		this.chosenNumber = chosenNumber;
		this.eliminatedNumbers = eliminatedNumbers;
	}
	
	public GameMachineNotifyEvent getEventType() {
		return eventType;
	}
	
	public List<Integer> getListOfNumbers() {
		return listOfNumbers;
	}
	
	public IPlayer getCurrentPlayer() {
		return currentPlayer;
	}
	
	public IPlayer getNextPlayer() {
		return nextPlayer;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public int getChosenNumber() {
		return chosenNumber;
	}
	
	public List<Integer> getEliminatedNumbers() {
		return eliminatedNumbers;
	}
}

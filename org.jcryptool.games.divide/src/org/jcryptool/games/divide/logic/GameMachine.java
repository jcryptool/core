package org.jcryptool.games.divide.logic;

import java.util.List;
import java.util.Observable;

public class GameMachine extends Observable {

	// instance vars
	private List<IPlayer> players;
	private List<Integer> listOfNumbers;
	List<Integer> oldListOfNumbers;
	private IPlayer playerLastRound;
	private IPlayer playerPresentRound;
	private IMathEngine mathEngine;
	private int startingNumber;
	private int turn;
	private int chosenNumber;
	
	// constructor
	public GameMachine(List<IPlayer> players, IMathEngine engine, int n) {
		super();
		this.players = players;
		mathEngine = engine;
		startingNumber = n;
	}

	public void start(IPlayer starter) {
		listOfNumbers = mathEngine.getDivider(startingNumber);
		playerPresentRound = starter;
		GameMachineEvent startEvent = new GameMachineEvent(GameMachineNotifyEvent.START_EVENT);
		startEvent.setData(listOfNumbers, null, playerPresentRound, turn, 0, null);
		notifyObservers(startEvent);
	}
	
	public void nextTurn(int chosenNumber) {
		this.chosenNumber = chosenNumber;
		turn++;
		// save old listOfNumbers to calculate difference
		oldListOfNumbers = listOfNumbers;
		
		if (!isSelectionValid(chosenNumber)) {
			throw new IllegalStateException("Not a valid selection");
		}
		listOfNumbers = mathEngine.dropMultiples(listOfNumbers, chosenNumber);
		oldListOfNumbers.removeAll(listOfNumbers);
		
		// set players
		playerLastRound = playerPresentRound;
		int nextPlayerIndex = (players.indexOf(playerPresentRound) + 1) % players.size();
		playerPresentRound = players.get(nextPlayerIndex);
		
		if (chosenNumber == 1) {
			sendEndEvent();
			return;
		}
		// notify observers
		GameMachineEvent nextRound = new GameMachineEvent(GameMachineNotifyEvent.NEXT_ROUND_EVENT);
		nextRound.setData(listOfNumbers, playerLastRound, playerPresentRound, turn, chosenNumber, oldListOfNumbers);
		notifyObservers(nextRound);
	}
	
	private void sendEndEvent() {
		GameMachineEvent gameOver = new GameMachineEvent(GameMachineNotifyEvent.END_EVENT);
		gameOver.setData(listOfNumbers, playerLastRound, playerPresentRound, turn, chosenNumber, oldListOfNumbers);
		notifyObservers(gameOver);
	}
	
	private boolean isSelectionValid(int chosenNumber) {
		if (!listOfNumbers.contains(chosenNumber)) {
			return false;
		}
		return true;
	}
	
	private void notifyObservers(GameMachineEvent event) {
		setChanged();
		super.notifyObservers(event);
	}
	
	public List<IPlayer> getPlayers() {
		return players;
	}
}

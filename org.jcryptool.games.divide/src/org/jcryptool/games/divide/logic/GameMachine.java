package org.jcryptool.games.divide.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class GameMachine extends Observable {

	// instance vars
	private IMathEngine mathEngine;
	private List<IPlayer> players;
	private int startingNumber;
	private List<GameState> stateHistory;
	private int currentState;
	private boolean hasComputerPlayer;
	private IPlayer startingPlayer;
	
	// constructor
	public GameMachine(IMathEngine mathEngine, List<IPlayer> players, int n) {
		super();
		this.mathEngine = mathEngine;
		this.players = players;
		currentState = -1;
		stateHistory = new LinkedList<GameState>();
		startingNumber = n;
		hasComputerPlayer = false;
		for (IPlayer player : players) {
			if (!player.isHuman()) {
				hasComputerPlayer = true;
				break;
			}
		}
	}

	public void start(IPlayer starter) {
		startingPlayer = starter;
		// set pregame state
		GameState state = new GameState();
		state.setTurn(0);
		state.setListOfNumbers(mathEngine.getDivider(startingNumber));
		state.setPlayerLastRound(null);
		state.setPlayerCurrentRound(starter);
		state.setChosenNumber(0);
		state.setEliminatedNumbers(null);
		addNewState(state);
		
		// notify observer
		GameMachineEvent startEvent = new GameMachineEvent(GameMachineNotifyEvent.START_EVENT, getCurrentState());
		notifyObservers(startEvent);
	}
	
	public void nextTurn(int chosenNumber) {
		if (!isSelectionValid(chosenNumber)) {
			throw new IllegalStateException("Not a valid selection");
		}
		
		/*
		 * if we are not at the end of the linked list we have to cut the
		 * last elements because else we could redo actions which should not
		 * be valid anymore. e.g. we did an undo on picking number 3. if the next player
		 * picks the 3 then it shouldnt be possible to do a redo which would lead to
		 * replaying the 3 which is not valid anymore.
		 */
		while (getCurrentState().getTurn() != stateHistory.size() -1) {
				stateHistory.remove(stateHistory.size() - 1);
		}
		
		// determine new state
		GameState state = new GameState();
		state.setTurn(getCurrentState().getTurn() + 1);
		state.setChosenNumber(chosenNumber);
		// set new list of Numbers by dropping all multiples of chosenNumber from current list of numbers
		state.setListOfNumbers(mathEngine.dropMultiples(getCurrentState().getListOfNumbers(), chosenNumber));
		
		// determine eliminated numbers (dont remove from old state)
		List<Integer> eliminatedNumbers = new ArrayList<Integer>();
		for (Integer num : getCurrentState().getListOfNumbers()) {
			eliminatedNumbers.add(num);
		}
		eliminatedNumbers.removeAll(state.getListOfNumbers());
		state.setEliminatedNumbers(eliminatedNumbers);

		// set players
		state.setPlayerLastRound(getCurrentState().getPlayerCurrentRound());
		int nextPlayerIndex = (players.indexOf(state.getPlayerLastRound()) + 1) % players.size();
		state.setPlayerCurrentRound(players.get(nextPlayerIndex));
		addNewState(state);
		
		if (chosenNumber == 1) {
			sendEndEvent();
			return;
		}
		
		// notify observers
		GameMachineEvent nextRound = new GameMachineEvent(GameMachineNotifyEvent.NEXT_ROUND_EVENT, getCurrentState());
		notifyObservers(nextRound);
	}
	
	public void undo() {
		if (hasComputerPlayer) {
			currentState -= 2;
		} else {
			currentState--;
		}
		GameMachineEvent undo = new GameMachineEvent(GameMachineNotifyEvent.UNDO_EVENT, getCurrentState());
		notifyObservers(undo);
	}
	
	public void redo() {
		//if (getCurrentState().getTurn() < stateHistory.size() - 1) {
			currentState++;
			GameMachineEvent redo = new GameMachineEvent(GameMachineNotifyEvent.REDO_EVENT, getCurrentState());
			notifyObservers(redo);
		//}
	}
	
	private void addNewState(GameState state) {
		/*
		 * set only replaces if there is an element at that index.
		 * add would not replace at index instead moves element in between
		 */
		currentState++;
		if (stateHistory.size() > currentState) {
			stateHistory.set(currentState, state);
		} else {
			stateHistory.add(currentState, state);
		}
	}
	
	private GameState getCurrentState() {
		return stateHistory.get(currentState);
	}
	
	private void sendEndEvent() {
		// notify observers
		GameMachineEvent gameOver = new GameMachineEvent(GameMachineNotifyEvent.END_EVENT, getCurrentState());
		notifyObservers(gameOver);
	}
	
	private boolean isSelectionValid(int chosenNumber) {
		if (!getCurrentState().getListOfNumbers().contains(chosenNumber)) {
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

	protected void setPlayers(List<IPlayer> players) {
		this.players = players;
	}
	
	public int getStateHistorySize() {
		return stateHistory.size();
	}
	
	public boolean hasComputerPlayer() {
		return hasComputerPlayer;
	}
	
	public IPlayer getStartingPlayer() {
		return startingPlayer;
	}
}

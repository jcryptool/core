package org.jcryptool.games.divide.logic;

import java.util.List;

public class GameState {

	// instance vars
	private List<Integer> listOfNumbers;
	private List<Integer> eliminatedNumbers;

	private IPlayer playerLastRound;
	private IPlayer playerCurrentRound;
	private int turn;
	private int chosenNumber;
	
	// constructor
	public GameState() {
		super();
	}
	
	// methods
	public List<Integer> getListOfNumbers() {
		return listOfNumbers;
	}

	protected void setListOfNumbers(List<Integer> listOfNumbers) {
		this.listOfNumbers = listOfNumbers;
	}

	public List<Integer> getEliminatedNumbers() {
		return eliminatedNumbers;
	}

	protected void setEliminatedNumbers(List<Integer> eliminatedNumbers) {
		this.eliminatedNumbers = eliminatedNumbers;
	}

	public IPlayer getPlayerCurrentRound() {
		return playerCurrentRound;
	}

	protected void setPlayerCurrentRound(IPlayer playerCurrentRound) {
		this.playerCurrentRound = playerCurrentRound;
	}

	public IPlayer getPlayerLastRound() {
		return playerLastRound;
	}

	protected void setPlayerLastRound(IPlayer playerLastRound) {
		this.playerLastRound = playerLastRound;
	}
	
	public int getTurn() {
		return turn;
	}

	protected void setTurn(int turn) {
		this.turn = turn;
	}

	public int getChosenNumber() {
		return chosenNumber;
	}

	protected void setChosenNumber(int chosenNumber) {
		this.chosenNumber = chosenNumber;
	}
}

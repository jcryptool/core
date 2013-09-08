package org.jcryptool.games.divide.logic;

import java.util.List;

public class HumanPlayer implements IPlayer {

	// instance vars
	private String name;
	private boolean isHuman;
	
	// constructor
	public HumanPlayer(String name) {
		super();
		this.name = name;
		isHuman = true;
	}
	
	// methods
	@Override
	public int chooseNumber(List<Integer> n) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isHuman() {
		return isHuman;
	}
}

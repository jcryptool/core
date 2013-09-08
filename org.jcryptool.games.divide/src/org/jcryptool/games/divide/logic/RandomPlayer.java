package org.jcryptool.games.divide.logic;

import java.util.List;
import java.util.Random;

public class RandomPlayer implements IPlayer {

	// instance vars
	private String name;
	private boolean isHuman;
	
	// constructor
	public RandomPlayer(String name) {
		super();
		this.name = name;
		isHuman = false;
	}
	
	// methods
	@Override
	public int chooseNumber(List<Integer> n) {
		Random r = new Random();
		int random;
		do {
			random = n.get(r.nextInt(n.size()));
		} while (random == 1 && n.size() > 1);
			
		return random;
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

package org.jcryptool.games.divide.logic;

import java.util.List;

public interface IPlayer {

	int chooseNumber(List<Integer> n) throws UnsupportedOperationException;
	void setName(String name);
	String getName();
	boolean isHuman();
}

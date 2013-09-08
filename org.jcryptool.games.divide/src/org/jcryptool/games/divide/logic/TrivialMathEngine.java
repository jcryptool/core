package org.jcryptool.games.divide.logic;

import java.util.ArrayList;
import java.util.List;

public class TrivialMathEngine implements IMathEngine {

	// instance vars
	
	// constructor
	public TrivialMathEngine() {
		super();
	}

	// methods
	@Override
	public List<Integer> getDivider(int x) {
		List<Integer> divider = new ArrayList<Integer>();
		for (int i = 1; i <= x; i++) {
			if (x % i == 0) {
				divider.add(i);
			}
		}
		return divider;
	}

	@Override
	public List<Integer> dropMultiples(List<Integer> oldList, int x) {
		List<Integer> updatedList = new ArrayList<Integer>();
		for (int i = 0; i < oldList.size(); i++) {
			if (oldList.get(i) % x != 0) {
				updatedList.add(oldList.get(i));
			}
		}
		return updatedList;
	}
}

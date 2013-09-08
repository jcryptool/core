package org.jcryptool.games.divide.logic;

import java.util.List;

public interface IMathEngine {

	List<Integer> getDivider(int x);
	List<Integer> dropMultiples(List<Integer> n, int x);

}

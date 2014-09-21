package org.jcryptool.games.divide.logic;

import java.util.List;

public interface IStrategy {

    String getName();

    int chooseNumber(List<Integer> n);
}

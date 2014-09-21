package org.jcryptool.games.divide.logic;

import java.util.List;

public class HighestStrategy implements IStrategy {

    // instance vars
    private String name;

    // constructor
    public HighestStrategy() {
        super();
        this.name = "Highest";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int chooseNumber(List<Integer> n) {
        return n.get(n.size() - 1);
    }
}

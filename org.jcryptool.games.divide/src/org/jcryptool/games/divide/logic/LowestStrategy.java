package org.jcryptool.games.divide.logic;

import java.util.List;

public class LowestStrategy implements IStrategy {

    // instance vars
    private String name;

    // constructor
    public LowestStrategy() {
        super();
        this.name = "Lowest";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int chooseNumber(List<Integer> n) {
        if (n.size() > 1) {
            return n.get(1);
        } else {
            return n.get(0);
        }
    }
}

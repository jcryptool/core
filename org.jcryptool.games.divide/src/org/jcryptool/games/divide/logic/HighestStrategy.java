package org.jcryptool.games.divide.logic;

import java.util.List;

import org.jcryptool.games.divide.views.Messages;

public class HighestStrategy implements IStrategy {

    // instance vars
    private String name;

    // constructor
    public HighestStrategy() {
        super();
        this.name = Messages.DivideView_25;
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

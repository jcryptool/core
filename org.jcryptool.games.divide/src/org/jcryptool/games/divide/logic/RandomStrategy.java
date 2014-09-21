package org.jcryptool.games.divide.logic;

import java.util.List;
import java.util.Random;

public class RandomStrategy implements IStrategy {

    // instance vars
    private String name;

    // constructor
    public RandomStrategy() {
        super();
        this.name = "Random";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int chooseNumber(List<Integer> n) {
        Random r = new Random();
        int random;
        do {
            random = n.get(r.nextInt(n.size()));
        } while (random == 1 && n.size() > 1);

        return random;
    }

}

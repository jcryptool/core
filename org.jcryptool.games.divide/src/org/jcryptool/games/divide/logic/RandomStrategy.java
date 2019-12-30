// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.logic;

import java.util.List;
import java.util.Random;

import org.jcryptool.games.divide.views.Messages;

public class RandomStrategy implements IStrategy {

    // instance vars
    private String name;

    // constructor
    public RandomStrategy() {
        super();
        this.name = Messages.DivideView_24;
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

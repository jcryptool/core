//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.games.numbershark.strategies;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jcryptool.games.numbershark.views.NumberField;

public class Schu1Strategy {

    private String[][] output = null;
    private int stoppedAt;
    private int von = 2;

    public Schu1Strategy(int von, int bis, IProgressMonitor monitor) {
        this.von = von;

        output = new String[bis + 1 - von][5];
        for (int gameSize = von; gameSize <= bis; gameSize++) {
            if (!monitor.isCanceled()) {
                long start = System.currentTimeMillis();
                calculate(gameSize);

                long stop = System.currentTimeMillis();
                int t = (int) (stop - start);
                String zeit = t < 1000 ? t + "ms" : (t / 1000) + "s";

                output[gameSize - von][4] = zeit;
                stoppedAt = gameSize;
            }
        }
    }

    private void calculate(int gameSize) {
        NumberField numberField = new NumberField(gameSize);
        int playerPoints = 0;
        String takeSeq = "";
        while (numberField.getActiveNumbers() > 0) {
            ArrayList<Integer> chooseableNumbers = new ArrayList<Integer>();
            java.util.Random random = new java.util.Random();
            int borderValue = random.nextInt(gameSize / 2 + 1);
            int k = -1;
            int numberOfDivs = gameSize;
            for (int i = gameSize; i > borderValue; i--) {
                int divisors = numberField.getActiveDivisors(i).size();
                if (numberField.isActiveNumber(i) && divisors < numberOfDivs && divisors > 0) {
                    numberOfDivs = numberField.getActiveDivisors(i).size();
                    k = i;
                    if (numberOfDivs == 1) {
                        break;
                    }
                }
            }
            if (k != -1) {
                chooseableNumbers.add(k);
            }
            if (gameSize > 3) {
                borderValue = random.nextInt(gameSize / 2 - 1) + gameSize / 2 + 1;
                k = -1;
                for (int i = gameSize; i > borderValue; i--) {
                    if (numberField.isActiveNumber(i) && numberField.getActiveDivisors(i).size() == 1) {
                        k = i;
                        break;
                    }
                }
                if (k != -1) {
                    chooseableNumbers.add(k);
                }
            }

            if (chooseableNumbers.size() > 0) {
                int toTake = chooseableNumbers.get(random.nextInt(chooseableNumbers.size()));
                numberField.take(toTake);
                playerPoints += toTake;

                takeSeq += "" + toTake + ",";
            }
        }

        takeSeq = takeSeq.substring(0, takeSeq.length() - 1);
        int sharkPoints = gameSize * (gameSize + 1) / 2 - playerPoints;
        output[gameSize - von][0] = "" + gameSize;
        output[gameSize - von][1] = "" + playerPoints;
        output[gameSize - von][2] = "" + sharkPoints;
        output[gameSize - von][3] = takeSeq;
    }

    public int getStoppedAt() {
        return stoppedAt;
    }

    public String[][] getOutput() {
        return output;
    }

}

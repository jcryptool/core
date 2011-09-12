// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.numbershark.handler;

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.numbershark.views.Number;
import org.jcryptool.games.numbershark.views.NumberSharkView;

/**
 * This handler creates a shark meal.
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class SharkMealHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof NumberSharkView) {
            NumberSharkView view = ((NumberSharkView) HandlerUtil.getActivePart(event));

            ArrayList<Integer> sharkMealList = new ArrayList<Integer>();
            int[] lostNumbers;
            int gameNMax = view.getNumberOfFields();
            Number[] numNum = view.getNumNum();
            boolean[] activeNumbers = view.getActiveNumbers();

            // calculate numbers to be eaten by the shark
            for (int i = gameNMax; i > gameNMax / 2; i--) {
                if (activeNumbers[i - 1]) {
                    int counter = 0;
                    boolean stop = false;
                    for (int j = 0; j < numNum[i - 1].getDivisors().size(); j++) {
                        int n = numNum[i - 1].getDivisors().get(j);
                        if (activeNumbers[n - 1]) {
                            counter++;
                            if (counter > 0) {
                                stop = true;
                                break;
                            }
                        }
                    }
                    if (!stop) {
                        sharkMealList.add(i);
                        activeNumbers[i - 1] = false;
                        numNum[i - 1].setEnabled(false);

                        int tabFolderIndex = view.getSelectedTabFolderIndex();

                        if (tabFolderIndex * 40 < i && i < (tabFolderIndex + 1) * 40 + 1) {
                            view.disableNumber(i - 1);
                        }
                    }
                }
            }

            if (sharkMealList.size() > 0) {
                lostNumbers = new int[sharkMealList.size()];
                for (int i = 0; i < lostNumbers.length; i++) {
                    lostNumbers[i] = sharkMealList.get(i);
                }

                view.addMoveToTable(0, lostNumbers);
            }
        }

        return null;
    }
}

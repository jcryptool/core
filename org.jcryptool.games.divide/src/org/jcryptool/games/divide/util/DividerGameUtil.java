// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool team and contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.util;

import java.util.List;

public class DividerGameUtil {

    public static String createStringFromIntArray(List<Integer> listOfNumbers) {
        String ret = ("[");
        if (listOfNumbers != null) {
            for (int i = 0; i < listOfNumbers.size(); i++) {
                if (i != listOfNumbers.size() - 1) {
                    ret += (listOfNumbers.get(i));
                    ret += ", ";
                } else {
                    ret += listOfNumbers.get(i);
                }
            }
            ret += "]";
        }
        return ret;
    }

    /*
     * public static void dumpNotifications(GameMachineEvent event) { GameState state =
     * event.getStateList().get(0); String listOfNumbers =
     * DividerGameUtil.createStringFromIntArray(state.getListOfNumbers()); String eliminatedNumbers
     * = DividerGameUtil.createStringFromIntArray(state.getEliminatedNumbers());
     * System.out.println("======================================");
     * System.out.println("Event Type: " + event.getEventType().name()); System.out.println("Turn: "
     * + state.getTurn()); System.out.println("Chosen Number: " + state.getChosenNumber());
     * System.out.println("Player current: " + state.getPlayerCurrentRound().getName()); if
     * (state.getPlayerLastRound() != null) { System.out.println("Player last: " +
     * state.getPlayerLastRound().getName()); } else { System.out.println("Player last: NULL"); }
     * System.out.println("List of numbers: " + listOfNumbers); if (state.getEliminatedNumbers() !=
     * null) { System.out.println("Eliminated numbers: " + eliminatedNumbers); } else {
     * System.out.println("Eliminated numbers: NULL"); }
     * System.out.println("======================================"); System.out.println(""); }
     */

}

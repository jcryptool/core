// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool team and contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class GameMachine extends Observable {

    // instance vars
    private IMathEngine mathEngine;
    private List<IPlayer> players;
    private int startingNumber;
    private List<GameState> stateHistory;
    private int currentState;
    private boolean hasComputerPlayer;
    private IPlayer startingPlayer;

    // constructor
    public GameMachine(IMathEngine mathEngine, List<IPlayer> players, int n) {
        super();
        this.mathEngine = mathEngine;
        this.players = players;
        currentState = -1;
        stateHistory = new LinkedList<GameState>();
        startingNumber = n;
        hasComputerPlayer = false;
        for (IPlayer player : players) {
            if (!player.isHuman()) {
                hasComputerPlayer = true;
                break;
            }
        }
    }

    // methods
    public void start(IPlayer starter) {
        startingPlayer = starter;
        // initialize
        GameState state = new GameState();
        state.setTurn(0);
        state.setListOfNumbers(mathEngine.getDivider(startingNumber));
        state.setPlayerLastRound(null);
        state.setPlayerCurrentRound(starter);
        state.setChosenNumber(0);
        state.setEliminatedNumbers(null);
        addNewState(state);

        // notify observers
        List<GameState> notificationState = new LinkedList<GameState>();
        notificationState.add(getCurrentState());
        GameMachineEvent startEvent = new GameMachineEvent(GameMachineNotifyEvent.START_EVENT, notificationState);
        notifyObservers(startEvent);
    }

    public void nextTurn(int chosenNumber) {
        if (!isSelectionValid(chosenNumber)) {
            throw new IllegalStateException("Not a valid selection");
        }

        /*
         * if we are not at the end of the linked list we have to cut the last elements because else
         * we could redo actions which should not be valid anymore. e.g. we did an undo on picking
         * number 3. if the next player picks the 3 then it shouldnt be possible to do a redo which
         * would lead to replaying the 3 which is not valid anymore.
         */
        while (getCurrentState().getTurn() != stateHistory.size() - 1) {
            stateHistory.remove(stateHistory.size() - 1);
        }

        // determine new state
        GameState state = new GameState();
        state.setTurn(getCurrentState().getTurn() + 1);
        state.setChosenNumber(chosenNumber);
        // set new list of Numbers by dropping all multiples of chosenNumber from current list of
        // numbers
        state.setListOfNumbers(mathEngine.dropMultiples(getCurrentState().getListOfNumbers(), chosenNumber));

        // determine eliminated numbers (dont remove from old state)
        List<Integer> eliminatedNumbers = new ArrayList<Integer>();
        for (Integer num : getCurrentState().getListOfNumbers()) {
            eliminatedNumbers.add(num);
        }
        eliminatedNumbers.removeAll(state.getListOfNumbers());
        state.setEliminatedNumbers(eliminatedNumbers);

        // set players
        state.setPlayerLastRound(getCurrentState().getPlayerCurrentRound());
        int nextPlayerIndex = (players.indexOf(state.getPlayerLastRound()) + 1) % players.size();
        state.setPlayerCurrentRound(players.get(nextPlayerIndex));
        addNewState(state);

        // notify observers
        List<GameState> notificationState = new LinkedList<GameState>();
        notificationState.add(getCurrentState());
        if (chosenNumber == 1) {
            GameMachineEvent gameOver = new GameMachineEvent(GameMachineNotifyEvent.END_EVENT, notificationState);
            notifyObservers(gameOver);
        } else {
            GameMachineEvent nextRound = new GameMachineEvent(GameMachineNotifyEvent.NEXT_ROUND_EVENT,
                    notificationState);
            notifyObservers(nextRound);
        }
    }

    public void undo() {
        if (hasComputerPlayer) {
            currentState -= 2;
        } else {
            currentState--;
        }
        // notify observers
        List<GameState> notificationState = new LinkedList<GameState>();
        notificationState.add(getCurrentState());
        GameMachineEvent undo = new GameMachineEvent(GameMachineNotifyEvent.UNDO_EVENT, notificationState);
        notifyObservers(undo);
    }

    public void redo() {
        List<GameState> notificationState = new LinkedList<GameState>();
        if (hasComputerPlayer) {
            for (int i = 0; i < 2; i++) {
                notificationState.add(stateHistory.get(++currentState));
            }
        } else {
            notificationState.add(stateHistory.get(++currentState));
        }

        GameMachineEvent redo = new GameMachineEvent(GameMachineNotifyEvent.REDO_EVENT, notificationState);
        notifyObservers(redo);
    }

    private void addNewState(GameState state) {
        /*
         * set only replaces if there is an element at that index. add would not replace at index
         * instead moves element in between
         */
        currentState++;
        if (stateHistory.size() > currentState) {
            stateHistory.set(currentState, state);
        } else {
            stateHistory.add(currentState, state);
        }
    }

    private GameState getCurrentState() {
        return stateHistory.get(currentState);
    }

    private boolean isSelectionValid(int chosenNumber) {
        if (!getCurrentState().getListOfNumbers().contains(chosenNumber)) {
            return false;
        }
        return true;
    }

    private void notifyObservers(GameMachineEvent event) {
        setChanged();
        // DividerGameUtil.dumpNotifications(event);
        super.notifyObservers(event);
    }

    public List<IPlayer> getPlayers() {
        return players;
    }

    protected void setPlayers(List<IPlayer> players) {
        this.players = players;
    }

    public int getStateHistorySize() {
        return stateHistory.size();
    }

    public boolean hasComputerPlayer() {
        return hasComputerPlayer;
    }

    public IPlayer getStartingPlayer() {
        return startingPlayer;
    }
}

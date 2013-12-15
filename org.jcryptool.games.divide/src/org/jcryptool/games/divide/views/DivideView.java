// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool team and contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.games.divide.DividePlugin;
import org.jcryptool.games.divide.dialogs.ChoosePlayerDialog;
import org.jcryptool.games.divide.logic.GameMachine;
import org.jcryptool.games.divide.logic.GameMachineEvent;
import org.jcryptool.games.divide.logic.GameState;
import org.jcryptool.games.divide.logic.HumanPlayer;
import org.jcryptool.games.divide.logic.IMathEngine;
import org.jcryptool.games.divide.logic.IPlayer;
import org.jcryptool.games.divide.logic.RandomPlayer;
import org.jcryptool.games.divide.logic.TrivialMathEngine;
import org.jcryptool.games.divide.sourceProviders.MenuBarActivation;
import org.jcryptool.games.divide.sourceProviders.NewGameStateSourceProvider;
import org.jcryptool.games.divide.util.DividerGameUtil;

public class DivideView extends ViewPart implements Observer {

    // instance vars
    private Composite parent;
    private SashForm sashForm;
    private Composite upperContent;
    private Group optionsGroup;
    private Group descriptionGroup;
    private Button buttonStartGame;
    private Composite playingField;
    private Composite lowerContent;
    private Group gameInformationGroup;
    private CLabel[] labelPlayerActive;
    private Button button1pVsComp;
    private Button button1pVs2P;
    private Group detailedGameInformationGroup;
    private Table scoreTable;
    private CLabel[] labels;
    private GameMachine gameMachine;
    private Text textStartValue;

    // constructor
    public DivideView() {
        super();
    }

    // methods
    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;

        sashForm = new SashForm(parent, SWT.VERTICAL);
        sashForm.setLayout(new GridLayout(1, false));
        sashForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        upperContent = new Composite(sashForm, SWT.NONE);
        upperContent.setLayout(new GridLayout(1, false));
        upperContent.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        // description
        descriptionGroup = new Group(upperContent, SWT.NONE);
        descriptionGroup.setText(Messages.DivideView_19);
        descriptionGroup.setLayout(new RowLayout());
        descriptionGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        StyledText description = new StyledText(descriptionGroup, SWT.READ_ONLY);
        description.setText(Messages.DivideView_20);

        // options pane

        // gaps
        RowData smallGapLayout = new RowData(1, SWT.DEFAULT);
        // RowData largeGapLayout = new RowData(15, SWT.DEFAULT);
        RowData hugeGapLayout = new RowData(50, SWT.DEFAULT);

        optionsGroup = new Group(upperContent, SWT.NONE);
        optionsGroup.setText(Messages.DivideView_0);
        optionsGroup.setLayout(new RowLayout());
        optionsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        RowData fieldData = new RowData(SWT.DEFAULT, SWT.DEFAULT);

        Label gameType = new Label(optionsGroup, SWT.LEFT);
        gameType.setText(Messages.DivideView_1);
        gameType.setFont(FontService.getNormalBoldFont());
        Label gap1 = new Label(optionsGroup, SWT.LEFT);
        gap1.setLayoutData(smallGapLayout);
        button1pVsComp = new Button(optionsGroup, SWT.RADIO);
        button1pVsComp.setLayoutData(fieldData);
        button1pVsComp.setText(Messages.DivideView_2);
        button1pVsComp.setFont(FontService.getNormalFont());
        button1pVsComp.setSelection(true);
        Label gap2 = new Label(optionsGroup, SWT.LEFT);
        gap2.setLayoutData(smallGapLayout);
        button1pVs2P = new Button(optionsGroup, SWT.RADIO);
        button1pVs2P.setLayoutData(fieldData);
        button1pVs2P.setText(Messages.DivideView_3);
        button1pVs2P.setFont(FontService.getNormalFont());
        button1pVs2P.setSelection(false);
        Label gap3 = new Label(optionsGroup, SWT.LEFT);
        gap3.setLayoutData(hugeGapLayout);

        Label labelStartValue = new Label(optionsGroup, SWT.LEFT);
        labelStartValue.setText(Messages.DivideView_4);
        labelStartValue.setFont(FontService.getNormalBoldFont());
        Label gap4 = new Label(optionsGroup, SWT.LEFT);
        gap4.setLayoutData(smallGapLayout);
        textStartValue = new Text(optionsGroup, SWT.RIGHT);
        textStartValue.setLayoutData(new RowData(60, SWT.DEFAULT));
        textStartValue.setTextLimit(9);
        textStartValue.setText(Messages.DivideView_18);
        textStartValue.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(VerifyEvent e) {
                String text = e.text;
                if (!text.isEmpty() && !Character.isDigit(text.charAt(0))) {
                    e.doit = false;
                }
            }
        });

        Label gap5 = new Label(optionsGroup, SWT.LEFT);
        gap5.setLayoutData(hugeGapLayout);
        buttonStartGame = new Button(optionsGroup, SWT.PUSH);
        buttonStartGame.setText(Messages.DivideView_5);
        buttonStartGame.setFont(FontService.getNormalFont());
        buttonStartGame.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                // start game pushed
                // create game machine, pass over params and start
                if (!textStartValue.getText().isEmpty()) {
                    ArrayList<IPlayer> players = new ArrayList<IPlayer>();
                    if (button1pVsComp.getSelection()) {
                        players.add(new HumanPlayer(Messages.DivideView_6));
                        players.add(new RandomPlayer(Messages.DivideView_7));
                    } else {
                        players.add(new HumanPlayer(Messages.DivideView_8));
                        players.add(new HumanPlayer(Messages.DivideView_9));
                    }
                    ChoosePlayerDialog choosePlayer = new ChoosePlayerDialog(players, new Shell());
                    int playerIndex = choosePlayer.open();
                    if (playerIndex != SWT.DEFAULT) {
                        IMathEngine mathEngine = new TrivialMathEngine();
                        gameMachine = new GameMachine(mathEngine, players, Integer.parseInt(textStartValue.getText()));
                        gameMachine.addObserver(DivideView.this);
                        gameMachine.start(players.get(playerIndex));
                    }
                }
            }
        });

        // create initial playing field according to default number input from config
        playingField = new Composite(upperContent, SWT.NONE);
        playingField.setLayout(new GridLayout(10, true));
        playingField.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        int startingNumber = Integer.parseInt(textStartValue.getText());
        IMathEngine tmpMathEngine = new TrivialMathEngine();
        createPlayingField(tmpMathEngine.getDivider(startingNumber));

        lowerContent = new Composite(sashForm, SWT.NONE);
        lowerContent.setLayout(new GridLayout(1, false));
        lowerContent.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        // game information
        gameInformationGroup = new Group(lowerContent, SWT.NONE);
        gameInformationGroup.setText(Messages.DivideView_10);
        gameInformationGroup.setLayout(new RowLayout());
        gameInformationGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        // score table
        detailedGameInformationGroup = new Group(lowerContent, SWT.NONE);
        detailedGameInformationGroup.setText(Messages.DivideView_11);
        detailedGameInformationGroup.setLayout(new GridLayout());
        detailedGameInformationGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        scoreTable = new Table(detailedGameInformationGroup, SWT.BORDER);
        scoreTable.setLinesVisible(true);
        scoreTable.setHeaderVisible(true);
        scoreTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        scoreTable.setFocus();
        TableColumn[] columns = new TableColumn[5];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = new TableColumn(scoreTable, SWT.NONE);
        }
        columns[0].setText(Messages.DivideView_12);
        columns[1].setText(Messages.DivideView_13);
        columns[2].setText(Messages.DivideView_14);
        columns[3].setText(Messages.DivideView_15);
        columns[4].setText(Messages.DivideView_16);
        for (int i = 0; i < columns.length; i++) {
            columns[i].pack();
        }

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, DividePlugin.PLUGIN_ID + ".helpView");
    }

    @Override
    public void setFocus() {
        parent.setFocus();
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        GameMachineEvent event = (GameMachineEvent) arg1;
        if (event != null) {
            List<GameState> stateList = event.getStateList();
            if (stateList != null) {
                GameState state = stateList.get(0);
                setUndoRedo(state);

                switch (event.getEventType()) {

                case START_EVENT: {
                    // disable new game command button
                    MenuBarActivation.enableNewGameState(false);
                    // disable options
                    enableOptionsGroup(false);

                    if (NewGameStateSourceProvider.hasBeenEnabledEver() || startingValueChanged()) {
                        /*
                         * there has been already a game before or the starting value has been
                         * changed so that the initial playing field has to be recreated
                         */
                        cleanupPlayingArea();
                        createPlayingField(state.getListOfNumbers());
                    }
                    updatePlayingField(state.getListOfNumbers());

                    labelPlayerActive = new CLabel[gameMachine.getPlayers().size()];
                    for (int i = 0; i < labelPlayerActive.length; i++) {
                        labelPlayerActive[i] = new CLabel(gameInformationGroup, SWT.CENTER | SWT.SHADOW_OUT);
                        labelPlayerActive[i].setText(gameMachine.getPlayers().get(i).getName());
                        labelPlayerActive[i].setFont(FontService.getNormalFont());
                    }
                    setActivePlayer(state.getPlayerCurrentRound());
                    lowerContent.layout();

                    nextTurn(state.getPlayerCurrentRound(), state.getListOfNumbers());
                    break;
                }

                case REDO_EVENT:
                case NEXT_ROUND_EVENT: {
                    Iterator<GameState> stateIterator = stateList.iterator();
                    while (stateIterator.hasNext()) {
                        state = stateIterator.next();
                        // update playing field
                        updatePlayingField(state.getListOfNumbers());
                        // update table
                        addTableRow(state);
                        setActivePlayer(state.getPlayerCurrentRound());
                        setUndoRedo(state);
                    }
                    nextTurn(state.getPlayerCurrentRound(), state.getListOfNumbers());
                    break;
                }

                case UNDO_EVENT: {
                    // update playing field
                    updatePlayingField(state.getListOfNumbers());
                    // update table
                    if (gameMachine.hasComputerPlayer()) {
                        scoreTable.remove(scoreTable.getItemCount() - 2, scoreTable.getItemCount() - 1);
                    } else {
                        scoreTable.remove(scoreTable.getItemCount() - 1);
                    }
                    setActivePlayer(state.getPlayerCurrentRound());
                    nextTurn(state.getPlayerCurrentRound(), state.getListOfNumbers());
                    break;
                }

                case END_EVENT: {
                    // update playing field
                    updatePlayingField(state.getListOfNumbers());
                    setActivePlayer(null);

                    RowData fieldData = new RowData(5, 20);
                    Label gap = new Label(gameInformationGroup, SWT.CENTER);
                    gap.setLayoutData(fieldData);
                    gap.setFont(FontService.getLargeBoldFont());
                    CLabel winner = new CLabel(gameInformationGroup, SWT.CENTER);
                    winner.setText(Messages.DivideView_17 + state.getPlayerCurrentRound().getName());
                    winner.setFont(FontService.getLargeBoldFont());
                    gameInformationGroup.layout();

                    // update table
                    addTableRow(state);

                    MenuBarActivation.enableNewGameState(true);
                    MenuBarActivation.enableUndo(false);
                    MenuBarActivation.enableRedo(false);
                    gameMachine.deleteObserver(DivideView.this);
                    break;
                }

                default:
                    break;
                }
            }
        }
    }

    private void addTableRow(GameState state) {
        String turn = String.valueOf(state.getTurn());
        String player = state.getPlayerLastRound().getName();
        String chosenNumber = String.valueOf(state.getChosenNumber());
        String eliminatedNumbers = DividerGameUtil.createStringFromIntArray(state.getEliminatedNumbers());
        String remainingNumbers = DividerGameUtil.createStringFromIntArray(state.getListOfNumbers());

        TableItem tableEntry = new TableItem(scoreTable, SWT.NONE);
        tableEntry.setText(0, turn);
        tableEntry.setText(1, player);
        tableEntry.setText(2, chosenNumber);
        tableEntry.setText(3, eliminatedNumbers);
        tableEntry.setText(4, remainingNumbers);

        for (TableColumn col : scoreTable.getColumns()) {
            col.pack();
        }
    }

    private void createPlayingField(List<Integer> listOfNumbers) {
        /*
         * creates a playing field including listeners but all labels are disabled by default and
         * grayed out
         */
        int numOfLabels = listOfNumbers.size();
        labels = new CLabel[numOfLabels];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new CLabel(playingField, SWT.PUSH | SWT.CENTER);
            labels[i].setText(listOfNumbers.get(i).toString());
            labels[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            labels[i].setFont(FontService.getLargeBoldFont());
            labels[i].setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
            labels[i].setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
            labels[i].addMouseListener(new MouseListener() {

                @Override
                public void mouseUp(MouseEvent e) {

                }

                @Override
                public void mouseDown(MouseEvent e) {
                    // human interaction
                    CLabel label = (CLabel) e.widget;
                    int number = Integer.valueOf(label.getText());
                    gameMachine.nextTurn(number);
                }

                @Override
                public void mouseDoubleClick(MouseEvent e) {

                }
            });
            labels[i].setEnabled(false);
            playingField.layout();
        }
    }

    private void updatePlayingField(List<Integer> listOfNumbers) {
        // enable / disable labels according to listOfNumbers and set color
        for (CLabel label : labels) {
            Integer value = Integer.parseInt(label.getText());

            if (!listOfNumbers.contains(value)) {
                label.setEnabled(false);
                label.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
            } else {
                label.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
                label.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
                label.setEnabled(true);
            }
        }
        playingField.layout();
    }

    private void nextTurn(IPlayer player, List<Integer> listOfNumbers) {
        if (!player.isHuman()) {
            gameMachine.nextTurn(player.chooseNumber(listOfNumbers));
        }
    }

    public void enableOptionsGroup(boolean b) {
        for (Control c : optionsGroup.getChildren()) {
            c.setEnabled(b);
        }
        optionsGroup.setEnabled(b);
    }

    private void setActivePlayer(IPlayer player) {
        for (CLabel label : labelPlayerActive) {
            if (player == null) {
                label.setFont(FontService.getNormalFont());
                continue;
            }
            String labelText = label.getText();
            if (labelText.compareTo(player.getName()) == 0) {
                label.setFont(FontService.getNormalBoldFont());
            } else {
                label.setFont(FontService.getNormalFont());
            }
        }
        gameInformationGroup.layout();
    }

    private void cleanupPlayingArea() {
        // cleanup playingField
        Control[] currentActiveLabelsPlayingArea = playingField.getChildren();
        if (currentActiveLabelsPlayingArea.length != 0) {
            for (Control label : currentActiveLabelsPlayingArea) {
                label.dispose();
            }
        }
        // cleanup gameInformationGroup
        Control[] currentActiveLabelsGameInformationGroup = gameInformationGroup.getChildren();
        if (currentActiveLabelsGameInformationGroup.length != 0) {
            for (Control label : currentActiveLabelsGameInformationGroup) {
                label.dispose();
            }
        }
        // cleanup scoreTable
        if (scoreTable.getItemCount() > 0) {
            scoreTable.removeAll();
        }
    }

    private void setUndoRedo(GameState state) {
        /*
         * it could be that the computer has started the game. in that case there is no undo if the
         * turn == 1 because that would mean we could undo the decision of the computer
         */

        // set undo
        IPlayer startingPlayer = gameMachine.getStartingPlayer();
        if (!startingPlayer.isHuman() && state.getTurn() == 1) {
            MenuBarActivation.enableUndo(false);
        } else if (state.getTurn() > 0) {
            MenuBarActivation.enableUndo(true);
        } else {
            MenuBarActivation.enableUndo(false);
        }

        // set redo
        if (state.getTurn() < gameMachine.getStateHistorySize() - 1) {
            MenuBarActivation.enableRedo(true);
        } else {
            MenuBarActivation.enableRedo(false);
        }
    }

    private boolean startingValueChanged() {
        return !Messages.DivideView_18.equals(textStartValue.getText());
    }

    public Group getOptionsGroup() {
        return optionsGroup;
    }

    public GameMachine getGameMachine() {
        return gameMachine;
    }
}

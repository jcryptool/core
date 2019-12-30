// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
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
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.games.divide.DividePlugin;
import org.jcryptool.games.divide.dialogs.ChoosePlayerDialog;
import org.jcryptool.games.divide.logic.ComputerPlayer;
import org.jcryptool.games.divide.logic.GameMachine;
import org.jcryptool.games.divide.logic.GameMachineEvent;
import org.jcryptool.games.divide.logic.GameState;
import org.jcryptool.games.divide.logic.HighestStrategy;
import org.jcryptool.games.divide.logic.HumanPlayer;
import org.jcryptool.games.divide.logic.IMathEngine;
import org.jcryptool.games.divide.logic.IPlayer;
import org.jcryptool.games.divide.logic.IStrategy;
import org.jcryptool.games.divide.logic.LowestStrategy;
import org.jcryptool.games.divide.logic.RandomStrategy;
import org.jcryptool.games.divide.logic.TrivialMathEngine;
import org.jcryptool.games.divide.sourceProviders.MenuBarActivation;
import org.jcryptool.games.divide.sourceProviders.NewGameStateSourceProvider;
import org.jcryptool.games.divide.util.DividerGameUtil;

public class DivideView extends ViewPart implements Observer {

    // instance vars
    private Composite parent;
    private Composite content;
    private Composite upperContent;
    private Group optionsGroup;
    private Composite descriptionComposite;
    private Text titleText;
    private Text descriptionText;
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
    private Label gameType;
    private Label labelStrategy;
    private Combo strategyCombo;
    private ArrayList<IStrategy> strategies;

    // constructor
    public DivideView() {
        super();
        strategies = new ArrayList<IStrategy>();
        strategies.add(new RandomStrategy());
        strategies.add(new LowestStrategy());
        strategies.add(new HighestStrategy());
    }

    // methods
    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        
        ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setLayout(new GridLayout());
        
        content = new Composite(scrolledComposite, SWT.NONE);
        GridLayout gl_content = new GridLayout();
        gl_content.marginWidth = 0;
        gl_content.marginHeight = 0;
        content.setLayout(gl_content);

        // description
        descriptionComposite = new Composite(content, SWT.NONE);
        descriptionComposite.setLayout(new GridLayout(1, false));
        descriptionComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        descriptionComposite.setBackground(ColorService.WHITE);
        
        scrolledComposite.setContent(content);
        
        titleText = new Text(descriptionComposite, SWT.NONE);
        titleText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        titleText.setFont(FontService.getHeaderFont());
        titleText.setText(Messages.DivideView_19);
        titleText.setBackground(ColorService.WHITE);
        titleText.setEditable(false);
        
        descriptionText = new Text(descriptionComposite, SWT.WRAP);
        GridData gd_descriptionText = new GridData(SWT.FILL, SWT.FILL, true, false);
        //Needed that the text will wrap
        gd_descriptionText.widthHint = 800;
        descriptionText.setLayoutData(gd_descriptionText);
        descriptionText.setText(Messages.DivideView_20);
        descriptionText.setBackground(ColorService.WHITE);
        descriptionText.setEditable(false);
        
        upperContent = new Composite(content, SWT.NONE);
        upperContent.setLayout(new GridLayout(1, true));
        upperContent.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
       
        // options pane
        optionsGroup = new Group(upperContent, SWT.NONE);
        optionsGroup.setText(Messages.DivideView_0);
        optionsGroup.setLayout(new GridLayout(7, false));
        optionsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        Label labelStartValue = new Label(optionsGroup, SWT.NONE);
        labelStartValue.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 2));
        labelStartValue.setText(Messages.DivideView_4);
        labelStartValue.setFont(FontService.getNormalBoldFont());

        textStartValue = new Text(optionsGroup, SWT.NONE);
        textStartValue.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 2));
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

        gameType = new Label(optionsGroup, SWT.NONE);
        GridData gd_gameType = new GridData();
        gd_gameType.horizontalIndent = 40;
        gd_gameType.verticalAlignment = SWT.TOP;
        gd_gameType.verticalSpan = 2;
        gameType.setLayoutData(gd_gameType);
        gameType.setText(Messages.DivideView_1);
        gameType.setFont(FontService.getNormalBoldFont());

        button1pVsComp = new Button(optionsGroup, SWT.RADIO);
        button1pVsComp.setText(Messages.DivideView_2);
        button1pVsComp.setFont(FontService.getNormalFont());
        button1pVsComp.setSelection(true);

        labelStrategy = new Label(optionsGroup, SWT.NONE);
        GridData gd_labelStrategy = new GridData();
        gd_labelStrategy.horizontalIndent = 40;
        labelStrategy.setLayoutData(gd_labelStrategy);
        labelStrategy.setText(Messages.DivideView_21);
        labelStrategy.setFont(FontService.getNormalBoldFont());

        strategyCombo = new Combo(optionsGroup, SWT.READ_ONLY);
        
        for (IStrategy strategy : strategies) {
            if (strategy != null && strategy.getName() != null) {
                strategyCombo.add(strategy.getName());
            }
        }
        strategyCombo.select(0);

        buttonStartGame = new Button(optionsGroup, SWT.PUSH);
        GridData gd_buttonStartGame = new GridData();
        gd_buttonStartGame.horizontalIndent = 60;
        gd_buttonStartGame.verticalSpan = 2;
        buttonStartGame.setLayoutData(gd_buttonStartGame);
        buttonStartGame.setText(Messages.DivideView_5);
        buttonStartGame.setFont(FontService.getNormalFont());
        buttonStartGame.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                // start game pushed
                // create game machine, pass over params and start
                if (!textStartValue.getText().isEmpty()) {
                    MenuBarActivation.enableNewGameState(true);
                    ArrayList<IPlayer> players = new ArrayList<IPlayer>();
                    if (button1pVsComp.getSelection()) {
                        players.add(new HumanPlayer(Messages.DivideView_6));
                        players.add(new ComputerPlayer(strategies.get(strategyCombo.getSelectionIndex())));

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
        
        button1pVs2P = new Button(optionsGroup, SWT.RADIO);
        button1pVs2P.setText(Messages.DivideView_3);
        button1pVs2P.setFont(FontService.getNormalFont());
        button1pVs2P.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (button1pVsComp.getSelection()) {
                    labelStrategy.setEnabled(true);
                    strategyCombo.setEnabled(true);
                } else {
                    labelStrategy.setEnabled(false);
                    strategyCombo.setEnabled(false);
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

        lowerContent = new Composite(content, SWT.NONE);
        GridLayout gl_lowerContent = new GridLayout();
        gl_lowerContent.marginWidth = 0;
        gl_lowerContent.marginHeight = 0;
        lowerContent.setLayout(gl_lowerContent);
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
        GridData gd_scoreTable = new GridData(GridData.FILL, GridData.FILL, true, true);
        gd_scoreTable.heightHint = 150;
        scoreTable.setLayoutData(gd_scoreTable);
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

        scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
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
                    // MenuBarActivation.enableNewGameState(false);
                    // disable save game command button
                    MenuBarActivation.enableSaveGameState(false);
                    // disable options
                    enableOptionsGroup(optionsGroup, false);

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

                    // MenuBarActivation.enableNewGameState(true);
                    MenuBarActivation.enableSaveGameState(true);
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
            GridData gd_labels = new GridData(SWT.FILL, SWT.FILL, true, true);
            labels[i].setLayoutData(gd_labels);
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
                String tooltip = Messages.DivideView_22 + " " + label.getText() + System.lineSeparator()
                        + Messages.DivideView_23 + " " + DividerGameUtil.getMultiples(listOfNumbers, value);
                label.setToolTipText(tooltip);
            }
        }
        playingField.layout();
    }

    private void nextTurn(IPlayer player, List<Integer> listOfNumbers) {
        if (!player.isHuman()) {
            gameMachine.nextTurn(player.getStrategy().chooseNumber(listOfNumbers));
        }
    }

    public void enableOptionsGroup(Composite comp, boolean b) {
        for (Control c : comp.getChildren()) {
            if (c instanceof Composite) {
                enableOptionsGroup((Composite) c, b);
            }
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

    public void cleanupPlayingArea() {
        // cleanup playingField
        Control[] currentActiveLabelsPlayingArea = playingField.getChildren();
        if (currentActiveLabelsPlayingArea.length != 0) {
            for (Control label : currentActiveLabelsPlayingArea) {
                label.dispose();
            }
        }
        // cleanup gameInformationGroup
        cleanupGameInformationGroup();
        // cleanup scoreTable
        cleanUpScoreTable();
    }

    public void cleanupGameInformationGroup() {
        // cleanup gameInformationGroup
        Control[] currentActiveLabelsGameInformationGroup = gameInformationGroup.getChildren();
        if (currentActiveLabelsGameInformationGroup.length != 0) {
            for (Control label : currentActiveLabelsGameInformationGroup) {
                label.dispose();
            }
        }
    }

    public void cleanUpScoreTable() {
        // cleanup scoreTable
        if (scoreTable.getItemCount() > 0) {
            scoreTable.removeAll();
        }
    }

    public void disablePlayingArea() {
        for (CLabel label : labels) {
            label.setEnabled(false);
            label.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
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

    public Table getScoreTable() {
        return scoreTable;
    }

    public boolean getGameType() {
        // 0 = 1P/Comp 1 = 1P/2P
        return button1pVsComp.getSelection() ? true : false;
    }

    public Text getTextStartValue() {
        return textStartValue;
    }

    public Combo getStrategyCombo() {
        return strategyCombo;
    }
}

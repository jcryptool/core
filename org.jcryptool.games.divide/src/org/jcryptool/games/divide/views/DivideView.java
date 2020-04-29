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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
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
    private StackLayout gameInformationStack;
    private Composite gameInfoStartContainer;
    private HashMap<IPlayer, Composite> gameInfoPlayerContainers;
    private Composite gameInfoEndContainer;
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
    private MouseAdapter userInputListener;

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

        scrolledComposite.setContent(content);
        
        
		TitleAndDescriptionComposite titleAndDescription = new TitleAndDescriptionComposite(content);
		titleAndDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		titleAndDescription.setTitle(Messages.DivideView_19);
		titleAndDescription.setDescription(Messages.DivideView_20);
        
        upperContent = new Composite(content, SWT.NONE);
        upperContent.setLayout(new GridLayout(1, true));
        upperContent.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
       
        // options pane
        optionsGroup = new Group(upperContent, SWT.NONE);
        optionsGroup.setText(Messages.DivideView_0);
        optionsGroup.setLayout(new GridLayout(7, false));
        optionsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        Label labelStartValue = new Label(optionsGroup, SWT.NONE);
        labelStartValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 2));
        labelStartValue.setText(Messages.DivideView_4);
        labelStartValue.setFont(FontService.getNormalBoldFont());

        textStartValue = new Text(optionsGroup, SWT.NONE);
        textStartValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 2));
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
		textStartValue.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				var currentInput = textStartValue.getText();
				if (currentInput.startsWith("0") && currentInput.endsWith("0") || currentInput.length() == 0) {
					// If no input is provided, disable the start game button and color the input field red.
					buttonStartGame.setEnabled(false);
					textStartValue.setBackground(ColorService.LIGHT_AREA_RED);
					textStartValue.setForeground(ColorService.BLACK);
				} else if (!buttonStartGame.getEnabled()) {
					// If the conditions are valid, but the start game button is disabled --> enable it again
					buttonStartGame.setEnabled(true);
					textStartValue.setBackground(null);
					textStartValue.setForeground(null);
				}
			}
		});

        gameType = new Label(optionsGroup, SWT.NONE);
        GridData gd_gameType = new GridData();
        gd_gameType.horizontalIndent = 40;
        gd_gameType.verticalAlignment = SWT.CENTER;
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
        gd_labelStrategy.verticalSpan = 2;
        gd_labelStrategy.verticalAlignment = SWT.CENTER;
        labelStrategy.setLayoutData(gd_labelStrategy);
        labelStrategy.setText(Messages.DivideView_21);
        labelStrategy.setFont(FontService.getNormalBoldFont());

        strategyCombo = new Combo(optionsGroup, SWT.READ_ONLY);
        strategyCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 2));
        
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
        buttonStartGame.setFocus();
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
                    ChoosePlayerDialog choosePlayer = new ChoosePlayerDialog(players, Display.getCurrent().getActiveShell());
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
                syncStrategyButtonsEnabled();
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

        // game information (basically displays current player and who won)
        gameInformationGroup = new Group(lowerContent, SWT.NONE);
        gameInformationStack = new StackLayout();
        gameInformationGroup.setLayout(gameInformationStack);
        GridData gameInformationGroupLayout = new GridData(GridData.CENTER, GridData.FILL, true, false);
        gameInformationGroupLayout.minimumWidth = 300;
        gameInformationGroupLayout.minimumHeight = 50;
        gameInformationGroup.setLayoutData(gameInformationGroupLayout);
        
        // The initial view of the game information group (empty) 
        gameInfoStartContainer = new Composite(gameInformationGroup, SWT.NONE);
        gameInformationStack.topControl = gameInfoStartContainer;

        // score table
        detailedGameInformationGroup = new Group(lowerContent, SWT.NONE);
        detailedGameInformationGroup.setText(Messages.DivideView_11);
        detailedGameInformationGroup.setLayout(new GridLayout());
        detailedGameInformationGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        scoreTable = new Table(detailedGameInformationGroup, SWT.BORDER);
        scoreTable.setLinesVisible(true);
        scoreTable.setHeaderVisible(true);
        GridData gd_scoreTable = new GridData(GridData.FILL, GridData.FILL, true, true);
        scoreTable.setLayoutData(gd_scoreTable);
        
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

        scrolledComposite.setMinSize(725, content.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);
        
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
                    // reset the who's next text (may have been altered if game has been won before)
                    gameInformationGroup.setText(Messages.DivideView_10);

                    if (NewGameStateSourceProvider.hasBeenEnabledEver() || startingValueChanged()) {
                        /*
                         * there has been already a game before or the starting value has been
                         * changed so that the initial playing field has to be recreated
                         */
                        cleanupPlayingArea();
                        createPlayingField(state.getListOfNumbers());
                    }
                    updatePlayingField(state.getListOfNumbers());

                    
                    gameInfoPlayerContainers = new HashMap<IPlayer, Composite>();
                    var players = gameMachine.getPlayers();
                    /*
                     * Each player gets his own composite, which displays him as active player
                     * The outer loop goes through all players and creates a composite for each of them.
                     * These can then be switched by the StackLayout provided on the parent group.
                     */
                    for (var active : players) {
                    	gameInfoPlayerContainers.put(active, new Composite(gameInformationGroup, SWT.NONE));
                    	gameInfoPlayerContainers.get(active).setLayout(new GridLayout(2, true));
                    	/*
                    	 * The inner loop fills the composite with a label for each player. Obviously the active player
                    	 * label gets a different design.
                    	 */
                    	for (var player : players) {
                        	// The label of the current turn's player has a SHADOW_OUT and NormalBoldFont
                    		// The label of the inactive player has a SHADOW_IN and NormalFont
                    		var style = player == active ? SWT.CENTER | SWT.SHADOW_OUT : SWT.CENTER | SWT.SHADOW_IN;
                    		var font = player == active ? FontService.getNormalBoldFont() : FontService.getNormalFont();
                    		
                    		var clabel = new CLabel(gameInfoPlayerContainers.get(active), style);
                    		clabel.setFont(font);
                    		clabel.setText(player.getName());
                    		clabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
                        }
                    }
                    
                    setActivePlayer(state.getPlayerCurrentRound());
                    if (!state.getPlayerCurrentRound().isHuman())
                    	computerTurn(state.getPlayerCurrentRound(), state.getListOfNumbers());
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
                    if (!state.getPlayerCurrentRound().isHuman())
                    	computerTurn(state.getPlayerCurrentRound(), state.getListOfNumbers());
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
                    if (!state.getPlayerCurrentRound().isHuman())
                    	computerTurn(state.getPlayerCurrentRound(), state.getListOfNumbers());
                    break;
                }

                case END_EVENT: {
                    // update playing field
                    updatePlayingField(state.getListOfNumbers());
                    enableOptionsGroup(optionsGroup, true);
                    syncStrategyButtonsEnabled();
                    
                    gameInformationGroup.setText(Messages.DivideView_51);
                    gameInfoEndContainer = new Composite(gameInformationGroup, SWT.NONE);
                    gameInfoEndContainer.setLayout(new GridLayout());
                    CLabel winnerLabel = new CLabel(gameInfoEndContainer, SWT.CENTER);
                    winnerLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
                    winnerLabel.setText(Messages.DivideView_17 + state.getPlayerCurrentRound().getName());
                    winnerLabel.setFont(FontService.getLargeBoldFont());

                    gameInformationStack.topControl = gameInfoEndContainer;
                    gameInformationGroup.layout();

                    // update table
                    addTableRow(state);

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

        var items = scoreTable.getColumns();
        items[1].pack();  // Pack column "Player"
        items[3].pack();  // Pack column "Eliminated"
        items[4].pack();  // Pack column "Remaining"
        // For the rest of the Columns I don't think a pack is required.
        // Too many packs() in tables lead to not so nice artifacts on slow machines.

        scoreTable.setTopIndex(scoreTable.getItems().length - 1);
    }

    private void createPlayingField(List<Integer> listOfNumbers) {
        /*
         * creates a playing field including listeners but all labels are disabled by default and
         * grayed out
         */
        int numOfLabels = listOfNumbers.size();
        labels = new CLabel[numOfLabels];
        
        userInputListener = new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				/*
				 * The handler for human interaction.
				 */
				CLabel label = (CLabel) e.widget;
				int number = Integer.valueOf(label.getText());
				gameMachine.nextTurn(number);

			}
		};
        
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new CLabel(playingField, SWT.PUSH | SWT.CENTER);
            labels[i].setText(listOfNumbers.get(i).toString());
            GridData gd_labels = new GridData(SWT.FILL, SWT.FILL, true, true);
            labels[i].setLayoutData(gd_labels);
            labels[i].setFont(FontService.getLargeBoldFont());
            labels[i].setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
            labels[i].setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
            labels[i].addMouseListener(userInputListener);
               
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

	private void computerTurn(IPlayer player, List<Integer> listOfNumbers) {
		Display.getCurrent().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(700);
					gameMachine.nextTurn(player.getStrategy().chooseNumber(listOfNumbers));
				} catch (InterruptedException e) {
					// We do not care if we get interrupted
				}
			}
		});
	}

    /**
     * Recursively enables/disables children of a composite.
     * This is used for enabling/disabling the options if a game is currently running or not.
     * @param comp A composite which children should be disabled.
     * @param enable whether all children should be enabled (true) or disabled (false)
     */
    public void enableOptionsGroup(Composite comp, boolean enable) {
        for (Control c : comp.getChildren()) {
            if (c instanceof Composite) {
                enableOptionsGroup((Composite) c, enable);
            }
            c.setEnabled(enable);
        }
        optionsGroup.setEnabled(enable);
    }

    private void setActivePlayer(IPlayer player) {
    	gameInformationStack.topControl = gameInfoPlayerContainers.get(player);
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
    
    /**
     * Enable or disable the strategy selection depending on whether a computer player is chosen or not.
     * If the game mode is set vs Computer --> enable<br>
     * If the game mode is set vs Human--> disable<br>
     */
    public void syncStrategyButtonsEnabled() {
    	if (button1pVsComp.getSelection()) {
            labelStrategy.setEnabled(true);
            strategyCombo.setEnabled(true);
        } else {
            labelStrategy.setEnabled(false);
            strategyCombo.setEnabled(false);
        }
    }
    
    public void resetInfoGroupText() {
    	gameInformationGroup.setText("");
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

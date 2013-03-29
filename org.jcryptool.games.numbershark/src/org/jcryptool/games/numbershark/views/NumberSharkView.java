// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.numbershark.views;

import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.core.util.numbers.NumberService;
import org.jcryptool.games.numbershark.NumberSharkPlugin;
import org.jcryptool.games.numbershark.dialogs.EndOfGameDialog;
import org.jcryptool.games.numbershark.util.CommandState;
import org.jcryptool.games.numbershark.util.CommandStateChanger;
import org.jcryptool.games.numbershark.util.ScoreTableRow;

/**
 * The actual view of the game
 * 
 * @author Johannes Spaeth
 * @version 0.9.5
 */
public class NumberSharkView extends ViewPart {
	private int numberOfFields = 40;
	private Table scoreTable;
	private Number[] numberField;
	private Label sharkScore;
	private Label playerScore;
	private Label requiredScore;
	private Composite parent;
	private Composite playingField;
	public static final String ZERO_SCORE = "0"; //$NON-NLS-1$
	private int hint = 0;
	private Group detailedScore;

	private TabFolder numberTabs = null;
	private TabItem[] tab;
	final private int MAX_NUM_PER_TAB = 40;
	private CLabel[] buttons = new CLabel[MAX_NUM_PER_TAB];

	private ScoreTableRow scoreTableRow;
	private Hashtable<Integer, ScoreTableRow> scoreTableRowList = new Hashtable<Integer, ScoreTableRow>();
	private int playerMove;

	@Override
	public void createPartControl(final Composite parent) {
		this.parent = parent;

		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayout(new GridLayout(1, false));
		sashForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				true));
		playingField = new Composite(sashForm, SWT.NONE);
		playingField.setLayout(new GridLayout(1, false));
		playingField.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));
		
		Composite lowerContent = new Composite(sashForm, SWT.NONE);
		lowerContent.setLayout(new GridLayout(1, false));
		lowerContent.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				true));
		Group score = new Group(lowerContent, SWT.NONE);
		
		score.setText(Messages.NumberSetView_9);
		score.setLayout(new RowLayout());
		score.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				false));
		

		RowData fieldData = new RowData(60, 20);

		Label playerScoreLabel = new Label(score, SWT.RIGHT);
		playerScoreLabel.setText(Messages.NumberSetView_13);
		playerScoreLabel.setFont(FontService.getLargeBoldFont());

		playerScore = new Label(score, SWT.LEFT);
		playerScore.setLayoutData(fieldData);
		playerScore.setFont(FontService.getLargeBoldFont());

		Label sharkScoreLabel = new Label(score, SWT.RIGHT);
		sharkScoreLabel.setText(Messages.NumberSetView_12);
		sharkScoreLabel.setFont(FontService.getLargeBoldFont());


		sharkScore = new Label(score, SWT.LEFT);
		sharkScore.setLayoutData(fieldData);
		sharkScore.setFont(FontService.getLargeBoldFont());

		Label requiredScoreLabel = new Label(score, SWT.RIGHT);
		requiredScoreLabel.setText(Messages.NumberSetView_10);
		requiredScoreLabel.setFont(FontService.getLargeBoldFont());

		requiredScore = new Label(score, SWT.LEFT);
		requiredScore.setLayoutData(fieldData);
		requiredScore.setFont(FontService.getLargeBoldFont());

		detailedScore = new Group(lowerContent, SWT.NONE);
		detailedScore.setText(Messages.NumberSetView_14);
		detailedScore.setLayout(new GridLayout(1, false));
		detailedScore.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));

		scoreTable = new Table(detailedScore, SWT.BORDER);
		scoreTable.setLinesVisible(true);
		scoreTable.setHeaderVisible(true);
		scoreTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));
		scoreTable.setFocus();

		TableColumn[] columns = new TableColumn[6];

		for (int i = 0; i < 6; i++) {
			columns[i] = new TableColumn(scoreTable, SWT.NONE);
		}

		columns[0].setText(Messages.NumberSetView_15);
		columns[1].setText(Messages.NumberSetView_16);
		columns[2].setText(Messages.NumberSetView_17);
		columns[3].setText(Messages.NumberSetView_18);
		columns[4].setText(Messages.NumberSetView_19);
		columns[5].setText(Messages.NumberSetView_20);

		for (int i = 0; i < 6; i++) {
			columns[i].pack();
		}

		createPlayingField(numberOfFields);

		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(parent, NumberSharkPlugin.PLUGIN_ID + ".view"); //$NON-NLS-1$

		hookActionBar();
	}

	private void hookActionBar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		getViewSite().getActionBars().updateActionBars();
	}

	/**
	 * Adds the last move to the score table.
	 * 
	 * @param takenNumber
	 * @param lostNumbers
	 */
	public void addMoveToTable(int takenNumber, int[] lostNumbers) {
		int score = 0;
		int lostScore = 0;
		int remainingNumbers = numberOfFields-2;

		boolean isPrime = takenNumber == 0 ? false : NumberService
				.isPrime(takenNumber);

		scoreTableRow = new ScoreTableRow();

		int numberOfRows = scoreTable.getItemCount();

		scoreTableRow.setMove(String.valueOf(numberOfRows + 1));

		if (isPrime) {
			scoreTableRow.setTakenNumbers(String.valueOf(takenNumber)
					+ Messages.NumberSharkView_0);
		} else {
			scoreTableRow.setTakenNumbers(String.valueOf(takenNumber));
		}

		if (numberOfRows > 0) {
			score = Integer.parseInt(scoreTable.getItem(numberOfRows - 1)
					.getText(2));
			lostScore = Integer.parseInt(scoreTable.getItem(numberOfRows - 1)
					.getText(4));
			remainingNumbers = Integer.parseInt(scoreTable.getItem(
					numberOfRows - 1).getText(5)) - 2;
		}

		if (takenNumber == 0) {
			scoreTableRow.setTakenNumbers("-"); //$NON-NLS-1$
			remainingNumbers++;
		}

		// col 3 value of points
		scoreTableRow.setPoints(String.valueOf((score + takenNumber)));
		isPrime = lostNumbers[0] == 0 ? false : NumberService
				.isPrime(lostNumbers[0]);
		String lostNum = String.valueOf(lostNumbers[0]);
		if (isPrime) {
			lostNum += Messages.NumberSharkView_0;
		}
		
		int lostSum = lostNumbers[0];
		for (int k = 1; k < lostNumbers.length; k++) {
			isPrime = lostNumbers[k] == 0 ? false : NumberService
					.isPrime(lostNumbers[k]);
			lostNum += ", " + lostNumbers[k]; //$NON-NLS-1$
			if (isPrime) {
				lostNum += Messages.NumberSharkView_0;
			}
			lostSum += lostNumbers[k];
			remainingNumbers--;
		}

		scoreTableRow.setLostNumbers(lostNum);

		lostScore += lostSum;
		scoreTableRow.setLostScore(String.valueOf(lostScore));
		scoreTableRow.setRemainingNumbers(String.valueOf(remainingNumbers));
		
		if(score + takenNumber == lostScore){
			sharkScore.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW));
			playerScore.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW));
		} else if  (score + takenNumber > lostScore) {
			sharkScore.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
			playerScore.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
		} else {
			sharkScore.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
			playerScore.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
		}
		
		sharkScore.setText(String.valueOf(lostScore));
		playerScore.setText(String.valueOf(score + takenNumber));
		setPlayerMove(numberOfRows + 1);
		removeElementsFromScoreTableRowList();

		// scoreTableRowList kepp the moves from the player.
		// scoreTableRowList is used from undo-/ redo-function for navigating
		scoreTableRowList.put(numberOfRows + 1, this.scoreTableRow);
		addScoreTableRow2ScoreTableView(scoreTableRow);

		scoreTable.setSelection(scoreTable.getItemCount() - 1);
		
		// Change UndoCommandState 2 enable because more than 1 Entry in
		// ScoreTableRowList
		// Change RedoCommandState 2 disable because no Entry for RedoCommand in
		// ScoreTableRowList
		CommandStateChanger commandStateChanger = new CommandStateChanger();
		commandStateChanger.chageCommandState(CommandState.Variable.UNDO_STATE,
				CommandState.State.UNDO_ENABLED);
		commandStateChanger.chageCommandState(CommandState.Variable.REDO_STATE,
				CommandState.State.REDO_DISABLED);


		if (remainingNumbers == 0) {
			Shell shell = Display.getCurrent().getActiveShell();
			EndOfGameDialog dialog = new EndOfGameDialog(shell, this);
			dialog.open();
		}

	}

	public void cleanPlayingField() {
		numberTabs.dispose();
		for (int i = scoreTable.getItemCount(); i > 0; i--) {
			scoreTable.getItem(i - 1).dispose();
		}
	}

	/**
	 * initialize the playing field one the right side (buttons you play with).
	 * 
	 * @param numberOfFields
	 *            the number of numbers you will play with
	 * @param parent
	 *            the parent Group where the content will be created in
	 */
	public void createPlayingField(int numberOfFields) {
		
		int numOfTabs = (numberOfFields - 1) / MAX_NUM_PER_TAB + 1;

		numberField = new Number[numberOfFields];
		for (int i = 0; i < numberOfFields; i++) {
			numberField[i] = new Number(i + 1);
		}
		int minPtsToWin = (numberOfFields * (numberOfFields + 1) / 4)+1;
		requiredScore.setText(String.valueOf(minPtsToWin));
		sharkScore.setText(ZERO_SCORE);
		playerScore.setText(ZERO_SCORE);

		numberTabs = new TabFolder(playingField, SWT.NONE);
		numberTabs.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));

		tab = new TabItem[numOfTabs];
		for (int j = 0; j < numOfTabs; j++) {
			tab[j] = new TabItem(numberTabs, SWT.NONE);
			tab[j].setText(j * MAX_NUM_PER_TAB + 1 + "-"
					+ min((j + 1) * MAX_NUM_PER_TAB, numberOfFields));
		}
		this.numberOfFields = numberOfFields;

		hint = getHint();
		numberTabs.setSelection(numOfTabs-1);
		initTab(numOfTabs-1);
		numberTabs.addListener(SWT.Selection, tabsSelect);
		playingField.layout();
		
		CommandStateChanger commandStateChanger = new CommandStateChanger();
		commandStateChanger.chageCommandState(CommandState.Variable.SHARKMEAL_STATE,
					CommandState.State.SHARKMEAL_DISABLED);
		if(hint != 0 ){
			commandStateChanger.chageCommandState(CommandState.Variable.HINT_STATE,
						CommandState.State.HINT_ENABLED);
		} else {
			commandStateChanger.chageCommandState(CommandState.Variable.HINT_STATE,
					CommandState.State.HINT_DISABLED);
		}

	}

	/**
	 * initializes a tab of the TabFolder with the playing buttons on the right
	 * side
	 * 
	 * @param selectedTab
	 *            which tab is selecteed right now
	 */
	public void initTab(int selectedTab) {

		Composite compTabs = new Composite(numberTabs, SWT.NONE);
		GridLayout numbersLayout = new GridLayout();
		numbersLayout.numColumns = 10;
		numbersLayout.makeColumnsEqualWidth = true;
		compTabs.setLayout(numbersLayout);
		compTabs.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		for (int i = selectedTab * MAX_NUM_PER_TAB + 1; i <=
				(selectedTab + 1) * MAX_NUM_PER_TAB; i++) {
			int translation = selectedTab * MAX_NUM_PER_TAB + 1;
			if (i <= numberOfFields) {
				buttons[i - translation] = new CLabel(compTabs, SWT.CENTER | SWT.SHADOW_OUT | SWT.PUSH);
				buttons[i - translation].setText("" + i);
				buttons[i - translation].setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
				buttons[i - translation].setEnabled(numberField[i - 1].isEnabled());
				buttons[i - translation].setFont(FontService.getHugeBoldFont());
				buttons[i - translation].setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
				buttons[i - translation].setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));

				buttons[i - translation].addMouseListener(mouseListener);
			}
		}

		refreshButtons();

		numberTabs.getItem(selectedTab).setData(new GridData(GridData.FILL, GridData.FILL, true, true));
		numberTabs.getItem(selectedTab).setControl(compTabs);
		compTabs.layout();
	}

	MouseListener mouseListener = new MouseListener() {

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseDown(MouseEvent e) {
			CLabel pressedButton = (CLabel) e.getSource();
			int numToDeactivate = Integer.parseInt(pressedButton.getText());

			pressedButton.setToolTipText(null);
			deactivateNumber(numToDeactivate);
		}

		@Override
		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	};
	/**
	 * Calculates the ToolTip for the number i.
	 * 
	 * @param i
	 *            number for which the ToolTip shall be created for
	 * @return String with the ToolTip
	 */
	private String calcToolTip(int i) {
		StringBuffer sb = new StringBuffer();
		sb.append("" + Messages.NumberSetView_31 + i + "\n");
		ArrayList<Integer> activeDivisors = new ArrayList<Integer>();
		ArrayList<Integer> divisors = numberField[i - 1].getDivisors();

		for (int j = divisors.size() - 1; j >= 0; j--) {
			if (numberField[divisors.get(j) - 1].isEnabled()) {
				activeDivisors.add(divisors.get(j));
			}
		}
		if (activeDivisors.size() == 0) {
			sb.append(Messages.NumberSetView_33 + "\n");
		} else {
			sb.append(Messages.NumberSetView_32);
			for (int j = 0; j < activeDivisors.size() - 1; j++) {
				sb.append(activeDivisors.get(j) + ", ");
			}
			sb.append(activeDivisors.get(activeDivisors.size() - 1) + " \n");
		}
		int m = 2;
		if (m * i > numberOfFields) {
			sb.append(Messages.NumberSetView_35);
		} else {
			sb.append(Messages.NumberSetView_34);
			while (m * i <= numberOfFields - i) {
				sb.append(m * i + ", ");
				m++;
			}
			sb.append(m * i);
		}
		String s = sb.toString();
		return s;
	}

	Listener tabsSelect = new Listener() {
		public void handleEvent(Event arg0) {
			if (arg0.type == SWT.Selection) {
				initTab(((TabFolder) arg0.widget).getSelectionIndex());
			}
		}
	};

	/**
	 * defines what happens if you press a button in the playing field
	 */
	SelectionListener buttonsListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent se) {

			Button pressedButton = (Button) se.getSource();
			int numToDeactivate = Integer.parseInt(pressedButton.getText());

			pressedButton.setToolTipText(null);
			deactivateNumber(numToDeactivate);
		}
	};
	

	/**
	 * deactivate a certain number in the game
	 * 
	 * @param numToDeactivate
	 *            deactivate that number in the game
	 */
	public void deactivateNumber(int numToDeactivate) {
		ArrayList<Integer> lostNumbers = new ArrayList<Integer>();
		ArrayList<Integer> divisors = numberField[numToDeactivate - 1]
				.getDivisors();

		for (int n = 0; n < divisors.size(); n++) {
			int k = divisors.get(n) - 1;
			if (numberField[k].isEnabled()) {
				numberField[k].setEnabled(false);
				lostNumbers.add(k + 1);
			}
		}
		numberField[numToDeactivate - 1].setEnabled(false);

		int[] lostNumbersInt = new int[lostNumbers.size()];
		if (lostNumbers.size() > 0) {
			lostNumbersInt = new int[lostNumbers.size()];
			for (int i = 0; i < lostNumbers.size(); i++) {
				lostNumbersInt[i] = lostNumbers.get(i);
			}
		} else {
			lostNumbersInt = new int[1];
			lostNumbersInt[0] = numToDeactivate;
			numToDeactivate = 0;
		}

		refreshButtons();
		hint = getHint();
		CommandStateChanger commandStateChanger = new CommandStateChanger();
		if(hint != 0 ){
			commandStateChanger.chageCommandState(CommandState.Variable.HINT_STATE,
						CommandState.State.HINT_ENABLED);
		} else {
			commandStateChanger.chageCommandState(CommandState.Variable.HINT_STATE,
					CommandState.State.HINT_DISABLED);
		}
		
		ArrayList<Integer> sharkMealList = getSharkMealList();
		if(sharkMealList.isEmpty()){
			commandStateChanger.chageCommandState(CommandState.Variable.SHARKMEAL_STATE,
					CommandState.State.SHARKMEAL_DISABLED);
		} else {
			commandStateChanger.chageCommandState(CommandState.Variable.SHARKMEAL_STATE,
					CommandState.State.SHARKMEAL_ENABLED);
		}
		
		addMoveToTable(numToDeactivate, lostNumbersInt);
	}

	/**
	 * refreshes the disabled/enabled status of all playing Buttons
	 */
	public void refreshButtons() {
		int bound = MAX_NUM_PER_TAB;
		if((getSelectedTabFolderIndex()+1)*MAX_NUM_PER_TAB > numberOfFields){
			bound = numberOfFields % MAX_NUM_PER_TAB;
		}
		for (int i = 0; i < bound; i++) {
			int m = Integer.parseInt(buttons[i].getText());
			if(m <= numberOfFields){
				if(numberField[m-1].isEnabled()){
					buttons[i].setEnabled(true);
				    buttons[i].setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
					buttons[i].setToolTipText(calcToolTip(m));
				} else {
					buttons[i].setEnabled(false);
				    buttons[i].setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
				}
			}
		}
		numberTabs.layout();
	}

	@Override
	public void setFocus() {
		parent.setFocus();
	}


	public void setSharkScore(String text) {
		sharkScore.setText(text);
	}

	public void setPlayerScore(String text) {
		playerScore.setText(text);
	}

	public int getNumberOfFields() {
		return numberOfFields;
	}

	public Table getTable() {
		return scoreTable;
	}

	public int getPlayerScore() {
		return Integer.parseInt(playerScore.getText());
	}

	public int getSharkScore() {
		return Integer.parseInt(sharkScore.getText());
	}

	public int getSelectedTabFolderIndex() {
		return numberTabs.getSelectionIndex();
	}

	public void setStatus(int index, boolean status) {
		numberField[index].setEnabled(status);
		refreshButtons();
	}

	public Number[] getNumberField() {
		return numberField;
	}

	public void increasePlayerMove() {
		this.playerMove++;
	}

	public void decreasePlayerMove() {
		this.playerMove--;
	}

	public int getActualPlayerMove() {
		return this.playerMove;
	}

	public int getNextPlayerMove() {
		return this.playerMove + 1;
	}

	public int getLastPlayerMove() {
		return this.playerMove - 1;
	}

	public void setPlayerMove(int playerPosition) {
		this.playerMove = playerPosition;
	}

	public void addScoreTableRow2ScoreTableView(ScoreTableRow scoreTableRow) {
		TableItem item = new TableItem(scoreTable, SWT.NONE);
		item.setText(0, scoreTableRow.getMove());
		item.setText(1, scoreTableRow.getTakenNumbers());
		item.setText(2, scoreTableRow.getPoints());
		item.setText(3, scoreTableRow.getLostNumbers());
		item.setText(4, scoreTableRow.getLostScore());
		item.setText(5, scoreTableRow.getRemainingNumbers());
	}

	public boolean hasScoreTableRowListNextEntry() {
		if (this.scoreTableRowList.containsKey(this.playerMove + 1)) {
			return true;
		}
		return false;
	}

	public ScoreTableRow getScoreTableRowByActualPlayerPosition() {
		return (ScoreTableRow) this.scoreTableRowList.get(this.playerMove);
	}

	public ScoreTableRow getNextScoreTableRow() {
		return (ScoreTableRow) this.scoreTableRowList.get(this.playerMove + 1);
	}

	public ScoreTableRow getLastScoreTableRow() {
		return (ScoreTableRow) this.scoreTableRowList.get(this.playerMove - 1);
	}

	/**
	 * Function remove elements from ScoreTableRowList
	 */
	private void removeElementsFromScoreTableRowList() {

		if (scoreTableRowList.size() > this.playerMove) {
			int listSize = scoreTableRowList.size();
			for (int i = this.playerMove; i <= listSize; i++) {
				scoreTableRowList.remove(i);
			}
		}
	}
	
    public int getHint() {
        for (int i = numberOfFields; i > numberOfFields / 2; i--) {
            if (numberField[i - 1].isEnabled()) {
                int counter = 0;

                for (int j = 0; j < numberField[i - 1].getDivisors().size(); j++) {
                    int n = numberField[i - 1].getDivisors().get(j);
                    if (numberField[n - 1].isEnabled()) {
                        counter++;
                    }

                }
                if (counter == 1) {
                    return i;
                }

            }
        }

        return 0;
    }
    
    
    public ArrayList<Integer> getSharkMealList(){
        // calculate numbers to be eaten by the shark
        ArrayList<Integer> sharkMealList = new ArrayList<Integer>();
        for (int i = numberOfFields; i > numberOfFields / 2; i--) {
            if (numberField[i - 1].isEnabled()) {
                int counter = 0;
                boolean stop = false;
                for (int j = 0; j < numberField[i - 1].getDivisors().size(); j++) {
                    int n = numberField[i - 1].getDivisors().get(j);
                    if (numberField[n - 1].isEnabled()) {
                        counter++;
                        if (counter > 0) {
                            stop = true;
                            break;
                        }
                    }
                }
                if (!stop) {
                    sharkMealList.add(i);

                }
            }
        }
    	return sharkMealList;
    }
    
    public Composite getParent(){
    	return parent;
    }
   

}

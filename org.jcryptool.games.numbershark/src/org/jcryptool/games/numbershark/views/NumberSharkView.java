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

import java.util.ArrayList;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
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

/**
 * @author Johannes Späth
 * @version 0.9.5
 */
public class NumberSharkView extends ViewPart {
    private int numberOfFields = 40;
    private Table scoreTable;
    private CLabel[] numbers = new CLabel[numberOfFields];
    private boolean[] activeNumbers = new boolean[numberOfFields];
    private Number[] numNum;
    private Label sharkScore;
    private Label playerScore;
    private Label requiredScore;
    private Composite parent;
    public static final String ZERO_SCORE = "0"; //$NON-NLS-1$
    private TabFolder numberTabs = null;
    private MouseListener numberSelectedListener;

    @Override
    public void createPartControl(final Composite parent) {
        this.parent = parent;

        Composite playingField = new Composite(parent, SWT.NONE);
        playingField.setLayout(new GridLayout(1, false));
        playingField.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        numberTabs = new TabFolder(playingField, SWT.NONE);

        initNumberSelectionListener();

        Group score = new Group(playingField, SWT.NONE);
        score.setText(Messages.NumberSetView_9);
        score.setLayout(new RowLayout());
        score.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

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

        Group detailedScore = new Group(playingField, SWT.NONE);
        detailedScore.setText(Messages.NumberSetView_14);
        detailedScore.setLayout(new GridLayout());

        GridData gridDataDetScore = new GridData(GridData.FILL, GridData.FILL, true, true);
        detailedScore.setLayoutData(gridDataDetScore);

        scoreTable = new Table(detailedScore, SWT.BORDER);
        scoreTable.setLinesVisible(true);
        scoreTable.setHeaderVisible(true);
        scoreTable.setLayoutData(gridDataDetScore);
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

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, NumberSharkPlugin.PLUGIN_ID + ".view"); //$NON-NLS-1$

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
        int remainingNumbers = numberOfFields - 2;

        boolean isPrime = takenNumber == 0 ? false : NumberService.isPrime(takenNumber);

        TableItem item = new TableItem(scoreTable, SWT.NONE);
        int numberOfRows = scoreTable.getItemCount();
        item.setText(0, String.valueOf(numberOfRows));

        if (isPrime) {
            item.setText(1, String.valueOf(takenNumber) + Messages.NumberSharkView_0);
        } else {
            item.setText(1, String.valueOf(takenNumber));
        }

        if (numberOfRows > 1) {
            score = Integer.parseInt(scoreTable.getItem(numberOfRows - 2).getText(2));
            lostScore = Integer.parseInt(scoreTable.getItem(numberOfRows - 2).getText(4));
            remainingNumbers = Integer.parseInt(scoreTable.getItem(numberOfRows - 2).getText(5)) - 2;
        }

        if (takenNumber == 0) {
            item.setText(1, "-"); //$NON-NLS-1$
            remainingNumbers++;
        }

        item.setText(2, String.valueOf((score + takenNumber)));

        String lostNum = String.valueOf(lostNumbers[0]);
        int lostSum = lostNumbers[0];
        for (int k = 1; k < lostNumbers.length; k++) {
            lostNum += ", " + lostNumbers[k]; //$NON-NLS-1$
            lostSum += lostNumbers[k];
            remainingNumbers--;
        }

        item.setText(3, lostNum);

        lostScore += lostSum;
        item.setText(4, String.valueOf(lostScore));
        item.setText(5, String.valueOf(remainingNumbers));

        scoreTable.setSelection(numberOfRows - 1);

        sharkScore.setText(String.valueOf(lostScore));

        if (remainingNumbers == 0) {
            String msg;
            score += takenNumber;
            if (score > lostScore) {
                msg = NLS.bind(Messages.NumberSetView_40, new Object[] {score, lostScore});
            } else {
                msg = NLS.bind(Messages.NumberSetView_43, new Object[] {score, lostScore});
            }

            MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
            mb.setText(Messages.NumberSetView_46);
            mb.setMessage(msg);
            mb.open();
        }
    }

    /**
     * Creates the listener for each number on the playing field.
     */
    private void initNumberSelectionListener() {
        numberSelectedListener = new MouseAdapter() {
            public void mouseDown(MouseEvent me) {
                ArrayList<Integer> lostNumbers = new ArrayList<Integer>();
                CLabel choosenNumber = (CLabel) me.getSource();
                int numToDeactivate = Integer.parseInt(choosenNumber.getText());

                choosenNumber.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_GRAY));
                choosenNumber.removeMouseListener(this);

                ArrayList<Integer> divisors = numNum[numToDeactivate - 1].getDivisors();

                for (int n = 0; n < divisors.size(); n++) {
                    int k = divisors.get(n) - 1;
                    if (activeNumbers[k]) {
                        if (k < 41) {
                            numbers[k].setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_GRAY));
                            numbers[k].removeMouseListener(this);
                        }
                        activeNumbers[k] = false;
                        lostNumbers.add(divisors.get(n));
                    }
                }
                activeNumbers[numToDeactivate - 1] = false;

                int[] lostNumbersInt = new int[lostNumbers.size()];
                if (!lostNumbers.isEmpty()) {
                    lostNumbersInt = new int[lostNumbers.size()];
                    for (int i = 0; i < lostNumbers.size(); i++) {
                        lostNumbersInt[i] = lostNumbers.get(i);
                    }
                } else {
                    lostNumbersInt = new int[1];
                    lostNumbersInt[0] = numToDeactivate;
                    numToDeactivate = 0;
                }
                addMoveToTable(numToDeactivate, lostNumbersInt);

                TableItem lastRow = scoreTable.getItem(scoreTable.getItemCount() - 1);

                playerScore.setText(lastRow.getText(2));
            }
        };
    }

    public void cleanPlayingField() {
        for (int i = numberTabs.getItemCount() - 1; i >= 0; i--) {
            numberTabs.getItem(i).dispose();
        }

        for (int i = scoreTable.getItemCount(); i > 0; i--) {
            scoreTable.getItem(i - 1).dispose();
        }
    }

    /**
     * Called when clicking on new game, the field of number is recreated.
     *
     * @param numberOfFields
     */
    public void createPlayingField(int numberOfFields) {
        this.numberOfFields = numberOfFields;

        int minPtsToWin = numberOfFields * (numberOfFields + 1) / 4;
        requiredScore.setText(String.valueOf(minPtsToWin));
        sharkScore.setText(ZERO_SCORE);
        playerScore.setText(ZERO_SCORE);

        numNum = new Number[numberOfFields];
        activeNumbers = new boolean[numberOfFields];
        int evenNumberOfFields = numberOfFields;

        if (numberOfFields % 40 != 0) {
            evenNumberOfFields = numberOfFields + (40 - (numberOfFields % 40));
        }

        numbers = new CLabel[evenNumberOfFields];

        for (int i = 0; i < numberOfFields; i++) {
            numNum[i] = new Number(i + 1);
            activeNumbers[i] = true;
        }

        numberTabs.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        GridLayout numbersLayout = new GridLayout();
        numbersLayout.numColumns = 10;
        numbersLayout.makeColumnsEqualWidth = true;

        int numOfTabs = (numberOfFields - 1) / 40 + 1;

        TabItem[] tabItems = new TabItem[numOfTabs];
        Composite compTabs = new Composite(numberTabs, SWT.NONE);
        compTabs.setLayout(numbersLayout);
        compTabs.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        for (int tabNumber = 0; tabNumber < numOfTabs; tabNumber++) {
            tabItems[tabNumber] = new TabItem(numberTabs, SWT.NONE);
            tabItems[tabNumber].setText(tabNumber * 40 + 1 + "-" + (tabNumber + 1) * 40); //$NON-NLS-1$
            tabItems[tabNumber].setData(new GridData(GridData.FILL, GridData.FILL, true, true));
        }

        tabItems[numOfTabs - 1].setText((numOfTabs - 1) * 40 + 1 + "-" + numberOfFields); //$NON-NLS-1$

        for (int fieldNumber = 0, tabCounter = 0; fieldNumber < numberOfFields; fieldNumber++) {
            numbers[fieldNumber] = new CLabel(compTabs, SWT.CENTER | SWT.SHADOW_OUT);
            numbers[fieldNumber].setText(String.valueOf(fieldNumber + 1));
            numbers[fieldNumber].setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
            numbers[fieldNumber].setFont(FontService.getHugeBoldFont());
            numbers[fieldNumber].setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
            numbers[fieldNumber].setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
            numbers[fieldNumber].addMouseListener(numberSelectedListener);

            if (fieldNumber == numberOfFields - 1 || (fieldNumber + 1) % 40 == 0) {
                if (fieldNumber == numberOfFields - 1) {
                    // add empty (invisible) fields on the last page to create the same page layout as on all other tabs
                    int emptyFields = ((tabCounter + 1) * 40) - numberOfFields;

                    if (emptyFields > 0) {
                        for (int i = 0; i < emptyFields; i++, fieldNumber++) {
                            numbers[fieldNumber] = new CLabel(compTabs, SWT.NONE);
                            numbers[fieldNumber].setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
                        }
                    }
                }

                numberTabs.getItem(tabCounter).setControl(compTabs);

                compTabs = new Composite(numberTabs, SWT.NONE);
                compTabs.setLayout(numbersLayout);
                compTabs.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

                tabCounter++;
            }
        }
    }

    @Override
    public void setFocus() {
        parent.setFocus();
    }

    public void disableNumber(int number) {
        numbers[number].removeMouseListener(numberSelectedListener);
        numbers[number].setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_GRAY));
    }

    public void enableNumber(int number) {
        numbers[number].addMouseListener(numberSelectedListener);
        numbers[number].setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
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

    public int getSelectedTabFolderIndex() {
        return numberTabs.getSelectionIndex();
    }

    public void setStatus(int index, boolean status) {
        activeNumbers[index] = status;
    }

    public boolean[] getActiveNumbers() {
        return activeNumbers;
    }

    public Number[] getNumNum() {
        return numNum;
    }
}

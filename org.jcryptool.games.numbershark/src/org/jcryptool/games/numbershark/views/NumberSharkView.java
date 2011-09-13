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
import org.eclipse.swt.custom.StyledText;
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
import org.jcryptool.games.numbershark.NumberSharkPlugin;

/**
 * @author Johannes Späth
 * @author jojospaeth@gmx.de
 * @version 0.1.2, 04.04.2011 Calculation of optimal strategy / help
 */
public class NumberSharkView extends ViewPart {
    private int numberOfFields = 40;
    private Table scoreTable;
    private TableColumn[] column = new TableColumn[6];
    private CLabel[] numbers = new CLabel[numberOfFields];
    private boolean[] activeNumbers = new boolean[numberOfFields];
    private Number[] numNum;
    private StyledText sharkPts;
    private StyledText yourPts;
    private StyledText minToWinPts;
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
        createPlayingField(numberOfFields);

        Group score = new Group(playingField, SWT.NONE);
        score.setText(Messages.NumberSetView_9);
        RowLayout scoreRowLay = new RowLayout();
        scoreRowLay.justify = true;
        score.setLayout(scoreRowLay);
        score.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        RowData fieldData = new RowData(60, 15);
        RowData textData = new RowData(120, 15);

        Label minToWin = new Label(score, SWT.RIGHT);
        minToWin.setText(Messages.NumberSetView_10);
        minToWin.setLayoutData(textData);

        minToWinPts = new StyledText(score, SWT.BORDER);
        minToWinPts.setText("410"); //$NON-NLS-1$
        minToWinPts.setLayoutData(fieldData);

        Label sharkPtsLabel = new Label(score, SWT.RIGHT);
        sharkPtsLabel.setText(Messages.NumberSetView_12);
        sharkPtsLabel.setLayoutData(textData);

        sharkPts = new StyledText(score, SWT.BORDER);
        sharkPts.setText(ZERO_SCORE);
        sharkPts.setLayoutData(fieldData);

        Label yourPtsLabel = new Label(score, SWT.RIGHT);
        yourPtsLabel.setText(Messages.NumberSetView_13);
        yourPtsLabel.setLayoutData(textData);

        yourPts = new StyledText(score, SWT.BORDER);
        yourPts.setLayoutData(fieldData);

        Group detailedScore = new Group(playingField, SWT.NONE);
        detailedScore.setText(Messages.NumberSetView_14);
        detailedScore.setLayout(new GridLayout());

        GridData gridDataDetScore = new GridData(GridData.FILL, GridData.FILL, true, true);
        detailedScore.setLayoutData(gridDataDetScore);

        scoreTable = new Table(detailedScore, SWT.BORDER);
        scoreTable.setLinesVisible(true);
        scoreTable.setHeaderVisible(true);
        scoreTable.setLayoutData(gridDataDetScore);

        for (int i = 0; i < 6; i++) {
            column[i] = new TableColumn(scoreTable, SWT.NONE);
        }

        column[0].setText(Messages.NumberSetView_15);
        column[1].setText(Messages.NumberSetView_16);
        column[2].setText(Messages.NumberSetView_17);
        column[3].setText(Messages.NumberSetView_18);
        column[4].setText(Messages.NumberSetView_19);
        column[5].setText(Messages.NumberSetView_20);

        for (int i = 0; i < 6; i++) {
            column[i].pack();
        }

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

        TableItem item = new TableItem(scoreTable, SWT.NONE);
        int l = scoreTable.getItemCount();
        item.setText(0, "" + l); //$NON-NLS-1$

        item.setText(1, "" + takenNumber); //$NON-NLS-1$

        if (l > 1) {
            score = Integer.parseInt(scoreTable.getItem(l - 2).getText(2));
            lostScore = Integer.parseInt(scoreTable.getItem(l - 2).getText(4));
            remainingNumbers = Integer.parseInt(scoreTable.getItem(l - 2).getText(5)) - 2;
        }

        if (takenNumber == 0) {
            item.setText(1, "-"); //$NON-NLS-1$
            remainingNumbers++;
        }

        item.setText(2, "" + (score + takenNumber)); //$NON-NLS-1$

        String lostNum = "" + lostNumbers[0]; //$NON-NLS-1$
        int lostSum = lostNumbers[0];
        for (int k = 1; k < lostNumbers.length; k++) {
            lostNum += ", " + lostNumbers[k]; //$NON-NLS-1$
            lostSum += lostNumbers[k];
            remainingNumbers--;
        }

        item.setText(3, lostNum);

        lostScore += lostSum;
        item.setText(4, "" + lostScore); //$NON-NLS-1$
        item.setText(5, "" + remainingNumbers); //$NON-NLS-1$
        sharkPts.setText("" + lostScore); //$NON-NLS-1$

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
                addMoveToTable(numToDeactivate, lostNumbersInt);

                TableItem lastRow = scoreTable.getItem(scoreTable.getItemCount() - 1);

                yourPts.setText(lastRow.getText(2));
            }
        };
    }

    /**
     * Called when clicking on new game, the field of number is recreated.
     *
     * @param numberOfFields
     */
    public void createPlayingField(int numberOfFields) {
        this.numberOfFields = numberOfFields;

        numNum = new Number[numberOfFields];
        activeNumbers = new boolean[numberOfFields];
        numbers = new CLabel[numberOfFields];

        for (int i = 0; i < numberOfFields; i++) {
            numNum[i] = new Number(i + 1);
            activeNumbers[i] = true;
        }

        for (int i = numberTabs.getItemCount() - 1; i >= 0; i--) {
            numberTabs.getItem(i).dispose();
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

        for (int tabCounter = 0, fieldNumber = 0; fieldNumber < numberOfFields; fieldNumber++) {
            numbers[fieldNumber] = new CLabel(compTabs, SWT.CENTER | SWT.SHADOW_OUT);
            numbers[fieldNumber].setText(String.valueOf(fieldNumber + 1));
            numbers[fieldNumber].setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
            numbers[fieldNumber].setFont(FontService.getHugeBoldFont());
            numbers[fieldNumber].setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
            numbers[fieldNumber].setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
            numbers[fieldNumber].addMouseListener(numberSelectedListener);

            if (fieldNumber == numberOfFields - 1 || (fieldNumber + 1) % 40 == 0) {
                compTabs.layout();

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

    public void setSharkPtsText(String text) {
        sharkPts.setText(text);
    }

    public void setYourPtsText(String text) {
        yourPts.setText(text);
    }

    public void setMinToWinPtsText(String text) {
        minToWinPts.setText(text);
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

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.numbershark.views;

import java.util.ArrayList;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.games.numbershark.NumberSharkPlugin;

/**
 * @author Johannes Späth
 * @author jojospaeth@gmx.de
 * @version 0.1.2, 04.04.2011 Calculation of optimal strategy / help
 */
public class NumberSetView extends ViewPart {
    private int gameNMax = 40;
    private Table tableScore;
    private TableColumn[] column = new TableColumn[6];
    private Button[] number = new Button[gameNMax];
    private boolean[] actNum = new boolean[gameNMax];
    private Number[] numNum;
    private StyledText sharkPts;
    private StyledText yourPts;
    private StyledText minToWinPts;
    private Button undo;
    private Composite parent;
    private static final String ZERO_SCORE = "0"; //$NON-NLS-1$

    // set maximal number to play with
    final void setNMax(int n) {
        gameNMax = n;
    }

    // get actual number you are playing with
    private int getNMax() {
        return gameNMax;
    }

    @Override
    public void createPartControl(final Composite parent) {
        this.parent = parent;

        // basic layout
        GridLayout gridLayout = new GridLayout(2, false);

        parent.setLayout(gridLayout);

        final Composite leftCol = new Composite(parent, SWT.NONE);
        final Composite rightCol = new Composite(parent, SWT.NONE);

        leftCol.setLayout(new GridLayout(1, false));
        leftCol.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, true));
        rightCol.setLayout(new GridLayout(1, false));
        rightCol.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        GridData buttonData = new GridData();
        buttonData.widthHint = 180;
        buttonData.heightHint = 40;

        Group maximumNumberGroup = new Group(leftCol, SWT.NONE);
        maximumNumberGroup.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
        maximumNumberGroup.setLayout(new GridLayout(2, false));
        maximumNumberGroup.setText(Messages.NumberSetView_1);

        final Slider numSlider = new Slider(maximumNumberGroup, SWT.NONE);
        numSlider.setValues(this.getNMax(), 1, 1031, 7, 1, 10);
        numSlider.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        final Spinner numSpinner = new Spinner(maximumNumberGroup, SWT.BORDER);
        numSpinner.setValues(this.getNMax(), 1, 1024, 0, 1, 10);

        Group newGameGroup = new Group(leftCol, SWT.NONE);
        newGameGroup.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
        newGameGroup.setLayout(new GridLayout(1, false));
        newGameGroup.setText(Messages.NumberSetView_2);

        Button newGame = new Button(newGameGroup, SWT.PUSH);
        newGame.setText(Messages.NumberSetView_3);
        newGame.setLayoutData(buttonData);

        Group optionsGroup = new Group(leftCol, SWT.NONE);
        optionsGroup.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
        optionsGroup.setLayout(new GridLayout(1, false));
        optionsGroup.setText(Messages.NumberSetView_4);

        undo = new Button(optionsGroup, SWT.PUSH);
        undo.setText(Messages.NumberSetView_5);
        undo.setLayoutData(buttonData);
        undo.setEnabled(false);

        Button sharkMeal = new Button(optionsGroup, SWT.PUSH);
        sharkMeal.setText(Messages.NumberSetView_6);
        sharkMeal.setLayoutData(buttonData);

        Button hint = new Button(optionsGroup, SWT.PUSH);
        hint.setText(Messages.NumberSetView_7);
        hint.setLayoutData(buttonData);

        Group playButtons = new Group(rightCol, SWT.NONE);
        playButtons.setText(Messages.NumberSetView_8);
        playButtons.setLayout(new GridLayout(1, false));
        playButtons.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        final TabFolder numberTabs = new TabFolder(playButtons, SWT.NONE);

        createPlayingField(gameNMax, numberTabs);

        Group score = new Group(rightCol, SWT.NONE);
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

        Group detailedScore = new Group(rightCol, SWT.NONE);
        detailedScore.setText(Messages.NumberSetView_14);
        detailedScore.setLayout(new GridLayout());

        GridData gridDataDetScore = new GridData(GridData.FILL, GridData.FILL, true, true);
        detailedScore.setLayoutData(gridDataDetScore);

        tableScore = new Table(detailedScore, SWT.BORDER);
        tableScore.setLinesVisible(true);
        tableScore.setHeaderVisible(true);
        tableScore.setLayoutData(gridDataDetScore);

        for (int i = 0; i < 6; i++) {
            column[i] = new TableColumn(tableScore, SWT.NONE);
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

        // Listeners
        numSpinner.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                numSlider.setSelection(numSpinner.getSelection());
                setNMax(numSlider.getSelection());
            }

        });

        numSlider.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                numSpinner.setSelection(numSlider.getSelection());
                setNMax(numSlider.getSelection());
            }
        });

        newGame.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                undo.setEnabled(false);
                int minPtsToWin = gameNMax * (gameNMax + 1) / 4;
                minToWinPts.setText("" + minPtsToWin); //$NON-NLS-1$
                sharkPts.setText(ZERO_SCORE);
                yourPts.setText(ZERO_SCORE);

                for (int i = tableScore.getItemCount(); i > 0; i--) {
                    tableScore.getItem(i - 1).dispose();
                }

                createPlayingField(gameNMax, numberTabs);
            }
        });

        undo.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {

                int lastRow = tableScore.getItemCount();
                ArrayList<Integer> undoNumbers = new ArrayList<Integer>();
                TableItem row = tableScore.getItem(lastRow - 1);
                String undoLostNumbers = row.getText(3);
                int iterator = undoLostNumbers.lastIndexOf(", "); //$NON-NLS-1$

                int j = numberTabs.getSelectionIndex();

                // reactivate numbers and buttons
                while (iterator != -1) {
                    int toEnable =
                            Integer.parseInt(undoLostNumbers.substring(iterator + 2,
                                    undoLostNumbers.length()));
                    undoLostNumbers = undoLostNumbers.substring(0, iterator);
                    undoNumbers.add(toEnable);
                    actNum[toEnable - 1] = true;
                    numNum[toEnable - 1].setEnabled(true);
                    iterator = undoLostNumbers.lastIndexOf(", "); //$NON-NLS-1$
                    if (j * 40 < toEnable && toEnable < (j + 1) * 40 + 1) {
                        number[toEnable - 1].setEnabled(true);
                    }
                }

                int toEnable = Integer.parseInt(undoLostNumbers);
                actNum[toEnable - 1] = true;
                numNum[toEnable - 1].setEnabled(true);
                if (j * 40 < toEnable && toEnable < (j + 1) * 40 + 1) {
                    number[toEnable - 1].setEnabled(true);
                }

                String str_takenNumber = row.getText(1);
                if (str_takenNumber.equals("-")) { //$NON-NLS-1$
                } else {
                    int takenNumber = Integer.parseInt(row.getText(1));
                    actNum[takenNumber - 1] = true;
                    numNum[takenNumber - 1].setEnabled(true);
                    if (j * 40 < takenNumber && takenNumber < (j + 1) * 40 + 1) {
                        number[takenNumber - 1].setEnabled(true);
                    }
                }

                tableScore.getItem(lastRow - 1).dispose();

                // set the previous score
                if (tableScore.getItemCount() > 0) {
                    TableItem previousRow = tableScore.getItem(tableScore.getItemCount() - 1);
                    sharkPts.setText(previousRow.getText(4));
                    yourPts.setText(previousRow.getText(2));
                } else {
                    sharkPts.setText(ZERO_SCORE);
                    yourPts.setText(ZERO_SCORE);
                }

                // activate undo button if possible
                if (tableScore.getItemCount() > 0) {
                    undo.setEnabled(true);
                } else {
                    undo.setEnabled(false);
                }
            }
        });

        sharkMeal.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                ArrayList<Integer> sharkMealList = new ArrayList<Integer>();
                int[] lostNumbers;

                // calculate numbers to be eaten by the shark
                for (int i = gameNMax; i > gameNMax / 2; i--) {
                    if (actNum[i - 1]) {
                        int counter = 0;
                        boolean stop = false;
                        for (int j = 0; j < numNum[i - 1].divisors.size(); j++) {
                            int n = numNum[i - 1].divisors.get(j);
                            if (actNum[n - 1]) {
                                counter++;
                                if (counter > 0) {
                                    stop = true;
                                    break;
                                }
                            }
                        }
                        if (!stop) {
                            sharkMealList.add(i);
                            actNum[i - 1] = false;
                            numNum[i - 1].setEnabled(false);

                            int j = numberTabs.getSelectionIndex();
                            if (j * 40 < i && i < (j + 1) * 40 + 1) {
                                number[i - 1].setEnabled(false);
                            }
                        }
                    }
                }

                if (sharkMealList.size() > 0) {
                    lostNumbers = new int[sharkMealList.size()];
                    for (int i = 0; i < lostNumbers.length; i++) {
                        lostNumbers[i] = sharkMealList.get(i);
                    }

                    addMoveToTable(0, lostNumbers);
                }
            }

        });

        hint.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                boolean stop = false;
                String msg;
                int hint = 0;

                // calculate number for the hint
                for (int i = gameNMax; i > gameNMax / 2; i--) {
                    if (actNum[i - 1]) {
                        int counter = 0;

                        for (int j = 0; j < numNum[i - 1].divisors.size(); j++) {
                            int n = numNum[i - 1].divisors.get(j);
                            if (actNum[n - 1]) {
                                counter++;
                            }

                        }
                        if (counter == 1) {
                            hint = i;
                            stop = true;
                            break;
                        }

                    }
                }

                if (stop) {
                    msg = NLS.bind(Messages.NumberSetView_25, new Object[] {hint, gameNMax / 2});
                } else {
                    msg = NLS.bind(Messages.NumberSetView_28, new Object[] {gameNMax / 2});
                }

                MessageBox mb =
                        new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION
                                | SWT.OK);
                mb.setText(Messages.NumberSetView_30);
                mb.setMessage(msg);
                mb.open();
            }

        });

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
                NumberSharkPlugin.PLUGIN_ID + ".view"); //$NON-NLS-1$
    }

    // function adds the last move to the score table
    private void addMoveToTable(int takenNumber, int[] lostNumbers) {
        int score = 0;
        int lostScore = 0;
        int remainingNumbers = gameNMax - 2;

        TableItem item = new TableItem(tableScore, SWT.NONE);
        int l = tableScore.getItemCount();
        item.setText(0, "" + l); //$NON-NLS-1$

        item.setText(1, "" + takenNumber); //$NON-NLS-1$

        if (l > 1) {
            score = Integer.parseInt(tableScore.getItem(l - 2).getText(2));
            lostScore = Integer.parseInt(tableScore.getItem(l - 2).getText(4));
            remainingNumbers = Integer.parseInt(tableScore.getItem(l - 2).getText(5)) - 2;
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
            MessageBox mb =
                    new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION
                            | SWT.OK);
            mb.setText(Messages.NumberSetView_46);
            mb.setMessage(msg);
            mb.open();

        }

    }

    // method called when clicking on new game, the field of number is recreated
    private void createPlayingField(int selection, TabFolder numberTabs) {

        for (int i = 0; i < number.length; i++) {
            if (number[i] != null && !number[i].isDisposed()) {
                number[i].dispose();
            }
        }

        numNum = new Number[gameNMax];
        actNum = new boolean[gameNMax];

        for (int i = 1; i <= gameNMax; i++) {
            numNum[i - 1] = new Number(i);
            actNum[i - 1] = true;
        }

        for (int i = numberTabs.getItemCount() - 1; i >= 0; i--) {
            numberTabs.getItem(i).dispose();
        }

        numberTabs.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        GridLayout numbersLayout = new GridLayout();
        numbersLayout.numColumns = 10;
        numbersLayout.makeColumnsEqualWidth = true;

        GridData numbersData = new GridData(GridData.FILL, GridData.FILL, true, true);
        number = new Button[selection];
        int numOfTabs = (selection - 1) / 40 + 1;
        int k = 1;

        TabItem[] tab = new TabItem[numOfTabs];
        Composite compTabs = new Composite(numberTabs, SWT.NONE);
        compTabs.setLayout(numbersLayout);
        compTabs.setLayoutData(numbersData);

        for (int j = 0; j < numOfTabs; j++) {

            tab[j] = new TabItem(numberTabs, SWT.NONE);
            tab[j].setText(j * 40 + 1 + "-" + (j + 1) * 40); //$NON-NLS-1$

            tab[j].setData(numbersData);
        }

        tab[numOfTabs - 1].setText((numOfTabs - 1) * 40 + 1 + "-" + gameNMax); //$NON-NLS-1$

        for (int i = 0; i < 40; i++) {
            if (k <= selection) {
                number[k - 1] = new Button(compTabs, SWT.PUSH);
                number[k - 1].setText("" + k); //$NON-NLS-1$
                number[k - 1].setLayoutData(numbersData);
                // number[k-1].setToolTipText(calculateToolTips(k));
                // number[k-1].setEnabled(numNum[k-1].isEnabled());
                k++;
            }
        }

        numberTabs.getItem(0).setData(numbersData);
        numberTabs.getItem(0).setControl(compTabs);
        compTabs.layout();

        // listener for each button on the playing field
        final SelectionListener buttonPress = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent se) {
                ArrayList<Integer> lostNumbers = new ArrayList<Integer>();
                Button pressedButton = (Button) se.getSource();
                int numToDeactivate = Integer.parseInt(pressedButton.getText());

                pressedButton.setEnabled(false);

                ArrayList<Integer> divisors = numNum[numToDeactivate - 1].divisors;

                for (int n = 0; n < divisors.size(); n++) {
                    int k = divisors.get(n) - 1;
                    if (actNum[k]) {
                        if (k < 41) {
                            number[k].setEnabled(false);
                        }
                        actNum[k] = false;
                        lostNumbers.add(divisors.get(n));
                    }
                }
                actNum[numToDeactivate - 1] = false;

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

                TableItem lastRow = tableScore.getItem(tableScore.getItemCount() - 1);

                yourPts.setText(lastRow.getText(2));

                undo.setEnabled(true);
            }
        };

        for (int i = 0; i < 40; i++) {
            number[i].addSelectionListener(buttonPress);
        }

        Listener tabsSelect = new Listener() {
            public void handleEvent(Event arg0) {
                if (arg0.type == SWT.Selection) {
                    TabFolder numberTabs = (TabFolder) arg0.widget;
                    int j = numberTabs.getSelectionIndex();
                    GridData numbersData = new GridData(GridData.FILL, GridData.FILL, true, true);
                    Composite compTabs = new Composite(numberTabs, SWT.NONE);

                    GridLayout numbersLayout = new GridLayout();
                    numbersLayout.numColumns = 10;
                    numbersLayout.makeColumnsEqualWidth = true;

                    compTabs.setLayout(numbersLayout);
                    compTabs.setLayoutData(numbersData);

                    numberTabs.getItem(j).setData(numbersData);
                    numberTabs.getItem(j).setControl(compTabs);
                }

            }

        };

        numberTabs.addListener(SWT.Selection, tabsSelect);
    }

    @Override
    public void setFocus() {
        parent.setFocus();
    }
}

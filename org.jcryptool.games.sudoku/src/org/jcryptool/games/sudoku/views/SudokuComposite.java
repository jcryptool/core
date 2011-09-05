// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.sudoku.views;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.games.sudoku.Messages;
import org.jcryptool.games.sudoku.SudokuPlugin;

public class SudokuComposite extends Composite {

    public Display display;

    public final int NORMAL = 1, KILLER = 2, HEX = 3;

    public final int BOX_SIZE_NORMAL = 50, BOX_SIZE_KILLER = 50, BOX_SIZE_HEX = 40;
    final private int ADDITION = 0, SUBTRACTION = 1, MULTIPLICATION = 2, DIVISION = 3;

    public int tabChoice, numberOfGuesses = 0;

    public int[][] boardNormal, boardKiller, boardHex, tempBoardHex;

    public Label[][][] boardLabelsNormal, boardLabelsKiller, boardLabelsHex;

    public Text[][] boardTextNormal, boardTextKiller, boardTextHex;

    public List<List<List<Integer>>> possibleNormal, possibleKiller, possibleHex, tempPossibleHex;

    public Color WHITE, GREEN, GRAY, RED, BLACK, BLUE;

    public Button solveButton, showPossibleButton, autoFillOneButton, loadButton, saveButton,
            boxRuleButton, loadStandardPuzzle;
    public Button onePossibleButton, nakedSingleButton, hiddenSingleButton, blockAndCRButton,
            nakedSubsetButton, candidateLineButton, doublePairButton, multipleLinesButton;
    public Button additionButton, subtractionButton, multiplicationButton, divisionButton;

    Map<Text, UserInputPoint> inputBoxesNormal = new HashMap<Text, UserInputPoint>();

    Map<Text, UserInputPoint> inputBoxesKiller = new HashMap<Text, UserInputPoint>();

    Map<Composite, Point> compositeBoxesNormal = new HashMap<Composite, Point>();

    Map<Composite, Point> compositeBoxesKiller = new HashMap<Composite, Point>();

    Map<Composite, Point> compositeBoxesHex = new HashMap<Composite, Point>();

    Map<Text, UserInputPoint> inputBoxesHex = new HashMap<Text, UserInputPoint>();

    public List<Point> selected;

    public List<Area> areas;

    public Composite[][] labelCellNormal, labelCellKiller, labelCellHex;

    public Composite playField;

    public boolean showPossible, autoFillOne, solved, loading, solving, boxRule,
            killerFirstPossible;

    public Runnable refresh;

    public Thread blinkerRed = null, blinkerWhite = null, makeWhite;

    public Random rnd;

    public SudokuComposite(final Composite parent, final int tabChoice, final int style) {
        super(parent, style);
        this.display = SudokuComposite.this.getDisplay();
        this.tabChoice = tabChoice;
        this.initialize();
        this.WHITE = this.display.getSystemColor(SWT.COLOR_WHITE);
        this.GREEN = this.display.getSystemColor(SWT.COLOR_GREEN);
        this.GRAY = this.display.getSystemColor(SWT.COLOR_GRAY);
        this.RED = this.display.getSystemColor(SWT.COLOR_RED);
        this.BLACK = this.display.getSystemColor(SWT.COLOR_BLACK);
        this.BLUE = this.display.getSystemColor(SWT.COLOR_BLUE);
        this.showPossible = true;
        this.boxRule = false;
        this.killerFirstPossible = false;
        this.autoFillOne = false;
        this.solved = false;
        this.loading = false;
        this.rnd = new Random(System.currentTimeMillis());

        this.refresh = new Runnable() {
            public void run() {
                switch (tabChoice) {
                    case NORMAL:
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                labelCellNormal[i][j].layout();
                            }
                        }
                        break;
                    case KILLER:
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                labelCellKiller[i][j].layout();
                            }
                        }
                        break;
                    case HEX:
                        for (int i = 0; i < 16; i++) {
                            for (int j = 0; j < 16; j++) {
                                labelCellHex[i][j].layout();
                            }
                        }
                        break;
                }
            }
        };
    }

    public void startBlinkingArea(final int x, final int y) {
        blinkerRed = new Thread() {
            public void run() {
            	switch (tabChoice) {
                	case NORMAL:
                    labelCellNormal[x][y].setBackground(RED); break;
                	case KILLER:
                    labelCellKiller[x][y].setBackground(RED); break;
                	case HEX:
                    labelCellHex[x][y].setBackground(RED); break;
            	}
            	try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
				}
            }
        };
        blinkerWhite = new Thread() {
            public void run() {
            	switch (tabChoice) {
                	case NORMAL:
                    labelCellNormal[x][y].setBackground(WHITE); break;
                	case KILLER:
                    labelCellKiller[x][y].setBackground(WHITE); break;
                	case HEX:
                    labelCellHex[x][y].setBackground(WHITE); break;
            	}
            	try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
				}
            }
        };
        for (int i = 0; i < 3; i++) {
	        SudokuComposite.this.getDisplay().asyncExec(blinkerRed);
	        SudokuComposite.this.getDisplay().asyncExec(blinkerWhite);
        }
    }

    public void makeWhite() {
    	this.makeWhite = new Thread() {
            public void run() {
                switch (tabChoice) {
                    case NORMAL:
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                labelCellNormal[i][j].setBackground(WHITE);
                                for (int k = 0; k < 8; k++) {
                                	boardLabelsNormal[i][j][k].setBackground(WHITE);
                                }
                            }
                        }
                        break;
                    case KILLER:
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                labelCellKiller[i][j].setBackground(WHITE);
                                for (int k = 0; k < 8; k++) {
                                	boardLabelsKiller[i][j][k].setBackground(WHITE);
                                }
                            }
                        }
                        break;
                    case HEX:
                        for (int i = 0; i < 16; i++) {
                            for (int j = 0; j < 16; j++) {
                                labelCellHex[i][j].setBackground(WHITE);
                                for (int k = 0; k < 8; k++) {
                                	boardLabelsHex[i][j][k].setBackground(WHITE);
                                }
                            }
                        }
                        break;
                }
            }
        };
        SudokuComposite.this.getDisplay().asyncExec(makeWhite);
    }

    public void initialize() {
        this.setLayout(new GridLayout());
        this.createHead();
        this.createMain();
        if (tabChoice == KILLER)
            this.boxRuleButton.setBackground(RED);
        this.showPossibleButton.setBackground(GREEN);
    }

    public void createHead() {
        final Composite head = new Composite(this, SWT.NONE);
        head.setBackground(WHITE);
        head.setLayout(new GridLayout());

        final Label label = new Label(head, SWT.NONE);
        label.setFont(FontService.getHeaderFont());
        label.setBackground(WHITE);

        /** Deals with the choice of scheme */
        switch (tabChoice) {
            case NORMAL:
                label.setText(Messages.SudokuComposite_Normal_Title);
                break;
            case KILLER:
                label.setText(Messages.SudokuComposite_Killer_Title);
                break;
            case HEX:
                label.setText(Messages.SudokuComposite_Hex_Title);
                break;
        }

        final StyledText stDescription = new StyledText(head, SWT.READ_ONLY);
        switch (tabChoice) {
            case NORMAL:
                stDescription.setText(Messages.SudokuComposite_Normal_Desc);
                break;
            case KILLER:
                stDescription.setText(Messages.SudokuComposite_Killer_Desc);
                break;
            case HEX:
                stDescription.setText(Messages.SudokuComposite_Hex_Desc);
                break;
        }
        stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    }

    public void createMain() {
        final Group g = new Group(this, SWT.NONE);
        g.setLayout(new GridLayout(2, false));
        g.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
        g.setText(Messages.SudokuComposite_MainGroup_Title);
        this.createButtonArea(g);
        this.createPlayFieldArea(g);
        makeWhite();
    }

    public void createButtonArea(final Composite parent) {
        final Composite mainComposite = new Composite(parent, SWT.SHADOW_NONE);
        mainComposite.setLayout(new GridLayout());
        mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final RowLayout mrl = new RowLayout(SWT.VERTICAL);
        final RowData buttonrd = new RowData(130, 30);

        Group subComposite = new Group(mainComposite, SWT.SHADOW_NONE);
        subComposite.setText("Actions");
        subComposite.setLayout(mrl);

        this.solveButton = new Button(subComposite, SWT.PUSH);
        this.solveButton.setLayoutData(buttonrd);
        this.solveButton.setEnabled(true);
        this.solveButton.setText(Messages.SudokuComposite_SolveButton);
        this.solveButton.setToolTipText(Messages.SudokuComposite_SolveButton_Tooltip);
        this.solveButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                switch (tabChoice) {
                    case NORMAL: {
                        if (SudokuComposite.this.solvePuzzleNormal()) {
                            refresh();
                        }
                    }
                        break;
                    case KILLER: {
                        if (SudokuComposite.this.solvePuzzleKiller()) {
                            refresh();
                        }
                    }
                        break;
                    case HEX: {
                        if (SudokuComposite.this.solvePuzzleHex()) {
                            refresh();
                        }
                    }
                        break;
                }
            }
        });

        if (tabChoice == KILLER) {
            this.boxRuleButton = new Button(subComposite, SWT.PUSH);
            this.boxRuleButton.setLayoutData(buttonrd);
            this.boxRuleButton.setBackground(GREEN);
            this.boxRuleButton.setEnabled(true);
            this.boxRuleButton.setText("BoxRule");
            this.boxRuleButton.setToolTipText("BoxRule");
            this.boxRuleButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    if (SudokuComposite.this.boxRule) {
                        SudokuComposite.this.boxRule = false;
                        SudokuComposite.this.boxRuleButton.setBackground(RED);
                    } else {
                        SudokuComposite.this.boxRule = true;
                        SudokuComposite.this.boxRuleButton.setBackground(GREEN);
                    }
                    refresh();
                }
            });
        }

        this.showPossibleButton = new Button(subComposite, SWT.PUSH);
        this.showPossibleButton.setLayoutData(buttonrd);
        this.showPossibleButton.setBackground(RED);
        this.showPossibleButton.setEnabled(true);
        this.showPossibleButton.setText(Messages.SudokuComposite_ShowPossibleButton);
        this.showPossibleButton.setToolTipText(Messages.SudokuComposite_ShowPossibleButton_Tooltip);
        this.showPossibleButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                if (SudokuComposite.this.showPossible) {
                    SudokuComposite.this.showPossible = false;
                    SudokuComposite.this.showPossibleButton.setBackground(RED);
                } else {
                    SudokuComposite.this.showPossible = true;
                    SudokuComposite.this.showPossibleButton.setBackground(GREEN);
                }
                switch (tabChoice) {
                    case NORMAL:
                        SudokuComposite.this.updatePossibilitiesNormal();
                        break;
                    case KILLER:
                        SudokuComposite.this.updatePossibilitiesKiller();
                        break;
                    case HEX:
                        SudokuComposite.this.updatePossibilitiesHex();
                        break;
                }
                refresh();
            }
        });

        this.autoFillOneButton = new Button(subComposite, SWT.PUSH);
        this.autoFillOneButton.setLayoutData(buttonrd);
        this.autoFillOneButton.setEnabled(true);
        this.autoFillOneButton.setText(Messages.SudokuComposite_AutoFillOneButton);
        this.autoFillOneButton.setToolTipText(Messages.SudokuComposite_AutoFillOneButton_Tooltip);
        this.autoFillOneButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                switch (tabChoice) {
                    case NORMAL:
                        SudokuComposite.this.fillOneNormal();
                        break;
                    case KILLER:
                        SudokuComposite.this.fillOneKiller();
                        break;
                    case HEX:
                        SudokuComposite.this.fillOneHex();
                        break;
                }
                refresh();
            }
        });

        this.loadStandardPuzzle = new Button(subComposite, SWT.PUSH);
        this.loadStandardPuzzle.setLayoutData(buttonrd);
        this.loadStandardPuzzle.setText(Messages.SudokuComposite_loadStandardPuzzle);
        this.loadStandardPuzzle.setToolTipText(Messages.SudokuComposite_loadStandardPuzzle_Tooltip);
        this.loadStandardPuzzle.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
            	int puzzle;
            	URL fileName = null;
				try {
					fileName = FileLocator.toFileURL((SudokuPlugin.getDefault().getBundle().getEntry("/")));
				} catch (IOException ex) {
					LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
				}
				StringBuilder path = new StringBuilder();
                path.append(fileName.getFile());
                path.append("data/");
                switch (tabChoice) {
                    case NORMAL:
                    	puzzle = rnd.nextInt(6) + 1;
                    	path.append("sudoku" + puzzle + ".sud");
                        loadNormal(path.toString());
                        break;
                    case KILLER:
                    	puzzle = rnd.nextInt(2) + 1;
                    	path.append("killer" + puzzle + ".sud");
                        loadKiller(path.toString());
                        break;
                    case HEX:
                    	puzzle = rnd.nextInt(3) + 1;
                    	path.append("hex" + puzzle + ".sud");
                        loadHex(path.toString());
                        break;
                }
                refresh();
            }
        });

        this.loadButton = new Button(subComposite, SWT.PUSH);
        this.loadButton.setLayoutData(buttonrd);
        this.loadButton.setEnabled(true);
        this.loadButton.setText(Messages.SudokuComposite_LoadButton);
        this.loadButton.setToolTipText(Messages.SudokuComposite_LoadButton_Tooltip);
        this.loadButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                switch (tabChoice) {
                    case NORMAL:
                        loadPuzzleNormal();
                        break;
                    case KILLER:
                        loadPuzzleKiller();
                        break;
                    case HEX:
                        loadPuzzleHex();
                        break;
                }
                refresh();
            }
        });

        this.saveButton = new Button(subComposite, SWT.PUSH);
        this.saveButton.setLayoutData(buttonrd);
        this.saveButton.setEnabled(true);
        this.saveButton.setText(Messages.SudokuComposite_SaveButton);
        this.saveButton.setToolTipText(Messages.SudokuComposite_SaveButton_Tooltip);
        this.saveButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                switch (tabChoice) {
                    case NORMAL:
                        savePuzzleNormal();
                        break;
                    case KILLER:
                        savePuzzleKiller();
                        break;
                    case HEX:
                        savePuzzleHex();
                        break;
                }
            }
        });

        if (tabChoice == HEX) {
            subComposite = new Group(mainComposite, SWT.SHADOW_NONE);
            subComposite.setText("Strategies");
            subComposite.setLayout(mrl);
            this.onePossibleButton = new Button(subComposite, SWT.PUSH);
            this.onePossibleButton.setLayoutData(buttonrd);
            this.onePossibleButton.setEnabled(true);
            this.onePossibleButton.setText(Messages.SudokuComposite_OnePossibleButton);
            this.onePossibleButton.setToolTipText(Messages.SudokuComposite_OnePossibleButton_Tooltip);
            this.onePossibleButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    onePossibleHex();
                    updateLabelsHex();
                }
            });

            this.nakedSingleButton = new Button(subComposite, SWT.PUSH);
            this.nakedSingleButton.setLayoutData(buttonrd);
            this.nakedSingleButton.setEnabled(true);
            this.nakedSingleButton.setText(Messages.SudokuComposite_NakedSingleButton);
            this.nakedSingleButton.setToolTipText(Messages.SudokuComposite_NakedSingleButton_Tooltip);
            this.nakedSingleButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    nakedSingleHex();
                    updateLabelsHex();
                }
            });

            this.hiddenSingleButton = new Button(subComposite, SWT.PUSH);
            this.hiddenSingleButton.setLayoutData(buttonrd);
            this.hiddenSingleButton.setEnabled(true);
            this.hiddenSingleButton.setText(Messages.SudokuComposite_HiddenSingleButton);
            this.hiddenSingleButton.setToolTipText(Messages.SudokuComposite_HiddenSingleButton_Tooltip);
            this.hiddenSingleButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    hiddenSingleHex();
                    updateLabelsHex();
                }
            });

            this.blockAndCRButton = new Button(subComposite, SWT.PUSH);
            this.blockAndCRButton.setLayoutData(buttonrd);
            this.blockAndCRButton.setEnabled(true);
            this.blockAndCRButton.setText(Messages.SudokuComposite_BlockAndCRButton);
            this.blockAndCRButton.setToolTipText(Messages.SudokuComposite_BlockAndCRButton_Tooltip);
            this.blockAndCRButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    blockAndCRHex();
                    updateLabelsHex();
                }
            });

            this.nakedSubsetButton = new Button(subComposite, SWT.PUSH);
            this.nakedSubsetButton.setLayoutData(buttonrd);
            this.nakedSubsetButton.setEnabled(true);
            this.nakedSubsetButton.setText(Messages.SudokuComposite_NakedSubsetButton);
            this.nakedSubsetButton.setToolTipText(Messages.SudokuComposite_NakedSubsetButton_Tooltip);
            this.nakedSubsetButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    nakedSubsetHex();
                    updateLabelsHex();
                }
            });

            this.candidateLineButton = new Button(subComposite, SWT.PUSH);
            this.candidateLineButton.setLayoutData(buttonrd);
            this.candidateLineButton.setEnabled(true);
            this.candidateLineButton.setText(Messages.SudokuComposite_CandidateLineButton);
            this.candidateLineButton.setToolTipText(Messages.SudokuComposite_CandidateLineButton_Tooltip);
            this.candidateLineButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    candidateLineHex();
                    updateLabelsHex();
                }
            });

            this.doublePairButton = new Button(subComposite, SWT.PUSH);
            this.doublePairButton.setLayoutData(buttonrd);
            this.doublePairButton.setEnabled(true);
            this.doublePairButton.setText(Messages.SudokuComposite_DoublePairButton);
            this.doublePairButton.setToolTipText(Messages.SudokuComposite_DoublePairButton_Tooltip);
            this.doublePairButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    doublePairHex();
                    updateLabelsHex();
                }
            });

            this.multipleLinesButton = new Button(subComposite, SWT.PUSH);
            this.multipleLinesButton.setLayoutData(buttonrd);
            this.multipleLinesButton.setEnabled(true);
            this.multipleLinesButton.setText(Messages.SudokuComposite_MultipleLinesButton);
            this.multipleLinesButton.setToolTipText(Messages.SudokuComposite_MultipleLinesButton_Tooltip);
            this.multipleLinesButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    multipleLinesHex();
                    updateLabelsHex();
                }
            });
        }

        if (tabChoice == KILLER) {
            subComposite = new Group(mainComposite, SWT.SHADOW_NONE);
            subComposite.setText("Operators");
            subComposite.setLayout(mrl);
            this.additionButton = new Button(subComposite, SWT.PUSH);
            this.additionButton.setLayoutData(buttonrd);
            this.additionButton.setEnabled(true);
            this.additionButton.setText("Addition");
            this.additionButton.setToolTipText("Addition");
            this.additionButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    if (selected.size() > 0) {
                        InputDialog dlg =
                                new InputDialog(SudokuComposite.this.getShell(), "Value Input",
                                        "Input value", "", (new IInputValidator() {
                                            private final Pattern INTEGER_PATTERN =
                                                    Pattern.compile("[0-9]+");

                                            public String isValid(String newText) {
                                                String toReturn =
                                                        INTEGER_PATTERN.matcher(newText).matches()
                                                                ? null
                                                                : (newText.length() == 0)
                                                                        ? "Please enter an integer."
                                                                        : "'"
                                                                                + newText
                                                                                + "' is not a valid integer.";
                                                return toReturn;
                                            }
                                        }));
                        if (dlg.open() == InputDialog.OK) {
                            int value = Integer.parseInt(dlg.getValue());
                            areas.add(new Area(ADDITION, selected, value));
                        }
                        for (int i = 0; i < selected.size(); i++) {
                            labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(WHITE);
                            boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(WHITE);
                        }
                        selected.clear();
                        updateInitialPossibilitiesKiller();
                        updatePossibilitiesKiller();
                    }
                }
            });

            this.subtractionButton = new Button(subComposite, SWT.PUSH);
            this.subtractionButton.setLayoutData(buttonrd);
            this.subtractionButton.setEnabled(true);
            this.subtractionButton.setText("Subtraction");
            this.subtractionButton.setToolTipText("Subtraction");
            this.subtractionButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    if (selected.size() > 0) {
                        if (selected.size() == 2) {
                            InputDialog dlg =
                                    new InputDialog(SudokuComposite.this.getShell(), "Value Input",
                                            "Input value", "", (new IInputValidator() {
                                                private final Pattern INTEGER_PATTERN =
                                                        Pattern.compile("[0-9]+");

                                                public String isValid(String newText) {
                                                    String toReturn =
                                                            INTEGER_PATTERN.matcher(newText).matches()
                                                                    ? null
                                                                    : (newText.length() == 0)
                                                                            ? "Please enter an integer."
                                                                            : "'"
                                                                                    + newText
                                                                                    + "' is not a valid integer.";
                                                    return toReturn;
                                                }
                                            }));
                            if (dlg.open() == InputDialog.OK) {
                                int value = Integer.parseInt(dlg.getValue());
                                areas.add(new Area(SUBTRACTION, selected, value));
                            }
                        }
                        for (int i = 0; i < selected.size(); i++) {
                            labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(WHITE);
                            boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(WHITE);
                        }
                        selected.clear();
                        updateInitialPossibilitiesKiller();
                        updatePossibilitiesKiller();
                    }
                }
            });

            this.multiplicationButton = new Button(subComposite, SWT.PUSH);
            this.multiplicationButton.setLayoutData(buttonrd);
            this.multiplicationButton.setEnabled(true);
            this.multiplicationButton.setText("Multiplication");
            this.multiplicationButton.setToolTipText("Multiplication");
            this.multiplicationButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    if (selected.size() > 0) {
                        InputDialog dlg =
                                new InputDialog(SudokuComposite.this.getShell(), "Value Input",
                                        "Input value", "", (new IInputValidator() {
                                            private final Pattern INTEGER_PATTERN =
                                                    Pattern.compile("[0-9]+");

                                            public String isValid(String newText) {
                                                String toReturn =
                                                        INTEGER_PATTERN.matcher(newText).matches()
                                                                ? null
                                                                : (newText.length() == 0)
                                                                        ? "Please enter an integer."
                                                                        : "'"
                                                                                + newText
                                                                                + "' is not a valid integer.";
                                                return toReturn;
                                            }
                                        }));
                        if (dlg.open() == InputDialog.OK) {
                            int value = Integer.parseInt(dlg.getValue());
                            areas.add(new Area(MULTIPLICATION, selected, value));
                        }
                        for (int i = 0; i < selected.size(); i++) {
                            labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(WHITE);
                            boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(WHITE);
                        }
                        selected.clear();
                        updateInitialPossibilitiesKiller();
                        updatePossibilitiesKiller();
                    }
                }
            });

            this.divisionButton = new Button(subComposite, SWT.PUSH);
            this.divisionButton.setLayoutData(buttonrd);
            this.divisionButton.setEnabled(true);
            this.divisionButton.setText("Division");
            this.divisionButton.setToolTipText("Division");
            this.divisionButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    if (selected.size() > 0) {
                        if (selected.size() == 2) {
                            InputDialog dlg =
                                    new InputDialog(SudokuComposite.this.getShell(), "Value Input",
                                            "Input value", "", (new IInputValidator() {
                                                private final Pattern INTEGER_PATTERN =
                                                        Pattern.compile("[0-9]+");

                                                public String isValid(String newText) {
                                                    String toReturn =
                                                            INTEGER_PATTERN.matcher(newText).matches()
                                                                    ? null
                                                                    : (newText.length() == 0)
                                                                            ? "Please enter an integer."
                                                                            : "'"
                                                                                    + newText
                                                                                    + "' is not a valid integer.";
                                                    return toReturn;
                                                }
                                            }));
                            if (dlg.open() == InputDialog.OK) {
                                int value = Integer.parseInt(dlg.getValue());
                                areas.add(new Area(DIVISION, selected, value));
                            }
                        }
                        for (int i = 0; i < selected.size(); i++) {
                            labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(WHITE);
                            boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(WHITE);
                        }
                        selected.clear();
                        updateInitialPossibilitiesKiller();
                        updatePossibilitiesKiller();
                    }
                }
            });
        }
    }

    public void loadPuzzleNormal() {
        String fileName = openFileDialog(SWT.OPEN);

        if (fileName == null) {
            return;
        }

        loadNormal(fileName);
    }

    public void loadNormal(String fileName) {
        solved = false;

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            loading = true;
            clearPuzzleNormal();
            StringBuilder puzzle = new StringBuilder("");
            Pattern p = Pattern.compile("[1-9]");
            List<String> specialAll = new ArrayList<String>(), specialDistinct =
                    new ArrayList<String>();
            String emptyCharacter = "0";
            int step = 1;
            String line = reader.readLine();
            while (puzzle.length() != 81) {
                if (line == null) {
                    reader = new BufferedReader(new FileReader(fileName));
                    line = reader.readLine();
                    emptyCharacter = "0";
                    puzzle = new StringBuilder("");
                    specialAll.clear();
                    step++;
                }
                while (line != null) {
                    while (line.length() == 1)
                        line = reader.readLine();
                    for (int i = 0; i < line.length(); i = i + step) {
                        if (p.matcher(line.subSequence(i, i + 1)).matches()) {
                            puzzle.append(line.charAt(i));
                        } else if (line.substring(i, i + 1).equals(emptyCharacter)) {
                            puzzle.append("0");
                        } else {
                            if (i == 0 && step == 2) {
                                for (int j = 1; j < line.length(); j = j + 2) {
                                    if (p.matcher(line.subSequence(j, j + 1)).matches()) {
                                        i--;
                                        break;
                                    }
                                }
                            }
                            if (i != -1) {
                                specialAll.add(line.substring(i, i + 1));
                                if (specialDistinct.indexOf(line.substring(i, i + 1)) == -1)
                                    specialDistinct.add(line.substring(i, i + 1));
                            }
                        }
                    }
                    line = reader.readLine();
                }
                for (int i = 0; i < specialDistinct.size(); i++) {
                    int occ = 0;
                    for (int j = 0; j < specialAll.size(); j++)
                        if (specialAll.get(j).equals(specialDistinct.get(i)))
                            occ++;
                    if (occ + puzzle.length() == 81) {
                        reader = new BufferedReader(new FileReader(fileName));
                        line = reader.readLine();
                        emptyCharacter = specialDistinct.get(i);
                        puzzle = new StringBuilder("");
                        specialAll.clear();
                        specialDistinct.clear();
                        break;
                    }
                }
                if (step > 5)
                    break;
            }
            if (puzzle.length() == 81) {
                int x = 0, y = 0;
                for (int i = 0; i < puzzle.length(); i++) {
                    boardNormal[y][x] = Integer.parseInt(puzzle.substring(i, i + 1));
                    if (boardNormal[y][x] != 0)
                        boardTextNormal[y][x].setText(puzzle.substring(i, i + 1));
                    x++;
                    if (x == 9) {
                        x = 0;
                        y++;
                    }
                }
            }
        } catch (Exception ex) {
            LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
        }

        loading = false;
        updatePossibilitiesNormal();
    }

    private String openFileDialog(int type) {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), type);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterExtensions(new String[] {"*.sud"});
        dialog.setFilterNames(new String[] {"Sudoku Files (*.sud)"});
        dialog.setOverwrite(true);

        return dialog.open();
    }

    public void savePuzzleNormal() {
        String fileName = openFileDialog(SWT.SAVE);

        if (fileName == null) {
            return;
        }

        try {
            FileOutputStream out = new FileOutputStream(fileName);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    out.write(Integer.toString(boardNormal[i][j]).getBytes());
                }
            }
        } catch (Exception ex) {
            LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
        }
    }

    public void loadPuzzleKiller() {
        String fileName = openFileDialog(SWT.OPEN);

        if (fileName == null) {
            return;
        }

        loadKiller(fileName);
    }

    public void loadKiller(String fileName) {
    	solved = false;
        this.killerFirstPossible = false;

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            loading = true;
            clearPuzzleKiller();
            Map<String, Integer> areaName = new HashMap<String, Integer>();
            String line = reader.readLine();
            int ida = 0, idx = -1, idy = -1;
            while (idy < 9) {
                line = reader.readLine();
                if (Character.toString(line.charAt(0)).equals("+"))
                    line = reader.readLine();
                idy++;
                idx = -1;
                for (int j = 0; j < line.length(); j = j + 2) {
                    String val = Character.toString(line.charAt(j));
                    if (!val.equals("|"))
                        idx++;
                    if (!((int) line.charAt(j) == 160 || (int) line.charAt(j) == 32)
                            & !val.equals("|")) {
                        if (areaName.get(val) == null) {
                            areaName.put(val, ida);
                            ida++;
                        }
                        if (areaName.get(val) > areas.size() - 1)
                            areas.add(new Area());
                        areas.get(areaName.get(val)).addPoint(new Point(idy, idx));
                    }
                }
            }
            line = reader.readLine();
            while (line != null) {
                if ((int) line.charAt(0) == 160)
                    line = reader.readLine();
                else {
                    String val = Character.toString(line.charAt(0));
                    ida = areaName.get(val);
                    idx = 1;
                    int t = (int) line.charAt(idx);
                    while (t == 160 || t == 32) {
                        idx++;
                        t = (int) line.charAt(idx);
                    }
                    val = Character.toString(line.charAt(idx));
                    if (val.equals("=") || val.equals("+"))
                        areas.get(ida).setOperator(ADDITION);
                    else if (val.equals("-"))
                        areas.get(ida).setOperator(SUBTRACTION);
                    else if (val.equals("*") || val.equals("x"))
                        areas.get(ida).setOperator(MULTIPLICATION);
                    else
                        areas.get(ida).setOperator(DIVISION);
                    idx++;
                    t = (int) line.charAt(idx);
                    while (t == 160 || t == 32) {
                        idx++;
                        t = (int) line.charAt(idx);
                    }
                    areas.get(ida).setValue(Integer.parseInt(line.substring(idx)));
                }
                line = reader.readLine();
            }
        } catch (Exception ex) {
            LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
        }
        loading = false;

        updateInitialPossibilitiesKiller();
        updatePossibilitiesKiller();
    }

    public void savePuzzleKiller() {
        String fileName = openFileDialog(SWT.SAVE);

        if (fileName == null) {
            return;
        }

        boolean areaTest = false;
        try {
            Map<Integer, String> operatorMap = new HashMap<Integer, String>();
            operatorMap.put(ADDITION, "+");
            operatorMap.put(SUBTRACTION, "-");
            operatorMap.put(MULTIPLICATION, "*");
            operatorMap.put(DIVISION, ":");
            Map<Integer, String> areaName = new HashMap<Integer, String>();
            for (int i = 0; i < areas.size(); i++)
                areaName.put(i, Character.toString((char) (i + 58)));
            FileOutputStream out = new FileOutputStream(fileName + ".txt");
            out.write(("+-------+-------+-------+\n").getBytes());
            for (int i = 0; i < 9; i++) {
                out.write(("| ").getBytes());
                for (int j = 0; j < 9; j++) {
                    areaTest = false;
                    for (int k = 0; k < areas.size(); k++) {
                        if (areas.get(k).pointUsed(new Point(i, j))) {
                            out.write((areaName.get(k) + " ").getBytes());
                            areaTest = true;
                            break;
                        }
                    }
                    if (!areaTest)
                        out.write("  ".getBytes());
                    if (j == 2 || j == 5)
                        out.write(("| ").getBytes());
                }
                out.write(("|\n").getBytes());
                if (i == 2 || i == 5)
                    out.write(("+-------+-------+-------+\n").getBytes());
            }
            out.write(("+-------+-------+-------+\n\n").getBytes());
            for (int i = 0; i < areas.size(); i++) {
                out.write((areaName.get(i) + " " + operatorMap.get(areas.get(i).getOperator())
                        + " " + Integer.toString(areas.get(i).getValue()) + "\n").getBytes());
            }
        } catch (Exception ex) {
            LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
        }
    }

    public void loadPuzzleHex() {
        String fileName = openFileDialog(SWT.OPEN);

        if (fileName == null) {
            return;
        }

        loadHex(fileName);
    }

    public void loadHex(String fileName) {
    	solved = false;

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            loading = true;
            clearPuzzleHex();
            switch (Integer.parseInt(reader.readLine())) {
                case 1:
                    for (int i = 0; i < 16; i++) {
                        String line = reader.readLine();
                        for (int j = 0; j < 16; j++) {
                            String val = Character.toString(line.charAt(j));
                            if (!val.equals(".")) {
                                int temp;
                                if (val.equals("A")) {
                                    temp = 10;
                                } else if (val.equals("B")) {
                                    temp = 11;
                                } else if (val.equals("C")) {
                                    temp = 12;
                                } else if (val.equals("D")) {
                                    temp = 13;
                                } else if (val.equals("E")) {
                                    temp = 14;
                                } else if (val.equals("F")) {
                                    temp = 15;
                                } else {
                                    temp = Integer.parseInt(val);
                                }
                                boardHex[j][i] = temp;
                                boardTextHex[j][i].setText(valToTextHex(temp));
                            }
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < 16; i++) {
                        String line = reader.readLine();
                        if (Character.toString(line.charAt(0)).equals("+"))
                            line = reader.readLine();
                        int idx = -1;
                        for (int j = 0; j < line.length(); j = j + 2) {
                            String val = Character.toString(line.charAt(j));
                            if (!val.equals("|"))
                                idx++;
                            if (!((int) line.charAt(j) == 160) & !val.equals("|")) {
                                int temp;
                                if (val.equals("A")) {
                                    temp = 10;
                                } else if (val.equals("B")) {
                                    temp = 11;
                                } else if (val.equals("C")) {
                                    temp = 12;
                                } else if (val.equals("D")) {
                                    temp = 13;
                                } else if (val.equals("E")) {
                                    temp = 14;
                                } else if (val.equals("F")) {
                                    temp = 15;
                                } else {
                                    temp = Integer.parseInt(val);
                                }
                                boardHex[idx][i] = temp;
                                boardTextHex[idx][i].setText(valToTextHex(temp));
                            }
                        }
                    }
                    break;
            }
        } catch (Exception ex) {
            LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
        }
        loading = false;
        updatePossibilitiesHex();
    }

    public void savePuzzleHex() {
        String fileName = openFileDialog(SWT.SAVE);

        if (fileName == null) {
            return;
        }

        try {
            Map<Integer, String> hexMap = new HashMap<Integer, String>();
            hexMap.put(-1, " ");
            hexMap.put(0, "0");
            hexMap.put(1, "1");
            hexMap.put(2, "2");
            hexMap.put(3, "3");
            hexMap.put(4, "4");
            hexMap.put(5, "5");
            hexMap.put(6, "6");
            hexMap.put(7, "7");
            hexMap.put(8, "8");
            hexMap.put(9, "9");
            hexMap.put(10, "A");
            hexMap.put(11, "B");
            hexMap.put(12, "C");
            hexMap.put(13, "D");
            hexMap.put(14, "E");
            hexMap.put(15, "F");
            FileOutputStream out = new FileOutputStream(fileName + ".txt");
            out.write("2\n".getBytes());
            out.write("+---------+---------+---------+---------+\n".getBytes());
            for (int i = 0; i < 16; i++) {
                out.write(("| ").getBytes());
                for (int j = 0; j < 16; j++) {
                    out.write((hexMap.get(boardHex[j][i]) + " ").getBytes());
                    if (j == 3 || j == 7 || j == 11)
                        out.write("| ".getBytes());
                }
                out.write(("|\n").getBytes());
                if (i == 3 || i == 7 || i == 11)
                    out.write(("+---------+---------+---------+---------+\n").getBytes());
            }
            out.write(("+---------+---------+---------+---------+").getBytes());
        } catch (Exception ex) {
            LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
        }
    }

    public void clearPuzzleNormal() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boardNormal[i][j] = 0;
                boardTextNormal[i][j].setText("");
                for (int k = 0; k < 8; k++)
                    boardLabelsNormal[i][j][k].setText("");
                possibleNormal.get(i).get(j).clear();
                for (int k = 1; k <= 9; k++)
                    possibleNormal.get(i).get(j).add(k);
            }
        }
    }

    public void clearPuzzleKiller() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boardKiller[i][j] = 0;
                boardTextKiller[i][j].setText("");
                for (int k = 0; k < 8; k++)
                    boardLabelsKiller[i][j][k].setText("");
                possibleKiller.get(i).get(j).clear();
                for (int k = 1; k <= 9; k++)
                    possibleKiller.get(i).get(j).add(k);
            }
        }
        areas.clear();
    }

    public void clearPuzzleHex() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                boardHex[i][j] = -1;
                boardTextHex[i][j].setText("");
                for (int k = 0; k < 8; k++)
                    boardLabelsHex[i][j][k].setText("");
                possibleHex.get(i).get(j).clear();
                for (int k = 0; k < 16; k++)
                    possibleHex.get(i).get(j).add(k);
            }
        }
    }

    public void updateLabelsHex() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (showPossible && possibleHex.get(i).get(j).size() < 9) {
                    for (int k = 0; k < possibleHex.get(i).get(j).size(); k++) {
                        boardLabelsHex[i][j][k].setText(valToTextHex(possibleHex.get(i).get(j).get(
                                k)));
                    }
                    for (int k = possibleHex.get(i).get(j).size(); k < 8; k++) {
                        boardLabelsHex[i][j][k].setText("");
                    }
                }
                if (!showPossible) {
                    for (int k = 0; k < 8; k++)
                        boardLabelsHex[i][j][k].setText("");
                }
                if (possibleHex.get(i).get(j).size() == 9) {
                    for (int k = 0; k < 8; k++)
                        boardLabelsHex[i][j][k].setText("");
                }
                labelCellHex[i][j].layout();
            }
        }
    }

    public void createPlayFieldArea(final Composite parent) {
        final Composite playField = new Composite(parent, SWT.SHADOW_NONE);
        playField.setLayout(new GridLayout());
        playField.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
        switch (tabChoice) {
            case NORMAL: {
                boardNormal = new int[9][9];
                labelCellNormal = new Composite[9][9];
                boardLabelsNormal = new Label[9][9][8];
                boardTextNormal = new Text[9][9];
                possibleNormal = new ArrayList<List<List<Integer>>>();
                for (int i = 0; i < 9; i++) {
                    possibleNormal.add(new ArrayList<List<Integer>>());
                    for (int j = 0; j < 9; j++) {
                        boardNormal[i][j] = 0;
                        possibleNormal.get(i).add(new ArrayList<Integer>());
                        for (int k = 1; k <= 9; k++) {
                            possibleNormal.get(i).get(j).add(k);
                        }
                    }
                }
                createFieldNormal(playField);
            }
                break;
            case KILLER: {
                boardKiller = new int[9][9];
                labelCellKiller = new Composite[9][9];
                boardLabelsKiller = new Label[9][9][8];
                boardTextKiller = new Text[9][9];
                possibleKiller = new ArrayList<List<List<Integer>>>();
                selected = new ArrayList<Point>();
                areas = new ArrayList<Area>();
                for (int i = 0; i < 9; i++) {
                    possibleKiller.add(new ArrayList<List<Integer>>());
                    for (int j = 0; j < 9; j++) {
                        boardKiller[i][j] = 0;
                        possibleKiller.get(i).add(new ArrayList<Integer>());
                        for (int k = 1; k <= 9; k++) {
                            possibleKiller.get(i).get(j).add(k);
                        }
                    }
                }
                createFieldKiller(playField);
            }
                break;
            case HEX: {
                boardHex = new int[16][16];
                labelCellHex = new Composite[16][16];
                boardLabelsHex = new Label[16][16][8];
                boardTextHex = new Text[16][16];
                possibleHex = new ArrayList<List<List<Integer>>>();
                for (int i = 0; i < 16; i++) {
                    possibleHex.add(new ArrayList<List<Integer>>());
                    for (int j = 0; j < 16; j++) {
                        boardHex[i][j] = -1;
                        possibleHex.get(i).add(new ArrayList<Integer>());
                        for (int k = 0; k < 16; k++) {
                            possibleHex.get(i).get(j).add(k);
                        }
                    }
                }
                createFieldHex(playField);
            }
                break;
        }
    }

    public void createFieldNormal(final Composite parent) {
        GridLayout layout = new GridLayout(9, false);
        layout.verticalSpacing = 0;
        layout.horizontalSpacing = 0;

        playField = new Composite(parent, SWT.NONE);
        playField.setLayout(layout);

        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.heightHint = BOX_SIZE_NORMAL;
        gridData.widthHint = BOX_SIZE_NORMAL;
        gridData.verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                labelCellNormal[i][j] = new Composite(playField, SWT.NONE);

                compositeBoxesNormal.put(labelCellNormal[i][j], new Point(i, j));
                labelCellNormal[i][j].addListener(SWT.MouseDown, new Listener() {

                    public void handleEvent(Event event) {
                        Composite composite = (Composite) event.widget;
                        Point point = compositeBoxesNormal.get(composite);
                        boardTextNormal[point.x][point.y].setFocus();
                    }

                });

                labelCellNormal[i][j].setLayoutData(gridData);
                if (i + 1 != 9 && j + 1 != 9) {
                    labelCellNormal[i][j].addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent e) {
                            e.gc.drawRectangle(0, 0, BOX_SIZE_NORMAL, BOX_SIZE_NORMAL);
                        }

                    });
                } else if (i + 1 != 9) {
                    labelCellNormal[i][j].addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent e) {
                            e.gc.drawRectangle(0, 0, BOX_SIZE_NORMAL - 1, BOX_SIZE_NORMAL);
                        }

                    });
                } else if (j + 1 != 9) {
                    labelCellNormal[i][j].addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent e) {
                            e.gc.drawRectangle(0, 0, BOX_SIZE_NORMAL, BOX_SIZE_NORMAL - 1);
                        }

                    });
                } else {
                    labelCellNormal[i][j].addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent e) {
                            e.gc.drawRectangle(0, 0, BOX_SIZE_NORMAL - 1, BOX_SIZE_NORMAL - 1);
                        }

                    });
                }

                if ((j + 1) % 3 == 0 && (j + 1) != 9) {
                    labelCellNormal[i][j].addPaintListener(new PaintListener() {
                        public void paintControl(PaintEvent e) {
                            e.gc.drawLine(BOX_SIZE_NORMAL - 1, BOX_SIZE_NORMAL - 1,
                                    BOX_SIZE_NORMAL - 1, 0);
                        }
                    });
                }

                if ((i + 1) % 3 == 0 && (i + 1) != 9) {
                    labelCellNormal[i][j].addPaintListener(new PaintListener() {
                        public void paintControl(PaintEvent e) {
                            e.gc.drawLine(BOX_SIZE_NORMAL - 1, BOX_SIZE_NORMAL - 1, 0,
                                    BOX_SIZE_NORMAL - 1);
                        }
                    });
                }

                labelCellNormal[i][j].setBackground(WHITE);
                GridLayout gridlayout = new GridLayout(3, true);
                gridlayout.verticalSpacing = 0;
                gridlayout.horizontalSpacing = 0;
                labelCellNormal[i][j].setLayout(gridlayout);
                for (int k = 0; k < 4; k++) {
                    boardLabelsNormal[i][j][k] = createLabelNormal(labelCellNormal[i][j]);
                }
                boardTextNormal[i][j] = createTextNormal(labelCellNormal[i][j]);
                inputBoxesNormal.put(boardTextNormal[i][j], new UserInputPoint(i, j));
                for (int k = 4; k < 8; k++) {
                    boardLabelsNormal[i][j][k] = createLabelNormal(labelCellNormal[i][j]);
                }
                if (boardNormal[i][j] != 0)
                    boardTextNormal[i][j].setText(Integer.toString(boardNormal[i][j]));
                else {
                    if (possibleNormal.get(i).get(j).size() < 9) {
                        for (int k = 0; k < possibleNormal.get(i).get(j).size(); k++) {
                            boardLabelsNormal[i][j][k].setText(Integer.toString(possibleNormal.get(
                                    i).get(j).get(k)));
                        }
                    }
                }
            }
        }
    }

    public void createFieldKiller(final Composite parent) {
        GridLayout layout = new GridLayout(9, false);
        layout.verticalSpacing = 0;
        layout.horizontalSpacing = 0;

        playField = new Composite(parent, SWT.NONE);
        playField.setLayout(layout);

        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.heightHint = BOX_SIZE_KILLER;
        gridData.widthHint = BOX_SIZE_KILLER;
        gridData.verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                labelCellKiller[i][j] = new Composite(playField, SWT.NONE);
                compositeBoxesKiller.put(labelCellKiller[i][j], new Point(i, j));
                labelCellKiller[i][j].setLayoutData(gridData);
                labelCellKiller[i][j].setBackground(WHITE);
                labelCellKiller[i][j].addListener(SWT.MouseDown, new Listener() {

                    public void handleEvent(Event event) {
                        Composite composite = (Composite) event.widget;
                        Point point = compositeBoxesKiller.get(composite);
                        if (selected.contains(point)) {
                            composite.setBackground(WHITE);
                            boardTextKiller[point.x][point.y].setBackground(WHITE);
                            selected.remove(point);

                        } else {
                            composite.setBackground(RED);
                            boardTextKiller[point.x][point.y].setBackground(RED);
                            selected.add(point);
                        }
                    }

                });

                if (i + 1 != 9 && j + 1 != 9) {
                    labelCellKiller[i][j].addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent e) {
                            e.gc.drawRectangle(0, 0, BOX_SIZE_KILLER, BOX_SIZE_KILLER);
                        }

                    });
                } else if (i + 1 != 9) {
                    labelCellKiller[i][j].addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent e) {
                            e.gc.drawRectangle(0, 0, BOX_SIZE_KILLER - 1, BOX_SIZE_KILLER);
                        }

                    });
                } else if (j + 1 != 9) {
                    labelCellKiller[i][j].addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent e) {
                            e.gc.drawRectangle(0, 0, BOX_SIZE_KILLER, BOX_SIZE_KILLER - 1);
                        }

                    });
                } else {
                    labelCellKiller[i][j].addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent e) {
                            e.gc.drawRectangle(0, 0, BOX_SIZE_KILLER - 1, BOX_SIZE_KILLER - 1);
                        }

                    });
                }

                if ((j + 1) % 3 == 0 && (j + 1) != 9) {
                    labelCellKiller[i][j].addPaintListener(new PaintListener() {
                        public void paintControl(PaintEvent e) {
                            if (boxRule)
                                e.gc.drawLine(BOX_SIZE_KILLER - 1, BOX_SIZE_KILLER - 1,
                                        BOX_SIZE_KILLER - 1, 0);
                        }
                    });
                }

                if ((i + 1) % 3 == 0 && (i + 1) != 9) {
                    labelCellKiller[i][j].addPaintListener(new PaintListener() {
                        public void paintControl(PaintEvent e) {
                            if (boxRule)
                                e.gc.drawLine(BOX_SIZE_KILLER - 1, BOX_SIZE_KILLER - 1, 0,
                                        BOX_SIZE_KILLER - 1);
                        }
                    });
                }

                labelCellKiller[i][j].addPaintListener(new PaintListener() {

                    public void paintControl(PaintEvent e) {
                        Font tempFont = e.gc.getFont();
                        e.gc.setFont(FontService.getTinyFont());
                        e.gc.setForeground(RED);
                        Composite composite = (Composite) e.widget;
                        Point point = compositeBoxesKiller.get(composite);
                        if (leftLine(point))
                            e.gc.drawLine(2, 2, 2, BOX_SIZE_KILLER - 3);
                        if (topLine(point))
                            e.gc.drawLine(2, 2, BOX_SIZE_KILLER - 3, 2);
                        if (topLabel(point))
                            e.gc.drawString(topLabelValue(point), 1, 1);
                        if (bottomLine(point))
                            e.gc.drawLine(2, BOX_SIZE_KILLER - 3, BOX_SIZE_KILLER - 3,
                                    BOX_SIZE_KILLER - 3);
                        if (rightLine(point))
                            e.gc.drawLine(BOX_SIZE_KILLER - 3, 2, BOX_SIZE_KILLER - 3,
                                    BOX_SIZE_KILLER - 3);
                        e.gc.setFont(tempFont);
                        e.gc.setForeground(BLACK);
                    }
                });
                GridLayout gridlayout = new GridLayout(3, true);
                gridlayout.verticalSpacing = 0;
                gridlayout.horizontalSpacing = 0;
                labelCellKiller[i][j].setLayout(gridlayout);
                for (int k = 0; k < 4; k++) {
                    boardLabelsKiller[i][j][k] = createLabelKiller(labelCellKiller[i][j]);

                }
                boardTextKiller[i][j] = createTextKiller(labelCellKiller[i][j]);
                inputBoxesKiller.put(boardTextKiller[i][j], new UserInputPoint(i, j));
                for (int k = 4; k < 8; k++) {
                    boardLabelsKiller[i][j][k] = createLabelKiller(labelCellKiller[i][j]);
                }
                if (boardKiller[i][j] != 0)
                    boardTextKiller[i][j].setText(Integer.toString(boardKiller[i][j]));
                else {
                    if (possibleKiller.get(i).get(j).size() < 8) {
                        for (int k = 0; k < possibleKiller.get(i).get(j).size(); k++) {
                            boardLabelsKiller[i][j][k + 1].setText(Integer.toString(possibleKiller.get(
                                    i).get(j).get(k)));
                            boardLabelsKiller[i][j][k + 1].setBackground(WHITE);
                        }
                    }
                }
            }
        }
    }

    public boolean leftLine(Point point) {
        boolean test = false;
        Area area = null;
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).pointUsed(point)) {
                area = areas.get(i);
                test = true;
                break;
            }
        }
        if (test) {
            if (point.y != 0) {
                Point leftPoint = new Point(point.x, point.y - 1);
                return !(area.pointUsed(leftPoint));
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean topLine(Point point) {
        boolean test = false;
        Area area = null;
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).pointUsed(point)) {
                area = areas.get(i);
                test = true;
                break;
            }
        }
        if (test) {
            if (point.x != 0) {
                Point topPoint = new Point(point.x - 1, point.y);
                return !(area.pointUsed(topPoint));
            } else {
                return true;
            }
            /*
             * if (point.x == 0 && point.y == 0) return false; else if (point.x == 0) { Point
             * leftPoint = new Point(point.x, point.y-1); return (area.pointUsed(leftPoint)); } else
             * { Point topPoint = new Point(point.x-1, point.y); return !(area.pointUsed(topPoint));
             * }
             */
        }
        return false;
    }

    public boolean topLabel(Point point) {
        boolean test = false;
        Area area = null;
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).pointUsed(point)) {
                area = areas.get(i);
                test = true;
                break;
            }
        }
        if (test) {
            if (point.x == 0 && point.y == 0)
                return true;
            else if (point.x == 0) {
                Point leftPoint = new Point(point.x, point.y - 1);
                return !(area.pointUsed(leftPoint));
            } else {
                Point topPoint = new Point(point.x - 1, point.y);
                Point leftPoint = new Point(point.x, point.y - 1);
                return (!area.pointUsed(topPoint)) && (!area.pointUsed(leftPoint));
            }
        }
        return false;
    }

    public String topLabelValue(Point point) {
        boolean test = false;
        Area area = null;
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).pointUsed(point)) {
                area = areas.get(i);
                test = true;
                break;
            }
        }
        if (test) {
            switch (area.getOperator()) {
                case ADDITION:
                    return Integer.toString(area.getValue()) + "+";
                case SUBTRACTION:
                    return Integer.toString(area.getValue()) + "-";
                case MULTIPLICATION:
                    return Integer.toString(area.getValue()) + "x";
                case DIVISION:
                    return Integer.toString(area.getValue()) + ":";
            }
        }
        return null;
    }

    public boolean bottomLine(Point point) {
        boolean test = false;
        Area area = null;
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).pointUsed(point)) {
                area = areas.get(i);
                test = true;
                break;
            }
        }
        if (test) {
            if (point.x != 15) {
                Point bottomPoint = new Point(point.x + 1, point.y);
                return !(area.pointUsed(bottomPoint));
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean rightLine(Point point) {
        boolean test = false;
        Area area = null;
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).pointUsed(point)) {
                area = areas.get(i);
                test = true;
                break;
            }
        }
        if (test) {
            if (point.y != 15) {
                Point rightPoint = new Point(point.x, point.y + 1);
                return !(area.pointUsed(rightPoint));
            } else {
                return true;
            }
        }
        return false;
    }

    public void createFieldHex(final Composite parent) {
        GridLayout layout = new GridLayout(16, false);
        layout.verticalSpacing = 0;
        layout.horizontalSpacing = 0;

        playField = new Composite(parent, SWT.NONE);
        playField.setLayout(layout);

        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.heightHint = BOX_SIZE_HEX;
        gridData.widthHint = BOX_SIZE_HEX;
        gridData.verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                labelCellHex[i][j] = new Composite(playField, SWT.NONE);

                compositeBoxesHex.put(labelCellHex[i][j], new Point(i, j));

                labelCellHex[i][j].setLayoutData(gridData);

                labelCellHex[i][j].addListener(SWT.MouseDown, new Listener() {

                    public void handleEvent(Event event) {
                        Composite composite = (Composite) event.widget;
                        Point point = compositeBoxesHex.get(composite);
                        boardTextHex[point.x][point.y].setFocus();
                    }

                });

                if (i + 1 != 16 && j + 1 != 16) {
                    labelCellHex[i][j].addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent e) {
                            e.gc.drawRectangle(0, 0, BOX_SIZE_HEX, BOX_SIZE_HEX);
                        }

                    });
                } else if (i + 1 != 16) {
                    labelCellHex[i][j].addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent e) {
                            e.gc.drawRectangle(0, 0, BOX_SIZE_HEX - 1, BOX_SIZE_HEX);
                        }

                    });
                } else if (j + 1 != 16) {
                    labelCellHex[i][j].addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent e) {
                            e.gc.drawRectangle(0, 0, BOX_SIZE_HEX, BOX_SIZE_HEX - 1);
                        }

                    });
                } else {
                    labelCellHex[i][j].addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent e) {
                            e.gc.drawRectangle(0, 0, BOX_SIZE_HEX - 1, BOX_SIZE_HEX - 1);
                        }

                    });
                }
                if ((j + 1) % 4 == 0 && (j + 1) != 16) {
                    labelCellHex[i][j].addPaintListener(new PaintListener() {
                        public void paintControl(PaintEvent e) {

                            e.gc.drawLine(BOX_SIZE_HEX - 1, BOX_SIZE_HEX - 1, BOX_SIZE_HEX - 1, 0);
                        }
                    });
                }

                if ((i + 1) % 4 == 0 && (i + 1) != 16) {
                    labelCellHex[i][j].addPaintListener(new PaintListener() {
                        public void paintControl(PaintEvent e) {
                            e.gc.drawLine(BOX_SIZE_HEX - 1, BOX_SIZE_HEX - 1, 0, BOX_SIZE_HEX - 1);
                        }
                    });
                }

                labelCellHex[i][j].setBackground(WHITE);
                GridLayout gridlayout = new GridLayout(3, true);
                gridlayout.verticalSpacing = 0;
                gridlayout.horizontalSpacing = 0;
                labelCellHex[i][j].setLayout(gridlayout);
                for (int k = 0; k < 4; k++) {
                    boardLabelsHex[i][j][k] = createLabelHex(labelCellHex[i][j]);

                }
                boardTextHex[i][j] = createTextHex(labelCellHex[i][j]);
                inputBoxesHex.put(boardTextHex[i][j], new UserInputPoint(i, j));
                for (int k = 4; k < 8; k++) {
                    boardLabelsHex[i][j][k] = createLabelHex(labelCellHex[i][j]);
                }
                if (boardHex[i][j] != -1)
                    boardTextHex[i][j].setText(Integer.toString(boardHex[i][j]));
                else {
                    if (possibleHex.get(i).get(j).size() < 9) {
                        for (int k = 0; k < possibleHex.get(i).get(j).size(); k++) {
                            boardLabelsHex[i][j][k].setText(Integer.toString(possibleHex.get(i).get(
                                    j).get(k)));
                        }
                    }
                }
            }
        }
    }

    public Label createLabelNormal(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setAlignment(SWT.CENTER);
        label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        label.setSize(BOX_SIZE_NORMAL / 3, BOX_SIZE_NORMAL / 3);
        label.setFont(FontService.getSmallFont());
        return label;
    }

    public Label createLabelKiller(Composite parent) {
        final Label label = new Label(parent, SWT.NONE);
        label.setAlignment(SWT.CENTER);
        label.setBackground(WHITE);
        label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        label.setSize(BOX_SIZE_KILLER / 3, BOX_SIZE_KILLER / 3);
        label.setFont(FontService.getSmallFont());
        label.setForeground(RED);
        return label;
    }

    public Label createLabelHex(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setAlignment(SWT.CENTER);
        label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        label.setSize(BOX_SIZE_HEX / 3, BOX_SIZE_HEX / 3);
        label.setFont(FontService.getTinyFont());
        return label;
    }

    public Text createTextNormal(Composite parent) {
        Text input = new Text(parent, SWT.NONE);
        input.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        input.setSize(BOX_SIZE_NORMAL / 3, BOX_SIZE_NORMAL / 3);
        input.setTextLimit(1);
        input.setFont(FontService.getNormalFont());
        // input.setForeground(GREEN);

        input.addListener(SWT.Verify, new Listener() {
            public void handleEvent(Event e) {
                String input = e.text;
                Text textbox = (Text) e.widget;
                if (input.length() == 0 && !loading && !solving)
                    updateBoardDataWithUserInputNormal(textbox, input);
                if (!solved && !loading && !solving) {
                    char[] chars = new char[input.length()];
                    input.getChars(0, chars.length, chars, 0);
                    UserInputPoint point = inputBoxesNormal.get(textbox);
                    for (int i = 0; i < chars.length; i++) {
                        if (!('1' <= chars[i] && chars[i] <= '9')
                                || possibleNormal.get(point.x).get(point.y).indexOf(
                                        Integer.parseInt(input)) == -1) {
                            e.doit = false;
                            return;
                        }
                    }
                    updateBoardDataWithUserInputNormal(textbox, input);
                }
            }
        });
        return input;
    }

    public Text createTextKiller(Composite parent) {
        Text input = new Text(parent, SWT.NONE);
        input.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        input.setSize(BOX_SIZE_KILLER / 3, BOX_SIZE_KILLER / 3);
        input.setTextLimit(1);
        input.setFont(FontService.getNormalFont());

        input.addListener(SWT.Verify, new Listener() {
            public void handleEvent(Event e) {
                String input = e.text;
                Text textbox = (Text) e.widget;
                // textbox.setForeground(GREEN);
                if (input.length() == 0 && !loading && !solving)
                    updateBoardDataWithUserInputKiller(textbox, input);
                if (!solved && !loading && !solving) {
                    char[] chars = new char[input.length()];
                    input.getChars(0, chars.length, chars, 0);
                    UserInputPoint point = inputBoxesKiller.get(textbox);
                    for (int i = 0; i < chars.length; i++) {
                        if (!('1' <= chars[i] && chars[i] <= '9')
                                || possibleKiller.get(point.x).get(point.y).indexOf(
                                        Integer.parseInt(input)) == -1) {
                            e.doit = false;
                            return;
                        }
                    }
                    updateBoardDataWithUserInputKiller(textbox, input);
                }
            }
        });
        return input;
    }

    public Text createTextHex(Composite parent) {
        Text input = new Text(parent, SWT.RIGHT);
        input.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        input.setSize(BOX_SIZE_HEX / 3, BOX_SIZE_HEX / 3);
        // input.setForeground(GREEN);
        input.setTextLimit(1);
        input.setFont(FontService.getSmallFont());

        input.addListener(SWT.Verify, new Listener() {
            public void handleEvent(Event e) {
                String input = e.text;
                Text textbox = (Text) e.widget;
                UserInputPoint point = inputBoxesHex.get(textbox);
                if (input.length() == 0 && !loading && !solving)
                    updateBoardDataWithUserInputHex(textbox, input);
                if (!solved && !loading && !solving) {
                    if (input.toUpperCase().equals("A")
                            && possibleHex.get(point.x).get(point.y).indexOf(10) != -1) {
                        updateBoardDataWithUserInputHex(textbox, Integer.toString(10));
                    } else if (input.toUpperCase().equals("B")
                            && possibleHex.get(point.x).get(point.y).indexOf(11) != -1) {
                        updateBoardDataWithUserInputHex(textbox, Integer.toString(11));
                    } else if (input.toUpperCase().equals("C")
                            && possibleHex.get(point.x).get(point.y).indexOf(12) != -1) {
                        updateBoardDataWithUserInputHex(textbox, Integer.toString(12));
                    } else if (input.toUpperCase().equals("D")
                            && possibleHex.get(point.x).get(point.y).indexOf(13) != -1) {
                        updateBoardDataWithUserInputHex(textbox, Integer.toString(13));
                    } else if (input.toUpperCase().equals("E")
                            && possibleHex.get(point.x).get(point.y).indexOf(14) != -1) {
                        updateBoardDataWithUserInputHex(textbox, Integer.toString(14));
                    } else if (input.toUpperCase().equals("F")
                            && possibleHex.get(point.x).get(point.y).indexOf(15) != -1) {
                        updateBoardDataWithUserInputHex(textbox, Integer.toString(15));
                    } else {
                        char[] chars = new char[input.length()];
                        input.getChars(0, chars.length, chars, 0);
                        for (int i = 0; i < chars.length; i++) {
                            if (!('0' <= chars[i] && chars[i] <= '9')
                                    || possibleHex.get(point.x).get(point.y).indexOf(
                                            Integer.parseInt(input)) == -1) {
                                e.doit = false;
                                return;
                            }
                        }
                        updateBoardDataWithUserInputHex(textbox, input);
                    }
                }
            }
        });
        return input;
    }

    public void updateBoardDataWithUserInputNormal(Text inputBox, String inputStr) {
        solved = false;
        UserInputPoint point = inputBoxesNormal.get(inputBox);
        int num = 0;
        if (inputStr.length() > 0) {
            num = Integer.parseInt(inputStr);
        }
        if (num == 0 && boardNormal[point.x][point.y] != 0)
            addPossibleNormal(point.x, point.y, boardNormal[point.x][point.y]);
        boardNormal[point.x][point.y] = num;
        updatePossibilitiesNormal();
    }

    public void updateBoardDataWithUserInputKiller(Text inputBox, String inputStr) {
        solved = false;
        UserInputPoint point = inputBoxesKiller.get(inputBox);
        int num = 0;
        if (inputStr.length() > 0) {
            num = Integer.parseInt(inputStr);
        }
        if (num == 0 && boardKiller[point.x][point.y] != 0)
            addPossibleKiller(point.x, point.y, boardKiller[point.x][point.y]);
        boardKiller[point.x][point.y] = num;
        updatePossibilitiesKiller();
    }

    public void updateBoardDataWithUserInputHex(Text inputBox, String inputStr) {
        solved = false;
        UserInputPoint point = inputBoxesHex.get(inputBox);
        int num = -1;
        if (inputStr.length() > 0) {
            num = Integer.parseInt(inputStr);
        }
        if (num == -1 && boardHex[point.x][point.y] != -1)
            addPossibleHex(point.x, point.y, boardHex[point.x][point.y]);
        boardHex[point.x][point.y] = num;

        updatePossibilitiesHex();
    }

    public static class UserInputPoint {
        int x;
        int y;

        UserInputPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public void updatePossibilitiesNormal() {
        boolean changed = false;
        List<Integer> used;
        int idx;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (boardNormal[i][j] != 0) {
                    possibleNormal.get(i).get(j).clear();
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            used = new ArrayList<Integer>();
            for (int j = 0; j < 9; j++)
                if (boardNormal[i][j] != 0)
                    used.add(boardNormal[i][j]);
            if (used.size() > 0) {
                for (int j = 0; j < used.size(); j++) {
                    for (int k = 0; k < 9; k++) {
                        if (boardNormal[i][k] == 0) {
                            idx = possibleNormal.get(i).get(k).indexOf(used.get(j));
                            if (idx != -1)
                                possibleNormal.get(i).get(k).remove(idx);
                            if (autoFillOne && possibleNormal.get(i).get(k).size() == 1) {
                                boardNormal[i][k] = possibleNormal.get(i).get(k).get(0);
                                boardTextNormal[i][k].setText(Integer.toString(boardNormal[i][k]));
                                labelCellNormal[i][k].layout();
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            used = new ArrayList<Integer>();
            for (int j = 0; j < 9; j++)
                if (boardNormal[j][i] != 0)
                    used.add(boardNormal[j][i]);
            if (used.size() > 0) {
                for (int j = 0; j < used.size(); j++) {
                    for (int k = 0; k < 9; k++) {
                        if (boardNormal[k][i] == 0) {
                            idx = possibleNormal.get(k).get(i).indexOf(used.get(j));
                            if (idx != -1)
                                possibleNormal.get(k).get(i).remove(idx);
                            if (autoFillOne && possibleNormal.get(k).get(i).size() == 1) {
                                boardNormal[k][i] = possibleNormal.get(k).get(i).get(0);
                                boardTextNormal[k][i].setText(Integer.toString(boardNormal[k][i]));
                                labelCellNormal[k][i].layout();
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                used = new ArrayList<Integer>();
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        if (boardNormal[3 * i + k][3 * j + l] != 0)
                            used.add(boardNormal[3 * i + k][3 * j + l]);
                    }
                }
                if (used.size() > 0) {
                    for (int k = 0; k < used.size(); k++) {
                        for (int l = 0; l < 3; l++) {
                            for (int m = 0; m < 3; m++) {
                                if (boardNormal[3 * i + l][3 * j + m] == 0) {
                                    idx =
                                            possibleNormal.get(3 * i + l).get(3 * j + m).indexOf(
                                                    used.get(k));
                                    if (idx != -1)
                                        possibleNormal.get(3 * i + l).get(3 * j + m).remove(idx);
                                    if (autoFillOne
                                            && possibleNormal.get(3 * i + l).get(3 * j + m).size() == 1) {
                                        boardNormal[3 * i + l][3 * j + m] =
                                                possibleNormal.get(3 * i + l).get(3 * j + m).get(0);
                                        boardTextNormal[3 * i + l][3 * j + m].setText(Integer.toString(boardNormal[3
                                                * i + l][3 * j + m]));
                                        labelCellNormal[3 * i + l][3 * j + m].layout();
                                        changed = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (showPossible && possibleNormal.get(i).get(j).size() < 9) {
                    for (int k = 0; k < possibleNormal.get(i).get(j).size(); k++) {
                        boardLabelsNormal[i][j][k].setText(Integer.toString(possibleNormal.get(i).get(
                                j).get(k)));
                    }
                    for (int k = possibleNormal.get(i).get(j).size(); k < 8; k++) {
                        boardLabelsNormal[i][j][k].setText("");
                    }
                }
                if (!showPossible) {
                    for (int k = 0; k < 8; k++)
                        boardLabelsNormal[i][j][k].setText("");
                }
                if (possibleNormal.get(i).get(j).size() == 9) {
                    for (int k = 0; k < 8; k++)
                        boardLabelsNormal[i][j][k].setText("");
                }
                labelCellNormal[i][j].layout();
            }
        }
        if (changed)
            updatePossibilitiesNormal();
    }

    public void fillOneNormal() {
    	boolean changed = false;
    	for (int i = 0; i < 9 & !changed; i++) {
    		for (int j = 0; j < 9 & !changed; j++) {
    			if (possibleNormal.get(i).get(j).size() == 1) {
    				 boardNormal[i][j] = possibleNormal.get(i).get(j).get(0);
                     boardTextNormal[i][j].setText(Integer.toString(boardNormal[i][j]));
                     labelCellNormal[i][j].layout();
                     startBlinkingArea(i,j);
                     changed = true;
    			}
    		}
    	}
    }

    public void fillOneKiller() {
    	boolean changed = false;
    	for (int i = 0; i < 9 & !changed; i++) {
    		for (int j = 0; j < 9 & !changed; j++) {
    			if (possibleKiller.get(i).get(j).size() == 1) {
    				 boardKiller[i][j] = possibleKiller.get(i).get(j).get(0);
                     boardTextKiller[i][j].setText(Integer.toString(boardKiller[i][j]));
                     labelCellKiller[i][j].layout();
                     startBlinkingArea(i,j);
                     changed = true;
    			}
    		}
    	}
    }

    public void fillOneHex() {
    	boolean changed = false;
    	for (int i = 0; i < 16 & !changed; i++) {
    		for (int j = 0; j < 16 & !changed; j++) {
    			if (possibleHex.get(i).get(j).size() == 1) {
    				 boardHex[i][j] = possibleHex.get(i).get(j).get(0);
                     boardTextHex[i][j].setText(Integer.toString(boardHex[i][j]));
                     labelCellHex[i][j].layout();
                     startBlinkingArea(i,j);
                     changed = true;
    			}
    		}
    	}
    }

    public boolean sameLineKiller(List<Point> points) {
        boolean horizontal = true, vertical = true, box = true;
        for (int i = 0; i < points.size() - 1; i++) {
            if (points.get(i).x != points.get(i + 1).x)
                horizontal = false;
            if (points.get(i).y != points.get(i + 1).y)
                vertical = false;
            if (boxRule
                    && (points.get(i).x % 3 != points.get(i + 1).x % 3 || points.get(i).y % 3 != points.get(i + 1).y % 3))
                box = false;
        }
        return horizontal || vertical || (boxRule ? box : false);
    }

    public void updateInitialPossibilitiesKiller() {
        for (int i = 0; i < areas.size(); i++) {
            List<Point> tempList = areas.get(i).getList();
            List<Integer> possible = new ArrayList<Integer>();
            for (int j = 0; j < tempList.size(); j++) {
                for (int k = 0; k < possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size(); k++) {
                    if (possible.indexOf(possibleKiller.get(tempList.get(j).x).get(
                            tempList.get(j).y).get(k)) == -1) {
                        possible.add(possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(
                                k));
                    }
                }
            }
            switch (areas.get(i).getOperator()) {
                case ADDITION: {
                    List<Integer> allowed =
                            generateAllowedValuesKiller(areas.get(i).getList().size(),
                                    areas.get(i).getValue(), ADDITION, sameLineKiller(tempList),
                                    possible);
                    for (int j = 0; j < tempList.size(); j++) {
                        if (boardKiller[tempList.get(j).x][tempList.get(j).y] == 0) {
                            for (int k =
                                    possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size() - 1; k >= 0; k--) {
                                if (allowed.indexOf(possibleKiller.get(tempList.get(j).x).get(
                                        tempList.get(j).y).get(k)) == -1) {
                                    possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).remove(
                                            k);
                                }
                            }
                        }
                    }
                }
                    break;
                case SUBTRACTION: {
                    if (boardKiller[tempList.get(0).x][tempList.get(0).y] == 0
                            && boardKiller[tempList.get(1).x][tempList.get(1).y] != 0) {
                        int result =
                                (boardKiller[tempList.get(1).x][tempList.get(1).y] > areas.get(i).getValue())
                                        ? boardKiller[tempList.get(1).x][tempList.get(1).y]
                                                - areas.get(i).getValue() : areas.get(i).getValue()
                                                - boardKiller[tempList.get(1).x][tempList.get(1).y];
                        if (possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).indexOf(
                                result) != -1) {
                            possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).clear();
                            possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).add(result);
                        }
                    } else if (boardKiller[tempList.get(0).x][tempList.get(0).y] != 0
                            && boardKiller[tempList.get(1).x][tempList.get(1).y] == 0) {
                        int result =
                                (boardKiller[tempList.get(0).x][tempList.get(0).y] > areas.get(i).getValue())
                                        ? boardKiller[tempList.get(0).x][tempList.get(0).y]
                                                - areas.get(i).getValue() : areas.get(i).getValue()
                                                - boardKiller[tempList.get(0).x][tempList.get(0).y];
                        if (possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).indexOf(
                                result) != -1) {
                            possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).clear();
                            possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).add(result);
                        }
                    }
                }
                    break;
                case MULTIPLICATION: {
                    List<Integer> allowed =
                            generateAllowedValuesKiller(areas.get(i).getList().size(),
                                    areas.get(i).getValue(), MULTIPLICATION,
                                    sameLineKiller(tempList), possible);
                    for (int j = 0; j < tempList.size(); j++) {
                        if (boardKiller[tempList.get(j).x][tempList.get(j).y] == 0) {
                            for (int k =
                                    possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size() - 1; k >= 0; k--) {
                                if (allowed.indexOf(possibleKiller.get(tempList.get(j).x).get(
                                        tempList.get(j).y).get(k)) == -1) {
                                    possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).remove(
                                            k);
                                }
                            }
                        }
                    }
                }
                    break;
                case DIVISION: {
                    List<Integer> allowed =
                            generateAllowedValuesKiller(areas.get(i).getList().size(),
                                    areas.get(i).getValue(), DIVISION, sameLineKiller(tempList),
                                    possible);
                    if (boardKiller[tempList.get(0).x][tempList.get(0).y] == 0
                            && boardKiller[tempList.get(1).x][tempList.get(1).y] != 0) {
                        int result =
                                (boardKiller[tempList.get(1).x][tempList.get(1).y] > areas.get(i).getValue())
                                        ? boardKiller[tempList.get(1).x][tempList.get(1).y]
                                                / areas.get(i).getValue() : areas.get(i).getValue()
                                                * boardKiller[tempList.get(1).x][tempList.get(1).y];
                        if (possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).indexOf(
                                result) != -1) {
                            possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).clear();
                            possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).add(result);
                        }
                    } else if (boardKiller[tempList.get(0).x][tempList.get(0).y] != 0
                            && boardKiller[tempList.get(1).x][tempList.get(1).y] == 0) {
                        int result =
                                (boardKiller[tempList.get(0).x][tempList.get(0).y] > areas.get(i).getValue())
                                        ? boardKiller[tempList.get(0).x][tempList.get(0).y]
                                                / areas.get(i).getValue() : areas.get(i).getValue()
                                                * boardKiller[tempList.get(0).x][tempList.get(0).y];
                        if (possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).indexOf(
                                result) != -1) {
                            possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).clear();
                            possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).add(result);
                        }
                    } else {
                        for (int j =
                                possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).size() - 1; j >= 0; j--) {
                            if (allowed.indexOf(possibleKiller.get(tempList.get(0).x).get(
                                    tempList.get(0).y).get(j)) == -1)
                                possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).remove(
                                        j);
                        }
                        for (int j =
                                possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).size() - 1; j >= 0; j--) {
                            if (allowed.indexOf(possibleKiller.get(tempList.get(1).x).get(
                                    tempList.get(1).y).get(j)) == -1)
                                possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).remove(
                                        j);
                        }
                    }
                }
                    break;
            }
        }
    }

    public void updatePossibilitiesKiller() {
        boolean changed = false;
        List<Integer> used;
        int idx;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (boardKiller[i][j] != 0) {
                    possibleKiller.get(i).get(j).clear();
                }
            }
        }
        if (this.killerFirstPossible == false) {
            this.killerFirstPossible = true;
            for (int i = 0; i < areas.size(); i++) {
                List<Point> tempList = areas.get(i).getList();
                List<Integer> possible = new ArrayList<Integer>();
                for (int j = 0; j < tempList.size(); j++) {
                    for (int k = 0; k < possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size(); k++) {
                        if (possible.indexOf(possibleKiller.get(tempList.get(j).x).get(
                                tempList.get(j).y).get(k)) == -1) {
                            possible.add(possibleKiller.get(tempList.get(j).x).get(
                                    tempList.get(j).y).get(k));
                        }
                    }
                }
                switch (areas.get(i).getOperator()) {
                    case ADDITION: {
                        int tempSum = 0, tempSet = 0;
                        for (int j = 0; j < tempList.size(); j++) {
                            if (boardKiller[tempList.get(j).x][tempList.get(j).y] != 0) {
                                tempSet++;
                                tempSum =
                                        tempSum + boardKiller[tempList.get(j).x][tempList.get(j).y];
                            }
                        }
                        List<Integer> allowed =
                                generateAllowedValuesKiller(
                                        areas.get(i).getList().size() - tempSet,
                                        areas.get(i).getValue() - tempSum, ADDITION,
                                        sameLineKiller(tempList), possible);
                        for (int j = 0; j < tempList.size(); j++) {
                            if (boardKiller[tempList.get(j).x][tempList.get(j).y] == 0) {
                                for (int k =
                                        possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size() - 1; k >= 0; k--) {
                                    if (allowed.indexOf(possibleKiller.get(tempList.get(j).x).get(
                                            tempList.get(j).y).get(k)) == -1) {
                                        possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).remove(
                                                k);
                                    }
                                }
                            }
                        }
                    }
                        break;
                    case SUBTRACTION: {
                        if (boardKiller[tempList.get(0).x][tempList.get(0).y] == 0
                                && boardKiller[tempList.get(1).x][tempList.get(1).y] != 0) {
                            int result =
                                    (boardKiller[tempList.get(1).x][tempList.get(1).y] > areas.get(
                                            i).getValue())
                                            ? boardKiller[tempList.get(1).x][tempList.get(1).y]
                                                    - areas.get(i).getValue()
                                            : areas.get(i).getValue()
                                                    - boardKiller[tempList.get(1).x][tempList.get(1).y];
                            if (possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).indexOf(
                                    result) != -1) {
                                possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).clear();
                                possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).add(
                                        result);
                            }
                        } else if (boardKiller[tempList.get(0).x][tempList.get(0).y] != 0
                                && boardKiller[tempList.get(1).x][tempList.get(1).y] == 0) {
                            int result =
                                    (boardKiller[tempList.get(0).x][tempList.get(0).y] > areas.get(
                                            i).getValue())
                                            ? boardKiller[tempList.get(0).x][tempList.get(0).y]
                                                    - areas.get(i).getValue()
                                            : areas.get(i).getValue()
                                                    - boardKiller[tempList.get(0).x][tempList.get(0).y];
                            if (possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).indexOf(
                                    result) != -1) {
                                possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).clear();
                                possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).add(
                                        result);
                            }
                        }
                    }
                        break;
                    case MULTIPLICATION: {
                        int tempProd = 1, tempSet = 0;
                        for (int j = 0; j < tempList.size(); j++) {
                            if (boardKiller[tempList.get(j).x][tempList.get(j).y] != 0) {
                                tempSet++;
                                tempProd =
                                        tempProd
                                                * boardKiller[tempList.get(j).x][tempList.get(j).y];
                            }
                        }
                        List<Integer> allowed =
                                generateAllowedValuesKiller(
                                        areas.get(i).getList().size() - tempSet,
                                        areas.get(i).getValue() / tempProd, MULTIPLICATION,
                                        sameLineKiller(tempList), possible);
                        for (int j = 0; j < tempList.size(); j++) {
                            if (boardKiller[tempList.get(j).x][tempList.get(j).y] == 0) {
                                for (int k =
                                        possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size() - 1; k >= 0; k--) {
                                    if (allowed.indexOf(possibleKiller.get(tempList.get(j).x).get(
                                            tempList.get(j).y).get(k)) == -1) {
                                        possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).remove(
                                                k);
                                    }
                                }
                            }
                        }
                    }
                        break;
                    case DIVISION: {
                        List<Integer> allowed =
                                generateAllowedValuesKiller(areas.get(i).getList().size(),
                                        areas.get(i).getValue(), DIVISION,
                                        sameLineKiller(tempList), possible);
                        if (boardKiller[tempList.get(0).x][tempList.get(0).y] == 0
                                && boardKiller[tempList.get(1).x][tempList.get(1).y] != 0) {
                            int result =
                                    (boardKiller[tempList.get(1).x][tempList.get(1).y] > areas.get(
                                            i).getValue())
                                            ? boardKiller[tempList.get(1).x][tempList.get(1).y]
                                                    / areas.get(i).getValue()
                                            : areas.get(i).getValue()
                                                    * boardKiller[tempList.get(1).x][tempList.get(1).y];
                            if (possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).indexOf(
                                    result) != -1) {
                                possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).clear();
                                possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).add(
                                        result);
                            }
                        } else if (boardKiller[tempList.get(0).x][tempList.get(0).y] != 0
                                && boardKiller[tempList.get(1).x][tempList.get(1).y] == 0) {
                            int result =
                                    (boardKiller[tempList.get(0).x][tempList.get(0).y] > areas.get(
                                            i).getValue())
                                            ? boardKiller[tempList.get(0).x][tempList.get(0).y]
                                                    / areas.get(i).getValue()
                                            : areas.get(i).getValue()
                                                    * boardKiller[tempList.get(0).x][tempList.get(0).y];
                            if (possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).indexOf(
                                    result) != -1) {
                                possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).clear();
                                possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).add(
                                        result);
                            }
                        } else {
                            for (int j =
                                    possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).size() - 1; j >= 0; j--) {
                                if (allowed.indexOf(possibleKiller.get(tempList.get(0).x).get(
                                        tempList.get(0).y).get(j)) == -1)
                                    possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).remove(
                                            j);
                            }
                            for (int j =
                                    possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).size() - 1; j >= 0; j--) {
                                if (allowed.indexOf(possibleKiller.get(tempList.get(1).x).get(
                                        tempList.get(1).y).get(j)) == -1)
                                    possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).remove(
                                            j);
                            }
                        }
                    }
                        break;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            used = new ArrayList<Integer>();
            for (int j = 0; j < 9; j++)
                if (boardKiller[i][j] != 0)
                    used.add(boardKiller[i][j]);
            if (used.size() > 0) {
                for (int j = 0; j < used.size(); j++) {
                    for (int k = 0; k < 9; k++) {
                        if (boardKiller[i][k] == 0) {
                            idx = possibleKiller.get(i).get(k).indexOf(used.get(j));
                            if (idx != -1)
                                possibleKiller.get(i).get(k).remove(idx);
                            if (autoFillOne && possibleKiller.get(i).get(k).size() == 1) {
                                boardKiller[i][k] = possibleKiller.get(i).get(k).get(0);
                                boardTextKiller[i][k].setText(Integer.toString(boardKiller[i][k]));
                                labelCellKiller[i][k].layout();
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            used = new ArrayList<Integer>();
            for (int j = 0; j < 9; j++)
                if (boardKiller[j][i] != 0)
                    used.add(boardKiller[j][i]);
            if (used.size() > 0) {
                for (int j = 0; j < used.size(); j++) {
                    for (int k = 0; k < 9; k++) {
                        if (boardKiller[k][i] == 0) {
                            idx = possibleKiller.get(k).get(i).indexOf(used.get(j));
                            if (idx != -1)
                                possibleKiller.get(k).get(i).remove(idx);
                            if (autoFillOne && possibleKiller.get(k).get(i).size() == 1) {
                                boardKiller[k][i] = possibleKiller.get(k).get(i).get(0);
                                boardTextKiller[k][i].setText(Integer.toString(boardKiller[k][i]));
                                labelCellKiller[k][i].layout();
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (showPossible && possibleKiller.get(i).get(j).size() < 8) {
                    for (int k = 0; k < possibleKiller.get(i).get(j).size(); k++) {
                        boardLabelsKiller[i][j][k + 1].setText(Integer.toString(possibleKiller.get(
                                i).get(j).get(k)));
                        boardLabelsKiller[i][j][k + 1].setBackground(WHITE);
                    }
                    for (int k = possibleKiller.get(i).get(j).size() + 1; k < 8; k++) {
                        boardLabelsKiller[i][j][k].setText("");
                    }
                }
                if (!showPossible) {
                    for (int k = 0; k < 8; k++)
                        boardLabelsKiller[i][j][k].setText("");
                }
                if (possibleKiller.get(i).get(j).size() == 9) {
                    for (int k = 0; k < 8; k++)
                        boardLabelsKiller[i][j][k].setText("");
                }
                labelCellKiller[i][j].layout();
            }
        }
        if (changed)
            updatePossibilitiesKiller();
    }

    public void refresh() {
        switch (tabChoice) {
            case NORMAL:
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        labelCellNormal[i][j].layout();
                    }
                }
                break;
            case KILLER:
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (selected.contains(new Point(i, j))) {
                            labelCellKiller[i][j].setBackground(WHITE);
                            labelCellKiller[i][j].setBackground(RED);
                        } else {
                            labelCellKiller[i][j].setBackground(RED);
                            labelCellKiller[i][j].setBackground(WHITE);
                        }
                    }
                }
                break;
            case HEX:
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        labelCellHex[i][j].layout();
                    }
                }
                break;
        }
    }

    public List<Integer> generateAllowedValuesKiller(int numberOfPoints, int value, int operator,
            boolean sameLine, List<Integer> possible) {
        List<Integer> allowedValues = new ArrayList<Integer>();
        Vector<Integer[]> allSubsets;
        int[] intSet;
        switch (operator) {
            case ADDITION:
                int maxPossible = value - ((numberOfPoints - 1) * numberOfPoints) / 2;
                for (int i = 1; i <= maxPossible && i <= 9; i++)
                    if (possible.indexOf(i) != -1)
                        allowedValues.add(i);
                allSubsets = new Vector<Integer[]>();
                intSet = new int[allowedValues.size()];
                for (int k = 0; k < allowedValues.size(); k++)
                    intSet[k] = allowedValues.get(k);
                if (sameLine)
                    generateSubsets(allSubsets, intSet, new int[numberOfPoints], 0, 0);
                else
                    generateSets(allSubsets, intSet, new int[numberOfPoints], 0, 0);
                for (int i = allSubsets.size() - 1; i >= 0; i--) {
                    int sum = 0;
                    for (int j = 0; j < allSubsets.elementAt(i).length; j++)
                        sum = sum + allSubsets.elementAt(i)[j];
                    if (sum != value)
                        allSubsets.remove(i);
                }
                allowedValues.clear();
                for (int i = 0; i < allSubsets.size(); i++) {
                    for (int j = 0; j < allSubsets.elementAt(i).length; j++) {
                        if (allowedValues.indexOf(allSubsets.elementAt(i)[j]) == -1)
                            allowedValues.add(allSubsets.elementAt(i)[j]);
                    }
                }
                break;
            case SUBTRACTION:

                break;

            case MULTIPLICATION:
                for (int i = 1; i <= 9; i++)
                    if (value % i == 0 && possible.indexOf(i) != -1)
                        allowedValues.add(i);
                allSubsets = new Vector<Integer[]>();
                intSet = new int[allowedValues.size()];
                for (int k = 0; k < allowedValues.size(); k++)
                    intSet[k] = allowedValues.get(k);
                if (sameLine)
                    generateSubsets(allSubsets, intSet, new int[numberOfPoints], 0, 0);
                else
                    generateSets(allSubsets, intSet, new int[numberOfPoints], 0, 0);
                for (int i = allSubsets.size() - 1; i >= 0; i--) {
                    int prod = 1;
                    for (int j = 0; j < allSubsets.elementAt(i).length; j++)
                        prod = prod * allSubsets.elementAt(i)[j];
                    if (prod != value)
                        allSubsets.remove(i);
                }
                allowedValues.clear();
                for (int i = 0; i < allSubsets.size(); i++) {
                    for (int j = 0; j < allSubsets.elementAt(i).length; j++) {
                        if (allowedValues.indexOf(allSubsets.elementAt(i)[j]) == -1)
                            allowedValues.add(allSubsets.elementAt(i)[j]);
                    }
                }
                break;

            case DIVISION:
                allowedValues.add(1);
                allowedValues.add(value);
                switch (value) {
                    case 2: {
                        allowedValues.add(3);
                        allowedValues.add(4);
                        allowedValues.add(6);
                        allowedValues.add(8);
                    }
                        break;
                    case 3: {
                        allowedValues.add(2);
                        allowedValues.add(6);
                        allowedValues.add(9);
                    }
                        break;
                    case 4: {
                        allowedValues.add(2);
                        allowedValues.add(8);
                    }
                        break;
                }
                break;
        }
        return allowedValues;
    }

    public void updatePossibilitiesHex() {
        boolean changed = false;
        List<Integer> used;
        int idx;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (boardHex[i][j] != -1) {
                    possibleHex.get(i).get(j).clear();
                }
            }
        }
        for (int i = 0; i < 16; i++) {
            used = new ArrayList<Integer>();
            for (int j = 0; j < 16; j++)
                if (boardHex[i][j] != -1)
                    used.add(boardHex[i][j]);
            if (used.size() > 0) {
                for (int j = 0; j < used.size(); j++) {
                    for (int k = 0; k < 16; k++) {
                        if (boardHex[i][k] == -1) {
                            idx = possibleHex.get(i).get(k).indexOf(used.get(j));
                            if (idx != -1)
                                possibleHex.get(i).get(k).remove(idx);
                            if (autoFillOne && possibleHex.get(i).get(k).size() == 1) {
                                boardHex[i][k] = possibleHex.get(i).get(k).get(0);
                                boardTextHex[i][k].setText(valToTextHex(boardHex[i][k]));
                                labelCellHex[i][k].layout();
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 16; i++) {
            used = new ArrayList<Integer>();
            for (int j = 0; j < 16; j++)
                if (boardHex[j][i] != -1)
                    used.add(boardHex[j][i]);
            if (used.size() > 0) {
                for (int j = 0; j < used.size(); j++) {
                    for (int k = 0; k < 16; k++) {
                        if (boardHex[k][i] == -1) {
                            idx = possibleHex.get(k).get(i).indexOf(used.get(j));
                            if (idx != -1)
                                possibleHex.get(k).get(i).remove(idx);
                            if (autoFillOne && possibleHex.get(k).get(i).size() == 1) {
                                boardHex[k][i] = possibleHex.get(k).get(i).get(0);
                                boardTextHex[k][i].setText(valToTextHex(boardHex[k][i]));
                                labelCellHex[k][i].layout();
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                used = new ArrayList<Integer>();
                for (int k = 0; k < 4; k++) {
                    for (int l = 0; l < 4; l++) {
                        if (boardHex[4 * i + k][4 * j + l] != -1)
                            used.add(boardHex[4 * i + k][4 * j + l]);
                    }
                }
                if (used.size() > 0) {
                    for (int k = 0; k < used.size(); k++) {
                        for (int l = 0; l < 4; l++) {
                            for (int m = 0; m < 4; m++) {
                                if (boardHex[4 * i + l][4 * j + m] == -1) {
                                    idx =
                                            possibleHex.get(4 * i + l).get(4 * j + m).indexOf(
                                                    used.get(k));
                                    if (idx != -1)
                                        possibleHex.get(4 * i + l).get(4 * j + m).remove(idx);
                                    if (autoFillOne
                                            && possibleHex.get(4 * i + l).get(4 * j + m).size() == 1) {
                                        boardHex[4 * i + l][4 * j + m] =
                                                possibleHex.get(4 * i + l).get(4 * j + m).get(0);
                                        boardTextHex[4 * i + l][4 * j + m].setText(valToTextHex(boardHex[4
                                                * i + l][4 * j + m]));
                                        labelCellHex[4 * i + l][4 * j + m].layout();
                                        changed = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (showPossible && possibleHex.get(i).get(j).size() < 9) {
                    for (int k = 0; k < possibleHex.get(i).get(j).size(); k++) {
                        boardLabelsHex[i][j][k].setText(valToTextHex(possibleHex.get(i).get(j).get(
                                k)));
                    }
                    for (int k = possibleHex.get(i).get(j).size(); k < 8; k++) {
                        boardLabelsHex[i][j][k].setText("");
                    }
                }
                if (!showPossible) {
                    for (int k = 0; k < 8; k++)
                        boardLabelsHex[i][j][k].setText("");
                }
                if (possibleHex.get(i).get(j).size() == 9) {
                    for (int k = 0; k < 8; k++)
                        boardLabelsHex[i][j][k].setText("");
                }
                boardTextHex[i][j].redraw();
                labelCellHex[i][j].layout();
            }
        }
        if (changed)
            updatePossibilitiesHex();
    }

    public String valToTextHex(int val) {
        switch (val) {
            case 10:
                return "A";
            case 11:
                return "B";
            case 12:
                return "C";
            case 13:
                return "D";
            case 14:
                return "E";
            case 15:
                return "F";
        }
        return Integer.toString(val);
    }

    public void addPossibleNormal(int x, int y, int val) {
        int idx;
        for (int i = 0; i < 9; i++) {
            idx = possibleNormal.get(i).get(y).indexOf(val);
            if (idx == -1) {
                possibleNormal.get(i).get(y).add(val);
                Collections.sort(possibleNormal.get(i).get(y));
            }
        }
        for (int i = 0; i < 9; i++) {
            idx = possibleNormal.get(x).get(i).indexOf(val);
            if (idx == -1) {
                possibleNormal.get(x).get(i).add(val);
                Collections.sort(possibleNormal.get(x).get(i));
            }
        }
        int xx = 3 * (int) Math.floor(x / 3);
        int yy = 3 * (int) Math.floor(y / 3);
        for (int i = xx; i < xx + 3; i++) {
            for (int j = yy; j < yy + 3; j++) {
                idx = possibleNormal.get(i).get(j).indexOf(val);
                if (idx == -1) {
                    possibleNormal.get(i).get(j).add(val);
                    Collections.sort(possibleNormal.get(i).get(j));
                }
            }
        }
        for (int i = 1; i <= 9; i++) {
            idx = possibleNormal.get(x).get(y).indexOf(i);
            if (idx == -1) {
                possibleNormal.get(x).get(y).add(i);
                Collections.sort(possibleNormal.get(x).get(y));
            }
        }
    }

    public void addPossibleKiller(int x, int y, int val) {
        int idx;
        for (int i = 0; i < 9; i++) {
            idx = possibleKiller.get(i).get(y).indexOf(val);
            if (idx == -1) {
                possibleKiller.get(i).get(y).add(val);
                Collections.sort(possibleKiller.get(i).get(y));
            }
        }
        for (int i = 0; i < 9; i++) {
            idx = possibleKiller.get(x).get(i).indexOf(val);
            if (idx == -1) {
                possibleKiller.get(x).get(i).add(val);
                Collections.sort(possibleKiller.get(x).get(i));
            }
        }
        for (int i = 1; i <= 9; i++) {
            idx = possibleKiller.get(x).get(y).indexOf(i);
            if (idx == -1) {
                possibleKiller.get(x).get(y).add(i);
                Collections.sort(possibleKiller.get(x).get(y));
            }
        }
    }

    public void addPossibleHex(int x, int y, int val) {
        int idx;
        for (int i = 0; i < 16; i++) {
            idx = possibleHex.get(i).get(y).indexOf(val);
            if (idx == -1) {
                possibleHex.get(i).get(y).add(val);
                Collections.sort(possibleHex.get(i).get(y));
            }
        }
        for (int i = 0; i < 16; i++) {
            idx = possibleHex.get(x).get(i).indexOf(val);
            if (idx == -1) {
                possibleHex.get(x).get(i).add(val);
                Collections.sort(possibleHex.get(x).get(i));
            }
        }
        int xx = 4 * (int) Math.floor(x / 4);
        int yy = 4 * (int) Math.floor(y / 4);
        for (int i = xx; i < xx + 4; i++) {
            for (int j = yy; j < yy + 4; j++) {
                idx = possibleHex.get(i).get(j).indexOf(val);
                if (idx == -1) {
                    possibleHex.get(i).get(j).add(val);
                    Collections.sort(possibleHex.get(i).get(j));
                }
            }
        }
        for (int i = 0; i < 16; i++) {
            idx = possibleHex.get(x).get(y).indexOf(i);
            if (idx == -1) {
                possibleHex.get(x).get(y).add(i);
                Collections.sort(possibleHex.get(x).get(y));
            }
        }
    }

    public Point getEmptySquareNormal() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (boardNormal[i][j] == 0) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    public Point getEmptySquareKiller() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (boardKiller[i][j] == 0) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    public Point getEmptySquareHex() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (boardHex[i][j] == -1) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    public boolean solvePuzzleNormal() {
        solving = true;
        if (solveNormal()) {
            solved = true;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    for (int k = 0; k < 8; k++)
                        boardLabelsNormal[i][j][k].setText("");
                    boardTextNormal[i][j].setText(Integer.toString(boardNormal[i][j]));
                }
            }
            autoFillOne = false;
            solving = false;
            return true;
        }
        solving = false;
        return false;
    }

    public boolean solvePuzzleKiller() {
        solving = true;
        if (boxRule)
            singleOuttie();
        humanStrategiesKiller();
        if (solveKiller()) {
            solved = true;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    for (int k = 0; k < 8; k++)
                        boardLabelsKiller[i][j][k].setText("");
                    boardTextKiller[i][j].setText(Integer.toString(boardKiller[i][j]));
                }
            }
            autoFillOne = false;
            solving = false;
            return true;
        }
        solving = false;
        updatePossibilitiesKiller();
        return false;
    }

    public boolean solvePuzzleHex() {
        solving = true;
        humanStrategiesHex();
        guessOnDiagonalHex();
        if (getEmptySquareHex() != null) {
            if (solveHex()) {
                // Solved with bruteforce
                solved = true;
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        for (int k = 0; k < 8; k++)
                            boardLabelsHex[i][j][k].setText("");
                        boardTextHex[i][j].setText(valToTextHex(boardHex[i][j]));
                    }
                }
                autoFillOne = false;
                solving = false;
                return true;
            }
        } else {
            if (!checkPuzzleHex()) {
                // Solved correctly without bruteforce
                solved = true;
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        for (int k = 0; k < 8; k++)
                            boardLabelsHex[i][j][k].setText("");
                        boardTextHex[i][j].setText(valToTextHex(boardHex[i][j]));
                    }
                }
                autoFillOne = false;
                solving = false;
                return true;
            }
        }
        solving = false;
        return false;
    }

    public boolean solveNormal() {
        Point start = getEmptySquareNormal();
        if (start == null) {
            return true;
        }

        int x = start.x;
        int y = start.y;
        boolean solved = false;

        for (int c = 1; c <= 9 && !solved; c++) {
            if (possibleNormal.get(x).get(y).indexOf(c) != -1) {
                if (!isConflictNormal(x, y, c)) {
                    boardNormal[x][y] = c;
                    solved = solveNormal();
                    if (!solved) {
                        boardNormal[x][y] = 0;
                    }
                }
            }
        }
        return solved;
    }

    public boolean solveKiller() {
        Point start = getEmptySquareKiller();
        if (start == null) {
            return true;
        }

        int x = start.x;
        int y = start.y;
        boolean solved = false;

        for (int c = 1; c <= 9 && !solved; c++) {
            if (possibleKiller.get(x).get(y).indexOf(c) != -1) {
                if (!isConflictKiller(x, y, c)) {
                    boardKiller[x][y] = c;
                    solved = solveKiller();
                    if (!solved) {
                        boardKiller[x][y] = 0;
                    }
                }
            }
        }
        return solved;
    }

    public boolean solveHex() {
        Point start = getEmptySquareHex();
        if (start == null) {
            return true;
        }

        int x = start.x;
        int y = start.y;
        boolean solved = false;

        for (int c = 0; c <= 15 && !solved; c++) {
            if (possibleHex.get(x).get(y).indexOf(c) != -1) {
                if (!isConflictHex(x, y, c)) {
                    boardHex[x][y] = c;
                    solved = solveHex();
                    if (!solved) {
                        boardHex[x][y] = -1;
                    }
                }
            }
        }
        return solved;
    }

    public boolean isConflictNormal(int x, int y, int c) {
        return rowConflictNormal(x, y, c) || colConflictNormal(x, y, c)
                || boxConflictNormal(x, y, c);
    }

    public boolean isConflictKiller(int x, int y, int c) {
        if (boxRule) {
            return rowConflictKiller(y, c) || colConflictKiller(x, c) || boxConflictKiller(x, y, c)
                    || prodConflictKiller(x, y, c) || additionConflictKiller(x, y, c)
                    || subtractionConflictKiller(x, y, c) || divConflictKiller(x, y, c);
        }
        return rowConflictKiller(y, c) || colConflictKiller(x, c) || prodConflictKiller(x, y, c)
                || additionConflictKiller(x, y, c) || subtractionConflictKiller(x, y, c)
                || divConflictKiller(x, y, c);
    }

    public boolean isConflictHex(int x, int y, int c) {
        return rowConflictHex(y, c) || colConflictHex(x, c) || boxConflictHex(x, y, c);
    }

    public boolean rowConflictNormal(int x, int y, int c) {
        for (int i = 0; i < 9; i++) {
            if (boardNormal[i][y] == c) {
                return true;
            }
        }
        return false;
    }

    public boolean colConflictNormal(int x, int y, int c) {
        for (int i = 0; i < 9; i++) {
            if (boardNormal[x][i] == c) {
                return true;
            }
        }
        return false;
    }

    public boolean boxConflictNormal(int xx, int yy, int c) {
        int x = 3 * (int) Math.floor(xx / 3);
        int y = 3 * (int) Math.floor(yy / 3);
        for (int i = x; i < x + 3; i++) {
            for (int j = y; j < y + 3; j++) {
                if (boardNormal[i][j] == c) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean rowConflictKiller(int y, int c) {
        for (int i = 0; i < 9; i++) {
            if (boardKiller[i][y] == c) {
                return true;
            }
        }
        return false;
    }

    public boolean colConflictKiller(int x, int c) {
        for (int i = 0; i < 9; i++) {
            if (boardKiller[x][i] == c) {
                return true;
            }
        }
        return false;
    }

    public boolean boxConflictKiller(int xx, int yy, int c) {
        int x = 3 * (int) Math.floor(xx / 3);
        int y = 3 * (int) Math.floor(yy / 3);
        for (int i = x; i < x + 3; i++) {
            for (int j = y; j < y + 3; j++) {
                if (boardKiller[i][j] == c) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean additionConflictKiller(int x, int y, int c) {
        int tempAddition = c, tempReqAddition = 0;
        List<Point> tempPoints;
        boolean allSet;
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).getOperator() == ADDITION) {
                if (areas.get(i).pointUsed(new Point(x, y))) {
                    tempPoints = areas.get(i).getList();
                    tempReqAddition = areas.get(i).getValue();
                    allSet = true;
                    for (int j = 0; j < tempPoints.size(); j++) {
                        tempAddition =
                                tempAddition
                                        + boardKiller[tempPoints.get(j).x][tempPoints.get(j).y];
                        if (tempPoints.get(j).x != x || tempPoints.get(j).y != y)
                            if (boardKiller[tempPoints.get(j).x][tempPoints.get(j).y] == 0)
                                allSet = false;
                    }
                    if (allSet && tempAddition != tempReqAddition) {
                        return true;
                    }
                    if (tempAddition > tempReqAddition) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean allSetSubtraction(Area subtraction, Point p) {
        List<Point> subtractionPoints = subtraction.getList();
        for (int i = 0; i < subtractionPoints.size(); i++) {
            if (!subtractionPoints.get(i).equals(p)) {
                if (boardKiller[subtractionPoints.get(i).x][subtractionPoints.get(i).y] == 0)
                    return false;
            }
        }
        return true;
    }

    public boolean subtractionConflictKiller(int x, int y, int c) {
        int tempReqSubtraction = 0;
        List<Point> tempPoints = null;
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).getOperator() == SUBTRACTION) {
                if (areas.get(i).pointUsed(new Point(x, y))) {
                    tempPoints = areas.get(i).getList();
                    tempReqSubtraction = areas.get(i).getValue();
                    if (allSetSubtraction(areas.get(i), new Point(x, y))) {
                        if (boardKiller[tempPoints.get(0).x][tempPoints.get(0).y] != 0) {
                            if (Math.abs(boardKiller[tempPoints.get(0).x][tempPoints.get(0).y] - c) != tempReqSubtraction) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            if (Math.abs(boardKiller[tempPoints.get(1).x][tempPoints.get(1).y] - c) != tempReqSubtraction) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean prodConflictKiller(int x, int y, int c) {
        int tempProd = c, tempReqProd = 0;
        List<Point> tempPoints;
        boolean allSet;
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).getOperator() == MULTIPLICATION) {
                if (areas.get(i).pointUsed(new Point(x, y))) {
                    tempPoints = areas.get(i).getList();
                    tempReqProd = areas.get(i).getValue();
                    allSet = true;
                    for (int j = 0; j < tempPoints.size(); j++) {
                        if (boardKiller[tempPoints.get(j).x][tempPoints.get(j).y] != 0) {
                            tempProd =
                                    tempProd
                                            * boardKiller[tempPoints.get(j).x][tempPoints.get(j).y];
                            if (tempReqProd % boardKiller[tempPoints.get(j).x][tempPoints.get(j).y] != 0) {
                                return true;
                            }
                        } else {
                            if (tempPoints.get(j).x != x || tempPoints.get(j).y != y)
                                allSet = false;
                        }
                    }
                    if (allSet && tempReqProd != tempProd) {
                        return true;
                    }
                    if (tempProd > tempReqProd) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean allSetDiv(Area div, Point p) {
        List<Point> divPoints = div.getList();
        for (int i = 0; i < divPoints.size(); i++) {
            if (!divPoints.get(i).equals(p)) {
                if (boardKiller[divPoints.get(i).x][divPoints.get(i).y] == 0)
                    return false;
            }
        }
        return true;
    }

    public boolean divConflictKiller(int x, int y, int c) {
        int tempReqDiv = 0;
        List<Point> tempPoints;
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).getOperator() == DIVISION) {
                if (areas.get(i).pointUsed(new Point(x, y))) {
                    if (allSetDiv(areas.get(i), new Point(x, y))) {
                        tempReqDiv = areas.get(i).getValue();
                        tempPoints = areas.get(i).getList();
                        if (boardKiller[tempPoints.get(0).x][tempPoints.get(0).y] != 0) {
                            if (boardKiller[tempPoints.get(0).x][tempPoints.get(0).y] >= c) {
                                if (boardKiller[tempPoints.get(0).x][tempPoints.get(0).y] % c != 0) {
                                    return true;
                                } else if (boardKiller[tempPoints.get(0).x][tempPoints.get(0).y]
                                        / c != tempReqDiv) {
                                    return true;
                                }
                                return false;
                            } else {
                                if (c % boardKiller[tempPoints.get(0).x][tempPoints.get(0).y] != 0) {
                                    return true;
                                } else if (c
                                        / boardKiller[tempPoints.get(0).x][tempPoints.get(0).y] != tempReqDiv) {
                                    return true;
                                }
                                return false;
                            }
                        } else {
                            if (boardKiller[tempPoints.get(1).x][tempPoints.get(1).y] >= c) {
                                if (boardKiller[tempPoints.get(1).x][tempPoints.get(1).y] % c != 0) {
                                    return true;
                                } else if (boardKiller[tempPoints.get(1).x][tempPoints.get(1).y]
                                        / c != tempReqDiv) {
                                    return true;
                                }
                                return false;
                            } else {
                                if (c % boardKiller[tempPoints.get(1).x][tempPoints.get(1).y] != 0) {
                                    return true;
                                } else if (c
                                        / boardKiller[tempPoints.get(1).x][tempPoints.get(1).y] != tempReqDiv) {
                                    return true;
                                }
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void humanStrategiesKiller() {
        boolean changed = true;
        while (changed) {
            changed = false;
            changed = changed || onePossibleKiller() || nakedSingleKiller() || nakedSubsetKiller();
            if (boxRule)
                changed = changed || hiddenSingleKiller() || blockAndCRKiller();
        }
    }

    /**
     * Check if there exist squares for which only one value is possible
     *
     * @return if this was the case
     */
    public boolean onePossibleKiller() {
        boolean changed = false;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (boardKiller[i][j] == 0 & possibleKiller.get(i).get(j).size() == 1) {
                    changed = true;
                    boardKiller[i][j] = possibleKiller.get(i).get(j).get(0);
                }
            }
        }
        return changed;
    }

    /**
     * Checks if there exists a square which is a "naked single"
     *
     * @return if this was the case
     */
    public boolean nakedSingleKiller() {
        boolean changed = false;
        Vector<Integer> possible;
        int idx;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (boardKiller[i][j] == 0) {
                    possible = new Vector<Integer>();
                    for (int k = 1; k <= 9; k++)
                        possible.add(k);
                    for (int k = 0; k < 9; k++)
                        if (boardKiller[k][j] != 0) {
                            idx = possible.indexOf(boardKiller[k][j]);
                            if (idx != -1)
                                possible.remove(idx);
                        }
                    for (int k = 0; k < 9; k++)
                        if (boardKiller[i][k] != 0) {
                            idx = possible.indexOf(boardKiller[i][k]);
                            if (idx != -1)
                                possible.remove(idx);
                        }
                    if (possible.size() == 1) {
                        changed = true;
                        boardKiller[i][j] = possible.elementAt(0);
                        updatePossibilitiesKiller();
                    }
                }
            }
        }
        return changed;
    }

    /**
     * Checks if there exists a square which is a "hidden single"
     *
     * @return if this was the case
     */
    public boolean hiddenSingleKiller() {
        boolean changed = false;
        Vector<Integer> set1;
        Vector<Integer> set2;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (boardKiller[i][j] == 0) {
                    set1 = new Vector<Integer>();
                    set2 = new Vector<Integer>();
                    int x = 3 * (int) Math.floor(i / 3);
                    int y = 3 * (int) Math.floor(j / 3);
                    boolean neighborsSet =
                            (boardKiller[x][j] != 0 || x == i)
                                    & (boardKiller[x + 1][j] != 0 || x + 1 == i)
                                    & (boardKiller[x + 2][j] != 0 || x + 2 == i);
                    if (neighborsSet) {
                        if (y == j) {
                            for (int k = 0; k < 9; k++)
                                if ((3 * (k / 3) != x) & (boardKiller[k][y + 1] != 0))
                                    set1.add(boardKiller[k][y + 1]);
                            for (int k = 0; k < 9; k++)
                                if ((3 * (k / 3) != x) & (boardKiller[k][y + 2] != 0))
                                    set2.add(boardKiller[k][y + 2]);
                        } else if (y + 1 == j) {
                            for (int k = 0; k < 9; k++)
                                if ((3 * (k / 3) != x) & (boardKiller[k][y] != 0))
                                    set1.add(boardKiller[k][y]);
                            for (int k = 0; k < 9; k++)
                                if ((3 * (k / 3) != x) & (boardKiller[k][y + 2] != 0))
                                    set2.add(boardKiller[k][y + 2]);
                        } else {
                            for (int k = 0; k < 9; k++)
                                if ((3 * (k / 3) != x) & (boardKiller[k][y] != 0))
                                    set1.add(boardKiller[k][y]);
                            for (int k = 0; k < 9; k++)
                                if ((3 * (k / 3) != x) & (boardKiller[k][y + 1] != 0))
                                    set2.add(boardKiller[k][y + 1]);
                        }
                        int idx;
                        if (x == i) {
                            idx = set1.indexOf(boardKiller[x + 1][j]);
                            if (idx != -1)
                                set1.remove(idx);
                            idx = set1.indexOf(boardKiller[x + 2][j]);
                            if (idx != -1)
                                set1.remove(idx);
                        } else if (x + 1 == i) {
                            idx = set1.indexOf(boardKiller[x][j]);
                            if (idx != -1)
                                set1.remove(idx);
                            idx = set1.indexOf(boardKiller[x + 2][j]);
                            if (idx != -1)
                                set1.remove(idx);
                        } else {
                            idx = set1.indexOf(boardKiller[x][j]);
                            if (idx != -1)
                                set1.remove(idx);
                            idx = set1.indexOf(boardKiller[x + 1][j]);
                            if (idx != -1)
                                set1.remove(idx);
                        }
                        if (set1.size() != 0 & set2.size() != 0) {
                            for (int k = 0; k < set1.size(); k++) {
                                if (set2.indexOf(set1.elementAt(k)) != -1) {
                                    boardKiller[i][j] = set1.elementAt(k);
                                    updatePossibilitiesKiller();
                                    changed = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (boardKiller[i][j] == 0) {
                        set1 = new Vector<Integer>();
                        set2 = new Vector<Integer>();
                        neighborsSet =
                                (boardKiller[i][y] != 0 || y == j)
                                        & (boardKiller[i][y + 1] != 0 || y + 1 == j)
                                        & (boardKiller[i][y + 2] != 0 || y + 2 == j);
                        if (neighborsSet) {
                            if (x == i) {
                                for (int k = 0; k < 9; k++)
                                    if ((3 * (k / 3) != y) & (boardKiller[x + 1][k] != 0))
                                        set1.add(boardKiller[x + 1][k]);
                                for (int k = 0; k < 9; k++)
                                    if ((3 * (k / 3) != y) & (boardKiller[x + 2][k] != 0))
                                        set2.add(boardKiller[x + 2][k]);
                            } else if (x + 1 == i) {
                                for (int k = 0; k < 9; k++)
                                    if ((3 * (k / 3) != y) & (boardKiller[x][k] != 0))
                                        set1.add(boardKiller[x][k]);
                                for (int k = 0; k < 9; k++)
                                    if ((3 * (k / 3) != y) & (boardKiller[x + 2][k] != 0))
                                        set2.add(boardKiller[x + 2][k]);
                            } else {
                                for (int k = 0; k < 9; k++)
                                    if ((3 * (k / 3) != y) & (boardKiller[x][k] != 0))
                                        set1.add(boardKiller[x][k]);
                                for (int k = 0; k < 9; k++)
                                    if ((3 * (k / 3) != y) & (boardKiller[x + 1][k] != 0))
                                        set2.add(boardKiller[x + 1][k]);
                            }
                            int idx;
                            if (y == j) {
                                idx = set1.indexOf(boardKiller[i][y + 1]);
                                if (idx != -1)
                                    set1.remove(idx);
                                idx = set1.indexOf(boardKiller[i][y + 2]);
                                if (idx != -1)
                                    set1.remove(idx);
                            } else if (y + 1 == j) {
                                idx = set1.indexOf(boardKiller[i][y]);
                                if (idx != -1)
                                    set1.remove(idx);
                                idx = set1.indexOf(boardKiller[i][y + 2]);
                                if (idx != -1)
                                    set1.remove(idx);
                            } else {
                                idx = set1.indexOf(boardKiller[i][y]);
                                if (idx != -1)
                                    set1.remove(idx);
                                idx = set1.indexOf(boardKiller[i][y + 2]);
                                if (idx != -1)
                                    set1.remove(idx);
                            }
                            if (set1.size() != 0 & set2.size() != 0) {
                                for (int k = 0; k < set1.size(); k++) {
                                    if (set2.indexOf(set1.elementAt(k)) != -1) {
                                        boardKiller[i][j] = set1.elementAt(k);
                                        updatePossibilitiesKiller();
                                        changed = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return changed;
    }

    /**
     * Checks if there are Block and Column/Row interactions
     *
     * @return if this was the case
     */
    public boolean blockAndCRKiller() {
        boolean changed = false;
        Vector<Integer> set;
        int idx;
        for (int i1 = 0; i1 < 3; i1++) {
            for (int i2 = 0; i2 < 3; i2++) {
                for (int i3 = 0; i3 < 3; i3++) {
                    for (int i4 = 0; i4 < 3; i4++) {
                        // remove from columns
                        if (boardKiller[3 * i1 + i4][3 * i2 + i3] == 0
                                & boardKiller[3 * i1 + i4][3 * i2 + (1 + i3) % 3] == 0 &

                                boardKiller[3 * i1 + (1 + i4) % 3][3 * i2 + i3] != 0
                                & boardKiller[3 * i1 + (1 + i4) % 3][3 * i2 + (1 + i3) % 3] != 0 &

                                boardKiller[3 * i1 + (2 + i4) % 3][3 * i2 + i3] != 0
                                & boardKiller[3 * i1 + (2 + i4) % 3][3 * i2 + (1 + i3) % 3] != 0) {
                            set = new Vector<Integer>();
                            for (int j = 0; j < 9; j++) {
                                if ((j / 3) != i1 & boardKiller[j][3 * i2 + (2 + i3) % 3] != 0) {
                                    set.add(boardKiller[j][3 * i2 + (2 + i3) % 3]);
                                }
                            }
                            if (set.size() > 0) {
                                for (int j = 0; j < set.size(); j++) {
                                    if (boardKiller[3 * i1 + (1 + i4) % 3][3 * i2 + i3] != set.elementAt(j)
                                            & boardKiller[3 * i1 + (1 + i4) % 3][3 * i2 + (1 + i3)
                                                    % 3] != set.elementAt(j)
                                            &

                                            boardKiller[3 * i1 + (2 + i4) % 3][3 * i2 + i3] != set.elementAt(j)
                                            & boardKiller[3 * i1 + (2 + i4) % 3][3 * i2 + (1 + i3)
                                                    % 3] != set.elementAt(j)) {
                                        for (int k = 0; k < 9; k++) {
                                            if ((k / 3) != i2) {
                                                idx =
                                                        possibleKiller.get(3 * i1 + i4).get(k).indexOf(
                                                                set.elementAt(j));
                                                if (idx != -1) {
                                                    possibleKiller.get(3 * i1 + i4).get(k).remove(
                                                            idx);
                                                    changed = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // remove from rows
                        if (boardKiller[3 * i2 + i3][3 * i1 + i4] == 0
                                & boardKiller[3 * i2 + (1 + i3) % 3][3 * i1 + i4] == 0 &

                                boardKiller[3 * i2 + i3][3 * i1 + (1 + i4) % 3] != 0
                                & boardKiller[3 * i2 + (1 + i3) % 3][3 * i1 + (1 + i4) % 3] != 0 &

                                boardKiller[3 * i2 + i3][3 * i1 + (2 + i4) % 3] != 0
                                & boardKiller[3 * i2 + (1 + i3) % 3][3 * i1 + (2 + i4) % 3] != 0) {
                            set = new Vector<Integer>();
                            for (int j = 0; j < 9; j++) {
                                if ((j / 3) != i1 & boardKiller[3 * i2 + (2 + i3) % 3][j] != 0) {
                                    set.add(boardKiller[3 * i2 + (2 + i3) % 3][j]);
                                }
                            }
                            if (set.size() > 0) {
                                for (int j = 0; j < set.size(); j++) {
                                    if (boardKiller[3 * i2 + i3][3 * i1 + (1 + i4) % 3] != set.elementAt(j)
                                            & boardKiller[3 * i2 + (1 + i3) % 3][3 * i1 + (1 + i4)
                                                    % 3] != set.elementAt(j)
                                            &

                                            boardKiller[3 * i2 + i3][3 * i1 + (2 + i4) % 3] != set.elementAt(j)
                                            & boardKiller[3 * i2 + (1 + i3) % 3][3 * i1 + (2 + i4)
                                                    % 3] != set.elementAt(j)) {
                                        for (int k = 0; k < 9; k++) {
                                            if ((k / 3) != i2) {
                                                idx =
                                                        possibleKiller.get(k).get(3 * i1 + i4).indexOf(
                                                                set.elementAt(j));
                                                if (idx != -1) {
                                                    possibleKiller.get(k).get(3 * i1 + i4).remove(
                                                            idx);
                                                    changed = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return changed;
    }

    public boolean nakedSubsetKiller() {
        boolean changed = false, temp;
        int total, idx;
        int[] intSet;
        Vector<Integer> set, values;
        Vector<Integer[]> allSubsets, goodSubsets;
        Vector<Point> pointSet;
        int i = 2; // for i = 2, the subsets need to have size exactly 2
        for (int j = 0; j < 9; j++) {
            set = new Vector<Integer>();
            for (int k = 0; k < 9; k++) {
                if (possibleKiller.get(j).get(k).size() == i & boardKiller[j][k] == -1)
                    set.add(k);
            }
            while (set.size() >= i) {
                total = 1;
                for (int k = 1; k < set.size(); k++) {
                    if (possibleKiller.get(j).get(set.elementAt(0)).equals(
                            possibleKiller.get(j).get(set.elementAt(k))))
                        total++;
                }
                if (total != i) {
                    set.remove(0);
                } else {
                    for (int k = set.size() - 1; k > 0; k--) {
                        if (!possibleKiller.get(j).get(set.elementAt(0)).equals(
                                possibleKiller.get(j).get(set.elementAt(k))))
                            set.remove(k);
                    }
                    break;
                }
            }
            if (set.size() == i) {
                for (int k = 0; k < 9; k++) {
                    if (set.indexOf(k) == -1) {
                        for (int l = 0; l < possibleKiller.get(j).get(set.elementAt(0)).size(); l++) {
                            idx =
                                    possibleKiller.get(j).get(k).indexOf(
                                            possibleKiller.get(j).get(set.elementAt(0)).get(l));
                            if (idx != -1) {
                                possibleKiller.get(j).get(k).remove(idx);
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
        for (int j = 0; j < 9; j++) {
            set = new Vector<Integer>();
            for (int k = 0; k < 9; k++) {
                if (possibleKiller.get(k).get(j).size() == i & boardKiller[k][j] == -1)
                    set.add(k);
            }
            while (set.size() >= i) {
                total = 1;
                for (int k = 1; k < set.size(); k++) {
                    if (possibleKiller.get(set.elementAt(0)).get(j).equals(
                            possibleKiller.get(set.elementAt(k)).get(j)))
                        total++;
                }
                if (total != i) {
                    set.remove(0);
                } else {
                    for (int k = set.size() - 1; k > 0; k--) {
                        if (!possibleKiller.get(set.elementAt(0)).get(j).equals(
                                possibleKiller.get(set.elementAt(k)).get(j)))
                            set.remove(k);
                    }
                    break;
                }
            }
            if (set.size() == i) {
                for (int k = 0; k < 9; k++) {
                    if (set.indexOf(k) == -1) {
                        for (int l = 0; l < possibleKiller.get(set.elementAt(0)).get(j).size(); l++) {
                            idx =
                                    possibleKiller.get(k).get(j).indexOf(
                                            possibleKiller.get(set.elementAt(0)).get(j).get(l));
                            if (idx != -1) {
                                possibleKiller.get(k).get(j).remove(idx);
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
        for (int j1 = 0; j1 < 3; j1++) {
            for (int j2 = 0; j2 < 3; j2++) {
                pointSet = new Vector<Point>();
                for (int k1 = 0; k1 < 3; k1++) {
                    for (int k2 = 0; k2 < 3; k2++) {
                        if (possibleKiller.get(3 * j1 + k1).get(3 * j2 + k2).size() == i
                                & boardKiller[3 * j1 + k1][3 * j2 + k2] == -1)
                            pointSet.add(new Point(3 * j1 + k1, 3 * j2 + k2));
                    }
                }
                while (pointSet.size() >= i) {
                    total = 1;
                    for (int k = 1; k < pointSet.size(); k++) {
                        if (possibleKiller.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y).equals(
                                possibleKiller.get(pointSet.elementAt(k).x).get(
                                        pointSet.elementAt(k).y)))
                            total++;
                    }
                    if (total != i) {
                        pointSet.remove(0);
                    } else {
                        for (int k = pointSet.size() - 1; k > 0; k--) {
                            if (!possibleKiller.get(pointSet.elementAt(0).x).get(
                                    pointSet.elementAt(0).y).equals(
                                    possibleKiller.get(pointSet.elementAt(0).x).get(
                                            pointSet.elementAt(0).y)))
                                pointSet.remove(k);
                        }
                        break;
                    }
                }
                if (pointSet.size() == i) {
                    for (int k1 = 0; k1 < 3; k1++) {
                        for (int k2 = 0; k2 < 3; k2++) {
                            if (pointSet.indexOf(new Point(3 * j1 + k1, 3 * j2 + k2)) == -1) {
                                for (int l = 0; l < possibleKiller.get(pointSet.elementAt(0).x).get(
                                        pointSet.elementAt(0).y).size(); l++) {
                                    idx =
                                            possibleKiller.get(3 * j1 + k1).get(3 * j2 + k2).indexOf(
                                                    possibleKiller.get(pointSet.elementAt(0).x).get(
                                                            pointSet.elementAt(0).y).get(l));
                                    if (idx != -1) {
                                        possibleKiller.get(3 * j1 + k1).get(3 * j2 + k2).remove(idx);
                                        changed = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (i = 3; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                set = new Vector<Integer>();
                values = new Vector<Integer>();
                allSubsets = new Vector<Integer[]>();
                goodSubsets = new Vector<Integer[]>();
                for (int k = 0; k < 9; k++) {
                    if (possibleKiller.get(j).get(k).size() <= i & boardKiller[j][k] == -1)
                        set.add(k);
                }
                if (set.size() >= i) {
                    intSet = new int[set.size()];
                    for (int k = 0; k < set.size(); k++)
                        intSet[k] = set.elementAt(k);
                    generateSubsets(allSubsets, intSet, new int[i], 0, 0);
                    for (int k1 = 0; k1 < allSubsets.size(); k1++) {
                        for (int k2 = 0; k2 < allSubsets.elementAt(k1).length; k2++) {
                            for (int k3 = 0; k3 < possibleKiller.get(j).get(
                                    allSubsets.elementAt(k1)[k2]).size(); k3++) {
                                if (values.indexOf(possibleKiller.get(j).get(
                                        allSubsets.elementAt(k1)[k2]).get(k3)) == -1) {
                                    values.add(possibleKiller.get(j).get(
                                            allSubsets.elementAt(k1)[k2]).get(k3));
                                }
                            }
                        }
                        if (values.size() == i) {
                            goodSubsets.add(allSubsets.elementAt(k1));
                        }
                        values.removeAllElements();
                    }
                }
                if (goodSubsets.size() > 0) {
                    for (int k1 = 0; k1 < goodSubsets.size(); k1++) {
                        for (int k2 = 0; k2 < goodSubsets.elementAt(k1).length; k2++) {
                            for (int k3 = 0; k3 < possibleKiller.get(j).get(
                                    goodSubsets.elementAt(k1)[k2]).size(); k3++) {
                                if (values.indexOf(possibleKiller.get(j).get(
                                        goodSubsets.elementAt(k1)[k2]).get(k3)) == -1) {
                                    values.add(possibleKiller.get(j).get(
                                            goodSubsets.elementAt(k1)[k2]).get(k3));
                                }
                            }
                        }
                        for (int k2 = 0; k2 < 9; k2++) {
                            temp = true;
                            for (int k3 = 0; k3 < goodSubsets.elementAt(k1).length; k3++) {
                                if (goodSubsets.elementAt(k1)[k3] == k2)
                                    temp = false;
                            }
                            if (temp) {
                                for (int l = 0; l < values.size(); l++) {
                                    idx =
                                            possibleKiller.get(j).get(k2).indexOf(
                                                    values.elementAt(l));
                                    if (idx != -1) {
                                        possibleKiller.get(j).get(k2).remove(idx);
                                        changed = true;
                                    }
                                }
                            }
                        }
                        values.removeAllElements();
                    }
                }
            }
            for (int j = 0; j < 9; j++) {
                set = new Vector<Integer>();
                values = new Vector<Integer>();
                allSubsets = new Vector<Integer[]>();
                goodSubsets = new Vector<Integer[]>();
                for (int k = 0; k < 9; k++) {
                    if (possibleKiller.get(k).get(j).size() <= i & boardKiller[k][j] == -1)
                        set.add(k);
                }
                if (set.size() >= i) {
                    intSet = new int[set.size()];
                    for (int k = 0; k < set.size(); k++)
                        intSet[k] = set.elementAt(k);
                    generateSubsets(allSubsets, intSet, new int[i], 0, 0);
                    for (int k1 = 0; k1 < allSubsets.size(); k1++) {
                        for (int k2 = 0; k2 < allSubsets.elementAt(k1).length; k2++) {
                            for (int k3 = 0; k3 < possibleKiller.get(allSubsets.elementAt(k1)[k2]).get(
                                    j).size(); k3++) {
                                if (values.indexOf(possibleKiller.get(allSubsets.elementAt(k1)[k2]).get(
                                        j).get(k3)) == -1) {
                                    values.add(possibleKiller.get(allSubsets.elementAt(k1)[k2]).get(
                                            j).get(k3));
                                }
                            }
                        }
                        if (values.size() == i) {
                            goodSubsets.add(allSubsets.elementAt(k1));
                        }
                        values.removeAllElements();
                    }
                }
                if (goodSubsets.size() > 0) {
                    for (int k1 = 0; k1 < goodSubsets.size(); k1++) {
                        for (int k2 = 0; k2 < goodSubsets.elementAt(k1).length; k2++) {
                            for (int k3 = 0; k3 < possibleKiller.get(goodSubsets.elementAt(k1)[k2]).get(
                                    j).size(); k3++) {
                                if (values.indexOf(possibleKiller.get(goodSubsets.elementAt(k1)[k2]).get(
                                        j).get(k3)) == -1) {
                                    values.add(possibleKiller.get(goodSubsets.elementAt(k1)[k2]).get(
                                            j).get(k3));
                                }
                            }
                        }
                        for (int k2 = 0; k2 < 9; k2++) {
                            temp = true;
                            for (int k3 = 0; k3 < goodSubsets.elementAt(k1).length; k3++) {
                                if (goodSubsets.elementAt(k1)[k3] == k2)
                                    temp = false;
                            }
                            if (temp) {
                                for (int l = 0; l < values.size(); l++) {
                                    idx =
                                            possibleKiller.get(k2).get(j).indexOf(
                                                    values.elementAt(l));
                                    if (idx != -1) {
                                        possibleKiller.get(k2).get(j).remove(idx);
                                        changed = true;
                                    }
                                }
                            }
                        }
                        values.removeAllElements();
                    }
                }
            }

            for (int j1 = 0; j1 < 3; j1++) {
                for (int j2 = 0; j2 < 3; j2++) {
                    set = new Vector<Integer>();
                    values = new Vector<Integer>();
                    allSubsets = new Vector<Integer[]>();
                    goodSubsets = new Vector<Integer[]>();
                    pointSet = new Vector<Point>();
                    for (int k1 = 0; k1 < 3; k1++) {
                        for (int k2 = 0; k2 < 3; k2++) {
                            if (possibleKiller.get(3 * j1 + k1).get(3 * j2 + k2).size() <= i
                                    & boardKiller[3 * j1 + k1][3 * j2 + k2] == -1)
                                pointSet.add(new Point(3 * j1 + k1, 3 * j2 + k2));
                        }
                    }
                    if (pointSet.size() >= i) {
                        intSet = new int[pointSet.size()];
                        for (int k = 0; k < pointSet.size(); k++)
                            intSet[k] = k;
                        generateSubsets(allSubsets, intSet, new int[i], 0, 0);
                        for (int k1 = 0; k1 < allSubsets.size(); k1++) {
                            for (int k2 = 0; k2 < allSubsets.elementAt(k1).length; k2++) {
                                for (int k3 = 0; k3 < possibleKiller.get(
                                        pointSet.elementAt(allSubsets.elementAt(k1)[k2]).x).get(
                                        pointSet.elementAt(allSubsets.elementAt(k1)[k2]).y).size(); k3++) {
                                    if (values.indexOf(possibleKiller.get(
                                            pointSet.elementAt(allSubsets.elementAt(k1)[k2]).x).get(
                                            pointSet.elementAt(allSubsets.elementAt(k1)[k2]).y).get(
                                            k3)) == -1) {
                                        values.add(possibleKiller.get(
                                                pointSet.elementAt(allSubsets.elementAt(k1)[k2]).x).get(
                                                pointSet.elementAt(allSubsets.elementAt(k1)[k2]).y).get(
                                                k3));
                                    }
                                }
                            }
                            if (values.size() == i) {
                                goodSubsets.add(allSubsets.elementAt(k1));
                            }
                            values.removeAllElements();
                        }
                    }
                    if (goodSubsets.size() > 0) {
                        for (int k1 = 0; k1 < goodSubsets.size(); k1++) {
                            for (int k2 = 0; k2 < goodSubsets.elementAt(k1).length; k2++) {
                                for (int k3 = 0; k3 < possibleKiller.get(
                                        pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).x).get(
                                        pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).y).size(); k3++) {
                                    if (values.indexOf(possibleKiller.get(
                                            pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).x).get(
                                            pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).y).get(
                                            k3)) == -1) {
                                        values.add(possibleKiller.get(
                                                pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).x).get(
                                                pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).y).get(
                                                k3));
                                    }
                                }
                            }
                            for (int k2 = 0; k2 < 3; k2++) {
                                for (int k3 = 0; k3 < 3; k3++) {
                                    temp = true;
                                    for (int k4 = 0; k4 < goodSubsets.elementAt(k1).length; k4++) {
                                        if (pointSet.elementAt(goodSubsets.elementAt(k1)[k4]).equals(
                                                new Point(3 * j1 + k2, 3 * j2 + k3)) == true)
                                            temp = false;
                                    }
                                    if (temp) {
                                        for (int l = 0; l < values.size(); l++) {
                                            idx =
                                                    possibleKiller.get(3 * j1 + k2).get(3 * j2 + k3).indexOf(
                                                            values.elementAt(l));
                                            if (idx != -1) {
                                                possibleKiller.get(3 * j1 + k2).get(3 * j2 + k3).remove(
                                                        idx);
                                                changed = true;
                                            }
                                        }
                                    }
                                }
                            }
                            values.removeAllElements();
                        }
                    }
                }
            }
        }
        return changed;
    }

    public void singleOuttie() {
        List<Area> boxAreas;
        List<Point> tempPoints;
        int totalPoints = 0, totalValue = 0;
        Point outtie = null;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boxAreas = new ArrayList<Area>();
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        for (int k = 0; k < areas.size(); k++) {
                            if (areas.get(k).pointUsed(new Point(3 * i + x, 3 * j + y))
                                    && boxAreas.indexOf(areas.get(k)) == -1)
                                boxAreas.add(areas.get(k));
                        }
                    }
                }
                totalPoints = 0;
                for (int k = 0; k < boxAreas.size(); k++)
                    totalPoints = totalPoints + boxAreas.get(k).getList().size();
                if (totalPoints == 10) {
                    totalValue = 0;
                    for (int k = 0; k < boxAreas.size(); k++) {
                        totalValue = totalValue + boxAreas.get(k).getValue();
                        tempPoints = boxAreas.get(k).getList();
                        for (int l = 0; l < tempPoints.size(); l++) {
                            if (tempPoints.get(l).x / 3 != i || tempPoints.get(l).y / 3 != j)
                                outtie = tempPoints.get(l);
                        }
                    }
                    if (0 < totalValue - 45 && totalValue - 45 < 9) {
                        boardKiller[outtie.x][outtie.y] = totalValue - 45;
                    }
                }
            }
        }
    }

    public boolean rowConflictHex(int y, int c) {
        boolean temp = false;
        for (int i = 0; i < 16 && !temp; i++) {
            if (boardHex[i][y] == c) {
                temp = true;
            } else {
                temp = false;
            }
        }
        return temp;
    }

    public boolean colConflictHex(int x, int c) {
        boolean temp = false;
        for (int i = 0; i < 16 && !temp; i++) {
            if (boardHex[x][i] == c) {
                temp = true;
            } else {
                temp = false;
            }
        }
        return temp;
    }

    public boolean boxConflictHex(int xx, int yy, int c) {
        int x = 4 * (int) Math.floor(xx / 4);
        int y = 4 * (int) Math.floor(yy / 4);
        for (int i = x; i < x + 4; i++) {
            for (int j = y; j < y + 4; j++) {
                if (boardHex[i][j] == c) {
                    return true;
                }
            }
        }
        return false;
    }

    public void humanStrategiesHex() {
        boolean changed = true;
        while (changed) {
            changed = false;
            changed =
                    changed || onePossibleHex() || nakedSingleHex() || hiddenSingleHex()
                            || blockAndCRHex() || nakedSubsetHex() || candidateLineHex()
                            || doublePairHex() || multipleLinesHex();
        }
    }

    /**
     * Check if there exist squares for which only one value is possible
     *
     * @return if this was the case
     */
    public boolean onePossibleHex() {
        boolean changed = false;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (boardHex[i][j] == -1 & possibleHex.get(i).get(j).size() == 1) {
                    boardHex[i][j] = possibleHex.get(i).get(j).get(0);
                    boardTextHex[i][j].setText(valToTextHex(boardHex[i][j]));
                    labelCellHex[i][j].layout();
                    changed = true;
                }
            }
        }
        if (changed) {
            updatePossibilitiesHex();
        }
        return changed;
    }

    /**
     * Adds c to the possible values for "neighbors" of (xx,yy)
     *
     * @param xx the horizontal coordinate
     * @param yy the vertical coordinate
     * @param c the entered value
     */
    public void addPossible(int xx, int yy, int c) {
        int idx;
        for (int i = 0; i < 16; i++) {
            if (boardHex[i][yy] == -1) {
                idx = possibleHex.get(i).get(yy).indexOf(c);
                if (idx == -1)
                    possibleHex.get(i).get(yy).add(c);
            }
        }
        for (int j = 0; j < 16; j++) {
            if (boardHex[xx][j] == -1) {
                idx = possibleHex.get(xx).get(j).indexOf(c);
                if (idx == -1)
                    possibleHex.get(xx).get(j).add(c);
            }
        }
        int x = 4 * (int) Math.floor(xx / 4);
        int y = 4 * (int) Math.floor(yy / 4);
        for (int i = x; i < x + 4; i++) {
            for (int j = y; j < y + 4; j++) {
                if (boardHex[i][j] == -1) {
                    idx = possibleHex.get(i).get(j).indexOf(c);
                    if (idx == -1)
                        possibleHex.get(i).get(j).add(c);
                }
            }
        }
    }

    /**
     * Checks if there exists a square which is a "naked single"
     *
     * @return if this was the case
     */
    public boolean nakedSingleHex() {
        boolean changed = false;
        Vector<Integer> possible;
        int idx;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (boardHex[i][j] == -1) {
                    possible = new Vector<Integer>();
                    for (int k = 0; k < 16; k++)
                        possible.add(k);
                    for (int k = 0; k < 16; k++)
                        if (boardHex[k][j] != -1) {
                            idx = possible.indexOf(boardHex[k][j]);
                            if (idx != -1)
                                possible.remove(idx);
                        }
                    for (int k = 0; k < 16; k++)
                        if (boardHex[i][k] != -1) {
                            idx = possible.indexOf(boardHex[i][k]);
                            if (idx != -1)
                                possible.remove(idx);
                        }
                    int x = 4 * (int) Math.floor(i / 4);
                    int y = 4 * (int) Math.floor(j / 4);
                    for (int k = x; k < x + 4; k++) {
                        for (int l = y; l < y + 4; l++) {
                            if (boardHex[k][l] != -1) {
                                idx = possible.indexOf(boardHex[k][l]);
                                if (idx != -1)
                                    possible.remove(idx);
                            }
                        }
                    }
                    if (possible.size() == 1) {
                        boardHex[i][j] = possible.elementAt(0);
                        boardTextHex[i][j].setText(valToTextHex(boardHex[i][j]));
                        labelCellHex[i][j].layout();
                        changed = true;
                    }
                }
            }
        }
        if (changed) {
            updatePossibilitiesHex();
        }
        return changed;
    }

    /**
     * Checks if there exists a square which is a "hidden single"
     *
     * @return if this was the case
     */
    public boolean hiddenSingleHex() {
        boolean changed = false;
        Vector<Integer> set1, set2, set3;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (boardHex[i][j] == -1) {
                    set1 = new Vector<Integer>();
                    set2 = new Vector<Integer>();
                    set3 = new Vector<Integer>();
                    int x = 4 * (int) Math.floor(i / 4);
                    int y = 4 * (int) Math.floor(j / 4);
                    boolean neighborsSet =
                            (boardHex[x][j] != -1 || x == i)
                                    & (boardHex[x + 1][j] != -1 || x + 1 == i)
                                    & (boardHex[x + 2][j] != -1 || x + 2 == i)
                                    & (boardHex[x + 3][j] != -1 || x + 3 == i);
                    if (neighborsSet) {
                        if (y == j) {
                            for (int k = 0; k < 16; k++)
                                if ((4 * (k / 4) != x) & (boardHex[k][y + 1] != -1))
                                    set1.add(boardHex[k][y + 1]);
                            for (int k = 0; k < 16; k++)
                                if ((4 * (k / 4) != x) & (boardHex[k][y + 2] != -1))
                                    set2.add(boardHex[k][y + 2]);
                            for (int k = 0; k < 16; k++)
                                if ((4 * (k / 4) != x) & (boardHex[k][y + 3] != -1))
                                    set3.add(boardHex[k][y + 3]);
                        } else if (y + 1 == j) {
                            for (int k = 0; k < 16; k++)
                                if ((4 * (k / 4) != x) & (boardHex[k][y] != -1))
                                    set1.add(boardHex[k][y]);
                            for (int k = 0; k < 16; k++)
                                if ((4 * (k / 4) != x) & (boardHex[k][y + 2] != -1))
                                    set2.add(boardHex[k][y + 2]);
                            for (int k = 0; k < 16; k++)
                                if ((4 * (k / 4) != x) & (boardHex[k][y + 3] != -1))
                                    set3.add(boardHex[k][y + 3]);
                        } else if (y + 2 == j) {
                            for (int k = 0; k < 16; k++)
                                if ((4 * (k / 4) != x) & (boardHex[k][y] != -1))
                                    set1.add(boardHex[k][y]);
                            for (int k = 0; k < 16; k++)
                                if ((4 * (k / 4) != x) & (boardHex[k][y + 1] != -1))
                                    set2.add(boardHex[k][y + 1]);
                            for (int k = 0; k < 16; k++)
                                if ((4 * (k / 4) != x) & (boardHex[k][y + 3] != -1))
                                    set3.add(boardHex[k][y + 3]);
                        } else {
                            for (int k = 0; k < 16; k++)
                                if ((4 * (k / 4) != x) & (boardHex[k][y] != -1))
                                    set1.add(boardHex[k][y]);
                            for (int k = 0; k < 16; k++)
                                if ((4 * (k / 4) != x) & (boardHex[k][y + 1] != -1))
                                    set2.add(boardHex[k][y + 1]);
                            for (int k = 0; k < 16; k++)
                                if ((4 * (k / 4) != x) & (boardHex[k][y + 2] != -1))
                                    set3.add(boardHex[k][y + 2]);
                        }
                        int idx;
                        if (x == i) {
                            idx = set1.indexOf(boardHex[x + 1][j]);
                            if (idx != -1)
                                set1.remove(idx);
                            idx = set1.indexOf(boardHex[x + 2][j]);
                            if (idx != -1)
                                set1.remove(idx);
                            idx = set1.indexOf(boardHex[x + 3][j]);
                            if (idx != -1)
                                set1.remove(idx);
                        } else if (x + 1 == i) {
                            idx = set1.indexOf(boardHex[x][j]);
                            if (idx != -1)
                                set1.remove(idx);
                            idx = set1.indexOf(boardHex[x + 2][j]);
                            if (idx != -1)
                                set1.remove(idx);
                            idx = set1.indexOf(boardHex[x + 3][j]);
                            if (idx != -1)
                                set1.remove(idx);
                        } else if (x + 2 == i) {
                            idx = set1.indexOf(boardHex[x][j]);
                            if (idx != -1)
                                set1.remove(idx);
                            idx = set1.indexOf(boardHex[x + 1][j]);
                            if (idx != -1)
                                set1.remove(idx);
                            idx = set1.indexOf(boardHex[x + 3][j]);
                            if (idx != -1)
                                set1.remove(idx);
                        } else {
                            idx = set1.indexOf(boardHex[x][j]);
                            if (idx != -1)
                                set1.remove(idx);
                            idx = set1.indexOf(boardHex[x + 1][j]);
                            if (idx != -1)
                                set1.remove(idx);
                            idx = set1.indexOf(boardHex[x + 2][j]);
                            if (idx != -1)
                                set1.remove(idx);
                        }
                        if (set1.size() != 0 & set2.size() != 0 & set3.size() != 0) {
                            for (int k = 0; k < set1.size(); k++) {
                                if (set2.indexOf(set1.elementAt(k)) != -1
                                        & set3.indexOf(set1.elementAt(k)) != -1) {
                                    boardHex[i][j] = set1.elementAt(k);
                                    boardTextHex[i][j].setText(valToTextHex(boardHex[i][j]));
                                    labelCellHex[i][j].layout();
                                    changed = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (boardHex[i][j] == -1) {
                        set1 = new Vector<Integer>();
                        set2 = new Vector<Integer>();
                        set3 = new Vector<Integer>();
                        neighborsSet =
                                (boardHex[i][y] != -1 || y == j)
                                        & (boardHex[i][y + 1] != -1 || y + 1 == j)
                                        & (boardHex[i][y + 2] != -1 || y + 2 == j)
                                        & (boardHex[i][y + 3] != -1 || y + 3 == j);
                        if (neighborsSet) {
                            if (x == i) {
                                for (int k = 0; k < 16; k++)
                                    if ((4 * (k / 4) != y) & (boardHex[x + 1][k] != -1))
                                        set1.add(boardHex[x + 1][k]);
                                for (int k = 0; k < 16; k++)
                                    if ((4 * (k / 4) != y) & (boardHex[x + 2][k] != -1))
                                        set2.add(boardHex[x + 2][k]);
                                for (int k = 0; k < 16; k++)
                                    if ((4 * (k / 4) != y) & (boardHex[x + 3][k] != -1))
                                        set3.add(boardHex[x + 3][k]);
                            } else if (x + 1 == i) {
                                for (int k = 0; k < 16; k++)
                                    if ((4 * (k / 4) != y) & (boardHex[x][k] != -1))
                                        set1.add(boardHex[x][k]);
                                for (int k = 0; k < 16; k++)
                                    if ((4 * (k / 4) != y) & (boardHex[x + 2][k] != -1))
                                        set2.add(boardHex[x + 2][k]);
                                for (int k = 0; k < 16; k++)
                                    if ((4 * (k / 4) != y) & (boardHex[x + 3][k] != -1))
                                        set3.add(boardHex[x + 3][k]);
                            } else if (x + 2 == i) {
                                for (int k = 0; k < 16; k++)
                                    if ((4 * (k / 4) != y) & (boardHex[x][k] != -1))
                                        set1.add(boardHex[x][k]);
                                for (int k = 0; k < 16; k++)
                                    if ((4 * (k / 4) != y) & (boardHex[x + 1][k] != -1))
                                        set2.add(boardHex[x + 1][k]);
                                for (int k = 0; k < 16; k++)
                                    if ((4 * (k / 4) != y) & (boardHex[x + 3][k] != -1))
                                        set3.add(boardHex[x + 3][k]);
                            } else {
                                for (int k = 0; k < 16; k++)
                                    if ((4 * (k / 4) != y) & (boardHex[x][k] != -1))
                                        set1.add(boardHex[x][k]);
                                for (int k = 0; k < 16; k++)
                                    if ((4 * (k / 4) != y) & (boardHex[x + 1][k] != -1))
                                        set2.add(boardHex[x + 1][k]);
                                for (int k = 0; k < 16; k++)
                                    if ((4 * (k / 4) != y) & (boardHex[x + 2][k] != -1))
                                        set3.add(boardHex[x + 2][k]);
                            }
                            int idx;
                            if (y == j) {
                                idx = set1.indexOf(boardHex[i][y + 1]);
                                if (idx != -1)
                                    set1.remove(idx);
                                idx = set1.indexOf(boardHex[i][y + 2]);
                                if (idx != -1)
                                    set1.remove(idx);
                                idx = set1.indexOf(boardHex[i][y + 3]);
                                if (idx != -1)
                                    set1.remove(idx);
                            } else if (y + 1 == j) {
                                idx = set1.indexOf(boardHex[i][y]);
                                if (idx != -1)
                                    set1.remove(idx);
                                idx = set1.indexOf(boardHex[i][y + 2]);
                                if (idx != -1)
                                    set1.remove(idx);
                                idx = set1.indexOf(boardHex[i][y + 3]);
                                if (idx != -1)
                                    set1.remove(idx);
                            } else if (y + 2 == j) {
                                idx = set1.indexOf(boardHex[i][y]);
                                if (idx != -1)
                                    set1.remove(idx);
                                idx = set1.indexOf(boardHex[i][y + 1]);
                                if (idx != -1)
                                    set1.remove(idx);
                                idx = set1.indexOf(boardHex[i][y + 3]);
                                if (idx != -1)
                                    set1.remove(idx);
                            } else {
                                idx = set1.indexOf(boardHex[i][y]);
                                if (idx != -1)
                                    set1.remove(idx);
                                idx = set1.indexOf(boardHex[i][y + 1]);
                                if (idx != -1)
                                    set1.remove(idx);
                                idx = set1.indexOf(boardHex[i][y + 2]);
                                if (idx != -1)
                                    set1.remove(idx);
                            }
                            if (set1.size() != 0 & set2.size() != 0 & set3.size() != 0) {
                                for (int k = 0; k < set1.size(); k++) {
                                    if (set2.indexOf(set1.elementAt(k)) != -1
                                            & set3.indexOf(set1.elementAt(k)) != -1) {
                                        boardHex[i][j] = set1.elementAt(k);
                                        boardTextHex[i][j].setText(valToTextHex(boardHex[i][j]));
                                        labelCellHex[i][j].layout();
                                        changed = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (changed) {
            updatePossibilitiesHex();
        }
        return changed;
    }

    /**
     * Checks if there are Block and Column/Row interactions
     *
     * @return if this was the case
     */
    public boolean blockAndCRHex() {
        boolean changed = false;
        Vector<Integer> set;
        int idx;
        for (int i1 = 0; i1 < 4; i1++) {
            for (int i2 = 0; i2 < 4; i2++) {
                for (int i3 = 0; i3 < 4; i3++) {
                    for (int i4 = 0; i4 < 4; i4++) {
                        // remove from columns
                        if (boardHex[4 * i1 + i4][4 * i2 + i3] == -1
                                & boardHex[4 * i1 + i4][4 * i2 + (1 + i3) % 4] == -1
                                & boardHex[4 * i1 + i4][4 * i2 + (2 + i3) % 4] == -1 &

                                boardHex[4 * i1 + (1 + i4) % 4][4 * i2 + i3] != -1
                                & boardHex[4 * i1 + (1 + i4) % 4][4 * i2 + (1 + i3) % 4] != -1
                                & boardHex[4 * i1 + (1 + i4) % 4][4 * i2 + (2 + i3) % 4] != -1 &

                                boardHex[4 * i1 + (2 + i4) % 4][4 * i2 + i3] != -1
                                & boardHex[4 * i1 + (2 + i4) % 4][4 * i2 + (1 + i3) % 4] != -1
                                & boardHex[4 * i1 + (2 + i4) % 4][4 * i2 + (2 + i3) % 4] != -1 &

                                boardHex[4 * i1 + (3 + i4) % 4][4 * i2 + i3] != -1
                                & boardHex[4 * i1 + (3 + i4) % 4][4 * i2 + (1 + i3) % 4] != -1
                                & boardHex[4 * i1 + (3 + i4) % 4][4 * i2 + (2 + i3) % 4] != -1) {
                            set = new Vector<Integer>();
                            for (int j = 0; j < 16; j++) {
                                if ((j / 4) != i1 & boardHex[j][4 * i2 + (3 + i3) % 4] != -1) {
                                    set.add(boardHex[j][4 * i2 + (3 + i3) % 4]);
                                }
                            }
                            if (set.size() > 0) {
                                for (int j = 0; j < set.size(); j++) {
                                    if (boardHex[4 * i1 + (1 + i4) % 4][4 * i2 + i3] != set.elementAt(j)
                                            & boardHex[4 * i1 + (1 + i4) % 4][4 * i2 + (1 + i3) % 4] != set.elementAt(j)
                                            & boardHex[4 * i1 + (1 + i4) % 4][4 * i2 + (2 + i3) % 4] != set.elementAt(j)
                                            &

                                            boardHex[4 * i1 + (2 + i4) % 4][4 * i2 + i3] != set.elementAt(j)
                                            & boardHex[4 * i1 + (2 + i4) % 4][4 * i2 + (1 + i3) % 4] != set.elementAt(j)
                                            & boardHex[4 * i1 + (2 + i4) % 4][4 * i2 + (2 + i3) % 4] != set.elementAt(j)
                                            &

                                            boardHex[4 * i1 + (3 + i4) % 4][4 * i2 + i3] != set.elementAt(j)
                                            & boardHex[4 * i1 + (3 + i4) % 4][4 * i2 + (1 + i3) % 4] != set.elementAt(j)
                                            & boardHex[4 * i1 + (3 + i4) % 4][4 * i2 + (2 + i3) % 4] != set.elementAt(j)) {
                                        for (int k = 0; k < 16; k++) {
                                            if ((k / 4) != i2) {
                                                idx =
                                                        possibleHex.get(4 * i1 + i4).get(k).indexOf(
                                                                set.elementAt(j));
                                                if (idx != -1) {
                                                    possibleHex.get(4 * i1 + i4).get(k).remove(idx);
                                                    changed = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // remove from rows
                        if (boardHex[4 * i2 + i3][4 * i1 + i4] == -1
                                & boardHex[4 * i2 + (1 + i3) % 4][4 * i1 + i4] == -1
                                & boardHex[4 * i2 + (2 + i3) % 4][4 * i1 + i4] == -1 &

                                boardHex[4 * i2 + i3][4 * i1 + (1 + i4) % 4] != -1
                                & boardHex[4 * i2 + (1 + i3) % 4][4 * i1 + (1 + i4) % 4] != -1
                                & boardHex[4 * i2 + (2 + i3) % 4][4 * i1 + (1 + i4) % 4] != -1 &

                                boardHex[4 * i2 + i3][4 * i1 + (2 + i4) % 4] != -1
                                & boardHex[4 * i2 + (1 + i3) % 4][4 * i1 + (2 + i4) % 4] != -1
                                & boardHex[4 * i2 + (2 + i3) % 4][4 * i1 + (2 + i4) % 4] != -1 &

                                boardHex[4 * i2 + i3][4 * i1 + (3 + i4) % 4] != -1
                                & boardHex[4 * i2 + (1 + i3) % 4][4 * i1 + (3 + i4) % 4] != -1
                                & boardHex[4 * i2 + (2 + i3) % 4][4 * i1 + (3 + i4) % 4] != -1) {
                            set = new Vector<Integer>();
                            for (int j = 0; j < 16; j++) {
                                if ((j / 4) != i1 & boardHex[4 * i2 + (3 + i3) % 4][j] != -1) {
                                    set.add(boardHex[4 * i2 + (3 + i3) % 4][j]);
                                }
                            }
                            if (set.size() > 0) {
                                for (int j = 0; j < set.size(); j++) {
                                    if (boardHex[4 * i2 + i3][4 * i1 + (1 + i4) % 4] != set.elementAt(j)
                                            & boardHex[4 * i2 + (1 + i3) % 4][4 * i1 + (1 + i4) % 4] != set.elementAt(j)
                                            & boardHex[4 * i2 + (2 + i3) % 4][4 * i1 + (1 + i4) % 4] != set.elementAt(j)
                                            &

                                            boardHex[4 * i2 + i3][4 * i1 + (2 + i4) % 4] != set.elementAt(j)
                                            & boardHex[4 * i2 + (1 + i3) % 4][4 * i1 + (2 + i4) % 4] != set.elementAt(j)
                                            & boardHex[4 * i2 + (2 + i3) % 4][4 * i1 + (2 + i4) % 4] != set.elementAt(j)
                                            &

                                            boardHex[4 * i2 + i3][4 * i1 + (3 + i4) % 4] != set.elementAt(j)
                                            & boardHex[4 * i2 + (1 + i3) % 4][4 * i1 + (3 + i4) % 4] != set.elementAt(j)
                                            & boardHex[4 * i2 + (2 + i3) % 4][4 * i1 + (3 + i4) % 4] != set.elementAt(j)) {
                                        for (int k = 0; k < 16; k++) {
                                            if ((k / 4) != i2) {
                                                idx =
                                                        possibleHex.get(k).get(4 * i1 + i4).indexOf(
                                                                set.elementAt(j));
                                                if (idx != -1) {
                                                    possibleHex.get(k).get(4 * i1 + i4).remove(idx);
                                                    changed = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return changed;
    }

    public boolean nakedSubsetHex() {
        boolean changed = false, temp;
        int total, idx;
        int[] intSet;
        Vector<Integer> set, values;
        Vector<Integer[]> allSubsets, goodSubsets;
        Vector<Point> pointSet;
        int i = 2; // for i = 2, the subsets need to have size exactly 2
        for (int j = 0; j < 16; j++) {
            set = new Vector<Integer>();
            for (int k = 0; k < 16; k++) {
                if (possibleHex.get(j).get(k).size() == i & boardHex[j][k] == -1)
                    set.add(k);
            }
            while (set.size() >= i) {
                total = 1;
                for (int k = 1; k < set.size(); k++) {
                    if (possibleHex.get(j).get(set.elementAt(0)).equals(
                            possibleHex.get(j).get(set.elementAt(k))))
                        total++;
                }
                if (total != i) {
                    set.remove(0);
                } else {
                    for (int k = set.size() - 1; k > 0; k--) {
                        if (!possibleHex.get(j).get(set.elementAt(0)).equals(
                                possibleHex.get(j).get(set.elementAt(k))))
                            set.remove(k);
                    }
                    break;
                }
            }
            if (set.size() == i) {
                for (int k = 0; k < 16; k++) {
                    if (set.indexOf(k) == -1) {
                        for (int l = 0; l < possibleHex.get(j).get(set.elementAt(0)).size(); l++) {
                            idx =
                                    possibleHex.get(j).get(k).indexOf(
                                            possibleHex.get(j).get(set.elementAt(0)).get(l));
                            if (idx != -1) {
                                possibleHex.get(j).get(k).remove(idx);
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
        for (int j = 0; j < 16; j++) {
            set = new Vector<Integer>();
            for (int k = 0; k < 16; k++) {
                if (possibleHex.get(k).get(j).size() == i & boardHex[k][j] == -1)
                    set.add(k);
            }
            while (set.size() >= i) {
                total = 1;
                for (int k = 1; k < set.size(); k++) {
                    if (possibleHex.get(set.elementAt(0)).get(j).equals(
                            possibleHex.get(set.elementAt(k)).get(j)))
                        total++;
                }
                if (total != i) {
                    set.remove(0);
                } else {
                    for (int k = set.size() - 1; k > 0; k--) {
                        if (!possibleHex.get(set.elementAt(0)).get(j).equals(
                                possibleHex.get(set.elementAt(k)).get(j)))
                            set.remove(k);
                    }
                    break;
                }
            }
            if (set.size() == i) {
                for (int k = 0; k < 16; k++) {
                    if (set.indexOf(k) == -1) {
                        for (int l = 0; l < possibleHex.get(set.elementAt(0)).get(j).size(); l++) {
                            idx =
                                    possibleHex.get(k).get(j).indexOf(
                                            possibleHex.get(set.elementAt(0)).get(j).get(l));
                            if (idx != -1) {
                                possibleHex.get(k).get(j).remove(idx);
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
        for (int j1 = 0; j1 < 4; j1++) {
            for (int j2 = 0; j2 < 4; j2++) {
                pointSet = new Vector<Point>();
                for (int k1 = 0; k1 < 4; k1++) {
                    for (int k2 = 0; k2 < 4; k2++) {
                        if (possibleHex.get(4 * j1 + k1).get(4 * j2 + k2).size() == i
                                & boardHex[4 * j1 + k1][4 * j2 + k2] == -1)
                            pointSet.add(new Point(4 * j1 + k1, 4 * j2 + k2));
                    }
                }
                while (pointSet.size() >= i) {
                    total = 1;
                    for (int k = 1; k < pointSet.size(); k++) {
                        if (possibleHex.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y).equals(
                                possibleHex.get(pointSet.elementAt(k).x).get(
                                        pointSet.elementAt(k).y)))
                            total++;
                    }
                    if (total != i) {
                        pointSet.remove(0);
                    } else {
                        for (int k = pointSet.size() - 1; k > 0; k--) {
                            if (!possibleHex.get(pointSet.elementAt(0).x).get(
                                    pointSet.elementAt(0).y).equals(
                                    possibleHex.get(pointSet.elementAt(0).x).get(
                                            pointSet.elementAt(0).y)))
                                pointSet.remove(k);
                        }
                        break;
                    }
                }
                if (pointSet.size() == i) {
                    for (int k1 = 0; k1 < 4; k1++) {
                        for (int k2 = 0; k2 < 4; k2++) {
                            if (pointSet.indexOf(new Point(4 * j1 + k1, 4 * j2 + k2)) == -1) {
                                for (int l = 0; l < possibleHex.get(pointSet.elementAt(0).x).get(
                                        pointSet.elementAt(0).y).size(); l++) {
                                    idx =
                                            possibleHex.get(4 * j1 + k1).get(4 * j2 + k2).indexOf(
                                                    possibleHex.get(pointSet.elementAt(0).x).get(
                                                            pointSet.elementAt(0).y).get(l));
                                    if (idx != -1) {
                                        possibleHex.get(4 * j1 + k1).get(4 * j2 + k2).remove(idx);
                                        changed = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (i = 3; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                set = new Vector<Integer>();
                values = new Vector<Integer>();
                allSubsets = new Vector<Integer[]>();
                goodSubsets = new Vector<Integer[]>();
                for (int k = 0; k < 16; k++) {
                    if (possibleHex.get(j).get(k).size() <= i & boardHex[j][k] == -1)
                        set.add(k);
                }
                if (set.size() >= i) {
                    intSet = new int[set.size()];
                    for (int k = 0; k < set.size(); k++)
                        intSet[k] = set.elementAt(k);
                    generateSubsets(allSubsets, intSet, new int[i], 0, 0);
                    for (int k1 = 0; k1 < allSubsets.size(); k1++) {
                        for (int k2 = 0; k2 < allSubsets.elementAt(k1).length; k2++) {
                            for (int k3 = 0; k3 < possibleHex.get(j).get(
                                    allSubsets.elementAt(k1)[k2]).size(); k3++) {
                                if (values.indexOf(possibleHex.get(j).get(
                                        allSubsets.elementAt(k1)[k2]).get(k3)) == -1) {
                                    values.add(possibleHex.get(j).get(allSubsets.elementAt(k1)[k2]).get(
                                            k3));
                                }
                            }
                        }
                        if (values.size() == i) {
                            goodSubsets.add(allSubsets.elementAt(k1));
                        }
                        values.removeAllElements();
                    }
                }
                if (goodSubsets.size() > 0) {
                    for (int k1 = 0; k1 < goodSubsets.size(); k1++) {
                        for (int k2 = 0; k2 < goodSubsets.elementAt(k1).length; k2++) {
                            for (int k3 = 0; k3 < possibleHex.get(j).get(
                                    goodSubsets.elementAt(k1)[k2]).size(); k3++) {
                                if (values.indexOf(possibleHex.get(j).get(
                                        goodSubsets.elementAt(k1)[k2]).get(k3)) == -1) {
                                    values.add(possibleHex.get(j).get(goodSubsets.elementAt(k1)[k2]).get(
                                            k3));
                                }
                            }
                        }
                        for (int k2 = 0; k2 < 16; k2++) {
                            temp = true;
                            for (int k3 = 0; k3 < goodSubsets.elementAt(k1).length; k3++) {
                                if (goodSubsets.elementAt(k1)[k3] == k2)
                                    temp = false;
                            }
                            if (temp) {
                                for (int l = 0; l < values.size(); l++) {
                                    idx = possibleHex.get(j).get(k2).indexOf(values.elementAt(l));
                                    if (idx != -1) {
                                        possibleHex.get(j).get(k2).remove(idx);
                                        changed = true;
                                    }
                                }
                            }
                        }
                        values.removeAllElements();
                    }
                }
            }
            for (int j = 0; j < 16; j++) {
                set = new Vector<Integer>();
                values = new Vector<Integer>();
                allSubsets = new Vector<Integer[]>();
                goodSubsets = new Vector<Integer[]>();
                for (int k = 0; k < 16; k++) {
                    if (possibleHex.get(k).get(j).size() <= i & boardHex[k][j] == -1)
                        set.add(k);
                }
                if (set.size() >= i) {
                    intSet = new int[set.size()];
                    for (int k = 0; k < set.size(); k++)
                        intSet[k] = set.elementAt(k);
                    generateSubsets(allSubsets, intSet, new int[i], 0, 0);
                    for (int k1 = 0; k1 < allSubsets.size(); k1++) {
                        for (int k2 = 0; k2 < allSubsets.elementAt(k1).length; k2++) {
                            for (int k3 = 0; k3 < possibleHex.get(allSubsets.elementAt(k1)[k2]).get(
                                    j).size(); k3++) {
                                if (values.indexOf(possibleHex.get(allSubsets.elementAt(k1)[k2]).get(
                                        j).get(k3)) == -1) {
                                    values.add(possibleHex.get(allSubsets.elementAt(k1)[k2]).get(j).get(
                                            k3));
                                }
                            }
                        }
                        if (values.size() == i) {
                            goodSubsets.add(allSubsets.elementAt(k1));
                        }
                        values.removeAllElements();
                    }
                }
                if (goodSubsets.size() > 0) {
                    for (int k1 = 0; k1 < goodSubsets.size(); k1++) {
                        for (int k2 = 0; k2 < goodSubsets.elementAt(k1).length; k2++) {
                            for (int k3 = 0; k3 < possibleHex.get(goodSubsets.elementAt(k1)[k2]).get(
                                    j).size(); k3++) {
                                if (values.indexOf(possibleHex.get(goodSubsets.elementAt(k1)[k2]).get(
                                        j).get(k3)) == -1) {
                                    values.add(possibleHex.get(goodSubsets.elementAt(k1)[k2]).get(j).get(
                                            k3));
                                }
                            }
                        }
                        for (int k2 = 0; k2 < 16; k2++) {
                            temp = true;
                            for (int k3 = 0; k3 < goodSubsets.elementAt(k1).length; k3++) {
                                if (goodSubsets.elementAt(k1)[k3] == k2)
                                    temp = false;
                            }
                            if (temp) {
                                for (int l = 0; l < values.size(); l++) {
                                    idx = possibleHex.get(k2).get(j).indexOf(values.elementAt(l));
                                    if (idx != -1) {
                                        possibleHex.get(k2).get(j).remove(idx);
                                        changed = true;
                                    }
                                }
                            }
                        }
                        values.removeAllElements();
                    }
                }
            }

            for (int j1 = 0; j1 < 4; j1++) {
                for (int j2 = 0; j2 < 4; j2++) {
                    set = new Vector<Integer>();
                    values = new Vector<Integer>();
                    allSubsets = new Vector<Integer[]>();
                    goodSubsets = new Vector<Integer[]>();
                    pointSet = new Vector<Point>();
                    for (int k1 = 0; k1 < 4; k1++) {
                        for (int k2 = 0; k2 < 4; k2++) {
                            if (possibleHex.get(4 * j1 + k1).get(4 * j2 + k2).size() <= i
                                    & boardHex[4 * j1 + k1][4 * j2 + k2] == -1)
                                pointSet.add(new Point(4 * j1 + k1, 4 * j2 + k2));
                        }
                    }
                    if (pointSet.size() >= i) {
                        intSet = new int[pointSet.size()];
                        for (int k = 0; k < pointSet.size(); k++)
                            intSet[k] = k;
                        generateSubsets(allSubsets, intSet, new int[i], 0, 0);
                        for (int k1 = 0; k1 < allSubsets.size(); k1++) {
                            for (int k2 = 0; k2 < allSubsets.elementAt(k1).length; k2++) {
                                for (int k3 = 0; k3 < possibleHex.get(
                                        pointSet.elementAt(allSubsets.elementAt(k1)[k2]).x).get(
                                        pointSet.elementAt(allSubsets.elementAt(k1)[k2]).y).size(); k3++) {
                                    if (values.indexOf(possibleHex.get(
                                            pointSet.elementAt(allSubsets.elementAt(k1)[k2]).x).get(
                                            pointSet.elementAt(allSubsets.elementAt(k1)[k2]).y).get(
                                            k3)) == -1) {
                                        values.add(possibleHex.get(
                                                pointSet.elementAt(allSubsets.elementAt(k1)[k2]).x).get(
                                                pointSet.elementAt(allSubsets.elementAt(k1)[k2]).y).get(
                                                k3));
                                    }
                                }
                            }
                            if (values.size() == i) {
                                goodSubsets.add(allSubsets.elementAt(k1));
                            }
                            values.removeAllElements();
                        }
                    }
                    if (goodSubsets.size() > 0) {
                        for (int k1 = 0; k1 < goodSubsets.size(); k1++) {
                            for (int k2 = 0; k2 < goodSubsets.elementAt(k1).length; k2++) {
                                for (int k3 = 0; k3 < possibleHex.get(
                                        pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).x).get(
                                        pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).y).size(); k3++) {
                                    if (values.indexOf(possibleHex.get(
                                            pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).x).get(
                                            pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).y).get(
                                            k3)) == -1) {
                                        values.add(possibleHex.get(
                                                pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).x).get(
                                                pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).y).get(
                                                k3));
                                    }
                                }
                            }
                            for (int k2 = 0; k2 < 4; k2++) {
                                for (int k3 = 0; k3 < 4; k3++) {
                                    temp = true;
                                    for (int k4 = 0; k4 < goodSubsets.elementAt(k1).length; k4++) {
                                        if (pointSet.elementAt(goodSubsets.elementAt(k1)[k4]).equals(
                                                new Point(4 * j1 + k2, 4 * j2 + k3)) == true)
                                            temp = false;
                                    }
                                    if (temp) {
                                        for (int l = 0; l < values.size(); l++) {
                                            idx =
                                                    possibleHex.get(4 * j1 + k2).get(4 * j2 + k3).indexOf(
                                                            values.elementAt(l));
                                            if (idx != -1) {
                                                possibleHex.get(4 * j1 + k2).get(4 * j2 + k3).remove(
                                                        idx);
                                                changed = true;
                                            }
                                        }
                                    }
                                }
                            }
                            values.removeAllElements();
                        }
                    }
                }
            }
        }
        return changed;
    }

    public void generateSubsets(Vector<Integer[]> subsets, int[] set, int[] subset, int subsetSize,
            int nextIndex) {
        if (subsetSize == subset.length) {
            Integer[] temp = new Integer[subset.length];
            for (int i = 0; i < subset.length; i++)
                temp[i] = new Integer(subset[i]);
            subsets.add(temp);
        } else {
            for (int j = nextIndex; j < set.length; j++) {
                subset[subsetSize] = set[j];
                generateSubsets(subsets, set, subset, subsetSize + 1, j + 1);
            }
        }
    }

    public void generateSets(Vector<Integer[]> subsets, int[] set, int[] subset, int subsetSize,
            int nextIndex) {
        if (subsetSize == subset.length) {
            Integer[] temp = new Integer[subset.length];
            for (int i = 0; i < subset.length; i++)
                temp[i] = new Integer(subset[i]);
            subsets.add(temp);
        } else {
            for (int j = nextIndex; j < set.length; j++) {
                subset[subsetSize] = set[j];
                generateSets(subsets, set, subset, subsetSize + 1, j);
            }
        }
    }

    public boolean candidateLineHex() {
        boolean changed = false;
        int idx;
        Vector<Point> pos;
        for (int boxX = 0; boxX < 4; boxX++) {
            for (int boxY = 0; boxY < 4; boxY++) {
                for (int i = 0; i < 16; i++) {
                    pos = new Vector<Point>();
                    for (int sqX = 0; sqX < 4; sqX++) {
                        for (int sqY = 0; sqY < 4; sqY++) {
                            if (boardHex[4 * boxX + sqX][4 * boxY + sqY] == -1
                                    & possibleHex.get(4 * boxX + sqX).get(4 * boxY + sqY).indexOf(i) != -1) {
                                pos.add(new Point(4 * boxX + sqX, 4 * boxY + sqY));
                            }
                        }
                    }
                    if (pos.size() == 2) {
                        if (pos.elementAt(0).x == pos.elementAt(1).x) {
                            for (int j = 0; j < 16; j++) {
                                if ((j / 4) != boxY) {
                                    idx = possibleHex.get(pos.elementAt(0).x).get(j).indexOf(i);
                                    if (idx != -1)
                                        possibleHex.get(pos.elementAt(0).x).get(j).remove(idx);
                                }
                            }
                        } else if (pos.elementAt(0).y == pos.elementAt(1).y) {
                            for (int j = 0; j < 16; j++) {
                                if ((j / 4) != boxX) {
                                    idx = possibleHex.get(j).get(pos.elementAt(0).y).indexOf(i);
                                    if (idx != -1)
                                        possibleHex.get(j).get(pos.elementAt(0).y).remove(idx);
                                }
                            }
                        }
                    }
                }
            }
        }
        return changed;
    }

    public boolean doublePairHex() {
        boolean changed = false, temp;
        int idx;
        Vector<Point> pos;
        Vector<Integer> pos1;
        for (int boxX = 0; boxX < 4; boxX++) {
            for (int boxY = 0; boxY < 4; boxY++) {
                for (int i = 0; i < 16; i++) {
                    pos = new Vector<Point>();
                    for (int sqX = 0; sqX < 4; sqX++) {
                        for (int sqY = 0; sqY < 4; sqY++) {
                            if (boardHex[4 * boxX + sqX][4 * boxY + sqY] == -1
                                    & possibleHex.get(4 * boxX + sqX).get(4 * boxY + sqY).indexOf(i) != -1) {
                                pos.add(new Point(4 * boxX + sqX, 4 * boxY + sqY));
                            }
                        }
                    }
                    if (pos.size() == 2) {
                        if (pos.elementAt(0).x == pos.elementAt(1).x) {
                            pos1 = new Vector<Integer>();
                            for (int j = 0; j < 16; j++) {
                                if ((j / 4) != boxX) {
                                    if (boardHex[j][pos.elementAt(0).y] == -1
                                            & boardHex[j][pos.elementAt(1).y] == -1
                                            & possibleHex.get(j).get(pos.elementAt(0).y).indexOf(i) != -1
                                            & possibleHex.get(j).get(pos.elementAt(1).y).indexOf(i) != -1) {
                                        temp = true;
                                        for (int k = 0; k < 16 & temp; k++) {
                                            if (k != pos.elementAt(0).y & k != pos.elementAt(1).y
                                                    & possibleHex.get(j).get(k).indexOf(i) != -1)
                                                temp = false;
                                        }
                                        if (temp) {
                                            pos1.add(j);
                                        }
                                    }
                                }
                            }
                            if (pos1.size() == 1) {
                                int x1 = pos.elementAt(0).x;
                                int x2 = pos1.elementAt(0);
                                int y1 = pos.elementAt(0).y;
                                int y2 = pos.elementAt(1).y;
                                for (int j = 0; j < 16; j++) {
                                    if (j != x1 & j != x2) {
                                        idx = possibleHex.get(j).get(y1).indexOf(i);
                                        if (idx != -1) {
                                            changed = true;
                                            possibleHex.get(j).get(y1).remove(idx);
                                        }
                                        idx = possibleHex.get(j).get(y2).indexOf(i);
                                        if (idx != -1) {
                                            changed = true;
                                            possibleHex.get(j).get(y2).remove(idx);
                                        }
                                    }
                                }
                            }
                        } else if (pos.elementAt(0).y == pos.elementAt(1).y) {
                            pos1 = new Vector<Integer>();
                            for (int j = 0; j < 16; j++) {
                                if ((j / 4) != boxY) {
                                    if (boardHex[pos.elementAt(0).x][j] == -1
                                            & boardHex[pos.elementAt(1).x][j] == -1
                                            & possibleHex.get(pos.elementAt(0).x).get(j).indexOf(i) != -1
                                            & possibleHex.get(pos.elementAt(1).x).get(j).indexOf(i) != -1) {
                                        temp = true;
                                        for (int k = 0; k < 16 & temp; k++) {
                                            if (k != pos.elementAt(0).x & k != pos.elementAt(1).x
                                                    & possibleHex.get(k).get(j).indexOf(i) != -1)
                                                temp = false;
                                        }
                                        if (temp) {
                                            pos1.add(j);
                                        }
                                    }
                                }
                            }
                            if (pos1.size() == 1) {
                                int x1 = pos.elementAt(0).x;
                                int x2 = pos.elementAt(1).x;
                                int y1 = pos.elementAt(0).y;
                                int y2 = pos1.elementAt(0);
                                for (int j = 0; j < 16; j++) {
                                    if (j != y1 & j != y2) {
                                        idx = possibleHex.get(x1).get(j).indexOf(i);
                                        if (idx != -1) {
                                            changed = true;
                                            possibleHex.get(x1).get(j).remove(idx);
                                        }
                                        idx = possibleHex.get(x2).get(j).indexOf(i);
                                        if (idx != -1) {
                                            changed = true;
                                            possibleHex.get(x2).get(j).remove(idx);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return changed;
    }

    public boolean multipleLinesHex() {
        boolean changed = false, temp;
        int idx, possible;
        Vector<Point> pos;
        Vector<Integer> x, y, goodBox, badBox;
        for (int boxX = 0; boxX < 4; boxX++) {
            for (int boxY = 0; boxY < 4; boxY++) {
                for (int i = 0; i < 16; i++) {
                    pos = new Vector<Point>();
                    for (int sqX = 0; sqX < 4; sqX++) {
                        for (int sqY = 0; sqY < 4; sqY++) {
                            if (boardHex[4 * boxX + sqX][4 * boxY + sqY] == -1
                                    & possibleHex.get(4 * boxX + sqX).get(4 * boxY + sqY).indexOf(i) != -1) {
                                pos.add(new Point(4 * boxX + sqX, 4 * boxY + sqY));
                            }
                        }
                    }
                    if (pos.size() >= 2) {
                        x = new Vector<Integer>();
                        y = new Vector<Integer>();
                        for (int j = 0; j < pos.size(); j++) {
                            if (x.indexOf(pos.elementAt(j).x) == -1)
                                x.add(pos.elementAt(j).x);
                            if (y.indexOf(pos.elementAt(j).y) == -1)
                                y.add(pos.elementAt(j).y);
                        }
                        if (x.size() > 1 & x.size() < 4) {
                            goodBox = new Vector<Integer>();
                            badBox = new Vector<Integer>();
                            for (int j = 0; j < 4; j++) {
                                if (j != boxY) {
                                    temp = true;
                                    possible = 0;
                                    for (int sqX = 0; sqX < 4; sqX++) {
                                        for (int sqY = 0; sqY < 4; sqY++) {
                                            if (boardHex[4 * boxX + sqX][4 * j + sqY] == -1
                                                    & possibleHex.get(4 * boxX + sqX).get(
                                                            4 * j + sqY).indexOf(i) != -1
                                                    & x.indexOf(sqX) != -1) {
                                                possible++;
                                            }
                                            if (boardHex[4 * boxX + sqX][4 * j + sqY] == -1
                                                    & possibleHex.get(4 * boxX + sqX).get(
                                                            4 * j + sqY).indexOf(i) != -1
                                                    & x.indexOf(sqX) == -1) {
                                                temp = false;
                                            }
                                            if (!temp)
                                                break;
                                        }
                                        if (!temp)
                                            break;
                                    }
                                    if (temp & possible >= 2)
                                        goodBox.add(j);
                                    else
                                        badBox.add(j);
                                }
                            }
                            if (goodBox.size() == x.size() - 1 & badBox.size() == 4 - x.size()) {
                                for (int sqX = 0; sqX < 4; sqX++) {
                                    for (int sqY = 0; sqY < 4; sqY++) {
                                        if (x.indexOf(sqX) != -1) {
                                            for (int j = 0; j < badBox.size(); j++) {
                                                idx =
                                                        possibleHex.get(4 * boxX + sqX).get(
                                                                4 * badBox.elementAt(j) + sqY).indexOf(
                                                                i);
                                                if (idx != -1
                                                        & boardHex[4 * boxX + sqX][4
                                                                * badBox.elementAt(j) + sqY] == -1) {
                                                    changed = true;
                                                    possibleHex.get(4 * boxX + sqX).get(
                                                            4 * badBox.elementAt(j) + sqY).remove(
                                                            idx);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (y.size() > 1 & y.size() < 4) {
                            goodBox = new Vector<Integer>();
                            badBox = new Vector<Integer>();
                            for (int j = 0; j < 4; j++) {
                                if (j != boxX) {
                                    temp = true;
                                    possible = 0;
                                    for (int sqX = 0; sqX < 4; sqX++) {
                                        for (int sqY = 0; sqY < 4; sqY++) {
                                            if (boardHex[4 * j + sqX][4 * boxY + sqY] == -1
                                                    & possibleHex.get(4 * j + sqX).get(
                                                            4 * boxY + sqY).indexOf(i) != -1
                                                    & y.indexOf(sqY) != -1) {
                                                possible++;
                                            }
                                            if (boardHex[4 * j + sqX][4 * boxY + sqY] == -1
                                                    & possibleHex.get(4 * j + sqX).get(
                                                            4 * boxY + sqY).indexOf(i) != -1
                                                    & y.indexOf(sqY) == -1) {
                                                temp = false;
                                            }
                                            if (!temp)
                                                break;
                                        }
                                        if (!temp)
                                            break;
                                    }
                                    if (temp & possible >= 2)
                                        goodBox.add(j);
                                    else
                                        badBox.add(j);
                                }
                            }
                            if (goodBox.size() == y.size() - 1 & badBox.size() == 4 - y.size()) {
                                for (int sqX = 0; sqX < 4; sqX++) {
                                    for (int sqY = 0; sqY < 4; sqY++) {
                                        if (y.indexOf(sqY) != -1) {
                                            for (int j = 0; j < badBox.size(); j++) {
                                                idx =
                                                        possibleHex.get(
                                                                4 * badBox.elementAt(j) + sqX).get(
                                                                4 * boxY + sqY).indexOf(i);
                                                if (idx != -1
                                                        & boardHex[4 * badBox.elementAt(j) + sqX][4
                                                                * boxY + sqY] == -1) {
                                                    changed = true;
                                                    possibleHex.get(4 * badBox.elementAt(j) + sqX).get(
                                                            4 * boxY + sqY).remove(idx);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        return changed;
    }

    public void guessOnDiagonalHex() {
        numberOfGuesses++;
        int i = 2, j = 0, guessPointX = -1, guessPointY = -1, guessPossible = 0;
        while (i < 6) {
            if (possibleHex.get(j).get(j).size() == i) {
                guessPointX = j;
                guessPointY = j;
                break;
            }
            if (possibleHex.get(j).get(15 - j).size() == i) {
                guessPointX = j;
                guessPointY = 15 - j;
                break;
            }

            j++;
            if (j == 16) {
                j = 0;
                i++;
            }
        }
        if (guessPointX != -1) {
            tempBoardHex = new int[16][16];
            tempPossibleHex = new ArrayList<List<List<Integer>>>();

            for (i = 0; i < 16; i++) {
                tempPossibleHex.add(new ArrayList<List<Integer>>());
                for (j = 0; j < 16; j++) {
                    tempPossibleHex.get(i).add(new ArrayList<Integer>());
                    tempBoardHex[i][j] = boardHex[i][j];
                    for (int k = 0; k < possibleHex.get(i).get(j).size(); k++)
                        tempPossibleHex.get(i).get(j).add(possibleHex.get(i).get(j).get(k));
                }
            }
            boolean correct = false;
            while (true) {
                boardHex[guessPointX][guessPointY] =
                        possibleHex.get(guessPointX).get(guessPointY).get(guessPossible);
                humanStrategiesHex();
                correct = checkPuzzleHex();
                if (!correct)
                    break;
                else {
                    for (i = 0; i < 16; i++) {
                        for (j = 0; j < 16; j++) {
                            boardHex[i][j] = tempBoardHex[i][j];
                            possibleHex.get(i).get(j).clear();
                            for (int k = 0; k < tempPossibleHex.get(i).get(j).size(); k++)
                                possibleHex.get(i).get(j).add(tempPossibleHex.get(i).get(j).get(k));
                        }
                    }
                    guessPossible++;
                }
            }
            guessOnDiagonalHex();
        }
    }

    public boolean checkPuzzleHex(int x, int y, int c) {
        for (int i = 0; i < 16; i++) {
            if (i != x & boardHex[i][y] == c) {
                return true;
            }
        }
        for (int i = 0; i < 16; i++) {
            if (i != y & boardHex[x][i] == c) {
                return true;
            }
        }
        int xx = 4 * (int) Math.floor(x / 4);
        int yy = 4 * (int) Math.floor(y / 4);
        for (int i = xx; i < xx + 4; i++) {
            for (int j = yy; j < yy + 4; j++) {
                if (i != x & j != y & boardHex[i][j] == c) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkPuzzleHex(int x, int y) {
        if (possibleHex.get(x).get(y).size() > 0) {
            for (int k = 0; k < possibleHex.get(x).get(y).size(); k++) {
                if (checkPuzzleHex(x, y, possibleHex.get(x).get(y).get(k)))
                    return true;
            }
        }
        return false;
    }

    public boolean checkPuzzleHex() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (boardHex[i][j] != -1) {
                    if (checkPuzzleHex(i, j, boardHex[i][j]))
                        return true;
                }
            }
        }
        return false;
    }

    private class Area {
//        final private int SUBTRACTION = 1, DIVISION = 3;
        private int operator, value;
        private List<Point> points;

        public Area() {
            this.operator = -1;
            this.value = -1;
            this.points = new ArrayList<Point>();
        }

        public Area(int operator, List<Point> points, int value) {
            this.operator = operator;
            this.points = new ArrayList<Point>();
            this.value = value;
            for (int i = 0; i < points.size(); i++)
                this.points.add(points.get(i));
//            if ((operator == SUBTRACTION || operator == DIVISION) && points.size() != 2)
//                System.out.println("More than 2 points for sub/div!");
        }

        public void addPoint(Point point) {
            this.points.add(point);
        }

        public List<Point> getList() {
            return this.points;
        }

        public int getOperator() {
            return operator;
        }

        public void setOperator(int operator) {
            this.operator = operator;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public boolean pointUsed(Point point) {
            return points.contains(point);
        }
    }
}

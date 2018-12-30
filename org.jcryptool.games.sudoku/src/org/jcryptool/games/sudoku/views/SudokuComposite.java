// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.sudoku.views;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
//import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
//import org.eclipse.jface.dialogs.IInputValidator;
//import org.eclipse.jface.dialogs.InputDialog;
//import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
//import org.eclipse.swt.graphics.Color;
//import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.games.sudoku.Messages;
import org.jcryptool.games.sudoku.SudokuPlugin;

public class SudokuComposite extends Composite {

//	public Display display;

//	public final int NORMAL = 1, KILLER = 2, HEX = 3;
//	public final int KILLER = 2, HEX = 3;
	public final int HEX = 3;

//	public final int BOX_SIZE_NORMAL = 66, BOX_SIZE_KILLER = 66, BOX_SIZE_HEX = 54;
//	public final int BOX_SIZE_KILLER = 66, BOX_SIZE_HEX = 54;
	public final int BOX_SIZE_HEX = 54;
	
//	final private int ADDITION = 0, SUBTRACTION = 1, MULTIPLICATION = 2, DIVISION = 3;

	public int tabChoice, numberOfGuesses = 0;

//	public int[][] boardNormal, boardKiller, boardHex, guessBoardHex, tempBoard, givenNormal, givenKiller, givenHex;
//	public int[][] boardNormal, boardKiller, boardHex, guessBoardHex, tempBoard, givenKiller, givenHex;
//	public int[][] boardKiller, boardHex, guessBoardHex, tempBoard, givenKiller, givenHex;
//	public int[][] boardHex, guessBoardHex, tempBoard, givenKiller, givenHex;
	public int[][] boardHex, guessBoardHex, tempBoard, givenHex;

//	public Label[][][] boardLabelsNormal, boardLabelsKiller, boardLabelsHex;
//	public Label[][][] boardLabelsKiller, boardLabelsHex;
	public Label[][][] boardLabelsHex;

//	public Text[][] boardTextNormal, boardTextKiller, boardTextHex;
//	public Text[][] boardTextKiller, boardTextHex;
	public Text[][] boardTextHex;

//	public List<List<List<Integer>>> possibleNormal, possibleKiller, possibleHex, guessPossibleHex, tempPossibleKiller,
//			tempPossibleHex;
//	public List<List<List<Integer>>> possibleKiller, possibleHex, guessPossibleHex, tempPossibleKiller, tempPossibleHex;
//	public List<List<List<Integer>>> possibleHex, guessPossibleHex, tempPossibleKiller, tempPossibleHex;
	public List<List<List<Integer>>> possibleHex, guessPossibleHex, tempPossibleHex;

//	public Color WHITE, GREEN, GRAY, RED, BLACK;
//	public Color GREEN, GRAY, RED, BLACK;
//	public Color GRAY, RED, BLACK;
//	public Color RED, BLACK;
//	public Color BLACK;

	public Button solveButton, showPossibleButton, autoFillOneButton, loadButton, saveButton, clearButton,
			boxRuleButton, loadStandardPuzzle;
	public Button onePossibleButton, nakedSingleButton, hiddenSingleButton, blockAndCRButton, nakedSubsetButton,
			candidateLineButton, doublePairButton, multipleLinesButton;
	public Button additionButton, subtractionButton, multiplicationButton, divisionButton, solveModeButton,
			enterModeButton, hintButton, undoButton;

//	Map<Text, UserInputPoint> inputBoxesNormal = new HashMap<Text, UserInputPoint>();

//	Map<Text, UserInputPoint> inputBoxesKiller = new HashMap<Text, UserInputPoint>();

//	Map<Composite, Point> compositeBoxesNormal = new HashMap<Composite, Point>();

//	Map<Composite, Point> compositeBoxesKiller = new HashMap<Composite, Point>();

	Map<Composite, Point> compositeBoxesHex = new HashMap<Composite, Point>();

	Map<Text, UserInputPoint> inputBoxesHex = new HashMap<Text, UserInputPoint>();

	public List<Point> selected;

	public List<Area> areas;

//	public Composite[][] labelCellNormal, labelCellKiller, labelCellHex;
//	public Composite[][] labelCellKiller, labelCellHex;
	public Composite[][] labelCellHex;

//	public Composite playField;

//	public boolean showPossible, autoFillOne, solved, loading, solving, boxRule, killerFirstPossible, loadedKiller,
//			solveMode = false, backgroundSolved = false;
//	public boolean showPossible, autoFillOne, solved, loading, solving, killerFirstPossible, loadedKiller,
//	solveMode = false, backgroundSolved = false;
//	public boolean showPossible, autoFillOne, solved, loading, solving, loadedKiller,
//	solveMode = false, backgroundSolved = false;
	public boolean showPossible, autoFillOne, solved, loading, solving,
	solveMode = false, backgroundSolved = false;

	public Runnable refresh, backgroundSolveComplete, solveComplete;

	public Job backgroundSolve, dummyJob;

//	public Thread blinkerRed = null, blinkerWhite = null, makeWhite;
	public Thread blinkerRed = null, blinkerWhite = null;

	
	public Random rnd;

//	public Vector<Point> movesNormal, movesKiller, movesHex;
//	public Vector<Point> movesKiller, movesHex;
	public Vector<Point> movesHex;

	public SudokuComposite(final Composite parent, final int tabChoice, final int style) {
		super(parent, style);
//		display = getDisplay();
		this.tabChoice = tabChoice;
		initialize();
//		WHITE = ColorService.WHITE;
//		GREEN = ColorService.GREEN;
//		GRAY = ColorService.GRAY;
//		RED = ColorService.RED;
//		BLACK = ColorService.BLACK;
		showPossible = true;
//		boxRule = false;
//		killerFirstPossible = false;
		autoFillOne = false;
		solved = false;
//		loadedKiller = false;
		loading = false;
		rnd = new Random(System.currentTimeMillis());
//		movesNormal = new Vector<Point>();
//		movesKiller = new Vector<Point>();
		movesHex = new Vector<Point>();
//		givenNormal = new int[9][9];
//		givenKiller = new int[9][9];
		givenHex = new int[16][16];
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (i < 9 && j < 9) {
					switch (tabChoice) {
//					case NORMAL:
//						givenNormal[i][j] = 0;
//						break;
//					case KILLER:
//						givenKiller[i][j] = 0;
//						break;
					case HEX:
						givenHex[i][j] = 0;
						break;
					}
				} else {
					if (tabChoice == HEX)
						givenHex[i][j] = 0;
				}
			}
		}

		refresh = new Runnable() {
			@Override
			public void run() {
				switch (tabChoice) {
//				case NORMAL:
//					for (int i = 0; i < 9; i++) {
//						for (int j = 0; j < 9; j++) {
//							labelCellNormal[i][j].layout();
//						}
//					}
//					break;
//				case KILLER:
//					for (int i = 0; i < 9; i++) {
//						for (int j = 0; j < 9; j++) {
//							labelCellKiller[i][j].layout();
//						}
//					}
//					break;
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

		dummyJob = new Job(Messages.SudokuComposite_SolvingPuzzle) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				while (backgroundSolve.getState() == Job.RUNNING) {
					if (monitor.isCanceled())
						return Status.CANCEL_STATUS;
				}
				if (backgroundSolve.getResult() == Status.OK_STATUS) {
					getDisplay().asyncExec(solveComplete);
					return Status.OK_STATUS;
				}
				return null;
			}

		};

		solveComplete = new Runnable() {
			@Override
			public void run() {
				switch (tabChoice) {
//				case NORMAL: {
//					if (solvePuzzleNormal()) {
//						refresh();
//					}
//				}
//					break;
//				case KILLER: {
//					if (solvePuzzleKiller()) {
//						refresh();
//					}
//				}
//					break;
				case HEX: {
					if (solvePuzzleHex()) {
						refresh();
					}
				}
					break;
				}
			}
		};

		backgroundSolveComplete = new Runnable() {
			@Override
			public void run() {
				backgroundSolved = true;
				hintButton.setEnabled(true);
			}

		};

		backgroundSolve = new Job("Solving Puzzle in Background") {
			@Override
			public IStatus run(final IProgressMonitor monitor) {
				switch (tabChoice) {
//				case NORMAL: {
//					tempBoard = new int[9][9];
//					for (int i = 0; i < 9; i++) {
//						for (int j = 0; j < 9; j++) {
//							tempBoard[i][j] = boardNormal[i][j];
//						}
//					}
//					if (solveNormal(tempBoard)) {
//						getDisplay().asyncExec(backgroundSolveComplete);
//					}
//				}
//					break;
//				case KILLER: {
//					tempBoard = new int[9][9];
//					tempPossibleKiller = new ArrayList<List<List<Integer>>>();
//					for (int i = 0; i < 9; i++) {
//						for (int j = 0; j < 9; j++) {
//							tempBoard[i][j] = boardKiller[i][j];
//						}
//					}
//					for (int i = 0; i < possibleKiller.size(); i++) {
//						tempPossibleKiller.add(new ArrayList<List<Integer>>());
//						for (int j = 0; j < possibleKiller.get(i).size(); j++) {
//							tempPossibleKiller.get(i).add(new ArrayList<Integer>());
//							for (int k = 0; k < possibleKiller.get(i).get(j).size(); k++) {
//								tempPossibleKiller.get(i).get(j).add(possibleKiller.get(i).get(j).get(k));
//							}
//						}
//					}
//					if (boxRule)
//						singleOuttie(tempBoard);
//					humanStrategiesKiller(tempBoard, tempPossibleKiller);
//					if (solveKiller(tempBoard, monitor)) {
//						getDisplay().asyncExec(backgroundSolveComplete);
//					} else {
//						return Status.CANCEL_STATUS;
//					}
//				}
//					break;
				case HEX: {
					tempBoard = new int[16][16];
					tempPossibleHex = new ArrayList<List<List<Integer>>>();
					for (int i = 0; i < 16; i++) {
						for (int j = 0; j < 16; j++) {
							tempBoard[i][j] = boardHex[i][j];
						}
					}
					for (int i = 0; i < possibleHex.size(); i++) {
						tempPossibleHex.add(new ArrayList<List<Integer>>());
						for (int j = 0; j < possibleHex.get(i).size(); j++) {
							tempPossibleHex.get(i).add(new ArrayList<Integer>());
							for (int k = 0; k < possibleHex.get(i).get(j).size(); k++) {
								tempPossibleHex.get(i).get(j).add(possibleHex.get(i).get(j).get(k));
							}
						}
					}
					humanStrategiesHex(tempBoard, tempPossibleHex);
					guessOnDiagonalHex(tempBoard, tempPossibleHex);
					if (solveHex(tempBoard, monitor)) {
						getDisplay().asyncExec(backgroundSolveComplete);
					} else {
						return Status.CANCEL_STATUS;
					}
				}
					break;
				}
				return Status.OK_STATUS;
			}

		};
	}

	public void startBlinkingArea(final int x, final int y) {
		blinkerRed = new Thread() {
			@Override
			public void run() {
				switch (tabChoice) {
//				case NORMAL:
//					labelCellNormal[x][y].setBackground(ColorService.RED);
//					break;
//				case KILLER:
//					labelCellKiller[x][y].setBackground(ColorService.RED);
//					break;
				case HEX:
					labelCellHex[x][y].setBackground(ColorService.RED);
					break;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
				}
			}
		};
		blinkerWhite = new Thread() {
			@Override
			public void run() {
				switch (tabChoice) {
//				case NORMAL:
//					labelCellNormal[x][y].setBackground(ColorService.WHITE);
//					break;
//				case KILLER:
//					labelCellKiller[x][y].setBackground(ColorService.WHITE);
//					break;
				case HEX:
					labelCellHex[x][y].setBackground(ColorService.WHITE);
					break;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
				}
			}
		};
		for (int i = 0; i < 3; i++) {
			getDisplay().asyncExec(blinkerRed);
			getDisplay().asyncExec(blinkerWhite);
		}
	}

	private void makeWhite() {
		Thread makeWhite = new Thread() {
			@Override
			public void run() {
				switch (tabChoice) {
//				case NORMAL:
//					for (int i = 0; i < 9; i++) {
//						for (int j = 0; j < 9; j++) {
//							labelCellNormal[i][j].setBackground(ColorService.WHITE);
//							for (int k = 0; k < 8; k++) {
//								boardLabelsNormal[i][j][k].setBackground(ColorService.WHITE);
//							}
//						}
//					}
//					break;
//				case KILLER:
//					for (int i = 0; i < 9; i++) {
//						for (int j = 0; j < 9; j++) {
//							labelCellKiller[i][j].setBackground(ColorService.WHITE);
//							for (int k = 0; k < 8; k++) {
//								boardLabelsKiller[i][j][k].setBackground(ColorService.WHITE);
//							}
//						}
//					}
//					break;
				case HEX:
					for (int i = 0; i < 16; i++) {
						for (int j = 0; j < 16; j++) {
							labelCellHex[i][j].setBackground(ColorService.WHITE);
							for (int k = 0; k < 8; k++) {
								boardLabelsHex[i][j][k].setBackground(ColorService.WHITE);
							}
						}
					}
					break;
				}
			}
		};
		getDisplay().asyncExec(makeWhite);
	}

	public void initialize() {
		setLayout(new GridLayout());
		createHead();
		createMain();
//		if (tabChoice == KILLER) {
//			boxRuleButton.setBackground(ColorService.RED);
//		}
		showPossibleButton.setBackground(ColorService.GREEN);
	}

	public void createHead() {
		final Composite headComposite = new Composite(this, SWT.NONE);
//        headComposite.setBackground(ColorService.WHITE);
		GridData gd_headComposite = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_headComposite.minimumWidth = 300;
		gd_headComposite.widthHint = 300;
		headComposite.setLayoutData(gd_headComposite);
		headComposite.setLayout(new GridLayout());

		final StyledText title = new StyledText(headComposite, SWT.READ_ONLY);
		title.setFont(FontService.getHeaderFont());
		title.setBackground(ColorService.WHITE);

		/** Deals with the choice of scheme */
		switch (tabChoice) {
//		case NORMAL:
//			title.setText(Messages.SudokuComposite_Normal_Title);
//			break;
//		case KILLER:
//			title.setText(Messages.SudokuComposite_Killer_Title);
//			break;
		case HEX:
			title.setText(Messages.SudokuComposite_Hex_Title);
			break;
		}

		final StyledText stDescription = new StyledText(headComposite, SWT.READ_ONLY | SWT.WRAP);
		switch (tabChoice) {
//		case NORMAL:
//			stDescription.setText(Messages.SudokuComposite_Normal_Desc);
//			break;
//		case KILLER:
//			stDescription.setText(Messages.SudokuComposite_Killer_Desc);
//			break;
		case HEX:
			stDescription.setText(Messages.SudokuComposite_Hex_Desc);
			break;
		}
		stDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
	}

	public void createMain() {
		final Group mainGroup = new Group(this, SWT.NONE);
		mainGroup.setLayout(new GridLayout(2, false));
		GridData gd_mainGroup = new GridData(SWT.FILL, SWT.FILL, true, true);
		mainGroup.setLayoutData(gd_mainGroup);
		mainGroup.setText(Messages.SudokuComposite_MainGroup_Title);
		createButtonArea(mainGroup);
		createPlayFieldArea(mainGroup);
		makeWhite();

		getDisplay().asyncExec(refresh);
	}

	private void createButtonArea(final Composite parent) {
		final Composite mainComposite = new Composite(parent, SWT.SHADOW_NONE);
		mainComposite.setLayout(new GridLayout());
		mainComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

		Group grpModeChoice = new Group(mainComposite, SWT.SHADOW_NONE);
		grpModeChoice.setText(Messages.SudokuComposite_ModeAreaTitle);
		grpModeChoice.setLayout(new GridLayout(2, false));
		grpModeChoice.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		solveModeButton = new Button(grpModeChoice, SWT.RADIO);
		solveModeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		solveModeButton.setText(Messages.SudokuComposite_SolveModeButton);
		solveModeButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				solveMode = true;
				solveButton.setEnabled(true);
				hintButton.setEnabled(false);
				showPossibleButton.setEnabled(true);
				autoFillOneButton.setEnabled(true);

				loadStandardPuzzle.setEnabled(false);
				loadButton.setEnabled(false);

//				if (tabChoice == NORMAL)
//					movesNormal.clear();

				for (int i = 0; i < (tabChoice == HEX ? 16 : 9); i++) {
					for (int j = 0; j < (tabChoice == HEX ? 16 : 9); j++) {
						switch (tabChoice) {
//						case NORMAL: {
//							if (boardNormal[i][j] > 0) {
//								boardTextNormal[i][j].setEditable(false);
//								givenNormal[i][j] = 1;
//							}
//						}
//							break;
//						case KILLER: {
//							if (boardKiller[i][j] > 0) {
//								boardTextKiller[i][j].setEditable(false);
//								givenKiller[i][j] = 1;
//							}
//						}
//							break;
						case HEX: {
							if (boardHex[i][j] > -1) {
								boardTextHex[i][j].setEditable(false);
								givenHex[i][j] = 1;
							}
						}
							break;
						}
					}
				}

//				if (tabChoice == KILLER) {
//					movesKiller.clear();
//					additionButton.setEnabled(false);
//					subtractionButton.setEnabled(false);
//					multiplicationButton.setEnabled(false);
//					divisionButton.setEnabled(false);
//					if (selected.size() > 0) {
//						for (int i = 0; i < selected.size(); i++) {
//							labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
//							boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
//						}
//						selected.clear();
//					}
//				}

				if (tabChoice == HEX) {
					movesHex.clear();
					onePossibleButton.setEnabled(true);
					nakedSingleButton.setEnabled(true);
					hiddenSingleButton.setEnabled(true);
					blockAndCRButton.setEnabled(true);
					nakedSubsetButton.setEnabled(true);
					candidateLineButton.setEnabled(true);
					doublePairButton.setEnabled(true);
					multipleLinesButton.setEnabled(true);
				}
				if (backgroundSolve.getState() != Job.RUNNING)
					backgroundSolve.setSystem(true);
				backgroundSolve.schedule();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		enterModeButton = new Button(grpModeChoice, SWT.RADIO);
		enterModeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		enterModeButton.setSelection(true);
		enterModeButton.setText(Messages.SudokuComposite_EnterModeButton);
		enterModeButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				backgroundSolve.cancel();
				backgroundSolved = false;
				hintButton.setEnabled(false);

				showPossibleButton.setEnabled(false);
				autoFillOneButton.setEnabled(false);

				solveMode = false;
				solveButton.setEnabled(false);

				loadStandardPuzzle.setEnabled(true);
				loadButton.setEnabled(true);

				for (int i = 0; i < (tabChoice == HEX ? 16 : 9); i++) {
					for (int j = 0; j < (tabChoice == HEX ? 16 : 9); j++) {
						switch (tabChoice) {
//						case NORMAL: {
//							boardTextNormal[i][j].setEditable(true);
//						}
//							break;
//						case KILLER: {
//							boardTextKiller[i][j].setEditable(true);
//						}
//							break;
						case HEX: {
							boardTextHex[i][j].setEditable(true);
						}
							break;
						}
					}
				}

//				if (tabChoice == KILLER) {
//					additionButton.setEnabled(true);
//					subtractionButton.setEnabled(true);
//					multiplicationButton.setEnabled(true);
//					divisionButton.setEnabled(true);
//				}

				if (tabChoice == HEX) {
					onePossibleButton.setEnabled(false);
					nakedSingleButton.setEnabled(false);
					hiddenSingleButton.setEnabled(false);
					blockAndCRButton.setEnabled(false);
					nakedSubsetButton.setEnabled(false);
					candidateLineButton.setEnabled(false);
					doublePairButton.setEnabled(false);
					multipleLinesButton.setEnabled(false);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		Group grpActionButtons = new Group(mainComposite, SWT.SHADOW_NONE);
		grpActionButtons.setText(Messages.SudokuComposite_ActionsAreaTitle);
		grpActionButtons.setLayout(new GridLayout());
		grpActionButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		solveButton = new Button(grpActionButtons, SWT.PUSH);
		solveButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		solveButton.setEnabled(false);
		solveButton.setText(Messages.SudokuComposite_SolveButton);
		solveButton.setToolTipText(Messages.SudokuComposite_SolveButton_Tooltip);
		solveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				// backgroundSolve.cancel();
				dummyJob.setUser(true);
				dummyJob.schedule();
			}
		});

		hintButton = new Button(grpActionButtons, SWT.PUSH);
		hintButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		hintButton.setEnabled(false);
		hintButton.setText(Messages.SudokuComposite_HintButton);
		hintButton.setToolTipText(Messages.SudokuComposite_HintButton_Tooltip);
		hintButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				switch (tabChoice) {
//				case NORMAL: {
//					if (backgroundSolved && getEmptySquare(boardNormal) != null) {
//						Point square = new Point(rnd.nextInt(9), rnd.nextInt(9));
//						while (boardNormal[square.x][square.y] > 0)
//							square = new Point(rnd.nextInt(9), rnd.nextInt(9));
//						boardNormal[square.x][square.y] = tempBoard[square.x][square.y];
//						for (int k = 0; k < 8; k++)
//							boardLabelsNormal[square.x][square.y][k].setText("");
//						boardTextNormal[square.x][square.y].setText(Integer.toString(boardNormal[square.x][square.y]));
//						startBlinkingArea(square.x, square.y);
//					}
//				}
//					break;
//				case KILLER: {
//					if (backgroundSolved && getEmptySquare(boardKiller) != null) {
//						Point square = new Point(rnd.nextInt(9), rnd.nextInt(9));
//						while (boardKiller[square.x][square.y] > 0)
//							square = new Point(rnd.nextInt(9), rnd.nextInt(9));
//						boardKiller[square.x][square.y] = tempBoard[square.x][square.y];
//						for (int k = 0; k < 8; k++)
//							boardLabelsKiller[square.x][square.y][k].setText("");
//						boardTextKiller[square.x][square.y].setText(Integer.toString(boardKiller[square.x][square.y]));
//						startBlinkingArea(square.x, square.y);
//					}
//				}
//					break;
				case HEX: {
					if (backgroundSolved && getEmptySquare(boardHex) != null) {
						Point square = new Point(rnd.nextInt(16), rnd.nextInt(16));
						while (boardHex[square.x][square.y] > -1)
							square = new Point(rnd.nextInt(9), rnd.nextInt(9));
						boardHex[square.x][square.y] = tempBoard[square.x][square.y];
						for (int k = 0; k < 8; k++)
							boardLabelsHex[square.x][square.y][k].setText("");
						boardTextHex[square.x][square.y].setText(valToTextHex(boardHex[square.x][square.y]));
						startBlinkingArea(square.x, square.y);
					}
				}
					break;
				}
			}
		});

		undoButton = new Button(grpActionButtons, SWT.PUSH);
		undoButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		undoButton.setEnabled(false);
		undoButton.setText(Messages.SudokuComposite_UndoButton);
		undoButton.setToolTipText(Messages.SudokuComposite_UndoButton_Tooltip);
		undoButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				switch (tabChoice) {
//				case NORMAL: {
//					if (movesNormal.size() > 0) {
//						Point pt = movesNormal.get(movesNormal.size() - 1);
//						movesNormal.remove(movesNormal.size() - 1);
//						boardTextNormal[pt.x][pt.y].setText("");
//						updateBoardDataWithUserInputNormal(boardTextNormal[pt.x][pt.y], "");
//						if (movesNormal.size() == 0)
//							undoButton.setEnabled(false);
//					}
//				}
//					break;
//				case KILLER: {
//					if (movesKiller.size() > 0) {
//						Point pt = movesKiller.get(movesKiller.size() - 1);
//						movesKiller.remove(movesKiller.size() - 1);
//						boardTextKiller[pt.x][pt.y].setText("");
//						updateBoardDataWithUserInputKiller(boardTextKiller[pt.x][pt.y], "");
//						if (movesKiller.size() == 0)
//							undoButton.setEnabled(false);
//					}
//				}
//					break;
				case HEX: {
					if (movesHex.size() > 0) {
						Point pt = movesHex.get(movesHex.size() - 1);
						movesHex.remove(movesHex.size() - 1);
						boardTextHex[pt.x][pt.y].setText("");
						updateBoardDataWithUserInputHex(boardTextHex[pt.x][pt.y], "");
						if (movesHex.size() == 0)
							undoButton.setEnabled(false);
					}
				}
					break;
				}
			}
		});

//		if (tabChoice == KILLER) {
//			boxRuleButton = new Button(grpActionButtons, SWT.PUSH);
//			boxRuleButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//			boxRuleButton.setBackground(ColorService.GREEN);
//			boxRuleButton.setEnabled(true);
//			boxRuleButton.setText(Messages.SudokuComposite_BoxRuleButton);
//			boxRuleButton.setToolTipText(Messages.SudokuComposite_BoxRuleButton_Tooltip);
//			boxRuleButton.addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(final SelectionEvent e) {
//					if (boxRule) {
//						boxRule = false;
//						boxRuleButton.setBackground(ColorService.RED);
//					} else {
//						boxRule = true;
//						boxRuleButton.setBackground(ColorService.GREEN);
//					}
//					refresh();
//				}
//			});
//		}

		showPossibleButton = new Button(grpActionButtons, SWT.PUSH);
		showPossibleButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		showPossibleButton.setEnabled(false);
		showPossibleButton.setText(Messages.SudokuComposite_ShowPossibleButton);
		showPossibleButton.setToolTipText(Messages.SudokuComposite_ShowPossibleButton_Tooltip);
		showPossibleButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (showPossible) {
					showPossible = false;
					showPossibleButton.setBackground(ColorService.RED);
				} else {
					showPossible = true;
					showPossibleButton.setBackground(ColorService.GREEN);
				}
				switch (tabChoice) {
//				case NORMAL:
//					updatePossibilitiesNormal();
//					break;
//				case KILLER:
//					updatePossibilitiesKiller(boardKiller, possibleKiller);
//					break;
				case HEX:
					updatePossibilitiesHex(boardHex, possibleHex, true);
					break;
				}
				refresh();
			}
		});

		autoFillOneButton = new Button(grpActionButtons, SWT.PUSH);
		autoFillOneButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		autoFillOneButton.setEnabled(false);
		autoFillOneButton.setText(Messages.SudokuComposite_AutoFillOneButton);
		autoFillOneButton.setToolTipText(Messages.SudokuComposite_AutoFillOneButton_Tooltip);
		autoFillOneButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				switch (tabChoice) {
//				case NORMAL:
//					fillOneNormal();
//					break;
//				case KILLER:
//					fillOneKiller();
//					break;
				case HEX:
					fillOneHex();
					break;
				}
				refresh();
			}
		});

		loadStandardPuzzle = new Button(grpActionButtons, SWT.PUSH);
		loadStandardPuzzle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		loadStandardPuzzle.setText(Messages.SudokuComposite_LoadStandardPuzzle);
		loadStandardPuzzle.setToolTipText(Messages.SudokuComposite_LoadStandardPuzzle_Tooltip);
		loadStandardPuzzle.addSelectionListener(new SelectionAdapter() {
			@Override
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
//				case NORMAL:
//					puzzle = rnd.nextInt(5) + 1;
//					path.append("sudoku" + puzzle + ".sud");
//					// Load normal puzzle. If it fails jump out of the method.
//					if (!loadNormal(path.toString())) {
//						return;
//					}
//					break;
//				case KILLER:
//					// data/killer2.sud is corrupted.
////					puzzle = rnd.nextInt(2) + 1;
////					path.append("killer" + puzzle + ".sud");
//					path.append("killer1.sud");
//					//load killer sudoku. If it fails jump out of the method.
//					if (!loadKiller(path.toString())) {
//						return;
//					}
//					break;
				case HEX:
					puzzle = rnd.nextInt(3) + 1;
					path.append("hex" + puzzle + ".sud");
					// load 16*16 sudoku. If it fails jump out of method.
					if (!loadHex(path.toString())) {
						return;
					}
					break;
				}
				refresh();

				enterModeButton.setSelection(false);
				solveModeButton.setSelection(true);
				solveModeButton.notifyListeners(SWT.Selection, null);
			}
		});

		loadButton = new Button(grpActionButtons, SWT.PUSH);
		loadButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		loadButton.setEnabled(true);
		loadButton.setText(Messages.SudokuComposite_LoadButton);
		loadButton.setToolTipText(Messages.SudokuComposite_LoadButton_Tooltip);
		loadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				switch (tabChoice) {
//				case NORMAL:
//					loadPuzzleNormal();
//					break;
//				case KILLER:
//					loadPuzzleKiller();
//					break;
				case HEX:
					loadPuzzleHex();
					break;
				}
				refresh();
			}
		});

		saveButton = new Button(grpActionButtons, SWT.PUSH);
		saveButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		saveButton.setEnabled(true);
		saveButton.setText(Messages.SudokuComposite_SaveButton);
		saveButton.setToolTipText(Messages.SudokuComposite_SaveButton_Tooltip);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				switch (tabChoice) {
//				case NORMAL:
//					savePuzzleNormal();
//					break;
//				case KILLER:
//					savePuzzleKiller();
//					break;
				case HEX:
					savePuzzleHex();
					break;
				}
			}
		});

		clearButton = new Button(grpActionButtons, SWT.PUSH);
		clearButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		clearButton.setEnabled(true);
		clearButton.setText(Messages.SudokuComposite_ClearButton);
		clearButton.setToolTipText(Messages.SudokuComposite_ClearButton_Tooltip);
		clearButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				enterModeButton.setSelection(true);
				solveModeButton.setSelection(false);
				enterModeButton.notifyListeners(SWT.Selection, null);
				backgroundSolve.cancel();
				loading = true;
				switch (tabChoice) {
//				case NORMAL:
//					clearPuzzleNormal();
//					break;
//				case KILLER:
//					clearPuzzleKiller();
//
//					loadedKiller = false;
//					additionButton.setEnabled(true);
//					subtractionButton.setEnabled(true);
//					multiplicationButton.setEnabled(true);
//					divisionButton.setEnabled(true);
//					break;
				case HEX:
					clearPuzzleHex();
					break;
				}
				loading = false;
				refresh();
			}
		});

		if (tabChoice == HEX) {
			Group grpStrategiesButtons = new Group(mainComposite, SWT.SHADOW_NONE);
			grpStrategiesButtons.setText(Messages.SudokuComposite_StrategiesAreaTitle);
			grpStrategiesButtons.setLayout(new GridLayout());
			grpStrategiesButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			onePossibleButton = new Button(grpStrategiesButtons, SWT.PUSH);
			onePossibleButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			onePossibleButton.setEnabled(false);
			onePossibleButton.setText(Messages.SudokuComposite_OnePossibleButton);
			onePossibleButton.setToolTipText(Messages.SudokuComposite_OnePossibleButton_Tooltip);
			onePossibleButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					onePossibleHex(boardHex, possibleHex, true);
					updateLabelsHex();
				}
			});

			nakedSingleButton = new Button(grpStrategiesButtons, SWT.PUSH);
			nakedSingleButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			nakedSingleButton.setEnabled(false);
			nakedSingleButton.setText(Messages.SudokuComposite_NakedSingleButton);
			nakedSingleButton.setToolTipText(Messages.SudokuComposite_NakedSingleButton_Tooltip);
			nakedSingleButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					nakedSingleHex(boardHex, possibleHex, true);
					updateLabelsHex();
				}
			});

			hiddenSingleButton = new Button(grpStrategiesButtons, SWT.PUSH);
			hiddenSingleButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			hiddenSingleButton.setEnabled(false);
			hiddenSingleButton.setText(Messages.SudokuComposite_HiddenSingleButton);
			hiddenSingleButton.setToolTipText(Messages.SudokuComposite_HiddenSingleButton_Tooltip);
			hiddenSingleButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					hiddenSingleHex(boardHex, possibleHex, true);
					updateLabelsHex();
				}
			});

			blockAndCRButton = new Button(grpStrategiesButtons, SWT.PUSH);
			blockAndCRButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			blockAndCRButton.setEnabled(false);
			blockAndCRButton.setText(Messages.SudokuComposite_BlockAndCRButton);
			blockAndCRButton.setToolTipText(Messages.SudokuComposite_BlockAndCRButton_Tooltip);
			blockAndCRButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					blockAndCRHex(boardHex, possibleHex);
					updateLabelsHex();
				}
			});

			nakedSubsetButton = new Button(grpStrategiesButtons, SWT.PUSH);
			nakedSubsetButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			nakedSubsetButton.setEnabled(false);
			nakedSubsetButton.setText(Messages.SudokuComposite_NakedSubsetButton);
			nakedSubsetButton.setToolTipText(Messages.SudokuComposite_NakedSubsetButton_Tooltip);
			nakedSubsetButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					nakedSubsetHex(boardHex, possibleHex);
					updateLabelsHex();
				}
			});

			candidateLineButton = new Button(grpStrategiesButtons, SWT.PUSH);
			candidateLineButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			candidateLineButton.setEnabled(false);
			candidateLineButton.setText(Messages.SudokuComposite_CandidateLineButton);
			candidateLineButton.setToolTipText(Messages.SudokuComposite_CandidateLineButton_Tooltip);
			candidateLineButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					candidateLineHex(boardHex, possibleHex);
					updateLabelsHex();
				}
			});

			doublePairButton = new Button(grpStrategiesButtons, SWT.PUSH);
			doublePairButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			doublePairButton.setEnabled(false);
			doublePairButton.setText(Messages.SudokuComposite_DoublePairButton);
			doublePairButton.setToolTipText(Messages.SudokuComposite_DoublePairButton_Tooltip);
			doublePairButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					doublePairHex(boardHex, possibleHex);
					updateLabelsHex();
				}
			});

			multipleLinesButton = new Button(grpStrategiesButtons, SWT.PUSH);
			multipleLinesButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			multipleLinesButton.setEnabled(false);
			multipleLinesButton.setText(Messages.SudokuComposite_MultipleLinesButton);
			multipleLinesButton.setToolTipText(Messages.SudokuComposite_MultipleLinesButton_Tooltip);
			multipleLinesButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					multipleLinesHex(boardHex, possibleHex);
					updateLabelsHex();
				}
			});
		}
	}

//		if (tabChoice == KILLER) {
//			Group grpOperatorsButtons = new Group(mainComposite, SWT.SHADOW_NONE);
//			grpOperatorsButtons.setText(Messages.SudokuComposite_OperatorsAreaTitle);
//			grpOperatorsButtons.setLayout(new GridLayout());
//			grpOperatorsButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//
//			additionButton = new Button(grpOperatorsButtons, SWT.PUSH);
//			additionButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//			additionButton.setEnabled(true);
//			additionButton.setText(Messages.SudokuComposite_AdditionButton);
//			additionButton.setToolTipText(Messages.SudokuComposite_AdditionButton_Tooltip);
//			additionButton.addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(final SelectionEvent e) {
//					if (selected.size() > 0) {
//						InputDialog dlg = new InputDialog(getShell(), "Value Input", "Input value",
//								"", (new IInputValidator() {
//									private final Pattern INTEGER_PATTERN = Pattern.compile("[0-9]+");
//
//									@Override
//									public String isValid(String newText) {
//										String toReturn = INTEGER_PATTERN.matcher(newText).matches() ? null
//												: (newText.length() == 0) ? "Please enter an integer."
//														: "'" + newText + "' is not a valid integer.";
//										return toReturn;
//									}
//								}));
//						if (dlg.open() == Window.OK) {
//							int value = Integer.parseInt(dlg.getValue());
//							areas.add(new Area(ADDITION, selected, value));
//						}
//						for (int i = 0; i < selected.size(); i++) {
//							labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
//							boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
//						}
//						selected.clear();
//						updateInitialPossibilitiesKiller();
//						updatePossibilitiesKiller(boardKiller, possibleKiller);
//					}
//				}
//			});
//
//			subtractionButton = new Button(grpOperatorsButtons, SWT.PUSH);
//			subtractionButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//			subtractionButton.setEnabled(true);
//			subtractionButton.setText(Messages.SudokuComposite_SubtractionButton);
//			subtractionButton.setToolTipText(Messages.SudokuComposite_SubtractionButton_Tooltip);
//			subtractionButton.addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(final SelectionEvent e) {
//					if (selected.size() > 0) {
//						if (selected.size() == 2) {
//							InputDialog dlg = new InputDialog(getShell(), "Value Input",
//									"Input value", "", (new IInputValidator() {
//										private final Pattern INTEGER_PATTERN = Pattern.compile("[0-9]+");
//
//										@Override
//										public String isValid(String newText) {
//											String toReturn = INTEGER_PATTERN.matcher(newText).matches() ? null
//													: (newText.length() == 0) ? "Please enter an integer."
//															: "'" + newText + "' is not a valid integer.";
//											return toReturn;
//										}
//									}));
//							if (dlg.open() == Window.OK) {
//								int value = Integer.parseInt(dlg.getValue());
//								areas.add(new Area(SUBTRACTION, selected, value));
//							}
//						}
//						for (int i = 0; i < selected.size(); i++) {
//							labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
//							boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
//						}
//						selected.clear();
//						updateInitialPossibilitiesKiller();
//						updatePossibilitiesKiller(boardKiller, possibleKiller);
//					}
//				}
//			});
//
//			multiplicationButton = new Button(grpOperatorsButtons, SWT.PUSH);
//			multiplicationButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//			multiplicationButton.setEnabled(true);
//			multiplicationButton.setText(Messages.SudokuComposite_MultiplicationButton);
//			multiplicationButton.setToolTipText(Messages.SudokuComposite_MultiplicationButton_Tooltip);
//			multiplicationButton.addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(final SelectionEvent e) {
//					if (selected.size() > 0) {
//						InputDialog dlg = new InputDialog(getShell(), "Value Input", "Input value",
//								"", (new IInputValidator() {
//									private final Pattern INTEGER_PATTERN = Pattern.compile("[0-9]+");
//
//									@Override
//									public String isValid(String newText) {
//										String toReturn = INTEGER_PATTERN.matcher(newText).matches() ? null
//												: (newText.length() == 0) ? "Please enter an integer."
//														: "'" + newText + "' is not a valid integer.";
//										return toReturn;
//									}
//								}));
//						if (dlg.open() == Window.OK) {
//							int value = Integer.parseInt(dlg.getValue());
//							areas.add(new Area(MULTIPLICATION, selected, value));
//						}
//						for (int i = 0; i < selected.size(); i++) {
//							labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
//							boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
//						}
//						selected.clear();
//						updateInitialPossibilitiesKiller();
//						updatePossibilitiesKiller(boardKiller, possibleKiller);
//					}
//				}
//			});
//
//			divisionButton = new Button(grpOperatorsButtons, SWT.PUSH);
//			divisionButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//			divisionButton.setEnabled(true);
//			divisionButton.setText(Messages.SudokuComposite_DivisionButton);
//			divisionButton.setToolTipText(Messages.SudokuComposite_DivisionButton_Tooltip);
//			divisionButton.addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(final SelectionEvent e) {
//					if (selected.size() > 0) {
//						if (selected.size() == 2) {
//							InputDialog dlg = new InputDialog(getShell(), "Value Input",
//									"Input value", "", (new IInputValidator() {
//										private final Pattern INTEGER_PATTERN = Pattern.compile("[0-9]+");
//
//										@Override
//										public String isValid(String newText) {
//											String toReturn = INTEGER_PATTERN.matcher(newText).matches() ? null
//													: (newText.length() == 0) ? "Please enter an integer."
//															: "'" + newText + "' is not a valid integer.";
//											return toReturn;
//										}
//									}));
//							if (dlg.open() == Window.OK) {
//								int value = Integer.parseInt(dlg.getValue());
//								areas.add(new Area(DIVISION, selected, value));
//							}
//						}
//						for (int i = 0; i < selected.size(); i++) {
//							labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
//							boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
//						}
//						selected.clear();
//						updateInitialPossibilitiesKiller();
//						updatePossibilitiesKiller(boardKiller, possibleKiller);
//					}
//				}
//			});
//		}
//	}

	/**
	 * Opens a dialog where the user can select an .sud that should be loaded.
	 */
//	public void loadPuzzleNormal() {
//		String fileName = openFileDialog(SWT.OPEN);
//
//		if (fileName == null) {
//			return;
//		}
//
//		loadNormal(fileName);
//	}

	/**
	 * Loads the values from the given file.
	 * Can load files that look like this: </br>
	 *  020305400\n</br>
	 *	090087000\n</br>
	 *	001000070\n</br>
	 *	002001007\n</br>
	 *	900000002\n</br>
	 *	800600500\n</br>
	 *	040000600\n</br>
	 *	000540090\n</br>
	 *	007209050\n</br>
	 *</br>
	 *<b>Note:</b> This method is pretty slow!
	 * @param fileName The path to the file that should be read.
	 */
//	public boolean loadNormal(String fileName) {
//		solved = false;
//		BufferedReader reader = null;
//		clearPuzzleNormal();
//		loading = true;
//		try {
//			reader = new BufferedReader(new FileReader(fileName));
//			int count = 0;
//			String line;
//			while ((line = reader.readLine()) != null) {
//				for (int i = 0; i < 9; i++) {
//					boardNormal[count][i] = Integer.parseInt(line.substring(i, i + 1));
//					if (boardNormal[count][i] != 0) {
//						boardTextNormal[count][i].setText(line.substring(i, i + 1));
//					}
//				}
//				count++;
//			}
//		} catch (NumberFormatException nfe) {
//			LogUtil.logError(SudokuPlugin.PLUGIN_ID, nfe);
//			MessageBox brokenFile = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
//			brokenFile.setText("Loading puzzle encountered a problem");
//			brokenFile.setMessage("Puzzle could not be loadeed. There is a wrong character in the loaded file.\n");
//			brokenFile.open();
//			return false;
//		} catch (FileNotFoundException e) {
//			printFileNotFoundExceptionDialogLoading(e);
//			return false;
//		} catch (IOException e) {
//			printIOExceptionDialogLoading(e);
//			return false;
//		} finally {
//			try {
//				reader.close();
//			} catch (IOException e) {
//				LogUtil.logError(SudokuPlugin.PLUGIN_ID, e);
//			}
//		}
//		loading = false;
//		updatePossibilitiesNormal();
//		return true;
//	}

	private String openFileDialog(int type) {
		FileDialog dialog = new FileDialog(getDisplay().getActiveShell(), type);
		dialog.setFilterPath(DirectoryService.getUserHomeDir());
		dialog.setFilterExtensions(new String[] { "*.sud" });
		dialog.setFilterNames(new String[] { "Sudoku Files (*.sud)" });
		dialog.setOverwrite(true);
		return dialog.open();
	}

	/**
	 * Saves to the sodoku to a file which looks likes this.</br>
	 * 020305400\n</br>
	 * 090087000\n</br>
	 * 001000070\n</br>
	 * 002001007\n</br>
	 * 900000002\n</br>
	 * 800600500\n</br>
	 * 040000600\n</br>
	 * 000540090\n</br>
	 * 007209050
	 * @return True, if saving was successful. False, if something went wrong.
	 */
//	public boolean savePuzzleNormal() {
//		String fileName = openFileDialog(SWT.SAVE);
//		FileOutputStream out = null;
//
//		if (fileName == null) {
//			return false;
//		}
//
//		try {
//			out = new FileOutputStream(fileName);
//			for (int i = 0; i < 9; i++) {
//				for (int j = 0; j < 9; j++) {
//					out.write(Integer.toString(boardNormal[i][j]).getBytes());
//				}
//				out.write('\n');
//			}
//		} catch (FileNotFoundException e) {
//			printFileNotFoundExceptionDialogSaving(e);
//			return false;
//		} catch (IOException e) {
//			MessageBox ioExceptionDialog = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
//			ioExceptionDialog.setText("Saving puzzle encountered a problem");
//			ioExceptionDialog.setMessage("An IOException occured. See the error log for further information.");
//			ioExceptionDialog.open();
//			LogUtil.logError(SudokuPlugin.PLUGIN_ID, e);
//			return false;
//		} finally {
//			try {
//				if (out != null) {
//					out.flush();
//					out.close();
//				}
//			} catch (IOException ex) {
//				LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
//			}
//		}
//		return true;
//	}

//	public void loadPuzzleKiller() {
//		String fileName = openFileDialog(SWT.OPEN);
//
//		if (fileName == null) {
//			return;
//		}
//
//		loadKiller(fileName);
//	}

	/**
	 * Prints a message box informing the user that an ioexception occurred.
	 * @param ioe Logs the exception in the error log.
	 */
	private void printIOExceptionDialogLoading(IOException ioe) {
		MessageBox fileNotFound = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
		fileNotFound.setText("Loading puzzle encountered a problem");
		fileNotFound.setMessage("An IOException occured. See the error log for more information.");
		fileNotFound.open();
		LogUtil.logError(SudokuPlugin.PLUGIN_ID, ioe);
	}
	
	/**
	 * Loads a killer sudoku from a file.
	 * @param fileName The file which contains the killer sudoku values.
	 * @return True if file is correctly loaded. False if an error occurred and the file is not loaded.
	 */
//	public boolean loadKiller(String fileName) {
//		solved = false;
//		killerFirstPossible = false;
//
//		BufferedReader reader = null;
//		try {
//			reader = new BufferedReader(new FileReader(fileName));
//			loading = true;
//			clearPuzzleKiller();
//			Map<String, Integer> areaName = new HashMap<String, Integer>();
//			String line = null;
//			int ida = 0;
//			String val;
//
//			// Read first nine lines of the file.
//			for (int i = 0; i < 9; i++) {
//				line = reader.readLine();
//				for (int j = 0; j < 9; j++) {
//					val = Character.toString(line.charAt(j));
//					// Checks if the the area of the value is already in the hashmap.
//					// If not it adds it to the hashmap.
//					if (areaName.get(val) == null) {
//						areaName.put(val, ida);
//						ida++;
//					}
//					if (areaName.get(val) > areas.size() - 1) {
//						areas.add(new Area());
//					}
//					areas.get(areaName.get(val)).addPoint(new Point(i, j));
//				}
//			}
//			int area;
//			while ((line = reader.readLine()) != null) {
//				val = Character.toString(line.charAt(0));
//				area = areaName.get(val);
//
//				val = Character.toString(line.charAt(1));
//				if (val.equals("+")) {
//					areas.get(area).setOperator(ADDITION);
//				} else if (val.equals("-")) {
//					areas.get(area).setOperator(SUBTRACTION);
//				} else if (val.equals("*")) {
//					areas.get(area).setOperator(MULTIPLICATION);
//				} else if (val.equals(":")) {
//					areas.get(area).setOperator(DIVISION);
//				} else {
//					MessageBox fileNotFound = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
//					fileNotFound.setText("Invalid Operator in loaded file");
//					fileNotFound.setMessage(
//							"An invalid operator was found in the loaded file. Operator must be \"+\", \"-\", \"*\", \":\".");
//					fileNotFound.open();
//					return false;
//				}
//				areas.get(area).setValue(Integer.parseInt(line.substring(2)));
//			}
//		} catch (FileNotFoundException e) {
//			printFileNotFoundExceptionDialogLoading(e);
//			return false;
//		} catch (IOException e) {
//			printIOExceptionDialogLoading(e);
//			return false;
//		} finally {
//			try {
//				reader.close();
//			} catch (IOException e) {
//				LogUtil.logError(SudokuPlugin.PLUGIN_ID, e);
//				return false;
//			}
//		}
//		loading = false;
//
//		updateInitialPossibilitiesKiller();
//		updatePossibilitiesKiller(boardKiller, possibleKiller);
//
//		loadedKiller = true;
//		additionButton.setEnabled(false);
//		subtractionButton.setEnabled(false);
//		multiplicationButton.setEnabled(false);
//		divisionButton.setEnabled(false);
//		return true;
//	}
	
	/**
	 * Shows a dialog that informs the user that the puzzle could not be saved, 
	 * due to a missing or invalid file path.
	 * Error will be logged in the error log.
	 */
	private void printFileNotFoundExceptionDialogSaving(FileNotFoundException fnfe) {
		// Print a message that puzzle is not saved.
		MessageBox emptyFileDialog = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
		emptyFileDialog.setText("Saving puzzle encountered a problem");
		emptyFileDialog.setMessage("Puzzle could not be saved. Invalid file path.");
		emptyFileDialog.open();
		// Log the error in the error log.
		LogUtil.logError(SudokuPlugin.PLUGIN_ID, "File Name for puzzle is empty.", fnfe, false);
	}

	/**
	 * Saves the current killer sudoku.
	 * 
	 * @return True, if saving succeeded. False if something went wrong.
	 */
//	public boolean savePuzzleKiller() {
//		String fileName = openFileDialog(SWT.SAVE);
//		FileOutputStream out = null;
//
//		if (fileName == null) {
//			return false;
//		}
//
//		Map<Integer, String> operatorMap = new HashMap<Integer, String>();
//		operatorMap.put(ADDITION, "+");
//		operatorMap.put(SUBTRACTION, "-");
//		operatorMap.put(MULTIPLICATION, "*");
//		operatorMap.put(DIVISION, ":");
//		Map<Integer, String> areaName = new HashMap<Integer, String>();
//		for (int i = 0; i < areas.size(); i++) {
//			areaName.put(i, Character.toString((char) (i + 58)));
//		}
//		
//		try {
//			out = new FileOutputStream(fileName);
//			for (int i = 0; i < 9; i++) {
//				for (int j = 0; j < 9; j++) {
//					for (int k = 0; k < areas.size(); k++) {
//						if (areas.get(k).pointUsed(new Point(i, j))) {
//							out.write((areaName.get(k)).getBytes());
//							break;
//						}
//					}
//				}
//				out.write('\n');
//			}
//			for (int i = 0; i < areas.size(); i++) {
//				out.write((areaName.get(i) + operatorMap.get(areas.get(i).getOperator())
//						+ Integer.toString(areas.get(i).getValue()) + "\n").getBytes());
//			}
//		} catch (FileNotFoundException e) {
//			printFileNotFoundExceptionDialogSaving(e);
//			return false;
//		} catch (IOException e) {
//			printIOExceptionDialogSaving(e);
//			return false;
//		} finally {
//			try {
//				out.flush();
//				out.close();
//			} catch (IOException e) {
//				LogUtil.logError(SudokuPlugin.PLUGIN_ID, e);
//			}
//		}
//		return true;
//	}
	
	/**
	 * Prints a dialog informing the user that the puzzle he wished to load could not be loaded.
	 */
	private void printFileNotFoundExceptionDialogLoading(FileNotFoundException fnfe) {
		// Print a message that puzzle is not loaded.
		MessageBox emptyFileDialog = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
		emptyFileDialog.setText("Loading puzzle encountered a problem");
		emptyFileDialog.setMessage("Puzzle could not be loaded. Invalid file path.");
		emptyFileDialog.open();
		// Log the error in the error log.
		LogUtil.logError(SudokuPlugin.PLUGIN_ID, "File Name for puzzle is empty.", fnfe, false);
	}

	public void loadPuzzleHex() {
		String fileName = openFileDialog(SWT.OPEN);

		if (fileName == null) {
			return;
		}

		loadHex(fileName);
	}

	/**
	 * Loads a 16*16 sudoku from a file.
	 * @param fileName Path to the file that should be read.
	 * @return True, if everything worked properly. False, if something went wrong.
	 */
	public boolean loadHex(String fileName) {
		solved = false;

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));

			loading = true;
			clearPuzzleHex();
			for (int i = 0; i < 16; i++) {
				String line = reader.readLine();
				for (int j = 0; j < 16; j++) {
					String val = Character.toString(line.charAt(j));
					if (!val.equals("0")) {
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
		} catch (FileNotFoundException e) {
			printFileNotFoundExceptionDialogLoading(e);
			return false;
		} catch (IOException e) {
			printIOExceptionDialogLoading(e);
			return false;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				LogUtil.logError(SudokuPlugin.PLUGIN_ID, e);
				return false;
			}
		}
		loading = false;
		updatePossibilitiesHex(boardHex, possibleHex, true);
		return true;
	}

	/**
	 * Saves the current puzzle to a .sud file. The file will look like this:<br>
	 * 470F59C00A000000\n<br>
	 * 00E00000000C0976\n<br>
	 * B600000000D000A0\n<br>
	 * 0000001058064E00\n<br>
	 * 0000F000000B048A\n<br>
	 * 3C01000000E0D002\n<br>
	 * 800A20E00D000300\n<br>
	 * 00070C300092E0F0\n<br>
	 * 09064D0000000010\n<br>
	 * 00D060000000904C\n<br>
	 * 1005000000370000\n<br>
	 * 0F001A00020080BE\n<br>
	 * 050C000000A00000\n<br>
	 * 00000200B0000005\n<br>
	 * 0000C600D014FBE8\n<br>
	 * 001D00006928CA00\n<br>
	 * 
	 */
	public boolean savePuzzleHex() {
		String fileName = openFileDialog(SWT.SAVE);
		FileOutputStream out = null;

		if (fileName == null) {
			return false;
		}
		Map<Integer, String> hexMap = new HashMap<Integer, String>();
		hexMap.put(-1, "0");
		// The mapping of 0 is useless because it should not occur.
		// Didn't removed it, because i only viewed the code superficially.
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
		try {
			out = new FileOutputStream(fileName);

			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					out.write(hexMap.get(boardHex[j][i]).getBytes());
				}
				out.write('\n');
			}
		} catch (FileNotFoundException e) {
			printFileNotFoundExceptionDialogSaving(e);
			return false;
		} catch (IOException e) {
			printIOExceptionDialogSaving(e);
			return false;
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException ex) {
				LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Prints a message box to inform the user that an ioexception occured.
	 * @param ioeLogs the given exception in the error log.
	 */
	private void printIOExceptionDialogSaving(IOException ioe) {
		MessageBox emptyFileDialog = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
		emptyFileDialog.setText("Saving puzzle encountered a problem");
		emptyFileDialog.setMessage("Puzzle could not be saved. An IOException occured.\n"
				+ "See the error log for further information.");
		emptyFileDialog.open();
		LogUtil.logError(SudokuPlugin.PLUGIN_ID, "Puzzle could not be saved. An IOException occured.", ioe, false);
	}

	/**
	 * Removes all entries from the current sudoku
	 */
//	public void clearPuzzleNormal() {
//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				boardNormal[i][j] = 0;
//				boardTextNormal[i][j].setText("");
//				for (int k = 0; k < 8; k++)
//					boardLabelsNormal[i][j][k].setText("");
//				possibleNormal.get(i).get(j).clear();
//				for (int k = 1; k <= 9; k++)
//					possibleNormal.get(i).get(j).add(k);
//			}
//		}
//	}

//	public void clearPuzzleKiller() {
//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				boardKiller[i][j] = 0;
//				boardTextKiller[i][j].setText("");
//				for (int k = 0; k < 8; k++)
//					boardLabelsKiller[i][j][k].setText("");
//				possibleKiller.get(i).get(j).clear();
//				for (int k = 1; k <= 9; k++)
//					possibleKiller.get(i).get(j).add(k);
//			}
//		}
//		areas.clear();
//		for (int i = 0; i < selected.size(); i++) {
//			labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
//			boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
//		}
//		selected.clear();
//	}

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
						boardLabelsHex[i][j][k].setText(valToTextHex(possibleHex.get(i).get(j).get(k)));
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
		GridData gd_playField = new GridData(SWT.FILL, SWT.FILL, true, true); // 25.09. changed from left, top, true,
																				// true
		playField.setLayoutData(gd_playField);
		playField.addControlListener(new ControlListener() {
			@Override
			public void controlMoved(ControlEvent e) {
			}

			@Override
			public void controlResized(ControlEvent e) {
				// Ensures that the playField is always quadratic
				Rectangle controlSize = playField.getBounds();
				if (controlSize.height < controlSize.width) {
					playField.setSize(controlSize.height, controlSize.height);
				} else {
					playField.setSize(controlSize.width, controlSize.width);
				}
			}

		});
		switch (tabChoice) {
//		case NORMAL: {
//			boardNormal = new int[9][9];
//			labelCellNormal = new Composite[9][9];
//			boardLabelsNormal = new Label[9][9][8];
//			boardTextNormal = new Text[9][9];
//			possibleNormal = new ArrayList<List<List<Integer>>>();
//			for (int i = 0; i < 9; i++) {
//				possibleNormal.add(new ArrayList<List<Integer>>());
//				for (int j = 0; j < 9; j++) {
//					boardNormal[i][j] = 0;
//					possibleNormal.get(i).add(new ArrayList<Integer>());
//					for (int k = 1; k <= 9; k++) {
//						possibleNormal.get(i).get(j).add(k);
//					}
//				}
//			}
//			createFieldNormal(playField);
//		}
//			break;
//		case KILLER: {
//			boardKiller = new int[9][9];
//			labelCellKiller = new Composite[9][9];
//			boardLabelsKiller = new Label[9][9][8];
//			boardTextKiller = new Text[9][9];
//			possibleKiller = new ArrayList<List<List<Integer>>>();
//			selected = new ArrayList<Point>();
//			areas = new ArrayList<Area>();
//			for (int i = 0; i < 9; i++) {
//				possibleKiller.add(new ArrayList<List<Integer>>());
//				for (int j = 0; j < 9; j++) {
//					boardKiller[i][j] = 0;
//					possibleKiller.get(i).add(new ArrayList<Integer>());
//					for (int k = 1; k <= 9; k++) {
//						possibleKiller.get(i).get(j).add(k);
//					}
//				}
//			}
//			createFieldKiller(playField);
//		}
//			break;
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

//	public void createFieldNormal(final Composite parent) {
//		Composite playField = new Composite(parent, SWT.NONE);
//		GridData gd_playField = new GridData(SWT.FILL, SWT.FILL, true, true);
//		gd_playField.widthHint = gd_playField.heightHint = 600;
//		playField.setLayoutData(gd_playField);
//		GridLayout layout = new GridLayout(9, false);
//		layout.verticalSpacing = layout.horizontalSpacing = 0;
//		playField.setLayout(layout);
//
//		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//		Map<Composite, Point> compositeBoxesNormal = new HashMap<Composite, Point>();
//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				labelCellNormal[i][j] = new Composite(playField, SWT.NONE);
//
//				compositeBoxesNormal.put(labelCellNormal[i][j], new Point(i, j));
//				labelCellNormal[i][j].addListener(SWT.MouseDown, new Listener() {
//
//					@Override
//					public void handleEvent(Event event) {
//						Composite composite = (Composite) event.widget;
//						Point point = compositeBoxesNormal.get(composite);
//						boardTextNormal[point.x][point.y].setFocus();
//					}
//
//				});
//				labelCellNormal[i][j].setLayoutData(gridData);
//				final int f_i = i, f_j = j; // Final variables allow access in listener class
//				labelCellNormal[i][j].addPaintListener(new PaintListener() {
//					@Override
//					public void paintControl(PaintEvent e) {
//						Rectangle a = ((Composite) e.getSource()).getClientArea();
//
//						if (f_i != 8 && f_j != 8) { // draws rectangles
//							e.gc.drawRectangle(0, 0, a.width, a.height);
//						} else if (f_i != 8) {
//							e.gc.drawRectangle(0, 0, a.width - 1, a.height);
//						} else if (f_j != 8) {
//							e.gc.drawRectangle(0, 0, a.width, a.height - 1);
//						} else {
//							e.gc.drawRectangle(0, 0, a.width - 1, a.height - 1);
//						}
//
//						if ((f_j + 1) % 3 == 0 && (f_j + 1) != 9) { // draws bold lines
//							e.gc.drawLine(a.width - 1, a.height - 1, a.width - 1, 0);
//						}
//						if ((f_i + 1) % 3 == 0 && (f_i + 1) != 9) {
//							e.gc.drawLine(a.width - 1, a.height - 1, 0, a.height - 1);
//						}
//					}
//				});
//
//				labelCellNormal[i][j].setBackground(ColorService.WHITE);
//				GridLayout cellNormalLayout = new GridLayout(3, true);
//				cellNormalLayout.verticalSpacing = 0;
//				cellNormalLayout.horizontalSpacing = 0;
//				labelCellNormal[i][j].setLayout(cellNormalLayout);
//				for (int k = 0; k < 4; k++) {
//					boardLabelsNormal[i][j][k] = createLabelNormal(labelCellNormal[i][j]);
//				}
//				boardTextNormal[i][j] = createTextNormal(labelCellNormal[i][j]);
//				inputBoxesNormal.put(boardTextNormal[i][j], new UserInputPoint(i, j));
//				for (int k = 4; k < 8; k++) {
//					boardLabelsNormal[i][j][k] = createLabelNormal(labelCellNormal[i][j]);
//				}
//				if (boardNormal[i][j] != 0)
//					boardTextNormal[i][j].setText(Integer.toString(boardNormal[i][j]));
//				else {
//					if (possibleNormal.get(i).get(j).size() < 9) {
//						for (int k = 0; k < possibleNormal.get(i).get(j).size(); k++) {
//							boardLabelsNormal[i][j][k].setText(Integer.toString(possibleNormal.get(i).get(j).get(k)));
//						}
//					}
//				}
//			}
//		}
//	}

//	public void createFieldKiller(final Composite parent) {
//		Composite playField = new Composite(parent, SWT.NONE);
//		GridData gd_playField = new GridData(SWT.FILL, SWT.FILL, true, true);
//		gd_playField.widthHint = gd_playField.heightHint = 600;
//		playField.setLayoutData(gd_playField);
//		GridLayout layout = new GridLayout(9, false);
//		layout.verticalSpacing = layout.horizontalSpacing = 0;
//		playField.setLayout(layout);
//
//		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//
//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				labelCellKiller[i][j] = new Composite(playField, SWT.NONE);
//				compositeBoxesKiller.put(labelCellKiller[i][j], new Point(i, j));
//				labelCellKiller[i][j].setLayoutData(gridData);
//				labelCellKiller[i][j].setBackground(ColorService.WHITE);
//				labelCellKiller[i][j].addListener(SWT.MouseDown, new Listener() {
//
//					@Override
//					public void handleEvent(Event event) {
//						Composite composite = (Composite) event.widget;
//						Point point = compositeBoxesKiller.get(composite);
//						if (!solveMode) {
//							if (!loadedKiller) {
//								if (selected.contains(point)) {
//									composite.setBackground(ColorService.WHITE);
//									boardTextKiller[point.x][point.y].setBackground(ColorService.WHITE);
//									selected.remove(point);
//
//								} else {
//									if (adjacent(point)) {
//										composite.setBackground(ColorService.RED);
//										boardTextKiller[point.x][point.y].setBackground(ColorService.RED);
//										selected.add(point);
//									}
//								}
//							} else {
//								boardTextKiller[point.x][point.y].setFocus();
//							}
//						} else {
//							boardTextKiller[point.x][point.y].setFocus();
//						}
//					}
//
//				});
//
//				final int f_i = i, f_j = j; // Final variables allow access in listener class
//				labelCellKiller[i][j].addPaintListener(new PaintListener() {
//					@Override
//					public void paintControl(PaintEvent e) {
//						Rectangle a = ((Composite) e.getSource()).getClientArea();
//						if (f_i + 1 != 9 && f_j + 1 != 9) {
//							e.gc.drawRectangle(0, 0, a.width, a.height);
//						} else if (f_i + 1 != 9) {
//							e.gc.drawRectangle(0, 0, a.width - 1, a.height);
//						} else if (f_j + 1 != 9) {
//							e.gc.drawRectangle(0, 0, a.width, a.height - 1);
//						} else {
//							e.gc.drawRectangle(0, 0, a.width - 1, a.height - 1);
//						}
//
//						if ((f_j + 1) % 3 == 0 && (f_j + 1) != 9 && boxRule) {
//							e.gc.drawLine(a.width - 1, a.height - 1, a.width - 1, 0);
//						}
//						if ((f_i + 1) % 3 == 0 && (f_i + 1) != 9 && boxRule) {
//							e.gc.drawLine(a.width - 1, a.height - 1, 0, a.height - 1);
//						}
//					}
//				});
//
//				labelCellKiller[i][j].addPaintListener(new PaintListener() {
//					@Override
//					public void paintControl(PaintEvent e) {
//						Font tempFont = e.gc.getFont();
//						e.gc.setFont(FontService.getTinyFont());
//						e.gc.setForeground(ColorService.RED);
//						Point point = compositeBoxesKiller.get(e.widget);
//						Rectangle a = ((Composite) e.getSource()).getClientArea();
//						if (leftLine(point))
//							e.gc.drawLine(2, 2, 2, a.height - 3);
//						if (topLine(point))
//							e.gc.drawLine(2, 2, a.width - 3, 2);
//						if (topLabel(point))
//							e.gc.drawString(topLabelValue(point), 1, 1);
//						if (bottomLine(point))
//							e.gc.drawLine(2, a.height - 3, a.width - 3, a.height - 3);
//						if (rightLine(point))
//							e.gc.drawLine(a.width - 3, 2, a.width - 3, a.height - 3);
//						e.gc.setFont(tempFont);
//						e.gc.setForeground(ColorService.BLACK);
//					}
//				});
//				GridLayout gridlayout = new GridLayout(3, true);
//				gridlayout.verticalSpacing = 0;
//				gridlayout.horizontalSpacing = 0;
//				labelCellKiller[i][j].setLayout(gridlayout);
//				for (int k = 0; k < 4; k++) {
//					boardLabelsKiller[i][j][k] = createLabelKiller(labelCellKiller[i][j]);
//
//				}
//				boardTextKiller[i][j] = createTextKiller(labelCellKiller[i][j]);
//				inputBoxesKiller.put(boardTextKiller[i][j], new UserInputPoint(i, j));
//				for (int k = 4; k < 8; k++) {
//					boardLabelsKiller[i][j][k] = createLabelKiller(labelCellKiller[i][j]);
//				}
//				if (boardKiller[i][j] != 0)
//					boardTextKiller[i][j].setText(Integer.toString(boardKiller[i][j]));
//				else {
//					if (possibleKiller.get(i).get(j).size() < 8) {
//						for (int k = 0; k < possibleKiller.get(i).get(j).size(); k++) {
//							boardLabelsKiller[i][j][k + 1]
//									.setText(Integer.toString(possibleKiller.get(i).get(j).get(k)));
//							boardLabelsKiller[i][j][k + 1].setBackground(ColorService.WHITE);
//						}
//					}
//				}
//			}
//		}
//	}

	public void createFieldHex(final Composite parent) {
		Composite playField = new Composite(parent, SWT.NONE);
		GridData gd_playField = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_playField.widthHint = gd_playField.heightHint = 870;
		playField.setLayoutData(gd_playField);
		GridLayout layout = new GridLayout(16, false);
		layout.verticalSpacing = layout.horizontalSpacing = 0;
		playField.setLayout(layout);

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				labelCellHex[i][j] = new Composite(playField, SWT.NONE);

				compositeBoxesHex.put(labelCellHex[i][j], new Point(i, j));

				labelCellHex[i][j].setLayoutData(gridData);
				labelCellHex[i][j].addListener(SWT.MouseDown, new Listener() {
					@Override
					public void handleEvent(Event event) {
						Composite composite = (Composite) event.widget;
						Point point = compositeBoxesHex.get(composite);
						boardTextHex[point.x][point.y].setFocus();
					}

				});
				final int f_i = i, f_j = j; // Final variables allow access in listener class
				labelCellHex[i][j].addPaintListener(new PaintListener() {
					@Override
					public void paintControl(PaintEvent e) {
						Rectangle a = ((Composite) e.getSource()).getClientArea();
						if (f_i + 1 != 16 && f_j + 1 != 16) {
							e.gc.drawRectangle(0, 0, a.width, a.height);
						} else if (f_i + 1 != 16) {
							e.gc.drawRectangle(0, 0, a.width - 1, a.height);
						} else if (f_j + 1 != 16) {
							e.gc.drawRectangle(0, 0, a.width, a.height - 1);
						} else {
							e.gc.drawRectangle(0, 0, a.width - 1, a.height - 1);
						}

						if ((f_j + 1) % 4 == 0 && (f_j + 1) != 16) {
							e.gc.drawLine(a.width - 1, a.height - 1, a.width - 1, 0);
						}
						if ((f_i + 1) % 4 == 0 && (f_i + 1) != 16) {
							e.gc.drawLine(a.width - 1, a.height - 1, 0, a.height - 1);
						}
					}
				});

				labelCellHex[i][j].setBackground(ColorService.WHITE);
				GridLayout gridlayout = new GridLayout(3, true);
				gridlayout.verticalSpacing = 0;
				gridlayout.horizontalSpacing = 0;
				labelCellHex[i][j].setLayout(gridlayout);
				for (int k = 0; k < 4; k++) {
					boardLabelsHex[i][j][k] = createLabelHex(labelCellHex[i][j], k);

				}
				boardTextHex[i][j] = createTextHex(labelCellHex[i][j]);
				inputBoxesHex.put(boardTextHex[i][j], new UserInputPoint(i, j));
				for (int k = 4; k < 8; k++) {
					boardLabelsHex[i][j][k] = createLabelHex(labelCellHex[i][j], k);
				}
				if (boardHex[i][j] != -1)
					boardTextHex[i][j].setText(Integer.toString(boardHex[i][j]));
				else {
					if (possibleHex.get(i).get(j).size() < 9) {
						for (int k = 0; k < possibleHex.get(i).get(j).size(); k++) {
							boardLabelsHex[i][j][k].setText(Integer.toString(possibleHex.get(i).get(j).get(k)));
						}
					}
				}
			}
		}
	}

//	private boolean adjacent(Point point) {
//		if (selected.size() == 0)
//			return true;
//		for (int i = 0; i < selected.size(); i++) {
//			if (Math.abs(selected.get(i).x - point.x) + Math.abs(selected.get(i).y - point.y) <= 1)
//				return true;
//		}
//		return false;
//	}

//	private boolean leftLine(Point point) {
//		boolean test = false;
//		Area area = null;
//		for (int i = 0; i < areas.size(); i++) {
//			if (areas.get(i).pointUsed(point)) {
//				area = areas.get(i);
//				test = true;
//				break;
//			}
//		}
//		if (test) {
//			if (point.y != 0) {
//				Point leftPoint = new Point(point.x, point.y - 1);
//				return !(area.pointUsed(leftPoint));
//			} else {
//				return true;
//			}
//		}
//		return false;
//	}

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
			 * if (point.x == 0 && point.y == 0) return false; else if (point.x == 0) {
			 * Point leftPoint = new Point(point.x, point.y-1); return
			 * (area.pointUsed(leftPoint)); } else { Point topPoint = new Point(point.x-1,
			 * point.y); return !(area.pointUsed(topPoint)); }
			 */
		}
		return false;
	}

//	private boolean topLabel(Point point) {
//		boolean test = false;
//		Area area = null;
//		for (int i = 0; i < areas.size(); i++) {
//			if (areas.get(i).pointUsed(point)) {
//				area = areas.get(i);
//				test = true;
//				break;
//			}
//		}
//		if (test) {
//			if (point.x == 0 && point.y == 0)
//				return true;
//			else if (point.x == 0) {
//				Point leftPoint = new Point(point.x, point.y - 1);
//				return !(area.pointUsed(leftPoint));
//			} else {
//				Point topPoint = new Point(point.x - 1, point.y);
//				Point leftPoint = new Point(point.x, point.y - 1);
//				return (!area.pointUsed(topPoint)) && (!area.pointUsed(leftPoint));
//			}
//		}
//		return false;
//	}

//	public String topLabelValue(Point point) {
//		boolean test = false;
//		Area area = null;
//		for (int i = 0; i < areas.size(); i++) {
//			if (areas.get(i).pointUsed(point)) {
//				area = areas.get(i);
//				test = true;
//				break;
//			}
//		}
//		if (test) {
//			switch (area.getOperator()) {
//			case ADDITION:
//				return Integer.toString(area.getValue()) + "+";
//			case SUBTRACTION:
//				return Integer.toString(area.getValue()) + "-";
//			case MULTIPLICATION:
//				return Integer.toString(area.getValue()) + "x";
//			case DIVISION:
//				return Integer.toString(area.getValue()) + ":";
//			}
//		}
//		return null;
//	}

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

	public Label createLabelNormal(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setAlignment(SWT.CENTER);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		label.setFont(FontService.getTinyFont());
		return label;
	}

//	private Label createLabelKiller(Composite parent) {
//		final Label label = new Label(parent, SWT.NONE);
//		label.setAlignment(SWT.CENTER);
//		label.setBackground(ColorService.WHITE);
//		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
//		label.setFont(FontService.getTinyFont());
//		label.setForeground(ColorService.RED);
//		return label;
//	}

	public Label createLabelHex(Composite parent, int k) {
		Label label = new Label(parent, SWT.NONE);
		label.setAlignment(SWT.CENTER);
		int horizontal = SWT.CENTER, vertical = SWT.CENTER;
		switch (k) {
		case 0: {
			horizontal = SWT.LEFT;
			vertical = SWT.TOP;
		}
			break;
		case 1: {
			horizontal = SWT.CENTER;
			vertical = SWT.TOP;
		}
			break;
		case 2: {
			horizontal = SWT.RIGHT;
			vertical = SWT.TOP;
		}
			break;
		case 3: {
			horizontal = SWT.LEFT;
			vertical = SWT.CENTER;
		}
			break;
		case 4: {
			horizontal = SWT.RIGHT;
			vertical = SWT.CENTER;
		}
			break;
		case 5: {
			horizontal = SWT.LEFT;
			vertical = SWT.BOTTOM;
		}
			break;
		case 6: {
			horizontal = SWT.CENTER;
			vertical = SWT.BOTTOM;
		}
			break;
		case 7: {
			horizontal = SWT.RIGHT;
			vertical = SWT.BOTTOM;
		}
			break;
		}
		label.setLayoutData(new GridData(horizontal, vertical, true, true));
		label.setSize(BOX_SIZE_HEX / 3, BOX_SIZE_HEX / 3);
		label.setFont(FontService.getTinyFont());
		return label;
	}

//	public Text createTextNormal(Composite parent) {
//		Text input = new Text(parent, SWT.CENTER);
//		input.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
//		input.setTextLimit(1);
//		input.setFont(FontService.getSmallFont());
//
//		input.addListener(SWT.Verify, new Listener() {
//			@Override
//			public void handleEvent(Event e) {
//				String input = e.text;
//				Text textbox = (Text) e.widget;
//				if (input.length() == 0 && !loading && !solving)
//					updateBoardDataWithUserInputNormal(textbox, input);
//				if (!solved && !loading && !solving) {
//					char[] chars = new char[input.length()];
//					input.getChars(0, chars.length, chars, 0);
//					UserInputPoint point = inputBoxesNormal.get(textbox);
//					for (int i = 0; i < chars.length; i++) {
//						if (!('1' <= chars[i] && chars[i] <= '9')
//								|| possibleNormal.get(point.x).get(point.y).indexOf(Integer.parseInt(input)) == -1
//								|| createsZeroPossible(new Point(point.x, point.y), Integer.parseInt(input))) {
//							e.doit = false;
//							return;
//						}
//					}
//					updateBoardDataWithUserInputNormal(textbox, input);
//				}
//			}
//		});
//		return input;
//	}

//	public Text createTextKiller(Composite parent) {
//		Text input = new Text(parent, SWT.CENTER);
//		input.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
//		input.setTextLimit(1);
//		input.setFont(FontService.getSmallFont());
//
//		input.addListener(SWT.Verify, new Listener() {
//			@Override
//			public void handleEvent(Event e) {
//				String input = e.text;
//				Text textbox = (Text) e.widget;
//				// textbox.setForeground(ColorService.GREEN);
//				if (input.length() == 0 && !loading && !solving)
//					updateBoardDataWithUserInputKiller(textbox, input);
//				if (!solved && !loading && !solving) {
//					char[] chars = new char[input.length()];
//					input.getChars(0, chars.length, chars, 0);
//					UserInputPoint point = inputBoxesKiller.get(textbox);
//					for (int i = 0; i < chars.length; i++) {
//						if (!('1' <= chars[i] && chars[i] <= '9')
//								|| possibleKiller.get(point.x).get(point.y).indexOf(Integer.parseInt(input)) == -1
//								|| createsZeroPossible(new Point(point.x, point.y), Integer.parseInt(input))) {
//							e.doit = false;
//							return;
//						}
//					}
//					updateBoardDataWithUserInputKiller(textbox, input);
//				}
//			}
//		});
//		return input;
//	}

	public Text createTextHex(Composite parent) {
		Text input = new Text(parent, SWT.CENTER);
		input.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		// input.setForeground(ColorService.GREEN);
		input.setTextLimit(1);
		input.setFont(FontService.getSmallFont());

		input.addListener(SWT.Verify, new Listener() {
			@Override
			public void handleEvent(Event e) {
				String input = e.text;
				Text textbox = (Text) e.widget;
				UserInputPoint point = inputBoxesHex.get(textbox);
				if (input.length() == 0 && !loading && !solving)
					updateBoardDataWithUserInputHex(textbox, input);
				if (!solved && !loading && !solving) {
					if (input.toUpperCase().equals("A") && possibleHex.get(point.x).get(point.y).indexOf(10) != -1) {
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
									|| possibleHex.get(point.x).get(point.y).indexOf(Integer.parseInt(input)) == -1
									|| createsZeroPossible(new Point(point.x, point.y), Integer.parseInt(input))) {
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

	public boolean createsZeroPossible(Point point, int input) {
		boolean returnValue = false;
		Vector<Point> affectedPointsH = new Vector<Point>();
		Vector<Point> affectedPointsV = new Vector<Point>();
		Vector<Point> affectedPointsS = new Vector<Point>();
		if (tabChoice == HEX) {
			int x = 4 * (int) Math.floor(point.x / 4);
			int y = 4 * (int) Math.floor(point.y / 4);
			for (int i = 0; i < 16; i++) {
				if (point.y != i && possibleHex.get(point.x).get(i).size() == 1
						&& possibleHex.get(point.x).get(i).get(0) == input)
					returnValue = true;
				if (point.x != i && possibleHex.get(i).get(point.y).size() == 1
						&& possibleHex.get(i).get(point.y).get(0) == input)
					returnValue = true;
				if (point.y != i && boardHex[point.x][i] == -1)
					affectedPointsH.add(new Point(point.x, i));
				if (point.x != i && boardHex[i][point.y] == -1)
					affectedPointsV.add(new Point(i, point.y));
			}
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if ((point.x != x + i || point.y != y + j) && possibleHex.get(x + i).get(y + j).size() == 1
							&& possibleHex.get(x + i).get(y + j).size() == input)
						returnValue = true;
					if ((point.x != x + i || point.y != y + j) && boardHex[x + i][y + j] == -1)
						affectedPointsS.add(new Point(x + i, y + j));
				}
			}
			if (checkSubset(affectedPointsH, possibleHex, input) || checkSubset(affectedPointsV, possibleHex, input)
					|| checkSubset(affectedPointsS, possibleHex, input))
				returnValue = true;
		} else {
			affectedPointsH = new Vector<Point>();
			affectedPointsV = new Vector<Point>();
			affectedPointsS = new Vector<Point>();
//			int x = 3 * (int) Math.floor(point.x / 3);
//			int y = 3 * (int) Math.floor(point.y / 3);
//			if (tabChoice == KILLER) {
//				for (int i = 0; i < 9; i++) {
//					if (point.y != i && possibleKiller.get(point.x).get(i).size() == 1
//							&& possibleKiller.get(point.x).get(i).get(0) == input)
//						returnValue = true;
//					if (point.x != i && possibleKiller.get(i).get(point.y).size() == 1
//							&& possibleKiller.get(i).get(point.y).get(0) == input)
//						returnValue = true;
//					if (point.y != i && boardKiller[point.x][i] == 0)
//						affectedPointsH.add(new Point(point.x, i));
//					if (point.x != i && boardKiller[i][point.y] == 0)
//						affectedPointsV.add(new Point(i, point.y));
//				}
//				for (int i = 0; i < 3; i++) {
//					for (int j = 0; j < 3; j++) {
//						if ((point.x != x + i || point.y != y + j) && possibleKiller.get(x + i).get(y + j).size() == 1
//								&& possibleKiller.get(x + i).get(y + j).get(0) == input)
//							returnValue = true;
//						if ((point.x != x + i || point.y != y + j) && boardKiller[x + i][y + j] == 0)
//							affectedPointsS.add(new Point(x + i, y + j));
//					}
//				}
//				if (checkSubset(affectedPointsH, possibleKiller, input)
//						|| checkSubset(affectedPointsV, possibleKiller, input)
//						|| checkSubset(affectedPointsS, possibleKiller, input))
//					returnValue = true;
//			} else {
//				affectedPointsH = new Vector<Point>();
//				affectedPointsV = new Vector<Point>();
//				affectedPointsS = new Vector<Point>();
//				for (int i = 0; i < 9; i++) {
//					if (point.y != i && possibleNormal.get(point.x).get(i).size() == 1
//							&& possibleNormal.get(point.x).get(i).get(0) == input)
//						returnValue = true;
//					if (point.x != i && possibleNormal.get(i).get(point.y).size() == 1
//							&& possibleNormal.get(i).get(point.y).get(0) == input)
//						returnValue = true;
//					if (point.y != i && boardNormal[point.x][i] == 0)
//						affectedPointsH.add(new Point(point.x, i));
//					if (point.x != i && boardNormal[i][point.y] == 0)
//						affectedPointsV.add(new Point(i, point.y));
//				}
//				for (int i = 0; i < 3; i++) {
//					for (int j = 0; j < 3; j++) {
//						if ((point.x != x + i || point.y != y + j) && possibleNormal.get(x + i).get(y + j).size() == 1
//								&& possibleNormal.get(x + i).get(y + j).get(0) == input)
//							returnValue = true;
//						if ((point.x != x + i || point.y != y + j) && boardNormal[x + i][y + j] == 0)
//							affectedPointsS.add(new Point(x + i, y + j));
//					}
//				}
//				if (checkSubset(affectedPointsH, possibleNormal, input)
//						|| checkSubset(affectedPointsV, possibleNormal, input)
//						|| checkSubset(affectedPointsS, possibleNormal, input))
//					returnValue = true;
//			}

		}
		if (returnValue) {
			if (backgroundSolved && checkErroneousEntries()) {
				MessageBox dialog = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.YES | SWT.NO);
				dialog.setText(Messages.SudokuComposite_Error);
				dialog.setMessage(Messages.SudokuComposite_CreatesZeroPossible_Solved);
				if (dialog.open() == SWT.YES) {
					showErroneousEntries();
				}
			} else {
				MessageBox dialog = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
				dialog.setText(Messages.SudokuComposite_Error);
				dialog.setMessage(Messages.SudokuComposite_CreatesZeroPossible_Unsolved);
				dialog.open();
			}
		}
		return returnValue;
	}

	/**
	 * Check whether the affected points contain a subset which has fewer
	 * possibilities than affected points
	 * 
	 * @param affectedPoints the points affected by the input
	 * @param possible       the list containing the possibilities per box
	 * @return
	 */
	private boolean checkSubset(Vector<Point> affectedPoints, List<List<List<Integer>>> possible, int input) {
		Vector<Point> sortedPoints = new Vector<Point>();
		Vector<Integer> sortedSize = new Vector<Integer>();
		Vector<Integer> maxSubset = new Vector<Integer>();
		int size = 1, maxIndex = 0;
		while (sortedPoints.size() != affectedPoints.size()) {
			for (int i = 0; i < affectedPoints.size(); i++) {
				if ((possible.get(affectedPoints.get(i).x).get(affectedPoints.get(i).y).indexOf(input) == -1
						? possible.get(affectedPoints.get(i).x).get(affectedPoints.get(i).y).size()
						: possible.get(affectedPoints.get(i).x).get(affectedPoints.get(i).y).size() - 1) == size) {
					sortedPoints.add(affectedPoints.get(i));
					sortedSize.add(size);
				}
			}
			size++;
		}
		for (int i = 0; i < sortedPoints.size(); i++) {
			size = sortedSize.get(i);
			maxSubset = new Vector<Integer>();
			for (int j = 0; j < sortedPoints.size() && sortedSize.get(j) <= size; j++) {
				maxIndex = j;
				for (int k = 0; k < possible.get(sortedPoints.get(j).x).get(sortedPoints.get(j).y).size(); k++) {
					if (possible.get(sortedPoints.get(j).x).get(sortedPoints.get(j).y).get(k) != input && maxSubset
							.indexOf(possible.get(sortedPoints.get(j).x).get(sortedPoints.get(j).y).get(k)) == -1)
						maxSubset.add(possible.get(sortedPoints.get(j).x).get(sortedPoints.get(j).y).get(k));
				}
			}
			if (maxSubset.size() < maxIndex + 1)
				return true;
		}
		return false;
	}

	private boolean checkErroneousEntries() {
		if (backgroundSolved) {
			switch (tabChoice) {
//			case NORMAL:
//				for (int i = 0; i < 9; i++) {
//					for (int j = 0; j < 9; j++) {
//						if (boardNormal[i][j] != 0 && boardNormal[i][j] != tempBoard[i][j]) {
//							return true;
//						}
//					}
//				}
//				break;
//			case KILLER:
//				for (int i = 0; i < 9; i++) {
//					for (int j = 0; j < 9; j++) {
//						if (boardKiller[i][j] != 0 && boardKiller[i][j] != tempBoard[i][j]) {
//							return true;
//						}
//					}
//				}
//				break;
			case HEX:
				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 16; j++) {
						if (boardHex[i][j] != -1 && boardHex[i][j] != tempBoard[i][j]) {
							return true;
						}
					}
				}
				break;
			}
		}
		return false;
	}

	private void showErroneousEntries() {
		if (backgroundSolved) {
			switch (tabChoice) {
//			case NORMAL:
//				for (int i = 0; i < 9; i++) {
//					for (int j = 0; j < 9; j++) {
//						if (boardNormal[i][j] != 0 && boardNormal[i][j] != tempBoard[i][j]) {
//							labelCellNormal[i][j].setBackground(ColorService.RED);
//							boardTextNormal[i][j].setBackground(ColorService.RED);
//						}
//					}
//				}
//				break;
//			case KILLER:
//				for (int i = 0; i < 9; i++) {
//					for (int j = 0; j < 9; j++) {
//						if (boardKiller[i][j] != 0 && boardKiller[i][j] != tempBoard[i][j]) {
//							labelCellKiller[i][j].setBackground(ColorService.RED);
//							boardTextKiller[i][j].setBackground(ColorService.RED);
//						}
//					}
//				}
//				break;
			case HEX:
				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 16; j++) {
						if (boardHex[i][j] != -1 && boardHex[i][j] != tempBoard[i][j]) {
							labelCellHex[i][j].setBackground(ColorService.RED);
							boardTextHex[i][j].setBackground(ColorService.RED);
						}
					}
				}
				break;
			}
		}
	}

//	public void updateBoardDataWithUserInputNormal(Text inputBox, String inputStr) {
//		solved = false;
//		UserInputPoint point = inputBoxesNormal.get(inputBox);
//		int num = 0;
//		if (inputStr.length() > 0) {
//			num = Integer.parseInt(inputStr);
//			Point pt = new Point(point.x, point.y);
//			movesNormal.add(pt);
//			undoButton.setEnabled(true);
//		}
//		if (num == 0 && boardNormal[point.x][point.y] != 0)
//			addPossibleNormal(point.x, point.y, boardNormal[point.x][point.y]);
//		boardNormal[point.x][point.y] = num;
//		labelCellNormal[point.x][point.y].setBackground(ColorService.WHITE);
//		boardTextNormal[point.x][point.y].setBackground(ColorService.WHITE);
//		updatePossibilitiesNormal();
//	}

//	public void updateBoardDataWithUserInputKiller(Text inputBox, String inputStr) {
//		solved = false;
//		UserInputPoint point = inputBoxesKiller.get(inputBox);
//		int num = 0;
//		if (inputStr.length() > 0) {
//			num = Integer.parseInt(inputStr);
//			Point pt = new Point(point.x, point.y);
//			movesKiller.add(pt);
//			undoButton.setEnabled(true);
//		}
//		if (num == 0 && boardKiller[point.x][point.y] != 0)
//			addPossibleKiller(point.x, point.y, boardKiller[point.x][point.y]);
//		boardKiller[point.x][point.y] = num;
//		labelCellKiller[point.x][point.y].setBackground(ColorService.WHITE);
//		boardTextKiller[point.x][point.y].setBackground(ColorService.WHITE);
//		updatePossibilitiesKiller(boardKiller, possibleKiller);
//	}

	public void updateBoardDataWithUserInputHex(Text inputBox, String inputStr) {
		solved = false;
		UserInputPoint point = inputBoxesHex.get(inputBox);
		int num = -1;
		if (inputStr.length() > 0) {
			num = Integer.parseInt(inputStr);
			Point pt = new Point(point.x, point.y);
			movesHex.add(pt);
			undoButton.setEnabled(true);
		}
		if (num == -1 && boardHex[point.x][point.y] != -1)
			addPossibleHex(point.x, point.y, boardHex[point.x][point.y]);
		boardHex[point.x][point.y] = num;
		labelCellHex[point.x][point.y].setBackground(ColorService.WHITE);
		boardTextHex[point.x][point.y].setBackground(ColorService.WHITE);
		updatePossibilitiesHex(boardHex, possibleHex, true);
	}

	public static class UserInputPoint {
		int x;
		int y;

		UserInputPoint(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

//	public void updatePossibilitiesNormal() {
//		boolean changed = false;
//		List<Integer> used;
//		int idx;
//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				if (boardNormal[i][j] != 0) {
//					possibleNormal.get(i).get(j).clear();
//				}
//			}
//		}
//		for (int i = 0; i < 9; i++) {
//			used = new ArrayList<Integer>();
//			for (int j = 0; j < 9; j++)
//				if (boardNormal[i][j] != 0)
//					used.add(boardNormal[i][j]);
//			if (used.size() > 0) {
//				for (int j = 0; j < used.size(); j++) {
//					for (int k = 0; k < 9; k++) {
//						if (boardNormal[i][k] == 0) {
//							idx = possibleNormal.get(i).get(k).indexOf(used.get(j));
//							if (idx != -1)
//								possibleNormal.get(i).get(k).remove(idx);
//							if (autoFillOne && possibleNormal.get(i).get(k).size() == 1) {
//								boardNormal[i][k] = possibleNormal.get(i).get(k).get(0);
//								boardTextNormal[i][k].setText(Integer.toString(boardNormal[i][k]));
//								labelCellNormal[i][k].layout();
//								changed = true;
//							}
//						}
//					}
//				}
//			}
//		}
//		for (int i = 0; i < 9; i++) {
//			used = new ArrayList<Integer>();
//			for (int j = 0; j < 9; j++)
//				if (boardNormal[j][i] != 0)
//					used.add(boardNormal[j][i]);
//			if (used.size() > 0) {
//				for (int j = 0; j < used.size(); j++) {
//					for (int k = 0; k < 9; k++) {
//						if (boardNormal[k][i] == 0) {
//							idx = possibleNormal.get(k).get(i).indexOf(used.get(j));
//							if (idx != -1)
//								possibleNormal.get(k).get(i).remove(idx);
//							if (autoFillOne && possibleNormal.get(k).get(i).size() == 1) {
//								boardNormal[k][i] = possibleNormal.get(k).get(i).get(0);
//								boardTextNormal[k][i].setText(Integer.toString(boardNormal[k][i]));
//								labelCellNormal[k][i].layout();
//								changed = true;
//							}
//						}
//					}
//				}
//			}
//		}
//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j < 3; j++) {
//				used = new ArrayList<Integer>();
//				for (int k = 0; k < 3; k++) {
//					for (int l = 0; l < 3; l++) {
//						if (boardNormal[3 * i + k][3 * j + l] != 0)
//							used.add(boardNormal[3 * i + k][3 * j + l]);
//					}
//				}
//				if (used.size() > 0) {
//					for (int k = 0; k < used.size(); k++) {
//						for (int l = 0; l < 3; l++) {
//							for (int m = 0; m < 3; m++) {
//								if (boardNormal[3 * i + l][3 * j + m] == 0) {
//									idx = possibleNormal.get(3 * i + l).get(3 * j + m).indexOf(used.get(k));
//									if (idx != -1)
//										possibleNormal.get(3 * i + l).get(3 * j + m).remove(idx);
//									if (autoFillOne && possibleNormal.get(3 * i + l).get(3 * j + m).size() == 1) {
//										boardNormal[3 * i + l][3 * j + m] = possibleNormal.get(3 * i + l).get(3 * j + m)
//												.get(0);
//										boardTextNormal[3 * i + l][3 * j + m]
//												.setText(Integer.toString(boardNormal[3 * i + l][3 * j + m]));
//										labelCellNormal[3 * i + l][3 * j + m].layout();
//										changed = true;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				if (showPossible && possibleNormal.get(i).get(j).size() < 9) {
//					for (int k = 0; k < possibleNormal.get(i).get(j).size(); k++) {
//						boardLabelsNormal[i][j][k].setText(Integer.toString(possibleNormal.get(i).get(j).get(k)));
//					}
//					for (int k = possibleNormal.get(i).get(j).size(); k < 8; k++) {
//						boardLabelsNormal[i][j][k].setText("");
//					}
//				}
//				if (!showPossible) {
//					for (int k = 0; k < 8; k++)
//						boardLabelsNormal[i][j][k].setText("");
//				}
//				if (possibleNormal.get(i).get(j).size() == 9) {
//					for (int k = 0; k < 8; k++)
//						boardLabelsNormal[i][j][k].setText("");
//				}
//				labelCellNormal[i][j].layout();
////                boardTextNormal[i][j].redraw();
//			}
//		}
//		if (changed)
//			updatePossibilitiesNormal();
//	}

//	public void fillOneNormal() {
//		boolean changed = false;
//		for (int i = 0; i < 9 & !changed; i++) {
//			for (int j = 0; j < 9 & !changed; j++) {
//				if (possibleNormal.get(i).get(j).size() == 1) {
//					boardNormal[i][j] = possibleNormal.get(i).get(j).get(0);
//					boardTextNormal[i][j].setText(Integer.toString(boardNormal[i][j]));
//					labelCellNormal[i][j].layout();
//					startBlinkingArea(i, j);
//					changed = true;
//				}
//			}
//		}
//	}

//	public void fillOneKiller() {
//		boolean changed = false;
//		for (int i = 0; i < 9 & !changed; i++) {
//			for (int j = 0; j < 9 & !changed; j++) {
//				if (possibleKiller.get(i).get(j).size() == 1) {
//					boardKiller[i][j] = possibleKiller.get(i).get(j).get(0);
//					boardTextKiller[i][j].setText(Integer.toString(boardKiller[i][j]));
//					labelCellKiller[i][j].layout();
//					startBlinkingArea(i, j);
//					changed = true;
//				}
//			}
//		}
//	}

	public void fillOneHex() {
		boolean changed = false;
		for (int i = 0; i < 16 & !changed; i++) {
			for (int j = 0; j < 16 & !changed; j++) {
				if (possibleHex.get(i).get(j).size() == 1) {
					boardHex[i][j] = possibleHex.get(i).get(j).get(0);
					boardTextHex[i][j].setText(Integer.toString(boardHex[i][j]));
					labelCellHex[i][j].layout();
					startBlinkingArea(i, j);
					changed = true;
				}
			}
		}
	}

	public boolean sameLineKiller(List<Point> points) {
		boolean horizontal = true, vertical = true, box = true, boxRule = false;
		for (int i = 0; i < points.size() - 1; i++) {
			if (points.get(i).x != points.get(i + 1).x)
				horizontal = false;
			if (points.get(i).y != points.get(i + 1).y)
				vertical = false;
			if (boxRule && (points.get(i).x % 3 != points.get(i + 1).x % 3
					|| points.get(i).y % 3 != points.get(i + 1).y % 3))
				box = false;
		}
		return horizontal || vertical || (boxRule ? box : false);
	}

//	public void updateInitialPossibilitiesKiller() {
//		for (int i = 0; i < areas.size(); i++) {
//			List<Point> tempList = areas.get(i).getList();
//			List<Integer> possible = new ArrayList<Integer>();
//			for (int j = 0; j < tempList.size(); j++) {
//				for (int k = 0; k < possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size(); k++) {
//					if (possible.indexOf(possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k)) == -1) {
//						possible.add(possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k));
//					}
//				}
//			}
//			switch (areas.get(i).getOperator()) {
//			case ADDITION: {
//				List<Integer> allowed = generateAllowedValuesKiller(areas.get(i).getList().size(),
//						areas.get(i).getValue(), ADDITION, sameLineKiller(tempList), possible);
//				for (int j = 0; j < tempList.size(); j++) {
//					if (boardKiller[tempList.get(j).x][tempList.get(j).y] == 0) {
//						for (int k = possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size()
//								- 1; k >= 0; k--) {
//							if (allowed.indexOf(
//									possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k)) == -1) {
//								possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).remove(k);
//							}
//						}
//					}
//				}
//			}
//				break;
//			case SUBTRACTION: {
//				if (boardKiller[tempList.get(0).x][tempList.get(0).y] == 0
//						&& boardKiller[tempList.get(1).x][tempList.get(1).y] != 0) {
//					int result = (boardKiller[tempList.get(1).x][tempList.get(1).y] > areas.get(i).getValue())
//							? boardKiller[tempList.get(1).x][tempList.get(1).y] - areas.get(i).getValue()
//							: areas.get(i).getValue() - boardKiller[tempList.get(1).x][tempList.get(1).y];
//					if (possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).indexOf(result) != -1) {
//						possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).clear();
//						possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).add(result);
//					}
//				} else if (boardKiller[tempList.get(0).x][tempList.get(0).y] != 0
//						&& boardKiller[tempList.get(1).x][tempList.get(1).y] == 0) {
//					int result = (boardKiller[tempList.get(0).x][tempList.get(0).y] > areas.get(i).getValue())
//							? boardKiller[tempList.get(0).x][tempList.get(0).y] - areas.get(i).getValue()
//							: areas.get(i).getValue() - boardKiller[tempList.get(0).x][tempList.get(0).y];
//					if (possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).indexOf(result) != -1) {
//						possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).clear();
//						possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).add(result);
//					}
//				}
//			}
//				break;
//			case MULTIPLICATION: {
//				List<Integer> allowed = generateAllowedValuesKiller(areas.get(i).getList().size(),
//						areas.get(i).getValue(), MULTIPLICATION, sameLineKiller(tempList), possible);
//				for (int j = 0; j < tempList.size(); j++) {
//					if (boardKiller[tempList.get(j).x][tempList.get(j).y] == 0) {
//						for (int k = possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size()
//								- 1; k >= 0; k--) {
//							if (allowed.indexOf(
//									possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k)) == -1) {
//								possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).remove(k);
//							}
//						}
//					}
//				}
//			}
//				break;
//			case DIVISION: {
//				List<Integer> allowed = generateAllowedValuesKiller(areas.get(i).getList().size(),
//						areas.get(i).getValue(), DIVISION, sameLineKiller(tempList), possible);
//				if (boardKiller[tempList.get(0).x][tempList.get(0).y] == 0
//						&& boardKiller[tempList.get(1).x][tempList.get(1).y] != 0) {
//					int result = (boardKiller[tempList.get(1).x][tempList.get(1).y] > areas.get(i).getValue())
//							? boardKiller[tempList.get(1).x][tempList.get(1).y] / areas.get(i).getValue()
//							: areas.get(i).getValue() * boardKiller[tempList.get(1).x][tempList.get(1).y];
//					if (possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).indexOf(result) != -1) {
//						possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).clear();
//						possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).add(result);
//					}
//				} else if (boardKiller[tempList.get(0).x][tempList.get(0).y] != 0
//						&& boardKiller[tempList.get(1).x][tempList.get(1).y] == 0) {
//					int result = (boardKiller[tempList.get(0).x][tempList.get(0).y] > areas.get(i).getValue())
//							? boardKiller[tempList.get(0).x][tempList.get(0).y] / areas.get(i).getValue()
//							: areas.get(i).getValue() * boardKiller[tempList.get(0).x][tempList.get(0).y];
//					if (possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).indexOf(result) != -1) {
//						possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).clear();
//						possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).add(result);
//					}
//				} else {
//					for (int j = possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).size() - 1; j >= 0; j--) {
//						if (allowed.indexOf(possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).get(j)) == -1)
//							possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).remove(j);
//					}
//					for (int j = possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).size() - 1; j >= 0; j--) {
//						if (allowed.indexOf(possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).get(j)) == -1)
//							possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).remove(j);
//					}
//				}
//			}
//				break;
//			}
//		}
//	}

//	private void updatePossibilitiesKiller(int[][] board, List<List<List<Integer>>> posibilities) {
//		boolean changed = false;
//		List<Integer> used;
//		int idx;
//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				if (board[i][j] != 0) {
//					posibilities.get(i).get(j).clear();
//				}
//			}
//		}
//		if (killerFirstPossible == false) {
//			killerFirstPossible = true;
//			for (int i = 0; i < areas.size(); i++) {
//				List<Point> tempList = areas.get(i).getList();
//				List<Integer> possible = new ArrayList<Integer>();
//				for (int j = 0; j < tempList.size(); j++) {
//					for (int k = 0; k < possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size(); k++) {
//						if (possible
//								.indexOf(possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k)) == -1) {
//							possible.add(possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k));
//						}
//					}
//				}
//				switch (areas.get(i).getOperator()) {
//				case ADDITION: {
//					int tempSum = 0, tempSet = 0;
//					for (int j = 0; j < tempList.size(); j++) {
//						if (boardKiller[tempList.get(j).x][tempList.get(j).y] != 0) {
//							tempSet++;
//							tempSum = tempSum + boardKiller[tempList.get(j).x][tempList.get(j).y];
//						}
//					}
//					List<Integer> allowed = generateAllowedValuesKiller(areas.get(i).getList().size() - tempSet,
//							areas.get(i).getValue() - tempSum, ADDITION, sameLineKiller(tempList), possible);
//					for (int j = 0; j < tempList.size(); j++) {
//						if (boardKiller[tempList.get(j).x][tempList.get(j).y] == 0) {
//							for (int k = possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size()
//									- 1; k >= 0; k--) {
//								if (allowed.indexOf(
//										possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k)) == -1) {
//									possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).remove(k);
//								}
//							}
//						}
//					}
//				}
//					break;
//				case SUBTRACTION: {
//					if (boardKiller[tempList.get(0).x][tempList.get(0).y] == 0
//							&& boardKiller[tempList.get(1).x][tempList.get(1).y] != 0) {
//						int result = (boardKiller[tempList.get(1).x][tempList.get(1).y] > areas.get(i).getValue())
//								? boardKiller[tempList.get(1).x][tempList.get(1).y] - areas.get(i).getValue()
//								: areas.get(i).getValue() - boardKiller[tempList.get(1).x][tempList.get(1).y];
//						if (possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).indexOf(result) != -1) {
//							possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).clear();
//							possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).add(result);
//						}
//					} else if (boardKiller[tempList.get(0).x][tempList.get(0).y] != 0
//							&& boardKiller[tempList.get(1).x][tempList.get(1).y] == 0) {
//						int result = (boardKiller[tempList.get(0).x][tempList.get(0).y] > areas.get(i).getValue())
//								? boardKiller[tempList.get(0).x][tempList.get(0).y] - areas.get(i).getValue()
//								: areas.get(i).getValue() - boardKiller[tempList.get(0).x][tempList.get(0).y];
//						if (possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).indexOf(result) != -1) {
//							possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).clear();
//							possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).add(result);
//						}
//					}
//				}
//					break;
//				case MULTIPLICATION: {
//					int tempProd = 1, tempSet = 0;
//					for (int j = 0; j < tempList.size(); j++) {
//						if (boardKiller[tempList.get(j).x][tempList.get(j).y] != 0) {
//							tempSet++;
//							tempProd = tempProd * boardKiller[tempList.get(j).x][tempList.get(j).y];
//						}
//					}
//					List<Integer> allowed = generateAllowedValuesKiller(areas.get(i).getList().size() - tempSet,
//							areas.get(i).getValue() / tempProd, MULTIPLICATION, sameLineKiller(tempList), possible);
//					for (int j = 0; j < tempList.size(); j++) {
//						if (boardKiller[tempList.get(j).x][tempList.get(j).y] == 0) {
//							for (int k = possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size()
//									- 1; k >= 0; k--) {
//								if (allowed.indexOf(
//										possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k)) == -1) {
//									possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).remove(k);
//								}
//							}
//						}
//					}
//				}
//					break;
//				case DIVISION: {
//					List<Integer> allowed = generateAllowedValuesKiller(areas.get(i).getList().size(),
//							areas.get(i).getValue(), DIVISION, sameLineKiller(tempList), possible);
//					if (boardKiller[tempList.get(0).x][tempList.get(0).y] == 0
//							&& boardKiller[tempList.get(1).x][tempList.get(1).y] != 0) {
//						int result = (boardKiller[tempList.get(1).x][tempList.get(1).y] > areas.get(i).getValue())
//								? boardKiller[tempList.get(1).x][tempList.get(1).y] / areas.get(i).getValue()
//								: areas.get(i).getValue() * boardKiller[tempList.get(1).x][tempList.get(1).y];
//						if (possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).indexOf(result) != -1) {
//							possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).clear();
//							possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).add(result);
//						}
//					} else if (boardKiller[tempList.get(0).x][tempList.get(0).y] != 0
//							&& boardKiller[tempList.get(1).x][tempList.get(1).y] == 0) {
//						int result = (boardKiller[tempList.get(0).x][tempList.get(0).y] > areas.get(i).getValue())
//								? boardKiller[tempList.get(0).x][tempList.get(0).y] / areas.get(i).getValue()
//								: areas.get(i).getValue() * boardKiller[tempList.get(0).x][tempList.get(0).y];
//						if (possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).indexOf(result) != -1) {
//							possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).clear();
//							possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).add(result);
//						}
//					} else {
//						for (int j = possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).size()
//								- 1; j >= 0; j--) {
//							if (allowed
//									.indexOf(possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).get(j)) == -1)
//								possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).remove(j);
//						}
//						for (int j = possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).size()
//								- 1; j >= 0; j--) {
//							if (allowed
//									.indexOf(possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).get(j)) == -1)
//								possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).remove(j);
//						}
//					}
//				}
//					break;
//				}
//			}
//		}
//		for (int i = 0; i < 9; i++) {
//			used = new ArrayList<Integer>();
//			for (int j = 0; j < 9; j++)
//				if (boardKiller[i][j] != 0)
//					used.add(boardKiller[i][j]);
//			if (used.size() > 0) {
//				for (int j = 0; j < used.size(); j++) {
//					for (int k = 0; k < 9; k++) {
//						if (boardKiller[i][k] == 0) {
//							idx = possibleKiller.get(i).get(k).indexOf(used.get(j));
//							if (idx != -1)
//								possibleKiller.get(i).get(k).remove(idx);
//							if (autoFillOne && possibleKiller.get(i).get(k).size() == 1) {
//								boardKiller[i][k] = possibleKiller.get(i).get(k).get(0);
//								boardTextKiller[i][k].setText(Integer.toString(boardKiller[i][k]));
//								labelCellKiller[i][k].layout();
//								changed = true;
//							}
//						}
//					}
//				}
//			}
//		}
//		for (int i = 0; i < 9; i++) {
//			used = new ArrayList<Integer>();
//			for (int j = 0; j < 9; j++)
//				if (boardKiller[j][i] != 0)
//					used.add(boardKiller[j][i]);
//			if (used.size() > 0) {
//				for (int j = 0; j < used.size(); j++) {
//					for (int k = 0; k < 9; k++) {
//						if (boardKiller[k][i] == 0) {
//							idx = possibleKiller.get(k).get(i).indexOf(used.get(j));
//							if (idx != -1)
//								possibleKiller.get(k).get(i).remove(idx);
//							if (autoFillOne && possibleKiller.get(k).get(i).size() == 1) {
//								boardKiller[k][i] = possibleKiller.get(k).get(i).get(0);
//								boardTextKiller[k][i].setText(Integer.toString(boardKiller[k][i]));
//								labelCellKiller[k][i].layout();
//								changed = true;
//							}
//						}
//					}
//				}
//			}
//		}
//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				if (showPossible && possibleKiller.get(i).get(j).size() < 8) {
//					for (int k = 0; k < possibleKiller.get(i).get(j).size(); k++) {
//						boardLabelsKiller[i][j][k + 1].setText(Integer.toString(possibleKiller.get(i).get(j).get(k)));
//						boardLabelsKiller[i][j][k + 1].setBackground(ColorService.WHITE);
//					}
//					for (int k = possibleKiller.get(i).get(j).size() + 1; k < 8; k++) {
//						boardLabelsKiller[i][j][k].setText("");
//					}
//				}
//				if (!showPossible) {
//					for (int k = 0; k < 8; k++)
//						boardLabelsKiller[i][j][k].setText("");
//				}
//				if (possibleKiller.get(i).get(j).size() == 9) {
//					for (int k = 0; k < 8; k++)
//						boardLabelsKiller[i][j][k].setText("");
//				}
//				labelCellKiller[i][j].layout();
////                boardTextKiller[i][j].redraw();
//			}
//		}
//		if (changed) {
//			updatePossibilitiesKiller(boardKiller, possibleKiller);
//		}
//		}

	public void refresh() {
		switch (tabChoice) {
//		case NORMAL:
//			for (int i = 0; i < 9; i++) {
//				for (int j = 0; j < 9; j++) {
//					labelCellNormal[i][j].layout();
//				}
//			}
//			break;
//		case KILLER:
//			for (int i = 0; i < 9; i++) {
//				for (int j = 0; j < 9; j++) {
//					if (selected.contains(new Point(i, j))) {
//						labelCellKiller[i][j].setBackground(ColorService.WHITE);
//						labelCellKiller[i][j].setBackground(ColorService.RED);
//					} else {
//						labelCellKiller[i][j].setBackground(ColorService.RED);
//						labelCellKiller[i][j].setBackground(ColorService.WHITE);
//					}
//				}
//			}
//			break;
		case HEX:
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					labelCellHex[i][j].layout();
				}
			}
			break;
		}
	}

//	public List<Integer> generateAllowedValuesKiller(int numberOfPoints, int value, int operator, boolean sameLine,
//			List<Integer> possible) {
//		List<Integer> allowedValues = new ArrayList<Integer>();
//		Vector<Integer[]> allSubsets;
//		int[] intSet;
//		switch (operator) {
//		case ADDITION:
//			int maxPossible = value - ((numberOfPoints - 1) * numberOfPoints) / 2;
//			for (int i = 1; i <= maxPossible && i <= 9; i++)
//				if (possible.indexOf(i) != -1)
//					allowedValues.add(i);
//			allSubsets = new Vector<Integer[]>();
//			intSet = new int[allowedValues.size()];
//			for (int k = 0; k < allowedValues.size(); k++)
//				intSet[k] = allowedValues.get(k);
//			if (sameLine)
//				generateSubsets(allSubsets, intSet, new int[numberOfPoints], 0, 0);
//			else
//				generateSets(allSubsets, intSet, new int[numberOfPoints], 0, 0);
//			for (int i = allSubsets.size() - 1; i >= 0; i--) {
//				int sum = 0;
//				for (int j = 0; j < allSubsets.elementAt(i).length; j++)
//					sum = sum + allSubsets.elementAt(i)[j];
//				if (sum != value)
//					allSubsets.remove(i);
//			}
//			allowedValues.clear();
//			for (int i = 0; i < allSubsets.size(); i++) {
//				for (int j = 0; j < allSubsets.elementAt(i).length; j++) {
//					if (allowedValues.indexOf(allSubsets.elementAt(i)[j]) == -1)
//						allowedValues.add(allSubsets.elementAt(i)[j]);
//				}
//			}
//			break;
//		case SUBTRACTION:
//
//			break;
//
//		case MULTIPLICATION:
//			for (int i = 1; i <= 9; i++)
//				if (value % i == 0 && possible.indexOf(i) != -1)
//					allowedValues.add(i);
//			allSubsets = new Vector<Integer[]>();
//			intSet = new int[allowedValues.size()];
//			for (int k = 0; k < allowedValues.size(); k++)
//				intSet[k] = allowedValues.get(k);
//			if (sameLine)
//				generateSubsets(allSubsets, intSet, new int[numberOfPoints], 0, 0);
//			else
//				generateSets(allSubsets, intSet, new int[numberOfPoints], 0, 0);
//			for (int i = allSubsets.size() - 1; i >= 0; i--) {
//				int prod = 1;
//				for (int j = 0; j < allSubsets.elementAt(i).length; j++)
//					prod = prod * allSubsets.elementAt(i)[j];
//				if (prod != value)
//					allSubsets.remove(i);
//			}
//			allowedValues.clear();
//			for (int i = 0; i < allSubsets.size(); i++) {
//				for (int j = 0; j < allSubsets.elementAt(i).length; j++) {
//					if (allowedValues.indexOf(allSubsets.elementAt(i)[j]) == -1)
//						allowedValues.add(allSubsets.elementAt(i)[j]);
//				}
//			}
//			break;
//
//		case DIVISION:
//			allowedValues.add(1);
//			allowedValues.add(value);
//			switch (value) {
//			case 2: {
//				allowedValues.add(3);
//				allowedValues.add(4);
//				allowedValues.add(6);
//				allowedValues.add(8);
//			}
//				break;
//			case 3: {
//				allowedValues.add(2);
//				allowedValues.add(6);
//				allowedValues.add(9);
//			}
//				break;
//			case 4: {
//				allowedValues.add(2);
//				allowedValues.add(8);
//			}
//				break;
//			}
//			break;
//		}
//		return allowedValues;
//	}

	public void updatePossibilitiesHex(int[][] board, List<List<List<Integer>>> possibilities, boolean button) {
		boolean changed = false;
		List<Integer> used;
		int idx;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (board[i][j] != -1) {
					possibilities.get(i).get(j).clear();
				}
			}
		}
		for (int i = 0; i < 16; i++) {
			used = new ArrayList<Integer>();
			for (int j = 0; j < 16; j++)
				if (board[i][j] != -1)
					used.add(board[i][j]);
			if (used.size() > 0) {
				for (int j = 0; j < used.size(); j++) {
					for (int k = 0; k < 16; k++) {
						if (board[i][k] == -1) {
							idx = possibilities.get(i).get(k).indexOf(used.get(j));
							if (idx != -1)
								possibilities.get(i).get(k).remove(idx);
							if (autoFillOne && possibilities.get(i).get(k).size() == 1) {
								board[i][k] = possibilities.get(i).get(k).get(0);
								if (button) {
									boardTextHex[i][k].setText(valToTextHex(board[i][k]));
									labelCellHex[i][k].layout();
								}
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
				if (board[j][i] != -1)
					used.add(board[j][i]);
			if (used.size() > 0) {
				for (int j = 0; j < used.size(); j++) {
					for (int k = 0; k < 16; k++) {
						if (board[k][i] == -1) {
							idx = possibilities.get(k).get(i).indexOf(used.get(j));
							if (idx != -1)
								possibilities.get(k).get(i).remove(idx);
							if (autoFillOne && possibilities.get(k).get(i).size() == 1) {
								board[k][i] = possibilities.get(k).get(i).get(0);
								if (button) {
									boardTextHex[k][i].setText(valToTextHex(board[k][i]));
									labelCellHex[k][i].layout();
								}
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
						if (board[4 * i + k][4 * j + l] != -1)
							used.add(board[4 * i + k][4 * j + l]);
					}
				}
				if (used.size() > 0) {
					for (int k = 0; k < used.size(); k++) {
						for (int l = 0; l < 4; l++) {
							for (int m = 0; m < 4; m++) {
								if (board[4 * i + l][4 * j + m] == -1) {
									idx = possibilities.get(4 * i + l).get(4 * j + m).indexOf(used.get(k));
									if (idx != -1)
										possibilities.get(4 * i + l).get(4 * j + m).remove(idx);
									if (autoFillOne && possibilities.get(4 * i + l).get(4 * j + m).size() == 1) {
										board[4 * i + l][4 * j + m] = possibilities.get(4 * i + l).get(4 * j + m)
												.get(0);
										if (button) {
											boardTextHex[4 * i + l][4 * j + m]
													.setText(valToTextHex(board[4 * i + l][4 * j + m]));
											labelCellHex[4 * i + l][4 * j + m].layout();
										}
										changed = true;
									}
								}
							}
						}
					}
				}
			}
		}
		if (button) {
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					if (showPossible && possibilities.get(i).get(j).size() < 9) {
						for (int k = 0; k < possibilities.get(i).get(j).size(); k++) {
							boardLabelsHex[i][j][k].setText(valToTextHex(possibilities.get(i).get(j).get(k)));
						}
						for (int k = possibilities.get(i).get(j).size(); k < 8; k++) {
							boardLabelsHex[i][j][k].setText("");
						}
					}
					if (!showPossible) {
						for (int k = 0; k < 8; k++)
							boardLabelsHex[i][j][k].setText("");
					}
					if (possibilities.get(i).get(j).size() == 9) {
						for (int k = 0; k < 8; k++)
							boardLabelsHex[i][j][k].setText("");
					}
					boardTextHex[i][j].redraw();
					labelCellHex[i][j].layout();
				}
			}
		}
		if (changed)
			updatePossibilitiesHex(board, possibilities, button);
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

//	public void addPossibleNormal(int x, int y, int val) {
//		int idx;
//		for (int i = 0; i < 9; i++) {
//			idx = possibleNormal.get(i).get(y).indexOf(val);
//			if (idx == -1) {
//				possibleNormal.get(i).get(y).add(val);
//				Collections.sort(possibleNormal.get(i).get(y));
//			}
//		}
//		for (int i = 0; i < 9; i++) {
//			idx = possibleNormal.get(x).get(i).indexOf(val);
//			if (idx == -1) {
//				possibleNormal.get(x).get(i).add(val);
//				Collections.sort(possibleNormal.get(x).get(i));
//			}
//		}
//		int xx = 3 * (int) Math.floor(x / 3);
//		int yy = 3 * (int) Math.floor(y / 3);
//		for (int i = xx; i < xx + 3; i++) {
//			for (int j = yy; j < yy + 3; j++) {
//				idx = possibleNormal.get(i).get(j).indexOf(val);
//				if (idx == -1) {
//					possibleNormal.get(i).get(j).add(val);
//					Collections.sort(possibleNormal.get(i).get(j));
//				}
//			}
//		}
//		for (int i = 1; i <= 9; i++) {
//			idx = possibleNormal.get(x).get(y).indexOf(i);
//			if (idx == -1) {
//				possibleNormal.get(x).get(y).add(i);
//				Collections.sort(possibleNormal.get(x).get(y));
//			}
//		}
//	}

//	public void addPossibleKiller(int x, int y, int val) {
//		int idx;
//		for (int i = 0; i < 9; i++) {
//			idx = possibleKiller.get(i).get(y).indexOf(val);
//			if (idx == -1) {
//				possibleKiller.get(i).get(y).add(val);
//				Collections.sort(possibleKiller.get(i).get(y));
//			}
//		}
//		for (int i = 0; i < 9; i++) {
//			idx = possibleKiller.get(x).get(i).indexOf(val);
//			if (idx == -1) {
//				possibleKiller.get(x).get(i).add(val);
//				Collections.sort(possibleKiller.get(x).get(i));
//			}
//		}
//		for (int i = 1; i <= 9; i++) {
//			idx = possibleKiller.get(x).get(y).indexOf(i);
//			if (idx == -1) {
//				possibleKiller.get(x).get(y).add(i);
//				Collections.sort(possibleKiller.get(x).get(y));
//			}
//		}
//	}

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

	public Point getEmptySquare(int[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				switch (tabChoice) {
				case HEX:
					if (board[i][j] == -1) {
						return new Point(i, j);
					}
					break;
				default:
					if (board[i][j] == 0) {
						return new Point(i, j);
					}
					break;
				}
			}
		}
		return null;
	}

//	public boolean solvePuzzleNormal() {
//		if (backgroundSolve.getState() == Job.RUNNING)
//			backgroundSolve.cancel();
//		solving = true;
//		if (backgroundSolved) {
//			for (int i = 0; i < 9; i++) {
//				for (int j = 0; j < 9; j++) {
//					if (boardNormal[i][j] != 0 && boardNormal[i][j] != tempBoard[i][j]) {
//						showErroneousEntries();
//						MessageBox dialog = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
//						dialog.setText(Messages.SudokuComposite_Error);
//						dialog.setMessage(Messages.SudokuComposite_ContainsErrors);
//						dialog.open();
//						return false;
//					}
//				}
//			}
//			solved = true;
//			for (int i = 0; i < 9; i++) {
//				for (int j = 0; j < 9; j++) {
//					boardNormal[i][j] = tempBoard[i][j];
//				}
//			}
//		} else {
//			if (solveNormal(boardNormal)) {
//				solved = true;
//			}
//		}
//		if (solved) {
//			for (int i = 0; i < 9; i++) {
//				for (int j = 0; j < 9; j++) {
//					for (int k = 0; k < 8; k++)
//						boardLabelsNormal[i][j][k].setText("");
//					boardTextNormal[i][j].setText(Integer.toString(boardNormal[i][j]));
//				}
//			}
//			autoFillOne = false;
//			solving = false;
//			return true;
//		}
//		solving = false;
//		return false;
//	}

//	private boolean solvePuzzleKiller() {
//		if (backgroundSolve.getState() == Job.RUNNING)
//			backgroundSolve.cancel();
//		solving = true;
//		if (backgroundSolved) {
//			for (int i = 0; i < 9; i++) {
//				for (int j = 0; j < 9; j++) {
//					if (boardKiller[i][j] != 0 && boardKiller[i][j] != tempBoard[i][j]) {
//						showErroneousEntries();
//						MessageBox dialog = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
//						dialog.setText(Messages.SudokuComposite_Error);
//						dialog.setMessage(Messages.SudokuComposite_ContainsErrors);
//						dialog.open();
//						return false;
//					}
//				}
//			}
//			solved = true;
//
//			for (int i = 0; i < 9; i++) {
//				for (int j = 0; j < 9; j++) {
//					boardKiller[i][j] = tempBoard[i][j];
//				}
//			}
//		} else {
//			if (boxRule)
//				singleOuttie(boardKiller);
//			humanStrategiesKiller(boardKiller, possibleKiller);
//			if (solveKiller(boardKiller, null)) {
//				solved = true;
//			}
//		}
//		if (solved) {
//			for (int i = 0; i < 9; i++) {
//				for (int j = 0; j < 9; j++) {
//					for (int k = 0; k < 8; k++)
//						boardLabelsKiller[i][j][k].setText("");
//					boardTextKiller[i][j].setText(Integer.toString(boardKiller[i][j]));
//				}
//			}
//			autoFillOne = false;
//			solving = false;
//			return true;
//		}
//		solving = false;
//		updatePossibilitiesKiller(boardKiller, possibleKiller);
//		return false;
//	}

	public boolean solvePuzzleHex() {
		if (backgroundSolve.getState() == Job.RUNNING)
			backgroundSolve.cancel();
		solving = true;
		if (backgroundSolved) {
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					if (boardHex[i][j] != 0 && boardHex[i][j] != tempBoard[i][j]) {
						showErroneousEntries();
						MessageBox dialog = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText(Messages.SudokuComposite_Error);
						dialog.setMessage(Messages.SudokuComposite_ContainsErrors);
						dialog.open();
						return false;
					}
				}
			}
			solved = true;
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					boardHex[i][j] = tempBoard[i][j];
				}
			}
		} else {
			humanStrategiesHex(boardHex, possibleHex);
			guessOnDiagonalHex(boardHex, possibleHex);
			if (getEmptySquare(boardHex) != null) {
				if (solveHex(boardHex, null)) {
					// Solved with bruteforce
					solved = true;
				}
			} else {
				if (!checkPuzzleHex(boardHex)) {
					// Solved correctly without bruteforce
					solved = true;
				}
			}
		}
		if (solved) {
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
		solving = false;
		return false;
	}

//	public boolean solveNormal(int[][] board) {
//		Point start = getEmptySquare(board);
//		if (start == null) {
//			return true;
//		}
//
//		int x = start.x;
//		int y = start.y;
//		boolean solved = false;
//
//		for (int c = 1; c <= 9 && !solved; c++) {
//			if (possibleNormal.get(x).get(y).indexOf(c) != -1) {
//				if (!isConflictNormal(board, x, y, c)) {
//					board[x][y] = c;
//					solved = solveNormal(board);
//					if (!solved) {
//						board[x][y] = 0;
//					}
//				}
//			}
//		}
//		return solved;
//	}

//	private boolean solveKiller(int[][] board, final IProgressMonitor monitor) {
//		Point start = getEmptySquare(board);
//		if (start == null) {
//			return true;
//		}
//
//		int x = start.x;
//		int y = start.y;
//		boolean solved = false;
//
//		for (int c = 1; c <= 9 && !solved; c++) {
//			if (monitor != null && monitor.isCanceled())
//				return solved;
//			if (possibleKiller.get(x).get(y).indexOf(c) != -1) {
//				if (!isConflictKiller(board, x, y, c)) {
//					board[x][y] = c;
//					solved = solveKiller(board, monitor);
//					if (!solved) {
//						board[x][y] = 0;
//					}
//				}
//			}
//		}
//		return solved;
//	}

	public boolean solveHex(int[][] board, final IProgressMonitor monitor) {
		Point start = getEmptySquare(board);
		if (start == null) {
			return true;
		}

		int x = start.x;
		int y = start.y;
		boolean solved = false;

		for (int c = 0; c <= 15 && !solved; c++) {
			if (monitor != null && monitor.isCanceled())
				return solved;
			if (possibleHex.get(x).get(y).indexOf(c) != -1) {
				if (!isConflictHex(board, x, y, c)) {
					board[x][y] = c;
					solved = solveHex(board, monitor);
					if (!solved) {
						board[x][y] = -1;
					}
				}
			}
		}
		return solved;
	}

	public boolean isConflictNormal(int[][] board, int x, int y, int c) {
		return rowConflictNormal(board, x, y, c) || colConflictNormal(board, x, y, c)
				|| boxConflictNormal(board, x, y, c);
	}

//	public boolean isConflictKiller(int[][] board, int x, int y, int c) {
//		if (boxRule) {
//			return rowConflictKiller(board, y, c) || colConflictKiller(board, x, c) || boxConflictKiller(board, x, y, c)
//					|| prodConflictKiller(board, x, y, c) || additionConflictKiller(board, x, y, c)
//					|| subtractionConflictKiller(board, x, y, c) || divConflictKiller(board, x, y, c);
//		}
//		return rowConflictKiller(board, y, c) || colConflictKiller(board, x, c) || prodConflictKiller(board, x, y, c)
//				|| additionConflictKiller(board, x, y, c) || subtractionConflictKiller(board, x, y, c)
//				|| divConflictKiller(board, x, y, c);
//	}

	public boolean isConflictHex(int[][] board, int x, int y, int c) {
		return rowConflictHex(board, y, c) || colConflictHex(board, x, c) || boxConflictHex(board, x, y, c);
	}

	public boolean rowConflictNormal(int[][] board, int x, int y, int c) {
		for (int i = 0; i < 9; i++) {
			if (board[i][y] == c) {
				return true;
			}
		}
		return false;
	}

	public boolean colConflictNormal(int[][] board, int x, int y, int c) {
		for (int i = 0; i < 9; i++) {
			if (board[x][i] == c) {
				return true;
			}
		}
		return false;
	}

	public boolean boxConflictNormal(int[][] board, int xx, int yy, int c) {
		int x = 3 * (int) Math.floor(xx / 3);
		int y = 3 * (int) Math.floor(yy / 3);
		for (int i = x; i < x + 3; i++) {
			for (int j = y; j < y + 3; j++) {
				if (board[i][j] == c) {
					return true;
				}
			}
		}
		return false;
	}

//	private boolean rowConflictKiller(int[][] board, int y, int c) {
//		for (int i = 0; i < 9; i++) {
//			if (board[i][y] == c) {
//				return true;
//			}
//		}
//		return false;
//	}

//	private boolean colConflictKiller(int[][] board, int x, int c) {
//		for (int i = 0; i < 9; i++) {
//			if (board[x][i] == c) {
//				return true;
//			}
//		}
//		return false;
//	}

//	private boolean boxConflictKiller(int[][] board, int xx, int yy, int c) {
//		int x = 3 * (int) Math.floor(xx / 3);
//		int y = 3 * (int) Math.floor(yy / 3);
//		for (int i = x; i < x + 3; i++) {
//			for (int j = y; j < y + 3; j++) {
//				if (board[i][j] == c) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}

//	private boolean additionConflictKiller(int[][] board, int x, int y, int c) {
//		int tempAddition = c, tempReqAddition = 0;
//		List<Point> tempPoints;
//		boolean allSet;
//		for (int i = 0; i < areas.size(); i++) {
//			if (areas.get(i).getOperator() == ADDITION) {
//				if (areas.get(i).pointUsed(new Point(x, y))) {
//					tempPoints = areas.get(i).getList();
//					tempReqAddition = areas.get(i).getValue();
//					allSet = true;
//					for (int j = 0; j < tempPoints.size(); j++) {
//						tempAddition = tempAddition + board[tempPoints.get(j).x][tempPoints.get(j).y];
//						if (tempPoints.get(j).x != x || tempPoints.get(j).y != y)
//							if (board[tempPoints.get(j).x][tempPoints.get(j).y] == 0)
//								allSet = false;
//					}
//					if (allSet && tempAddition != tempReqAddition) {
//						return true;
//					}
//					if (tempAddition > tempReqAddition) {
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}

//	private boolean allSetSubtraction(int[][] board, Area subtraction, Point p) {
//		List<Point> subtractionPoints = subtraction.getList();
//		for (int i = 0; i < subtractionPoints.size(); i++) {
//			if (!subtractionPoints.get(i).equals(p)) {
//				if (board[subtractionPoints.get(i).x][subtractionPoints.get(i).y] == 0)
//					return false;
//			}
//		}
//		return true;
//	}

//	private boolean subtractionConflictKiller(int[][] board, int x, int y, int c) {
//		int tempReqSubtraction = 0;
//		List<Point> tempPoints = null;
//		for (int i = 0; i < areas.size(); i++) {
//			if (areas.get(i).getOperator() == SUBTRACTION) {
//				if (areas.get(i).pointUsed(new Point(x, y))) {
//					tempPoints = areas.get(i).getList();
//					tempReqSubtraction = areas.get(i).getValue();
//					if (allSetSubtraction(board, areas.get(i), new Point(x, y))) {
//						if (board[tempPoints.get(0).x][tempPoints.get(0).y] != 0) {
//							if (Math.abs(board[tempPoints.get(0).x][tempPoints.get(0).y] - c) != tempReqSubtraction) {
//								return true;
//							} else {
//								return false;
//							}
//						} else {
//							if (Math.abs(board[tempPoints.get(1).x][tempPoints.get(1).y] - c) != tempReqSubtraction) {
//								return true;
//							} else {
//								return false;
//							}
//						}
//					}
//				}
//			}
//		}
//		return false;
//	}

//	private boolean prodConflictKiller(int[][] board, int x, int y, int c) {
//		int tempProd = c, tempReqProd = 0;
//		List<Point> tempPoints;
//		boolean allSet;
//		for (int i = 0; i < areas.size(); i++) {
//			if (areas.get(i).getOperator() == MULTIPLICATION) {
//				if (areas.get(i).pointUsed(new Point(x, y))) {
//					tempPoints = areas.get(i).getList();
//					tempReqProd = areas.get(i).getValue();
//					allSet = true;
//					for (int j = 0; j < tempPoints.size(); j++) {
//						if (board[tempPoints.get(j).x][tempPoints.get(j).y] != 0) {
//							tempProd = tempProd * board[tempPoints.get(j).x][tempPoints.get(j).y];
//							if (tempReqProd % board[tempPoints.get(j).x][tempPoints.get(j).y] != 0) {
//								return true;
//							}
//						} else {
//							if (tempPoints.get(j).x != x || tempPoints.get(j).y != y)
//								allSet = false;
//						}
//					}
//					if (allSet && tempReqProd != tempProd) {
//						return true;
//					}
//					if (tempProd > tempReqProd) {
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}

//	private boolean allSetDiv(int[][] board, Area div, Point p) {
//		List<Point> divPoints = div.getList();
//		for (int i = 0; i < divPoints.size(); i++) {
//			if (!divPoints.get(i).equals(p)) {
//				if (board[divPoints.get(i).x][divPoints.get(i).y] == 0)
//					return false;
//			}
//		}
//		return true;
//	}

//	private boolean divConflictKiller(int[][] board, int x, int y, int c) {
//		int tempReqDiv = 0;
//		List<Point> tempPoints;
//		for (int i = 0; i < areas.size(); i++) {
//			if (areas.get(i).getOperator() == DIVISION) {
//				if (areas.get(i).pointUsed(new Point(x, y))) {
//					if (allSetDiv(board, areas.get(i), new Point(x, y))) {
//						tempReqDiv = areas.get(i).getValue();
//						tempPoints = areas.get(i).getList();
//						if (board[tempPoints.get(0).x][tempPoints.get(0).y] != 0) {
//							if (board[tempPoints.get(0).x][tempPoints.get(0).y] >= c) {
//								if (board[tempPoints.get(0).x][tempPoints.get(0).y] % c != 0) {
//									return true;
//								} else if (board[tempPoints.get(0).x][tempPoints.get(0).y] / c != tempReqDiv) {
//									return true;
//								}
//								return false;
//							} else {
//								if (c % board[tempPoints.get(0).x][tempPoints.get(0).y] != 0) {
//									return true;
//								} else if (c / board[tempPoints.get(0).x][tempPoints.get(0).y] != tempReqDiv) {
//									return true;
//								}
//								return false;
//							}
//						} else {
//							if (board[tempPoints.get(1).x][tempPoints.get(1).y] >= c) {
//								if (board[tempPoints.get(1).x][tempPoints.get(1).y] % c != 0) {
//									return true;
//								} else if (board[tempPoints.get(1).x][tempPoints.get(1).y] / c != tempReqDiv) {
//									return true;
//								}
//								return false;
//							} else {
//								if (c % board[tempPoints.get(1).x][tempPoints.get(1).y] != 0) {
//									return true;
//								} else if (c / board[tempPoints.get(1).x][tempPoints.get(1).y] != tempReqDiv) {
//									return true;
//								}
//								return false;
//							}
//						}
//					}
//				}
//			}
//		}
//		return false;
//	}

//	private void humanStrategiesKiller(int[][] board, List<List<List<Integer>>> possibilities) {
//		boolean changed = true;
//		while (changed) {
//			changed = false;
//			changed = changed || onePossibleKiller(board, possibilities) || nakedSingleKiller(board, possibilities)
//					|| nakedSubsetKiller(board, possibilities);
//			if (boxRule)
//				changed = changed || hiddenSingleKiller(board, possibilities) || blockAndCRKiller(board, possibilities);
//		}
//	}

	/**
	 * Check if there exist squares for which only one value is possible
	 *
	 * @return if this was the case
	 */
//	private boolean onePossibleKiller(int[][] board, List<List<List<Integer>>> possibilities) {
//		boolean changed = false;
//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				if (board[i][j] == 0 & possibilities.get(i).get(j).size() == 1) {
//					changed = true;
//					board[i][j] = possibilities.get(i).get(j).get(0);
//				}
//			}
//		}
//		return changed;
//	}

	/**
	 * Checks if there exists a square which is a "naked single"
	 *
	 * @return if this was the case
	 */
//	private boolean nakedSingleKiller(int[][] board, List<List<List<Integer>>> possibilities) {
//		boolean changed = false;
//		Vector<Integer> possible;
//		int idx;
//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				if (board[i][j] == 0) {
//					possible = new Vector<Integer>();
//					for (int k = 1; k <= 9; k++)
//						possible.add(k);
//					for (int k = 0; k < 9; k++)
//						if (board[k][j] != 0) {
//							idx = possible.indexOf(board[k][j]);
//							if (idx != -1)
//								possible.remove(idx);
//						}
//					for (int k = 0; k < 9; k++)
//						if (board[i][k] != 0) {
//							idx = possible.indexOf(board[i][k]);
//							if (idx != -1)
//								possible.remove(idx);
//						}
//					if (possible.size() == 1) {
//						changed = true;
//						board[i][j] = possible.elementAt(0);
//						updatePossibilitiesKiller(board, possibilities);
//					}
//				}
//			}
//		}
//		return changed;
//	}

	/**
	 * Checks if there exists a square which is a "hidden single"
	 *
	 * @return if this was the case
	 */
//	private boolean hiddenSingleKiller(int[][] board, List<List<List<Integer>>> possibilities) {
//		boolean changed = false;
//		Vector<Integer> set1;
//		Vector<Integer> set2;
//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				if (board[i][j] == 0) {
//					set1 = new Vector<Integer>();
//					set2 = new Vector<Integer>();
//					int x = 3 * (int) Math.floor(i / 3);
//					int y = 3 * (int) Math.floor(j / 3);
//					boolean neighborsSet = (board[x][j] != 0 || x == i) & (board[x + 1][j] != 0 || x + 1 == i)
//							& (board[x + 2][j] != 0 || x + 2 == i);
//					if (neighborsSet) {
//						if (y == j) {
//							for (int k = 0; k < 9; k++)
//								if ((3 * (k / 3) != x) & (board[k][y + 1] != 0))
//									set1.add(board[k][y + 1]);
//							for (int k = 0; k < 9; k++)
//								if ((3 * (k / 3) != x) & (board[k][y + 2] != 0))
//									set2.add(board[k][y + 2]);
//						} else if (y + 1 == j) {
//							for (int k = 0; k < 9; k++)
//								if ((3 * (k / 3) != x) & (board[k][y] != 0))
//									set1.add(board[k][y]);
//							for (int k = 0; k < 9; k++)
//								if ((3 * (k / 3) != x) & (board[k][y + 2] != 0))
//									set2.add(board[k][y + 2]);
//						} else {
//							for (int k = 0; k < 9; k++)
//								if ((3 * (k / 3) != x) & (board[k][y] != 0))
//									set1.add(board[k][y]);
//							for (int k = 0; k < 9; k++)
//								if ((3 * (k / 3) != x) & (board[k][y + 1] != 0))
//									set2.add(board[k][y + 1]);
//						}
//						int idx;
//						if (x == i) {
//							idx = set1.indexOf(board[x + 1][j]);
//							if (idx != -1)
//								set1.remove(idx);
//							idx = set1.indexOf(board[x + 2][j]);
//							if (idx != -1)
//								set1.remove(idx);
//						} else if (x + 1 == i) {
//							idx = set1.indexOf(board[x][j]);
//							if (idx != -1)
//								set1.remove(idx);
//							idx = set1.indexOf(board[x + 2][j]);
//							if (idx != -1)
//								set1.remove(idx);
//						} else {
//							idx = set1.indexOf(board[x][j]);
//							if (idx != -1)
//								set1.remove(idx);
//							idx = set1.indexOf(board[x + 1][j]);
//							if (idx != -1)
//								set1.remove(idx);
//						}
//						if (set1.size() != 0 & set2.size() != 0) {
//							for (int k = 0; k < set1.size(); k++) {
//								if (set2.indexOf(set1.elementAt(k)) != -1) {
//									board[i][j] = set1.elementAt(k);
//									updatePossibilitiesKiller(board, possibilities);
//									changed = true;
//									break;
//								}
//							}
//						}
//					}
//					if (board[i][j] == 0) {
//						set1 = new Vector<Integer>();
//						set2 = new Vector<Integer>();
//						neighborsSet = (board[i][y] != 0 || y == j) & (board[i][y + 1] != 0 || y + 1 == j)
//								& (board[i][y + 2] != 0 || y + 2 == j);
//						if (neighborsSet) {
//							if (x == i) {
//								for (int k = 0; k < 9; k++)
//									if ((3 * (k / 3) != y) & (board[x + 1][k] != 0))
//										set1.add(board[x + 1][k]);
//								for (int k = 0; k < 9; k++)
//									if ((3 * (k / 3) != y) & (board[x + 2][k] != 0))
//										set2.add(board[x + 2][k]);
//							} else if (x + 1 == i) {
//								for (int k = 0; k < 9; k++)
//									if ((3 * (k / 3) != y) & (board[x][k] != 0))
//										set1.add(board[x][k]);
//								for (int k = 0; k < 9; k++)
//									if ((3 * (k / 3) != y) & (board[x + 2][k] != 0))
//										set2.add(board[x + 2][k]);
//							} else {
//								for (int k = 0; k < 9; k++)
//									if ((3 * (k / 3) != y) & (board[x][k] != 0))
//										set1.add(board[x][k]);
//								for (int k = 0; k < 9; k++)
//									if ((3 * (k / 3) != y) & (board[x + 1][k] != 0))
//										set2.add(board[x + 1][k]);
//							}
//							int idx;
//							if (y == j) {
//								idx = set1.indexOf(board[i][y + 1]);
//								if (idx != -1)
//									set1.remove(idx);
//								idx = set1.indexOf(board[i][y + 2]);
//								if (idx != -1)
//									set1.remove(idx);
//							} else if (y + 1 == j) {
//								idx = set1.indexOf(board[i][y]);
//								if (idx != -1)
//									set1.remove(idx);
//								idx = set1.indexOf(board[i][y + 2]);
//								if (idx != -1)
//									set1.remove(idx);
//							} else {
//								idx = set1.indexOf(board[i][y]);
//								if (idx != -1)
//									set1.remove(idx);
//								idx = set1.indexOf(board[i][y + 2]);
//								if (idx != -1)
//									set1.remove(idx);
//							}
//							if (set1.size() != 0 & set2.size() != 0) {
//								for (int k = 0; k < set1.size(); k++) {
//									if (set2.indexOf(set1.elementAt(k)) != -1) {
//										board[i][j] = set1.elementAt(k);
//										updatePossibilitiesKiller(board, possibilities);
//										changed = true;
//										break;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		return changed;
//	}

	/**
	 * Checks if there are Block and Column/Row interactions
	 *
	 * @return if this was the case
	 */
//	private boolean blockAndCRKiller(int[][] board, List<List<List<Integer>>> possibilities) {
//		boolean changed = false;
//		Vector<Integer> set;
//		int idx;
//		for (int i1 = 0; i1 < 3; i1++) {
//			for (int i2 = 0; i2 < 3; i2++) {
//				for (int i3 = 0; i3 < 3; i3++) {
//					for (int i4 = 0; i4 < 3; i4++) {
//						// remove from columns
//						if (board[3 * i1 + i4][3 * i2 + i3] == 0 & board[3 * i1 + i4][3 * i2 + (1 + i3) % 3] == 0 &
//
//								board[3 * i1 + (1 + i4) % 3][3 * i2 + i3] != 0
//								& board[3 * i1 + (1 + i4) % 3][3 * i2 + (1 + i3) % 3] != 0 &
//
//								board[3 * i1 + (2 + i4) % 3][3 * i2 + i3] != 0
//								& board[3 * i1 + (2 + i4) % 3][3 * i2 + (1 + i3) % 3] != 0) {
//							set = new Vector<Integer>();
//							for (int j = 0; j < 9; j++) {
//								if ((j / 3) != i1 & board[j][3 * i2 + (2 + i3) % 3] != 0) {
//									set.add(board[j][3 * i2 + (2 + i3) % 3]);
//								}
//							}
//							if (set.size() > 0) {
//								for (int j = 0; j < set.size(); j++) {
//									if (board[3 * i1 + (1 + i4) % 3][3 * i2 + i3] != set.elementAt(j)
//											& board[3 * i1 + (1 + i4) % 3][3 * i2 + (1 + i3) % 3] != set.elementAt(j) &
//
//											board[3 * i1 + (2 + i4) % 3][3 * i2 + i3] != set.elementAt(j)
//											& board[3 * i1 + (2 + i4) % 3][3 * i2 + (1 + i3) % 3] != set.elementAt(j)) {
//										for (int k = 0; k < 9; k++) {
//											if ((k / 3) != i2) {
//												idx = possibilities.get(3 * i1 + i4).get(k).indexOf(set.elementAt(j));
//												if (idx != -1) {
//													possibilities.get(3 * i1 + i4).get(k).remove(idx);
//													changed = true;
//												}
//											}
//										}
//									}
//								}
//							}
//						}
//						// remove from rows
//						if (board[3 * i2 + i3][3 * i1 + i4] == 0 & board[3 * i2 + (1 + i3) % 3][3 * i1 + i4] == 0 &
//
//								board[3 * i2 + i3][3 * i1 + (1 + i4) % 3] != 0
//								& board[3 * i2 + (1 + i3) % 3][3 * i1 + (1 + i4) % 3] != 0 &
//
//								board[3 * i2 + i3][3 * i1 + (2 + i4) % 3] != 0
//								& board[3 * i2 + (1 + i3) % 3][3 * i1 + (2 + i4) % 3] != 0) {
//							set = new Vector<Integer>();
//							for (int j = 0; j < 9; j++) {
//								if ((j / 3) != i1 & board[3 * i2 + (2 + i3) % 3][j] != 0) {
//									set.add(board[3 * i2 + (2 + i3) % 3][j]);
//								}
//							}
//							if (set.size() > 0) {
//								for (int j = 0; j < set.size(); j++) {
//									if (board[3 * i2 + i3][3 * i1 + (1 + i4) % 3] != set.elementAt(j)
//											& board[3 * i2 + (1 + i3) % 3][3 * i1 + (1 + i4) % 3] != set.elementAt(j) &
//
//											board[3 * i2 + i3][3 * i1 + (2 + i4) % 3] != set.elementAt(j)
//											& board[3 * i2 + (1 + i3) % 3][3 * i1 + (2 + i4) % 3] != set.elementAt(j)) {
//										for (int k = 0; k < 9; k++) {
//											if ((k / 3) != i2) {
//												idx = possibilities.get(k).get(3 * i1 + i4).indexOf(set.elementAt(j));
//												if (idx != -1) {
//													possibilities.get(k).get(3 * i1 + i4).remove(idx);
//													changed = true;
//												}
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		return changed;
//	}

//	private boolean nakedSubsetKiller(int[][] board, List<List<List<Integer>>> possibilities) {
//		boolean changed = false, temp;
//		int total, idx;
//		int[] intSet;
//		Vector<Integer> set, values;
//		Vector<Integer[]> allSubsets, goodSubsets;
//		Vector<Point> pointSet;
//		int i = 2; // for i = 2, the subsets need to have size exactly 2
//		for (int j = 0; j < 9; j++) {
//			set = new Vector<Integer>();
//			for (int k = 0; k < 9; k++) {
//				if (possibilities.get(j).get(k).size() == i & board[j][k] == -1)
//					set.add(k);
//			}
//			while (set.size() >= i) {
//				total = 1;
//				for (int k = 1; k < set.size(); k++) {
//					if (possibilities.get(j).get(set.elementAt(0)).equals(possibilities.get(j).get(set.elementAt(k))))
//						total++;
//				}
//				if (total != i) {
//					set.remove(0);
//				} else {
//					for (int k = set.size() - 1; k > 0; k--) {
//						if (!possibilities.get(j).get(set.elementAt(0))
//								.equals(possibilities.get(j).get(set.elementAt(k))))
//							set.remove(k);
//					}
//					break;
//				}
//			}
//			if (set.size() == i) {
//				for (int k = 0; k < 9; k++) {
//					if (set.indexOf(k) == -1) {
//						for (int l = 0; l < possibilities.get(j).get(set.elementAt(0)).size(); l++) {
//							idx = possibilities.get(j).get(k)
//									.indexOf(possibilities.get(j).get(set.elementAt(0)).get(l));
//							if (idx != -1) {
//								possibilities.get(j).get(k).remove(idx);
//								changed = true;
//							}
//						}
//					}
//				}
//			}
//		}
//		for (int j = 0; j < 9; j++) {
//			set = new Vector<Integer>();
//			for (int k = 0; k < 9; k++) {
//				if (possibilities.get(k).get(j).size() == i & board[k][j] == -1)
//					set.add(k);
//			}
//			while (set.size() >= i) {
//				total = 1;
//				for (int k = 1; k < set.size(); k++) {
//					if (possibilities.get(set.elementAt(0)).get(j).equals(possibilities.get(set.elementAt(k)).get(j)))
//						total++;
//				}
//				if (total != i) {
//					set.remove(0);
//				} else {
//					for (int k = set.size() - 1; k > 0; k--) {
//						if (!possibilities.get(set.elementAt(0)).get(j)
//								.equals(possibilities.get(set.elementAt(k)).get(j)))
//							set.remove(k);
//					}
//					break;
//				}
//			}
//			if (set.size() == i) {
//				for (int k = 0; k < 9; k++) {
//					if (set.indexOf(k) == -1) {
//						for (int l = 0; l < possibilities.get(set.elementAt(0)).get(j).size(); l++) {
//							idx = possibilities.get(k).get(j)
//									.indexOf(possibilities.get(set.elementAt(0)).get(j).get(l));
//							if (idx != -1) {
//								possibilities.get(k).get(j).remove(idx);
//								changed = true;
//							}
//						}
//					}
//				}
//			}
//		}
//		for (int j1 = 0; j1 < 3; j1++) {
//			for (int j2 = 0; j2 < 3; j2++) {
//				pointSet = new Vector<Point>();
//				for (int k1 = 0; k1 < 3; k1++) {
//					for (int k2 = 0; k2 < 3; k2++) {
//						if (possibilities.get(3 * j1 + k1).get(3 * j2 + k2).size() == i
//								& board[3 * j1 + k1][3 * j2 + k2] == -1)
//							pointSet.add(new Point(3 * j1 + k1, 3 * j2 + k2));
//					}
//				}
//				while (pointSet.size() >= i) {
//					total = 1;
//					for (int k = 1; k < pointSet.size(); k++) {
//						if (possibilities.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y)
//								.equals(possibilities.get(pointSet.elementAt(k).x).get(pointSet.elementAt(k).y)))
//							total++;
//					}
//					if (total != i) {
//						pointSet.remove(0);
//					} else {
//						for (int k = pointSet.size() - 1; k > 0; k--) {
//							if (!possibilities.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y)
//									.equals(possibilities.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y)))
//								pointSet.remove(k);
//						}
//						break;
//					}
//				}
//				if (pointSet.size() == i) {
//					for (int k1 = 0; k1 < 3; k1++) {
//						for (int k2 = 0; k2 < 3; k2++) {
//							if (pointSet.indexOf(new Point(3 * j1 + k1, 3 * j2 + k2)) == -1) {
//								for (int l = 0; l < possibilities.get(pointSet.elementAt(0).x)
//										.get(pointSet.elementAt(0).y).size(); l++) {
//									idx = possibilities.get(3 * j1 + k1).get(3 * j2 + k2).indexOf(possibilities
//											.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y).get(l));
//									if (idx != -1) {
//										possibilities.get(3 * j1 + k1).get(3 * j2 + k2).remove(idx);
//										changed = true;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//
//		for (i = 3; i < 9; i++) {
//			for (int j = 0; j < 9; j++) {
//				set = new Vector<Integer>();
//				values = new Vector<Integer>();
//				allSubsets = new Vector<Integer[]>();
//				goodSubsets = new Vector<Integer[]>();
//				for (int k = 0; k < 9; k++) {
//					if (possibilities.get(j).get(k).size() <= i & board[j][k] == -1)
//						set.add(k);
//				}
//				if (set.size() >= i) {
//					intSet = new int[set.size()];
//					for (int k = 0; k < set.size(); k++)
//						intSet[k] = set.elementAt(k);
//					generateSubsets(allSubsets, intSet, new int[i], 0, 0);
//					for (int k1 = 0; k1 < allSubsets.size(); k1++) {
//						for (int k2 = 0; k2 < allSubsets.elementAt(k1).length; k2++) {
//							for (int k3 = 0; k3 < possibilities.get(j).get(allSubsets.elementAt(k1)[k2]).size(); k3++) {
//								if (values.indexOf(
//										possibilities.get(j).get(allSubsets.elementAt(k1)[k2]).get(k3)) == -1) {
//									values.add(possibilities.get(j).get(allSubsets.elementAt(k1)[k2]).get(k3));
//								}
//							}
//						}
//						if (values.size() == i) {
//							goodSubsets.add(allSubsets.elementAt(k1));
//						}
//						values.removeAllElements();
//					}
//				}
//				if (goodSubsets.size() > 0) {
//					for (int k1 = 0; k1 < goodSubsets.size(); k1++) {
//						for (int k2 = 0; k2 < goodSubsets.elementAt(k1).length; k2++) {
//							for (int k3 = 0; k3 < possibilities.get(j).get(goodSubsets.elementAt(k1)[k2])
//									.size(); k3++) {
//								if (values.indexOf(
//										possibilities.get(j).get(goodSubsets.elementAt(k1)[k2]).get(k3)) == -1) {
//									values.add(possibilities.get(j).get(goodSubsets.elementAt(k1)[k2]).get(k3));
//								}
//							}
//						}
//						for (int k2 = 0; k2 < 9; k2++) {
//							temp = true;
//							for (int k3 = 0; k3 < goodSubsets.elementAt(k1).length; k3++) {
//								if (goodSubsets.elementAt(k1)[k3] == k2)
//									temp = false;
//							}
//							if (temp) {
//								for (int l = 0; l < values.size(); l++) {
//									idx = possibilities.get(j).get(k2).indexOf(values.elementAt(l));
//									if (idx != -1) {
//										possibilities.get(j).get(k2).remove(idx);
//										changed = true;
//									}
//								}
//							}
//						}
//						values.removeAllElements();
//					}
//				}
//			}
//			for (int j = 0; j < 9; j++) {
//				set = new Vector<Integer>();
//				values = new Vector<Integer>();
//				allSubsets = new Vector<Integer[]>();
//				goodSubsets = new Vector<Integer[]>();
//				for (int k = 0; k < 9; k++) {
//					if (possibilities.get(k).get(j).size() <= i & board[k][j] == -1)
//						set.add(k);
//				}
//				if (set.size() >= i) {
//					intSet = new int[set.size()];
//					for (int k = 0; k < set.size(); k++)
//						intSet[k] = set.elementAt(k);
//					generateSubsets(allSubsets, intSet, new int[i], 0, 0);
//					for (int k1 = 0; k1 < allSubsets.size(); k1++) {
//						for (int k2 = 0; k2 < allSubsets.elementAt(k1).length; k2++) {
//							for (int k3 = 0; k3 < possibilities.get(allSubsets.elementAt(k1)[k2]).get(j).size(); k3++) {
//								if (values.indexOf(
//										possibilities.get(allSubsets.elementAt(k1)[k2]).get(j).get(k3)) == -1) {
//									values.add(possibilities.get(allSubsets.elementAt(k1)[k2]).get(j).get(k3));
//								}
//							}
//						}
//						if (values.size() == i) {
//							goodSubsets.add(allSubsets.elementAt(k1));
//						}
//						values.removeAllElements();
//					}
//				}
//				if (goodSubsets.size() > 0) {
//					for (int k1 = 0; k1 < goodSubsets.size(); k1++) {
//						for (int k2 = 0; k2 < goodSubsets.elementAt(k1).length; k2++) {
//							for (int k3 = 0; k3 < possibilities.get(goodSubsets.elementAt(k1)[k2]).get(j)
//									.size(); k3++) {
//								if (values.indexOf(
//										possibilities.get(goodSubsets.elementAt(k1)[k2]).get(j).get(k3)) == -1) {
//									values.add(possibilities.get(goodSubsets.elementAt(k1)[k2]).get(j).get(k3));
//								}
//							}
//						}
//						for (int k2 = 0; k2 < 9; k2++) {
//							temp = true;
//							for (int k3 = 0; k3 < goodSubsets.elementAt(k1).length; k3++) {
//								if (goodSubsets.elementAt(k1)[k3] == k2)
//									temp = false;
//							}
//							if (temp) {
//								for (int l = 0; l < values.size(); l++) {
//									idx = possibilities.get(k2).get(j).indexOf(values.elementAt(l));
//									if (idx != -1) {
//										possibilities.get(k2).get(j).remove(idx);
//										changed = true;
//									}
//								}
//							}
//						}
//						values.removeAllElements();
//					}
//				}
//			}
//
//			for (int j1 = 0; j1 < 3; j1++) {
//				for (int j2 = 0; j2 < 3; j2++) {
//					set = new Vector<Integer>();
//					values = new Vector<Integer>();
//					allSubsets = new Vector<Integer[]>();
//					goodSubsets = new Vector<Integer[]>();
//					pointSet = new Vector<Point>();
//					for (int k1 = 0; k1 < 3; k1++) {
//						for (int k2 = 0; k2 < 3; k2++) {
//							if (possibilities.get(3 * j1 + k1).get(3 * j2 + k2).size() <= i
//									& board[3 * j1 + k1][3 * j2 + k2] == -1)
//								pointSet.add(new Point(3 * j1 + k1, 3 * j2 + k2));
//						}
//					}
//					if (pointSet.size() >= i) {
//						intSet = new int[pointSet.size()];
//						for (int k = 0; k < pointSet.size(); k++)
//							intSet[k] = k;
//						generateSubsets(allSubsets, intSet, new int[i], 0, 0);
//						for (int k1 = 0; k1 < allSubsets.size(); k1++) {
//							for (int k2 = 0; k2 < allSubsets.elementAt(k1).length; k2++) {
//								for (int k3 = 0; k3 < possibilities
//										.get(pointSet.elementAt(allSubsets.elementAt(k1)[k2]).x)
//										.get(pointSet.elementAt(allSubsets.elementAt(k1)[k2]).y).size(); k3++) {
//									if (values.indexOf(possibilities
//											.get(pointSet.elementAt(allSubsets.elementAt(k1)[k2]).x)
//											.get(pointSet.elementAt(allSubsets.elementAt(k1)[k2]).y).get(k3)) == -1) {
//										values.add(possibilities.get(pointSet.elementAt(allSubsets.elementAt(k1)[k2]).x)
//												.get(pointSet.elementAt(allSubsets.elementAt(k1)[k2]).y).get(k3));
//									}
//								}
//							}
//							if (values.size() == i) {
//								goodSubsets.add(allSubsets.elementAt(k1));
//							}
//							values.removeAllElements();
//						}
//					}
//					if (goodSubsets.size() > 0) {
//						for (int k1 = 0; k1 < goodSubsets.size(); k1++) {
//							for (int k2 = 0; k2 < goodSubsets.elementAt(k1).length; k2++) {
//								for (int k3 = 0; k3 < possibilities
//										.get(pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).x)
//										.get(pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).y).size(); k3++) {
//									if (values.indexOf(possibilities
//											.get(pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).x)
//											.get(pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).y).get(k3)) == -1) {
//										values.add(possibilities
//												.get(pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).x)
//												.get(pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).y).get(k3));
//									}
//								}
//							}
//							for (int k2 = 0; k2 < 3; k2++) {
//								for (int k3 = 0; k3 < 3; k3++) {
//									temp = true;
//									for (int k4 = 0; k4 < goodSubsets.elementAt(k1).length; k4++) {
//										if (pointSet.elementAt(goodSubsets.elementAt(k1)[k4])
//												.equals(new Point(3 * j1 + k2, 3 * j2 + k3)) == true)
//											temp = false;
//									}
//									if (temp) {
//										for (int l = 0; l < values.size(); l++) {
//											idx = possibilities.get(3 * j1 + k2).get(3 * j2 + k3)
//													.indexOf(values.elementAt(l));
//											if (idx != -1) {
//												possibilities.get(3 * j1 + k2).get(3 * j2 + k3).remove(idx);
//												changed = true;
//											}
//										}
//									}
//								}
//							}
//							values.removeAllElements();
//						}
//					}
//				}
//			}
//		}
//		return changed;
//	}

//	private void singleOuttie(int[][] board) {
//		List<Area> boxAreas;
//		List<Point> tempPoints;
//		int totalPoints = 0, totalValue = 0;
//		Point outtie = null;
//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j < 3; j++) {
//				boxAreas = new ArrayList<Area>();
//				for (int x = 0; x < 3; x++) {
//					for (int y = 0; y < 3; y++) {
//						for (int k = 0; k < areas.size(); k++) {
//							if (areas.get(k).pointUsed(new Point(3 * i + x, 3 * j + y))
//									&& boxAreas.indexOf(areas.get(k)) == -1)
//								boxAreas.add(areas.get(k));
//						}
//					}
//				}
//				totalPoints = 0;
//				for (int k = 0; k < boxAreas.size(); k++)
//					totalPoints = totalPoints + boxAreas.get(k).getList().size();
//				if (totalPoints == 10) {
//					totalValue = 0;
//					for (int k = 0; k < boxAreas.size(); k++) {
//						totalValue = totalValue + boxAreas.get(k).getValue();
//						tempPoints = boxAreas.get(k).getList();
//						for (int l = 0; l < tempPoints.size(); l++) {
//							if (tempPoints.get(l).x / 3 != i || tempPoints.get(l).y / 3 != j)
//								outtie = tempPoints.get(l);
//						}
//					}
//					if (0 < totalValue - 45 && totalValue - 45 < 9) {
//						board[outtie.x][outtie.y] = totalValue - 45;
//					}
//				}
//			}
//		}
//	}

	public boolean rowConflictHex(int[][] board, int y, int c) {
		boolean temp = false;
		for (int i = 0; i < 16 && !temp; i++) {
			if (board[i][y] == c) {
				temp = true;
			} else {
				temp = false;
			}
		}
		return temp;
	}

	public boolean colConflictHex(int[][] board, int x, int c) {
		boolean temp = false;
		for (int i = 0; i < 16 && !temp; i++) {
			if (board[x][i] == c) {
				temp = true;
			} else {
				temp = false;
			}
		}
		return temp;
	}

	public boolean boxConflictHex(int[][] board, int xx, int yy, int c) {
		int x = 4 * (int) Math.floor(xx / 4);
		int y = 4 * (int) Math.floor(yy / 4);
		for (int i = x; i < x + 4; i++) {
			for (int j = y; j < y + 4; j++) {
				if (board[i][j] == c) {
					return true;
				}
			}
		}
		return false;
	}

	public void humanStrategiesHex(int[][] board, List<List<List<Integer>>> possibilities) {
		boolean changed = true;
		while (changed) {
			changed = false;
			changed = changed || onePossibleHex(board, possibilities, false)
					|| nakedSingleHex(board, possibilities, false) || hiddenSingleHex(board, possibilities, false)
					|| blockAndCRHex(board, possibilities) || nakedSubsetHex(board, possibilities)
					|| candidateLineHex(board, possibilities) || doublePairHex(board, possibilities)
					|| multipleLinesHex(board, possibilities);
		}
	}

	/**
	 * Check if there exist squares for which only one value is possible
	 *
	 * @return if this was the case
	 */
	public boolean onePossibleHex(int[][] board, List<List<List<Integer>>> possibilities, boolean button) {
		boolean changed = false;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (board[i][j] == -1 & possibilities.get(i).get(j).size() == 1) {
					board[i][j] = possibilities.get(i).get(j).get(0);
					if (button) {
						boardTextHex[i][j].setText(valToTextHex(board[i][j]));
						labelCellHex[i][j].layout();
					}
					changed = true;
				}
			}
		}
		if (changed) {
			updatePossibilitiesHex(board, possibilities, button);
		}
		return changed;
	}

	/**
	 * Adds c to the possible values for "neighbors" of (xx,yy)
	 *
	 * @param xx the horizontal coordinate
	 * @param yy the vertical coordinate
	 * @param c  the entered value
	 */
	public void addPossible(int[][] board, int xx, int yy, int c, List<List<List<Integer>>> possibilities) {
		int idx;
		for (int i = 0; i < 16; i++) {
			if (board[i][yy] == -1) {
				idx = possibilities.get(i).get(yy).indexOf(c);
				if (idx == -1)
					possibilities.get(i).get(yy).add(c);
			}
		}
		for (int j = 0; j < 16; j++) {
			if (board[xx][j] == -1) {
				idx = possibilities.get(xx).get(j).indexOf(c);
				if (idx == -1)
					possibilities.get(xx).get(j).add(c);
			}
		}
		int x = 4 * (int) Math.floor(xx / 4);
		int y = 4 * (int) Math.floor(yy / 4);
		for (int i = x; i < x + 4; i++) {
			for (int j = y; j < y + 4; j++) {
				if (board[i][j] == -1) {
					idx = possibilities.get(i).get(j).indexOf(c);
					if (idx == -1)
						possibilities.get(i).get(j).add(c);
				}
			}
		}
	}

	/**
	 * Checks if there exists a square which is a "naked single"
	 *
	 * @return if this was the case
	 */
	public boolean nakedSingleHex(int[][] board, List<List<List<Integer>>> possibilities, boolean button) {
		boolean changed = false;
		Vector<Integer> possible;
		int idx;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (board[i][j] == -1) {
					possible = new Vector<Integer>();
					for (int k = 0; k < 16; k++)
						possible.add(k);
					for (int k = 0; k < 16; k++)
						if (board[k][j] != -1) {
							idx = possible.indexOf(board[k][j]);
							if (idx != -1)
								possible.remove(idx);
						}
					for (int k = 0; k < 16; k++)
						if (board[i][k] != -1) {
							idx = possible.indexOf(board[i][k]);
							if (idx != -1)
								possible.remove(idx);
						}
					int x = 4 * (int) Math.floor(i / 4);
					int y = 4 * (int) Math.floor(j / 4);
					for (int k = x; k < x + 4; k++) {
						for (int l = y; l < y + 4; l++) {
							if (board[k][l] != -1) {
								idx = possible.indexOf(board[k][l]);
								if (idx != -1)
									possible.remove(idx);
							}
						}
					}
					if (possible.size() == 1) {
						board[i][j] = possible.elementAt(0);
						if (button) {
							boardTextHex[i][j].setText(valToTextHex(board[i][j]));
							labelCellHex[i][j].layout();
						}
						changed = true;
					}
				}
			}
		}
		if (changed) {
			updatePossibilitiesHex(board, possibilities, button);
		}
		return changed;
	}

	/**
	 * Checks if there exists a square which is a "hidden single"
	 *
	 * @return if this was the case
	 */
	public boolean hiddenSingleHex(int[][] board, List<List<List<Integer>>> possibilities, boolean button) {
		boolean changed = false;
		Vector<Integer> set1, set2, set3;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (board[i][j] == -1) {
					set1 = new Vector<Integer>();
					set2 = new Vector<Integer>();
					set3 = new Vector<Integer>();
					int x = 4 * (int) Math.floor(i / 4);
					int y = 4 * (int) Math.floor(j / 4);
					boolean neighborsSet = (board[x][j] != -1 || x == i) & (board[x + 1][j] != -1 || x + 1 == i)
							& (board[x + 2][j] != -1 || x + 2 == i) & (board[x + 3][j] != -1 || x + 3 == i);
					if (neighborsSet) {
						if (y == j) {
							for (int k = 0; k < 16; k++)
								if ((4 * (k / 4) != x) & (board[k][y + 1] != -1))
									set1.add(board[k][y + 1]);
							for (int k = 0; k < 16; k++)
								if ((4 * (k / 4) != x) & (board[k][y + 2] != -1))
									set2.add(board[k][y + 2]);
							for (int k = 0; k < 16; k++)
								if ((4 * (k / 4) != x) & (board[k][y + 3] != -1))
									set3.add(board[k][y + 3]);
						} else if (y + 1 == j) {
							for (int k = 0; k < 16; k++)
								if ((4 * (k / 4) != x) & (board[k][y] != -1))
									set1.add(board[k][y]);
							for (int k = 0; k < 16; k++)
								if ((4 * (k / 4) != x) & (board[k][y + 2] != -1))
									set2.add(board[k][y + 2]);
							for (int k = 0; k < 16; k++)
								if ((4 * (k / 4) != x) & (board[k][y + 3] != -1))
									set3.add(board[k][y + 3]);
						} else if (y + 2 == j) {
							for (int k = 0; k < 16; k++)
								if ((4 * (k / 4) != x) & (board[k][y] != -1))
									set1.add(board[k][y]);
							for (int k = 0; k < 16; k++)
								if ((4 * (k / 4) != x) & (board[k][y + 1] != -1))
									set2.add(board[k][y + 1]);
							for (int k = 0; k < 16; k++)
								if ((4 * (k / 4) != x) & (board[k][y + 3] != -1))
									set3.add(board[k][y + 3]);
						} else {
							for (int k = 0; k < 16; k++)
								if ((4 * (k / 4) != x) & (board[k][y] != -1))
									set1.add(board[k][y]);
							for (int k = 0; k < 16; k++)
								if ((4 * (k / 4) != x) & (board[k][y + 1] != -1))
									set2.add(board[k][y + 1]);
							for (int k = 0; k < 16; k++)
								if ((4 * (k / 4) != x) & (board[k][y + 2] != -1))
									set3.add(board[k][y + 2]);
						}
						int idx;
						if (x == i) {
							idx = set1.indexOf(board[x + 1][j]);
							if (idx != -1)
								set1.remove(idx);
							idx = set1.indexOf(board[x + 2][j]);
							if (idx != -1)
								set1.remove(idx);
							idx = set1.indexOf(board[x + 3][j]);
							if (idx != -1)
								set1.remove(idx);
						} else if (x + 1 == i) {
							idx = set1.indexOf(board[x][j]);
							if (idx != -1)
								set1.remove(idx);
							idx = set1.indexOf(board[x + 2][j]);
							if (idx != -1)
								set1.remove(idx);
							idx = set1.indexOf(board[x + 3][j]);
							if (idx != -1)
								set1.remove(idx);
						} else if (x + 2 == i) {
							idx = set1.indexOf(board[x][j]);
							if (idx != -1)
								set1.remove(idx);
							idx = set1.indexOf(board[x + 1][j]);
							if (idx != -1)
								set1.remove(idx);
							idx = set1.indexOf(board[x + 3][j]);
							if (idx != -1)
								set1.remove(idx);
						} else {
							idx = set1.indexOf(board[x][j]);
							if (idx != -1)
								set1.remove(idx);
							idx = set1.indexOf(board[x + 1][j]);
							if (idx != -1)
								set1.remove(idx);
							idx = set1.indexOf(board[x + 2][j]);
							if (idx != -1)
								set1.remove(idx);
						}
						if (set1.size() != 0 & set2.size() != 0 & set3.size() != 0) {
							for (int k = 0; k < set1.size(); k++) {
								if (set2.indexOf(set1.elementAt(k)) != -1 & set3.indexOf(set1.elementAt(k)) != -1) {
									board[i][j] = set1.elementAt(k);
									if (button) {
										boardTextHex[i][j].setText(valToTextHex(board[i][j]));
										labelCellHex[i][j].layout();
									}
									changed = true;
									break;
								}
							}
						}
					}
					if (board[i][j] == -1) {
						set1 = new Vector<Integer>();
						set2 = new Vector<Integer>();
						set3 = new Vector<Integer>();
						neighborsSet = (board[i][y] != -1 || y == j) & (board[i][y + 1] != -1 || y + 1 == j)
								& (board[i][y + 2] != -1 || y + 2 == j) & (board[i][y + 3] != -1 || y + 3 == j);
						if (neighborsSet) {
							if (x == i) {
								for (int k = 0; k < 16; k++)
									if ((4 * (k / 4) != y) & (board[x + 1][k] != -1))
										set1.add(board[x + 1][k]);
								for (int k = 0; k < 16; k++)
									if ((4 * (k / 4) != y) & (board[x + 2][k] != -1))
										set2.add(board[x + 2][k]);
								for (int k = 0; k < 16; k++)
									if ((4 * (k / 4) != y) & (board[x + 3][k] != -1))
										set3.add(board[x + 3][k]);
							} else if (x + 1 == i) {
								for (int k = 0; k < 16; k++)
									if ((4 * (k / 4) != y) & (board[x][k] != -1))
										set1.add(board[x][k]);
								for (int k = 0; k < 16; k++)
									if ((4 * (k / 4) != y) & (board[x + 2][k] != -1))
										set2.add(board[x + 2][k]);
								for (int k = 0; k < 16; k++)
									if ((4 * (k / 4) != y) & (board[x + 3][k] != -1))
										set3.add(board[x + 3][k]);
							} else if (x + 2 == i) {
								for (int k = 0; k < 16; k++)
									if ((4 * (k / 4) != y) & (board[x][k] != -1))
										set1.add(board[x][k]);
								for (int k = 0; k < 16; k++)
									if ((4 * (k / 4) != y) & (board[x + 1][k] != -1))
										set2.add(board[x + 1][k]);
								for (int k = 0; k < 16; k++)
									if ((4 * (k / 4) != y) & (board[x + 3][k] != -1))
										set3.add(board[x + 3][k]);
							} else {
								for (int k = 0; k < 16; k++)
									if ((4 * (k / 4) != y) & (board[x][k] != -1))
										set1.add(board[x][k]);
								for (int k = 0; k < 16; k++)
									if ((4 * (k / 4) != y) & (board[x + 1][k] != -1))
										set2.add(board[x + 1][k]);
								for (int k = 0; k < 16; k++)
									if ((4 * (k / 4) != y) & (board[x + 2][k] != -1))
										set3.add(board[x + 2][k]);
							}
							int idx;
							if (y == j) {
								idx = set1.indexOf(board[i][y + 1]);
								if (idx != -1)
									set1.remove(idx);
								idx = set1.indexOf(board[i][y + 2]);
								if (idx != -1)
									set1.remove(idx);
								idx = set1.indexOf(board[i][y + 3]);
								if (idx != -1)
									set1.remove(idx);
							} else if (y + 1 == j) {
								idx = set1.indexOf(board[i][y]);
								if (idx != -1)
									set1.remove(idx);
								idx = set1.indexOf(board[i][y + 2]);
								if (idx != -1)
									set1.remove(idx);
								idx = set1.indexOf(board[i][y + 3]);
								if (idx != -1)
									set1.remove(idx);
							} else if (y + 2 == j) {
								idx = set1.indexOf(board[i][y]);
								if (idx != -1)
									set1.remove(idx);
								idx = set1.indexOf(board[i][y + 1]);
								if (idx != -1)
									set1.remove(idx);
								idx = set1.indexOf(board[i][y + 3]);
								if (idx != -1)
									set1.remove(idx);
							} else {
								idx = set1.indexOf(board[i][y]);
								if (idx != -1)
									set1.remove(idx);
								idx = set1.indexOf(board[i][y + 1]);
								if (idx != -1)
									set1.remove(idx);
								idx = set1.indexOf(board[i][y + 2]);
								if (idx != -1)
									set1.remove(idx);
							}
							if (set1.size() != 0 & set2.size() != 0 & set3.size() != 0) {
								for (int k = 0; k < set1.size(); k++) {
									if (set2.indexOf(set1.elementAt(k)) != -1 & set3.indexOf(set1.elementAt(k)) != -1) {
										board[i][j] = set1.elementAt(k);
										if (button) {
											boardTextHex[i][j].setText(valToTextHex(board[i][j]));
											labelCellHex[i][j].layout();
										}
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
			updatePossibilitiesHex(board, possibilities, button);
		}
		return changed;
	}

	/**
	 * Checks if there are Block and Column/Row interactions
	 *
	 * @return if this was the case
	 */
	public boolean blockAndCRHex(int[][] board, List<List<List<Integer>>> possibilities) {
		boolean changed = false;
		Vector<Integer> set;
		int idx;
		for (int i1 = 0; i1 < 4; i1++) {
			for (int i2 = 0; i2 < 4; i2++) {
				for (int i3 = 0; i3 < 4; i3++) {
					for (int i4 = 0; i4 < 4; i4++) {
						// remove from columns
						if (board[4 * i1 + i4][4 * i2 + i3] == -1 & board[4 * i1 + i4][4 * i2 + (1 + i3) % 4] == -1
								& board[4 * i1 + i4][4 * i2 + (2 + i3) % 4] == -1 &

								board[4 * i1 + (1 + i4) % 4][4 * i2 + i3] != -1
								& board[4 * i1 + (1 + i4) % 4][4 * i2 + (1 + i3) % 4] != -1
								& board[4 * i1 + (1 + i4) % 4][4 * i2 + (2 + i3) % 4] != -1 &

								board[4 * i1 + (2 + i4) % 4][4 * i2 + i3] != -1
								& board[4 * i1 + (2 + i4) % 4][4 * i2 + (1 + i3) % 4] != -1
								& board[4 * i1 + (2 + i4) % 4][4 * i2 + (2 + i3) % 4] != -1 &

								board[4 * i1 + (3 + i4) % 4][4 * i2 + i3] != -1
								& board[4 * i1 + (3 + i4) % 4][4 * i2 + (1 + i3) % 4] != -1
								& board[4 * i1 + (3 + i4) % 4][4 * i2 + (2 + i3) % 4] != -1) {
							set = new Vector<Integer>();
							for (int j = 0; j < 16; j++) {
								if ((j / 4) != i1 & board[j][4 * i2 + (3 + i3) % 4] != -1) {
									set.add(board[j][4 * i2 + (3 + i3) % 4]);
								}
							}
							if (set.size() > 0) {
								for (int j = 0; j < set.size(); j++) {
									if (board[4 * i1 + (1 + i4) % 4][4 * i2 + i3] != set.elementAt(j)
											& board[4 * i1 + (1 + i4) % 4][4 * i2 + (1 + i3) % 4] != set.elementAt(j)
											& board[4 * i1 + (1 + i4) % 4][4 * i2 + (2 + i3) % 4] != set.elementAt(j) &

											board[4 * i1 + (2 + i4) % 4][4 * i2 + i3] != set.elementAt(j)
											& board[4 * i1 + (2 + i4) % 4][4 * i2 + (1 + i3) % 4] != set.elementAt(j)
											& board[4 * i1 + (2 + i4) % 4][4 * i2 + (2 + i3) % 4] != set.elementAt(j) &

											board[4 * i1 + (3 + i4) % 4][4 * i2 + i3] != set.elementAt(j)
											& board[4 * i1 + (3 + i4) % 4][4 * i2 + (1 + i3) % 4] != set.elementAt(j)
											& board[4 * i1 + (3 + i4) % 4][4 * i2 + (2 + i3) % 4] != set.elementAt(j)) {
										for (int k = 0; k < 16; k++) {
											if ((k / 4) != i2) {
												idx = possibilities.get(4 * i1 + i4).get(k).indexOf(set.elementAt(j));
												if (idx != -1) {
													possibilities.get(4 * i1 + i4).get(k).remove(idx);
													changed = true;
												}
											}
										}
									}
								}
							}
						}
						// remove from rows
						if (board[4 * i2 + i3][4 * i1 + i4] == -1 & board[4 * i2 + (1 + i3) % 4][4 * i1 + i4] == -1
								& board[4 * i2 + (2 + i3) % 4][4 * i1 + i4] == -1 &

								board[4 * i2 + i3][4 * i1 + (1 + i4) % 4] != -1
								& board[4 * i2 + (1 + i3) % 4][4 * i1 + (1 + i4) % 4] != -1
								& board[4 * i2 + (2 + i3) % 4][4 * i1 + (1 + i4) % 4] != -1 &

								board[4 * i2 + i3][4 * i1 + (2 + i4) % 4] != -1
								& board[4 * i2 + (1 + i3) % 4][4 * i1 + (2 + i4) % 4] != -1
								& board[4 * i2 + (2 + i3) % 4][4 * i1 + (2 + i4) % 4] != -1 &

								board[4 * i2 + i3][4 * i1 + (3 + i4) % 4] != -1
								& board[4 * i2 + (1 + i3) % 4][4 * i1 + (3 + i4) % 4] != -1
								& board[4 * i2 + (2 + i3) % 4][4 * i1 + (3 + i4) % 4] != -1) {
							set = new Vector<Integer>();
							for (int j = 0; j < 16; j++) {
								if ((j / 4) != i1 & board[4 * i2 + (3 + i3) % 4][j] != -1) {
									set.add(board[4 * i2 + (3 + i3) % 4][j]);
								}
							}
							if (set.size() > 0) {
								for (int j = 0; j < set.size(); j++) {
									if (board[4 * i2 + i3][4 * i1 + (1 + i4) % 4] != set.elementAt(j)
											& board[4 * i2 + (1 + i3) % 4][4 * i1 + (1 + i4) % 4] != set.elementAt(j)
											& board[4 * i2 + (2 + i3) % 4][4 * i1 + (1 + i4) % 4] != set.elementAt(j) &

											board[4 * i2 + i3][4 * i1 + (2 + i4) % 4] != set.elementAt(j)
											& board[4 * i2 + (1 + i3) % 4][4 * i1 + (2 + i4) % 4] != set.elementAt(j)
											& board[4 * i2 + (2 + i3) % 4][4 * i1 + (2 + i4) % 4] != set.elementAt(j) &

											board[4 * i2 + i3][4 * i1 + (3 + i4) % 4] != set.elementAt(j)
											& board[4 * i2 + (1 + i3) % 4][4 * i1 + (3 + i4) % 4] != set.elementAt(j)
											& board[4 * i2 + (2 + i3) % 4][4 * i1 + (3 + i4) % 4] != set.elementAt(j)) {
										for (int k = 0; k < 16; k++) {
											if ((k / 4) != i2) {
												idx = possibilities.get(k).get(4 * i1 + i4).indexOf(set.elementAt(j));
												if (idx != -1) {
													possibilities.get(k).get(4 * i1 + i4).remove(idx);
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

	public boolean nakedSubsetHex(int[][] board, List<List<List<Integer>>> possibilities) {
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
				if (possibilities.get(j).get(k).size() == i & board[j][k] == -1)
					set.add(k);
			}
			while (set.size() >= i) {
				total = 1;
				for (int k = 1; k < set.size(); k++) {
					if (possibilities.get(j).get(set.elementAt(0)).equals(possibilities.get(j).get(set.elementAt(k))))
						total++;
				}
				if (total != i) {
					set.remove(0);
				} else {
					for (int k = set.size() - 1; k > 0; k--) {
						if (!possibilities.get(j).get(set.elementAt(0))
								.equals(possibilities.get(j).get(set.elementAt(k))))
							set.remove(k);
					}
					break;
				}
			}
			if (set.size() == i) {
				for (int k = 0; k < 16; k++) {
					if (set.indexOf(k) == -1) {
						for (int l = 0; l < possibilities.get(j).get(set.elementAt(0)).size(); l++) {
							idx = possibilities.get(j).get(k)
									.indexOf(possibilities.get(j).get(set.elementAt(0)).get(l));
							if (idx != -1) {
								possibilities.get(j).get(k).remove(idx);
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
				if (possibilities.get(k).get(j).size() == i & board[k][j] == -1)
					set.add(k);
			}
			while (set.size() >= i) {
				total = 1;
				for (int k = 1; k < set.size(); k++) {
					if (possibilities.get(set.elementAt(0)).get(j).equals(possibilities.get(set.elementAt(k)).get(j)))
						total++;
				}
				if (total != i) {
					set.remove(0);
				} else {
					for (int k = set.size() - 1; k > 0; k--) {
						if (!possibilities.get(set.elementAt(0)).get(j)
								.equals(possibilities.get(set.elementAt(k)).get(j)))
							set.remove(k);
					}
					break;
				}
			}
			if (set.size() == i) {
				for (int k = 0; k < 16; k++) {
					if (set.indexOf(k) == -1) {
						for (int l = 0; l < possibilities.get(set.elementAt(0)).get(j).size(); l++) {
							idx = possibilities.get(k).get(j)
									.indexOf(possibilities.get(set.elementAt(0)).get(j).get(l));
							if (idx != -1) {
								possibilities.get(k).get(j).remove(idx);
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
						if (possibilities.get(4 * j1 + k1).get(4 * j2 + k2).size() == i
								& board[4 * j1 + k1][4 * j2 + k2] == -1)
							pointSet.add(new Point(4 * j1 + k1, 4 * j2 + k2));
					}
				}
				while (pointSet.size() >= i) {
					total = 1;
					for (int k = 1; k < pointSet.size(); k++) {
						if (possibilities.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y)
								.equals(possibilities.get(pointSet.elementAt(k).x).get(pointSet.elementAt(k).y)))
							total++;
					}
					if (total != i) {
						pointSet.remove(0);
					} else {
						for (int k = pointSet.size() - 1; k > 0; k--) {
							if (!possibilities.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y)
									.equals(possibilities.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y)))
								pointSet.remove(k);
						}
						break;
					}
				}
				if (pointSet.size() == i) {
					for (int k1 = 0; k1 < 4; k1++) {
						for (int k2 = 0; k2 < 4; k2++) {
							if (pointSet.indexOf(new Point(4 * j1 + k1, 4 * j2 + k2)) == -1) {
								for (int l = 0; l < possibilities.get(pointSet.elementAt(0).x)
										.get(pointSet.elementAt(0).y).size(); l++) {
									idx = possibilities.get(4 * j1 + k1).get(4 * j2 + k2).indexOf(possibilities
											.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y).get(l));
									if (idx != -1) {
										possibilities.get(4 * j1 + k1).get(4 * j2 + k2).remove(idx);
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
					if (possibilities.get(j).get(k).size() <= i & board[j][k] == -1)
						set.add(k);
				}
				if (set.size() >= i) {
					intSet = new int[set.size()];
					for (int k = 0; k < set.size(); k++)
						intSet[k] = set.elementAt(k);
					generateSubsets(allSubsets, intSet, new int[i], 0, 0);
					for (int k1 = 0; k1 < allSubsets.size(); k1++) {
						for (int k2 = 0; k2 < allSubsets.elementAt(k1).length; k2++) {
							for (int k3 = 0; k3 < possibilities.get(j).get(allSubsets.elementAt(k1)[k2]).size(); k3++) {
								if (values.indexOf(
										possibilities.get(j).get(allSubsets.elementAt(k1)[k2]).get(k3)) == -1) {
									values.add(possibilities.get(j).get(allSubsets.elementAt(k1)[k2]).get(k3));
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
							for (int k3 = 0; k3 < possibilities.get(j).get(goodSubsets.elementAt(k1)[k2])
									.size(); k3++) {
								if (values.indexOf(
										possibilities.get(j).get(goodSubsets.elementAt(k1)[k2]).get(k3)) == -1) {
									values.add(possibilities.get(j).get(goodSubsets.elementAt(k1)[k2]).get(k3));
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
									idx = possibilities.get(j).get(k2).indexOf(values.elementAt(l));
									if (idx != -1) {
										possibilities.get(j).get(k2).remove(idx);
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
					if (possibilities.get(k).get(j).size() <= i & board[k][j] == -1)
						set.add(k);
				}
				if (set.size() >= i) {
					intSet = new int[set.size()];
					for (int k = 0; k < set.size(); k++)
						intSet[k] = set.elementAt(k);
					generateSubsets(allSubsets, intSet, new int[i], 0, 0);
					for (int k1 = 0; k1 < allSubsets.size(); k1++) {
						for (int k2 = 0; k2 < allSubsets.elementAt(k1).length; k2++) {
							for (int k3 = 0; k3 < possibilities.get(allSubsets.elementAt(k1)[k2]).get(j).size(); k3++) {
								if (values.indexOf(
										possibilities.get(allSubsets.elementAt(k1)[k2]).get(j).get(k3)) == -1) {
									values.add(possibilities.get(allSubsets.elementAt(k1)[k2]).get(j).get(k3));
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
							for (int k3 = 0; k3 < possibilities.get(goodSubsets.elementAt(k1)[k2]).get(j)
									.size(); k3++) {
								if (values.indexOf(
										possibilities.get(goodSubsets.elementAt(k1)[k2]).get(j).get(k3)) == -1) {
									values.add(possibilities.get(goodSubsets.elementAt(k1)[k2]).get(j).get(k3));
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
									idx = possibilities.get(k2).get(j).indexOf(values.elementAt(l));
									if (idx != -1) {
										possibilities.get(k2).get(j).remove(idx);
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
							if (possibilities.get(4 * j1 + k1).get(4 * j2 + k2).size() <= i
									& board[4 * j1 + k1][4 * j2 + k2] == -1)
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
								for (int k3 = 0; k3 < possibilities
										.get(pointSet.elementAt(allSubsets.elementAt(k1)[k2]).x)
										.get(pointSet.elementAt(allSubsets.elementAt(k1)[k2]).y).size(); k3++) {
									if (values.indexOf(possibilities
											.get(pointSet.elementAt(allSubsets.elementAt(k1)[k2]).x)
											.get(pointSet.elementAt(allSubsets.elementAt(k1)[k2]).y).get(k3)) == -1) {
										values.add(possibilities.get(pointSet.elementAt(allSubsets.elementAt(k1)[k2]).x)
												.get(pointSet.elementAt(allSubsets.elementAt(k1)[k2]).y).get(k3));
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
								for (int k3 = 0; k3 < possibilities
										.get(pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).x)
										.get(pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).y).size(); k3++) {
									if (values.indexOf(possibilities
											.get(pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).x)
											.get(pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).y).get(k3)) == -1) {
										values.add(possibilities
												.get(pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).x)
												.get(pointSet.elementAt(goodSubsets.elementAt(k1)[k2]).y).get(k3));
									}
								}
							}
							for (int k2 = 0; k2 < 4; k2++) {
								for (int k3 = 0; k3 < 4; k3++) {
									temp = true;
									for (int k4 = 0; k4 < goodSubsets.elementAt(k1).length; k4++) {
										if (pointSet.elementAt(goodSubsets.elementAt(k1)[k4])
												.equals(new Point(4 * j1 + k2, 4 * j2 + k3)) == true)
											temp = false;
									}
									if (temp) {
										for (int l = 0; l < values.size(); l++) {
											idx = possibilities.get(4 * j1 + k2).get(4 * j2 + k3)
													.indexOf(values.elementAt(l));
											if (idx != -1) {
												possibilities.get(4 * j1 + k2).get(4 * j2 + k3).remove(idx);
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

	public void generateSubsets(Vector<Integer[]> subsets, int[] set, int[] subset, int subsetSize, int nextIndex) {
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

	public void generateSets(Vector<Integer[]> subsets, int[] set, int[] subset, int subsetSize, int nextIndex) {
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

	public boolean candidateLineHex(int[][] board, List<List<List<Integer>>> possibilities) {
		boolean changed = false;
		int idx;
		Vector<Point> pos;
		for (int boxX = 0; boxX < 4; boxX++) {
			for (int boxY = 0; boxY < 4; boxY++) {
				for (int i = 0; i < 16; i++) {
					pos = new Vector<Point>();
					for (int sqX = 0; sqX < 4; sqX++) {
						for (int sqY = 0; sqY < 4; sqY++) {
							if (board[4 * boxX + sqX][4 * boxY + sqY] == -1
									& possibilities.get(4 * boxX + sqX).get(4 * boxY + sqY).indexOf(i) != -1) {
								pos.add(new Point(4 * boxX + sqX, 4 * boxY + sqY));
							}
						}
					}
					if (pos.size() == 2) {
						if (pos.elementAt(0).x == pos.elementAt(1).x) {
							for (int j = 0; j < 16; j++) {
								if ((j / 4) != boxY) {
									idx = possibilities.get(pos.elementAt(0).x).get(j).indexOf(i);
									if (idx != -1)
										possibilities.get(pos.elementAt(0).x).get(j).remove(idx);
								}
							}
						} else if (pos.elementAt(0).y == pos.elementAt(1).y) {
							for (int j = 0; j < 16; j++) {
								if ((j / 4) != boxX) {
									idx = possibilities.get(j).get(pos.elementAt(0).y).indexOf(i);
									if (idx != -1)
										possibilities.get(j).get(pos.elementAt(0).y).remove(idx);
								}
							}
						}
					}
				}
			}
		}
		return changed;
	}

	public boolean doublePairHex(int[][] board, List<List<List<Integer>>> possibilities) {
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
							if (board[4 * boxX + sqX][4 * boxY + sqY] == -1
									& possibilities.get(4 * boxX + sqX).get(4 * boxY + sqY).indexOf(i) != -1) {
								pos.add(new Point(4 * boxX + sqX, 4 * boxY + sqY));
							}
						}
					}
					if (pos.size() == 2) {
						if (pos.elementAt(0).x == pos.elementAt(1).x) {
							pos1 = new Vector<Integer>();
							for (int j = 0; j < 16; j++) {
								if ((j / 4) != boxX) {
									if (board[j][pos.elementAt(0).y] == -1 & board[j][pos.elementAt(1).y] == -1
											& possibilities.get(j).get(pos.elementAt(0).y).indexOf(i) != -1
											& possibilities.get(j).get(pos.elementAt(1).y).indexOf(i) != -1) {
										temp = true;
										for (int k = 0; k < 16 & temp; k++) {
											if (k != pos.elementAt(0).y & k != pos.elementAt(1).y
													& possibilities.get(j).get(k).indexOf(i) != -1)
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
										idx = possibilities.get(j).get(y1).indexOf(i);
										if (idx != -1) {
											changed = true;
											possibilities.get(j).get(y1).remove(idx);
										}
										idx = possibilities.get(j).get(y2).indexOf(i);
										if (idx != -1) {
											changed = true;
											possibilities.get(j).get(y2).remove(idx);
										}
									}
								}
							}
						} else if (pos.elementAt(0).y == pos.elementAt(1).y) {
							pos1 = new Vector<Integer>();
							for (int j = 0; j < 16; j++) {
								if ((j / 4) != boxY) {
									if (board[pos.elementAt(0).x][j] == -1 & board[pos.elementAt(1).x][j] == -1
											& possibilities.get(pos.elementAt(0).x).get(j).indexOf(i) != -1
											& possibilities.get(pos.elementAt(1).x).get(j).indexOf(i) != -1) {
										temp = true;
										for (int k = 0; k < 16 & temp; k++) {
											if (k != pos.elementAt(0).x & k != pos.elementAt(1).x
													& possibilities.get(k).get(j).indexOf(i) != -1)
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
										idx = possibilities.get(x1).get(j).indexOf(i);
										if (idx != -1) {
											changed = true;
											possibilities.get(x1).get(j).remove(idx);
										}
										idx = possibilities.get(x2).get(j).indexOf(i);
										if (idx != -1) {
											changed = true;
											possibilities.get(x2).get(j).remove(idx);
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

	public boolean multipleLinesHex(int[][] board, List<List<List<Integer>>> possibilities) {
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
							if (board[4 * boxX + sqX][4 * boxY + sqY] == -1
									& possibilities.get(4 * boxX + sqX).get(4 * boxY + sqY).indexOf(i) != -1) {
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
											if (board[4 * boxX + sqX][4 * j + sqY] == -1 & possibilities
													.get(4 * boxX + sqX).get(4 * j + sqY).indexOf(i) != -1
													& x.indexOf(sqX) != -1) {
												possible++;
											}
											if (board[4 * boxX + sqX][4 * j + sqY] == -1 & possibilities
													.get(4 * boxX + sqX).get(4 * j + sqY).indexOf(i) != -1
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
												idx = possibilities.get(4 * boxX + sqX)
														.get(4 * badBox.elementAt(j) + sqY).indexOf(i);
												if (idx != -1
														& board[4 * boxX + sqX][4 * badBox.elementAt(j) + sqY] == -1) {
													changed = true;
													possibilities.get(4 * boxX + sqX).get(4 * badBox.elementAt(j) + sqY)
															.remove(idx);
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
											if (board[4 * j + sqX][4 * boxY + sqY] == -1 & possibilities
													.get(4 * j + sqX).get(4 * boxY + sqY).indexOf(i) != -1
													& y.indexOf(sqY) != -1) {
												possible++;
											}
											if (board[4 * j + sqX][4 * boxY + sqY] == -1 & possibilities
													.get(4 * j + sqX).get(4 * boxY + sqY).indexOf(i) != -1
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
												idx = possibilities.get(4 * badBox.elementAt(j) + sqX)
														.get(4 * boxY + sqY).indexOf(i);
												if (idx != -1
														& board[4 * badBox.elementAt(j) + sqX][4 * boxY + sqY] == -1) {
													changed = true;
													possibilities.get(4 * badBox.elementAt(j) + sqX).get(4 * boxY + sqY)
															.remove(idx);
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

	public void guessOnDiagonalHex(int[][] board, List<List<List<Integer>>> possibilities) {
		numberOfGuesses++;
		int i = 2, j = 0, guessPointX = -1, guessPointY = -1, guessPossible = 0;
		while (i < 6) {
			if (possibilities.get(j).get(j).size() == i) {
				guessPointX = j;
				guessPointY = j;
				break;
			}
			if (possibilities.get(j).get(15 - j).size() == i) {
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
			guessBoardHex = new int[16][16];
			guessPossibleHex = new ArrayList<List<List<Integer>>>();

			for (i = 0; i < 16; i++) {
				guessPossibleHex.add(new ArrayList<List<Integer>>());
				for (j = 0; j < 16; j++) {
					guessPossibleHex.get(i).add(new ArrayList<Integer>());
					guessBoardHex[i][j] = board[i][j];
					for (int k = 0; k < possibilities.get(i).get(j).size(); k++)
						guessPossibleHex.get(i).get(j).add(possibilities.get(i).get(j).get(k));
				}
			}
			boolean correct = false;
			while (true) {
				board[guessPointX][guessPointY] = possibilities.get(guessPointX).get(guessPointY).get(guessPossible);
				humanStrategiesHex(board, possibilities);
				correct = checkPuzzleHex(board);
				if (!correct)
					break;
				else {
					for (i = 0; i < 16; i++) {
						for (j = 0; j < 16; j++) {
							board[i][j] = guessBoardHex[i][j];
							possibilities.get(i).get(j).clear();
							for (int k = 0; k < guessPossibleHex.get(i).get(j).size(); k++)
								possibilities.get(i).get(j).add(guessPossibleHex.get(i).get(j).get(k));
						}
					}
					guessPossible++;
				}
			}
			guessOnDiagonalHex(board, possibilities);
		}
	}

	public boolean checkPuzzleHex(int[][] board, int x, int y, int c) {
		for (int i = 0; i < 16; i++) {
			if (i != x & board[i][y] == c) {
				return true;
			}
		}
		for (int i = 0; i < 16; i++) {
			if (i != y & board[x][i] == c) {
				return true;
			}
		}
		int xx = 4 * (int) Math.floor(x / 4);
		int yy = 4 * (int) Math.floor(y / 4);
		for (int i = xx; i < xx + 4; i++) {
			for (int j = yy; j < yy + 4; j++) {
				if (i != x & j != y & board[i][j] == c) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkPuzzleHex(int[][] board, int x, int y, List<List<List<Integer>>> possibilities) {
		if (possibilities.get(x).get(y).size() > 0) {
			for (int k = 0; k < possibilities.get(x).get(y).size(); k++) {
				if (checkPuzzleHex(board, x, y, possibilities.get(x).get(y).get(k)))
					return true;
			}
		}
		return false;
	}

	public boolean checkPuzzleHex(int[][] board) {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (board[i][j] != -1) {
					if (checkPuzzleHex(board, i, j, board[i][j]))
						return true;
				}
			}
		}
		return false;
	}

}

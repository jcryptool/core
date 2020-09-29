// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
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
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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


/**
 * GUI and Logic for the Killer Sudoku Tab.
 * @author Thorben Groos
 *
 */
public class KillerPuzzle extends Composite {

	private static final int ADDITION = 0;
	private static final int SUBTRACTION = 1;
	private static final int MULTIPLICATION = 2;
	private static final int DIVISION = 3;
	
	private Button solveModeButton;
	private Button enterModeButton;
	private Button solveButton;
	private Button hintButton;
	private Button undoButton;
//	private Button boxRuleButton;
	private Button showPossibleButton;
	private Button autoFillOneButton;
	private Button loadStandardPuzzle;
	private Button loadButton;
	private Button saveButton;
	private Button restartButton;
	private Button additionButton;
	private Button subtractionButton;
	private Button multiplicationButton;
	private Button divisionButton;
	
	protected boolean solveMode;
	/**
	 * contains the values that are entered in the sudoku.
	 */
	protected int[][] boardKiller;
	/**
	 * The value in the middle of each field in the sudoku.
	 */
	protected Text[][] boardTextKiller;
	protected Vector<Point> movesKiller = new Vector<Point>();
	/**
	 * List contains the points that are marked red while entering a new soduko.
	 */
	protected List<Point> selected;
	/**
	 * This are the fields of the sudoku that contain the possible values (boardLabelsNormal) and the entered
	 * value in the middle of a field .
	 */
	protected Composite[][] labelCellKiller;
	private Composite playField;
	protected Job backgroundSolve;
	protected Job dummyJob;
	protected boolean backgroundSolved;
	protected Random rnd = new Random(System.currentTimeMillis());
	/**
	 * The possibilities of each field.
	 */
	protected Label[][][] boardLabelsKiller;
	protected int[][] tempBoard;
	private boolean solved;
	private Map<Text, Point> inputBoxesKiller = new HashMap<Text, Point>();
	private List<List<List<Integer>>> possibleKiller;
	private boolean killerFirstPossible;
	private List<Area> areas;
	private boolean boxRule = false;
	private boolean autoFillOne;
	private boolean showPossible = true;
	private boolean loading;
	private boolean loadedKiller;
	protected boolean solving;
	private Runnable refresh;
	protected Runnable solveComplete;
	protected Runnable backgroundSolveComplete;
	/**
	 * Used to save the initial areas before solving a sudoku. Used by the restart button.
	 */
	protected List<Area> originalAreas;
	/**
	 * Used to save the initial values before solving a sudoku. Used by the restart button.
	 */
	protected int[][] originalSudoku = new int[9][9];
	

	/**
	 * A constructor
	 * @param parent The parent Composite
	 * @param style The Style. Most likely SWT.NONE.
	 */
	public KillerPuzzle(Composite parent, int style) {
		super(parent, style);
		
		GridLayout gl_this = new GridLayout();
		gl_this.marginWidth = 0;
		gl_this.marginHeight = 0;
		setLayout(gl_this);
		
		createHead(this);
		createMain(this);
		
		refresh = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						labelCellKiller[i][j].layout();
					}
				}
			}
		};
		
		dummyJob = new Job(Messages.SudokuComposite_SolvingPuzzle) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				while (backgroundSolve.getState() == Job.RUNNING) {
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
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
				if (solvePuzzleKiller()) {
					refresh();
				}
			}
		};
		
		backgroundSolve = new Job("Solving Puzzle in Background") {
			@Override
			public IStatus run(final IProgressMonitor monitor) {
				tempBoard = new int[9][9];
				ArrayList<List<List<Integer>>> tempPossibleKiller = new ArrayList<List<List<Integer>>>();
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						tempBoard[i][j] = boardKiller[i][j];
					}
				}
				for (int i = 0; i < possibleKiller.size(); i++) {
					tempPossibleKiller.add(new ArrayList<List<Integer>>());
					for (int j = 0; j < possibleKiller.get(i).size(); j++) {
						tempPossibleKiller.get(i).add(new ArrayList<Integer>());
						for (int k = 0; k < possibleKiller.get(i).get(j).size(); k++) {
							tempPossibleKiller.get(i).get(j).add(possibleKiller.get(i).get(j).get(k));
						}
					}
				}
				if (boxRule) {
					singleOuttie(tempBoard);
				}
				humanStrategiesKiller(tempBoard, tempPossibleKiller);
				if (solveKiller(tempBoard, monitor)) {
					getDisplay().asyncExec(backgroundSolveComplete);
				} else {
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			}
		};
		
		backgroundSolveComplete = new Runnable() {
			@Override
			public void run() {
				backgroundSolved = true;
				hintButton.setEnabled(true);
			}

		};
		
	}
	
	private boolean solvePuzzleKiller() {
		if (backgroundSolve.getState() == Job.RUNNING) {
			backgroundSolve.cancel();
		}
		solving = true;
		if (backgroundSolved) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (boardKiller[i][j] != 0 && boardKiller[i][j] != tempBoard[i][j]) {
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

			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					boardKiller[i][j] = tempBoard[i][j];
				}
			}
		} else {
			if (boxRule) {
				singleOuttie(boardKiller);
			}
			humanStrategiesKiller(boardKiller, possibleKiller);
			if (solveKiller(boardKiller, null)) {
				solved = true;
			}
		}
		if (solved) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					for (int k = 0; k < 8; k++) {
						boardLabelsKiller[i][j][k].setText("");
					}
					boardTextKiller[i][j].setText(Integer.toString(boardKiller[i][j]));
				}
			}
			autoFillOne = false;
			solving = false;
			return true;
		}
		solving = false;
		updatePossibilitiesKiller(boardKiller, possibleKiller);
		return false;
	}
	
	private boolean solveKiller(int[][] board, final IProgressMonitor monitor) {
		Point start = getEmptySquare(board);
		if (start == null) {
			return true;
		}

		int x = start.x;
		int y = start.y;
		boolean solved = false;

		for (int c = 1; c <= 9 && !solved; c++) {
			if (monitor != null && monitor.isCanceled())
				return solved;
			if (possibleKiller.get(x).get(y).indexOf(c) != -1) {
				if (!isConflictKiller(board, x, y, c)) {
					board[x][y] = c;
					solved = solveKiller(board, monitor);
					if (!solved) {
						board[x][y] = 0;
					}
				}
			}
		}
		return solved;
	}
	
	private boolean isConflictKiller(int[][] board, int x, int y, int c) {
		if (boxRule) {
			return rowConflictKiller(board, y, c) || colConflictKiller(board, x, c) || boxConflictKiller(board, x, y, c)
					|| prodConflictKiller(board, x, y, c) || additionConflictKiller(board, x, y, c)
					|| subtractionConflictKiller(board, x, y, c) || divConflictKiller(board, x, y, c);
		}
		return rowConflictKiller(board, y, c) || colConflictKiller(board, x, c) || prodConflictKiller(board, x, y, c)
				|| additionConflictKiller(board, x, y, c) || subtractionConflictKiller(board, x, y, c)
				|| divConflictKiller(board, x, y, c);
	}
	
	private boolean divConflictKiller(int[][] board, int x, int y, int c) {
		int tempReqDiv = 0;
		List<Point> tempPoints;
		for (int i = 0; i < areas.size(); i++) {
			if (areas.get(i).getOperator() == DIVISION) {
				if (areas.get(i).pointUsed(new Point(x, y))) {
					if (allSetDiv(board, areas.get(i), new Point(x, y))) {
						tempReqDiv = areas.get(i).getValue();
						tempPoints = areas.get(i).getList();
						if (board[tempPoints.get(0).x][tempPoints.get(0).y] != 0) {
							if (board[tempPoints.get(0).x][tempPoints.get(0).y] >= c) {
								if (board[tempPoints.get(0).x][tempPoints.get(0).y] % c != 0) {
									return true;
								} else if (board[tempPoints.get(0).x][tempPoints.get(0).y] / c != tempReqDiv) {
									return true;
								}
								return false;
							} else {
								if (c % board[tempPoints.get(0).x][tempPoints.get(0).y] != 0) {
									return true;
								} else if (c / board[tempPoints.get(0).x][tempPoints.get(0).y] != tempReqDiv) {
									return true;
								}
								return false;
							}
						} else {
							if (board[tempPoints.get(1).x][tempPoints.get(1).y] >= c) {
								if (board[tempPoints.get(1).x][tempPoints.get(1).y] % c != 0) {
									return true;
								} else if (board[tempPoints.get(1).x][tempPoints.get(1).y] / c != tempReqDiv) {
									return true;
								}
								return false;
							} else {
								if (c % board[tempPoints.get(1).x][tempPoints.get(1).y] != 0) {
									return true;
								} else if (c / board[tempPoints.get(1).x][tempPoints.get(1).y] != tempReqDiv) {
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
	
	private boolean allSetDiv(int[][] board, Area div, Point p) {
		List<Point> divPoints = div.getList();
		for (int i = 0; i < divPoints.size(); i++) {
			if (!divPoints.get(i).equals(p)) {
				if (board[divPoints.get(i).x][divPoints.get(i).y] == 0)
					return false;
			}
		}
		return true;
	}
	
	private boolean subtractionConflictKiller(int[][] board, int x, int y, int c) {
		int tempReqSubtraction = 0;
		List<Point> tempPoints = null;
		for (int i = 0; i < areas.size(); i++) {
			if (areas.get(i).getOperator() == SUBTRACTION) {
				if (areas.get(i).pointUsed(new Point(x, y))) {
					tempPoints = areas.get(i).getList();
					tempReqSubtraction = areas.get(i).getValue();
					if (allSetSubtraction(board, areas.get(i), new Point(x, y))) {
						if (board[tempPoints.get(0).x][tempPoints.get(0).y] != 0) {
							if (Math.abs(board[tempPoints.get(0).x][tempPoints.get(0).y] - c) != tempReqSubtraction) {
								return true;
							} else {
								return false;
							}
						} else {
							if (Math.abs(board[tempPoints.get(1).x][tempPoints.get(1).y] - c) != tempReqSubtraction) {
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
	
	private boolean allSetSubtraction(int[][] board, Area subtraction, Point p) {
		List<Point> subtractionPoints = subtraction.getList();
		for (int i = 0; i < subtractionPoints.size(); i++) {
			if (!subtractionPoints.get(i).equals(p)) {
				if (board[subtractionPoints.get(i).x][subtractionPoints.get(i).y] == 0)
					return false;
			}
		}
		return true;
	}
	
	private boolean additionConflictKiller(int[][] board, int x, int y, int c) {
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
						tempAddition = tempAddition + board[tempPoints.get(j).x][tempPoints.get(j).y];
						if (tempPoints.get(j).x != x || tempPoints.get(j).y != y)
							if (board[tempPoints.get(j).x][tempPoints.get(j).y] == 0)
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
	
	private boolean prodConflictKiller(int[][] board, int x, int y, int c) {
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
						if (board[tempPoints.get(j).x][tempPoints.get(j).y] != 0) {
							tempProd = tempProd * board[tempPoints.get(j).x][tempPoints.get(j).y];
							if (tempReqProd % board[tempPoints.get(j).x][tempPoints.get(j).y] != 0) {
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
	
	private boolean boxConflictKiller(int[][] board, int xx, int yy, int c) {
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
	
	private boolean colConflictKiller(int[][] board, int x, int c) {
		for (int i = 0; i < 9; i++) {
			if (board[x][i] == c) {
				return true;
			}
		}
		return false;
	}
	
	private boolean rowConflictKiller(int[][] board, int y, int c) {
		for (int i = 0; i < 9; i++) {
			if (board[i][y] == c) {
				return true;
			}
		}
		return false;
	}
	
	private void humanStrategiesKiller(int[][] board, List<List<List<Integer>>> possibilities) {
		boolean changed = true;
		while (changed) {
			changed = false;
			changed = changed || onePossibleKiller(board, possibilities) || nakedSingleKiller(board, possibilities)
					|| nakedSubsetKiller(board, possibilities);
			if (boxRule)
				changed = changed || hiddenSingleKiller(board, possibilities) || blockAndCRKiller(board, possibilities);
		}
	}
	
	/**
	 * Checks if there are Block and Column/Row interactions
	 *
	 * @return if this was the case
	 */
	private boolean blockAndCRKiller(int[][] board, List<List<List<Integer>>> possibilities) {
		boolean changed = false;
		Vector<Integer> set;
		int idx;
		for (int i1 = 0; i1 < 3; i1++) {
			for (int i2 = 0; i2 < 3; i2++) {
				for (int i3 = 0; i3 < 3; i3++) {
					for (int i4 = 0; i4 < 3; i4++) {
						// remove from columns
						if (board[3 * i1 + i4][3 * i2 + i3] == 0 & board[3 * i1 + i4][3 * i2 + (1 + i3) % 3] == 0 &

								board[3 * i1 + (1 + i4) % 3][3 * i2 + i3] != 0
								& board[3 * i1 + (1 + i4) % 3][3 * i2 + (1 + i3) % 3] != 0 &

								board[3 * i1 + (2 + i4) % 3][3 * i2 + i3] != 0
								& board[3 * i1 + (2 + i4) % 3][3 * i2 + (1 + i3) % 3] != 0) {
							set = new Vector<Integer>();
							for (int j = 0; j < 9; j++) {
								if ((j / 3) != i1 & board[j][3 * i2 + (2 + i3) % 3] != 0) {
									set.add(board[j][3 * i2 + (2 + i3) % 3]);
								}
							}
							if (set.size() > 0) {
								for (int j = 0; j < set.size(); j++) {
									if (board[3 * i1 + (1 + i4) % 3][3 * i2 + i3] != set.elementAt(j)
											& board[3 * i1 + (1 + i4) % 3][3 * i2 + (1 + i3) % 3] != set.elementAt(j) &

											board[3 * i1 + (2 + i4) % 3][3 * i2 + i3] != set.elementAt(j)
											& board[3 * i1 + (2 + i4) % 3][3 * i2 + (1 + i3) % 3] != set.elementAt(j)) {
										for (int k = 0; k < 9; k++) {
											if ((k / 3) != i2) {
												idx = possibilities.get(3 * i1 + i4).get(k).indexOf(set.elementAt(j));
												if (idx != -1) {
													possibilities.get(3 * i1 + i4).get(k).remove(idx);
													changed = true;
												}
											}
										}
									}
								}
							}
						}
						// remove from rows
						if (board[3 * i2 + i3][3 * i1 + i4] == 0 & board[3 * i2 + (1 + i3) % 3][3 * i1 + i4] == 0 &

								board[3 * i2 + i3][3 * i1 + (1 + i4) % 3] != 0
								& board[3 * i2 + (1 + i3) % 3][3 * i1 + (1 + i4) % 3] != 0 &

								board[3 * i2 + i3][3 * i1 + (2 + i4) % 3] != 0
								& board[3 * i2 + (1 + i3) % 3][3 * i1 + (2 + i4) % 3] != 0) {
							set = new Vector<Integer>();
							for (int j = 0; j < 9; j++) {
								if ((j / 3) != i1 & board[3 * i2 + (2 + i3) % 3][j] != 0) {
									set.add(board[3 * i2 + (2 + i3) % 3][j]);
								}
							}
							if (set.size() > 0) {
								for (int j = 0; j < set.size(); j++) {
									if (board[3 * i2 + i3][3 * i1 + (1 + i4) % 3] != set.elementAt(j)
											& board[3 * i2 + (1 + i3) % 3][3 * i1 + (1 + i4) % 3] != set.elementAt(j) &

											board[3 * i2 + i3][3 * i1 + (2 + i4) % 3] != set.elementAt(j)
											& board[3 * i2 + (1 + i3) % 3][3 * i1 + (2 + i4) % 3] != set.elementAt(j)) {
										for (int k = 0; k < 9; k++) {
											if ((k / 3) != i2) {
												idx = possibilities.get(k).get(3 * i1 + i4).indexOf(set.elementAt(j));
												if (idx != -1) {
													possibilities.get(k).get(3 * i1 + i4).remove(idx);
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
	
	/**
	 * Checks if there exists a square which is a "hidden single"
	 *
	 * @return if this was the case
	 */
	private boolean hiddenSingleKiller(int[][] board, List<List<List<Integer>>> possibilities) {
		boolean changed = false;
		Vector<Integer> set1;
		Vector<Integer> set2;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] == 0) {
					set1 = new Vector<Integer>();
					set2 = new Vector<Integer>();
					int x = 3 * (int) Math.floor(i / 3);
					int y = 3 * (int) Math.floor(j / 3);
					boolean neighborsSet = (board[x][j] != 0 || x == i) & (board[x + 1][j] != 0 || x + 1 == i)
							& (board[x + 2][j] != 0 || x + 2 == i);
					if (neighborsSet) {
						if (y == j) {
							for (int k = 0; k < 9; k++)
								if ((3 * (k / 3) != x) & (board[k][y + 1] != 0))
									set1.add(board[k][y + 1]);
							for (int k = 0; k < 9; k++)
								if ((3 * (k / 3) != x) & (board[k][y + 2] != 0))
									set2.add(board[k][y + 2]);
						} else if (y + 1 == j) {
							for (int k = 0; k < 9; k++)
								if ((3 * (k / 3) != x) & (board[k][y] != 0))
									set1.add(board[k][y]);
							for (int k = 0; k < 9; k++)
								if ((3 * (k / 3) != x) & (board[k][y + 2] != 0))
									set2.add(board[k][y + 2]);
						} else {
							for (int k = 0; k < 9; k++)
								if ((3 * (k / 3) != x) & (board[k][y] != 0))
									set1.add(board[k][y]);
							for (int k = 0; k < 9; k++)
								if ((3 * (k / 3) != x) & (board[k][y + 1] != 0))
									set2.add(board[k][y + 1]);
						}
						int idx;
						if (x == i) {
							idx = set1.indexOf(board[x + 1][j]);
							if (idx != -1)
								set1.remove(idx);
							idx = set1.indexOf(board[x + 2][j]);
							if (idx != -1)
								set1.remove(idx);
						} else if (x + 1 == i) {
							idx = set1.indexOf(board[x][j]);
							if (idx != -1)
								set1.remove(idx);
							idx = set1.indexOf(board[x + 2][j]);
							if (idx != -1)
								set1.remove(idx);
						} else {
							idx = set1.indexOf(board[x][j]);
							if (idx != -1)
								set1.remove(idx);
							idx = set1.indexOf(board[x + 1][j]);
							if (idx != -1)
								set1.remove(idx);
						}
						if (set1.size() != 0 & set2.size() != 0) {
							for (int k = 0; k < set1.size(); k++) {
								if (set2.indexOf(set1.elementAt(k)) != -1) {
									board[i][j] = set1.elementAt(k);
									updatePossibilitiesKiller(board, possibilities);
									changed = true;
									break;
								}
							}
						}
					}
					if (board[i][j] == 0) {
						set1 = new Vector<Integer>();
						set2 = new Vector<Integer>();
						neighborsSet = (board[i][y] != 0 || y == j) & (board[i][y + 1] != 0 || y + 1 == j)
								& (board[i][y + 2] != 0 || y + 2 == j);
						if (neighborsSet) {
							if (x == i) {
								for (int k = 0; k < 9; k++)
									if ((3 * (k / 3) != y) & (board[x + 1][k] != 0))
										set1.add(board[x + 1][k]);
								for (int k = 0; k < 9; k++)
									if ((3 * (k / 3) != y) & (board[x + 2][k] != 0))
										set2.add(board[x + 2][k]);
							} else if (x + 1 == i) {
								for (int k = 0; k < 9; k++)
									if ((3 * (k / 3) != y) & (board[x][k] != 0))
										set1.add(board[x][k]);
								for (int k = 0; k < 9; k++)
									if ((3 * (k / 3) != y) & (board[x + 2][k] != 0))
										set2.add(board[x + 2][k]);
							} else {
								for (int k = 0; k < 9; k++)
									if ((3 * (k / 3) != y) & (board[x][k] != 0))
										set1.add(board[x][k]);
								for (int k = 0; k < 9; k++)
									if ((3 * (k / 3) != y) & (board[x + 1][k] != 0))
										set2.add(board[x + 1][k]);
							}
							int idx;
							if (y == j) {
								idx = set1.indexOf(board[i][y + 1]);
								if (idx != -1)
									set1.remove(idx);
								idx = set1.indexOf(board[i][y + 2]);
								if (idx != -1)
									set1.remove(idx);
							} else if (y + 1 == j) {
								idx = set1.indexOf(board[i][y]);
								if (idx != -1)
									set1.remove(idx);
								idx = set1.indexOf(board[i][y + 2]);
								if (idx != -1)
									set1.remove(idx);
							} else {
								idx = set1.indexOf(board[i][y]);
								if (idx != -1)
									set1.remove(idx);
								idx = set1.indexOf(board[i][y + 2]);
								if (idx != -1)
									set1.remove(idx);
							}
							if (set1.size() != 0 & set2.size() != 0) {
								for (int k = 0; k < set1.size(); k++) {
									if (set2.indexOf(set1.elementAt(k)) != -1) {
										board[i][j] = set1.elementAt(k);
										updatePossibilitiesKiller(board, possibilities);
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
	
	private boolean nakedSubsetKiller(int[][] board, List<List<List<Integer>>> possibilities) {
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
				for (int k = 0; k < 9; k++) {
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
		for (int j = 0; j < 9; j++) {
			set = new Vector<Integer>();
			for (int k = 0; k < 9; k++) {
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
				for (int k = 0; k < 9; k++) {
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
		for (int j1 = 0; j1 < 3; j1++) {
			for (int j2 = 0; j2 < 3; j2++) {
				pointSet = new Vector<Point>();
				for (int k1 = 0; k1 < 3; k1++) {
					for (int k2 = 0; k2 < 3; k2++) {
						if (possibilities.get(3 * j1 + k1).get(3 * j2 + k2).size() == i
								& board[3 * j1 + k1][3 * j2 + k2] == -1)
							pointSet.add(new Point(3 * j1 + k1, 3 * j2 + k2));
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
					for (int k1 = 0; k1 < 3; k1++) {
						for (int k2 = 0; k2 < 3; k2++) {
							if (pointSet.indexOf(new Point(3 * j1 + k1, 3 * j2 + k2)) == -1) {
								for (int l = 0; l < possibilities.get(pointSet.elementAt(0).x)
										.get(pointSet.elementAt(0).y).size(); l++) {
									idx = possibilities.get(3 * j1 + k1).get(3 * j2 + k2).indexOf(possibilities
											.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y).get(l));
									if (idx != -1) {
										possibilities.get(3 * j1 + k1).get(3 * j2 + k2).remove(idx);
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
						for (int k2 = 0; k2 < 9; k2++) {
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
			for (int j = 0; j < 9; j++) {
				set = new Vector<Integer>();
				values = new Vector<Integer>();
				allSubsets = new Vector<Integer[]>();
				goodSubsets = new Vector<Integer[]>();
				for (int k = 0; k < 9; k++) {
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
						for (int k2 = 0; k2 < 9; k2++) {
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

			for (int j1 = 0; j1 < 3; j1++) {
				for (int j2 = 0; j2 < 3; j2++) {
					set = new Vector<Integer>();
					values = new Vector<Integer>();
					allSubsets = new Vector<Integer[]>();
					goodSubsets = new Vector<Integer[]>();
					pointSet = new Vector<Point>();
					for (int k1 = 0; k1 < 3; k1++) {
						for (int k2 = 0; k2 < 3; k2++) {
							if (possibilities.get(3 * j1 + k1).get(3 * j2 + k2).size() <= i
									& board[3 * j1 + k1][3 * j2 + k2] == -1)
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
							for (int k2 = 0; k2 < 3; k2++) {
								for (int k3 = 0; k3 < 3; k3++) {
									temp = true;
									for (int k4 = 0; k4 < goodSubsets.elementAt(k1).length; k4++) {
										if (pointSet.elementAt(goodSubsets.elementAt(k1)[k4])
												.equals(new Point(3 * j1 + k2, 3 * j2 + k3)) == true)
											temp = false;
									}
									if (temp) {
										for (int l = 0; l < values.size(); l++) {
											idx = possibilities.get(3 * j1 + k2).get(3 * j2 + k3)
													.indexOf(values.elementAt(l));
											if (idx != -1) {
												possibilities.get(3 * j1 + k2).get(3 * j2 + k3).remove(idx);
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
	
	/**
	 * Checks if there exists a square which is a "naked single"
	 *
	 * @return if this was the case
	 */
	private boolean nakedSingleKiller(int[][] board, List<List<List<Integer>>> possibilities) {
		boolean changed = false;
		Vector<Integer> possible;
		int idx;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] == 0) {
					possible = new Vector<Integer>();
					for (int k = 1; k <= 9; k++)
						possible.add(k);
					for (int k = 0; k < 9; k++)
						if (board[k][j] != 0) {
							idx = possible.indexOf(board[k][j]);
							if (idx != -1)
								possible.remove(idx);
						}
					for (int k = 0; k < 9; k++)
						if (board[i][k] != 0) {
							idx = possible.indexOf(board[i][k]);
							if (idx != -1)
								possible.remove(idx);
						}
					if (possible.size() == 1) {
						changed = true;
						board[i][j] = possible.elementAt(0);
						updatePossibilitiesKiller(board, possibilities);
					}
				}
			}
		}
		return changed;
	}
	
	/**
	 * Check if there exist squares for which only one value is possible
	 *
	 * @return if this was the case
	 */
	private boolean onePossibleKiller(int[][] board, List<List<List<Integer>>> possibilities) {
		boolean changed = false;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] == 0 & possibilities.get(i).get(j).size() == 1) {
					changed = true;
					board[i][j] = possibilities.get(i).get(j).get(0);
				}
			}
		}
		return changed;
	}
	
	private void singleOuttie(int[][] board) {
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
						board[outtie.x][outtie.y] = totalValue - 45;
					}
				}
			}
		}
	}

	/**
	 * Creates the part of the GUI in which the buttons and the text field is.
	 * @param killerPuzzle
	 */
	private void createMain(KillerPuzzle killerPuzzle) {
		Group mainGroup = new Group(this, SWT.NONE);
		mainGroup.setLayout(new GridLayout(2, false));
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mainGroup.setText(Messages.SudokuComposite_MainGroup_Title);

		createButtonArea(mainGroup);
		createPlayFieldArea(mainGroup);
		makeWhite();

		getDisplay().asyncExec(refresh);
		
	}
	
	private void makeWhite() {
		Thread makeWhite = new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						labelCellKiller[i][j].setBackground(ColorService.WHITE);
						for (int k = 0; k < 8; k++) {
							boardLabelsKiller[i][j][k].setBackground(ColorService.WHITE);
						}
					}
				}
			}
		};
		getDisplay().asyncExec(makeWhite);
	}

	private void createPlayFieldArea(Composite parent) {
		playField = new Composite(parent, SWT.SHADOW_NONE);
		playField.setLayout(new GridLayout());																	// true
		playField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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

	private void createFieldKiller(Composite parent) {
		Composite playField = new Composite(parent, SWT.NONE);
		GridData gd_playField = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_playField.widthHint = gd_playField.heightHint = 600;
		playField.setLayoutData(gd_playField);
		GridLayout layout = new GridLayout(9, false);
		layout.verticalSpacing = layout.horizontalSpacing = 0;
		playField.setLayout(layout);

		Map<Composite, Point> compositeBoxesKiller = new HashMap<Composite, Point>();
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				labelCellKiller[i][j] = new Composite(playField, SWT.NONE);
				compositeBoxesKiller.put(labelCellKiller[i][j], new Point(i, j));
				labelCellKiller[i][j].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				labelCellKiller[i][j].setBackground(ColorService.WHITE);
				labelCellKiller[i][j].addListener(SWT.MouseDown, new Listener() {

					@Override
					public void handleEvent(Event event) {
						Composite composite = (Composite) event.widget;
						Point point = compositeBoxesKiller.get(composite);
						if (!solveMode) {
							if (!loadedKiller) {
								//This checks if the point is already in use by another area.
								//This prevents adding a composite to two areas.
								for (Area area : areas) {
									if (area.pointUsed(point)) {
										return;
									}
								}
								//Checks if the composite is already selected (red).
								if (selected.contains(point)) {
									composite.setBackground(ColorService.WHITE);
									boardTextKiller[point.x][point.y].setBackground(ColorService.WHITE);
									selected.remove(point);
								} else {
									if (adjacent(point)) {
										composite.setBackground(ColorService.RED);
										boardTextKiller[point.x][point.y].setBackground(ColorService.RED);
										selected.add(point);
									}
								}
							} else {
								boardTextKiller[point.x][point.y].setFocus();
							}
						} else {
							boardTextKiller[point.x][point.y].setFocus();
						}
					}

				});

				final int f_i = i, f_j = j; // Final variables allow access in listener class
				labelCellKiller[i][j].addPaintListener(new PaintListener() {
					@Override
					public void paintControl(PaintEvent e) {
						Rectangle a = ((Composite) e.getSource()).getClientArea();
						if (f_i + 1 != 9 && f_j + 1 != 9) {
							e.gc.drawRectangle(0, 0, a.width, a.height);
						} else if (f_i + 1 != 9) {
							e.gc.drawRectangle(0, 0, a.width - 1, a.height);
						} else if (f_j + 1 != 9) {
							e.gc.drawRectangle(0, 0, a.width, a.height - 1);
						} else {
							e.gc.drawRectangle(0, 0, a.width - 1, a.height - 1);
						}

						if ((f_j + 1) % 3 == 0 && (f_j + 1) != 9 && boxRule) {
							e.gc.drawLine(a.width - 1, a.height - 1, a.width - 1, 0);
						}
						if ((f_i + 1) % 3 == 0 && (f_i + 1) != 9 && boxRule) {
							e.gc.drawLine(a.width - 1, a.height - 1, 0, a.height - 1);
						}
					}
				});

				labelCellKiller[i][j].addPaintListener(new PaintListener() {
					@Override
					public void paintControl(PaintEvent e) {
						Font tempFont = e.gc.getFont();
						e.gc.setFont(FontService.getTinyFont());
						e.gc.setForeground(ColorService.RED);
						Point point = compositeBoxesKiller.get(e.widget);
						Rectangle a = ((Composite) e.getSource()).getClientArea();
						if (leftLine(point))
							e.gc.drawLine(2, 2, 2, a.height - 3);
						if (topLine(point))
							e.gc.drawLine(2, 2, a.width - 3, 2);
						if (topLabel(point))
							e.gc.drawString(topLabelValue(point), 1, 1);
						if (bottomLine(point))
							e.gc.drawLine(2, a.height - 3, a.width - 3, a.height - 3);
						if (rightLine(point))
							e.gc.drawLine(a.width - 3, 2, a.width - 3, a.height - 3);
						e.gc.setFont(tempFont);
						e.gc.setForeground(ColorService.BLACK);
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
				inputBoxesKiller.put(boardTextKiller[i][j], new Point(i, j));
				for (int k = 4; k < 8; k++) {
					boardLabelsKiller[i][j][k] = createLabelKiller(labelCellKiller[i][j]);
				}
				if (boardKiller[i][j] != 0) {
					boardTextKiller[i][j].setText(Integer.toString(boardKiller[i][j]));
				} else {
					if (possibleKiller.get(i).get(j).size() < 8) {
						for (int k = 0; k < possibleKiller.get(i).get(j).size(); k++) {
							boardLabelsKiller[i][j][k + 1]
									.setText(Integer.toString(possibleKiller.get(i).get(j).get(k)));
							boardLabelsKiller[i][j][k + 1].setBackground(ColorService.WHITE);
						}
					}
				}
			}
		}
	}
	
	private Text createTextKiller(Composite parent) {
		Text input = new Text(parent, SWT.CENTER);
		input.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		input.setTextLimit(1);
		input.setFont(FontService.getSmallFont());
		input.addListener(SWT.Verify, new Listener() {
			@Override
			public void handleEvent(Event e) {
				//Validate the input
				String input = e.text;
				Text textbox = (Text) e.widget;
				if (input.length() == 0 && !loading && !solving) {
					updateBoardDataWithUserInputKiller(textbox, input);
				}
				if (!solved && !loading && !solving) {
					char[] chars = new char[input.length()];
					input.getChars(0, chars.length, chars, 0);
					Point point = inputBoxesKiller.get(textbox);
					for (int i = 0; i < chars.length; i++) {
						if (!('1' <= chars[i] && chars[i] <= '9')
								|| possibleKiller.get(point.x).get(point.y).indexOf(Integer.parseInt(input)) == -1
								|| createsZeroPossible(new Point(point.x, point.y), Integer.parseInt(input))) {
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
	
	private boolean createsZeroPossible(Point point, int input) {
		boolean returnValue = false;
		Vector<Point> affectedPointsH = new Vector<Point>();
		Vector<Point> affectedPointsV = new Vector<Point>();
		Vector<Point> affectedPointsS = new Vector<Point>();

		affectedPointsH = new Vector<Point>();
		affectedPointsV = new Vector<Point>();
		affectedPointsS = new Vector<Point>();
		int x = 3 * (int) Math.floor(point.x / 3);
		int y = 3 * (int) Math.floor(point.y / 3);

		for (int i = 0; i < 9; i++) {
			if (point.y != i && possibleKiller.get(point.x).get(i).size() == 1
					&& possibleKiller.get(point.x).get(i).get(0) == input)
				returnValue = true;
			if (point.x != i && possibleKiller.get(i).get(point.y).size() == 1
					&& possibleKiller.get(i).get(point.y).get(0) == input)
				returnValue = true;
			if (point.y != i && boardKiller[point.x][i] == 0)
				affectedPointsH.add(new Point(point.x, i));
			if (point.x != i && boardKiller[i][point.y] == 0)
				affectedPointsV.add(new Point(i, point.y));
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if ((point.x != x + i || point.y != y + j) && possibleKiller.get(x + i).get(y + j).size() == 1
						&& possibleKiller.get(x + i).get(y + j).get(0) == input)
					returnValue = true;
				if ((point.x != x + i || point.y != y + j) && boardKiller[x + i][y + j] == 0)
					affectedPointsS.add(new Point(x + i, y + j));
			}
		}
		if (checkSubset(affectedPointsH, possibleKiller, input) || checkSubset(affectedPointsV, possibleKiller, input)
				|| checkSubset(affectedPointsS, possibleKiller, input))
			returnValue = true;

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
	
	private void showErroneousEntries() {
		if (backgroundSolved) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (boardKiller[i][j] != 0 && boardKiller[i][j] != tempBoard[i][j]) {
						labelCellKiller[i][j].setBackground(ColorService.RED);
						boardTextKiller[i][j].setBackground(ColorService.RED);
					}
				}
			}
		}
	}
	
	private boolean checkErroneousEntries() {
		if (backgroundSolved) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (boardKiller[i][j] != 0 && boardKiller[i][j] != tempBoard[i][j]) {
						return true;
					}
				}
			}
		}
		return false;
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
	
	/**
	 * Creates a Label in the given Composite.</br>
	 * White Background, Centered, tiny font, red foreground.
	 * @param parent
	 * @return
	 */
	private Label createLabelKiller(Composite parent) {
		final Label label = new Label(parent, SWT.NONE);
		label.setAlignment(SWT.CENTER);
		label.setBackground(ColorService.WHITE);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		label.setFont(FontService.getTinyFont());
		label.setForeground(ColorService.BLACK);
		return label;
	}
	
	private boolean rightLine(Point point) {
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
	
	private boolean bottomLine(Point point) {
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
	
	private String topLabelValue(Point point) {
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
	
	private boolean topLabel(Point point) {
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
	
	private boolean topLine(Point point) {
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
	
	private boolean leftLine(Point point) {
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
	
	private boolean adjacent(Point point) {
		if (selected.size() == 0) {
			return true;
		}
		for (int i = 0; i < selected.size(); i++) {
			if (Math.abs(selected.get(i).x - point.x) + Math.abs(selected.get(i).y - point.y) <= 1) {
				return true;
			}
		}
		return false;
	}

	private void createButtonArea(Group parent) {
		Composite mainComposite = new Composite(parent, SWT.SHADOW_NONE);
		mainComposite.setLayout(new GridLayout());
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
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
				
				restartButton.setEnabled(true);
				
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						if (boardKiller[i][j] > 0) {
							boardTextKiller[i][j].setEditable(false);
							boardTextKiller[i][j].setFont(FontService.getSmallBoldFont());
						}
						// Copy values in a new int[][]. Used for getting the initial state for 
						// the possibility of restarting a sudoku.
						originalSudoku[i][j] = boardKiller[i][j];
					}
				}
				
				// Copy the list with the areas into a new List
				// Also used for restarting a sudoku.
				originalAreas = new ArrayList<Area>(areas);
				
				movesKiller.clear();
				additionButton.setEnabled(false);
				subtractionButton.setEnabled(false);
				multiplicationButton.setEnabled(false);
				divisionButton.setEnabled(false);
				
				if (selected.size() > 0) {
					for (int i = 0; i < selected.size(); i++) {
						labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
						boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
					}
					selected.clear();
				}
				
				if (backgroundSolve.getState() != Job.RUNNING) {
					backgroundSolve.setSystem(true);
				}
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
				
				restartButton.setEnabled(false);
				
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						boardTextKiller[i][j].setEditable(true);
					}
				}
				
				additionButton.setEnabled(true);
				subtractionButton.setEnabled(true);
				multiplicationButton.setEnabled(true);
				divisionButton.setEnabled(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
				
			}
		});
		
		Group grpActionButtons = new Group(mainComposite, SWT.SHADOW_NONE);
		grpActionButtons.setText(Messages.SudokuComposite_ActionsAreaTitle);
		grpActionButtons.setLayout(new GridLayout());
		grpActionButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		loadStandardPuzzle = new Button(grpActionButtons, SWT.PUSH);
		loadStandardPuzzle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		loadStandardPuzzle.setText(Messages.SudokuComposite_LoadStandardPuzzle);
		loadStandardPuzzle.setToolTipText(Messages.SudokuComposite_LoadStandardPuzzle_Tooltip);
		loadStandardPuzzle.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				URL fileName = null;
				try {
					fileName = FileLocator.toFileURL((SudokuPlugin.getDefault().getBundle().getEntry("/")));
				} catch (IOException ex) {
					LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
				}
				StringBuilder path = new StringBuilder();
				path.append(fileName.getFile());
				path.append("data/");
				
				// data/killer2.sud is corrupted.
				path.append("killer1.sud");
				//load killer sudoku. If it fails jump out of the method.
				if (!loadKiller(path.toString())) {
					return;
				}
				
				refresh();
				
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						labelCellKiller[i][j].redraw();
					}
				}

				enterModeButton.setSelection(false);
				solveModeButton.setSelection(true);
				solveModeButton.notifyListeners(SWT.Selection, null);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		loadButton = new Button(grpActionButtons, SWT.PUSH);
		loadButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		loadButton.setEnabled(true);
		loadButton.setText(Messages.SudokuComposite_LoadButton);
		loadButton.setToolTipText(Messages.SudokuComposite_LoadButton_Tooltip);
		loadButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadPuzzleKiller();
				refresh();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		Group grpOptionButtons = new Group(mainComposite, SWT.SHADOW_NONE);
		grpOptionButtons.setText(Messages.SudokuComposite_optionsAreaTitle);
		grpOptionButtons.setLayout(new GridLayout());
		grpOptionButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		

		solveButton = new Button(grpOptionButtons, SWT.PUSH);
		solveButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		solveButton.setEnabled(false);
		solveButton.setText(Messages.SudokuComposite_SolveButton);
		solveButton.setToolTipText(Messages.SudokuComposite_SolveButton_Tooltip);
		solveButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				dummyJob.setUser(true);
				dummyJob.schedule();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
				
			}
		});
		
		hintButton = new Button(grpOptionButtons, SWT.PUSH);
		hintButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		hintButton.setEnabled(false);
		hintButton.setText(Messages.SudokuComposite_HintButton);
		hintButton.setToolTipText(Messages.SudokuComposite_HintButton_Tooltip);
		hintButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (backgroundSolved && getEmptySquare(boardKiller) != null) {
					Point square = new Point(rnd.nextInt(9), rnd.nextInt(9));
					while (boardKiller[square.x][square.y] > 0)
						square = new Point(rnd.nextInt(9), rnd.nextInt(9));
					boardKiller[square.x][square.y] = tempBoard[square.x][square.y];
					for (int k = 0; k < 8; k++)
						boardLabelsKiller[square.x][square.y][k].setText("");
					boardTextKiller[square.x][square.y].setText(Integer.toString(boardKiller[square.x][square.y]));
					startBlinkingArea(square.x, square.y);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {	
				
			}
		});
		
		undoButton = new Button(grpOptionButtons, SWT.PUSH);
		undoButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		undoButton.setEnabled(false);
		undoButton.setText(Messages.SudokuComposite_UndoButton);
		undoButton.setToolTipText(Messages.SudokuComposite_UndoButton_Tooltip);
		undoButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (movesKiller.size() > 0) {
					Point pt = movesKiller.get(movesKiller.size() - 1);
					movesKiller.remove(movesKiller.size() - 1);
					boardTextKiller[pt.x][pt.y].setText("");
					updateBoardDataWithUserInputKiller(boardTextKiller[pt.x][pt.y], "");
					if (movesKiller.size() == 0) {
						undoButton.setEnabled(false);
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
//		boxRuleButton = new Button(grpOptionButtons, SWT.PUSH);
//		boxRuleButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//		boxRuleButton.setBackground(ColorService.GREEN);
//		boxRuleButton.setEnabled(true);
//		boxRuleButton.setBackground(ColorService.RED);
//		boxRuleButton.setText(Messages.SudokuComposite_BoxRuleButton);
//		boxRuleButton.setToolTipText(Messages.SudokuComposite_BoxRuleButton_Tooltip);
//		boxRuleButton.addSelectionListener(new SelectionListener() {
//			
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if (boxRule) {
//					boxRule = false;
//					boxRuleButton.setBackground(ColorService.RED);
//				} else {
//					boxRule = true;
//					boxRuleButton.setBackground(ColorService.GREEN);
//				}
//				refresh();
//			}
//			
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//				
//			}
//		});
		
		showPossibleButton = new Button(grpOptionButtons, SWT.PUSH);
		showPossibleButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		showPossibleButton.setEnabled(false);
		showPossibleButton.setBackground(ColorService.GREEN);
		showPossibleButton.setText(Messages.SudokuComposite_ShowPossibleButton);
		showPossibleButton.setToolTipText(Messages.SudokuComposite_ShowPossibleButton_Tooltip);
		showPossibleButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (showPossible) {
					showPossible = false;
					showPossibleButton.setBackground(ColorService.RED);
				} else {
					showPossible = true;
					showPossibleButton.setBackground(ColorService.GREEN);
				}
				
				updatePossibilitiesKiller(boardKiller, possibleKiller);
				refresh();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		autoFillOneButton = new Button(grpOptionButtons, SWT.PUSH);
		autoFillOneButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		autoFillOneButton.setEnabled(false);
		autoFillOneButton.setText(Messages.SudokuComposite_AutoFillOneButton);
		autoFillOneButton.setToolTipText(Messages.SudokuComposite_AutoFillOneButton_Tooltip);
		autoFillOneButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				fillOneKiller();
				refresh();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		saveButton = new Button(grpOptionButtons, SWT.PUSH);
		saveButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		saveButton.setEnabled(true);
		saveButton.setText(Messages.SudokuComposite_SaveButton);
		saveButton.setToolTipText(Messages.SudokuComposite_SaveButton_Tooltip);
		saveButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				savePuzzleKiller();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		restartButton = new Button(grpOptionButtons, SWT.PUSH);
		restartButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		restartButton.setEnabled(false);
		restartButton.setText(Messages.SudokuComposite_ClearButton);
		restartButton.setToolTipText(Messages.SudokuComposite_ClearButton_Tooltip);
		restartButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				reset();
				
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						boardKiller[i][j] = originalSudoku[i][j];
						if (originalSudoku[i][j] != 0) {
							boardTextKiller[i][j].setText(Integer.toString(originalSudoku[i][j]));
						}
					}
				}
				
				areas.clear();
				areas = new ArrayList<>(originalAreas);
				originalAreas.clear();
				updateInitialPossibilitiesKiller();
				updatePossibilitiesKiller(boardKiller, possibleKiller);
				solveModeButton.notifyListeners(SWT.Selection, new Event());
				refresh();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		Group grpOperatorsButtons = new Group(mainComposite, SWT.SHADOW_NONE);
		grpOperatorsButtons.setText(Messages.SudokuComposite_OperatorsAreaTitle);
		grpOperatorsButtons.setLayout(new GridLayout());
		grpOperatorsButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		additionButton = new Button(grpOperatorsButtons, SWT.PUSH);
		additionButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		additionButton.setEnabled(true);
		additionButton.setText(Messages.SudokuComposite_AdditionButton);
		additionButton.setToolTipText(Messages.SudokuComposite_AdditionButton_Tooltip);
		additionButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (selected.size() > 0) {
					InputDialog dlg = new InputDialog(getShell(), "Value Input", "Input value",
							"", (new IInputValidator() {
								private final Pattern INTEGER_PATTERN = Pattern.compile("[0-9]+");

								@Override
								public String isValid(String newText) {
									String toReturn = INTEGER_PATTERN.matcher(newText).matches() ? null
											: (newText.length() == 0) ? "Please enter an integer."
													: "'" + newText + "' is not a valid integer.";
									return toReturn;
								}
							}));
					if (dlg.open() == Window.OK) {
						int value = Integer.parseInt(dlg.getValue());
						areas.add(new Area(ADDITION, selected, value));
					}
					for (int i = 0; i < selected.size(); i++) {
						labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
						boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
					}
					selected.clear();
					updateInitialPossibilitiesKiller();
					updatePossibilitiesKiller(boardKiller, possibleKiller);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		subtractionButton = new Button(grpOperatorsButtons, SWT.PUSH);
		subtractionButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		subtractionButton.setEnabled(true);
		subtractionButton.setText(Messages.SudokuComposite_SubtractionButton);
		subtractionButton.setToolTipText(Messages.SudokuComposite_SubtractionButton_Tooltip);
		subtractionButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selected.size() > 0) {
					if (selected.size() == 2) {
						InputDialog dlg = new InputDialog(getShell(), "Value Input",
								"Input value", "", (new IInputValidator() {
									private final Pattern INTEGER_PATTERN = Pattern.compile("[0-9]+");

									@Override
									public String isValid(String newText) {
										String toReturn = INTEGER_PATTERN.matcher(newText).matches() ? null
												: (newText.length() == 0) ? "Please enter an integer."
														: "'" + newText + "' is not a valid integer.";
										return toReturn;
									}
								}));
						if (dlg.open() == Window.OK) {
							int value = Integer.parseInt(dlg.getValue());
							areas.add(new Area(SUBTRACTION, selected, value));
						}
					}
					for (int i = 0; i < selected.size(); i++) {
						labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
						boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
					}
					selected.clear();
					updateInitialPossibilitiesKiller();
					updatePossibilitiesKiller(boardKiller, possibleKiller);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		multiplicationButton = new Button(grpOperatorsButtons, SWT.PUSH);
		multiplicationButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		multiplicationButton.setEnabled(true);
		multiplicationButton.setText(Messages.SudokuComposite_MultiplicationButton);
		multiplicationButton.setToolTipText(Messages.SudokuComposite_MultiplicationButton_Tooltip);
		multiplicationButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selected.size() > 0) {
					InputDialog dlg = new InputDialog(getShell(), "Value Input", "Input value",
							"", (new IInputValidator() {
								private final Pattern INTEGER_PATTERN = Pattern.compile("[0-9]+");

								@Override
								public String isValid(String newText) {
									String toReturn = INTEGER_PATTERN.matcher(newText).matches() ? null
											: (newText.length() == 0) ? "Please enter an integer."
													: "'" + newText + "' is not a valid integer.";
									return toReturn;
								}
							}));
					if (dlg.open() == Window.OK) {
						int value = Integer.parseInt(dlg.getValue());
						areas.add(new Area(MULTIPLICATION, selected, value));
					}
					for (int i = 0; i < selected.size(); i++) {
						labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
						boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
					}
					selected.clear();
					updateInitialPossibilitiesKiller();
					updatePossibilitiesKiller(boardKiller, possibleKiller);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		divisionButton = new Button(grpOperatorsButtons, SWT.PUSH);
		divisionButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		divisionButton.setEnabled(true);
		divisionButton.setText(Messages.SudokuComposite_DivisionButton);
		divisionButton.setToolTipText(Messages.SudokuComposite_DivisionButton_Tooltip);
		divisionButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selected.size() > 0) {
					if (selected.size() == 2) {
						InputDialog dlg = new InputDialog(getShell(), "Value Input",
								"Input value", "", (new IInputValidator() {
									private final Pattern INTEGER_PATTERN = Pattern.compile("[0-9]+");

									@Override
									public String isValid(String newText) {
										String toReturn = INTEGER_PATTERN.matcher(newText).matches() ? null
												: (newText.length() == 0) ? "Please enter an integer."
														: "'" + newText + "' is not a valid integer.";
										return toReturn;
									}
								}));
						if (dlg.open() == Window.OK) {
							int value = Integer.parseInt(dlg.getValue());
							areas.add(new Area(DIVISION, selected, value));
						}
					}
					for (int i = 0; i < selected.size(); i++) {
						labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
						boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
					}
					selected.clear();
					updateInitialPossibilitiesKiller();
					updatePossibilitiesKiller(boardKiller, possibleKiller);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
	}

	/**
	 * Resets the current puzzle
	 */
	protected void reset() {
		backgroundSolve.cancel();
		dummyJob.cancel();
		
		enterModeButton.setSelection(true);
		solveModeButton.setSelection(false);
		enterModeButton.notifyListeners(SWT.Selection, null);
		backgroundSolve.cancel();
		undoButton.setEnabled(false);
		
		loading = true;
		clearPuzzleKiller();
		loadedKiller = false;
		
		additionButton.setEnabled(true);
		subtractionButton.setEnabled(true);
		multiplicationButton.setEnabled(true);
		divisionButton.setEnabled(true);
		restartButton.setEnabled(false);
		
		loading = false;
		refresh();
	}

	protected boolean savePuzzleKiller() {
		String fileName = openFileDialog(SWT.SAVE);
		FileOutputStream out = null;

		if (fileName == null) {
			return false;
		}

		Map<Integer, String> operatorMap = new HashMap<Integer, String>();
		operatorMap.put(ADDITION, "+");
		operatorMap.put(SUBTRACTION, "-");
		operatorMap.put(MULTIPLICATION, "*");
		operatorMap.put(DIVISION, ":");
		Map<Integer, String> areaName = new HashMap<Integer, String>();
		for (int i = 0; i < areas.size(); i++) {
			areaName.put(i, Character.toString((char) (i + 58)));
		}
		
		try {
			out = new FileOutputStream(fileName);
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					for (int k = 0; k < areas.size(); k++) {
						if (areas.get(k).pointUsed(new Point(i, j))) {
							out.write((areaName.get(k)).getBytes());
							break;
						}
					}
				}
				out.write('\n');
			}
			for (int i = 0; i < areas.size(); i++) {
				out.write((areaName.get(i) + operatorMap.get(areas.get(i).getOperator())
						+ Integer.toString(areas.get(i).getValue()) + "\n").getBytes());
			}
		} catch (FileNotFoundException e) {
			printFileNotFoundExceptionDialogSaving(e);
			return false;
		} catch (IOException e) {
			printIOExceptionDialogSaving(e);
			return false;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				LogUtil.logError(SudokuPlugin.PLUGIN_ID, e);
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

	protected void loadPuzzleKiller() {
		String fileName = openFileDialog(SWT.OPEN);

		if (fileName == null) {
			return;
		}

		loadKiller(fileName);
	}

	private String openFileDialog(int type) {
		FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), type);
		dialog.setFilterPath(DirectoryService.getUserHomeDir());
		dialog.setFilterExtensions(new String[] { "*.sud" });
		dialog.setFilterNames(new String[] { "Sudoku Files (*.sud)" });
		dialog.setOverwrite(true);
		return dialog.open();
	}

	/**
	 * Loads a killer sudoku from a file.
	 * @param fileName The file which contains the killer sudoku values.
	 * @return True if file is correctly loaded. False if an error occurred and the file is not loaded.
	 */
	protected boolean loadKiller(String fileName) {
		solved = false;
		killerFirstPossible = false;

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			loading = true;
			clearPuzzleKiller();
			Map<String, Integer> areaName = new HashMap<String, Integer>();
			String line = null;
			int ida = 0;
			String val;

			// Read first nine lines of the file.
			for (int i = 0; i < 9; i++) {
				line = reader.readLine();
				for (int j = 0; j < 9; j++) {
					val = Character.toString(line.charAt(j));
					// Checks if the the area of the value is already in the hashmap.
					// If not it adds it to the hashmap.
					if (areaName.get(val) == null) {
						areaName.put(val, ida);
						ida++;
					}
					if (areaName.get(val) > areas.size() - 1) {
						areas.add(new Area());
					}
					areas.get(areaName.get(val)).addPoint(new Point(i, j));
				}
			}
			int area;
			while ((line = reader.readLine()) != null) {
				val = Character.toString(line.charAt(0));
				area = areaName.get(val);

				val = Character.toString(line.charAt(1));
				if (val.equals("+")) {
					areas.get(area).setOperator(ADDITION);
				} else if (val.equals("-")) {
					areas.get(area).setOperator(SUBTRACTION);
				} else if (val.equals("*")) {
					areas.get(area).setOperator(MULTIPLICATION);
				} else if (val.equals(":")) {
					areas.get(area).setOperator(DIVISION);
				} else {
					MessageBox fileNotFound = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
					fileNotFound.setText("Invalid Operator in loaded file");
					fileNotFound.setMessage(
							"An invalid operator was found in the loaded file. Operator must be \"+\", \"-\", \"*\", \":\".");
					fileNotFound.open();
					return false;
				}
				areas.get(area).setValue(Integer.parseInt(line.substring(2)));
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

		updateInitialPossibilitiesKiller();
		updatePossibilitiesKiller(boardKiller, possibleKiller);

		loadedKiller = true;
		additionButton.setEnabled(false);
		subtractionButton.setEnabled(false);
		multiplicationButton.setEnabled(false);
		divisionButton.setEnabled(false);
		return true;
	}
	
	private void updateInitialPossibilitiesKiller() {
		for (int i = 0; i < areas.size(); i++) {
			List<Point> tempList = areas.get(i).getList();
			List<Integer> possible = new ArrayList<Integer>();
			for (int j = 0; j < tempList.size(); j++) {
				for (int k = 0; k < possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size(); k++) {
					if (possible.indexOf(possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k)) == -1) {
						possible.add(possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k));
					}
				}
			}
			switch (areas.get(i).getOperator()) {
			case ADDITION: {
				List<Integer> allowed = generateAllowedValuesKiller(areas.get(i).getList().size(),
						areas.get(i).getValue(), ADDITION, sameLineKiller(tempList), possible);
				for (int j = 0; j < tempList.size(); j++) {
					if (boardKiller[tempList.get(j).x][tempList.get(j).y] == 0) {
						for (int k = possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size()
								- 1; k >= 0; k--) {
							if (allowed.indexOf(
									possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k)) == -1) {
								possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).remove(k);
							}
						}
					}
				}
			}
				break;
			case SUBTRACTION: {
				if (boardKiller[tempList.get(0).x][tempList.get(0).y] == 0
						&& boardKiller[tempList.get(1).x][tempList.get(1).y] != 0) {
					int result = (boardKiller[tempList.get(1).x][tempList.get(1).y] > areas.get(i).getValue())
							? boardKiller[tempList.get(1).x][tempList.get(1).y] - areas.get(i).getValue()
							: areas.get(i).getValue() - boardKiller[tempList.get(1).x][tempList.get(1).y];
					if (possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).indexOf(result) != -1) {
						possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).clear();
						possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).add(result);
					}
				} else if (boardKiller[tempList.get(0).x][tempList.get(0).y] != 0
						&& boardKiller[tempList.get(1).x][tempList.get(1).y] == 0) {
					int result = (boardKiller[tempList.get(0).x][tempList.get(0).y] > areas.get(i).getValue())
							? boardKiller[tempList.get(0).x][tempList.get(0).y] - areas.get(i).getValue()
							: areas.get(i).getValue() - boardKiller[tempList.get(0).x][tempList.get(0).y];
					if (possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).indexOf(result) != -1) {
						possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).clear();
						possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).add(result);
					}
				}
			}
				break;
			case MULTIPLICATION: {
				List<Integer> allowed = generateAllowedValuesKiller(areas.get(i).getList().size(),
						areas.get(i).getValue(), MULTIPLICATION, sameLineKiller(tempList), possible);
				for (int j = 0; j < tempList.size(); j++) {
					if (boardKiller[tempList.get(j).x][tempList.get(j).y] == 0) {
						for (int k = possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size()
								- 1; k >= 0; k--) {
							if (allowed.indexOf(
									possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k)) == -1) {
								possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).remove(k);
							}
						}
					}
				}
			}
				break;
			case DIVISION: {
				List<Integer> allowed = generateAllowedValuesKiller(areas.get(i).getList().size(),
						areas.get(i).getValue(), DIVISION, sameLineKiller(tempList), possible);
				if (boardKiller[tempList.get(0).x][tempList.get(0).y] == 0
						&& boardKiller[tempList.get(1).x][tempList.get(1).y] != 0) {
					int result = (boardKiller[tempList.get(1).x][tempList.get(1).y] > areas.get(i).getValue())
							? boardKiller[tempList.get(1).x][tempList.get(1).y] / areas.get(i).getValue()
							: areas.get(i).getValue() * boardKiller[tempList.get(1).x][tempList.get(1).y];
					if (possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).indexOf(result) != -1) {
						possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).clear();
						possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).add(result);
					}
				} else if (boardKiller[tempList.get(0).x][tempList.get(0).y] != 0
						&& boardKiller[tempList.get(1).x][tempList.get(1).y] == 0) {
					int result = (boardKiller[tempList.get(0).x][tempList.get(0).y] > areas.get(i).getValue())
							? boardKiller[tempList.get(0).x][tempList.get(0).y] / areas.get(i).getValue()
							: areas.get(i).getValue() * boardKiller[tempList.get(0).x][tempList.get(0).y];
					if (possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).indexOf(result) != -1) {
						possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).clear();
						possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).add(result);
					}
				} else {
					for (int j = possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).size() - 1; j >= 0; j--) {
						if (allowed.indexOf(possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).get(j)) == -1)
							possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).remove(j);
					}
					for (int j = possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).size() - 1; j >= 0; j--) {
						if (allowed.indexOf(possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).get(j)) == -1)
							possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).remove(j);
					}
				}
			}
				break;
			}
		}
	}

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

	/**
	 * Clears the playfield, that means all values are removed from the gui.
	 * The background is set to white and some other things.
	 */
	private void clearPuzzleKiller() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				boardKiller[i][j] = 0;
				boardTextKiller[i][j].setText("");
				labelCellKiller[i][j].setBackground(ColorService.WHITE);

				for (int k = 0; k < 8; k++) {
					boardLabelsKiller[i][j][k].setText("");
				}
				possibleKiller.get(i).get(j).clear();
				for (int k = 1; k <= 9; k++) {
					possibleKiller.get(i).get(j).add(k);
				}
			}
		}
		
		//This is used to remove the red lines painted by the paint listener.
		getDisplay().getActiveShell().layout(true, true);
		
		areas.clear();
		for (int i = 0; i < selected.size(); i++) {
			labelCellKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
			boardTextKiller[selected.get(i).x][selected.get(i).y].setBackground(ColorService.WHITE);
		}
		selected.clear();
	}

	/**
	 * Fills a field in which only one value is possible.
	 */
	protected void fillOneKiller() {
		boolean changed = false;
		for (int i = 0; i < 9 & !changed; i++) {
			for (int j = 0; j < 9 & !changed; j++) {
				if (possibleKiller.get(i).get(j).size() == 1) {
					boardKiller[i][j] = possibleKiller.get(i).get(j).get(0);
					boardTextKiller[i][j].setText(Integer.toString(boardKiller[i][j]));
					labelCellKiller[i][j].layout();
					startBlinkingArea(i, j);
					changed = true;
				}
			}
		}
	}

	protected void refresh() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (selected.contains(new Point(i, j))) {
					labelCellKiller[i][j].setBackground(ColorService.RED);
				} else {
					labelCellKiller[i][j].setBackground(ColorService.WHITE);
				}
			}
		}
	}

	protected void updateBoardDataWithUserInputKiller(Text inputBox, String inputStr) {
		solved = false;
		Point point = inputBoxesKiller.get(inputBox);
		int num = 0;
		if (inputStr.length() > 0) {
			num = Integer.parseInt(inputStr);
			Point pt = new Point(point.x, point.y);
			movesKiller.add(pt);
			undoButton.setEnabled(true);
		}
		if (num == 0 && boardKiller[point.x][point.y] != 0) {
			addPossibleKiller(point.x, point.y, boardKiller[point.x][point.y]);
		}
		boardKiller[point.x][point.y] = num;
		labelCellKiller[point.x][point.y].setBackground(ColorService.WHITE);
		boardTextKiller[point.x][point.y].setBackground(ColorService.WHITE);
		updatePossibilitiesKiller(boardKiller, possibleKiller);

	}

	private void updatePossibilitiesKiller(int[][] board, List<List<List<Integer>>> posibilities) {
		boolean changed = false;
		List<Integer> used;
		int idx;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] != 0) {
					posibilities.get(i).get(j).clear();
				}
			}
		}
		if (killerFirstPossible == false) {
			killerFirstPossible = true;
			for (int i = 0; i < areas.size(); i++) {
				List<Point> tempList = areas.get(i).getList();
				List<Integer> possible = new ArrayList<Integer>();
				for (int j = 0; j < tempList.size(); j++) {
					for (int k = 0; k < possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size(); k++) {
						if (possible
								.indexOf(possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k)) == -1) {
							possible.add(possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k));
						}
					}
				}
				switch (areas.get(i).getOperator()) {
				case ADDITION: {
					int tempSum = 0, tempSet = 0;
					for (int j = 0; j < tempList.size(); j++) {
						if (boardKiller[tempList.get(j).x][tempList.get(j).y] != 0) {
							tempSet++;
							tempSum = tempSum + boardKiller[tempList.get(j).x][tempList.get(j).y];
						}
					}
					List<Integer> allowed = generateAllowedValuesKiller(areas.get(i).getList().size() - tempSet,
							areas.get(i).getValue() - tempSum, ADDITION, sameLineKiller(tempList), possible);
					for (int j = 0; j < tempList.size(); j++) {
						if (boardKiller[tempList.get(j).x][tempList.get(j).y] == 0) {
							for (int k = possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size()
									- 1; k >= 0; k--) {
								if (allowed.indexOf(
										possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k)) == -1) {
									possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).remove(k);
								}
							}
						}
					}
				}
					break;
				case SUBTRACTION: {
					if (boardKiller[tempList.get(0).x][tempList.get(0).y] == 0
							&& boardKiller[tempList.get(1).x][tempList.get(1).y] != 0) {
						int result = (boardKiller[tempList.get(1).x][tempList.get(1).y] > areas.get(i).getValue())
								? boardKiller[tempList.get(1).x][tempList.get(1).y] - areas.get(i).getValue()
								: areas.get(i).getValue() - boardKiller[tempList.get(1).x][tempList.get(1).y];
						if (possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).indexOf(result) != -1) {
							possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).clear();
							possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).add(result);
						}
					} else if (boardKiller[tempList.get(0).x][tempList.get(0).y] != 0
							&& boardKiller[tempList.get(1).x][tempList.get(1).y] == 0) {
						int result = (boardKiller[tempList.get(0).x][tempList.get(0).y] > areas.get(i).getValue())
								? boardKiller[tempList.get(0).x][tempList.get(0).y] - areas.get(i).getValue()
								: areas.get(i).getValue() - boardKiller[tempList.get(0).x][tempList.get(0).y];
						if (possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).indexOf(result) != -1) {
							possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).clear();
							possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).add(result);
						}
					}
				}
					break;
				case MULTIPLICATION: {
					int tempProd = 1, tempSet = 0;
					for (int j = 0; j < tempList.size(); j++) {
						if (boardKiller[tempList.get(j).x][tempList.get(j).y] != 0) {
							tempSet++;
							tempProd = tempProd * boardKiller[tempList.get(j).x][tempList.get(j).y];
						}
					}
					List<Integer> allowed = generateAllowedValuesKiller(areas.get(i).getList().size() - tempSet,
							areas.get(i).getValue() / tempProd, MULTIPLICATION, sameLineKiller(tempList), possible);
					for (int j = 0; j < tempList.size(); j++) {
						if (boardKiller[tempList.get(j).x][tempList.get(j).y] == 0) {
							for (int k = possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).size()
									- 1; k >= 0; k--) {
								if (allowed.indexOf(
										possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).get(k)) == -1) {
									possibleKiller.get(tempList.get(j).x).get(tempList.get(j).y).remove(k);
								}
							}
						}
					}
				}
					break;
				case DIVISION: {
					List<Integer> allowed = generateAllowedValuesKiller(areas.get(i).getList().size(),
							areas.get(i).getValue(), DIVISION, sameLineKiller(tempList), possible);
					if (boardKiller[tempList.get(0).x][tempList.get(0).y] == 0
							&& boardKiller[tempList.get(1).x][tempList.get(1).y] != 0) {
						int result = (boardKiller[tempList.get(1).x][tempList.get(1).y] > areas.get(i).getValue())
								? boardKiller[tempList.get(1).x][tempList.get(1).y] / areas.get(i).getValue()
								: areas.get(i).getValue() * boardKiller[tempList.get(1).x][tempList.get(1).y];
						if (possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).indexOf(result) != -1) {
							possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).clear();
							possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).add(result);
						}
					} else if (boardKiller[tempList.get(0).x][tempList.get(0).y] != 0
							&& boardKiller[tempList.get(1).x][tempList.get(1).y] == 0) {
						int result = (boardKiller[tempList.get(0).x][tempList.get(0).y] > areas.get(i).getValue())
								? boardKiller[tempList.get(0).x][tempList.get(0).y] / areas.get(i).getValue()
								: areas.get(i).getValue() * boardKiller[tempList.get(0).x][tempList.get(0).y];
						if (possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).indexOf(result) != -1) {
							possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).clear();
							possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).add(result);
						}
					} else {
						for (int j = possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).size()
								- 1; j >= 0; j--) {
							if (allowed
									.indexOf(possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).get(j)) == -1)
								possibleKiller.get(tempList.get(0).x).get(tempList.get(0).y).remove(j);
						}
						for (int j = possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).size()
								- 1; j >= 0; j--) {
							if (allowed
									.indexOf(possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).get(j)) == -1)
								possibleKiller.get(tempList.get(1).x).get(tempList.get(1).y).remove(j);
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
							if (idx != -1) {
								possibleKiller.get(i).get(k).remove(idx);
							}
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
			for (int j = 0; j < 9; j++) {
				if (boardKiller[j][i] != 0) {
					used.add(boardKiller[j][i]);
				}
			}
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
						boardLabelsKiller[i][j][k + 1].setText(Integer.toString(possibleKiller.get(i).get(j).get(k)));
						boardLabelsKiller[i][j][k + 1].setBackground(ColorService.WHITE);
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
		if (changed) {
			updatePossibilitiesKiller(boardKiller, possibleKiller);
		}
	}

	private List<Integer> generateAllowedValuesKiller(int numberOfPoints, int value, int operator, boolean sameLine,
			List<Integer> possible) {
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

	private void generateSets(Vector<Integer[]> subsets, int[] set, int[] subset, int subsetSize, int nextIndex) {
		if (subsetSize == subset.length) {
			Integer[] temp = new Integer[subset.length];
			for (int i = 0; i < subset.length; i++)
				temp[i] = subset[i];
			subsets.add(temp);
		} else {
			for (int j = nextIndex; j < set.length; j++) {
				subset[subsetSize] = set[j];
				generateSets(subsets, set, subset, subsetSize + 1, j);
			}
		}
	}

	private void generateSubsets(Vector<Integer[]> subsets, int[] set, int[] subset, int subsetSize, int nextIndex) {
		if (subsetSize == subset.length) {
			Integer[] temp = new Integer[subset.length];
			for (int i = 0; i < subset.length; i++)
				temp[i] = subset[i];
			subsets.add(temp);
		} else {
			for (int j = nextIndex; j < set.length; j++) {
				subset[subsetSize] = set[j];
				generateSubsets(subsets, set, subset, subsetSize + 1, j + 1);
			}
		}
	}

	private boolean sameLineKiller(List<Point> points) {
		boolean horizontal = true;
		boolean vertical = true;
		boolean box = true;
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

	private void addPossibleKiller(int x, int y, int val) {
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

	protected void startBlinkingArea(int x, int y) {
		Thread blinkerRed = new Thread() {
			@Override
			public void run() {
				labelCellKiller[x][y].setBackground(ColorService.RED);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					LogUtil.logError(SudokuPlugin.PLUGIN_ID, e);
				}
			}
		};
		
		Thread blinkerWhite = new Thread() {
			@Override
			public void run() {
				labelCellKiller[x][y].setBackground(ColorService.WHITE);
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

	/**
	 * Searchs for an empty field in the board.
	 * @param board
	 * @return
	 */
	protected Point getEmptySquare(int[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 0) {
					return new Point(i, j);
				}
			}
		}
		return null;
	}

	/**
	 * Creates the part of the GUI in which the title and description are.
	 * @param killerPuzzle The parent Composite
	 */
	private void createHead(KillerPuzzle killerPuzzle) {
		Composite headComposite = new Composite(this, SWT.NONE);
		GridData gd_headComposite = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_headComposite.minimumWidth = 300;
		gd_headComposite.widthHint = 300;
		headComposite.setLayoutData(gd_headComposite);
		headComposite.setLayout(new GridLayout());
		
		Text title = new Text(headComposite, SWT.READ_ONLY);
		title.setFont(FontService.getHeaderFont());
		title.setBackground(ColorService.WHITE);
		title.setText(Messages.SudokuComposite_Killer_Title);
		
		Text stDescription = new Text(headComposite, SWT.READ_ONLY | SWT.WRAP);
		stDescription.setText(Messages.SudokuComposite_Killer_Desc);
		stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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
import org.jcryptool.games.sudoku.views.SudokuComposite.UserInputPoint;

/**
 * GUI and Logic for the normal sudoku.<br>
 * Externalized Code from SudokuComposite.
 * @author Thorben Groos 
 *
 */
public class NormalPuzzle extends Composite {

	private Button solveModeButton;
	private Button enterModeButton;
	private Button solveButton;
	private Button hintButton;
	private Button undoButton;
	private Button showPossibleButton;
	private Button autoFillOneButton;
	private Button loadStandardPuzzle;
	private Button loadButton;
	private Button saveButton;
	private Button clearButton;
	
	private int[][] boardNormal;
	private Composite[][] labelCellNormal;
	private Label[][][] boardLabelsNormal;
	private Text[][] boardTextNormal;
	private ArrayList<List<List<Integer>>> possibleNormal;
	private Map<Text, UserInputPoint> inputBoxesNormal = new HashMap<Text, UserInputPoint>();
	protected boolean solveMode;
	protected Vector<Point> movesNormal = new Vector<Point>();
	protected int[][] givenNormal = new int[9][9];
	protected Random rnd = new Random(System.currentTimeMillis());
	protected int[][] tempBoard;
	private boolean solved;
	private boolean autoFillOne = false;
	private boolean showPossible = true;
	private boolean loading = false;
	private Job backgroundSolve;
	private Runnable backgroundSolveComplete;
	protected boolean backgroundSolved;
	private Job dummyJob;
	private Runnable solveComplete;
	private boolean solving;
	private Runnable refresh;
	

	
	

	/**
	 * Constructor
	 * @param parent The parent Composite
	 * @param style Style. Most likely SWT.NONE
	 */
	public NormalPuzzle(Composite parent, int style) {
		super(parent, style);
		
		GridLayout gl_this = new GridLayout();
		gl_this.marginWidth = 0;
		gl_this.marginHeight = 0;
		setLayout(gl_this);
		
		createHead(this);
		createMain(this);
		
		showPossibleButton.setBackground(ColorService.GREEN);
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (i < 9 && j < 9) {
					givenNormal[i][j] = 0;
				}
			}
		}
					
		// Defining some jobs for the calculations
		
		backgroundSolve = new Job("Solving Puzzle in Background") { //$NON-NLS-1$
			public IStatus run(IProgressMonitor monitor) {
					tempBoard = new int[9][9];
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {
							tempBoard[i][j] = boardNormal[i][j];
						}
					}
					if (solveNormal(tempBoard)) {
						getDisplay().asyncExec(backgroundSolveComplete);
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
				if (solvePuzzleNormal()) {
					refresh();
				}
			}
		};
		
		refresh = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						labelCellNormal[i][j].layout();
					}
				}
			}
		};
	}

	protected boolean solvePuzzleNormal() {
		if (backgroundSolve.getState() == Job.RUNNING) {
			backgroundSolve.cancel();
		}
		solving = true;
		if (backgroundSolved) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (boardNormal[i][j] != 0 && boardNormal[i][j] != tempBoard[i][j]) {
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
					boardNormal[i][j] = tempBoard[i][j];
				}
			}
		} else {
			if (solveNormal(boardNormal)) {
				solved = true;
			}
		}
		if (solved) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					for (int k = 0; k < 8; k++)
						boardLabelsNormal[i][j][k].setText(""); //$NON-NLS-1$
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

	private void showErroneousEntries() {
		if (backgroundSolved) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (boardNormal[i][j] != 0 && boardNormal[i][j] != tempBoard[i][j]) {
						labelCellNormal[i][j].setBackground(ColorService.RED);
						boardTextNormal[i][j].setBackground(ColorService.RED);
					}
				}
			}
		}
	}

	protected boolean solveNormal(int[][] board) {
		Point start = getEmptySquare(board);
		if (start == null) {
			return true;
		}

		int x = start.x;
		int y = start.y;
		boolean solved = false;

		for (int c = 1; c <= 9 && !solved; c++) {
			if (possibleNormal.get(x).get(y).indexOf(c) != -1) {
				if (!isConflictNormal(board, x, y, c)) {
					board[x][y] = c;
					solved = solveNormal(board);
					if (!solved) {
						board[x][y] = 0;
					}
				}
			}
		}
		return solved;
	}

	private boolean isConflictNormal(int[][] board, int x, int y, int c) {
		return rowConflictNormal(board, x, y, c) || colConflictNormal(board, x, y, c) || boxConflictNormal(board, x, y, c);
	}

	private boolean boxConflictNormal(int[][] board, int xx, int yy, int c) {
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

	private boolean colConflictNormal(int[][] board, int x, int y, int c) {
		for (int i = 0; i < 9; i++) {
			if (board[x][i] == c) {
				return true;
			}
		}
		return false;
	}

	private boolean rowConflictNormal(int[][] board, int x, int y, int c) {
		for (int i = 0; i < 9; i++) {
			if (board[i][y] == c) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Builds the Group with the name "Sudoku".
	 * @param normalPuzzle The parent Composite
	 */
	private void createMain(NormalPuzzle normalPuzzle) {
		Group mainGroup = new Group(normalPuzzle, SWT.NONE);
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
						labelCellNormal[i][j].setBackground(ColorService.WHITE);
						for (int k = 0; k < 8; k++) {
							boardLabelsNormal[i][j][k].setBackground(ColorService.WHITE);
						}
					}
				}
			}
		};
		getDisplay().asyncExec(makeWhite);
	}

	/**
	 * Creates the right part of the GUI. The area where you can enter values etc..
	 * @param parent The parent Group
	 */
	private void createPlayFieldArea(Group parent) {
		Composite playField = new Composite(parent, SWT.NONE);
		playField.setLayout(new GridLayout());
		playField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		playField.addControlListener(new ControlListener() {
			
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle controlSize = playField.getBounds();
				if (controlSize.height < controlSize.width) {
					playField.setSize(controlSize.height, controlSize.height);
				} else {
					playField.setSize(controlSize.width, controlSize.width);
				}
			}
			
			@Override
			public void controlMoved(ControlEvent e) {
				
			}
		});
		
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

	/**
	 * Creates the sudoku field and the data for it.
	 * @param parent
	 */
	private void createFieldNormal(Composite parent) {
		Composite playField = new Composite(parent, SWT.NONE);
		GridData gd_playField = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_playField.widthHint = 600;
		gd_playField.heightHint = 600;
		playField.setLayoutData(gd_playField);
		GridLayout gl_playField = new GridLayout(9, false);
		gl_playField.horizontalSpacing = 0;
		gl_playField.verticalSpacing = 0;
		playField.setLayout(gl_playField);
		
		Map<Composite, Point> compositeBoxesNormal = new HashMap<Composite, Point>();

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				labelCellNormal[i][j] = new Composite(playField, SWT.NONE);
				
				compositeBoxesNormal.put(labelCellNormal[i][j], new Point(i, j));
				labelCellNormal[i][j].addListener(SWT.MouseDown, new Listener() {

					@Override
					public void handleEvent(Event event) {
						Composite composite = (Composite) event.widget;
						Point point = compositeBoxesNormal.get(composite);
						boardTextNormal[point.x][point.y].setFocus();
					}

				});
				labelCellNormal[i][j].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				final int f_i = i, f_j = j; // Final variables allow access in listener class
				labelCellNormal[i][j].addPaintListener(new PaintListener() {
					@Override
					public void paintControl(PaintEvent e) {
						Rectangle a = ((Composite) e.getSource()).getClientArea();

						if (f_i != 8 && f_j != 8) { // draws rectangles
							e.gc.drawRectangle(0, 0, a.width, a.height);
						} else if (f_i != 8) {
							e.gc.drawRectangle(0, 0, a.width - 1, a.height);
						} else if (f_j != 8) {
							e.gc.drawRectangle(0, 0, a.width, a.height - 1);
						} else {
							e.gc.drawRectangle(0, 0, a.width - 1, a.height - 1);
						}

						if ((f_j + 1) % 3 == 0 && (f_j + 1) != 9) { // draws bold lines
							e.gc.drawLine(a.width - 1, a.height - 1, a.width - 1, 0);
						}
						if ((f_i + 1) % 3 == 0 && (f_i + 1) != 9) {
							e.gc.drawLine(a.width - 1, a.height - 1, 0, a.height - 1);
						}
					}
				});

				labelCellNormal[i][j].setBackground(ColorService.WHITE);
				GridLayout cellNormalLayout = new GridLayout(3, true);
				cellNormalLayout.verticalSpacing = 0;
				cellNormalLayout.horizontalSpacing = 0;
				labelCellNormal[i][j].setLayout(cellNormalLayout);
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
							boardLabelsNormal[i][j][k].setText(Integer.toString(possibleNormal.get(i).get(j).get(k)));
						}
					}
				}
			}
		}
	}

	private Text createTextNormal(Composite parent) {
		Text input = new Text(parent, SWT.CENTER);
		input.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		input.setTextLimit(1);
		input.setFont(FontService.getSmallFont());
		input.addListener(SWT.Verify, new Listener() {
			@Override
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
								|| possibleNormal.get(point.x).get(point.y).indexOf(Integer.parseInt(input)) == -1
								|| createsZeroPossible(new Point(point.x, point.y), Integer.parseInt(input))) {
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

	protected boolean createsZeroPossible(Point point, int input) {
		boolean returnValue = false;
		Vector<Point> affectedPointsH = new Vector<Point>();
		Vector<Point> affectedPointsV = new Vector<Point>();
		Vector<Point> affectedPointsS = new Vector<Point>();{
		affectedPointsH = new Vector<Point>();
		affectedPointsV = new Vector<Point>();
		affectedPointsS = new Vector<Point>();
		int x = 3 * (int) Math.floor(point.x / 3);
		int y = 3 * (int) Math.floor(point.y / 3);
		affectedPointsH = new Vector<Point>();
		affectedPointsV = new Vector<Point>();
		affectedPointsS = new Vector<Point>();
		for (int i = 0; i < 9; i++) {
			if (point.y != i && possibleNormal.get(point.x).get(i).size() == 1
					&& possibleNormal.get(point.x).get(i).get(0) == input)
				returnValue = true;
			if (point.x != i && possibleNormal.get(i).get(point.y).size() == 1
					&& possibleNormal.get(i).get(point.y).get(0) == input)
				returnValue = true;
			if (point.y != i && boardNormal[point.x][i] == 0)
				affectedPointsH.add(new Point(point.x, i));
			if (point.x != i && boardNormal[i][point.y] == 0)
				affectedPointsV.add(new Point(i, point.y));
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if ((point.x != x + i || point.y != y + j) && possibleNormal.get(x + i).get(y + j).size() == 1
						&& possibleNormal.get(x + i).get(y + j).get(0) == input)
					returnValue = true;
				if ((point.x != x + i || point.y != y + j) && boardNormal[x + i][y + j] == 0)
					affectedPointsS.add(new Point(x + i, y + j));
			}
		}
		if (checkSubset(affectedPointsH, possibleNormal, input)
				|| checkSubset(affectedPointsV, possibleNormal, input)
				|| checkSubset(affectedPointsS, possibleNormal, input))
			returnValue = true;

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
	
	private boolean checkErroneousEntries() {
		if (backgroundSolved) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (boardNormal[i][j] != 0 && boardNormal[i][j] != tempBoard[i][j]) {
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

	private Label createLabelNormal(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setAlignment(SWT.CENTER);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		label.setFont(FontService.getTinyFont());
		return label;
	}

	/**
	 * Creates the part of the GUI in which the Buttons for the settings etc. are.
	 * @param mainGroup The parent Group.
	 */
	private void createButtonArea(Composite mainGroup) {
		Composite mainComposite = new Composite(mainGroup, SWT.None);
		mainComposite.setLayout(new GridLayout());
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		Group grpModeChoice = new Group(mainComposite, SWT.NONE);
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
				
				movesNormal.clear();
				
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
							if (boardNormal[i][j] > 0) {
								boardTextNormal[i][j].setEditable(false);
								givenNormal[i][j] = 1;
							}
						}
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

				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						boardTextNormal[i][j].setEditable(true);
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		Group grpActionButtons = new Group(mainComposite, SWT.NONE);
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
				int puzzle;
				URL fileName = null;
				try {
					fileName = FileLocator.toFileURL((SudokuPlugin.getDefault().getBundle().getEntry("/"))); //$NON-NLS-1$
				} catch (IOException ex) {
					LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
				}
				StringBuilder path = new StringBuilder();
				path.append(fileName.getFile());
				path.append("data/"); //$NON-NLS-1$
				puzzle = rnd.nextInt(5) + 1;
				path.append("sudoku" + puzzle + ".sud"); //$NON-NLS-1$ //$NON-NLS-2$
				// Load normal puzzle. If it fails jump out of the method.
				if (!loadNormal(path.toString())) {
					return;
				}
				
				refresh();

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
				loadPuzzleNormal();
				refresh();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		Group grpOptionButtons = new Group(mainComposite, SWT.NONE);
		grpOptionButtons.setLayout(new GridLayout());
		grpOptionButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		grpOptionButtons.setText(Messages.SudokuComposite_optionsAreaTitle);
		
		
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
		hintButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
					if (backgroundSolved && getEmptySquare(boardNormal) != null) {
						Point square = new Point(rnd.nextInt(9), rnd.nextInt(9));
						while (boardNormal[square.x][square.y] > 0) {
							square = new Point(rnd.nextInt(9), rnd.nextInt(9));
						}
						boardNormal[square.x][square.y] = tempBoard[square.x][square.y];
						for (int k = 0; k < 8; k++) {
							boardLabelsNormal[square.x][square.y][k].setText(""); //$NON-NLS-1$
						}
						boardTextNormal[square.x][square.y].setText(Integer.toString(boardNormal[square.x][square.y]));
						startBlinkingArea(square.x, square.y);
				}
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
				fillOneNormal();
				refresh();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		showPossibleButton = new Button(grpOptionButtons, SWT.PUSH);
		showPossibleButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		showPossibleButton.setEnabled(false);
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
				updatePossibilitiesNormal();
				refresh();
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
				if (movesNormal.size() > 0) {
					Point pt = movesNormal.get(movesNormal.size() - 1);
					movesNormal.remove(movesNormal.size() - 1);
					boardTextNormal[pt.x][pt.y].setText(""); //$NON-NLS-1$
					updateBoardDataWithUserInputNormal(boardTextNormal[pt.x][pt.y], ""); //$NON-NLS-1$
					if (movesNormal.size() == 0) {
						undoButton.setEnabled(false);
					}
				}
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
				savePuzzleNormal();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		clearButton = new Button(grpOptionButtons, SWT.PUSH);
		clearButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		clearButton.setEnabled(true);
		clearButton.setText(Messages.SudokuComposite_ClearButton);
		clearButton.setToolTipText(Messages.SudokuComposite_ClearButton_Tooltip);
		clearButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				enterModeButton.setSelection(true);
				solveModeButton.setSelection(false);
				enterModeButton.notifyListeners(SWT.Selection, null);
				backgroundSolve.cancel();
				loading = true;
				
				clearPuzzleNormal();
				
				loading = false;
				refresh();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
	}

	/**
	 * Opens a dialog where the user can select an .sud that should be loaded.
	 */
	protected void loadPuzzleNormal() {
		String fileName = openFileDialog(SWT.OPEN);

		if (fileName == null) {
			return;
		}

		loadNormal(fileName);
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
	public boolean savePuzzleNormal() {
		String fileName = openFileDialog(SWT.SAVE);
		FileOutputStream out = null;

		if (fileName == null) {
			return false;
		}

		try {
			out = new FileOutputStream(fileName);
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					out.write(Integer.toString(boardNormal[i][j]).getBytes());
				}
				out.write('\n');
			}
		} catch (FileNotFoundException e) {
			printFileNotFoundExceptionDialogSaving(e);
			return false;
		} catch (IOException e) {
			MessageBox ioExceptionDialog = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
			ioExceptionDialog.setText("Saving puzzle encountered a problem"); //$NON-NLS-1$
			ioExceptionDialog.setMessage("An IOException occured. See the error log for further information."); //$NON-NLS-1$
			ioExceptionDialog.open();
			LogUtil.logError(SudokuPlugin.PLUGIN_ID, e);
			return false;
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException ex) {
				LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
			}
		}
		return true;
	}
	
	/**
	 * Shows a dialog that informs the user that the puzzle could not be saved, 
	 * due to a missing or invalid file path.
	 * Error will be logged in the error log.
	 */
	private void printFileNotFoundExceptionDialogSaving(FileNotFoundException fnfe) {
		// Print a message that puzzle is not saved.
		MessageBox emptyFileDialog = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
		emptyFileDialog.setText("Saving puzzle encountered a problem"); //$NON-NLS-1$
		emptyFileDialog.setMessage("Puzzle could not be saved. Invalid file path."); //$NON-NLS-1$
		emptyFileDialog.open();
		// Log the error in the error log.
		LogUtil.logError(SudokuPlugin.PLUGIN_ID, "File Name for puzzle is empty.", fnfe, false); //$NON-NLS-1$
	}
	
	/**
	 * Opens a dialog where the user can select an .sud that should be loaded.
	 * @param type Style of the dialog. Most likely SWT.OPEN
	 * @return The path to the file the user chose.
	 */
	private String openFileDialog(int type) {
		FileDialog dialog = new FileDialog(getDisplay().getActiveShell(), type);
		dialog.setFilterPath(DirectoryService.getUserHomeDir());
		dialog.setFilterExtensions(new String[] { "*.sud" }); //$NON-NLS-1$
		dialog.setFilterNames(new String[] { "Sudoku Files (*.sud)" }); //$NON-NLS-1$
		dialog.setOverwrite(true);
		return dialog.open();
	}

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
	public boolean loadNormal(String fileName) {
		long wholeLoadNormalTime = System.currentTimeMillis();
		solved = false;
		BufferedReader reader = null;
		clearPuzzleNormal();
		loading = true;
		try {
			long t1 = System.currentTimeMillis();
			reader = new BufferedReader(new FileReader(fileName));
			int count = 0;
			String line;
			while ((line = reader.readLine()) != null) {
				for (int i = 0; i < 9; i++) {
					boardNormal[count][i] = Integer.parseInt(line.substring(i, i + 1));
					if (boardNormal[count][i] != 0) {
						boardTextNormal[count][i].setText(line.substring(i, i + 1));
					}
				}
				count++;
			}
			long t2 = System.currentTimeMillis();
			System.out.println("Laufzeit des try blocks " + ( t2 - t1));
		} catch (NumberFormatException nfe) {
			LogUtil.logError(SudokuPlugin.PLUGIN_ID, nfe);
			MessageBox brokenFile = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
			brokenFile.setText("Loading puzzle encountered a problem"); //$NON-NLS-1$
			brokenFile.setMessage("Puzzle could not be loadeed. There is a wrong character in the loaded file.\n"); //$NON-NLS-1$
			brokenFile.open();
			return false;
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
			}
		}
		loading = false;
		updatePossibilitiesNormal();
		long l = System.currentTimeMillis();
		System.out.println("Laufzeit der kompletten Schleife " + (l - wholeLoadNormalTime));
		return true;
	}
	
	/**
	 * Prints a message box informing the user that an ioexception occurred.
	 * @param ioe Logs the exception in the error log.
	 */
	private void printIOExceptionDialogLoading(IOException ioe) {
		MessageBox fileNotFound = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
		fileNotFound.setText("Loading puzzle encountered a problem"); //$NON-NLS-1$
		fileNotFound.setMessage("An IOException occured. See the error log for more information."); //$NON-NLS-1$
		fileNotFound.open();
		LogUtil.logError(SudokuPlugin.PLUGIN_ID, ioe);
	}
	
	/**
	 * Prints a dialog informing the user that the puzzle he wished to load could not be loaded.
	 */
	private void printFileNotFoundExceptionDialogLoading(FileNotFoundException fnfe) {
		// Print a message that puzzle is not loaded.
		MessageBox emptyFileDialog = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
		emptyFileDialog.setText("Loading puzzle encountered a problem"); //$NON-NLS-1$
		emptyFileDialog.setMessage("Puzzle could not be loaded. Invalid file path."); //$NON-NLS-1$
		emptyFileDialog.open();
		// Log the error in the error log.
		LogUtil.logError(SudokuPlugin.PLUGIN_ID, "File Name for puzzle is empty.", fnfe, false); //$NON-NLS-1$
	}

	/**
	 * Removes all entries from the current sudoku
	 */
	private void clearPuzzleNormal() {
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				boardNormal[i][j] = 0;
				boardTextNormal[i][j].setText(""); //$NON-NLS-1$
				for (int k = 0; k < 8; k++) {
					boardLabelsNormal[i][j][k].setText(""); //$NON-NLS-1$
				}
				possibleNormal.get(i).get(j).clear();
				for (int k = 1; k <= 9; k++) {
					possibleNormal.get(i).get(j).add(k);
				}
			}
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Laufzeit clearPuzzleNormal " + ( t2 - t1));
	}

	/**
	 * Fills a field in the sudoku with a value.
	 */
	protected void fillOneNormal() {
		boolean changed = false;
		for (int i = 0; i < 9 & !changed; i++) {
			for (int j = 0; j < 9 & !changed; j++) {
				if (possibleNormal.get(i).get(j).size() == 1) {
					boardNormal[i][j] = possibleNormal.get(i).get(j).get(0);
					boardTextNormal[i][j].setText(Integer.toString(boardNormal[i][j]));
					labelCellNormal[i][j].layout();
					startBlinkingArea(i, j);
					changed = true;
				}
			}
		}
	}

	/**
	 * Does a layout() to all labels in the sudoku field.
	 */
	protected void refresh() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				labelCellNormal[i][j].layout();
			}
		}
	}

	/**
	 * Part of the undo mechanism.
	 * @param inputBox
	 * @param inputStr
	 */
	protected void updateBoardDataWithUserInputNormal(Text inputBox, String inputStr) {
		solved = false;
		UserInputPoint point = inputBoxesNormal.get(inputBox);
		int num = 0;
		if (inputStr.length() > 0) {
			num = Integer.parseInt(inputStr);
			Point pt = new Point(point.x, point.y);
			movesNormal.add(pt);
			undoButton.setEnabled(true);
		}
		if (num == 0 && boardNormal[point.x][point.y] != 0) {
			addPossibleNormal(point.x, point.y, boardNormal[point.x][point.y]);
		}
		boardNormal[point.x][point.y] = num;
		labelCellNormal[point.x][point.y].setBackground(ColorService.WHITE);
		boardTextNormal[point.x][point.y].setBackground(ColorService.WHITE);
		updatePossibilitiesNormal();
	}

	/**
	 * Updates the possible values of the sudoku.<br>
	 * Should be transformed into a job or thread.
	 */
	private void updatePossibilitiesNormal() {
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
									idx = possibleNormal.get(3 * i + l).get(3 * j + m).indexOf(used.get(k));
									if (idx != -1)
										possibleNormal.get(3 * i + l).get(3 * j + m).remove(idx);
									if (autoFillOne && possibleNormal.get(3 * i + l).get(3 * j + m).size() == 1) {
										boardNormal[3 * i + l][3 * j + m] = possibleNormal.get(3 * i + l).get(3 * j + m)
												.get(0);
										boardTextNormal[3 * i + l][3 * j + m]
												.setText(Integer.toString(boardNormal[3 * i + l][3 * j + m]));
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
						boardLabelsNormal[i][j][k].setText(Integer.toString(possibleNormal.get(i).get(j).get(k)));
					}
					for (int k = possibleNormal.get(i).get(j).size(); k < 8; k++) {
						boardLabelsNormal[i][j][k].setText(""); //$NON-NLS-1$
					}
				}
				if (!showPossible) {
					for (int k = 0; k < 8; k++)
						boardLabelsNormal[i][j][k].setText(""); //$NON-NLS-1$
				}
				if (possibleNormal.get(i).get(j).size() == 9) {
					for (int k = 0; k < 8; k++)
						boardLabelsNormal[i][j][k].setText(""); //$NON-NLS-1$
				}
				labelCellNormal[i][j].layout();
			}
		}
		if (changed)
			updatePossibilitiesNormal();
	}

	private void addPossibleNormal(int x, int y, int val) {
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

	/**
	 * Lets a field of the sudoku blink alternating white and red.<br>
	 * Indicating where a field has changed.
	 * @param x The x coordinate of the field.
	 * @param y The y coordinate og the field.
	 */
	protected void startBlinkingArea(int x, int y) {
		Thread blinkerRed = new Thread() {
			
			@Override
			public void run() {
				labelCellNormal[x][y].setBackground(ColorService.RED);
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
				labelCellNormal[x][y].setBackground(ColorService.WHITE);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					LogUtil.logError(SudokuPlugin.PLUGIN_ID, e);
				}
			}
		};
		
		for (int i = 0; i < 3; i++) {
			getDisplay().asyncExec(blinkerRed);
			getDisplay().asyncExec(blinkerWhite);
		}
	}

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
	 * Creates the title and description
	 * @param normalPuzzle The parent Composite
	 */
	private void createHead(NormalPuzzle normalPuzzle) {
		Composite headComposite = new Composite(normalPuzzle, SWT.NONE);
		headComposite.setLayout(new GridLayout());
		GridData gd_headComposite = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_headComposite.minimumWidth  = 300;
		gd_headComposite.widthHint = 300;
		headComposite.setLayoutData(gd_headComposite);
		
		Text title = new Text(headComposite, SWT.READ_ONLY);
		title.setFont(FontService.getHeaderFont());
		title.setBackground(ColorService.WHITE);
		title.setText(Messages.SudokuComposite_Normal_Title);
		
		Text description = new Text(headComposite, SWT.READ_ONLY);
		description.setText(Messages.SudokuComposite_Normal_Desc);
		description.setBackground(ColorService.WHITE);
	}
}

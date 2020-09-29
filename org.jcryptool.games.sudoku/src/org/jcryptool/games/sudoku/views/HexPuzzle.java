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


/**
 * Class for the GUI and logic of the hex-sudokus.
 * @author Thorben Groos
 *
 */
public class HexPuzzle extends Composite {
	
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
	private Button restartButton;
	private Button onePossibleButton;
	private Button nakedSingleButton;
	private Button hiddenSingleButton;
	private Button blockAndCRButton;
	private Button nakedSubsetButton;
	private Button candidateLineButton;
	private Button doublePairButton;
	private Button multipleLinesButton;
	
	private Random rnd = new Random(System.currentTimeMillis());
	private Job backgroundSolve;
	private Job dummyJob;
	private Runnable backgroundSolveComplete;
	private Runnable solveComplete;
	private Runnable refresh;
	protected boolean backgroundSolved;
	private boolean autoFillOne = false;
	private boolean showPossible = true;
	private boolean solved = false;
	private boolean solving;
	private boolean loading = false;
	private Vector<Point> movesHex = new Vector<Point>();
	/**
	 * This are the fields of the sudoku that contain the possible values (boardLabelsNormal) and the entered
	 * value in the middle of a field .
	 */
	protected Composite[][] labelCellHex;
	protected int[][] tempBoard;
	private int [][] guessBoardHex;
	/**
	 * contains the values that are entered in the sudoku.
	 */
	protected int[][] boardHex;
	protected List<List<List<Integer>>> possibleHex;
	protected List<List<List<Integer>>> tempPossibleHex;
	private ArrayList<List<List<Integer>>> guessPossibleHex;
	private int numberOfGuesses = 0;
	/**
	 * The possibilities of each field.
	 */
	private Label[][][] boardLabelsHex;
	/**
	 * The value in the middle of each field in the sudoku.
	 */
	private Text[][] boardTextHex;
	private Map<Text, Point> inputBoxesHex = new HashMap<Text, Point>();
	
	/**
	 * Saves the sudoku when changing from enter mode to solve mode. Used to save the initial values 
	 * of the sudoku to enable a restart.
	 */
	protected int [][] originalSudoku = new int[16][16];
	
//	Tried to prevent an error that some jobs are still running after platform shutdown.
//	Does not work!
//	@Override
//	public void addDisposeListener(DisposeListener listener) {
//		super.addDisposeListener(listener);
//		backgroundSolve.cancel();
//		dummyJob.cancel();
//	}
	
	//There is a failure in the recursive call of generateSubsetsb
	//but i could not fix it. 
//	protected int counterBackgroundSolve = 0;
//	protected int counterHumanStrategiesHex = 0;
//	protected int counterGuessOnDiagonalHex = 0;
//	protected int counterBackgroundSolveComplete = 0;
//	protected int counterSolveHex = 0;
//	protected int counterCheckPuzzleHex = 0;
//	protected int counterGenerateSubsets = 0;
//	protected int counterGenerateSubsetsRecursiveCalls = 0;
	
/**
 * The constructor of HexPuzzle.
 * @param parent The parent composite.
 * @param style Style
 */
	public HexPuzzle(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new GridLayout());
		
		createHead(this);
		createMain(this);
		
		showPossibleButton.setBackground(ColorService.GREEN);

		refresh = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 16; j++) {
						labelCellHex[i][j].layout();
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
				if (solvePuzzleHex()) {
					refresh();
				}
			}
		};

		backgroundSolveComplete = new Runnable() {
			@Override
			public void run() {
//				counterBackgroundSolveComplete++;
//				System.out.println("Aufrufe backgroundSolveComplete:\t" + counterBackgroundSolveComplete);
				backgroundSolved = true;
				hintButton.setEnabled(true);
			}

		};

		backgroundSolve = new Job("Solving Puzzle in Background") {
			@Override
			public IStatus run(final IProgressMonitor monitor) {
//				counterBackgroundSolve++;
//				System.out.println("Aufrufe backgroundSolve:\t" + counterBackgroundSolve);
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

				return Status.OK_STATUS;
			}
		};

	}

	/**
	 * Creates the major part of the GUI. 
	 * @param hexPuzzle
	 */
	private void createMain(HexPuzzle hexPuzzle) {
		
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
	
	private void createPlayFieldArea(final Composite parent) {
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

	private void createButtonArea(Composite parent) {
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
				solveButton.setEnabled(true);
				hintButton.setEnabled(false);
				showPossibleButton.setEnabled(true);
				autoFillOneButton.setEnabled(true);

				loadStandardPuzzle.setEnabled(false);
				loadButton.setEnabled(false);
				
				restartButton.setEnabled(true);

				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 16; j++) {
						if (boardHex[i][j] > -1) {
							boardTextHex[i][j].setEditable(false);
							boardTextHex[i][j].setFont(FontService.getSmallBoldFont());
						}
						originalSudoku[i][j] = boardHex[i][j];
					}
				}

				movesHex.clear();
				onePossibleButton.setEnabled(true);
				nakedSingleButton.setEnabled(true);
				hiddenSingleButton.setEnabled(true);
				blockAndCRButton.setEnabled(true);
				nakedSubsetButton.setEnabled(true);
				candidateLineButton.setEnabled(true);
				doublePairButton.setEnabled(true);
				multipleLinesButton.setEnabled(true);
				
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

				solveButton.setEnabled(false);

				loadStandardPuzzle.setEnabled(true);
				loadButton.setEnabled(true);
				
				restartButton.setEnabled(false);

				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 16; j++) {
						boardTextHex[i][j].setEditable(true);
					}
				}

				onePossibleButton.setEnabled(false);
				nakedSingleButton.setEnabled(false);
				hiddenSingleButton.setEnabled(false);
				blockAndCRButton.setEnabled(false);
				nakedSubsetButton.setEnabled(false);
				candidateLineButton.setEnabled(false);
				doublePairButton.setEnabled(false);
				multipleLinesButton.setEnabled(false);
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
				puzzle = rnd.nextInt(3) + 1;
				path.append("hex" + puzzle + ".sud");
				// load 16*16 sudoku. If it fails jump out of method.
				if (!loadHex(path.toString())) {
					return;
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
				loadPuzzleHex();
				refresh();
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
		solveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				dummyJob.setUser(true);
				dummyJob.schedule();
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
				if (backgroundSolved && getEmptySquare(boardHex) != null) {
					Point square = new Point(rnd.nextInt(16), rnd.nextInt(16));
					while (boardHex[square.x][square.y] > -1)
						square = new Point(rnd.nextInt(9), rnd.nextInt(9));
					boardHex[square.x][square.y] = tempBoard[square.x][square.y];
					for (int k = 0; k < 8; k++) {
						boardLabelsHex[square.x][square.y][k].setText("");
					}
					boardTextHex[square.x][square.y].setText(valToTextHex(boardHex[square.x][square.y]));
					startBlinkingArea(square.x, square.y);
				}
			}
		});

		undoButton = new Button(grpOptionButtons, SWT.PUSH);
		undoButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		undoButton.setEnabled(false);
		undoButton.setText(Messages.SudokuComposite_UndoButton);
		undoButton.setToolTipText(Messages.SudokuComposite_UndoButton_Tooltip);
		undoButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (movesHex.size() > 0) {
					Point pt = movesHex.get(movesHex.size() - 1);
					movesHex.remove(movesHex.size() - 1);
					boardTextHex[pt.x][pt.y].setText("");
					updateBoardDataWithUserInputHex(boardTextHex[pt.x][pt.y], "");
					if (movesHex.size() == 0) {
						undoButton.setEnabled(false);
					}
				}
			}
		});

		showPossibleButton = new Button(grpOptionButtons, SWT.PUSH);
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
				updatePossibilitiesHex(boardHex, possibleHex, true);
				refresh();
			}
		});

		autoFillOneButton = new Button(grpOptionButtons, SWT.PUSH);
		autoFillOneButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		autoFillOneButton.setEnabled(false);
		autoFillOneButton.setText(Messages.SudokuComposite_AutoFillOneButton);
		autoFillOneButton.setToolTipText(Messages.SudokuComposite_AutoFillOneButton_Tooltip);
		autoFillOneButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				fillOneHex();
				refresh();
			}
		});

		saveButton = new Button(grpOptionButtons, SWT.PUSH);
		saveButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		saveButton.setEnabled(true);
		saveButton.setText(Messages.SudokuComposite_SaveButton);
		saveButton.setToolTipText(Messages.SudokuComposite_SaveButton_Tooltip);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				savePuzzleHex();
			}
		});

		restartButton = new Button(grpOptionButtons, SWT.PUSH);
		restartButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		restartButton.setEnabled(false);
		restartButton.setText(Messages.NormalPuzzle_restartPuzzle);
		restartButton.setToolTipText(Messages.NormalPuzzle_restartPuzzleTooltip);
		restartButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				reset();
				
				solved = false;
				autoFillOne = false;
				
				if (originalSudoku != null) {
					for (int i = 0; i < 16; i++) {
						for (int j = 0; j < 16; j++) {
							boardHex[i][j] = originalSudoku[i][j];
							boardTextHex[i][j].setText(valToTextHex(originalSudoku[i][j]));
						}
					}
				}
//				loading = true;

				updatePossibilitiesHex(boardHex, possibleHex, true);
//				loading = false;
				solveModeButton.notifyListeners(SWT.Selection, new Event());
				refresh();
			}
		});

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

	/**
	 * Resets the current puzzle.
	 */
	protected void reset() {
		backgroundSolve.cancel();
		dummyJob.cancel();
		
		enterModeButton.setSelection(true);
		solveModeButton.setSelection(false);
		restartButton.setEnabled(false);
		enterModeButton.notifyListeners(SWT.Selection, null);
		backgroundSolve.cancel();
		undoButton.setEnabled(false);
		
		loading = true;
		
		clearPuzzleHex();
		
		loading = false;
		
		refresh();
	}

	/**
	 * Creates the Title and Description of the Hex Sudoku tab.
	 * @param hexPuzzle The parent composite
	 */
	private void createHead(Composite hexPuzzle) {
		Composite headComposite = new Composite(this, SWT.NONE);
		GridData gd_headComposite = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_headComposite.minimumWidth = 300;
		gd_headComposite.widthHint = 300;
		headComposite.setLayoutData(gd_headComposite);
		headComposite.setLayout(new GridLayout());

		final Text title = new Text(headComposite, SWT.READ_ONLY);
		title.setFont(FontService.getHeaderFont());
		title.setBackground(ColorService.WHITE);
		title.setText(Messages.SudokuComposite_Hex_Title);
		
		final Text stDescription = new Text(headComposite, SWT.READ_ONLY | SWT.WRAP);
		stDescription.setText(Messages.SudokuComposite_Hex_Desc);
		stDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
	}
	
	/**
	 * Does a layout() to all label inside the playfield.
	 */
	private void refresh() {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				labelCellHex[i][j].layout();
			}
		}
	}
	
	private boolean solvePuzzleHex() {
		if (backgroundSolve.getState() == Job.RUNNING) {
			backgroundSolve.cancel();
		}
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
					for (int k = 0; k < 8; k++) {
						boardLabelsHex[i][j][k].setText("");
					}
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
	
	private void humanStrategiesHex(int[][] board, List<List<List<Integer>>> possibilities) {
//		counterHumanStrategiesHex++;
//		System.out.println("Aufrufe humanStrategiesHex:\t" + counterHumanStrategiesHex);
		boolean changed = true;
		while (changed) {
			changed = false;
			changed = changed 
					|| onePossibleHex(board, possibilities, false)
					|| nakedSingleHex(board, possibilities, false) 
					|| hiddenSingleHex(board, possibilities, false)
					|| blockAndCRHex(board, possibilities) 
					|| nakedSubsetHex(board, possibilities)
					|| candidateLineHex(board, possibilities) 
					|| doublePairHex(board, possibilities)
					|| multipleLinesHex(board, possibilities);
		}
	}
	
	private void guessOnDiagonalHex(int[][] board, List<List<List<Integer>>> possibilities) {
		numberOfGuesses = numberOfGuesses + 1;
//		counterGuessOnDiagonalHex++;
//		System.out.println("Aufrufe guessOnDiagonalHex:\t" + counterGuessOnDiagonalHex);
		int i = 2;
		int j = 0;
		int guessPointX = -1;
		int guessPointY = -1;
		int guessPossible = 0;
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
					for (int k = 0; k < possibilities.get(i).get(j).size(); k++) {
						guessPossibleHex.get(i).get(j).add(possibilities.get(i).get(j).get(k));
					}
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
	
	private boolean solveHex(int[][] board, final IProgressMonitor monitor) {
//		counterSolveHex++;
//		System.out.println("Aufrufe solveHex:\t" + counterSolveHex);
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
	
	private void makeWhite() {
		Thread makeWhite = new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 16; j++) {
						labelCellHex[i][j].setBackground(ColorService.WHITE);
						for (int k = 0; k < 8; k++) {
							boardLabelsHex[i][j][k].setBackground(ColorService.WHITE);
						}
					}
				}
			}
		};
		getDisplay().asyncExec(makeWhite);
	}
	
	private void createFieldHex(final Composite parent) {
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
				
				Map<Composite, Point> compositeBoxesHex = new HashMap<Composite, Point>();
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
				inputBoxesHex.put(boardTextHex[i][j], new Point(i, j));
				for (int k = 4; k < 8; k++) {
					boardLabelsHex[i][j][k] = createLabelHex(labelCellHex[i][j], k);
				}
				if (boardHex[i][j] != -1)
//					boardTextHex[i][j].setText(Integer.toString(boardHex[i][j]));
					boardTextHex[i][j].setText(valToTextHex(boardHex[i][j]));
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
	
	private Point getEmptySquare(int[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == -1) {
					return new Point(i, j);
				}
			}
		}
		return null;
	}
	
	/**
	 * Converts an int into a corresponding string value.<br>
	 * The method is only a <code>Integer.toHexString(val).toUpperCase();</code>
	 * @param val The value that should be converted.
	 * @return A string 'A', 'B', 'C', 'D', 'E' or 'F'.
	 */
	private String valToTextHex(int val) {
		return Integer.toHexString(val).toUpperCase();
	}
	
	/**
	 * Lets a field in the sudoku blink red and white alternating.<br>
	 * Doesn't block the GUI :)
	 * @param x x-coordinate of the field.
	 * @param y y-coordinate of the field.
	 */
	private void startBlinkingArea(final int x, final int y) {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					for (int i = 0; i < 3; i++) {
						getDisplay().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								labelCellHex[x][y].setBackground(ColorService.RED);
							}
						});
						Thread.sleep(500);
						getDisplay().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								labelCellHex[x][y].setBackground(ColorService.WHITE);
							}
						});
						Thread.sleep(500);
					}
				} catch (InterruptedException ex) {
					LogUtil.logError(SudokuPlugin.PLUGIN_ID, ex);
				}
			}
		});
		
		t.start();
	}
	
	/**
	 * Marks a field on the playfield red for 10 seconds.<br>
	 * This methods blocks the user interface.
	 * @param x The x-coordinate of the composite.
	 * @param y Thy y-coordinate of the composite.
	 */
	private void markRed(int x, int y) {
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						labelCellHex[x][y].setBackground(ColorService.RED);
					}
				});
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					LogUtil.logError(SudokuPlugin.PLUGIN_ID, e);
				}
				getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						labelCellHex[x][y].setBackground(ColorService.WHITE);
					}
				});
			}
		});
		
		t.start();
	}
	
	private void updateBoardDataWithUserInputHex(Text inputBox, String inputStr) {
		solved = false;
		Point point = inputBoxesHex.get(inputBox);
		int num = -1;
		if (inputStr.length() > 0) {
			num = Integer.parseInt(inputStr);
			Point pt = new Point(point.x, point.y);
			movesHex.add(pt);
			undoButton.setEnabled(true);
		}
		if (num == -1 && boardHex[point.x][point.y] != -1) {
			addPossibleHex(point.x, point.y, boardHex[point.x][point.y]);
		}
		boardHex[point.x][point.y] = num;
		labelCellHex[point.x][point.y].setBackground(ColorService.WHITE);
		boardTextHex[point.x][point.y].setBackground(ColorService.WHITE);
		updatePossibilitiesHex(boardHex, possibleHex, true);
	}
	
	private void updatePossibilitiesHex(int[][] board, List<List<List<Integer>>> possibilities, boolean button) {
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
				if (board[i][j] != -1) {
					used.add(board[i][j]);
				}
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
				if (board[j][i] != -1) {
					used.add(board[j][i]);
				}
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
		if (changed) {
			updatePossibilitiesHex(board, possibilities, button);
		}
	}
	
	/**
	 * Searchs for a field that has just one opportunity for a value and 
	 * fills the boardHex, boardTextHex and LabelCellHex  with it.
	 */
	private void fillOneHex() {
		boolean changed = false;
		for (int i = 0; i < 16 & !changed; i++) {
			for (int j = 0; j < 16 & !changed; j++) {
				if (possibleHex.get(i).get(j).size() == 1) {
					boardHex[i][j] = possibleHex.get(i).get(j).get(0);
					boardTextHex[i][j].setText(valToTextHex(boardHex[i][j]));
					labelCellHex[i][j].layout();
					markRed(i, j);
					changed = true;
				}
			}
		}
	}
	
	/**
	 * Loads a 16*16 sudoku from a file.
	 * @param fileName Path to the file that should be read.
	 * @return True, if everything worked properly. False, if something went wrong.
	 */
	private boolean loadHex(String fileName) {
		solved = false;
		loading = true;

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));

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
	
	private void clearPuzzleHex() {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				boardHex[i][j] = -1;
				boardTextHex[i][j].setText("");
				boardTextHex[i][j].setFont(FontService.getSmallFont());
				labelCellHex[i][j].setBackground(ColorService.WHITE);
				for (int k = 0; k < 8; k++) {
					boardLabelsHex[i][j][k].setText("");
				}
				possibleHex.get(i).get(j).clear();
				for (int k = 0; k < 16; k++) {
					possibleHex.get(i).get(j).add(k);
				}
			}
		}
	}
	
	private void loadPuzzleHex() {
		String fileName = openFileDialog(SWT.OPEN);

		if (fileName == null) {
			return;
		}

		loadHex(fileName);
	}
	
	private String openFileDialog(int type) {
		FileDialog dialog = new FileDialog(getDisplay().getActiveShell(), type);
		dialog.setFilterPath(DirectoryService.getUserHomeDir());
		dialog.setFilterExtensions(new String[] { "*.sud" });
		dialog.setFilterNames(new String[] { "Sudoku Files (*.sud)" });
		dialog.setOverwrite(true);
		return dialog.open();
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
	private boolean savePuzzleHex() {
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
	 * Check if there exist squares for which only one value is possible
	 *
	 * @return if this was the case
	 */
	private boolean onePossibleHex(int[][] board, List<List<List<Integer>>> possibilities, boolean button) {
		boolean changed = false;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (board[i][j] == -1 & possibilities.get(i).get(j).size() == 1) {
					board[i][j] = possibilities.get(i).get(j).get(0);
					if (button) {
						boardTextHex[i][j].setText(valToTextHex(board[i][j]));
						labelCellHex[i][j].layout();
						markRed(i, j);
						
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
	
	private void updateLabelsHex() {
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
	
	/**
	 * Checks if there exists a square which is a "naked single"
	 *
	 * @return if this was the case
	 */
	private boolean nakedSingleHex(int[][] board, List<List<List<Integer>>> possibilities, boolean button) {
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
							markRed(i, j);
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
	private boolean hiddenSingleHex(int[][] board, List<List<List<Integer>>> possibilities, boolean button) {
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
										markRed(i, j);
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
											markRed(i, j);
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
	private boolean blockAndCRHex(int[][] board, List<List<List<Integer>>> possibilities) {
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
	
	private boolean nakedSubsetHex(int[][] board, List<List<List<Integer>>> possibilities) {
		boolean changed = false;
		boolean temp;
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
								.equals(possibilities.get(j).get(set.elementAt(k)))) {
							set.remove(k);
						}
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
				if (possibilities.get(k).get(j).size() == i & board[k][j] == -1) {
					set.add(k);
				}
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
								.equals(possibilities.get(set.elementAt(k)).get(j))) {
							set.remove(k);
						}
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
								& board[4 * j1 + k1][4 * j2 + k2] == -1) {
							pointSet.add(new Point(4 * j1 + k1, 4 * j2 + k2));
						}
					}
				}
				while (pointSet.size() >= i) {
					total = 1;
					for (int k = 1; k < pointSet.size(); k++) {
						if (possibilities.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y)
								.equals(possibilities.get(pointSet.elementAt(k).x).get(pointSet.elementAt(k).y))) {
							total++;
						}
					}
					if (total != i) {
						pointSet.remove(0);
					} else {
						for (int k = pointSet.size() - 1; k > 0; k--) {
							if (!possibilities.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y)
									.equals(possibilities.get(pointSet.elementAt(0).x).get(pointSet.elementAt(0).y))) {
								pointSet.remove(k);
							}
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
					if (possibilities.get(j).get(k).size() <= i & board[j][k] == -1) {
						set.add(k);
					}
				}
				if (set.size() >= i) {
					intSet = new int[set.size()];
					for (int k = 0; k < set.size(); k++) {
						intSet[k] = set.elementAt(k);
					}
//					System.out.println("Zeile 2106 vor Aufruf von generateSubsets");
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
								if (goodSubsets.elementAt(k1)[k3] == k2) {
									temp = false;
								}
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
					if (possibilities.get(k).get(j).size() <= i & board[k][j] == -1) {
						set.add(k);
					}
				}
				if (set.size() >= i) {
					intSet = new int[set.size()];
					for (int k = 0; k < set.size(); k++) {
						intSet[k] = set.elementAt(k);
					}
//					System.out.println("Zeile 2170 vor Aufruf von generateSubsets");
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
									& board[4 * j1 + k1][4 * j2 + k2] == -1) {
								pointSet.add(new Point(4 * j1 + k1, 4 * j2 + k2));
							}
						}
					}
					if (pointSet.size() >= i) {
						intSet = new int[pointSet.size()];
						for (int k = 0; k < pointSet.size(); k++) {
							intSet[k] = k;
						}
//						System.out.println("Zeile 2239 vor Aufruf von generateSubsets");
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
												.equals(new Point(4 * j1 + k2, 4 * j2 + k3)) == true) {
											temp = false;
										}
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
	
	private boolean candidateLineHex(int[][] board, List<List<List<Integer>>> possibilities) {
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
	
	private boolean doublePairHex(int[][] board, List<List<List<Integer>>> possibilities) {
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
	
	private boolean multipleLinesHex(int[][] board, List<List<List<Integer>>> possibilities) {
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
	
	private void showErroneousEntries() {
		if (backgroundSolved) {
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					if (boardHex[i][j] != -1 && boardHex[i][j] != tempBoard[i][j]) {
						labelCellHex[i][j].setBackground(ColorService.RED);
						boardTextHex[i][j].setBackground(ColorService.RED);
					}
				}
			}
		}
	}
	
	/**
	 * Attention! The method is overloaded. 
	 * So there are other methods with the same 
	 * name but different parameters.
	 * @param board
	 * @return
	 */
	private boolean checkPuzzleHex(int[][] board) {
//		counterCheckPuzzleHex++;
//		System.out.println("Anzahl checkPuzzleHex:\t" + counterCheckPuzzleHex);
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
	
	/**
	 * Attention! The method is overloaded. 
	 * So there are other methods with the same 
	 * name but different parameters.
	 * @param board
	 * @param x
	 * @param y
	 * @param c
	 * @return
	 */
	private boolean checkPuzzleHex(int[][] board, int x, int y, int c) {
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
	
	private boolean isConflictHex(int[][] board, int x, int y, int c) {
		return rowConflictHex(board, y, c) || colConflictHex(board, x, c) || boxConflictHex(board, x, y, c);
	}
	
	private boolean rowConflictHex(int[][] board, int y, int c) {
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
	
	private boolean colConflictHex(int[][] board, int x, int c) {
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
	
	private boolean boxConflictHex(int[][] board, int xx, int yy, int c) {
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
	
	private Label createLabelHex(Composite parent, int k) {
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
		label.setSize(54 / 3, 54 / 3);
		label.setFont(FontService.getTinyFont());
		return label;
	}
	
	private Text createTextHex(Composite parent) {
		Text input = new Text(parent, SWT.CENTER);
		input.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		input.setBackground(ColorService.WHITE);
		input.setTextLimit(1);
		input.setFont(FontService.getSmallFont());

		input.addListener(SWT.Verify, new Listener() {
			@Override
			public void handleEvent(Event e) {
				String input = e.text;
				Text textbox = (Text) e.widget;
				Point point = inputBoxesHex.get(textbox);
				//The case when an entry is removed
				if (input.length() == 0 && !loading && !solving) {
					updateBoardDataWithUserInputHex(textbox, input);
				}
				if (!solved && !loading && !solving) {
					input = input.toUpperCase();
					if (input.equals("A") && possibleHex.get(point.x).get(point.y).indexOf(10) != -1) {
						updateBoardDataWithUserInputHex(textbox, "10");
					} else if (input.equals("B") && possibleHex.get(point.x).get(point.y).indexOf(11) != -1) {
						updateBoardDataWithUserInputHex(textbox, "11");
					} else if (input.equals("C") && possibleHex.get(point.x).get(point.y).indexOf(12) != -1) {
						updateBoardDataWithUserInputHex(textbox, "12");
					} else if (input.equals("D") && possibleHex.get(point.x).get(point.y).indexOf(13) != -1) {
						updateBoardDataWithUserInputHex(textbox, "13");
					} else if (input.equals("E") && possibleHex.get(point.x).get(point.y).indexOf(14) != -1) {
						updateBoardDataWithUserInputHex(textbox, "14");
					} else if (input.equals("F") && possibleHex.get(point.x).get(point.y).indexOf(15) != -1) {
						updateBoardDataWithUserInputHex(textbox, "15");
					} else {
						//Der check auf Längen > 1 ist sinnlos, da das text Limit von Text input 1 ist.
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
					e.text = input;
				}
			}
		});

		//Return the textfield
		return input;
	}
	
	private boolean createsZeroPossible(Point point, int input) {
		boolean returnValue = false;
		Vector<Point> affectedPointsH = new Vector<Point>();
		Vector<Point> affectedPointsV = new Vector<Point>();
		Vector<Point> affectedPointsS = new Vector<Point>();

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
				|| checkSubset(affectedPointsS, possibleHex, input)) {
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
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					if (boardHex[i][j] != -1 && boardHex[i][j] != tempBoard[i][j]) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void generateSubsets(Vector<Integer[]> subsets, int[] set, int[] subset, int subsetSize, int nextIndex) {
//		counterGenerateSubsets++;
//		System.out.print("Aufruf generateSubsets:\t" + counterGenerateSubsets);
//		counterGenerateSubsetsRecursiveCalls++;
//		System.out.println("\t generateSubsetsRecursiceCall:\t" + counterGenerateSubsetsRecursiveCalls);

		if (subsetSize == subset.length) {
			Integer[] temp = new Integer[subset.length];
			for (int i = 0; i < subset.length; i++) {
				temp[i] = subset[i];
			}
			subsets.add(temp);
		} else {
			for (int j = nextIndex; j < set.length; j++) {
				subset[subsetSize] = set[j];
				generateSubsets(subsets, set, subset, subsetSize + 1, j + 1);
			}
		}
//		counterGenerateSubsetsRecursiveCalls = 0;
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
							.indexOf(possible.get(sortedPoints.get(j).x).get(sortedPoints.get(j).y).get(k)) == -1) {
						maxSubset.add(possible.get(sortedPoints.get(j).x).get(sortedPoints.get(j).y).get(k));
					}
				}
			}
			if (maxSubset.size() < maxIndex + 1) {
				return true;
			}
		}
		return false;
	}
	
	private void addPossibleHex(int x, int y, int val) {
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
	
}

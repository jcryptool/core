// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.fleissner.UI;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.analysis.fleissner.Activator;
import org.jcryptool.analysis.fleissner.key.Grille;
import org.jcryptool.analysis.fleissner.key.KeySchablone;
import org.jcryptool.analysis.fleissner.logic.MethodApplication;
import org.jcryptool.analysis.fleissner.logic.ParameterSettings;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.crypto.ui.background.BackgroundJob;
import org.jcryptool.crypto.ui.textloader.ui.wizard.TextLoadController;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.jobs.ProgressProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

public class FleissnerWindow extends Composite {

	private Grille model;
	private Composite mainComposite;
	private Composite method;
	private Composite process;
	private Group key;
	private Group inOutText;
	private Group plaintextComposite;
	private Group ciphertextComposite;
	private Group analysis;
	private Group textSelectionGroup;
	private Group analysisSettingsGroup;
	private Group methodComposite;
	private Canvas canvasKey;
	private KeyListener keyListener;
	private Text plaintext;
	private Text ciphertext;
	private Text analysisOutput;
	private Text chooseLanguage;
	private Text descriptionText;
	private Label statisticNameIdentifier;
	private Text loadedStatisticName;
	private Text keyText;
	private Label textNameIdentifier;
	private Text loadedTextName;
	private Spinner keySize;
	private Spinner restarts;
	private Spinner nGramSize;
	private Button analyze;
	private Button encrypt;
	private Button decrypt;
	private Button writeText;
	private Button statistics;
	private Button start;
	private Button statisticsLoad;
	private Button loadStatistics;
	private Button exampleText;
	private Button loadOwntext;
	private Button loadText;
	private Button deleteHoles;
	private Button randomKey;
	private Button logOutput;
	private Combo language;
	private Combo chooseExample;
	private Combo selectStatistic;
	private Label numberOfRestarts;
	private Label chooseNGramSize;
	private boolean plain = false;
	private boolean userText = false;
	private boolean userStatistics = false;
	private boolean startSettings = true;
//    private Hashtable<Integer, Integer> htRestarts = new Hashtable<Integer, Integer>();

	private int textState = 0;
	private int languageState = 0;
	private int statisticState = 0;
	private int statisticInputState = 0;
	private int textInputState = 0;
	private int[] keyToLogic = null;
	private String argMethod = null;
	private String argText = Messages.FleissnerWindow_empty;
	private String argKey = null;
	private double[] argStatistics = null;
	private String argLanguage = null;
	private String[] args;
	private String textName;
	private String statisticName;
	private String oldNgramSize = null;
	private String oldLanguage = null;
	private InputStream fis = null;
	private InputStream fisOld = null;
	private LoadFiles lf = new LoadFiles();
	private OutputDialog dialog;
//    private String dialogOutput;
	private ArrayList<String> outputInput;
	private String lastSuccessfulLoadedTextName;
	private String lastSuccessfulLoadedText;
	private TextInputWithSource lastSuccessfulLoadedTextSource;
	
	private Composite composite0;
	private TextLoadController textloader;
	private TextInputWithSource source;
	private String text;
	
	private static final int fleissner_max_text_length = 1000000;

	/**
	 * Constructor creates header and main, sets new default grille
	 * 
	 * @param parent
	 * @param style
	 */
	public FleissnerWindow(Composite parent, int style) {
		super(parent, style);

		model = new Grille();
		model.setKey(new KeySchablone(7));

		createMain(this);

		startSettings = false;
		reset();
	}

	/**
	 * creates main composite that includes all information except of header
	 * 
	 * @param parent
	 */
	private void createMain(Composite parent) {

		mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(3, false));
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createHeader(mainComposite);
		createMethod(mainComposite);
		createKey(mainComposite);
		createInOutText(mainComposite);
		Composite platzHalter = new Composite(mainComposite, SWT.NONE);
		platzHalter.setLayout(new GridLayout());
		platzHalter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		createText(mainComposite);
		createAnalysisOutput(mainComposite);
	}

	/**
	 * creates method composite where one of the three methods can be choosen,
	 * method can be started or example analysis can be run
	 * 
	 * @param parent
	 */
	private void createMethod(Composite parent) {

		methodComposite = new Group(parent, SWT.NONE);
		methodComposite.setLayout(new GridLayout());
		methodComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
		methodComposite.setText(Messages.FleissnerWindow_methods);

		method = new Composite(methodComposite, SWT.NONE);
		method.setLayout(new GridLayout());
		method.setLayoutData(new GridData(SWT.FILL, SWT.UP, false, true));

		process = new Composite(methodComposite, SWT.NONE);
		process.setLayout(new GridLayout());
		process.setLayoutData(new GridData(SWT.FILL, SWT.DOWN, false, false));

		analyze = new Button(method, SWT.RADIO);
		analyze.setText(Messages.FleissnerWindow_label_analyze);
		analyze.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
//                only execute after plugin has already been started
				if (!startSettings) {
//                    only execute if analyze as method has been selected, not through switching between other radio buttons in same composite
					if (analyze.getSelection()) {
//                        only execute if 'analyze' was not already selected
						if (!argMethod.equals(Messages.FleissnerWindow_method_analyze)) {
							analyze();
							reset();
						}
					} else {
						analysisOutput.setText(Messages.FleissnerWindow_output_progress);
//                        dialogOutput = analysisOutput.getText();
					}
				}
			}
		});
		analyze.setSelection(true);
		argMethod = Messages.FleissnerWindow_method_analyze;

		encrypt = new Button(method, SWT.RADIO);
		encrypt.setText(Messages.FleissnerWindow_label_encrypt);
		encrypt.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (encrypt.getSelection()) {
//                  only execute if 'encrypt' was not already selected
					if (!argMethod.equals(Messages.FleissnerWindow_method_encrypt)) {
						encrypt();
						reset();
					}
				}
			}
		});

		decrypt = new Button(method, SWT.RADIO);
		decrypt.setText(Messages.FleissnerWindow_label_decrypt);
		decrypt.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (decrypt.getSelection()) {
//                  only execute if 'decrypt' was not already selected
					if (!argMethod.equals(Messages.FleissnerWindow_method_decrypt)) {
						decrypt();
						reset();
					}
				}
			}
		});

		GridData methods = new GridData(SWT.FILL, SWT.TOP, true, true);
		analyze.setLayoutData(methods);
		encrypt.setLayoutData(methods);
		decrypt.setLayoutData(methods);

		start = new Button(process, SWT.PUSH);
		start.setText(Messages.FleissnerWindow_start_button);
		start.setEnabled(true);
		start.setToolTipText(Messages.FleissnerWindow_toolTipText_start);
		start.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					startMethod();
				} catch (IllegalArgumentException e1) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.FleissnerWindow_error_noMethod, e1, true);
				}
			}
		});

		GridData startOptions = new GridData(SWT.FILL, SWT.TOP, true, true);
		start.setLayoutData(startOptions);
	}

	/**
	 * creates key composite including key grille, grille length and buttons 'random
	 * key' and 'delete key' to respectively generate a random key or delete the
	 * chosen holes
	 * 
	 * @param parent
	 */
	private void createKey(Composite parent) {

		key = new Group(parent, SWT.NONE);
		key.setLayout(new GridLayout(3, false));
		key.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
		key.setText(Messages.FleissnerWindow_label_key);

//        canvas for grille with fixed height and width
		canvasKey = new Canvas(key, SWT.DOUBLE_BUFFERED);
		canvasKey.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		canvasKey.addPaintListener(new KeyPainter(canvasKey, model));
		keyListener = new org.jcryptool.analysis.fleissner.UI.KeyListener(model, this);
		canvasKey.addMouseListener(keyListener);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 4);
		gridData.widthHint = 201;
		gridData.heightHint = 201;
		canvasKey.setLayoutData(gridData);
		canvasKey.setEnabled(false);

		Label spinner = new Label(key, SWT.NONE);
		spinner.setText(Messages.FleissnerWindow_label_keyLength);
		GridData gd_spinner = new GridData(SWT.FILL, SWT.TOP, false, true);
		gd_spinner.horizontalIndent = 20;
		spinner.setLayoutData(gd_spinner);

		keySize = new Spinner(key, SWT.NONE);
		keySize.setMinimum(2);
		keySize.setMaximum(20);
		keySize.setIncrement(1);
		keySize.setSelection(7);
		keySize.setEnabled(true);
		GridData gd_keySize = new GridData(SWT.LEFT, SWT.TOP, false, true);
		gd_keySize.horizontalIndent = 20;
		keySize.setLayoutData(gd_keySize);
		keySize.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
//            refreshes key with changing key sizes and refreshes Example text, if needed
			public void widgetSelected(SelectionEvent e) {
				if (Integer.parseInt(keySize.getText()) > 20 || Integer.parseInt(keySize.getText()) < 2)
					keySize.setSelection(7);
				model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
				canvasKey.redraw();
				updateKeyText();
				canvasKey.removeMouseListener(keyListener);
				canvasKey.addMouseListener(keyListener);
				if (keySize.getSelection() < 5)
					restarts.setEnabled(false);
				else
					restarts.setEnabled(true);

				if (exampleText.getSelection() && !plain)
					refreshExampleText();
				reset();
			}
		});

		randomKey = new Button(key, SWT.PUSH);
		GridData gd_setHoles = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 2, 1);
		gd_setHoles.horizontalIndent = 20;
		randomKey.setLayoutData(gd_setHoles);
		randomKey.setEnabled(false);
		randomKey.setText(Messages.FleissnerWindow_label_randomKey);
		randomKey.setToolTipText(Messages.FleissnerWindow_toolTipText_randomKey);
		randomKey.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				generateRandomKey();
				reset();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		deleteHoles = new Button(key, SWT.PUSH);
		GridData gd_deleteHoles = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 2, 1);
		gd_deleteHoles.horizontalIndent = 20;
		deleteHoles.setLayoutData(gd_deleteHoles);
		deleteHoles.setEnabled(false);
		deleteHoles.setText(Messages.FleissnerWindow_label_deleteKey);
		deleteHoles.setToolTipText(Messages.FleissnerWindow_toolTipText_deleteKey);
		deleteHoles.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteHoles();
				reset();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		keyText = new Text(key, SWT.H_SCROLL);
		GridData grid = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
//        limits text field width and height so text will wrap at the end of composite
		grid.widthHint = key.getSize().y;
		keyText.setLayoutData(grid);
		keyText.setEditable(false);
	}

	/**
	 * creates composite for plaintext and ciphertext
	 * 
	 * @param parent
	 */
	private void createInOutText(Composite parent) {

		inOutText = new Group(parent, SWT.NONE);
		inOutText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 6));
		GridLayout gl_inOutText = new GridLayout(1, false);
		inOutText.setLayout(gl_inOutText);

		createPlaintext(inOutText);

		createCiphertext(inOutText);
	}

	/**
	 * creates and controls plaintext composite
	 * 
	 * @param parent
	 */
	private void createPlaintext(Composite parent) {

		plaintextComposite = new Group(parent, SWT.NONE);
		plaintextComposite.setLayout(new GridLayout());
		plaintextComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		plaintextComposite.setText(Messages.FleissnerWindow_label_plaintext + "(0)"); //$NON-NLS-1$

		plaintext = new Text(plaintextComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//        limits text field width and height so text will wrap at the end of composite
		gridData.widthHint = plaintextComposite.getSize().y;
		gridData.heightHint = plaintextComposite.getSize().x;
		plaintext.setLayoutData(gridData);
		plaintext.setBackground(ColorService.WHITE);
		plaintext.setEditable(false);
		plaintext.setEnabled(false);
		plaintext.addKeyListener(new org.eclipse.swt.events.KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				e.doit = true;
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});
		plaintext.addModifyListener(new ModifyListener() {

			@Override
//            parameter 'argText' is set every time plaintext input is modified
			public void modifyText(ModifyEvent e) {
				plaintextComposite
						.setText(Messages.FleissnerWindow_label_plaintext + " (" + plaintext.getText().length() + ")"); //$NON-NLS-1$//$NON-NLS-2$
				updateCiphertext();
				if (!startSettings) {
					if (writeText.getSelection() && encrypt.getSelection()) {
						argText = plaintext.getText();
						reset();
					}
				}
			}
		});
	}

	/**
	 * creates and controls ciphertext composite
	 * 
	 * @param parent
	 */
	private void createCiphertext(Composite parent) {

		ciphertextComposite = new Group(parent, SWT.NONE);
		ciphertextComposite.setLayout(new GridLayout());
		ciphertextComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		ciphertextComposite.setText(Messages.FleissnerWindow_label_ciphertext + " (0)"); //$NON-NLS-1$

		ciphertext = new Text(ciphertextComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//        limits text field width and height so text will wrap at the end of composite
		gridData.widthHint = ciphertextComposite.getSize().y;
		gridData.heightHint = ciphertextComposite.getSize().x;
		ciphertext.setLayoutData(gridData);
		ciphertext.setEnabled(true);
		ciphertext.setEditable(false);
		ciphertext.setBackground(ColorService.WHITE);
//        default text
		textName = Messages.FleissnerWindow_file_ciphertext; // $NON-NLS-1$
		argText = lf.InputStreamToString(lf.openMyTestStream(textName));
		refreshInOutTexts();

		ciphertext.addKeyListener(new org.eclipse.swt.events.KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				e.doit = true;
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});

		ciphertext.addModifyListener(new ModifyListener() {

			@Override
//          parameter 'argText' is set every time ciphertext input is modified
			public void modifyText(ModifyEvent e) {
				ciphertextComposite.setText(
						Messages.FleissnerWindow_label_ciphertext + " (" + ciphertext.getText().length() + ")"); //$NON-NLS-1$//$NON-NLS-2$
				updatePlaintext();
				if (!startSettings) {
					if (writeText.getSelection() && !encrypt.getSelection()) {
						argText = ciphertext.getText();
						reset();
					}
				}
			}
		});
		ciphertextComposite
				.setText(Messages.FleissnerWindow_label_ciphertext + " (" + ciphertext.getText().length() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * creates composites for text selection and analysis settings. Text selection
	 * manages the text input. Analysis settings manages the used language, number
	 * of restarts and used language statistics as well as statistics input
	 * 
	 * @param parent
	 */
	private void createText(Composite parent) {

		textSelectionGroup = new Group(parent, SWT.NONE);
		textSelectionGroup.setLayout(new GridLayout(4, false));
		textSelectionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		textSelectionGroup.setText(Messages.FleissnerWindow_label_textChoiceCipher);

		Composite platzHalter = new Composite(parent, SWT.NONE);
		platzHalter.setLayout(new GridLayout());
		platzHalter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

		analysisSettingsGroup = new Group(parent, SWT.NONE);
		analysisSettingsGroup.setLayout(new GridLayout(4, false));
		analysisSettingsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		analysisSettingsGroup.setText(Messages.FleissnerWindow_label_analysisSettings);
		analysisSettingsGroup.setEnabled(true);

		createLoadtextComposite(textSelectionGroup);

		Composite languageAndRestarts = new Composite(analysisSettingsGroup, SWT.NONE);
		languageAndRestarts.setLayout(new GridLayout(4, false));
		languageAndRestarts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));

		Group statisticGroup = new Group(analysisSettingsGroup, SWT.NONE);
		statisticGroup.setLayout(new GridLayout(4, false));
		statisticGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));
		statisticGroup.setText(Messages.FleissnerWindow_label_statisticChoice);

		numberOfRestarts = new Label(languageAndRestarts, SWT.NONE);
		numberOfRestarts.setText(Messages.FleissnerWindow_label_restarts);
		numberOfRestarts.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));

		restarts = new Spinner(languageAndRestarts, SWT.NONE);
		restarts.setMinimum(1);
		restarts.setMaximum(1400);
		restarts.setIncrement(1);
		restarts.setSelection(5);
		restarts.setEnabled(true);
		restarts.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 2, 1));
		restarts.setToolTipText(Messages.FleissnerWindow_toolTipText_restarts);

		createLoadstatisticsComposite(statisticGroup);
	}

	/**
	 * creates buttons and controls in text selection composite
	 * 
	 * @param thirdGroup
	 */
	private void createLoadtextComposite(Composite thirdGroup) {
		String[] items = { Messages.FleissnerWindow_textSelect_dawkinsGer, Messages.FleissnerWindow_textSelect_wikiGer,
				Messages.FleissnerWindow_textSelect_dawkinsEng, Messages.FleissnerWindow_textSelect_wikiEng };
		String[] langItems = { Messages.FleissnerWindow_languageSelect_german,
				Messages.FleissnerWindow_languageSelect_english };

		chooseLanguage = new Text(thirdGroup, SWT.WRAP | SWT.MULTI);
		chooseLanguage.setText(Messages.FleissnerWindow_label_languageChoice);
		chooseLanguage.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
		chooseLanguage.setBackground(ColorService.LIGHTGRAY);

		language = new Combo(thirdGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		language.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		language.setItems(langItems);

//        default language
		if (Locale.getDefault().toString().equals("de")) {
			language.select(0);
			argLanguage = Messages.FleissnerWindow_language_german;
		} else {
			language.select(1);
			argLanguage = Messages.FleissnerWindow_language_english;
		}
		language.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {

//                listener should only react when another item has been chosen
				if (languageState != language.getSelectionIndex()) {
					updateLanguageSettings(Messages.FleissnerWindow_case_language);
					if (statistics.getSelection()) {
//                        changes example statistic when language changed
						refreshStatistics(null);
					}
					if (exampleText.getSelection()) {
//                        changes example text when language changed
						refreshExampleText();
						textState = chooseExample.getSelectionIndex();
					}
					setArgLanguage();
					languageState = language.getSelectionIndex();
					reset();
				}
			}
		});

		exampleText = new Button(thirdGroup, SWT.RADIO);
		exampleText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		exampleText.setText(Messages.FleissnerWindow_label_exampleTextCipher);
		exampleText.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (exampleText.getSelection()) {
					if (textInputState != 0) {
						//loadedTextName.setText(Messages.FleissnerWindow_empty);
						userText = false;
						textSelection(true, false, false, false);
						refreshExampleText();
						textInputState = 0;
						//textloader.setEnabled(false);
					}
					reset();
				}
			}
		});
		exampleText.setSelection(true);

		chooseExample = new Combo(thirdGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		chooseExample.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		chooseExample.setItems(items);
		if (Locale.getDefault().toString().equals("de")) {
			chooseExample.select(0);
		} else {
			chooseExample.select(2);
		}

		chooseExample.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (textState != chooseExample.getSelectionIndex()) {
					userText = false;
					refreshExampleText();

					if (checkTextLangChange()) {
						updateLanguageSettings(Messages.FleissnerWindow_case_text);
						if (statistics.getSelection()) {
							refreshStatistics(null);
						}
						setArgLanguage();
						languageState = language.getSelectionIndex();
					}
					if (argMethod.equals(Messages.FleissnerWindow_method_analyze)) {
						analysisOutput.setText(Messages.FleissnerWindow_output_progress);
//                        dialogOutput = analysisOutput.getText();
					}

					textState = chooseExample.getSelectionIndex();
					reset();
				
				}
			}
		});

		writeText = new Button(thirdGroup, SWT.RADIO);
		writeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
		writeText.setText(Messages.FleissnerWindow_label_writeTextCipher);
		writeText.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (writeText.getSelection()) {
					
//                    sets plaintext or ciphertext editable for encryption respectively analysis or decryption
					//loadedTextName.setText(Messages.FleissnerWindow_empty);
					boolean editPlaintext = false;
					boolean editCiphertext = false;
					userText = true;

					if (plain) {
						editPlaintext = true;
						editCiphertext = false;
					} else {
						editPlaintext = false;
						editCiphertext = true;
					}
					if (textInputState != 2) {

						deleteHoles();
						argText = Messages.FleissnerWindow_empty; // $NON-NLS-1$
						refreshInOutTexts();
					}
					textSelection(false, false, editPlaintext, editCiphertext);
					reset();
					textInputState = 2;
					//textloader.setEnabled(false);
				}
			}
		});
		
		
		loadOwntext = new Button(thirdGroup, SWT.RADIO);
		loadOwntext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		loadOwntext.setText(Messages.FleissnerWindow_label_loadOwnTextCipher);
		loadOwntext.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (loadOwntext.getSelection()) {

					if (textInputState != 1) {
						deleteHoles();
						userText = true;
						argText = Messages.FleissnerWindow_empty;
						textName = Messages.FleissnerWindow_empty;
						refreshInOutTexts();
						textSelection(false, true, false, false);
						textInputState = 1;
						reset();
						//textloader.setEnabled(true);
					}
				}
			}
		});
		
		

		textloader = new TextLoadController(thirdGroup, this, SWT.PUSH, true, false);
		textloader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		textloader.setEnabled(false);
		
		textloader.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				
				if (textloader.getText() != null) {
					
					if(textloader.getText().getText().length() < fleissner_max_text_length) {
					argText = textloader.getText().getText();
					userText = true;
					lastSuccessfulLoadedTextSource = textloader.getText();
					source = textloader.getText();
					}else{
						
						boolean result = MessageDialog.openQuestion(FleissnerWindow.this.getShell(), Messages.FleissnerWindow_warning, Messages.FleissnerWindow_warning_text);
						if(result) {
							argText = textloader.getText().getText();
							userText = true;
							source = textloader.getText();
						}
						else {
							
							source = lastSuccessfulLoadedTextSource;
							return;
						}
					}	
					
					if (argMethod.equals(Messages.FleissnerWindow_method_analyze)) {
						analysisOutput.setText(Messages.FleissnerWindow_output_progress);
					}

					refreshInOutTexts();
					reset();
				}
			}
		});
		
		/*
		
		loadText = new Button(thirdGroup, SWT.PUSH);
		loadText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		loadText.setText(Messages.FleissnerWindow_label_loadText);
		loadText.setEnabled(false);
		loadText.setToolTipText(Messages.FleissnerWindow_toolTipText_loadText);
		loadText.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// opening file with fileDialog
				String filename = lf.openFileDialog(SWT.OPEN);
				// Check if the user selected a file. If not filename will by null.
				// This check avoids exceptions.
				if (filename != null) {
					String[] a = filename.split("\\\\"); //$NON-NLS-1$
					textName = filename;
					// load opened file
					argText = loadNormal(filename);
					userText = true;
					// display fileName to user
					loadedTextName.setText(lastSuccessfulLoadedTextName);
					//loadedTextName.setText(a[a.length - 1]);

					if (argMethod.equals(Messages.FleissnerWindow_method_analyze)) {
						analysisOutput.setText(Messages.FleissnerWindow_output_progress);
//                    dialogOutput = analysisOutput.getText();
					}

					refreshInOutTexts();
					reset();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		

		textNameIdentifier = new Label(thirdGroup, SWT.NONE);
		textNameIdentifier.setText(Messages.FleissnerWindow_label_uploadedText);
		textNameIdentifier.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));

		loadedTextName = new Text(thirdGroup, SWT.NONE);
		loadedTextName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		loadedTextName.setBackground(ColorService.LIGHTGRAY);
		*/
	}

	/**
	 * creates and controls buttons and other tools for analysis settings
	 * 
	 * @param thirdGroup
	 */
	private void createLoadstatisticsComposite(Group thirdGroup) {
		String[] items = { Messages.FleissnerWindow_statisticSelect_4gramGer,
				Messages.FleissnerWindow_statisticSelect_4gramEng, Messages.FleissnerWindow_statisticSelect_3gramEng };

		statistics = new Button(thirdGroup, SWT.RADIO);
		statistics.setSelection(true);
		statistics.setText(Messages.FleissnerWindow_label_statistic);
		statistics.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (statistics.getSelection()) {
					if (statisticInputState != 0) {
						loadedStatisticName.setText(Messages.FleissnerWindow_empty);
						userStatistics = false;
						statisticSelection();
						updateLanguageSettings(Messages.FleissnerWindow_case_text);
						refreshStatistics(null);
						reset();
						statisticInputState = 0;
					}
				}
			}
		});

		selectStatistic = new Combo(thirdGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		selectStatistic.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		selectStatistic.setItems(items);
		if (Locale.getDefault().toString().equals("de")) {
			selectStatistic.select(0);
		} else {
			selectStatistic.select(1);
		}

		refreshStatistics(null);
		selectStatistic.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (statisticState != selectStatistic.getSelectionIndex()) {
					if (selectStatistic.getSelectionIndex() == 2) {
						nGramSize.setSelection(3);
					} else {
						nGramSize.setSelection(4);
					}
					if (statistics.getSelection()) {
						refreshStatistics(null);
					}
					if (checkStatisticLangChange()) {
						updateLanguageSettings(Messages.FleissnerWindow_case_statistic);
						if (exampleText.getSelection()) {
							refreshExampleText();
							textState = chooseExample.getSelectionIndex();
						}
						setArgLanguage();
						languageState = language.getSelectionIndex();
					}
					reset();
				}
			}
		});

		statisticsLoad = new Button(thirdGroup, SWT.RADIO);
		statisticsLoad.setText(Messages.FleissnerWindow_label_statisticLoad);
		statisticsLoad.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (statisticsLoad.getSelection()) {
					if (statisticInputState != 1) {
						userStatistics = true;
						refreshStatistics(null);
						statisticSelection();
						reset();
						statisticInputState = 1;
					}
				}
			}
		});

		loadStatistics = new Button(thirdGroup, SWT.PUSH);
		loadStatistics.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		loadStatistics.setText(Messages.FleissnerWindow_label_loadStatistics);
		loadStatistics.setEnabled(false);
		loadStatistics.setToolTipText(Messages.FleissnerWindow_toolTipText_loadStatistics);
		loadStatistics.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
//                 open user statistic with fileDialog
				String filename = lf.openStatFileDialog(SWT.OPEN);
				userStatistics = true;
//                 set fileInputStream from filename
				refreshStatistics(filename);
				String[] a = filename.split("\\\\"); //$NON-NLS-1$
				loadedStatisticName.setText(a[a.length - 1]);
				reset();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		chooseNGramSize = new Label(thirdGroup, SWT.NONE);
		chooseNGramSize.setText(Messages.FleissnerWindow_label_NgramSize);
		chooseNGramSize.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));

//         nGramSize currently limited to 3-4 because there are no other working nGrams with the same structure
		nGramSize = new Spinner(thirdGroup, SWT.NONE);
		nGramSize.setMinimum(3);
		nGramSize.setMaximum(4);
		nGramSize.setIncrement(1);
		nGramSize.setSelection(4);
		nGramSize.setEnabled(false);
		nGramSize.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));
		nGramSize.setToolTipText(Messages.FleissnerWindow_toolTipText_nGramSize);

		statisticNameIdentifier = new Label(thirdGroup, SWT.NONE);
		statisticNameIdentifier.setText(Messages.FleissnerWindow_label_uploadedStatistics);
		statisticNameIdentifier.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));

		loadedStatisticName = new Text(thirdGroup, SWT.WRAP | SWT.MULTI);
		loadedStatisticName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		loadedStatisticName.setBackground(ColorService.LIGHTGRAY);
	}

	/**
	 * creates composite for analysis output where all results if analysis will be
	 * displayed
	 * 
	 * @param parent
	 */
	private void createAnalysisOutput(Composite parent) {

		analysis = new Group(parent, SWT.NONE);
		analysis.setLayout(new GridLayout(2, false));
		GridData grid = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		analysis.setLayoutData(grid);
		analysis.setText(Messages.FleissnerWindow_label_output);

		Shell shell = new Shell();

		logOutput = new Button(analysis, SWT.PUSH);
		logOutput.setLayoutData(new GridData(SWT.FILL, SWT.UP, false, false));
		logOutput.setText(Messages.FleissnerWindow_label_outputDetails);
		logOutput.setEnabled(false);
		logOutput.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				dialog = new OutputDialog(shell, outputInput);
				dialog.create(Messages.FleissnerWindow_label_dialogOutput,
						Messages.FleissnerWindow_label_dialogDescription);
				dialog.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		analysisOutput = new Text(analysis, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		GridData gridOut = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridOut.heightHint = 150;
		analysisOutput.setLayoutData(gridOut);
		analysisOutput.setText(Messages.FleissnerWindow_output_progress);
		analysisOutput.setEditable(false);
		analysisOutput.setBackground(ColorService.WHITE);
		analysisOutput.setFont(FontService.getNormalMonospacedFont());

//        dialogOutput = new String(analysisOutput.getText());
	}

	/**
	 * reads choosen .txt file into a String
	 * 
	 * @param fileName
	 * @return the input String
	 */
	private String loadNormal(String fileName) {

		BufferedReader reader = null;
		String text = Messages.FleissnerWindow_empty;
		String text_buffer = Messages.FleissnerWindow_empty;

		try {
//			InputStream inputStream = new FileInputStream(fileName);
//			String nimm_diesen = inputStreamToString(inputStream);
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), IConstants.UTF8_ENCODING));
			String line = reader.readLine();
			
			int count = 1;
			while (line != null) {
				
				text_buffer += line;
				LogUtil.logInfo(count + Messages.FleissnerWindow_infoMessage_read + text);
				count++;
				line = reader.readLine();
			}
			System.out.println(text_buffer.length());
			if(text_buffer.length() < fleissner_max_text_length) {
				
				text = text_buffer;
				lastSuccessfulLoadedText = text_buffer;
				lastSuccessfulLoadedTextName = fileName;
				
			}else {
				boolean result = MessageDialog.openQuestion(FleissnerWindow.this.getShell(), Messages.FleissnerWindow_warning, Messages.FleissnerWindow_warning_text);
				if(result) {
					text = text_buffer;
					lastSuccessfulLoadedText = text_buffer;
					lastSuccessfulLoadedTextName = fileName;
				}
				else {
					text = lastSuccessfulLoadedText;
					
				}
			}
			
			
		} catch (NumberFormatException nfe) {
			LogUtil.logError(Activator.PLUGIN_ID, nfe);
			MessageBox brokenFile = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
			brokenFile.setText(Messages.FleissnerWindow_error_thereIsAProblem);
			brokenFile.setMessage(Messages.FleissnerWindow_error_textCouldNotBeLoaded);
			brokenFile.open();

		} catch (FileNotFoundException e) {
			LogUtil.logError(Activator.PLUGIN_ID, Messages.FleissnerWindow_error_textCouldNotBeLoaded, e, true);
			return Messages.FleissnerWindow_error_textCouldNotBeLoaded;

		} catch (IOException e) {
			LogUtil.logError(Activator.PLUGIN_ID, Messages.FleissnerWindow_error_textCouldNotBeLoaded, e, true);
			return Messages.FleissnerWindow_error_textCouldNotBeLoaded;

		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				LogUtil.logError(Activator.PLUGIN_ID, e);
			}
		}

		return text;
	}

	/**
	 * Creates the header and short description.
	 * 
	 * @param parent
	 */
	private void createHeader(Composite parent) {
		TitleAndDescriptionComposite td = new TitleAndDescriptionComposite(parent);
		td.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		td.setTitle(Messages.FleissnerWindow_title);
		td.setDescription(Messages.FleissnerWindow_subtitle);

//        headerComposite = new Composite(parent, SWT.NONE);
//        headerComposite.setBackground(ColorService.WHITE);
//        headerComposite.setLayout(new GridLayout());
//        headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.UP, true, false,3,1));
//   
//        Text headerText = new Text(headerComposite, SWT.READ_ONLY);
//        headerText.setText(Messages.FleissnerWindow_title);
//        headerText.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
//        headerText.setFont(FontService.getHeaderFont());
//        headerText.setEditable(false);
//        headerText.setBackground(ColorService.WHITE);
//        
//        descriptionText = new Text(headerComposite, SWT.READ_ONLY |SWT.WRAP);
//        descriptionText.setBackground(ColorService.WHITE);
//        
////        field 'descriptionText' has to be limited so it won't allocate unnecessary space
//        final GridData gridData = new GridData(SWT.FILL, SWT.UP, true, false);
//        gridData.widthHint = headerComposite.getClientArea().width;
//        System.err.println("Fleißner line count: " + descriptionText.getLineHeight());
//        gridData.heightHint = (descriptionText.getLineCount())*(descriptionText.getLineHeight());
//        descriptionText.setLayoutData(gridData);
//        descriptionText.setEditable(false);       
//        descriptionText.addListener(SWT.Resize | SWT.MouseDoubleClick, new Listener(){
//            
//            @Override
////            recalculating needed space for description with resize by movement of text field
//            public void handleEvent(Event arg0)
//            {
//                gridData.heightHint = (descriptionText.getLineCount())*(descriptionText.getLineHeight());
//                descriptionText.requestLayout();
//            } 
//         });
//        descriptionText.setText(Messages.FleissnerWindow_subtitle);
	}

	/**
	 * installs settings for 'analyze' is chosen as the active method
	 */
	public void analyze() {

		plain = false;
		argMethod = Messages.FleissnerWindow_method_analyze;

		setArgText();
		deleteHoles();

//        text settings
		plaintext.setEnabled(false);
		plaintext.setForeground(null);
		ciphertext.setEnabled(true);
		ciphertext.setForeground(null);

//        key settings
		canvasKey.setEnabled(false);
		randomKey.setEnabled(false);
		deleteHoles.setEnabled(false);

//        analysis settings
		analysis.setEnabled(true);
		analysisSettingsGroup.setEnabled(true);
		analysisOutput.setEnabled(true);

		chooseLanguage.setEnabled(true);
		language.setEnabled(true);
		setArgLanguage();

		restarts.setEnabled(true);
		statistics.setEnabled(true);
		statisticsLoad.setEnabled(true);

		if (statistics.getSelection())
			userStatistics = false;
		else
			userStatistics = true;

		statisticSelection();
		refreshStatistics(null);
	}

	/**
	 * installs settings for 'encrypt' is chosen as the active method
	 */
	public void encrypt() {

		plain = true;

		argMethod = Messages.FleissnerWindow_method_encrypt;

		deleteHoles();
		setArgText();

//        text settings
		plaintext.setEnabled(true);
		plaintext.setForeground(null);
		ciphertext.setEnabled(false);
		ciphertext.setForeground(null);

//        key settings
		canvasKey.setEnabled(true);
		randomKey.setEnabled(true);
		deleteHoles.setEnabled(true);

//        analysis settings
		analysisSettingsGroup.setEnabled(false);
		analysis.setEnabled(false);
		analysisOutput.setEnabled(false);
		logOutput.setEnabled(false);

		statistics.setEnabled(false);
		selectStatistic.setEnabled(false);
		statisticsLoad.setEnabled(false);
		loadStatistics.setEnabled(false);
		loadedStatisticName.setText(Messages.FleissnerWindow_empty);
		userStatistics = false;

		refreshStatistics(null);

		language.setEnabled(false);
		restarts.setEnabled(false);
		nGramSize.setEnabled(false);
	}

	/**
	 * installs settings for 'decrypt' is chosen as the active method
	 */
	public void decrypt() {

		plain = false;
		argMethod = Messages.FleissnerWindow_method_decrypt;

		setArgText();

//        text settings
		plaintext.setEnabled(false);
		plaintext.setForeground(null);
		ciphertext.setEnabled(true);
		ciphertext.setForeground(null);

//        key settings
		canvasKey.setEnabled(true);
		randomKey.setEnabled(true);
		deleteHoles.setEnabled(true);

//        analysis settings
		analysisSettingsGroup.setEnabled(false);
		analysis.setEnabled(false);
		analysisOutput.setEnabled(false);
		logOutput.setEnabled(false);

		statistics.setEnabled(false);
		selectStatistic.setEnabled(false);
		statisticsLoad.setEnabled(false);
		loadStatistics.setEnabled(false);
		loadedStatisticName.setText(Messages.FleissnerWindow_empty);
		userStatistics = false;

		refreshStatistics(null);

		language.setEnabled(false);
		restarts.setEnabled(false);
		nGramSize.setEnabled(false);
	}

// -- Fragment on how to split a BackgroundJob task into multiple steps which each have a name ("phases" of an algo)
//    		        SubMonitor subMonitor = SubMonitor.convert(monitor, tasks.size());
//    		        for (Task task : tasks) {
//    		            try {
//    		                // TimeUnit.SECONDS.sleep(0.2L); // example: how to sleep
//    		                subMonitor.setTaskName("I'm working on Task " + task.getSummary());
//    		                // workOnTask is a method in this class which does some work, "1" unit of it (one task, submonitor has size tasks.size()))
//    		                workOnTask(task, subMonitor.split(1));
//
//    		            } catch (InterruptedException e) {
//    		                return Status.CANCEL_STATUS;
//    		            }
//    		        }
//    		        return Status.OK_STATUS;

	public class FleissnerMethodJob extends BackgroundJob {

		private ParameterSettings ps;
		private MethodApplication ma;
		private String keysize1;
		private String ngramsize1;
		private String restarts1;
		private int selection;
		private boolean encryptSelection;
		private String checkedArgs;

		/**
		 * 
		 * @return the most important parameters for starting analysis. Method will be
		 *         executed at the start of every analysis
		 */
		public String checkArgs() {

			String args = Messages.FleissnerWindow_empty;

			final String keysize = keySize.getText();
			args += Messages.FleissnerWindow_parameter_enlistment_text + textName;
			args += Messages.FleissnerWindow_parameter_enlistment_keyLength + keysize;
			args += Messages.FleissnerWindow_parameter_enlistment_restarts + restarts.getText();
			args += Messages.FleissnerWindow_parameter_enlistment_language + argLanguage;
			args += Messages.FleissnerWindow_parameter_enlistment_statistic + statisticName;
			args += Messages.FleissnerWindow_parameter_enlistment_nGram + nGramSize.getText();

			return args;
		}

		@Override
		public IStatus computation(IProgressMonitor monitor) {
			if (argMethod != null) {
				switch (argMethod) {

				case "analyze": //$NON-NLS-1$
// 					monitor.worked(1);
					userText = true;
					getDisplay().syncExec(() -> analysisOutput.setText(Messages.FleissnerWindow_output_progress));

					getDisplay().syncExec(() -> {
						this.keysize1 = keySize.getText();
						this.ngramsize1 = nGramSize.getText();
						this.restarts1 = restarts.getText();
						this.selection = nGramSize.getSelection();
					});

					args = new String[12];
					args[0] = Messages.FleissnerWindow_input_method;
					args[1] = argMethod;
					args[2] = Messages.FleissnerWindow_input_keyLength;
					args[3] = keysize1;
					args[4] = Messages.FleissnerWindow_input_cryptedText;
					args[5] = argText;
					args[6] = Messages.FleissnerWindow_input_nGramSize;
					args[7] = ngramsize1;
					args[8] = Messages.FleissnerWindow_input_language;
					args[9] = argLanguage;
					args[10] = Messages.FleissnerWindow_input_restarts;
					args[11] = restarts1;

					// argStatistics shall only be reloaded if changed
					if (fisOld != fis || !oldNgramSize.equals(ngramsize1) || !oldLanguage.equals(argLanguage)) {
						try {
							argStatistics = lf.loadBinNgramFrequencies(fis, argLanguage, selection);
							fisOld = fis;
							oldNgramSize = ngramsize1;
							oldLanguage = argLanguage;
						} catch (NumberFormatException e) {
							LogUtil.logError(Activator.PLUGIN_ID, Messages.FleissnerWindow_error_enterValidStatistic, e,
									true);
							return Status.CANCEL_STATUS;
						} catch (FileNotFoundException e) {
							LogUtil.logError(Activator.PLUGIN_ID, Messages.FleissnerWindow_error_fileNotFound, e, true);
							return Status.CANCEL_STATUS;
						} catch (IllegalArgumentException e) {
							LogUtil.logError(Activator.PLUGIN_ID,
									Messages.FleissnerWindow_error_invalidParameterCombination, e, true);
							return Status.CANCEL_STATUS;
						}
					}
					break;

				case "encrypt": //$NON-NLS-1$
// 					monitor.worked(1);
					// if 'encrypt' is not selected this method will only be called in random
					// encryption and userText stays false
					getDisplay().syncExec(() -> {
						this.encryptSelection = encrypt.getSelection();
					});
					if (encryptSelection)
						userText = true;

					args = new String[6];
					args[0] = Messages.FleissnerWindow_input_method;
					args[1] = argMethod;
					args[2] = Messages.FleissnerWindow_input_key;
					argKey = model.translateKeyToLogic();
					args[3] = argKey;
					args[4] = Messages.FleissnerWindow_input_plaintext;
					args[5] = argText;

					break;

				case "decrypt": //$NON-NLS-1$
// 					monitor.worked(1);

					userText = true;

					args = new String[6];
					args[0] = Messages.FleissnerWindow_input_method;
					args[1] = argMethod;
					args[2] = Messages.FleissnerWindow_input_key;
					argKey = model.translateKeyToLogic();
					args[3] = argKey;
					args[4] = Messages.FleissnerWindow_input_cryptedText;
					args[5] = argText;

					break;
				}
			} else {
				throw new IllegalArgumentException(Messages.FleissnerWindow_error_noMethod);
			}
			return this.run(monitor);

		}

		private IStatus run(IProgressMonitor monitor) {
			this.ps = null;
			this.ma = null;

			try {
				// Configuration of given parameters and selecting and applying one of the three
				// methods

				this.ps = new ParameterSettings(args);
				this.ma = new MethodApplication(ps, argStatistics);

			} catch (IllegalArgumentException ex) {
//				MessageDialog.openError(Display.getCurrent().getActiveShell(),
//						Messages.FleissnerWindow_error_enterValidParameters,
//						ex.getMessage());
				// TODO: when counting up the spinner, "invalid key" is thrown.
//				LogUtil.logError(Activator.PLUGIN_ID, "wrong parameters", ex, false); 
				return Status.CANCEL_STATUS;
			} catch (FileNotFoundException ex) {
//				MessageDialog.openError(Display.getCurrent().getActiveShell(),
//						Messages.FleissnerWindow_error_fileNotFound,
//						ex.getMessage());
				LogUtil.logError(Activator.PLUGIN_ID, "File not found!", ex, true);

				return Status.CANCEL_STATUS;
			}

			switch (argMethod) {
			case "analyze":
				getDisplay().syncExec(() -> {
					this.checkedArgs = checkArgs();
					analysisOutput.append(Messages.FleissnerWindow_parameter_enlistment_analysisOut + checkedArgs); // $NON-NLS-1$
				});
				// dialogOutput = new
				// String(Messages.FleissnerWindow_parameter_enlistment_dialog+checkedArgs);
				ma.analyze();
				getDisplay().syncExec(() -> {
					logOutput.setEnabled(true);
					outputInput = ma.getAnalysisOut();
					outputInput.add(0, new String(Messages.FleissnerWindow_parameter_enlistment_dialog + checkedArgs));
					// dialogOutput += new String(ma.getFwAnalysisOutput());
					analysisOutput.append(ma.toString());
					// dialogOutput += new String(ma.toString());
					outputInput.add(new String(ma.toString()));
					plaintext.setEnabled(true);
					plaintext.setForeground(ColorService.GRAY);
					plaintext.setText(Messages.FleissnerWindow_output_foundPlaintext + ma.getBestDecryptedText());
				});
				keyToLogic = ma.getBestTemplate();
				// TODO: make progress tracking possible in ma, include here
// 					monitor.worked(100);
				monitor.done();
				getDisplay().syncExec(() -> {

					printFoundKey();
				});
				break;
			case "encrypt": //$NON-NLS-1$
				ma.encrypt();
				getDisplay().syncExec(() -> {
					if (encrypt.getSelection()) {
						ciphertext.setEnabled(true);
						ciphertext.setForeground(ColorService.GRAY);
					}
					ciphertext.setText(ma.getEncryptedText());
				});
// 					monitor.worked(100);
				monitor.done();
				break;
			case "decrypt": //$NON-NLS-1$
				ma.decrypt();
				String decryptedText = ma.getDecryptedText();
				getDisplay().syncExec(() -> {
					plaintext.setEnabled(true);
					plaintext.setForeground(ColorService.GRAY);
					plaintext.setText(decryptedText);
				});
// 					monitor.worked(100);
				monitor.done();
				break;
			}
			return Status.OK_STATUS;

		}

	}

	/**
	 * sets the arguments for chosen method and starts method 'startApplication'
	 * that execudes method
	 * 
	 * @throws IllegalArgumentException
	 */
	public void startMethod() throws IllegalArgumentException {
		FleissnerMethodJob job = new FleissnerMethodJob();
		job.finalizeListeners.add(status -> {
			getDisplay().syncExec(() -> {
				liftNoClick(); // TODO: mechanism to not let the user start other things in the background
			});
		});
		imposeNoClick(); // TODO: mechanism to not let the user start other things in the background
		job.runInBackground();
	}

	private void liftNoClick() {
		getShell().setCursor(getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
	}

	private void imposeNoClick() {
		getShell().setCursor(getDisplay().getSystemCursor(SWT.CURSOR_WAIT));
	}

//    /**
//     * executes chosen method with given parameters and returns result in particular text field
//     */
//    public void startApplication() {
//        
//        ParameterSettings ps = null;
//        MethodApplication ma = null;
//        
//        try {
////          Configuration of given parameters and selecting and applying one of the three methods
//            ps = new ParameterSettings(args);
//            ma = new MethodApplication(ps, argStatistics);
//
//        } catch (IllegalArgumentException ex) {
//            MessageDialog.openError(Display.getCurrent().getActiveShell(),
//                    Messages.FleissnerWindow_error_enterValidParameters,
//                    ex.getMessage());
//            LogUtil.logError(Activator.PLUGIN_ID, ex);
//            return;
//        } catch (FileNotFoundException ex) {
//            MessageDialog.openError(Display.getCurrent().getActiveShell(),
//                    Messages.FleissnerWindow_error_fileNotFound,
//                    ex.getMessage());
//            LogUtil.logError(Activator.PLUGIN_ID, ex);
//            
//            return;
//        } 
//        
//          switch (argMethod) {
//          case "analyze": analysisOutput.append(Messages.FleissnerWindow_parameter_enlistment_analysisOut+checkArgs()); //$NON-NLS-1$
////                          dialogOutput = new String(Messages.FleissnerWindow_parameter_enlistment_dialog+checkArgs());
//                          ma.analyze();
//                          logOutput.setEnabled(true);
//                          outputInput = ma.getAnalysisOut();
//                          outputInput.add(0, new String(Messages.FleissnerWindow_parameter_enlistment_dialog+checkArgs()));
////                          dialogOutput += new String(ma.getFwAnalysisOutput());
//                          analysisOutput.append(ma.toString());
////                          dialogOutput += new String(ma.toString());
//                          outputInput.add(new String(ma.toString()));
//                          plaintext.setEnabled(true);
//                          plaintext.setForeground(ColorService.GRAY);
//                          plaintext.setText(Messages.FleissnerWindow_output_foundPlaintext+ma.getBestDecryptedText());
//                          keyToLogic = ma.getBestTemplate();
//                          printFoundKey();
//                          break;
//          case "encrypt": ma.encrypt(); //$NON-NLS-1$
//                          if (encrypt.getSelection()) {
//                              ciphertext.setEnabled(true);
//                              ciphertext.setForeground(ColorService.GRAY);
//                          }
//                          ciphertext.setText(ma.getEncryptedText());
//                          break;
//          case "decrypt": ma.decrypt(); //$NON-NLS-1$
//                          plaintext.setEnabled(true);
//                          plaintext.setForeground(ColorService.GRAY);
//                          plaintext.setText(ma.getDecryptedText());
//                          break;
//          }
//    }

	/**
	 * generates a random key with current key length and displays key in grille
	 */
	public void generateRandomKey() {

		model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));

		int size = model.getKey().getSize();
		int x, y;

		do {
			do {
				x = ThreadLocalRandom.current().nextInt(0, size);
				y = ThreadLocalRandom.current().nextInt(0, size);
			} while (model.getKey().get(x, y) != '0');
			model.getKey().set(x, y);
		} while (!model.getKey().isValid());

		canvasKey.redraw();
		updateKeyText();
	}

	/**
	 * executes random encryption with random key for example texts that are needed
	 * for analysis or decryption. If 'analyze' is the chosen method the key
	 * displayed on the grille will be deleted afterwards
	 */
	public void randomEncryption() {

		String tempMethod = argMethod;

		generateRandomKey();
		argMethod = Messages.FleissnerWindow_method_encrypt;
		try {
			startMethod();
		} catch (IllegalArgumentException e) {
			LogUtil.logError(Activator.PLUGIN_ID, Messages.FleissnerWindow_error_noMethod, e, true);
		}
		argMethod = tempMethod;
		argText = ciphertext.getText();
		if (argMethod.equals(Messages.FleissnerWindow_method_analyze))
			deleteHoles();
	}

	/**
	 * deletes all chosen holes on the grille
	 */
	public void deleteHoles() {

		model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
		canvasKey.redraw();
		updateKeyText();
		canvasKey.removeMouseListener(keyListener);
		canvasKey.addMouseListener(keyListener);
	}

	/**
	 * refreshes parameter argText after changes in example text selection
	 */
	public void refreshExampleText() {

		userText = false;
		textName = lf.textFiles(chooseExample.getSelectionIndex());
		argText = lf.InputStreamToString(lf.openMyTestStream(textName));

		if (!plain)
			randomEncryption();

		refreshInOutTexts();
	}

	/**
	 * refreshes displayed text in plaintext or ciphertext field after parameter
	 * argtext has changed
	 */
	public void refreshInOutTexts() {

		if (plain) {
			plaintext.setText(argText);
			ciphertext.setText(Messages.FleissnerWindow_empty);
		} else {
			ciphertext.setText(argText);
			plaintext.setText(Messages.FleissnerWindow_empty);
		}
	}

	/**
	 * sets statisticName and (FileInputStream) fis when input statistic has changed
	 * 
	 * @param fileName
	 */
	public void refreshStatistics(String fileName) {

		if (argMethod.equals(Messages.FleissnerWindow_method_analyze)) {
			if (userStatistics) {
				if (fileName != null) {
//                    only if user loads own statistics
					try {
						fis = new FileInputStream(fileName);
						statisticName = fileName;
					} catch (FileNotFoundException e) {
						LogUtil.logError(Activator.PLUGIN_ID, Messages.FleissnerWindow_error_fileNotFound, e, true);
					}
				} else {
					statisticName = Messages.FleissnerWindow_empty;
					fis = null;
				}
			} else {
				statisticName = lf.statisticFiles(selectStatistic.getSelectionIndex());
				fis = lf.openMyFileStream(statisticName);
				statisticState = selectStatistic.getSelectionIndex();
			}
		} else {
			statisticName = Messages.FleissnerWindow_empty;
			fis = null;
		}
	}

	/**
	 * enables and disables buttons and text fields in composite for text selection
	 * dependent on chosen text input
	 * 
	 * @param enableChooseExample
	 * @param enableLoadText
	 * @param editPlaintext
	 * @param editCiphertext
	 */
	public void textSelection(boolean enableChooseExample, boolean enableLoadText, boolean editPlaintext,
			boolean editCiphertext) {

		chooseExample.setEnabled(enableChooseExample);
		//loadText.setEnabled(enableLoadText);
		textloader.setEnabled(enableLoadText);
		plaintext.setEditable(editPlaintext);
		ciphertext.setEditable(editCiphertext);

		if (argMethod.equals(Messages.FleissnerWindow_method_analyze)) {
			analysisOutput.setText(Messages.FleissnerWindow_output_progress);
//            dialogOutput = analysisOutput.getText();
		}

	}

	/**
	 * enables and disables buttons for statistics settings dependent on chosen
	 * statistics input
	 */
	public void statisticSelection() {

		if (!userStatistics) {
			selectStatistic.setEnabled(true);
			loadStatistics.setEnabled(false);
			nGramSize.setEnabled(false);
		} else {
			selectStatistic.setEnabled(false);
			loadStatistics.setEnabled(true);
			nGramSize.setEnabled(true);
		}
	}

	/**
	 * sets parameter argLanguage
	 */
	public void setArgLanguage() {

		if (language.getSelectionIndex() == 0) {
			argLanguage = Messages.FleissnerWindow_language_german;
		} else {
			argLanguage = Messages.FleissnerWindow_language_english;
		}
	}

	/**
	 * sets parameter 'argText' in method changes and manages text input settings
	 */
	public void setArgText() {

		String tempArgText;
		boolean edit;
		if (plain) {
			tempArgText = plaintext.getText();
			textSelectionGroup.setText(Messages.FleissnerWindow_label_textChoicePlain);
			exampleText.setText(Messages.FleissnerWindow_label_exampleTextPlain);
			writeText.setText(Messages.FleissnerWindow_label_writeTextPlain);
			loadOwntext.setText(Messages.FleissnerWindow_label_loadOwnTextPlain);
			edit = true;

		} else {
			tempArgText = ciphertext.getText();
			textSelectionGroup.setText(Messages.FleissnerWindow_label_textChoiceCipher);
			exampleText.setText(Messages.FleissnerWindow_label_exampleTextCipher);
			writeText.setText(Messages.FleissnerWindow_label_writeTextCipher);
			loadOwntext.setText(Messages.FleissnerWindow_label_loadOwnTextCipher);
			edit = false;
		}

		if (textInputState != 1 || tempArgText.equals(Messages.FleissnerWindow_empty))
//			loadedTextName.setText(Messages.FleissnerWindow_empty);

		if (textInputState == 2) {
			plaintext.setEditable(edit);
			ciphertext.setEditable(!edit);
		}
		if (startSettings) {
			argText = lf.InputStreamToString(lf.openMyTestStream(Messages.FleissnerWindow_file_ciphertext));
			refreshInOutTexts();
		} else if (exampleText.getSelection()) {

			if (userText && !tempArgText.equals(Messages.FleissnerWindow_empty)) {
				argText = tempArgText;
				refreshInOutTexts();
			} else {
				refreshExampleText();
			}
		} else {
			argText = tempArgText;
			refreshInOutTexts();
		}
	}

	/**
	 * updates settings of all three important statistics settings (text, language,
	 * statistic) if one of them has been changed
	 * 
	 * @param setting
	 */
	public void updateLanguageSettings(String setting) {

		switch (setting) {
		case "text": //$NON-NLS-1$
			if (chooseExample.getSelectionIndex() == 0 || chooseExample.getSelectionIndex() == 1) {
				language.select(0);
				selectStatistic.select(0);
				nGramSize.setSelection(4);
			} else {
				language.select(1);
				if (nGramSize.getSelection() == 4)
					selectStatistic.select(1);
				else
					selectStatistic.select(2);
			}
			break;
		case "language": //$NON-NLS-1$
			if (language.getSelectionIndex() == 0) {
				chooseExample.select(0);
				selectStatistic.select(0);
			} else {
				chooseExample.select(2);
				selectStatistic.select(1);
			}
			break;
		case "statistic": //$NON-NLS-1$
			if (selectStatistic.getSelectionIndex() == 0) {
				if (textState != 1)
					chooseExample.select(0);
				language.select(0);
			} else {
				if (textState != 3)
					chooseExample.select(2);
				language.select(1);
			}
			break;
		}
	}

	/**
	 * displays key in grille that has been found in analysis
	 */
	public void printFoundKey() {

		deleteHoles();
		for (int i = 0; i < keyToLogic.length / 2; i++) {
			model.getKey().set(keyToLogic[2 * i + 1], keyToLogic[2 * i]);
		}
		updateKeyText();
	}

	/**
	 * checks if all parameters for chosen method are correct and enables 'start'
	 * button if so
	 */
	public void checkOkButton() {

		if (analyze.getSelection()) {

			if (!argText.equals(Messages.FleissnerWindow_empty) && fis != null)
				start.setEnabled(true);
			else
				start.setEnabled(false);
		} else {

			if (model.getKey().isValid() && !argText.equals(Messages.FleissnerWindow_empty)) {
				start.setEnabled(true);
			} else {
				start.setEnabled(false);
			}
		}
	}

	/**
	 * checks start button and redraws grille
	 */
	public void reset() {

		checkOkButton();
		canvasKey.redraw();
	}

	/**
	 * checks if change of example text selection has changed language
	 * 
	 * @return change that is true if language has changed and false otherwise
	 */
	public boolean checkTextLangChange() {

		boolean change;

		if (languageState == 0 && (chooseExample.getSelectionIndex() == 2 || chooseExample.getSelectionIndex() == 3)) {
			change = true;
		} else if (languageState == 1
				&& (chooseExample.getSelectionIndex() == 0 || chooseExample.getSelectionIndex() == 1)) {
			change = true;
		} else {
			change = false;
		}
		return change;
	}

	/**
	 * checks if change of example language statistics selection has changed
	 * language
	 * 
	 * @return change that is true if language has changed and false otherwise
	 */
	public boolean checkStatisticLangChange() {

		boolean change;

		if (languageState == 0
				&& (selectStatistic.getSelectionIndex() == 1 || selectStatistic.getSelectionIndex() == 2)) {
			change = true;
		} else if (languageState == 1 && selectStatistic.getSelectionIndex() == 0) {
			change = true;
		} else {
			change = false;
		}
		return change;
	}

	/**
	 * writes key in textform underneath the key template
	 */
	public void updateKeyText() {
		int counter = 1;
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < model.getKey().getSize(); j++) {
			for (int k = 0; k < model.getKey().getSize(); k++) {
				if (model.getKey().get(j, k) == '1') {
					sb.append(counter);
					sb.append(" ");
				}
				counter++;
			}
		}

		keyText.setText(Messages.FleissnerWindow_label_key + ": " + sb.toString().trim());
		updateCiphertext();
	}

	public void updateCiphertext() {

		if (encrypt.getSelection())
			ciphertext.setText(Messages.FleissnerWindow_empty);
	}

	public void updatePlaintext() {

		if (decrypt.getSelection())
			plaintext.setText(Messages.FleissnerWindow_empty);
		else if (analyze.getSelection()) {
			plaintext.setText(Messages.FleissnerWindow_empty);
			analysisOutput.setText(Messages.FleissnerWindow_output_progress);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Canvas getCanvasKey() {
		return canvasKey;
	}

	/**
	 * 
	 * @param canvasKey
	 */
	public void setCanvasKey(Canvas canvasKey) {
		this.canvasKey = canvasKey;
	}

	/**
	 * 
	 * @return
	 */
	public Grille getModel() {
		return model;
	}

	/**
	 * 
	 * @param model
	 */
	public void setModel(Grille model) {
		this.model = model;
	}

	/**
	 * 
	 * @return
	 */
	public Spinner getKeySize() {
		return keySize;
	}

	/**
	 * 
	 * @param keySize
	 */
	public void setKeySize(Spinner keySize) {
		this.keySize = keySize;
	}

	/**
	 * @return the descriptionText
	 */
	public Text getDescriptionText() {
		return descriptionText;
	}

	/**
	 * @return the dialog
	 */
	public OutputDialog getDialog() {
		return dialog;
	}
}
// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2019 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.ECDH.ui.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.ECDH.ECDHPlugin;
import org.jcryptool.visual.ECDH.ECDHUtil;
import org.jcryptool.visual.ECDH.Messages;
import org.jcryptool.visual.ECDH.algorithm.EC;
import org.jcryptool.visual.ECDH.algorithm.ECFm;
import org.jcryptool.visual.ECDH.algorithm.ECPoint;
import org.jcryptool.visual.ECDH.ui.wizards.PublicParametersWizard;
import org.jcryptool.visual.ECDH.ui.wizards.SecretKeyComposite;
import org.jcryptool.visual.ECDH.ui.wizards.SecretKeyWizard;
import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.ellipticcurves.EllipticCurve;
import de.flexiprovider.common.math.ellipticcurves.Point;

public class ECDHComposite extends Composite {

	private Button btnSetPublicParameters ;
	private Button btnChooseSecrets ;
	private GridData gd_btnChooseSecrets;
	private Button btnCreateSharedKeys ;
	private Button btnExchangeKeys ;
	private Button btnGenerateKey ;
	private Button btnSecretA ;
	private Button btnCalculateSharedA ;
	private Button btnCalculateKeyA ;
	private Button btnSecretB ;
	private Button btnCalculateSharedB ;
	private Button btnCalculateKeyB ;
	private Button btnSaveToFile;
	private Button btnSaveToEditor;
	private Button btnShowAnimation;
	private Canvas canvasBtn ;
	private Canvas canvasExchange ;
	private Canvas canvasKey ;
	private Color cRed = new Color(Display.getCurrent(), 214, 100, 100);
	private Color cGreen = new Color(Display.getCurrent(), 140, 220, 132);
	private Color grey = new Color(Display.getCurrent(), 140, 138, 140);
	private Group groupAlice ;
	private Group groupBob ;
	private Group groupMain ;
	private Group groupParameters ;
	private Label placeholder;
	private Text infoText;
	private Text textCurve ;
	private Text textGenerator ;
	private Text textSecretA ;
	private Text textSharedA ;
	private Text textCommonKeyA ;
	private Text textSecretB ;
	private Text textSharedB ;
	private Text textCommonKeyB ;
	private EC curve; // @jve:decl-index=0:
	private int[] elements;
	private int secretA = -1;
	private int secretB = -1;
	private FlexiBigInt secretLargeA ;
	private FlexiBigInt secretLargeB ;
	private ECPoint shareA;
	private ECPoint shareB;
	private Point shareLargeA;
	private Point shareLargeB;
	private ECPoint keyA;
	private ECPoint keyB;
	private Point keyLargeA;
	private Point keyLargeB;
	private ECPoint generator;
	private int valueN;
	private ECDHView view;
	private File outputFile;
	private boolean large;
	private EllipticCurve largeCurve;
	private Point pointG;
	private FlexiBigInt largeOrder;
	private boolean showAnimation = true;
	private boolean chooseSecretButtonResets;
	private Image id;

	public static final int RESET_ALL = 0;
	public static final int RESET_PUBLIC_PARAMETERS = 1;
	public static final int RESET_SECRET_PARAMETERS = 2;

	/**
	 * Create the major UI elements of the plugin
	 * 
	 * @param parent parent
	 * @param style SWT style
	 * @param view the calling ViewPart
	 */
	public ECDHComposite(Composite parent, int style, ECDHView view) {
		super(parent, style);
		this.view = view;
		setLayout(new GridLayout());
		createCompositeHeader();
		createGroupMain();
	}
	
	@Override
	public void dispose() {
		id.dispose();
		cRed.dispose();
		cGreen.dispose();
		super.dispose();
	}

	/**
	 * Create the Header bar containing the Header text, description, animation toggle switch and save results
	 */
	private void createCompositeHeader() {
		Composite compositeIntro = new Composite(this, SWT.NONE);
		compositeIntro.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		compositeIntro.setLayout(new GridLayout(6, true));

		Label title = new Label(compositeIntro, SWT.NONE);
		title.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));
		title.setFont(FontService.getHeaderFont());
		title.setText(Messages.getString("ECDHView.title")); 

		StyledText stDescription = new StyledText(compositeIntro, SWT.READ_ONLY | SWT.WRAP);
		stDescription.setText(Messages.getString("ECDHView.description")); 
		GridData gd_stDescription = new GridData(SWT.FILL, SWT.FILL, false, false, 4 ,2);
		gd_stDescription.widthHint = 900;
		stDescription.setLayoutData(gd_stDescription);
		
		btnShowAnimation = new Button(compositeIntro, SWT.CHECK);
		btnShowAnimation.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 2));
		btnShowAnimation.setSelection(showAnimation);
		btnShowAnimation.setText(Messages.getString("ECDHComposite.6")); 
		btnShowAnimation.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				showAnimation = showAnimation ? false : true;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		
		btnSaveToEditor= new Button(compositeIntro, SWT.PUSH);
		btnSaveToEditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true, 1, 1));
		btnSaveToEditor.setText(Messages.getString("ECDHComposite.2")); 
		btnSaveToFile = new Button(compositeIntro, SWT.PUSH);
		btnSaveToFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true, 1, 1));
		btnSaveToFile.setText(Messages.getString("ECDHComposite.3")); 
		
		btnSaveToFile.setVisible(false);
		btnSaveToEditor.setVisible(false);	
		
		btnSaveToFile.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveToFile();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		btnSaveToEditor.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveToEditor();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	/**
	 * Create the majority of UI elements except the Header bar
	 */
	private void createGroupMain() {
		groupMain = new Group(this, SWT.NONE);
		groupMain.setLayout(new GridLayout(5, false));
		groupMain.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));
		groupMain.setText(Messages.getString("ECDHView.groupMain")); 

		createCanvasBtn();
		createGroupParameters();
		createGroupAlice();
		createCanvasExchange();
		createGroupBob();
		createGroupInfo();
		createCanvasKey();
	}
	
	/**
	 * Create the buttons on the left including the gray canvas arrow 
	 */
	private void createCanvasBtn() {
		canvasBtn = new Canvas(groupMain, SWT.NO_REDRAW_RESIZE);
		canvasBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 3));
		canvasBtn.setLayout(new GridLayout());

		btnSetPublicParameters = new Button(canvasBtn, SWT.NONE);
		GridData gd_btnSetPublicParameters = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_btnSetPublicParameters.heightHint = 60;
		btnSetPublicParameters.setLayoutData(gd_btnSetPublicParameters);
		btnSetPublicParameters.setBackground(cRed);
		btnSetPublicParameters.setText(Messages.getString("ECDHView.setPublicParameters")); 
		btnSetPublicParameters.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Information needed to adapt the button distance
				int previousSize = textCurve.getLineCount() * textCurve.getLineHeight();
				int currentSize;
				
				PublicParametersWizard wiz = new PublicParametersWizard(curve, generator);
				WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wiz);
				dialog.setHelpAvailable(false);
				if (dialog.open() == Window.OK) {
					reset(RESET_PUBLIC_PARAMETERS);
					groupMain.requestLayout();
					infoText.setText(Messages.getString("ECDHView.Step1") +
									 Messages.getString("ECDHView.Step2"));
					large = wiz.isLarge();
					String curveString, generatorString;
					if (large) {
						largeCurve = wiz.getLargeCurve();
						pointG = wiz.getLargeGenerator();
						largeOrder = wiz.getLargeOrder();
						curveString = ECDHUtil.formatLargeCurve(largeCurve.toString(), wiz.getLargeCurveType());
						generatorString = ECDHUtil.formatLargeGenerator(pointG.getXAffin().toString(),
															   pointG.getYAffin().toString(),
															   wiz.getLargeCurveType());				
						textCurve.setText(curveString);			
						textGenerator.setText(generatorString);
					} else {
						curve = wiz.getCurve();
						if (curve != null && curve.getType() == ECFm.ECFm)
							elements = ((ECFm) curve).getElements();
						textCurve.setText(curve.toString());
						generator = wiz.getGenerator();
						valueN = wiz.getOrder();
						textGenerator.setText(generator.toString());
					}
					btnChooseSecrets.setEnabled(true);
					btnSetPublicParameters.setBackground(cGreen);
				}
			
				currentSize = textCurve.getLineCount() * textCurve.getLineHeight();
				gd_btnChooseSecrets.verticalIndent += currentSize - previousSize;  // Calculate the new size this way
				canvasBtn.requestLayout();
				canvasBtn.layout(true);
				btnSecretA.setEnabled(true);
				btnSecretB.setEnabled(true);
			}
		});

		btnChooseSecrets = new Button(canvasBtn, SWT.NONE);
		gd_btnChooseSecrets = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_btnChooseSecrets.verticalIndent = 40;
		gd_btnChooseSecrets.heightHint = 60;
		btnChooseSecrets.setLayoutData(gd_btnChooseSecrets);
		btnChooseSecrets.setEnabled(false);
		btnChooseSecrets.setBackground(cRed);
		btnChooseSecrets.setText(Messages.getString("ECDHView.chooseSecrets")); 
		btnChooseSecrets.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// This button changes behavior. In the first place it has no special purpose.
				// If the program has proceeded it will function as reset-to-this-step button
				// Therefore this is false by default and set true in the next step
				if (chooseSecretButtonResets) {
					reset(ECDHComposite.RESET_SECRET_PARAMETERS);
					infoText.setText(Messages.getString("ECDHView.Step1") +
					         		 Messages.getString("ECDHView.Step2") +
					                 Messages.getString("ECDHView.Step3"));
					chooseSecretButtonResets = false;
				}
			}
		});

		btnCreateSharedKeys = new Button(canvasBtn, SWT.NONE);
		GridData gd_btnCreateSharedKeys = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_btnCreateSharedKeys.verticalIndent = 50;
		gd_btnCreateSharedKeys.heightHint = 60;
		btnCreateSharedKeys.setLayoutData(gd_btnCreateSharedKeys);
		btnCreateSharedKeys.setEnabled(false);
		btnCreateSharedKeys.setBackground(cRed);
		btnCreateSharedKeys.setText(Messages.getString("ECDHView.createSharedKeys")); 

		btnExchangeKeys = new Button(canvasBtn, SWT.NONE);
		GridData gd_btnExchangeKeys = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_btnExchangeKeys.verticalIndent = 50;
		gd_btnExchangeKeys.heightHint = 60;
		btnExchangeKeys.setLayoutData(gd_btnExchangeKeys);
		btnExchangeKeys.setEnabled(false);
		btnExchangeKeys.setBackground(cRed);
		btnExchangeKeys.setText(Messages.getString("ECDHView.exchangeSharedKeys")); 
		btnExchangeKeys.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (showAnimation) {
					String[] animationMessages = buildAnimationMessages();
					String messageA = animationMessages[0];
					String messageB = animationMessages[1];
					new Animation(canvasExchange, messageA, messageB).run();
				}
				infoText.setText(Messages.getString("ECDHView.Step1") +
						         Messages.getString("ECDHView.Step2") +
						         Messages.getString("ECDHView.Step3") +
						         Messages.getString("ECDHView.Step4") +
						         Messages.getString("ECDHView.Step5"));
				btnGenerateKey.setEnabled(true);
				btnExchangeKeys.setBackground(cGreen);
				btnCalculateKeyA.setEnabled(true);
				btnCalculateKeyB.setEnabled(true);
			}
		});

		btnGenerateKey = new Button(canvasBtn, SWT.NONE);
		GridData gd_btnGenerateKey = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_btnGenerateKey.verticalIndent = 50;
		gd_btnGenerateKey.heightHint = 60;
		btnGenerateKey.setLayoutData(gd_btnGenerateKey);
		btnGenerateKey.setEnabled(false);
		btnGenerateKey.setBackground(cRed);
		btnGenerateKey.setText(Messages.getString("ECDHView.generateCommonKey")); 
		btnGenerateKey.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				generateSessionKey();
			}
		});

		canvasBtn.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				paintArrowLeft(e);
			}
		});

		placeholder = new Label(canvasBtn, SWT.NONE);
		GridData gd_placeholder = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd_placeholder.verticalIndent = 65;
		gd_placeholder.heightHint = 10;
		placeholder.setLayoutData(gd_placeholder);
		placeholder.setVisible(false);

	}
	
	/**
	 * Create the group which shows the public parameters (curve and generator)
	 */
	private void createGroupParameters() {
		groupParameters = new Group(groupMain, SWT.NONE);
		groupParameters.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));
		groupParameters.setText(Messages.getString("ECDHView.groupParameters")); 
		GridLayout gridLayout = new GridLayout(2, false);
		groupParameters.setLayout(gridLayout);
		Label label = new Label(groupParameters, SWT.NONE);
		label.setText(Messages.getString("ECDHView.labelCurve")); 
		textCurve = new Text(groupParameters, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		textCurve.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		textCurve.setFont(FontService.getNormalMonospacedFont());
		label = new Label(groupParameters, SWT.NONE);
		label.setText(Messages.getString("ECDHView.labelGenerator")); 
		textGenerator = new Text(groupParameters, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		textGenerator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		textGenerator.setFont(FontService.getNormalMonospacedFont());
	}
	
	/**
	 * Create the left part (Alice)
	 */
	private void createGroupAlice() {
		groupAlice = new Group(groupMain, SWT.NONE);
		GridData gd_groupAlice = new GridData(SWT.DEFAULT, SWT.FILL, false, false);
		gd_groupAlice.widthHint = 300;
		gd_groupAlice.heightHint = 400;
		groupAlice.setLayoutData(gd_groupAlice);
		groupAlice.setText("Alice"); 
		groupAlice.setLayout(new GridLayout(2, false));

		btnSecretA = new Button(groupAlice, SWT.NONE);
		btnSecretA.setText(Messages.getString("ECDHView.secret")); 
		btnSecretA.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 2, 1));
		btnSecretA.setBackground(cRed);
		btnSecretA.setEnabled(false);
		btnSecretA.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SecretKeyWizard wiz;
				if (large)
					wiz = new SecretKeyWizard("Alice", secretLargeA, largeOrder); 
				else
					wiz = new SecretKeyWizard("Alice", secretA, valueN); 
				WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wiz);
				dialog.setHelpAvailable(false);
				dialog.setPageSize(SecretKeyComposite.minimumWidth, SecretKeyComposite.minimumHeight);
				if (dialog.open() == Window.OK) {
					reset(RESET_SECRET_PARAMETERS); // If the user goes back to this point reset from here
					if (large) {
						secretLargeA = wiz.getLargeSecret();
						if (secretLargeA != null && secretLargeB != null) {
							enableCalculateSharedButtons();
							btnCreateSharedKeys.setEnabled(true);
							btnChooseSecrets.setBackground(cGreen);
						}
					} else {
						secretA = wiz.getSecret();
						if (secretA > 0 && secretB > 0) {
							enableCalculateSharedButtons();
							btnCreateSharedKeys.setEnabled(true);
							btnChooseSecrets.setBackground(cGreen);
						}
					}
					textSecretA.setText("xxxxxxxxxxxxxxxxxxxxxx");
					btnSecretA.setBackground(cGreen);
				}
			}
		});
		Label label = new Label(groupAlice, SWT.NONE);
		label.setText("a ="); 
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));

		textSecretA = new Text(groupAlice, SWT.BORDER | SWT.PASSWORD | SWT.READ_ONLY);
		textSecretA.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));

		btnCalculateSharedA = new Button(groupAlice, SWT.NONE);
		btnCalculateSharedA.setText(Messages.getString("ECDHView.calculate"));
		btnCalculateSharedA.setEnabled(false);
		btnCalculateSharedA.setBackground(cRed);
		btnCalculateSharedA.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btnCalculateSharedA.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (large) {
					shareLargeA = ECDHUtil.exponentiateLargePoint(pointG, secretLargeA);
					textSharedA.setText("(" +shareLargeA.getXAffin() + ", " + shareLargeA.getYAffin() + ")");
				} else {
					shareA = curve.multiplyPoint(generator, secretA);
					textSharedA.setText(shareA.toString());
				}
				btnCalculateSharedA.setBackground(cGreen);
				// Tell the previous button btnChooseSecrets he acts in a reset function from now on 
				chooseSecretButtonResets = true;
				
				if ((large && shareLargeA != null && shareLargeB != null) ||(!large && shareA != null && shareB != null)) {
					btnExchangeKeys.setEnabled(true);
					btnCreateSharedKeys.setBackground(cGreen);
					infoText.setText(Messages.getString("ECDHView.Step1") +
					         		 Messages.getString("ECDHView.Step2") +
					         		 Messages.getString("ECDHView.Step3") +
					         		 Messages.getString("ECDHView.Step4"));
				}
			}
		});
		label = new Label(groupAlice, SWT.NONE);
		label.setText("A ="); 
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

		textSharedA = new Text(groupAlice, SWT.BORDER | SWT.READ_ONLY);
		textSharedA.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		btnCalculateKeyA = new Button(groupAlice, SWT.NONE);
		btnCalculateKeyA.setText(Messages.getString("ECDHView.calculate")); 
		btnCalculateKeyA.setEnabled(false);
		btnCalculateKeyA.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, true, 2, 1));
		btnCalculateKeyA.setBackground(cRed);
		btnCalculateKeyA.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				generateKeyA();
				btnCalculateKeyA.setBackground(cGreen);
				if (large) {
					keyLargeA = ECDHUtil.exponentiateLargePoint(shareLargeB, secretLargeA);
					textCommonKeyA.setText(keyLargeA.getXAffin().toString());
				} else {
					keyA = curve.multiplyPoint(shareB, secretA);
					if (keyA == null)
						keyA = generator;
					textCommonKeyA.setText(keyA.toString());
				}
				btnCalculateKeyA.setBackground(cGreen);
				Boolean b;
				if (large)
					b = keyLargeA != null && keyLargeB != null;
				else
					b = keyA != null && keyB != null;
				if (b) {
					if (large)
						b = keyLargeA.getXAffin().equals(keyLargeB.getXAffin());
					else
						b = keyA.equals(keyB);
					if (b) {
						showKeyImageUpdateText();
						btnGenerateKey.setBackground(cGreen);
					} else {
						infoText.append(Messages.getString("ECDHView.messageFail"));
					}
				}
			}

		});

		label = new Label(groupAlice, SWT.NONE);
		label.setText("S ="); 
		label.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));

		textCommonKeyA = new Text(groupAlice, SWT.BORDER | SWT.READ_ONLY);
		textCommonKeyA.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
	}

	/**
	 * Create the middle exchange part with the arrows
	 */
	private void createCanvasExchange() {
		canvasExchange = new Canvas(groupMain, SWT.NO_REDRAW_RESIZE);
		GridData gd_canvasExchange = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd_canvasExchange.widthHint = 150;
		gd_canvasExchange.heightHint = 400;
		canvasExchange.setLayoutData(gd_canvasExchange);
		canvasExchange.setLayout(new GridLayout());

		canvasExchange.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				paintExchangeArrows(e);
			}
		});

	}

	/**
	 * Create the right part (Bob)
	 */
	private void createGroupBob() {
		groupBob = new Group(groupMain, SWT.NONE);
		GridData gd_groupBob = new GridData(SWT.DEFAULT, SWT.FILL, false, false);
		gd_groupBob.widthHint = 300;
		gd_groupBob.heightHint = 400;
		groupBob.setLayoutData(gd_groupBob);
		groupBob.setText("Bob"); 
		groupBob.setLayout(new GridLayout(2, false));

		btnSecretB = new Button(groupBob, SWT.NONE);
		btnSecretB.setText(Messages.getString("ECDHView.secret")); 
		btnSecretB.setEnabled(false);
		btnSecretB.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 2, 1));
		btnSecretB.setBackground(cRed);
		btnSecretB.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SecretKeyWizard wiz;
				if (large)
					wiz = new SecretKeyWizard("Bob", secretLargeB, largeOrder); 
				else
					wiz = new SecretKeyWizard("Bob", secretB, valueN); 

				WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wiz);
				dialog.setHelpAvailable(false);
				dialog.setPageSize(SecretKeyComposite.minimumWidth, SecretKeyComposite.minimumHeight);
				if (dialog.open() == Window.OK) {
					reset(RESET_SECRET_PARAMETERS); // If the user goes back to this point reset from here
					if (large) {
						secretLargeB = wiz.getLargeSecret();
						if (secretLargeA != null && secretLargeB != null) {
							enableCalculateSharedButtons();
							btnCreateSharedKeys.setEnabled(true);
							btnChooseSecrets.setBackground(cGreen);
						}
					} else {
						secretB = wiz.getSecret();
						if (secretA > 0 && secretB > 0) {
							enableCalculateSharedButtons();
							btnCreateSharedKeys.setEnabled(true);
							btnChooseSecrets.setBackground(cGreen);
						}
					}
					textSecretB.setText("xxxxxxxxxxxxxxxxxxxxxx"); 
					btnSecretB.setBackground(cGreen);
				}
			}

		});
		Label label = new Label(groupBob, SWT.NONE);
		label.setText("b ="); 
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));

		textSecretB = new Text(groupBob, SWT.BORDER | SWT.PASSWORD | SWT.READ_ONLY);
		textSecretB.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));

		btnCalculateSharedB = new Button(groupBob, SWT.NONE);
		btnCalculateSharedB.setText(Messages.getString("ECDHView.calculate")); 
		btnCalculateSharedB.setEnabled(false);
		btnCalculateSharedB.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btnCalculateSharedB.setBackground(cRed);
		btnCalculateSharedB.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (large) {
					shareLargeB = ECDHUtil.exponentiateLargePoint(pointG, secretLargeB);
					textSharedB.setText("(" + shareLargeB.getXAffin() + ", " + shareLargeB.getYAffin() + ")");
				} else {
					shareB = curve.multiplyPoint(generator, secretB);
					textSharedB.setText(shareB.toString());
				}
				btnCalculateSharedB.setBackground(cGreen);
				// Tell the previous button btnChooseSecrets he acts in a reset function from now on 
				chooseSecretButtonResets = true;
				
				if ((large && shareLargeA != null && shareLargeB != null) || (!large && shareA != null && shareB != null)) {
					btnExchangeKeys.setEnabled(true);
					btnCreateSharedKeys.setBackground(cGreen);
					infoText.setText(Messages.getString("ECDHView.Step1") +
					         		 Messages.getString("ECDHView.Step2") +
					         		 Messages.getString("ECDHView.Step3") +
					         		 Messages.getString("ECDHView.Step4"));
				}
			}

		});
		label = new Label(groupBob, SWT.NONE);
		label.setText("B ="); 
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

		textSharedB = new Text(groupBob, SWT.BORDER | SWT.READ_ONLY);
		textSharedB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		btnCalculateKeyB = new Button(groupBob, SWT.NONE);
		btnCalculateKeyB.setText(Messages.getString("ECDHView.calculate")); 
		btnCalculateKeyB.setEnabled(false);
		btnCalculateKeyB.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, true, 2, 1));
		btnCalculateKeyB.setBackground(cRed);
		btnCalculateKeyB.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				generateKeyB();
				btnCalculateKeyB.setBackground(cGreen);
				Boolean b;
				if (large)
					b = keyLargeA != null && keyLargeB != null;
				else
					b = keyA != null && keyB != null;
				if (b) {
					if (large)
						b = keyLargeA.getXAffin().equals(keyLargeB.getXAffin());
					else
						b = keyA.equals(keyB);
					if (b) {
						showKeyImageUpdateText();
						btnGenerateKey.setBackground(cGreen);
					} else {
						infoText.append(Messages.getString("ECDHView.messageFail"));
					}
				}
			}
		});
		label = new Label(groupBob, SWT.NONE);
		label.setText("S ="); 
		label.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));

		textCommonKeyB = new Text(groupBob, SWT.BORDER | SWT.READ_ONLY);
		textCommonKeyB.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
	}

	
	/**
	 * Create the info text box on the right
	 */
	private void createGroupInfo() {
		Group groupInfo = new Group(groupMain, SWT.NONE);
		groupInfo.setLayout(new GridLayout());
		groupInfo.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 2));
		groupInfo.setText("Aktueller Schritt");

		infoText = new Text(groupInfo, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		infoText.setText(Messages.getString("ECDHView.Step1"));
		GridData infoTextLayout = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		infoTextLayout.widthHint = 370;
		infoText.setLayoutData(infoTextLayout);
	}
	
	/**
	 * Create the canvas containing the Key image
	 */
	private void createCanvasKey() {
		id = ECDHPlugin.getImageDescriptor("icons/key.png").createImage(); 
		canvasKey = new Canvas(groupMain, SWT.NO_REDRAW_RESIZE);
		GridData gd_canvasKey = new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1);
		gd_canvasKey.verticalIndent = 10;
		gd_canvasKey.widthHint = 850;
		gd_canvasKey.heightHint = 69;
		canvasKey.setLayoutData(gd_canvasKey);
		canvasKey.setVisible(false);
		canvasKey.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
					e.gc.drawImage(id, 305, 0);
			}
		});
	}




	protected void saveToEditor() {
		saveToEditor(buildOutputString());
	}

	protected void saveToFile() {
		saveToFile(buildOutputString());
	}

	
	/**
	 * Build the result string which can be saved. Does not cover any standard or anything similar.
	 * 
	 * @return a nicely formatted String which can be saved as output
	 */
	private String buildOutputString() {
		String s;
		if (large) {
			s = Messages.getString("ECDHView.logHeader") + "\n\n" + Messages.getString("ECDHView.curve") + ": "    
					+ largeCurve + "\n\n"; 

			s += Messages.getString("ECDHView.AliceParameters") + ":\n";  
			s += Messages.getString("ECDHView.secretKey") + " = " + secretLargeA + "\n";   
			s += Messages.getString("ECDHView.sharedKey") + " = " + shareLargeA.toString() + "\n";   
			s += Messages.getString("ECDHView.commonKey") + " = " + secretLargeA + " * " + shareLargeB + " = "    
					+ keyLargeA + "\n\n"; 

			s += Messages.getString("ECDHView.BobParameters") + ":\n";  
			s += Messages.getString("ECDHView.secretKey") + " = " + secretLargeB + "\n";   
			s += Messages.getString("ECDHView.sharedKey") + " = " + shareLargeB + "\n";   
			s += Messages.getString("ECDHView.commonKey") + " = " + secretLargeB + " * " + shareLargeA + " = "    
					+ keyLargeB + "\n\n"; 
		} else {
			s = Messages.getString("ECDHView.logHeader") + "\n\n" + Messages.getString("ECDHView.curve") + ": " + curve    
					+ "\n\n"; 

			s += Messages.getString("ECDHView.AliceParameters") + ":\n";  
			s += Messages.getString("ECDHView.secretKey") + " = " + secretA + "\n";   
			s += Messages.getString("ECDHView.sharedKey") + " = " + shareA.toString() + "\n";   
			s += Messages.getString("ECDHView.commonKey") + " = " + secretA + " * " + shareB + " = " + keyA + "\n\n";     

			s += Messages.getString("ECDHView.BobParameters") + ":\n";  
			s += Messages.getString("ECDHView.secretKey") + " = " + secretB + "\n";   
			s += Messages.getString("ECDHView.sharedKey") + " = " + shareB + "\n";   
			s += Messages.getString("ECDHView.commonKey") + " = " + secretB + " * " + shareA + " = " + keyB + "\n\n";     
		}
		return s;
	}

	/**
	 * Save result to a JCrypTool intern editor
	 * 
	 * TODO: managed to get a NullPointerException in the for loop, maybe handle that.
	 * 
	 * @param result of the key exchange as String
	 */
	private void saveToEditor(String result) {
		if (outputFile == null) {
			outputFile = new File(DirectoryService.getTempDir() + "ECDH results.txt");  
			outputFile.deleteOnExit();
		}

		try {
			writeFile(result, outputFile);
		}
		catch (IOException e) {
				handleSaveException(e);
		}
		
		IWorkbenchPage editorPage = view.getSite().getPage();
		IEditorReference[] er = editorPage.getEditorReferences();
		for (int i = 0; i < er.length; i++) {
			if (er[i].getName().equals("ECDH results.txt")) { 
				er[i].getEditor(false).getSite().getPage().closeEditor(er[i].getEditor(false), false);
			}
		}

		try {
			IPath location = new org.eclipse.core.runtime.Path(outputFile.getAbsolutePath());
			editorPage.openEditor(new PathEditorInput(location), "org.jcryptool.editor.text.editor.JCTTextEditor");
		} catch (PartInitException e) {
			LogUtil.logError(ECDHPlugin.PLUGIN_ID, e);
		}
	}
	

	/**
	 * Let the User choose a file and write the result into it.
	 * Shows a MessageBox on IOException
	 * 
	 * @param result of the key exchange as String
	 */
	private void saveToFile(String result) {
		selectFileLocation();
		if (outputFile != null) {
			try {
				writeFile(result, outputFile);
			} catch (IOException e) {
				handleSaveException(e);
			}
		}
	}
	
	private void writeFile(String s, File file) throws IOException {
		String[] sa = s.split("\n");
		if (sa.length > 1 || !sa[0].equals("")) {
			if (!outputFile.exists())
				outputFile.createNewFile();
			FileWriter fw = new FileWriter(outputFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < sa.length; i++) {
				if (i < sa.length - 1 || (i == sa.length - 1 && !sa[i].equals(""))) {
					bw.write(sa[i]);
					bw.newLine();
				}
			}
			bw.close();
			fw.close();
		}
	}
	
	/**
	 * Display a MessageBox informing the User about a failed save action
	 * 
	 * @param e The exception itself
	 */
	private void handleSaveException(IOException e) {
		MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell());
		messageBox.setText(Messages.getString("ECDHComposite.160")); 
		messageBox.setMessage(Messages.getString("ECDHComposite.161") + e.getMessage()); 
		messageBox.open();
	}

	private void selectFileLocation() {
		FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
		dialog.setFilterNames(new String[] { IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME });
		dialog.setFilterExtensions(new String[] { IConstants.TXT_FILTER_EXTENSION, IConstants.ALL_FILTER_EXTENSION });
		dialog.setFilterPath(DirectoryService.getUserHomeDir());
		dialog.setFileName("ECDH.txt"); 
		dialog.setOverwrite(true);
		String filename = dialog.open();
		if (filename == null) {
			outputFile = null;
			return;
		} else
			outputFile = new File(filename);
	}

	/**
	 *(Re)set the state of the plug-in.
	 *
	 * Multiple states are available which are specified by following constants
	 * 
	 * <ul>
	 * 	<li>{@code ECDHComposite.RESET_ALL}</li>
	 * <li>{@code ECDHComposite.RESET_PUBLIC_PARAMETERS}</li>
	 * <li>{@code ECDHComposite.RESET_SECRET_PARAMETERS}</li>
	 * </ul>
	 * @param state to reset to
	 */
	public void reset(int state) {
		switch (state) {
		case RESET_ALL: // complete reset
			curve = null;
			valueN = 0;
			generator = null;
			elements = null;

			textCurve.setText(""); 
			textGenerator.setText(""); 
			btnSetPublicParameters.setBackground(cRed);
			infoText.setText(Messages.getString("ECDHView.Step1"));
		case RESET_PUBLIC_PARAMETERS:// reset from Set public parameters button
			secretA = -1;
			secretB = -1;
			secretLargeA = null;
			secretLargeB = null;

			btnChooseSecrets.setEnabled(false);
			btnChooseSecrets.setBackground(cRed);
			btnSecretA.setEnabled(false);
			btnSecretA.setBackground(cRed);
			textSecretA.setText(""); 
			btnSecretB.setEnabled(false);
			btnSecretB.setBackground(cRed);
			textSecretB.setText(""); 
			btnCreateSharedKeys.setEnabled(false);		
			btnCalculateSharedA.setEnabled(false);
			btnCalculateSharedB.setEnabled(false);

			/*
			 * This part of the switch (RESET_PUBLIC_PARAMETERS) is called when the public
			 * parameters are changed. As we want a layout change (on textCurve and
			 * textGenerator) when choosing large or small curves, we layout here. The text
			 * fields are set empty beforehand to prevent layout to make them weird when
			 * they are filled with a long string.
			 */
			textCurve.setText("");
			textGenerator.setText("");
			layout();
		case RESET_SECRET_PARAMETERS: // this line is just for your information what will be reset
		default:
			shareA = null;
			shareB = null;
			keyA = null;
			keyB = null;
			shareLargeA = null;
			shareLargeB = null;
			keyLargeA = null;
			keyLargeB = null;

			canvasKey.setVisible(false);
			btnCreateSharedKeys.setBackground(cRed);
			btnCalculateSharedA.setBackground(cRed);
			textSharedA.setText(""); 
			btnCalculateSharedB.setBackground(cRed);
			textSharedB.setText(""); 
			btnExchangeKeys.setEnabled(false);
			btnExchangeKeys.setBackground(cRed);
			btnGenerateKey.setEnabled(false);
			btnGenerateKey.setBackground(cRed);
			btnCalculateKeyA.setEnabled(false);
			btnCalculateKeyA.setBackground(cRed);
			textCommonKeyA.setText(""); 
			btnCalculateKeyB.setEnabled(false);
			btnCalculateKeyB.setBackground(cRed);
			textCommonKeyB.setText(""); 
			btnSaveToFile.setVisible(false);
			btnSaveToEditor.setVisible(false);
		}
		groupMain.redraw();
	}
	
	/**
	 * Calculates the Key A (for Alice) and sets the text {@code textCommonKeyA}
	 */
	private void generateKeyA() {
		if (large) {
			keyLargeA = ECDHUtil.exponentiateLargePoint(shareLargeB, secretLargeA);
			textCommonKeyA.setText(keyLargeA.getXAffin().toString());
		} else {
			keyA = curve.multiplyPoint(shareB, secretA);
			if (keyA == null)
				keyA = generator;
			textCommonKeyA.setText(keyA.toString());
		}
	}
	
	/**
	 * Calculates the Key B (for Bob) and sets the text {@code textCommonKeyB}
	 */
	private void generateKeyB() {
		if (large) {
			keyLargeB = ECDHUtil.exponentiateLargePoint(shareLargeA, secretLargeB);
			textCommonKeyB.setText(keyLargeB.getXAffin().toString());
		} else {
			keyB = curve.multiplyPoint(shareA, secretB);
			if (keyB == null)
				keyB = generator;
			textCommonKeyB.setText(keyB.toString());
		}
	}
	
	/**
	 * Generate the session key on both sides (Alice and Bob) and update UI elements
	 */
	private void generateSessionKey() {
		generateKeyA();
		generateKeyB();
		btnCalculateKeyA.setBackground(cGreen);
		btnCalculateKeyB.setBackground(cGreen);
		btnGenerateKey.setBackground(cGreen);
		showKeyImageUpdateText();
	}
	
	private void showKeyImageUpdateText() {
		canvasKey.setVisible(true);
		canvasKey.redraw();
		infoText.setText(Messages.getString("ECDHView.Step1") +
						 Messages.getString("ECDHView.Step2") +
						 Messages.getString("ECDHView.Step3") +
						 Messages.getString("ECDHView.Step4") +
						 Messages.getString("ECDHView.Step5") +
						 Messages.getString("ECDHView.messageSucces"));
		btnSaveToFile.setVisible(true);
		btnSaveToEditor.setVisible(true);
	}
	
	/**
	 * Enable the buttons {@code btnCalculateSharedA} and {@code btnCalculateSharedA} needed in next step
	 */
	private void enableCalculateSharedButtons() {
		btnCalculateSharedA.setEnabled(true);
		btnCalculateSharedB.setEnabled(true);
		infoText.setText(Messages.getString("ECDHView.Step1") +
		         		 Messages.getString("ECDHView.Step2") +
		         		 Messages.getString("ECDHView.Step3"));
	}

	/**
	 * Builds the messages shown in the animation to exchange between
	 * 
	 * @return Array of size 2 with messageA on [0] and messageB on [1]
	 */
	private String[] buildAnimationMessages() {
		String[] messages = new String[2]; 
		// If large curve
		if (large) {
			messages[0] = shareLargeA.getXAffin().toString(2).substring(0, 4) + " " 
					+ shareLargeA.getYAffin().toString(2).substring(0, 4);
			messages[1] = shareLargeB.getXAffin().toString(2).substring(0, 4) + " " 
					+ shareLargeB.getYAffin().toString(2).substring(0, 4);
		// If small ECFm curve
		} else if (curve.getType() == ECFm.ECFm) {
				messages[0] = ECDHUtil.intToBitString(shareA.x == elements.length ? 0 : elements[shareA.x], 5) + " " 
						+ ECDHUtil.intToBitString(shareA.y == elements.length ? 0 : elements[shareA.y], 5);
				messages[1] = ECDHUtil.intToBitString(shareB.x == elements.length ? 0 : elements[shareB.x], 5) + " " 
						+ ECDHUtil.intToBitString(shareB.y == elements.length ? 0 : elements[shareB.y], 5);
		// If small normal curve
		} else {
				messages[0] = ECDHUtil.intToBitString(shareA.x, 5) + " " + ECDHUtil.intToBitString(shareA.y, 5); 
				messages[1] = ECDHUtil.intToBitString(shareB.x, 5) + " " + ECDHUtil.intToBitString(shareB.y, 5); 
		}				
		return messages;
	}
	
	/**
	 * Paint the grey arrows on the left connecting the buttons
	 * 
	 * @param e the PaintListener from a PaintEvent 
	 */
	private void paintArrowLeft(PaintEvent e) {
		
		int x1, y1, width, x2, y3, x4, y5, x6, y6, y7, y8;
		
		// The line should be at 1/3 left of the button width
		Rectangle bounds = btnSetPublicParameters.getBounds();
		x1 = bounds.x + (bounds.width / 3) - 5;
		y1 = bounds.y;
		width = 10;

		Path connection = new Path(Display.getCurrent());
		// Horizontal line on top
		connection.moveTo(x1, y1);
		x2 = x1 + width;
		connection.lineTo(x2, y1);

		// 60 (button height) + 40 - 5 (so it's in the middle)
		y3 = btnGenerateKey.getBounds().y + 120 - 5;
		connection.lineTo(x2, y3);

		// Arrow heads need 40px space
		x4 = canvasBtn.getBounds().x + canvasBtn.getBounds().width - 40;
		connection.lineTo(x4, y3);

		// Line overlaps 10px
		y5 = y3 - 10;
		connection.lineTo(x4, y5);

		// Arrow head
		x6 = canvasBtn.getBounds().x + canvasBtn.getBounds().width - 10;
		y6 = y5 + 15;
		connection.lineTo(x6, y6);

		y7 = y6 + 15;
		connection.lineTo(x4, y7);

		y8 = y7 - 10;
		connection.lineTo(x4, y8);

		// Connect to bottom left line
		connection.lineTo(x1, y8);

		// and back again to top
		connection.lineTo(x1, y1);

		e.gc.setBackground(grey);
		e.gc.fillPath(connection);
	}
	
	/**
	 * Paint the gray middle exchange path arrows
	 * It is quite confusing code, I just left it
	 * @param e
	 */
	private void paintExchangeArrows(PaintEvent e) {
		int canvasWidth = canvasExchange.getBounds().width;
		int x1, y1, x2, y3, x4, y4, x5, y6, x7, y7, y8, y9, x10, x11, y11, y12;
		int bax1, bay1, bax2, bay3, bax4, bay4, bax5, bay6, bax7, bay7, bay8, bay9, bax10, bax11, bay11, bay12;
		GC gc = e.gc;
		
		/*
		 * Path from left (Alice) to right (Bob)
		 */
		Path ab = new Path(Display.getCurrent());
		// left edge of the canvas
		x1 = 0;
		// middle of textSharedA - 5
		y1 = textSharedA.getBounds().y + (textSharedA.getBounds().height / 2) - 5;
		ab.moveTo(x1, y1);
		// left quarter of the canvas
		x2 = (canvasWidth / 4) + 5;
		ab.lineTo(x2, y1);
		y3 = textCommonKeyB.getBounds().y + (textCommonKeyB.getBounds().height / 2) - (canvasWidth * 2 / 4);
		ab.lineTo(x2, y3);
		// right edge of the canvas
		x4 = canvasWidth - canvasWidth / 4;
		y4 = textCommonKeyB.getBounds().y + textCommonKeyB.getBounds().height / 2 - 5;
		ab.lineTo(x4, y4);
		// right edge of the canvas - 20 for the arrow
		x5 = canvasWidth - 20;
		ab.lineTo(x5, y4);
		y6 = y4 - 5;
		ab.lineTo(x5, y6);
		// arrow tip
		x7 = canvasWidth;
		y7 = textCommonKeyB.getBounds().y + textCommonKeyB.getBounds().height / 2;
		ab.lineTo(x7, y7);
		y8 = y4 + 15;
		ab.lineTo(x5, y8);
		y9 = y8 - 5;
		ab.lineTo(x5, y9);
		x10 = x4 - 4;
		ab.lineTo(x10, y9);
		x11 = x2 - 10;
		y11 = y3 + 4;
		ab.lineTo(x11, y11);
		y12 = y1 + 10;
		ab.lineTo(x11, y12);
		ab.lineTo(x1, y12);
		// back to the beginning
		ab.lineTo(x1, y1);
		gc.setBackground(grey);
		gc.fillPath(ab);

		/*
		 * Path from right (Bob) to left (Alice)
		 */
		Path ba = new Path(Display.getCurrent());		
		bax1 = canvasWidth;
		bay1 = textSharedB.getBounds().y + (textSharedB.getBounds().height / 2) - 5;
		ba.moveTo(bax1, bay1);
		bax2 = (3 * canvasWidth / 4) - 5;
		ba.lineTo(bax2, bay1);
		bay3 = textCommonKeyA.getBounds().y + (textCommonKeyA.getBounds().height / 2) - (canvasWidth * 2 / 4);
		ba.lineTo(bax2, bay3);
		// left edge of the canvas
		bax4 = canvasWidth / 4;
		bay4 = textCommonKeyA.getBounds().y + textCommonKeyA.getBounds().height / 2 - 5;
		ba.lineTo(bax4, bay4);
		// right edge of the canvas - 20 for the arrow
		bax5 = 20;
		ba.lineTo(bax5, bay4);
		bay6 = bay4 - 5;
		ba.lineTo(bax5, bay6);
		// arrow tip
		bax7 = 0;
		bay7 = textCommonKeyA.getBounds().y + textCommonKeyA.getBounds().height / 2;
		ba.lineTo(bax7, bay7);
		bay8 = bay4 + 15;
		ba.lineTo(bax5, bay8);
		bay9 = bay8 - 5;
		ba.lineTo(bax5, bay9);
		bax10 = bax4 + 4;
		ba.lineTo(bax10, bay9);
		bax11 = bax2 + 10;
		bay11 = bay3 + 4;
		ba.lineTo(bax11, bay11);
		bay12 = bay1 + 10;
		ba.lineTo(bax11, bay12);
		ba.lineTo(bax1, bay12);
		// back to the beginning
		ba.lineTo(bax1, bay1);
		e.gc.fillPath(ba);
	}
}

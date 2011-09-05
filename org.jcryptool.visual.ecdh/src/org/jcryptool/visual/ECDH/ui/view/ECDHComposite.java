// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
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

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
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
import org.eclipse.swt.widgets.Shell;
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
import org.jcryptool.visual.ECDH.Messages;
import org.jcryptool.visual.ECDH.algorithm.EC;
import org.jcryptool.visual.ECDH.algorithm.ECFm;
import org.jcryptool.visual.ECDH.algorithm.ECPoint;
import org.jcryptool.visual.ECDH.ui.wizards.PublicParametersWizard;
import org.jcryptool.visual.ECDH.ui.wizards.SecretKeyWizard;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.ellipticcurves.EllipticCurve;
import de.flexiprovider.common.math.ellipticcurves.Point;

public class ECDHComposite extends Composite implements PaintListener {
    private Composite compositeIntro = null;
    private StyledText stDescription = null;
    private Group groupMain = null;
    private Canvas canvasMain = null;
    private Button btnSetPublicParameters = null;
    private Group groupParameters = null;
    private Button btnChooseSecrets = null;
    private Button btnCreateSharedKeys = null;
    private Button btnExchangeKeys = null;
    private Button btnGenerateKey = null;
    private Group groupAlice = null;
    private Group groupBob = null;
    private Text textCurve = null;
    private Text textGenerator = null;
    private Button btnSecretA = null;
    private Color cRed = new Color(Display.getCurrent(), 247, 56, 51);
    private Color cGreen = new Color(Display.getCurrent(), 0, 255, 64); // @jve:decl-index=0:
    private Button btnCalculateSharedA = null;
    private Text textSecretA = null;
    private Text textSharedA = null;
    private Button btnCalculateKeyA = null;
    private Text textCommonKeyA = null;
    private Button btnSecretB = null;
    private Text textSecretB = null;
    private Button btnCalculateSharedB = null;
    private Text textSharedB = null;
    private Button btnCalculateKeyB = null;
    private Text textCommonKeyB = null;
    private EC curve; // @jve:decl-index=0:
    private int[] elements;
    private int secretA = -1;
    private int secretB = -1;
    private FlexiBigInt secretLargeA = null;
    private FlexiBigInt secretLargeB = null;
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
    private File logFile;
    private IWorkbenchPage editorPage;
    private boolean large;
    private EllipticCurve largeCurve;
    private Point pointG;
    private FlexiBigInt largeOrder;
    private boolean showAnimation = true;
    private boolean showInformationDialogs = true;
    private Action action_saveToEditor;
    private Action action_saveToFile;

    public ECDHComposite(Composite parent, int style, ECDHView view) {
        super(parent, style);
        this.view = view;
        setLayout(new GridLayout());
        createCompositeIntro();
        createGroupMain();

        IMenuManager dropDownMenu = view.getViewSite().getActionBars().getMenuManager();
        Action action = new Action(Messages.getString("ECDHComposite.0"), IAction.AS_CHECK_BOX) {public void run() {toggleAnimation();}}; //$NON-NLS-1$
        action.setChecked(true);
        dropDownMenu.add(action);
        action = new Action(Messages.getString("ECDHComposite.1"), IAction.AS_CHECK_BOX) {public void run() {toggleInformationDialogs();}}; //$NON-NLS-1$
        action.setChecked(true);
        dropDownMenu.add(action);
        dropDownMenu.add(new Separator());
        action_saveToEditor = new Action(Messages.getString("ECDHComposite.2"), IAction.AS_PUSH_BUTTON) {public void run() {saveToEditor();}}; //$NON-NLS-1$
        action_saveToEditor.setEnabled(false);
        dropDownMenu.add(action_saveToEditor);
        action_saveToFile = new Action(Messages.getString("ECDHComposite.3"), IAction.AS_PUSH_BUTTON) {public void run() {saveToFile();}}; //$NON-NLS-1$
        action_saveToFile.setEnabled(false);
        dropDownMenu.add(action_saveToFile);

        IToolBarManager toolBarMenu = view.getViewSite().getActionBars().getToolBarManager();
        action = new Action(Messages.getString("ECDHComposite.4"), IAction.AS_PUSH_BUTTON) {public void run() {reset(0);}}; //$NON-NLS-1$
        action.setImageDescriptor(ECDHPlugin.getImageDescriptor("icons/reset.gif")); //$NON-NLS-1$
        toolBarMenu.add(action);
    }

    @Override
    public void dispose() {
        cRed.dispose();
        cGreen.dispose();
        super.dispose();
    }

    /**
     * This method initializes compositeIntro
     *
     */
    private void createCompositeIntro() {
        compositeIntro = new Composite(this, SWT.NONE);
        compositeIntro.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        compositeIntro.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        compositeIntro.setLayout(new GridLayout(1, false));

        Label label = new Label(compositeIntro, SWT.NONE);
        label.setFont(FontService.getHeaderFont());
        label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        label.setText(Messages.getString("ECDHView.title")); //$NON-NLS-1$

        stDescription = new StyledText(compositeIntro, SWT.READ_ONLY);
        stDescription.setText(Messages.getString("ECDHView.description")); //$NON-NLS-1$
        stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    }

    protected void toggleInformationDialogs() {
        showInformationDialogs = !showInformationDialogs;
    }

    protected void toggleAnimation() {
        showAnimation = !showAnimation;
    }

    /**
     * This method initializes groupMain
     *
     */
    private void createGroupMain() {
        groupMain = new Group(this, SWT.NONE);
        groupMain.setLayout(new GridLayout());
        groupMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        createCanvasMain();
        groupMain.setText(Messages.getString("ECDHView.groupMain")); //$NON-NLS-1$
    }

    /**
     * This method initializes canvasMain
     *
     */
    private void createCanvasMain() {
        GridData gridData6 = new GridData(SWT.BEGINNING, SWT.BEGINNING, true, true);
        gridData6.widthHint = 774;
        gridData6.heightHint = 539;
        canvasMain = new Canvas(groupMain, SWT.DOUBLE_BUFFERED);
        canvasMain.setLayout(new FormLayout());
        canvasMain.setLayoutData(gridData6);
        canvasMain.addPaintListener(this);
        btnSetPublicParameters = new Button(canvasMain, SWT.NONE);
        btnSetPublicParameters.setBackground(cRed);
        btnSetPublicParameters.setText(Messages.getString("ECDHView.setPublicParameters")); //$NON-NLS-1$
        FormData formData = new FormData(180, 60);
        formData.left = new FormAttachment(4);
        formData.top = new FormAttachment(4);
        btnSetPublicParameters.setLayoutData(formData);
        btnSetPublicParameters.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                try {
                    if (showInformationDialogs) {
                        MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                                | SWT.OK);
                        messageBox.setText(Messages.getString("ECDHView.setPublicParameters")); //$NON-NLS-1$
                        messageBox.setMessage(Messages.getString("ECDHView.Step1")); //$NON-NLS-1$
                        messageBox.open();
                    }

                    PublicParametersWizard wiz = new PublicParametersWizard(curve, generator);
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz);
                    if (dialog.open() == Window.OK) {
                        reset(1);
                        large = wiz.isLarge();
                        if (large) {
                            largeCurve = wiz.getLargeCurve();
                            pointG = wiz.getLargeGenerator();
                            largeOrder = wiz.getLargeOrder();
                            textCurve.setText(largeCurve.toString());
                            textGenerator.setText("" + pointG.getXAffin() + ", " + pointG.getYAffin() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
                        canvasMain.redraw();
                    }
                } catch (Exception ex) {
                    LogUtil.logError(ECDHPlugin.PLUGIN_ID, ex);
                }
            }
        });
        createGroupParameters();
        btnChooseSecrets = new Button(canvasMain, SWT.NONE);
        btnChooseSecrets.setEnabled(false);
        btnChooseSecrets.setBackground(cRed);
        formData = new FormData(180, 60);
        formData.left = new FormAttachment(4);
        formData.top = new FormAttachment(22);
        btnChooseSecrets.setLayoutData(formData);
        btnChooseSecrets.setText(Messages.getString("ECDHView.chooseSecrets")); //$NON-NLS-1$
        btnChooseSecrets.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                if (showInformationDialogs) {
                    MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                            | SWT.OK);
                    messageBox.setText(Messages.getString("ECDHView.chooseSecrets")); //$NON-NLS-1$
                    messageBox.setMessage(Messages.getString("ECDHView.Step2")); //$NON-NLS-1$
                    messageBox.open();
                }
                btnSecretA.setEnabled(true);
                btnSecretB.setEnabled(true);
                btnChooseSecrets.setBackground(cGreen);
            }

        });
        btnCreateSharedKeys = new Button(canvasMain, SWT.NONE);
        btnCreateSharedKeys.setEnabled(false);
        btnCreateSharedKeys.setBackground(cRed);
        formData = new FormData(180, 60);
        formData.left = new FormAttachment(4);
        formData.top = new FormAttachment(40);
        btnCreateSharedKeys.setLayoutData(formData);
        btnCreateSharedKeys.setText(Messages.getString("ECDHView.createSharedKeys")); //$NON-NLS-1$
        btnCreateSharedKeys.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                if (showInformationDialogs) {
                    MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                            | SWT.OK);
                    messageBox.setText(Messages.getString("ECDHView.createSharedKeys")); //$NON-NLS-1$
                    messageBox.setMessage(Messages.getString("ECDHView.Step3")); //$NON-NLS-1$
                    messageBox.open();
                }
                btnCalculateSharedA.setEnabled(true);
                btnCalculateSharedB.setEnabled(true);
                btnCreateSharedKeys.setBackground(cGreen);
            }

        });
        btnExchangeKeys = new Button(canvasMain, SWT.NONE);
        btnExchangeKeys.setEnabled(false);

        btnExchangeKeys.setBackground(cRed);
        formData = new FormData(180, 60);
        formData.left = new FormAttachment(4);
        formData.top = new FormAttachment(57);
        btnExchangeKeys.setLayoutData(formData);
        btnExchangeKeys.setText(Messages.getString("ECDHView.exchangeSharedKeys")); //$NON-NLS-1$
        btnExchangeKeys.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                if (showInformationDialogs) {
                    MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                            | SWT.OK);
                    messageBox.setText(Messages.getString("ECDHView.exchangeSharedKeys")); //$NON-NLS-1$
                    messageBox.setMessage(Messages.getString("ECDHView.Step4")); //$NON-NLS-1$
                    messageBox.open();
                }
                new Animate().run();
                btnGenerateKey.setEnabled(true);
                btnExchangeKeys.setBackground(cGreen);
                canvasMain.redraw();
            }
        });
        btnGenerateKey = new Button(canvasMain, SWT.NONE);
        btnGenerateKey.setEnabled(false);
        btnGenerateKey.setBackground(cRed);
        formData = new FormData(180, 60);
        formData.left = new FormAttachment(4);
        formData.top = new FormAttachment(75);
        btnGenerateKey.setLayoutData(formData);
        btnGenerateKey.setText(Messages.getString("ECDHView.generateCommonKey")); //$NON-NLS-1$
        btnGenerateKey.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                if (showInformationDialogs) {
                    MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                            | SWT.OK);
                    messageBox.setText(Messages.getString("ECDHView.generateCommonKey")); //$NON-NLS-1$
                    messageBox.setMessage(Messages.getString("ECDHView.Step5")); //$NON-NLS-1$
                    messageBox.open();
                }
                btnCalculateKeyA.setEnabled(true);
                btnCalculateKeyB.setEnabled(true);
                btnGenerateKey.setBackground(cGreen);
            }

        });
        createGroupAlice();
        createGroupBob();
    }

    /**
     * This method initializes groupParameters
     *
     */
    private void createGroupParameters() {
        GridLayout gridLayout = new GridLayout(2, false);
        groupParameters = new Group(canvasMain, SWT.NONE);
        groupParameters.setText(Messages.getString("ECDHView.groupParameters")); //$NON-NLS-1$
        FormData formData = new FormData(525, SWT.DEFAULT);
        formData.left = new FormAttachment(28);
        formData.top = new FormAttachment(3);
        groupParameters.setLayoutData(formData);
        groupParameters.setLayout(gridLayout);
        Label label = new Label(groupParameters, SWT.NONE);
        label.setText(Messages.getString("ECDHView.labelCurve")); //$NON-NLS-1$
        textCurve = new Text(groupParameters, SWT.BORDER | SWT.READ_ONLY);
        textCurve.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        label = new Label(groupParameters, SWT.NONE);
        label.setText(Messages.getString("ECDHView.labelGenerator")); //$NON-NLS-1$
        textGenerator = new Text(groupParameters, SWT.BORDER | SWT.READ_ONLY);
        textGenerator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    }

    /**
     * This method initializes groupAlice
     *
     */
    private void createGroupAlice() {
        groupAlice = new Group(canvasMain, SWT.NONE);
        groupAlice.setText("Alice"); //$NON-NLS-1$
        FormData formData = new FormData(200, 360);
        formData.left = new FormAttachment(28);
        formData.top = new FormAttachment(21);
        groupAlice.setLayoutData(formData);
        groupAlice.setLayout(new GridLayout(2, false));
        btnSecretA = new Button(groupAlice, SWT.NONE);
        btnSecretA.setText(Messages.getString("ECDHView.secret")); //$NON-NLS-1$
        btnSecretA.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1));
        btnSecretA.setBackground(cRed);
        btnSecretA.setEnabled(false);
        btnSecretA.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                SecretKeyWizard wiz;
                if (large)
                    wiz = new SecretKeyWizard("Alice", secretLargeA, largeOrder); //$NON-NLS-1$
                else
                    wiz = new SecretKeyWizard("Alice", secretA, valueN); //$NON-NLS-1$
                WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz);
                dialog.setPageSize(600, 80);
                if (dialog.open() == Window.OK) {
                    reset(2);
                    if (large) {
                        secretLargeA = wiz.getLargeSecret();
                        if (secretLargeA != null && secretLargeB != null) {
                            btnCreateSharedKeys.setEnabled(true);
                            canvasMain.redraw();
                        }
                    } else {
                        secretA = wiz.getSecret();
                        if (secretA > 0 && secretB > 0) {
                            btnCreateSharedKeys.setEnabled(true);
                            canvasMain.redraw();
                        }
                    }
                    textSecretA.setText("xxxxxxxxxxxxxxxxxxxxxx"); //$NON-NLS-1$
                    btnSecretA.setBackground(cGreen);
                    if (showInformationDialogs) {
                        MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                                | SWT.OK);
                        messageBox.setText("Alice " + Messages.getString("ECDHView.messageSecretKeyTitle")); //$NON-NLS-1$ //$NON-NLS-2$
                        messageBox
                                .setMessage("Alice " + Messages.getString("ECDHView.messageSecretKey") + " Alice" + Messages.getString("ECDHView.messageSecretKey2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        messageBox.open();
                    }
                }
            }
        });
        Label label = new Label(groupAlice, SWT.NONE);
        label.setText("a ="); //$NON-NLS-1$
        textSecretA = new Text(groupAlice, SWT.BORDER | SWT.PASSWORD | SWT.READ_ONLY);
        textSecretA.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        btnCalculateSharedA = new Button(groupAlice, SWT.NONE);
        btnCalculateSharedA.setText(Messages.getString("ECDHView.calculate")); //$NON-NLS-1$
        btnCalculateSharedA.setEnabled(false);
        btnCalculateSharedA.setBackground(cRed);
        GridData gridData = new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1);
        gridData.verticalIndent = 40;
        btnCalculateSharedA.setLayoutData(gridData);
        btnCalculateSharedA.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                if (large) {
                    shareLargeA = multiplyLargePoint(pointG, secretLargeA);
                    textSharedA.setText("(" + shareLargeA.getXAffin() + ", " + shareLargeA.getYAffin() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                } else {
                    shareA = curve.multiplyPoint(generator, secretA);
                    textSharedA.setText(shareA.toString());
                }
                btnCalculateSharedA.setBackground(cGreen);
                if (showInformationDialogs) {
                    MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                            | SWT.OK);
                    messageBox.setText(Messages.getString("ECDHView.messageSharedKeyTitle")); //$NON-NLS-1$
                    messageBox.setMessage("Alice " + Messages.getString("ECDHView.messageSharedKeyHer")); //$NON-NLS-1$ //$NON-NLS-2$
                    messageBox.open();
                }
                if ((large && shareLargeA != null && shareLargeB != null)
                        || (!large && shareA != null && shareB != null)) {
                    btnExchangeKeys.setEnabled(true);
                    canvasMain.redraw();
                }
            }
        });
        label = new Label(groupAlice, SWT.NONE);
        label.setText("A ="); //$NON-NLS-1$
        textSharedA = new Text(groupAlice, SWT.BORDER | SWT.READ_ONLY);
        textSharedA.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        btnCalculateKeyA = new Button(groupAlice, SWT.NONE);
        btnCalculateKeyA.setText(Messages.getString("ECDHView.calculate")); //$NON-NLS-1$
        btnCalculateKeyA.setEnabled(false);
        gridData = new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1);
        gridData.verticalIndent = 130;
        btnCalculateKeyA.setLayoutData(gridData);
        btnCalculateKeyA.setBackground(cRed);
        btnCalculateKeyA.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                if (large) {
                    keyLargeA = multiplyLargePoint(shareLargeB, secretLargeA);
                    textCommonKeyA.setText(keyLargeA.getXAffin().toString());
                } else {
                    keyA = curve.multiplyPoint(shareB, secretA);
                    if (keyA == null)
                        keyA = generator;
                    textCommonKeyA.setText(keyA.toString());
                }
                btnCalculateKeyA.setBackground(cGreen);
                if (showInformationDialogs) {
                    MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                            | SWT.OK);
                    messageBox.setText(Messages.getString("ECDHView.messageCommonKeyTitle")); //$NON-NLS-1$
                    messageBox.setMessage("Alice " + Messages.getString("ECDHView.messageCommonKey")); //$NON-NLS-1$ //$NON-NLS-2$
                    messageBox.open();
                }
                Boolean b;
                if (large)
                    b = keyLargeA != null && keyLargeB != null;
                else
                    b = keyA != null && keyB != null;
                if (b) {
                    action_saveToEditor.setEnabled(true);
                    action_saveToFile.setEnabled(true);
                    canvasMain.redraw();
                    if (large)
                        b = keyLargeA.getXAffin().equals(keyLargeB.getXAffin());
                    else
                        b = keyA.equals(keyB);
                    if (b) {
                        if (showInformationDialogs) {
                            MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()),
                                    SWT.ICON_INFORMATION | SWT.OK);
                            messageBox.setText(Messages.getString("ECDHView.messageSuccesTitle")); //$NON-NLS-1$
                            messageBox.setMessage(Messages.getString("ECDHView.messageSucces")); //$NON-NLS-1$
                            messageBox.open();
                        }
                    } else {
                        if (showInformationDialogs) {
                            MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_ERROR
                                    | SWT.OK);
                            messageBox.setText(Messages.getString("ECDHView.messageFailTitle")); //$NON-NLS-1$
                            messageBox.setMessage(Messages.getString("ECDHView.messageFail")); //$NON-NLS-1$
                            messageBox.open();
                        }
                    }
                }
            }

        });
        label = new Label(groupAlice, SWT.NONE);
        label.setText("S ="); //$NON-NLS-1$
        textCommonKeyA = new Text(groupAlice, SWT.BORDER | SWT.READ_ONLY);
        textCommonKeyA.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    }

    /**
     * This method initializes groupBob
     *
     */
    private void createGroupBob() {
        groupBob = new Group(canvasMain, SWT.NONE);
        groupBob.setText("Bob"); //$NON-NLS-1$
        FormData formData = new FormData(200, 360);
        formData.left = new FormAttachment(70);
        formData.top = new FormAttachment(21);
        groupBob.setLayoutData(formData);
        groupBob.setLayout(new GridLayout(2, false));
        btnSecretB = new Button(groupBob, SWT.NONE);
        btnSecretB.setText(Messages.getString("ECDHView.secret")); //$NON-NLS-1$
        btnSecretB.setEnabled(false);
        btnSecretB.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1));
        btnSecretB.setBackground(cRed);
        btnSecretB.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                SecretKeyWizard wiz;
                if (large)
                    wiz = new SecretKeyWizard("Bob", secretLargeB, largeOrder); //$NON-NLS-1$
                else
                    wiz = new SecretKeyWizard("Bob", secretB, valueN); //$NON-NLS-1$

                WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz);
                dialog.setPageSize(600, 80);
                if (dialog.open() == Window.OK) {
                    reset(2);
                    if (large) {
                        secretLargeB = wiz.getLargeSecret();
                        if (secretLargeA != null && secretLargeB != null) {
                            btnCreateSharedKeys.setEnabled(true);
                            canvasMain.redraw();
                        }
                    } else {
                        secretB = wiz.getSecret();
                        if (secretA > 0 && secretB > 0) {
                            btnCreateSharedKeys.setEnabled(true);
                            canvasMain.redraw();
                        }
                    }
                    textSecretB.setText("xxxxxxxxxxxxxxxxxxxxxx"); //$NON-NLS-1$
                    btnSecretB.setBackground(cGreen);
                    if (showInformationDialogs) {
                        MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                                | SWT.OK);
                        messageBox.setText("Bob " + Messages.getString("ECDHView.messageSecretKeyTitle")); //$NON-NLS-1$ //$NON-NLS-2$
                        messageBox
                                .setMessage("Bob " + Messages.getString("ECDHView.messageSecretKey") + " Bob" + Messages.getString("ECDHView.messageSecretKey2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        messageBox.open();
                    }
                }
            }

        });
        Label label = new Label(groupBob, SWT.NONE);
        label.setText("b ="); //$NON-NLS-1$
        textSecretB = new Text(groupBob, SWT.BORDER | SWT.PASSWORD | SWT.READ_ONLY);
        textSecretB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        btnCalculateSharedB = new Button(groupBob, SWT.NONE);
        btnCalculateSharedB.setText(Messages.getString("ECDHView.calculate")); //$NON-NLS-1$
        btnCalculateSharedB.setEnabled(false);
        GridData gridData = new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1);
        gridData.verticalIndent = 40;
        btnCalculateSharedB.setLayoutData(gridData);
        btnCalculateSharedB.setBackground(cRed);
        btnCalculateSharedB.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                if (large) {
                    shareLargeB = multiplyLargePoint(pointG, secretLargeB);
                    textSharedB.setText("(" + shareLargeB.getXAffin() + ", " + shareLargeB.getYAffin() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                } else {
                    shareB = curve.multiplyPoint(generator, secretB);
                    textSharedB.setText(shareB.toString());
                }
                btnCalculateSharedB.setBackground(cGreen);
                if (showInformationDialogs) {
                    MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                            | SWT.OK);
                    messageBox.setText(Messages.getString("ECDHView.messageSharedKeyTitle")); //$NON-NLS-1$
                    messageBox.setMessage("Bob " + Messages.getString("ECDHView.messageSharedKeyHis")); //$NON-NLS-1$ //$NON-NLS-2$
                    messageBox.open();
                }
                if ((large && shareLargeA != null && shareLargeB != null)
                        || (!large && shareA != null && shareB != null)) {
                    btnExchangeKeys.setEnabled(true);
                    canvasMain.redraw();
                }
            }

        });
        label = new Label(groupBob, SWT.NONE);
        label.setText("B ="); //$NON-NLS-1$
        textSharedB = new Text(groupBob, SWT.BORDER | SWT.READ_ONLY);
        textSharedB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        btnCalculateKeyB = new Button(groupBob, SWT.NONE);
        btnCalculateKeyB.setText(Messages.getString("ECDHView.calculate")); //$NON-NLS-1$
        btnCalculateKeyB.setEnabled(false);
        gridData = new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1);
        gridData.verticalIndent = 130;
        btnCalculateKeyB.setLayoutData(gridData);
        btnCalculateKeyB.setBackground(cRed);
        btnCalculateKeyB.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                if (large) {
                    keyLargeB = multiplyLargePoint(shareLargeA, secretLargeB);
                    textCommonKeyB.setText(keyLargeB.getXAffin().toString());
                } else {
                    keyB = curve.multiplyPoint(shareA, secretB);
                    if (keyB == null)
                        keyB = generator;
                    textCommonKeyB.setText(keyB.toString());
                }
                btnCalculateKeyB.setBackground(cGreen);
                if (showInformationDialogs) {
                    MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                            | SWT.OK);
                    messageBox.setText(Messages.getString("ECDHView.messageCommonKeyTitle")); //$NON-NLS-1$
                    messageBox.setMessage("Bob " + Messages.getString("ECDHView.messageCommonKey")); //$NON-NLS-1$ //$NON-NLS-2$
                    messageBox.open();
                }
                Boolean b;
                if (large)
                    b = keyLargeA != null && keyLargeB != null;
                else
                    b = keyA != null && keyB != null;
                if (b) {
                    action_saveToEditor.setEnabled(true);
                    action_saveToFile.setEnabled(true);
                    canvasMain.redraw();
                    if (large)
                        b = keyLargeA.getXAffin().equals(keyLargeB.getXAffin());
                    else
                        b = keyA.equals(keyB);
                    if (b) {
                        if (showInformationDialogs) {
                            MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()),
                                    SWT.ICON_INFORMATION | SWT.OK);
                            messageBox.setText(Messages.getString("ECDHView.messageSuccesTitle")); //$NON-NLS-1$
                            messageBox.setMessage(Messages.getString("ECDHView.messageSucces")); //$NON-NLS-1$
                            messageBox.open();
                        }
                    } else {
                        if (showInformationDialogs) {
                            MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_ERROR
                                    | SWT.OK);
                            messageBox.setText(Messages.getString("ECDHView.messageFailTitle")); //$NON-NLS-1$
                            messageBox.setMessage(Messages.getString("ECDHView.messageFail")); //$NON-NLS-1$
                            messageBox.open();
                        }
                    }
                }
            }
        });
        label = new Label(groupBob, SWT.NONE);
        label.setText("S ="); //$NON-NLS-1$
        textCommonKeyB = new Text(groupBob, SWT.BORDER | SWT.READ_ONLY);
        textCommonKeyB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    }

    public void paintControl(PaintEvent e) {
        GC gc = e.gc;
        Color grey = new Color(Display.getCurrent(), 140, 138, 140);
        Color lightGrey = new Color(Display.getCurrent(), 180, 177, 180);
        gc.setBackground(grey);
        int x = 100;
        int y = 70;
        int width = 10;
        int height = 0;
        int totalheight = 430;
        boolean b;
        if (large)
            b = keyLargeA == null || keyLargeB == null || !keyLargeA.getXAffin().equals(keyLargeB.getXAffin());
        else
            b = keyA == null || keyB == null || !keyA.equals(keyB);
        if (!btnChooseSecrets.getEnabled()) {
            height = 0;
        } else if (!btnCreateSharedKeys.getEnabled()) {
            height = 50;
        } else if (!btnExchangeKeys.getEnabled()) {
            height = 150;
        } else if (!btnGenerateKey.getEnabled()) {
            height = 250;
        } else if (b) {
            height = 350;
        } else
            height = totalheight;
        gc.fillRectangle(x, y, width, height);
        if (b) {
            gc.setBackground(lightGrey);
        } else {
            ImageDescriptor id = ECDHPlugin.getImageDescriptor("icons/key.png"); //$NON-NLS-1$
            ImageData imD = id.getImageData();
            imD.transparentPixel = 16777215; // white
            Image img = new Image(Display.getCurrent(), imD);
            gc.drawImage(img, 400, 480);
        }
        gc.fillRectangle(x, y + height, width, totalheight - height);
        gc.fillRectangle(x, y + totalheight, 60, width);
        Path p = new Path(Display.getCurrent());
        p.moveTo(160, 495);
        p.lineTo(160, 515);
        p.lineTo(190, 505);
        p.lineTo(160, 495);
        gc.fillPath(p);

        if (!btnGenerateKey.getEnabled())
            gc.setBackground(lightGrey);
        else
            gc.setBackground(grey);
        Path ab = new Path(Display.getCurrent());
        x = 422;
        y = 257;
        ab.moveTo(x, y);
        ab.lineTo(x + 35, y);
        ab.lineTo(x + 90, y + 65);
        ab.lineTo(x + 90, y + 180);
        ab.lineTo(x + 100, y + 180);
        ab.lineTo(x + 100, y + 175);
        ab.lineTo(x + 120, y + 185);
        ab.lineTo(x + 100, y + 195);
        ab.lineTo(x + 100, y + 190);
        ab.lineTo(x + 80, y + 190);
        ab.lineTo(x + 80, y + 70);
        ab.lineTo(x + 30, y + 10);
        ab.lineTo(x, y + 10);
        ab.lineTo(x, y);
        gc.fillPath(ab);
        ab.dispose();

        Path ba = new Path(Display.getCurrent());
        x = 543;
        y = 257;
        ba.moveTo(x, y);
        ba.lineTo(x - 35, y);
        ba.lineTo(x - 90, y + 65);
        ba.lineTo(x - 90, y + 180);
        ba.lineTo(x - 100, y + 180);
        ba.lineTo(x - 100, y + 175);
        ba.lineTo(x - 120, y + 185);
        ba.lineTo(x - 100, y + 195);
        ba.lineTo(x - 100, y + 190);
        ba.lineTo(x - 80, y + 190);
        ba.lineTo(x - 80, y + 70);
        ba.lineTo(x - 30, y + 10);
        ba.lineTo(x, y + 10);
        ba.lineTo(x, y);
        gc.fillPath(ba);
        ba.dispose();
        grey.dispose();
        lightGrey.dispose();
    }

    private String intToBitString(int i, int length) {
        String s = ""; //$NON-NLS-1$
        int j = i;
        for (int k = 0; k < length; k++) {
            s = (j % 2) + s;
            j /= 2;
        }
        return s;
    }

    protected void saveToEditor() {
        saveToEditor(getLogString());
    }

    protected void saveToFile() {
        saveToFile(getLogString());
    }

    private String getLogString() {
        String s;
        if (large) {
            s = Messages.getString("ECDHView.logHeader") + "\n\n" + Messages.getString("ECDHView.curve") + ": " + largeCurve + "\n\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

            s += Messages.getString("ECDHView.AliceParameters") + ":\n"; //$NON-NLS-1$ //$NON-NLS-2$
            s += Messages.getString("ECDHView.secretKey") + " = " + secretLargeA + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            s += Messages.getString("ECDHView.sharedKey") + " = " + shareLargeA.toString() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            s += Messages.getString("ECDHView.commonKey") + " = " + secretLargeA + " * " + shareLargeB + " = " + keyLargeA + "\n\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

            s += Messages.getString("ECDHView.BobParameters") + ":\n"; //$NON-NLS-1$ //$NON-NLS-2$
            s += Messages.getString("ECDHView.secretKey") + " = " + secretLargeB + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            s += Messages.getString("ECDHView.sharedKey") + " = " + shareLargeB + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            s += Messages.getString("ECDHView.commonKey") + " = " + secretLargeB + " * " + shareLargeA + " = " + keyLargeB + "\n\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        } else {
            s = Messages.getString("ECDHView.logHeader") + "\n\n" + Messages.getString("ECDHView.curve") + ": " + curve + "\n\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

            s += Messages.getString("ECDHView.AliceParameters") + ":\n"; //$NON-NLS-1$ //$NON-NLS-2$
            s += Messages.getString("ECDHView.secretKey") + " = " + secretA + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            s += Messages.getString("ECDHView.sharedKey") + " = " + shareA.toString() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            s += Messages.getString("ECDHView.commonKey") + " = " + secretA + " * " + shareB + " = " + keyA + "\n\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

            s += Messages.getString("ECDHView.BobParameters") + ":\n"; //$NON-NLS-1$ //$NON-NLS-2$
            s += Messages.getString("ECDHView.secretKey") + " = " + secretB + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            s += Messages.getString("ECDHView.sharedKey") + " = " + shareB + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            s += Messages.getString("ECDHView.commonKey") + " = " + secretB + " * " + shareA + " = " + keyB + "\n\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        }
        return s;
    }

    private void saveToEditor(String s) {
        if (logFile == null) {
            logFile = new File(DirectoryService.getTempDir() + "ECDH results.txt"); //$NON-NLS-1$ //$NON-NLS-2$
            logFile.deleteOnExit();
        }

        saveToFile(s);

        if (editorPage == null)
            editorPage = view.getSite().getPage();

        IEditorReference[] er = editorPage.getEditorReferences();
        for (int i = 0; i < er.length; i++) {
            if (er[i].getName().equals("ECDH results.txt")) { //$NON-NLS-1$
                er[i].getEditor(false).getSite().getPage().closeEditor(er[i].getEditor(false), false);
            }
        }

        try {
            IPath location = new org.eclipse.core.runtime.Path(logFile.getAbsolutePath());
            editorPage.openEditor(new PathEditorInput(location), "org.jcryptool.editor.text.editor.JCTTextEditor"); //$NON-NLS-1$
        } catch (PartInitException e) {
            LogUtil.logError(ECDHPlugin.PLUGIN_ID, e);
        }
    }

    private void saveToFile(String s) {
        selectFileLocation();
        if (logFile != null) {
            try {
                String[] sa = s.split("\n"); //$NON-NLS-1$
                if (sa.length > 1 || !sa[0].equals("")) { //$NON-NLS-1$
                    if (!logFile.exists())
                        logFile.createNewFile();
                    FileWriter fw = new FileWriter(logFile, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    for (int i = 0; i < sa.length; i++) {
                        if (i < sa.length - 1 || (i == sa.length - 1 && !sa[i].equals(""))) { //$NON-NLS-1$
                            bw.write(sa[i]);
                            bw.newLine();
                        }
                    }
                    bw.close();
                    fw.close();
                }
            } catch (Exception e) {
                MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()));
                messageBox.setText(Messages.getString("ECDHComposite.160")); //$NON-NLS-1$
                messageBox.setMessage(Messages.getString("ECDHComposite.161") + e.getMessage()); //$NON-NLS-1$
                messageBox.open();
            }
        }
    }

    private void selectFileLocation() {
        FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
        dialog.setFilterNames(new String[] {IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME});
        dialog.setFilterExtensions(new String[] {IConstants.TXT_FILTER_EXTENSION, IConstants.ALL_FILTER_EXTENSION});
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFileName("ECDH.txt"); //$NON-NLS-1$
        dialog.setOverwrite(true);
        String filename = dialog.open();
        if (filename == null) {
            logFile = null;
            return;
        } else
            logFile = new File(filename);
    }

    private void reset(int i) {
        switch (i) {
            case 0: // complete reset
                curve = null;
                valueN = 0;
                generator = null;
                elements = null;

                textCurve.setText(""); //$NON-NLS-1$
                textGenerator.setText(""); //$NON-NLS-1$
                btnSetPublicParameters.setBackground(cRed);
            case 1:// reset from Set public parameters button
                secretA = -1;
                secretB = -1;
                secretLargeA = null;
                secretLargeB = null;

                btnChooseSecrets.setEnabled(false);
                btnChooseSecrets.setBackground(cRed);
                btnSecretA.setEnabled(false);
                btnSecretA.setBackground(cRed);
                textSecretA.setText(""); //$NON-NLS-1$
                btnSecretB.setEnabled(false);
                btnSecretB.setBackground(cRed);
                textSecretB.setText(""); //$NON-NLS-1$
                btnCreateSharedKeys.setEnabled(false);
            default:
                shareA = null;
                shareB = null;
                keyA = null;
                keyB = null;
                shareLargeA = null;
                shareLargeB = null;
                keyLargeA = null;
                keyLargeB = null;

                btnCreateSharedKeys.setBackground(cRed);
                btnCalculateSharedA.setEnabled(false);
                btnCalculateSharedA.setBackground(cRed);
                textSharedA.setText(""); //$NON-NLS-1$
                btnCalculateSharedB.setEnabled(false);
                btnCalculateSharedB.setBackground(cRed);
                textSharedB.setText(""); //$NON-NLS-1$
                btnExchangeKeys.setEnabled(false);
                btnExchangeKeys.setBackground(cRed);
                btnGenerateKey.setEnabled(false);
                btnGenerateKey.setBackground(cRed);
                btnCalculateKeyA.setEnabled(false);
                btnCalculateKeyA.setBackground(cRed);
                textCommonKeyA.setText(""); //$NON-NLS-1$
                btnCalculateKeyB.setEnabled(false);
                btnCalculateKeyB.setBackground(cRed);
                textCommonKeyB.setText(""); //$NON-NLS-1$
                action_saveToEditor.setEnabled(false);
                action_saveToFile.setEnabled(false);
        }
        canvasMain.redraw();
        layout();
    }

    private Point multiplyLargePoint(Point p, FlexiBigInt m) {
        if (m.doubleValue() == 0)
            return null;
        if (m.doubleValue() == 1)
            return p;
        if (m.mod(new FlexiBigInt("2")).doubleValue() == 0) //$NON-NLS-1$
            return multiplyLargePoint(p, m.divide(new FlexiBigInt("2"))).multiplyBy2(); //$NON-NLS-1$
        else
            return p.add(multiplyLargePoint(p, m.subtract(new FlexiBigInt("1")))); //$NON-NLS-1$
    }

    class Animate extends Thread {
        public void run() {
            if (showAnimation) {
                GC gc = new GC(canvasMain);
                Image original = new Image(canvasMain.getDisplay(), 150, 210);
                gc.copyArea(original, 400, 250);
                double x = -50;
                double y = 0;
                String msg;
                if (large) {
                    msg = shareLargeA.getXAffin().toString(2).substring(0, 4)
                            + " " + shareLargeA.getYAffin().toString(2).substring(0, 4); //$NON-NLS-1$
                } else {
                    if (curve.getType() == ECFm.ECFm)
                        msg = intToBitString(shareA.x == elements.length ? 0 : elements[shareA.x], 5)
                                + " " + intToBitString(shareA.y == elements.length ? 0 : elements[shareA.y], 5); //$NON-NLS-1$
                    else
                        msg = intToBitString(shareA.x, 5) + " " + intToBitString(shareA.y, 5); //$NON-NLS-1$
                }
                for (int i = 0; i < 140; i++) {
                    Image im = new Image(canvasMain.getDisplay(), original, SWT.IMAGE_COPY);
                    GC gcI = new GC(im);
                    gcI.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
                    gcI.setFont(FontService.getHeaderFont());
                    gcI.drawText(msg, (int) x, (int) y, true);

                    gc.drawImage(im, 400, 250);
                    if (i < 12) {
                        x += 5;
                    } else if (i < 23) {
                        x += 5 * 11 / 12;
                        y += 5;
                    } else if (i < 48) {
                        y += 5;
                    } else if (i < 68) {
                        x += 5;
                    } else if (i == 68) {
                        y = 0;
                        x = 120;
                        if (large) {
                            msg = shareLargeB.getXAffin().toString(2).substring(0, 4)
                                    + " " + shareLargeB.getYAffin().toString(2).substring(0, 4); //$NON-NLS-1$
                        } else {
                            if (curve.getType() == ECFm.ECFm)
                                msg = intToBitString(shareB.x == elements.length ? 0 : elements[shareB.x], 5)
                                        + " " + intToBitString(shareB.y == elements.length ? 0 : elements[shareB.y], 5); //$NON-NLS-1$
                            else
                                msg = intToBitString(shareB.x, 5) + " " + intToBitString(shareB.y, 5); //$NON-NLS-1$
                        }
                    } else if (i < 83) {
                        x -= 5;
                    } else if (i < 94) {
                        x -= 5 * 11 / 12;
                        y += 4;
                    } else if (i < 121) {
                        y += 5;
                    } else {
                        x -= 5;
                    }

                    try {
                        sleep(50);
                    } catch (InterruptedException ex) {
                        LogUtil.logError(ECDHPlugin.PLUGIN_ID, ex);
                    }
                }
            }
        }
    }
}

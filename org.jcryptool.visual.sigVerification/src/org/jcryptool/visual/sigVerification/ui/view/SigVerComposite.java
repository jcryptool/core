// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sigVerification.Messages;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;
import org.jcryptool.visual.sigVerification.algorithm.Hash;
import org.jcryptool.visual.sigVerification.algorithm.Input;
import org.jcryptool.visual.sigVerification.algorithm.SigVerification;
import org.jcryptool.visual.sigVerification.ui.wizards.HashWizard;
import org.jcryptool.visual.sigVerification.ui.wizards.InputKeyWizard;
import org.jcryptool.visual.sigVerification.ui.wizards.InputWizard;
import org.jcryptool.visual.sigVerification.ui.wizards.SignaturResult;
import org.jcryptool.visual.sigVerification.ui.wizards.SignatureWizard;

/**
 * This class contains all the code required for the design and functionality of the main view. It
 * creates the components, calls the wizards and constructs the string ("SHA256withECDSA" etc.) used
 * for signing.
 *
 * @author Wilfing/Huber
 */
public class SigVerComposite extends Composite {
    private Text lblHeader;
    private Text lblDescriptionStep1;
    private Text lblDescriptionStep2;
    private Text lblDescriptionStep3;
    private Text lblDescriptionStep4;
    private Button btnHash;
    private Button btnAddInput;
    private Button btnReset;
    private Button btnDecrypt;
    private Button btnResult;
    private Canvas canvas1;
    private TabFolder tabFolder;
    private Text textGeneralDescription;
    private Label lblProgress;
    private ControlDecoration resultTrue;
    private ControlDecoration resultFalse;
    private MenuItem mntm1;
    private MenuItem mntm2;
    private MenuItem mntm3;
    private MenuItem mntm4;
    private MenuItem mntm0;

    private int hash = 0; // Values: 0-4. Hash and signature contain the
    // selected method; default is 0
    private String[] hashes = { org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdomd5,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha1,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha256,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha384,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha512 };
    private int signature = 0; // 0-3

    // Erzeugen der benötigten Objekte
    Input input = new Input();
    Hash hashInst = new Hash();
    SigVerification sigVerification = new SigVerification();
    SigVerView sigVerView;
    private int step = 0; // Fortschritt für Schritt zurück

    /**
     * @return the hash
     */
    public int getHash() {
        return hash;
    }

    /**
     * @param hash the hash to set
     */
    public void setHash(int hash) {
        this.hash = hash;
    }

    /**
     * @return the signature
     */
    public int getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(int signature) {
        this.signature = signature;
    }

    /**
     * Create the application window.
     */
    public SigVerComposite(Composite parent, int style, SigVerView view) {
        super(parent, style);
        setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        createContents(parent);
        createActions();
        this.sigVerView = view;

        // Adds reset button to the Toolbar
        IToolBarManager toolBarMenu = view.getViewSite().getActionBars().getToolBarManager();
        Action action = new Action("Reset", IAction.AS_PUSH_BUTTON) {public void run() {reset(0);}}; //$NON-NLS-1$
        action.setImageDescriptor(SigVerificationPlugin.getImageDescriptor("/icons/reset.gif")); //$NON-NLS-1$
        toolBarMenu.add(action);

    }

    /**
     * Create contents of the application window.
     *
     * @param parent
     */
    private void createContents(Composite parent) { // vs Control createContents
        parent.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
        parent.setLayout(null);

        {
            textGeneralDescription = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
            textGeneralDescription.setBounds(10, 36, 1035, 48);
            textGeneralDescription.setText(Messages.SigVerComposite_description);
            textGeneralDescription.setEditable(false);
            textGeneralDescription.setBackground(SWTResourceManager.getColor(255, 255, 255));
        }

        Menu menu = new Menu(textGeneralDescription);
        textGeneralDescription.setMenu(menu);

        mntm0 = new MenuItem(menu, SWT.NONE);
        mntm0.setText(Messages.SigVerComposite_menu);

        lblHeader = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        lblHeader.setBounds(10, 10, 1035, 35);
        lblHeader.setEditable(false);
        lblHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
        lblHeader.setText(Messages.SigVerComposite_lblHeader);
        lblHeader.setBackground(SWTResourceManager.getColor(255, 255, 255));

        {
            Group border = new Group(this, SWT.NONE);
            border.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
            // border.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
            border.setBounds(10, 99, 1035, 549);
            border.setText(Messages.SigVerComposite_lblTitle);

            {
                final Group littleBorder = new Group(border, SWT.NONE);
                littleBorder.addPaintListener(new PaintListener() {
                    public void paintControl(PaintEvent e) {
                        Rectangle clientArea;
                        int width;
                        int height;
                        GC gc;

                        gc = e.gc;
                        // Get the size of the canvas area
                        clientArea = littleBorder.getClientArea();
                        width = clientArea.width;
                        height = clientArea.height;

                        // Insert the image of the right doc
                        ImageDescriptor id = SigVerificationPlugin.getImageDescriptor("icons/image3013.png"); //$NON-NLS-1$
                        ImageData imD = id.getImageData();
                        Image img = new Image(Display.getCurrent(), imD);
                        gc.drawImage(img, width - 175, height - 222);

                    }
                });

                littleBorder.setBounds(793, 65, 218, 268);
                littleBorder.setText(Messages.SigVerComposite_btnSignature);

                btnResult = new Button(littleBorder, SWT.NONE);
                btnResult.setEnabled(false);
                btnResult.setBounds(13, 189, 190, 50);
                btnResult.setText(Messages.SigVerComposite_btnResult);

                {
                    ImageDescriptor id1 = SigVerificationPlugin.getImageDescriptor("icons/gruenerHacken.png"); //$NON-NLS-1$
                    ImageData imD1 = id1.getImageData();
                    Image img1 = new Image(Display.getCurrent(), imD1);
                    resultTrue = new ControlDecoration(littleBorder, SWT.LEFT | SWT.BOTTOM);
                    resultTrue.setMarginWidth(-18);
                    resultTrue.setDescriptionText(Messages.SigVerComposite_resutTrueDescription);
                    resultTrue.setImage(img1);
                    resultTrue.hide();

                    ImageDescriptor id2 = SigVerificationPlugin.getImageDescriptor("icons/rotesKreuz.png"); //$NON-NLS-1$
                    ImageData imD2 = id2.getImageData();
                    Image img2 = new Image(Display.getCurrent(), imD2);
                    resultFalse = new ControlDecoration(littleBorder, SWT.LEFT | SWT.BOTTOM);
                    resultFalse.setMarginWidth(-20);
                    resultFalse.setDescriptionText(Messages.SigVerComposite_resutFalseDescription);
                    resultFalse.setImage(img2);
                    resultFalse.hide();
                }

            }

            btnHash = new Button(border, SWT.NONE);
            btnHash.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                }
            });
            btnHash.setBounds(386, 81, 190, 90);
            btnHash.setEnabled(false);
            btnHash.setText(Messages.SigVerComposite_btnHash);

            btnAddInput = new Button(border, SWT.NONE);
            btnAddInput.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                }
            });
            btnAddInput.setLocation(38, 29);
            btnAddInput.setSize(200, 60);
            btnAddInput.setText(Messages.SigVerComposite_btnAddInput);

            btnReset = new Button(border, SWT.NONE);
            btnReset.setLocation(906, 376);
            btnReset.setSize(105, 30);
            btnReset.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                }
            });
            btnReset.setText(Messages.SigVerComposite_btnReset);

            btnDecrypt = new Button(border, SWT.NONE);
            btnDecrypt.setEnabled(false);
            btnDecrypt.setLocation(386, 189);
            btnDecrypt.setSize(190, 90);
            btnDecrypt.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                }
            });
            btnDecrypt.setText(Messages.SigVerComposite_btnDecrypt);
            {
                lblProgress = new Label(border, SWT.NONE);
                lblProgress.setBounds(822, 383, 90, 30);
                lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 1));
            }

            tabFolder = new TabFolder(border, SWT.NONE);
            tabFolder.setBounds(0, 406, 1011, 121);
            {
                TabItem tabStep1 = new TabItem(tabFolder, SWT.NONE);
                tabStep1.setText(Messages.SigVerComposite_tbtmNewItem_0);
                {
                    lblDescriptionStep1 = new Text(tabFolder, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
                    lblDescriptionStep1.setBackground(SWTResourceManager.getColor(255, 255, 255));
                    lblDescriptionStep1.setEditable(false);
                    lblDescriptionStep1.setText(Messages.SigVerComposite_txtDescriptionOfStep1);
                    tabStep1.setControl(lblDescriptionStep1);
                }
            }

            canvas1 = new Canvas(border, SWT.NONE);
            // canvas1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
            canvas1.setBounds(10, 88, 1011, 320);

            Menu menu1 = new Menu(lblDescriptionStep1);
            lblDescriptionStep1.setMenu(menu1);

            mntm1 = new MenuItem(menu1, SWT.NONE);
            mntm1.setText(Messages.SigVerComposite_menu);
            {
                TabItem tabStep2 = new TabItem(tabFolder, SWT.NONE);
                tabStep2.setText(Messages.SigVerComposite_tbtmNewItem_1);
                {
                    lblDescriptionStep2 = new Text(tabFolder, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
                    lblDescriptionStep2.setBackground(SWTResourceManager.getColor(255, 255, 255));
                    lblDescriptionStep2.setEditable(false);
                    lblDescriptionStep2.setText(Messages.SigVerComposite_txtDescriptionOfStep2);
                    tabStep2.setControl(lblDescriptionStep2);
                }
            }
            Menu menu2 = new Menu(lblDescriptionStep2);
            lblDescriptionStep2.setMenu(menu2);

            mntm2 = new MenuItem(menu2, SWT.NONE);
            mntm2.setText(Messages.SigVerComposite_menu);
            {
                TabItem tabStep3 = new TabItem(tabFolder, SWT.NONE);
                tabStep3.setText(Messages.SigVerComposite_tbtmNewItem_2);
                {
                    lblDescriptionStep3 = new Text(tabFolder, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
                    lblDescriptionStep3.setBackground(SWTResourceManager.getColor(255, 255, 255));
                    lblDescriptionStep3.setEditable(false);
                    lblDescriptionStep3.setText(Messages.SigVerComposite_txtDescriptionOfStep3);
                    tabStep3.setControl(lblDescriptionStep3);
                }
            }
            Menu menu3 = new Menu(lblDescriptionStep3);
            lblDescriptionStep3.setMenu(menu3);

            mntm3 = new MenuItem(menu3, SWT.NONE);
            mntm3.setText(Messages.SigVerComposite_menu);
            {
                TabItem tabStep4 = new TabItem(tabFolder, SWT.NONE);
                tabStep4.setText(Messages.SigVerComposite_tbtmNewItem_3);
                {
                    lblDescriptionStep4 = new Text(tabFolder, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
                    lblDescriptionStep4.setBackground(SWTResourceManager.getColor(255, 255, 255));
                    lblDescriptionStep4.setEditable(false);
                    lblDescriptionStep4.setText(Messages.SigVerComposite_txtDescriptionOfStep4);
                    tabStep4.setControl(lblDescriptionStep4);
                }
            }
            Menu menu4 = new Menu(lblDescriptionStep4);
            lblDescriptionStep4.setMenu(menu4);

            mntm4 = new MenuItem(menu4, SWT.NONE);
            mntm4.setText(Messages.SigVerComposite_menu);
        }
        canvas1.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                // Set the used colors
                Color lightgrey = new Color(Display.getCurrent(), 192, 192, 192);
                Color darkgrey = new Color(Display.getCurrent(), 128, 128, 128);
                Rectangle clientArea;
                int width;
                int height;
                GC gc;

                gc = e.gc;
                // Get the size of the canvas area
                clientArea = canvas1.getClientArea();
                width = clientArea.width;
                height = clientArea.height;

                // Insert the image of the key
                ImageDescriptor id = SigVerificationPlugin.getImageDescriptor("icons/key.png"); //$NON-NLS-1$
                ImageData imD = id.getImageData();
                Image img = new Image(Display.getCurrent(), imD);
                gc.drawImage(img, 425, 218);

                // Insert the image of the document
                id = SigVerificationPlugin.getImageDescriptor("icons/image3013.png"); //$NON-NLS-1$
                imD = id.getImageData();
                img = new Image(Display.getCurrent(), imD);
                gc.drawImage(img, 70, 10); // first doch pic


                gc.setBackground(lightgrey);
                // Color the all the areas in lightgrey
                // Draw shaft
                gc.fillRectangle(220, height - 285, width - 480, 30);
                gc.fillPolygon(new int[] { width - 265, height - 240, width - 265, height - 300, width - 240,
                        height - 270 });
                gc.fillRectangle(220, height - 195, width - 480, 30);
                gc.fillPolygon(new int[] { width - 265, height - 150, width - 265, height - 210, width - 240,
                        height - 180 });
                gc.fillRectangle(455, 188, 35, 30);
                gc.setBackground(darkgrey);

                gc.dispose();

            }
        });

    }

    private void createActions() {
        // Create the actions
        btnAddInput.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    // If the user already finished other steps, reset
                    // everything to this step (keep the chosen algorithms)
                    if (step > 0)
                        reset(0);

                    // Create the HashWirard
                    InputWizard wiz = new InputWizard(input);
                    // Display it
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz) {
                        @Override
                        protected void configureShell(Shell newShell) {
                            super.configureShell(newShell);
                            // set size of the wizard-window (x,y)
                            newShell.setSize(550, 500);
                        }
                    };
                    if (dialog.open() == Window.OK) {
                        // Enable to select the hash method Activate the second tab of the
                        // description
                        btnHash.setEnabled(true);
                        tabFolder.setSelection(1);
                        lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 2));
                        step = 1;
                    }
                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }
            }
        });

        // Adds a Listener for the hash select button
        btnHash.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    // If the user already finished other steps, reset
                    // everything to this step (keep the chosen algorithms)
                    if (step > 1)
                        reset(1);
                    // Create the HashWirard
                    HashWizard wiz = new HashWizard(input);
                    // Display it
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz) {
                        @Override
                        protected void configureShell(Shell newShell) {
                            super.configureShell(newShell);
                            // set size of the wizard-window (x,y)
                            newShell.setSize(350, 650);
                        }
                    };
                    if (dialog.open() == Window.OK) {
                        hash = wiz.getHash(); // get hash method (an integer)

                        // Enable to select the signature method Activate the third tab of the
                        // description
                        btnDecrypt.setEnabled(true);
                        tabFolder.setSelection(2);
                        lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 3));
                        step = 2;
                    }
                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }
            }
        });

        // Adds a Listener for the Signature select button
        btnDecrypt.addSelectionListener(new SelectionAdapter() {
            // @SuppressWarnings("deprecation")
            public void widgetSelected(SelectionEvent e) {
                try {
                    // If the user already finished other steps, reset
                    // everything to this step (keep the chosen algorithms)
                    if (step > 2)
                        reset(2);

                    SignatureWizard wiz = new SignatureWizard(hash, input);
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz) {
                        @Override
                        protected void configureShell(Shell newShell) {
                            super.configureShell(newShell);
                            // set size of the wizard-window (x,y)
                            newShell.setSize(450, 700);
                        }
                    };
                    if (dialog.open() == Window.OK) {
                        resultFalse.hide();
                        resultTrue.hide();

                        // get signature method (integer)
                        signature = wiz.getSignature();
                        // KeyStoreAlias alias = wiz.getAlias();

                        // Set method and size of signature (ex. RSA, 1024)
                        input.setSignaturemethod();
                        input.setSignatureSize();

                        // Divides signature and plaintext from imported file.
                        input.divideSignaturePlaintext();

                        // Arguments: Hash method, data to hash

                        hashInst.hashInput(hashes[hash], input.plain); // Hash
                                                                       // the
                                                                       // input

                        // Shows the description for the actual step
                        tabFolder.setSelection(3);
                        lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 4));

                    } else {
                        input.signaturemethod = null;
                    }
                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }

                // Only if a signaturemethod was chosen, the wizard for the key should open.
                if (input.signaturemethod != null) {
                    // Input Key Wizard
                    try {
                        // If the user already finished other steps, reset
                        // everything to this step (keep the chosen algorithms)
                        if (step > 2)
                            reset(2);

                        // Create the InputKeyWizard
                        InputKeyWizard wiz = new InputKeyWizard(input, sigVerification, hashInst);
                        // Display it
                        WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz) {
                            @Override
                            protected void configureShell(Shell newShell) {
                                super.configureShell(newShell);
                                // set size of the wizard-window (x,y)
                                newShell.setSize(550, 500);
                            }
                        };
                        if (dialog.open() == Window.OK) {
                            btnResult.setEnabled(true);

                            // Shows green check mark or red fail sign if comparison is
                            // correct or false
                            if (sigVerification.getResult() == true) {
                            	lblDescriptionStep4.setText(Messages.SigVerComposite_resutTrueDescription+Messages.SigVerComposite_txtDescriptionOfStep4);
                                resultFalse.hide();
                                resultTrue.show();
                            } else {
                            	lblDescriptionStep4.setText(Messages.SigVerComposite_resutFalseDescription+Messages.SigVerComposite_txtDescriptionOfStep4);
                                resultTrue.hide();
                                resultFalse.show();
                            }
                            step = 3;
                        } else {
                            sigVerification.reset();
                        }

                    } catch (Exception ex) {
                        LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                    }
                }
            }
        });

        btnResult.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    // If the user already finished other steps, reset
                    // everything to this step (keep the chosen algorithms)
                    if (step > 3) {
                        reset(3);
                    }
                    // Show the result
                    // Create the Show signature shell
                    Display display = Display.getCurrent();
                    SignaturResult shell = new SignaturResult(display, input, hashInst, sigVerification, sigVerView);
                    shell.open();
                    shell.layout();
                    while (!shell.isDisposed()) {
                        if (!display.readAndDispatch()) {
                            display.sleep();
                        }
                    }

                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }
            }
        });

        // Adds a Listener for the reset button
        btnReset.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (step > 0) {
                    step = step - 1;
                }
                reset(step);
            }
        });
    }

    private void reset(int step) {
        // If the user already finished other steps, reset everything to this
        // step (keep the chosen algorithms)
        switch (step) {
        case 0:
            btnHash.setEnabled(false);
            btnDecrypt.setEnabled(false);
            btnResult.setEnabled(false);
            tabFolder.setSelection(0);
            lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 1));
            resultFalse.hide();
            resultTrue.hide();
            break;
        case 1:
            btnDecrypt.setEnabled(false);
            btnResult.setEnabled(false);
            hashInst.reset();
            tabFolder.setSelection(1);
            lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 2));
            resultFalse.hide();
            resultTrue.hide();
            break;
        case 2:
            btnResult.setEnabled(false);
            lblDescriptionStep4.setText(Messages.SigVerComposite_txtDescriptionOfStep4);
            sigVerification.reset();
            tabFolder.setSelection(2);
            lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 3));
            resultFalse.hide();
            resultTrue.hide();
            break;
        default:
            break;
        }
    }
}

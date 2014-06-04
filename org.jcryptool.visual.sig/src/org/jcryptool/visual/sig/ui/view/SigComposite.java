// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sig.ui.view;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.services.IServiceLocator;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.visual.sig.Messages;
import org.jcryptool.visual.sig.SigPlugin;
import org.jcryptool.visual.sig.algorithm.Hash;
import org.jcryptool.visual.sig.algorithm.Input;
import org.jcryptool.visual.sig.algorithm.SigGeneration;
import org.jcryptool.visual.sig.ui.wizards.HashWizard;
import org.jcryptool.visual.sig.ui.wizards.InputWizard;
import org.jcryptool.visual.sig.ui.wizards.ShowSig;
import org.jcryptool.visual.sig.ui.wizards.SignatureWizard;

/**
 * This class contains all the code required for the design and functionality of the main view. It creates the
 * components, calls the wizards and constructs the string ("SHA256withECDSA" etc.) used for signing.
 * 
 */
public class SigComposite extends Composite implements PaintListener {
    private Text txtHash;
    private Text txtGeneralDescription;
    private Canvas canvas1;
    private Text txtDescriptionOfStep1;
    private Text txtDescriptionOfStep2;
    private Text txtDescriptionOfStep3;
    private Text txtDescriptionOfStep4;
    private TabFolder tabDescription;
    private Button btnHash;
    private Button btnSignature;
    private Button btnOpenInEditor;
    private Button btnChooseInput;
    private Button btnReset;
    private Label lblProgress;
    private Label lblHash;
    private Label lblSignature;
    private Button btnReturn;
    private MenuItem mntm1;
    private MenuItem mntm2;
    private MenuItem mntm3;
    private MenuItem mntm4;
    private MenuItem mntm0;
    private boolean called = false;
    private int hash = 0; // Values: 0-4. Hash and signature contain the
                          // selected method; default is 0
    private String[] hashes = { org.jcryptool.visual.sig.ui.wizards.Messages.HashWizard_rdomd5,
            org.jcryptool.visual.sig.ui.wizards.Messages.HashWizard_rdosha1,
            org.jcryptool.visual.sig.ui.wizards.Messages.HashWizard_rdosha256,
            org.jcryptool.visual.sig.ui.wizards.Messages.HashWizard_rdosha384,
            org.jcryptool.visual.sig.ui.wizards.Messages.HashWizard_rdosha512 };
    private int signature = 0; // 0-3
    private String sigstring = ""; //$NON-NLS-1$
    private String[] signatures = { org.jcryptool.visual.sig.ui.wizards.Messages.SignatureWizard_DSA,
            org.jcryptool.visual.sig.ui.wizards.Messages.SignatureWizard_RSA,
            org.jcryptool.visual.sig.ui.wizards.Messages.SignatureWizard_ECDSA,
            org.jcryptool.visual.sig.ui.wizards.Messages.SignatureWizard_RSAandMGF1 };

    private ICommandService commandService;
    private Category autogeneratedCategory;
    private IServiceLocator serviceLocator;

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

    private void defineCommand(final String commandId, final String name, AbstractHandler handler) {
    	Command command = commandService.getCommand(commandId);
    	command.define(name,  null, autogeneratedCategory);
    	command.setHandler(handler);
    }

    private void addContributionItem(IContributionManager manager, final String commandId,
       	final ImageDescriptor icon, final String tooltip)
    {
       	CommandContributionItemParameter param = new CommandContributionItemParameter(serviceLocator,
       		null, commandId, SWT.PUSH);
       	if(icon != null)
       		param.icon = icon;
       	if(tooltip != null && !tooltip.equals(""))
       		param.tooltip = tooltip;
       	CommandContributionItem item = new CommandContributionItem(param);
       	manager.add(item);
    }

    // Generates all Elements of the GUI
    public SigComposite(Composite parent, int style, SigView view) {
        super(parent, style);
        // The color of the textboxes
        Color white = new Color(Display.getCurrent(), 255, 255, 255);

        Label lblHeader = new Label(this, SWT.NONE);
        lblHeader.setBounds(10, 10, 699, 21);
        lblHeader.setText(Messages.SigComposite_lblHeader);
        lblHeader.setBackground(white);
        FontData fontData = lblHeader.getFont().getFontData()[0];
        Font font = new Font(this.getDisplay(), new FontData(fontData.getName(), 12, SWT.BOLD));
        lblHeader.setFont(font);

        txtGeneralDescription = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        txtGeneralDescription.setEditable(false);
        txtGeneralDescription.setBounds(10, 21, 699, 63);
        txtGeneralDescription.setText(Messages.SigComposite_description);
        txtGeneralDescription.setBackground(white);

        Menu menu = new Menu(txtGeneralDescription);
        txtGeneralDescription.setMenu(menu);

        mntm0 = new MenuItem(menu, SWT.NONE);
        mntm0.setText(Messages.SigComposite_menu);

        Group grpSignatureGeneration = new Group(this, SWT.NONE);
        grpSignatureGeneration.setText(Messages.SigComposite_grpSignatureGeneration);
        grpSignatureGeneration.setBounds(10, 90, 699, 548);

        btnHash = new Button(grpSignatureGeneration, SWT.NONE);
        btnHash.setEnabled(false);
        btnHash.setBounds(34, 190, 136, 60);
        btnHash.setText(Messages.SigComposite_btnHash);

        txtHash = new Text(grpSignatureGeneration, SWT.BORDER | SWT.WRAP);
        txtHash.setBounds(34, 365, 136, 60);
        txtHash.setEditable(false);

        btnSignature = new Button(grpSignatureGeneration, SWT.NONE);
        btnSignature.setEnabled(false);
        btnSignature.setBounds(248, 365, 136, 60);
        btnSignature.setText(Messages.SigComposite_btnSignature);

        tabDescription = new TabFolder(grpSignatureGeneration, SWT.NONE);
        tabDescription.setBounds(187, 20, 488, 191);

        TabItem tbtmStep1 = new TabItem(tabDescription, SWT.NONE);
        tbtmStep1.setText(Messages.SigComposite_tbtmNewItem_0);

        txtDescriptionOfStep1 = new Text(tabDescription, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
        txtDescriptionOfStep1.setBackground(white);
        txtDescriptionOfStep1.setEditable(false);
        txtDescriptionOfStep1.setText(Messages.SigComposite_txtDescriptionOfStep1);
        tbtmStep1.setControl(txtDescriptionOfStep1);

        Menu menu1 = new Menu(txtDescriptionOfStep1);
        txtDescriptionOfStep1.setMenu(menu1);

        mntm1 = new MenuItem(menu1, SWT.NONE);
        mntm1.setText(Messages.SigComposite_menu);

        TabItem tbtmStep2 = new TabItem(tabDescription, SWT.NONE);
        tbtmStep2.setText(Messages.SigComposite_tbtmNewItem_1);

        txtDescriptionOfStep2 = new Text(tabDescription, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
        txtDescriptionOfStep2.setBackground(white);
        txtDescriptionOfStep2.setEditable(false);
        txtDescriptionOfStep2.setText(Messages.SigComposite_txtDescriptionOfStep2);
        tbtmStep2.setControl(txtDescriptionOfStep2);

        Menu menu2 = new Menu(txtDescriptionOfStep2);
        txtDescriptionOfStep2.setMenu(menu2);

        mntm2 = new MenuItem(menu2, SWT.NONE);
        mntm2.setText(Messages.SigComposite_menu);

        TabItem tbtmStep3 = new TabItem(tabDescription, SWT.NONE);
        tbtmStep3.setText(Messages.SigComposite_tbtmNewItem_2);

        txtDescriptionOfStep3 = new Text(tabDescription, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
        txtDescriptionOfStep3.setBackground(white);
        txtDescriptionOfStep3.setEditable(false);
        txtDescriptionOfStep3.setText(Messages.SigComposite_txtDescriptionOfStep3);
        tbtmStep3.setControl(txtDescriptionOfStep3);

        Menu menu3 = new Menu(txtDescriptionOfStep3);
        txtDescriptionOfStep3.setMenu(menu3);

        mntm3 = new MenuItem(menu3, SWT.NONE);
        mntm3.setText(Messages.SigComposite_menu);

        TabItem tbtmStep4 = new TabItem(tabDescription, SWT.NONE);
        tbtmStep4.setText(Messages.SigComposite_tbtmNewItem_3);

        txtDescriptionOfStep4 = new Text(tabDescription, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
        txtDescriptionOfStep4.setBackground(white);
        txtDescriptionOfStep4.setEditable(false);
        txtDescriptionOfStep4.setText(Messages.SigComposite_txtDescriptionOfStep4);
        tbtmStep4.setControl(txtDescriptionOfStep4);

        Menu menu4 = new Menu(txtDescriptionOfStep4);
        txtDescriptionOfStep4.setMenu(menu4);

        mntm4 = new MenuItem(menu4, SWT.NONE);
        mntm4.setText(Messages.SigComposite_menu);

        btnReset = new Button(grpSignatureGeneration, SWT.NONE);
        btnReset.setBounds(579, 457, 94, 26);
        btnReset.setText(Messages.SigComposite_btnReset);

        lblProgress = new Label(grpSignatureGeneration, SWT.NONE);
        lblProgress.setBounds(490, 463, 83, 14);
        lblProgress.setText(String.format(Messages.SigComposite_lblProgress, 1));

        lblSignature = new Label(grpSignatureGeneration, SWT.NONE);
        lblSignature.setText(Messages.SigComposite_lblSignature);
        lblSignature.setBounds(248, 431, 136, 14);

        canvas1 = new Canvas(grpSignatureGeneration, SWT.NONE | SWT.TRANSPARENT);
        canvas1.setBounds(34, 21, 628, 392);

        btnChooseInput = new Button(canvas1, SWT.NONE);
        btnChooseInput.setBounds(0, 0, 136, 41);
        btnChooseInput.setText(Messages.SigComposite_btnChooseInput);

        btnOpenInEditor = new Button(canvas1, SWT.NONE);
        btnOpenInEditor.setBounds(451, 352, 175, 40);
        btnOpenInEditor.setEnabled(false);
        btnOpenInEditor.setText(Messages.SigComposite_btnOpenInEditor);

        lblHash = new Label(canvas1, SWT.NONE | SWT.TRANSPARENT);
        lblHash.setBounds(0, 235, 52, 14);
        lblHash.setText(Messages.SigComposite_lblHash);

        Group grpSignedDoc = new Group(grpSignatureGeneration, SWT.NONE);
        grpSignedDoc.setBounds(463, 251, 212, 191);
        grpSignedDoc.setText(Messages.SigComposite_grpSignedDoc);

        Label lblHashhex = new Label(grpSignatureGeneration, SWT.NONE);
        lblHashhex.setBounds(34, 431, 59, 14);
        lblHashhex.setText(org.jcryptool.visual.sig.ui.view.Messages.SigComposite_1);

        btnReturn = new Button(grpSignatureGeneration, SWT.NONE);
        btnReturn.setBounds(550, 493, 125, 28);
        btnReturn.setText(Messages.SigComposite_btnReturn);
        btnReturn.setVisible(false); // Invisible by default

        createEvents();

        // Adding the PantListener to all the canvas so the arrows can be drawn
        canvas1.addPaintListener(this);

        // Adds reset button to the toolbar
        IToolBarManager toolBarMenu = view.getViewSite().getActionBars().getToolBarManager();
        final String commandId = "org.jcyptool.visual.sig.commands.reset";
        serviceLocator = view.getViewSite();
        commandService = (ICommandService)serviceLocator.getService(ICommandService.class);
        autogeneratedCategory = commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID);
        defineCommand(commandId, "Reset", new AbstractHandler() {
        	public Object execute(ExecutionEvent event) {
        		reset(0);
        		return(null);
        	}
        });
        addContributionItem(toolBarMenu, commandId, SigPlugin.getImageDescriptor("icons/reset.gif"), "Reset");	//$NON-NLS-1$

        // Check if called by JCT-CA
        if (Input.privateKey != null) {
            btnReturn.setVisible(true); // Set button to return visible
            called = true;
        }
    }

    /**
     * This method paints the arrows used to indicate the steps. They are painted in light grey and are later changed to
     * a darker grey (stepFinished()).
     * 
     * @param e
     */
    public void paintControl(PaintEvent e) {
        // Set the used colors
        Color lightgrey = new Color(Display.getCurrent(), 192, 192, 192);
        Color darkgrey = new Color(Display.getCurrent(), 128, 128, 128);
        Rectangle clientArea;
        int width;
        int height;
        // Coordinates of the document icon
        int picx = 30;
        int picy = 55;
        GC gc;

        gc = e.gc;
        // Get the size of the canvas area
        clientArea = canvas1.getClientArea();
        width = clientArea.width;
        height = clientArea.height;

        // Insert the image of the key
        ImageDescriptor id = SigPlugin.getImageDescriptor("icons/key.png"); //$NON-NLS-1$
        ImageData imD = id.getImageData();
        Image img = new Image(Display.getCurrent(), imD);
        gc.drawImage(img, 230, 200);

        // Insert the image of the document
        id = SigPlugin.getImageDescriptor("icons/doc.png"); //$NON-NLS-1$
        imD = id.getImageData();
        img = new Image(Display.getCurrent(), imD);
        // Draw second document icon
        gc.drawImage(img, width - 130, height - 130);

        gc.setBackground(lightgrey);
        // Color the all the areas in lightgrey
        // Draw shaft
        gc.fillRectangle(55, 60, 20, height);
        gc.fillRectangle(0, height - 30, width - 220, 20);
        gc.fillRectangle(270, 300, 20, 80);
        gc.fillPolygon(new int[] { width - 220, height - 40, width - 220, height, width - 200, height - 20 });
        gc.setBackground(darkgrey);
        gc.drawImage(img, picx, picy);
        // Color the specified areas in darkgrey
        if (btnHash.getEnabled() == false) {
            // color nothing
        } else if (btnSignature.getEnabled() == false) { // Step 2
            // draw the first part of the arrow (from Document to btnHash)
            gc.fillRectangle(55, 60, 20, height / 2 - 30);
            gc.drawImage(img, picx, picy);
        } else if (btnOpenInEditor.getEnabled() == false) { // Step 3
            // draw another part of the arrow (from btnHash to btnSignature)
            gc.fillRectangle(55, 60, 20, height);
            gc.fillRectangle(0, height - 30, width / 2, 20);
            gc.drawImage(img, picx, picy);
        } else { // Step 4
            gc.fillRectangle(55, 60, 20, height);
            gc.fillRectangle(0, height - 30, width - 220, 20);
            gc.fillRectangle(270, 300, 20, 80);
            gc.fillPolygon(new int[] { width - 220, height - 40, width - 220, height, width - 200, height - 20 });
            gc.drawImage(img, picx, picy);
        }

        gc.dispose();

    }

    /**
     * Adds SelectionListeners to the Controls that need them
     */
    public void createEvents() {
        // Adds a Listener for the document
        btnChooseInput.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    // If the user already finished other steps, reset
                    // everything to this step (keep the chosen algorithms)
                    reset(0);

                    // Create the HashWirard
                    InputWizard wiz = new InputWizard();
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
                        btnHash.setEnabled(true); // Enable to select the hash method
                        tabDescription.setSelection(1); // Activate the second
                                                        // tab of the
                                                        // description
                        canvas1.redraw();
                        lblProgress.setText(String.format(Messages.SigComposite_lblProgress, 2));
                    }

                } catch (Exception ex) {
                    LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
                }
            }
        });

        // Adds a Listener for the hash select button
        btnHash.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    reset(1);
                    // Create the HashWirard
                    HashWizard wiz = new HashWizard();
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
                        lblHash.setText(hashes[hash]);

                        // Arguments: Hash method, data to hash
                        Hash.hashInput(hashes[hash], Input.data); // Hash the input

                        // Update the GUI:
                        btnSignature.setEnabled(true); // Enable to select the
                                                       // signature method
                        tabDescription.setSelection(2); // Activate the third
                                                        // tab of the
                                                        // description
                        canvas1.redraw();
                        lblProgress.setText(String.format(Messages.SigComposite_lblProgress, 3));
                        txtHash.setText(Input.hashHex);
                    }
                } catch (Exception ex) {
                    LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
                }
            }
        });

        // Adds a Listener for the Signature select button
        btnSignature.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    reset(2);
                    SignatureWizard wiz = new SignatureWizard(hash);
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz) {
                        @Override
                        protected void configureShell(Shell newShell) {
                            super.configureShell(newShell);
                            // set size of the wizard-window (x,y)
                            newShell.setSize(450, 700);
                        }
                    };
                    if (dialog.open() == Window.OK) {
                        // get signature method (integer)
                        signature = wiz.getSignature();
                        KeyStoreAlias alias = wiz.getAlias();
                        lblSignature.setText(signatures[signature]);

                        // Creates the signature for the calculated hash.
                        // Arguments: Signature methods, data to sign, Key
                        SigGeneration.signInput(chooseSignature(), Input.data, alias);

                        btnOpenInEditor.setEnabled(true);
                        // Activate the second tab of the description
                        tabDescription.setSelection(3);
                        canvas1.redraw();
                        lblProgress.setText(String.format(Messages.SigComposite_lblProgress, 4));
                        txtDescriptionOfStep4.setText(Messages.SigComposite_txtDescriptionOfStep4_Success
                                + Messages.SigComposite_txtDescriptionOfStep4);

                        if (called) {
                            MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()),
                                    SWT.ICON_INFORMATION | SWT.OK);
                            messageBox.setText(Messages.SigComposite_MessageTitleReturn);
                            messageBox.setMessage(Messages.SigComposite_MessageTextReturn);
                            messageBox.open();
                        }
                    }
                } catch (Exception ex) {
                    LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
                }
            }
        });

        // Adds a Listener for the reset button
        btnReset.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                reset(0);
            }
        });

        // Adds a Listener for OpenInEditor
        btnOpenInEditor.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {

                    // Create the Show signature shell
                    Display display = Display.getDefault();
                    ShowSig shell = new ShowSig(display, sigstring);
                    shell.open();
                    shell.layout();
                    while (!shell.isDisposed()) {
                        if (!display.readAndDispatch()) {
                            display.sleep();
                        }
                    }
                } catch (Exception ex) {
                    LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
                }
            }
        });

        // Adds a Listener for Return Button
        btnReturn.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    Input.privateKey = null;
                    Input.publicKey = null;
                    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    IViewReference ref = page.findViewReference("org.jcryptool.visual.sig.view"); //$NON-NLS-1$
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(ref);
                    page.closePerspective(null, false, true);
                } catch (Exception ex) {
                    LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
                }
            }
        });

        // To select all text
        mntm0.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                txtGeneralDescription.selectAll();
            }
        });

        // To select all text
        mntm1.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                txtDescriptionOfStep1.selectAll();
            }
        });

        // To select all text
        mntm2.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                txtDescriptionOfStep2.selectAll();
            }
        });

        // To select all text
        mntm3.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                txtDescriptionOfStep3.selectAll();
            }
        });

        // To select all text
        mntm4.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                txtDescriptionOfStep4.selectAll();
            }
        });

        // To clear the key is view is closed
        this.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                Input.reset();
            }
        });
    }

    /**
     * Resets the arrow and disables the buttons of future steps if the user clicks the button of a previous step. Also
     * clears all description-tabs of future steps and jumps to the current tab.
     * 
     * @param step the step to which the progress will be reset (valid numbers: 0-2)
     */
    private void reset(int step) {
        String s = String.format(Messages.SigComposite_lblProgress, step + 1);
        // If the user already finished other steps, reset everything to this
        // step (keep the chosen algorithms)
        switch (step) {
        case 0:
            btnHash.setEnabled(false);
        case 1:
            btnSignature.setEnabled(false);
            txtHash.setText(""); //$NON-NLS-1$
        case 2:
            btnOpenInEditor.setEnabled(false);
            txtDescriptionOfStep4.setText(Messages.SigComposite_txtDescriptionOfStep4);
            if (!called) { // If not called by jctca, reset key
                Input.privateKey = null;
            }
            Input.key = null;
            break;
        default:
            break;
        }

        lblProgress.setText(s);
        tabDescription.setSelection(step);
        // redraw canvas (to reset the arrows)
        canvas1.redraw();
    }

    /**
     * Helper method to get the correct signature method with the correct hash method. (Not every signature method
     * matches with every hash method).
     * 
     * @return The string that can be used for signing
     */
    private String chooseSignature() {
        sigstring = ""; //$NON-NLS-1$

        // Temporary solution

        if (hashes[hash].contains("MD5")) { //$NON-NLS-1$
            sigstring = "MD5with"; //$NON-NLS-1$
        }
        if (hashes[hash].contains("SHA-1")) { //$NON-NLS-1$
            sigstring = "SHA1with"; //$NON-NLS-1$
        }
        if (hashes[hash].contains("SHA-256")) { //$NON-NLS-1$
            sigstring = "SHA256with"; //$NON-NLS-1$
        }
        if (hashes[hash].contains("SHA-384")) { //$NON-NLS-1$
            sigstring = "SHA384with"; //$NON-NLS-1$
        }
        if (hashes[hash].contains("SHA-512")) { //$NON-NLS-1$
            sigstring = "SHA512with"; //$NON-NLS-1$
        }

        if (signatures[signature].contains("MGF1")) { //$NON-NLS-1$
            sigstring = sigstring + "RSAandMGF1"; //$NON-NLS-1$
        } else {
            if (signatures[signature].contains("RSA")) { //$NON-NLS-1$
                sigstring = sigstring + "RSA"; //$NON-NLS-1$
            }
        }

        if (signatures[signature].contains("ECDSA")) { //$NON-NLS-1$
            sigstring = sigstring + "ECDSA"; //$NON-NLS-1$
        } else {
            if (signatures[signature].contains("DSA")) { //$NON-NLS-1$
                sigstring = sigstring + "DSA"; //$NON-NLS-1$
            }
        }

        return sigstring;
    }
}

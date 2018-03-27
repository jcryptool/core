// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013, 2014 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.view;

import java.util.Arrays;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IToolBarManager;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
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
 * @author Holger Friedrich (replaced an Action with a Command)
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
    private TabFolder tabFolderSteps;
    private Text textGeneralDescription;
    private Label lblProgress;
    private MenuItem mntm1;
    private MenuItem mntm2;
    private MenuItem mntm3;
    private MenuItem mntm4;
    private MenuItem mntm0;
    private boolean resultOk = false;
    private boolean resultErr = false;
    private int btnHeight = 90;

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
	private Canvas canvasDocRight;
	private Canvas canvasDocLeft;
	private Composite centerTopComposite;

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
     * Sets the booleans resultOk and resultErr to the new values and redraws the canvas on the right.
     * Also, updates the tooltip for the canvas on the right side of the main composite.
     * 
     * @param resultOk the boolean to set the variable resultOk to
     */
    private void setResult(boolean resultOk, boolean resultErr) {
    	this.resultOk = resultOk;
    	this.resultErr = resultErr;
    	canvasDocRight.redraw();
    	canvasDocRight.update();
    	if (resultOk) {
    		canvasDocRight.setToolTipText(Messages.SigVerComposite_resutTrueDescription);
    	} else if (resultErr) {
    		canvasDocRight.setToolTipText(Messages.SigVerComposite_resutFalseDescription);
    	} else {
    		canvasDocRight.setToolTipText("");
    	}
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
        final String resetCommandId = "org.jcryptool.visual.sigVerification.commands.reset.sigVer";
        AbstractHandler resetHandler = new AbstractHandler() {
	       	public Object execute(ExecutionEvent event) {
	       		reset(0);
	       		return null;
	       	}
        };
        defineCommand(resetCommandId, "Reset", resetHandler);
        addContributionItem(toolBarMenu, resetCommandId,
       		 SigVerificationPlugin.getImageDescriptor("/icons/reset.gif"), null, SWT.PUSH);
    }

    private void defineCommand(final String commandId, final String name, AbstractHandler handler) {
        ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
    	Command command = commandService.getCommand(commandId);
    	command.define(name,  null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
    	command.setHandler(handler);
    }

    private void addContributionItem(IContributionManager manager, final String commandId,
           	final ImageDescriptor icon, final String tooltip, int Style)
        {
           	CommandContributionItemParameter param = new CommandContributionItemParameter(PlatformUI.getWorkbench(),
           		null, commandId, Style);
           	if(icon != null)
           		param.icon = icon;
           	if(tooltip != null && !tooltip.equals(""))
           		param.tooltip = tooltip;
           	CommandContributionItem item = new CommandContributionItem(param);
           	manager.add(item);
        }

    /**
     * Create contents of the application window.
     *
     * @param parent
     */
    private void createContents(Composite parent) {
        parent.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
        Color white = SWTResourceManager.getColor(255, 255, 255);
        setLayout(new GridLayout());

        //HEADER AND DESCRPITION (TOP)
        Composite introComposite = new Composite(this, SWT.NONE);
        introComposite.setBackground(white);
        introComposite.setLayout(new GridLayout());
        introComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        lblHeader = new Text(introComposite, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        lblHeader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        lblHeader.setEditable(false);
        lblHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
        lblHeader.setText(Messages.SigVerComposite_lblHeader);
        lblHeader.setBackground(white);

        textGeneralDescription = new Text(introComposite, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        GridData gd_textGeneralDescription = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_textGeneralDescription.widthHint = 600;
        textGeneralDescription.setLayoutData(gd_textGeneralDescription);
        textGeneralDescription.setText(Messages.SigVerComposite_description);
        textGeneralDescription.setEditable(false);
        textGeneralDescription.setBackground(white);
        Menu menu = new Menu(textGeneralDescription);
        textGeneralDescription.setMenu(menu);
        mntm0 = new MenuItem(menu, SWT.NONE);
        mntm0.setText(Messages.SigVerComposite_menu);

        Group mainGroup = new Group(this, SWT.NONE);
        mainGroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout gl_mainGroup = new GridLayout(3, false);
        gl_mainGroup.horizontalSpacing = 0;
        mainGroup.setLayout(gl_mainGroup);
        mainGroup.setText(Messages.SigVerComposite_lblTitle);
        
        //GROUP INPUT (LEFT)
        Composite compositeLeft = new Composite(mainGroup, SWT.NONE);
        GridData gd_compositeLeft = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_compositeLeft.widthHint = 250;
        gd_compositeLeft.heightHint = 350;
        compositeLeft.setLayoutData(gd_compositeLeft);
        GridLayout gl_compositeLeft = new GridLayout();
        gl_compositeLeft.marginWidth = 0;
        gl_compositeLeft.marginLeft = 5;
        compositeLeft.setLayout(gl_compositeLeft);
        
        Group groupInput = new Group(compositeLeft, SWT.NONE);
        GridData gd_groupInput = new GridData(SWT.FILL, SWT.CENTER, true, true);
        gd_groupInput.heightHint = 330;
        groupInput.setLayoutData(gd_groupInput);
        groupInput.setLayout(new GridLayout());
        
        btnAddInput = new Button(groupInput, SWT.NONE);
        GridData gd_btnAddInput = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_btnAddInput.widthHint = 190;
        gd_btnAddInput.heightHint = 50;
        btnAddInput.setLayoutData(gd_btnAddInput);
        btnAddInput.setText(Messages.SigVerComposite_btnAddInput);
        
        canvasDocLeft = new Canvas(groupInput, SWT.NONE);
        GridData gd_canvasDocLeft = new GridData(SWT.FILL, SWT.FILL, true, true);
        canvasDocLeft.setLayoutData(gd_canvasDocLeft);
        canvasDocLeft.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {         
                GC gc = e.gc;
                // Get the size of the canvas area
                Rectangle clientArea = canvasDocLeft.getClientArea();
                
                // Insert the image of the left document
                ImageDescriptor id = SigVerificationPlugin.getImageDescriptor("icons/image3013.png"); //$NON-NLS-1$
                ImageData imD = id.getImageData(100);
                Image img = new Image(Display.getCurrent(), imD);
                gc.drawImage(img, (clientArea.width - imD.width) /2 , (clientArea.height - imD.height) / 2);
                
                gc.dispose();
            }
        });
        
        @SuppressWarnings("unused") //dummy Composite 
		Composite compositeBtmLeft = new Composite(compositeLeft, SWT.NONE);
        
        //CENTER AREA BETWEEN THE INPUT GROUP AND THE VERIFY GROUP
        Composite centerComposite = new Composite(mainGroup, SWT.NONE);
        GridData gd_centerComposite = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd_centerComposite.widthHint = 400;
        centerComposite.setLayoutData(gd_centerComposite);
        GridLayout gl_centerComposite = new GridLayout();
        gl_centerComposite.marginWidth = 0;
        centerComposite.setLayout(gl_centerComposite);
        
        centerTopComposite = new Composite(centerComposite, SWT.NONE);
        GridData gd_centerTopComposite = new GridData(SWT.FILL, SWT.FILL, true, true);
        centerTopComposite.setLayoutData(gd_centerTopComposite);
        GridLayout gl_centerTopComposite = new GridLayout();
        gl_centerTopComposite.verticalSpacing = 20;
        centerTopComposite.setLayout(gl_centerTopComposite);        
        centerTopComposite.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                // Set the used colors
                Color lightgrey = new Color(Display.getCurrent(), 192, 192, 192);
                Color darkgrey = new Color(Display.getCurrent(), 128, 128, 128);

                GC gc = e.gc;
                // Get the size of the canvas area
                Rectangle clientArea = centerTopComposite.getClientArea();
                int width = clientArea.width;
                int height = clientArea.height;
                
                //Compute values for drawing the arrows
                int verticalCenter = height / 2;
                int arrowHeight = 30;
                int spacingBetweenButtons = gl_centerTopComposite.verticalSpacing;
                
                //First arrow
                if (step > 1) {
                	gc.setBackground(darkgrey);
                } else {
                	gc.setBackground(lightgrey);
                }
                int yCoordFirstArrow = verticalCenter - spacingBetweenButtons/2 - btnHeight/2 - arrowHeight/2;
                gc.fillRectangle(0, yCoordFirstArrow , width - 30, arrowHeight);
                gc.fillPolygon(new int[] { width - 30, yCoordFirstArrow - arrowHeight/2, 
                		width - 30, yCoordFirstArrow + arrowHeight + arrowHeight/2, 
                		width, yCoordFirstArrow + arrowHeight/2 });
                
                //Second arrow
                if (step > 2) {
                	gc.setBackground(darkgrey);
                } else {
                	gc.setBackground(lightgrey);
                }
                int yCoordSecondArrow = verticalCenter + spacingBetweenButtons/2 + btnHeight/2 - arrowHeight/2;
                gc.fillRectangle(0, yCoordSecondArrow, width - 30 , arrowHeight);
                gc.fillPolygon(new int[] { width - 30, yCoordSecondArrow - arrowHeight/2, 
                		width - 30, yCoordSecondArrow + arrowHeight + arrowHeight/2, 
                		width, yCoordSecondArrow + arrowHeight/2 });
                
                //Arrow pointing on key
                gc.fillRectangle(width/2 - 15, yCoordSecondArrow, arrowHeight, height - yCoordSecondArrow);
                
                gc.setBackground(darkgrey);

                gc.dispose();
            }
        });
        
        
        
        btnHash = new Button(centerTopComposite, SWT.NONE);
        GridData gd_btnHash = new GridData(SWT.CENTER, SWT.BOTTOM, true, true);
        gd_btnHash.widthHint = 200;
        gd_btnHash.heightHint = btnHeight;
        btnHash.setLayoutData(gd_btnHash);
        btnHash.setEnabled(false);
        btnHash.setText(Messages.SigVerComposite_btnHash);

        btnDecrypt = new Button(centerTopComposite, SWT.NONE);
        GridData gd_btnDecrypt = new GridData(SWT.CENTER, SWT.TOP, true, true);
        gd_btnDecrypt.widthHint = 200;
        gd_btnDecrypt.heightHint = btnHeight;
        btnDecrypt.setLayoutData(gd_btnDecrypt);
        btnDecrypt.setEnabled(false);
        btnDecrypt.setText(Messages.SigVerComposite_btnDecrypt);
        
        Composite centerBtmComposite = new Composite(centerComposite, SWT.NONE);
        GridData gd_centerBtmComposite = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_centerBtmComposite.heightHint = 110;
        centerBtmComposite.setLayoutData(gd_centerBtmComposite);
        centerBtmComposite.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
            	GC gc = e.gc;
                // Get the size of the canvas area
                Rectangle clientArea = centerBtmComposite.getClientArea();
                int width = clientArea.width;
                int height = clientArea.height;

                // Insert the image of the key
                ImageDescriptor id = SigVerificationPlugin.getImageDescriptor("icons/key.png"); //$NON-NLS-1$
                ImageData imD = id.getImageData(100);
                Image img = new Image(Display.getCurrent(), imD);
                gc.drawImage(img, width/2 - imD.width/2, height - imD.height);
                
                gc.dispose();
            }
        });
        
        //GROUP VERIFY (RIGHT)
        Composite compositeRight = new Composite(mainGroup, SWT.NONE);
        GridData gd_compositeRight = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_compositeRight.widthHint = 250;
        gd_compositeRight.heightHint = 350;
        compositeRight.setLayoutData(gd_compositeRight);
        GridLayout gl_compositeRight = new GridLayout();
        gl_compositeRight.marginWidth = 0;
        gl_compositeRight.marginRight = 5;
        compositeRight.setLayout(gl_compositeRight);
        
        Group groupVerify = new Group(compositeRight, SWT.NONE);
        GridData gd_groupVerify = new GridData(SWT.FILL, SWT.CENTER, true, true);
        gd_groupVerify.heightHint = 330;
        groupVerify.setLayoutData(gd_groupVerify);
        groupVerify.setLayout(new GridLayout());
        groupVerify.setText(Messages.SigVerComposite_btnSignature);
        
        canvasDocRight = new Canvas(groupVerify, SWT.NONE);
        GridData gd_canvasDocRight = new GridData(SWT.FILL, SWT.FILL, true, true);
        canvasDocRight.setLayoutData(gd_canvasDocRight);
        canvasDocRight.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {         
                GC gc = e.gc;
                Rectangle clientArea = canvasDocRight.getClientArea();
                
                ImageDescriptor idRightDoc = SigVerificationPlugin.getImageDescriptor("icons/image3013.png"); //$NON-NLS-1$
                ImageData imdRightDoc = idRightDoc.getImageData(100);
                Image imgRightDoc = new Image(Display.getCurrent(), imdRightDoc);

                int docX = (clientArea.width - imdRightDoc.width) /2;
                int docY = (clientArea.height - imdRightDoc.height) / 2;
                gc.drawImage(imgRightDoc,  docX, docY);
                
                if (resultOk) {
                	ImageDescriptor idIconGreen = SigVerificationPlugin.getImageDescriptor("icons/gruenerHacken.png"); //$NON-NLS-1$
                    ImageData imdIconGreen = idIconGreen.getImageData(100);
                    Image imgIconGreen = new Image(Display.getCurrent(), imdIconGreen);
                	gc.drawImage(imgIconGreen, docX-70, docY-90+imdRightDoc.height);
                }
                if (resultErr) {
                    ImageDescriptor idIconRed = SigVerificationPlugin.getImageDescriptor("icons/rotesKreuz.png"); //$NON-NLS-1$
                    ImageData imdIconRed = idIconRed.getImageData(100);
                    Image imgIconRed = new Image(Display.getCurrent(), imdIconRed);
                	gc.drawImage(imgIconRed, docX-70, docY-90+imdRightDoc.height);
                }
                
                gc.dispose();
            }
        });

        btnResult = new Button(groupVerify, SWT.NONE);
        GridData gd_btnResult = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_btnResult.widthHint = 190;
        gd_btnResult.heightHint = 50;
        btnResult.setLayoutData(gd_btnResult);
        btnResult.setEnabled(false);
        btnResult.setText(Messages.SigVerComposite_btnResult);
        
        //PROGRESS AREA (BOTTOM RIGHT)
        Composite progressComposite = new Composite(compositeRight, SWT.NONE);
        progressComposite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        progressComposite.setLayout(new GridLayout(2, false));
        
        lblProgress = new Label(progressComposite, SWT.NONE);
        GridData gd_lblProgress = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        gd_lblProgress.widthHint = 160;
        lblProgress.setLayoutData(gd_lblProgress);
        lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 1));
        
        btnReset = new Button(progressComposite, SWT.NONE);
        GridData gd_btnReset = new GridData(SWT.RIGHT, SWT.FILL, true, false);
        gd_btnReset.widthHint = 130;
        btnReset.setLayoutData(gd_btnReset);
        btnReset.setText(Messages.SigVerComposite_btnReset);

        //TAB FOLDER (BOTTOM)
        tabFolderSteps = new TabFolder(mainGroup, SWT.NONE);
        GridData gd_tabFolderSteps = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
        gd_tabFolderSteps.widthHint = 800;
        gd_tabFolderSteps.heightHint = 170;
        tabFolderSteps.setLayoutData(gd_tabFolderSteps);
        
        TabItem tabStep1 = new TabItem(tabFolderSteps, SWT.NONE);
        tabStep1.setText(Messages.SigVerComposite_tbtmNewItem_0);
        lblDescriptionStep1 = new Text(tabFolderSteps, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
        lblDescriptionStep1.setBackground(SWTResourceManager.getColor(255, 255, 255));
        lblDescriptionStep1.setEditable(false);
        lblDescriptionStep1.setText(Messages.SigVerComposite_txtDescriptionOfStep1);
        tabStep1.setControl(lblDescriptionStep1);
        Menu menu1 = new Menu(lblDescriptionStep1);
        lblDescriptionStep1.setMenu(menu1);
        mntm1 = new MenuItem(menu1, SWT.NONE);
        mntm1.setText(Messages.SigVerComposite_menu);
        
	    TabItem tabStep2 = new TabItem(tabFolderSteps, SWT.NONE);
	    tabStep2.setText(Messages.SigVerComposite_tbtmNewItem_1);
        lblDescriptionStep2 = new Text(tabFolderSteps, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
        lblDescriptionStep2.setBackground(SWTResourceManager.getColor(255, 255, 255));
        lblDescriptionStep2.setEditable(false);
        lblDescriptionStep2.setText(Messages.SigVerComposite_txtDescriptionOfStep2);
        tabStep2.setControl(lblDescriptionStep2);        
        Menu menu2 = new Menu(lblDescriptionStep2);
        lblDescriptionStep2.setMenu(menu2);
        mntm2 = new MenuItem(menu2, SWT.NONE);
        mntm2.setText(Messages.SigVerComposite_menu);
        
        TabItem tabStep3 = new TabItem(tabFolderSteps, SWT.NONE);
        tabStep3.setText(Messages.SigVerComposite_tbtmNewItem_2);
        lblDescriptionStep3 = new Text(tabFolderSteps, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
        lblDescriptionStep3.setBackground(SWTResourceManager.getColor(255, 255, 255));
        lblDescriptionStep3.setEditable(false);
        lblDescriptionStep3.setText(Messages.SigVerComposite_txtDescriptionOfStep3);
        tabStep3.setControl(lblDescriptionStep3);      
        Menu menu3 = new Menu(lblDescriptionStep3);
        lblDescriptionStep3.setMenu(menu3);
        mntm3 = new MenuItem(menu3, SWT.NONE);
        mntm3.setText(Messages.SigVerComposite_menu);
        
        TabItem tabStep4 = new TabItem(tabFolderSteps, SWT.NONE);
        tabStep4.setText(Messages.SigVerComposite_tbtmNewItem_3);
        lblDescriptionStep4 = new Text(tabFolderSteps, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
        lblDescriptionStep4.setBackground(SWTResourceManager.getColor(255, 255, 255));
        lblDescriptionStep4.setEditable(false);
        lblDescriptionStep4.setText(Messages.SigVerComposite_txtDescriptionOfStep4);
        tabStep4.setControl(lblDescriptionStep4);        
        Menu menu4 = new Menu(lblDescriptionStep4);
        lblDescriptionStep4.setMenu(menu4);
        mntm4 = new MenuItem(menu4, SWT.NONE);
        mntm4.setText(Messages.SigVerComposite_menu);
    }

    // Creates the actions
    private void createActions() {
    	// Adds a Listener for the add input button
        btnAddInput.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    //Memorize first 1000 bytes of the old message
                	int lengthOldMessage;
                	byte[] oldMessage = null;
                	if (input.data != null) {
                    	lengthOldMessage = Math.min(input.data.length, 1000);
                        oldMessage = Arrays.copyOfRange(input.data, 0, lengthOldMessage);
                	}

                    // Create the InputWizard and display it
                    InputWizard wiz = new InputWizard(input);
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz) {
                        @Override
                        protected void configureShell(Shell newShell) {
                            super.configureShell(newShell);
                            // set size of the wizard-window (x,y)
                            newShell.setSize(800, 480);
                            newShell.setMinimumSize(800, 480);
                        }
                    };
                    if (dialog.open() == Window.OK) { 
                    	//Get the first 1000 bytes of the new message
                    	int lengthNewMessage;
                    	byte[] newMessage = null;
                    	if (input.data != null) {
                        	lengthNewMessage = Math.min(input.data.length, 1000);
                        	newMessage = Arrays.copyOfRange(input.data, 0, lengthNewMessage);
                    	}
                    	
                    	// Only act if the user changed the input text (if the first 1000 bytes differ from old input)
                    	if (input.data == null || !Arrays.equals(oldMessage, newMessage)) { 
                            // If the user already finished other steps, reset
                            // everything to this step (keep the chosen algorithms)
                            if (step > 0)
                                reset(0);
                            
                            //Show tooltip information
                            if (input.tooltipData != null) {
                            	if (input.path != null) {
                            		canvasDocLeft.setToolTipText(input.path + "\n\n" + Messages.SigVerComposite_FileInput_Tooltip + input.tooltipData);
                            	}
                            }

                            // Enable to select the hash method Activate the second tab of the description
                            btnHash.setEnabled(true);
                            tabFolderSteps.setSelection(1);
                            lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 2));
                            step = 1;
                            centerTopComposite.redraw();
                    	}

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
                    // Create the HashWizard and display it
                    HashWizard wiz = new HashWizard(input);
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz) {
                        @Override
                        protected void configureShell(Shell newShell) {
                            super.configureShell(newShell);
                            // set size of the wizard-window (x,y)
                            newShell.setSize(350, 700);
                        }
                    };
                    if (dialog.open() == Window.OK) {
                        hash = wiz.getHash(); // get hash method (an integer)

                        // Enable to select the signature method Activate the third tab of the description
                        btnDecrypt.setEnabled(true);
                        tabFolderSteps.setSelection(2);
                        lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 3));
                        step = 2;
                        centerTopComposite.redraw();
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
                            newShell.setSize(550, 700);
                            newShell.setMinimumSize(550, 700);
                        }
                    };
                    if (dialog.open() == Window.OK) {
                    	setResult(false, false);

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
                        tabFolderSteps.setSelection(3);
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

                            // Shows green check mark or red fail sign if comparison is correct or false
                            if (sigVerification.getResult() == true) {
                            	lblDescriptionStep4.setText(Messages.SigVerComposite_resutTrueDescription+Messages.SigVerComposite_txtDescriptionOfStep4);
                            	setResult(true, false);
                            } else {
                            	lblDescriptionStep4.setText(Messages.SigVerComposite_resutFalseDescription+Messages.SigVerComposite_txtDescriptionOfStep4);
                            	setResult(false, true);
                            }
                            step = 3;
                            centerTopComposite.redraw();
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
            tabFolderSteps.setSelection(0);
            lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 1));
            canvasDocLeft.setToolTipText("");
        	setResult(false, false);
        	centerTopComposite.redraw();
            break;
        case 1:
            btnDecrypt.setEnabled(false);
            btnResult.setEnabled(false);
            hashInst.reset();
            tabFolderSteps.setSelection(1);
            lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 2));
        	setResult(false, false);
        	centerTopComposite.redraw();
            break;
        case 2:
            btnResult.setEnabled(false);
            lblDescriptionStep4.setText(Messages.SigVerComposite_txtDescriptionOfStep4);
            sigVerification.reset();
            tabFolderSteps.setSelection(2);
            lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 3));
        	setResult(false, false);
        	centerTopComposite.redraw();
            break;
        default:
            break;
        }
    }
}

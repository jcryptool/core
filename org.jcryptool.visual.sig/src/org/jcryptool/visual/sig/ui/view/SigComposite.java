// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sig.ui.view;

import java.util.Arrays;

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
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
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
public class SigComposite extends Composite {
    private Text txtHash;
    private Text txtGeneralDescription;
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
	private Label lblHashhex;
	private Canvas c1;
	private Canvas c2;
	private Canvas canvasBtmCenter;
	private Canvas c3;
	private MenuItem copy;

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
        setLayout(new GridLayout());
        
        Clipboard clipboard = new Clipboard(getDisplay());
        
        Color white = new Color(Display.getCurrent(), 255, 255, 255);
        
        Composite introComposite = new Composite(this, SWT.NONE);
        introComposite.setBackground(white);
        introComposite.setLayout(new GridLayout());
        introComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Label lblHeader = new Label(introComposite, SWT.NONE);
        lblHeader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        lblHeader.setText(Messages.SigComposite_lblHeader);
        lblHeader.setBackground(white);
        FontData fontData = lblHeader.getFont().getFontData()[0];
        Font font = new Font(this.getDisplay(), new FontData(fontData.getName(), 12, SWT.BOLD));
        lblHeader.setFont(font);

        txtGeneralDescription = new Text(introComposite, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        txtGeneralDescription.setEditable(false);
        GridData gd_txtGeneralDescription = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_txtGeneralDescription.widthHint = 500;
        txtGeneralDescription.setLayoutData(gd_txtGeneralDescription);
        txtGeneralDescription.setText(Messages.SigComposite_description);
        txtGeneralDescription.setBackground(white);

        Menu menu = new Menu(txtGeneralDescription);
        txtGeneralDescription.setMenu(menu);

        mntm0 = new MenuItem(menu, SWT.NONE);
        mntm0.setText(Messages.SigComposite_menu);

        Group grpSignatureGeneration = new Group(this, SWT.NONE);
        grpSignatureGeneration.setText(Messages.SigComposite_grpSignatureGeneration);
        GridLayout gl_grpSignatureGeneration = new GridLayout();
        grpSignatureGeneration.setLayout(gl_grpSignatureGeneration);
        grpSignatureGeneration.setLayout(new GridLayout());
        grpSignatureGeneration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        Composite compSignatureGeneration = new Composite(grpSignatureGeneration, SWT.NONE);
        compSignatureGeneration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout gl_compSignatureGeneration = new GridLayout(2, false);
        gl_compSignatureGeneration.verticalSpacing = 0;
        gl_compSignatureGeneration.horizontalSpacing = 0;
        compSignatureGeneration.setLayout(gl_compSignatureGeneration);
        
        /* columnLeft contains the buttons "Choose Input" and "Hash function",
           as well as the hash text field and the downwards arrow */
        Composite columnLeft = new Composite(compSignatureGeneration, SWT.NONE | SWT.TRANSPARENT);
        columnLeft.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 6));
        GridLayout gl_columnLeft = new GridLayout();
        gl_columnLeft.verticalSpacing = 0;
        gl_columnLeft.horizontalSpacing = 0;
        gl_columnLeft.marginWidth = 0;
        gl_columnLeft.marginHeight = 0;
        gl_columnLeft.marginLeft = 5;
        columnLeft.setLayout(gl_columnLeft);
        
        btnChooseInput = new Button(columnLeft, SWT.NONE);
        GridData gd_btnChooseInput = new GridData(SWT.CENTER, SWT.FILL, false, false);
        gd_btnChooseInput.heightHint = 41;
        gd_btnChooseInput.widthHint = 150;
        btnChooseInput.setLayoutData(gd_btnChooseInput);
        btnChooseInput.setText(Messages.SigComposite_btnChooseInput);
        
        //Canvas between "Choose Input" button and "Hash function" button
        c1 = new Canvas(columnLeft, SWT.NONE);
        GridData gd_c1 = new GridData(SWT.FILL, SWT.FILL, false, true);
        gd_c1.heightHint = 150;
        c1.setLayoutData(gd_c1);
        c1.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
			    GC gc = e.gc;
		        Rectangle area = c1.getClientArea(); // Get the size of the canvas

		        //Set correct color (light or dark grey, depending on the status) and draw arrow part
		        int rgbVal = btnHash.getEnabled() ? 128 : 192; //128 dark grey, 192 light grey
		        gc.setBackground(new Color(Display.getCurrent(), rgbVal, rgbVal, rgbVal));
		        gc.fillRectangle(area.width / 2 - 10, 20, 20, area.height);	

		        //Draw the image
		        ImageDescriptor id = SigPlugin.getImageDescriptor("icons/doc.png"); //$NON-NLS-1$
		        ImageData imD = id.getImageData(100);
		        Image img = new Image(Display.getCurrent(), imD);
		        gc.drawImage(img, (area.width - imD.width) / 2, 10);

		        gc.dispose();			
			}
        });
        
        //Add context menu for copying to clipboard
        Menu c1Menu = new Menu(c1);
        c1.setMenu(c1Menu);
        copy = new MenuItem(c1Menu, SWT.NONE);
        copy.setEnabled(false);
        copy.setText(Messages.SigComposite_Copy);
        copy.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String tooltipText = c1.getToolTipText();
				if (tooltipText != null) {
					clipboard.setContents(new Object[] {tooltipText}, new Transfer[] {TextTransfer.getInstance()});
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {} 	
        });
        
        
        btnHash = new Button(columnLeft, SWT.NONE);
        btnHash.setEnabled(false);
        GridData gd_btnHash = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_btnHash.heightHint = 60;
        btnHash.setLayoutData(gd_btnHash);
        btnHash.setText(Messages.SigComposite_btnHash);
        
        //Canvas between "Hash function" button and Hash text field
        c2 = new Canvas(columnLeft, SWT.NONE);
        GridData gd_c2 = new GridData(SWT.FILL, SWT.FILL, false, true);
        gd_c2.heightHint = 150;
        c2.setLayoutData(gd_c2);
        c2.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
			    GC gc = e.gc;
		        Rectangle area = c2.getClientArea(); // Get the size of the canvas

		        //Set correct color (light(192) or dark(128) grey, depending on the status) and draw arrow part
		        int rgbVal = btnHash.getEnabled() && btnSignature.getEnabled() ? 128 : 192;
		        gc.setBackground(new Color(Display.getCurrent(), rgbVal, rgbVal, rgbVal));
		        gc.fillRectangle(area.width / 2 - 10, 0, 20, area.height);			      

		        gc.dispose();			
			}
        });
        
        lblHash = new Label(c2, SWT.TRANSPARENT);
        lblHash.setBounds(5, 0, 60, 30);
        lblHash.setText(Messages.SigComposite_lblHash);

        txtHash = new Text(columnLeft, SWT.BORDER | SWT.WRAP);
        GridData gd_txtHash = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_txtHash.heightHint = 60;
        gd_txtHash.widthHint = 160;
        txtHash.setLayoutData(gd_txtHash);
        txtHash.setEditable(false);
        
        /** columnRight contains the tabbed text box with the explanations, as well as canvasBtmCenter with
            all the arrows and images below it, the "Signature function" button and the "Signed document" group */         
        Composite columnRight = new Composite(compSignatureGeneration, SWT.NONE | SWT.TRANSPARENT);
        columnRight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 6));
        GridLayout gl_columnRight = new GridLayout(5, false);
        gl_columnRight.verticalSpacing = 0;
        gl_columnRight.horizontalSpacing = 0;
        gl_columnRight.marginWidth = 0;
        gl_columnRight.marginHeight = 0;
        gl_columnRight.marginRight = 5;
        columnRight.setBackground(new Color(Display.getCurrent(), 120, 120, 120));
        columnRight.setLayout(gl_columnRight);
        
        tabDescription = new TabFolder(columnRight, SWT.NONE);
        GridData gd_tabDescription = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
        gd_tabDescription.widthHint = 488;
        gd_tabDescription.horizontalIndent = 20;
        int btnChooseInputHeight = ((GridData)btnChooseInput.getLayoutData()).heightHint;
        int c1Height = ((GridData)c1.getLayoutData()).heightHint;
        int btnHashHeight = ((GridData)btnHash.getLayoutData()).heightHint;
        gd_tabDescription.heightHint = btnChooseInputHeight + c1Height + (btnHashHeight / 2) - 35;
        tabDescription.setLayoutData(gd_tabDescription);

        TabItem tbtmStep1 = new TabItem(tabDescription, SWT.NONE);
        tbtmStep1.setText(Messages.SigComposite_tbtmNewItem_0);

        txtDescriptionOfStep1 = new Text(tabDescription, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
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

        txtDescriptionOfStep2 = new Text(tabDescription, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
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

        txtDescriptionOfStep3 = new Text(tabDescription, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
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

        txtDescriptionOfStep4 = new Text(tabDescription, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
        txtDescriptionOfStep4.setBackground(white);
        txtDescriptionOfStep4.setEditable(false);
        txtDescriptionOfStep4.setText(Messages.SigComposite_txtDescriptionOfStep4);
        tbtmStep4.setControl(txtDescriptionOfStep4);

        Menu menu4 = new Menu(txtDescriptionOfStep4);
        txtDescriptionOfStep4.setMenu(menu4);

        mntm4 = new MenuItem(menu4, SWT.NONE);
        mntm4.setText(Messages.SigComposite_menu);

        //canvasBtmCenter contains the arrows left and right of the "Signature function" button
        canvasBtmCenter = new Canvas(columnRight, SWT.NONE);
        GridLayout gl_canvasBtmCenter = new GridLayout(3, false);
        gl_canvasBtmCenter.verticalSpacing = 0;
        gl_canvasBtmCenter.horizontalSpacing = 0;
        gl_canvasBtmCenter.marginHeight = 0;
        canvasBtmCenter.setLayout(gl_canvasBtmCenter);
        canvasBtmCenter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        canvasBtmCenter.addPaintListener(new PaintListener() {
        	@Override
        	public void paintControl(PaintEvent e) {
			    GC gc = e.gc;
		        Rectangle area = canvasBtmCenter.getClientArea(); // Get the size of the canvas

		        //Set correct color (light or dark grey, depending on the status) and draw left arrow part
		        int rgbVal = btnHash.getEnabled() && btnSignature.getEnabled()  ? 128 : 192;
		        gc.setBackground(new Color(Display.getCurrent(), rgbVal, rgbVal, rgbVal));
		        int arrowHeight = btnSignature.getBounds().y + btnSignature.getBounds().height / 2;
		        gc.fillRectangle(0, arrowHeight - 10, area.width / 2, 20);	

		        //Set correct color for the right arrow and draw it
		        rgbVal = btnHash.getEnabled() && btnSignature.getEnabled() && btnOpenInEditor.getEnabled() ? 128 : 192;
		        gc.setBackground(new Color(Display.getCurrent(), rgbVal, rgbVal, rgbVal));
		        gc.fillRectangle(area.width / 2, arrowHeight - 10, area.width / 2 - 15, 20);
		        gc.fillPolygon(new int[] {
		        		area.width - 20, arrowHeight - 20, 
		        		area.width, arrowHeight, 
		        		area.width - 20, arrowHeight + 20
			     });

		        gc.dispose();		
        	}
        });
        
        //the canvas between the tabbed text box and the "Signature function" button
        c3 = new Canvas(canvasBtmCenter, SWT.NONE);
        GridData gd_c3 = new GridData(SWT.CENTER, SWT.FILL, true, true, 3, 1);
        c3.setLayoutData(gd_c3);
        c3.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
			    GC gc = e.gc;
		        Rectangle area = c3.getClientArea(); // Get the size of the canvas
		        
		        // Insert the image of the key
		        ImageDescriptor id = SigPlugin.getImageDescriptor("icons/key.png"); //$NON-NLS-1$
		        ImageData imD = id.getImageData(100);
		        Image img = new Image(Display.getCurrent(), imD);
		        gc.drawImage(img, area.width / 2 - img.getBounds().width / 2, 30); //Draw image of the key

		        //Set correct color (light or dark grey, depending on the status) and draw arrow part
		        int rgbVal = btnHash.getEnabled() && btnSignature.getEnabled() && btnOpenInEditor.getEnabled() ? 128 : 192;
		        gc.setBackground(new Color(Display.getCurrent(), rgbVal, rgbVal, rgbVal));
		        gc.fillRectangle(area.width / 2 - 10, img.getBounds().height + 30, 20, area.height);	

		        gc.dispose();			
			}
        });
        
        btnSignature = new Button(canvasBtmCenter, SWT.NONE);
        GridData gd_btnSignature = new GridData(SWT.CENTER, SWT.FILL, true, false, 3, 1);
        gd_btnSignature.widthHint = 150;
        gd_btnSignature.heightHint = 60;
        btnSignature.setLayoutData(gd_btnSignature);
        btnSignature.setEnabled(false);
        btnSignature.setText(Messages.SigComposite_btnSignature);
        
        //compositeBtmRight only contains grpSignedDoc
        Composite compositeBtmRight = new Composite(columnRight, SWT.NONE);
        compositeBtmRight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        GridLayout gl_compositeBtmRight = new GridLayout(2, false);
        gl_compositeBtmRight.verticalSpacing = 0;
        gl_compositeBtmRight.horizontalSpacing = 0;
        gl_compositeBtmRight.marginWidth = 0;
        gl_compositeBtmRight.marginHeight = 0;
        compositeBtmRight.setLayout(gl_compositeBtmRight);
        
        Group grpSignedDoc = new Group(compositeBtmRight, SWT.NONE);
        GridData gd_grpSignedDoc = new GridData(SWT.FILL, SWT.BOTTOM, false, true, 2, 1);
        gd_grpSignedDoc.widthHint = 250;
        grpSignedDoc.setLayoutData(gd_grpSignedDoc);
        grpSignedDoc.setLayout(new GridLayout());
        grpSignedDoc.setText(Messages.SigComposite_grpSignedDoc);
        
        //canvas for the document image in the "Signed document" group
        Canvas c4 = new Canvas(grpSignedDoc, SWT.NONE);
        GridData gd_c4 = new GridData(SWT.CENTER, SWT.FILL, true, false);
        gd_c4.heightHint = 150;
        gd_c4.widthHint = 100;
        c4.setLayoutData(gd_c4);
        c4.addPaintListener(new PaintListener() {
        	@Override
        	public void paintControl(PaintEvent e) {
			    GC gc = e.gc;
		        Rectangle area = c4.getClientArea(); // Get the size of the canvas
		        
		        // Insert the image
		        ImageDescriptor id = SigPlugin.getImageDescriptor("icons/doc.png"); //$NON-NLS-1$
		        ImageData imD = id.getImageData(100);
		        Image img = new Image(Display.getCurrent(), imD);
		        gc.drawImage(img, (area.width - imD.width) / 2, 20);
        	}
        });
        
        btnOpenInEditor = new Button(grpSignedDoc, SWT.NONE);
        GridData gd_btnOpenInEditor = new GridData(SWT.CENTER, SWT.FILL, false, false);
        gd_btnOpenInEditor.widthHint = 230;
        gd_btnOpenInEditor.heightHint = 40;
        btnOpenInEditor.setLayoutData(gd_btnOpenInEditor);
        btnOpenInEditor.setEnabled(false);
        btnOpenInEditor.setText(Messages.SigComposite_btnOpenInEditor);
        
        //bottomRow contains all the labels at the bottom of the view, as well as the reset button
        Composite bottomRow = new Composite(compSignatureGeneration, SWT.NONE);
        bottomRow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        GridLayout gl_bottomRow = new GridLayout(4, false);
        gl_bottomRow.verticalSpacing = 0;
        gl_bottomRow.horizontalSpacing = 0;
        gl_bottomRow.marginHeight = 0;
        bottomRow.setLayout(gl_bottomRow);
        
        lblHashhex = new Label(bottomRow, SWT.NONE);
        GridData gl_lblHashhex = new GridData(SWT.FILL, SWT.TOP, false, false);
        gl_lblHashhex.verticalIndent = 5;
        gl_lblHashhex.widthHint = 175;
        lblHashhex.setLayoutData(gl_lblHashhex);
        lblHashhex.setText(org.jcryptool.visual.sig.ui.view.Messages.SigComposite_1);
        
        lblSignature = new Label(bottomRow, SWT.CENTER);
        GridData gd_lblSignature = new GridData(SWT.CENTER, SWT.FILL, true, false);
        gd_lblSignature.verticalIndent = 5;
        gd_lblSignature.widthHint = 150;
        lblSignature.setLayoutData(gd_lblSignature);
        lblSignature.setText(Messages.SigComposite_lblSignature);
        
        lblProgress = new Label(bottomRow, SWT.NONE);
        GridData gd_lblProgress = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        gd_lblProgress.verticalIndent = 5;
        gd_lblProgress.widthHint = 145;
        lblProgress.setLayoutData(gd_lblProgress);
        lblProgress.setText(String.format(Messages.SigComposite_lblProgress, 1));
      
        btnReset = new Button(bottomRow, SWT.NONE);
        GridData gd_btnReset = new GridData(SWT.RIGHT, SWT.FILL, false, false);
        gd_btnReset.verticalIndent = 5;
        gd_btnReset.widthHint = 110;
        btnReset.setLayoutData(gd_btnReset);
        btnReset.setText(Messages.SigComposite_btnReset);

        btnReturn = new Button(compSignatureGeneration, SWT.NONE);
        btnReturn.setText(Messages.SigComposite_btnReturn);
        btnReturn.setVisible(false); // Invisible by default

        createEvents();

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
     * Adds SelectionListeners to the Controls that need them
     */
    public void createEvents() {
    	
        // Adds a Listener for the document
        btnChooseInput.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {               
                    //Memorize first 1000 bytes of the old message
                	int lengthOldMessage;
                	byte[] oldMessage = null;
                	if (Input.data != null) {
                    	lengthOldMessage = Math.min(Input.data.length, 1000);
                        oldMessage = Arrays.copyOfRange(Input.data, 0, lengthOldMessage);
                	}
                	
                    // Create the HashWizard
                    InputWizard wiz = new InputWizard();
                    // Display it
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz) {
                        @Override
                        protected void configureShell(Shell newShell) {
                            super.configureShell(newShell);
                            newShell.setSize(800, 480);  // set size of the wizard-window (x,y)
                            newShell.setMinimumSize(800, 480);
                        }
                    };
                 
                    if (dialog.open() == Window.OK) {                 	
                    	//Get the first 1000 bytes of the new message
                    	int lengthNewMessage;
                    	byte[] newMessage = null;
                    	if (Input.data != null) {
                        	lengthNewMessage = Math.min(Input.data.length, 1000);
                        	newMessage = Arrays.copyOfRange(Input.data, 0, lengthNewMessage);
                    	}
                    	
                    	// Only act if the user changed the input text (if the first 1000 bytes differ from old input)
                    	if (Input.data == null || !Arrays.equals(oldMessage, newMessage)) { 
                    		
                            // If the user already finished other steps, reset
                            // everything to this step (keep the chosen algorithms)
                            reset(0);
                        	
                            btnHash.setEnabled(true); // Enable to select the hash method
                            tabDescription.setSelection(1); // Activate the second
                                                            // tab of the
                                                            // description
                            c1.redraw();
                            c2.redraw();
                            c3.redraw();
                            
                            if (Input.dataPlain != null) {
                            	if (Input.filename != null) {
                            		c1.setToolTipText(Input.filename + "\n\n" + Messages.SigComposite_FileInput_Tooltip + Input.dataPlain);
                            	} else {
                            		c1.setToolTipText(Input.dataPlain);
                            	}
                            	copy.setEnabled(true);
                            }
                            
                            canvasBtmCenter.redraw();
                            lblProgress.setText(String.format(Messages.SigComposite_lblProgress, 2));
                    	}
                    	//prevent memory leak
                    	newMessage = null;
                    	oldMessage = null;
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
                    // Create the HashWizard
                    HashWizard wiz = new HashWizard();
                    // Display it
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz) {
                        @Override
                        protected void configureShell(Shell newShell) {
                            super.configureShell(newShell);
                            // set size of the wizard-window (x,y)
                        }
                    };
                    if (dialog.open() == Window.OK) {
                		hash = wiz.getHash();
                		reset(1);
                		
                		lblHash.setText(hashes[hash]);

                        // Arguments: Hash method, data to hash
                        Hash.hashInput(hashes[hash], Input.data); // Hash the input

                        // Update the GUI:
                        btnSignature.setEnabled(true); // Enable to select the
                                                       // signature method
                        tabDescription.setSelection(2); // Activate the third
                                                        // tab of the
                                                        // description
                        c1.redraw();
                        c2.redraw();
                        c3.redraw();
                        canvasBtmCenter.redraw();
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
                    SignatureWizard wiz = new SignatureWizard(hash);
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
                		reset(2);       	
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
         
                        if (alias != null && alias.getContactName() != null) {
                        	if (signature == 2) {
                        		c3.setToolTipText(lblSignature.getText());
                        	} else {
                        		 String keyDescription = alias.getContactName() + " - " + alias.getKeyLength() + " bit"; //$NON-NLS-1$ //$NON-NLS-2$
                                 c3.setToolTipText(keyDescription);
                        	}
                        } else {
                        	c3.setToolTipText(""); //$NON-NLS-1$
                        }
           
                        c1.redraw();
                        c2.redraw();
                        c3.redraw();
                        canvasBtmCenter.redraw();
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
                Input.reset();
                System.gc();
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
            c1.setToolTipText(null);
            copy.setEnabled(false);
            lblHash.setText(""); //$NON-NLS-1$
            Input.hash = null;
            Input.hashHex = null;
            Input.h = -1;
        case 1:
            btnSignature.setEnabled(false);
            c3.setToolTipText(null);
            lblSignature.setText(""); //$NON-NLS-1$
            txtHash.setText(""); //$NON-NLS-1$
            Input.signature = null;
            Input.signatureHex = null;
            Input.signatureOct = null;
            Input.privateKey = null;
            Input.publicKey = null;
            Input.key = null;
            Input.s = -1;
        case 2:
        	Input.savePath = null;
            btnOpenInEditor.setEnabled(false);
            txtDescriptionOfStep4.setText(Messages.SigComposite_txtDescriptionOfStep4);
            if (!called) { // If not called by jctca, reset key
                Input.privateKey = null;
            }
            break;
        default:
            break;
        }

        lblProgress.setText(s);
        tabDescription.setSelection(step);
        // redraw canvas (to reset the arrows)
        c1.redraw();
        c2.redraw();
        c3.redraw();
        canvasBtmCenter.redraw();
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

package org.jcryptool.visual.sigVerification.ui.view;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
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
 * This class contains all the code required for the design and functionality of the main view. It creates the
 * components, calls the wizards and constructs the string ("SHA256withECDSA" etc.) used for signing.
 * 
 */
public class SigVerComposite extends Composite  {
    private Text lblGeneralDescription;
    private Text lblHeader;
    private Label lblTitle;
    private Label lblCheckSig;
    private Label lblPubKey;
    private Label lblKeyPic;
    private Label tempVert;
    private Label lblDocPic;
    private Text lblDescriptionStep1;
    private Text lblDescriptionStep2;
    private Text lblDescriptionStep3;
    private Text lblDescriptionStep4;
    private Label label;                // toDo Umbenennen
    private Label lblNewLabel_1;        // toDo Umbennenen
    private Label lblNewLabel_2;        // toDo Umbennenen
    private Label lblNewLabel_3;        // toDo Umbennenen
    private Button btnHash;
    private Button btnAddInput;
    private Button btnReset;
    private Button btnDecrypt;
    private Button btnResult;
    private TabItem tabStep1;
    private TabItem tabStep2;
    private TabItem tabStep3;
    private TabItem tabStep4;
    private Canvas canvas1;
    private TabFolder tabFolder;
    
    private int hash = 0; // Values: 0-4. Hash and signature contain the
    // selected method; default is 0
    private String[] hashes = { org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdomd5,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha1,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha256,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha384,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha512 };
    private int signature = 0; // 0-3
    private String sigstring = ""; //$NON-NLS-1$
    private String[] signatures = { org.jcryptool.visual.sigVerification.ui.wizards.Messages.SignatureWizard_DSA,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.SignatureWizard_RSA,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.SignatureWizard_ECDSA,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.SignatureWizard_RSAandMGF1 };
    private Text textGeneralDescription;
    private Label lblProgress;
    
    
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
        createContents(parent);
        createActions();
        
        // Adds reset button to the Toolbar
        IToolBarManager toolBarMenu = view.getViewSite().getActionBars().getToolBarManager();
        Action action = new Action("Reset", IAction.AS_PUSH_BUTTON) {public void run() {reset(0);}}; //$NON-NLS-1$
        action.setImageDescriptor(SigVerificationPlugin.getImageDescriptor("/icons/reset.gif")); //$NON-NLS-1$
        toolBarMenu.add(action);
        
    }

    /**
     * Create contents of the application window.
     * @param parent
     */
    private void createContents(Composite parent) { //vs Control createContents
        parent.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
        parent.setLayout(null);
        

        
        {
            textGeneralDescription = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
            textGeneralDescription.setBounds(10, 36, 1035, 77);
            textGeneralDescription.setText(Messages.SigVerComposite_description);
            textGeneralDescription.setEditable(false);
            textGeneralDescription.setBackground(SWTResourceManager.getColor(255, 255, 255));
        }
        
                        lblHeader = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
                        lblHeader.setBounds(10, 10, 1035, 35);
                        lblHeader.setEditable(false);
                        lblHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
                        lblHeader.setText(Messages.SigVerComposite_lblHeader);
                        lblHeader.setBackground(SWTResourceManager.getColor(255, 255, 255));
                        
                        lblTitle = new Label(this, SWT.NONE);
                        lblTitle.setLocation(20, 119);
                        lblTitle.setSize(120, 20);
                        lblTitle.setText(Messages.SigVerComposite_lblTitle);
                        {
                            Composite border = new Composite(this, SWT.BORDER);
                            border.setBounds(10, 130, 1035, 575);
                            
//                                    {
//                                        lblDocPic = new Label(border, SWT.ICON);
//                                        lblDocPic.setLocation(69, 136);
//                                        lblDocPic.setSize(150, 165);
//                                        lblDocPic.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/image3013.png"));
//                                    }
                                    
                            canvas1 = new Canvas(border, SWT.NONE | SWT.TRANSPARENT);
                            canvas1.setBounds(10, 10, 1021, 551);
                            
                            btnHash = new Button(canvas1, SWT.NONE);
                            btnHash.addSelectionListener(new SelectionAdapter() {
                                @Override
                                public void widgetSelected(SelectionEvent e) {
                                }
                            });
                            btnHash.setBounds(386, 98, 190, 90);
                            btnHash.setEnabled(false);
                            btnHash.setText(Messages.SigVerComposite_btnHash);
                            {
                                lblCheckSig = new Label(canvas1, SWT.NONE);
                                lblCheckSig.setLocation(802, 46);
                                lblCheckSig.setSize(102, 20);
                                lblCheckSig.setText(Messages.SigVerComposite_btnSignature);
                            }
                            
                            btnAddInput = new Button(canvas1, SWT.NONE);
                            btnAddInput.addSelectionListener(new SelectionAdapter() {
                                @Override
                                public void widgetSelected(SelectionEvent e) {
                                }
                            });
                            btnAddInput.setLocation(38, 54);
                            btnAddInput.setSize(200, 60);
                            btnAddInput.setText(Messages.SigVerComposite_btnAddInput);
                            
                            btnReset = new Button(canvas1, SWT.NONE);
                            btnReset.setLocation(921, 404);
                            btnReset.setSize(90, 30);
                            btnReset.addSelectionListener(new SelectionAdapter() {
                                @Override
                                public void widgetSelected(SelectionEvent e) {
                                }
                            });
                            btnReset.setText(Messages.SigVerComposite_btnReset);
                            
                            btnDecrypt = new Button(canvas1, SWT.NONE);
                            btnDecrypt.setEnabled(false);
                            btnDecrypt.setLocation(386, 240);
                            btnDecrypt.setSize(190, 90);
                            btnDecrypt.addSelectionListener(new SelectionAdapter() {
                                @Override
                                public void widgetSelected(SelectionEvent e) {
                                }
                            });
                            btnDecrypt.setText(Messages.SigVerComposite_btnDecrypt);
                            {
                                lblProgress = new Label(canvas1, SWT.NONE);
                                lblProgress.setBounds(825, 412, 90, 30);
                                lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 1));
                            }
                            {
                                lblPubKey = new Label(border, SWT.NONE);
                                lblPubKey.setLocation(765, 592);
                                lblPubKey.setSize(70, 20);
                                lblPubKey.setText(Messages.SigVerComposite_lblPubKey);
                            }
                            
//                            {
//                                lblKeyPic = new Label(border, SWT.ICON);
//                                lblKeyPic.setLocation(435, 333);
//                                lblKeyPic.setSize(100, 118);
//                                lblKeyPic.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/key.png"));
//                            }
                            
                            tempVert = new Label(border, SWT.NONE);
                            tempVert.setLocation(455, 307);
                            tempVert.setSize(55, 52);
                            tempVert.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
                                            {
                                                label = new Label(border, SWT.NONE);
                                                label.setLocation(168, 78);
                                                label.setSize(229, 130);
                                                label.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/rect2985.png"));
                                            }
                                            {
                                                lblNewLabel_1 = new Label(border, SWT.NONE);
                                                lblNewLabel_1.setLocation(168, 229);
                                                lblNewLabel_1.setSize(229, 130);
                                                lblNewLabel_1.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/rect.png"));
 
                                            }
                                            {
                                                lblNewLabel_2 = new Label(border, SWT.NONE);
                                                lblNewLabel_2.setLocation(396, 98);
                                                lblNewLabel_2.setSize(383, 110);
                                                lblNewLabel_2.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/path.png"));
                                            }
                                                    {
                                                        lblNewLabel_3 = new Label(border, SWT.NONE);
                                                        lblNewLabel_3.setLocation(390, 219);
                                                        lblNewLabel_3.setSize(389, 100);
                                                        lblNewLabel_3.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/path3019.png"));
                                                        
                                                    }
                                            
                                                    {
                                                        Composite littleBorder = new Composite(border, SWT.BORDER);
                                                        littleBorder.setLocation(803, 66);
                                                        littleBorder.setSize(218, 253);
                                                        
                                                        btnResult = new Button(littleBorder, SWT.NONE);
                                                        btnResult.setEnabled(true);					// wieder auf false setzten
                                                        btnResult.setBounds(13, 189, 190, 50);
                                                        btnResult.setText(Messages.SigVerComposite_btnResult);
                                                        {
                                                            lblDocPic = new Label(littleBorder, SWT.ICON);
                                                            lblDocPic.setBounds(35, 10, 163, 159);
                                                            lblDocPic.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/image3013.png"));
                                                        }
                                                    }
                                                    
                                                    tabFolder = new TabFolder(border, SWT.NONE);
                                                    tabFolder.setBounds(10, 430, 1011, 131);
                                                    {
                                                        tabStep1 = new TabItem(tabFolder, SWT.NONE);
                                                        tabStep1.setText(Messages.SigVerComposite_tbtmNewItem_0);
                                                        {
                                                            lblDescriptionStep1 = new Text(tabFolder, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
                                                            lblDescriptionStep1.setBackground(SWTResourceManager.getColor(255, 255, 255));
                                                            lblDescriptionStep1.setEditable(false);
                                                            lblDescriptionStep1.setText(Messages.SigVerComposite_txtDescriptionOfStep1);
                                                            tabStep1.setControl(lblDescriptionStep1);
                                                        }
                                                    }
                                                    
                                                    {
                                                        tabStep2 = new TabItem(tabFolder, SWT.NONE);
                                                        tabStep2.setText(Messages.SigVerComposite_tbtmNewItem_1);
                                                        {
                                                            lblDescriptionStep2 = new Text(tabFolder, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
                                                            lblDescriptionStep2.setBackground(SWTResourceManager.getColor(255, 255, 255));
                                                            lblDescriptionStep2.setEditable(false);
                                                            lblDescriptionStep2.setText(Messages.SigVerComposite_txtDescriptionOfStep2);
                                                            tabStep1.setControl(lblDescriptionStep2);
                                                        }
                                                    }
                                                    {
                                                        tabStep3 = new TabItem(tabFolder, SWT.NONE);
                                                        tabStep3.setText(Messages.SigVerComposite_tbtmNewItem_2);
                                                        {
                                                            lblDescriptionStep3 = new Text(tabFolder, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
                                                            lblDescriptionStep3.setBackground(SWTResourceManager.getColor(255, 255, 255));
                                                            lblDescriptionStep3.setEditable(false);
                                                            lblDescriptionStep3.setText(Messages.SigVerComposite_txtDescriptionOfStep3);
                                                            tabStep1.setControl(lblDescriptionStep3);
                                                        }
                                                    }
                                                    {
                                                        tabStep4 = new TabItem(tabFolder, SWT.NONE);
                                                        tabStep4.setText(Messages.SigVerComposite_tbtmNewItem_3);
                                                        {
                                                            lblDescriptionStep4 = new Text(tabFolder, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
                                                            lblDescriptionStep4.setBackground(SWTResourceManager.getColor(255, 255, 255));
                                                            lblDescriptionStep4.setEditable(false);
                                                            lblDescriptionStep4.setText(Messages.SigVerComposite_txtDescriptionOfStep4);
                                                            tabStep1.setControl(lblDescriptionStep4);
                                                        }
                                                    }
                                                    
                        }         
                        canvas1.addPaintListener(new PaintListener() {
                            public void paintControl (PaintEvent e) {
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
                                ImageDescriptor id = SigVerificationPlugin.getImageDescriptor("icons/key.png"); //$NON-NLS-1$
                                ImageData imD = id.getImageData();
                                Image img = new Image(Display.getCurrent(), imD);
                                gc.drawImage(img, 435, 333);

                                // Insert the image of the document
                                id = SigVerificationPlugin.getImageDescriptor("icons/image3013.png"); //$NON-NLS-1$
                                imD = id.getImageData();
                                img = new Image(Display.getCurrent(), imD);
                                // Draw second document icon
                                gc.drawImage(img, 69, 136);
                                gc.drawImage(img, 163, 159);

//                                gc.setBackground(lightgrey);
//                                // Color the all the areas in lightgrey
//                                // Draw shaft
//                                gc.fillRectangle(55, 60, 20, height);
//                                gc.fillRectangle(0, height - 30, width - 220, 20);
//                                gc.fillRectangle(270, 300, 20, 80);
//                                gc.fillPolygon(new int[] { width - 220, height - 40, width - 220, height, width - 200, height - 20 });
//                                gc.setBackground(darkgrey);
//                                gc.drawImage(img, picx, picy);
//                                // Color the specified areas in darkgrey
//                                if (btnHash.getEnabled() == false) {
//                                    // color nothing
//                                } else if (btnDecrypt.getEnabled() == false) { // Step 2
//                                    // draw the first part of the arrow (from Document to btnHash)
//                                    gc.fillRectangle(55, 60, 20, height / 2 - 30);
//                                    gc.drawImage(img, picx, picy);
//                                } else if (btnResult.getEnabled() == false) { // Step 3
//                                    // draw another part of the arrow (from btnHash to btnSignature)
//                                    gc.fillRectangle(55, 60, 20, height);
//                                    gc.fillRectangle(0, height - 30, width / 2, 20);
//                                    gc.drawImage(img, picx, picy);
//                                } else { // Step 4
//                                    gc.fillRectangle(55, 60, 20, height);
//                                    gc.fillRectangle(0, height - 30, width - 220, 20);
//                                    gc.fillRectangle(270, 300, 20, 80);
//                                    gc.fillPolygon(new int[] { width - 220, height - 40, width - 220, height, width - 200, height - 20 });
//                                    gc.drawImage(img, picx, picy);
//                                }

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
                        btnResult.setEnabled(true);     // Activate the second
                                                        // tab of the
                                                        // description       
                        tabFolder.setSelection(1);
                        lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 2));
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
                       
                        // Update the GUI:
                        btnDecrypt.setEnabled(true); // Enable to select the
                                                       // signature method
                                                        
                        tabFolder.setSelection(2);      // Activate the third
                                                        // tab of the
                                                        // description 
                        lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 3));
                    }
                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }
            }
        });
        
     // Adds a Listener for the Signature select button
        btnDecrypt.addSelectionListener(new SelectionAdapter() {
            @SuppressWarnings("deprecation")
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
                        //KeyStoreAlias alias = wiz.getAlias();
                        Input.dataString = new String(Input.data);
//                        Input.data = Input.removeLineBreaks(Input.data);
                        
                        
                        //Set method and size of signature (ex. RSA, 1024)                        
                        Input.setSignaturemethod();
                        Input.setSignatureSize();
                        
                        // Divides signature and plaintext from imported file.
                        Input.divideSignaturePlaintext();
                        
                        // Arguments: Hash method, data to hash
                        Hash.hashInput(hashes[hash], Input.plain); // Hash the input  
                        System.out.println(new String(Input.data, 0));
                        System.out.println(new String(Input.plain, 0));
                        System.out.println(new String(Input.hash, 0));
                        Input.hashHex = Input.bytesToHex(Input.hash);
                        System.out.println(Input.hashHex);
                        System.out.println(new String(Input.signature, 0));
                        System.out.println(Input.signaturemethod);
                        System.out.println(Input.signatureSize);
                        
                        btnResult.setEnabled(true);
                        tabFolder.setSelection(3);
                        lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 4));
                        
                    }
                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }
            	
            	
            	// Input Key Wizard
            	try {
                    // If the user already finished other steps, reset
                    // everything to this step (keep the chosen algorithms)
                    reset(2);

                    // Create the InputKeyWizard
                    InputKeyWizard wiz = new InputKeyWizard();
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
                        
                    }
                    System.out.println(Input.hashNew);
                	// Creates the signature for the calculated hash.
                    // Arguments: Signature methods, data to sign, Key
                    SigVerification.verifySignature(Input.signaturemethod);                        
                    
                    btnResult.setEnabled(true);
                    // Compares the two hashes.
                    System.out.println(Input.result);
                    
                    Input.hashHex = Input.bytesToHex(Input.hash);
                    if (Input.hashNew != null){
                    	Input.hashNewHex = Input.bytesToHex(Input.hashNew);
                    }
                    
                    // Shows green check mark or red fail sign if compairism is correct or false
                    //if(Input.result){
                        //show green check mark
                    //}else{
                        //show red fail sign
                    //}

                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }
            	
            }
        });
        
        btnResult.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    // If the user already finished other steps, reset
                    // everything to this step (keep the chosen algorithms)
                    reset(3);

                    // Show the result
                    Display display = Display.getCurrent();
                    Shell shell = new SignaturResult(display, Input.signaturemethod);
                    shell.open();
                    // Display it
                 // run the event loop as long as the window is open
                    while (!shell.isDisposed()) {
                        // read the next OS event queue and transfer it to a SWT event 
                      if (!display.readAndDispatch())
                       {
                      // if there are currently no other OS event to process
                      // sleep until the next OS event is available 
                        display.sleep();
                       }
                    }

                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }
            }
        });
        
     // Adds a Listener for the reset button
        /*btnReset.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
                try {
                    Input.privateKey = null;
                    Input.publicKey = null;
                    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    IViewReference ref = page.findViewReference("org.jcryptool.visual.sigVerification.view"); //$NON-NLS-1$
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(ref);
                    page.closePerspective(null, false, true);
                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }
            }
        });*/       
    }
   
    
    private void reset(int step) {
        // If the user already finished other steps, reset everything to this
        // step (keep the chosen algorithms)
        switch (step) {
        case 0:
            btnHash.setEnabled(false);
        case 1:
            btnDecrypt.setEnabled(false);
        case 2:
            btnResult.setEnabled(false);
            break;
        default:
            break;
        }
    }

    /**
     * Helper method to get the correct signature method with the correct hash method. (Not every signature method
     * matches with every hash method).
     * 
     * @return The string that can be used for signing
     */
    /*private String chooseSignature() {
        sigstring = ""; //$NON-NLS-1$

        // Temporary solution
        if (signatures[signature].contains("RSA")) { //$NON-NLS-1$         
        	sigstring = "RSA"; //$NON-NLS-1$
        } else if (signatures[signature].contains("ECDSA")) { //$NON-NLS-1$
            sigstring =  "ECDSA"; //$NON-NLS-1$
        } else if (signatures[signature].contains("DSA")) { //$NON-NLS-1$
                sigstring = "DSA"; //$NON-NLS-1$
        }

        return sigstring;
    }*/

}

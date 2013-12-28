package org.jcryptool.visual.sigVerification.ui.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
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
import org.jcryptool.visual.sigVerification.ui.wizards.SignatureWizard;

/**
 * This class contains all the code required for the design and functionality of the main view. It creates the
 * components, calls the wizards and constructs the string ("SHA256withECDSA" etc.) used for signing.
 * 
 */
public class SigVerComposite extends Composite  {
    private Label lblGeneralDescription;
    private Label lblHeader;
    private Label lblTitle;
    private Label lblCheckSig;
    private Label lblPubKey;
    private Label lblKeyPic;
    private Label tempVert;
    private Label lblDocPic;
    private Label lblDescriptionStep1;
    private Label lblDescriptionStep2;
    private Label lblDescriptionStep3;
    private Label lblDescriptionStep4;
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
                            lblGeneralDescription = new Label(this, SWT.NONE);
                            lblGeneralDescription.setLocation(10, 36);
                            lblGeneralDescription.setSize(1035, 77);
                            lblGeneralDescription.setBackground(SWTResourceManager.getColor(255, 255, 255));
                            lblGeneralDescription.setText(Messages.SigVerComposite_description);
                           
                        }
                        lblHeader = new Label(this, SWT.NONE);
                        lblHeader.setLocation(10, 10);
                        lblHeader.setSize(1035, 35);
                        lblHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
                        lblHeader.setText(Messages.SigVerComposite_lblHeader);
                        lblHeader.setBackground(SWTResourceManager.getColor(255, 255, 255));
                        
                        lblTitle = new Label(this, SWT.NONE);
                        lblTitle.setLocation(20, 119);
                        lblTitle.setSize(137, 20);
                        lblTitle.setText(Messages.SigVerComposite_lblTitle);
                        {
                            Composite border = new Composite(this, SWT.BORDER);
                            border.setBounds(10, 130, 1035, 575);
                            
                                    {
                                        lblDocPic = new Label(border, SWT.ICON);
                                        lblDocPic.setLocation(69, 136);
                                        lblDocPic.setSize(150, 165);
                                        lblDocPic.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/image3013.png"));
                                    }
                            
                            btnHash = new Button(border, SWT.NONE);
                            btnHash.addSelectionListener(new SelectionAdapter() {
                                @Override
                                public void widgetSelected(SelectionEvent e) {
                                }
                            });
                            btnHash.setBounds(386, 98, 190, 90);
                            btnHash.setEnabled(false);
                            btnHash.setText(Messages.SigVerComposite_btnHash);
                            {
                                lblCheckSig = new Label(border, SWT.NONE);
                                lblCheckSig.setLocation(812, 55);
                                lblCheckSig.setSize(109, 20);
                                lblCheckSig.setText(Messages.SigVerComposite_btnSignature);
                            }
                            
                            btnAddInput = new Button(border, SWT.NONE);
                            btnAddInput.addSelectionListener(new SelectionAdapter() {
                                @Override
                                public void widgetSelected(SelectionEvent e) {
                                }
                            });
                            btnAddInput.setLocation(38, 54);
                            btnAddInput.setSize(200, 60);
                            btnAddInput.setText(Messages.SigVerComposite_btnAddInput);
                            
                            btnReset = new Button(border, SWT.NONE);
                            btnReset.setLocation(931, 412);
                            btnReset.setSize(90, 30);
                            btnReset.addSelectionListener(new SelectionAdapter() {
                                @Override
                                public void widgetSelected(SelectionEvent e) {
                                }
                            });
                            btnReset.setText(Messages.SigVerComposite_btnReset);
                            
                            btnDecrypt = new Button(border, SWT.NONE);
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
                                lblPubKey = new Label(border, SWT.NONE);
                                lblPubKey.setLocation(765, 592);
                                lblPubKey.setSize(70, 20);
                                lblPubKey.setText(Messages.SigVerComposite_lblPubKey);
                            }
                            
                            {
                                lblKeyPic = new Label(border, SWT.ICON);
                                lblKeyPic.setLocation(435, 333);
                                lblKeyPic.setSize(100, 118);
                                lblKeyPic.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/key.png"));
                            }
                            
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
                                                    
                                                    TabFolder tabFolder = new TabFolder(border, SWT.NONE);
                                                    tabFolder.setBounds(10, 430, 1011, 131);
                                                    {
                                                        tabStep1 = new TabItem(tabFolder, SWT.NONE);
                                                        tabStep1.setText(Messages.SigVerComposite_tbtmNewItem_0);
                                                        {
                                                            lblDescriptionStep1 = new Label(tabFolder, SWT.NONE);
                                                            tabStep1.setControl(lblDescriptionStep1);
                                                            lblDescriptionStep1.setText(Messages.SigVerComposite_txtDescriptionOfStep1);
                                                        }
                                                    }
                                                    {
                                                        tabStep2 = new TabItem(tabFolder, SWT.NONE);
                                                        tabStep2.setText(Messages.SigVerComposite_tbtmNewItem_1);
                                                        {
                                                            lblDescriptionStep2 = new Label(tabFolder, SWT.NONE);
                                                            tabStep2.setControl(lblDescriptionStep2);
                                                            lblDescriptionStep2.setText(Messages.SigVerComposite_txtDescriptionOfStep2);
                                                        }
                                                    }
                                                    {
                                                        tabStep3 = new TabItem(tabFolder, SWT.NONE);
                                                        tabStep3.setText(Messages.SigVerComposite_tbtmNewItem_2);
                                                        {
                                                            lblDescriptionStep3 = new Label(tabFolder, SWT.NONE);
                                                            tabStep3.setControl(lblDescriptionStep3);
                                                            lblDescriptionStep3.setText(Messages.SigVerComposite_txtDescriptionOfStep3);
                                                        }
                                                    }
                                                    {
                                                        tabStep4 = new TabItem(tabFolder, SWT.NONE);
                                                        tabStep4.setText(Messages.SigVerComposite_tbtmNewItem_3);
                                                        {
                                                            lblDescriptionStep4 = new Label(tabFolder, SWT.NONE);
                                                            tabStep4.setControl(lblDescriptionStep4);
                                                            lblDescriptionStep4.setText(Messages.SigVerComposite_txtDescriptionOfStep4);
                                                        }
                                                    }
                        }         

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
                                                        
                                                        // Activate the third
                                                        // tab of the
                                                        // description                     
                    }
                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }
            }
        });
        
     // Adds a Listener for the Signature select button
        btnDecrypt.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	try {
                    // If the user already finished other steps, reset
                    // everything to this step (keep the chosen algorithms)
                    reset(2);

                    // Create the HashWirard
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

                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }
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
                        
                        // Divides signature and plaintext from imported file.
                        Input.divideSignaturePlaintext();
                        
                        // Arguments: Hash method, data to hash
                        Hash.hashInput(hashes[hash], Input.plain); // Hash the input

                        // Creates the signature for the calculated hash.
                        // Arguments: Signature methods, data to sign, Key
                        SigVerification.verifyInput(Input.signaturemethod, Input.signature, Input.pubKey);

                        // Compares the two hashes.
                        //Input.compareHashes();
                        
                        // Shows green check mark or red fail sign if compairism is correct or false
                        if(Input.result){
                            //show green check mark
                        }else{
                            //show red fail sign
                        }                     
                        
                        
                        btnResult.setEnabled(true);
                        // Activate the second tab of the description

                    }
                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }
            }
        });
        
        btnResult.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                /*try {
                    // If the user already finished other steps, reset
                    // everything to this step (keep the chosen algorithms)
                    reset(3);

                    // Create the HashWirard
                    SignaturResult wiz = new SignaturResult();
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
                    }

                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }*/
            }
        });
        
     // Adds a Listener for the reset button
        btnReset.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                reset(0);
            }
        });
        
        // Adds a Listener for Return Button
/*        btnReturn.addSelectionListener(new SelectionAdapter() {
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
        });
*/        
    }

    /**
     * Create the menu manager.
     * @return the menu manager
     */
/*    @Override
    protected MenuManager createMenuManager() {
        MenuManager menuManager = new MenuManager("menu");
        return menuManager;
    }
*/
   
    /**
     * Create the status line manager.
     * @return the status line manager
     */
/*    @Override
    protected StatusLineManager createStatusLineManager() {
        StatusLineManager statusLineManager = new StatusLineManager();
        return statusLineManager;
    }
*/
    /**
     * Launch the application.
     * @param args
     */
/*    public static void main(String args[]) {
        try {
            SigGUI window = new SigGUI();
            window.setBlockOnOpen(true);
            window.open();
            Display.getCurrent().dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    /**
     * Configure the shell.
     * @param newShell
     */
/*    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("New Application");
    }
*/
    /**
     * Return the initial size of the window.
     */
/*    @Override
    protected Point getInitialSize() {
        return new Point(1680, 1050);
    }
*/    
    
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
    private String chooseSignature() {
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
    }

}

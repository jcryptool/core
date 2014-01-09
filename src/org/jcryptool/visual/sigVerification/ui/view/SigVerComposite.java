//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
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
import org.eclipse.swt.widgets.Menu;
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
    private Text lblHeader;
    private Label lblTitle;
    private Label lblCheckSig;
    private Label lblPubKey;
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
    
    private int hash = 0; // Values: 0-4. Hash and signature contain the
    // selected method; default is 0
    private String[] hashes = { org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdomd5,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha1,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha256,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha384,
            org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha512 };
    private int signature = 0; // 0-3

    //Erzeugen der benötigten Objekte
    Input input = new Input();
    SigVerification sigVerification = new SigVerification();
    Hash hashInst = new Hash();
    private int step = 0;		// Fortschritt für Reset
    
    
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
			textGeneralDescription = new Text(this, SWT.READ_ONLY | SWT.MULTI
					| SWT.WRAP);
			textGeneralDescription.setBounds(10, 36, 1035, 77);
			textGeneralDescription
					.setText(Messages.SigVerComposite_description);
			textGeneralDescription.setEditable(false);
			textGeneralDescription.setBackground(SWTResourceManager.getColor(
					255, 255, 255));
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

			canvas1 = new Canvas(border, SWT.NONE | SWT.TRANSPARENT);
			canvas1.setBounds(10, 10, 1021, 551);

			btnHash = new Button(canvas1, SWT.NONE);
			btnHash.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
				}
			});
			btnHash.setBounds(386, 106, 190, 90);
			btnHash.setEnabled(false);
			btnHash.setText(Messages.SigVerComposite_btnHash);
			{
				lblCheckSig = new Label(canvas1, SWT.NONE);
				lblCheckSig.setLocation(794, 70);
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
			btnDecrypt.setLocation(386, 214);
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
				lblProgress.setText(String.format(
						Messages.SigVerComposite_lblProgress, 1));
			}

			{
				Composite littleBorder = new Composite(border, SWT.BORDER);
				littleBorder.setBounds(793, 90, 218, 253);

				btnResult = new Button(littleBorder, SWT.NONE);
				btnResult.setEnabled(false);
				btnResult.setBounds(13, 189, 190, 50);
				btnResult.setText(Messages.SigVerComposite_btnResult);
			}
			{
				lblPubKey = new Label(border, SWT.NONE);
				lblPubKey.setLocation(765, 592);
				lblPubKey.setSize(70, 20);
				lblPubKey.setText(Messages.SigVerComposite_lblPubKey);
			}

			tabFolder = new TabFolder(border, SWT.NONE);
			tabFolder.setBounds(10, 430, 1011, 131);
			{
				TabItem tabStep1 = new TabItem(tabFolder, SWT.NONE);
				tabStep1.setText(Messages.SigVerComposite_tbtmNewItem_0);
				{
					lblDescriptionStep1 = new Text(tabFolder, SWT.MULTI
							| SWT.WRAP | SWT.READ_ONLY);
					lblDescriptionStep1.setBackground(SWTResourceManager
							.getColor(255, 255, 255));
					lblDescriptionStep1.setEditable(false);
					lblDescriptionStep1
							.setText(Messages.SigVerComposite_txtDescriptionOfStep1);
					tabStep1.setControl(lblDescriptionStep1);
				}
			}

			{
				TabItem tabStep2 = new TabItem(tabFolder, SWT.NONE);
				tabStep2.setText(Messages.SigVerComposite_tbtmNewItem_1);
				{
					lblDescriptionStep2 = new Text(tabFolder, SWT.MULTI
							| SWT.WRAP | SWT.READ_ONLY);
					lblDescriptionStep2.setBackground(SWTResourceManager
							.getColor(255, 255, 255));
					lblDescriptionStep2.setEditable(false);
					lblDescriptionStep2
							.setText(Messages.SigVerComposite_txtDescriptionOfStep2);
					tabStep2.setControl(lblDescriptionStep2);
				}
			}
			{
				TabItem tabStep3 = new TabItem(tabFolder, SWT.NONE);
				tabStep3.setText(Messages.SigVerComposite_tbtmNewItem_2);
				{
					lblDescriptionStep3 = new Text(tabFolder, SWT.MULTI
							| SWT.WRAP | SWT.READ_ONLY);
					lblDescriptionStep3.setBackground(SWTResourceManager
							.getColor(255, 255, 255));
					lblDescriptionStep3.setEditable(false);
					lblDescriptionStep3
							.setText(Messages.SigVerComposite_txtDescriptionOfStep3);
					tabStep3.setControl(lblDescriptionStep3);
				}
			}
			{
				TabItem tabStep4 = new TabItem(tabFolder, SWT.NONE);
				tabStep4.setText(Messages.SigVerComposite_tbtmNewItem_3);
				{
					lblDescriptionStep4 = new Text(tabFolder, SWT.MULTI
							| SWT.WRAP | SWT.READ_ONLY);
					lblDescriptionStep4.setBackground(SWTResourceManager
							.getColor(255, 255, 255));
					lblDescriptionStep4.setEditable(false);
					lblDescriptionStep4
							.setText(Messages.SigVerComposite_txtDescriptionOfStep4);
					tabStep4.setControl(lblDescriptionStep4);
				}
			}

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
				ImageDescriptor id = SigVerificationPlugin
						.getImageDescriptor("icons/key.png"); //$NON-NLS-1$
				ImageData imD = id.getImageData();
				Image img = new Image(Display.getCurrent(), imD);
				gc.drawImage(img, 435, 333);

				// Insert the image of the document
				id = SigVerificationPlugin
						.getImageDescriptor("icons/image3013.png"); //$NON-NLS-1$
				imD = id.getImageData();
				img = new Image(Display.getCurrent(), imD);
				gc.drawImage(img, 69, 136);
				gc.drawImage(img, 820, 100);

				gc.setBackground(lightgrey);
				// Color the all the areas in lightgrey
				// Draw shaft
				gc.fillRectangle(220, height - 315, width - 480, 30);
				gc.fillPolygon(new int[] { width - 265, height - 270,
						width - 265, height - 330, width - 240, height - 300 });
				gc.fillRectangle(220, height - 405, width - 480, 30);
				gc.fillPolygon(new int[] { width - 265, height - 360,
						width - 265, height - 420, width - 240, height - 390 });
				gc.fillRectangle(465, 300, 35, 30);
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
                        btnHash.setEnabled(true); // Enable to select the hash method
                        btnResult.setEnabled(true);     // Activate the second
                                                        // tab of the
                                                        // description       
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
                       
                        // Update the GUI:
                        btnDecrypt.setEnabled(true); // Enable to select the
                                                       // signature method
                                                        
                        tabFolder.setSelection(2);      // Activate the third
                                                        // tab of the
                                                        // description 
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
            @SuppressWarnings("deprecation")
			public void widgetSelected(SelectionEvent e) {
            	try {
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
                        // get signature method (integer)
                        signature = wiz.getSignature();
                        //KeyStoreAlias alias = wiz.getAlias();
                        
                        //Set method and size of signature (ex. RSA, 1024)                        
                        input.setSignaturemethod();
                        input.setSignatureSize();
                        
                        // Divides signature and plaintext from imported file.
                        input.divideSignaturePlaintext();
                        
                        // Arguments: Hash method, data to hash
                        hashInst.hashInput(hashes[hash], input.plain); // Hash the input  
                        System.out.println(new String(input.data, 0));
                        System.out.println(new String(input.plain, 0));
                        System.out.println(new String(hashInst.getHash(), 0));
                        hashInst.setHashHex();
                        System.out.println(hashInst.hashHex);
                        System.out.println(new String(input.signature, 0));
                        System.out.println(input.signaturemethod);
                        System.out.println(input.signatureSize);
                        
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
                        // step = 3;
                    }
                    //System.out.println(sigVerification.hashNew.getHash());
                	// Creates the signature for the calculated hash.
                    // Arguments: Signature methods, data to sign, Key
                    //sigVerification.verifySignature(input, hashInst);                        
                    
                    btnResult.setEnabled(true);
                    // Compares the two hashes.
                    //System.out.println(sigVerification.getResult());
                    
//                    hashInst.setHashHex();
//                    if (sigVerification.hashNew.hash != null){
//                    	sigVerification.hashNew.setHashHex();
//                    }
                    
                    // Shows green check mark or red fail sign if comparison is correct or false
//                    if(input.result){
//                        GC gc;
//                        
//                         ImageDescriptor id1 = SigVerificationPlugin.getImageDescriptor("icons/gruenerHacken.png"); //$NON-NLS-1$
//                         ImageData imD1 = id1.getImageData();
//                         Image img1 = new Image(Display.getCurrent(), imD1);
//                         gc.drawImage(img1, 820, 225);
//                         gc.dispose();
//                    }else{
//                          GC gc;
//                  
//                          ImageDescriptor id1 = SigVerificationPlugin.getImageDescriptor("icons/rotesKreuz.png"); //$NON-NLS-1$
//                          ImageData imD1 = id1.getImageData();
//                          Image img1 = new Image(Display.getCurrent(), imD1);
//                          gc.drawImage(img1, 820, 225);
//                          gc.dispose();
//                    }

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
                    Shell shell = new SignaturResult(display, input.signaturemethod, input, hashInst, sigVerification);
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
        btnReset.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
                if (step > 0 ){
                	step= step-1;
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
            tabFolder.setSelection(0);
            lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 1));
            break;
        case 1:
            btnDecrypt.setEnabled(false);
            hashInst = null;
            tabFolder.setSelection(1);
            lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 2));
            break;
        case 2:
            btnResult.setEnabled(false);
            sigVerification = null;
            tabFolder.setSelection(2);
            lblProgress.setText(String.format(Messages.SigVerComposite_lblProgress, 3));
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

//This class contains all the code required for the GUI

package org.jcryptool.visual.sig.ui.view;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.sig.SigPlugin;
import org.jcryptool.visual.sig.Messages;
import org.jcryptool.visual.sig.ui.wizards.HashWizard;
import org.jcryptool.visual.sig.ui.wizards.InputWizard;
import org.jcryptool.visual.sig.ui.wizards.SignatureWizard;
import org.eclipse.jface.resource.ImageDescriptor;

public class SigComposite extends Composite implements PaintListener {//,ActionListener{
	private Text txtHash;
	private Text txtGeneralDescription;
	private Text txtSignature;
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
	SigComposite sc = this;
	//hash and signature contain the selected method; default is 0
	private int hash = 0; //0-4
	private String[] hashes = {org.jcryptool.visual.sig.ui.wizards.Messages.HashWizard_rdomd5, 
			org.jcryptool.visual.sig.ui.wizards.Messages.HashWizard_rdosha1,
			org.jcryptool.visual.sig.ui.wizards.Messages.HashWizard_rdosha256,
			org.jcryptool.visual.sig.ui.wizards.Messages.HashWizard_rdosha384,
			org.jcryptool.visual.sig.ui.wizards.Messages.HashWizard_rdosha512};
	private int signature = 0; //0-4
	private String[] signatures = {org.jcryptool.visual.sig.ui.wizards.Messages.SignatureWizard_rdomd5, 
			org.jcryptool.visual.sig.ui.wizards.Messages.SignatureWizard_rdosha1,
			org.jcryptool.visual.sig.ui.wizards.Messages.SignatureWizard_rdosha256,
			org.jcryptool.visual.sig.ui.wizards.Messages.SignatureWizard_rdosha384,
			org.jcryptool.visual.sig.ui.wizards.Messages.SignatureWizard_rdosha512};

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

	//Generates all Elements of the GUI
	public SigComposite(Composite parent, int style, SigView view) {
		super(parent, style);
		sc = this;
		//The color of the textboxes
		Color grey = new Color(Display.getCurrent(), 220, 220, 220);
		Color white = new Color(Display.getCurrent(),255,255,255);
		
		Label lblHeader = new Label(this, SWT.NONE);
		lblHeader.setBounds(10, 10, 699, 21);
		lblHeader.setText(Messages.SigComposite_lblHeader);
		lblHeader.setBackground(white);
		FontData fontData = lblHeader.getFont().getFontData()[0];
		Font font = new Font(this.getDisplay(), new FontData(fontData.getName(), 12, SWT.BOLD));
	    lblHeader.setFont(font);
				
		txtGeneralDescription = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
		txtGeneralDescription.setEditable(false);
		txtGeneralDescription.setBounds(10, 15, 699, 59);
		txtGeneralDescription.setText(Messages.SigComposite_description);
		txtGeneralDescription.setBackground(white);
		
		Group grpSignatureGeneration = new Group(this, SWT.NONE);
		grpSignatureGeneration.setText(Messages.SigComposite_grpSignatureGeneration); 
		grpSignatureGeneration.setBounds(10, 75, 699, 538);
		
		lblHash = new Label(grpSignatureGeneration, SWT.NONE);
		lblHash.setBounds(34, 246, 136, 14);
		lblHash.setText(Messages.SigComposite_lblHash); 
		//btnOpenDocumentTemp.setVisible(false);
		
		btnHash = new Button(grpSignatureGeneration, SWT.NONE);
		btnHash.setEnabled(false);
		btnHash.setBounds(34, 184, 136, 60);
		btnHash.setText(Messages.SigComposite_btnHash);
		
		txtHash = new Text(grpSignatureGeneration, SWT.BORDER);
		txtHash.setBounds(34, 365, 136, 56);
		txtHash.setEditable(false);
		txtHash.setText("<Hash>");
		
		btnSignature = new Button(grpSignatureGeneration, SWT.NONE);
		btnSignature.setEnabled(false);
		btnSignature.setBounds(248, 365, 136, 60);
		btnSignature.setText(Messages.SigComposite_btnSignature);
		
		tabDescription = new TabFolder(grpSignatureGeneration, SWT.NONE);
		tabDescription.setBounds(187, 10, 488, 191);
		
		TabItem tbtmStep1 = new TabItem(tabDescription, SWT.NONE);
		tbtmStep1.setText(Messages.SigComposite_tbtmNewItem_0); 
		
		txtDescriptionOfStep1 = new Text(tabDescription, SWT.MULTI);
		txtDescriptionOfStep1.setBackground(grey);
		txtDescriptionOfStep1.setEditable(false);
		txtDescriptionOfStep1.setText(Messages.SigComposite_txtDescriptionOfStep1);
		tbtmStep1.setControl(txtDescriptionOfStep1);
		
		TabItem tbtmStep2 = new TabItem(tabDescription, SWT.NONE);
		tbtmStep2.setText(Messages.SigComposite_tbtmNewItem_1); 
		
		txtDescriptionOfStep2 = new Text(tabDescription, SWT.NONE);
		txtDescriptionOfStep2.setBackground(grey);
		txtDescriptionOfStep2.setEditable(false);
		txtDescriptionOfStep2.setText(Messages.SigComposite_txtDescriptionOfStep2);
		tbtmStep2.setControl(txtDescriptionOfStep2);
		
		TabItem tbtmStep3 = new TabItem(tabDescription, SWT.NONE);
		tbtmStep3.setText(Messages.SigComposite_tbtmNewItem_2); 
		
		txtDescriptionOfStep3 = new Text(tabDescription, SWT.NONE);
		txtDescriptionOfStep3.setBackground(grey);
		txtDescriptionOfStep3.setEditable(false);
		txtDescriptionOfStep3.setText(Messages.SigComposite_txtDescriptionOfStep3);
		tbtmStep3.setControl(txtDescriptionOfStep3);
		
		TabItem tbtmStep4 = new TabItem(tabDescription, SWT.NONE);
		tbtmStep4.setText(Messages.SigComposite_tbtmNewItem_3);
		
		txtDescriptionOfStep4 = new Text(tabDescription, SWT.NONE);
		txtDescriptionOfStep4.setBackground(grey);
		txtDescriptionOfStep4.setEditable(false);
		txtDescriptionOfStep4.setText(Messages.SigComposite_txtDescriptionOfStep4);
		tbtmStep4.setControl(txtDescriptionOfStep4);
		
		btnReset = new Button(grpSignatureGeneration, SWT.NONE);
		btnReset.setBounds(581, 485, 94, 26);
		btnReset.setText(Messages.SigComposite_btnReset);
		
		lblProgress = new Label(grpSignatureGeneration, SWT.NONE);
		lblProgress.setBounds(490, 491, 83, 14);
		lblProgress.setText(String.format(Messages.SigComposite_lblProgress,1));  
		
		lblSignature = new Label(grpSignatureGeneration, SWT.NONE);
		lblSignature.setText(Messages.SigComposite_lblSignature);
		lblSignature.setBounds(248, 431, 136, 14);
		
		canvas1 = new Canvas(grpSignatureGeneration, SWT.NONE);
		canvas1.setBounds(34, 21, 628, 392);
		
		btnChooseInput = new Button(canvas1, SWT.NONE);
		btnChooseInput.setBounds(0, 0, 136, 41);
		btnChooseInput.setText(Messages.SigComposite_btnChooseInput);
		
		Group grpSignedDoc = new Group(grpSignatureGeneration, SWT.NONE);
		grpSignedDoc.setBounds(463, 207, 212, 258);
		grpSignedDoc.setText(Messages.SigComposite_grpSignedDoc); 
		
		txtSignature = new Text(grpSignedDoc, SWT.BORDER);
		txtSignature.setText(""); 
		txtSignature.setBounds(10, 143, 188, 56);
		
		btnOpenInEditor = new Button(grpSignedDoc, SWT.NONE);
		btnOpenInEditor.setEnabled(false);
		btnOpenInEditor.setBounds(10, 205, 109, 26);
		btnOpenInEditor.setText(Messages.SigComposite_btnOpenInEditor);

		createEvents();
		
		//Adding the PantListener to all the canvas so the arrows can be drawn
		canvas1.addPaintListener(this);
		//canvas2.addPaintListener(this);
 
	}

	/**
	 * This method paints the arrows used to indicate the steps. They are painted in light grey and are later changed
	 * to a darker grey (stepFinished()).
	 * 
	 * @param e 
	 */
	public void paintControl(PaintEvent e) {
		//Set the used colors
		Color lightgrey = new Color(Display.getCurrent(), 192, 192, 192);
	    Color darkgrey = new Color(Display.getCurrent(), 128, 128, 128);
	    Rectangle clientArea;
	    int width;
	    int height;
	    int picx = 30;
	    int picy = 45;
	    GC gc;
	    
		gc = e.gc;
        //Get the size of the canvas area
        clientArea = canvas1.getClientArea(); 
        width = clientArea.width; 
        height = clientArea.height; 
        
        //Insert the image of the key
        ImageDescriptor id = SigPlugin.getImageDescriptor("icons/key.png"); 
        ImageData imD = id.getImageData();
        Image img = new Image(Display.getCurrent(), imD);
        gc.drawImage(img, 230, 200);
        
        //Insert the image of the document
        id = SigPlugin.getImageDescriptor("icons/doc.png"); 
        imD = id.getImageData();
        img = new Image(Display.getCurrent(), imD);
        //gc.drawImage(img, 10, 0); Bring to front!
        gc.drawImage(img, width-130, height-170);
        
        gc.setBackground(lightgrey); 
        //Color the all the areas in lightgrey
        //Draw shaft
        gc.fillRectangle(55,50,20,height);
        gc.fillRectangle(0,height-30,width-220,20);
        gc.fillRectangle(270,300,20,80);
        //Draw head (x1, y1, x2, y2, x3, y3)
    	gc.fillPolygon(new int[] {width-220,height-40, width-220, height, width-200, height-20});
        gc.setBackground(darkgrey);
        gc.drawImage(img, picx, picy);
        //Color the specified areas in darkgrey
        if (btnHash.getEnabled() == false) {
        	//color nothing
        }else if (btnSignature.getEnabled() == false) { //Step 2
        	//draw the first part of the arrow (from Document to btnHash)
        	gc.fillRectangle(55,50,20,height/2-30);
        	gc.drawImage(img, picx, picy);
        }
        else if (btnOpenInEditor.getEnabled() == false){ //Step 3
        	//draw another part of the arrow (from btnHash to btnSignature)
        	gc.fillRectangle(55,50,20,height);
        	gc.fillRectangle(0,height-30,width/2,20);
        	gc.drawImage(img, picx, picy);
        }
        else { //Step 4
        	gc.fillRectangle(55,50,20,height);
        	gc.fillRectangle(0,height-30,width-220,20);
        	gc.fillRectangle(270,300,20,80);
        	gc.fillPolygon(new int[] {width-220,height-40, width-220, height, width-200, height-20});
        	gc.drawImage(img, picx, picy);
        }
       
        gc.dispose(); 
        
	}//end paintControl
	
	/**
	 * Adds SelectionListeners to the Controls that need them
	 */
	public void createEvents() {
		//Adds a Listener for the document
		btnChooseInput.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
		    }
		    public void widgetSelected(SelectionEvent e) {
		    	try {
		    		//If the user already finished other steps, reset everything to this step (keep the chosen algorithms)
		    		Reset(0);
		    		
		    		//Create the HashWirard
                    InputWizard wiz = new InputWizard();
                    //Display it
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz) {
                    	 @Override
                    	 protected void configureShell(Shell newShell) {
                    		 super.configureShell(newShell);
                    		 newShell.setSize(500, 500);
                    	 } 
                    };
                    if (dialog.open() == Window.OK) {
                    	//Enable to select the hash method
                    	btnHash.setEnabled(true); 
                    	//Activate the second tab of the description
                    	tabDescription.setSelection(1);
                    	canvas1.redraw();
                    	 lblProgress.setText(String.format(Messages.SigComposite_lblProgress,2));  
                    }//end if
                    
		    		/*
		    		//--------------
                    //Enable to select the hash method
                	btnHash.setEnabled(true); 
                	//Activate the second tab of the description
                	tabDescription.setSelection(1);
                	canvas1.redraw();
                	lblProgress.setText(String.format(Messages.SigComposite_lblProgress,2));    
                	//-----------------
                	 */
		        } //end try
		    	catch (Exception ex) {
		        }//end catch
		   }//end widgetSelected
		});
				
		//Adds a Listener for the hash select button
		btnHash.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }
            public void widgetSelected(SelectionEvent e) {
                try {
                	Reset(1);
                    //Create the HashWirard
                    HashWizard wiz = new HashWizard();
                    //Display it
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz) {
                    	 @Override
                    	 protected void configureShell(Shell newShell) {
                    		 super.configureShell(newShell);
                    		 newShell.setSize(300, 600);
                    	 } 
                    };
                    if (dialog.open() == Window.OK) {
                    	//get hash method (integer)
                    	hash = wiz.getHash();
                    	lblHash.setText(hashes[hash]);
                    	//Enable to select the signature method
                        btnSignature.setEnabled(true); 
                        //Activate the third tab of the description
                        tabDescription.setSelection(2);
                        canvas1.redraw();
                        lblProgress.setText(String.format(Messages.SigComposite_lblProgress,3));   
                    }//end if
                } catch (Exception ex) {
                }
            }
        });
		
		//Adds a Listener for the Signature select button
		btnSignature.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }
            public void widgetSelected(SelectionEvent e) {
                try {
                	Reset(2);
                    SignatureWizard wiz = new SignatureWizard();
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz) {
                   	 	@Override
                   	 	protected void configureShell(Shell newShell) {
                   	 		super.configureShell(newShell);
                   	 		newShell.setSize(300, 600);
                   	 } 
                    };
                    if (dialog.open() == Window.OK) { 
                    	//get signature method (integer)
                    	signature = wiz.getSignature();
                    	lblSignature.setText(signatures[signature]);
                    	btnOpenInEditor.setEnabled(true);
                    	//Activate the second tab of the description
                    	tabDescription.setSelection(3);
                    	canvas1.redraw();
                    	lblProgress.setText(String.format(Messages.SigComposite_lblProgress,4));   
                    }
                } catch (Exception ex) {
                    //LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
                }
            }
        });
		
		//Adds a Listener for the reset button
				btnReset.addSelectionListener(new SelectionListener() {
		            public void widgetDefaultSelected(SelectionEvent e) {
		            }
		            public void widgetSelected(SelectionEvent e) {
		                Reset(0);
		            }
		        });
		
	}//end createEvents
	
	/**
	 * Resets the arrow and disables the buttons of future steps if the user clicks the button 
	 * of a previous step. Also clears all description-tabs of future steps and jumps to the current tab.
	 * 
	 * @param step the step to which the progress will be reset (valid numbers: 0-2)
	 */
	private void Reset (int step) {
		String s = String.format(Messages.SigComposite_lblProgress, step+1);
		//If the user already finished other steps, reset everything to this step (keep the chosen algorithms)
		switch (step) {
			case 0: btnHash.setEnabled(false); lblHash.setText(""); 
			case 1: btnSignature.setEnabled(false); lblSignature.setText(""); 
			case 2: btnOpenInEditor.setEnabled(false); break;
			default: break;	
		}
		//Temporary!!
//		switch (step) {
//			case 0: lblProgress.setText(Messages.SigComposite_lblProgress); break;
//			case 1: lblProgress.setText(Messages.SigComposite_lblProgress); break;
//			case 2: lblProgress.setText(Messages.SigComposite_lblProgress); break;
//			default: break;
//		}
		lblProgress.setText(s);
		tabDescription.setSelection(step);
		//redraw canvas (to reset the arrows)
		canvas1.redraw();
	}
}

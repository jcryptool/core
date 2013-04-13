//This class contains all the code required for the GUI

package org.jcryptool.visual.sig.ui.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jcryptool.visual.sig.Messages;
import org.jcryptool.visual.sig.ui.wizards.HashWizard;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

public class SigComposite extends Composite implements PaintListener,ActionListener{
	private Text txtHash;
	private Text txtGeneralDescription;
	private Text txtSignature;
	private Canvas canvas1;
	private Text txtDescriptionOfStep1;
	private Text txtDescriptionOfStep2;
	private Text txtDescriptionOfStep3;
	private Text txtDescriptionOfStep4;
	private Button btnHash;
	private Button btnSignature;
//	private Canvas canvas2;
	SigComposite sc = this;

	//Generates all Elements of the GUI
	public SigComposite(Composite parent, int style, SigView view) {
		super(parent, style);
		sc = this;
		//The color for the textboxes
		Color grey = new Color(Display.getCurrent(), 220, 220, 220);
		
		txtGeneralDescription = new Text(this, SWT.MULTI);
    	txtGeneralDescription.setEditable(false);
		txtGeneralDescription.setBounds(10, 10, 699, 45);
		txtGeneralDescription.setText(Messages.SigComposite_description);
		
		Group grpSignatureGeneration = new Group(this, SWT.NONE);
		grpSignatureGeneration.setText(Messages.SigComposite_grpSignatureGeneration); 
		grpSignatureGeneration.setBounds(10, 61, 699, 529);
		
		Button btnDocumentTemp = new Button(grpSignatureGeneration, SWT.NONE);
		btnDocumentTemp.setBounds(34, 64, 136, 41);
		btnDocumentTemp.setText(Messages.SigComposite_btnDocumentTemp);
		
		Label lblHash = new Label(grpSignatureGeneration, SWT.NONE);
		lblHash.setBounds(34, 230, 136, 14);
		lblHash.setText(Messages.SigComposite_lblHash); 
		
		btnHash = new Button(grpSignatureGeneration, SWT.NONE);
		btnHash.setBounds(34, 164, 136, 60);
		btnHash.setText(Messages.SigComposite_btnHash);
		
		
		
//		Listener listener = new Listener() {
//		      public void handleEvent(Event event) {
//		    	  btnHash.setText("Clicked");
//		    	  HashWizard hw = new HashWizard(.this, SWT.BORDER);
//		    	  hw.setVisible(true);
//		      }
//		    };
//		    
//		btnHash.addListener(SWT.Selection, listener);
		
		txtHash = new Text(grpSignatureGeneration, SWT.BORDER);
		txtHash.setBounds(34, 365, 136, 56);
		txtHash.setEditable(false);
		txtHash.setText("<Hash>");
		
		btnSignature = new Button(grpSignatureGeneration, SWT.NONE);
		btnSignature.setEnabled(false);
		btnSignature.setBounds(248, 365, 136, 60);
		btnSignature.setText(Messages.SigComposite_btnSignature);
		
		Group grpSignedDoc = new Group(grpSignatureGeneration, SWT.NONE);
		grpSignedDoc.setBounds(463, 207, 212, 261);
		grpSignedDoc.setText(Messages.SigComposite_grpSignedDoc); 
		
		txtSignature = new Text(grpSignedDoc, SWT.BORDER);
		txtSignature.setText(""); 
		txtSignature.setBounds(10, 144, 188, 56);
		
		Button btnOpenInEditor = new Button(grpSignedDoc, SWT.NONE);
		btnOpenInEditor.setBounds(10, 206, 109, 28);
		btnOpenInEditor.setText(Messages.SigComposite_btnOpenInEditor);
		
		TabFolder tabDescription = new TabFolder(grpSignatureGeneration, SWT.NONE);
		tabDescription.setBounds(187, 10, 488, 191);
		
		TabItem tbtmNewItem = new TabItem(tabDescription, SWT.NONE);
		tbtmNewItem.setText(Messages.SigComposite_tbtmNewItem_0); 
		
		txtDescriptionOfStep1 = new Text(tabDescription, SWT.MULTI);
		txtDescriptionOfStep1.setBackground(grey);
		txtDescriptionOfStep1.setEditable(false);
		txtDescriptionOfStep1.setText(Messages.SigComposite_txtDescriptionOfStep1);
		tbtmNewItem.setControl(txtDescriptionOfStep1);
		
		TabItem tbtmNewItem_1 = new TabItem(tabDescription, SWT.NONE);
		tbtmNewItem_1.setText(Messages.SigComposite_tbtmNewItem_1); 
		
		txtDescriptionOfStep2 = new Text(tabDescription, SWT.NONE);
		txtDescriptionOfStep2.setBackground(grey);
		txtDescriptionOfStep2.setEditable(false);
		txtDescriptionOfStep2.setText(Messages.SigComposite_txtDescriptionOfStep2);
		tbtmNewItem_1.setControl(txtDescriptionOfStep2);
		
		TabItem tbtmNewItem_2 = new TabItem(tabDescription, SWT.NONE);
		tbtmNewItem_2.setText(Messages.SigComposite_tbtmNewItem_2); 
		
		txtDescriptionOfStep3 = new Text(tabDescription, SWT.NONE);
		txtDescriptionOfStep3.setBackground(grey);
		txtDescriptionOfStep3.setEditable(false);
		txtDescriptionOfStep3.setText(Messages.SigComposite_txtDescriptionOfStep3);
		tbtmNewItem_2.setControl(txtDescriptionOfStep3);
		
		TabItem tbtmNewItem_3 = new TabItem(tabDescription, SWT.NONE);
		tbtmNewItem_3.setText(Messages.SigComposite_tbtmNewItem_3);
		
		txtDescriptionOfStep4 = new Text(tabDescription, SWT.NONE);
		txtDescriptionOfStep4.setBackground(grey);
		txtDescriptionOfStep4.setEditable(false);
		txtDescriptionOfStep4.setText(Messages.SigComposite_txtDescriptionOfStep4);
		tbtmNewItem_3.setControl(txtDescriptionOfStep4);
		
		Button btnReset = new Button(grpSignatureGeneration, SWT.NONE);
		btnReset.setBounds(581, 474, 94, 28);
		btnReset.setText(Messages.SigComposite_btnReset);
		
		Label lblProgress = new Label(grpSignatureGeneration, SWT.NONE);
		lblProgress.setBounds(492, 481, 83, 14);
		lblProgress.setText(Messages.SigComposite_lblProgress);  
		
		Label lblSignature = new Label(grpSignatureGeneration, SWT.NONE);
		lblSignature.setText(Messages.SigComposite_lblSignature);
		lblSignature.setBounds(248, 431, 136, 14);
		
		canvas1 = new Canvas(grpSignatureGeneration, SWT.NONE);
		canvas1.setBounds(70, 88, 64, 281);

		createEvents();
		//canvas2 = new Canvas(grpSignatureGeneration, SWT.NONE);
		//canvas2.setBounds(165, 375, 301, 38);
		
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
		GC gc = e.gc;
		//Set the used colors
		Color grey = new Color(Display.getCurrent(), 140, 138, 140);
        Color lightGrey = new Color(Display.getCurrent(), 180, 177, 180);
        //Canvas1 - The horizontal part:
        //Get the size of the canvas area
        Rectangle clientArea = canvas1.getClientArea(); 
        int width = clientArea.width; 
        int height = clientArea.height; 
        gc.setBackground(lightGrey); 
        //Color the specified area
        gc.fillRectangle(width/2-10,0,20,height);
        
        //Canvas2 - The horizontal part:
        
        gc.dispose(); 
        
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                | SWT.OK);
        messageBox.setText("Click"); 
        messageBox.setMessage("Click"); 
        messageBox.open();
		
	}
	
	public void createEvents() {
		btnHash.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }
 		  public void widgetSelected(SelectionEvent e) {
                try {
                    
                    HashWizard wiz = new HashWizard();
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz);
                    if (dialog.open() == Window.OK) {
                        btnSignature.setEnabled(true);
                    }
                } catch (Exception ex) {
                    //LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
                }
            }
        });
	}
	
}

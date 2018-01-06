// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.rsa.ui.wizards.wizardpages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.rsa.Action;
import org.jcryptool.visual.rsa.Messages;
import org.jcryptool.visual.rsa.RSAData;

/**
 * Page for entering a plaintext to sign or encrypt.
 *
 * @author Michael Gaber
 */
public class EnterPlaintextPage extends TextWizardPage {

    /** Limit for the length of the text that might be entered into the plaintext field */
    private static final int TEXTLIMIT = 150;

    /** unique pagename to get this page from inside a wizard. */
    private static final String PAGENAME = "Enter Plaintext Page"; //$NON-NLS-1$

    /** title of this page, displayed in the head of the wizard. */
    private static final String TITLE = Messages.EnterPlaintextPage_enter_plaintext;

    /** the action of this run, decides whether to display the hash-method description. */
    private final Action action;

    /** common data object to store the entries. */
    private final RSAData data;

    /** Checkbox to select that numbers are to be entered directly and not to be converted */
    private Button numberCheckBox;

    /**
     * Constructor setting the data and the action.
     *
     * @param action the cryptographic action
     * @param data the shared data object
     */
    public EnterPlaintextPage(Action action, RSAData data) {
        super(PAGENAME, TITLE, null);
        this.setDescription(Messages.EnterPlaintextPage_enter_plaintext_text);
        this.setPageComplete(false);
        this.action = action;
        this.data = data;
    }

    /**
     * sets up all the UI stuff.
     *
     * @param parent the parent composite
     */
    @Override
	public final void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        // do stuff like layout et al
        composite.setLayout(new GridLayout());
        Label label, label1;
        if (action == Action.SignAction) {
            label = new Label(composite, SWT.WRAP);
            label.setText(Messages.EnterPlaintextPage_simple_hash);

            // separator
            new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                    false));
        }
        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText(Messages.EnterCiphertextPage_textentry);
        text = new Text(composite, SWT.BORDER | SWT.WRAP);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        text.setTextLimit(TEXTLIMIT);
        text.addModifyListener(new ModifyListener() {
            @Override
			public void modifyText(ModifyEvent e) {
                setPageComplete(!((Text) e.widget).getText().equals("")); //$NON-NLS-1$
            }
        });

        
      text.addVerifyListener(new VerifyListener() { 
    	@Override
		public void verifyText(VerifyEvent e) { 
    		switch (e.keyCode) { 
    			case SWT.DEL: 
    			case SWT.BS: 
    			case ' ': 
    				return; 
    			default:
                     break; 
            }
            if (numberCheckBox.getSelection()) { 
            	e.doit = e.text.matches(DIGIT);
            }
//            } else { 
//            	if (e.text.matches(CHARACTERS)) { 
//             	e.doit = true; 
//            	} else { 
//               // Removes everything except "a-A0-9_ " from the inserted text. 
//               e.text = e.text.replaceAll("[^\\w\u00E4\u00F6\u00FC\u00C4\u00D6\u00DC ]*",""); 
//               } 
//               } 
              } 
        }); 
        
//        text.addVerifyListener(new VerifyListener() { 
//        	public void verifyText(VerifyEvent e) { 
//        		switch (e.keyCode) { 
//        			case SWT.DEL: 
//        			case SWT.BS: 
//        			case ' ': 
//        				return; 
//        			default: 
//                         break; 
//                } 
//                if (numberCheckBox.getSelection()) { 
//                	e.doit = e.text.matches(DIGIT); 
//                } else { 
//                	if (e.text.matches(CHARACTERS)) { 
//                 	e.doit = true; 
//                	} else { 
//                   // Removes everything except "a-A0-9_ " from the inserted text. 
//                   e.text = e.text.replaceAll("[^\\w\u00E4\u00F6\u00FC\u00C4\u00D6\u00DC ]*",""); 
//                   } 
//                   }; 
//                  } 
//            }); 


        
        
        
        numberCheckBox = new Button(composite, SWT.CHECK);
        numberCheckBox.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        numberCheckBox.setText(Messages.EnterPlaintextPage_0);
        numberCheckBox.setToolTipText(Messages.EnterPlaintextPage_1);
        numberCheckBox.addSelectionListener(new SelectionAdapter() {
    	
    	@Override
		public void widgetSelected(SelectionEvent e) {
    		if (numberCheckBox.getSelection()) {
    		setPageComplete(text.getText().matches(DIGIT+WHITESPACE));
    		}
    		else {
    			setPageComplete(!(text.getText().equals("")));
    		}
    		getWizard().getContainer().updateButtons();
    	}
    });
        
		label1 = new Label(composite, SWT.NONE);
		label1.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		label1.setText(Messages.EnterPlaintextPage_textmodify);
        
        if (action == Action.SignAction) {
            final Button SHA1Checkbox = new Button(composite, SWT.CHECK);
            SHA1Checkbox.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
            SHA1Checkbox.setText(Messages.EnterPlaintextPage_use_sha1);
            SHA1Checkbox.setToolTipText(Messages.EnterPlaintextPage_use_sha1_popup);
            SHA1Checkbox.addSelectionListener(new SelectionAdapter() {

                @Override
				public void widgetSelected(SelectionEvent e) {
                    data.setSimpleHash(!SHA1Checkbox.getSelection());
                }
            });
            SHA1Checkbox.setSelection(!data.getSimpleHash());
        }
                
        // fill in old data
        text.setText(data.getPlainText_Old());
        // finish
        setControl(composite);
    }

    /**
     * getter for the pagename.
     *
     * @return the pagename
     */
    public static String getPagename() {
        return PAGENAME;
    }

    public boolean setPageCompleteNumbers(){
    	if (this.text.getText().matches(DIGIT)){
    		return true;
    	}
    	else {
    		return true;
    	}
    }
    
    @Override
    public String getText() {
        if (numberCheckBox.getSelection()) {
            String[] text = this.text.getText().replaceAll("\\s+", " ").split(" "); //$NON-NLS-1$
            StringBuilder sb = new StringBuilder(text.length);
            for (int i = 0; i < text.length; i++) {
                sb.append(Character.toChars(Integer.parseInt(text[i])));
                //sb.append(Integer.parseInt(text[i]));
            }
            return sb.toString();
        } else {
            return super.getText();
        }
    }
    
    @Override
    public boolean canFlipToNextPage(){
    	   if (numberCheckBox.getSelection()) {
    		   return false;
    	   }
    	   else {
    		   return true;
    	   }
    	}
    
}
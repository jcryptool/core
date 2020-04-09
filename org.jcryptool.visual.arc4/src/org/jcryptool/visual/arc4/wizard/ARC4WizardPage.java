//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.arc4.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.arc4.ARC4Con;
import org.jcryptool.visual.arc4.Messages;
import org.jcryptool.visual.arc4.Type;
import org.jcryptool.visual.arc4.algorithm.ARC4Algorithm;

/**
 * The wizard page that takes the user input
 * 
 * @author Luca Rupp
 * @author Thorben Groos (switchable keylength)
 */
public class ARC4WizardPage extends WizardPage {

	private ARC4Algorithm alg;
	
    // the background for the page
    private Composite page;

    // the input field
    private Text input;
      
    // an area to display messages e.g. if the user tries to input invalid data
    private Label messages, notification;
    
    // the org.jcryptool.visual.library.Lib class provides a similar constant, 
    // but it also allows whitespace, which is undesirable here
    private String HEXDIGIT = "[0-9a-fA-F]*";
    
    private String NOTHEXDIGIT = "[^0-9a-fA-F]*";
    
    // Which button was pressed? Choose key or choose plain text.
    private Type type;
    
    private VerifyListener verifyInput = new VerifyListener() {
		
		@Override
		public void verifyText(VerifyEvent e) {
			e.doit = true;
			
            // get the current text in the textfield.
            String olds = input.getText();
            String userInput = olds.substring(0, e.start) + e.text + olds.substring(e.end);
            
			if (userInput.length() > ARC4Con.HEXLEN) {
				messages.setText(Messages.WizardPageToLong);
				e.doit = false;
				return;
			}
			
			
            // delete all whitespace characters
            e.text = e.text.replaceAll("\\s+", "");
            // find invalid chars and remove them
            if (!e.text.matches(HEXDIGIT)) {
                messages.setText("'" + e.character + "' " + Messages.WizardPageInvalidHexDigit);
                // error message is printed either way, but unless the user 
                // only typed one character it is immediately overwritten
                if (e.text.length() == 1) {
                    e.doit = false;
                    return;
                }
            }
            
            e.text = e.text.replaceAll(NOTHEXDIGIT, "");
            
            e.text = e.text.toLowerCase();      
            
            // modified input
            String modifiedInput = olds.substring(0, e.start) + e.text + olds.substring(e.end);
            
            // print the keylength in byte
            if (type == Type.KEY) {
            	messages.setText(Messages.WizardPageKeyLengthInfo1
            			+ modifiedInput.length() / 2 + " "
            			+ Messages.WizardPageKeyLengthInfo2
            			+ modifiedInput.length() + " "
            			+ Messages.WizardPageKeyLengthInfo3);
            } else if (type == Type.PLAIN) {
            	messages.setText(Messages.WizardPagePlaintextLengthInfo1
            			+ modifiedInput.length() / 2 + " "
            			+ Messages.WizardPagePlaintextLengthInfo2
            			+ modifiedInput.length() + " "
            			+ Messages.WizardPagePlaintextLengthInfo3);
            }
            
        	if (modifiedInput.length() >= 2 && modifiedInput.length() % 2 == 0) {
                  setPageComplete(true);
              } else {
                  setPageComplete(false);
              }
		}
	};

    /**
     * The constructor fir the ARC4WizardPage
     * 
     * @param pageName the name of the page
     * @param description the description of the page
     */
    protected ARC4WizardPage(ARC4Algorithm alg, Type type, String pageName, String description) {
        super(pageName);
        this.alg = alg;
        this.type = type;
        setTitle(pageName);
        setDescription(description);
    }

    /**
     * Initialize the page
     */
    @Override
	public void createControl(final Composite parent) {

        // create the background
        page = new Composite(parent, SWT.NONE);
        page.setLayout(new GridLayout(3, false));
        page.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        setControl(page);

        input = new Text(page, SWT.BORDER);
        input.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 3, 1));
        
        // Set the old key to the input field.
        if (type == Type.KEY) {
        	setKeyToTextfield(alg.getKey());
        } else if (type == Type.PLAIN) {
        	setKeyToTextfield(alg.getPlain());
        }
        
        input.addVerifyListener(verifyInput);

        messages = new Label(page, SWT.NONE);
        messages.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 3, 1));
        if (type == Type.KEY) {
        	messages.setText(Messages.WizardPageKeyLengthInfo1
        			+ input.getText().length() / 2 + " "
        			+ Messages.WizardPageKeyLengthInfo2
        			+ input.getText().length() + " "
        			+ Messages.WizardPageKeyLengthInfo3);
        } else if (type == Type.PLAIN) {
        	messages.setText(Messages.WizardPagePlaintextLengthInfo1
        			+ input.getText().length() / 2 + " "
        			+ Messages.WizardPagePlaintextLengthInfo2
        			+ input.getText().length() + " "
        			+ Messages.WizardPagePlaintextLengthInfo3);
        }
        
        
        notification = new Label(page, SWT.NONE);
        notification.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        notification.setText(Messages.WizardPageHexNotification);
    }
    
    /**
     * Set the key from the alg-Object to the input field.
     */
    private void setKeyToTextfield(int[] key) {
    	
    	input.setText("");
        for (int i = 0; i < key.length; i++) {
        	
        	// Add a zero if the key value at position i is less
        	// than 16. This necessary because the Integer.toHexString(int)
        	// will not display the preceding zero. So I add it manually.
        	if (key[i] < 16) {
        		input.append("0" + Integer.toHexString(key[i]));
        	} else {
        		input.append(Integer.toHexString(key[i]));
        	}

        }
    }

    /**
     * return the data from the wizard
     * 
     * @return returns an array of integers that represent the input from the user
     */
    public int[] getData() {
        String in = this.input.getText();
        // two hex-chars for one byte
        int[] ret = new int[in.length() / 2];
        for (int a = 0; a < ret.length; a += 1) {
            // the prefix string "0x" is needed for the Integer.decode function to recognize the
            // number as hexadecimal
            ret[a] = Integer.decode("0x" + in.charAt(a * 2) + in.charAt(a * 2 + 1));
        }
        return ret;
    }
    
}

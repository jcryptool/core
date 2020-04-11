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
package org.jcryptool.visual.arc4.ui;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.arc4.ARC4Con;
import org.jcryptool.visual.arc4.Messages;
import org.jcryptool.visual.arc4.Type;
import org.jcryptool.visual.arc4.algorithm.ARC4Algorithm;
import org.jcryptool.visual.arc4.wizard.ARC4Wizard;

/**
 * This class is used to display the key, the plaintext, the pseudorandom numbers and the encrypted
 * text
 * 
 * @author Luca Rupp
 */
public class DatavectorVisual extends Composite {

    // The labeled box around the content
    private Group group;

    // The labels that show the actual content
    private Label[] data;

    // The button that allows you to open a wizard and modify the data of this data vector
    private Button wizbutton;
    
    // The button that allows you to copy the data to the clipboard
    private Button clipbutton;

    // allows you to modify the data; is not available for all data vector types
    private ARC4Wizard wizard;

    // these text variables contain the texts for the labels and buttons, specific for each
    // datavectorvisual type
    // Grouptext is the label of the border, buttonTool is the toolTip of the button
    private String groupText = "", buttonText = "", toolTip = "", buttonTool = "";

    // The algorithm object is where the data from the wizard is ultimately passed to
    private ARC4Algorithm alg;
    
    /**
     * The parent Composite of this Composite.
     */
    private ARC4Composite parent;
    
    private Type type;


    /**
     * The constructor of the datavector visual class
     * 
     * @param parent the parent of this widget
     * @param style the style of this widget, this can be used to draw a border around the widget
     *            depending on its type
     * @param type the type of the datavector visual; this may not be very object oriented and
     *            pretty but i think making a subclass for every type would be overkill
     * @param alg the algorithm that holds the data on which this datavector visual operates
     */
    public DatavectorVisual(ARC4Composite parent, int style, Type type, ARC4Algorithm alg) {
        super(parent, style);
        this.alg = alg;
        this.parent = parent;
        this.type = type;
        // three columns for the data and one column for the button; this assures proper alignment
        // of the widgets
        // in ARC4Composite; for the explanation of the factor two look into the comments in
        // ARC4Composite
        
        // plaintext and key datavectors are surrounded by a composite for the purpose of laying 
        // them out correctly this code is to
        // correct the fife pixels of miss-alignment that would occur with random and enc otherwise
        GridLayout tlayout = new GridLayout(ARC4Con.H_SPAN_MAIN, true);
        tlayout.marginWidth = 0;
        tlayout.marginHeight = 0;
        setLayout(tlayout);

        // set the type specific texts
        setTextForType(type);
        
        group = new Group(this, SWT.NONE);
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, ARC4Con.H_SPAN_LEFT, 2));
        // 17 and not 16 to keep proper alignment in ARC4Con
        group.setLayout(new GridLayout(ARC4Con.DATAVECTOR_VISUAL_LENGTH + 1, true));
        // set the label of the box and its tool tip
        group.setText(groupText);
        group.setToolTipText(toolTip);

        createLabels();

        // create the button that allows you to open a wizard; in case the type of datavector visual
        // does not allow
        // to open a wizard the method will take care of it
        createWizardButton(type);
        
        // create the button that allows you to paste data from this datavector to the system clipboard
        createClipButton(type);

        // to initialize key and plain with pseudorandom values
        if (type == Type.KEY) {
            setDataToGUI(alg.getKey());
        } else if (type == Type.PLAIN) {
            setDataToGUI(alg.getPlain());
        }
    }
    
    /**
     * Create the group that contains the key, plain or encrpted hex values.
     * @param parent
     */
    private void createLabels() {
        // the border around the data is initialized


        // a empty label to fill the first column and preserve alignment, this works because all
        // children of the
        // group are the same with
        new Label(group, SWT.NONE);

        // Create the labels that show the actual data and style them
        data = new Label[ARC4Con.DATAVECTOR_VISUAL_LENGTH];
        for (int i = 0; i < data.length; i++) {
            // center text on the labels
            data[i] = new Label(group, SWT.CENTER);
            data[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
            
            if (type == Type.ENC || type == Type.RAND) {
            	data[i].setEnabled(false);
            }
            
        }
    }

    /**
     * Set the type specific strings for the four different kinds of datavectors; why there are
     * these different types instead of subclasses of datavector visual has been already explained
     * above
     * 
     * @param type a numeric constant that describes the kind of the datavector; values are defined
     *            in ARC4Con
     */
    private void setTextForType(Type type) {
        // As only KEY and PLAIN have got a button, only they get a text and tool tip for their
        // button
        if (type == Type.KEY) {
            groupText = Messages.DatavectorVisualKEYGroup;
            toolTip = Messages.DatavectorVisualKEYTool;
            buttonText = Messages.DatavectorVisualKEYButton;
            buttonTool = Messages.DatavectorVisualKEYButtonTool;
        } else if (type == Type.ENC) {
            groupText = Messages.DatavectorVisualENCGroup;
            toolTip = Messages.DatavectorVisualENCTool;
        } else if (type == Type.PLAIN) {
            groupText = Messages.DatavectorVisualPLAINGroup;
            buttonText = Messages.DatavectorVisualPLAINButton;
            toolTip = Messages.DatavectorVisualPLAINTool;
            buttonTool = Messages.DatavectorVisualPLAINButtonTool;
        } else if (type == Type.RAND) {
            groupText = Messages.DatavectorVisualRANDGroup;
            toolTip = Messages.DatavectorVisualRANDTool;
        }
    }

    /**
     * Create the button that allows you to open a wizard and modify the data
     * 
     * @param type the integer constant from ARC4Con that represents the type of datavector
     */
    private void createWizardButton(Type type) {
        wizbutton = new Button(this, SWT.PUSH);
        wizbutton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        // Actually for the sake of simplicity and proper alignment a button is created in any case,
        // even if the
        // type of datavector visual does not permit one, but in those cases it is just made
        // invisible
        if (type == Type.ENC || type == Type.RAND) {
            wizbutton.setVisible(false);
            return;
        }
        wizbutton.setToolTipText(buttonTool);
        wizbutton.setText(buttonText);
        // add the action to the button; the two methods have been separated to keep them shorter
        // and the code
        // more readable even though one could argue that they belong together
        addActionToWizButton(type);
        
    }

    /**
     * Add the desired functionality to the button: the ability to open a wizard to modify the data
     * 
     * @param type the numeric constant from ARC4Con that represents the kind of datavector
     */
    private void addActionToWizButton(final Type type) {
        // those types do not even have a visible button
        if (type == Type.ENC || type == Type.RAND) {
            return;
        }
        this.wizbutton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                wizard = new ARC4Wizard(alg, type, DatavectorVisual.this);
                WizardDialog dialog = new WizardDialog(getShell(), wizard);
                // remove the help button from the wizard
                dialog.setHelpAvailable(false);
                dialog.open();
            }
        });
    }
    
    /**
     * Create the button that allows you to copy the data from the datavector to the system clipboard
     */
    private void createClipButton(Type type) {
    	clipbutton = new Button(this, SWT.PUSH);
    	clipbutton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        clipbutton.setText(Messages.CopyToClipboard);
        clipbutton.setToolTipText(Messages.CopyToClipboardTool);
        addActionToClipButton(type);
    }
    
    /**
     * Add the desired functionality to the button: the ability to copy the data to the system clipboard
     * 
     * @param type the numeric constant from ARC4Con that represents the kind of datavector
     */
    private void addActionToClipButton(final Type type) {
        this.clipbutton.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent e) {
                Clipboard clipboard = new Clipboard(DatavectorVisual.this.getDisplay());
                int [] dataint = new int[ARC4Con.DATAVECTOR_VISUAL_LENGTH];
                String datastring = "";
                // get the right data from the algorithm depending on the type
                if (type == Type.ENC) {
                    dataint = alg.getEnc();
                } else if (type == Type.KEY) {
                    dataint = alg.getKey();
                } else if (type == Type.PLAIN) {
                    dataint = alg.getPlain();
                } else if (type == Type.RAND) {
                    dataint = alg.getRandom();
                }
                // alg returns the integers in decimal number format:
                // convert the decimal values to hexadecimal strings and append them
                for (int a : dataint) {
                    // to pad leading zeros
                    String temp = Integer.toHexString(a);
                    if (temp.length() == 1) {
                        datastring += ("0" + temp);
                    } else {
                        datastring += temp;
                    }
                }
                TextTransfer textTransfer = TextTransfer.getInstance();
                // write the string to the system clipboard
                clipboard.setContents(new String[]{datastring}, new Transfer[]{textTransfer});
                clipboard.dispose();
            }
        });
    }

    /**
     * Set the data for the datavector
     * 
     * @param data an integer array that will become the new content of the datavector; it has to be
     *            the same length as the array of data-labels of this datavector
     */
    public void setDataToGUI(int[] data) {
    	
    	for (int a = 0; a < data.length; a++) {
            // the purpose of this differentiation is padding: if it was not for this if, if you had
            // 10 (10) in the algorithm and write it into the UI, it would become 0xA and not 0x0A
            if (data[a] >= ARC4Con.DATAVECTOR_VISUAL_LENGTH) {
                this.data[a].setText(ARC4Con.HEX_MARK + Integer.toHexString(data[a]));
            } else {
                this.data[a].setText(ARC4Con.HEX_MARK + "0" + Integer.toHexString(data[a]));
            }
            this.data[a].setVisible(true);
    	}
    	
    	// Hide labels if the plaintext is not 16 byte long.
    	for (int i = data.length; i < ARC4Con.DATAVECTOR_VISUAL_LENGTH; i++) {
    		this.data[i].setVisible(false);
    	}
    	

    	// This disables the labels if they display the random or
    	// the encrypted text and if they are not set yet.
    	if (type == Type.RAND || type == Type.ENC) {
        	boolean isAllZero = true; 	
        	for (int i = 0; i < alg.getRandom().length; i++) {
        		if (type == Type.RAND) {
        			if (!(alg.getRandom()[i] == 0)) {
        				isAllZero = false;
        			}
        		} else if (type == Type.ENC) {
        			if (!(alg.getEnc()[i] == 0)) {
        				isAllZero = false;
        			}
        		}
        	}
        	
    		for (int i = 0; i < ARC4Con.DATAVECTOR_VISUAL_LENGTH; i++) {
    			this.data[i].setEnabled(!isAllZero);
    		}
    	}



    }
    
    public void updateCompleteGUI() {
    	parent.syncronizeInternWithExtern();
    }
  

    /**
     * Allows to enable and disable the button that allows you to open a wizard
     * 
     * @param par true to activate the button, false to deactivate it
     */
    public void switchButton(boolean par) {
        wizbutton.setEnabled(par);
    }

}
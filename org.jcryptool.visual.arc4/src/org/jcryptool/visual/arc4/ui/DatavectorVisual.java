//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
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

    // The type of the data vector visual; values are defined in ARC4Con
    private int type;

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
    public DatavectorVisual(Composite parent, int style, int type, ARC4Algorithm alg) {
        super(parent, style);
        this.alg = alg;
        this.type = type;
        // three columns for the data and one column for the button; this assures proper alignment
        // of the widgets
        // in ARC4Composite; for the explanation of the factor two look into the comments in
        // ARC4Composite
        
        // plaintext and key datavectors are surrounded by a composite for the purpose of laying 
        // them out correctly this code is to
        // correct the fife pixels of miss-alignment that would occur with random and enc otherwise
        GridLayout tlayout = new GridLayout(ARC4Con.H_SPAN_MAIN, true);
        this.setLayout(tlayout);

        // set the type specific texts
        setTextForType(type);

        // the border around the data is initialized
        this.group = new Group(this, SWT.SHADOW_IN);
        this.group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, ARC4Con.H_SPAN_LEFT, 1));
        // 17 and not 16 to keep proper alignment in ARC4Con
        this.group.setLayout(new GridLayout(ARC4Con.DATAVECTOR_VISUAL_LENGTH + 1, true));
        // set the label of the box and its tool tip
        this.group.setText(this.groupText);
        this.group.setToolTipText(this.toolTip);

        // a empty label to fill the first column and preserve alignment, this works because all
        // children of the
        // group are the same with
        new Label(this.group, SWT.NONE);

        // create the labels that show the actual content
        createDataLabels();

        // create the button that allows you to open a wizard; in case the type of datavector visual
        // does not allow
        // to open a wizard the method will take care of it
        createWizardButton(type);
        
        // create the button that allows you to paste data from this datavector to the system clipboard
        createClipButton(type);

        // to initialize key and plain with pseudorandom values
        if (type == ARC4Con.KEY) {
            this.setData(alg.getKey());
        } else if (type == ARC4Con.PLAIN) {
            this.setData(alg.getPlain());
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
    private void setTextForType(int type) {
        // As only KEY and PLAIN have got a button, only they get a text and tool tip for their
        // button
        if (type == ARC4Con.KEY) {
            this.groupText = Messages.DatavectorVisualKEYGroup;
            this.toolTip = Messages.DatavectorVisualKEYTool;
            this.buttonText = Messages.DatavectorVisualKEYButton;
            this.buttonTool = Messages.DatavectorVisualKEYButtonTool;
        } else if (type == ARC4Con.ENC) {
            this.groupText = Messages.DatavectorVisualENCGroup;
            this.toolTip = Messages.DatavectorVisualENCTool;
        } else if (type == ARC4Con.PLAIN) {
            this.groupText = Messages.DatavectorVisualPLAINGroup;
            this.buttonText = Messages.DatavectorVisualPLAINButton;
            this.toolTip = Messages.DatavectorVisualPLAINTool;
            this.buttonTool = Messages.DatavectorVisualPLAINButtonTool;
        } else if (type == ARC4Con.RAND) {
            this.groupText = Messages.DatavectorVisualRANDGroup;
            this.toolTip = Messages.DatavectorVisualRANDTool;
        }
    }

    /**
     * Create the labels that show the actual data and style them
     */
    private void createDataLabels() {
        this.data = new Label[ARC4Con.DATAVECTOR_VISUAL_LENGTH];
        for (int i = 0; i < this.data.length; i++) {
            // center text on the labels
            this.data[i] = new Label(this.group, SWT.CENTER);
            this.data[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        }
    }

    /**
     * Create the button that allows you to open a wizard and modify the data
     * 
     * @param type the integer constant from ARC4Con that represents the type of datavector
     */
    private void createWizardButton(int type) {
        wizbutton = new Button(this, SWT.PUSH);
        GridData dataBut = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        dataBut.minimumHeight = ARC4Con.DATAVECTOR_VISUAL_L_B_HEIGHT;
        dataBut.minimumWidth = ARC4Con.DATAVECTOR_VISUAL_L_B_WIDTH;
        wizbutton.setLayoutData(dataBut);
        // Actually for the sake of simplicity and proper alignment a button is created in any case,
        // even if the
        // type of datavector visual does not permit one, but in those cases it is just made
        // invisible
        if (type == ARC4Con.ENC || type == ARC4Con.RAND) {
            wizbutton.setVisible(false);
            return;
        }
        wizbutton.setToolTipText(this.buttonTool);
        wizbutton.setText(this.buttonText);
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
    private void addActionToWizButton(final int type) {
        // those types do not even have a visible button
        if (type == ARC4Con.ENC || type == ARC4Con.RAND) {
            return;
        }
        this.wizbutton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                wizard = new ARC4Wizard(type, DatavectorVisual.this);
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
    private void createClipButton(int type) {
        clipbutton = new Button(this, SWT.PUSH | SWT.CENTER);
        GridData dataClip = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        dataClip.minimumHeight = ARC4Con.DATAVECTOR_VISUAL_L_B_HEIGHT;
        dataClip.minimumWidth = ARC4Con.DATAVECTOR_VISUAL_L_B_WIDTH;
        clipbutton.setLayoutData(dataClip);
        clipbutton.setText(Messages.CopyToClipboard);
        clipbutton.setToolTipText(Messages.CopyToClipboardTool);
        addActionToClipButton(type);
    }
    
    /**
     * Add the desired functionality to the button: the ability to copy the data to the system clipboard
     * 
     * @param type the numeric constant from ARC4Con that represents the kind of datavector
     */
    private void addActionToClipButton(final int type) {
        this.clipbutton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                Clipboard clipboard = new Clipboard(DatavectorVisual.this.getDisplay());
                int [] dataint = new int[ARC4Con.DATAVECTOR_VISUAL_LENGTH];
                String datastring = "";
                // get the right data from the algorithm depending on the type
                if (type == ARC4Con.ENC) {
                    dataint = alg.getEnc();
                } else if (type == ARC4Con.KEY) {
                    dataint = alg.getKey();
                } else if (type == ARC4Con.PLAIN) {
                    dataint = alg.getPlain();
                } else if (type == ARC4Con.RAND) {
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
    public void setData(int[] data) {
        // incorrect length
        if (data == null || data.length != ARC4Con.DATAVECTOR_VISUAL_LENGTH) {
            return;
        }
        for (int a = 0; a < data.length; a++) {
            // pass the new data to the internal representation of the data
            if (this.type == ARC4Con.KEY) {
                this.alg.setKey(data);
            } else if (this.type == ARC4Con.PLAIN) {
                this.alg.setPlain(data);
            }
            // the purpose of this differentiation is padding: if it was not for this if, if you had
            // 10 (10) in the algorithm and write it into the UI, it would become 0xA and not 0x0A
            if (data[a] >= ARC4Con.DATAVECTOR_VISUAL_LENGTH) {
                this.data[a].setText(ARC4Con.HEX_MARK + Integer.toHexString(data[a]));
            } else {
                this.data[a].setText(ARC4Con.HEX_MARK + "0" + Integer.toHexString(data[a]));
            }
        }
    }

    /**
     * Allows to enable and disable the button that allows you to open a wizard
     * 
     * @param par true to activate the button, false to deactivate it
     */
    public void switchButton(boolean par) {
        this.wizbutton.setEnabled(par);
    }

}
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.visual.arc4.ARC4Con;
import org.jcryptool.visual.arc4.Messages;

/**
 * This class offers the visual representation of the s box of the algorithm and methods for
 * accessing it
 * 
 * @author Luca Rupp
 */
public class VectorVisual extends Composite {

    // used for the labeled box around the content
    private Group group;

    // what you actually see of the vector: the labels that show the data
    private Label[] vector = new Label[(ARC4Con.DATAVECTOR_VISUAL_LENGTH + 1) * (ARC4Con.DATAVECTOR_VISUAL_LENGTH + 1)];

    // style data for every label; in SWT GridData objects should not be reused
    //private GridData[] data = new GridData[vector.length];

    /**
     * Constructor for the VectorVisual class
     * 
     * @param parent the parent of this VectorVisual
     * @param style is ignored, this is only here due to the inheritance
     */
    public VectorVisual(Composite parent, int style) {
        super(parent, SWT.NONE);
        // contains only one object: the group (It has to be that way, because Group dosn't allow to
        // inherit from it)
        GridLayout gl_this = new GridLayout();
        gl_this.marginHeight = 0;
        gl_this.marginWidth = 0;
        setLayout(gl_this);
        
        group = new Group(this, SWT.NONE);
        group.setLayout(new GridLayout(ARC4Con.DATAVECTOR_VISUAL_LENGTH + 1, true));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        group.setText(Messages.VectorVisualGroup);
        group.setToolTipText(Messages.VectorVisualTool);
        
        // create the labels that hold the data
        drawVector();
        // label those labels that are column names
        setColumnNames();
        // label those labels that are row names
        setRowNames();
        
    }

    /**
     * Generate and style the labels and give them their tool tip texts
     */
    private void drawVector() {
        for (int a = 0; a < vector.length; a++) {
            // SWT.CENTER for centering text on the label
            vector[a] = new Label(group, SWT.CENTER);
            //data[a] = new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
            // make the labels big enough from the beginning as they do not resign themselves when
            // you assign them
            // a text that is to long for their current size
			//data[a].minimumHeight = ARC4Con.VECTOR_VISUAL_LABEL_HEIGHT;
			//data[a].minimumWidth = ARC4Con.VECTOR_VISUAL_LABEL_WIDTH;
			//vector[a].setLayoutData(data[a]);
            vector[a].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
            // they get their groups tool tip in order to have it displayed even if the use
            // technically hovers
            // his mouse over one of the labels and not the group itself
            vector[a].setToolTipText(Messages.VectorVisualTool);
        }
    }

    /**
     * Set the column names
     */
    private void setColumnNames() {
        for (int a = 1; a < (ARC4Con.DATAVECTOR_VISUAL_LENGTH + 1); a++) {
            // The HEX_MARK string is there to allow it to give the numbers a prefix (for example
            // "0x" for hexadecimal);
            // currently this is not used to save space
            vector[a].setText(ARC4Con.HEX_MARK + Integer.toHexString(a - 1));
            // color the column name labels
            vector[a].setBackground(ARC4Con.VH_COLOR);
        }
    }

    /**
     * Set the row names
     */
    private void setRowNames() {
        int a = ARC4Con.DATAVECTOR_VISUAL_LENGTH + 1;
        for ( ; a < vector.length; a += (ARC4Con.DATAVECTOR_VISUAL_LENGTH + 1)) {
            vector[a].setText(ARC4Con.HEX_MARK + Integer.toHexString(a / (ARC4Con.DATAVECTOR_VISUAL_LENGTH + 1) - 1));
            vector[a].setBackground(ARC4Con.VH_COLOR);
        }
    }

    /**
     * The visualization does not differentiate between the labels that hold row or column names and
     * the labels that show the actual data, but users want to address the bytes in a natural
     * fashion, so this method converts from external to internal indexes
     * 
     * @param index the external index that is to be converted into a internal index
     * @return returns an integer: the internal index that corresponds to the external index
     */
    private int convertPosition(int index) {
        return ((int) Math.floor(index / ARC4Con.DATAVECTOR_VISUAL_LENGTH) + 1) * (ARC4Con.DATAVECTOR_VISUAL_LENGTH + 1)
                + (index % ARC4Con.DATAVECTOR_VISUAL_LENGTH + 1);
    }

    /**
     * Take a integer array and use it to fill the visual representation of the array
     * 
     * @param data an integer array that holds only the real data (no row or column names)
     */
    public void setData(int[] data) {
        // check if the vector has the correct length
        if (data.length != (int) Math.pow(ARC4Con.DATAVECTOR_VISUAL_LENGTH, 2)) {
            return;
        }
        // run through the integer array, convert positions and fill the labels
        for (int a = 0; a < data.length; a++) {
            if (data[a] >= ARC4Con.DATAVECTOR_VISUAL_LENGTH) {
                vector[convertPosition(a)].setText(ARC4Con.HEX_MARK + Integer.toHexString(data[a]));
                // this is necessary, because without it for the decimal value ten you would get "A"
                // and not "0A"
                // so this is just for padding and to improve the visual appearance of the s box
            } else {
                vector[convertPosition(a)].setText(ARC4Con.HEX_MARK + "0" + Integer.toHexString(data[a]));
            }
        }
    }

    /**
     * Highlight two bytes of the byte vector
     * 
     * @param a the index of the first byte (external index)
     * @param b the index of the second byte (external index)
     */
    public void highlight(int a, int b) {
        // remove previous coloring of data labels
        clearColor();
        vector[convertPosition(a)].setBackground(ARC4Con.VECTOR_HIGHLIGHT);
        vector[convertPosition(b)].setBackground(ARC4Con.VECTOR_HIGHLIGHT);
    }

    /**
     * Remove custom coloring from all data labels, but leave the row and column names as they are
     */
    public void clearColor() {
        for (int i = 0; i < ARC4Con.S_BOX_LEN; i++) {
            vector[convertPosition(i)].setBackground(ColorService.LIGHTGRAY);
        }
    }


}
package org.jcryptool.visual.arc4.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.arc4.ARC4Con;
import org.jcryptool.visual.arc4.Messages;

/**
 * Provides the part of the UI that allows you to watch the internal variables of the algorithm
 * 
 * @author Luca Rupp
 */
public class VariablesVisual extends Composite {

    // The labeled box around the content
    private Group group;

    // The Labels that show the values
    private Label i, j, step, idec, jdec, ihex, jhex, stepvaluedec, stepvaluehex, dec, hex;
    
    /**
     * Constructor for the VariablesVisual class
     * 
     * @param parent the parent of this VariablesVisual
     * @param style this is ignored and only here due to the inheritance
     */
    public VariablesVisual(ARC4Composite parent, int style) {
        super(parent, SWT.NONE);
        GridLayout tlayout = new GridLayout(1, true);
        tlayout.marginHeight = 0;
        tlayout.marginWidth = 0;
        this.setLayout(tlayout);

        // one column for the labeling, one column for the values in decimal format and one column
        // for hexadecimal
        // format
        this.group = new Group(this, SWT.SHADOW_IN);
        this.group.setLayout(new GridLayout(ARC4Con.VAR_COL, true));
        this.group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        this.group.setText(Messages.VariablesVisualGroup);
        this.group.setToolTipText(Messages.VariablesVisualTool);

        // create the labels and assign them their text
        createContent();
    }

    /**
     * Create and style the labels that show the internal variables of the algorithm
     */
    private void createContent() {
        // a empty label to fill the first field of the grid-layout
        new Label(this.group, SWT.NONE);

        dec = new Label(this.group, SWT.CENTER);
        dec.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
        dec.setText(Messages.VariablesVisualDec);

        hex = new Label(this.group, SWT.CENTER);
        hex.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
        hex.setText(Messages.VariablesVisualHex);

        i = new Label(this.group, SWT.CENTER);
        i.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
        i.setText(Messages.VariablesVisualLabelI);

        // It is important to set a minimum width for idec, ihex, jdec, jhex and stepvalue; if you
        // do not,
        // the labels will be one character wide by default and will not become bigger by
        // themselves,
        // if their text is to long. You would possibly hunt for an mistake in the implementation
        // of the algorithm when in fact there is none and you do not see an incorrect result
        // displayed
        // in those labels, but rather only one character of the correct result

        // decimal value of i
        idec = new Label(this.group, SWT.CENTER);
        GridData idecData = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
        idecData.minimumWidth = ARC4Con.INST_MIN_IN_WIDTH;
        idec.setLayoutData(idecData);
        idec.setText("0");

        // hexadecimal value of i
        ihex = new Label(this.group, SWT.CENTER);
        GridData ihexData = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
        ihexData.minimumWidth = ARC4Con.INST_MIN_IN_WIDTH;
        ihex.setLayoutData(ihexData);
        ihex.setText("0");

        j = new Label(this.group, SWT.CENTER);
        j.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
        j.setText(Messages.VariablesVisualLabelJ);

        // decimal value of j
        jdec = new Label(this.group, SWT.CENTER);
        GridData jdecData = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
        jdecData.minimumWidth = ARC4Con.INST_MIN_IN_WIDTH;
        jdec.setLayoutData(jdecData);
        jdec.setText("0");

        // hexadecimal value of j
        jhex = new Label(this.group, SWT.CENTER);
        GridData jhexData = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
        jhexData.minimumWidth = ARC4Con.INST_MIN_IN_WIDTH;
        jhex.setLayoutData(jhexData);
        jhex.setText("0");

        step = new Label(this.group, SWT.CENTER);
        step.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
        step.setText(Messages.VariablesVisualLabelStep);

        // decimal value of step
        stepvaluedec = new Label(this.group, SWT.CENTER);
        GridData stepvaluedecData = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
        stepvaluedecData.minimumWidth = ARC4Con.INST_MIN_IN_WIDTH;
        stepvaluedec.setLayoutData(stepvaluedecData);
        stepvaluedec.setText("0");

        // hexadecimal value of step
        stepvaluehex = new Label(this.group, SWT.CENTER);
        GridData stepvaluehexData = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
        stepvaluehexData = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
        stepvaluehexData.minimumWidth = ARC4Con.INST_MIN_IN_WIDTH;
        stepvaluehex.setLayoutData(stepvaluehexData);
        stepvaluehex.setText("0");
    }

    /**
     * Set the content for the I label
     * 
     * @param num the value that will be displayed as I: in decimal number format
     */
    public void setI(int num) {
        idec.setText(Integer.toString(num));
        ihex.setText(Integer.toHexString(num));
    }

    /**
     * Set the number for the J label
     * 
     * @param num the value that will be displayed as J: in decimal number format
     */
    public void setJ(int num) {
        jdec.setText(Integer.toString(num));
        jhex.setText(Integer.toHexString(num));
    }

    /**
     * Set the number for the step label
     * 
     * @param num the value that will be displayed as step: in decimal number format
     */
    public void setStep(int num) {
        stepvaluedec.setText(Integer.toString(num));
        stepvaluehex.setText(Integer.toHexString(num));
    }

}
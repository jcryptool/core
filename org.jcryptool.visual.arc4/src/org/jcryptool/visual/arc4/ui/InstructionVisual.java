package org.jcryptool.visual.arc4.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.arc4.ARC4Con;
import org.jcryptool.visual.arc4.Messages;
import org.jcryptool.visual.library.Lib;

/**
 * Provides the part of the interface, that allows you to control the algorithm
 * 
 * @author Luca Rupp
 */
public class InstructionVisual extends Composite {

    // The labeled box around the content
    private Group group;

    private Label label;

    // The input field for the number of steps
    private Text input;

    // The buttons for executing steps and finishing the algorithm
    private Button button, finish;

    // Is needed to make the connection to execute steps of the algorithm
    private ARC4Composite parent;

    /**
     * The constructor for the instruction visual
     * 
     * @param parent the parent of this composite
     * @param style is ignored, this is just here due to the inheritance
     */
    public InstructionVisual(ARC4Composite parent, int style) {
        super(parent, SWT.NONE);
        this.parent = parent;
        // The instruction composite has just one child: the group
        GridLayout tlayout = new GridLayout(1, true);
        tlayout.marginHeight = 0;
        tlayout.marginWidth = 0;
        this.setLayout(tlayout);
        this.group = new Group(this, SWT.SHADOW_IN);
        this.group.setLayout(new GridLayout(2, true));
        this.group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        this.group.setText(Messages.InstructionVisualGroup);
        this.group.setToolTipText(Messages.InstructionVisualTool);
        // create the contents of the group
        createContent();
    }

    /**
     * Create and style the labels and buttons and add the functionality to the button
     */
    private void createContent() {
        label = new Label(this.group, SWT.CENTER);
        label.setText(Messages.InstructionVisualLabel);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

        // it is important to specify a minimum width for this text field, otherwise it will be one
        // character
        // wide and if you write something longer than one character into it, you will only see one
        // of the
        // characters that you typed, as the field will not make itself bigger automatically
        input = new Text(this.group, SWT.CENTER);
        // default number of steps
        input.setText(Integer.toString(ARC4Con.DEFAULT_STEPS));
        input.setToolTipText(Messages.InstructionVisualInputTool);
        GridData gd = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
        gd.minimumWidth = ARC4Con.INST_MIN_IN_WIDTH;
        input.setLayoutData(gd);
        // only digits are allowed as input; you are not able to specify negative numbers, as "-" is
        // not allowed
        input.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                // do not allow by default
                e.doit = false;
                // allow backspace and delete
                switch (e.keyCode) {
                case SWT.DEL:
                case SWT.BS:
                    e.doit = true;
                    break;
                default:
                    break;
                }
                // allow digits
                if (e.text.matches(Lib.DIGIT)) {
                    e.doit = true;
                }
            }
        });

        // create the execute button
        button = new Button(this.group, SWT.CENTER);
        button.setText(Messages.InstructionVisualButton);
        button.setToolTipText(Messages.InstructionVisualButtonTool);
        button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }

            @Override
            public void mouseUp(MouseEvent e) {
            }

            @Override
            public void mouseDown(MouseEvent e) {
                // in case the user clears the input field and presses the button
                try {
                    String temp = input.getText().replaceAll("\\s+", "");
                    parent.performSteps(Integer.parseInt(temp));
                } catch (NumberFormatException nfe) {
                    // just do nothing; no action needed
                    return;
                }
            }

        });

        // create the finish button
        finish = new Button(this.group, SWT.CENTER);
        finish.setText(Messages.InstuctionVisualFinish);
        finish.setToolTipText(Messages.InstructionVisualFinishTool);
        finish.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        finish.addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }

            @Override
            public void mouseUp(MouseEvent e) {
            }

            @Override
            public void mouseDown(MouseEvent e) {
                parent.performSteps(ARC4Con.S_BOX_LEN + ARC4Con.DATAVECTOR_VISUAL_LENGTH);
            }
        });

    }

}
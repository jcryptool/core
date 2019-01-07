//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.arc4.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
        setLayout(tlayout);
        
        group = new Group(this, SWT.SHADOW_IN);
        group.setLayout(new GridLayout(2, true));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        group.setText(Messages.InstructionVisualGroup);
        group.setToolTipText(Messages.InstructionVisualTool);
        // create the contents of the group
        createContent();
    }

    /**
     * Create and style the labels and buttons and add the functionality to the button
     */
    private void createContent() {
        label = new Label(group, SWT.CENTER);
        label.setText(Messages.InstructionVisualLabel);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

        input = new Text(group, SWT.LEFT |SWT.BORDER);
        // default number of steps
        input.setText(Integer.toString(ARC4Con.DEFAULT_STEPS));
        input.setToolTipText(Messages.InstructionVisualInputTool);
        input.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        // only digits are allowed as input; you are not able to specify negative numbers, as "-" is
        // not allowed
		input.addVerifyListener(new VerifyListener() {
			@Override
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
        button = new Button(group, SWT.PUSH);
        button.setText(Messages.InstructionVisualButton);
        button.setToolTipText(Messages.InstructionVisualButtonTool);
        button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
                try {
                    String temp = input.getText().replaceAll("\\s+", "");
                    parent.performSteps(Integer.parseInt(temp));
                } catch (NumberFormatException nfe) {
                    // just do nothing; no action needed
                    return;
                }
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});

        // create the finish button
        finish = new Button(group, SWT.PUSH);
        finish.setText(Messages.InstuctionVisualFinish);
        finish.setToolTipText(Messages.InstructionVisualFinishTool);
        finish.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        finish.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				parent.performSteps(ARC4Con.S_BOX_LEN + ARC4Con.DATAVECTOR_VISUAL_LENGTH);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});

    }

}
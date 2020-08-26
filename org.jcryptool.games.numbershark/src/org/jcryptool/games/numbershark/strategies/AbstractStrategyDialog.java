//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.games.numbershark.strategies;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;

/**
 * Settings dialog for the calculation of the optimal strategies
 * 
 * @author Johannes Spaeth
 * @version 0.9.5
 */
public class AbstractStrategyDialog extends TitleAreaDialog {
    private int minSelected = 2;
    private int maxSelected = 40;
    protected int selectedStrategy;
	private Label lblNumOfPlayingFieldsNumber;
	private Spinner minValueSpinner;
	private Slider maxValueSlider;
	private Label lblMinimalValue;
	private Spinner maxValueSpinner;
	private Label lblMaximalValue;
	private Label lblNumOfPlayingFields;
	private Slider minValueSlider;
	private Label longRunningProcessWarning;
	private boolean showWarning;

    public AbstractStrategyDialog(Shell shell) {
        super(shell);
        setShellStyle(SWT.TITLE | SWT.APPLICATION_MODAL | SWT.CLOSE);
    }

    protected Group createSliders(Composite parent, final boolean showWarning, int max, int defaultSelection) {   	
    	this.showWarning = true;
    	
        final Group groupSliders = new Group(parent, SWT.NONE);
        GridData gd_groupSliders = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_groupSliders.widthHint = 200;
        groupSliders.setLayoutData(gd_groupSliders);
        groupSliders.setText(Messages.OptStratDialog_8);
        groupSliders.setLayout(new GridLayout(3, false));

        minValueSlider = new Slider(groupSliders, SWT.NONE);
        minValueSlider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        minValueSlider.setValues(2, 2, max, 1, 1, 10);

        minValueSpinner = new Spinner(groupSliders, SWT.NONE);
        minValueSpinner.setValues(2, 2, max, 0, 1, 10);

        lblMinimalValue = new Label(groupSliders, SWT.NONE);
        lblMinimalValue.setText(Messages.OptStratDialog_3);
        
        maxValueSlider = new Slider(groupSliders, SWT.NONE);
        maxValueSlider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        maxValueSlider.setValues(defaultSelection, 2, max, 1, 1, 10);

        maxValueSpinner = new Spinner(groupSliders, SWT.NONE);
        maxValueSpinner.setValues(defaultSelection, 2, max, 0, 1, 10);

        lblMaximalValue = new Label(groupSliders, SWT.NONE);
        lblMaximalValue.setText(Messages.OptStratDialog_4);

        lblNumOfPlayingFields = new Label(groupSliders, SWT.LEFT);
        lblNumOfPlayingFields.setText(Messages.AbstStratDialog_0);
        
        lblNumOfPlayingFieldsNumber = new Label(groupSliders, SWT.CENTER);
        lblNumOfPlayingFieldsNumber.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        lblNumOfPlayingFieldsNumber.setText("\t" + String.valueOf(defaultSelection - 1));
        
        longRunningProcessWarning = new Label(parent, SWT.NONE);
        longRunningProcessWarning.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        longRunningProcessWarning.setText(Messages.OptStratDialog_6);
        longRunningProcessWarning.setVisible(false);

        maxValueSpinner.addModifyListener(new ModifyListener() {
            @Override
			public void modifyText(ModifyEvent e) {
                int max = maxValueSpinner.getSelection();
                maxValueSlider.setSelection(max);
                maxSelected = max;
                int min = minValueSlider.getSelection();
                int value = max - min + 1;
                reflectMinMaxSettings(minSelected, maxSelected);
            }

        });

        minValueSpinner.addModifyListener(new ModifyListener() {
            @Override
			public void modifyText(ModifyEvent e) {
                int min = minValueSpinner.getSelection();
                minValueSlider.setSelection(min);
                int max = maxValueSlider.getSelection();
                int value = max - min + 1;
                minSelected = min;
                reflectMinMaxSettings(minSelected, maxSelected);
            }

        });

        maxValueSlider.addListener(SWT.Selection, new Listener() {
            @Override
			public void handleEvent(Event e) {
                int max = maxValueSlider.getSelection();
                maxValueSpinner.setSelection(max);
                maxSelected = max;

                int min = minValueSlider.getSelection();
                int value = max - min + 1;
                reflectMinMaxSettings(minSelected, maxSelected);
            }
        });

        minValueSlider.addListener(SWT.Selection, new Listener() {
            @Override
			public void handleEvent(Event e) {
                int min = minValueSlider.getSelection();
                minValueSpinner.setSelection(min);
                int max = maxValueSlider.getSelection();
                int value = max - min + 1;
                minSelected = min;
                reflectMinMaxSettings(minSelected, maxSelected);
            }
        });
        
        reflectMinMaxSettings(minSelected, maxSelected);

        return groupSliders;
    }
    
    private void reflectMinMaxSettings(int min, int max) {
    	lblNumOfPlayingFieldsNumber.setText(String.valueOf(max-min+1));
    	if (max < min || max < minValueSpinner.getSelection()) {
    		minValueSlider.setSelection(max);
    		minValueSpinner.setSelection(max);
    	}
    	if (max > 200 && showWarning) {
    		longRunningProcessWarning.setVisible(true);
    	} else {
    		longRunningProcessWarning.setVisible(false);
    	}
    	
    }

    public int getMin() {
        return minSelected;
    }

    public int getMax() {
        return maxSelected;
    }

    public int getSelectedStrategy() {
        return selectedStrategy;
    }
}

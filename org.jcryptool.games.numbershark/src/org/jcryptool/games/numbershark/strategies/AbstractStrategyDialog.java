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
package org.jcryptool.games.numbershark.strategies;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
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
    private int maxSelected = 100;
    protected int selectedStrategy;

    public AbstractStrategyDialog(Shell shell) {
        super(shell);
        setShellStyle(SWT.TITLE | SWT.APPLICATION_MODAL);
    }

    protected Group createSliders(Composite parent, final boolean showWarning, int max, int defaultSelection) {

        final Group compositeSliders = new Group(parent, SWT.NONE);
        GridData gd_compositeSliders = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_compositeSliders.verticalIndent = 5;
        gd_compositeSliders.widthHint = 430;
        gd_compositeSliders.horizontalIndent = 27;
        compositeSliders.setLayoutData(gd_compositeSliders);
        compositeSliders.setText(Messages.OptStratDialog_8);
        GridLayout gl_compositeSliders = new GridLayout(3, false);
        gl_compositeSliders.marginTop = 5;
        gl_compositeSliders.marginLeft = 2;
        compositeSliders.setLayout(gl_compositeSliders);

        final Slider minValueSlider = new Slider(compositeSliders, SWT.NONE);

        minValueSlider.setValues(2, 2, max, 1, 1, 10);

        final Spinner minValueSpinner = new Spinner(compositeSliders, SWT.NONE);
        minValueSpinner.setValues(2, 2, max, 0, 1, 10);

        Label lblMinimalValue = new Label(compositeSliders, SWT.NONE);
        lblMinimalValue.setText(Messages.OptStratDialog_3);
        final Slider maxValueSlider = new Slider(compositeSliders, SWT.NONE);
        maxValueSlider.setValues(defaultSelection, 2, max, 1, 1, 10);

        final Spinner maxValueSpinner = new Spinner(compositeSliders, SWT.NONE);
        maxValueSpinner.setValues(defaultSelection, 2, max, 0, 1, 10);

        Label lblMaximalValue = new Label(compositeSliders, SWT.NONE);
        lblMaximalValue.setText(Messages.OptStratDialog_4);

        Label lblNumOfPlayingFields = new Label(compositeSliders, SWT.NONE);
        lblNumOfPlayingFields.setText(Messages.AbstStratDialog_0);
        GridData gd_numOfPlayFields = new GridData();
        gd_numOfPlayFields.horizontalSpan = 2;
        lblNumOfPlayingFields.setLayoutData(gd_numOfPlayFields);
        final Label lblNumOfPlayingFieldsNumber = new Label(compositeSliders, SWT.RIGHT);
        lblNumOfPlayingFieldsNumber.setText("\t" + String.valueOf(defaultSelection - 1));
        final Label longRunningProcessWarning = new Label(parent, SWT.NONE);
        GridData gd_longRunningProcessWarning = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        gd_longRunningProcessWarning.verticalIndent = 5;
        gd_longRunningProcessWarning.horizontalIndent = 37;
        longRunningProcessWarning.setLayoutData(gd_longRunningProcessWarning);
        longRunningProcessWarning.setText(Messages.OptStratDialog_6);
        longRunningProcessWarning.setVisible(false);

        maxValueSpinner.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                int max = maxValueSpinner.getSelection();
                maxValueSlider.setSelection(max);
                maxSelected = max;
                int min = minValueSlider.getSelection();
                int value = max - min + 1;
                lblNumOfPlayingFieldsNumber.setText(String.valueOf(value));
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

        });

        maxValueSlider.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                int max = maxValueSlider.getSelection();
                maxValueSpinner.setSelection(max);
                maxSelected = max;

                int min = minValueSlider.getSelection();
                int value = max - min + 1;
                lblNumOfPlayingFieldsNumber.setText(String.valueOf(value));
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
        });

        minValueSlider.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                int min = minValueSlider.getSelection();
                minValueSpinner.setSelection(min);
                int max = maxValueSlider.getSelection();
                int value = max - min + 1;
                lblNumOfPlayingFieldsNumber.setText(String.valueOf(value));
                if (min > max || min > maxValueSpinner.getSelection()) {
                    maxValueSlider.setSelection(min);
                    maxValueSpinner.setSelection(min);
                }
                minSelected = min;
            }
        });
        minValueSpinner.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                int min = minValueSpinner.getSelection();
                minValueSlider.setSelection(min);
                int max = maxValueSlider.getSelection();
                int value = max - min + 1;
                lblNumOfPlayingFieldsNumber.setText(String.valueOf(value));
                if (min > max || min > maxValueSpinner.getSelection()) {
                    maxValueSlider.setSelection(min);
                    maxValueSpinner.setSelection(min);
                }
                minSelected = min;
            }

        });
        return compositeSliders;
    }

    @Override
    protected Point getInitialSize() {
        return new Point(500, 350);
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
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

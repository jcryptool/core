// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.numbershark.optStrat;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.games.numbershark.NumberSharkPlugin;

/**
 * Settings dialog for the calculation of the optimal strategies
 * @author Johannes Spaeth
 * @version 0.9.5
 */
public class OptimalStrategyDialog extends TitleAreaDialog {
	private int minSelected = 2;
	private int maxSelected = 100;
	private boolean calculateStrategy = true;

	public OptimalStrategyDialog(Shell shell) {
		super(shell);
		setShellStyle(SWT.TITLE | SWT.APPLICATION_MODAL);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(Messages.OptStratDialog_0);
		setMessage(Messages.OptStratDialog_5, IMessageProvider.INFORMATION);

		Composite area = (Composite) super.createDialogArea(parent);

		Composite composite = new Composite(area, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1);
		gd_composite.widthHint = 470;
		composite.setLayoutData(gd_composite);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginTop = 15;
		gl_composite.marginLeft = 25;
		composite.setLayout(gl_composite);

		Button calcButton = new Button(composite, SWT.RADIO);
		calcButton.setText(Messages.OptStratDialog_1);

	
		Button showButton = new Button(composite, SWT.RADIO);
		showButton.setText(Messages.OptStratDialog_2);

		

		final Composite compositeSliders = new Composite(area, SWT.NONE);
		GridData gd_compositeSliders = new GridData(SWT.FILL, SWT.TOP,
				true , true, 1, 1);
		compositeSliders.setLayoutData(gd_compositeSliders);
		GridLayout gl_compositeSliders = new GridLayout(3, false);
		gl_compositeSliders.marginTop = 20;
		gl_compositeSliders.marginLeft = 25;
		compositeSliders.setLayout(gl_compositeSliders);

		final Slider minValueSlider = new Slider(compositeSliders, SWT.NONE);

		minValueSlider.setValues(2, 2, 400, 1, 1, 10);


		final Spinner minValueSpinner = new Spinner(compositeSliders, SWT.NONE);
		minValueSpinner.setValues(2, 2, 400, 0, 1, 10);


		Label lblMinimalValue = new Label(compositeSliders, SWT.NONE);
		lblMinimalValue.setText(Messages.OptStratDialog_3);
		final Slider maxValueSlider = new Slider(compositeSliders, SWT.NONE);
		maxValueSlider.setValues(100, 2, 400, 1, 1, 10);


		final Spinner maxValueSpinner = new Spinner(compositeSliders, SWT.NONE);
		maxValueSpinner.setValues(100, 2, 400, 0, 1, 10);
		
		Label lblMaximalValue = new Label(compositeSliders, SWT.NONE);
		lblMaximalValue.setText(Messages.OptStratDialog_4);

		
		final Label longRunningProcessWarning = new Label(compositeSliders, SWT.NONE);
		longRunningProcessWarning.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true , true, 3 , 1));
		longRunningProcessWarning.setText(Messages.OptStratDialog_6);
		longRunningProcessWarning.setVisible(false);
		
		calcButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				calculateStrategy = true;
				compositeSliders.setVisible(true);
			}
		});
		
		showButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				calculateStrategy = false;
				compositeSliders.setVisible(false);
			}
		});
		
		maxValueSpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				int max = maxValueSpinner.getSelection();
				maxValueSlider.setSelection(max);
				maxSelected = max;
				if(max > 200){
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
				if(max > 200){
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
				maxValueSlider.setMinimum(min);
				maxValueSpinner.setMinimum(min);
				minSelected = min;
			}
		});
		minValueSpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				int min = minValueSlider.getSelection();
				maxValueSlider.setMinimum(min);
				maxValueSpinner.setMinimum(min);
				minValueSlider.setSelection(min);
				minSelected = min;
			}

		});
		

		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(parent, NumberSharkPlugin.PLUGIN_ID + ".optStratDialog"); //$NON-NLS-1$

		return area;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 350);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, true);
	}

	public int getMin() {
		return minSelected;
	}

	public int getMax() {
		return maxSelected;
	}

	public boolean getCalculateStrategy() {
		return calculateStrategy;
	}
}

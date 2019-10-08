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
package org.jcryptool.analysis.freqanalysis.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.analysis.freqanalysis.calc.FreqAnalysisCalc;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.crypto.ui.textmodify.wizard.ModifyWizard;

/**
 * @author SLeischnig
 *
 */
public class SimpleAnalysisUI extends AbstractAnalysisUI {
	private Button button;
	private Button button0;
	private Button button1;
	private Composite composite0;
	private Composite composite1;
	private Group group1;
	private Label label1;
	private Button button2;
	private Button button4;
	private Composite composite2;
	private Group group4;
	private Label label2;
	private Group group2;

	private FreqAnalysisCalc myAnalysis;
	TransformData myModifySettings;

	public SimpleAnalysisUI(final Composite parent, final int style) {
		super(parent, style);
		initGUI();
		hideObject(group2, button3.getSelection());

		myModifySettings = new TransformData();
		myModifySettings.setUnmodified();
	}

	private void initGUI() {
		setLayout(new GridLayout());

		composite0 = new Composite(this, SWT.NONE);
		composite0.setLayout(new GridLayout(2, false));
		composite0.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		button = new Button(composite0, SWT.NONE);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		button.setText(Messages.FullAnalysisUI_loadtext);
		
		button0 = new Button(composite0, SWT.NONE);
		button0.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		button0.setText(Messages.FullAnalysisUI_loadeditor);
		
		button1 = new Button(this, SWT.PUSH | SWT.CENTER);
		button1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		button1.setText(Messages.FullAnalysisUI_startanalysis);
		button1.setEnabled(false);
				
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				try {
					Display display = Display.getDefault();
					Shell dialogShell = new Shell(display, SWT.APPLICATION_MODAL);
					FileDialog fd_ChooseFile = new FileDialog(dialogShell, SWT.OPEN);
					fd_ChooseFile.setFilterPath("\\"); //$NON-NLS-1$
					fd_ChooseFile.setFilterExtensions(new String[] { "*.txt" }); //$NON-NLS-1$
					File file_LoadReferenceText = new File(fd_ChooseFile.open());
					BufferedReader br = new BufferedReader(new FileReader(file_LoadReferenceText)); 
					text = new String();
					String line;
					while ((line = br.readLine()) != null) {
						text += line;	
					}
					
					if (text == "") {
						throw new Exception();
					}
					button1.setEnabled(true);
				} catch (Exception ex) {
		        	MessageDialog.openInformation(getShell(), Messages.AbstractAnalysisUI_0, Messages.AbstractAnalysisUI_2);
				}
			}
		});
		
		button0.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				if (checkEditor()) {
					text = getEditorText();	
					button1.setEnabled(true);
				}
			}
		});
			
		button1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				if (text.equals("") || text == null) {
		        	MessageDialog.openInformation(getShell(), Messages.AbstractAnalysisUI_0, Messages.AbstractAnalysisUI_2);
				} else {
					recalcGraph();
				}
			}
		});
		
//		button1 = new Button(this, SWT.PUSH | SWT.CENTER);
//		button1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
//		button1.setText(Messages.SimpleAnalysisUI_startanalysis);
//		button1.addMouseListener(new MouseAdapter() {
//
//			@Override
//			public void mouseDown(MouseEvent evt) {
//				// ----------------- Begin of Handler
//				// Main Function Button
//				if (checkEditor()) {
//					text = getEditorText();
//					recalcGraph();
//				}
//
//				// ----------------- End of Handler
//			}
//		});
		
		composite1 = new Composite(this, SWT.NONE);
		composite1.setLayout(new GridLayout(2, false));
		composite1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		group1 = new Group(composite1, SWT.NONE);
		group1.setLayout(new GridLayout());
		group1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group1.setText(Messages.SimpleAnalysisUI_graphlabel);

		myGraph = new CustomFreqCanvas(group1, SWT.NONE);
		myGraph.setLayout(new GridLayout());
		myGraph.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		group4 = new Group(composite1, SWT.NONE);
		group4.setLayout(new GridLayout());
		group4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		group4.setText(Messages.SimpleAnalysisUI_properties);

		composite2 = new Composite(group4, SWT.NONE);
		composite2.setLayout(new GridLayout());
		composite2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		button3 = new Button(composite2, SWT.RADIO);
		button3.setText(Messages.SimpleAnalysisUI_monoalphabetic);
		button3.setSelection(true);
		button3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				polyOnOffSelected(evt);
			}
		});

		button4 = new Button(composite2, SWT.RADIO);
		button4.setText(Messages.SimpleAnalysisUI_polyalphabetic);
		button4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				polyOnOffSelected(evt);
			}
		});

		group2 = new Group(group4, SWT.NONE);
		group2.setLayout(new GridLayout(2, false));
		group2.setText(Messages.SimpleAnalysisUI_vigeneresettings);

		spinner1 = new Spinner(group2, SWT.NONE);
		spinner1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent evt) {
				recalcGraph();
			}
		});

		spinner1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				changedVigOptions();
			}
		});
		spinner1.setSelection(1);

		label1 = new Label(group2, SWT.NONE);
		label1.setText(Messages.SimpleAnalysisUI_keylength);

		spinner2 = new Spinner(group2, SWT.NONE);
		spinner2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent evt) {
				recalcGraph();
			}
		});

		spinner2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				changedVigOptions();
			}
		});
		spinner2.setSelection(0);

		label2 = new Label(group2, SWT.NONE);
		label2.setText(Messages.SimpleAnalysisUI_offset);

		button2 = new Button(group4, SWT.PUSH | SWT.CENTER);
		button2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		button2.setText(Messages.SimpleAnalysisUI_filtersettings);
		button2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent evt) {
				myModifySettings = getTransformWizardSettings(myModifySettings);
				recalcGraph();
			}
		});

		layout();
	}

	/**
	 * executes a TextModify Wizard.
	 * 
	 * @param the
	 *            Settings that have to be displayed at the beginning.
	 * @return the selected settings.
	 */
	private TransformData getTransformWizardSettings(final TransformData predefined) {

		ModifyWizard wizard = new ModifyWizard();
		wizard.setPredefinedData(predefined);
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setHelpAvailable(false);
		int result = dialog.open();

		if (result == 0) {
			return wizard.getWizardData();
		} else {
			return predefined;
		}

	}

	/**
	 * frequency analysis main procedure
	 */
	@Override
	protected void analyze() {
		myAnalysis = new FreqAnalysisCalc(text, myLength, myOffset, myModifySettings);
		myGraph.setAnalysis(myAnalysis);
	}

	/**
	 * excludes a control from the Layout calculation
	 * 
	 * @param that
	 * @param hideit
	 */
	private void hideObject(final Control that, final boolean hideit) {
		GridData GData = (GridData) that.getLayoutData();
		// the (GData != null) test is to work around a NullPointerException (bug #95)
		// happening after one leaves the Frequency Analysis open
		// while closing down JCrypTool, then restarts JCrypTool
		if (GData != null) {
			GData.exclude = true && hideit;
		}
		that.setVisible(true && !hideit);
		Control[] myArray = { that };
		layout(myArray);
	}

	private void polyOnOffSelected(final SelectionEvent evt) {
		hideObject(group2, button3.getSelection());

	}

	private void changedVigOptions() {
		if (spinner1.getSelection() < 1) {
			spinner1.setSelection(1);
		}
		if (spinner2.getSelection() < 0) {
			spinner2.setSelection(0);
		}
		spinner1.setMinimum(1);
		spinner1.setMaximum(999999);
		spinner2.setMinimum(0);
		spinner2.setMaximum(spinner1.getSelection() - 1);
		if (spinner2.getSelection() >= spinner1.getSelection()) {
			spinner2.setSelection(spinner1.getSelection() - 1);
		}
		spinner2.setMaximum(spinner1.getSelection());
	}

	public final void execute(final int keyLength, final int keyPos, final boolean resetShift,
			final boolean executeCalc) {
		if (keyLength > 0) {
			if (keyLength == 1) {
				button3.setSelection(true);
				button4.setSelection(false);
			} else {
				button3.setSelection(false);
				button4.setSelection(true);
			}
			polyOnOffSelected(null);

			spinner1.setSelection(keyLength);
			changedVigOptions();
			if (keyPos < 0) {
				recalcGraph();
			}
		}
		if (keyPos > -1) {
			spinner2.setSelection(keyPos);
			changedVigOptions();
			recalcGraph();
		}

		if (resetShift) {
			myGraph.getFrequencyGraph().resetDrag();
		}
		if (executeCalc) {
			if (checkEditor()) {
				text = getEditorText();
				recalcGraph();
			}
		}

	}

}

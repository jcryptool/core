//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.freqanalysis.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.analysis.freqanalysis.FreqAnalysisPlugin;
import org.jcryptool.analysis.freqanalysis.calc.FreqAnalysisCalc;
import org.jcryptool.analysis.textmodify.wizard.ModifyWizard;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.constants.IConstants;

/**
 * @author SLeischnig
 *
 */
public class SimpleAnalysisUI extends org.eclipse.swt.widgets.Composite {
	private Button button1;
	private Composite composite1;
	private Group group1;
	private Label label1;
	private Button button2;
	private Spinner spinner2;
	private Spinner spinner1;
	private Button button4;
	private Button button3;
	private Composite composite2;
	private Group group4;
	private Label label2;
	private Group group2;
	private CustomFreqCanvas myGraph;

	private String text;
	private FreqAnalysisCalc myAnalysis;
	private int myLength, myOffset;
	TransformData myModifySettings;



	public SimpleAnalysisUI(final org.eclipse.swt.widgets.Composite parent, final int style) {
		super(parent, style);
		initGUI();
		hideObject(group2, button3.getSelection());

		myModifySettings = new TransformData();
		myModifySettings.setUnmodified();
	}

	private void initGUI() {
		try {
			GridLayout thisLayout = new GridLayout();
			thisLayout.makeColumnsEqualWidth = true;
			this.setLayout(thisLayout);
			this.setSize(604, 263);
			button1 = new Button(this, SWT.PUSH | SWT.CENTER);
			GridData button1LData = new GridData();
			button1LData.horizontalAlignment = GridData.FILL;
			button1LData.grabExcessHorizontalSpace = true;
			button1.setLayoutData(button1LData);
			button1.setText(Messages.SimpleAnalysisUI_startanalysis);
			button1.addMouseListener(new MouseAdapter() {

				public void mouseDown(MouseEvent evt) {
					//----------------- Begin of Handler
					// Main Function Button
					if(checkEditor())
					{
						text = getEditorText();
						recalcGraph();
					}

					//----------------- End of Handler
				}
			});
			composite1 = new Composite(this, SWT.NONE);
			GridLayout composite1Layout = new GridLayout();
			composite1Layout.numColumns = 2;
			composite1Layout.marginWidth = 0;
			composite1Layout.marginHeight = 0;
			GridData composite1LData = new GridData();
			composite1LData.grabExcessHorizontalSpace = true;
			composite1LData.horizontalAlignment = GridData.FILL;
			composite1LData.verticalAlignment = GridData.FILL;
			composite1LData.grabExcessVerticalSpace = true;
			composite1.setLayoutData(composite1LData);
			composite1.setLayout(composite1Layout);
			{
				group1 = new Group(composite1, SWT.NONE);
				GridLayout group1Layout = new GridLayout();
				group1.setLayout(group1Layout);
				GridData group1LData = new GridData();
				group1LData.horizontalAlignment = GridData.FILL;
				group1LData.grabExcessHorizontalSpace = true;
				group1LData.verticalAlignment = GridData.FILL;
				group1LData.grabExcessVerticalSpace = true;
				group1.setLayoutData(group1LData);
				group1.setText(Messages.SimpleAnalysisUI_graphlabel);
				{
					myGraph = new CustomFreqCanvas(group1, SWT.NONE);

					GridLayout myGraphLayout = new GridLayout();
					myGraph.setLayout(myGraphLayout);
					GridData myGraphLData = new GridData();
					myGraphLData.verticalAlignment = GridData.FILL;
					myGraphLData.grabExcessVerticalSpace = true;
					myGraphLData.grabExcessHorizontalSpace = true;
					myGraphLData.horizontalAlignment = GridData.FILL;
					myGraph.setLayoutData(myGraphLData);
				}
			}
			{
				group4 = new Group(composite1, SWT.NONE);
				GridLayout group4Layout = new GridLayout();
				group4.setLayout(group4Layout);
				GridData group4LData = new GridData();
				group4LData.horizontalAlignment = GridData.FILL;
				group4LData.grabExcessVerticalSpace = true;
				group4LData.verticalAlignment = GridData.FILL;
				group4.setLayoutData(group4LData);
				group4.setText(Messages.SimpleAnalysisUI_properties);
				{
					composite2 = new Composite(group4, SWT.NONE);
					GridLayout composite2Layout = new GridLayout();
					composite2Layout.makeColumnsEqualWidth = true;
					composite2Layout.marginHeight = 0;
					GridData composite2LData = new GridData();
					composite2LData.horizontalAlignment = GridData.FILL;
					composite2LData.verticalAlignment = GridData.FILL;
					composite2.setLayoutData(composite2LData);
					composite2.setLayout(composite2Layout);
					{
						button3 = new Button(composite2, SWT.RADIO | SWT.LEFT);
						button3.setText(Messages.SimpleAnalysisUI_monoalphabetic);
						button3.setSelection(true);
						button3.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt) {
								polyOnOffSelected(evt);
							}
						});
					}
					{
						button4 = new Button(composite2, SWT.RADIO | SWT.LEFT);
						button4.setText(Messages.SimpleAnalysisUI_polyalphabetic);
						button4.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt) {
								polyOnOffSelected(evt);
							}
						});
					}
				}
				{
					group2 = new Group(group4, SWT.NONE);
					GridLayout group2Layout = new GridLayout();
					group2Layout.numColumns = 2;
					group2.setLayout(group2Layout);
					group2.setText(Messages.SimpleAnalysisUI_vigeneresettings);
					{
						GridData spinner1LData = new GridData();
						spinner1 = new Spinner(group2, SWT.NONE);
						spinner1.setLayoutData(spinner1LData);
						spinner1.addMouseListener(new MouseAdapter() {
							public void mouseDown(MouseEvent evt) {
								recalcGraph();
							}
						});
						spinner1.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt) {
								changedVigOptions();
							}
						});
						spinner1.setSelection(1);
					}
					{
						label1 = new Label(group2, SWT.NONE);
						label1.setLayoutData(new GridData());
						label1.setText(Messages.SimpleAnalysisUI_keylength);
					}
					{
						GridData spinner2LData = new GridData();
						spinner2 = new Spinner(group2, SWT.NONE);
						spinner2.setLayoutData(spinner2LData);
						spinner2.addMouseListener(new MouseAdapter() {
							public void mouseDown(MouseEvent evt) {
								recalcGraph();
							}
						});
						spinner2.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt) {
								changedVigOptions();
							}
						});
						spinner2.setSelection(0);
					}
					{
						label2 = new Label(group2, SWT.NONE);
						label2.setLayoutData(new GridData());
						label2.setText(Messages.SimpleAnalysisUI_offset);
					}
				}
				{
					button2 = new Button(group4, SWT.PUSH | SWT.CENTER);
					GridData button2LData = new GridData();
					button2LData.grabExcessHorizontalSpace = true;
					button2LData.horizontalAlignment = GridData.FILL;
					button2.setLayoutData(button2LData);
					button2.setText(Messages.SimpleAnalysisUI_filtersettings);
					button2.addMouseListener(new MouseAdapter() {
						public void mouseDown(MouseEvent evt) {
							myModifySettings = getTransformWizardSettings(myModifySettings);
							recalcGraph();
						}
					});
				}
			}
			this.layout();
		} catch (Exception e) {
			LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e);
		}
	}

	/** executes a TextModify Wizard.
	 * @param the Settings that have to be displayed at the beginning.
	 * @return the selected settings.
	 */
	private TransformData getTransformWizardSettings(final TransformData predefined)
	{

		ModifyWizard wizard = new ModifyWizard();
		wizard.setPredefinedData(predefined);
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		int result = dialog.open();

		if(result==0) {
			return wizard.getWizardData();
		} else {
			return predefined;
		}

	}

	/**
	 * takes the input control's values and sets the final analysis parameters
	 */
	private void setFinalVigParameters()
	{
		myLength = 1; if(! button3.getSelection()) {
			myLength = spinner1.getSelection();
		}
		myOffset = 0; if(! button3.getSelection()) {
			myOffset = spinner2.getSelection();
		}
	}

	/**
	 * frequency analysis main procedure
	 */
	private void analyze()
	{
		myAnalysis = new FreqAnalysisCalc(text, myLength, myOffset, myModifySettings);
		myGraph.setAnalysis(myAnalysis);
	}

	/**
	 * rebuilds the frequency analysis graph
	 */
	private void recalcGraph()
	{
		if(checkEditor() && text != null)
		{
			setFinalVigParameters();
			analyze();
			myGraph.redraw();
		}
	}

	/**checks, whether an editor is opened or not.
	 */
	private boolean checkEditor()
	{
		InputStream stream = EditorsManager.getInstance().getActiveEditorContentInputStream();
		if(stream==null) {
			return false;
		} return true;
	}

	/** get the text from an opened editor
	 */
	private String getEditorText()
	{
		String text=""; //$NON-NLS-1$
		InputStream stream = EditorsManager.getInstance().getActiveEditorContentInputStream();
		text = InputStreamToString(stream);
		return text;
	}

	/**
     * reads the current value from an input stream
     *
     * @param in the input stream
     */
    private String InputStreamToString(InputStream in) {
    	BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, IConstants.UTF8_ENCODING));
		} catch (UnsupportedEncodingException e1) {
			LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e1);
		}

    	StringBuffer myStrBuf = new StringBuffer();
        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = reader.read()) != -1) {
            	myStrBuf.append(String.valueOf((char) charOut));
            }
        } catch (IOException e) {
            LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }

	/** excludes a control from the Layout calculation
	 * @param that
	 * @param hideit
	 */
	private void hideObject(final Control that, final boolean hideit)
	{
		GridData GData  = (GridData) that.getLayoutData();
		GData.exclude = true && hideit;
		that.setVisible(true && !hideit);
		Control[] myArray = {that};
		layout(myArray);
	}

	private void polyOnOffSelected(final SelectionEvent evt) {
		hideObject(group2, button3.getSelection());

	}

	private void changedVigOptions() {
		if(spinner1.getSelection() < 1) {
			spinner1.setSelection(1);
		} if(spinner2.getSelection() < 0) {
			spinner2.setSelection(0);
		}
		spinner1.setMinimum(1); spinner1.setMaximum(999999);
		spinner2.setMinimum(0); spinner2.setMaximum(spinner1.getSelection()-1);
		if(spinner2.getSelection() >= spinner1.getSelection()) {
			spinner2.setSelection(spinner1.getSelection()-1);
		}
		spinner2.setMaximum(spinner1.getSelection());
	}

	public final void execute(final int keyLength, final int keyPos, final boolean resetShift, final boolean executeCalc) {
		if(keyLength > 0)
		{
			if(keyLength == 1) {
				button3.setSelection(true); button4.setSelection(false); }
			else {
				button3.setSelection(false); button4.setSelection(true); }
			polyOnOffSelected(null);

			spinner1.setSelection(keyLength);
			changedVigOptions();
			if(keyPos < 0) {
				recalcGraph();
			}
		}
		if(keyPos > -1)
		{
			spinner2.setSelection(keyPos); changedVigOptions(); recalcGraph();
		}

		if(resetShift) {
			myGraph.getFrequencyGraph().resetDrag();
		}
		if(executeCalc)
		{
			if(checkEditor())
			{	text = getEditorText();
				recalcGraph(); }
		}

	}

}

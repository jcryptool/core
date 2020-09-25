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
package org.jcryptool.analysis.transpositionanalysis.ui.wizards.autoanalysiswizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisPadding;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisPaddingInput;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class PaddingAnalysisPage extends SingleAnalysisPage implements TranspositionAnalysisPaddingInput {
	private Label labelTest;
	private Group compPaddingSelect;
	private Label labelPaddingSelect;
	private Text textPaddingSelector;
	private Label labelAutoDescription;
	private Button buttonAutoDetect;
	private Label separatorAutoExplanation;

	public PaddingAnalysisPage(TranspositionAnalysisPadding analysis) {
		super(analysis);
	}

	@Override
	protected void createMainControls(Composite parent) {
		{
			labelTest = new Label(parent, SWT.WRAP);
			GridData labelTestLData = new GridData();
			labelTestLData.horizontalAlignment = GridData.FILL;
			labelTestLData.grabExcessHorizontalSpace = true;
			labelTestLData.widthHint = 350;
			labelTest.setLayoutData(labelTestLData);
			labelTest.setText(
					"The padding of a cipher is a filling at the end of the text, used for fitting the blocklength. A padding often consists of just easily-distinguishable character, like '#', '0'."
							+ "\nPlease try to find the padding characters in the end of the cipher. Example: In 'biu##dl#', mark the last five characters.");
		}
		{
			compPaddingSelect = new Group(parent, SWT.NONE);
			GridLayout compPaddingSelectLayout = new GridLayout();
			GridData compPaddingSelectLData = new GridData();
			compPaddingSelectLayout.numColumns = 4;
			compPaddingSelectLayout.makeColumnsEqualWidth = false;
			compPaddingSelectLData.grabExcessHorizontalSpace = true;
			compPaddingSelectLData.horizontalAlignment = SWT.FILL;
			compPaddingSelectLData.verticalIndent = 5;
			compPaddingSelect.setLayout(compPaddingSelectLayout);
			compPaddingSelect.setLayoutData(compPaddingSelectLData);
			compPaddingSelect.setText("Padding selection");
			{
				labelPaddingSelect = new Label(compPaddingSelect, SWT.WRAP);
				GridData labelPaddingSelectLData = new GridData();
				// labelPaddingSelectLData.horizontalAlignment = GridData.FILL;
				// labelPaddingSelectLData.grabExcessHorizontalSpace = false;
				labelPaddingSelectLData.widthHint = 150;
				labelPaddingSelect.setLayoutData(labelPaddingSelectLData);
				labelPaddingSelect.setText("Mark the smallest interval of padding occurence:");
			}
			{
				textPaddingSelector = new Text(compPaddingSelect, SWT.NONE);
				GridData text1LData = new GridData();
				text1LData.horizontalIndent = 5;
				textPaddingSelector.setLayoutData(text1LData);
				textPaddingSelector.setText(TranspositionAnalysisPadding.getPaddingExcerpt(
						((AnalysisWizard) getWizard()).getInitializationInput().getMaxKeylength(), getCiphertext()));
			}
			{
				separatorAutoExplanation = new Label(compPaddingSelect, SWT.SEPARATOR | SWT.VERTICAL);
				GridData separatorAutoExplanationLData = new GridData();
				separatorAutoExplanationLData.horizontalIndent = 5;
				separatorAutoExplanation.setLayoutData(separatorAutoExplanationLData);
			}
			{
				labelAutoDescription = new Label(compPaddingSelect, SWT.WRAP);
				GridData labelAutoDescriptionLData = new GridData();
				labelAutoDescriptionLData.horizontalAlignment = GridData.FILL;
				labelAutoDescriptionLData.grabExcessHorizontalSpace = true;
				labelAutoDescriptionLData.widthHint = 200;
				labelAutoDescription.setLayoutData(labelAutoDescriptionLData);
				labelAutoDescription.setText(
						"The automatical detection of the padding is just a rough approach and is often not correct.");
			}
			{
				buttonAutoDetect = new Button(compPaddingSelect, SWT.PUSH | SWT.CENTER);
				GridData buttonAutoDetectLData = new GridData();
				buttonAutoDetect.setLayoutData(buttonAutoDetectLData);
				buttonAutoDetect.setText("Try to autodetect the padding");
				buttonAutoDetect.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent evt) {
						textPaddingSelector.setSelection(
								textPaddingSelector.getText().length() - TranspositionAnalysisPadding
										.guessPaddingDistanceFromEndOfSampletext(textPaddingSelector.getText()),
								textPaddingSelector.getText().length());
						hideObject(buttonAutoDetect, true);
						hideObject(labelAutoDescription, false);
					}
				});
			}

		}
		hideObject(labelAutoDescription, true);
	}

	@Override
	public int getSelectedPaddingLengthFromEnd() {
		if (textPaddingSelector.getSelection().y != textPaddingSelector.getSelection().x)
			return textPaddingSelector.getText().length()
					- Math.min(textPaddingSelector.getSelection().x, textPaddingSelector.getSelection().y);
		return 0;
	}

	@Override
	protected void calcPageComplete() {
		setPageComplete(true);
	}

	/**
	 * Excludes a control from Layout calculation
	 * 
	 * @param that
	 * @param hideit
	 */
	private void hideObject(final Control that, final boolean hideit) {
		GridData GData = (GridData) that.getLayoutData();
		GData.exclude = hideit;
		that.setVisible(!hideit);
		Control[] myArray = { that };
		pageComposite.layout(myArray);
	}

}

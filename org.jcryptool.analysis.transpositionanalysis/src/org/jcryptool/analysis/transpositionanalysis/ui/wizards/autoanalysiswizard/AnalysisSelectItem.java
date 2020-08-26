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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysis;
import org.jcryptool.core.logging.utils.LogUtil;

public class AnalysisSelectItem extends Composite {

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */

	private Button analysis1check;
	private Composite analysis1CompTextual;
	private Label analysisLink;
	private Composite analysis1description;
	private Label analysis1descriptionlabel;
	private TranspositionAnalysis analysis;

	/**
	 * Overriding checkSubclass allows this class to extend
	 * org.eclipse.swt.widgets.Composite
	 */
	@Override
	protected void checkSubclass() {
	}

	/**
	 * Auto-generated method to display this org.eclipse.swt.widgets.Composite
	 * inside a new Shell.
	 */

	public AnalysisSelectItem(org.eclipse.swt.widgets.Composite parent, TranspositionAnalysis analysis) {
		super(parent, SWT.BORDER);
		this.analysis = analysis;
		initGUI();
	}

	private void initGUI() {
		try {

			GridLayout analysis1Layout = new GridLayout();
			analysis1Layout.numColumns = 2;
			this.setLayout(analysis1Layout);
			{
				GridData analysis1checkLData = new GridData();
				analysis1check = new Button(this, SWT.CHECK | SWT.LEFT);
				analysis1check.setLayoutData(analysis1checkLData);
			}
			{
				analysis1CompTextual = new Composite(this, SWT.NONE);
				GridLayout analysis1CompTextualLayout = new GridLayout();
				analysis1CompTextualLayout.makeColumnsEqualWidth = true;
				GridData analysis1CompTextualLData = new GridData();
				analysis1CompTextualLData.grabExcessHorizontalSpace = true;
				analysis1CompTextualLData.horizontalAlignment = SWT.FILL;
				analysis1CompTextualLData.horizontalIndent = 10;
				analysis1CompTextual.setLayoutData(analysis1CompTextualLData);
				analysis1CompTextual.setLayout(analysis1CompTextualLayout);
				{
					analysisLink = new Label(analysis1CompTextual, SWT.NONE);
					GridData analysisLinkLData = new GridData();
					analysisLink.setLayoutData(analysisLinkLData);
					analysisLink.setText(analysis.getAnalysisName());
					Font segeo = new Font(analysisLink.getDisplay(), new FontData("Segoe UI", 10, 0));
					analysisLink.setFont(segeo);
				}
				{
					analysis1description = new Composite(analysis1CompTextual, SWT.NONE);
					GridLayout analysis1descriptionLayout = new GridLayout();
					analysis1descriptionLayout.makeColumnsEqualWidth = true;
					GridData analysis1descriptionLData = new GridData();
					analysis1descriptionLData.grabExcessHorizontalSpace = true;
					analysis1descriptionLData.horizontalAlignment = SWT.FILL;
					analysis1description.setLayoutData(analysis1descriptionLData);
					analysis1description.setLayout(analysis1descriptionLayout);
					{
						analysis1descriptionlabel = new Label(analysis1description, SWT.WRAP);
						GridData analysis1descriptionlabelLData = new GridData();
						analysis1descriptionlabelLData.grabExcessHorizontalSpace = true;
						analysis1descriptionlabelLData.horizontalAlignment = GridData.FILL;
						analysis1descriptionlabel.setLayoutData(analysis1descriptionlabelLData);
						analysis1descriptionlabelLData.widthHint = 300;
						analysis1descriptionlabel.setText(analysis.getAnalysisDescription());
					}
				}
			}

			this.layout();
		} catch (Exception e) {
			LogUtil.logError(e);
		}
	}

	public boolean getSelection() {
		return analysis1check.getSelection();
	}

	public void addSelectionListener(Listener selectionListener) {
		analysis1check.addListener(SWT.Selection, selectionListener);
	}

	@Override
	public void setEnabled(boolean enabled) {
		analysis1check.setEnabled(enabled);
	}

	public void setSelection(boolean selected) {
		analysis1check.setSelection(selected);
	}

}

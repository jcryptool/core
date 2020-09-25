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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisCipherlengthDividers;

public class DividerAnalysisPage extends SingleAnalysisPage {

	private Label labelNoInputNeeded;

	public DividerAnalysisPage(TranspositionAnalysisCipherlengthDividers analysis) {
		super(analysis);
	}

	@Override
	protected void createMainControls(Composite parent) {
		labelNoInputNeeded = new Label(parent, SWT.WRAP);
		GridData labelNoInputNeededLData = new GridData();
		labelNoInputNeededLData.horizontalAlignment = GridData.FILL;
		labelNoInputNeededLData.grabExcessHorizontalSpace = true;
		labelNoInputNeededLData.widthHint = 200;
		labelNoInputNeeded.setLayoutData(labelNoInputNeededLData);
		labelNoInputNeeded
				.setText("The cipherlength divider analysis does not need any input but the ciphertext itself.");
	}

	@Override
	protected void calcPageComplete() {
		setPageComplete(true);
	}

}

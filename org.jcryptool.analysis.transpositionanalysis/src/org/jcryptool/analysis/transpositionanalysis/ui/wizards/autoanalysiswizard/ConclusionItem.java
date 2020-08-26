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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysis;
import org.jcryptool.core.logging.utils.LogUtil;

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
public class ConclusionItem extends org.eclipse.swt.widgets.Composite {
	private Label labelAnalysisName;
	private Text areaConclusion;
	private TranspositionAnalysis analysis;

	/**
	 * Overriding checkSubclass allows this class to extend
	 * org.eclipse.swt.widgets.Composite
	 */
	@Override
	protected void checkSubclass() {
	}

	public ConclusionItem(Composite parent, TranspositionAnalysis analysis) {
		super(parent, SWT.BORDER);
		this.analysis = analysis;
		initGUI();
	}

	private void initGUI() {
		try {
			GridLayout thisLayout = new GridLayout();
			thisLayout.makeColumnsEqualWidth = true;
			this.setLayout(thisLayout);
			{
				labelAnalysisName = new Label(this, SWT.NONE);
				GridData labelAnalysisNameLData = new GridData();
				labelAnalysisName.setLayoutData(labelAnalysisNameLData);
				labelAnalysisName.setText(analysis.getAnalysisName());
				Font segeo = new Font(labelAnalysisName.getDisplay(), new FontData("Segoe UI", 10, 0));
				labelAnalysisName.setFont(segeo);
			}
			{
				GridData areaConclusionLData = new GridData();
				areaConclusionLData.grabExcessHorizontalSpace = true;
				areaConclusionLData.horizontalAlignment = GridData.FILL;
				areaConclusionLData.verticalAlignment = GridData.BEGINNING;
				areaConclusionLData.grabExcessVerticalSpace = true;
				areaConclusionLData.widthHint = 300;
				areaConclusion = new Text(this, SWT.MULTI | SWT.WRAP);
				areaConclusion.setLayoutData(areaConclusionLData);
				areaConclusion.setEditable(false);
				areaConclusion.setText(analysis.getConclusion().toString());
			}
			this.layout();
		} catch (Exception e) {
			LogUtil.logError(e);
		}
	}

}

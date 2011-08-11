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
package org.jcryptool.analysis.transpositionanalysis.ui.wizards.autoanalysiswizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisDataobject;

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
public class CalculationPage extends WizardPage {

	private Composite pageComposite;
	private Label labelContinueNow;
	private ProgressBar progressBar1;
	private Button button1;

	protected CalculationPage(TranspositionAnalysisDataobject dataobject) {
		super("Calculation");
		this.setTitle("Calculation");
		this.setDescription("After collecting the data for the analysis, the analysis is now ready to be performed.");

	}

	public void setProgress(double progress) {
		progressBar1.setSelection((int) Math.round(progress
				* (double) (progressBar1.getMaximum())));
	}

	public void createControl(Composite parent) {
		{
			pageComposite = new Composite(parent, SWT.NONE);
			GridLayout pageCompositeLayout = new GridLayout();
			GridData pageCompositeLData = new GridData();
			pageCompositeLData.horizontalAlignment = SWT.FILL;
			pageCompositeLData.verticalAlignment = SWT.FILL;
			pageCompositeLData.grabExcessHorizontalSpace = true;
			pageCompositeLData.grabExcessVerticalSpace = true;
			pageCompositeLayout.makeColumnsEqualWidth = true;
			pageComposite.setLayout(pageCompositeLayout);

			{
				button1 = new Button(pageComposite, SWT.PUSH | SWT.CENTER);
				GridData button1LData = new GridData();
				button1LData.horizontalAlignment = GridData.CENTER;
				button1LData.grabExcessHorizontalSpace = true;
				button1LData.grabExcessVerticalSpace = true;
				button1LData.verticalAlignment = GridData.END;
				button1LData.widthHint = 250;
				button1.setLayoutData(button1LData);
				button1.setText("Perform analysis");
				button1.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						((AnalysisWizard) getWizard()).analyze();
					}
				});
			}
			{
				GridData progressBar1LData = new GridData();
				progressBar1LData.grabExcessVerticalSpace = true;
				progressBar1LData.grabExcessHorizontalSpace = true;
				progressBar1LData.horizontalAlignment = GridData.CENTER;
				progressBar1LData.verticalAlignment = GridData.BEGINNING;
				progressBar1LData.widthHint = 250;
				progressBar1 = new ProgressBar(pageComposite, SWT.NONE);
				progressBar1.setLayoutData(progressBar1LData);
			}
			{
				labelContinueNow = new Label(pageComposite, SWT.NONE);
				GridData labelContinueNowLData = new GridData();
				labelContinueNowLData.verticalAlignment = GridData.BEGINNING;
				labelContinueNowLData.grabExcessVerticalSpace = true;
				labelContinueNowLData.grabExcessHorizontalSpace = true;
				labelContinueNowLData.horizontalAlignment = GridData.CENTER;
				labelContinueNow.setLayoutData(labelContinueNowLData);
				labelContinueNow
						.setText("Analysis complete! You may now continue to the conclusion.");
				labelContinueNow.setVisible(false);
			}
		}

		this.setControl(pageComposite);
		this.setPageComplete(false);
	}

	public void analysisComplete() {
		setPageComplete(true);
		labelContinueNow.setVisible(true);
	}

}

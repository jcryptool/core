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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.Messages;

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
public class AnalysisWizardIndexPage extends WizardPage {
	private Label label1;
	private Label nonfunctionalHintLabel;
	private Label analysis1descriptionlabel;
	private Composite analysis1description;
	private Composite analysis1;
	private Link analysisLink;
	private Button keyCountValueBtn;
	private Label keyMainWindowValue;
	private Label keyMainWindowLabel;
	private Composite keyMainWindow;
	private Group keyGroup;

	Composite pageComposite;
	private Composite overviewComposite;
	private Composite keyCalculated;
	private Label keyCalculatedLabel;
	private Label keyCalculatedValue;
	private Group columnCountGroup;
	private Composite columnCountMainWindow;
	private Label columnCountMainWindowLabel;
	private Label columnCountMainWindowValue;
	private Composite columnCountCalculated;
	private Label columnCountCalculatedLabel;
	private Label columnCountCalculatedValue;
	private Button takeColumnCountValueBtn;
	private Label label2;

	private void initPage() {
		this.setTitle(Messages.AnalysisWizardIndexPage_analysisindex);
		this.setMessage(Messages.AnalysisWizardIndexPage_pagedescr);
	}

	public AnalysisWizardIndexPage(String pageName) {
		super(pageName);
		initPage();
	}

	public AnalysisWizardIndexPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		initPage();
	}

	@Override
	public void createControl(Composite parent) {

		{
			GridData pageCompositeLayoutData = new GridData();
			GridLayout pageCompositeLayout = new GridLayout();
			pageCompositeLayoutData.grabExcessHorizontalSpace = true;
			pageCompositeLayoutData.grabExcessVerticalSpace = false;
			pageCompositeLayoutData.horizontalAlignment = SWT.FILL;
			pageCompositeLayoutData.verticalAlignment = SWT.TOP;
			pageComposite = new Composite(parent, SWT.NONE);
			pageComposite.setLayout(pageCompositeLayout);
			pageComposite.setLayoutData(pageCompositeLayoutData);
			{
				nonfunctionalHintLabel = new Label(pageComposite, SWT.NONE);
				GridData nonfunctionalHintLabelLData = new GridData();
				nonfunctionalHintLabel.setLayoutData(nonfunctionalHintLabelLData);
				nonfunctionalHintLabel.setText(Messages.AnalysisWizardIndexPage_nomethodsavailable);
				nonfunctionalHintLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent evt) {
						setPageComplete(true);
					}
				});
			}
			{
				label1 = new Label(pageComposite, SWT.NONE);
				GridData label1LData = new GridData();
				label1LData.grabExcessHorizontalSpace = true;
				label1LData.horizontalAlignment = GridData.FILL;
				label1LData.verticalIndent = 20;
				label1.setLayoutData(label1LData);
				label1.setText(Messages.AnalysisWizardIndexPage_actualsituation);
				label1.setEnabled(false);
			}
			{
				overviewComposite = new Composite(pageComposite, SWT.NONE);
				GridLayout overviewCompositeLayout = new GridLayout();
				overviewCompositeLayout.numColumns = 2;
				GridData overviewCompositeLData = new GridData();
				overviewCompositeLData.grabExcessHorizontalSpace = true;
				overviewCompositeLData.horizontalAlignment = GridData.FILL;
				overviewComposite.setLayoutData(overviewCompositeLData);
				overviewComposite.setLayout(overviewCompositeLayout);
				{
					columnCountGroup = new Group(overviewComposite, SWT.NONE);
					GridLayout columnCountGroupLayout = new GridLayout();
					columnCountGroup.setLayout(columnCountGroupLayout);
					GridData columnCountGroupLData = new GridData();
					columnCountGroupLData.grabExcessHorizontalSpace = true;
					columnCountGroupLData.horizontalAlignment = GridData.FILL;
					columnCountGroup.setLayoutData(columnCountGroupLData);
					columnCountGroup.setText("");// Messages.AnalysisWizardIndexPage_columncount); //$NON-NLS-1$
					columnCountGroup.setEnabled(false);
					{
						columnCountMainWindow = new Composite(columnCountGroup, SWT.NONE);
						GridLayout columnCountMainwindowLayout = new GridLayout();
						columnCountMainwindowLayout.numColumns = 3;
						GridData columnCountMainwindowLData = new GridData();
						columnCountMainWindow.setLayoutData(columnCountMainwindowLData);
						columnCountMainWindow.setLayout(columnCountMainwindowLayout);
						{
							columnCountMainWindowLabel = new Label(columnCountMainWindow, SWT.NONE);
							GridData columnCountMainWindowLabelLData = new GridData();
							columnCountMainWindowLabel.setLayoutData(columnCountMainWindowLabelLData);
							columnCountMainWindowLabel.setText(Messages.AnalysisWizardIndexPage_columncountmain);
							columnCountMainWindowLabel.setEnabled(false);
						}
						{
							columnCountMainWindowValue = new Label(columnCountMainWindow, SWT.NONE);
							GridData columnCountMainWindowValueLData = new GridData();
							columnCountMainWindowValueLData.grabExcessHorizontalSpace = true;
							columnCountMainWindowValue.setLayoutData(columnCountMainWindowValueLData);
							columnCountMainWindowValue.setText("0"); //$NON-NLS-1$
							columnCountMainWindowValue.setEnabled(false);
						}
					}
					{
						columnCountCalculated = new Composite(columnCountGroup, SWT.NONE);
						GridLayout columnCountCalculatedLayout = new GridLayout();
						columnCountCalculatedLayout.numColumns = 3;
						GridData columnCountCalculatedLData = new GridData();
						columnCountCalculated.setLayoutData(columnCountCalculatedLData);
						columnCountCalculated.setLayout(columnCountCalculatedLayout);
						{
							columnCountCalculatedLabel = new Label(columnCountCalculated, SWT.NONE);
							GridData columnCountCalculatedLabelLData = new GridData();
							columnCountCalculatedLabel.setLayoutData(columnCountCalculatedLabelLData);
							columnCountCalculatedLabel.setText(Messages.AnalysisWizardIndexPage_calculatedcolcount);
							columnCountCalculatedLabel.setEnabled(false);
						}
						{
							columnCountCalculatedValue = new Label(columnCountCalculated, SWT.NONE);
							GridData columnCountCalculatedValueLData = new GridData();
							columnCountCalculatedValueLData.grabExcessHorizontalSpace = true;
							columnCountCalculatedValue.setLayoutData(columnCountCalculatedValueLData);
							columnCountCalculatedValue.setText("0"); //$NON-NLS-1$
							columnCountCalculatedValue.setEnabled(false);
						}
						{
							takeColumnCountValueBtn = new Button(columnCountCalculated, SWT.PUSH | SWT.CENTER);
							GridData takeColumnCountValueBtnLData = new GridData();
							takeColumnCountValueBtn.setLayoutData(takeColumnCountValueBtnLData);
							takeColumnCountValueBtn.setText(Messages.AnalysisWizardIndexPage_adopttomainwindow);
							takeColumnCountValueBtn.setEnabled(false);
						}
					}

				}
				{
					keyGroup = new Group(overviewComposite, SWT.NONE);
					GridLayout columnCountGroupLayout = new GridLayout();
					keyGroup.setLayout(columnCountGroupLayout);
					GridData columnCountGroupLData = new GridData();
					columnCountGroupLData.grabExcessHorizontalSpace = true;
					columnCountGroupLData.horizontalAlignment = GridData.FILL;
					keyGroup.setLayoutData(columnCountGroupLData);
					keyGroup.setText("");// Messages.AnalysisWizardIndexPage_key + ""); //$NON-NLS-1$
					keyGroup.setEnabled(false);

					{
						keyMainWindow = new Composite(keyGroup, SWT.NONE);
						GridLayout columnCountMainwindowLayout = new GridLayout();
						columnCountMainwindowLayout.numColumns = 3;
						GridData columnCountMainwindowLData = new GridData();
						columnCountMainwindowLData.grabExcessHorizontalSpace = true;
						columnCountMainwindowLData.horizontalAlignment = GridData.FILL;
						keyMainWindow.setLayoutData(columnCountMainwindowLData);
						keyMainWindow.setLayout(columnCountMainwindowLayout);
						{
							keyMainWindowLabel = new Label(keyMainWindow, SWT.NONE);
							GridData columnCountMainWindowLabelLData = new GridData();
							keyMainWindowLabel.setLayoutData(columnCountMainWindowLabelLData);
							keyMainWindowLabel.setText(Messages.AnalysisWizardIndexPage_keyinmainwindow);
							keyMainWindowLabel.setEnabled(false);
						}
						{
							keyMainWindowValue = new Label(keyMainWindow, SWT.NONE);
							GridData columnCountMainWindowValueLData = new GridData();
							columnCountMainWindowValueLData.grabExcessHorizontalSpace = true;
							keyMainWindowValue.setLayoutData(columnCountMainWindowValueLData);
							keyMainWindowValue.setText("0"); //$NON-NLS-1$
							keyMainWindowValue.setEnabled(false);
						}
					}
					{
						keyCalculated = new Composite(keyGroup, SWT.NONE);
						GridLayout columnCountCalculatedLayout = new GridLayout();
						columnCountCalculatedLayout.numColumns = 3;
						GridData columnCountCalculatedLData = new GridData();
						keyCalculated.setLayoutData(columnCountCalculatedLData);
						keyCalculated.setLayout(columnCountCalculatedLayout);
						{
							keyCalculatedLabel = new Label(keyCalculated, SWT.NONE);
							GridData columnCountCalculatedLabelLData = new GridData();
							keyCalculatedLabel.setLayoutData(columnCountCalculatedLabelLData);
							keyCalculatedLabel.setText(Messages.AnalysisWizardIndexPage_calculatedkey);
							keyCalculatedLabel.setEnabled(false);
						}
						{
							keyCalculatedValue = new Label(keyCalculated, SWT.NONE);
							GridData columnCountCalculatedValueLData = new GridData();
							columnCountCalculatedValueLData.grabExcessHorizontalSpace = true;
							keyCalculatedValue.setLayoutData(columnCountCalculatedValueLData);
							keyCalculatedValue.setText("0"); //$NON-NLS-1$
							keyCalculatedValue.setEnabled(false);
						}
						{
							keyCountValueBtn = new Button(keyCalculated, SWT.PUSH | SWT.CENTER);
							GridData takeColumnCountValueBtnLData = new GridData();
							keyCountValueBtn.setLayoutData(takeColumnCountValueBtnLData);
							keyCountValueBtn.setText(Messages.AnalysisWizardIndexPage_adopttomainwindow);
							keyCountValueBtn.setEnabled(false);
						}
					}

				}
			}
			{
				label2 = new Label(pageComposite, SWT.NONE | SWT.WRAP);
				GridData label2LData = new GridData();
				label2LData.grabExcessHorizontalSpace = true;
				label2LData.horizontalAlignment = GridData.FILL;
				label2.setLayoutData(label2LData);
				label2.setText(Messages.AnalysisWizardIndexPage_analysisoptions);
				label2.setEnabled(false);
			}
			{
				analysis1 = new Composite(pageComposite, SWT.BORDER);
				GridLayout analysis1Layout = new GridLayout();
				analysis1Layout.makeColumnsEqualWidth = true;
				GridData analysis1LData = new GridData();
				analysis1LData.grabExcessHorizontalSpace = true;
				analysis1LData.horizontalAlignment = SWT.FILL;
				analysis1LData.verticalAlignment = SWT.TOP;
				analysis1.setLayoutData(analysis1LData);
				analysis1.setLayout(analysis1Layout);
				analysis1.setEnabled(false);
				{
					analysisLink = new Link(analysis1, SWT.NONE);
					GridData analysisLinkLData = new GridData();
					analysisLink.setLayoutData(analysisLinkLData);
					analysisLink.setText(Messages.AnalysisWizardIndexPage_simpleColumnAnalysis);
					analysisLink.setEnabled(false);
				}
				{
					analysis1description = new Composite(analysis1, SWT.NONE);
					GridLayout analysis1descriptionLayout = new GridLayout();
					analysis1descriptionLayout.makeColumnsEqualWidth = true;
					GridData analysis1descriptionLData = new GridData();
					analysis1descriptionLData.grabExcessHorizontalSpace = true;
					analysis1descriptionLData.horizontalAlignment = GridData.FILL;
					analysis1description.setLayoutData(analysis1descriptionLData);
					analysis1description.setLayout(analysis1descriptionLayout);
					analysis1description.setEnabled(false);
					{
						analysis1descriptionlabel = new Label(analysis1description, SWT.WRAP);
						GridData analysis1descriptionlabelLData = new GridData();
						analysis1descriptionlabelLData.grabExcessHorizontalSpace = true;
						analysis1descriptionlabelLData.horizontalAlignment = GridData.FILL;
						analysis1descriptionlabel.setLayoutData(analysis1descriptionlabelLData);
						analysis1descriptionlabelLData.widthHint = 300;
						analysis1descriptionlabel
								.setText(Messages.AnalysisWizardIndexPage_simpleColumnAnalysisDescription);
					}
				}
			}
		}

		setPageComplete(false);
		setControl(pageComposite);

	}

	public String getText() {
		return label2.getText();
	}

}

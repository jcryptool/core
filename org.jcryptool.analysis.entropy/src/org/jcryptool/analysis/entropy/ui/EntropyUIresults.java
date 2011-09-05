// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.entropy.ui;

import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.analysis.entropy.calc.EntropyCalc;
import org.jcryptool.core.logging.utils.LogUtil;

import com.cloudgarden.resource.SWTResourceManager;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class EntropyUIresults extends Composite {

	{
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	private DecimalFormat twoD;
	private StyledText sTcriteria;
	private CLabel cLabel1;
	private CLabel cLdiffchars;
	private CLabel labelCond;
	private CLabel labelRedn;
	private CLabel labelEntn;
	private CLabel labelRed1;
	private CLabel labelEnt1;
	private CLabel labelMaxent;
	private CLabel cLabel7;
	private CLabel cLabel6;
	private CLabel cLabel5;
	private CLabel labelFilename;
	private CLabel cLabel3;
	private CLabel cLabel4;
	private CLabel cLabel2;
	private CLabel labelAllchars;
	private CLabel labelDiffchars;
	private Group groupAnalysis;
	private CLabel cLallchars;
	private Group groupFilter;

	/**
	 * Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	 */
	protected void checkSubclass() {
	}

	/**
	 * Auto-generated method to display this org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		EntropyUIresults inst = new EntropyUIresults(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if (size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public EntropyUIresults(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
		twoD = new DecimalFormat(Messages.EntropyUIresults_0);
	}

	private void initGUI() {
		try {
			this.setLayout(new FormLayout());
			{
				groupAnalysis = new Group(this, SWT.NONE);
				FormLayout groupAnalysisLayout = new FormLayout();
				groupAnalysis.setLayout(groupAnalysisLayout);
				FormData groupAnalysisLData = new FormData();
				groupAnalysisLData.left =  new FormAttachment(0, 1000, 12);
				groupAnalysisLData.top =  new FormAttachment(0, 1000, 161);
				groupAnalysisLData.width = 618;
				groupAnalysisLData.height = 174;
				groupAnalysisLData.right =  new FormAttachment(1000, 1000, -12);
				groupAnalysis.setLayoutData(groupAnalysisLData);
				groupAnalysis.setText(Messages.EntropyUIresults_3);
				{
					labelCond = new CLabel(groupAnalysis, SWT.NONE);
					FormData labelCondLData = new FormData();
					labelCondLData.left =  new FormAttachment(0, 1000, 232);
					labelCondLData.top =  new FormAttachment(0, 1000, 146);
					labelCondLData.width = 101;
					labelCondLData.height = 19;
					labelCond.setLayoutData(labelCondLData);
					labelCond.setText("F(n) = 0"); //$NON-NLS-1$
					labelCond.setForeground(SWTResourceManager.getColor(0, 0, 255));
				}
				{
					labelRedn = new CLabel(groupAnalysis, SWT.NONE);
					FormData labelRednLData = new FormData();
					labelRednLData.left =  new FormAttachment(0, 1000, 232);
					labelRednLData.top =  new FormAttachment(0, 1000, 115);
					labelRednLData.width = 77;
					labelRednLData.height = 19;
					labelRedn.setLayoutData(labelRednLData);
					labelRedn.setText("0 %"); //$NON-NLS-1$
					labelRedn.setForeground(SWTResourceManager.getColor(0, 0, 255));
				}
				{
					labelEntn = new CLabel(groupAnalysis, SWT.NONE);
					FormData labelEntnLData = new FormData();
					labelEntnLData.left =  new FormAttachment(0, 1000, 232);
					labelEntnLData.top =  new FormAttachment(0, 1000, 96);
					labelEntnLData.width = 72;
					labelEntnLData.height = 19;
					labelEntn.setLayoutData(labelEntnLData);
					labelEntn.setText("G(n) = 0"); //$NON-NLS-1$
					labelEntn.setForeground(SWTResourceManager.getColor(0, 0, 255));
				}
				{
					labelRed1 = new CLabel(groupAnalysis, SWT.NONE);
					FormData labelRed1LData = new FormData();
					labelRed1LData.left =  new FormAttachment(0, 1000, 232);
					labelRed1LData.top =  new FormAttachment(0, 1000, 65);
					labelRed1LData.width = 56;
					labelRed1LData.height = 19;
					labelRed1.setLayoutData(labelRed1LData);
					labelRed1.setText("0 %"); //$NON-NLS-1$
					labelRed1.setForeground(SWTResourceManager.getColor(0, 0, 255));
				}
				{
					labelEnt1 = new CLabel(groupAnalysis, SWT.NONE);
					FormData labelEnt1LData = new FormData();
					labelEnt1LData.left =  new FormAttachment(0, 1000, 232);
					labelEnt1LData.top =  new FormAttachment(0, 1000, 46);
					labelEnt1LData.width = 72;
					labelEnt1LData.height = 19;
					labelEnt1.setLayoutData(labelEnt1LData);
					labelEnt1.setText("G(1) = 0"); //$NON-NLS-1$
					labelEnt1.setForeground(SWTResourceManager.getColor(0, 0, 255));
				}
				{
					labelMaxent = new CLabel(groupAnalysis, SWT.NONE);
					FormData labelMaxentLData = new FormData();
					labelMaxentLData.left =  new FormAttachment(0, 1000, 232);
					labelMaxentLData.top =  new FormAttachment(0, 1000, 15);
					labelMaxentLData.width = 72;
					labelMaxentLData.height = 19;
					labelMaxent.setLayoutData(labelMaxentLData);
					labelMaxent.setText("0"); //$NON-NLS-1$
					labelMaxent.setForeground(SWTResourceManager.getColor(0, 0, 255));
				}
				{
					cLabel7 = new CLabel(groupAnalysis, SWT.NONE);
					FormData cLabel7LData = new FormData();
					cLabel7LData.left =  new FormAttachment(0, 1000, 9);
					cLabel7LData.top =  new FormAttachment(0, 1000, 146);
					cLabel7LData.width = 211;
					cLabel7LData.height = 19;
					cLabel7.setLayoutData(cLabel7LData);
					cLabel7.setText(Messages.EntropyUIresults_10);
				}
				{
					cLabel6 = new CLabel(groupAnalysis, SWT.NONE);
					FormData cLabel6LData = new FormData();
					cLabel6LData.left =  new FormAttachment(0, 1000, 21);
					cLabel6LData.top =  new FormAttachment(0, 1000, 115);
					cLabel6LData.width = 199;
					cLabel6LData.height = 19;
					cLabel6.setLayoutData(cLabel6LData);
					cLabel6.setText(Messages.EntropyUIresults_11);
				}
				{
					cLabel5 = new CLabel(groupAnalysis, SWT.NONE);
					FormData cLabel5LData = new FormData();
					cLabel5LData.left =  new FormAttachment(0, 1000, 9);
					cLabel5LData.top =  new FormAttachment(0, 1000, 96);
					cLabel5LData.width = 211;
					cLabel5LData.height = 19;
					cLabel5.setLayoutData(cLabel5LData);
					cLabel5.setText(Messages.EntropyUIresults_12);
				}
				{
					cLabel4 = new CLabel(groupAnalysis, SWT.NONE);
					FormData cLabel4LData = new FormData();
					cLabel4LData.left =  new FormAttachment(0, 1000, 21);
					cLabel4LData.top =  new FormAttachment(0, 1000, 65);
					cLabel4LData.width = 199;
					cLabel4LData.height = 19;
					cLabel4.setLayoutData(cLabel4LData);
					cLabel4.setText(Messages.EntropyUIresults_13);
				}
				{
					cLabel3 = new CLabel(groupAnalysis, SWT.NONE);
					FormData cLabel3LData = new FormData();
					cLabel3LData.left =  new FormAttachment(0, 1000, 9);
					cLabel3LData.top =  new FormAttachment(0, 1000, 46);
					cLabel3LData.width = 211;
					cLabel3LData.height = 19;
					cLabel3.setLayoutData(cLabel3LData);
					cLabel3.setText(Messages.EntropyUIresults_14);
				}
				{
					cLabel2 = new CLabel(groupAnalysis, SWT.NONE);
					FormData cLabel2LData = new FormData();
					cLabel2LData.left =  new FormAttachment(0, 1000, 9);
					cLabel2LData.top =  new FormAttachment(0, 1000, 15);
					cLabel2LData.width = 211;
					cLabel2LData.height = 19;
					cLabel2.setLayoutData(cLabel2LData);
					cLabel2.setText(Messages.EntropyUIresults_15);
				}
			}
			{
				sTcriteria = new StyledText(this, SWT.NONE);
				FormData sTcriteriaLData = new FormData();
				sTcriteriaLData.left =  new FormAttachment(0, 1000, 12);
				sTcriteriaLData.top =  new FormAttachment(0, 1000, 12);
				sTcriteriaLData.width = 618;
				sTcriteriaLData.height = 22;
				sTcriteriaLData.right =  new FormAttachment(1000, 1000, -12);
				sTcriteria.setLayoutData(sTcriteriaLData);
				sTcriteria.setText(Messages.EntropyUIresults_16);
				sTcriteria.setEditable(false);
				sTcriteria.setEnabled(false);
				sTcriteria.setOrientation(SWT.HORIZONTAL);
				sTcriteria.setWordWrap(true);
				sTcriteria.setDoubleClickEnabled(false);
				sTcriteria.setDragDetect(false);
				sTcriteria.setBackground(SWTResourceManager.getColor(192, 192, 192));
				sTcriteria.setTopMargin(5);
				sTcriteria.setLeftMargin(5);
				sTcriteria.setForeground(SWTResourceManager.getColor(0, 0, 255));
			}
			{
				groupFilter = new Group(this, SWT.NONE);
				FormLayout groupFilterLayout = new FormLayout();
				groupFilter.setLayout(groupFilterLayout);
				FormData groupFilterLData = new FormData();
				groupFilterLData.left =  new FormAttachment(0, 1000, 12);
				groupFilterLData.top =  new FormAttachment(0, 1000, 45);
				groupFilterLData.width = 618;
				groupFilterLData.height = 87;
				groupFilterLData.right =  new FormAttachment(1000, 1000, -12);
				groupFilter.setLayoutData(groupFilterLData);
				groupFilter.setText(Messages.EntropyUIresults_17);
				{
					labelAllchars = new CLabel(groupFilter, SWT.NONE);
					FormData labelAllcharsLData = new FormData();
					labelAllcharsLData.left =  new FormAttachment(0, 1000, 180);
					labelAllcharsLData.top =  new FormAttachment(0, 1000, 61);
					labelAllcharsLData.width = 42;
					labelAllcharsLData.height = 19;
					labelAllchars.setLayoutData(labelAllcharsLData);
					labelAllchars.setText("0"); //$NON-NLS-1$
					labelAllchars.setForeground(SWTResourceManager.getColor(0, 0, 255));
				}
				{
					labelDiffchars = new CLabel(groupFilter, SWT.NONE);
					FormData labelDiffcharsLData = new FormData();
					labelDiffcharsLData.left =  new FormAttachment(0, 1000, 180);
					labelDiffcharsLData.top =  new FormAttachment(0, 1000, 36);
					labelDiffcharsLData.width = 42;
					labelDiffcharsLData.height = 19;
					labelDiffchars.setLayoutData(labelDiffcharsLData);
					labelDiffchars.setText("0"); //$NON-NLS-1$
					labelDiffchars.setForeground(SWTResourceManager.getColor(0, 0, 255));
				}
				{
					labelFilename = new CLabel(groupFilter, SWT.NONE);
					FormData labelFilenameLData = new FormData();
					labelFilenameLData.left =  new FormAttachment(0, 1000, 180);
					labelFilenameLData.top =  new FormAttachment(0, 1000, 10);
					labelFilenameLData.width = 429;
					labelFilenameLData.height = 19;
					labelFilenameLData.right =  new FormAttachment(1000, 1000, -9);
					labelFilename.setLayoutData(labelFilenameLData);
					labelFilename.setText("..."); //$NON-NLS-1$
					labelFilename.setForeground(SWTResourceManager.getColor(0, 0, 255));
				}
				{
					cLallchars = new CLabel(groupFilter, SWT.NONE);
					FormData cLallcharsLData = new FormData();
					cLallcharsLData.left =  new FormAttachment(0, 1000, 9);
					cLallcharsLData.top =  new FormAttachment(0, 1000, 61);
					cLallcharsLData.width = 175;
					cLallcharsLData.height = 19;
					cLallchars.setLayoutData(cLallcharsLData);
					cLallchars.setText(Messages.EntropyUIresults_21);
				}
				{
					cLdiffchars = new CLabel(groupFilter, SWT.NONE);
					FormData cLdiffcharsLData = new FormData();
					cLdiffcharsLData.left =  new FormAttachment(0, 1000, 9);
					cLdiffcharsLData.top =  new FormAttachment(0, 1000, 36);
					cLdiffcharsLData.width = 175;
					cLdiffcharsLData.height = 19;
					cLdiffchars.setLayoutData(cLdiffcharsLData);
					cLdiffchars.setText(Messages.EntropyUIresults_22);
				}
				{
					cLabel1 = new CLabel(groupFilter, SWT.NONE);
					FormData cLabel1LData = new FormData();
					cLabel1LData.width = 175;
					cLabel1LData.height = 20;
					cLabel1LData.top =  new FormAttachment(0, 1000, 10);
					cLabel1LData.left =  new FormAttachment(0, 1000, 9);
					cLabel1.setLayoutData(cLabel1LData);
					cLabel1.setText(Messages.EntropyUIresults_23);
				}
			}
			this.layout();
			pack();
		} catch (Exception e) {
            LogUtil.logError(e);
		}
	}

	public void printSummary(EntropyCalc eC) {
		Integer actualN = Integer.valueOf(eC.getActualN());
		Integer n = Integer.valueOf(eC.getN());
		Integer diffChars = Integer.valueOf(eC.getMyData().getLengthAlphabet());
		Integer totalChars = Integer.valueOf(eC.getMyData().getLengthFilteredText());
		double signiveau = eC.getSigniveau();
		double[][] entMatrix = eC.getResultMatrix();

		if (actualN.equals(n)) {
			sTcriteria.setText(Messages.EntropyUIresults_24+ (actualN+1) +Messages.EntropyUIresults_25);
		} else {
			sTcriteria.setText(Messages.EntropyUIresults_26+twoD.format(signiveau * 100)+Messages.EntropyUIresults_27);
		}

		//TODO: dateiname einsetzen
		labelFilename.setText(eC.getEditorname());
		labelDiffchars.setText(diffChars.toString());
		labelAllchars.setText(totalChars.toString());
		labelMaxent.setText(twoD.format(entMatrix[0][4]));
		labelEnt1.setText("G(1) = "+twoD.format(entMatrix[0][6])); //$NON-NLS-1$
		labelRed1.setText(twoD.format(entMatrix[0][8]*100)+" %"); //$NON-NLS-1$
		labelEntn.setText("G("+(actualN)+") = "+twoD.format(entMatrix[actualN-1][6])); //$NON-NLS-1$ //$NON-NLS-2$
		labelRedn.setText(twoD.format(entMatrix[actualN-1][8]*100)+" %"); //$NON-NLS-1$
		labelCond.setText("F("+actualN+") = "+twoD.format(entMatrix[actualN-1][0])); //$NON-NLS-1$ //$NON-NLS-2$
	}
}

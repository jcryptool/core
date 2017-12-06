// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * Feld fuer die Informationstexte im Tutorial zur Kryptanalyse von
 * Transpositionschiffren mit einem Ameisenalgorithmus.
 * 
 * @author Philipp Blohm
 * @version 03.08.07
 * 
 */
public class AntColDescriptionComposite extends Composite {

	private Label descriptionLeft;
	private Label descriptionRight;
	private Composite innerComp;
	private ScrolledComposite sc;

	/**
	 * Konstruktor. Erhaelt Model, setzt grundlegende Einstellungen.
	 * 
	 * @param m
	 *            Model mit den Daten des Tutorials
	 * @param c
	 *            Parent
	 */
	public AntColDescriptionComposite(Composite c) {
		super(c, SWT.NONE);

		this.setLayout(new GridLayout(1, false));

		Group descriptionGroup = new Group(this, SWT.NONE);
		descriptionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		descriptionGroup.setText(Messages.Description_title);
		descriptionGroup.setToolTipText(Messages.Description_tooltip);
		descriptionGroup.setLayout(new GridLayout(1, false));
		
		sc = new ScrolledComposite(descriptionGroup,
				SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_sc = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_sc.widthHint = 600;
		gd_sc.heightHint = 200;
		sc.setLayoutData(gd_sc);
		
		innerComp = new Composite(sc, SWT.NONE);
		innerComp.setLayout(new GridLayout(2, true));
		sc.setContent(innerComp);
		
		descriptionLeft = new Label(innerComp, SWT.WRAP);
		descriptionLeft.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		descriptionRight = new Label(innerComp, SWT.WRAP);
		descriptionRight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		setDescriptionText(0);
		sc.setMinSize(innerComp.computeSize(SWT.DEFAULT,SWT.DEFAULT));
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		layout();
	}

	private void setDescriptionText(String left, String right) {
		descriptionLeft.setText(left);
		descriptionRight.setText(right);
		sc.layout(true);
	}

	/**
	 * number 0: Configuration Comp 1: Analysis Comp knot by knot 2: Analysis Comp multiple iteration at once, 3: Algo settings
	 * 
	 * @param number
	 */
	public void setDescriptionText(int number) {
		switch (number) {
		case 0:
			setDescriptionText(Messages.Desc_configComp_left,
					Messages.Desc_configComp_right);
			break;
		case 1:
			setDescriptionText(Messages.Desc_analysisComp_left,
					Messages.Desc_analysisComp_right);
			break;
		case 2:
			setDescriptionText(Messages.Desc_analysisComp_left,
					Messages.Desc_analysisCompMulti_right);
			break;
		case 3:
			setDescriptionText(Messages.Desc_analysisAlgoSett_left,
					Messages.Desc_analysisAlgoSett_right);
			break;
		}

		sc.setMinSize(innerComp.computeSize(SWT.DEFAULT,SWT.DEFAULT));
	}


}

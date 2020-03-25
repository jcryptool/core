// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * Feld fuer die Informationstexte im Tutorial zur Kryptanalyse von
 * Transpositionschiffren mit einem Ameisenalgorithmus.
 * 
 * @author Philipp Blohm
 * @version 03.08.07
 * 
 */
public class AntColDescriptionComposite extends Composite {

	private Group descriptionGroup;
	private Text descriptionLeft;
	private GridData gd_descriptionLeftData;
	private Text descriptionRight;
	private GridData gd_descriptionRight;
	private AntColView acv;

	/**
	 * Konstruktor. Erhaelt Model, setzt grundlegende Einstellungen.
	 * 
	 * @param m
	 *            Model mit den Daten des Tutorials
	 * @param c
	 *            Parent
	 */
	public AntColDescriptionComposite(AntColView acv, Composite c) {
		super(c, SWT.NONE);
		this.acv = acv;

		this.setLayout(new GridLayout());

		descriptionGroup = new Group(this, SWT.NONE);
		descriptionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		descriptionGroup.setText(Messages.Description_title);
		descriptionGroup.setToolTipText(Messages.Description_tooltip);
		descriptionGroup.setLayout(new GridLayout(2, true));
		
		descriptionLeft = new Text(descriptionGroup, SWT.READ_ONLY | SWT.MULTI);
		gd_descriptionLeftData = new GridData(SWT.FILL, SWT.FILL, true, false);
		descriptionLeft.setLayoutData(gd_descriptionLeftData);
		
		descriptionRight = new Text(descriptionGroup, SWT.READ_ONLY | SWT.MULTI);
		gd_descriptionRight = new GridData(SWT.FILL, SWT.FILL, true, false);
		descriptionRight.setLayoutData(gd_descriptionRight);
		
		setDescriptionText(0);
	}

	private void setDescriptionText(String left, String right) {
		descriptionLeft.setText(left);
		descriptionRight.setText(right);
		
		// Recalculate the size of the whole plugin.
		// If a longer text as the start text is displayed
		// this is necessary to avoid the text from being cut of at
		// the bottom.
		acv.recalculateSize();
		
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
		
	}

}

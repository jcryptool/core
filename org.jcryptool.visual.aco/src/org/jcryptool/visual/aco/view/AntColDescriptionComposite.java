// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
//import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.aco.model.Model;

/**
 * Feld fuer die Informationstexte im Tutorial zur Kryptanalyse von
 * Transpositionschiffren mit einem Ameisenalgorithmus.
 * 
 * @author Philipp Blohm
 * @version 03.08.07
 * 
 */
public class AntColDescriptionComposite extends Composite {

	private Label text;
	private Model m;
	private boolean isText3and4 = false;
	//private Color eyeColor = new Color(this.getDisplay(), 255, 255, 255);

	/**
	 * Konstruktor. Erhaelt Model, setzt grundlegende Einstellungen.
	 * 
	 * @param m
	 *            Model mit den Daten des Tutorials
	 * @param c
	 *            Parent
	 */
	public AntColDescriptionComposite(Model m, Composite c) {
		super(c, SWT.NONE);
		this.m = m;

		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.setLayout(new FillLayout());

		Group descriptionGroup = new Group(this, SWT.NONE);
		descriptionGroup.setLayout(new FillLayout());
		descriptionGroup.setText(Messages.Description_title);
		descriptionGroup.setToolTipText(Messages.Description_tooltip);

		ScrolledComposite sc = new ScrolledComposite(descriptionGroup, SWT.H_SCROLL
				| SWT.V_SCROLL);
		Composite innerComp = new Composite(sc, SWT.NONE);
		innerComp.setLayout(new FillLayout());
		sc.setContent(innerComp);
		sc.setMinSize(innerComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		text = new Label(innerComp, SWT.WRAP);
		updateText();
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		layout();
	}

	/**
	 * Reagiert auf Aenderungen des Models, liest den jeweils benoetigten Text
	 * aus einer Datei und schreibt diesen in sein Textfeld.
	 */
	public void updateText() {
		String s = ""; //$NON-NLS-1$
		int nr = m.getState();
		switch (nr) {
		case 0:
			s = Messages.Info_description1;
			isText3and4 = false;
			break; //$NON-NLS-1$
		case 1:
			s = Messages.Info_description2;
			isText3and4 = false;
			break; //$NON-NLS-1$
		case 2:
			s = Messages.Info_description3;
			if (m.isFinishCycle()) {
				s += "\n"+ Messages.Info_description4;
				isText3and4 = true;
			}
			break; //$NON-NLS-1$
		case 3:
			if (isText3and4) {
				return;
			}
			s = Messages.Info_description4;
			break; //$NON-NLS-1$
		}
		text.setText(s);
		layout();
	}

}

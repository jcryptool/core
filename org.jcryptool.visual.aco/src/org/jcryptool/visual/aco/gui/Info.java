// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.gui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.aco.tutorial.Model;

/**
 * Feld fuer die Informationstexte im Tutorial zur Kryptanalyse von
 * Transpositionschiffren mit einem Ameisenalgorithmus.
 *
 * @author Philipp Blohm
 * @version 03.08.07
 *
 */
public class Info extends Composite implements Observer {

	private Text text;
	private Model m;

	/**
	 * Konstruktor. Erhaelt Model, setzt grundlegende Einstellungen.
	 *
	 * @param m
	 *            Model mit den Daten des Tutorials
	 * @param c
	 *            Parent
	 */
	public Info(Model m, Composite c) {
		super(c, SWT.BORDER);
		this.m = m;
		setLayout(new FillLayout());
		text = new Text(this, SWT.V_SCROLL | SWT.MULTI | SWT.WRAP);
		text.setEditable(false);
		layout();
	}

	/**
	 * Reagiert auf Aenderungen des Models, liest den jeweils benoetigten Text
	 * aus einer Datei und schreibt diesen in sein Textfeld.
	 */
	public void update(Observable o, Object arg) {
		String s = ""; //$NON-NLS-1$
		int nr = m.getNr() + 1;

		switch (nr) {
		case 1:
			s = Messages.getString("Info.description1");break; //$NON-NLS-1$
		case 2:
			s = Messages.getString("Info.description2");break; //$NON-NLS-1$
		case 3:
			s = Messages.getString("Info.description3");break; //$NON-NLS-1$
		case 4:
			s = Messages.getString("Info.description4");break; //$NON-NLS-1$
		default:
			s = Messages.getString("Info.description5"); //$NON-NLS-1$
		}
		text.setText(s);

		// always show the beginning of the text
		text.setSelection(0, 0);
		text.showSelection();
	}

}

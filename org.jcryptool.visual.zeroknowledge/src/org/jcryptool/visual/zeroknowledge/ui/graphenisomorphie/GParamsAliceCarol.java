// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui.graphenisomorphie;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GAlice;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GBeweiser;
import org.jcryptool.visual.zeroknowledge.ui.ParamsPerson;

/**
 * Enthält eine Group, auf dem die Parameter von Alice oder Carol dargestellt
 * werden.
 *
 * @author Mareike Paul
 * @version 1.0.0
 */
public class GParamsAliceCarol extends ParamsPerson {

	private GCombiLabel a;

	private GCombiLabel f;

	private GCombiLabel g;

	private GCombiLabel h;

	private GBeweiser person;

	/**
	 * Konstruktor, der die graphischen Komponenten erstellt und eingefügt. Die
	 * Group besteht aus je einem CombiLabel für die Werte der Attribute a, f, g und
	 * h.
	 *
	 * @param beweiser
	 *            Beweiser, dessen Werte dargestellt werden sollen
	 * @param comp
	 *            Parent der graphischen Komponente
	 */
	public GParamsAliceCarol(GBeweiser beweiser, Composite comp) {
		super(comp);
		this.person = beweiser;

		group.setLayout(new GridLayout(2, true));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		group.setText(beweiser.getName());

		boolean secret = (beweiser instanceof GAlice);

		Group group_secret = new Group(group, SWT.NONE);
		group_secret.setText(Messages.GParamsAliceCarol_0);
		group_secret.setLayout(new GridLayout());
		group_secret.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		f = new GCombiLabel(Messages.GParamsAliceCarol_1, secret, group_secret);

		a = new GCombiLabel(Messages.GParamsAliceCarol_2, secret, group_secret);

		g = new GCombiLabel(Messages.GParamsAliceCarol_3, secret, group_secret);

		Group group_public = new Group(group, SWT.NONE);
		group_public.setLayout(new GridLayout());
		group_public.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		group_public.setText(Messages.GParamsAliceCarol_4);

		Label pub = new Label(group_public, SWT.CENTER);
		pub.setText(Messages.GParamsAliceCarol_5);

		h = new GCombiLabel(Messages.GParamsAliceCarol_6, secret, group_public);

		setVisible(true);
	}

	/**
	 * Getter for GCombiLabel a. Used for updating the size of the composite of a.
	 * 
	 * @return the GCombiLabel for a.
	 */
	public GCombiLabel getA() {
		return a;
	}

	/**
	 * Getter for GCombiLabel f. Used for updating the size of the composite of f.
	 * 
	 * @return the GCombiLabel for f.
	 */
	public GCombiLabel getF() {
		return f;
	}

	/**
	 * Getter for GCombiLabel g. Used for updating the size of the composite of g.
	 * 
	 * @return the GCombiLabel for g.
	 */
	public GCombiLabel getG() {
		return g;
	}

	/**
	 * Getter for GCombiLabel h. Used for updating the size of the composite of h.
	 * 
	 * @return the GCombiLabel for h.
	 */
	public GCombiLabel getH() {
		return h;
	}

	/**
	 * setzt den Beweiser neu
	 *
	 * @param b
	 *            neuer Beweisender
	 */
	public void setBeweiser(GBeweiser b) {
		this.person = b;
		update();
	}

	/**
	 * Methode zum updaten des Panels
	 *
	 * @see ParamsPerson#update()
	 */
	@Override
	public void update() {
		a.update(person.getA());
		f.update(person.getF());
		g.update(person.getG());
		h.update(person.getH());
	}
}

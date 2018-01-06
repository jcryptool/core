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
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GBeweiser;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GBob;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math.Graph;

/**
 * Klasse, die alle Zeichenflächen enthält, die zum Graphenisomorphie-Protokoll
 * gehören.
 *
 * @author Mareike Paul
 * @version 1.0.0
 */
public class ShowGraphen extends Composite {

	private GBeweiser b;

	private Zeichenflaeche g0;

	private Zeichenflaeche g1;

	private Group group_G_0;

	private Composite g0Composite;

	private Group group_G_1;

	private Composite g1Composite;

	private Group group_H;

	private Composite hComposite;

	private Group group_H_G_b;

	private Composite hgbComposite;

	private Zeichenflaeche H;

	private Zeichenflaeche H_G_b;

	/**
	 * Konstruktor, der die Zeichenflächen für die Graphen G<sub>0</sub> und
	 * G<sub>1</sub> erstellt und Group-Objekte für die Graphen H und
	 * h(G<sub>b</sub>)
	 *
	 * @param composite
	 *            Parent-Objekt für die graphischen Objekte
	 * @param g_0
	 *            Graph G<sub>0</sub>, der dargestellt werden soll
	 * @param g_1
	 *            Graph G<sub>1</sub>, der dargestellt werden soll
	 */
	public ShowGraphen(Composite composite, Graph g_0, Graph g_1, GBeweiser bew) {
		super(composite, SWT.NONE);
		this.b = bew;
		GridData gd_this = new GridData(SWT.CENTER, SWT.FILL, true, false);
		this.setLayoutData(gd_this);
		this.setLayout(new GridLayout(4, true));

		group_G_0 = new Group(this, SWT.NONE);
		group_G_0.setLayout(new GridLayout());
		group_G_0.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group_G_0.setText(Messages.ShowGraphen_0);

		g0Composite = new Composite(group_G_0, SWT.NONE);
		g0Composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		g0 = new Zeichenflaeche(g0Composite, g_0, SWT.COLOR_BLUE, 0, false);
		g0.setToolTipText(Messages.ShowGraphen_1);

		group_H = new Group(this, SWT.NONE);
		group_H.setLayout(new GridLayout());
		group_H.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group_H.setText(Messages.ShowGraphen_4);

		hComposite = new Composite(group_H, SWT.NONE);
		hComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		group_H_G_b = new Group(this, SWT.NONE);
		group_H_G_b.setLayout(new GridLayout());
		group_H_G_b.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group_H_G_b.setText(Messages.ShowGraphen_5);

		hgbComposite = new Composite(group_H_G_b, SWT.NONE);
		hgbComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		group_G_1 = new Group(this, SWT.NONE);
		group_G_1.setLayout(new GridLayout());
		group_G_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		group_G_1.setText(Messages.ShowGraphen_2);

		g1Composite = new Composite(group_G_1, SWT.NONE);
		g1Composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		g1 = new Zeichenflaeche(g1Composite, g_1, SWT.COLOR_RED, SWT.COLOR_BLUE, true);
		g1.setToolTipText(Messages.ShowGraphen_3);
	}

	/**
	 * Methode zum Erhalten der Zeichenfläche für den Graphen H
	 *
	 * @return Zeichenfläche, auf der der Graph H gezeichnet wird
	 */
	public Zeichenflaeche getH() {
		return H;
	}

	/**
	 * Methode zum Erhalten der Zeichenfläche für den Graphen h(G<sub>b</sub>)
	 *
	 * @return Zeichenfläche, auf der der Graph h(G<sub>b</sub>) gezeichnet wird
	 */
	public Zeichenflaeche getH_G_b() {
		return H_G_b;
	}

	/**
	 * Methode zum Entfernen des Graphen H
	 */
	public void removeH() {
		if (H == null) {
			return;
		}
		if (!H.isDisposed()) {
			H.setVisible(false);
			H.dispose();
		}
		H = null;
		this.redraw();
	}

	/**
	 * Methode zum Entfernen des Graphen H_G_b
	 */
	public void removeH_G_b() {
		if (H_G_b == null) {
			return;
		}
		if (!H_G_b.isDisposed()) {
			H_G_b.setVisible(false);
			H_G_b.dispose();
		}
		H_G_b = null;
		this.redraw();
	}

	/**
	 * Methode zum Erzeugen der Zeichenfläche für den Graphen H
	 *
	 * @param h
	 *            Graph, der in der Zeichenfläche H dargestellt werden soll
	 * @param p
	 *            Beweiser, der den Graphen h erstellt hat.
	 */
	public void setH(Graph h, GBeweiser p) {
		if (p.getA() == 0) {
			H = new Zeichenflaeche(hComposite, h, SWT.COLOR_GREEN, SWT.COLOR_BLUE, true);
			H.setToolTipText(Messages.ShowGraphen_6);
		} else {
			H = new Zeichenflaeche(hComposite, h, SWT.COLOR_GREEN, SWT.COLOR_RED, true);
			H.setToolTipText(Messages.ShowGraphen_7);
		}
		redraw();
	}

	/**
	 * Methode zum Erzeugen der Zeichenfläche für den Graphen h(G<sub>b</sub>
	 *
	 * @param g
	 *            Graph, der in der Zeichenfläche H_G_b dargestellt werden soll
	 * @param b
	 *            Bob, der den Graphen g erstellt hat.
	 */
	public void setH_G_b(Graph g, GBob b) {
		if (b.getB() == 0) {
			H_G_b = new Zeichenflaeche(hgbComposite, g, SWT.COLOR_DARK_GRAY, SWT.COLOR_BLUE, true);
			H_G_b.setToolTipText(Messages.ShowGraphen_8);
		} else {
			H_G_b = new Zeichenflaeche(hgbComposite, g, SWT.COLOR_DARK_GRAY, SWT.COLOR_RED, true);
			H_G_b.setToolTipText(Messages.ShowGraphen_12);
		}
		redraw();
	}

	/**
	 * Methode zum Updaten der beiden äußeren Zeichenflächen
	 *
	 * @see Composite#update()
	 */
	@Override
	public void update() {
		this.g0.dispose();
		this.g1.dispose();
		g0 = new Zeichenflaeche(g0Composite, b.getG0(), SWT.COLOR_BLUE, 0, false);
		g0.setToolTipText(Messages.ShowGraphen_16);
		g1 = new Zeichenflaeche(g1Composite, b.getG1(), SWT.COLOR_RED, SWT.COLOR_BLUE, true);
		g1.setToolTipText(Messages.ShowGraphen_17);
		redraw();
	}
}

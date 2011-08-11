// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui.graphenisomorphie;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GBeweiser;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GBob;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math.Graph;

/**
 * Klasse, die alle Zeichenflächen enthält, die zum Graphenisomorphie-Protokoll gehören.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class ShowGraphen extends Composite {

    private GBeweiser b;

    private Zeichenflaeche g0;

    private Zeichenflaeche g1;

    private Group group_G_0;

    private Group group_G_1;

    private Group group_H;

    private Group group_H_G_b;

    private Zeichenflaeche H;

    private Zeichenflaeche H_G_b;

    /**
     * Konstruktor, der die Zeichenflächen für die Graphen G<sub>0</sub> und G<sub>1</sub> erstellt
     * und Group-Objekte für die Graphen H und h(G<sub>b</sub>)
     *
     * @param composite Parent-Objekt für die graphischen Objekte
     * @param g_0 Graph G<sub>0</sub>, der dargestellt werden soll
     * @param g_1 Graph G<sub>1</sub>, der dargestellt werden soll
     */
    public ShowGraphen(Composite composite, Graph g_0, Graph g_1, GBeweiser bew) {
        super(composite, SWT.NONE);
        this.setSize(600, 160);
        this.b = bew;
        // GridLayout gridLayout = new GridLayout();
        // gridLayout.numColumns = 4;
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.CENTER;
        gridData.horizontalAlignment = GridData.CENTER;
        // this.setLayout(gridLayout);
        this.setLayoutData(gridData);

        group_G_0 = new Group(this, SWT.NONE);
        group_G_0.setBounds(8, 0, 140, 150);
        group_G_0.setText(Messages.ShowGraphen_0);
        group_G_0.setLayout(null);
        g0 = new Zeichenflaeche(group_G_0, g_0, SWT.COLOR_BLUE, 0, false);
        g0.setToolTipText(Messages.ShowGraphen_1);

        group_G_1 = new Group(this, SWT.NONE);
        group_G_1.setBounds(452, 0, 140, 150);
        group_G_1.setText(Messages.ShowGraphen_2);
        group_G_1.setLayout(null);
        g1 = new Zeichenflaeche(group_G_1, g_1, SWT.COLOR_RED, SWT.COLOR_BLUE, true);
        g1.setToolTipText(Messages.ShowGraphen_3);

        group_H = new Group(this, SWT.NONE);
        group_H.setBounds(156, 0, 140, 150);
        group_H.setText(Messages.ShowGraphen_4);
        group_H.setLayout(null);

        group_H_G_b = new Group(this, SWT.NONE);
        group_H_G_b.setBounds(304, 0, 140, 150);
        group_H_G_b.setText(Messages.ShowGraphen_5);
        group_H_G_b.setLayout(null);
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
     * @param h Graph, der in der Zeichenfläche H dargestellt werden soll
     * @param p Beweiser, der den Graphen h erstellt hat.
     */
    public void setH(Graph h, GBeweiser p) {
        if (p.getA() == 0) {
            H = new Zeichenflaeche(group_H, h, SWT.COLOR_GREEN, SWT.COLOR_BLUE, true);
            H.setToolTipText(Messages.ShowGraphen_6);
        } else {
            H = new Zeichenflaeche(group_H, h, SWT.COLOR_GREEN, SWT.COLOR_RED, true);
            H.setToolTipText(Messages.ShowGraphen_7);
        }
        redraw();
    }

    /**
     * Methode zum Erzeugen der Zeichenfläche für den Graphen h(G<sub>b</sub>
     *
     * @param g Graph, der in der Zeichenfläche H_G_b dargestellt werden soll
     * @param b Bob, der den Graphen g erstellt hat.
     */
    public void setH_G_b(Graph g, GBob b) {
        if (b.getB() == 0) {
            H_G_b =
                    new Zeichenflaeche(group_H_G_b, g, SWT.COLOR_DARK_GRAY, SWT.COLOR_BLUE,
                            true);
            H_G_b.setToolTipText(Messages.ShowGraphen_8);
        } else {
            H_G_b =
                    new Zeichenflaeche(group_H_G_b, g, SWT.COLOR_DARK_GRAY, SWT.COLOR_RED,
                            true);
            H_G_b.setToolTipText(Messages.ShowGraphen_12);
        }
        redraw();
    }

    /**
     * Methode zum Updaten der beiden äußeren Zeichenflächen
     *
     * @see Composite#update()
     */
    public void update() {
        this.g0.dispose();
        this.g1.dispose();
        g0 = new Zeichenflaeche(group_G_0, b.getG0(), SWT.COLOR_BLUE, 0, false);
        g0.setToolTipText(Messages.ShowGraphen_16);
        g1 =
                new Zeichenflaeche(group_G_1, b.getG1(), SWT.COLOR_RED, SWT.COLOR_BLUE,
                        true);
        g1.setToolTipText(Messages.ShowGraphen_17);
        redraw();
    }
}

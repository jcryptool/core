// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui.magischetuer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * Composite, das aus einem Label mit der Beschreibung und einem Label mit dem Wert besteht.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class MCombiLabel {

    private Composite comp;

    private Label content;

    private Label name;

    /**
     * erstellt ein Panel, dass aus einem Label für die Beschreibung und einem Label für den Inhalt
     * hinter der Variable besteht.
     *
     * @param title Beschreibung der Variablen
     */
    public MCombiLabel(String title, Group group) {
        comp = new Composite(group, 0);
        comp.setLayout(null);
        comp.setSize(180, 20);

        name = new Label(comp, 0);
        name.setText(title);
        name.setBounds(0, 0, 90, 20);

        content = new Label(comp, 0);
        content.setText("?"); //$NON-NLS-1$
        content.setBounds(95, 0, 100, 20);
    }

    /**
     * Methode zum Erhalten der graphischen Komponente
     *
     * @return graphische Komponente
     */
    public Composite getComp() {
        return comp;
    }

    /**
     * setzt den Text im Label für den Inhalt neu
     *
     * @param text Text, der gesetzt werden soll
     */
    public void setText(String text) {
        content.setText(text);
    }

    /**
     * aktualisiert den Inhalt des CombiLabels
     *
     * @param k Wert, der dargestellt werden soll
     */
    public void update(int k) {
        if (k == -1) {
            setText("-"); //$NON-NLS-1$
        } else {
            if (k == 1)
                setText(Messages.MCombiLabel_0);
            else if (k == 2)
                setText(Messages.MCombiLabel_1);
        }
    }

    /**
     * aktualisiert den Inhalt des Combi Labels
     *
     * @param k Wert, der dargestellt werden soll
     */
    public void update(int[] k) {
        if (k.length == 0) {
            setText("?"); //$NON-NLS-1$
            return;
        }
        boolean stop = false;
        String s = ""; //$NON-NLS-1$
        for (int i = 0; i < k.length && !stop; i++) {
            if (k[i] == -1)
                stop = true;
            else
                s += k[i];
        }
        if (stop)
            setText("-"); //$NON-NLS-1$
        else
            setText(s);
    }
}

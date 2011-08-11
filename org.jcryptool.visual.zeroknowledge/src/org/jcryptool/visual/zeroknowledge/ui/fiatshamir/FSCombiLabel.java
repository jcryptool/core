// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui.fiatshamir;

import java.math.BigInteger;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * Enthält ein Group-Objekt, das aus einem Label mit der Beschreibung und einem Label mit dem Wert
 * besteht.
 *
 * @author Mareike Paul
 * @version 1.0.0
 */
public class FSCombiLabel {
    private Composite comp;
    private Label content;
    private Label name;
    private boolean secret;

    /**
     * Erstellt ein Composte, dass aus einem Label für die Beschreibung und einem Label für den
     * Inhalt hinter der Variable besteht.
     *
     * @param title Beschreibung der Variablen
     * @param known true, wenn der Wert bekannt ist, ansonsten false
     * @param group Group-Objekt, in das das Composite-Objekt eingebunden wird
     */
    public FSCombiLabel(String title, boolean known, Group group) {
        comp = new Composite(group, 0);
        this.secret = known;
        comp.setLayout(null);
        comp.setSize(130, 20);

        name = new Label(comp, 0);
        name.setText(title);
        name.setBounds(20, 0, 75, 20);

        content = new Label(comp, 0);
        if (known) {
            content.setText("-"); //$NON-NLS-1$
        } else {
            content.setText("?"); //$NON-NLS-1$
        }
        content.setBounds(100, 0, 80, 20);
    }

    public FSCombiLabel() {

    }

    /**
     * Methode zum Erhalten des Composite-Objektes
     *
     * @return das Composite-Objekt
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
     * Methode zum Updaten des Content-Labels. Wenn k den Wert 0 oder -1 hat, wird entweder - oder ?
     * dargestellt. ? wird gezeigt, wenn der Wert der Person unbekannt ist. Wenn der Wert noch nicht
     * generiert wurde, wird - dargestellt. Wenn k einen anderen Wert angenommen hat, wird dieser
     * Wert angezeigt.
     *
     * @param k BigInteger, dessen Wert u.U. dargestellt werden soll
     */
    public void update(BigInteger k) {
        if (k.equals(BigInteger.ZERO) || k.equals(new BigInteger("-1"))) { //$NON-NLS-1$
            if (secret) {
                setText("-"); //$NON-NLS-1$
            } else {
                setText("?"); //$NON-NLS-1$
            }
        } else {
            setText(k.toString());
        }
    }

    /**
     * Methode zum Updaten des Content-Labels. Wenn k den Wert -1 hat, wird - dargestellt. Wenn k
     * einen anderen Wert angenommen hat, wird dieser Wert angezeigt.
     *
     * @param k int, dessen Wert u.U. dargestellt werden soll
     */
    public void update(int k) {
        if (k == -1) {
            setText("-"); //$NON-NLS-1$
        } else {
            setText(k + ""); //$NON-NLS-1$
        }
    }
}

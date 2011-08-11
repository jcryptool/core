// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui.feigefiatshamir;

import java.math.BigInteger;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * Enthält ein Composite-Objekt, auf dem das ein Label mit der Beschreibung und einem Label mit dem
 * Wert dargestellt werden.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class FFSCombiLabel {

    private Composite comp;

    private Label content;

    private Label name;

    private boolean secret;

    /**
     * Erstellt ein Composite, dass aus einem Label für die Beschreibung und einem Label für den
     * Inhalt hinter der Variable besteht.
     *
     * @param title Beschreibung der Variablen
     * @param secret true, wenn der Wert geheim bleiben soll, ansonsten false
     * @param group Group-Objekt, in das das Composite-Objekt eingebunden wird
     */
    public FFSCombiLabel(String title, boolean secret, Group group) {
        comp = new Composite(group, 0);
        this.secret = secret;
        comp.setLayout(null);
        comp.setSize(135, 20);

        name = new Label(comp, 0);
        name.setText(title);
        name.setBounds(5, 0, 40, 20);

        content = new Label(comp, 0);
        if (secret) {
            content.setText(Messages.FFS_CombiLabel_0);
        } else {
            content.setText(Messages.FFS_CombiLabel_1);
        }
        content.setBounds(50, 0, 70, 20);
    }

    /**
     * Methode zum Erhalten des Composite-Objektes, das die graphischen Komponenten enthält
     *
     * @return Composite-Objekt, das die graphischen Komponenten enthält
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
                setText(Messages.FFS_CombiLabel_3);
            } else {
                setText(Messages.FFS_CombiLabel_4);
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

    /**
     * Methode zum Updaten des Content-Labels. Wenn k den Wert -1 hat, wird - dargestellt. Wenn k
     * einen anderen Wert angenommen hat, wird dieser Wert angezeigt.
     *
     * @param k int[], dessen Wert u.U. dargestellt werden soll
     */
    public void update(int[] k) {
        String s = ""; //$NON-NLS-1$
        for (int i = 0; i < k.length; i++) {
            if (k[i] == -1) {
                s = "-"; //$NON-NLS-1$
                i = k.length;
            } else {
                s += k[i];
            }
        }
        setText(s);
    }
}

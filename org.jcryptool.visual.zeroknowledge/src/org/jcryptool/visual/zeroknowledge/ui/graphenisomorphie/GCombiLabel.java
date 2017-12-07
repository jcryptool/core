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
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math.Isomorphismus;

/**
 * Composite, das aus einem Label mit der Beschreibung und einem Label mit dem Wert besteht.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class GCombiLabel {

    private Composite comp;

    private Label content;

    private Label name;

    /**
     * Erstellt ein Composite, dass aus einem Label für die Beschreibung und einem Label für den
     * Inhalt hinter der Variable besteht.
     *
     * @param title Beschreibung der Variablen
     * @param known true, wenn der Wert des Geheimnisses bekannt ist, ansonsten false
     * @param group Group-Objekt, in das das Composite-Objekt eingebunden wird
     */
    public GCombiLabel(String title, boolean known, Group group) {
    	comp = new Composite(group, SWT.NONE);
    	comp.setLayout(new GridLayout(2, false));
    	comp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    	
    	name = new Label(comp, SWT.NONE);
        name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        name.setText(title);

        content = new Label(comp, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        if (known) {
            content.setText(Messages.G_CombiLabel_0);
        } else {
            content.setText(Messages.G_CombiLabel_1);
        }
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
     * Methode zum Updaten des Content-Labels. Wenn k den Wert -1 hat, wird - dargestellt. Wenn k
     * einen anderen Wert angenommen hat, wird dieser Wert angezeigt.
     *
     * @param k int, dessen Wert u.U. dargestellt werden soll
     */
    public void update(int k) {
        if (k == -1) {
            setText(Messages.G_CombiLabel_2);
        } else {
            setText(k + ""); //$NON-NLS-1$
        }
        comp.layout();
    }

    /**
     * Methode zum Updaten des Content-Labels. Wenn k ein null-Verweis ist, wird - dargestellt. Wenn
     * k dem Null-Isomorphismus entspricht, wird ? dargestellt, ansonten wird die
     * String-Repräsentation von k angezeigt
     *
     * @param k Isomorphismus, dessen Wert u.U. dargestellt werden soll
     */
    public void update(Isomorphismus k) {
        if (k == null) {
            setText(Messages.G_CombiLabel_4);
        } else if (k.equals(Isomorphismus.NULL)) {
            setText(Messages.G_CombiLabel_5);
        } else {
            setText(k.toString());
        }
        comp.layout();
    }
}

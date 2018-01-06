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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GBob;
import org.jcryptool.visual.zeroknowledge.ui.ParamsPerson;

/**
 * Group, auf der die Parameter von Bob dargestellt werden.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class GParamsBob extends ParamsPerson {

    private GCombiLabel b;

    private GBob bob;

    private Label verifiziert;

    /**
     * Konstruktor, der die graphischen Komponenten erstellt und eingefügt. Die Group besteht aus
     * einem Label, das den Namen enthält und je einem CombiLabel für den Wert vom Attribut b
     * darstellen.
     */
    public GParamsBob(GBob bob, Composite parent) {
        super(parent);
        this.bob = bob;
        group.setText(Messages.GParamsBob_0);



        b = new GCombiLabel(Messages.GParamsBob_1, false, group);

        verifiziert = new Label(group, SWT.None);
        verifiziert.setFont(FontService.getNormalBoldFont());
        verifiziert.setText(Messages.GParamsBob_2);
        verifiziert.setVisible(false);
        verifiziert.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        setVisible(true);
    }

    /**
     * Methode zum Erhalten des Labels, auf dem ausgegeben wird, ob das Verifizieren erfolgreich war
     * oder nicht
     *
     * @return JLabel, das ausgibt, ob die Verifikation erfolgreich war
     */
    public Label getVerifiziert() {
        return verifiziert;
    }

    public void setBob(GBob bob) {
        this.bob = bob;
    }

    /**
     * setzt den Text des Verifiziert-Labels in Abhängigkeit von b: wenn b true ist, wird der Text
     * in grün ausgegeben, ansonsten in rot
     *
     * @param b gibt an, ob Bob das Ergebnis verifiziert hat oder nicht
     */
    public void setVerifiziert(boolean b) {
        if (b) {
            verifiziert.setText(Messages.GParamsBob_3);
            verifiziert.setForeground(group.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
        } else {
            verifiziert.setText(Messages.GParamsBob_4);
            verifiziert.setForeground(group.getDisplay().getSystemColor(SWT.COLOR_RED));
        }
        verifiziert.setVisible(true);
    }

    /**
     * Methode zum updaten des Panels
     *
     * @see ParamsPerson#update()
     */
    @Override
	public void update() {
        b.update(bob.getB());
    }

    /**
     * Setzt den Text der einzelnen Labels, die nur sichtbar werden, wenn Bob verifiziert.
     *
     * @param on true, wenn die Labels sichtbar werden sollen
     */
    public void verifizieren(boolean on) {
        if (!on) {
            verifiziert.setVisible(false);
        }
    }
}
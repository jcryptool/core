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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MBob;
import org.jcryptool.visual.zeroknowledge.ui.ParamsPerson;

/**
 * Enthält ein Group-Objekt, auf dem die Parameter von Bob dargestellt werden.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class MParamsBob extends ParamsPerson {

    private MBob b;

    private MCombiLabel raumwahl;

    private Label verifiziert;

    /**
     * Konstruktor, der die graphischen Komponenten erstellt und eingefügt. Die Group besteht einem
     * CombiLabel, das den Wert vom Attribut b darstellt. Die Group hat als Text den Namen "Bob".
     *
     * @param bob Bob-Objekt, dessen Werte dargestellt werden sollen
     * @param parent Parent-Objekt zur graphischen Oberfläche
     */
    public MParamsBob(MBob bob, Composite parent) {
        super(parent);
        group.setText(Messages.MParamsBob_0);

        b = bob;

        raumwahl = new MCombiLabel(Messages.MParamsBob_1, group);

        verifiziert = new Label(group, SWT.NONE);
        verifiziert.setFont(FontService.getNormalBoldFont());
        verifiziert.setText(Messages.MParamsBob_2);
        verifiziert.setVisible(false);
        GridData gridData = new GridData();
        gridData.widthHint = 200;
        verifiziert.setLayoutData(gridData);

        setVisible(true);
    }

    /**
     * Methode zum Erhalten des Labels, auf dem ausgegeben wird, ob das Verifizieren erfolgreich war
     * oder nicht
     *
     * @return Label, das ausgibt, ob die Verifikation erfolgreich war
     */
    public Label getVerifiziert() {
        return verifiziert;
    }

    /**
     * setzt den Text des Verifiziert-Labels in Abhängigkeit von b: wenn b true ist, wird der Text
     * in grün ausgegeben, ansonsten in rot
     *
     * @param b gibt an, ob Bob die Antwort verifiziert hat. Wenn ja, dann gibt das Label aus
     *        "wurde verifiziert", ansonsten "wurde nicht verifiziert"
     */
    public void setVerifiziert(boolean b) {
        if (b) {
            verifiziert.setText(Messages.MParamsBob_3);
            verifiziert.setForeground(group.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
        } else {
            verifiziert.setText(Messages.MParamsBob_4);
            verifiziert.setForeground(group.getDisplay().getSystemColor(SWT.COLOR_RED));
        }
        verifiziert.setVisible(true);
    }

    /**
     * Methode zum updaten des Panels
     */
    public void update() {
        raumwahl.update(b.getRaumwahl());
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
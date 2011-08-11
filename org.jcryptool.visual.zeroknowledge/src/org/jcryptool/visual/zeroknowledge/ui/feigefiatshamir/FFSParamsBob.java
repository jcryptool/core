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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.zeroknowledge.algorithm.feigefiatshamir.FFSBob;
import org.jcryptool.visual.zeroknowledge.ui.ParamsPerson;

/**
 * Enthält ein Group-Objekt, auf dem die Parameter von Bob dargestellt werden.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class FFSParamsBob extends ParamsPerson {

    private FFSCombiLabel b;
    private FFSBob bob;
    private Label verifiziert;
    private FFSCombiLabel xMalVHochB;
    private FFSCombiLabel yHoch2;

    /**
     * Konstruktor, der die graphischen Komponenten erstellt und eingefügt. Das Panel besteht aus
     * einem Label, das den Namen enthält und zwei Labels, die den Wert vom Attribut b darstellen
     * (einer für die Beschreibung und einer für den Wert)
     *
     * @param bob Objekt, dessen Werte dargestellt werden sollen
     * @param s Parent für die graphische Oberfläche
     */
    public FFSParamsBob(FFSBob bob, Composite s) {
        super(s);
        this.bob = bob;
        // group.setSize(300, 170);
        group.setText(Messages.FFS_ParamsBob_0);

        b = new FFSCombiLabel(Messages.FFS_ParamsBob_1, true, group);
        // b.getComp().setLocation(90, 45);

        yHoch2 = new FFSCombiLabel(Messages.FFS_ParamsBob_2, false, group);
        // yHoch2.getComp().setLocation(90, 75);
        yHoch2.getComp().setVisible(false);

        xMalVHochB = new FFSCombiLabel(Messages.FFS_ParamsBob_3, false, group);
        // xMalVHochB.setFont(ZeroKnowledge.normal);
        // xMalVHochB.setText(" x * v^b :");
        // xMalVHochB.setBounds(72, 105, 50, 20);
        xMalVHochB.getComp().setVisible(false);

        // xMalContent = new Label(group, 0);
        // xMalContent.setFont(ZeroKnowledge.normal);
        // xMalContent.setBounds(140, 105, 150, 20);
        // xMalContent.setVisible(false);

        verifiziert = new Label(group, SWT.None);
        verifiziert.setFont(FontService.getNormalBoldFont());
        verifiziert.setText(Messages.FFS_ParamsBob_4);
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
            verifiziert.setForeground(group.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
            verifiziert.setText(Messages.FFS_ParamsBob_5);
            verifiziert.setVisible(true);
        } else {
            verifiziert.setForeground(group.getDisplay().getSystemColor(SWT.COLOR_RED));
            verifiziert.setText(Messages.FFS_ParamsBob_6);
            verifiziert.setVisible(true);
        }
    }

    /**
     * Methode zum updaten des Panels
     *
     * @see ParamsPerson#update()
     */
    public void update() {
        b.update(bob.getB());
    }

    /**
     * Setzt den Text der einzelnen Labels, die nur sichtbar werden, wenn Bob verifiziert.
     *
     * @param on true, wenn die Labels sichtbar werden sollen
     */
    public void verifizieren(boolean on) {
        if (on) {
            BigInteger yQuadrat = bob.getToVerify().multiply(bob.getToVerify()).mod(bob.getN());
            yHoch2.setText(yQuadrat.toString());
            yHoch2.getComp().setVisible(true);

            xMalVHochB.getComp().setVisible(true);

            BigInteger x = bob.getReceivedX();
            int[] bs = bob.getB();
            for (int i = 0; i < bs.length; i++) {
                if (bs[i] == 1) {
                    x = x.multiply(bob.getV()[i]).mod(bob.getN());
                }
            }
            xMalVHochB.setText(x.toString());
            // xMalContent.setVisible(true);
        } else {
            xMalVHochB.getComp().setVisible(false);
            yHoch2.getComp().setVisible(false);
            // xMalContent.setVisible(false);
        }
    }
}
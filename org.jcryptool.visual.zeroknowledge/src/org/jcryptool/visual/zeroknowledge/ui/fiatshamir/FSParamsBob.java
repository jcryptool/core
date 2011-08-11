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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir.FSBob;
import org.jcryptool.visual.zeroknowledge.ui.ParamsPerson;

/**
 * Enthält ein Group-Objekt, auf dem die Parameter von Bob dargestellt werden.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class FSParamsBob extends ParamsPerson {

    private FSCombiLabel b;
    private FSBob bob;
    private Label verifiziert;
    private FSCombiLabel xMalVHochB;
    private FSCombiLabel yHoch2;

    /**
     * Konstruktor, der die graphischen Komponenten erstellt und einfuegt. Die Group besteht aus
     * zwei CombiLabels, die den Wert von den Attribut b und y<sup>2</sup> darstellen, zwei Label
     * zum Darstellen von xv<sup>b</sup> (eines für den Text und eines für den Wert) und einem
     * Label, das angibt, ob die Antwort richtig ist oder nicht.
     *
     * @param bob Bob-Objekt, dessen Werte dargestellt werden sollen
     * @param parent Parent der Group
     */
    public FSParamsBob(FSBob bob, Composite parent) {
        super(parent);
        this.bob = bob;
        group.setText(Messages.FS_ParamsBob_0);

        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.makeColumnsEqualWidth = false;
        gridLayout1.numColumns = 1;

        group.setLayout(gridLayout1);

        b = new FSCombiLabel(Messages.FS_ParamsBob_1, false, group);

        yHoch2 = new FSCombiLabel(Messages.FS_ParamsBob_2, false, group);
        yHoch2.getComp().setVisible(false);

        xMalVHochB = new FSCombiLabel(Messages.FS_ParamsBob_3, false, group);
        xMalVHochB.getComp().setVisible(false);

        verifiziert = new Label(group, SWT.None);
        verifiziert.setFont(FontService.getNormalBoldFont());
        verifiziert.setText(Messages.FS_ParamsBob_4);
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
            verifiziert.setText(Messages.FS_ParamsBob_5);
            verifiziert.setForeground(group.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
        } else {
            verifiziert.setText(Messages.FS_ParamsBob_6);
            verifiziert.setForeground(group.getDisplay().getSystemColor(SWT.COLOR_RED));
        }
        verifiziert.setVisible(true);
    }

    /**
     * Methode zum updaten des Panels
     *
     * @see ParamsPerson#update()
     */
    public void update() {
        int bobB = bob.getB();
        if (bobB == -1) {
            b.setText(Messages.FS_ParamsBob_7);
            verifizieren(false);
        } else {
            b.setText(bobB + ""); //$NON-NLS-1$

        }
    }

    /**
     * Setzt den Text der einzelnen Labels, die nur sichtbar werden, wenn Bob verifiziert.
     *
     * @param on true, wenn die Labels sichtbar werden sollen
     */
    public void verifizieren(boolean on) {
        if (on) {
            BigInteger yQuadrat = bob.getToVerify().pow(2).mod(bob.getN());
            yHoch2.setText(yQuadrat.toString());
            yHoch2.getComp().setVisible(true);

            xMalVHochB.getComp().setVisible(true);

            BigInteger x = bob.getReceivedX();
            if (bob.getB() == 1) {
                x = x.multiply(bob.getV()).mod(bob.getN());
            }
            xMalVHochB.setText(x.toString());
        } else {
            xMalVHochB.getComp().setVisible(false);
            yHoch2.getComp().setVisible(false);
            verifiziert.setVisible(false);
        }
    }
}
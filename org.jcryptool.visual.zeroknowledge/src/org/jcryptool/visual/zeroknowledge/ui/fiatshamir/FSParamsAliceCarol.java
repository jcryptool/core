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

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir.FSCarol;
import org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir.FSPerson;
import org.jcryptool.visual.zeroknowledge.ui.ParamsPerson;

/**
 * Enthält ein Group-Objekt, auf dem die Parameter von Alice oder Carol dargestellt werden.
 *
 * @author Mareike Paul
 * @version 1.0.0
 */
public class FSParamsAliceCarol extends ParamsPerson {
    private FSCombiLabel c;
    private FSPerson person;
    private FSCombiLabel r;
    private FSCombiLabel secret;
    private FSCombiLabel v;
    private FSCombiLabel x;
    private FSCombiLabel y;
    private FSCombiLabel xLabel;

    /**
     * Konstruktor, der die graphischen Komponenten erstellt und eingefügt. Das Panel besteht aus
     * einem Label, das den Namen enthält und jeweils einem CombiLabel, die den Wert vom Attribut s,
     * s^2, r, r^2, y und ggf. c darstellen.
     *
     * @param person Person, dessen Werte dargestellt werden sollen
     * @param comp Parent-Objekt der Group
     */
    public FSParamsAliceCarol(FSPerson person, Composite comp) {
        super(comp);
        this.person = person;

        GridData gridData2 = new GridData();
        gridData2.verticalAlignment = GridData.FILL;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.grabExcessHorizontalSpace = true;

        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;

        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(gridData);
        group.setText(person.getName());

        Group groupSecret = new Group(group, 0);
        groupSecret.setLayout(new GridLayout(1, false));
        groupSecret.setLayoutData(gridData2);
        groupSecret.setText(Messages.FS_ParamsAliceCarol_0);

        Group groupPublic = new Group(group, 0);
        groupPublic.setLayout(new GridLayout(1, false));
        groupPublic.setLayoutData(gridData2);
        groupPublic.setText(Messages.FS_ParamsAliceCarol_1);

        r = new FSCombiLabel(Messages.FS_ParamsAliceCarol_2, true, groupSecret);
        r.getComp().setLocation(2, 75);

        v = new FSCombiLabel(Messages.FS_ParamsAliceCarol_3, true, groupPublic);
        v.getComp().setLocation(150, 55);

        y = new FSCombiLabel(Messages.FS_ParamsAliceCarol_4, true, groupPublic);
        y.getComp().setLocation(150, 95);

        secret = new FSCombiLabel(Messages.FS_ParamsAliceCarol_7, false, groupSecret);
        secret.getComp().setLocation(2, 55);

        // Personabhaengige Ausgabe
        if (person instanceof FSCarol) {
            c = new FSCombiLabel(Messages.FS_ParamsAliceCarol_5, true, groupSecret);
            c.getComp().setLocation(2, 95);

            xLabel = new FSCombiLabel(Messages.FS_ParamsAliceCarol_6, true, groupPublic);
            xLabel.getComp().setLocation(150, 75);
        } else {
            x = new FSCombiLabel(Messages.FS_ParamsAliceCarol_8, true, groupPublic);
            x.getComp().setLocation(150, 75);
        }
    }

    /**
     * Methode zum updaten des Panels
     *
     * @see ParamsPerson#update()
     */
    public void update() {
        secret.update(person.getSecret());
        v.update(person.getV());
        r.update(person.getR());
        y.update(person.getY());

        if (person instanceof FSCarol) {
            c.update(((FSCarol) person).getC());
            if (person.getX().equals(BigInteger.ZERO)) {
                xLabel.setText(Messages.FS_ParamsAliceCarol_10);
            } else {
                xLabel.setText(person.getX().toString());
            }
        } else {
            x.update(person.getX());
        }
    }

    public void setPerson(FSPerson person) {
        this.person = person;

        group.setText(person.getName());

        update();
    }
}

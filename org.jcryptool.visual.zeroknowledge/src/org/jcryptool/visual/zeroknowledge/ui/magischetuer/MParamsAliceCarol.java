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
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MPerson;
import org.jcryptool.visual.zeroknowledge.ui.ParamsPerson;

/**
 * Group, auf der die Parameter von Alice oder Carol dargestellt werden.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class MParamsAliceCarol extends ParamsPerson {

    private MCombiLabel code;

    private MPerson person;

    private MCombiLabel raum;

    private MCombiLabel raumwahl;

    /**
     * Konstruktor, der die graphischen Komponenten erstellt und eingefügt. Die Group besteht aus
     * einem Label, das den Namen enthält und jeweils zwei Labeln, die den Wert vom Attribut s, s^2,
     * r, r^2 und y darstellen (einer für die Beschreibung und einer für den Wert)
     *
     * @param person Person, dessen Werte dargestellt werden sollen
     * @param comp Parent-Objekt zu dem graphischen Teil dieses Objektes
     */
    public MParamsAliceCarol(MPerson person, Composite comp) {
        super(comp);
        this.person = person;
        group.setText(person.getName());
        group.setSize(300, 121);

        code = new MCombiLabel(Messages.MParamsAliceCarol_0, group);
        code.getComp().setLocation(60, 40);

        raumwahl = new MCombiLabel(Messages.MParamsAliceCarol_1, group);
        raumwahl.getComp().setLocation(60, 65);

        raum = new MCombiLabel(Messages.MParamsAliceCarol_2, group);
        raum.getComp().setLocation(60, 90);
    }

    /**
     * Methode zum updaten des Panels
     */
    public void update() {
        code.update(person.getCode());
        raumwahl.update(person.getRaumwahl());
        raum.update(person.getRaum());
    }

    public void setPerson(MPerson person) {
        this.person = person;

        group.setText(person.getName());

        update();
    }
}
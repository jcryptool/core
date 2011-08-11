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

import org.eclipse.swt.widgets.Group;

/**
 * Klasse, in der die Werte eines Vektors im mathematischen Sinne dargestellt werden. Der Vektor
 * muss vier Einträge haben.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class ShowVector {

    private FFSCombiLabel drei;

    private FFSCombiLabel eins;

    private Group group;

    private FFSCombiLabel vier;

    private FFSCombiLabel zwei;

    /**
     * Konstruktor, der eine Group mit dem Text "name" erstellt.
     *
     * @param g Parent der neu zu erstellenden Group
     * @param name Name des Vektors, der dargestellt werden soll
     * @param isAlice gibt an, ob gerade der Fall betrachtet wird, dass Bob tatsächlich mit Alice
     *        kommuniziert.
     */
    public ShowVector(Group g, String name, boolean isAlice) {
        group = new Group(g, 0);
        group.setText(name);

        group.setLayout(null);
        // group.setSize(100, 100);
        eins = new FFSCombiLabel(name + "1 :", isAlice, group); //$NON-NLS-1$
        eins.getComp().setLocation(2, 17);
        zwei = new FFSCombiLabel(name + "2 :", isAlice, group); //$NON-NLS-1$
        zwei.getComp().setLocation(2, 37);
        drei = new FFSCombiLabel(name + "3 :", isAlice, group); //$NON-NLS-1$
        drei.getComp().setLocation(2, 57);
        vier = new FFSCombiLabel(name + "4 :", isAlice, group); //$NON-NLS-1$
        vier.getComp().setLocation(2, 77);
    }

    /**
     * Methode zum Erhalten des Group-Objektes, das innerhalb dieser Klasse erstellt wurde
     *
     * @return Group-Objekt, auf dem die graphischen Komponenten liegen
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Methode zum Updaten der Group. Abhängig von der Länge des übergebenen Arrays werden die
     * CombiLabels sichtbar gemacht und positioniert.
     *
     * @param vec Array, dessen Werte dargestellt werden sollen
     */
    public void update(BigInteger[] vec) {
        if (vec.length > 4)
            throw new IllegalArgumentException(Messages.ShowVector_4);
        if (vec.length == 1) {
            eins.update(vec[0]);
            eins.getComp().setLocation(2, 47);
            eins.getComp().setVisible(true);
            zwei.getComp().setVisible(false);
            drei.getComp().setVisible(false);
            vier.getComp().setVisible(false);
        } else if (vec.length == 2) {
            eins.update(vec[0]);
            eins.getComp().setLocation(2, 32);
            zwei.update(vec[1]);
            zwei.getComp().setLocation(2, 62);
            eins.getComp().setVisible(true);
            zwei.getComp().setVisible(true);
            drei.getComp().setVisible(false);
            vier.getComp().setVisible(false);
        } else if (vec.length == 3) {
            eins.update(vec[0]);
            eins.getComp().setLocation(2, 22);
            zwei.update(vec[1]);
            zwei.getComp().setLocation(2, 47);
            drei.update(vec[2]);
            drei.getComp().setLocation(2, 72);
            eins.getComp().setVisible(true);
            zwei.getComp().setVisible(true);
            drei.getComp().setVisible(true);
            vier.getComp().setVisible(false);
        } else {
            eins.update(vec[0]);
            eins.getComp().setLocation(2, 17);
            zwei.update(vec[1]);
            zwei.getComp().setLocation(2, 37);
            drei.update(vec[2]);
            drei.getComp().setLocation(2, 57);
            vier.update(vec[3]);
            vier.getComp().setLocation(2, 77);
            eins.getComp().setVisible(true);
            zwei.getComp().setVisible(true);
            drei.getComp().setVisible(true);
            vier.getComp().setVisible(true);
        }
    }
}

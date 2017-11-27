// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;

/**
 * Klasse, in der die Werte eines Vektors im mathematischen Sinne dargestellt werden. Der Vektor
 * muss vier Einträge haben.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class ShowVector {

    private FFSCombiLabel eins;
    
    private FFSCombiLabel zwei;
    
    private FFSCombiLabel drei;
    
    private FFSCombiLabel vier;

    private Group group;


    /**
     * Konstruktor, der eine Group mit dem Text "name" erstellt.
     *
     * @param parent Parent der neu zu erstellenden Group
     * @param name Name des Vektors, der dargestellt werden soll
     * @param isAlice gibt an, ob gerade der Fall betrachtet wird, dass Bob tatsächlich mit Alice
     *        kommuniziert.
     */
    public ShowVector(Group parent, String name, boolean isAlice) {
    	group = new Group(parent, SWT.NONE);
    	group.setLayout(new GridLayout());
    	group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    	
        eins = new FFSCombiLabel(name + "1 :", isAlice, group); //$NON-NLS-1$
        zwei = new FFSCombiLabel(name + "2 :", isAlice, group); //$NON-NLS-1$
        drei = new FFSCombiLabel(name + "3 :", isAlice, group); //$NON-NLS-1$
        vier = new FFSCombiLabel(name + "4 :", isAlice, group); //$NON-NLS-1$
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
            eins.getComp().setVisible(true);
            zwei.getComp().setVisible(false);
            drei.getComp().setVisible(false);
            vier.getComp().setVisible(false);
        } else if (vec.length == 2) {
            eins.update(vec[0]);
            zwei.update(vec[1]);
            eins.getComp().setVisible(true);
            zwei.getComp().setVisible(true);
            drei.getComp().setVisible(false);
            vier.getComp().setVisible(false);
        } else if (vec.length == 3) {
            eins.update(vec[0]);
            zwei.update(vec[1]);
            drei.update(vec[2]);
            eins.getComp().setVisible(true);
            zwei.getComp().setVisible(true);
            drei.getComp().setVisible(true);
            vier.getComp().setVisible(false);
        } else {
            eins.update(vec[0]);
            zwei.update(vec[1]);
            drei.update(vec[2]);
            vier.update(vec[3]);
            eins.getComp().setVisible(true);
            zwei.getComp().setVisible(true);
            drei.getComp().setVisible(true);
            vier.getComp().setVisible(true);
        }
    }
}

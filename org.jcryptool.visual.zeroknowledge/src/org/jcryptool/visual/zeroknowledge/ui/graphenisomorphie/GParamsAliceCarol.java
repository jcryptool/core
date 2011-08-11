// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
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
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GAlice;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GBeweiser;
import org.jcryptool.visual.zeroknowledge.ui.ParamsPerson;

/**
 * Enthält eine Group, auf dem die Parameter von Alice oder Carol dargestellt werden.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class GParamsAliceCarol extends ParamsPerson {

    private GCombiLabel a;

    private GCombiLabel f;

    private GCombiLabel g;

    private GCombiLabel h;

    private GBeweiser p;

    /**
     * Konstruktor, der die graphischen Komponenten erstellt und eingefügt. Die Group besteht aus je
     * einem CombiLabel für die Werte der Attribute a, f, g und h.
     *
     * @param beweiser Beweiser, dessen Werte dargestellt werden sollen
     * @param comp Parent der graphischen Komponente
     */
    public GParamsAliceCarol(GBeweiser beweiser, Composite comp) {
        super(comp);
        this.p = beweiser;

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = true;
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        GridData gridData2 = new GridData();
        gridData2.verticalAlignment = GridData.FILL;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.grabExcessHorizontalSpace = true;

        group.setLayout(gridLayout);
        group.setLayoutData(gridData);
        group.setText(beweiser.getName());
        boolean secret = (beweiser instanceof GAlice);

        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 1;
        gridLayout2.makeColumnsEqualWidth = true;

        Group group_2 = new Group(group, 0);
        group_2.setText(Messages.GParamsAliceCarol_0);
        group_2.setLayout(gridLayout2);
        group_2.setLayoutData(gridData);
        // group_2.setBounds(1, 15, 149, 105);
        // group_2.setVisible(true);

        f = new GCombiLabel(Messages.GParamsAliceCarol_1, secret, group_2);
        // f.getComp().setLocation(2, 20);

        a = new GCombiLabel(Messages.GParamsAliceCarol_2, secret, group_2);
        // a.getComp().setLocation(2, 50);
        // a.getComp().setSize(140, 20);

        g = new GCombiLabel(Messages.GParamsAliceCarol_3, secret, group_2);
        // g.getComp().setLocation(2, 70);
        group_2.layout();

        group_2 = new Group(group, 0);
        group_2.setText(Messages.GParamsAliceCarol_4);

        gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 1;
        gridLayout2.makeColumnsEqualWidth = true;

        group_2.setLayout(gridLayout2);
        group_2.setLayoutData(gridData2);
        //
        Label pub = new Label(group_2, SWT.CENTER);
        pub.setText(Messages.GParamsAliceCarol_5);
        // //pub.setBounds(2, 20, 146, 20);
        //
        h = new GCombiLabel(Messages.GParamsAliceCarol_6, secret, group_2);
        // //h.getComp().setLocation(2, 50);
        //
        // setVisible(true);
    }

    /**
     * Methode zum updaten des Panels
     *
     * @see ParamsPerson#update()
     */
    public void update() {
        a.update(p.getA());
        f.update(p.getF());
        g.update(p.getG());
        h.update(p.getH());
    }

    /**
     * setzt den Beweiser neu
     *
     * @param b neuer Beweisender
     */
    public void setBeweiser(GBeweiser b) {
        this.p = b;
        update();
    }
}

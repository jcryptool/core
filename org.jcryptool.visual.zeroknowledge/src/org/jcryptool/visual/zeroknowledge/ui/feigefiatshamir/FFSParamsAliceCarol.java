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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.zeroknowledge.algorithm.feigefiatshamir.FFSAlice;
import org.jcryptool.visual.zeroknowledge.algorithm.feigefiatshamir.FFSCarol;
import org.jcryptool.visual.zeroknowledge.algorithm.feigefiatshamir.FFSPerson;
import org.jcryptool.visual.zeroknowledge.ui.ParamsPerson;

/**
 * Enthält ein Group-Objekt, auf dem die Parameter von Alice oder Carol dargestellt werden.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class FFSParamsAliceCarol extends ParamsPerson {

    private FFSCombiLabel c;
    private FFSPerson p;
    private FFSCombiLabel r;
    private ShowVector s;
    private ShowVector v;
    private FFSCombiLabel x;
    private FFSCombiLabel y;

    /**
     * Konstruktor, der die graphischen Komponenten erstellt und einfuegt. Die Group besteht aus
     * zwei ShowVector-Objekten, die s und v zeigen, sowie mehreren FFS_CombiLabeln, die die anderen
     * Werte anzeigen
     *
     * @param p Person, dessen Werte dargestellt werden soll
     * @param parent Parent-Objekt fuer die graphische Oberflaeche
     */
    public FFSParamsAliceCarol(FFSPerson p, Composite parent) {
        super(parent);
        this.p = p;

        group.setLayout(new GridLayout(2, true));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        group.setText(p.getName());

        Group group_secret = new Group(group, SWT.NONE);
        group_secret.setLayout(new GridLayout());
        group_secret.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        group_secret.setText(Messages.FFS_ParamsAliceCarol_0);

        boolean isAlice = (p instanceof FFSAlice);

        s = new ShowVector(group_secret, "s", isAlice); //$NON-NLS-1$

        r = new FFSCombiLabel(Messages.FFS_ParamsAliceCarol_1, true, group_secret);

        Group group_public = new Group(group, SWT.NONE);
        group_public.setLayout(new GridLayout());
        group_public.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        group_public.setText(Messages.FFS_ParamsAliceCarol_4);
        
        v = new ShowVector(group_public, "v", true); //$NON-NLS-1$

        x = new FFSCombiLabel(Messages.FFS_ParamsAliceCarol_5, true, group_public);

        y = new FFSCombiLabel(Messages.FFS_ParamsAliceCarol_6, true, group_public);

        if (!isAlice) {
            c = new FFSCombiLabel(Messages.FFS_ParamsAliceCarol_7, true, group_public);
        } else {
            //als Spacer. hält Platz für c frei
            new Label(group_public, SWT.NONE);
            
        }
        
        setVisible(true);
    }

    /**
     * Methode zum updaten des Panels
     *
     * @see ParamsPerson#update()
     */
    public void update() {
        s.update(p.getSecret());
        v.update(p.getV());
        if (!p.getR().equals(BigInteger.ZERO)) {
            r.setText(p.getR().toString());
            x.setText(p.getX().toString());
        } else {
            r.setText(Messages.FFS_ParamsAliceCarol_8);
            x.setText(Messages.FFS_ParamsAliceCarol_9);
        }
        if (!p.getY().equals(BigInteger.ZERO)) {
            y.setText(p.getY().toString());
        } else {
            y.setText(Messages.FFS_ParamsAliceCarol_10);
        }
        if (!(p instanceof FFSAlice)) {
            c.update(((FFSCarol) p).getC());
        }
    }
}
// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui;

import java.math.BigInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.zeroknowledge.ModNCalculator;

/**
 * Enthält ein Group-Objekt, das den allgemeinen Parameter n darstellt
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class GeneralParams {

    private Group group;

    private Label n;

    private Label n_content;

    /**
     * Konstruktor für eine Group, dass den allgemeinen Parameter n darstellt. Die Group besteht aus
     * zwei Labeln: eins für die Beschreibung, eines für den Inhalt.
     *
     * @param comp Eltern-Objekt zum Group-Objekt
     */
    public GeneralParams(Composite comp) {
        group = new Group(comp, 0);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = SWT.FILL;
        group.setLayout(gridLayout);
        group.setLayoutData(gridData);

        n = new Label(group, 0);
        n.setText(Messages.GeneralParams_0);

        n_content = new Label(group, 0);
        n_content.setText(Messages.GeneralParams_1);
    }

    /**
     * Methode, um an das Group-Objekt zu kommen
     *
     * @return Group-Objekt
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Methode zum updaten des Panels
     *
     * @param shamir ModNCalculator, der die Modelle enthält
     */
    public void update(ModNCalculator shamir) {
        if (!shamir.getN().equals(BigInteger.ZERO)) {
            n_content.setText(shamir.getN().toString());
        } else {
            n_content.setText(Messages.GeneralParams_2);
        }
    }
}
// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
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
     * Layout von comp muss GridLayout sein.
     * Standard Layout Werte sind:
     * horizontalAlignment = SWT.FILL
     * verticalAlignment = SWT.FILL
     * grabExcessHorizontalSpace = true
     * grabExcessVerticalSpace = false
     *
     * @param comp Eltern-Objekt zum Group-Objekt
     */
    public GeneralParams(Composite comp) {
    	group = new Group(comp, SWT.NONE);
    	group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        n = new Label(group, SWT.NONE);
        n.setText(Messages.GeneralParams_0);
        n.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

        n_content = new Label(group, SWT.NONE);
        n_content.setText(Messages.GeneralParams_1);
        n_content.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
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
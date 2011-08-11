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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.visual.zeroknowledge.Protocol;
import org.jcryptool.visual.zeroknowledge.algorithm.Modell;
import org.jcryptool.visual.zeroknowledge.algorithm.feigefiatshamir.FFSAlice;
import org.jcryptool.visual.zeroknowledge.algorithm.feigefiatshamir.FFSFuncs;
import org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir.FSFuncs;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GFuncs;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MDoor;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MFuncs;

/**
 * Graphische Komponente, die einen "Reset"-Button, einen "neuer Durchlauf"-Button und einen
 * "Exit"-Button enthält.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Buttons {
    private Group group;
    private Button ok;
    private Protocol protocol;
    private Button reset;
    private Button repeatedly;

    /**
     * Konstruktor, der die graphische Komponente erstellt
     *
     * @param pro FeigeFiatShamir-Objekt, in dem die Modelle gespeichert sind
     * @param parent Parent-Objekt für die graphischen Komponenten
     */
    public Buttons(Protocol pro, final Composite parent, final Object par, final FFSAlice alice) {
        this.protocol = pro;

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;

        GridData gridData2 = new GridData();
        gridData2.grabExcessVerticalSpace = true;
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.horizontalSpan = 2;

        group = new Group(parent, 0);
        group.setLayout(gridLayout);
        group.setLayoutData(gridData2);

        reset = new Button(group, SWT.PUSH | SWT.CENTER);
        reset.setText(Messages.Buttons_0);
        reset.setLayoutData(gridData);
        // reset.setBounds(20, 20, 150, 20);
        reset.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom Reset-Button achtet
         */
        new SelectionAdapter() {
            /**
             * setzt das Protokoll auf den Anfangszustand zurück
             */
            public void widgetSelected(SelectionEvent arg0) {
                protocol.reset();
            }
        });
        reset.setToolTipText(Messages.Buttons_1);
        ok = new Button(group, SWT.PUSH | SWT.CENTER);
        ok.setText(Messages.Buttons_2);
        ok.setLayoutData(gridData);
        ok.setEnabled(false);
        ok.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom "neuer Durchlauf"-Button achtet
         */
        new SelectionAdapter() {
            /**
             * setzt die sich ständig ändernden Parameter und den Lauf des Protokolls zurück
             */
            public void widgetSelected(SelectionEvent arg0) {
                protocol.resetNotSecret();
            }
        });
        ok.setToolTipText(Messages.Buttons_3);

        // Modul zum Darstellen des Buttons zum mehrfachen Durchführen des
        // Protokolls
        repeatedly = new Button(group, SWT.PUSH | SWT.CENTER);
        repeatedly.setText(Messages.Buttons_4);
        repeatedly.setLayoutData(gridData);
        repeatedly.setEnabled(true);
        repeatedly.addSelectionListener(

        /**
         * SelectionAdapter, der darauf achtet, ob der Button "Mehrmals durchführen" betätigt wurde.
         */
        new SelectionAdapter() {
            /**
             * öffnet ein M_Repeat-Dialog, der das Protokoll mehrmals durchführen kann.
             */
            public void widgetSelected(SelectionEvent ev) {
                if (alice != null) {
                    // When FeigeFiatShamir
                    new Repeat(parent.getShell(), new FFSFuncs(alice, (Modell) par), "FFS."); //$NON-NLS-1$
                } else {
                    if (par == null) { // Graph
                        new Repeat(parent.getShell(), new GFuncs(), ""); //$NON-NLS-1$
                    } else if ("org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MDoor".equals(par.getClass().getName())) { // MagicDoor //$NON-NLS-1$
                        new Repeat(parent.getShell(), new MFuncs((MDoor) par), ""); //$NON-NLS-1$
                    } else if ("org.jcryptool.visual.zeroknowledge.algorithm.Modell".equals(par.getClass().getName())) { // FiatShamir //$NON-NLS-1$
                        new Repeat(parent.getShell(), new FSFuncs((Modell) par), ""); //$NON-NLS-1$
                    }
                }
            }
        });
        repeatedly.setToolTipText(Messages.Buttons_11);
    }

    /**
     * aktiviert oder deaktiviert den "Neuer Durchlauf"-Button
     *
     * @param enable true, wenn aktiviert werden soll, false, wenn deaktiviert werden soll
     */
    public void enableOK(boolean enable) {
        ok.setEnabled(enable);
        if (enable)
            ok.setFocus();
    }

    public void enableMehrmals(boolean enable) {
        repeatedly.setEnabled(enable);
    }

    /**
     * Methode zum Erhalten des Group-Objektes
     *
     * @return Group-Objekt mit den graphischen Komponenten
     */
    public Group getGroup() {
        return group;
    }
}

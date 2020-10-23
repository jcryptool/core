// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020 JCrypTool Team and Contributors
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
    private FFSAlice _alice;

    /**
     * Konstruktor, der die graphische Komponente erstellt
     *
     * @param pro FeigeFiatShamir-Objekt, in dem die Modelle gespeichert sind
     * @param parent Parent-Objekt für die graphischen Komponenten
     */
    public Buttons(Protocol pro, final Composite parent, final Object par, final FFSAlice alice) {
        this.protocol = pro;
        this._alice = alice;

        group = new Group(parent, SWT.NONE);
        group.setLayout(new GridLayout(3, true));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

        reset = new Button(group, SWT.PUSH);
        reset.setText(Messages.Buttons_0);
        reset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
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
        
        ok = new Button(group, SWT.PUSH);
        ok.setText(Messages.Buttons_2);
        ok.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
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
        repeatedly = new Button(group, SWT.PUSH);
        repeatedly.setText(Messages.Buttons_4);
        repeatedly.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
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
                if (_alice != null) {
                    // When FeigeFiatShamir
                    new Repeat(parent.getShell(), new FFSFuncs(_alice, (Modell) par), "FFS."); //$NON-NLS-1$
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
    
    public void setAlgorithmData(FFSAlice alice) {
    	this._alice = alice;
    }
}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.views;

import java.math.BigInteger;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.zeroknowledge.ModNCalculator;
import org.jcryptool.visual.zeroknowledge.Protocol;
import org.jcryptool.visual.zeroknowledge.algorithm.Modell;
import org.jcryptool.visual.zeroknowledge.algorithm.feigefiatshamir.FFSAlice;
import org.jcryptool.visual.zeroknowledge.algorithm.feigefiatshamir.FFSBob;
import org.jcryptool.visual.zeroknowledge.algorithm.feigefiatshamir.FFSCarol;
import org.jcryptool.visual.zeroknowledge.ui.Buttons;
import org.jcryptool.visual.zeroknowledge.ui.Introduction;
import org.jcryptool.visual.zeroknowledge.ui.PrimeGenerator;
import org.jcryptool.visual.zeroknowledge.ui.feigefiatshamir.FFSFlow;
import org.jcryptool.visual.zeroknowledge.ui.feigefiatshamir.FFSParamsAliceCarol;
import org.jcryptool.visual.zeroknowledge.ui.feigefiatshamir.FFSParamsBob;

public class FeigeFiatShamirView extends ViewPart implements Observer, ModNCalculator {
    private FFSAlice alice;
    private Combo anzahl;
    private FFSBob bob;
    private Buttons buttons;
    private FFSCarol carol;
    private Introduction constOne;
    private FFSFlow flow;
    private Composite main;
    private Modell modell;
    private FFSParamsAliceCarol paramsAlice;
    private FFSParamsBob paramsBob;
    private FFSParamsAliceCarol paramsCarol;
    private PrimeGenerator prime;
    private Group info;
    // Amount of entries in a vector
    private static final int vectorEntries = 4;
    private Composite parent;

    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        // Define layout elements
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.makeColumnsEqualWidth = true;
        parent.setLayoutData(gridData);

        // Create srollable composite and composite within it
        ScrolledComposite sc =
                new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        sc.setLayoutData(gridData);
        Composite pageComposite = new Composite(sc, SWT.NONE);
        sc.setContent(pageComposite);
        pageComposite.setLayout(gridLayout);
        pageComposite.setLayoutData(gridData);

        // pointer main points to pageComposite
        main = pageComposite;

        // Modelle
        modell = new Modell();
        bob = new FFSBob(vectorEntries);
        alice = new FFSAlice(vectorEntries);
        carol = new FFSCarol(vectorEntries);

        // bei den Modellen als Observer anmelden
        modell.addObserver(this);
        bob.addObserver(this);
        alice.addObserver(this);
        carol.addObserver(this);

        // Einfuehrung und Auswahl, ob das Geheimnis bekannt sein soll
        constOne = new Introduction(this, main, "FFS"); //$NON-NLS-1$
        constOne.getGroup().setLocation(0, 0);

        // Modul zum Erstellen von n
        prime = new PrimeGenerator(this, main);

        // Layout for Action-Flow group
        GridLayout gridLayoutP = new GridLayout();
        gridLayoutP.numColumns = 1;
        gridLayoutP.makeColumnsEqualWidth = false;
        Group action = new Group(main, SWT.None);
        action.setText(Messages.FeigeFiatShamirView_1);
        action.setLayout(gridLayoutP);
        action.setLayoutData(gridData);

        // Modul für den Durchgang
        flow = new FFSFlow(this, action);

        // Modul mit drei Buttons
        buttons = new Buttons(this, action, modell, alice);
        buttons.enableMehrmals(false);

        // Layout for information group
        info = new Group(main, SWT.None);
        GridLayout gridLayoutI = new GridLayout();
        gridLayoutI.numColumns = 2;
        gridLayoutI.makeColumnsEqualWidth = false;
        GridData gridDataI = new GridData();
        gridDataI.grabExcessVerticalSpace = true;
        gridDataI.grabExcessHorizontalSpace = true;
        gridDataI.horizontalAlignment = GridData.FILL;
        gridDataI.horizontalSpan = 1;
        info.setLayout(gridLayoutI);
        info.setLayoutData(gridDataI);
        info.setText(Messages.FeigeFiatShamirView_2);

        new Label(info, 0);
        Composite vectors = new Composite(info, 0);
        GridLayout vectorLayout = new GridLayout();
        vectorLayout.numColumns = 2;
        vectors.setLayout(vectorLayout);

        // Text zum Combo-Objekt
        Label label = new Label(vectors, SWT.RIGHT);
        label.setText(Messages.FeigeFiatShamirView_3);
        // Combo zum Angeben, wie viele Einträge die Vektoren haben sollen
        anzahl = new Combo(vectors, SWT.DROP_DOWN | SWT.READ_ONLY);
        anzahl.add("1"); //$NON-NLS-1$
        anzahl.add("2"); //$NON-NLS-1$
        anzahl.add("3"); //$NON-NLS-1$
        anzahl.add("4"); //$NON-NLS-1$
        anzahl.setText(String.valueOf(vectorEntries));
        anzahl.setToolTipText(Messages.FeigeFiatShamirView_8);
        anzahl.addSelectionListener(
        /**
         * SelectionAdapter, der darauf achtet, wenn das Combi-Objekt verändert wurde
         */
        new SelectionAdapter() {
            /**
             * Diese Methode setzt die Anzahl der Einträge in den Vektoren. Wenn der Modul n noch
             * nicht gesetzt war, wird der Button zum Generieren des Geheimnisses nicht
             * freigeschaltet.
             */
            public void widgetSelected(SelectionEvent e) {
                int index = anzahl.getSelectionIndex() + 1;
                resetNotSecret();
                flow.disableAll();
                BigInteger n = alice.getN();
                alice = new FFSAlice(index);
                carol = new FFSCarol(index);
                bob = new FFSBob(index);
                alice.addObserver(FeigeFiatShamirView.this);
                bob.addObserver(FeigeFiatShamirView.this);
                carol.addObserver(FeigeFiatShamirView.this);
                if (!n.equals(BigInteger.ZERO)) {
                    alice.setN(modell.getP(), modell.getQ());
                    bob.setN(modell.getP(), modell.getQ());
                    carol.setN(modell.getP(), modell.getQ());
                    prime.getSecret().setEnabled(true);
                }

                paramsBob.getGroup().dispose();
                paramsBob = new FFSParamsBob(bob, info);

                // If situation=alice -> redraw alice, else redraw carol
                if (paramsAlice != null) {
                    paramsAlice.getGroup().dispose();
                    paramsAlice = new FFSParamsAliceCarol(alice, info);
                } else {
                    paramsCarol.getGroup().dispose();
                    paramsCarol = new FFSParamsAliceCarol(carol, info);
                }

                // do a new lay-out because size of the boxes may have changed
                info.layout(true);
            }
        });

        // Modul zum Darstellen der Parameter von Bob
        paramsBob = new FFSParamsBob(bob, info);

        // Modul zum Darstellen der Parameter von Alice
        paramsAlice = new FFSParamsAliceCarol(alice, info);

        // Modul zum Darstellen der Parameter von Carol
        // params_carol = new FFS_ParamsAliceCarol(carol, info);
        // params_carol.setVisible(false);

        main.setVisible(true);

        // make whole content scrollable
        // main.setSize(main.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        sc.setMinSize(pageComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(),
                "org.jcryptool.visual.zeroknowledge.feigefiatshamirView"); //$NON-NLS-1$
    }

    /**
     * macht das Betätigen der Buttons im Flow-Objekt unmöglich
     *
     * @see ModNCalculator#disableAllInFlow()
     */
    public void disableAllInFlow() {
        flow.disableAll();
    }

    /**
     * gibt das Alice-Objekt zurück
     *
     * @return Alice-Objekt
     */
    public FFSAlice getAlice() {
        return alice;
    }

    /**
     * gibt das Bob-Objekt zurück
     *
     * @return Bob-Objekt
     */
    public FFSBob getBob() {
        return bob;
    }

    /**
     * gibt das Buttons-Objekt der graphischen Oberfläche zurück
     *
     * @return Buttons-Objekt
     */
    public Buttons getButtons() {
        return buttons;
    }

    public void setSecret() {
        alice.generateSecret();
        bob.setV(alice.getV());
        carol.generateSecret();
        flow.enableFirst();
        buttons.enableMehrmals(true);
    }

    /**
     * gibt das Carol-Objekt zurück
     *
     * @return Carol-Objekt
     */
    public FFSCarol getCarol() {
        return carol;
    }

    /**
     * Methode zum Erhalten des Flow-Objektes
     *
     * @return Flow-Objekt
     */
    public FFSFlow getFlow() {
        return flow;
    }

    /**
     * gibt das Modell zurück
     *
     * @return Modell, das die Primzahlen p und q enthält
     */
    public Modell getModell() {
        return modell;
    }

    /**
     * gibt den generierten Wert für n zurück
     *
     * @return Wert von n
     */
    public BigInteger getN() {
        return alice.getN();
    }

    /**
     * gibt das Objekt zurück, in dem die graphische Oberfläche gespeichert ist, die die Parameter
     * von Bob darstellt.
     *
     * @return Objekt, das die graphische Oberfläche enthält, die Bobs Parameter darstellt
     */
    public FFSParamsBob getParamsBob() {
        return paramsBob;
    }

    /**
     * gibt den PrimeGenerator zurück
     *
     * @return PrimeGenerator im Hauptcomposite
     */
    public PrimeGenerator getPrimeGen() {
        return prime;
    }

    /**
     * gibt den Button zurück, der die Erstellung des Geheimnisses aufruft
     *
     * @return Button, der für die Erstellung des Geheimnisses zuständig ist
     */
    public Button getSecret() {
        return prime.getSecret();
    }

    /**
     * Entfernt das Label, das Fehlermeldungen anzeigt
     *
     * @see ModNCalculator#removeException()
     */
    public void removeException() {
        prime.removeException();
    }

    /**
     * macht das "Verifiziert"-Feld unsichtbar
     *
     * @see ModNCalculator#removeVerifingItem
     */
    public void removeVerifingItem() {
        paramsBob.getVerifiziert().setVisible(false);
    }

    /**
     * setzt alle Werte zurück, inclusive n und des Geheimnisses
     *
     * @see Protocol#reset()
     */
    public void reset() {
        alice.reset();
        bob.reset();
        carol.reset();
        modell.reset();
        removeVerifingItem();
        flow.disableAll();
        flow.setStep(0);
        paramsBob.verifizieren(false);
    }

    /**
     * setzt alle Werte zurück außer dem Geheimnis und allen damit verbundenen Werten.
     *
     * @see Protocol#reset()
     */
    public void resetNotSecret() {
        getAlice().resetNotSecret();
        getBob().resetNotSecret();
        getCarol().resetNotSecret();
        buttons.enableOK(false);
        getFlow().enableFirst();
        getFlow().setStep(0);
        removeVerifingItem();
        getParamsBob().verifizieren(false);
    }

    /**
     * macht in Abhängigkeit von b die graphischen Komponenten sichtbar
     *
     * @param b true, wenn die Komponenten für den ersten Fall sichtbar sein sollen, false sonst
     * @see Protocol#setFirstCase(boolean)
     */
    public void setFirstCase(boolean b) {
        if (b) {
            if (paramsAlice != null) {
                Control[] children = paramsAlice.getGroup().getChildren();
                for (Control child : children)
                    child.dispose();
                paramsAlice.getGroup().dispose();
                paramsAlice = null;
            }
            paramsAlice = new FFSParamsAliceCarol(alice, info);
        } else {
            if (paramsAlice != null) {
                Control[] children = paramsAlice.getGroup().getChildren();
                for (Control child : children)
                    child.dispose();
                paramsAlice.getGroup().dispose();
                paramsAlice = null;
            }
            paramsAlice = new FFSParamsAliceCarol(carol, info);
        }
        info.layout(true);

        boolean gleich0 = true;
        for (BigInteger i : alice.getSecret()) {
            gleich0 &= i.equals(BigInteger.ZERO);
        }
        if (!gleich0) {
            alice.resetNotSecret();
            carol.resetNotSecret();
            flow.enableFirst();
            buttons.enableOK(false);
            buttons.enableMehrmals(true);
        }
        flow.setFirstCase(b);
        removeVerifingItem();
    }

    /**
     * Methode zum Setzen von p im Modell. Wenn q schon gesetzt ist, wird in den einzelnen Modellen
     * n gesetzt.
     *
     * @param p_string String, der zum BigInteger geparst wird, und dann als Primzahl gesetzt wird
     * @see ModNCalculator#setP(String)
     */
    public boolean setP(String p_string) {
        BigInteger p;
        try {
            p = new BigInteger(p_string);
        } catch (NumberFormatException e) {
            prime.setException(Messages.FeigeFiatShamirView_9);
            return false;
        }
        modell.setP(p);
        if (!modell.getQ().equals(BigInteger.ZERO)) {
            alice.setN(p, modell.getQ());
            bob.setN(p, modell.getQ());
            carol.setN(p, modell.getQ());
        }
        prime.removeException();
        return true;
    }

    /**
     * Methode zum Setzen von q im Modell. Wenn p schon gesetzt ist, wird in den einzelnen Modellen
     * n gesetzt.
     *
     * @param q_string String, der zum BigInteger geparst wird, und dann als Primzahl gesetzt wird
     * @see ModNCalculator#setQ(String)
     */
    public boolean setQ(String q_string) {
        BigInteger q;
        try {
            q = new BigInteger(q_string);
        } catch (NumberFormatException e) {
            prime.setException(Messages.FeigeFiatShamirView_10);
            return false;
        }
        modell.setQ(q);
        if (!modell.getP().equals(BigInteger.ZERO)) {
            alice.setN(modell.getP(), q);
            bob.setN(modell.getP(), q);
            carol.setN(modell.getP(), q);
        }
        prime.removeException();
        update(null, null);
        return true;
    }

    /**
     * Methode zum Updaten des Composites. Dies geschieht in erster Linie durch das Updaten der
     * einzelnen Komponenten des Composites
     *
     * @see Observer#update(Observable, Object)
     */
    public void update(Observable arg0, Object arg1) {
        if (paramsAlice != null)
            this.paramsAlice.update();
        this.paramsBob.update();
        if (paramsCarol != null)
            this.paramsCarol.update();
        this.prime.update(this);
    }

    @Override
    public void setFocus() {
        parent.setFocus();
    }
}
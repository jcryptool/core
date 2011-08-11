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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.zeroknowledge.ModNCalculator;
import org.jcryptool.visual.zeroknowledge.algorithm.Modell;
import org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir.FSAlice;
import org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir.FSBob;
import org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir.FSCarol;
import org.jcryptool.visual.zeroknowledge.ui.Buttons;
import org.jcryptool.visual.zeroknowledge.ui.Introduction;
import org.jcryptool.visual.zeroknowledge.ui.PrimeGenerator;
import org.jcryptool.visual.zeroknowledge.ui.fiatshamir.FSFlow;
import org.jcryptool.visual.zeroknowledge.ui.fiatshamir.FSParamsAliceCarol;
import org.jcryptool.visual.zeroknowledge.ui.fiatshamir.FSParamsBob;

public class FiatShamirView extends ViewPart implements Observer, ModNCalculator {

    private FSAlice alice;
    private FSBob bob;
    private Buttons buttons;
    private FSCarol carol;
    private FSFlow flow;
    private Composite main;
    private Modell modell;
    private FSParamsAliceCarol paramsAC;
    private FSParamsBob params_bob;
    private FSParamsAliceCarol params_carol;
    private PrimeGenerator prime;
    private Group info;
    private Composite parent;

    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        // Define layout elements
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = false;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = SWT.FILL;
        gridData.verticalAlignment = SWT.BEGINNING;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.makeColumnsEqualWidth = true;
        // parent.setLayoutData(gridData);

        // Create srollable composite and composite within it
        ScrolledComposite sc =
                new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        sc.setLayoutData(gridData);

        // gridlayout for elements
        Composite pageComposite = new Composite(sc, SWT.NONE);
        sc.setContent(pageComposite);
        pageComposite.setLayout(gridLayout);
        pageComposite.setLayoutData(gridData);

        // pointer main points to pageComposite
        main = pageComposite;

        // Modelle
        modell = new Modell();
        bob = new FSBob();
        alice = new FSAlice();
        carol = new FSCarol();

        // bei den Modellen als Observer anmelden
        modell.addObserver(this);
        bob.addObserver(this);
        alice.addObserver(this);
        carol.addObserver(this);

        GridData gridData2 = new GridData();
        gridData2.grabExcessVerticalSpace = false;
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.horizontalAlignment = GridData.FILL;

        Introduction situation = new Introduction(this, main, "FS"); //$NON-NLS-1$
        situation.getGroup().setLayoutData(gridData2);

        // Modul zum Erstellen von n
        prime = new PrimeGenerator(this, main);
        // prime.getGroup().setLocation(0, 89);

        // Layout for Action-Flow group
        GridLayout gridLayoutP = new GridLayout();
        gridLayoutP.numColumns = 1;
        gridLayoutP.makeColumnsEqualWidth = false;
        Group action = new Group(main, SWT.None);
        action.setText(Messages.FiatShamirView_1);
        action.setLayout(gridLayoutP);
        action.setLayoutData(gridData);

        // Modul für den Durchgang
        flow = new FSFlow(this, action);

        // "reset", "rerun", "several runs" buttons
        buttons = new Buttons(this, action, modell, null);
        buttons.enableMehrmals(false);

        // Layout for information group
        info = new Group(main, SWT.None);
        GridLayout gridLayoutI = new GridLayout();
        gridLayoutI.numColumns = 2;
        gridLayoutI.makeColumnsEqualWidth = false;
        GridData gridDataI = new GridData();
        gridDataI.grabExcessVerticalSpace = false;
        gridDataI.grabExcessHorizontalSpace = true;
        gridDataI.horizontalAlignment = GridData.FILL;
        gridDataI.horizontalSpan = 1;
        info.setLayout(gridLayoutI);
        info.setLayoutData(gridDataI);
        info.setText(Messages.FiatShamirView_2);

        // Modul zum Darstellen der Parameter von Bob
        params_bob = new FSParamsBob(bob, info);

        // Modul zum Darstellen der Parameter von Alice
        paramsAC = new FSParamsAliceCarol(alice, info);

        // Modul zum Darstellen der Parameter von Carol
        // params_carol = new FS_ParamsAliceCarol(carol, info);
        // params_carol.getGroup().setVisible(false);

        main.setVisible(true);

        sc.setMinSize(pageComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(),
                "org.jcryptool.visual.zeroknowledge.fiatshamirView"); //$NON-NLS-1$
    }

    /**
     * Methode, die das Betätigen der Buttons im Flow unmöglich macht.
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
    public FSAlice getAlice() {
        return alice;
    }

    public void setSecret() {
        alice.generateSecret();
        bob.setV(alice.getV());
        carol.generateSecret();
        flow.enableFirst();
        buttons.enableMehrmals(true);
    }

    /**
     * gibt das Bob-Objekt zurück
     *
     * @return Bob-Objekt
     */
    public FSBob getBob() {
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

    /**
     * gibt das Carol-Objekt zurück
     *
     * @return Carol-Objekt
     */
    public FSCarol getCarol() {
        return carol;
    }

    /**
     * gibt den Flow der graphsichen Oberfläche zurück
     *
     * @return Flow der graphischen Oberfläche
     */
    public FSFlow getFlow() {
        return flow;
    }

    /**
     * gibt das Modell zurück
     *
     * @return Modell der Applikation
     */
    public Modell getModell() {
        return modell;
    }

    /**
     * Methode, um n zu erhalten.
     *
     * @return Modul n
     */
    public BigInteger getN() {
        return alice.getN();
    }

    /**
     * gibt das Objekt zurück, das die Parameter von Bob darstellt
     *
     * @return Objekt, das die Parameter von Bob darstellt
     */
    public FSParamsBob getParamsBob() {
        return params_bob;
    }

    /**
     * gibt den PrimeGenerator zurück
     *
     * @return PrimeGenerator, der auf dem Haupt-Composite sichtbar ist
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
     * schaltet das Label für Exceptions aus
     */
    public void removeException() {
        prime.removeException();
    }

    /**
     * macht das "Verifiziert"-Feld unsichtbar
     */
    public void removeVerifingItem() {
        params_bob.getVerifiziert().setVisible(false);
    }

    /**
     * setzt das Protokoll zurück
     */
    public void reset() {
        alice.reset();
        bob.reset();
        carol.reset();
        modell.reset();
        removeVerifingItem();
        flow.disableAll();
        flow.setStep(0);
        buttons.enableMehrmals(false);
        params_bob.verifizieren(false);
    }

    /**
     * setzt alles außer dem Geheimnis und v zurück
     */
    public void resetNotSecret() {
        alice.resetNotSecret();
        bob.resetNotSecret();
        carol.resetNotSecret();
        buttons.enableOK(false);
        buttons.enableMehrmals(false);
        flow.enableFirst();
        flow.setStep(0);
        removeVerifingItem();
        params_bob.verifizieren(false);
    }

    /**
     * macht in Abhängigkeit von b die graphischen Komponenten sichtbar
     *
     * @param b true, wenn die Komponenten für den ersten Fall sichtbar sein sollen, false sonst
     */
    public void setFirstCase(boolean b) {
        if (b) {
            params_carol.getGroup().dispose();
            params_carol = null;
            paramsAC = new FSParamsAliceCarol(alice, info);
        } else {
            paramsAC.getGroup().dispose();
            paramsAC = null;
            params_carol = new FSParamsAliceCarol(carol, info);
        }
        info.layout(true);

        if (!alice.getSecret().equals(BigInteger.ZERO)) {
            alice.resetNotSecret();
            carol.resetNotSecret();
            flow.enableFirst();
            buttons.enableOK(false);
        }
        bob.resetNotSecret();
        flow.setFirstCase(b);
        removeVerifingItem();
    }

    /**
     * Methode zum Setzen von p im Modell. Wenn q schon gesetzt ist, wird in den einzelnen Modellen
     * n gesetzt.
     *
     * @param p_string String, der zum BigInteger geparst wird, und dann als Primzahl gesetzt wird
     */
    public boolean setP(String p_string) {
        BigInteger p;
        try {
            p = new BigInteger(p_string);
        } catch (NumberFormatException e) {
            prime.setException(Messages.FiatShamirView_3);
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
     */
    public boolean setQ(String q_string) {
        BigInteger q;
        try {
            q = new BigInteger(q_string);
        } catch (NumberFormatException e) {
            prime.setException(Messages.FiatShamirView_4);
            return false;
        }
        modell.setQ(q);
        if (!modell.getP().equals(BigInteger.ZERO)) {
            alice.setN(modell.getP(), q);
            bob.setN(modell.getP(), q);
            carol.setN(modell.getP(), q);
        }
        prime.removeException();
        return true;
    }

    /**
     * Methode zum Updaten der einzelnen Komponenten. Dies geschieht in erster Linie durch das
     * Updaten der einzelnen Komponenten des Composites
     */
    public void update(Observable arg0, Object arg1) {
        if (paramsAC != null)
            this.paramsAC.update();
        this.params_bob.update();
        if (params_carol != null)
            this.params_carol.update();
        this.prime.update(this);
    }

    @Override
    public void setFocus() {
        parent.setFocus();
    }
}

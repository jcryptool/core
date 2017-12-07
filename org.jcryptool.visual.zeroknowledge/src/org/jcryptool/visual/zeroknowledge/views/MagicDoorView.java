// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.views;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.zeroknowledge.Protocol;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MAlice;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MBob;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MDoor;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MPerson;
import org.jcryptool.visual.zeroknowledge.ui.Buttons;
import org.jcryptool.visual.zeroknowledge.ui.Introduction;
import org.jcryptool.visual.zeroknowledge.ui.magischetuer.Gebaeude;
import org.jcryptool.visual.zeroknowledge.ui.magischetuer.MFlow;
import org.jcryptool.visual.zeroknowledge.ui.magischetuer.MParamsAliceCarol;
import org.jcryptool.visual.zeroknowledge.ui.magischetuer.MParamsBob;

/**
 * Creates a view for the magical door (ZeroKnowledge).
 *
 * @author
 * @version
 */
public class MagicDoorView extends ViewPart implements Observer, Protocol {
    private MAlice alice;
    private MBob bob;
    private Buttons buttons;
    private MPerson carol;
    private MDoor door;
    private Label descAliceCarol;
    private Label descBob;
    private MFlow flow;
    private Gebaeude building;
    private MParamsAliceCarol paramsAC;
    private MParamsBob paramsBob;
    private Group info;
    private Composite parent;
    private Composite pageComposite;

    @Override
    public void createPartControl(final Composite parent) {
        this.parent = parent;

        ScrolledComposite sc =
                new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);

        pageComposite = new Composite(sc, SWT.NONE);
        sc.setContent(pageComposite);
        pageComposite.setLayout(new GridLayout());
        
		ZKHeaderComposite headerComp = new ZKHeaderComposite(pageComposite);
		headerComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

        bob = new MBob();
        alice = new MAlice();
        carol = new MPerson();
        door = new MDoor(1, 2, true, 10);
        alice.setCode(door.getCode());

        // bei den Modellen als Observer anmelden
        bob.addObserver(this);
        alice.addObserver(this);
        carol.addObserver(this);

        new Introduction(this, pageComposite, "M");
        
        // Layout for Action-Flow group
        Group action = new Group(pageComposite, SWT.NONE);
        action.setText(Messages.MagicDoorView_0);
        action.setLayout(new GridLayout(2, false));
        action.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        // Process-flow graph
        flow = new MFlow(this, action);
        flow.enableFirst();

        // Zeichnung des Gebäudes, A, B und C
        building = new Gebaeude(action, bob, door);

        // Creates buttons "reset", "rerun", "exit"
        buttons = new Buttons(this, action, door, null);

        // Layout for information group
        info = new Group(pageComposite, SWT.NONE);
        info.setLayout(new GridLayout(2, true));
        info.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        info.setText(Messages.MagicDoorView_29);

        paramsBob = new MParamsBob(bob, info);
        paramsAC = new MParamsAliceCarol(alice, info);

        paramsAC.setVisible(true);
        paramsAC.update();

        descAliceCarol = new Label(info, SWT.WRAP);
        descAliceCarol.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        descBob = new Label(info, SWT.WRAP);
        descBob.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        sc.setMinSize(pageComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(),
                "org.jcryptool.visual.zeroknowledge.magicdoorView"); //$NON-NLS-1$
    }

    /**
     * gibt das Alice-Objekt zurück
     *
     * @return Alice-Objekt
     */
    public MAlice getAlice() {
        return alice;
    }

    /**
     * gibt das Bob-Objekt zurück
     *
     * @return Bob-Objekt
     */
    public MBob getBob() {
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
    public MPerson getCarol() {
        return carol;
    }

    /**
     * Methode zum Erhalten des Door-Objektes
     *
     * @return Door-Objekt
     */
    public MDoor getDoor() {
        return door;
    }

    /**
     * gibt den Flow der graphischen Oberfläche zurück
     *
     * @return Flow der graphischen Oberfläche
     */
    public MFlow getFlow() {
        return flow;
    }

    /**
     * Methode zum Erhalten des Objektes, auf dem der Canvas liegt, auf dem das Gebäude gezeichnet
     * wird
     *
     * @return Gebäude-Objekt, auf dem der Canvas liegt
     */
    public Gebaeude getGebaeude() {
        return building;
    }

    /**
     * gibt das Panel zurück, das die Parameter von Bob darstellt
     *
     * @return Panel mit den Parametern von Bob
     */
    public MParamsBob getParamsBob() {
        return paramsBob;
    }

    /**
     * macht das "Verifiziert"-Feld unsichtbar
     */
    public void removeVerifingItem() {
        paramsBob.getVerifiziert().setVisible(false);
    }

    /**
     * setzt das Protokoll zurück auf den Anfangszustand
     *
     * @see Protocol#reset()
     */
    public void reset() {
        alice.reset();
        bob.reset();
        carol.reset();
        door.createCode();
        alice.setCode(getDoor().getCode());
        building.setStep(0, 0, false);
        removeVerifingItem();
        flow.enableFirst();
        flow.setStep(0);
        buttons.enableOK(false);
        paramsBob.verifizieren(false);
        setText(7);
    }

    /**
     * setzt alle Werte bis auf das Geheimnis und alles, was damit zu tun hat, zurück
     *
     * @see Protocol#resetNotSecret()
     */
    public void resetNotSecret() {
        alice.resetNotSecret();
        bob.resetNotSecret();
        carol.resetNotSecret();
        buttons.enableOK(false);
        flow.enableFirst();
        flow.setStep(0);
        removeVerifingItem();
        building.setStep(0, 0, false);
        paramsBob.verifizieren(false);
        setText(7);
    }

    /**
     * macht in Abhängigkeit von b die graphischen Komponenten sichtbar
     *
     * @param firstCase true, wenn die Komponenten für den ersten Fall sichtbar sein sollen, false
     *        sonst
     * @see Protocol#setFirstCase(boolean)
     */
    public void setFirstCase(boolean firstCase) {
        // reset when changing situation
        this.resetNotSecret();
        if (firstCase) {
            paramsAC.setPerson(alice);
        } else {
            paramsAC.setPerson(carol);
        }
        info.layout(true);
        building.setSecretKnown(firstCase);
        building.setStep(0, 0, false);
        bob.resetNotSecret();
        flow.setFirstCase(firstCase);
        flow.enableFirst();
        buttons.enableOK(false);
        removeVerifingItem();
        setText(7);
    }

    /**
     * Methode zum Setzen der fallabhängigen Seitentexte
     *
     * @param fall Fall, der betrachtet werden soll
     */
    public void setText(int fall) {
        descAliceCarol.setText(""); //$NON-NLS-1$
        descBob.setText(""); //$NON-NLS-1$
        switch (fall) {
            case 0:
                descAliceCarol.setText(Messages.MagicDoorView_1);
                break;
            case 1:
                descAliceCarol.setText(Messages.MagicDoorView_2);
                break;
            case 2:
                descAliceCarol.setText(Messages.MagicDoorView_3);
                break;
            case 3:
                descAliceCarol.setText(Messages.MagicDoorView_4);
                break;
            case 8:
                descAliceCarol.setText(Messages.MagicDoorView_5);
                break;
            case 9:
                descAliceCarol.setText(Messages.MagicDoorView_6);
                break;
            case 16:
                descAliceCarol.setText(Messages.MagicDoorView_7);
                break;
            case 17:
                descAliceCarol.setText(Messages.MagicDoorView_8);
                break;
            case 32:
                descAliceCarol.setText(Messages.MagicDoorView_9);
                descBob.setText(Messages.MagicDoorView_10);
                break;
            case 33:
                descAliceCarol.setText(Messages.MagicDoorView_11);
                descBob.setText(Messages.MagicDoorView_12);
                break;
            case 64:
                descAliceCarol.setText(Messages.MagicDoorView_13);
                descBob.setText(Messages.MagicDoorView_14);
                break;
            case 65:
                descAliceCarol.setText(Messages.MagicDoorView_15);
                descBob.setText(Messages.MagicDoorView_16);
                break;
            case 128:
                descAliceCarol.setText(Messages.MagicDoorView_17);
                descBob.setText(Messages.MagicDoorView_18);
                break;
            case 129:
                descAliceCarol.setText(Messages.MagicDoorView_19);
                descBob.setText(Messages.MagicDoorView_20);
                break;
            case 256:
                descAliceCarol.setText(Messages.MagicDoorView_21);
                descBob.setText(Messages.MagicDoorView_22);
                break;
            case 257:
                descAliceCarol.setText(Messages.MagicDoorView_23);
                descBob.setText(Messages.MagicDoorView_24);
                break;
            case 512:
                descAliceCarol.setText(Messages.MagicDoorView_25);
                break;
            case 513:
                descAliceCarol.setText(Messages.MagicDoorView_26);
                break;
            case 1024:
                descBob.setText(Messages.MagicDoorView_27);
                break;
            case 1025:
                descBob.setText(Messages.MagicDoorView_28);
                break;
        }
        pageComposite.layout();
        return;
    }

    /**
     * Methode zum Updaten der Hauptframes. Dies geschieht in erster Linie durch das Updaten der
     * einzelnen Komponenten des MainFrame
     *
     * @see Observer#update(Observable, Object)
     */
    public void update(Observable arg0, Object arg1) {
        paramsAC.update();
        paramsBob.update();
    }

    @Override
    public void setFocus() {
        parent.setFocus();
    }
}
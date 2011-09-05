// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.elGamal.ui;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.elGamal.Action;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;

/**
 * the view displaying this visualization.
 *
 * @author Michael Gaber
 */
public class ElGamalView extends ViewPart {

    @Override
    public final void createPartControl(final Composite parent) {
        final HashMap<Action, ElGamalData> datas = new HashMap<Action, ElGamalData>();
        final TabFolder tf = new TabFolder(parent, SWT.TOP);

        // Encrypt
        TabItem ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.ElGamalComposite_encrypt);
        ScrolledComposite sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        ElGamalComposite c = new ElGamalComposite(sc, SWT.NONE, Action.EncryptAction, datas);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);

        // Decrypt
        ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.ElGamalComposite_decrypt);
        sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        c = new ElGamalComposite(sc, SWT.NONE, Action.DecryptAction, datas);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);

        // Sign
        ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.ElGamalComposite_sign);
        sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        c = new ElGamalComposite(sc, SWT.NONE, Action.SignAction, datas);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);

        // Verify
        ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.ElGamalComposite_verify);
        sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        c = new ElGamalComposite(sc, SWT.NONE, Action.VerifyAction, datas);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);

        // Sofern ich noch ne help schreibe
        // PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), "org.jcryptool.visual.rsa.rsahelp");
    }

    @Override
    public void setFocus() {
    }
}

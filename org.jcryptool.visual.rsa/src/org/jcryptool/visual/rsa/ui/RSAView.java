// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.rsa.ui;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.rsa.Action;
import org.jcryptool.visual.rsa.Messages;
import org.jcryptool.visual.rsa.RSAData;

/**
 * the view displaying this visualization.
 *
 * @author Michael Gaber
 */
public class RSAView extends ViewPart {

    @Override
    public final void createPartControl(final Composite parent) {
        final HashMap<Action, RSAData> datas = new HashMap<Action, RSAData>();
        final TabFolder tf = new TabFolder(parent, SWT.TOP);

        // Encrypt
        TabItem ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.RSAComposite_encrypt);
        ScrolledComposite sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        RSAComposite c = new RSAComposite(sc, SWT.NONE, Action.EncryptAction, datas);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);

        // Decrypt
        ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.RSAComposite_decrypt);
        sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        c = new RSAComposite(sc, SWT.NONE, Action.DecryptAction, datas);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);

        // Sign
        ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.RSAComposite_sign);
        sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        c = new RSAComposite(sc, SWT.NONE, Action.SignAction, datas);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);

        // Verify
        ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.RSAComposite_verify);
        sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        c = new RSAComposite(sc, SWT.NONE, Action.VerifyAction, datas);
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

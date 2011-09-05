// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.he.Messages;


public class HEView extends ViewPart {

	/** Handles the tab choice */
	public final int GENTRY_HALEVI = 1, RSA = 2, PAILLIER = 3;

	public HEView() {
	}

	@Override
	public void createPartControl(final Composite parent) {
		final TabFolder tf = new TabFolder(parent, SWT.TOP);

		// Gentry & Halevi
        TabItem ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.HEComposite_GentryHalevi);
        ScrolledComposite sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        HEComposite c = new HEComposite(sc, GENTRY_HALEVI, SWT.NONE);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);

        // RSA
        ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.HEComposite_RSA);
        sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        c = new HEComposite(sc, RSA, SWT.NONE);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);

        // Paillier
        ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.HEComposite_Paillier);
        sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        c = new HEComposite(sc, PAILLIER, SWT.NONE);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);



	}

	@Override
	public void setFocus() {
	}

}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.sudoku.views;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.games.sudoku.Messages;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SudokuView extends ViewPart {

    public SudokuView() {

    }

    public final int NORMAL = 1, KILLER = 2, HEX = 3;

	@Override
	public void createPartControl(final Composite parent) {
		final TabFolder tf = new TabFolder(parent, SWT.TOP);

		// Gentry & Halevi
        TabItem ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.NormalTabTitle);
        ScrolledComposite sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        SudokuComposite c = new SudokuComposite(sc, NORMAL, SWT.NONE);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);

        // RSA
        ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.KillerTabTitle);
        sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        c = new SudokuComposite(sc, KILLER, SWT.NONE);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);

        // Paillier
        ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.HexTabTitle);
        sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        c = new SudokuComposite(sc, HEX, SWT.NONE);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);



	}

	@Override
	public void setFocus() {
	}


}
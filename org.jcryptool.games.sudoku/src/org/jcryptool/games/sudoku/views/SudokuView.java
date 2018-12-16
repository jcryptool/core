// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.games.sudoku.Messages;
import org.jcryptool.games.sudoku.SudokuPlugin;

public class SudokuView extends ViewPart {

    public SudokuView() { }

//    public final int NORMAL = 1, KILLER = 2, HEX = 3;
    public final int KILLER = 2, HEX = 3;

	@Override
	public void createPartControl(final Composite parent) {
		final TabFolder tf = new TabFolder(parent, SWT.TOP);

		//Normal 9*9 Sudoku Tab
        TabItem ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.NormalTabTitle);
        ScrolledComposite sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
//        SudokuComposite c = new SudokuComposite(sc, NORMAL, SWT.NONE);
        NormalPuzzle c = new NormalPuzzle(sc, SWT.NONE);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti.setControl(sc);

        //Killer Sudoku Tab
        TabItem ti2 = new TabItem(tf, SWT.NONE);
        ti2.setText(Messages.KillerTabTitle);
        ScrolledComposite sc2 = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc2.setExpandHorizontal(true);
        sc2.setExpandVertical(true);
        SudokuComposite c2 = new SudokuComposite(sc2, KILLER, SWT.NONE);
        sc2.setContent(c2);
        sc2.setMinSize(c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti2.setControl(sc2);

        //Hex Sudoku Tab (16*16)
        TabItem ti3 = new TabItem(tf, SWT.NONE);
        ti3.setText(Messages.HexTabTitle);
        ScrolledComposite sc3 = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc3.setExpandHorizontal(true);
        sc3.setExpandVertical(true);
        SudokuComposite c3 = new SudokuComposite(sc3, HEX, SWT.NONE);
        sc3.setContent(c3);
        sc3.setMinSize(c3.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti3.setControl(sc3);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), SudokuPlugin.PLUGIN_ID + ".sudokuview");
	}

	@Override
	public void setFocus() {
	}


}
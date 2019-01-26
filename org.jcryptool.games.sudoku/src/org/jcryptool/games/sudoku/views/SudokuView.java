// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.sudoku.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.games.sudoku.Messages;
import org.jcryptool.games.sudoku.SudokuPlugin;

public class SudokuView extends ViewPart {
	
	private TabFolder tf;
	private NormalPuzzle normalSudoku;
	private KillerPuzzle killerSudoku;
	private HexPuzzle hexadecimalSudoku;
	
//	private boolean normalSudokuCreated = false;
	private boolean killerSudokuCreated = false;
	private boolean hexSudokuCreateed = false;
	

    public SudokuView() { }

	@Override
	public void createPartControl(final Composite parent) {
		
		tf = new TabFolder(parent, SWT.TOP);

		//Normal 9*9 Sudoku Tab
        TabItem ti1 = new TabItem(tf, SWT.NONE);
        ti1.setText(Messages.NormalTabTitle);
        ScrolledComposite sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        normalSudoku = new NormalPuzzle(sc, SWT.NONE);
        sc.setContent(normalSudoku);
        sc.setMinSize(normalSudoku.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti1.setControl(sc);
        
        //Killer Sudoku Tab
        TabItem ti2 = new TabItem(tf, SWT.NONE);
        ti2.setText(Messages.KillerTabTitle);

        //Hex Sudoku Tab (16*16)
        TabItem ti3 = new TabItem(tf, SWT.NONE);
        ti3.setText(Messages.HexTabTitle);


        tf.addSelectionListener(new SelectionListener() {
			//The killer-Sudoku and Hex-Sudoku are only created if the user selects the specific tab.
        	//This is used to improve the performance.
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (tf.getSelectionIndex() == 0) {
//					if (!normalSudokuCreated) {
//				        ScrolledComposite sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
//				        sc.setExpandHorizontal(true);
//				        sc.setExpandVertical(true);
//				        normalSudoku = new NormalPuzzle(sc, SWT.NONE);
//				        sc.setContent(normalSudoku);
//				        sc.setMinSize(normalSudoku.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//				        ti1.setControl(sc);
//				        normalSudokuCreated = true;
//					}
				} else if (tf.getSelectionIndex() == 1) {
					if (!killerSudokuCreated) {
				        ScrolledComposite sc2 = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
				        sc2.setExpandHorizontal(true);
				        sc2.setExpandVertical(true);
				        killerSudoku = new KillerPuzzle(sc2, SWT.NONE);
				        sc2.setContent(killerSudoku);
				        sc2.setMinSize(killerSudoku.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				        ti2.setControl(sc2);
				        killerSudokuCreated = true;
					}
				} else if (tf.getSelectionIndex() == 2) {
					if (!hexSudokuCreateed) {
				        ScrolledComposite sc3 = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
				        sc3.setExpandHorizontal(true);
				        sc3.setExpandVertical(true);
				        hexadecimalSudoku = new HexPuzzle(sc3, SWT.NONE);
				        sc3.setContent(hexadecimalSudoku);
				        sc3.setMinSize(hexadecimalSudoku.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				        ti3.setControl(sc3);
				        hexSudokuCreateed = true;
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), SudokuPlugin.PLUGIN_ID + ".sudokuview");
	}

	@Override
	public void setFocus() {
		
	}
	
	/**
	 * Resets the current Tabitem.</br>
	 * Very ugly. Strong link between GUI and logic.</br>
	 * Checks if the current tab item has a child which is an 
	 * instance of NormalPuzzle, HexPuzzle or KillerPuzzle. If so
	 * it calls the reset method of this class. 
	 */
	public void reset() {

		//Get the current tab item 
		TabItem tit = tf.getItem(tf.getSelectionIndex());
		Control ctr = tit.getControl();
		// ctr has only one child. NormalPuzzle or KillerPuzzle or HexPuzzle.
		Control [] childs = ((Composite) ctr).getChildren();
		if (childs[0] instanceof NormalPuzzle) {
			NormalPuzzle np = (NormalPuzzle) childs[0];
			np.reset();
		} else if (childs[0] instanceof KillerPuzzle) {
			KillerPuzzle kp = (KillerPuzzle) childs[0];
			kp.reset();
		} else if (childs[0] instanceof HexPuzzle) {
			HexPuzzle hp = (HexPuzzle) childs[0];
			hp.reset();
		}	
	}


}
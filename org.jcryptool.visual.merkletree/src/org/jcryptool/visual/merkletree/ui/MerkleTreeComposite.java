//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2016, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.merkletree.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.MerkleTreeView;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Class for the Composite with the Descriptions in Tabpage 1
 * 
 * @author Kevin Muehlboeck
 * @author <i>revised by</i>
 * @author Maximilian Lindpointner
 * 
 */

public class MerkleTreeComposite extends Composite {

	private StyledText descText;
	private MerkleTreeView masterView;
	private MerkleTreeGeneration generationTab;
	private MerkleTreeComposite merkleTreeComposite;
	private SUIT mode = SUIT.MSS;

	/**
	 * Create the composite. Including Descriptions, Seed (,Bitmask) and Key
	 * 
	 * @param parent
	 *        Parent Composite
	 * @param masterView
	 *        Plugin-Main-Composite
	 */
	public MerkleTreeComposite(Composite parent, MerkleTreeView masterView) {
		super(parent, SWT.NONE);

		this.setLayout(new GridLayout());
		this.masterView = masterView;
		merkleTreeComposite = this;
		
		// Create the GUI
		createContent();

	}

	private void createContent() {
		// Combobox, to switch the different SUIT's (MSS,XMSS,XMSS_MT)
		Combo combo = new Combo(this, SWT.READ_ONLY);
		combo.setFont(FontService.getLargeFont());
		combo.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
		combo.add(Descriptions.CompositeDescriptionMerkleTree);
		combo.add(Descriptions.CompositeDescriptionXMSS);
		combo.add(Descriptions.CompositeDescriptionXMSS_MT);
		
		switch (mode) {
		case XMSS:
			combo.select(1);
			break;
		case XMSS_MT:
			combo.select(2);
			break;
		case MSS:
		default:
			combo.select(0);
			break;
		}


		// listener if another SUIT is selected
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch (combo.getSelectionIndex()) {
				case 1:
					mode = SUIT.XMSS;
					break;
				case 2:
					mode = SUIT.XMSS_MT;
					break;
				case 0:
				default:
					mode = SUIT.MSS;
					break;
				}
				((MerkleTreeView) masterView).setAlgorithm(null, mode);

				// clear actual frame before creating a new one
				// simply throw everything away and create 
				// new GUI with createContent();.
				Control[] children = merkleTreeComposite.getChildren();
				for (Control control : children) {
					control.dispose();
				}

				createContent();

				merkleTreeComposite.layout();
			}
		});

		// initial MSS - Layout
		createMerkleTreeDescription();
		
		
		generationTab = new MerkleTreeGeneration(this, SWT.NONE, mode, masterView);
		generationTab.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		setLocalFocus();
	}


	/**
	 * Generates the main description for the first tab
	 * 
	 * @param descr
	 *        Parent Composite
	 * @param mode
	 *        SUIT { MSS, XMSS or XMSS_MT }
	 * @author Maximilian Lindpointner
	 */
	private void createMerkleTreeDescription() {
		
		descText = new StyledText(this, SWT.WRAP | SWT.READ_ONLY);
		GridData gd_descText = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_descText.widthHint = MerkleConst.PLUGIN_WIDTH;
		descText.setLayoutData(gd_descText);

		switch (mode) {
		case XMSS:
			descText.setText(Descriptions.XMSS.Tab0_Txt0);
			break;
		case XMSS_MT:
			descText.setText(Descriptions.XMSS_MT.Tab0_Txt0);
			break;
		case MSS:
			descText.setText(Descriptions.MSS.Tab0_Txt0);
			break;
		}

	}

	/**
	 * Provides an interface to interact with the generation tab Called from
	 * MerkleTreeView.java
	 * 
	 * @return the merkle generation tab
	 */
	public MerkleTreeGeneration getMTS() {
		return generationTab;
	}

	public void setLocalFocus() {
		descText.setFocus();
	}
}

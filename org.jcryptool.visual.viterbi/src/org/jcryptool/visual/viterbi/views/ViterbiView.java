//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.viterbi.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.viterbi.ViterbiPlugin;
import org.jcryptool.visual.viterbi.algorithm.Combination;

/**
 * This class provides the basic structure for the gui. It creates the tabs.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */
public class ViterbiView extends ViewPart {

	private XORComposite xorComposite;
	private ViterbiComposite viterbiComposite;
	private TabFolder tf;
	private Composite parent;

	/**
	 * creates the two tabs
	 */
	@Override
	public final void createPartControl(final Composite parent) {
		this.parent = parent;

		tf = new TabFolder(parent, SWT.TOP);

		// XOR Tab
		TabItem ti = new TabItem(tf, SWT.NONE);
		ti.setText(Messages.XORComposite_tab_title);
		ScrolledComposite sc = new ScrolledComposite(tf, SWT.H_SCROLL
				| SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		xorComposite = new XORComposite(sc, SWT.NONE, this);
		sc.setContent(xorComposite);
		sc.setMinSize(xorComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		ti.setControl(sc);
		xorComposite.displayDefaultTexts();

		// Viterbi Tab
		ti = new TabItem(tf, SWT.NONE);
		ti.setText(Messages.ViterbiComposite_tab_title);
		sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		viterbiComposite = new ViterbiComposite(sc, SWT.NONE, this);
		sc.setContent(viterbiComposite);
		sc.setMinSize(viterbiComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		ti.setControl(sc);
		viterbiComposite.displayDefaultTexts();
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(parent, ViterbiPlugin.PLUGIN_ID + ".view"); //$NON-NLS-1$
	}

	@Override
	public void setFocus() {
		parent.setFocus();
	}

	/**
	 * This class changes the active Tab from the XOR Tab to the Viterbi Tab. It
	 * does not work the other way. Informs the Viterbi-Tab about the ciphertext
	 * and the combination used to create the cipher text
	 *
	 * @param ciphertext
	 *            the ciphertext that has been entered, and should be displayed
	 *            in the other tab
	 * @param combi
	 *            the type of combination that has been choosen by the user
	 *
	 */
	public void changeTab(String ciphertext, Combination combi) {
		viterbiComposite.setCipherText(ciphertext);
		tf.setSelection(1);
		viterbiComposite.setCombination(combi);
	}
}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.wizards.pages;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.Functions;
import org.jcryptool.visual.he.algo.GHData;

/**
 * Page to generate a new key for Gentry & Halevi fully homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class GHSettingsPage extends WizardPage {
	private static final String PAGENAME = "Settings page"; //$NON-NLS-1$
	private static final String TITLE=Messages.SettinsPage_Title;
	private final GHData data;
	private Combo dim = null;
	private Button genButton, yesButton, noButton;
	private Label own, pass;

	/** field for the owner of this keypair. */
	protected Text owner;

	/** field for the password. */
	private Text password;

	/** password verification field. */
	private Text passwordverify;

	/** boolean to check if the key is set*/
	private boolean keySet = false;

	public GHSettingsPage(GHData data) {
		super(PAGENAME, TITLE, null);
		this.setDescription("Change the amount of multiplications allowed before performing a Recrypt. \nMore is quicker, but might introduce decryption errors.");
		this.data = data;
		setPageComplete(false);
	}

	public final void createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout(SWT.VERTICAL);
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(300,30);
		final RowData combord = new RowData(300,18);
		Label label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);
		label.setText("Amount of Multiplications to perform before recrypt");
		dim = new Combo(subComposite, SWT.READ_ONLY);
		dim.setLayoutData(combord);
		dim.add("", 0);
		dim.add("1", 1);
		dim.add("2", 2);
		dim.add("3", 3);
		dim.add("4", 4);
		dim.select(data.getMaxMult());
		
		
		dim.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (((Combo) e.widget).getSelectionIndex() == 0) {
					setPageComplete(false);
				} else {
					setPageComplete(true);
					data.setMaxMult(((Combo) e.widget).getSelectionIndex());
				}
			}
		});

		setControl(mainComposite);
	}

	@Override
	public final IWizardPage getNextPage() {
			return null;
	}

	public static String getPagename() {
		return PAGENAME;
	}
}

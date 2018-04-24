// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.he.Messages;
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

	/** field for the owner of this keypair. */
	protected Text owner;

	public GHSettingsPage(GHData data) {
		super(PAGENAME, TITLE, null);
		this.setDescription(Messages.GHChooseMultiplicationsExplanation);
		this.data = data;
		setPageComplete(false);
	}

	public final void createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridLayout gl_mainComposite = new GridLayout();
		mainComposite.setLayout(gl_mainComposite);
		
		Label label = new Label(mainComposite, SWT.NONE);
		label.setText(Messages.GHChooseMultiplicationsText);
		
		dim = new Combo(mainComposite, SWT.READ_ONLY);
		GridData gd_dim = new GridData(SWT.LEFT, SWT.FILL, true, false);
		gd_dim.widthHint = 300;
		dim.setLayoutData(gd_dim);
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

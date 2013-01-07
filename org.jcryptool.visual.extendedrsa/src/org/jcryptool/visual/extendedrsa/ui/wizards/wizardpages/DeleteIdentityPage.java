//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa.ui.wizards.wizardpages;

import java.util.Vector;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.extendedrsa.ExtendedTabFolder;
import org.jcryptool.visual.extendedrsa.Identity;
import org.jcryptool.visual.extendedrsa.IdentityManager;

/**
 * This is the wizard to delete a new Identity with the button in the visual
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class DeleteIdentityPage extends WizardPage {

	private Combo selectedIdentity;
	private ExtendedTabFolder tabfolder;
	
	public DeleteIdentityPage(ExtendedTabFolder tabfolder) {
		super("Identit\u00e4t l\u00f6schen", "Identit\u00e4t l\u00f6schen", null);
        setDescription("W\u00e4hlen Sie die zu l\u00f6schende Identit\u00e4t aus und best\u00e4tigen Sie Ihre Auswahl.");
        this.tabfolder = tabfolder;
	}


	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		GridLayout grid = new GridLayout(2,false);
		grid.horizontalSpacing = 20;
		grid.verticalSpacing = 10;
		container.setLayout(grid);
		
		Label lbl_chooseId = new Label(container, SWT.NONE);
		lbl_chooseId.setText("Identit\u00e4t w\u00e4hlen: ");
		
		selectedIdentity = new Combo(container, SWT.READ_ONLY);
		GridData gd_selectedIdentity = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_selectedIdentity.widthHint = 256;
		selectedIdentity.setLayoutData(gd_selectedIdentity);
		
		Vector<String> ids = IdentityManager.getInstance().getContacts();
		for (String s : ids){
			if (!s.equals("Alice Whitehat") && (!s.equals("Bob Whitehat"))){
				selectedIdentity.add(s);
			}
		}
		
		selectedIdentity.select(0);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Text hint = new Text(container, SWT.READ_ONLY | SWT.MULTI| SWT.WRAP);
		hint.setBackground(container.getBackground());
		hint.setFont(FontService.getNormalBoldFont());
		hint.setText("ACHTUNG: Wenn Sie eine Identit\u00e4t ausgew\u00e4hlt haben und diese Auswahl durch einen Klick auf \"Fertigstellen\" best\u00e4tigen, wird diese Identit\u00e4t mit allen zugordneten Schl\u00fcsseln sofort und permanent gel\u00f6scht!\n\nDie Identit\u00e4ten \"Alice Whitehat\" und \"Bob Whitehat\" k\u00f6nnen nicht gel\u00f6scht werden.");
		GridData gd_hint = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_hint.widthHint = 559;
		hint.setLayoutData(gd_hint);
	}

	public Combo getSelectedIdentity() {
		return selectedIdentity;
	}
}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.jcryptool.visual.extendedrsa.Contact;
import org.jcryptool.visual.extendedrsa.IdentityManager;
import org.jcryptool.visual.extendedrsa.ui.wizards.wizardpages.NewIdentityPage;

/**
 * This is the wizard to create a new Identity with the button in the visual
 * @author Christoph Schnepf, Patrick Zillner
 */
public class NewIdentityWizard extends Wizard{
	
	private TabFolder tabfolder;
	private NewIdentityPage newIDPage;
	private Button delID;
	
	public NewIdentityWizard(TabFolder folder, Button delID){
		this.tabfolder = folder;
		this.delID = delID;
	}
	
	@Override
	public final void addPages() {
		newIDPage = new NewIdentityPage();
		addPage(newIDPage);
	}
	
	@Override
	public boolean performFinish() {
		if (newIDPage.isPageComplete()){
			Contact contact = new Contact(newIDPage.getIdName().toString(), newIDPage.getIdForename().toString(), newIDPage.getIdSurname().toString(), newIDPage.getIdOrganisation().toString(), newIDPage.getIdRegion().toString());
			IdentityManager.getInstance().getContactManger().addContact(contact);
			
			if (IdentityManager.getInstance().getContacts().size() > 2){
				delID.setEnabled(true);
			}else{
				delID.setEnabled(false);
			}
			
			return true;
		}
		return false;
	}

}

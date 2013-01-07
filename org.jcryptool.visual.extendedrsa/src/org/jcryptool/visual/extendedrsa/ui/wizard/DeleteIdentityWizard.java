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
package org.jcryptool.visual.extendedrsa.ui.wizard;


import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;
import org.jcryptool.visual.extendedrsa.ExtendedTabFolder;
import org.jcryptool.visual.extendedrsa.Identity;
import org.jcryptool.visual.extendedrsa.IdentityManager;
import org.jcryptool.visual.extendedrsa.ui.wizard.wizardpages.DeleteIdentityPage;

/**
 * This is the wizard to create a new Identity with the button in the visual
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class DeleteIdentityWizard extends Wizard{
	
	private ExtendedTabFolder tabfolder;
	private DeleteIdentityPage deleteIDPage;
	private Button delID;
	
	public DeleteIdentityWizard(ExtendedTabFolder folder, Button delID){
		this.tabfolder = folder;
		this.delID = delID;
	}
	
	@Override
	public final void addPages() {
		deleteIDPage = new DeleteIdentityPage(tabfolder);
		addPage(deleteIDPage);
	}
	
	@Override
	public boolean performFinish() { 
		String contactToDelete = deleteIDPage.getSelectedIdentity().getItem(deleteIDPage.getSelectedIdentity().getSelectionIndex()).toString();
		System.out.println("contact to delete: "+contactToDelete);
		//find the tabitem and delete it
		for (TabItem ti : tabfolder.getItems()){
			Identity current = (Identity)ti;
			if (current.getIdentityName().equals(contactToDelete)){
				current.dispose();
			}
		}
		
		KeyStoreManager.getInstance().deleteContact(contactToDelete);
		
		if (IdentityManager.getInstance().getContacts().size()>3){
			delID.setEnabled(true);
		}else{
			delID.setEnabled(false);
		}
		return true;
	}

}

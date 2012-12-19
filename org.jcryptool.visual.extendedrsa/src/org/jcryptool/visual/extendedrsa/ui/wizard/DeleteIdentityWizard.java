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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jcryptool.visual.extendedrsa.Identity;
import org.jcryptool.visual.extendedrsa.ui.wizard.wizardpages.DeleteIdentityPage;

/**
 * This is the wizard to create a new Identity with the button in the visual
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class DeleteIdentityWizard extends Wizard{
	
	TabFolder tabfolder;
	DeleteIdentityPage deleteIDPage;
	
	public DeleteIdentityWizard(TabFolder folder){
		this.tabfolder = folder;
	}
	
	@Override
	public final void addPages() {
		deleteIDPage = new DeleteIdentityPage(tabfolder);
		addPage(deleteIDPage);
	}
	
	@Override
	public boolean performFinish() { 
		//find the tabitem and delete it
		for (TabItem ti : tabfolder.getItems()){
			Identity current = (Identity)ti;
			if (current.getIdentityName().equals(deleteIDPage.getSelectedIdentity().getText())){
				current.dispose();
			}
		}
		
		return true;
	}

}

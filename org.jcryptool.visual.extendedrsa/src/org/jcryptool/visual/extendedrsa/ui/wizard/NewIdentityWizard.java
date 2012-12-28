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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.jcryptool.visual.extendedrsa.Identity;
import org.jcryptool.visual.extendedrsa.ui.wizard.wizardpages.NewIdentityPage;

/**
 * This is the wizard to create a new Identity with the button in the visual
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class NewIdentityWizard extends Wizard{
	
	TabFolder tabfolder;
	NewIdentityPage newIDPage;
	
	public NewIdentityWizard(TabFolder folder){
		this.tabfolder = folder;
	}
	
	@Override
	public final void addPages() {
		newIDPage = new NewIdentityPage();
		addPage(newIDPage);
	}
	
	@Override
	public boolean performFinish() {
		if (newIDPage.isPageComplete()){
//			new Identity(tabfolder, SWT.NONE, newIDPage.getIdName().getText(), newIDPage.getIdForename().getText(),  newIDPage.getIdSurname().getText(), newIDPage.getIdOrganisation().getText(),  newIDPage.getIdRegion().getText());
			return true;
		}
		return false;
	}

}

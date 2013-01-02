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
import org.jcryptool.visual.extendedrsa.ui.wizard.wizardpages.ManageVisibleIdentitiesPage;

/**
 * This is the wizard to create a new Identity with the button in the visual
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class ManageVisibleIdentitesWizard extends Wizard{

	ManageVisibleIdentitiesPage visiblePages;
	
	public ManageVisibleIdentitesWizard(){
	}
	
	@Override
	public final void addPages() {
		visiblePages = new ManageVisibleIdentitiesPage();
		addPage(visiblePages);
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}

}

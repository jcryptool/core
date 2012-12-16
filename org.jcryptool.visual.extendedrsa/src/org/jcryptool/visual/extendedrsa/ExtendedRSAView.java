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
package org.jcryptool.visual.extendedrsa;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
*
* @author Christoph Schnepf, Patrick Zillner
*/
public class ExtendedRSAView extends ViewPart {
	public ExtendedRSAView() {
	}
	
	public static final String ID = "org.jcryptool.visual.extendedrsa.ExtendedRSAView";
	
	@Override
	public void createPartControl(Composite parent) {
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,Activator.PLUGIN_ID + "rsaExtView");
	}


	@Override
	public void setFocus() {

	}

}

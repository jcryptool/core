// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.dsa.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * the view displaying this visualization.
 * @author Michael Gaber
 */
public class DSAView extends ViewPart {

    @Override
	public final void createPartControl(final Composite parent) {
		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		final DSAComposite c = new DSAComposite(sc, SWT.NONE);
		sc.setContent(c);
		sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// Sofern ich noch ne help schreibe
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), "org.jcryptool.visual.rsa.rsahelp");
	}

	@Override
	public void setFocus() {
	}
}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.entropy.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.analysis.entropy.EntropyPlugin;
import org.jcryptool.analysis.entropy.ui.EntropyUI;

public class EntropyAnalysisView extends ViewPart {
	private EntropyUI ui = null;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		ui = new EntropyUI(sc, SWT.NONE);
		sc.setContent(ui);
		sc.setMinSize(ui.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, EntropyPlugin.PLUGIN_ID + ".view"); //$NON-NLS-1$
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		ui.setFocus();
	}
}
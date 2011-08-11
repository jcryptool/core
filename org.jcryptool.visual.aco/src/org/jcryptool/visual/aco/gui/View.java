// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.aco.ACOPlugin;
import org.jcryptool.visual.aco.tutorial.Model;

/**
 * RCP View for the ACO Plugin
 *
 * @author mwalthart
 *
 */
public class View extends ViewPart {
	private Composite parent;

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		Model m = new Model();
		parent.setLayout(new GridLayout(2, false));

		Func func = new Func(m, parent, this);
		func.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2));

		Show view = new Show(m, parent);
		view.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Info text = new Info(m, parent);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		m.addObserver(view);
		m.addObserver(func);
		m.addObserver(text);
		m.setNotified();
		parent.layout();

		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(parent, ACOPlugin.PLUGIN_ID + ".view"); //$NON-NLS-1$
	}

	/**
	 * resets the model and view to its initial state
	 */
	public void reset() {
		Control[] children = parent.getChildren();
		for (Control control : children) {
			control.dispose();
		}
		createPartControl(parent);
	}

	@Override
	public void setFocus() {
		parent.setFocus();
	}
}

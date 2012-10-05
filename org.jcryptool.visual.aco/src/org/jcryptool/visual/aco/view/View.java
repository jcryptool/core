// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.aco.ACOPlugin;
import org.jcryptool.visual.aco.model.Model;

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
		parent.setLayout(new GridLayout(3, false));

		AntColControlComposite controlComps = new AntColControlComposite(m,
				parent);
		controlComps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));

		AntColVisualComposite visual = new AntColVisualComposite(m, parent);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		visual.setLayoutData(gridData);

		AntColDescriptionComposite descriptionComp = new AntColDescriptionComposite(
				m, parent);
		descriptionComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 3, 1));

		m.addViews(controlComps, visual, descriptionComp);
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

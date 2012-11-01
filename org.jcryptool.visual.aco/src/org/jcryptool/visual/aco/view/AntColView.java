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
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.aco.ACOPlugin;
import org.jcryptool.visual.aco.controller.AntColEventController;
import org.jcryptool.visual.aco.model.CommonModel;

/**
 * RCP View for the ACO Plugin
 * 
 * @author mwalthart
 * 
 */
public class AntColView extends ViewPart {
	private Composite parent;
	private AntColDescriptionComposite descriptionComp;
	private AntColConfigComposite configComp;
	private AntColVisualComposite visualComp;
	private AntColAnalysisComposite analysisComp;
	private AntColResultComposite resultComp;

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		CommonModel model = new CommonModel();
		parent.setLayout(new GridLayout(1, false));

		ScrolledComposite scrollContainer = new ScrolledComposite(parent,
				SWT.H_SCROLL | SWT.V_SCROLL);
		scrollContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		scrollContainer.setExpandHorizontal(true);
		scrollContainer.setExpandVertical(true);
		Composite container = new Composite(scrollContainer, SWT.NONE);
		container.setLayout(new GridLayout(4, false));

		AntColHeaderComposite headerComp = new AntColHeaderComposite(container);
		headerComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,
				4, 1));

		configComp = new AntColConfigComposite(model, container);
		configComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		analysisComp = new AntColAnalysisComposite(model, container);
		analysisComp
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		visualComp = new AntColVisualComposite(model, container);
		visualComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		resultComp = new AntColResultComposite(model, container);
		resultComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		descriptionComp = new AntColDescriptionComposite(container);
		descriptionComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 4, 1));

		new AntColEventController(model, this);

		scrollContainer.setContent(container);
		scrollContainer.setMinSize(container.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

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

	public AntColConfigComposite getConfigComp() {
		return configComp;
	}

	public AntColAnalysisComposite getAnalysisComp() {
		return analysisComp;
	}

	public AntColVisualComposite getVisualComp() {
		return visualComp;
	}

	public AntColDescriptionComposite getDescriptionComp() {
		return descriptionComp;
	}
	public AntColResultComposite getResultComp() {
		return resultComp;
	}
}

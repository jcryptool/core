// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
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
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.core.util.ui.auto.LayoutAdvisor;
import org.jcryptool.core.util.ui.auto.SmoothScroller;
import org.jcryptool.visual.aco.ACOPlugin;
import org.jcryptool.visual.aco.controller.AntColEventController;
import org.jcryptool.visual.aco.model.ACO;
import org.jcryptool.visual.aco.model.CommonModel;

/**
 * RCP View for the ACO Plugin
 * 
 * @author mwalthart
 * 
 */
public class AntColView extends ViewPart {
	private Composite parent;
	private ScrolledComposite scrollComposite;
	private Composite container;
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

		scrollComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrollComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.setExpandVertical(true);
		
		container = new Composite(scrollComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout(4, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		container.setLayout(gridLayout);
		
		TitleAndDescriptionComposite td = new TitleAndDescriptionComposite(container);
		td.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));
		td.setTitle(Messages.Header_title);
		td.setDescription(Messages.Header_text);

		configComp = new AntColConfigComposite(model, container);
		configComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		analysisComp = new AntColAnalysisComposite(model, container);
		analysisComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		visualComp = new AntColVisualComposite(model, container);
		visualComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		resultComp = new AntColResultComposite(model, container);
		resultComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		descriptionComp = new AntColDescriptionComposite(this, container);
		descriptionComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));

		new AntColEventController(model, this);

		scrollComposite.setContent(container);
		scrollComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		LayoutAdvisor.addPreLayoutRootComposite(scrollComposite);
		
		// This makes the ScrolledComposite scrolling, when the mouse 
		// is on a Text with one or more of the following tags: SWT.READ_ONLY,
		// SWT.V_SCROLL or SWT-H_SCROLL.
		SmoothScroller.scrollSmooth(scrollComposite);
		
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(parent, ACOPlugin.PLUGIN_ID + ".ContextHelpID"); //$NON-NLS-1$
	}

	/**
	 * resets the model and view to its initial state
	 */
	public void reset() {
		ACO.setAlpha(0.8);
		ACO.setBeta(0.8);
		ACO.setVerd(0.9);
		Control[] children = parent.getChildren();
		for (Control control : children) {
			control.dispose();
		}
		createPartControl(parent);
		parent.layout();
	}

	@Override
	public void setFocus() {
		scrollComposite.setFocus();
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

	public void recalculateSize() {
		// TODO Auto-generated method stub
		container.layout();
		scrollComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	
}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.PairingBDII.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.ui.auto.LayoutAdvisor;

public class View extends ViewPart {
	private Composite scrolledContent;
	private IntroductionAndParameters situation;
	private Illustration illustration;
	private DefinitionAndDetails protocol;
	private Logging tryagain;
	private ScrolledComposite scrolledComposite;
	private Composite parent;

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);

		scrolledContent = new Composite(scrolledComposite, SWT.NONE);
		scrolledContent.setLayout(new GridLayout(2, false));
		
		situation = new IntroductionAndParameters(scrolledContent);
		illustration = new Illustration(scrolledContent);
		protocol = new DefinitionAndDetails(scrolledContent, this);
		tryagain = new Logging(scrolledContent);

		Model.getDefault().setLinks(situation, illustration, protocol, tryagain);
		Model.getDefault().setNumberOfUsers(4);
		Model.getDefault().reset();
		Model.getDefault().setupStep1();

		scrolledComposite.setContent(scrolledContent);
		scrolledComposite.setMinSize(scrolledContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		LayoutAdvisor.addPreLayoutRootComposite(parent);
		
		// Register the context help for this plugin.
		PlatformUI.getWorkbench().getHelpSystem()
			.setHelp(parent.getShell(),"org.jcryptool.visual.PairingBDII.view");
		
	}
	
	/**
	 * This layous out the complete GUI.</br>
	 * It is used, when a user collapses on of the two 
	 * collapsable text field (definitions and notations)
	 * and (details). This forces a a recalculation of the 
	 * size of the GUI and adapts the vertical scrollbar to the 
	 * new size.
	 */
	public void layTheShitOut() {
		scrolledContent.layout();
		scrolledComposite.setMinSize(scrolledContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	public void setFocus() {
		scrolledComposite.setFocus();
	}

	public void reset() {
		Control[] children = parent.getChildren();
		for (Control control : children) {
			control.dispose();
		}
		createPartControl(parent);
		parent.layout();
	}
}
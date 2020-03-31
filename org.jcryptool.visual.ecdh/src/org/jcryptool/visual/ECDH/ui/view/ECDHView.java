// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.ECDH.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.ECDH.handlers.ShowAnimationHandler;

public class ECDHView extends ViewPart {

	private Composite parent;
	private ECDHComposite ecdhComposite;
	private ScrolledComposite sc;
	
	/**
	 * Show the animation (moving keys from A to S and B to S) or not.</br>
	 * Default is true
	 */
	public boolean showAnimation = true;

	/**
	 * The constructor.
	 */
	public ECDHView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		this.parent = parent;
		sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		ecdhComposite = new ECDHComposite(sc, SWT.NONE, this);
		sc.setContent(ecdhComposite);
		sc.setMinSize(ecdhComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), "org.jcryptool.visual.ecdh.ecdhview"); //$NON-NLS-1$
	}
	/**
	 * Passing the focus request to the viewer's control.
	 */

	@Override
	public void setFocus() {
		parent.setFocus();
	}
	
	public void reset() {
		ecdhComposite.reset(ECDHComposite.RESET_ALL);
		
		// Reset the showAnimation Icon in the top right corner of the plugin.
		ShowAnimationHandler.showAnimation = true;
		ICommandService commands = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		commands.refreshElements("org.jcryptool.visual.ecdh.showAnimationCommand", null); //$NON-NLS-1$
	
		recalculateWindowSize();
	}
	
	public void recalculateWindowSize() {
		ecdhComposite.layout();
		sc.setMinSize(ecdhComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
}

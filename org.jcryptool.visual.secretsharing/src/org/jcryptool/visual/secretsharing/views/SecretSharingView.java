//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.secretsharing.views;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.secretsharing.SecretSharingPlugin;

/**
 * @author Oryal Inel
 * @version 1.0.0
 */
public class SecretSharingView extends ViewPart {

	private Composite parent;
	private StackLayout layout;
	private ShamirsCompositeGraphical shamirsCompositeGraphical;
	private ShamirsCompositeNumerical shamirsCompositeNumerical;

	/**
	 * Create contents of the view part
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		layout = new StackLayout();
		parent.setLayout(layout);

		shamirsCompositeGraphical = new ShamirsCompositeGraphical(parent, SWT.NONE, this);
		shamirsCompositeNumerical = new ShamirsCompositeNumerical(parent, SWT.NONE, this);

		layout.topControl = shamirsCompositeGraphical;

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, SecretSharingPlugin.PLUGIN_ID + ".view");

        hookActionBar();
	}

    private void hookActionBar() {
        IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
        mgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        getViewSite().getActionBars().updateActionBars();
    }

	@Override
	public void setFocus() {
		parent.setFocus();
	}

	public void showNumerical() {
		if (!layout.topControl.equals(shamirsCompositeNumerical)) {
			shamirsCompositeNumerical.adjustButtons();
			layout.topControl = shamirsCompositeNumerical;
			parent.layout();
		}
	}

	public void showGraphical() {
		if (!layout.topControl.equals(shamirsCompositeGraphical)) {
			shamirsCompositeGraphical.adjustButtons();
			layout.topControl = shamirsCompositeGraphical;
			parent.layout();
		}
	}

}

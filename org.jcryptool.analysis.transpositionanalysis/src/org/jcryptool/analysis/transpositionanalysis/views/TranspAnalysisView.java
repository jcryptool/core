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
package org.jcryptool.analysis.transpositionanalysis.views;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.analysis.transpositionanalysis.TranspositionAnalysisPlugin;
import org.jcryptool.analysis.transpositionanalysis.ui.TranspAnalysisUI;

public class TranspAnalysisView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jcryptool.analysis.transposition.views.TranspAnalysisView"; //$NON-NLS-1$
	private TranspAnalysisUI myUI;
	private Composite parent;

	/**
	 * The constructor.
	 */
	public TranspAnalysisView() {
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		myUI.setFocus();
	}

	/**
	 * Resets the Plug-in.
	 */
	public void resetClick() {
		if (MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
				Messages.TranspAnalysisView_resetDialogTitle, Messages.TranspAnalysisView_resetDialogQuestion)) {
			for (Control ctr : parent.getChildren()) {
				ctr.dispose();
			}
			myUI = new TranspAnalysisUI(parent, SWT.NONE);
			parent.layout();
		} else {
			// Do nothing if the user chooses cancel.
		}

	}

	private void hookActionBar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		getViewSite().getActionBars().updateActionBars();
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		myUI = new TranspAnalysisUI(parent, SWT.NONE);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				TranspositionAnalysisPlugin.PLUGIN_ID + ".transpositionanalysis"); //$NON-NLS-1$

		hookActionBar();
	}
}
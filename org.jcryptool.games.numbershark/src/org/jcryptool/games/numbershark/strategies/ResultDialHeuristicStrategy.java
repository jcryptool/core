// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.numbershark.strategies;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.games.numbershark.NumberSharkPlugin;

/**
 * Result dialog for the calculation of the optimal strategies
 * 
 * @author Johannes Spaeth
 * @version 0.9.5
 */

public class ResultDialHeuristicStrategy extends AbstractResultDialog {
	public ResultDialHeuristicStrategy(Shell shell, int selectedStrategy) {
		super(shell, selectedStrategy);
	}

	protected Control createDialogArea(Composite parent) {
		setTitle(Messages.ShowHeuStrategy_1);
		String algo ="";
		switch (this.getSelectedStrategy())
    	{
		
		case 2:
			algo = Messages.ShowHeuStrategy_5;
			break;
			
		case 3:
			algo = Messages.ShowHeuStrategy_6;
			break;
			
		case 4:
			algo = Messages.ShowHeuStrategy_7;
			break;
    	}
		
		String msg = NLS.bind(Messages.ShowHeuStrategy_2, new Object[]{algo});
		setMessage(msg, IMessageProvider.INFORMATION);
		Composite area = (Composite) super.createDialogArea(parent);


		columns[0].setText(Messages.ShowOptStrategy_3);
		columns[1].setText(Messages.ShowHeuStrategy_4);
		columns[2].setText(Messages.ShowOptStrategy_5);
		columns[3].setText(Messages.ShowHeuStrategy_3);
		columns[4].setText(Messages.ShowOptStrategy_7);
		
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(parent,
						NumberSharkPlugin.PLUGIN_ID + ".heuStratResultDialog"); //$NON-NLS-1$

		return area;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(640, 500);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.ShowHeuStrategy_0);
	}
}

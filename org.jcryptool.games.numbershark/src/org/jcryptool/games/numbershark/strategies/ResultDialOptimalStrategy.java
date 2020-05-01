// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.numbershark.strategies;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.games.numbershark.NumberSharkPlugin;

/**
 * Result dialog for the calculation of the optimal strategies
 * 
 * @author Johannes Spaeth
 * @version 0.9.5
 */
public class ResultDialOptimalStrategy extends AbstractResultDialog {

    public ResultDialOptimalStrategy(Shell shell, int selectedStrategy) {
        super(shell, selectedStrategy);
    }

    @Override
	protected Control createDialogArea(Composite parent) {
        setTitle(Messages.ShowOptStrategy_1);
        setMessage(Messages.ShowOptStrategy_2);
        Composite area = (Composite) super.createDialogArea(parent);

        columns[0].setText(Messages.ShowOptStrategy_3);
        columns[1].setText(Messages.ShowOptStrategy_4);
        columns[2].setText(Messages.ShowOptStrategy_5);
        columns[3].setText(Messages.ShowOptStrategy_6);
        columns[4].setText(Messages.ShowOptStrategy_7);

        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(parent, NumberSharkPlugin.PLUGIN_ID + ".optStratResultDialog"); //$NON-NLS-1$

        parent.layout(true);
//        parent.getShell().pack();
        return area;
    }

    @Override
    protected Point getInitialSize() {
        return new Point(640, 500);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.ShowOptStrategy_0);
        newShell.setSize(Display.getCurrent().getBounds().width / 3, (int) (Display.getCurrent().getBounds().height * (0.75)));
    }
}

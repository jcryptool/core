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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.games.numbershark.NumberSharkPlugin;

/**
 * Settings dialog for the calculation of the optimal strategies
 * 
 * @author Johannes Spaeth
 * @version 0.9.5
 */
public class HeuristicStrategyDialog extends AbstractStrategyDialog {

    public HeuristicStrategyDialog(Shell shell) {
        super(shell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(Messages.HeuStratDialog_0);
        setMessage(Messages.HeuStratDialog_5);

        Composite area = (Composite) super.createDialogArea(parent);

        Composite composite = new Composite(area, SWT.NONE);
        GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd_composite.widthHint = 470;
        composite.setLayoutData(gd_composite);
        GridLayout gl_composite = new GridLayout(1, false);
        gl_composite.marginTop = 15;
        gl_composite.marginLeft = 15;

        composite.setLayout(gl_composite);

        Group strategyGroup = new Group(composite, SWT.NONE);
        strategyGroup.setText(Messages.HeuStratDialog_8);
        GridData gd_strategyGroup = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gd_strategyGroup.verticalIndent = 5;
        gd_strategyGroup.widthHint = 430;
        gd_strategyGroup.horizontalIndent = 6;

        strategyGroup.setLayoutData(gd_strategyGroup);
        GridLayout gl_strategyGroup = new GridLayout(1, true);
        strategyGroup.setLayout(gl_strategyGroup);
        final Button[] radioButton = new Button[4];
        radioButton[0] = new Button(strategyGroup, SWT.RADIO);
        radioButton[0].setText(Messages.HeuStratDialog_1);

        radioButton[1] = new Button(strategyGroup, SWT.RADIO);
        radioButton[1].setText(Messages.HeuStratDialog_2);

        radioButton[2] = new Button(strategyGroup, SWT.RADIO);
        radioButton[2].setText(Messages.HeuStratDialog_3);

        SelectionAdapter radioButtonListener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (e.getSource() == radioButton[0]) {
                    selectedStrategy = 2;
                } else if (e.getSource() == radioButton[1]) {
                    selectedStrategy = 3;
                } else if (e.getSource() == radioButton[2]) {
                    selectedStrategy = 4;
                } else if (e.getSource() == radioButton[3]) {
                    selectedStrategy = 5;
                }
            }
        };

        radioButton[0].addSelectionListener(radioButtonListener);
        radioButton[1].addSelectionListener(radioButtonListener);
        radioButton[2].addSelectionListener(radioButtonListener);

        createSliders(area, false, 2000, 40);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, NumberSharkPlugin.PLUGIN_ID + ".heuStratDialog"); //$NON-NLS-1$

        return area;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.HeuStratDialog_7);
    }

    @Override
    protected Point getInitialSize() {
        return new Point(500, 370);
    }
}

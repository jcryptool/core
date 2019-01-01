// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.games.numbershark.NumberSharkPlugin;

/**
 * Settings dialog for the calculation of the optimal strategies
 * 
 * @author Johannes Spaeth
 * @version 0.9.5
 */
public class OptimalStrategyDialog extends AbstractStrategyDialog {

    public OptimalStrategyDialog(Shell shell) {
        super(shell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(Messages.OptStratDialog_0);
        setMessage(Messages.OptStratDialog_5);

        Composite area = (Composite) super.createDialogArea(parent);

        Composite composite = new Composite(area, SWT.NONE);
        GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true);
        composite.setLayoutData(gd_composite);
        GridLayout gl_composite = new GridLayout();
        gl_composite.marginTop = 15;
        gl_composite.marginLeft = 15;
        gl_composite.marginBottom = 15;
        gl_composite.marginRight = 15;
        composite.setLayout(gl_composite);

        Button showButton = new Button(composite, SWT.RADIO);
        showButton.setText(Messages.OptStratDialog_2);

        Button calcButton = new Button(composite, SWT.RADIO);
        calcButton.setText(Messages.OptStratDialog_1);

        final Group groupSliders = createSliders(composite, true, 400, 40);

        calcButton.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent e) {
                selectedStrategy = 1;
                groupSliders.setVisible(true);
            }
        });

        showButton.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent e) {
                selectedStrategy = 0;
                groupSliders.setVisible(false);
            }
        });
        
        Label separartor = new Label(area, SWT.SEPARATOR | SWT.HORIZONTAL);
        separartor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, NumberSharkPlugin.PLUGIN_ID + ".optStratDialog"); //$NON-NLS-1$

        return area;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.OptStratDialog_7);
    }
}

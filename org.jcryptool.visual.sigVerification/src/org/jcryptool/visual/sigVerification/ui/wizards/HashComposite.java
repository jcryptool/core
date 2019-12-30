// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.sigVerification.algorithm.Input;

/**
 * Contains the elements (2 group boxes) of the HashWizard
 * 
 * @author Wilfing
 */
public class HashComposite extends Composite implements SelectionListener {
    private Group grpHashes;
    private Text txtDescription;
    private Button rdo1;
    private Button rdo2;
    private Button rdo3;
    private Button rdo4;
    private Button rdo5;
    private Menu menuHash;
    private MenuItem mntmHash;
    Input input;

    public HashComposite(Composite parent, int style, Input input) {
        super(parent, style);
        this.input = input;
        initialize();
    }

    /**
     * Draws all the controls of the composite
     */
    private void initialize() {
    	setLayout(new GridLayout());
    	int width = 400;

        grpHashes = new Group(this, SWT.NONE);
        GridData gd_grpHashes = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd_grpHashes.widthHint = width;
        grpHashes.setLayoutData(gd_grpHashes);
        grpHashes.setLayout(new GridLayout());
        grpHashes.setText(Messages.HashWizard_grpHashes);

        rdo1 = new Button(grpHashes, SWT.RADIO);
        rdo1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdo1.setText("MD5 (128)"); //$NON-NLS-1$

        rdo2 = new Button(grpHashes, SWT.RADIO);
        rdo2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdo2.setText("SHA-1 (160)"); //$NON-NLS-1$

        rdo3 = new Button(grpHashes, SWT.RADIO);
        rdo3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdo3.setText(Messages.HashWizard_rdosha256);

        rdo4 = new Button(grpHashes, SWT.RADIO);
        rdo4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdo4.setText(Messages.HashWizard_rdosha384);

        rdo5 = new Button(grpHashes, SWT.RADIO);
        rdo5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdo5.setText(Messages.HashWizard_rdosha512);
        
        Group grpDescription = new Group(this, SWT.NONE);
        GridData gd_grpDescription = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd_grpDescription.widthHint = width;
        gd_grpDescription.heightHint = 200;
        grpDescription.setLayoutData(gd_grpDescription);
        grpDescription.setText(Messages.HashWizard_grpDescription);
        grpDescription.setLayout(new GridLayout());

//        txtDescription = new Text(grpDescription, SWT.WRAP | SWT.TRANSPARENT | SWT.V_SCROLL);
//        GridData gd_txtDescription = new GridData(SWT.FILL, SWT.FILL, true, true);
//        gd_txtDescription.heightHint = 200;
//        txtDescription.setLayoutData(gd_txtDescription);
//        txtDescription.setEditable(false);
//        txtDescription.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
//        txtDescription.setText(Messages.HashWizard_rdomd5_description);
        
        txtDescription = new Text(grpDescription, SWT.WRAP | SWT.TRANSPARENT);
        GridData gd_txtDescription = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd_txtDescription.heightHint = 100;
        txtDescription.setLayoutData(gd_txtDescription);
        txtDescription.setEditable(false);
        txtDescription.setText(Messages.HashWizard_header + "\n\n" + Messages.HashWizard_FurtherInfoInOnlineHelp);

        menuHash = new Menu(txtDescription);
        txtDescription.setMenu(menuHash);

        mntmHash = new MenuItem(menuHash, SWT.NONE);
        mntmHash.setText(Messages.Wizard_menu);
        // To select all text
        mntmHash.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent e) {
                txtDescription.selectAll();
            }
        });
        
        pack();
        Point size = computeSize(width, SWT.DEFAULT);
        size.y += 200;
        size.x += 50;
        getShell().setMinimumSize(size);
        
        // Add event listeners
        rdo1.addSelectionListener(this);
        rdo2.addSelectionListener(this);
        rdo3.addSelectionListener(this);
        rdo4.addSelectionListener(this);
        rdo5.addSelectionListener(this);

        rdo1.setSelection(false);
        rdo2.setSelection(false);
        rdo3.setSelection(false);
        rdo4.setSelection(false);
        rdo5.setSelection(false);

        // Load the previous selection
        switch (input.h) {
        case 0:
            rdo1.setSelection(true);
            break;
        case 1:
            rdo2.setSelection(true);
            break;
        case 2:
            rdo3.setSelection(true);
            break;
        case 3:
            rdo4.setSelection(true);
            break;
        case 4:
            rdo5.setSelection(true);
            break;
        default:
            rdo2.setSelection(true);
            break;
        }
        // Fire an event to show the correct text. It doesn't matter which radio button triggers the
        // event
        // because it is checked in the event handler
        rdo1.notifyListeners(SWT.Selection, new Event());
    }

    /**
     * @return the grpHashes
     */
    public Group getGrpHashes() {
        return grpHashes;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
//        if (rdo1.getSelection()) {
//            txtDescription.setText(Messages.HashWizard_rdomd5_description);
//        } else if (rdo2.getSelection()) {
//            txtDescription.setText(Messages.HashWizard_rdosha1_description);
//        } else if (rdo3.getSelection()) {
//            txtDescription.setText(Messages.HashWizard_rdosha256_description);
//        } else if (rdo4.getSelection()) {
//            txtDescription.setText(Messages.HashWizard_rdosha384_description);
//        } else if (rdo5.getSelection()) {
//            txtDescription.setText(Messages.HashWizard_rdosha512_description);
//        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }
}

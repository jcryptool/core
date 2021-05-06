// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.wizardpages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class BackupRestorePage extends WizardPage {
	private static final String PAGENAME = "BackupRestorePage"; //$NON-NLS-1$

	private Button backupButton;
	private Button restoreButton;
	private boolean restoreRequested = false;
	
	public BackupRestorePage() {
		super(getPageName());
		setTitle(Messages.BackupRestorePage_0);
	}

	public BackupRestorePage(String title, ImageDescriptor titleImage) {
		super(getPageName(), title, titleImage);
		// TODO Auto-generated constructor stub
	}

	private static final String getPageName() {
		return PAGENAME;
	}
	
	public boolean getRestoreRequested() {
		return restoreRequested;
	}
	
    /** the selection listener which updates the buttons when changing from backup to restore and vice versa. */
    private final SelectionListener sl = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent e) {
            getContainer().updateButtons();
            restoreRequested = restoreButton.getSelection();
        }
    };

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(1, false));

        Label label = new Label(composite, 0);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText(Messages.BackupRestorePage_1);
        
        backupButton = new Button(composite, SWT.RADIO);
        backupButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        backupButton.setText(Messages.BackupRestorePage_2);
        backupButton.setToolTipText(Messages.BackupRestorePage_3);
        backupButton.addSelectionListener(sl);

        restoreButton = new Button(composite, SWT.RADIO);
        restoreButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        restoreButton.setText(Messages.BackupRestorePage_4);
        restoreButton.setToolTipText(Messages.BackupRestorePage_5);
        restoreButton.addSelectionListener(sl);
        
		setControl(composite);
	}

}

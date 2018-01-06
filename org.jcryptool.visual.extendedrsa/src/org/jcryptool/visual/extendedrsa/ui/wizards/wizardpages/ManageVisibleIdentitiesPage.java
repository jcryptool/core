// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa.ui.wizards.wizardpages;

import java.util.Arrays;
import java.util.Vector;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jcryptool.visual.extendedrsa.IdentityManager;

/**
 * This is the wizard to delete a new Identity with the button in the visual
 * 
 * @author Christoph Schnepf, Patrick Zillner
 * 
 */
public class ManageVisibleIdentitiesPage extends WizardPage {

    private TabFolder tabfolder;
    private Table table;
    private Vector<String> displayList;
    private Vector<String> alreadyShownIDs;

    public ManageVisibleIdentitiesPage(TabFolder folder) {
        super(Messages.ManageVisibleIdentitiesPage_0, Messages.ManageVisibleIdentitiesPage_1, null);
        setDescription(Messages.ManageVisibleIdentitiesPage_2);
        this.tabfolder = folder;

        displayList = new Vector<String>();
        alreadyShownIDs = new Vector<String>();
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        GridLayout grid = new GridLayout(1, false);
        grid.horizontalSpacing = 20;
        grid.verticalSpacing = 10;
        container.setLayout(grid);

        Label lbl = new Label(container, SWT.WRAP);
        lbl.setText(Messages.ManageVisibleIdentitiesPage_3);
        GridData gdLbl = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        lbl.setLayoutData(gdLbl);

        Vector<String> identites_vector = new Vector<String>();
        Vector<String> ids = IdentityManager.getInstance().getContacts();
        for (String s : ids) {
            if (!identites_vector.contains(s)) {
                identites_vector.add(s);
            }
        }

        String[] identities = new String[identites_vector.size()];
        identites_vector.toArray(identities);

        Arrays.sort(identities);

        // identify already shown tabs
        alreadyShownIDs = new Vector<String>();
        for (TabItem ti : tabfolder.getItems()) {
            alreadyShownIDs.add(ti.getText());
            // also add them to the display-list if the user don't changes the selection
            displayList.add(ti.getText());
        }

        table = new Table(container, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        for (String s : identities) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(s);
            if (alreadyShownIDs.contains(s)) {
                item.setChecked(true);
            }
        }

        table.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                displayList.removeAllElements();
                for (TableItem ti : table.getItems()) {
                    if (ti.getChecked()) {
                        displayList.add(ti.getText());
                    }
                }
                if (displayList.size() < 2) {
                    setPageComplete(false);
                } else {
                    setPageComplete(true);
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gdTable.heightHint = 150;
        table.setLayoutData(gdTable);

    }

    public Vector<String> getDisplayList() {
        return displayList;
    }

    public Vector<String> getAllreadyShownList() {
        return alreadyShownIDs;
    }
}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
/**
 *
 */
package org.jcryptool.core.operations.providers.preferences;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.providers.ProvidersManager;

/**
 * The PreferencesPage for the Providers.<br>
 * Allows the user to select a default Crypto Provider, or revert to the factory default Provider (which is
 * FlexiProvier). <br>
 * Additionally, the user can specify a fall-through hierarchy. This will determine the order in which the installed
 * (non-default) Providers will be searched for a requested service, if the previously searched Providers do not provide
 * the requested service.
 * 
 * @author t-kern
 * 
 */
public class ProvidersPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage, Listener {
    /** The Up Button */
    private Button upButton = null;

    /** The Down Button */
    private Button downButton = null;

    /** The (unsaved) currently specified fall-through hierarchy */
    private ArrayList<String> providerHierarchy;

    /** The group containing the table and buttons */
    private Group group;

    /** The table displaying all providers */
    private Table providersTable;

    private TableColumn numberColumn;

    private TableColumn labelColumn;

    private TableColumn defaultColumn;

    /**
     * No-Args constructor.
     */
    public ProvidersPreferencesPage() {
        super();
        setDescription(null);
    }

    /**
     * Creates the contents of this preferences page.<br>
     * Puts all composits together with the given layout, registers the listeners and fills the table.
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        Composite pageComposite = new Composite(parent, SWT.NULL);

        createGroup(pageComposite);
        pageComposite.setLayout(new GridLayout());

        initProviderHierarchy();

        fillProvidersTable();

        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(getControl(), "org.jcryptool.core.operations.providerPreferences"); //$NON-NLS-1$

        return pageComposite;
    }

    /**
     * This method initializes group
     * 
     */
    private void createGroup(Composite parent) {
        GridData upButtonGridData = new GridData();
        upButtonGridData.horizontalAlignment = GridData.FILL;
        upButtonGridData.verticalAlignment = GridData.BEGINNING;
        GridData downButtonGridData = new GridData();
        downButtonGridData.horizontalAlignment = GridData.FILL;
        downButtonGridData.verticalAlignment = GridData.BEGINNING;
        GridData providersTableGridData = new GridData();
        providersTableGridData.verticalSpan = 2;
        providersTableGridData.horizontalAlignment = GridData.FILL;
        providersTableGridData.verticalAlignment = GridData.FILL;
        providersTableGridData.grabExcessHorizontalSpace = true;
        providersTableGridData.grabExcessVerticalSpace = true;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        group = new Group(parent, SWT.NONE);
        group.setText(Messages.ProvidersPreferencesPage_0);
        group.setLayout(gridLayout);
        group.setLayoutData(gridData);
        providersTable = new Table(group, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
        providersTable.setHeaderVisible(true);
        providersTable.setLayoutData(providersTableGridData);
        providersTable.setLinesVisible(true);

        numberColumn = new TableColumn(providersTable, SWT.CENTER);
        numberColumn.setText(Messages.ProvidersPreferencesPage_1);
        numberColumn.setWidth(50);

        labelColumn = new TableColumn(providersTable, SWT.LEFT);
        labelColumn.setText(Messages.ProvidersPreferencesPage_2);
        labelColumn.setWidth(225);

        defaultColumn = new TableColumn(providersTable, SWT.LEFT);
        defaultColumn.setText(Messages.ProvidersPreferencesPage_3);
        defaultColumn.setWidth(50);

        upButton = new Button(group, SWT.NONE);
        upButton.setText(Messages.ProvidersPreferencesPage_4);
        upButton.setLayoutData(upButtonGridData);
        upButton.addListener(SWT.Selection, this);
        downButton = new Button(group, SWT.NONE);
        downButton.setText(Messages.ProvidersPreferencesPage_5);
        downButton.setLayoutData(downButtonGridData);
        downButton.addListener(SWT.Selection, this);
    }

    /**
     * Initializes the data fields for internal use.
     */
    private void initProviderHierarchy() {
        providerHierarchy = ProvidersManager.getInstance().getAvailableProviderInfos();
    }

    /**
     * Creates a new TableItem with the given parameters.
     * 
     * @param providerName The Provider that will be displayed by this TableItem
     * @param index The index of this TableItem in the table
     * @return The new TableItem
     */
    private TableItem newProviderTableItem(String providerName, int index) {
        TableItem item = new TableItem(providersTable, index);
        item.setText(0, String.valueOf(index + 1));
        item.setText(1, providerName);
        if (index == 0)
            item.setText(2, Messages.ProvidersPreferencesPage_6);
        return item;
    }

    /**
     * Fills the Provider table with the installed CryptoProviders and sets the parameters accordingly. (Reads the
     * default Provider from the ProvidersManager singleton)
     */
    private void fillProvidersTable() {
        providersTable.removeAll();
        for (int i = 0; i < providerHierarchy.size(); i++) {
            newProviderTableItem(providerHierarchy.get(i), i);
        }
        providersTable.setSelection(0);
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

    /**
     * Handles the event of the "Apply" (AND/OR "Okay") button in the lower right corner.
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    public boolean performOk() {
        performApply();
        return super.performOk();
    }

    /**
     * Handles the event of the "Reset to Default" button in the lower right corner.
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    protected void performDefaults() {
        // move flexi to the top of the table
        int index = providerHierarchy.indexOf(ProvidersManager.getInstance().getMetaFactoryDefault().getInfo());
        if (index != 0) {
            LogUtil.logInfo("index_of_default" + index); //$NON-NLS-1$
            String factoryDefaultInfo = providerHierarchy.get(index);
            providerHierarchy.remove(index);
            providerHierarchy.add(0, factoryDefaultInfo);
            fillProvidersTable();
        }
    }

    /**
     * Handles the event of the "Apply" button in the lower right corner.
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    protected void performApply() {
        setProviderHierarchy();
        save();
        super.updateApplyButton();
    }

    /**
     * Handles all events that occur in the context of this preferences page.
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event) {
        if (event.widget == upButton) {
            upButtonAction();
        } else if (event.widget == downButton) {
            downButtonAction();
        }
    }

    /**
     * Moves a selected item up one index in the table.
     */
    private void upButtonAction() {
        int selected = providersTable.getSelectionIndex();
        if (selected != -1) {
            if (selected == 0) {
                // can't move the uppermost item anymore to the top... doing
                // nothing
            } else {
                // the item we want to move up
                String delta = providerHierarchy.get(selected);
                // took it out of the vector
                providerHierarchy.remove(selected);
                // add it one position up
                providerHierarchy.add(selected - 1, delta);

                fillProvidersTable();
                providersTable.setSelection(selected - 1);
            }
        } else {
            MessageDialog.openInformation(this.getShell(), Messages.ProvidersPreferencesPage_7,
                    Messages.ProvidersPreferencesPage_8);
        }
    }

    /**
     * Moves a selected item down one index in the table.
     */
    private void downButtonAction() {
        int selected = providersTable.getSelectionIndex();
        if (selected != -1) {
            if (selected == providerHierarchy.size() - 1) {
                // can't move the downmost item anymore to the botton... doing
                // nothing
            } else {
                String delta = providerHierarchy.get(selected);
                providerHierarchy.remove(selected);
                providerHierarchy.add(selected + 1, delta);
                fillProvidersTable();
                providersTable.setSelection(selected + 1);
            }
        } else {
            MessageDialog.openInformation(this.getShell(), Messages.ProvidersPreferencesPage_7,
                    Messages.ProvidersPreferencesPage_8);
        }
    }

    /**
     * Sets the fall-through hierarchy in the Activator.<br>
     * <br>
     * Converts the internal Vector to a formatted String with the correct delimiters.
     */
    private void setProviderHierarchy() {
        ProvidersManager.getInstance().setProviderHierarchy(providerHierarchy);
    }

    /**
     * Saves the preferences store.
     */
    private void save() {
        ProvidersManager.getInstance().savePreferences();
    }

}

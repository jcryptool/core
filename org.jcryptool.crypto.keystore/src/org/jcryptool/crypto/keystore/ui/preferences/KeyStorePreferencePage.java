// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.preferences;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.ui.wizards.INewKeyStoreDescriptor;
import org.jcryptool.crypto.keystore.ui.wizards.INewKeyStoreWizard;
import org.jcryptool.crypto.keystore.ui.wizards.NewKeyStoreWizard;

/**
 * @author tkern
 *
 */
public class KeyStorePreferencePage extends PreferencePage implements IWorkbenchPreferencePage, Listener {
    public static final String ID = "org.jcryptool.crypto.keystore.ui.internal.preferences.KeyStorePreferencePage"; //$NON-NLS-1$

    /** The Remove Button */
    private Button removeButton = null;

    /** The New Button */
    private Button newButton = null;

    /** All available keystores */
    private List<String> availableKeyStores;

    /** The group containing the table and buttons */
    private Group group;

    /** The table displaying all providers */
    private Table keyStoreTable;

    private TableColumn keyStoreNameColumn;

    private TableColumn keyStoreFileColumn;

    private Shell shell;

    private WizardDialog dialog;

    /**
     * No-Args constructor.
     */
    public KeyStorePreferencePage() {
        noDefaultAndApplyButton();
        setDescription(null);
        KeyStorePlugin.loadPreferences();
        getAvailableKeyStores();
    }

    public void getAvailableKeyStores() {
        availableKeyStores = KeyStorePlugin.getAvailableKeyStores();
        Collections.sort(availableKeyStores);
        Iterator<String> it = availableKeyStores.iterator();
        LogUtil.logInfo("printing available keystores"); //$NON-NLS-1$
        while (it.hasNext()) {
            LogUtil.logInfo("KeyStore: " + it.next()); //$NON-NLS-1$
        }
    }

    /**
     * Creates the contents of this preferences page.<br>
     * Puts all composits together with the given layout, registers the listeners and fills the table.
     *
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite parent) {
        Composite pageComposite = new Composite(parent, SWT.NULL);

        createGroup(pageComposite);
        pageComposite.setLayout(new GridLayout());

        fillKeyStoresTable();

        return pageComposite;
    }

    /**
     * Creates a new TableItem with the given parameters.
     *
     * @param keyStoreName The name of the keystore
     * @param index The index of this TableItem in the table
     * @return The new TableItem
     */
    private TableItem newKeyStoreTableItem(String keyStoreName, int index) {
        TableItem item = new TableItem(keyStoreTable, index);
        item.setText(0, keyStoreName.substring(0, keyStoreName.indexOf(KeyStorePlugin.KEYSTORE_PREFERENCES_SEPARATOR)));
        item.setText(1, keyStoreName.substring(keyStoreName.indexOf(KeyStorePlugin.KEYSTORE_PREFERENCES_SEPARATOR) + 1,
                keyStoreName.length()));
        return item;
    }

    /**
     * Fills the Provider table with the installed CryptoProviders and sets the parameters accordingly. (Reads the
     * default Provider from the ProvidersManager singleton)
     */
    private void fillKeyStoresTable() {
        keyStoreTable.removeAll();
        LogUtil.logInfo("size: " + availableKeyStores.size()); //$NON-NLS-1$
        for (int i = 0; i < availableKeyStores.size(); i++) {
            LogUtil.logInfo("Entry: " + availableKeyStores.get(i)); //$NON-NLS-1$
            newKeyStoreTableItem(availableKeyStores.get(i), i);
        }
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

    /**
     * Handles all events that occur in the context of this preferences page.
     *
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event) {
        if (event.widget == removeButton) {
            removeAction();
        } else if (event.widget == newButton) {
            LogUtil.logInfo("new keystore"); //$NON-NLS-1$
            newKeyStoreAction();
        }
    }

    private void newKeyStoreAction() {
        shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        Wizard wizard = new NewKeyStoreWizard();
        dialog = new WizardDialog(shell, wizard);
        dialog.setMinimumPageSize(300, 175);

        int result = dialog.open();
        if (result == Window.OK) {
            INewKeyStoreDescriptor desc = ((INewKeyStoreWizard) wizard).getNewKeyStoreDescriptor();
            LogUtil.logInfo("descriptor name: " + desc.getKeyStoreName()); //$NON-NLS-1$
            LogUtil.logInfo("descriptor path:" + desc.getKeyStorePath()); //$NON-NLS-1$

            // URI keyStoreURI = new URI(desc.getKeyStorePath().);

            // create the new keystore
            KeyStoreManager.getInstance().createNewKeyStore(URIUtil.toURI(desc.getKeyStorePath()));

            // add it to the list
            availableKeyStores.add(desc.getKeyStoreName() + KeyStorePlugin.KEYSTORE_PREFERENCES_SEPARATOR
                    + URIUtil.toURI(desc.getKeyStorePath()).toString());
            fillKeyStoresTable();

            // save preferences
            save();
        }
    }

    /**
     * Saves changes to the preferences.
     */
    private void save() {
        LogUtil.logInfo("saving (size): " + availableKeyStores.size()); //$NON-NLS-1$
        KeyStorePlugin.setAvailableKeyStores(availableKeyStores);
        KeyStorePlugin.savePreferences();
    }

    /**
     * Moves a selected item down one index in the table.
     */
    private void removeAction() {
        if (keyStoreTable.getSelectionIndex() == -1) {
            MessageDialog.openInformation(getShell(), Messages.getString("KeyStorePreferencePage.0"),  //$NON-NLS-1$
                    Messages.getString("KeyStorePreferencePage.2")); //$NON-NLS-1$
        } else if (availableKeyStores.get(keyStoreTable.getSelectionIndex()).toLowerCase().startsWith(
                KeyStorePlugin.getCurrentKeyStore().toLowerCase())) {
            MessageDialog.openInformation(getShell(), Messages.getString("KeyStorePreferencePage.0"),  //$NON-NLS-1$
                    Messages.getString("KeyStorePreferencePage.1")); //$NON-NLS-1$
        } else {
            String toBeRemoved = availableKeyStores.get(keyStoreTable.getSelectionIndex());
            LogUtil.logInfo("removing the selected keystore " + toBeRemoved); //$NON-NLS-1$

            try {
                // remove the file
                URI uri = new URI(toBeRemoved.substring(toBeRemoved.indexOf(",") + 1, toBeRemoved.length())); //$NON-NLS-1$
                IFileStore store = EFS.getStore(uri);
                store.delete(EFS.NONE, null);

                // remove the entry
                availableKeyStores.remove(keyStoreTable.getSelectionIndex());
                KeyStorePlugin.setAvailableKeyStores(availableKeyStores);
                KeyStorePlugin.savePreferences();

                // reload the table
                fillKeyStoresTable();
            } catch (URISyntaxException e) {
                LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "URISyntaxException while creating a uri", e, false); //$NON-NLS-1$
            } catch (CoreException e) {
                LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CoreException ", e, false); //$NON-NLS-1$
            }
        }
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
        providersTableGridData.verticalSpan = 3;
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
        group.setText(Messages.getString("Label.AvailableKeystores")); //$NON-NLS-1$
        group.setLayout(gridLayout);
        group.setLayoutData(gridData);
        keyStoreTable = new Table(group, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
        keyStoreTable.setHeaderVisible(true);
        keyStoreTable.setLayoutData(providersTableGridData);
        keyStoreTable.setLinesVisible(true);

        keyStoreNameColumn = new TableColumn(keyStoreTable, SWT.LEFT);
        keyStoreNameColumn.setText(Messages.getString("Label.Keystores")); //$NON-NLS-1$
        keyStoreNameColumn.setWidth(125);

        keyStoreFileColumn = new TableColumn(keyStoreTable, SWT.LEFT);
        keyStoreFileColumn.setText(Messages.getString("Label.KeystorePath")); //$NON-NLS-1$
        keyStoreFileColumn.setWidth(175);

        newButton = new Button(group, SWT.NONE);
        newButton.setText(Messages.getString("Label.New")); //$NON-NLS-1$
        newButton.setLayoutData(upButtonGridData);
        newButton.addListener(SWT.Selection, this);

        removeButton = new Button(group, SWT.NONE);
        removeButton.setText(Messages.getString("Label.Remove")); //$NON-NLS-1$
        removeButton.setLayoutData(downButtonGridData);
        removeButton.addListener(SWT.Selection, this);
    }

}

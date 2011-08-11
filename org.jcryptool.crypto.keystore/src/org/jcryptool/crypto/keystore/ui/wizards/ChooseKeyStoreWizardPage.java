//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.wizards;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;

/**
 * @author tkern
 *
 */
public class ChooseKeyStoreWizardPage extends WizardPage implements Listener {
	private Group selectionGroup;
	private Table keyStoreTable;
	private TableColumn keyStoreNameColumn;
	private TableColumn keyStoreFileColumn;

	/** All available keystores */
	private List<String> availableKeyStores;

	public ChooseKeyStoreWizardPage() {
		super("1", Messages.getString("ChooseKeyStoreWizardPage.0"), //$NON-NLS-1$ //$NON-NLS-2$
		        KeyStorePlugin.getImageDescriptor("icons/48x48/kgpg_info.png")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setPageComplete(false);
		getAvailableKeyStores();
	}

	protected String getSelectedKeyStore() {
		int selectionIndex = keyStoreTable.getSelectionIndex();
		if (selectionIndex != -1) {
			return availableKeyStores.get(selectionIndex);
		} else {
			return null;
		}
	}

	public void handleEvent(Event event) {
//		if (event.widget.equals(keyStoreTable)) {
//
//		}
		setPageComplete(isComplete());
	}

	private boolean isComplete() {
		if (keyStoreTable.getSelectionIndex() != -1) {
			return true;
		}
		return false;
	}

	private void registerListener() {
		keyStoreTable.addListener(SWT.Selection, this);
	}

	public void createControl(Composite parent) {
		LogUtil.logInfo("creating control"); //$NON-NLS-1$
		Composite pageComposite = new Composite(parent, SWT.NULL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = true;
		pageComposite.setLayout(gridLayout);
		GridData gridData = new GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		pageComposite.setLayoutData(gridData);

		createSelectionGroup(pageComposite);

		fillKeyStoreTable();
		registerListener();
		pageComposite.setSize(350, 350);
		setControl(pageComposite);
	}

	public void getAvailableKeyStores() {
		availableKeyStores = KeyStorePlugin.getAvailableKeyStores();
		Collections.sort(availableKeyStores);
	}

	private void fillKeyStoreTable() {
		keyStoreTable.removeAll();
		LogUtil.logInfo("size: " + availableKeyStores.size()); //$NON-NLS-1$
		for (int i=0; i < availableKeyStores.size(); i++) {
			LogUtil.logInfo("Entry: " + availableKeyStores.get(i)); //$NON-NLS-1$
			newKeyStoreTableItem(availableKeyStores.get(i), i);
		}
	}

	/**
	 * Creates a new TableItem with the given parameters.
	 *
	 * @param keyStoreName	The name of the keystore
	 * @param index			The index of this TableItem in the table
	 * @return				The new TableItem
	 */
	private TableItem newKeyStoreTableItem(String keyStoreName, int index) {
		TableItem item = new TableItem(keyStoreTable, index);
		item.setText(0, keyStoreName.substring(0, keyStoreName.indexOf(KeyStorePlugin.KEYSTORE_PREFERENCES_SEPARATOR)));
		item.setText(1, keyStoreName.substring(keyStoreName.indexOf(KeyStorePlugin.KEYSTORE_PREFERENCES_SEPARATOR)+1, keyStoreName.length()));
		return item;
	}

	/**
	 * This method initializes selectionGroup
	 *
	 */
	private void createSelectionGroup(Composite parent) {
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = GridData.FILL;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		selectionGroup = new Group(parent, SWT.NONE);
		selectionGroup.setLayout(new GridLayout());
		selectionGroup.setLayoutData(gridData);
		selectionGroup.setText(Messages.getString("Label.SelectKeystore")); //$NON-NLS-1$
		keyStoreTable = new Table(selectionGroup, SWT.SINGLE);
		keyStoreTable.setHeaderVisible(true);
		keyStoreTable.setLayoutData(gridData1);
		keyStoreTable.setLinesVisible(true);

		keyStoreNameColumn = new TableColumn(keyStoreTable, SWT.LEFT);
		keyStoreNameColumn.setText(Messages.getString("Label.Keystores")); //$NON-NLS-1$
		keyStoreNameColumn.setWidth(125);

		keyStoreFileColumn = new TableColumn(keyStoreTable, SWT.LEFT);
		keyStoreFileColumn.setText(Messages.getString("Label.KeystorePath")); //$NON-NLS-1$
		keyStoreFileColumn.setWidth(175);

	}

}

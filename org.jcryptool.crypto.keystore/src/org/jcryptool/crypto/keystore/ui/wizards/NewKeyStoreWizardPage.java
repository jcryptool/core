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
/**
 *
 */
package org.jcryptool.crypto.keystore.ui.wizards;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;

/**
 * @author tkern
 *
 */
public class NewKeyStoreWizardPage extends WizardPage implements Listener {
	private Group keyStoreNameGroup;
	private Text keyStoreNameText;
	private Label keyStoreNameLabel;
	private Group fileNameGroup;
	private Label fileNameDescriptionLabel;
	private Button fileNameButton;
	private Label fileNameLabel;
	private IPath fileNamePath;

	protected NewKeyStoreWizardPage() {
		super("1", Messages.getString("Label.NewKeyStore"), KeyStorePlugin.getImageDescriptor("icons/48x48/kgpg_info.png")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setPageComplete(false);
	}

	private void registerListeners() {
		keyStoreNameText.addListener(SWT.Modify, this);
		fileNameButton.addListener(SWT.Selection, this);
	}

	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget.equals(fileNameButton)) {
			fileNameButtonAction();
		}
		setPageComplete(isComplete());
	}

	private boolean isComplete() {
		if (fileNamePath != null) {
			if (keyStoreNameText.getText() != null || !keyStoreNameText.getText().equals("")) { //$NON-NLS-1$
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private void fileNameButtonAction() {
		FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterExtensions(new String[] {"*.ksf"}); //$NON-NLS-1$
        dialog.setFilterNames(new String[] {Messages.getString("NewKeyStoreWizardPage.1")}); //$NON-NLS-1$
        dialog.setOverwrite(true);

		String filename = dialog.open();

		if (filename != null) {
    		fileNamePath = new Path(filename);
    		fileNameLabel.setText(fileNamePath.toString());
		}
	}

	public INewKeyStoreDescriptor getNewKeyStoreDescriptor() {
		return new NewKeyStoreDescriptor(keyStoreNameText.getText(), fileNamePath);
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
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

		// building the composite
		createKeyStoreNameGroup(pageComposite);
		createFileNameGroup(pageComposite);

		// register listeners
		registerListeners();

		pageComposite.setSize(350, 350);
		setControl(pageComposite);
	}

	/**
	 * This method initializes keyStoreNameGroup
	 *
	 */
	private void createKeyStoreNameGroup(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = GridData.CENTER;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = GridData.CENTER;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		keyStoreNameGroup = new Group(parent, SWT.NONE);
		keyStoreNameGroup.setText(Messages.getString("LabelKeyStoreName")); //$NON-NLS-1$
		keyStoreNameGroup.setLayout(gridLayout);
		keyStoreNameGroup.setLayoutData(gridData);
		keyStoreNameLabel = new Label(keyStoreNameGroup, SWT.NONE);
		keyStoreNameLabel.setText(Messages.getString("LabelKeyStoreEnterName")); //$NON-NLS-1$
		keyStoreNameLabel.setLayoutData(gridData2);
		keyStoreNameText = new Text(keyStoreNameGroup, SWT.BORDER);
		keyStoreNameText.setLayoutData(gridData3);
	}

	/**
	 * This method initializes fileNameGroup
	 *
	 */
	private void createFileNameGroup(Composite parent) {
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.FILL;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = GridData.CENTER;
		GridData gridData4 = new GridData();
		gridData4.horizontalSpan = 2;
		gridData4.verticalAlignment = GridData.CENTER;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		fileNameGroup = new Group(parent, SWT.NONE);
		fileNameGroup.setText(Messages.getString("LabelFileName")); //$NON-NLS-1$
		fileNameGroup.setLayout(gridLayout1);
		fileNameGroup.setLayoutData(gridData1);
		fileNameDescriptionLabel = new Label(fileNameGroup, SWT.NONE);
		fileNameDescriptionLabel.setText(Messages.getString("LabelKeyStoreLocation")); //$NON-NLS-1$
		fileNameDescriptionLabel.setLayoutData(gridData4);
		fileNameButton = new Button(fileNameGroup, SWT.NONE);
		fileNameButton.setText(Messages.getString("LabelCreateFile")); //$NON-NLS-1$
		fileNameLabel = new Label(fileNameGroup, SWT.NONE);
		fileNameLabel.setText(Messages.getString("LabelNoFile")); //$NON-NLS-1$
		fileNameLabel.setLayoutData(gridData5);
	}

}

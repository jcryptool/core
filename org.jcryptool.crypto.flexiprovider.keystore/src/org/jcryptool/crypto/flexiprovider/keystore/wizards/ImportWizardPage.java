// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.keystore.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.descriptors.ImportDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IImportDescriptor;
import org.jcryptool.crypto.keystore.keys.KeyType;
import org.jcryptool.crypto.keystore.ui.views.nodes.Contact;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;

/**
 * @author tkern
 *
 */
public class ImportWizardPage extends WizardPage implements Listener {
    private Group contactGroup;
    private Label contactDescriptionLabel;
    private Label contactNameLabel;
    private Combo contactNameCombo;
    private Group passwordGroup;
    private Label passwordDescriptionLabel;
    private Label enterPasswordLabel;
    private Text enterPasswordText;
    private Label confirmPasswordLabel;
    private Text confirmPasswordText;
    private Group importGroup;
    private Button secretKeyRadioButton;
    private Button sourceButton;
    private Button keyPairRadioButton;
    private Label selectedFileDescriptionLabel;
    private Button certificateRadioButton;
    private Label fileNameLabel;

    protected ImportWizardPage() {
        super("1", Messages.ImportWizardPage_0,
        		ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/48x48/kgpg_import.png"));
        setDescription(Messages.ImportWizardPage_1);
        setPageComplete(false);
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
        createContactGroup(pageComposite);
        createImportGroup(pageComposite);
        createPasswordGroup(pageComposite);

        // inititializing the composite
        initContactCombo();
        initImportGroup();

        // register listeners
        registerListeners();

        pageComposite.setSize(350, 350);
        setControl(pageComposite);
    }

    private void registerListeners() {
        secretKeyRadioButton.addListener(SWT.Selection, this);
        keyPairRadioButton.addListener(SWT.Selection, this);
        certificateRadioButton.addListener(SWT.Selection, this);
        sourceButton.addListener(SWT.Selection, this);
        enterPasswordText.addListener(SWT.Modify, this);
        confirmPasswordText.addListener(SWT.Modify, this);
    }

    private void initImportGroup() {
        secretKeyRadioButton.setSelection(true);
    }

    private void initContactCombo() {
        int size = ContactManager.getInstance().getContactSize();
        if (size > 0) {
            List<String> contactNames = new ArrayList<String>();
            Iterator<Contact> it = ContactManager.getInstance().getContacts();
            Contact contact;
            while (it.hasNext()) {
                contact = it.next();
                contactNames.add(contact.getName());
            }

            String[] contactNamesArray = (String[]) contactNames.toArray(new String[] {});
            Arrays.sort(contactNamesArray);
            contactNameCombo.setItems(contactNamesArray);
            contactNameCombo.select(0);
        } else {
            LogUtil.logInfo("No Contact"); //$NON-NLS-1$
        }
    }

    private String fileName = null;

    public void handleEvent(Event event) {
        if (event.widget.equals(secretKeyRadioButton)) {
            enterPasswordText.setEnabled(true);
            confirmPasswordText.setEnabled(true);
        } else if (event.widget.equals(keyPairRadioButton)) {
            enterPasswordText.setEnabled(true);
            confirmPasswordText.setEnabled(true);
        } else if (event.widget.equals(certificateRadioButton)) {
            enterPasswordText.setEnabled(false);
            confirmPasswordText.setEnabled(false);
        } else if (event.widget.equals(sourceButton)) {
            LogUtil.logInfo("opening file dialog"); //$NON-NLS-1$
            fileName = getImportFileName();
            if (fileName != null) {
                File file = new File(fileName);
                fileNameLabel.setText(file.getName());
                fileNameLabel.setToolTipText(fileName);
                getControl().pack();
            }
        }
        setPageComplete(isComplete());
    }

    protected IImportDescriptor getImportDescriptor() {
        KeyType type = null;
        if (secretKeyRadioButton.getSelection()) {
            type = KeyType.SECRETKEY;
        } else if (keyPairRadioButton.getSelection()) {
            type = KeyType.KEYPAIR;
        } else if (certificateRadioButton.getSelection()) {
            type = KeyType.PUBLICKEY;
        }
        return new ImportDescriptor(contactNameCombo.getText(), "", type, fileName, enterPasswordText.getText(), //$NON-NLS-1$
                "FlexiCore", -1); //$NON-NLS-1$
    }

    private boolean isComplete() {
        if (contactNameCombo.getText() != null) {
            // contact is specified
            if (fileName != null) {
                if (secretKeyRadioButton.getSelection() || keyPairRadioButton.getSelection()) {
                    // make sure the pws are in order
                    LogUtil.logInfo("secret key or key pair"); //$NON-NLS-1$
                    if ((!enterPasswordText.getText().equals("")) //$NON-NLS-1$
                            && enterPasswordText.getText().equals(confirmPasswordText.getText())) {
                        LogUtil.logInfo("enterPasswordText:" + enterPasswordText.getText() + ":"); //$NON-NLS-1$ //$NON-NLS-2$
                        LogUtil.logInfo("returning true :-)"); //$NON-NLS-1$
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    LogUtil.logInfo("certificate"); //$NON-NLS-1$
                    return true;
                }

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private String getImportFileName() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        String[] filters = null;
        if (secretKeyRadioButton.getSelection()) {
            filters = new String[1];
            filters[0] = "*.sec"; //$NON-NLS-1$
        } else if (keyPairRadioButton.getSelection()) {
            filters = new String[2];
            filters[0] = "*.p12"; //$NON-NLS-1$
            filters[1] = "*.pfx"; //$NON-NLS-1$
        } else if (certificateRadioButton.getSelection()) {
            filters = new String[1];
            filters[0] = "*.cer"; //$NON-NLS-1$
        }

        dialog.setFilterExtensions(filters);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());

        return dialog.open();
    }

    /* ---------- just boring stuff beneath this point ---------- */

    /**
     * This method initializes contactGroup
     *
     */
    private void createContactGroup(Composite parent) {
        GridData gridData5 = new GridData();
        gridData5.grabExcessVerticalSpace = true;
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.END;
        gridData4.grabExcessVerticalSpace = true;
        gridData4.widthHint = 200;
        gridData4.verticalAlignment = GridData.CENTER;
        GridData gridData3 = new GridData();
        gridData3.grabExcessHorizontalSpace = true;
        gridData3.grabExcessVerticalSpace = true;
        gridData3.horizontalSpan = 2;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        contactGroup = new Group(parent, SWT.NONE);
        contactGroup.setText(Messages.ImportWizardPage_2);
        contactGroup.setLayout(gridLayout);
        contactGroup.setLayoutData(gridData);
        contactDescriptionLabel = new Label(contactGroup, SWT.NONE);
        contactDescriptionLabel.setText(Messages.ImportWizardPage_3);
        contactDescriptionLabel.setLayoutData(gridData3);
        contactNameLabel = new Label(contactGroup, SWT.NONE);
        contactNameLabel.setText(Messages.ImportWizardPage_4);
        contactNameLabel.setLayoutData(gridData5);
        contactNameCombo = new Combo(contactGroup, SWT.BORDER);

        contactNameCombo.setLayoutData(gridData4);
    }

    /**
     * This method initializes importGroup
     *
     */
    private void createImportGroup(Composite parent) {
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.BEGINNING;
        gridData6.grabExcessHorizontalSpace = true;
        gridData6.grabExcessVerticalSpace = true;
        gridData6.verticalAlignment = GridData.CENTER;
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = GridData.BEGINNING;
        gridData5.grabExcessHorizontalSpace = true;
        gridData5.grabExcessVerticalSpace = true;
        gridData5.verticalAlignment = GridData.CENTER;
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.BEGINNING;
        gridData4.grabExcessHorizontalSpace = true;
        gridData4.grabExcessVerticalSpace = true;
        gridData4.verticalAlignment = GridData.CENTER;
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL;
        gridData3.grabExcessHorizontalSpace = true;
        gridData3.grabExcessVerticalSpace = true;
        gridData3.verticalAlignment = GridData.FILL;
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.verticalAlignment = GridData.FILL;
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.verticalAlignment = GridData.FILL;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        importGroup = new Group(parent, SWT.NONE);
        importGroup.setText(Messages.ImportWizardPage_5);
        importGroup.setLayout(gridLayout);
        importGroup.setLayoutData(gridData);
        secretKeyRadioButton = new Button(importGroup, SWT.RADIO);
        secretKeyRadioButton.setText(Messages.ImportWizardPage_6);
        secretKeyRadioButton.setLayoutData(gridData1);
        sourceButton = new Button(importGroup, SWT.NONE);
        sourceButton.setText(Messages.ImportWizardPage_7);
        sourceButton.setLayoutData(gridData6);
        keyPairRadioButton = new Button(importGroup, SWT.RADIO);
        keyPairRadioButton.setText(Messages.ImportWizardPage_8);
        keyPairRadioButton.setLayoutData(gridData2);
        selectedFileDescriptionLabel = new Label(importGroup, SWT.NONE);
        selectedFileDescriptionLabel.setText(Messages.ImportWizardPage_9);
        selectedFileDescriptionLabel.setLayoutData(gridData5);
        certificateRadioButton = new Button(importGroup, SWT.RADIO);
        certificateRadioButton.setText(Messages.ImportWizardPage_10);
        certificateRadioButton.setLayoutData(gridData3);
        fileNameLabel = new Label(importGroup, SWT.NONE);
        fileNameLabel.setText(Messages.ImportWizardPage_11);
        fileNameLabel.setLayoutData(gridData4);
    }

    /**
     * This method initializes passwordGroup
     *
     */
    private void createPasswordGroup(Composite parent) {
        GridData gridData16 = new GridData();
        gridData16.grabExcessVerticalSpace = true;
        GridData gridData15 = new GridData();
        gridData15.grabExcessVerticalSpace = true;
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = GridData.END;
        gridData11.grabExcessVerticalSpace = true;
        gridData11.widthHint = 200;
        gridData11.verticalAlignment = GridData.CENTER;
        GridData gridData10 = new GridData();
        gridData10.horizontalAlignment = GridData.END;
        gridData10.grabExcessVerticalSpace = true;
        gridData10.widthHint = 200;
        gridData10.verticalAlignment = GridData.CENTER;
        GridData gridData9 = new GridData();
        gridData9.horizontalSpan = 2;
        gridData9.grabExcessVerticalSpace = true;
        gridData9.grabExcessHorizontalSpace = true;
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 2;
        GridData gridData2 = new GridData();
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.verticalAlignment = GridData.FILL;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.horizontalAlignment = GridData.FILL;
        passwordGroup = new Group(parent, SWT.NONE);
        passwordGroup.setText(Messages.ImportWizardPage_12);
        passwordGroup.setLayout(gridLayout2);
        passwordGroup.setLayoutData(gridData2);
//        passwordDescriptionLabel = new Label(passwordGroup, SWT.NONE);
//        passwordDescriptionLabel.setText(Messages.ImportWizardPage_13);
//        passwordDescriptionLabel.setLayoutData(gridData9);
        enterPasswordLabel = new Label(passwordGroup, SWT.NONE);
        enterPasswordLabel.setText(Messages.ImportWizardPage_14);
        enterPasswordLabel.setLayoutData(gridData15);
        enterPasswordText = new Text(passwordGroup, SWT.BORDER | SWT.PASSWORD);
        enterPasswordText.setLayoutData(gridData10);
        confirmPasswordLabel = new Label(passwordGroup, SWT.NONE);
        confirmPasswordLabel.setText(Messages.ImportWizardPage_15);
        confirmPasswordLabel.setLayoutData(gridData16);
        confirmPasswordText = new Text(passwordGroup, SWT.BORDER | SWT.PASSWORD);
        confirmPasswordText.setLayoutData(gridData11);
    }

}

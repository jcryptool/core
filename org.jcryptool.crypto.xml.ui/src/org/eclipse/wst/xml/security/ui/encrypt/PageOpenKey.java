/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.encrypt;

import java.io.File;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.xml.security.core.cryptography.Keystore;
import org.eclipse.wst.xml.security.core.encrypt.Encryption;
import org.eclipse.wst.xml.security.core.utils.IGlobals;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.utils.IContextHelpIds;
import org.jcryptool.core.util.directories.DirectoryService;

/**
 * <p>Second default page of the Encryption Wizard. Lets the user select an existing <i>Public Key</i> in an existing
 * <i>Java KeyStore</i>.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PageOpenKey extends WizardPage implements Listener {
    /** Wizard page name. */
    public static final String PAGE_NAME = "EncryptPageOpenKey"; //$NON-NLS-1$
    /** Open Keystore button. */
    private Button bOpen = null;
    /** Button <i>Echo Keystore Password</i>. */
    private Button bEchoKeystorePassword = null;
    /** Button <i>Echo Key Password</i>. */
    private Button bEchoKeyPassword = null;
    /** Keystore name. */
    private Text tKeystore = null;
    /** Keystore password. */
    private Text tKeystorePassword = null;
    /** Key name. */
    private Text tKeyName = null;
    /** Key password. */
    private Text tKeyPassword = null;
    /** Default label width. */
    private static final int LABELWIDTH = 120;
    /** Model for the XML Encryption Wizard. */
    private Encryption encryption = null;
    /** The keystore containing all required key information. */
    private Keystore keystore = null;

    /**
     * Constructor for PageOpenKey.
     *
     * @param encryption The encryption wizard model
     */
    public PageOpenKey(final Encryption encryption) {
        super(PAGE_NAME);
        setTitle(Messages.encryptionTitle);
        setDescription(Messages.openKeyDescription);

        this.encryption = encryption;
    }

    /**
     * Creates the wizard page with the layout settings.
     *
     * @param parent The parent composite
     */
    public void createControl(final Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new FormLayout());

        createPageContent(container);
        loadSettings();
        addListeners();
        setControl(container);
        setPageComplete(false);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IContextHelpIds.WIZARD_ENCRYPTION_OPEN_KEY);
    }

    /**
     * Fills this wizard page with content. Two groups (<i>Keystore</i> and <i>Key</i>) and all their widgets
     * are inserted.
     *
     * @param parent The parent composite
     */
    private void createPageContent(final Composite parent) {
        FormLayout layout = new FormLayout();
        layout.marginTop = IGlobals.MARGIN / 2;
        layout.marginBottom = IGlobals.MARGIN / 2;
        layout.marginLeft = IGlobals.MARGIN / 2;
        layout.marginRight = IGlobals.MARGIN / 2;
        parent.setLayout(layout);

        // Two groups
        Group gKeystore = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gKeystore.setLayout(layout);
        gKeystore.setText(Messages.keystore);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gKeystore.setLayoutData(data);

        Group gKey = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gKey.setLayout(layout);
        gKey.setText(Messages.key);
        data = new FormData();
        data.top = new FormAttachment(gKeystore, IGlobals.MARGIN, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gKey.setLayoutData(data);

        // Elements for group "Key"
        Label lKeyName = new Label(gKey, SWT.SHADOW_IN);
        lKeyName.setText(Messages.name);
        data = new FormData();
        data.top = new FormAttachment(gKey);
        data.left = new FormAttachment(gKey);
        data.width = LABELWIDTH;
        lKeyName.setLayoutData(data);

        tKeyName = new Text(gKey, SWT.SINGLE);
        tKeyName.setTextLimit(IGlobals.KEY_NAME_MAX_SIZE);
        data = new FormData();
        data.top = new FormAttachment(lKeyName, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyName);
        data.width = IGlobals.SHORT_TEXT_WIDTH;
        tKeyName.setLayoutData(data);

        Label lKeyPassword = new Label(gKey, SWT.SHADOW_IN);
        lKeyPassword.setText(Messages.password);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lKeyName, IGlobals.MARGIN);
        data.left = new FormAttachment(gKey);
        lKeyPassword.setLayoutData(data);

        tKeyPassword = new Text(gKey, SWT.SINGLE);
        tKeyPassword.setTextLimit(IGlobals.KEYSTORE_PASSWORD_MAX_SIZE);
        data = new FormData();
        data.width = IGlobals.SHORT_TEXT_WIDTH;
        data.top = new FormAttachment(lKeyPassword, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyPassword);
        tKeyPassword.setEchoChar('*');
        tKeyPassword.setLayoutData(data);

        bEchoKeyPassword = new Button(gKey, SWT.PUSH);
        bEchoKeyPassword.setImage(XSTUIPlugin.getDefault().getImageRegistry().get("echo_password"));
        bEchoKeyPassword.setToolTipText(Messages.echoPassword);
        data = new FormData();
        data.top = new FormAttachment(tKeyPassword, 0, SWT.CENTER);
        data.left = new FormAttachment(tKeyPassword, IGlobals.MARGIN);
        bEchoKeyPassword.setLayoutData(data);

        // Elements for group "Keystore"
        Label lKeystore = new Label(gKeystore, SWT.SHADOW_IN);
        lKeystore.setText(Messages.name);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(gKeystore);
        data.left = new FormAttachment(gKeystore);
        lKeystore.setLayoutData(data);

        tKeystore = new Text(gKeystore, SWT.SINGLE);
        data = new FormData();
        data.top = new FormAttachment(lKeystore, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeystore);
        data.width = IGlobals.SHORT_TEXT_WIDTH;
        tKeystore.setLayoutData(data);

        bOpen = new Button(gKeystore, SWT.PUSH);
        bOpen.setText(Messages.open);
        data = new FormData();
        data.top = new FormAttachment(lKeystore, 0, SWT.CENTER);
        data.left = new FormAttachment(tKeystore, IGlobals.MARGIN);
        bOpen.setLayoutData(data);

        Label lKeystorePassword = new Label(gKeystore, SWT.SHADOW_IN);
        lKeystorePassword.setText(Messages.password);
        data = new FormData();
        data.top = new FormAttachment(lKeystore, IGlobals.MARGIN);
        data.left = new FormAttachment(gKeystore);
        data.width = LABELWIDTH;
        lKeystorePassword.setLayoutData(data);

        tKeystorePassword = new Text(gKeystore, SWT.SINGLE);
        tKeystorePassword.setTextLimit(IGlobals.KEYSTORE_PASSWORD_MAX_SIZE);
        data = new FormData();
        data.top = new FormAttachment(lKeystorePassword, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeystorePassword);
        data.width = IGlobals.SHORT_TEXT_WIDTH;
        tKeystorePassword.setEchoChar('*');
        tKeystorePassword.setLayoutData(data);

        bEchoKeystorePassword = new Button(gKeystore, SWT.PUSH);
        bEchoKeystorePassword.setImage(XSTUIPlugin.getDefault().getImageRegistry().get("echo_password"));
        bEchoKeystorePassword.setToolTipText(Messages.echoPassword);
        data = new FormData();
        data.top = new FormAttachment(tKeystorePassword, 0, SWT.CENTER);
        data.left = new FormAttachment(tKeystorePassword, IGlobals.MARGIN);
        bEchoKeystorePassword.setLayoutData(data);
    }

    /**
     * Adds all listeners for the current wizard page.
     */
    private void addListeners() {
        bOpen.addListener(SWT.Selection, this);
        bEchoKeystorePassword.addListener(SWT.Selection, this);
        bEchoKeyPassword.addListener(SWT.Selection, this);
        tKeystore.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tKeystorePassword.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tKeyName.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tKeyPassword.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
    }

    /**
     * Determines the (error) message for the missing field.
     */
    private void dialogChanged() {
        if (tKeystore.getText().length() == 0) {
            updateStatus(Messages.selectKeystoreFile, DialogPage.INFORMATION);
            return;
        }
        if (tKeystorePassword.getText().length() == 0) {
            updateStatus(Messages.enterKeystorePassword, DialogPage.INFORMATION);
            return;
        }
        if (tKeyName.getText().length() == 0) {
            updateStatus(Messages.enterKeyName, DialogPage.INFORMATION);
            return;
        }
        if (tKeyPassword.getText().length() == 0) {
            updateStatus(Messages.enterKeyPassword, DialogPage.INFORMATION);
            return;
        }
        if (new File(tKeystore.getText()).exists()) {
            try {
	        	keystore = new Keystore(tKeystore.getText(), tKeystorePassword.getText(), IGlobals.KEYSTORE_TYPE);
	        	keystore.load();
	        	if (!keystore.containsKey(tKeyName.getText())) {
	        	    updateStatus(Messages.verifyKeyName, DialogPage.ERROR);
	                return;
	        	}

	        	if (keystore.getSecretKey(tKeyName.getText(), tKeyPassword.getText().toCharArray()) == null) {
	        	    updateStatus(Messages.verifyKeyPassword, DialogPage.ERROR);
                    return;
	        	}
			} catch (Exception ex) {
			    updateStatus(Messages.verifyAll, DialogPage.ERROR);
                return;
			}
        } else {
        	updateStatus(Messages.keystoreNotFound, DialogPage.ERROR);
            return;
        }

        updateStatus(null, DialogPage.NONE);
    }

    /**
     * Shows a message to the user to complete the fields on this page.
     *
     * @param message The message for the user
     * @param status The status type of the message
     */
    private void updateStatus(final String message, final int status) {
        setMessage(message, status);
        if (message == null && getErrorMessage() == null) {
            setPageComplete(true);
            saveDataToModel();
        } else {
            setPageComplete(false);
        }
    }

    /**
     * Handles the events from this wizard page.
     *
     * @param e The triggered event
     */
    public void handleEvent(final Event e) {
        if (e.widget == bOpen) {
            openKeystore();
        } else if (e.widget == bEchoKeystorePassword) {
            echoPassword(e);
        } else if (e.widget == bEchoKeyPassword) {
            echoPassword(e);
        }
    }

    /**
     * Shows plain text (\0) or cipher text (*) in the password field.
     *
     * @param e The triggered event
     */
    private void echoPassword(final Event e) {
        if (e.widget == bEchoKeystorePassword) {
            if (tKeystorePassword.getEchoChar() == '*') {
                tKeystorePassword.setEchoChar('\0');
            } else {
                tKeystorePassword.setEchoChar('*');
            }
            tKeystorePassword.redraw();
        } else if (e.widget == bEchoKeyPassword) {
            if (tKeyPassword.getEchoChar() == '*') {
                tKeyPassword.setEchoChar('\0');
            } else {
                tKeyPassword.setEchoChar('*');
            }
            tKeyPassword.redraw();
        }
    }

    /**
     * Opens a FileDialog to select the keystore to use in this encrypting session.
     */
    private void openKeystore() {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setFilterNames(IGlobals.KEY_STORE_EXTENSION_NAME);
        dialog.setFilterExtensions(IGlobals.KEY_STORE_EXTENSION);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        String filename = dialog.open();
        if (filename != null && filename.length() > 0) {
            tKeystore.setText(filename);
        }
    }

    /**
     * Returns the next wizard page after all the necessary data is entered correctly.
     *
     * @return IWizardPage The next wizard page
     */
    public IWizardPage getNextPage() {
        saveDataToModel();
        PageAlgorithms page = ((NewEncryptionWizard) getWizard()).getPageAlgorithms();
        page.onEnterPage();
        return page;
    }

    /**
     * Saves the selections on this wizard page to the model. Called on exit of the page.
     */
    private void saveDataToModel() {
        encryption.setKeystore(keystore);
        encryption.setKeystorePassword(tKeystorePassword.getText().toCharArray());
        encryption.setKeyName(tKeyName.getText());
        encryption.setKeyPassword(tKeyPassword.getText().toCharArray());

        storeSettings();
    }

    /**
     * Loads the stored settings for this wizard page.
     */
    private void loadSettings() {
        String previousKeystore = getDialogSettings().get(NewEncryptionWizard.SETTING_KEYSTORE);
        if (previousKeystore == null) {
        	previousKeystore = "";
        }
        tKeystore.setText(previousKeystore);

        String previousKey = getDialogSettings().get(NewEncryptionWizard.SETTING_KEY_NAME);
        if (previousKey == null) {
        	previousKey = "";
        }
        tKeyName.setText(previousKey);
    }

    /**
     * Stores some settings of this wizard page in the current workspace.
     */
    private void storeSettings() {
        IDialogSettings settings = getDialogSettings();
        settings.put(NewEncryptionWizard.SETTING_KEYSTORE, tKeystore.getText());
        settings.put(NewEncryptionWizard.SETTING_KEY_NAME, tKeyName.getText());
    }
}

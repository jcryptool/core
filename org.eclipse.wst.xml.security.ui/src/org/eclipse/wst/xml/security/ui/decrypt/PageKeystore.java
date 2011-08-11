/*******************************************************************************
 * Copyright (c) 2010 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.decrypt;

import java.io.File;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IDialogSettings;
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
import org.eclipse.wst.xml.security.core.decrypt.Decryption;
import org.eclipse.wst.xml.security.core.utils.IGlobals;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.utils.IContextHelpIds;
import org.jcryptool.core.util.directories.DirectoryService;

/**
 * <p>Second page of the <strong>XML Decryption Wizard</strong>. Lets the user
 * select and enter the keystore containing the key used for encryption and the corresponding
 * passwords.</p>
 *
 * <p>After all information is entered the user can finish the Wizard and the XML fragment
 * identified by the given encryption id will be decrypted and restored in plain text.
 * Any wrong entered information that was not already discovered in the wizard will cause
 * decryption to fail.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PageKeystore extends WizardPage implements Listener {
    /** Wizard page name. */
    public static final String PAGE_NAME = "DecryptPageKeystore"; //$NON-NLS-1$
    /** Open Keystore button. */
    private Button bOpen = null;
    /** Button <i>Echo Key Password</i>. */
    private Button bEchoKeyPassword = null;
    /** Button <i>Echo Keystore Password</i>. */
    private Button bEchoKeystorePassword = null;
    /** Keystore file. */
    private Text tKeystore = null;
    /** Keystore password. */
    private Text tKeystorePassword = null;
    /** Key name. */
    private Text tKeyName = null;
    /** Key password. */
    private Text tKeyPassword = null;
    /** Model for the XML Decryption Wizard. */
    private Decryption decryption = null;
    /** Default label width. */
    private static final int LABELWIDTH = 120;
    /** The keystore containing all required key information. */
    private Keystore keystore = null;

    public PageKeystore(final Decryption decryption) {
        super(PAGE_NAME);
        setTitle(Messages.decryptionTitle);
        setDescription(Messages.PageKeystore_keystoreDescription);

        this.decryption = decryption;
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
     * Dialog to select the Keystore to use in this decryption operation.
     */
    private void openKeystore() {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setFilterNames(IGlobals.KEY_STORE_EXTENSION_NAME);
        dialog.setFilterExtensions(IGlobals.KEY_STORE_EXTENSION);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());

        String filename = dialog.open();

        if (filename != null && !filename.equals("")) { //$NON-NLS-1$
            tKeystore.setText(filename);
        }
    }

    /**
     * Creates the wizard page with the layout settings.
     *
     * @param parent Parent composite
     */
    public void createControl(final Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        FormLayout formLayout = new FormLayout();
        container.setLayout(formLayout);

        createPageContent(container);
        addListeners();
        setControl(container);
        loadSettings();

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IContextHelpIds.WIZARD_DECRYPTION_KEYSTORE);
    }

    /**
     * Fills this wizard page with content. Two groups (<i>Keystore</i> and <i>Key</i>)
     * and all their widgets are inserted.
     *
     * @param parent Parent composite
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

        // Elements for group "Keystore"
        Label lKeystoreName = new Label(gKeystore, SWT.SHADOW_IN);
        lKeystoreName.setText(Messages.name);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(gKeystore);
        data.left = new FormAttachment(gKeystore);
        lKeystoreName.setLayoutData(data);

        tKeystore = new Text(gKeystore, SWT.SINGLE);
        data = new FormData();
        data.width = IGlobals.SHORT_TEXT_WIDTH;
        data.top = new FormAttachment(lKeystoreName, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeystoreName);
        tKeystore.setLayoutData(data);

        bOpen = new Button(gKeystore, SWT.PUSH);
        bOpen.setText(Messages.selectButton);
        data = new FormData();
        data.top = new FormAttachment(tKeystore, 0, SWT.CENTER);
        data.left = new FormAttachment(tKeystore, IGlobals.MARGIN);
        bOpen.setLayoutData(data);

        Label lKeystorePassword = new Label(gKeystore, SWT.SHADOW_IN);
        lKeystorePassword.setText(Messages.password);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lKeystoreName, IGlobals.MARGIN);
        data.left = new FormAttachment(gKeystore);
        lKeystorePassword.setLayoutData(data);

        tKeystorePassword = new Text(gKeystore, SWT.SINGLE);
        tKeystorePassword.setTextLimit(IGlobals.KEYSTORE_PASSWORD_MAX_SIZE);
        data = new FormData();
        data.width = IGlobals.SHORT_TEXT_WIDTH;
        data.top = new FormAttachment(lKeystorePassword, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeystorePassword);
        tKeystorePassword.setEchoChar('*');
        tKeystorePassword.setLayoutData(data);

        bEchoKeystorePassword = new Button(gKeystore, SWT.PUSH);
        bEchoKeystorePassword.setImage(XSTUIPlugin.getDefault().getImageRegistry().get("echo_password")); //$NON-NLS-1$
        bEchoKeystorePassword.setToolTipText(Messages.echoPassword);
        data = new FormData();
        data.top = new FormAttachment(tKeystorePassword, 0, SWT.CENTER);
        data.left = new FormAttachment(tKeystorePassword, IGlobals.MARGIN);
        bEchoKeystorePassword.setLayoutData(data);

        // Elements for group "Key"
        Label lKeyName = new Label(gKey, SWT.SHADOW_IN);
        lKeyName.setText(Messages.name);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(gKey);
        data.left = new FormAttachment(gKey);
        lKeyName.setLayoutData(data);

        tKeyName = new Text(gKey, SWT.SINGLE);
        tKeyName.setTextLimit(IGlobals.KEY_NAME_MAX_SIZE);
        data = new FormData();
        data.width = IGlobals.SHORT_TEXT_WIDTH;
        data.top = new FormAttachment(lKeyName, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyName);
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
        bEchoKeyPassword.setImage(XSTUIPlugin.getDefault().getImageRegistry().get("echo_password")); //$NON-NLS-1$
        bEchoKeyPassword.setToolTipText(Messages.echoPassword);
        data = new FormData();
        data.top = new FormAttachment(tKeyPassword, 0, SWT.CENTER);
        data.left = new FormAttachment(tKeyPassword, IGlobals.MARGIN);
        bEchoKeyPassword.setLayoutData(data);
    }

    private void addListeners() {
        bOpen.addListener(SWT.Selection, this);
        bEchoKeyPassword.addListener(SWT.Selection, this);
        bEchoKeystorePassword.addListener(SWT.Selection, this);
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
            updateStatus(Messages.missingKeystore, DialogPage.INFORMATION);
            return;
        } else if (!(new File(tKeystore.getText()).exists())) {
            updateStatus(Messages.keystoreNotFound, DialogPage.ERROR);
            return;
        }

        if (tKeystorePassword.getText().length() == 0) {
            updateStatus(Messages.missingKeystorePassword, DialogPage.INFORMATION);
            return;
        }

        if (tKeyName.getText().length() == 0) {
            updateStatus(Messages.missingKeyName, DialogPage.INFORMATION);
            return;
        }

        if (tKeyPassword.getText().length() == 0) {
            updateStatus(Messages.missingKeyPassword, DialogPage.INFORMATION);
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
     * Called when finishing the wizard.
     *
     * @return Finishing status
     */
    public boolean performFinish() {
        return saveDataToModel();
    }

    /**
     * Saves the selections on this wizard page to the model. Called on exit of the page.
     *
     * @return Saving state
     */
    private boolean saveDataToModel() {
//        decryption.setFile(file.getLocation().toString());
        decryption.setKeystore(keystore);
        decryption.setKeystorePassword(tKeystorePassword.getText().toCharArray());
        decryption.setKeyName(tKeyName.getText());
        decryption.setKeyPassword(tKeyPassword.getText().toCharArray());

        storeSettings();

        return true;
    }

    /**
     * Stores some settings of this wizard page in the current workspace.
     */
    private void storeSettings() {
        IDialogSettings settings = getDialogSettings();
        settings.put(NewDecryptionWizard.SETTING_KEYSTORE, tKeystore.getText());
        settings.put(NewDecryptionWizard.SETTING_KEY_NAME, tKeyName.getText());
    }

    /**
     * Loads the stored settings for this wizard page.
     */
    private void loadSettings() {
        String previousKeystore = getDialogSettings().get(NewDecryptionWizard.SETTING_KEYSTORE);
        if (previousKeystore == null) {
            previousKeystore = ""; //$NON-NLS-1$
        }
        tKeystore.setText(previousKeystore);

        String previousKey = getDialogSettings().get(NewDecryptionWizard.SETTING_KEY_NAME);
        if (previousKey == null) {
            previousKey = ""; //$NON-NLS-1$
        }
        tKeyName.setText(previousKey);
    }

}

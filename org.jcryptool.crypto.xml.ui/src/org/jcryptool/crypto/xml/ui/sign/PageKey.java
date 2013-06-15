/*******************************************************************************
 * Copyright (c) 2013 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.jcryptool.crypto.xml.ui.sign;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.xml.core.sign.Signature;
import org.jcryptool.crypto.xml.core.utils.IGlobals;
import org.jcryptool.crypto.xml.ui.XSTUIPlugin;
import org.jcryptool.crypto.xml.ui.utils.IContextHelpIds;

/**
 * <p>
 * Second page of the XML Signature Wizard. Lets the user select an existing <i>key</i> and enter the corresponding
 * information.
 * </p>
 * 
 * @author Dominik Schadow
 * @version 1.0.0
 */
public class PageKey extends WizardPage implements Listener {
    /** Wizard page name. */
    public static final String PAGE_NAME = "SignPageKey"; //$NON-NLS-1$
    /** Button <i>Echo Key Password</i>. */
    private Button bEchoKeyPassword = null;
    /** Key password text. */
    private Text keyPassword = null;
    /** Key alias combo. */
    private Combo keyAlias = null;
    /** Default label width. */
    private static final int LABELWIDTH = 120;
    /** Stored setting for the key alias. */
    private static final String SETTING_KEY_ALIAS = "sign_key_alias";
    /** Model for the XML Signature Wizard. */
    private Signature signature = null;
    private ArrayList<IKeyStoreAlias> privateKeys = new ArrayList<IKeyStoreAlias>();

    /**
     * Constructor for PageOpenKey.
     * 
     * @param signature The signature wizard model
     */
    public PageKey(final Signature signature) {
        super(PAGE_NAME);
        setTitle(Messages.signatureTitle);
        setDescription(Messages.useKeyDescription);

        this.signature = signature;
    }

    /**
     * Creates the wizard page with the layout settings.
     * 
     * @param parent The parent composite
     */
    public void createControl(final Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        FormLayout formLayout = new FormLayout();
        container.setLayout(formLayout);

        createPageContent(container);
        addListeners();
        setControl(container);
        loadKeys();
        loadSettings();
        setPageComplete(false);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IContextHelpIds.WIZARD_SIGNATURE_KEY);
    }

    private void loadKeys() {
        privateKeys = KeyStoreManager.getInstance().getAllPrivateKeys();
        ArrayList<String> items = new ArrayList<String>();
        
        for (IKeyStoreAlias item : privateKeys) {
            items.add("\"" + item.getContactName() + "\" - " + item.getOperation() + " - " + item.getKeyLength() + " bit");
        }
        
        keyAlias.setItems(items.toArray(new String[privateKeys.size()]));
    }

    /**
     * Fills this wizard page with content. One group (<i>Key</i>) and all its widgets is inserted.
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

        // One group
        Group gKey = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gKey.setLayout(layout);
        gKey.setText(Messages.key);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
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

        keyAlias = new Combo(gKey, SWT.DROP_DOWN | SWT.READ_ONLY);
        data = new FormData();
        data.top = new FormAttachment(lKeyName, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyName);
        data.width = IGlobals.SHORT_TEXT_WIDTH;
        keyAlias.setLayoutData(data);

        Label lKeyPassword = new Label(gKey, SWT.SHADOW_IN);
        lKeyPassword.setText(Messages.password);
        data = new FormData();
        data.top = new FormAttachment(lKeyName, IGlobals.MARGIN);
        data.left = new FormAttachment(gKey);
        data.width = LABELWIDTH;
        lKeyPassword.setLayoutData(data);

        keyPassword = new Text(gKey, SWT.SINGLE | SWT.PASSWORD);
        keyPassword.setTextLimit(IGlobals.KEY_PASSWORD_MAX_SIZE);
        data = new FormData();
        data.top = new FormAttachment(lKeyPassword, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyPassword);
        data.width = IGlobals.SHORT_TEXT_WIDTH;
        keyPassword.setEchoChar('*');
        keyPassword.setLayoutData(data);

        bEchoKeyPassword = new Button(gKey, SWT.PUSH);
        bEchoKeyPassword.setImage(XSTUIPlugin.getDefault().getImageRegistry().get("echo_password"));
        data = new FormData();
        data.top = new FormAttachment(keyPassword, 0, SWT.CENTER);
        data.left = new FormAttachment(keyPassword, IGlobals.MARGIN);
        bEchoKeyPassword.setLayoutData(data);
    }

    /**
     * Adds all listeners for the current wizard page.
     */
    private void addListeners() {
        bEchoKeyPassword.addListener(SWT.Selection, this);
        keyPassword.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        keyAlias.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
    }

    /**
     * Determines the (error) message for the missing field.
     */
    private void dialogChanged() {
        if (keyAlias.getText().length() == 0) {
            updateStatus(Messages.selectKey, IMessageProvider.INFORMATION);
            return;
        }
        if (keyPassword.getText().length() == 0) {
            updateStatus(Messages.enterKeyPassword, IMessageProvider.INFORMATION);
            return;
        }

        updateStatus(null, IMessageProvider.NONE);
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
        if (e.widget == bEchoKeyPassword) {
            echoPassword(e);
        }
    }

    /**
     * Shows plain text (\0) or cipher text (*) in the password field.
     * 
     * @param e The triggered event
     */
    private void echoPassword(final Event e) {
        if (e.widget == bEchoKeyPassword) {
            if (keyPassword.getEchoChar() == '*') {
                keyPassword.setEchoChar('\0');
            } else {
                keyPassword.setEchoChar('*');
            }
            keyPassword.redraw();
        }
    }

    /**
     * Returns the next wizard page after all the necessary data is entered correctly.
     * 
     * @return IWizardPage The next wizard page
     */
    public IWizardPage getNextPage() {
        saveDataToModel();
        PageAlgorithms page = ((NewSignatureWizard) getWizard()).getPageAlgorithms();
        page.onEnterPage();
        return page;
    }

    /**
     * Saves the selections on this wizard page to the model. Called on exit of the page.
     */
    private void saveDataToModel() {
        signature.setKeyAlias(privateKeys.get(keyAlias.getSelectionIndex()));
        signature.setKeyPassword(keyPassword.getTextChars());
    }

    /**
     * Loads the stored settings for this wizard page.
     */
    private void loadSettings() {
        keyAlias.setText(getDialogSettings().get(SETTING_KEY_ALIAS) != null ? getDialogSettings().get(SETTING_KEY_ALIAS)
                : "");
    }

    /**
     * Stores some settings of this wizard page in the current workspace.
     */
    protected void storeSettings() {
        IDialogSettings settings = getDialogSettings();
        settings.put(SETTING_KEY_ALIAS, keyAlias.getText());
    }
}

/*******************************************************************************
 * Copyright (c) 2010 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.decrypt;

import java.io.InputStream;

import org.eclipse.jface.dialogs.DialogPage;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.xml.security.core.decrypt.Decryption;
import org.eclipse.wst.xml.security.core.utils.IGlobals;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.eclipse.wst.xml.security.ui.utils.IContextHelpIds;

/**
 * <p>
 * First page of the <strong>XML Decryption Wizard</strong>. Lets the user select the encryption id for the encrypted
 * document part to decrypt and determine the used encryption type (enveloping or detached). A detached encryption
 * requires the detached file to be identified.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PageResource extends WizardPage implements Listener {
    /** Wizard page name. */
    public static final String PAGE_NAME = "DecryptPageResource"; //$NON-NLS-1$
    /** Combo box <i>Encryption ID</i>. */
    private Combo cEncryptionId = null;
    /** Radio to decrypt a detached encryption. */
    private Button bDetached = null;
    /** Radio to decrypt an enveloping encryption. */
    private Button bEnveloping = null;
    /** The XML document. */
    private InputStream data;
    /** Model for the XML Decryption Wizard. */
    private Decryption decryption = null;

    /**
     * Constructor for the algorithms page with three parameters.
     *
     * @param decryption The decryption wizard model
     * @param data The selected file
     */
    public PageResource(final Decryption decryption, final InputStream data) {
        super(PAGE_NAME);
        setTitle(Messages.decryptionTitle);
        setDescription(Messages.resourceDescription);

        this.decryption = decryption;
        this.data = data;
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
        determineIds();
        addListeners();
        setControl(container);
        loadSettings();

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IContextHelpIds.WIZARD_DECRYPTION_RESOURCE);
    }

    /**
     * Fills this wizard page with content. Two groups (<i>Type</i> and <i>Encryption ID</i>) and all their widgets are
     * inserted.
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
        Group gEncryptionId = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gEncryptionId.setLayout(layout);
        gEncryptionId.setText(Messages.encryptionId);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gEncryptionId.setLayoutData(data);

        Group gType = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gType.setLayout(layout);
        gType.setText(Messages.encryptionType);
        data = new FormData();
        data.top = new FormAttachment(gEncryptionId, IGlobals.MARGIN, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gType.setLayoutData(data);

        // Elements for group "Encryption ID"
        cEncryptionId = new Combo(gEncryptionId, SWT.READ_ONLY);
        data = new FormData();
        data.top = new FormAttachment(gEncryptionId);
        data.left = new FormAttachment(gEncryptionId);
        data.width = IGlobals.COMBO_LARGE_WIDTH;
        cEncryptionId.setLayoutData(data);

        // Elements for group "Encryption Type"
        bEnveloping = new Button(gType, SWT.RADIO);
        bEnveloping.setText(Messages.encryptionEnveloping);
        bEnveloping.setSelection(true);
        data = new FormData();
        data.top = new FormAttachment(gType);
        data.left = new FormAttachment(gType);
        bEnveloping.setLayoutData(data);

        bDetached = new Button(gType, SWT.RADIO);
        bDetached.setText(Messages.encryptionDetached);
        data = new FormData();
        data.top = new FormAttachment(bEnveloping, IGlobals.MARGIN);
        data.left = new FormAttachment(gType);
        bDetached.setLayoutData(data);
    }

    /**
     * Determines all available encryption IDs in the XML document.<br>
     * The entry <i>Use first encryption ID</i> is always available and selected per default.
     */
    private void determineIds() {
        String[] ids = null;

        try {
            ids = Utils.getIds(data, "encryption");
        } catch (Exception ex) {
            ids = new String[] {};
        }

        cEncryptionId.setItems(ids);
        cEncryptionId.select(0);
    }

    /**
     * Adds all listeners for the current wizard page.
     */
    private void addListeners() {
        cEncryptionId.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
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
     * Determines the (error) message for the missing field.
     */
    private void dialogChanged() {
        if ("".equals(cEncryptionId.getText())) {
            updateStatus(Messages.missingEncryptionId, DialogPage.INFORMATION);
            return;
        }

        updateStatus(null, DialogPage.NONE);
    }

    /**
     * Handles the events from this wizard page.
     *
     * @param e The triggered event
     */
    public void handleEvent(final Event e) {
        // TODO changing ID requires a new search for the detached file
    }

    /**
     * Sets the completed field on the wizard class when all the data is entered and the wizard can be finished.
     *
     * @return Page is completed or not
     */
    public boolean isPageComplete() {
        saveDataToModel();
        if (getMessage() == null && getErrorMessage() == null) {
            return true;
        }
        return false;
    }

    /**
     * Saves the selections on this wizard page to the model. Called on exit of the page.
     */
    private void saveDataToModel() {
        decryption.setEncryptionId(cEncryptionId.getText());

        if (bEnveloping.getSelection()) {
            decryption.setEncryptionType("enveloping"); //$NON-NLS-1$
        } else if (bDetached.getSelection()) {
            decryption.setEncryptionType("detached"); //$NON-NLS-1$
            // TODO set file path and filename
            decryption.setDetachedFile("");
        }

        storeSettings();
    }

    /**
     * Loads the stored settings for this wizard page.
     */
    private void loadSettings() {
    }

    /**
     * Stores some settings of this wizard page in the current workspace.
     */
    private void storeSettings() {
    }
}

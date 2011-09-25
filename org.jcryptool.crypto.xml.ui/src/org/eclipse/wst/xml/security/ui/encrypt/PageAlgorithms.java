/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.encrypt;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
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
import org.eclipse.wst.xml.security.core.encrypt.Encryption;
import org.eclipse.wst.xml.security.core.utils.IAlgorithms;
import org.eclipse.wst.xml.security.core.utils.IGlobals;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.eclipse.wst.xml.security.ui.utils.IContextHelpIds;

/**
 * <p>
 * Second and last page of the XML Encryption Wizard. On this page the user has to select three algorithms, a
 * corresponding key size and a Java Keystore to store the generated key in. Optional (but recommended) is an encryption
 * id.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PageAlgorithms extends WizardPage implements Listener {
    /** Wizard page name. */
    public static final String PAGE_NAME = "EncryptPageAlgorithms"; //$NON-NLS-1$
    /** Combo box <i>Encryption Algorithm</i>. */
    private Combo cEncryptionAlgorithm = null;
    /** Combo box <i>Key Wrap Algorithm</i>. */
    private Combo cKeyWrapAlgorithm = null;
    /** Textfield <i>Encryption ID</i>. */
    private Text tID = null;
    /** Start XML Signature Wizard afterwards checkbox. */
    private Button bSigWiz = null;
    /** Checkbox to encrypt only element content. */
    private Button bContentOnly = null;
    /** BSP activation flag. */
    private boolean bsp;
    /** All encryption IDs in the current XML document. */
    private String[] ids;
    /** Model for the XML Encryption Wizard. */
    private Encryption encryption = null;

    /**
     * Constructor for the algorithms page of the wizard.
     *
     * @param encryption The encryption wizard model
     */
    public PageAlgorithms(final Encryption encryption) {
        super(PAGE_NAME);
        setTitle(Messages.encryptionTitle);
        setDescription(Messages.algorithmsDescription);

        this.encryption = encryption;

        determineIds();
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

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IContextHelpIds.WIZARD_ENCRYPTION_ALGORITHMS);
    }

    /**
     * Fills this wizard page with content. Four groups (<i>Algorithms</i>, <i>Key File</i>, <i>Encryption ID</i> and
     * <i>XML Signature Wizard</i>) and all their widgets are inserted.
     *
     * @param parent Parent composite
     */
    private void createPageContent(final Composite parent) {
        FormLayout layout = new FormLayout();
        layout.marginTop = IGlobals.MARGIN;
        layout.marginBottom = IGlobals.MARGIN;
        layout.marginLeft = IGlobals.MARGIN;
        layout.marginRight = IGlobals.MARGIN;
        parent.setLayout(layout);

        // Four groups
        Group gEncrypt = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gEncrypt.setLayout(layout);
        gEncrypt.setText(Messages.algorithms);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gEncrypt.setLayoutData(data);

        Group gContent = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gContent.setLayout(layout);
        gContent.setText(Messages.properties);
        data = new FormData();
        data.top = new FormAttachment(gEncrypt, IGlobals.MARGIN, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gContent.setLayoutData(data);

        Group gID = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gID.setLayout(layout);
        gID.setText(Messages.encryptionId);
        data = new FormData();
        data.top = new FormAttachment(gContent, IGlobals.MARGIN, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gID.setLayoutData(data);

        Group gSigWiz = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gSigWiz.setLayout(layout);
        gSigWiz.setText(Messages.signatureWizard);
        data = new FormData();
        data.top = new FormAttachment(gID, IGlobals.MARGIN * 2, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gSigWiz.setLayoutData(data);

        // Elements for group "Encryption and Key Wrap Algorithm"
        cEncryptionAlgorithm = new Combo(gEncrypt, SWT.READ_ONLY);
        data = new FormData();
        data.top = new FormAttachment(gEncrypt);
        data.left = new FormAttachment(gEncrypt);
        data.width = IGlobals.COMBO_LARGE_WIDTH;
        cEncryptionAlgorithm.setLayoutData(data);

        cKeyWrapAlgorithm = new Combo(gEncrypt, SWT.READ_ONLY);
        data = new FormData();
        data.top = new FormAttachment(gEncrypt);
        data.left = new FormAttachment(cEncryptionAlgorithm, IGlobals.COMBO_MARGIN);
        data.width = IGlobals.COMBO_LARGE_WIDTH;
        cKeyWrapAlgorithm.setLayoutData(data);

        // Elements for group "Root Element"
        bContentOnly = new Button(gContent, SWT.CHECK);
        bContentOnly.setText(Messages.keepRootElementPlain);
        data = new FormData();
        data.top = new FormAttachment(gContent);
        data.left = new FormAttachment(gContent);
        bContentOnly.setLayoutData(data);

        // Elements for group "Encryption ID"
        Label lID = new Label(gID, SWT.SHADOW_IN);
        lID.setText(Messages.encryptionId);
        data = new FormData();
        data.top = new FormAttachment(gID);
        data.left = new FormAttachment(gID);
        data.width = 90;
        lID.setLayoutData(data);

        tID = new Text(gID, SWT.SINGLE);
        tID.setTextLimit(IGlobals.ID_LIMIT);
        data = new FormData();
        data.top = new FormAttachment(gID);
        data.left = new FormAttachment(lID);
        data.width = IGlobals.MEDIUM_TEXT_WIDTH;
        tID.setLayoutData(data);

        // Elements for group "XML Signature Wizard"
        bSigWiz = new Button(gSigWiz, SWT.CHECK);
        bSigWiz.setText(Messages.startSignatureWizard);
        data = new FormData();
        data.top = new FormAttachment(gSigWiz);
        data.left = new FormAttachment(gSigWiz);
        bSigWiz.setLayoutData(data);
    }

    /**
     * Adds all listeners for the current wizard page.
     */
    private void addListeners() {
        cEncryptionAlgorithm.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        cKeyWrapAlgorithm.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tID.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {
                if (e.keyCode == 8 || e.keyCode == 127) {
                    e.doit = true;
                } else if (!Utils.validateId(e.text)) {
                    e.doit = false;
                }
            }
        });
        tID.addModifyListener(new ModifyListener() {
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
        if (cEncryptionAlgorithm.getText().equals("")) {
            updateStatus(Messages.selectEncryptionAlgorithm, DialogPage.INFORMATION);
            return;
        }
        if (cKeyWrapAlgorithm.getText().equals("")) {
            updateStatus(Messages.selectKeyWrapAlgorithm, DialogPage.INFORMATION);
            return;
        }
        if (!tID.getText().equals("")) {
            if (ids != null && ids.length > 0) {
                boolean uniqueId = Utils.ensureIdIsUnique(tID.getText(), ids);

                if (!uniqueId) {
                    updateStatus(Messages.ambiguousEncryptionId, DialogPage.ERROR);
                    return;
                }
            }
        }

        updateStatus(null, DialogPage.NONE);
    }

    /**
     * Handles the events from this wizard page.
     *
     * @param e The triggered event
     */
    public void handleEvent(final Event e) {
    }

    /**
     * Determines all available encryption ids in this XML document.
     */
    private void determineIds() {
        ids = encryption.getIds();
    }

    /**
     * Sets the completed field on the wizard class when all the data is entered and the wizard can be completed.
     *
     * @return Whether page is completed or not
     */
    public boolean isPageComplete() {
        saveDataToModel();
        if (getMessage() == null && getErrorMessage() == null) {
            return true;
        }
        return false;
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
        encryption.setEncryptionAlgorithm(cEncryptionAlgorithm.getText());
        encryption.setKeyWrapAlgorithm(cKeyWrapAlgorithm.getText());
        encryption.setContent(bContentOnly.getSelection());
        encryption.setEncryptionId(tID.getText());
        encryption.setLaunchSignatureWizard(bSigWiz.getSelection());

        storeSettings();

        return true;
    }

    /**
     * Called on enter of the page to fill the combo boxes based on the Basic Security Profile selection on the first
     * wizard page.
     */
    public void onEnterPage() {
        bsp = encryption.getBsp();

        if (bsp) { // BSP selected
            cEncryptionAlgorithm.setItems(IAlgorithms.ENCRYPTION_ALGORITHMS_BSP);
            cEncryptionAlgorithm.setText(IAlgorithms.ENCRYPTION_ALGORITHMS_BSP[0]);
            cKeyWrapAlgorithm.setItems(IAlgorithms.KEY_WRAP_ALGORITHMS_BSP);
            cKeyWrapAlgorithm.setText(IAlgorithms.KEY_WRAP_ALGORITHMS_BSP[0]);
        } else { // BSP not selected
            cEncryptionAlgorithm.setItems(IAlgorithms.ENCRYPTION_ALGORITHMS);
            cEncryptionAlgorithm.setText(IAlgorithms.ENCRYPTION_ALGORITHMS[0]);
            cKeyWrapAlgorithm.setItems(IAlgorithms.KEY_WRAP_ALGORITHMS);
            cKeyWrapAlgorithm.setText(IAlgorithms.KEY_WRAP_ALGORITHMS[0]);
        }

        setMessage(null);
    }

    /**
     * Loads the stored settings for this wizard page.
     */
    private void loadSettings() {
        String callSignatureWizard = getDialogSettings().get(NewEncryptionWizard.SETTING_CALL_SIGNATURE_WIZARD);
        boolean doCallSignatureWizard = false;
        if (callSignatureWizard != null) {
            doCallSignatureWizard = getDialogSettings().getBoolean(NewEncryptionWizard.SETTING_CALL_SIGNATURE_WIZARD);
        }
        String plainRootElement = getDialogSettings().get(NewEncryptionWizard.SETTING_SET_PLAIN_ROOT_ELEMENT);
        boolean doPlainRootElement = false;
        if (plainRootElement != null) {
            doPlainRootElement = getDialogSettings().getBoolean(NewEncryptionWizard.SETTING_SET_PLAIN_ROOT_ELEMENT);
        }
        bContentOnly.setSelection(doPlainRootElement);
        bSigWiz.setSelection(doCallSignatureWizard);
    }

    /**
     * Stores some settings of this wizard page in the current workspace.
     */
    private void storeSettings() {
        IDialogSettings settings = getDialogSettings();
        settings.put(NewEncryptionWizard.SETTING_SET_PLAIN_ROOT_ELEMENT, bContentOnly.getSelection());
        settings.put(NewEncryptionWizard.SETTING_CALL_SIGNATURE_WIZARD, bSigWiz.getSelection());
    }
}

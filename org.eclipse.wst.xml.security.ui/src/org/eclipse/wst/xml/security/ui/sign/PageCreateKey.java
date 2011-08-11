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
package org.eclipse.wst.xml.security.ui.sign;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.HashMap;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.xml.security.core.cryptography.Keystore;
import org.eclipse.wst.xml.security.core.sign.Signature;
import org.eclipse.wst.xml.security.core.utils.IAlgorithms;
import org.eclipse.wst.xml.security.core.utils.IGlobals;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.utils.IContextHelpIds;
import org.jcryptool.core.util.directories.DirectoryService;

/**
 * <p>Second alternative page of the XML Signature Wizard. Lets the user create a new <i>key</i> in an existing
 * <i>Java Keystore</i>. The created key is automatically used to create this XML signature.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PageCreateKey extends WizardPage implements Listener {
    /** Wizard page name. */
    public static final String PAGE_NAME = "SignPageCreateKey"; //$NON-NLS-1$
    /** Keystore name. */
    private String keystoreName;
    /** Key generation successful. */
    private boolean generated = false;
    /** Open keystore button. */
    private Button bOpen = null;
    /** Generate button. */
    private Button bGenerate = null;
    /** Button <i>Echo Keystore Password</i>. */
    private Button bEchoKeystorePassword = null;
    /** Button <i>Echo Key Password</i>. */
    private Button bEchoKeyPassword = null;
    /** Key algorithm combo. */
    private Combo cKeyAlgorithm = null;
    /** Key preview label. */
    private Label lPreview = null;
    /** Key generation result label. */
    private Label lResult = null;
    /** Common Name text. */
    private Text tCommonName = null;
    /** Organizational Unit text. */
    private Text tOrganizationalUnit = null;
    /** Organization text. */
    private Text tOrganization = null;
    /** Location text. */
    private Text tLocation = null;
    /** State text. */
    private Text tState = null;
    /** Country text. */
    private Text tCountry = null;
    /** Key name text. */
    private Text tKeyName = null;
    /** Keystore text. */
    private Text tKeystore = null;
    /** Keystore password text. */
    private Text tKeystorePassword = null;
    /** Key password text. */
    private Text tKeyPassword = null;
    /** Default label width. */
    private static final int LABELWIDTH = 160;
    /** Default preview textfield height. */
    private static final int TEXTHEIGHT = 40;
    /** Model for the XML Signature Wizard. */
    private Signature signature = null;
    /** The Keystore containing all required key information. */
    private Keystore keystore = null;

    /**
     * Constructor for PageCreateKey.
     *
     * @param signature The signature wizard model
     */
    public PageCreateKey(final Signature signature) {
        super(PAGE_NAME);
        setTitle(Messages.signatureTitle);
        setDescription(Messages.createKeyDescription);

        this.signature = signature;
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
        setPageComplete(false);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IContextHelpIds.WIZARD_SIGNATURE_CREATE_KEY);
    }

    /**
     * Fills this wizard page with content. Three groups (<i>Distinguished Name</i>, <i>Key</i> and <i>Keystore</i>)
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

        // Three groups
        Group gDN = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gDN.setLayout(layout);
        gDN.setText(Messages.certificate);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gDN.setLayoutData(data);

        Group gKey = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gKey.setLayout(layout);
        gKey.setText(Messages.key);
        data = new FormData();
        data.top = new FormAttachment(gDN, IGlobals.MARGIN, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gKey.setLayoutData(data);

        Group gKeystore = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gKeystore.setLayout(layout);
        gKeystore.setText(Messages.keystore);
        data = new FormData();
        data.top = new FormAttachment(gKey, IGlobals.MARGIN, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gKeystore.setLayoutData(data);

        // Elements for group "Certificate"
        Label lCommonName = new Label(gDN, SWT.SHADOW_IN);
        lCommonName.setText(Messages.commonName);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(gDN);
        data.left = new FormAttachment(gDN);
        lCommonName.setLayoutData(data);

        tCommonName = new Text(gDN, SWT.SINGLE);
        tCommonName.setTextLimit(IGlobals.KEY_DATA_LIMIT);
        data = new FormData();
        data.width = IGlobals.MEDIUM_TEXT_WIDTH;
        data.top = new FormAttachment(lCommonName, 0, SWT.CENTER);
        data.left = new FormAttachment(lCommonName);
        tCommonName.setLayoutData(data);

        Label lOrganizationalUnit = new Label(gDN, SWT.SHADOW_IN);
        lOrganizationalUnit.setText(Messages.organizationalUnit);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lCommonName, IGlobals.MARGIN);
        data.left = new FormAttachment(gDN);
        lOrganizationalUnit.setLayoutData(data);

        tOrganizationalUnit = new Text(gDN, SWT.SINGLE);
        tOrganizationalUnit.setTextLimit(IGlobals.KEY_DATA_LIMIT);
        data = new FormData();
        data.width = IGlobals.MEDIUM_TEXT_WIDTH;
        data.top = new FormAttachment(lOrganizationalUnit, 0, SWT.CENTER);
        data.left = new FormAttachment(lOrganizationalUnit);
        tOrganizationalUnit.setLayoutData(data);

        Label lOrganization = new Label(gDN, SWT.SHADOW_IN);
        lOrganization.setText(Messages.organization);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lOrganizationalUnit, IGlobals.MARGIN);
        data.left = new FormAttachment(gDN);
        lOrganization.setLayoutData(data);

        tOrganization = new Text(gDN, SWT.SINGLE);
        tOrganization.setTextLimit(IGlobals.KEY_DATA_LIMIT);
        data = new FormData();
        data.width = IGlobals.MEDIUM_TEXT_WIDTH;
        data.top = new FormAttachment(lOrganization, 0, SWT.CENTER);
        data.left = new FormAttachment(lOrganization);
        tOrganization.setLayoutData(data);

        Label lLocation = new Label(gDN, SWT.SHADOW_IN);
        lLocation.setText(Messages.location);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lOrganization, IGlobals.MARGIN);
        data.left = new FormAttachment(gDN);
        lLocation.setLayoutData(data);

        tLocation = new Text(gDN, SWT.SINGLE);
        tLocation.setTextLimit(IGlobals.KEY_DATA_LIMIT);
        data = new FormData();
        data.width = IGlobals.MEDIUM_TEXT_WIDTH;
        data.top = new FormAttachment(lLocation, 0, SWT.CENTER);
        data.left = new FormAttachment(lLocation);
        tLocation.setLayoutData(data);

        Label lState = new Label(gDN, SWT.SHADOW_IN);
        lState.setText(Messages.state);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lLocation, IGlobals.MARGIN);
        data.left = new FormAttachment(gDN);
        lState.setLayoutData(data);

        tState = new Text(gDN, SWT.SINGLE);
        tState.setTextLimit(IGlobals.KEY_DATA_LIMIT);
        data = new FormData();
        data.width = IGlobals.MEDIUM_TEXT_WIDTH;
        data.top = new FormAttachment(lState, 0, SWT.CENTER);
        data.left = new FormAttachment(lState);
        tState.setLayoutData(data);

        Label lCountry = new Label(gDN, SWT.SHADOW_IN);
        lCountry.setText(Messages.country);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lState, IGlobals.MARGIN);
        data.left = new FormAttachment(gDN);
        lCountry.setLayoutData(data);

        tCountry = new Text(gDN, SWT.SINGLE);
        tCountry.setTextLimit(IGlobals.KEY_DATA_LIMIT);
        data = new FormData();
        data.width = IGlobals.MEDIUM_TEXT_WIDTH;
        data.top = new FormAttachment(lCountry, 0, SWT.CENTER);
        data.left = new FormAttachment(lCountry);
        tCountry.setLayoutData(data);

        lPreview = new Label(gDN, SWT.WRAP);
        data = new FormData();
        data.height = TEXTHEIGHT;
        data.top = new FormAttachment(lCountry, IGlobals.MARGIN);
        data.left = new FormAttachment(gDN);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        lPreview.setLayoutData(data);

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
        tKeyPassword.setTextLimit(IGlobals.KEY_PASSWORD_MAX_SIZE);
        data = new FormData();
        data.width = IGlobals.SHORT_TEXT_WIDTH;
        data.top = new FormAttachment(lKeyPassword, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyPassword);
        tKeyPassword.setEchoChar('*');
        tKeyPassword.setLayoutData(data);

        Label lKeyAlgorithm = new Label(gKey, SWT.SHADOW_IN);
        lKeyAlgorithm.setText(Messages.keyAlgorithm);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lKeyPassword, IGlobals.MARGIN + 4);
        data.left = new FormAttachment(gKey);
        lKeyAlgorithm.setLayoutData(data);

        cKeyAlgorithm = new Combo(gKey, SWT.READ_ONLY);
        data = new FormData();
        data.top = new FormAttachment(lKeyAlgorithm, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyAlgorithm);
        data.width = IGlobals.COMBO_WIDTH;
        cKeyAlgorithm.setLayoutData(data);

        bEchoKeyPassword = new Button(gKey, SWT.PUSH);
        bEchoKeyPassword.setImage(XSTUIPlugin.getDefault().getImageRegistry().get("echo_password"));
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
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lKeystore, IGlobals.MARGIN);
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
        bEchoKeystorePassword.setImage(XSTUIPlugin.getDefault().getImageRegistry().get("echo_password"));
        data = new FormData();
        data.top = new FormAttachment(tKeystorePassword, 0, SWT.CENTER);
        data.left = new FormAttachment(tKeystorePassword, IGlobals.MARGIN);
        bEchoKeystorePassword.setLayoutData(data);

        bGenerate = new Button(gKeystore, SWT.PUSH);
        bGenerate.setText(Messages.generate);
        bGenerate.setEnabled(false);
        data = new FormData();
        data.top = new FormAttachment(lKeystorePassword, IGlobals.MARGIN * 2);
        data.left = new FormAttachment(gKeystore);
        bGenerate.setLayoutData(data);

        lResult = new Label(gKeystore, SWT.WRAP);
        data = new FormData();
        data.height = TEXTHEIGHT;
        data.top = new FormAttachment(bGenerate, IGlobals.MARGIN * 2);
        data.left = new FormAttachment(gKeystore);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        lResult.setLayoutData(data);
    }

    /**
     * Adds all listeners for the current wizard page.
     */
    private void addListeners() {
        bOpen.addListener(SWT.Selection, this);
        bGenerate.addListener(SWT.MouseDown, this);
        bEchoKeystorePassword.addListener(SWT.Selection, this);
        bEchoKeyPassword.addListener(SWT.Selection, this);
        tCommonName.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tOrganizationalUnit.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tOrganization.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tLocation.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tState.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tCountry.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tKeyName.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tKeystorePassword.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tKeyPassword.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        cKeyAlgorithm.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tKeystore.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
    }

    /**
     * Determines the (error) message for the missing field.
     */
    private void dialogChanged() {
        if (tCommonName.getText().length() > 0) {
            lPreview.setText("CN=" + tCommonName.getText()); //$NON-NLS-1$
        } else {
            lPreview.setText(""); //$NON-NLS-1$
            updateStatus(Messages.enterCommonName, DialogPage.INFORMATION);
            return;
        }
        if (tOrganizationalUnit.getText().length() > 0) {
            lPreview.setText("CN=" + tCommonName.getText() //$NON-NLS-1$
                    + ", OU=" + tOrganizationalUnit.getText()); //$NON-NLS-2$
        }
        if (tOrganization.getText().length() > 0) {
            lPreview.setText("CN=" + tCommonName.getText() //$NON-NLS-1$
                    + ", OU=" + tOrganizationalUnit.getText() //$NON-NLS-2$
                    + ", O=" + tOrganization.getText()); //$NON-NLS-1$
        }
        if (tLocation.getText().length() > 0) {
            lPreview.setText("CN=" + tCommonName.getText() //$NON-NLS-1$
                    + ", OU=" + tOrganizationalUnit.getText() //$NON-NLS-2$
                    + ", O=" + tOrganization.getText() //$NON-NLS-1$
                    + ", L=" + tLocation.getText()); //$NON-NLS-2$
        }
        if (tState.getText().length() > 0) {
            lPreview.setText("CN=" + tCommonName.getText() //$NON-NLS-1$
                    + ", OU=" + tOrganizationalUnit.getText() //$NON-NLS-1$
                    + ", O=" + tOrganization.getText() //$NON-NLS-1$
                    + ", L=" + tLocation.getText() //$NON-NLS-1$
                    + ", ST=" + tState.getText()); //$NON-NLS-1$
        }
        if (tCountry.getText().length() > 0) {
            lPreview.setText("CN=" + tCommonName.getText() //$NON-NLS-1$
                    + ", OU=" + tOrganizationalUnit.getText() //$NON-NLS-1$
                    + ", O=" + tOrganization.getText() //$NON-NLS-1$
                    + ", L=" + tLocation.getText() //$NON-NLS-1$
                    + ", ST=" + tState.getText() //$NON-NLS-1$
                    + ", C=" + tCountry.getText()); //$NON-NLS-1$
        }
        if (tKeyName.getText().length() < IGlobals.KEY_NAME_MIN_SIZE) {
            updateStatus(Messages.enterNewKeyName, DialogPage.INFORMATION);
            return;
        }
        if (tKeyPassword.getText().length() < IGlobals.KEY_PASSWORD_MIN_SIZE) {
            updateStatus(Messages.enterNewKeyPassword, DialogPage.INFORMATION);
            return;
        }
        if (cKeyAlgorithm.getSelectionIndex() < 0) {
            updateStatus(Messages.selectKeyAlgorithm, DialogPage.INFORMATION);
            return;
        }
        if (tKeystore.getText().length() == 0) {
            updateStatus(Messages.selectKeystoreForInsert, DialogPage.INFORMATION);
            return;
        }
        if (tKeystorePassword.getText().length() == 0) {
            updateStatus(Messages.enterKeystorePassword, DialogPage.INFORMATION);
            return;
        }

        if (tKeystore.getText().length() > 0 && tKeystorePassword.getText().length() > 0
                && tKeyName.getText().length() > 0) {
            String file = tKeystore.getText();
            keystoreName = file.substring(file.lastIndexOf(System.getProperty("file.separator")) + 1);

            try {
                keystore = new Keystore(file, tKeystorePassword.getText(), IGlobals.KEYSTORE_TYPE);
                boolean loaded = keystore.load();

                if (loaded) {
                    if (keystore.containsKey(tKeyName.getText())) {
                        updateStatus(Messages.keyExistsInKeystore, DialogPage.ERROR);
                        return;
                    }
                } else {
                    updateStatus(Messages.verifyKeystorePassword, DialogPage.ERROR);
                    return;
                }
            } catch (Exception e) {
                updateStatus(Messages.verifyAll, DialogPage.ERROR);
                return;
            }
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
        if (!generated && message == null) {
            bGenerate.setEnabled(true);
        } else {
            bGenerate.setEnabled(false);
        }
        setPageComplete(generated);
    }

    /**
     * Handles the events from this wizard page.
     *
     * @param e The triggered event
     */
    public void handleEvent(final Event e) {
        if (e.widget == bOpen) {
            openKeystore();
        } else if (e.widget == bGenerate) {
            try {
                createKey();
                updateStatus(null, DialogPage.NONE);
            } catch (Exception e1) {
                updateStatus(Messages.keyGenerationFailed, DialogPage.ERROR);
            }
        } else if (e.widget == bEchoKeystorePassword || e.widget == bEchoKeyPassword) {
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
     * Opens a FileDialog to select the .jks Keystore file to use in this signing session.
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
     * Generates the key based on the entered data and displays the user an
     * information message about the generation result.
     *
     * @throws Exception to indicate any exceptional condition
     */
    private void createKey() throws Exception {
        HashMap<String, String> certificateData = new HashMap<String, String>();
        certificateData.put("CN", tCommonName.getText()); //$NON-NLS-1$
        certificateData.put("OU", tOrganizationalUnit.getText()); //$NON-NLS-1$
        certificateData.put("O", tOrganization.getText()); //$NON-NLS-1$
        certificateData.put("L", tLocation.getText()); //$NON-NLS-1$
        certificateData.put("ST", tState.getText()); //$NON-NLS-1$
        certificateData.put("C", tCountry.getText()); //$NON-NLS-1$

        try {
            keystore = new Keystore(tKeystore.getText(), tKeystorePassword.getText(), IGlobals.KEYSTORE_TYPE);
            keystore.load();

            KeyPair kp = keystore.generateKeyPair(cKeyAlgorithm.getText(), 512);
//            X500Principal subjectDN = Principal.generatePrincipal(certificateData);

            Certificate[] certs = new Certificate[1];
//            certs[0] = new XmlSecurityCertificate(kp.getPublic(), subjectDN);

            generated = keystore.insertPrivateKey(tKeyName.getText(), tKeyPassword.getText().toCharArray(), kp.getPrivate(), certs);

            keystore.store();
        } catch (NoSuchAlgorithmException ex) {
            generated = false;

            lResult.setText(Messages.keyGenerationFailed);
        }

        if (generated) {
            lResult.setText(NLS.bind(Messages.keyGenerated, keystoreName));
            updateStatus(null, DialogPage.NONE);
        }
    }

    /**
     * Called on enter of the page to fill the certificate type combo box based on the Basic Security Profile selection
     * on the first wizard page. Preselects a default value in the combo box.
     */
    public void onEnterPage() {
        if (signature.getBsp()) { // BSP selected
            cKeyAlgorithm.setItems(IAlgorithms.SIGNATURE_KEY_ALGORITHMS_BSP);
            cKeyAlgorithm.select(0);
        } else { // BSP not selected
            cKeyAlgorithm.setItems(IAlgorithms.SIGNATURE_KEY_ALGORITHMS);
            cKeyAlgorithm.select(0);
        }
        setMessage(null);
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
        signature.setKeystore(keystore);
        signature.setKeystorePassword(tKeystorePassword.getText().toCharArray());
        signature.setKeyPassword(tKeyPassword.getText().toCharArray());
        signature.setKeyName(tKeyName.getText());
        if (cKeyAlgorithm.getText().equals("DSA")) {
            signature.setKeyAlgorithm("SHA1withDSA");
        } else if (cKeyAlgorithm.getText().equals("EC")) {
            signature.setKeyAlgorithm("SHA1withECDSA");
        } else if (cKeyAlgorithm.getText().equals("RSA")) {
            signature.setKeyAlgorithm("SHA1withRSA");
        }
    }
}

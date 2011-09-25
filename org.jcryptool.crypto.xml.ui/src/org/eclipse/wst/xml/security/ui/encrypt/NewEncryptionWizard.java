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

import java.io.InputStream;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wst.xml.security.core.encrypt.Encryption;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;

/**
 * <p>This class prepares and adds all wizard pages to the wizard and launches the <i>XML Encryption
 * Wizard</i> afterwards.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class NewEncryptionWizard extends Wizard implements INewWizard {
    /** PageResource first wizard page. */
    private PageResource pageResource = null;
    /** PageUseKey second default wizard page. */
    private PageOpenKey pageOpenKey = null;
    /** PageCreateKey second alternative wizard page. */
    private PageCreateKey pageCreateKey = null;
    /** PageCreateKeystore second alternative wizard page. */
    private PageCreateKeystore pageCreateKeystore = null;
    /** PageAlgorithms third wizard page. */
    private PageAlgorithms pageAlgorithms = null;
    /** The XML document to encrypt. */
    private InputStream data;
    /** The text selection in the editor. */
    private ITextSelection textSelection;
    /** The Encryption model. */
    private Encryption encryption;
    /** Stored setting for the encryption Keystore. */
    public static final String SETTING_KEYSTORE = "enc_keystore";
    /** Stored setting for the secret key name. */
    public static final String SETTING_KEY_NAME = "enc_key_name";
    /** Stored setting for the BSP encryption selection. */
    public static final String SETTING_BSP_COMPLIANT_ENCRYPTION = "enc_bsp_compliant";
    /** Stored setting for a plain root element. */
    public static final String SETTING_SET_PLAIN_ROOT_ELEMENT = "enc_plain_root_element";
    /** Stored setting for the Signature Wizard call after encrypting. */
    public static final String SETTING_CALL_SIGNATURE_WIZARD = "enc_sign";

    /**
     * Constructor for the wizard launcher.
     */
    public NewEncryptionWizard() {
        super();
        encryption = new Encryption();
        setWindowTitle(Messages.encryptionWizard);
        setDialogSettings(getEncryptionWizardSettings());
        setNeedsProgressMonitor(true);
    }

    /**
     * Return the settings used for all Encryption Wizard pages.
     *
     * @return The IDialogSettings for the Encryption Wizard
     */
    private IDialogSettings getEncryptionWizardSettings() {
        IDialogSettings workbenchSettings = XSTUIPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("EncryptionWizard"); // $NON-NLS-1$
        if (section == null) {
            section = workbenchSettings.addNewSection("EncryptionWizard"); // $NON-NLS-1$
        }
        return section;
    }

    /**
     * Initializes the wizard with a structured selection.
     *
     * @param workbench The workbench
     * @param selection The structured selection
     */
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
    }

    /**
     * Initializes the wizard with a selected file and a text selection.
     *
     * @param data The selected file
     * @param textSelection The text selection
     */
    public void init(final InputStream data, final ITextSelection textSelection) {
        this.data = data;
        this.textSelection = textSelection;

        if (textSelection != null && !Utils.parseSelection(textSelection.getText())) {
            this.textSelection = null;
        }
    }

    /**
     * Adds the five pages (<code>PageResource</code>,
     * <code>PageOpenKey</code>, <code>PageCreateKey</code>,
     * <code>PageCreateKeystore</code> and <code>PageAlgorithms</code>)
     * to the wizard.
     */
    public void addPages() {
        pageResource = new PageResource(encryption, data, textSelection != null ? true : false);
        addPage(pageResource);
        pageOpenKey = new PageOpenKey(encryption);
        addPage(pageOpenKey);
        pageCreateKey = new PageCreateKey(encryption);
        addPage(pageCreateKey);
        pageCreateKeystore = new PageCreateKeystore(encryption);
        addPage(pageCreateKeystore);
        pageAlgorithms = new PageAlgorithms(encryption);
        addPage(pageAlgorithms);
    }

    /**
     * Checks the currently active wizard page. It is impossible to finish the <i>XML Encryption
     * Wizard</i> from any other than the last page. Only the last wizard page can successfully
     * generate the expected XML encryption.
     *
     * @return Wizard completion status
     */
    public boolean canFinish() {
        if (this.getContainer().getCurrentPage() != pageAlgorithms) {
            return false;
        }
        return pageAlgorithms.isPageComplete();
    }

    /**
     * Finishes the wizard.
     *
     * @return Finishing status
     */
    public boolean performFinish() {
        return pageAlgorithms.performFinish();
    }

    /**
     * Returns the Decryption Wizard model.
     *
     * @return The model
     */
    public Encryption getModel() {
        return encryption;
    }

    /**
     * @return the pageResource
     */
    public PageResource getPageResource() {
        return pageResource;
    }

    /**
     * @return the pageOpenKey
     */
    public PageOpenKey getPageOpenKey() {
        return pageOpenKey;
    }

    /**
     * @return the pageCreateKey
     */
    public PageCreateKey getPageCreateKey() {
        return pageCreateKey;
    }

    /**
     * @return the pageCreateKeystore
     */
    public PageCreateKeystore getPageCreateKeystore() {
        return pageCreateKeystore;
    }

    /**
     * @return the pageAlgorithms
     */
    public PageAlgorithms getPageAlgorithms() {
        return pageAlgorithms;
    }
}

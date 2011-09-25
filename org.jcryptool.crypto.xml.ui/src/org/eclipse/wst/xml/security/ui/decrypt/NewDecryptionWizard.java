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

import java.io.InputStream;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wst.xml.security.core.decrypt.Decryption;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;

/**
 * <p>This class prepares and adds all wizard pages to the wizard and launches the <i>XML Decryption
 * Wizard</i> afterwards.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class NewDecryptionWizard extends Wizard implements INewWizard {
    /** PageResource first wizard page. */
    private PageResource pageResource = null;
    /** PageKeystore second wizard page. */
    private PageKeystore pageKeystore = null;
    /** XML document to decrypt. */
    private InputStream data;
    /** The Decryption model. */
    private Decryption decryption;
    /** Stored setting for the decryption Keystore. */
    public static final String SETTING_KEYSTORE = "dec_keystore";
    /** Stored setting for the secret key name. */
    public static final String SETTING_KEY_NAME = "dec_key_name";

    /**
     * Constructor for the wizard launcher.
     */
    public NewDecryptionWizard() {
        super();
        decryption = new Decryption();
        setWindowTitle(Messages.decryptionWizard);
        setDialogSettings(getDecryptionWizardSettings());
        setNeedsProgressMonitor(true);
    }

    /**
     * Return the settings used for all Decryption Wizard pages.
     *
     * @return The IDialogSettings for the Decryption Wizard
     */
    private IDialogSettings getDecryptionWizardSettings() {
        IDialogSettings workbenchSettings = XSTUIPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("DecryptionWizard"); // $NON-NLS-1$
        if (section == null) {
            section = workbenchSettings.addNewSection("DecryptionWizard"); // $NON-NLS-1$
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
     * Initializes the wizard with a selected file.
     *
     * @param data The selected IFile
     */
    public void init(final InputStream data) {
        this.data = data;
    }

    /**
     * Adds the two pages (<code>PageResource</code> and <code>PageKeystore</code>) to the wizard.
     */
    public void addPages() {
        pageResource = new PageResource(decryption, data);
        addPage(pageResource);

        pageKeystore = new PageKeystore(decryption);
        addPage(pageKeystore);
    }

    /**
     * Checks the currently active wizard page. Only the first wizard page can successfully generate
     * a decryption.
     *
     * @return Wizard completion status
     */
    public boolean canFinish() {
        if (this.getContainer().getCurrentPage() != pageKeystore) {
            return false;
        }

        return pageKeystore.isPageComplete();
    }

    /**
     * Finishes the wizard.
     *
     * @return true
     */
    public boolean performFinish() {
        return pageKeystore.performFinish();
    }

    /**
     * Returns the Encryption Wizard model.
     *
     * @return The model
     */
    public Decryption getModel() {
        return decryption;
    }
}

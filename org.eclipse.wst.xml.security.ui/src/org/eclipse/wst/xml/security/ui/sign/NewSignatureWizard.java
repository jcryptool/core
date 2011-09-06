/*******************************************************************************
 * Copyright (c) 2011 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.sign;

import java.io.InputStream;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wst.xml.security.core.sign.Signature;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;

/**
 * <p>This class prepares and adds all wizard pages to the wizard and launches the
 * <i>XML Signature Wizard</i> afterwards.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class NewSignatureWizard extends Wizard implements INewWizard {
    /** PageResource first wizard page. */
    private PageResource pageResource = null;
    /** PageOpenKey second default wizard page. */
    private PageOpenKey pageOpenKey = null;
    /** PageCreateKey second alternative wizard page. */
    private PageCreateKey pageCreateKey = null;
    /** PageCreateKeystore second alternative wizard page. */
    private PageCreateKeystore pageCreateKeystore = null;
    /** PageAlgorithms third wizard page. */
    private PageAlgorithms pageAlgorithms = null;
    /** The XML document to sign. */
    private InputStream data;
    /** The text selection in the editor. */
    private ITextSelection textSelection;
    /** The Signature model. */
    private Signature signature;

    /**
     * Constructor for the wizard launcher.
     */
    public NewSignatureWizard() {
        super();
        signature = new Signature();
        setWindowTitle(Messages.signatureWizard);
        setDialogSettings(getSignatureWizardSettings());
        setNeedsProgressMonitor(true);
    }

    /**
     * Return the settings used for all XML Signature Wizard pages.
     *
     * @return The IDialogSettings for the XML Signature Wizard
     */
    private IDialogSettings getSignatureWizardSettings() {
        IDialogSettings workbenchSettings = XSTUIPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("SignatureWizard"); // $NON-NLS-1$
        if (section == null) {
            section = workbenchSettings.addNewSection("SignatureWizard"); // $NON-NLS-1$
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
        pageResource = new PageResource(signature, data, textSelection != null ? true : false);
        addPage(pageResource);
        pageOpenKey = new PageOpenKey(signature);
        addPage(pageOpenKey);
        pageCreateKey = new PageCreateKey(signature);
        addPage(pageCreateKey);
        pageCreateKeystore = new PageCreateKeystore(signature);
        addPage(pageCreateKeystore);
        pageAlgorithms = new PageAlgorithms(signature);
        addPage(pageAlgorithms);
    }

    /**
     * Checks the currently active wizard page. It is impossible to finish the
     * <i>XML Signature Wizard</i> from the first or second page. Only the
     * third wizard page can successfully generate a signature.
     *
     * @return Wizard completion status
     */
    public boolean canFinish() {
        if (this.getContainer().getCurrentPage() != pageAlgorithms) {
            return false;
        } else {
            return pageAlgorithms.isPageComplete();
        }
    }

    /**
     * Finishes the wizard.
     *
     * @return Finishing status
     */
    public boolean performFinish() {
        pageResource.storeSettings();
        pageOpenKey.storeSettings();
        pageAlgorithms.storeSettings();
        return pageAlgorithms.performFinish();
    }

    /**
     * Returns the Signature Wizard model.
     *
     * @return The model
     */
    public Signature getModel() {
        return signature;
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

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.wizards;

import java.security.KeyStoreException;
import java.util.Enumeration;
import java.util.HashMap;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.exceptions.NoKeyStoreFileException;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.rsa.RSAData;

import de.flexiprovider.core.rsa.RSAPublicKey;

/**
 * Page for loading a public Key.
 *
 * @author Michael Gaber
 */
public class RSALoadPublicKeyPage extends WizardPage {

    /** the pagename for accessing the wizardpage. */
    private static final String PAGENAME = "Load Public Key Page"; //$NON-NLS-1$

    /** title of this page. */
    private static final String TITLE = Messages.RSALoadPublicKeyPage_select_pubkey;

    /** shared data object. */
    private final RSAData data;

    /** storage for the alias items. */
    private final HashMap<String, KeyStoreAlias> keystoreitems = new HashMap<String, KeyStoreAlias>();

    /** combo for displaying the aliases. */
    private Combo combo;

    /** the selected public alias. */
    protected KeyStoreAlias publicAlias;

    /**
     * Constructor setting the data object and some stuff.
     *
     * @param data the {@link #data}
     */
    public RSALoadPublicKeyPage(RSAData data) {
        super(PAGENAME, TITLE, null);
        this.setDescription(Messages.RSALoadPublicKeyPage_select_pubkey_text);
        this.data = data;
        this.setPageComplete(false);
        this.initKeystoreConnection();
    }

    /**
     * initializes the connection to the keystore.
     */
    private void initKeystoreConnection() {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        try {
            ksm.loadKeyStore(KeyStorePlugin.getPlatformKeyStoreURI());
            KeyStoreAlias alias;
            Enumeration<String> aliases = ksm.getAliases();
            while (aliases != null && aliases.hasMoreElements()) {
                alias = new KeyStoreAlias(aliases.nextElement());
                if (alias.getClassName().equals(RSAPublicKey.class.getName())) {
                    keystoreitems.put(alias.getContactName() + " - " + alias.getKeyLength() + "Bit - " //$NON-NLS-1$ //$NON-NLS-2$
                            + alias.getClassName(), alias);
                }
            }
        } catch (NoKeyStoreFileException e) {
            LogUtil.logError(e);
        } catch (KeyStoreException e) {
            LogUtil.logError(e);
        }
    }

    /**
     * Set up the UI stuff.
     *
     * @param parent the parent composite.
     */
    public final void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        // do stuff like layout et al
        GridLayout gl = new GridLayout();
        gl.marginWidth = 50;
        composite.setLayout(gl);
        new Label(composite, SWT.NONE).setText(Messages.RSALoadPublicKeyPage_select_pubkey_text2);
        combo = new Combo(composite, SWT.READ_ONLY);
        GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        combo.setLayoutData(gd);
        combo.setItems(keystoreitems.keySet().toArray(new String[keystoreitems.size()]));
        combo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                boolean complete = !combo.getText().equals(""); //$NON-NLS-1$
                if (complete) {
                    publicAlias = keystoreitems.get(combo.getText());
                    data.setPublicAlias(publicAlias);
                    data.setContactName(publicAlias.getContactName());
                }
                setPageComplete(complete);
            }
        });
        setControl(composite);
    }

    /**
     * getter for the pagename.
     *
     * @return the pagename
     */
    public static String getPagename() {
        return PAGENAME;
    }

    @Override
    public final IWizardPage getNextPage() {
        return null;
    }
}

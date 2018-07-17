// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.elGamal.ui.wizards.wizardpages;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;

import de.flexiprovider.core.elgamal.ElGamalPublicKey;

/**
 * Page for loading a public Key.
 * 
 * @author Michael Gaber
 * @author Thorben Groos
 */
public class LoadPublicKeyPage extends WizardPage {

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
    public LoadPublicKeyPage(final ElGamalData data) {
        super("Load Public Key Page", Messages.LoadPublicKeyPage_select_pubkey, null);
        setDescription(Messages.LoadPublicKeyPage_select_pubkey_text);
        setPageComplete(false);
        initKeystoreConnection();
    }

    /**
     * initializes the connection to the keystore.
     */
    private void initKeystoreConnection() {
        final KeyStoreManager ksm = KeyStoreManager.getInstance();
        KeyStoreAlias alias;
        final Enumeration<String> aliases = ksm.getAliases();
        while (aliases != null && aliases.hasMoreElements()) {
            alias = new KeyStoreAlias(aliases.nextElement());
            if (alias.getClassName().equals(ElGamalPublicKey.class.getName())) {
                keystoreitems.put(alias.getContactName() + " - " //$NON-NLS-1$
                        + alias.getKeyLength() + "Bit - " //$NON-NLS-1$
                        + alias.getClassName(), alias);
            }
        }
    }

    @Override
	public final void createControl(final Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout();
        gl.marginWidth = 50;
        composite.setLayout(gl);
        
        new Label(composite, SWT.NONE).setText(Messages.LoadPublicKeyPage_select_from_list);
        
        combo = new Combo(composite, SWT.READ_ONLY);
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        combo.setItems(keystoreitems.keySet().toArray(new String[keystoreitems.size()])); 
        combo.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(final SelectionEvent e) {
                boolean complete = !combo.getText().equals(""); //$NON-NLS-1$
                if (complete) {
                    publicAlias = keystoreitems.get(combo.getText());
                }
                setPageComplete(complete);
            }
        });
        
        // Select a default key and call the SelectionListener
        if (combo.getItems().length > 0) {
        	combo.select(0);
        	combo.notifyListeners(SWT.Selection, new Event());
        }
        
        setControl(composite);
    }

    @Override
    public final IWizardPage getNextPage() {
        return null;
    }
    
    /**
     * Getter for the combo box selection.
     * @return The alias that is selected in the comboBox. If nothing is selected probably null
     */
    public KeyStoreAlias getPublicAlias() {
    	return publicAlias;
    }
}

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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;

import de.flexiprovider.core.elgamal.ElGamalPrivateKey;

/**
 * Page for loading a Keypair.
 * 
 * @author Michael Gaber
 * @author Thorben Groos
 */
public class LoadKeypairPage extends WizardPage {

    /** Keystore manager for accessing the keystore. */
    private KeyStoreManager ksm;

    /** Hashmap for the Keystoreitems. */
    private final HashMap<String, KeyStoreAlias> keyStoreItems = new HashMap<String, KeyStoreAlias>();

    /** Combo box for storing the Aliases. */
    private Combo combo;

    /** Field for entering the password. */
    private Text passfield;

    /** the resulting private alias. */
    private KeyStoreAlias privateAlias;

    /** the resulting public alias. */
    private KeyStoreAlias publicAlias;
    
    /** hint for the wrong password */
    private Label pwHint;

    /**
     * Constructor setting pagename, description, data and completion status and initializes the keystore connection
     * 
     * @param data {@link #data}
     */
    public LoadKeypairPage(final ElGamalData data) {
        super("Load Keypair Page", Messages.LoadKeypairPage_select_keypair, null);
        setDescription(Messages.LoadKeypairPage_select_keypair_text);
        setPageComplete(false);
        initKeystoreConnection();
    }

    /**
     * initializes the keystore connection and gets a list of all compatible keys
     */
    private void initKeystoreConnection() {
        ksm = KeyStoreManager.getInstance();
        KeyStoreAlias alias;
        for (final Enumeration<String> aliases = ksm.getAliases(); aliases.hasMoreElements();) {
            alias = new KeyStoreAlias(aliases.nextElement());
            if (alias.getClassName().equals(ElGamalPrivateKey.class.getName())) {
                keyStoreItems.put(alias.getContactName() + " - " //$NON-NLS-1$
                        + alias.getKeyLength() + "Bit - " //$NON-NLS-1$
                        + alias.getClassName(), alias);
            }
        }
    }

    @Override
	public void createControl(final Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout();
        gl.marginWidth = 50;
        composite.setLayout(gl);
        new Label(composite, SWT.NONE).setText(Messages.LoadKeypairPage_select_keypair_from_list);
        
        combo = new Combo(composite, SWT.READ_ONLY);
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        combo.setItems(keyStoreItems.keySet().toArray(new String[keyStoreItems.size()]));
        combo.addSelectionListener(new SelectionListener() {
        	
            @Override
			public void widgetDefaultSelected(final SelectionEvent e) {
                // will not be called
            }

            @Override
			public void widgetSelected(final SelectionEvent e) {
                privateAlias = keyStoreItems.get(combo.getText());
                publicAlias = getPublicForPrivate();
                checkComplete();
            }
        });

        //Seperator
        GridData gd_seperator = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_seperator.verticalIndent = 30;
        new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd_seperator);

        Text enterPasswordText = new Text(composite, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
        enterPasswordText.setText(Messages.LoadKeypairPage_enter_password);
        GridData gd_enterPasswordText = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_enterPasswordText.verticalIndent = 30;
        enterPasswordText.setLayoutData(gd_enterPasswordText);
        
        passfield = new Text(composite, SWT.BORDER | SWT.PASSWORD);
        passfield.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        passfield.addModifyListener(new ModifyListener() {
            @Override
			public void modifyText(final ModifyEvent e) {
                checkComplete();
            }
        });
        
        pwHint = new Label(composite, SWT.NONE);
        pwHint.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
        pwHint.setVisible(false);
        pwHint.setText(Messages.LoadKeypairPage_0);
        pwHint.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        //select a key in the combo box
        if (combo.getItemCount() > 0) {
        	combo.select(0);
        	combo.notifyListeners(SWT.Selection, new Event());
        }
        
        setControl(composite);
    }

    /**
     * Sets the visibility for the hint for the password
     * True if it should be displayed. False if not.
     * @param status true, if the password is false. false, if it is right.
     */
    public void setPasswordHint(boolean status) {
    	pwHint.setVisible(status);
    }
    
    /**
     * gets the matching public entry for a private one.
     * 
     * @return the {@link KeyStoreAlias} for the public key
     */
    protected KeyStoreAlias getPublicForPrivate() {
        Enumeration<String> aliases = ksm.getAliases();
        KeyStoreAlias alias;
        while (aliases != null && aliases.hasMoreElements()) {
            alias = new KeyStoreAlias(aliases.nextElement());
            if (alias.getHashValue().equalsIgnoreCase(privateAlias.getHashValue()) && alias != privateAlias) {
                return alias;
            }
        }
        return null;
    }

    /**
     * checks whether this page is complete and sets setPageComplete.
     */
    private void checkComplete() {
    	setPageComplete(privateAlias != null && !passfield.getText().isEmpty());
    }

    @Override
    public final IWizardPage getNextPage() {
        return null;
    }
    
    /**
     * Getter for the selected private Alias.
     * @return The selected private KeyStoreAlias.
     */
    public KeyStoreAlias getPrivateAlias() {
    	return privateAlias;
    }
    
    /**
     * Getter for the selected public Alias.
     * @return The public Alias for the selected private Alias.
     */
    public KeyStoreAlias getPublicAlias() {
    	return publicAlias;
    }
    
    /**
     * Getter for the password of the alias.
     * @return The Password for the KeyStoreAlias.
     */
    public String getPassword() {
    	return passfield.getText();
    }
    
}

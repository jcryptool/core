// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.dsa.ui.wizards.wizardpages;

import java.security.KeyStoreException;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.exceptions.NoKeyStoreFileException;
import org.jcryptool.visual.dsa.DSAData;

import de.flexiprovider.core.elgamal.ElGamalPrivateKey;

/**
 * Page for loading a Keypair.
 *
 * @author Michael Gaber
 */
public class LoadKeypairPage extends WizardPage {

	/** constant for the pagename. */
	private static final String PAGENAME = "Load Keypair Page";

	/** constant for the title */
	private static final String TITLE = "Schlüsselpaar auswählen";

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

	/** shared data object. */
	private final DSAData data;

	/** the resulting public alias. */
	private KeyStoreAlias publicAlias;

	public LoadKeypairPage(final DSAData data) {
		super(PAGENAME, TITLE, null);
		this.setDescription("Bitte wählen Sie ein Schlüsselpaar aus");
		initKeystoreConnection();
		this.data = data;
		setPageComplete(false);
	}

	private void initKeystoreConnection() {
		ksm = KeyStoreManager.getInstance();
		try {
			ksm.loadKeyStore(KeyStorePlugin.getPlatformKeyStoreURI());
			KeyStoreAlias alias;
			for (final Enumeration<String> aliases = ksm.getAliases(); aliases
					.hasMoreElements();) {
				alias = new KeyStoreAlias(aliases.nextElement());
				if (alias.getClassName().equals(
						ElGamalPrivateKey.class.getName())) {
					keyStoreItems.put(alias.getContactName() + " - "
							+ alias.getKeyLength() + "Bit - "
							+ alias.getClassName(), alias);
				}
			}
		} catch (final NoKeyStoreFileException e) {
		    LogUtil.logError(e);
		} catch (final KeyStoreException e) {
		    LogUtil.logError(e);
		}
	}

	public void createControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		// TODO in between do stuff like layout et al
		final GridLayout gl = new GridLayout();
		gl.marginWidth = 50;
		composite.setLayout(gl);
		new Label(composite, SWT.NONE)
				.setText("Bitte wählen Sie ein Schlüsselpaar aus der Liste aus.");
		combo = new Combo(composite, SWT.READ_ONLY);
		final GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		combo.setLayoutData(gd);
		combo.setItems(keyStoreItems.keySet().toArray(
				new String[keyStoreItems.size()]));
		combo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				// will not be called
			}

			public void widgetSelected(final SelectionEvent e) {
				privateAlias = keyStoreItems.get(combo.getText());
				publicAlias = getPublicForPrivate();
				checkComplete();
			}
		});

		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd);

		final Text l = new Text(composite, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
		l
				.setText("Bitte geben Sie das zu diesem Schlüsselpaar gehörige Passwort ein.\nSollte kein Passwort gesetzt sein, lassen Sie das Feld einfach leer.");
		l.setLayoutData(gd);
		passfield = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		passfield.setLayoutData(gd);
		passfield.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				checkComplete();
			}
		});
		// end TODO
		setControl(composite);
	}

	/**
	 * gets the matching public entry for a private one.
	 *
	 * @return the {@link KeyStoreAlias} for the public key
	 */
	protected KeyStoreAlias getPublicForPrivate() {
		Enumeration<String> aliases;
		try {
			aliases = ksm.getAliases();
		} catch (final KeyStoreException e) {
		    LogUtil.logError(e);
			return null;
		}
		KeyStoreAlias alias;
		while (aliases != null && aliases.hasMoreElements()) {
			alias = new KeyStoreAlias(aliases.nextElement());
			if (alias.getHashValue().equalsIgnoreCase(
					privateAlias.getHashValue())
					&& alias != privateAlias) {
				return alias;
			}
		}
		return null;
	}

	/**
	 * checks whether this page is already complete.
	 */
	private void checkComplete() {
		final boolean complete = privateAlias != null
				&& !passfield.getText().equals("");
		if (complete) {
			data.setPrivateAlias(privateAlias);
			data.setPublicAlias(publicAlias);
			data.setContactName(privateAlias.getContactName());
			data.setPassword(passfield.getText());
		}
		setPageComplete(complete);
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

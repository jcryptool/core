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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.jcryptool.visual.dsa.DSAData;

import de.flexiprovider.core.elgamal.ElGamalPublicKey;

/**
 * Page for loading a public Key.
 *
 * @author Michael Gaber
 */
public class LoadPublicKeyPage extends WizardPage {

	/** the pagename for accessing the wizardpage. */
	private static final String PAGENAME = "Load Public Key Page";

	/** title of this page. */
	private static final String TITLE = "Öffentlichen Schlüssel auswählen.";

	/** shared data object. */
	private final DSAData data;

	/** storage for the alias items. */
	private final HashMap<String, KeyStoreAlias> keystoreitems = new HashMap<String, KeyStoreAlias>();

	/** combo for displaying the aliases. */
	private Combo combo;

	/** the selected public alias. */
	protected KeyStoreAlias publicAlias;

	/**
	 * Constructor setting the data object and some stuff.
	 *
	 * @param data
	 *            the {@link #data}
	 */
	public LoadPublicKeyPage(final DSAData data) {
		super(PAGENAME, TITLE, null);
		this
				.setDescription("Bitte wählen Sie aus, welchen öffentlichen Schlüssel Sie verwenden möchten.");
		this.data = data;
		this.setPageComplete(false);
		this.initKeystoreConnection();
	}

	/**
	 * initializes the connection to the keystore.
	 */
	private void initKeystoreConnection() {
		final KeyStoreManager ksm = KeyStoreManager.getInstance();
		try {
			ksm.loadKeyStore(KeyStorePlugin.getPlatformKeyStoreURI());
			KeyStoreAlias alias;
			final Enumeration<String> aliases = ksm.getAliases();
			while (aliases != null && aliases.hasMoreElements()) {
				alias = new KeyStoreAlias(aliases.nextElement());
				if (alias.getClassName().equals(
						ElGamalPublicKey.class.getName())) {
					keystoreitems.put(alias.getContactName() + " - "
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

	/**
	 * Set up the UI stuff.
	 *
	 * @param parent
	 *            the parent composite.
	 */
	public final void createControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		// TODO in between do stuff like layout et al
		final GridLayout gl = new GridLayout();
		gl.marginWidth = 50;
		composite.setLayout(gl);
		new Label(composite, SWT.NONE)
				.setText("Bitte wählen Sie einen öffentlichen Schlüssel aus der Liste aus");
		combo = new Combo(composite, SWT.READ_ONLY);
		final GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		combo.setLayoutData(gd);
		combo.setItems(keystoreitems.keySet().toArray(
				new String[keystoreitems.size()]));
		combo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(final SelectionEvent e) {
				final boolean complete = !combo.getText().equals("");
				if (complete) {
					publicAlias = keystoreitems.get(combo.getText());
					data.setPublicAlias(publicAlias);
					data.setContactName(publicAlias.getContactName());
				}
				setPageComplete(complete);
			}

			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
			}
		});
		// end TODO
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

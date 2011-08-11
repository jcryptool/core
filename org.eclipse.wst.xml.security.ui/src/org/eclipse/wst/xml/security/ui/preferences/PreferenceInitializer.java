/*******************************************************************************
 * Copyright (c) 2008 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;

/**
 * <p>This class is used to initialize all default preference values of the
 * XML Security Tools.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

  /**
   * Constructor.
   */
  public PreferenceInitializer() {
    super();
  }

  /**
   * Initializes default preference values for the XML Security Tools. This method is called
   * automatically by the preference initializer when the appropriate default preference node is
   * accessed.
   */
  public void initializeDefaultPreferences() {
    IPreferenceStore store = XSTUIPlugin.getDefault().getPreferenceStore();
    // Canonicalization
    store.setDefault(PreferenceConstants.CANON_TYPE, "exclusive");
    store.setDefault(PreferenceConstants.CANON_TARGET, "internal");
    // Encryption
    store.setDefault(PreferenceConstants.ENCRYPT_RESOURCE, "document");
    store.setDefault(PreferenceConstants.ENCRYPT_XPATH, "");
    store.setDefault(PreferenceConstants.ENCRYPT_TYPE, "enveloping");
    store.setDefault(PreferenceConstants.ENCRYPT_ENCRYPTION, "AES 128");
    store.setDefault(PreferenceConstants.ENCRYPT_KEY_WRAP, "AES-128 Key Wrap");
    store.setDefault(PreferenceConstants.ENCRYPT_ID, "myEncryption");
    store.setDefault(PreferenceConstants.ENCRYPT_KEY_STORE, "");
    store.setDefault(PreferenceConstants.ENCRYPT_KEY_NAME, "");
    // Signature
    store.setDefault(PreferenceConstants.SIGN_RESOURCE, "document");
    store.setDefault(PreferenceConstants.SIGN_XPATH, "");
    store.setDefault(PreferenceConstants.SIGN_TYPE, "enveloped");
    store.setDefault(PreferenceConstants.SIGN_ID, "mySignature");
    store.setDefault(PreferenceConstants.SIGN_CANON, "Exclusive without comments");
    store.setDefault(PreferenceConstants.SIGN_TRANS, "None");
    store.setDefault(PreferenceConstants.SIGN_MDA, "SHA 1");
    store.setDefault(PreferenceConstants.SIGN_SA, "DSA with SHA 1 (DSS)");
    store.setDefault(PreferenceConstants.SIGN_KEYSTORE_FILE, "");
    store.setDefault(PreferenceConstants.SIGN_KEY_NAME, "");
  }
}

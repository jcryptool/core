// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crtverification.verification;

import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.visual.crtverification.Activator;

/**
 * Connector to the JCT Keystore to load and store Certificates and Certificate Chains
 * 
 * @author Clemens Elst
 * 
 */
public class KeystoreConnector {

    /**
     * @return all aliases currently in the JCT Keystore
     */
    public Enumeration<String> getAllAliases() {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        return ksm.getAliases();
    }

    /**
     * Returns the alias of a given contact name
     * 
     * @param contactName the contacts name in the Keystore
     * @return the contacts alias or null if none was found
     */
    public IKeyStoreAlias getAliasByContactName(String contactName) {
        IKeyStoreAlias alias = null;
        KeyStoreManager ksm = KeyStoreManager.getInstance();

        Enumeration<String> allAliases = ksm.getAliases();
        String encodedContactName = encode(contactName).toLowerCase();

        while (allAliases.hasMoreElements()) {
            String compareAlias = allAliases.nextElement();
            if (compareAlias.contains(encodedContactName)) {
                alias = new KeyStoreAlias(compareAlias);
            }
        }

        return alias;
    }

    /**
     * @param alias the contacts name alias
     * @return certificate of the alias
     */
    public Certificate getCertificate(IKeyStoreAlias alias) {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        Certificate cert = null;

        try {
            cert = ksm.getCertificate(alias);
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }
        return cert;
    }

    /**
     * @return A ArrayList of all Certificates currently stored in the JCT Keystore as
     *         X509Certificates
     */
    public ArrayList<X509Certificate> getAllCertificates() {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        ArrayList<X509Certificate> certificates = new ArrayList<X509Certificate>(ksm.getAllPublicKeys().size());

        ArrayList<String> aliases = Collections.list(getAllAliases());

        for (String string : aliases) {
            try {
                KeyStoreAlias alias = new KeyStoreAlias(string);

                X509Certificate cert = (X509Certificate) ksm.getCertificate(alias);
                if (cert != null && !certificates.contains(cert)) {
                    certificates.add(cert);
                }
            } catch (UnrecoverableEntryException | NoSuchAlgorithmException e) {
                // do noting, try next alias
            }
        }

        return certificates;
    }

    /**
     * Adds a new certificate to the JCT Keystore bound to a given Keystore contact
     * <p>
     * Only one certificate per alias can be added
     * 
     * @param cert the certificate to add
     * @param alias the contacts alias
     */
    public void addCertificate(Certificate cert, IKeyStoreAlias alias) throws NullPointerException {
        if (cert != null && alias != null) {
            KeyStoreManager ksm = KeyStoreManager.getInstance();
            ksm.addCertificate(cert, alias);
        } else {
            NullPointerException e;
            if (cert == null) {
                e = new NullPointerException("cert");
            } else {
                e = new NullPointerException("alias");
            }
            throw e;
        }
    }

    /**
     * Encodes the given string to ASCII.
     * 
     * @param string The string parameter that will be encoded into ASCII
     * @return The corresponding ASCII value
     */
    private String encode(String string) {
        if (string == null) {
            return ""; //$NON-NLS-1$
        }
        StringBuffer sb = new StringBuffer();
        char c;
        for (int i = 0; i < string.length(); ++i) {
            c = string.charAt(i);
            String hex = Integer.toHexString(c).toUpperCase();
            for (int j = 0; j < 2 - hex.length(); j++) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}

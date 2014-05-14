package org.jcyptool.visual.crtverification.keystore;

import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;

import org.jcryptool.core.logging.utils.LogUtil;
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
    private static final char[] KEYSTORE_PASSWORD = { 'j', 'c', 'r', 'y', 'p', 't', 'o', 'o', 'l' };

    /**
     * @return all Aliases currently in the JCT Keystore
     */
    public Enumeration<String> getAllAliases() {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        return ksm.getAliases();
    }

    /**
     * Returns the alias of a given contacts name
     * 
     * @param contactName the contacts name in the Keystore
     * @return the contacts alias or null if none was found
     */
    public IKeyStoreAlias getAliasByContactName(String contactName) {
        IKeyStoreAlias alias = null;
        KeyStoreManager ksm = KeyStoreManager.getInstance();

        for (IKeyStoreAlias pubAlias : ksm.getAllPublicKeys()) {
            if (pubAlias.getContactName().contains(contactName)) {
                alias = pubAlias;
            }
        }

        return alias;
    }

    /**
     * @return A List of all Certificates currently stored in the JCT Keystore
     */
    public ArrayList<Certificate> getAllCertificates() {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        ArrayList<Certificate> certificates = new ArrayList<Certificate>(ksm.getAllPublicKeys().size());

        for (IKeyStoreAlias pubAlias : ksm.getAllPublicKeys()) {
            try {
                certificates.add(ksm.getCertificate(pubAlias));
            } catch (UnrecoverableEntryException | NoSuchAlgorithmException e) {
                LogUtil.logError(Activator.PLUGIN_ID, e);
                e.printStackTrace();
            }
        }
        
        return certificates;
    }

    /**
     * Adds a new certificate to the JCT Keystore bound to a given Keystore contact
     * 
     * @param cert the certificate to add
     * @param alias the contacts alias
     */
    public void addCertificate(Certificate cert, IKeyStoreAlias alias) throws NullPointerException{
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

    public void addCertificateChain(CertPath path, IKeyStoreAlias alias) {
        if (path != null && alias != null) {
            for (Certificate cert : path.getCertificates()) {
                addCertificate(cert, alias);
            }
        } else {
            NullPointerException e;

            if (path == null) {
                e = new NullPointerException("CertPath null");
            } else {
                e = new NullPointerException("alias null");
            }
            throw e;
        }
    }
}

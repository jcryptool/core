package org.jcryptool.visual.crtverification.keystore;

import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
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

    
    //not working
    /**
     * Returns the alias of a given contact name
     * 
     * @param contactName the contacts name in the Keystore
     * @return the contacts alias or null if none was found
     */
    public IKeyStoreAlias getAliasByContactName(String contactName) {
        IKeyStoreAlias alias = null;
        KeyStoreManager ksm = KeyStoreManager.getInstance();

        for (IKeyStoreAlias pubAlias : ksm.getAllPublicKeys()) {
            System.out.println(pubAlias.toString());
            if (pubAlias.getContactName().contains(contactName)) {
                alias = pubAlias;
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
     * @return A List of all Certificates currently stored in the JCT Keystore
     */
    public ArrayList<Certificate> getAllCertificates() {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        ArrayList<Certificate> certificates = new ArrayList<Certificate>(ksm.getAllPublicKeys().size());

        ArrayList<String> aliases = Collections.list(getAllAliases());

        for (String string : aliases) {
            try {
                KeyStoreAlias alias = new KeyStoreAlias(string);

                Certificate cert = ksm.getCertificate(alias);
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

}

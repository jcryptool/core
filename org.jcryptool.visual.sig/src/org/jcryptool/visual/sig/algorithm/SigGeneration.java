package org.jcryptool.visual.sig.algorithm;

import java.security.*;

import de.flexiprovider.core.rsa.RSAPrivateCrtKey;
import de.flexiprovider.core.dsa.*;
import java.util.Enumeration;
import java.util.HashMap;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sig.SigPlugin;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.exceptions.NoKeyStoreFileException;

public class SigGeneration {
	public String signature;
	private final static HashMap<String, KeyStoreAlias> keystoreitems = new HashMap<String, KeyStoreAlias>();
	private static PrivateKey k = null;
	
	/**
	 * This method signed a hash stored in Hash.jave with a given signature
	 * method.
	 * 
	 * @param signaturemethod
	 *            Chosen signature method to sign the hash.
	 * @param input
	 *            hash form Hash.java
	 * @return
	 * @throws Exception
	 */
	public static byte[] SignInput(String signaturemethod, byte[] input) throws Exception {
		//Check if called by JCT-CA
		if (org.jcryptool.visual.sig.algorithm.Input.privateKey != null) { //Use their key
			KeyStoreManager ksm = KeyStoreManager.getInstance();
			ksm.loadKeyStore(KeyStorePlugin.getPlatformKeyStoreURI());
			org.jcryptool.visual.sig.algorithm.Input.privateKey.getAliasString();
			k = ksm.getPrivateKey(org.jcryptool.visual.sig.algorithm.Input.privateKey, KeyStoreManager.getDefaultKeyPassword());
		}
		
		byte[] signature = null; //Stores the signature
		//signaturemethod = "DSA";
		
        
		try {
			
			if (k == null) { //Get key
				
				if (signaturemethod.contains("DSA")) {
					k = initKeystoreDSA();
				} else {
					if (signaturemethod.contains("RSA")) {
						k = initKeystoreRSA();
					} else {
						if (signaturemethod.contains("ECDSA")) {
							//method = "ECDSA";
						}
					}
				}
			}
			        
			// Get a signature object using the specified combo and sign the data with the private key
			Signature sig = Signature.getInstance(signaturemethod);
	        //sig.initSign(key.getPrivate());
			sig.initSign(k);
	        sig.update(input);
	        signature = sig.sign();
	        
	        //Store the generated signature
	        org.jcryptool.visual.sig.algorithm.Input.signature = signature; //Store the generated original signature
		    org.jcryptool.visual.sig.algorithm.Input.signatureHex = org.jcryptool.visual.sig.algorithm.Input.bytesToHex(signature); //Hex String
		      
		    return signature;
		}
				
		catch (Exception ex) {
			LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
		}
		return signature;
	}
	
    /**
     * initializes the connection to the keystore.
     * @throws Exception 
     */
    private static PrivateKey initKeystoreRSA() throws Exception {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        PrivateKey k = null;
        try {
            ksm.loadKeyStore(KeyStorePlugin.getPlatformKeyStoreURI());
            KeyStoreAlias alias;
            Enumeration<String> aliases = ksm.getAliases();
            while (aliases != null && aliases.hasMoreElements()) {
                alias = new KeyStoreAlias(aliases.nextElement());
                alias.getAliasString();
                //k1 = ksm.getKey(alias); 
                if (alias.getClassName().equals(RSAPrivateCrtKey.class.getName())) {
                    keystoreitems.put(alias.getContactName() + " - " + alias.getKeyLength() + "Bit - " //$NON-NLS-1$ //$NON-NLS-2$
                            + alias.getClassName(), alias);
                    k = ksm.getPrivateKey(alias, KeyStoreManager.getDefaultKeyPassword());
                } 
            }
            return k;
        } catch (NoKeyStoreFileException e) {
            LogUtil.logError(e);
        } catch (KeyStoreException e) {
            LogUtil.logError(e);
        }
		return k;
    }
    
    private static PrivateKey initKeystoreDSA() throws Exception {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        PrivateKey k = null;
        try {
            ksm.loadKeyStore(KeyStorePlugin.getPlatformKeyStoreURI());
            KeyStoreAlias alias;
            Enumeration<String> aliases = ksm.getAliases();
            while (aliases != null && aliases.hasMoreElements()) {
                alias = new KeyStoreAlias(aliases.nextElement());
                alias.getAliasString();
                //k1 = ksm.getKey(alias); 
                if (alias.getClassName().equals(DSAPrivateKey.class.getName())) {
                    keystoreitems.put(alias.getContactName() + " - " + alias.getKeyLength() + "Bit - " //$NON-NLS-1$ //$NON-NLS-2$
                            + alias.getClassName(), alias);
                    k = ksm.getPrivateKey(alias, KeyStoreManager.getDefaultKeyPassword());
                } 
            }
            return k;
        } catch (NoKeyStoreFileException e) {
            LogUtil.logError(e);
        } catch (KeyStoreException e) {
            LogUtil.logError(e);
        }
		return k;
    }

}

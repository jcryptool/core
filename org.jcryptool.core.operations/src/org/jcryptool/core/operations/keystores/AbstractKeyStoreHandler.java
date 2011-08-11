//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.operations.keystores;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;

/**
 * Abstract key store handler class that serves as the plug-in interface.
 * 
 * @author t-kern
 *
 */
public abstract class AbstractKeyStoreHandler {

	/**
	 * Adds a keypair to the KeyStore.
	 * 
	 * @param alias			The alias of the new KeyStore entry
	 * @param password		The password protecting the private key
	 * @param privateKey	The private key
	 * @param certificate	The certificate
	 */
	public abstract void addKeyPair(String alias, char[] password, PrivateKey privateKey, Certificate certificate);
	
	/**
	 * Returns a list of all aliases in the KeyStore.
	 * 
	 * @return A list of all aliases in the KeyStore	
	 */
	public abstract ArrayList<String> getAliases();
	
	/**
	 * Returns the alias of the certificate specified by the given issuer DN and serial number.
	 * 
	 * @param issuerDN		The ASN.1 encoded issuer DN
	 * @param serialNumber	The serial number of the certificate 
	 * @return				The alias of the corresponding KeyStore entry
	 */
	public abstract String getAliasForCertificate(byte[] issuerDN, BigInteger serialNumber);
	
	/**
	 * Returns the certificate.
	 * 
	 * @param alias	The alias of the KeyStore entry
	 * @return		The certificate
	 */
	public abstract Certificate getCertificate(String alias);
	
	/**
	 * Returns the private key.
	 * 
	 * @param alias		The alias of the KeyStore entry
	 * @param password	The password
	 * @return			The private key
	 */
	public abstract PrivateKey getPrivateKey(String alias, char[] password);
		
}

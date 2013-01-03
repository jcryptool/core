//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStoreException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaKeyGenerator;
import org.jcryptool.crypto.flexiprovider.keystore.FlexiProviderKeystorePlugin;
import org.jcryptool.crypto.flexiprovider.reflect.Reflector;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.descriptors.NewEntryDescriptor;
import org.jcryptool.crypto.keystore.descriptors.NewKeyPairDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IContactDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewEntryDescriptor;
import org.jcryptool.crypto.keystore.exceptions.NoKeyStoreFileException;
import org.jcryptool.crypto.keystore.ui.actions.AbstractNewKeyStoreEntryAction;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.keys.KeyPair;
import de.flexiprovider.api.keys.KeyPairGenerator;
import de.flexiprovider.api.keys.PrivateKey;
import de.flexiprovider.api.keys.PublicKey;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;
import de.flexiprovider.core.rsa.RSAPrivateCrtKey;
import de.flexiprovider.core.rsa.RSAPublicKey;

/**
 * Represents all actions concerning Identities
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class IdentityManager extends AbstractNewKeyStoreEntryAction{
	private ContactManager cManager;
	private KeyStoreManager ksManager;
	private Enumeration<String> aliases;
	
	public IdentityManager(){
		cManager = ContactManager.getInstance();
		ksManager = KeyStoreManager.getInstance();
	}

	public void createIdentity(final String name, final String algorithm, final String password, final int keyLength){
		final String concreteAlgorithm = getConcreteAlgorithm(algorithm);
		final INewEntryDescriptor nkd = new NewEntryDescriptor(name, algorithm, concreteAlgorithm, keyLength, password, "PLACEHOLDER", KeyType.KEYPAIR);
		final Integer[] argument = new Integer[1];
		argument[0] = keyLength;
		LogUtil.logInfo("create new keypair");
		Job job = new Job("New Key Pair Job") { //$NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("New KeyPair Task", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
				try {
					IMetaKeyGenerator gen = AlgorithmsXMLManager.getInstance().getKeyPairGenerator(algorithm);
					
					AlgorithmParameterSpec spec = null;
					if (keyLength != -1) {
						if (gen.getParameterSpecClassName() != null) {
							spec = Reflector.getInstance().instantiateParameterSpec(gen.getParameterSpecClassName(), argument);
						}
					}
					KeyPairGenerator generator = Registry.getKeyPairGenerator(nkd.getAlgorithmName());
					if (spec != null) {
						generator.initialize(spec, FlexiProviderKeystorePlugin.getSecureRandom());
					} else if (keyLength != -1) {
						generator.initialize(keyLength, FlexiProviderKeystorePlugin.getSecureRandom());
					}
					KeyPair keyPair = generator.genKeyPair();
		
					PrivateKey priv = keyPair.getPrivate();
					PublicKey pub = keyPair.getPublic();
					performNewKeyAction(new NewKeyPairDescriptor(nkd, priv, pub));
				} catch (NoSuchAlgorithmException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "NoSuchAlgorithmException while generating a key pair", e, true);
				} catch (InvalidAlgorithmParameterException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "InvalidAlgorithmParameterException while generating a key pair", e, true);
				} catch (SecurityException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "SecurityException while generating a key pair", e, true);
				} catch (IllegalArgumentException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "IllegalArgumentException while generating a key pair", e, true);
				} catch (ClassNotFoundException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "ClassNotFoundException while generating a key pair", e, true);
				} catch (NoSuchMethodException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "NoSuchMethodException while generating a key pair", e, true);
				} catch (InstantiationException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "InstantiationException while generating a key pair", e, true);
				} catch (IllegalAccessException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "IllegalAccessException while generating a key pair", e, true);
				} catch (InvocationTargetException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID,
                            "InvocationTargetException while generating a key pair", e, true);
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.LONG);
		job.schedule();
		
	}

	private String getConcreteAlgorithm(String algorithm) {
		List<IMetaKeyGenerator>  keyPairGenerators = AlgorithmsXMLManager.getInstance().getKeyPairGenerators();
        Iterator<IMetaKeyGenerator> it = keyPairGenerators.iterator();
        
        while (it.hasNext()) {
            IMetaKeyGenerator current = it.next();
            String allNames = ""; //$NON-NLS-1$
            Iterator<String> namesIt = current.getNames().iterator();
            while (namesIt.hasNext()) {
                allNames += namesIt.next() + ", "; //$NON-NLS-1$
            }
            allNames = allNames.substring(0, allNames.length() - 2);
            
            if (allNames.equals(algorithm)){
            	if (current.getOID() != null) {
            		return algorithm+" (OID: " + current.getOID().getStringOID() + ")";
            	}
            	else{
            		return algorithm;
            	}
            }
        }
        return algorithm;
    }
	
	public Vector<String> getContacts(){ 
		Vector<String> contactNames = new Vector<String>();             
        Iterator<IContactDescriptor> it = cManager.getContacts();
        IContactDescriptor meta;
        
        while (it.hasNext()) {
            meta = it.next();
            contactNames.add(meta.getName());
        }
        
        return contactNames;
	}
	
	public Vector<String>getAssymetricKeyAlgorithms(String identity){
		Vector<String> keyAlgos = new Vector<String>();
		KeyStoreAlias localKeyStoreAlias = null;
        try {
			aliases = ksManager.getAliases();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
        
        
        while (aliases.hasMoreElements()) {
            localKeyStoreAlias = new KeyStoreAlias(aliases.nextElement());
            
            if (localKeyStoreAlias.getKeyStoreEntryType().getType().contains(KeyType.KEYPAIR.getType())) { // asymmetric
                if (localKeyStoreAlias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PUBLIC_KEY) && localKeyStoreAlias.getContactName().equals(identity)) {
                    keyAlgos.add(ksManager.getKey(localKeyStoreAlias).getAlgorithm());
                }
            }
        }
        return keyAlgos;
	}
	/**
	 * Method to get possible recipient-keys
	 * @param identity specifies the identity looking for OTHER public key (at the moment, "identity" could contain "Alice" (without surname))
	 */
	public HashMap<String, KeyStoreAlias> getPublicKeys(String identity){
//		System.out.println("id: "+identity);
		HashMap<String, KeyStoreAlias> pubkeys = new HashMap<String, KeyStoreAlias>();
		KeyStoreAlias alias = null;
		int counter = 0;
        try {
			aliases = ksManager.getAliases();
			while (aliases != null && aliases.hasMoreElements()) {
                alias = new KeyStoreAlias(aliases.nextElement());
//                System.out.println("contact.name: "+alias.getContactName());
                if (alias.getClassName().equals(RSAPublicKey.class.getName())&&alias.getContactName().substring(0,alias.getContactName().indexOf(' ')).equals(identity)) {
                	counter++;
                	pubkeys.put(alias.getContactName().substring(0,alias.getContactName().indexOf(' ')) + " - " + alias.getKeyLength() + "Bit - "+ alias.getClassName().substring(alias.getClassName().lastIndexOf('.')+1)+" - "+counter,alias);
                	System.out.println(alias.getContactName().substring(0,alias.getContactName().indexOf(' ')) + " - " + alias.getKeyLength() + "Bit - "+ alias.getClassName()+" - "+counter);
                
                }
            }
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
       
       return pubkeys;
	}
	/**
	 * 
	 * @param alias The alias who wants to get his publicKey
	 * @return parameters: contains the publicKey Parameters. The first parameter is 'N', the second 'e'
	 */
	public Vector<BigInteger>getPublicKeyParameters(KeyStoreAlias alias){
		Vector<BigInteger> parameters = new Vector<BigInteger>();

		final RSAPublicKey pubkey = (RSAPublicKey) ksManager.getPublicKey(alias).getPublicKey();
		parameters.add(pubkey.getModulus());
		System.out.println("N: "+pubkey.getModulus());
		parameters.add(pubkey.getPublicExponent());
		System.out.println("e: "+pubkey.getPublicExponent());
		
		return parameters;
	}
}

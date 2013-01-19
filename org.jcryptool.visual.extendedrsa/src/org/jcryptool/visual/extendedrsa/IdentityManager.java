//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
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
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.certificates.CertificateFactory;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaKeyGenerator;
import org.jcryptool.crypto.flexiprovider.keystore.FlexiProviderKeystorePlugin;
import org.jcryptool.crypto.flexiprovider.reflect.Reflector;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.descriptors.NewEntryDescriptor;
import org.jcryptool.crypto.keystore.descriptors.NewKeyPairDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewEntryDescriptor;
import org.jcryptool.crypto.keystore.ui.actions.AbstractNewKeyStoreEntryAction;
import org.jcryptool.crypto.keystore.ui.views.nodes.Contact;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.keys.KeyPair;
import de.flexiprovider.api.keys.KeyPairGenerator;
import de.flexiprovider.api.keys.PublicKey;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;
import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.core.mprsa.MpRSAKeyGenParameterSpec;
import de.flexiprovider.core.mprsa.MpRSAPrivateKey;
import de.flexiprovider.core.mprsa.RSAOtherPrimeInfo;
import de.flexiprovider.core.rsa.RSAPrivateCrtKey;
import de.flexiprovider.core.rsa.RSAPublicKey;

/**
 * Represents all actions concerning Identities
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class IdentityManager extends AbstractNewKeyStoreEntryAction{
	private static IdentityManager identityManager;

	private Enumeration<String> aliases;
	private Map<String,Integer> keymgmt;
	private Map<String,Integer> privKeymgmt;
	private Map<String,Integer> allKeysForID;
	private int keyID;
	private int privKeyID;
	private int genKeyID;
	private KeyStoreAlias privateAlias;
	private KeyStoreAlias publicAlias;

	
    public static IdentityManager getInstance() {
        if (identityManager == null) {
        	identityManager = new IdentityManager();
        }
        return identityManager;
    }
    
	private IdentityManager(){
		keymgmt = new HashMap<String,Integer>();
		privKeymgmt = new HashMap<String,Integer>();
		allKeysForID = new HashMap<String,Integer>();
		keyID = 0;
		genKeyID = 0;
		privKeyID = 0;
	}

	/**
	 * method to create an identity with a key (GENERATED parameters)
	 * @param name of the identity
	 * @param algorithm used for the new key
	 * @param password for the private key
	 * @param keyLength
	 */
	public void createIdentity(final String name, final String algorithm, final String password, final int keyLength){
		final String concreteAlgorithm = getConcreteAlgorithm(algorithm);
		final INewEntryDescriptor nkd = new NewEntryDescriptor(name, algorithm, concreteAlgorithm, keyLength, password, Messages.IdentityManager_0, KeyType.KEYPAIR);
		final Integer[] argument = new Integer[1];
		argument[0] = keyLength;
		LogUtil.logInfo(Messages.IdentityManager_1);
		Job job = new Job("New Key Pair Job") { //$NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("New KeyPair Task", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
				try {
					IMetaKeyGenerator gen = AlgorithmsXMLManager.getInstance().getKeyPairGenerator(algorithm);
					
					if (gen != null && name != null && password != null && keyLength > 0){
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
					}else{
						LogUtil.logInfo("Attention: One of the given parameters is wrong! Identity name: '"+name+"', algorithm name: '"+algorithm+"', password: '"+password+"' and key length: '"+keyLength+"'. Key creation failed!");
					}
				} catch (NoSuchAlgorithmException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_2, e, true);
				} catch (InvalidAlgorithmParameterException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_3, e, true);
				} catch (SecurityException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_4, e, true);
				} catch (IllegalArgumentException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_5, e, true);
				} catch (ClassNotFoundException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_6, e, true);
				} catch (NoSuchMethodException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_7, e, true);
				} catch (InstantiationException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_8, e, true);
				} catch (IllegalAccessException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_9, e, true);
				} catch (InvocationTargetException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_10, e, true);
				} 
				finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.LONG);
		job.schedule();	
		
	}
	/**
	 * method to create a "big" new multi-prime RSA key with GENERATED parameters
	 * @param name the name of the identity
	 * @param password the password for the private key
	 * @param keyLength 
	 * @param numberOfPrimes number of used primes (should be between 3 and 5)
	 */
	public void createMpRSAIdentity(final String name, final String password, final int keyLength, final int numberOfPrimes){
		final String algorithm = "MpRSA";
		final INewEntryDescriptor nkd = new NewEntryDescriptor(name, algorithm, algorithm, keyLength, password, Messages.IdentityManager_0, KeyType.KEYPAIR);
		final Integer[] argument = new Integer[1];
		argument[0] = keyLength;
		LogUtil.logInfo(Messages.IdentityManager_1);
		Job job = new Job("New Key Pair Job") { //$NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("New KeyPair Task", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
				try {
					IMetaKeyGenerator gen = AlgorithmsXMLManager.getInstance().getKeyPairGenerator(algorithm);
					if (gen != null && numberOfPrimes > 2 && numberOfPrimes < 6 && name != null && password != null && keyLength > 0){
						final byte[] e = new byte[1];
						e[0] = (byte) 65537;
						FlexiBigInt exponent = new FlexiBigInt(e);
						
						AlgorithmParameterSpec spec = null;
						if (keyLength != -1) {
							if (gen.getParameterSpecClassName() != null) {
								spec = Reflector.getInstance().instantiateParameterSpec(gen.getParameterSpecClassName(), argument);
								spec = new MpRSAKeyGenParameterSpec(keyLength, exponent, numberOfPrimes);
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
					}else{
						LogUtil.logInfo("Attention: One of the given parameters is wrong! Identity name: '"+name+"', number of primes: '"+numberOfPrimes+"', password: '"+password+"' and key length: '"+keyLength+"'. Key creation failed!");
					}
				} catch (NoSuchAlgorithmException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_2, e, true);
				} catch (InvalidAlgorithmParameterException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_3, e, true);
				} catch (SecurityException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_4, e, true);
				} catch (IllegalArgumentException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_5, e, true);
				} catch (ClassNotFoundException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_6, e, true);
				} catch (NoSuchMethodException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_7, e, true);
				} catch (InstantiationException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_8, e, true);
				} catch (IllegalAccessException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_9, e, true);
				} catch (InvocationTargetException e) {
					LogUtil.logError(Activator.PLUGIN_ID, Messages.IdentityManager_10, e, true);
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.LONG);
		job.schedule();	
	}
	
	
	/**
     * setter for the private alias for the contact
     *
     * @param privateAlias the privateAlias to set
     */
    public final void setPrivateAlias(final KeyStoreAlias privateAlias) {
        this.privateAlias = privateAlias;
    }

    /**
     * setter for the public alias for the contact
     *
     * @param publicAlias the publicAlias to set
     */
    public final void setPublicAlias(final KeyStoreAlias publicAlias) {
        this.publicAlias = publicAlias;
    }
	
    /**
     * method to create a classic RSA-key with chosen parameters
     * @param name the name of the alias
     * @param password for the private key
     * @param modulus N
     * @param firstPrime p
     * @param secondPrime q
     * @param pubExponent e
     * @param privExponent d
     */
    public void saveRSAKeyToKeystore(final String name, final String password, final BigInteger modulus, final BigInteger firstPrime, final BigInteger secondPrime, final BigInteger pubExponent, final BigInteger privExponent) {
    	if (name == null || password == null || modulus.compareTo(BigInteger.ZERO) <= 0 || firstPrime.compareTo(BigInteger.ZERO) <= 0 || secondPrime.compareTo(BigInteger.ZERO) <= 0 || pubExponent.compareTo(BigInteger.ZERO) <= 0 || privExponent.compareTo(BigInteger.ZERO) <= 0){
    		throw new IllegalArgumentException("Error: One of the given parameters is not valid!");
    	}
    	final FlexiBigInt n = new FlexiBigInt(modulus), e = new FlexiBigInt(pubExponent);
    	final RSAPublicKey pubkey = new RSAPublicKey(n, e);

    	final KeyStoreAlias publicAlias = new KeyStoreAlias(name, KeyType.KEYPAIR_PUBLIC_KEY,getConcreteAlgorithm("RSA"), new BigInteger(modulus.toString()).bitLength(), (name.concat(modulus.toString())).hashCode() + "", pubkey.getClass().getName());
    	setPublicAlias(publicAlias);
    	
    	
    	final RSAPrivateCrtKey privkey = new RSAPrivateCrtKey(n, e, new FlexiBigInt(privExponent), new FlexiBigInt(firstPrime), new FlexiBigInt(secondPrime), FlexiBigInt.ZERO, FlexiBigInt.ZERO, FlexiBigInt.ZERO);
    	final KeyStoreAlias privateAlias = new KeyStoreAlias(name, KeyType.KEYPAIR_PRIVATE_KEY,getConcreteAlgorithm("RSA"), new BigInteger(modulus.toString()).bitLength(), (name.concat(modulus.toString())).hashCode() + "", privkey.getClass().getName());
    	setPrivateAlias(privateAlias);
    	
    	KeyStoreManager.getInstance().addKeyPair(privkey, CertificateFactory.createJCrypToolCertificate(pubkey), password, privateAlias, publicAlias);
    	KeyStoreManager.getInstance().addCertificate(CertificateFactory.createJCrypToolCertificate(pubkey), publicAlias);
    }
    
    /**
     * method to create a multi-prime RSA-key with chosen parameters
     * @param name of the alias
     * @param password for the private key
     * @param primeNumber number of used primes
     * @param modulus the N
     * @param firstPrime p
     * @param secondPrime q
     * @param thirdPrime r
     * @param fourthPrime s
     * @param fifthPrime t
     * @param pubExponent e
     * @param privExponent d
     */
    public void saveMpRSAKeyToKeystore(final String name, final String password, final int primeNumber, final BigInteger modulus, final BigInteger firstPrime, final BigInteger secondPrime, final BigInteger thirdPrime, final BigInteger fourthPrime, final BigInteger fifthPrime, final BigInteger pubExponent, final BigInteger privExponent) {
    	if (name == null || password == null || primeNumber < 3 || primeNumber > 5 ||modulus.compareTo(BigInteger.ZERO) <= 0 || firstPrime.compareTo(BigInteger.ZERO) <= 0 || secondPrime.compareTo(BigInteger.ZERO) <= 0 || thirdPrime.compareTo(BigInteger.ZERO) <= 0|| pubExponent.compareTo(BigInteger.ZERO) <= 0 || privExponent.compareTo(BigInteger.ZERO) <= 0){
    		throw new IllegalArgumentException("Error: One of the given parameters is not valid!");
    	}
    	switch (primeNumber) {
		case 4:
			if (thirdPrime.compareTo(BigInteger.ZERO) <= 0 || fourthPrime.compareTo(BigInteger.ZERO) <= 0 ){
				throw new IllegalArgumentException("Error: One of the given parameters is not valid!");
			}
			break;
		case 5:
			if (thirdPrime.compareTo(BigInteger.ZERO) <= 0 || fourthPrime.compareTo(BigInteger.ZERO) <= 0 || fifthPrime.compareTo(BigInteger.ZERO) <= 0){
				throw new IllegalArgumentException("Error: One of the given parameters is not valid!");
			}
			break;
		}
    	
    	final FlexiBigInt n = new FlexiBigInt(modulus), e = new FlexiBigInt(pubExponent);
    	final RSAPublicKey pubkey = new RSAPublicKey(n, e);

    	final KeyStoreAlias publicAlias = new KeyStoreAlias(name, KeyType.KEYPAIR_PUBLIC_KEY, "MpRSA", new BigInteger(modulus.toString()).bitLength(), (name.concat(modulus.toString())).hashCode() + "", pubkey.getClass().getName());
    	setPublicAlias(publicAlias);
    	
    	RSAOtherPrimeInfo[] otherPrimeInfo = null;
    	
    	final FlexiBigInt r = new FlexiBigInt(thirdPrime);
    	final RSAOtherPrimeInfo rR = new RSAOtherPrimeInfo(r, FlexiBigInt.ZERO, FlexiBigInt.ZERO);
    	
    	final FlexiBigInt s = new FlexiBigInt(fourthPrime);
    	final RSAOtherPrimeInfo sS = new RSAOtherPrimeInfo(s, FlexiBigInt.ZERO, FlexiBigInt.ZERO);

    	final FlexiBigInt t = new FlexiBigInt(fifthPrime);
    	final RSAOtherPrimeInfo tT = new RSAOtherPrimeInfo(t, FlexiBigInt.ZERO, FlexiBigInt.ZERO);
    	
    	if(primeNumber == 3){
    		otherPrimeInfo = new RSAOtherPrimeInfo[]{rR};
    	}        
    	if(primeNumber == 4){
    		otherPrimeInfo = new RSAOtherPrimeInfo[]{rR, sS};
    	}      
    	if(primeNumber == 5){
    		otherPrimeInfo = new RSAOtherPrimeInfo[]{rR, sS, tT};
    	}
    	
    	final MpRSAPrivateKey privkey = new MpRSAPrivateKey(n, e, new FlexiBigInt(privExponent), new FlexiBigInt(firstPrime), new FlexiBigInt(secondPrime), FlexiBigInt.ZERO, FlexiBigInt.ZERO, FlexiBigInt.ZERO, otherPrimeInfo);
    	final KeyStoreAlias privateAlias = new KeyStoreAlias(name, KeyType.KEYPAIR_PRIVATE_KEY,"MpRSA", new BigInteger(modulus.toString()).bitLength(), (name.concat(modulus.toString())).hashCode() + "", privkey.getClass().getName());
    	setPrivateAlias(privateAlias);
    	
    	KeyStoreManager.getInstance().addKeyPair(privkey, CertificateFactory.createJCrypToolCertificate(pubkey), password, privateAlias, publicAlias);
    	KeyStoreManager.getInstance().addCertificate(CertificateFactory.createJCrypToolCertificate(pubkey), publicAlias);
    }
    
    
    /**
     * gets the OID to a certain algorithm
     * @param algorithm the algorithm, you want to know the OID from
     * @return the OID
     */
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
            		return algorithm+Messages.IdentityManager_11 + current.getOID().getStringOID() + Messages.IdentityManager_12;
            	}
            	else{
            		return algorithm;
            	}
            }
        }
        return algorithm;
    }
	/**
	 * method to get all available contacts
	 * @return the contacts in a Vector<String>
	 */
	public Vector<String> getContacts(){ 
		Vector<String> contactNames = new Vector<String>();             
		Iterator<Contact> it = ContactManager.getInstance().getContacts();
		Contact contactNode;
        
        while (it.hasNext()) {
            contactNode = it.next();
            if (!contactNames.contains(contactNode.getName())){
            	contactNames.add(contactNode.getName());
            }
        }
        
        return contactNames;
	}
	/**
	 * get all asymmetric algorithms of an identity
	 * @param identity the identity
	 * @return a Vector<String> with the algorithm-names
	 */
	public Vector<String>getAsymmetricKeyAlgorithms(String identity){
		Vector<String> keyAlgos = new Vector<String>();
		KeyStoreAlias localKeyStoreAlias = null;
        try {
			aliases = KeyStoreManager.getInstance().getAliases();
		} catch (KeyStoreException e) {
			LogUtil.logError(e);
		}
        
        while (aliases.hasMoreElements()) {
            localKeyStoreAlias = new KeyStoreAlias(aliases.nextElement());
            
            if (localKeyStoreAlias.getClassName().equals(RSAPublicKey.class.getName())&&localKeyStoreAlias.getContactName().equals(identity)) {
//            	keyAlgos.add(KeyStoreManager.getInstance().getKey(localKeyStoreAlias).getAlgorithm());
            	keyAlgos.add(localKeyStoreAlias.getOperation());
            }
        }
        return keyAlgos;
	}
	
	
	public int countOwnKeys(String identity){
		int count = 0;
		KeyStoreAlias alias = null;
        try {
			aliases = KeyStoreManager.getInstance().getAliases();
			while (aliases != null && aliases.hasMoreElements()) {
                alias = new KeyStoreAlias(aliases.nextElement());

                if ((alias.getClassName().equals(RSAPublicKey.class.getName())||alias.getClassName().equals(RSAPrivateCrtKey.class.getName())) && alias.getContactName().equals(identity)) {
                	count++;
                }
            }
		} catch (KeyStoreException e) {
			LogUtil.logError(e);
		}
       
       return count;
	}
	
	
	
	/**
	 * Method to get publicKeys for a certain identity
	 * @param identity specifies the identity looking for public keys
	 */
	public HashMap<String, KeyStoreAlias> getPublicKeys(String identity){
		HashMap<String, KeyStoreAlias> pubkeys = new HashMap<String, KeyStoreAlias>();
		KeyStoreAlias alias = null;
        try {
			aliases = KeyStoreManager.getInstance().getAliases();
			while (aliases != null && aliases.hasMoreElements()) {
                alias = new KeyStoreAlias(aliases.nextElement());

                if (alias.getClassName().equals(RSAPublicKey.class.getName())&&alias.getContactName().equals(identity)) {
                	if (!keymgmt.containsKey(alias.getHashValue())){
                		keyID = keymgmt.size()+1;
                		keymgmt.put(alias.getHashValue(),keyID);
                	}else{
                		keyID = keymgmt.get(alias.getHashValue());
                	}
                	String[] operation = alias.getOperation().split(Messages.IdentityManager_13);
                	//for self-made rsa-keys.. no operation is available
                	if (operation[0].length() == 0){
                		operation[0] =Messages.IdentityManager_14;
                	}
                	pubkeys.put(alias.getContactName() + Messages.IdentityManager_15 + alias.getKeyLength() + Messages.IdentityManager_16+ operation[0]+Messages.IdentityManager_17+keyID,alias);
                }
            }
		} catch (KeyStoreException e) {
			LogUtil.logError(e);
		}
       
       return pubkeys;
	}
	
	/**
	 * Method to get little publicKeys to attack for a certain identity
	 * @param identity specifies the identity looking for public keys
	 */
	public HashMap<String, KeyStoreAlias> getAttackablePublicKeys(String identity){
		HashMap<String, KeyStoreAlias> attackPubkeys = new HashMap<String, KeyStoreAlias>();
		KeyStoreAlias alias = null;
        try {
			aliases = KeyStoreManager.getInstance().getAliases();
			int count = 1;
			while (aliases != null && aliases.hasMoreElements()) {
                alias = new KeyStoreAlias(aliases.nextElement());
                if (alias.getClassName().equals(RSAPublicKey.class.getName()) && !alias.getContactName().equals(identity)) {
                	attackPubkeys.put(alias.getContactName() + Messages.IdentityManager_18 + alias.getKeyLength() + Messages.IdentityManager_19+ alias.getClassName().substring(alias.getClassName().lastIndexOf('.')+1)+Messages.IdentityManager_20+count,alias);
                	count++;
                }
            }
		} catch (KeyStoreException e) {
			LogUtil.logError(e);
		}
       
       return attackPubkeys;
	}
	
	/**
	 * get all keys for an identity
	 * @param identity the identity
	 * @return HashMap<String, KeyStoreAlias>
	 */
	public TreeMap<String, KeyStoreAlias>loadAllKeysForIdentityAndOtherPublics(String identity){
		TreeMap<String, KeyStoreAlias> keys = new TreeMap<String, KeyStoreAlias>();
		KeyStoreAlias alias = null;
        try {
			aliases = KeyStoreManager.getInstance().getAliases();
			while (aliases != null && aliases.hasMoreElements()) {
                alias = new KeyStoreAlias(aliases.nextElement());
                //load RSA public and private keys for a certain identity and all other public keys for other identities
                if (((alias.getClassName().equals(RSAPublicKey.class.getName())||alias.getClassName().equals(RSAPrivateCrtKey.class.getName())||alias.getClassName().equals(MpRSAPrivateKey.class.getName())) && alias.getContactName().equals(identity))||alias.getClassName().equals(RSAPublicKey.class.getName()) && !alias.getContactName().equals(identity)) {
                	if (!allKeysForID.containsKey(alias.getHashValue())){
                		genKeyID = allKeysForID.size()+1;
                		allKeysForID.put(alias.getHashValue(),genKeyID);
                	}else{
                		genKeyID = allKeysForID.get(alias.getHashValue());
                	}
                	
                	keys.put(Messages.IdentityManager_21+genKeyID+Messages.IdentityManager_22+alias.getContactName() + Messages.IdentityManager_23 + alias.getKeyLength() + Messages.IdentityManager_24+ alias.getClassName().substring(alias.getClassName().lastIndexOf('.')+1),alias);
                }
            }
			
		} catch (KeyStoreException e) {
			LogUtil.logError(e);
		}
        
       return keys;
	}
	/**
	 * get all public key parameters of an RSA key
	 * @param alias the alias for the key
	 * @return the parameters in an Vector<String> in the following order: algorithm, format, e, N
	 */
	public Vector<String> getAllRSAPubKeyParameters(KeyStoreAlias alias){
		Vector<String> parameters = new Vector<String>();
		final RSAPublicKey pubkey = (RSAPublicKey) KeyStoreManager.getInstance().getPublicKey(alias).getPublicKey();
		
		parameters.add(pubkey.getAlgorithm());
		parameters.add(pubkey.getFormat());
		parameters.add(pubkey.getE().toString());
		parameters.add(pubkey.getModulus().toString());
		
		return parameters;
	}
	
	/**
	 * get all private key parameters of an RSA key
	 * @param alias the alias for the key
	 * @param password the password for the key
	 * @return the parameters in an Vector<String> in the following order: algorithm, format, p, q, d, e, N
	 */
	public Vector<String> getAllRSAPrivKeyParameters(KeyStoreAlias alias, String password){
		Vector<String> parameters = new Vector<String>();
		final RSAPrivateCrtKey privKey = getPrivateKey(alias, password);
		
		if (privKey != null){
			parameters.add(privKey.getAlgorithm());
			parameters.add(privKey.getFormat());
			parameters.add(privKey.getP().toString());
			parameters.add(privKey.getQ().toString());
			parameters.add(privKey.getD().toString());
			parameters.add(privKey.getE().toString());
			parameters.add(privKey.getN().toString());
		}
		return parameters;
	}
	
	/**
	 * get all private key parameters of an MULTI-PRIME! RSA key
	 * @param alias the alias for the key
	 * @param password the password for the key
	 * @return the parameters in an Vector<String> in the following order: algorithm, format, p, q, other Primes, d, e, N
	 */
	public Vector<String> getAllMpRSAPrivKeyParameters(KeyStoreAlias alias, String password){
		Vector<String> parameters = new Vector<String>();
		StringBuilder sb = new StringBuilder();
		
		final MpRSAPrivateKey privKey = getMpPrivateKey(alias, password);
		if (privKey != null){
			parameters.add(privKey.getAlgorithm());
			parameters.add(privKey.getFormat());
			parameters.add(privKey.getP().toString());
			parameters.add(privKey.getQ().toString());
			RSAOtherPrimeInfo[] otherPrimeIinfo = privKey.getOtherPrimeInfo();
			for (int i = 0; i < otherPrimeIinfo.length; i++){
				sb.append(otherPrimeIinfo[i].getPrime()+Messages.IdentityManager_25);
			}
			parameters.add(sb.toString());
			//other primes
			parameters.add(privKey.getD().toString());
			parameters.add(privKey.getE().toString());
			parameters.add(privKey.getN().toString());
		}
		return parameters;
	}
	
	
	/**
	 * get N and e of the public key
	 * @param alias The alias who wants to get his publicKey
	 * @return parameters: contains the publicKey Parameters. The first parameter is 'N', the second 'e'
	 */
	public Vector<BigInteger>getPublicKeyParameters(KeyStoreAlias alias){
		Vector<BigInteger> parameters = new Vector<BigInteger>();

		final RSAPublicKey pubkey = (RSAPublicKey) KeyStoreManager.getInstance().getPublicKey(alias).getPublicKey();
		parameters.add(pubkey.getModulus());
		parameters.add(pubkey.getPublicExponent());
		return parameters;
	}
	
	public RSAPublicKey getRSAPublicKey(KeyStoreAlias alias){
		final RSAPublicKey pubkey = (RSAPublicKey) KeyStoreManager.getInstance().getPublicKey(alias).getPublicKey();
		return pubkey;
	}
	
	public RSAPrivateCrtKey getPrivateKey(KeyStoreAlias privAlias, String password){
        PrivateKey key = null;
        RSAPrivateCrtKey privkey = null;
		try {
			key = KeyStoreManager.getInstance().getPrivateKey(privAlias, password.toCharArray());
			privkey = (RSAPrivateCrtKey) key;
		} catch (Exception e) {
			LogUtil.logError(e);
		}

		return privkey;
	}
	
	
	public MpRSAPrivateKey getMpPrivateKey(KeyStoreAlias privAlias, String password){
        PrivateKey key = null;
        MpRSAPrivateKey privkey = null;
		try {
			key = KeyStoreManager.getInstance().getPrivateKey(privAlias, password.toCharArray());
			privkey = (MpRSAPrivateKey) key;
		} catch (Exception e) {
			LogUtil.logError(e);
		}

		return privkey;
	}
	
	
	/**
	 * get the privateKey parameters
	 * @param privkey contains the privateKey parameters 
	 * @return vector with parameters in the following order: N, d, p, q, e
	 */
	public Vector<BigInteger> getPrivateKeyParametersRSA(RSAPrivateCrtKey privkey){
		Vector <BigInteger> privKeyValues = new Vector<BigInteger>();
	
        privKeyValues.add(privkey.getModulus());
        privKeyValues.add(privkey.getD().bigInt);
        privKeyValues.add(privkey.getP().bigInt);
        privKeyValues.add(privkey.getQ().bigInt);
        privKeyValues.add(privkey.getPublicExponent());
        
        return privKeyValues;
	}

	public HashMap<String, KeyStoreAlias> getPrivateKeys(String identityName) {
		HashMap<String, KeyStoreAlias> keyStoreItems = new HashMap<String, KeyStoreAlias>();
        try {
            KeyStoreAlias alias;
            for (Enumeration<String> aliases = KeyStoreManager.getInstance().getAliases(); aliases.hasMoreElements();) {
                alias = new KeyStoreAlias(aliases.nextElement());
                if ((alias.getClassName().equals(RSAPrivateCrtKey.class.getName())||(alias.getClassName().equals(MpRSAPrivateKey.class.getName()))) && alias.getContactName().equals(identityName)) {
                	if (!privKeymgmt.containsKey(alias.getHashValue())){
                		privKeyID = privKeymgmt.size()+1;
                		privKeymgmt.put(alias.getHashValue(),privKeyID);
                	}else{
                		privKeyID = privKeymgmt.get(alias.getHashValue());
                	}
                    keyStoreItems.put(alias.getContactName() + Messages.IdentityManager_26 + alias.getKeyLength() + Messages.IdentityManager_27+ alias.getClassName().substring(alias.getClassName().lastIndexOf('.')+1)+Messages.IdentityManager_28+privKeyID,alias);
                }
            }
        } catch (KeyStoreException e) {
            LogUtil.logError(e);
        }
        
		return keyStoreItems;
	}
	
	public KeyStoreAlias getPublicForPrivateRSA(KeyStoreAlias privAlias){
        Enumeration<String> aliases;
        try {
            aliases = KeyStoreManager.getInstance().getAliases();
        } catch (KeyStoreException e) {
            LogUtil.logError(e);
            return null;
        }
        KeyStoreAlias alias;
        while (aliases != null && aliases.hasMoreElements()) {
            alias = new KeyStoreAlias(aliases.nextElement());
            if (alias.getHashValue().equalsIgnoreCase(privAlias.getHashValue()) && alias != privAlias)
                return alias;
        }
        return null;
	}
       
}

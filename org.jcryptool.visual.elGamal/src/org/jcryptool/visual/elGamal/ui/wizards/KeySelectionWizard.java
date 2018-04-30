// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----

package org.jcryptool.visual.elGamal.ui.wizards;

import java.math.BigInteger;
import java.security.UnrecoverableKeyException;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.certificates.CertificateFactory;
import org.jcryptool.crypto.keystore.keys.KeyType;
import org.jcryptool.visual.elGamal.Action;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.ElGamalPlugin;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.LoadKeypairPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.LoadPublicKeyPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.NewChooseKeyTypePage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.NewKeypairPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.NewPublicKeyPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.SaveKeypairPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.SavePublicKeyPage;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.core.elgamal.ElGamalKeyFactory;
import de.flexiprovider.core.elgamal.ElGamalPrivateKey;
import de.flexiprovider.core.elgamal.ElGamalPrivateKeySpec;
import de.flexiprovider.core.elgamal.ElGamalPublicKey;
import de.flexiprovider.core.elgamal.ElGamalPublicKeySpec;

/**
 * wizard for key selection and creation.
 * 
 * A revised version of KeySelectionWizard.
 * 
 * @author Thorben Groos
 *
 */
public class KeySelectionWizard extends Wizard {
	
	private ElGamalData data;
	
	public KeySelectionWizard(ElGamalData data) {
		
		if (data == null) {
			this.data = new ElGamalData(Action.EncryptAction);
			this.data.setStandalone(true);
		} else {
			this.data = data;
		}
		setWindowTitle(Messages.KeySelectionWizard_keyselection);
	}
	
	@Override
	public void addPages() {

		//When the action is Decrypt or Sign just load the keypair pages for performance reasons.
		addPage(new NewChooseKeyTypePage(data));
		
		switch (data.getAction()) {
		default:
		case EncryptAction:
		case VerifyAction:
			addPage(new NewPublicKeyPage(data));
			addPage(new LoadPublicKeyPage(data));
			addPage(new SavePublicKeyPage(data));
		case DecryptAction:
		case SignAction:
			addPage(new NewKeypairPage(data));
			addPage(new LoadKeypairPage(data));
			addPage(new SaveKeypairPage(data));
			break;
		}
	}

	@Override
	public boolean performFinish() {
		
		boolean result = true;
		
		int radiobutton = ((NewChooseKeyTypePage) getPage("NewChooseKeyTypePage")).getSelection();
		
		switch (data.getAction()) {
		case EncryptAction:
		case VerifyAction:
			
			//Create new Public Key
			if (radiobutton == 0) {
				BigInteger modulus = ((NewPublicKeyPage) getPage("New Public Key Page")).getModulus();
				BigInteger generator = ((NewPublicKeyPage) getPage("New Public Key Page")).getGenerator();
				BigInteger publicB = ((NewPublicKeyPage) getPage("New Public Key Page")).getExponentB();
				
				data.setModulus(modulus);
				data.setGenerator(generator);
				data.setPublicA(publicB);
				
				if (((NewPublicKeyPage) getPage("New Public Key Page")).wantSave()) {
					String contactName = ((SavePublicKeyPage) getPage("Save Public Key Page")).getOwner();
					result = saveKey(contactName, null, modulus, generator, publicB, null);
				}
			}
			
			//Load Public Key
			if (radiobutton == 1) {
				KeyStoreAlias publicAlias = ((LoadPublicKeyPage) getPage("Load Public Key Page")).getPublicAlias();
				data.setPublicAlias(publicAlias);
				data.setContactName(publicAlias.getContactName());
				
				result = loadParametersToDataObject(publicAlias, null);
			}
			
		case DecryptAction:
		case SignAction:
			
			//Create new keypair
			if (radiobutton == 2) {
				
				BigInteger modulus = ((NewKeypairPage) getPage("New Keypair Page")).getModulus();
				BigInteger generator = ((NewKeypairPage) getPage("New Keypair Page")).getGenerator();
				BigInteger publicB = ((NewKeypairPage) getPage("New Keypair Page")).getExponentB();
				BigInteger privateB = ((NewKeypairPage) getPage("New Keypair Page")).getPrivateb();
				
				data.setModulus(modulus);
				data.setGenerator(generator);
				data.setPublicA(publicB);
				data.setA(privateB);
				
				if (((NewKeypairPage) getPage("New Keypair Page")).wantSave()) {
					
					String contactName = ((SaveKeypairPage) getPage("Save Keypair Page")).getOwner();
					String password = ((SaveKeypairPage) getPage("Save Keypair Page")).getPassword();
					
					result = saveKey(contactName, password, modulus, generator, publicB, privateB);
				}	
			}
			
			//Load keypair
			if (radiobutton == 3) {
				KeyStoreAlias privateAlias = ((LoadKeypairPage) getPage("Load Keypair Page")).getPrivateAlias();
				KeyStoreAlias publicAlias = ((LoadKeypairPage) getPage("Load Keypair Page")).getPublicAlias();
				String password = ((LoadKeypairPage) getPage("Load Keypair Page")).getPassword();
				
				data.setPrivateAlias(privateAlias);
				data.setContactName(privateAlias.getContactName());
				data.setPublicAlias(publicAlias);
				data.setPassword(password);
				
				result = loadParametersToDataObject(privateAlias, password);
			}
			break;

		default:
			break;
		}
		
		return result;
	}
	
	/**
	 * Set the modulus, generator, publicB and privateB (only if it is a private Key) to
	 * the ElGamalData object.
	 * @param keyStoreAlias The keyStoreAlias from the LoadPublicKeyPage or the LoadKeypairPage
	 * @param password The password the user has entered on the LodKeyPairPage. If it is a publicKey it is 
	 * 		does not matter what this argument is set to (Simply use null).
	 * @return true if anything has worked, else false.
	 */
	private boolean loadParametersToDataObject(KeyStoreAlias keyStoreAlias, String password) {
		try {
			KeyStoreManager ksm = KeyStoreManager.getInstance();
			
			if (keyStoreAlias.getKeyStoreEntryType() == KeyType.KEYPAIR_PUBLIC_KEY) {
				ElGamalPublicKey publicKey = (ElGamalPublicKey) ksm.getCertificate(keyStoreAlias).getPublicKey();
				
				data.setModulus(publicKey.getModulus().bigInt);
				data.setGenerator(publicKey.getGenerator().bigInt);
				data.setPublicA(publicKey.getPublicA().bigInt);
			}

			if (keyStoreAlias.getKeyStoreEntryType() == KeyType.KEYPAIR_PRIVATE_KEY) {
				ElGamalPrivateKey privateKey = (ElGamalPrivateKey) ksm.getPrivateKey(keyStoreAlias, password.toCharArray());
				
				data.setModulus(privateKey.getModulus().bigInt);
				data.setGenerator(privateKey.getGenerator().bigInt);
				data.setPublicA(privateKey.getPublicA().bigInt);
				data.setA(privateKey.getA().bigInt);
			}
			
		} catch (UnrecoverableKeyException uke) {
			((LoadKeypairPage) getPage("Load Keypair Page")).setPasswordHint(true);
			return false;
		} catch (Exception e) {
			LogUtil.logError(ElGamalPlugin.PLUGIN_ID, e);
			return false;
		} 
		return true;
	}
	
	/**
	 * Save a Key in the keystore
	 * @param contactName
	 * @param password If it is a public key set the argument null
	 * @param modulus
	 * @param generator
	 * @param publicB
	 * @param privateB if it is a public key set this argument null
	 * @return True, if the key was sucessfully saved in the keystore, false if not.
	 */
	private boolean saveKey(String contactName, String password, BigInteger modulus, BigInteger generator, BigInteger publicB, BigInteger privateB) {
		KeyStoreManager ksm = KeyStoreManager.getInstance();
		try {
			ElGamalKeyFactory keyFactory = new ElGamalKeyFactory();
			
			FlexiBigInt flexiModulus = new FlexiBigInt(modulus);
			FlexiBigInt flexiGenerator = new FlexiBigInt(generator);
			FlexiBigInt flexiPublicB = new FlexiBigInt(publicB);
			
			ElGamalPublicKey publicKey = (ElGamalPublicKey) keyFactory.generatePublic(new ElGamalPublicKeySpec(flexiModulus, flexiGenerator, flexiPublicB));
		
			KeyStoreAlias publicAlias = new KeyStoreAlias(contactName, KeyType.KEYPAIR_PUBLIC_KEY, "", modulus.bitLength(), Integer.toString(contactName.concat(modulus.toString()).hashCode()), publicKey.getClass().getName());
			
			//If a private key should be saved
			if (privateB != null) {
				
				FlexiBigInt flexiPrivateB = new FlexiBigInt(privateB);
				
				ElGamalPrivateKey privateKey = (ElGamalPrivateKey) keyFactory.generatePrivate(new ElGamalPrivateKeySpec(flexiModulus, flexiGenerator, flexiPublicB, flexiPrivateB));
				KeyStoreAlias privateAlias = new KeyStoreAlias(contactName, KeyType.KEYPAIR_PRIVATE_KEY, "", modulus.bitLength(), Integer.toString(contactName.concat(modulus.toString()).hashCode()), privateKey.getClass().getName());
				
				//Save the keypair
				ksm.addKeyPair(privateKey, CertificateFactory.createJCrypToolCertificate(publicKey), password.toCharArray(), privateAlias, publicAlias);
			
				//save the key in the data object
				data.setPassword(password);
				data.setPrivateAlias(privateAlias);
				data.setContactName(contactName);
			
			} else {
				//only save the public key
				ksm.addCertificate(CertificateFactory.createJCrypToolCertificate(publicKey), publicAlias);
				
				//save the key in the data object
				data.setPublicAlias(publicAlias);
				data.setContactName(contactName);
			}
		} catch (Exception e) {
			LogUtil.logError(ElGamalPlugin.PLUGIN_ID, e);
			return false;
		}
		return true;
	}
	
	
	
	@Override
	public boolean canFinish() {
		
		int radiobutton = ((NewChooseKeyTypePage) getPage("NewChooseKeyTypePage")).getSelection();
		
		boolean canFinish = false;
		switch (data.getAction()) {
		case EncryptAction:
		case VerifyAction:
			
			//Create new public Key
			if (radiobutton == 0) {
				if ((getPage("New Public Key Page").isPageComplete() && !((NewPublicKeyPage) getPage("New Public Key Page")).wantSave())
						|| getPage("Save Public Key Page").isPageComplete()) {
					canFinish = true;
				} else {
					canFinish = false;
				}
			}
			
			//Load public Key
			if (radiobutton == 1) {
				if (getPage("Load Public Key Page").isPageComplete()) {
					canFinish = true;
				} else {
					canFinish = false;
				}
			}
			
		case DecryptAction:
		case SignAction:
			
			//Create new keypair
			if (radiobutton == 2) {
				if ((getPage("New Keypair Page").isPageComplete() && !((NewKeypairPage) getPage("New Keypair Page")).wantSave())
						|| getPage("Save Keypair Page").isPageComplete()) {
					canFinish = true;
				} else {
					canFinish = false;
				}
			}
			
			//Load keypair
			if (radiobutton == 3) {
				if (getPage("Load Keypair Page").isPageComplete()) {
					canFinish = true;
				} else {
					canFinish = false;
				}
			}
			
			break;
			
		default:
			canFinish = false;
			break;
		}
		return canFinish;
	}
}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.rsa;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.ui.textblockloader.conversion.ConversionStringToBlocks;

import de.flexiprovider.core.rsa.RSAPrivateCrtKey;

/**
 * data class for exchanging and saving parameters between steps, wizards &co.
 *
 * @author Michael Gaber
 */
public class RSAData {

    /** the action to which this data object belongs */
    private final Action action;

    /**
     * constructor setting the action
     *
     * @param action the action for this object
     */
    public RSAData(final Action action) {
        this.action = action;
    }

    /** name of the Contact specified. */
    private String contactName;

    /**
     * getter for the contact name
     *
     * @return the contactName
     */
    public final String getContactName() {
        return contactName;
    }

    /**
     * setter for the contact name
     *
     * @param contactName the contactName to set
     */
    public final void setContactName(final String contactName) {
        this.contactName = contactName;
    }

    /** current ciphertext for decrypting. */
    private List<Integer> cipherText;

    /** private exponent. */
    private BigInteger d;

    /** public exponent. */
    private BigInteger e;

    /** Modulo. */
    private BigInteger N;

    /** first factor. */
    private BigInteger p;

    /** password for the private key. */
    private String password;

    /** plaintext for encryption or signing. */
    private List<Integer> plainText;

    /** second factor. */
    private BigInteger q;

    /** signature of a text. */
    private List<Integer> signature;

    /** the private key alias. */
    private KeyStoreAlias privateAlias;

    /** the public key alias. */
    private KeyStoreAlias publicAlias;

    /** whether the simple hash algorithm or sha-1 was and should be used. */
    private boolean simpleHash = true;

    /** if this data-object belongs to a standalone key generation wizard. */
    private boolean standalone;
    
    private boolean randomPlaintext;
    
    private boolean randomKey;

	private List<Integer> plainTextAsNumbers;

	private List<Integer> cipherTextAsNumbers;

	private List<Integer> signatureAsNumbers;

	private ConversionStringToBlocks plainTextConversion;

	private List<Integer> tempAsNumbers;

    /**
     * setter for the standalone property
     *
     * @param standalone the standalone to set
     */
    public final void setStandalone(final boolean standalone) {
        this.standalone = standalone;
    }
    
    /**
     * getter for randomPlaintext.
     *
     * @return the randomPlaintext
     */
    public final boolean getrandomPlaintext() {
//        return this.randomPlaintext;
    	return randomPlaintext;
    }
    
    /**
     * getter for randomPlaintext.
     *
     * @return the randomPlaintext
     */
    public final boolean getrandomKey() {
//        return this.randomKey;
    	return randomKey;
    }
    
//    /**
//     * getter for temp.
//     *
//     * @return the temp
//     */
//    public final String getTemp() {
//        if (this.temp == null) {
//            return ""; //$NON-NLS-1$
//        } else {
//            return this.temp;
//        }
//    }
    
    public List<Integer> getTempAsNumbers() {
		return tempAsNumbers;
	}
    
    /**
     * getter for the ciphertext.
     *
     * @return the cipherText
     */
    public final String getCipherText_Old() {
//        if (this.cipherText == null) {
    	if (cipherText == null) {
            return ""; //$NON-NLS-1$
        } else {
//            return this.cipherText;
        	return null;
        }
    }

    /**
     * getter for d.
     *
     * @return the d
     */
    public final BigInteger getD() {
//        return this.d;
    	return d;
    }

    /**
     * getter for e.
     *
     * @return the e
     */
    public final BigInteger getE() {
//        return this.e;
    	return e;
    }

    /**
     * getter for n.
     *
     * @return the n
     */
    public final BigInteger getN() {
//        return this.N;
    	return N;
    }

    /**
     * getter for p.
     *
     * @return the p
     */
    public final BigInteger getP() {
//        return this.p;
    	return p;
    }

    /**
     * getter for the password.
     *
     * @return the password
     */
    public final String getPassword() {
//        return this.password;
    	return password;
    }

    /**
     * getter for the plaintext.
     *
     * @return the plainText
     */
    public final String getPlainText_Old() {
//        if (this.plainText == null) {
    	if (plainText == null) {
            return ""; //$NON-NLS-1$
        } else {
//            return this.plainText;
        	return null;
        }
    }

    /**
     * getter for q.
     *
     * @return the q
     */
    public final BigInteger getQ() {
        return q;
    }

    /**
     * getter for the signature.
     *
     * @return the signature
     */
    public final String getSignature_Old() {
//        if (this.signature == null) {
    	if (signature == null) {
            return ""; //$NON-NLS-1$
        } else {
//            return this.signature;
        	return null;
        }
    }

    /**
     * setter for randomPlaintext.
     *
     * @param the randomPlaintext
     */
    public final void setrandomPlaintext(final boolean randomPlaintext) {
        this.randomPlaintext = randomPlaintext;
    }
    
    /**
     * getter for randomPlaintext.
     *
     * @return the randomPlaintext
     */
    public final void setrandomKey(final boolean randomKey) {
        this.randomKey = randomKey;
    }
    
    public void setTempAsNumbers(List<Integer> temp) {
		tempAsNumbers = temp;
	}
    
    /**
     * setter for the ciphertext.
     *
     * @param cipherText the cipherText to set
     */
    public final void setCipherText_Old(final String cipherText) {
//        this.cipherText = cipherText;
        this.cipherText = null;
    }

    /**
     * setter for d.
     *
     * @param d the d to set
     */
    public final void setD(final BigInteger d) {
        this.d = d;
    }

    /**
     * setter for e.
     *
     * @param e the e
     */
    public final void setE(final BigInteger e) {
        this.e = e;
    }

    /**
     * setter for n.
     *
     * @param n the n to set
     */
    public final void setN(final BigInteger n) {
        this.N = n;
    }

    /**
     * setter for p.
     *
     * @param p the p
     */
    public final void setP(final BigInteger p) {
        this.p = p;
    }

    /**
     * setter for the password.
     *
     * @param password the password to set
     */
    public final void setPassword(final String password) {
        this.password = password;
    }

    /**
     * setter for the plaintext.
     *
     * @param plainText the plainText to set
     */
    public final void setPlainText_Old(final String plainText) {
//        this.plainText = plainText;
        this.plainText = null;
    }

    /**
     * setter for q.
     *
     * @param q the q
     */
    public final void setQ(final BigInteger q) {
        this.q = q;
    }

    /**
     * setter for the signature.
     *
     * @param signature the signature to set
     */
    public final void setSignature_Old(final String signature) {
//        this.signature = signature;
        this.signature = null;
    }

    /**
     * getter for the private alias of the contact
     *
     * @return the privateAlias
     */
    public final KeyStoreAlias getPrivateAlias() {
//        return this.privateAlias;
    	return privateAlias;
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
     * getter for the public alias for the contact
     *
     * @return the publicAlias
     */
    public final KeyStoreAlias getPublicAlias() {
//        return this.publicAlias;
    	return publicAlias;
    }

    /**
     * getter for {@link #simpleHash}.
     *
     * @return the simpleHash
     */
    public final boolean getSimpleHash() {
//        return this.simpleHash;
    	return simpleHash;
    }

    /**
     * setter for simpleHash.
     *
     * @param simpleHash the simpleHash
     */
    public final void setSimpleHash(final boolean simpleHash) {
        this.simpleHash = simpleHash;
    }

    /**
     * @return if this belongs to a standalone keygeneration wizard
     */
    public final boolean isStandalone() {
//        return this.standalone;
    	return standalone;
    }

    /**
     * getter for the type of action
     *
     * @return the current action
     */
    public final Action getAction() {
//        return this.action;
    	return action;
    }

    /**
     * inherits the data from an other data object into this one. used for copying data between pages
     *
     * @param oldData the old data object from which data is copied
     */
    public void inherit(final RSAData oldData) {
        // this we can always get
        N = oldData.N;
        e = oldData.e;
        simpleHash = oldData.simpleHash;
        // only available if key was saved, but we might have a problem
        // otherwise anyway.
        publicAlias = oldData.publicAlias;
        contactName = oldData.contactName;
        if (oldData.randomKey){
            d = oldData.d;
            p = oldData.p;
            q = oldData.q;
        }
        // sometimes we need everything
        else if (action == Action.DecryptAction || action == Action.SignAction) {
            // easy if the other action already has everything we need
            if (oldData.action == Action.DecryptAction || oldData.action == Action.SignAction) {
                d = oldData.d;
                p = oldData.p;
                q = oldData.q;
                // only available if key was saved, but we might have a problem
                // otherwise anyway.
                privateAlias = oldData.privateAlias;
                password = oldData.password;
            } else {
                if (oldData.d != null){
                	privateAlias = KeyStoreManager.getInstance().getPrivateForPublic(publicAlias);
                	// get the password via some dialog
                	final InputDialog passDialog = new InputDialog(Display.getCurrent().getActiveShell(),
                        Messages.RSAData_inherit_password_title, Messages.RSAData_inherit_password_text, "", null); //$NON-NLS-1$
                	if (passDialog.open() == Window.OK) {
                		password = passDialog.getValue();
                	} else {
                		return;
                	}
                	
                	try {
                		getPrivateParams();
                	}
                	catch (UnrecoverableKeyException e) {
                		JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, RSAPlugin.PLUGIN_ID,
                				Messages.RSAData_ExAccessKeystorePassword, e));
                	} catch (final NullPointerException e) {
                		JCTMessageDialog.showErrorDialog(new Status(IStatus.ERROR, RSAPlugin.PLUGIN_ID,
                				e.getMessage(), e), Messages.RSAData_failedInitPrivParams);
                	} catch (final Exception e) {
                		LogUtil.logError(e);
                	}
                } else {
            		JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, RSAPlugin.PLUGIN_ID,
            				Messages.RSAData_privateKeyNull));
                        N = null;
                        e = null;
                }	
            }
        }

        cipherTextAsNumbers = oldData.cipherTextAsNumbers;
        plainTextAsNumbers = oldData.plainTextAsNumbers;
        signatureAsNumbers = oldData.signatureAsNumbers;

    }

    /**
     * tests if random key is needed
     */
    public boolean randomNeeded() {

    	switch (getAction()) {
    		case EncryptAction: {
//    			if (this.N == null || this.e == null) {
    			if (N == null || e == null) {
    				return true;
    			}
    			break;
    		}
    		case SignAction: {
//    			if (this.N == null || this.d == null) {
    			if (N == null || d == null) {
    				return true;
    				
    			}
    			break;
    		}
		case DecryptAction:
			break;
		case VerifyAction:
			break;
		default:
			break;
    	}
    	return false;
    }
    
    /**
     * tests if plain text is needed
     */
    
    public boolean plainNeeded() {

    	if (this.plainTextAsNumbers == null) {
//    	if (plainText == null) {
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    
    /**
     * fills in random values
     */
    
    public void randomKey() {
//    	this.randomKey = true;
//    	this.N = new BigInteger("323");
//        this.e = new BigInteger("19");
//        this.d = new BigInteger("91");
//        this.p = new BigInteger("17");
//        this.q = new BigInteger("19");
    	randomKey = true;
    	N = new BigInteger("323");
        e = new BigInteger("19");
        d = new BigInteger("91");
        p = new BigInteger("17");
        q = new BigInteger("19");
        randomPlain();
    }
    
    public void randomPlain() {
//    	this.randomPlaintext = true;
//        this.simpleHash = true;
    	randomPlaintext = true;
        simpleHash = true;
        ArrayList<Integer> plain = new ArrayList<>();
        plain.add(72);
        plain.add(97);
        plain.add(108);
        plain.add(108);
        plain.add(111);
        plainTextAsNumbers = plain;
//        plainText = plain;
    }
    
    
    /**
     * gets the private key corresponding to the {@link #privateAlias} with the specified {@link #password} from the
     * keystore and extracts the private parameters from it
     *
     * @throws Exception if anything went wrong while accessing the keystore
     */
    private void getPrivateParams() throws Exception {
        final KeyStoreManager ksm = KeyStoreManager.getInstance();
        final PrivateKey key = ksm.getPrivateKey(privateAlias, password.toCharArray());
        final RSAPrivateCrtKey privkey = (RSAPrivateCrtKey) key;
//        this.N = privkey.getModulus();
//        this.d = privkey.getD().bigInt;
//        this.p = privkey.getP().bigInt;
//        this.q = privkey.getQ().bigInt;
//        this.e = privkey.getPublicExponent();
        N = privkey.getModulus();
        d = privkey.getD().bigInt;
        p = privkey.getP().bigInt;
        q = privkey.getQ().bigInt;
        e = privkey.getPublicExponent();
    }

	public void setPlainTextAsNumbers(List<Integer> loadedData) {
		plainTextAsNumbers = loadedData;
	}
	
	public List<Integer> getPlainTextAsNumbers() {
		return plainTextAsNumbers;
	}

	public void setCipherTextAsNumbers(List<Integer> loadedData) {
		cipherTextAsNumbers = loadedData;
	}
	public List<Integer> getCipherTextAsNumbers() {
		return cipherTextAsNumbers;
	}

	public void setSignatureAsNumbers(List<Integer> loadedData) {
		signatureAsNumbers = loadedData;
	}
	public List<Integer> getSignatureAsNumbers() {
		return signatureAsNumbers;
	}

	public void setPlainTextConversion(ConversionStringToBlocks blockConversion) {
		plainTextConversion = blockConversion;
	}
	public ConversionStringToBlocks getPlainTextConversion() {
		return plainTextConversion;
	}
	
	/**
	 * Simple toString() Method to visualize RSAData Objects
	 */
	@Override
	public String toString() {
		String output = "Primes:";
		output += "\tp:" + (p != null ? p.toString() : "null");
		output += "\tq:" + (q != null ? q.toString() : "null");
		output += "\nKey:";
		output += "\te:" + (e != null ? e.toString() : "null");
		output += "\td:" + (d != null ? d.toString() : "null");
		output += "\tN:" + (N != null ? N.toString() : "null");
		output += "\nplainText:\t" + (plainText != null ? plainText.toString() : "null");
		output += "\tplainTextAsNumbers:\t" + (plainTextAsNumbers != null ? plainTextAsNumbers.toString() : "null");
		output += "\ncipherText:\t" + (plainText != null ? cipherText.toString() : "null");
		output += "\tcipherTextAsNumbers:\t" + (cipherTextAsNumbers != null ? cipherTextAsNumbers.toString() : "null");
		output += "\nsignature:\t" + (signature != null ? signature.toString() : "null"); 
		output += "\tsignatureAsNumbers:\t" + (signatureAsNumbers != null ? signatureAsNumbers.toString() : "null");
		output += "\n\t\t\ttempAsNumbers:\t\t" + (tempAsNumbers != null ? tempAsNumbers.toString() : "null");
		output += "\nsimpleHash:\t" + simpleHash;
		output += "\tstandalone:\t" + standalone;
		output += "\trandomPlaintext:\t" + randomPlaintext;
		output += "\trandomKey:\t" + randomKey;
		return output;
	}
}

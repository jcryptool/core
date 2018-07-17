// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.elGamal;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.ui.textblockloader.CharsToNumbersComposite;
import org.jcryptool.crypto.ui.textblockloader.ConversionCharsToNumbers;
import org.jcryptool.crypto.ui.textblockloader.conversion.AlphabetCharsToNumbers;
import org.jcryptool.crypto.ui.textblockloader.conversion.ConversionStringToBlocks;
import org.jcryptool.crypto.ui.textblockloader.conversion.NumbersToBlocksConversion;

import de.flexiprovider.core.elgamal.ElGamalPrivateKey;

/**
 * Shared data object for everything related to the ElGamal encryption.
 *
 * @author Michael Gaber
 */
public class ElGamalData {

    /** unique parameter k */
    private BigInteger k;

    /** partial result r */
    private BigInteger r;

    /** whether this data is connected to a standalone key-generation wizard. */
    private boolean standalone;

    /** the modulus */
    private BigInteger modulus;

    /** the generator */
    private BigInteger generator;

    /** the public A */
    private BigInteger publicA;

    /** the private a */
    private BigInteger a;

    /** whether simplehash should be used */
    private boolean simpleHash;

    /** alias of the privatekey */
    private KeyStoreAlias privateAlias;

    /** the password used to secure the private key */
    private String password;

    /** Alias of the public key */
    private KeyStoreAlias publicAlias;

    /** the contact name */
    private String contactName;

    /** the unique key b */
    private BigInteger b;

    /** the current action */
    private final Action action;
    
    private ConversionStringToBlocks plainTextConversion;
    
    /** is the cipherText in number representation */
    private List<Integer> cipherTextAsNumbers;
    
    /** is the plainText in number representation */
    private List<Integer> plainTextAsNumbers;
    
    /** is the signature in number representation */
    private List<Integer> signatureAsNumbers;
    
	/**
	 * The following is for the textfields that display the entered text
	 * It is used to define to which base the text should be calculated and which blockklength is used.
	 * Here the extended ASCII is used as base and block length is 1
	 */
    private ConversionCharsToNumbers ctn = new AlphabetCharsToNumbers(CharsToNumbersComposite.ASCII_ALPHABET);
    private NumbersToBlocksConversion ntb = new NumbersToBlocksConversion(1, 1);

    private ConversionStringToBlocks stb = new ConversionStringToBlocks(ctn, ntb);

	/**
     * constructor, setting {@link #action}
     *
     * @param action the {@link Action} to set to {@link #action}
     */
    public ElGamalData(final Action action) {
        this.action = action;
        plainTextAsNumbers = new ArrayList<>();
        cipherTextAsNumbers = new ArrayList<>();
        signatureAsNumbers = new ArrayList<>();
    }

    /**
     * inherits all possible data from another data object
     *
     * @param oldData the other {@link ElGamalData} to copy data from
     */
    public boolean inherit(ElGamalData oldData) {
        publicA = oldData.publicA;
        simpleHash = oldData.simpleHash;
        modulus = oldData.modulus;
        generator = oldData.generator;
        k = oldData.k;
        r = oldData.r;
        b = oldData.b;

        publicAlias = oldData.publicAlias;
        contactName = oldData.contactName;

        //Only do if the private key is required
        if (action == Action.DecryptAction || action == Action.SignAction) {
        	//If the old data had a private key inherit the private parameters
            if (oldData.action == Action.DecryptAction || oldData.action == Action.SignAction) {
                a = oldData.a;
                privateAlias = oldData.privateAlias;
                password = oldData.password;
			} else {
				if (publicAlias != null) {
					privateAlias = KeyStoreManager.getInstance().getPrivateForPublic(publicAlias);
					if (!enterPasswordDialog()) {
						return false;
					}
				} else {
					return false;
				}
			}
        }

        cipherTextAsNumbers = oldData.cipherTextAsNumbers;
        plainTextAsNumbers = oldData.plainTextAsNumbers;
        signatureAsNumbers = oldData.signatureAsNumbers;    
        return true;
    }

    /**
     * Opens the Enter Password Dialog when inheriting data.
     * If the entered Password is correct {@link #getPrivateParams()} is called.
     * @return False if the user has closed the dialog with the x in the top right corner or canceled the dialog.
     * True if he has entered the right password
     */
    private boolean enterPasswordDialog() {
    	boolean result = false;
    	do {
	    	InputDialog passDialog = new InputDialog(Display.getCurrent().getActiveShell(),
					Messages.ElGamalData_inherit_password_text, Messages.ElGamalData_inherit_password_title, "", null);
	    	try {
	    		if (passDialog.open() == Window.OK) {
					password = passDialog.getValue();
				} else {
					return false;
				}
	    		getPrivateParams();
	    		return true;
	    	} catch (Exception e) {
	    		JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, ElGamalPlugin.PLUGIN_ID,
						Messages.ElGamalData_ExAccessKeystorePassword, e));
	    		result =  false;
	    	}
    	} while (!result);
    	
    	return true;
    }
    
    
    /**
     * extracts the parameters of the private key from the keystore via the {@link #privateAlias}
     *
     * @throws Exception if something went wrong while accessing the keystore
     */
    private void getPrivateParams() throws Exception {
        final KeyStoreManager ksm = KeyStoreManager.getInstance();
        final PrivateKey key = ksm.getPrivateKey(privateAlias, password.toCharArray());
        final ElGamalPrivateKey privKey = (ElGamalPrivateKey) key;
        a = privKey.getA().bigInt;
        generator = privKey.getGenerator().bigInt;
        modulus = privKey.getModulus().bigInt;
        publicA = privKey.getPublicA().bigInt;
    }

    /**
     * whether this data object is from a standalone wizard
     *
     * @return the {@link #standalone}
     */
    public final boolean isStandalone() {
        return standalone;
    }

    /**
     * setter for the standalone property of this data-object.
     *
     * @param standalone the standalone property
     */
    public final void setStandalone(final boolean standalone) {
        this.standalone = standalone;
    }

    /**
     * getter for the {@link #modulus}
     *
     * @return the {@link #modulus}
     */
    public BigInteger getModulus() {
        return modulus;
    }

    /**
     * getter for the {@link #generator}
     *
     * @return the {@link #generator}
     */
    public BigInteger getGenerator() {
        return generator;
    }

    /**
     * getter for the {@link #publicA}
     *
     * @return the {@link #publicA}
     */
    public BigInteger getPublicA() {
        return publicA;
    }

    /**
     * getter for the private {@link #a}
     *
     * @return the private {@link #a}
     */
    public BigInteger getA() {
        return a;
    }

    /**
     * getter for the {@link #simpleHash} property
     *
     * @return the {@link #simpleHash} property
     */
    public boolean getSimpleHash() {
        return simpleHash;
    }

    /**
     * getter for the {@link #privateAlias}
     *
     * @return the {@link #privateAlias}
     */
    public KeyStoreAlias getPrivateAlias() {
        return privateAlias;
    }

    /**
     * getter for the {@link #password}
     *
     * @return the {@link #password}
     */
    public String getPassword() {
        return password;
    }

    /**
     * setter for the {@link #modulus}
     *
     * @param modulus the new {@link #modulus}
     */
    public void setModulus(final BigInteger modulus) {
        this.modulus = modulus;
    }

    /**
     * setter for the {@link #generator}
     *
     * @param generator the new {@link #generator}
     */
    public void setGenerator(final BigInteger generator) {
        this.generator = generator;
    }

    /**
     * setter for the private {@link #a}
     *
     * @param a the new private {@link #a}
     */
    public void setA(final BigInteger a) {
        this.a = a;
    }

    /**
     * setter for the {@link #publicA}
     *
     * @param publicA the new {@link #publicA}
     */
    public void setPublicA(final BigInteger publicA) {
        this.publicA = publicA;
    }

    /**
     * getter for the {@link #publicAlias}
     *
     * @return the {@link #publicAlias}
     */
    public KeyStoreAlias getPublicAlias() {
        return publicAlias;
    }

    /**
     * getter for the {@link #contactName}
     *
     * @return the {@link #contactName}
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * setter for the {@link #simpleHash} property
     *
     * @param simplehash the new {@link #simpleHash}
     */
    public void setSimpleHash(final boolean simplehash) {
        this.simpleHash = simplehash;
    }

    /**
     * setter for the {@link #privateAlias}
     *
     * @param privateAlias the new {@link #privateAlias}
     */
    public void setPrivateAlias(final KeyStoreAlias privateAlias) {
        this.privateAlias = privateAlias;
    }

    /**
     * setter for the {@link #publicAlias}
     *
     * @param publicAlias the new {@link #publicAlias}
     */
    public void setPublicAlias(final KeyStoreAlias publicAlias) {
        this.publicAlias = publicAlias;
    }

    /**
     * setter for the {@link #contactName}
     *
     * @param contactName the new {@link #contactName}
     */
    public void setContactName(final String contactName) {
        this.contactName = contactName;
    }

    /**
     * getter for the {@link #action}
     *
     * @return the {@link #action}
     */
    public Action getAction() {
        return action;
    }

    /**
     * calculates {@link #generator}^{@link #b} mod {@link #modulus}
     *
     * @return {@link #generator}^{@link #b} mod {@link #modulus}
     */
    public BigInteger getGPowB() {
        return generator.modPow(b, modulus);
    }

    /**
     * setter for the new {@link #password}
     *
     * @param password the {@link #password} to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * getter for the {@link #r}
     *
     * @return the {@link #r}
     */
    public BigInteger getR() {
        return r;
    }

    /**
     * setter for the {@link #r}
     *
     * @param r the new {@link #r} to set
     */
    public void setR(final BigInteger r) {
        this.r = r;
    }

    /**
     * getter for the {@link #k}
     *
     * @return the {@link #k}
     */
    public BigInteger getK() {
        return k;
    }

    /**
     * setter for the {@link #k}
     *
     * @param k the new {@link #k} to set
     */
    public void setK(final BigInteger k) {
        this.k = k;
    }

    /**
     * getter for the {@link #b}
     *
     * @return the {@link #b}
     */
    public BigInteger getB() {
        return b;
    }

    /**
     * setter for the {@link #b}
     *
     * @param b the new {@link #b} to set
     */
    public void setB(final BigInteger b) {
        this.b = b;
    }
    
    /**
     * Used for showing the right conversion in the NumberblocksAndTextViewer Fields in the GUI.
     * @param blockConversion
     */
	public void setPlainTextConversion(ConversionStringToBlocks blockConversion) {
		plainTextConversion = blockConversion;
	}
	
	/**
	 * Used for showing the right conversion in the NumberblocksAndTextViewer Fields in the GUI.
	 * @return the used ConversionStringToBlocks.
	 */
	public ConversionStringToBlocks getPlainTextConversion() {
		return plainTextConversion;
	}

	public List<Integer> getPlainTextAsNumbers() {
		return plainTextAsNumbers;
	}

	public void setPlainTextAsNumbers(List<Integer> plainTextAsNumbers) {
		this.plainTextAsNumbers = plainTextAsNumbers;
	}

	public List<Integer> getCipherTextAsNumbers() {
		return cipherTextAsNumbers;
	}

	public void setCipherTextAsNumbers(List<Integer> cipherTextAsNumbers) {
		this.cipherTextAsNumbers = cipherTextAsNumbers;
	}
	
    public ConversionStringToBlocks getStb() {
		return stb;
	}

    /**
     * 
     * @return First Element is r as decimal, second element is s as decimal.
     */
	public List<Integer> getSignatureAsNumbers() {
		return signatureAsNumbers;
	}

	/**
	 * First Element is r as decimal. Second Element is s as decimal.
	 * @param signatureAsNumbers
	 */
	public void setSignatureAsNumbers(List<Integer> signatureAsNumbers) {
		this.signatureAsNumbers = signatureAsNumbers;
	}

}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.elGamal;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;

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

    /** the public a */
    private BigInteger publicA;

    /** the private a */
    private BigInteger a;

    /** the plaintext */
    private String plainText;

    /** the ciphertext */
    private String cipherText;

    /** the signature */
    private String signature;

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

    /**
     * constructor, setting {@link #action}
     *
     * @param action the {@link Action} to set to {@link #action}
     */
    public ElGamalData(final Action action) {
        this.action = action;
    }

    /**
     * inherits all possible data from another data object
     *
     * @param oldData the other {@link ElGamalData} to copy data from
     */
    public void inherit(final ElGamalData oldData) {
        this.publicA = oldData.publicA;
        this.simpleHash = oldData.simpleHash;
        this.modulus = oldData.modulus;
        this.generator = oldData.generator;
        this.k = oldData.k;
        this.r = oldData.r;
        this.b = oldData.b;

        this.publicAlias = oldData.publicAlias;
        this.contactName = oldData.contactName;

        if (this.action == Action.DecryptAction || this.action == Action.SignAction) {
            if (oldData.action == Action.DecryptAction || oldData.action == Action.SignAction) {
                this.a = oldData.a;
                this.privateAlias = oldData.privateAlias;
                this.password = oldData.password;
            } else {
                this.privateAlias = KeyStoreManager.getInstance().getPrivateForPublic(this.privateAlias);
                final InputDialog passDialog = new InputDialog(Display.getCurrent().getActiveShell(),
                        Messages.ElGamalData_inherit_password_text, Messages.ElGamalData_inherit_password_title, "",
                        null);
                if (passDialog.open() == Window.OK) {
                    this.password = passDialog.getValue();
                } else {
                    return;
                }
                try {
                    this.getPrivateParams();
                } catch (UnrecoverableKeyException e) {
                    JCTMessageDialog.showInfoDialog(new Status(Status.INFO, ElGamalPlugin.PLUGIN_ID,
                            Messages.ElGamalData_ExAccessKeystorePassword, e));
                } catch (final Exception e) {
                    LogUtil.logError(e);
                }
            }
        }

        this.cipherText = oldData.cipherText;
        this.plainText = oldData.plainText;
        this.signature = oldData.signature;
    }

    /**
     * extracts the parameters of the private key from the keystore via the {@link #privateAlias}
     *
     * @throws Exception if something went wrong while accessing the keystore
     */
    private void getPrivateParams() throws Exception {
        final KeyStoreManager ksm = KeyStoreManager.getInstance();
        final PrivateKey key = ksm.getPrivateKey(this.privateAlias, this.password.toCharArray());
        final ElGamalPrivateKey privKey = (ElGamalPrivateKey) key;
        this.a = privKey.getA().bigInt;
        this.generator = privKey.getGenerator().bigInt;
        this.modulus = privKey.getModulus().bigInt;
        this.publicA = privKey.getPublicA().bigInt;
    }

    /**
     * whether this data object is from a standalone wizard
     *
     * @return the {@link #standalone}
     */
    public final boolean isStandalone() {
        return this.standalone;
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
        return this.modulus;
    }

    /**
     * getter for the {@link #generator}
     *
     * @return the {@link #generator}
     */
    public BigInteger getGenerator() {
        return this.generator;
    }

    /**
     * getter for the {@link #publicA}
     *
     * @return the {@link #publicA}
     */
    public BigInteger getPublicA() {
        return this.publicA;
    }

    /**
     * getter for the private {@link #a}
     *
     * @return the private {@link #a}
     */
    public BigInteger getA() {
        return this.a;
    }

    /**
     * getter for the {@link #plainText}
     *
     * @return the {@link #plainText} or an empty string if none is set
     */
    public String getPlainText() {
        if (this.plainText == null) {
            return ""; //$NON-NLS-1$
        } else {
            return this.plainText;
        }
    }

    /**
     * getter for the {@link #cipherText}
     *
     * @return the {@link #cipherText} or an empty string if none is set
     */
    public String getCipherText() {
        if (this.cipherText == null) {
            return ""; //$NON-NLS-1$
        } else {
            return this.cipherText;
        }
    }

    /**
     * getter for the {@link #signature}
     *
     * @return the {@link #signature} or an empty string if none is set
     */
    public String getSignature() {
        if (this.signature == null) {
            return ""; //$NON-NLS-1$
        } else {
            return this.signature;
        }
    }

    /**
     * getter for the {@link #simpleHash} property
     *
     * @return the {@link #simpleHash} property
     */
    public boolean getSimpleHash() {
        return this.simpleHash;
    }

    /**
     * getter for the {@link #privateAlias}
     *
     * @return the {@link #privateAlias}
     */
    public KeyStoreAlias getPrivateAlias() {
        return this.privateAlias;
    }

    /**
     * getter for the {@link #password}
     *
     * @return the {@link #password}
     */
    public String getPassword() {
        return this.password;
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
        return this.publicAlias;
    }

    /**
     * getter for the {@link #contactName}
     *
     * @return the {@link #contactName}
     */
    public String getContactName() {
        return this.contactName;
    }

    /**
     * setter for the {@link #plainText}
     *
     * @param plainText the new {@link #plainText}
     */
    public void setPlainText(final String plainText) {
        this.plainText = plainText;
    }

    /**
     * setter for the {@link #cipherText}
     *
     * @param cipherText the new {@link #cipherText}
     */
    public void setCipherText(final String cipherText) {
        this.cipherText = cipherText;
    }

    /**
     * setter for the {@link #signature}
     *
     * @param signature the new {@link #signature}
     */
    public void setSignature(final String signature) {
        this.signature = signature;
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
        return this.action;
    }

    /**
     * calculates {@link #generator}^{@link #b} mod {@link #modulus}
     *
     * @return {@link #generator}^{@link #b} mod {@link #modulus}
     */
    public BigInteger getGPowB() {
        return this.generator.modPow(this.b, this.modulus);
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
        return this.r;
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
        return this.k;
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
        return this.b;
    }

    /**
     * setter for the {@link #b}
     *
     * @param b the new {@link #b} to set
     */
    public void setB(final BigInteger b) {
        this.b = b;
    }

}

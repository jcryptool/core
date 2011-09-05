// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
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

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;

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
        return this.contactName;
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
    private String cipherText;

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
    private String plainText;

    /** second factor. */
    private BigInteger q;

    /** signature of a text. */
    private String signature;

    /** the private key alias. */
    private KeyStoreAlias privateAlias;

    /** the public key alias. */
    private KeyStoreAlias publicAlias;

    /** whether the simple hash algorithm or sha-1 was and should be used. */
    private boolean simpleHash = true;

    /** if this data-object belongs to a standalone key generation wizard. */
    private boolean standalone;

    /**
     * setter for the standalone property
     *
     * @param standalone the standalone to set
     */
    public final void setStandalone(final boolean standalone) {
        this.standalone = standalone;
    }

    /**
     * getter for the ciphertext.
     *
     * @return the cipherText
     */
    public final String getCipherText() {
        if (this.cipherText == null) {
            return ""; //$NON-NLS-1$
        } else {
            return this.cipherText;
        }
    }

    /**
     * getter for d.
     *
     * @return the d
     */
    public final BigInteger getD() {
        return this.d;
    }

    /**
     * getter for e.
     *
     * @return the e
     */
    public final BigInteger getE() {
        return this.e;
    }

    /**
     * getter for n.
     *
     * @return the n
     */
    public final BigInteger getN() {
        return this.N;
    }

    /**
     * getter for p.
     *
     * @return the p
     */
    public final BigInteger getP() {
        return this.p;
    }

    /**
     * getter for the password.
     *
     * @return the password
     */
    public final String getPassword() {
        return this.password;
    }

    /**
     * getter for the plaintext.
     *
     * @return the plainText
     */
    public final String getPlainText() {
        if (this.plainText == null) {
            return ""; //$NON-NLS-1$
        } else {
            return this.plainText;
        }
    }

    /**
     * getter for q.
     *
     * @return the q
     */
    public final BigInteger getQ() {
        return this.q;
    }

    /**
     * getter for the signature.
     *
     * @return the signature
     */
    public final String getSignature() {
        if (this.signature == null) {
            return ""; //$NON-NLS-1$
        } else {
            return this.signature;
        }
    }

    /**
     * setter for the ciphertext.
     *
     * @param cipherText the cipherText to set
     */
    public final void setCipherText(final String cipherText) {
        this.cipherText = cipherText;
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
    public final void setPlainText(final String plainText) {
        this.plainText = plainText;
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
    public final void setSignature(final String signature) {
        this.signature = signature;
    }

    /**
     * getter for the private alias of the contact
     *
     * @return the privateAlias
     */
    public final KeyStoreAlias getPrivateAlias() {
        return this.privateAlias;
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
        return this.publicAlias;
    }

    /**
     * getter for {@link #simpleHash}.
     *
     * @return the simpleHash
     */
    public final boolean getSimpleHash() {
        return this.simpleHash;
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
        return this.standalone;
    }

    /**
     * getter for the type of action
     *
     * @return the current action
     */
    public final Action getAction() {
        return this.action;
    }

    /**
     * inherits the data from an other data object into this one. used for copying data between pages
     *
     * @param oldData the old data object from which data is copied
     */
    public void inherit(final RSAData oldData) {
        // this we can always get
        this.N = oldData.N;
        this.e = oldData.e;
        this.simpleHash = oldData.simpleHash;
        // only available if key was saved, but we might have a problem
        // otherwise anyway.
        this.publicAlias = oldData.publicAlias;
        this.contactName = oldData.contactName;

        // sometimes we need everything
        if (this.action == Action.DecryptAction || this.action == Action.SignAction) {
            // easy if the other action already has everything we need
            if (oldData.action == Action.DecryptAction || oldData.action == Action.SignAction) {
                this.d = oldData.d;
                this.p = oldData.p;
                this.q = oldData.q;
                // only available if key was saved, but we might have a problem
                // otherwise anyway.
                this.privateAlias = oldData.privateAlias;
                this.password = oldData.password;
            } else {
                this.privateAlias = KeyStoreManager.getInstance().getPrivateForPublic(this.publicAlias);
                // get the password via some dialog
                final InputDialog passDialog = new InputDialog(Display.getCurrent().getActiveShell(),
                        Messages.RSAData_inherit_password_title, Messages.RSAData_inherit_password_text, "", null); //$NON-NLS-1$
                if (passDialog.open() == Window.OK) {
                    this.password = passDialog.getValue();
                } else {
                    return;
                }
                try {
                    this.getPrivateParams();
                }
                catch (UnrecoverableKeyException e) {
                    JCTMessageDialog.showInfoDialog(new Status(Status.INFO, RSAPlugin.PLUGIN_ID,
                            Messages.RSAData_ExAccessKeystorePassword, e));
                } catch (final NullPointerException e) {
                    JCTMessageDialog.showErrorDialog(new Status(Status.ERROR, RSAPlugin.PLUGIN_ID,
                            e.getMessage(), e), Messages.RSAData_failedInitPrivParams);
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
     * gets the private key corresponding to the {@link #privateAlias} with the specified {@link #password} from the
     * keystore and extracts the private parameters from it
     *
     * @throws Exception if anything went wrong while accessing the keystore
     */
    private void getPrivateParams() throws Exception {
        final KeyStoreManager ksm = KeyStoreManager.getInstance();
        final PrivateKey key = ksm.getPrivateKey(this.privateAlias, this.password.toCharArray());
        final RSAPrivateCrtKey privkey = (RSAPrivateCrtKey) key;
        this.N = privkey.getModulus();
        this.d = privkey.getD().bigInt;
        this.p = privkey.getP().bigInt;
        this.q = privkey.getQ().bigInt;
        this.e = privkey.getPublicExponent();
    }
}

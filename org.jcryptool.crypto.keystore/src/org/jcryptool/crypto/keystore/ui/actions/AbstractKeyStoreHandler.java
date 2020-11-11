// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.actions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Calendar;

import javax.crypto.SecretKey;

import org.eclipse.core.commands.AbstractHandler;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.util.ByteArrayUtils;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.certificates.CertificateFactory;
import org.jcryptool.crypto.keystore.descriptors.NewKeyPairDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewEntryDescriptor;
import org.jcryptool.crypto.keystore.keys.KeyType;

/**
 * Abstract (and empty) top-level action for plug-in.
 * 
 * @author tkern
 * @author Holger Friedrich (support for Commands, additional class based on AbstractKeyStoreAction)
 * 
 */
public abstract class AbstractKeyStoreHandler extends AbstractHandler {
    protected void addCertificate(INewEntryDescriptor descriptor, Certificate certificate) {
        KeyStoreManager.getInstance().addCertificate(
                certificate,
                new KeyStoreAlias(descriptor.getContactName(), KeyType.PUBLICKEY, certificate.getPublicKey()
                        .getAlgorithm(), descriptor.getKeyLength(), ByteArrayUtils
                        .toHexString(getHashValue(descriptor)), certificate.getPublicKey().getClass().getName()));
    }

    public static KeyStoreAlias addSecretKeyStatic(INewEntryDescriptor descriptor, SecretKey key) {
        LogUtil.logInfo("adding SecretKey"); //$NON-NLS-1$
        KeyStoreAlias alias = new KeyStoreAlias(descriptor.getContactName(), KeyType.SECRETKEY,
        // key.getAlgorithm(),
                descriptor.getDisplayedName(), (key.getEncoded().length * 8),
                ByteArrayUtils.toHexString(getHashValue(descriptor)), key.getClass().getName());

        KeyStoreManager.getInstance().addSecretKey(key, descriptor.getPassword().toCharArray(), alias);

        return alias;
    }

    protected KeyStoreAlias addSecretKey(INewEntryDescriptor descriptor, SecretKey key) {
        return addSecretKeyStatic(descriptor, key);
    }

	public static KeyStoreAlias[] addKeyPairStatic_GetBoth(INewEntryDescriptor descriptor, PrivateKey privateKey,
			PublicKey publicKey) {
        KeyStoreAlias privateAlias = new KeyStoreAlias(descriptor.getContactName(), KeyType.KEYPAIR_PRIVATE_KEY,
                descriptor.getDisplayedName(), descriptor.getKeyLength(),
                ByteArrayUtils.toHexString(getHashValue(descriptor)), privateKey.getClass().getName());

        KeyStoreAlias publicAlias = new KeyStoreAlias(descriptor.getContactName(), KeyType.KEYPAIR_PUBLIC_KEY,
                descriptor.getDisplayedName(), descriptor.getKeyLength(),
                ByteArrayUtils.toHexString(getHashValue(descriptor)), publicKey.getClass().getName());

        X509Certificate jctCertificate = CertificateFactory.createJCrypToolCertificate(publicKey);

        KeyStoreManager.getInstance().addKeyPair(privateKey, jctCertificate, descriptor.getPassword().toCharArray(),
                privateAlias, publicAlias);

        return new KeyStoreAlias[] {publicAlias, privateAlias};
	}
    public static KeyStoreAlias addKeyPairStatic(INewEntryDescriptor descriptor, PrivateKey privateKey,
			PublicKey publicKey) {
    	return addKeyPairStatic_GetBoth(descriptor, privateKey, publicKey)[0];
    }

    protected KeyStoreAlias addKeyPair(INewEntryDescriptor descriptor, PrivateKey privateKey, PublicKey publicKey) {
        return addKeyPairStatic(descriptor, privateKey, publicKey);
    }

    private static byte[] getHashValue(INewEntryDescriptor descriptor) {
        String timeStamp = Calendar.getInstance().getTime().toString();
        MessageDigest sha1;

        try {
            sha1 = MessageDigest.getInstance("SHA-1"); //$NON-NLS-1$
            sha1.update(descriptor.getContactName().getBytes());
            sha1.update(descriptor.getAlgorithmName().getBytes());
            sha1.update(descriptor.getProvider().getBytes());
            return sha1.digest(timeStamp.getBytes());
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "NoSuchAlgorithmException while digesting", e, true);
        }
        return new byte[] { 0 };
    }


}

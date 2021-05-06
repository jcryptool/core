// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.dialogs.contentproviders;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.CMSSPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.CMSSPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.DSAPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.DSAPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.ECPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.ECPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.ElGamalPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.ElGamalPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.GMSSPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.GMSSPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.IQDSAPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.IQDSAPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.IQGQPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.IQGQPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.IQRDSAPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.IQRDSAPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.LMOTSPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.LMOTSPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.McElieceCCA2PrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.McElieceCCA2PublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.McEliecePublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.MeRSAPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.MerkleOTSPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.MerkleOTSPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.MpRSAPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.NiederreiterPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.NiederreiterPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.PFlashPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.PFlashPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.RSAPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.RainbowPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.RainbowPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.SSVElGamalPrivateKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair.SSVElGamalPublicKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.secretkey.ECSecretKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.secretkey.PBESecretKeyContentProvider;
import org.jcryptool.crypto.keystore.ui.views.nodes.TreeNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.AbstractKeyNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.CertificateNode;

import de.flexiprovider.core.dsa.DSAPrivateKey;
import de.flexiprovider.core.dsa.DSAPublicKey;
import de.flexiprovider.core.elgamal.ElGamalPrivateKey;
import de.flexiprovider.core.elgamal.ElGamalPublicKey;
import de.flexiprovider.core.elgamal.semanticallysecure.SSVElGamalPrivateKey;
import de.flexiprovider.core.elgamal.semanticallysecure.SSVElGamalPublicKey;
import de.flexiprovider.core.mersa.MeRSAPrivateKey;
import de.flexiprovider.core.mprsa.MpRSAPrivateKey;
import de.flexiprovider.core.pbe.PBEKey;
import de.flexiprovider.core.rsa.RSAPublicKey;
import de.flexiprovider.ec.keys.ECPrivateKey;
import de.flexiprovider.ec.keys.ECPublicKey;
import de.flexiprovider.ec.keys.ECSecretKey;
import de.flexiprovider.nf.iq.iqdsa.IQDSAPrivateKey;
import de.flexiprovider.nf.iq.iqdsa.IQDSAPublicKey;
import de.flexiprovider.nf.iq.iqgq.IQGQPrivateKey;
import de.flexiprovider.nf.iq.iqgq.IQGQPublicKey;
import de.flexiprovider.nf.iq.iqrdsa.IQRDSAPrivateKey;
import de.flexiprovider.nf.iq.iqrdsa.IQRDSAPublicKey;
import de.flexiprovider.pqc.ecc.mceliece.McElieceCCA2PrivateKey;
import de.flexiprovider.pqc.ecc.mceliece.McElieceCCA2PublicKey;
import de.flexiprovider.pqc.ecc.mceliece.McEliecePublicKey;
import de.flexiprovider.pqc.ecc.niederreiter.NiederreiterPrivateKey;
import de.flexiprovider.pqc.ecc.niederreiter.NiederreiterPublicKey;
import de.flexiprovider.pqc.hbc.cmss.CMSSPrivateKey;
import de.flexiprovider.pqc.hbc.cmss.CMSSPublicKey;
import de.flexiprovider.pqc.hbc.gmss.GMSSPrivateKey;
import de.flexiprovider.pqc.hbc.gmss.GMSSPublicKey;
import de.flexiprovider.pqc.ots.lm.LMOTSPrivateKey;
import de.flexiprovider.pqc.ots.lm.LMOTSPublicKey;
import de.flexiprovider.pqc.ots.merkle.MerkleOTSPrivateKey;
import de.flexiprovider.pqc.ots.merkle.MerkleOTSPublicKey;
import de.flexiprovider.pqc.pflash.PFlashPrivateKey;
import de.flexiprovider.pqc.pflash.PFlashPublicKey;
import de.flexiprovider.pqc.rainbow.RainbowPrivateKey;
import de.flexiprovider.pqc.rainbow.RainbowPublicKey;

/**
 * @author Anatoli Barski
 * 
 */
public class ContentProviderFactory {

    public static IStructuredContentProvider create(TreeNode treeNode) {

        IKeyStoreAlias alias = null;

        if (treeNode instanceof CertificateNode) {
            alias = ((CertificateNode) treeNode).getAlias();
        } else if (treeNode instanceof AbstractKeyNode) {
            alias = ((AbstractKeyNode) treeNode).getAlias();
        }

        if (alias == null) {
            return new CommonContentProvider();
        }

        KeyType keyType = alias.getKeyStoreEntryType();
        String className = alias.getClassName();

        switch (keyType) {
        case SECRETKEY:
            if (className.equals(ECSecretKey.class.getName())) {
                return new ECSecretKeyContentProvider();
            } else if (className.equals(PBEKey.class.getName())) {
                return new PBESecretKeyContentProvider();
            } else {
                return new AbstractKeyNodeContentProvider();
            }
        case PUBLICKEY:
            return new CertificateContentProvider();
        case KEYPAIR_PRIVATE_KEY:
            if (className.equals(DSAPrivateKey.class.getName())) {
                return new DSAPrivateKeyContentProvider();
            } else if (className.equals(ElGamalPrivateKey.class.getName())) {
                return new ElGamalPrivateKeyContentProvider();
            } else if (className.equals(SSVElGamalPrivateKey.class.getName())) {
                return new SSVElGamalPrivateKeyContentProvider();
            } else if (className.equals(MeRSAPrivateKey.class.getName())) {
                return new MeRSAPrivateKeyContentProvider();
            } else if (className.equals(MpRSAPrivateKey.class.getName())) {
                return new MpRSAPrivateKeyContentProvider();
            } else if (className.equals(CMSSPrivateKey.class.getName())) {
                return new CMSSPrivateKeyContentProvider();
            } else if (className.equals(ECPrivateKey.class.getName())) {
                return new ECPrivateKeyContentProvider();
            } else if (className.equals(GMSSPrivateKey.class.getName())) {
                return new GMSSPrivateKeyContentProvider();
            } else if (className.equals(IQDSAPrivateKey.class.getName())) {
                return new IQDSAPrivateKeyContentProvider();
            } else if (className.equals(IQGQPrivateKey.class.getName())) {
                return new IQGQPrivateKeyContentProvider();
            } else if (className.equals(IQRDSAPrivateKey.class.getName())) {
                return new IQRDSAPrivateKeyContentProvider();
            } else if (className.equals(LMOTSPrivateKey.class.getName())) {
                return new LMOTSPrivateKeyContentProvider();
            } else if (className.equals(McElieceCCA2PrivateKey.class.getName())) {
                return new McElieceCCA2PrivateKeyContentProvider();
            } else if (className.equals(MerkleOTSPrivateKey.class.getName())) {
                return new MerkleOTSPrivateKeyContentProvider();
            } else if (className.equals(NiederreiterPrivateKey.class.getName())) {
                return new NiederreiterPrivateKeyContentProvider();
            } else if (className.equals(PFlashPrivateKey.class.getName())) {
                return new PFlashPrivateKeyContentProvider();
            } else if (className.equals(RainbowPrivateKey.class.getName())) {
                return new RainbowPrivateKeyContentProvider();
            } else {
                JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, KeyStorePlugin.PLUGIN_ID,
                        Messages.ContentProviderFactory_0 + "\n" + Messages.ContentProviderFactory_2));
                return new AbstractKeyNodeContentProvider();
            }
        case KEYPAIR_PUBLIC_KEY:
            if (className.equals(DSAPublicKey.class.getName())) {
                return new DSAPublicKeyContentProvider();
            } else if (className.equals(ElGamalPublicKey.class.getName())) {
                return new ElGamalPublicKeyContentProvider();
            } else if (className.equals(SSVElGamalPublicKey.class.getName())) {
                return new SSVElGamalPublicKeyContentProvider();
            } else if (className.equals(RSAPublicKey.class.getName())) {
                return new RSAPublicKeyContentProvider();
            } else if (className.equals(CMSSPublicKey.class.getName())) {
                return new CMSSPublicKeyContentProvider();
            } else if (className.equals(ECPublicKey.class.getName())) {
                return new ECPublicKeyContentProvider();
            } else if (className.equals(GMSSPublicKey.class.getName())) {
                return new GMSSPublicKeyContentProvider();
            } else if (className.equals(IQDSAPublicKey.class.getName())) {
                return new IQDSAPublicKeyContentProvider();
            } else if (className.equals(IQGQPublicKey.class.getName())) {
                return new IQGQPublicKeyContentProvider();
            } else if (className.equals(IQRDSAPublicKey.class.getName())) {
                return new IQRDSAPublicKeyContentProvider();
            } else if (className.equals(LMOTSPublicKey.class.getName())) {
                return new LMOTSPublicKeyContentProvider();
            } else if (className.equals(McElieceCCA2PublicKey.class.getName())) {
                return new McElieceCCA2PublicKeyContentProvider();
            } else if (className.equals(McEliecePublicKey.class.getName())) {
                return new McEliecePublicKeyContentProvider();
            } else if (className.equals(MerkleOTSPublicKey.class.getName())) {
                return new MerkleOTSPublicKeyContentProvider();
            } else if (className.equals(NiederreiterPublicKey.class.getName())) {
                return new NiederreiterPublicKeyContentProvider();
            } else if (className.equals(PFlashPublicKey.class.getName())) {
                return new PFlashPublicKeyContentProvider();
            } else if (className.equals(RainbowPublicKey.class.getName())) {
                return new RainbowPublicKeyContentProvider();
            } else {
                JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, KeyStorePlugin.PLUGIN_ID,
                        Messages.ContentProviderFactory_0 + "\n" + Messages.ContentProviderFactory_2));
                return new CertificateContentProvider();
            }
        default:
            break;
        }
        return new CommonContentProvider();
    }
}

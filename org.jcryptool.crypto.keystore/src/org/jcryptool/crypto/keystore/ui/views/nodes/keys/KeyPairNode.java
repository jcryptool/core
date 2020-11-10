// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
/**
 * 
 */
package org.jcryptool.crypto.keystore.ui.views.nodes.keys;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.ui.views.nodes.TreeNode;

/**
 * @author tkern
 * 
 */
public class KeyPairNode extends TreeNode {

    private int nameCounter = 0;

    private AbstractKeyNode privateKeyNode;
    private AbstractKeyNode publicKeyNode;
    private IKeyStoreAlias privateAlias, publicAlias;

    public KeyPairNode(IKeyStoreAlias privateAlias, IKeyStoreAlias publicAlias) {
        super(
                getOperation(privateAlias, publicAlias)
                        + " (" + Messages.getString("Label.KeyStrength") + ": " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                        + ((getKeyLength(privateAlias, publicAlias) > 0) ? getKeyLength(privateAlias, publicAlias)
                                : "n/a") + ")"/* + (addCounter==0?"":" ("+addCounter+")") */); //$NON-NLS-1$ //$NON-NLS-2$

        this.privateAlias = privateAlias;
        this.publicAlias = publicAlias;

        // super(getOperation(privateAlias, publicAlias) + " " +
        // KeyStorePlugin.getResourceBundle().getString("Label.KeyPair") + " ("+
        // KeyStorePlugin.getResourceBundle().getString("Label.KeyStrength") +": " + getKeyLength(privateAlias,
        // publicAlias) + ")");
        if (privateAlias != null) {
            privateKeyNode = new PrivateKeyNode(privateAlias);
            this.addChild(privateKeyNode);
        }
        if (publicAlias != null) {
            publicKeyNode = new CertificateNode(publicAlias);
            this.addChild(publicKeyNode);
        }
    }

    public void incNameCounter() {
        nameCounter++;
    }

    @Override
    public String getName() {
        return getOperation(privateAlias, publicAlias)
                + Messages.getString("Label.KeyPair") + " (" + Messages.getString("Label.KeyStrength") + ": " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + ((getKeyLength(privateAlias, publicAlias) > 0) ? getKeyLength(privateAlias, publicAlias) : "n/a") + ")" + (nameCounter == 0 ? "" : " (" + nameCounter + ")"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private static String getOperation(IKeyStoreAlias privateAlias, IKeyStoreAlias publicAlias) {
        if (privateAlias != null) {
            return privateAlias.getOperation();
        } else if (publicAlias != null) {
            return publicAlias.getOperation();
        }
        else {
            return "<undefined>"; //$NON-NLS-1$
        }
    }

    private static int getKeyLength(IKeyStoreAlias privateAlias, IKeyStoreAlias publicAlias) {
        if (privateAlias != null) {
            return privateAlias.getKeyLength();
        } else if (publicAlias != null) {
            return publicAlias.getKeyLength();
        } else {
            return -1;
        }
    }

    public void addPrivateKey(IKeyStoreAlias privateAlias) {
        if (this.privateKeyNode != null) {
            return;
        } else {
            this.privateAlias = privateAlias;
            privateKeyNode = new PrivateKeyNode(privateAlias);
            this.addChild(privateKeyNode);
        }
    }

    public void addPublicKey(IKeyStoreAlias publicAlias) {
        if (this.publicKeyNode != null) {
            return;
        } else {
            this.publicAlias = publicAlias;
            publicKeyNode = new CertificateNode(publicAlias);
            this.addChild(publicKeyNode);
        }
    }

    public IKeyStoreAlias getPrivateKeyAlias() {
        if (privateKeyNode != null) {
            return privateKeyNode.getAlias();
        } else {
            return null;
        }
    }

    public IKeyStoreAlias getPublicKeyAlias() {
        if (publicKeyNode != null) {
            return publicKeyNode.getAlias();
        } else {
            return null;
        }
    }

    /**
     * @see org.jcryptool.crypto.keystore.ui.views.nodes.TreeNode#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
    	return ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/kgpg_key2.png");
    }
}

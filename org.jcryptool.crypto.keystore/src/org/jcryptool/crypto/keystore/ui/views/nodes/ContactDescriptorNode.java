// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.views.nodes;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IContactDescriptor;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.AbstractContainerNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.CertificateContainerNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.IKeyPairContainerNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.KeyPairContainerNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.SecretKeyContainerNode;

public class ContactDescriptorNode extends TreeNode implements IContactDescriptor {
    private Contact contact;
    private AbstractContainerNode secretKeyContainer;
    private AbstractContainerNode keyPairContainer;
    private AbstractContainerNode certificateContainer;

    public ContactDescriptorNode(Contact contact) {
        super(contact.getName());
        this.contact = contact;

        secretKeyContainer = new SecretKeyContainerNode();
        addChild(secretKeyContainer);
        keyPairContainer = new KeyPairContainerNode();
        addChild(keyPairContainer);
        certificateContainer = new CertificateContainerNode();
        addChild(certificateContainer);
    }

    /**
     * @see org.jcryptool.crypto.keystore.ui.views.nodes.TreeNode#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
        return ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/personal.png");
    }

    public void addCertificate(IKeyStoreAlias alias) {
        LogUtil.logInfo("adding a certificate to: " + contact.getName()); //$NON-NLS-1$
        certificateContainer.add(alias);
    }

    public void addKeyPair(IKeyStoreAlias privateKey, IKeyStoreAlias publicKey) {
        LogUtil.logInfo("adding a key pair to: " + contact.getName()); //$NON-NLS-1$
        ((IKeyPairContainerNode) keyPairContainer).addKeyPair(privateKey, publicKey);

    }

    public void addSecretKey(IKeyStoreAlias alias) {
        LogUtil.logInfo("adding a secret key to: " + contact.getName()); //$NON-NLS-1$
        secretKeyContainer.add(alias);
    }

    public void removeCertificate(IKeyStoreAlias alias) {
        LogUtil.logInfo("removing a certificate from " + contact.getName()); //$NON-NLS-1$
        certificateContainer.remove(alias);
    }

    public void removeKeyPair(IKeyStoreAlias privateKey) {
        LogUtil.logInfo("removing a key pair from " + contact.getName()); //$NON-NLS-1$
        keyPairContainer.remove(privateKey);
    }

    public void removeSecretKey(IKeyStoreAlias alias) {
        LogUtil.logInfo("removing a secret key from " + contact.getName()); //$NON-NLS-1$
        secretKeyContainer.remove(alias);
    }

    @Override
    public Contact getContact() {
        return contact;
    }
}

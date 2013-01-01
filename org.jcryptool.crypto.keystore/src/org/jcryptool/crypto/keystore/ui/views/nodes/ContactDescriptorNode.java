// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.views.nodes;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IContactDescriptor;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.AbstractContainerNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.CertificateContainerNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.IKeyPairContainerNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.KeyPairContainerNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.SecretKeyContainerNode;

public class ContactDescriptorNode extends TreeNode implements IContactDescriptor {
    private String name;
    private String firstname;
    private String lastname;
    private String organization;
    private String region;
    private AbstractContainerNode secretKeyContainer;
    private AbstractContainerNode keyPairContainer;
    private AbstractContainerNode certificateContainer;

    public ContactDescriptorNode(String name) {
        this(name, "", "", "", "");
    }

    public ContactDescriptorNode(String name, String firstname, String lastname, String organization, String region) {
        super(name, firstname, lastname, organization, region);
        this.name = name;
        this.firstname = firstname;
        this.lastname = lastname;
        this.organization = organization;
        this.region = region;

        secretKeyContainer = new SecretKeyContainerNode();
        addChild(secretKeyContainer);
        keyPairContainer = new KeyPairContainerNode();
        addChild(keyPairContainer);
        certificateContainer = new CertificateContainerNode();
        addChild(certificateContainer);
    }

    public String getName() {
        return name;
    }

    /**
     * @see org.jcryptool.crypto.keystore.ui.views.nodes.TreeNode#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
        return KeyStorePlugin.getImageDescriptor("icons/16x16/personal.png"); //$NON-NLS-1$
    }

    public void addCertificate(KeyStoreAlias alias) {
        LogUtil.logInfo("adding a certificate to: " + name); //$NON-NLS-1$
        certificateContainer.add(alias);
    }

    public void addKeyPair(KeyStoreAlias privateKey, KeyStoreAlias publicKey) {
        LogUtil.logInfo("adding a key pair to: " + name); //$NON-NLS-1$
        ((IKeyPairContainerNode) keyPairContainer).addKeyPair(privateKey, publicKey);

    }

    public void addSecretKey(KeyStoreAlias alias) {
        LogUtil.logInfo("adding a secret key to: " + name); //$NON-NLS-1$
        secretKeyContainer.add(alias);
    }

    public void removeCertificate(KeyStoreAlias alias) {
        LogUtil.logInfo("removing a certificate from " + name); //$NON-NLS-1$
        certificateContainer.remove(alias);
    }

    public void removeKeyPair(KeyStoreAlias privateKey) {
        LogUtil.logInfo("removing a key pair from " + name); //$NON-NLS-1$
        keyPairContainer.remove(privateKey);
    }

    public void removeSecretKey(KeyStoreAlias alias) {
        LogUtil.logInfo("removing a secret key from " + name); //$NON-NLS-1$
        secretKeyContainer.remove(alias);
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    @Override
    public String getOrganization() {
        return organization;
    }

    @Override
    public String getRegion() {
        return region;
    }
}

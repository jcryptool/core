//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.keys;

import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.flexiprovider.operations.FlexiProviderOperationsPlugin;
import org.jcryptool.crypto.flexiprovider.operations.OperationsManager;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.IOperationChangedListener;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;

public class KeyPairNode extends KeyNode {

    private IKeyStoreAlias alias;

    public KeyPairNode() {
        super(Messages.KeyPairNode_0);
    }

    public IKeyStoreAlias getAlias() {
        return alias;
    }

    public void setAlias(IKeyStoreAlias alias) {
        if (alias == null) {
            this.alias = null;
            setName(Messages.KeyPairNode_1);
        } else {
            if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PRIVATE_KEY)
                    || alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PUBLIC_KEY)) {
                this.alias = alias;
                if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PRIVATE_KEY)) {
                    setName(NLS.bind(Messages.KeyPairNode_2, new Object[] {alias.getContactName(), alias.getOperation(), alias.getKeyLength()}));
                } else if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PUBLIC_KEY)) {
                    setName(NLS.bind(Messages.KeyPairNode_3, new Object[] {alias.getContactName(), alias.getOperation(), alias.getKeyLength()}));
                }
            } else {
                MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getShell(), Messages.KeyPairNode_4, Messages.KeyPairNode_5);
                return;
            }
        }
        Iterator<IOperationChangedListener> it = OperationsManager.getInstance()
                .getOperationChangedListeners();
        while (it.hasNext()) {
            it.next().update(this);
        }
    }

    public ImageDescriptor getImageDescriptor() {
        if (alias == null) {
            return ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/kgpg_key2.png");
        } else if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PRIVATE_KEY)) {
            return ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/kgpg_key1.png");
        } else if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PUBLIC_KEY)) {
        	return ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/kgpg_identity.png");
        }
        return super.getImageDescriptor();
    }
}

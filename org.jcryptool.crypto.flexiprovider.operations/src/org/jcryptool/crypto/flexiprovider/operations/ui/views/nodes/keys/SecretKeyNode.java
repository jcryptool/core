//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
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

public class SecretKeyNode extends KeyNode {
    private IKeyStoreAlias alias;

    public SecretKeyNode() {
        super(Messages.SecretKeyNode_0);
    }

    public void setAlias(IKeyStoreAlias alias) {
        if (alias == null) {
            this.alias = null;
            setName(Messages.SecretKeyNode_1);
        } else {
            if (alias.getKeyStoreEntryType().equals(KeyType.SECRETKEY)) {
                this.alias = alias;
                setName(NLS.bind(Messages.SecretKeyNode_2, new Object[] {alias.getContactName(), alias.getOperation(), alias.getKeyLength()}));
            } else {
                MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getShell(), Messages.SecretKeyNode_3, Messages.SecretKeyNode_4);
                return;
            }
        }
        Iterator<IOperationChangedListener> it = OperationsManager.getInstance()
                .getOperationChangedListeners();
        while (it.hasNext()) {
            it.next().update(this);
        }
    }

    public IKeyStoreAlias getAlias() {
        return alias;
    }

    public ImageDescriptor getImageDescriptor() {
        return ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/kgpg_key1.png");
    }
}

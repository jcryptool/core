// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.views.nodes.keys;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;

/**
 * @author tkern
 * 
 */
public class SecretKeyNode extends AbstractKeyNode {

    private IKeyStoreAlias alias;

    public SecretKeyNode(IKeyStoreAlias alias) {
        super(alias.getOperation() + " (" + Messages.getString("Label.KeyStrength") + ": " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ((alias.getKeyLength() > 0) ? alias.getKeyLength() : "n/a") + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        this.alias = alias;
    }

    public IKeyStoreAlias getAlias() {
        return alias;
    }

    /**
     * @see org.jcryptool.crypto.keystore.ui.views.nodes.TreeNode#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
    	return ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/kgpg_key1.png");
    }

}

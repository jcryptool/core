// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.PrivateKeyNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.SecretKeyNode;

/**
 * @author Anatoli Barski
 * 
 */
public class ShowSecretKeyDialog extends CommonPropertyDialog {

    /**
     * @wbp.parser.constructor
     */
    public ShowSecretKeyDialog(Shell parentShell, SecretKeyNode secretKeyNode) {
        super(parentShell, secretKeyNode);
    }

    public ShowSecretKeyDialog(Shell parentShell, PrivateKeyNode privateKeyNode) {
        super(parentShell, privateKeyNode);
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(Messages.getString("secretkey.dialog.title") + "\n" + treeNode.getName()); //$NON-NLS-1$
        setTitleImage(ImageService.getImage(KeyStorePlugin.PLUGIN_ID, "icons/48x48/kgpg_key1.png"));
        Composite container = (Composite) super.createDialogArea(parent);

        return container;
    }

}

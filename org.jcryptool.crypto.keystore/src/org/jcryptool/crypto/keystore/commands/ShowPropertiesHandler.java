// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.ui.dialogs.CommonPropertyDialog;
import org.jcryptool.crypto.keystore.ui.dialogs.ShowCertificateDialog;
import org.jcryptool.crypto.keystore.ui.dialogs.ShowSecretKeyDialog;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.CertificateNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.PrivateKeyNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.SecretKeyNode;

/**
 * @author Anatoli Barski
 */
public class ShowPropertiesHandler extends AbstractHandler {

    /*
     * (non-Javadoc)
     * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {

        ISelection selection = HandlerUtil.getCurrentSelection(event);
        Object selectedNode = ((IStructuredSelection) selection).getFirstElement();

        CommonPropertyDialog keyDialog = null;
        

        if (selectedNode instanceof SecretKeyNode) {
            keyDialog = new ShowSecretKeyDialog(HandlerUtil.getActiveShell(event), (SecretKeyNode) selectedNode);
        } else if (selectedNode instanceof PrivateKeyNode) {
            keyDialog = new ShowSecretKeyDialog(HandlerUtil.getActiveShell(event), (PrivateKeyNode) selectedNode);
        } else if (selectedNode instanceof CertificateNode) {
            keyDialog = new ShowCertificateDialog(HandlerUtil.getActiveShell(event), (CertificateNode) selectedNode);
        } else {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "unsupported key node type");
            return null;
        }
        keyDialog.create();
        keyDialog.getShell().setMinimumSize(400, 600);
        keyDialog.open();

        return null;
    }
}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.crypto.flexiprovider.operations.FlexiProviderOperationsPlugin;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.FlexiProviderOperationsView;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.EntryNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.keys.KeyNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.keys.KeyPairNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.keys.SecretKeyNode;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;
import org.jcryptool.crypto.keystore.ui.KeystoreWidget.Style;
import org.jcryptool.crypto.keystore.ui.dialogs.SelectKeyDialog;

/**
 * @author Anatoli Barski
 *
 */
public class SelectKeyHandler extends AbstractHandler {

    private boolean cancel = false;

    public Object execute(ExecutionEvent event) throws ExecutionException {

        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection instanceof IStructuredSelection) {
            Object selectedNode = ((IStructuredSelection) selection).getFirstElement();
            if (selectedNode instanceof KeyNode) {
                KeyNode keyNode = (KeyNode) selectedNode;

                IKeyStoreAlias selectionFromDialog = null;
                if (keyNode instanceof KeyPairNode) {
                    selectionFromDialog = select(Style.SHOW_PRIVATEKEYNODES | Style.SHOW_PUBLICKEYNODES, event);
                    if (this.cancel)
                        return null;
                    if (selectionFromDialog != null) {
                        if (!(selectionFromDialog.getKeyStoreEntryType() == KeyType.KEYPAIR_PRIVATE_KEY || selectionFromDialog
                                .getKeyStoreEntryType() == KeyType.KEYPAIR_PUBLIC_KEY)) {
                            selectionFromDialog = null;
                        }
                    }
                }

                if (keyNode instanceof SecretKeyNode) {
                    selectionFromDialog = select(Style.SHOW_SECRETKEYNODES, event);
                    if (this.cancel)
                        return null;
                    if (selectionFromDialog != null) {
                        if (!(selectionFromDialog.getKeyStoreEntryType() == KeyType.SECRETKEY)) {
                            selectionFromDialog = null;
                        }
                    }
                }

                if (selectionFromDialog == null) {
                    JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, FlexiProviderOperationsPlugin.PLUGIN_ID,
                            Messages.SelectKeyHandler_WrongKey));
                    return null;
                }

                FlexiProviderOperationsView operationsView = (FlexiProviderOperationsView) HandlerUtil
                        .getActivePart(event);
                EntryNode operationNode = operationsView.getFlexiProviderOperation();
                operationNode.setKeyStoreAlias(selectionFromDialog);
            }
        }

        return null;
    }

    /**
     * @param event
     * @param )
     * @return
     */
    private IKeyStoreAlias select(int options, ExecutionEvent event) {

        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

        String algorithm = null;

        SelectKeyDialog dialog = new SelectKeyDialog(window.getShell(), new Style(options), algorithm);
        dialog.create();
        dialog.open();

        IKeyStoreAlias selectionFromDialog = null;
        switch(dialog.getReturnCode())
        {
            case Window.OK:
                selectionFromDialog = dialog.getSelectedAlias();
                this.cancel = false;
                break;
            case Window.CANCEL:
                this.cancel = true;
                break;
        };

        return selectionFromDialog;
    }

}

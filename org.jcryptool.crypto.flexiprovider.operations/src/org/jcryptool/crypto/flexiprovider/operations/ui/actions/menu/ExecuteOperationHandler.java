// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.operations.FlexiProviderOperationsPlugin;
import org.jcryptool.crypto.flexiprovider.operations.engines.PerformOperationManager;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.ISelectedOperationListener;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;

public class ExecuteOperationHandler extends AbstractHandler {
    private ISelectedOperationListener listener;

    public ExecuteOperationHandler(ISelectedOperationListener listener) {
        this.listener = listener;
    }

    public Object execute(ExecutionEvent event) {
        IFlexiProviderOperation operation = listener.getFlexiProviderOperation();

        if (operation == null) {
            JCTMessageDialog.showInfoDialog(new Status(IStatus.WARNING, FlexiProviderOperationsPlugin.PLUGIN_ID,
                    Messages.ExecuteOperationAction_2));
            return (null);
        }
        if (isComplete(operation)) {
            PerformOperationManager.getInstance().firePerformOperation(operation);
        } else {
            showIncompleteDialog();
        }
        return (null);
    }

    private void showIncompleteDialog() {
        MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                Messages.ExecuteOperationAction_4, Messages.ExecuteOperationAction_5);
    }

    private boolean isComplete(IFlexiProviderOperation entry) {
        // all ciphers need the same parameters (signature almost as well)
        if (entry.getRegistryType().equals(RegistryType.ASYMMETRIC_BLOCK_CIPHER)
                || entry.getRegistryType().equals(RegistryType.ASYMMETRIC_HYBRID_CIPHER)
                || entry.getRegistryType().equals(RegistryType.BLOCK_CIPHER)
                || entry.getRegistryType().equals(RegistryType.CIPHER)
                || entry.getRegistryType().equals(RegistryType.SIGNATURE)) {

            if (entry.getOperation() == null || entry.getOperation().equals(OperationType.UNKNOWN)) {
                return false;
            }
            if (entry.getKeyStoreAlias() == null) {
                return false;
            }
            if (entry.getRegistryType().equals(RegistryType.SIGNATURE)) {
                if (entry.getInput() == null || entry.getSignature() == null) {
                    return false;
                }
            } else {
                if (entry.getInput() == null || entry.getOutput() == null) {
                    return false;
                }
            }
        } else if (entry.getRegistryType().equals(RegistryType.MAC)) {
            if (entry.getInput() == null || entry.getOutput() == null) {
                return false;
            }
            if (entry.getKeyStoreAlias() == null) {
                return false;
            }
        } else if (entry.getRegistryType().equals(RegistryType.MESSAGE_DIGEST)) {
            if (entry.getInput() == null || entry.getOutput() == null) {
                return false;
            }
        } else if (entry.getRegistryType().equals(RegistryType.SECURE_RANDOM)) {
            if (entry.getOutput() == null) {
                return false;
            }
        }
        return true;
    }
}

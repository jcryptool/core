// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.ISelectedOperationListener;
import org.jcryptool.crypto.flexiprovider.types.OperationType;

public class SelectSignatureHandler extends AbstractHandler {
    private IFlexiProviderOperation entryNode;
    private ISelectedOperationListener listener;

    public SelectSignatureHandler(ISelectedOperationListener listener) {
        // this.setText(Messages.SelectSignatureAction_0);
        // this.setToolTipText(Messages.SelectSignatureAction_1);
        this.listener = listener;
    }

    public Object execute(ExecutionEvent event) {
        this.entryNode = listener.getFlexiProviderOperation();
        try {
        	int dialogType = determineFileDialogType();
        	LogUtil.logInfo("select file for signature... (" + entryNode.getTimestamp() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        	FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), dialogType);
        	dialog.setFilterExtensions(new String[] {IConstants.ALL_FILTER_EXTENSION});
        	dialog.setFilterNames(new String[] {IConstants.ALL_FILTER_NAME});
        	dialog.setFilterPath(DirectoryService.getUserHomeDir());

        	String filename = dialog.open();
        	if (filename != null) {
        		entryNode.setSignature(filename);
        	}
        } catch(UndefinedOperationException ex) {
        	MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
        			null, Messages.SelectSignatureAction_2);
        }
        return(null);
    }

    /**
     * determineFileDialogType() -- for issue #75:  Signature file uses Open instead of Save
     * We need to check the operation type (sign, verify or UNKNOWN) and to adjust
     * the dialog type accordingly (SAVE for sign, OPEN for verify, Exception for UNKNOWN)
     * @return int (SWT.OPEN or SWT.SAVE)
     * @throws UndefinedOperationException if the operation is as yet undefined (UNKNOWN)
     */
    private int determineFileDialogType() throws UndefinedOperationException {
    	OperationType opType = entryNode.getOperation();
    	switch(opType) {
    	case SIGN:
    		return(SWT.SAVE);
    	case VERIFY:
    		return(SWT.OPEN);
    	case UNKNOWN:
    	default:
    		throw new UndefinedOperationException("must select operation type SIGN or VERIFY first");
    	}
    }
}

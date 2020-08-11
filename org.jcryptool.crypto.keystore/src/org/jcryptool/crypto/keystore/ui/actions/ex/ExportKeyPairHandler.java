// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2013, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.actions.ex;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.ImportExportManager;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.ui.views.interfaces.IViewKeyInformation;

/**
 * @author t-kern
 * @author Holger Friedrich (support for Commands, additional class based on ExportKeyPairAction)
 * 
 */
public class ExportKeyPairHandler extends AbstractHandler {
    private IViewKeyInformation info;

    /**
     * Creates a new instance of ExportKeyPairHandler
     */
    public ExportKeyPairHandler(IViewKeyInformation info) {
        this.info = info;
        // this.setText(Messages.getString("Label.ExportKeyPair")); //$NON-NLS-1$
        // this.setToolTipText(Messages.getString("Label.ExportKeyPair")); //$NON-NLS-1$
        // this.setImageDescriptor(KeyStorePlugin.getImageDescriptor("icons/16x16/kgpg_export.png")); //$NON-NLS-1$
    }

    @Override
	public Object execute(ExecutionEvent event) {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterExtensions(new String[] { Messages.getString("ExportKeyPairHandler.2") }); //$NON-NLS-1$
        dialog.setFilterNames(new String[] { Messages.getString("ExportKeyPairHandler.4") }); //$NON-NLS-1$
        dialog.setOverwrite(true);

        String filename = dialog.open();

        if (filename != null && info != null) {
            char[] password = promptPassword();

            if (password != null) {
                try {
                    ImportExportManager.getInstance().exportKeyPair(new Path(filename),
                            KeyStoreManager.getInstance().getPrivateKey(info.getSelectedKeyAlias(), password),
                            KeyStoreManager.getInstance().getCertificateChain(info.getSelectedKeyAlias(), password),
                            password);
                } catch (Exception ex) {
                    LogUtil.logError(KeyStorePlugin.PLUGIN_ID, Messages.getString("ExportKeyPairHandler.3"), ex, true); //$NON-NLS-1$
                }
            }
        }
        return(null);
    }

    private char[] promptPassword() {
        InputDialog dialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                Messages.getString("ExportKeyPairHandler.0"), Messages.getString("ExportKeyPairHandler.1"), "", null) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            @Override
			protected Control createDialogArea(Composite parent) {
                Control control = super.createDialogArea(parent);
                getText().setEchoChar('*');
                return control;
            }
        };

        if (dialog.open() == Window.OK) {
            return dialog.getValue().toCharArray();
        } else {
            return null;
        }
    }
}

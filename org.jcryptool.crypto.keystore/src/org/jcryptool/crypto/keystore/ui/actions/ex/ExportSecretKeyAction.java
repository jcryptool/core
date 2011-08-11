//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.actions.ex;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
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
 * @author tkern
 *
 */
public class ExportSecretKeyAction extends Action {
    private IViewKeyInformation info;

    /**
     * Creates a new instance of ExportSecretKeyAction
     */
    public ExportSecretKeyAction(IViewKeyInformation info) {
        this.info = info;
        this.setText(Messages.getString("Label.ExportSecretKey")); //$NON-NLS-1$
        this.setToolTipText(Messages.getString("Label.ExportSecretKey")); //$NON-NLS-1$
        this.setImageDescriptor(KeyStorePlugin.getImageDescriptor("icons/16x16/kgpg_export.png")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterExtensions(new String[] {"*.sec"});
        dialog.setOverwrite(true);

        String filename = dialog.open();

        if (filename != null) {
            IPath iPath = new Path(filename);
            if (iPath != null && info != null) {
                char[] password = promptPassword();
                try {
                    ImportExportManager.getInstance().exportSecretKey(
                            iPath,
                            KeyStoreManager.getInstance().getSecretKey(info.getSelectedKeyAlias(),
                                    password));
                } catch (Exception e) {
                    LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
                }
            }
        }
    }

    private char[] promptPassword() {
        InputDialog dialog = new InputDialog(
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                Messages.getString("ExportSecretKeyAction.0"), Messages.getString("ExportSecretKeyAction.1"), //$NON-NLS-1$ //$NON-NLS-2$
                "", null) { //$NON-NLS-1$

            protected Control createDialogArea(Composite parent) {
                Control control = super.createDialogArea(parent);
                getText().setEchoChar('*');
                return control;
            }
        };
        int result = dialog.open();
        if (result == Window.OK) {
            return dialog.getValue().toCharArray();
        } else {
            return null;
        }
    }

}

// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2013, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.actions.ex;

import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.ImportExportManager;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.ui.views.interfaces.IViewKeyInformation;

/**
 * @author t-kern
 * @author Holger Friedrich (support for Commands, additional class based on ExportCertificateAction)
 * 
 */
public class ExportCertificateHandler extends AbstractHandler {
    private IViewKeyInformation info;

    /**
     * Creates a new instance of ExportCertificateHandler
     */
    public ExportCertificateHandler(IViewKeyInformation info) {
        this.info = info;
        // this.setText(Messages.getString("Label.ExportPublicKey")); //$NON-NLS-1$
        // this.setToolTipText(Messages.getString("Label.ExportPublicKey")); //$NON-NLS-1$
        // this.setImageDescriptor(KeyStorePlugin.getImageDescriptor("icons/16x16/kgpg_export.png")); //$NON-NLS-1$
    }

    @Override
	public Object execute(ExecutionEvent event) {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterExtensions(new String[] { Messages.getString("ExportCertificateHandler.0") }); //$NON-NLS-1$
        dialog.setFilterNames(new String[] { Messages.getString("ExportCertificateHandler.1") }); //$NON-NLS-1$
        dialog.setOverwrite(true);

        String filename = dialog.open();

        if (filename != null && info != null) {
            try {
                ImportExportManager.getInstance().exportCertificate(new Path(filename),
                        KeyStoreManager.getInstance().getCertificate(info.getSelectedKeyAlias()));
            } catch (UnrecoverableEntryException e) {
                LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
            } catch (NoSuchAlgorithmException e) {
                LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
            }
        }
        return(null);
    }
}

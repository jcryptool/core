// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.ui.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.actions.core.registry.ActionCascadeService;
import org.jcryptool.actions.ui.ActionsUIPlugin;
import org.jcryptool.actions.ui.preferences.PreferenceConstants;
import org.jcryptool.actions.ui.utils.Constants;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.directories.DirectoryService;

/**
 * <b>Export handler</b> for the Actions view. Exports the complete action cascade into a specified XML file.
 *
 * @author Dominik Schadow
 * @version 0.9.3
 */
public class ExportHandler extends AbstractHandler {
    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        ActionCascadeService service = ActionCascadeService.getInstance();

        if (service.getCurrentActionCascade() != null && service.getCurrentActionCascade().getSize() > 0) {
            FileDialog dialog = new FileDialog(HandlerUtil.getActiveShell(event), SWT.SAVE);
            dialog.setFilterPath(DirectoryService.getUserHomeDir());
            dialog.setFileName(service.getCurrentActionCascade().getName() + ".xml"); //$NON-NLS-1$
            dialog.setFilterNames(Constants.FILTER_NAMES);
            dialog.setFilterExtensions(Constants.FILTER_EXTENSIONS);
            dialog.setOverwrite(true);

            String filename = dialog.open();

            if (filename != null && filename.length() > 0) {
                Writer fw = null;
                boolean confirmOverwrite = false;
                IPreferenceStore store = ActionsUIPlugin.getDefault().getPreferenceStore();
                boolean storePasswords = store.getBoolean(PreferenceConstants.P_STORE_PASSWORDS);

                try {
                    File file = new File(filename);

                    if (file.exists()) {
                        confirmOverwrite = MessageDialog.openConfirm(HandlerUtil.getActiveShell(event),
                                Messages.ExportHandler_1,
                                NLS.bind(Messages.ExportHandler_2, new Object[] {file.getName()}));
                    }

                    if (!file.exists() || confirmOverwrite) {
                        fw = new FileWriter(file);
                        service.getCurrentActionCascade().setSavePasswords(storePasswords);
                        fw.write(service.getCurrentActionCascade().toString());
                        fw.flush();
                    }
                } catch (IOException ex) {
                    LogUtil.logError(ActionsUIPlugin.PLUGIN_ID, Messages.ExportHandler_4, ex, true);
                } finally {
                    if (fw != null) {
                        try {
                            fw.close();
                        } catch (IOException ex) {
                            LogUtil.logError(ActionsUIPlugin.PLUGIN_ID,
                                    "Could not close FileOutputStream for action cascade", ex, false); //$NON-NLS-1$
                        }
                    }
                }
            }
        } else {
            MessageDialog.openInformation(HandlerUtil.getActiveShell(event), Messages.ExportHandler_3,
                    Messages.ExportHandler_5);
        }

        return null;
    }
}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.actions.core.registry.ActionCascadeService;
import org.jcryptool.actions.core.utils.ImportUtils;
import org.jcryptool.actions.ui.utils.Constants;
import org.jcryptool.actions.ui.views.ActionView;
import org.jcryptool.core.util.directories.DirectoryService;

/**
 * <b>Import handler</b> for the Actions view. Imports a complete action cascade from an XML file
 * into the Actions view.
 *
 * @author Dominik Schadow
 * @version 0.9.3
 */
public class ImportHandler extends AbstractHandler {
    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
    	ActionCascadeService service = ActionCascadeService.getInstance();

    	if (service.getCurrentActionCascade() != null && service.getCurrentActionCascade().getSize()>0){
            boolean confirmImport = MessageDialog
                    .openConfirm(HandlerUtil.getActiveShell(event), Messages.ImportHandler_0,
                            Messages.ImportHandler_1);
            if (!confirmImport) {
                return null;
            }
        }

    	ActionView view = (ActionView) HandlerUtil.getActivePart(event);

        FileDialog dialog = new FileDialog(HandlerUtil.getActiveShell(event), SWT.OPEN);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterNames(Constants.FILTER_NAMES);
        dialog.setFilterExtensions(Constants.FILTER_EXTENSIONS);
        String filename = dialog.open();

        if (filename != null && filename.length() > 0) {
            view.setImportPath(ImportUtils.getPathFromFile(filename));

            ImportUtils importUtil = new ImportUtils(filename);
            boolean isValid = importUtil.validateActionCascade();

            if (isValid) {
            	service.setCurrentActionCascade(importUtil.createActionCascade());
            } else {
                MessageDialog.openInformation(HandlerUtil.getActiveShell(event), Messages.ImportHandler_2,
                Messages.ImportHandler_3);
            }
        }

        return null;
    }
}

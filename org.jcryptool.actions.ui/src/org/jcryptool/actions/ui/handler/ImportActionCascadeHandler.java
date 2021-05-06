// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.ui.handler;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.actions.core.registry.ActionCascadeService;
import org.jcryptool.actions.core.utils.ImportUtils;
import org.jcryptool.actions.ui.ActionsUIPlugin;
import org.jcryptool.actions.ui.views.ActionView;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * <b>Import handler</b> for the Actions view. Imports a complete action cascade from an XML file
 * into the Actions view.
 *
 * @author Dominik Schadow
 * @version 0.9.3
 */
public class ImportActionCascadeHandler extends AbstractHandler {
    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        ActionCascadeService service = ActionCascadeService.getInstance();

        if (service.getCurrentActionCascade() != null
                && service.getCurrentActionCascade().getSize() > 0) {
            boolean confirmImport =
                    MessageDialog.openConfirm(HandlerUtil.getActiveShell(event),
                            Messages.ImportHandler_0, Messages.ImportHandler_1);
            if (!confirmImport) {
                return null;
            }
        }

        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection instanceof IStructuredSelection) {
            IFileStore file = (IFileStore) ((IStructuredSelection) selection).getFirstElement();

            if (file != null) {
                ImportUtils importUtil =
                        new ImportUtils(file.getParent().toURI().getPath() + File.separatorChar
                                + file.getName());

                if (importUtil.validateActionCascade()) {
                    service.setCurrentActionCascade(importUtil.createActionCascade());

                    try {
                        HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView(
                                ActionView.ID);
                    } catch (PartInitException ex) {
                        LogUtil.logError(ActionsUIPlugin.PLUGIN_ID,
                                Messages.ImportActionCascadeHandler_0, ex, true);
                    }
                } else {
                    MessageDialog.openInformation(HandlerUtil.getActiveShell(event),
                            Messages.ImportHandler_2, Messages.ImportHandler_3);
                }
            }
        }

        return null;
    }
}

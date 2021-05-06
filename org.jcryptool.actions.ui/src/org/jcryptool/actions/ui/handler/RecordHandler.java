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

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.services.IEvaluationService;
import org.jcryptool.actions.core.registry.ActionCascadeService;
import org.jcryptool.actions.core.types.ActionCascade;
import org.jcryptool.actions.ui.ActionsUIPlugin;
import org.jcryptool.actions.ui.preferences.PreferenceConstants;
import org.jcryptool.actions.ui.views.ActionView;

/**
 * <b>Record handler</b> for the Actions view. Records a new action cascade or appends new actions
 * to the existing one. Activating the record button does never delete any action information
 * collected before.
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class RecordHandler extends AbstractHandler implements IElementUpdater {
    private boolean doRecord = false;

    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        ICommandService service =
                PlatformUI.getWorkbench().getService(ICommandService.class);
        Command command = service.getCommand("org.jcryptool.actions.recordCommand"); //$NON-NLS-1$
        State state = command.getState("org.jcryptool.actions.recordCommand.toggleState"); //$NON-NLS-1$
        doRecord = !(Boolean) state.getValue();

        if (doRecord && HandlerUtil.getActivePart(event) instanceof ActionView) {
            boolean launched =
                    startRecording(HandlerUtil.getActiveEditor(event),
                            (ActionView) HandlerUtil.getActivePart(event));

            if (launched) {
                state.setValue(doRecord);
                service.refreshElements(event.getCommand().getId(), null);
            }
        } else {
            state.setValue(doRecord);
            service.refreshElements(event.getCommand().getId(), null);
        }

        final IWorkbenchWindow ww = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        final IEvaluationService evalService =
                ww.getService(IEvaluationService.class);
        if (evalService != null) {
            evalService.requestEvaluation("org.jcryptool.actions.isDoRecord"); //$NON-NLS-1$
        }

        return null;
    }

    private boolean startRecording(IEditorPart editor, ActionView view) {
        String activeEditorFile = editor.getTitle();

        if (activeEditorFile.contains(".")) { //$NON-NLS-1$
            activeEditorFile = activeEditorFile.substring(0, activeEditorFile.indexOf(".")); //$NON-NLS-1$
        }

        ActionCascade cascade = null;

        if (view.hasContent()) {
            return true;
        } else {
            cascade = new ActionCascade(activeEditorFile + "Cascade"); //$NON-NLS-1$
            ActionsUIPlugin.getDefault().getPreferenceStore().getBoolean(
                    PreferenceConstants.P_STORE_PASSWORDS);
            ActionCascadeService.getInstance().setCurrentActionCascade(cascade);

            return true;
        }
    }

    @Override
	@SuppressWarnings("rawtypes")
	public void updateElement(UIElement element, Map parameters) {
        element.setChecked(doRecord);
    }
}

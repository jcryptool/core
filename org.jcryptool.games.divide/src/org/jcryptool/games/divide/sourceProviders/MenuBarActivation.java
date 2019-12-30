// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.sourceProviders;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

public class MenuBarActivation {
    private MenuBarActivation() {
    }

    public static void enableNewGameState(boolean isEnabled) {
        ISourceProviderService service = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getService(ISourceProviderService.class);
        if (service != null) {
            NewGameStateSourceProvider provider = (NewGameStateSourceProvider) service
                    .getSourceProvider(NewGameStateSourceProvider.NEW_GAME_COMMAND_STATE);
            if (provider != null) {
                provider.setState(isEnabled);
            }
        }
    }

    public static void enableSaveGameState(boolean isEnabled) {
        ISourceProviderService service = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getService(ISourceProviderService.class);
        if (service != null) {
            SaveStateSourceProvider provider = (SaveStateSourceProvider) service
                    .getSourceProvider(SaveStateSourceProvider.SAVE_COMMAND_STATE);
            if (provider != null) {
                provider.setState(isEnabled);
            }
        }
    }

    public static void enableUndo(boolean isEnabled) {
        ISourceProviderService service = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getService(ISourceProviderService.class);
        if (service != null) {
            UndoSourceProvider provider = (UndoSourceProvider) service
                    .getSourceProvider(UndoSourceProvider.UNDO_COMMAND_STATE);
            if (provider != null) {
                provider.setState(isEnabled);
            }
        }
    }

    public static void enableRedo(boolean isEnabled) {
        ISourceProviderService service = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getService(ISourceProviderService.class);
        if (service != null) {
            RedoSourceProvider provider = (RedoSourceProvider) service
                    .getSourceProvider(RedoSourceProvider.REDO_COMMAND_STATE);
            if (provider != null) {
                provider.setState(isEnabled);
            }
        }
    }
}

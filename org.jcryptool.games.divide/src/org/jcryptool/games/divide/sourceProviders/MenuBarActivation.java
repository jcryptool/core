package org.jcryptool.games.divide.sourceProviders;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

public class MenuBarActivation {

	public static void enableNewGameState(boolean isEnabled) {
		ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ISourceProviderService.class);
		if (service != null) {
			NewGameStateSourceProvider provider = (NewGameStateSourceProvider) service.getSourceProvider(NewGameStateSourceProvider.NEW_GAME_COMMAND_STATE);
			if (provider != null) {
				provider.setState(isEnabled);
			}
		}
	}
	
	public static void enableUndo(boolean isEnabled) {
		ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ISourceProviderService.class);
		if (service != null) {
			UndoSourceProvider provider = (UndoSourceProvider) service.getSourceProvider(UndoSourceProvider.UNDO_COMMAND_STATE);
			if (provider != null) {
				provider.setState(isEnabled);
			}
		}
	}
	
	public static void enableRedo(boolean isEnabled) {
		ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ISourceProviderService.class);
		if (service != null) {
			RedoSourceProvider provider = (RedoSourceProvider) service.getSourceProvider(RedoSourceProvider.REDO_COMMAND_STATE);
			if (provider != null) {
				provider.setState(isEnabled);
			}
		}
	}
}

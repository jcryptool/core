package org.jcryptool.games.divide.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.divide.logic.GameMachine;
import org.jcryptool.games.divide.views.DivideView;

public class RedoHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart workbench = HandlerUtil.getActivePart(event);
		
		if (workbench != null && workbench instanceof DivideView) {
			DivideView view = (DivideView) HandlerUtil.getActivePart(event);
			GameMachine gameMachine = view.getGameMachine();
			if (gameMachine != null) {
				gameMachine.redo();
			}
		}
		
		return null;
	}
}

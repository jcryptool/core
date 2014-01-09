package org.jcryptool.games.zudoku.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.games.zudoku.ZudokuPlugin;

public class HelpHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	    PlatformUI.getWorkbench().getHelpSystem().displayHelp(ZudokuPlugin.PLUGIN_ID + ".view"); //$NON-NLS-1$
		return null;
	}
}
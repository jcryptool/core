package org.jcryptool.visual.hashing.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.hashing.views.HashingView;

/**
 * @author Ferit Dogan
 */
public class RestartHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (HandlerUtil.getActivePart(event) instanceof HashingView) {
			HashingView view = ((HashingView) HandlerUtil.getActivePart(event));

			view.resetView();
		}
		return null;
	}
}

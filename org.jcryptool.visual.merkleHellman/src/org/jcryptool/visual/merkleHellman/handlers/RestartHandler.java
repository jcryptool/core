package org.jcryptool.visual.merkleHellman.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.merkleHellman.views.MerkleHellmanView;

/**
 * @author Ferit Dogan
 */
public class RestartHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (HandlerUtil.getActivePart(event) instanceof MerkleHellmanView) {
			MerkleHellmanView view = ((MerkleHellmanView) HandlerUtil.getActivePart(event));

			view.resetView();
			view.init();
		}
		return null;
	}
}

package org.jcryptool.visual.merkletree.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.merkletree.MerkleTreeView;;

/**
 * @author Miray Inel
 */
public class RestartHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (HandlerUtil.getActivePart(event) instanceof MerkleTreeView) {
			MerkleTreeView view = ((MerkleTreeView) HandlerUtil.getActivePart(event));

			view.resetView();
		}
		return null;
	}
}
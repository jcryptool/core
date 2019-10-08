package org.jcryptool.visual.ECDH.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.ECDH.ui.view.ECDHView;

public class RestartHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (HandlerUtil.getActivePart(event) instanceof ECDHView) {
			ECDHView view = ((ECDHView) HandlerUtil.getActivePart(event));

			view.reset();
		}
		return null;
	}

}

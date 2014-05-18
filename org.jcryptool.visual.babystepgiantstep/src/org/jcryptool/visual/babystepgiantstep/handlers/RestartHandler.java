package org.jcryptool.visual.babystepgiantstep.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.babystepgiantstep.views.BabystepGiantstepView;

/**
 * @author Miray Inel
 */
public class RestartHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (HandlerUtil.getActivePart(event) instanceof BabystepGiantstepView) {
			BabystepGiantstepView view = ((BabystepGiantstepView) HandlerUtil.getActivePart(event));

			view.resetView();
		}
		return null;
	}

}

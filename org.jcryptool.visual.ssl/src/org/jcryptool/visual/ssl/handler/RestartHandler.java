package org.jcryptool.visual.ssl.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.ssl.views.SslView;

/*
 * 
 */
public class RestartHandler extends AbstractHandler{
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (HandlerUtil.getActivePart(event) instanceof SslView) {
			SslView view = ((SslView) HandlerUtil.getActivePart(event));
			view.resetStep();
		}
		return null;
	}
}
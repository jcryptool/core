package org.jcryptool.visual.euclid.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.euclid.View;

/**
 * 
 * @author Thorben Groos
 *
 */
public class ExportToCSVHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		if (HandlerUtil.getActivePart(event) instanceof View) {
			View view = ((View) HandlerUtil.getActivePart(event));
			view.getExtendedEuclid().exportToCSV();
		}
		
		return null;
	}

}

package org.jcryptool.visual.huffmanCoding.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.huffmanCoding.views.HuffmanCodingView;

/**
 * 
 * @author Miray Inel
 *
 */
public class ChangeLayout extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart findView = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().findView(HuffmanCodingView.ID);
		HuffmanCodingView view = (HuffmanCodingView) findView;
		view.setLayoutManager();
		return null;
	}

}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.huffmanCoding.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.huffmanCoding.views.HuffmanCodingView;
import org.jcryptool.visual.huffmanCoding.views.HuffmanCodingViewTree;

/**
 * This handler changes the layout of the ZEST graph on the second tab in the huffman plugin
 * The input comes from the upper right corner item change layout (3 dots) which issues the
 * command org.jcryptool.visual.huffmanCoding.command.ChangeLayout.
 *
 * @author Miray Inel
 *
 */
public class ChangeLayout extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart findView = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().findView(HuffmanCodingView.ID);
		HuffmanCodingViewTree view = ((HuffmanCodingView) findView).getViewTree();

		if (view != null)
			view.setLayoutManager();
		return null;
	}

}

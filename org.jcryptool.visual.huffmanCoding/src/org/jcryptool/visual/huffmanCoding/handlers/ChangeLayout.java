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

/**
 * NOTE: THIS CLASS IS CURRENTLY UNUSED. IT MAY BE USED FOR GRAPH EXTENSION
 * FEATURES LIKE ZOOMING/CHANGING LAYOUT SO I LET THIS ONE IN
 * (michael@altenhuber.net)
 * 
 * @author Miray Inel
 *
 */
public class ChangeLayout extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// IViewPart findView =
		// HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().findView(HuffmanCodingView.ID);
		// HuffmanCodingView view = (HuffmanCodingView) findView;
		// view.setLayoutManager();
		return null;
	}

}

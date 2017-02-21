package org.jcryptool.visual.zeroknowledge.handlers;

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.zeroknowledge.views.FeigeFiatShamirView;
import org.jcryptool.visual.zeroknowledge.views.FiatShamirView;
import org.jcryptool.visual.zeroknowledge.views.GraphenisomorphieView;
import org.jcryptool.visual.zeroknowledge.views.MagicDoorView;

/**
 * This handler starts a new game.
 * 
 * @author Johannes Späth
 * @version 0.9.5
 */
public class RestartHandler extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart actUtil = HandlerUtil.getActivePart(event);
		if (actUtil instanceof FeigeFiatShamirView) {
			FeigeFiatShamirView view = ((FeigeFiatShamirView) HandlerUtil
					.getActivePart(event));
			view.reset();
		}
		if (actUtil instanceof GraphenisomorphieView) {
			GraphenisomorphieView view = ((GraphenisomorphieView) HandlerUtil
					.getActivePart(event));
			view.reset();
		}

		if (actUtil instanceof FiatShamirView) {
			FiatShamirView view = ((FiatShamirView) HandlerUtil
					.getActivePart(event));
			view.reset();
		}
		if (actUtil instanceof MagicDoorView) {
			MagicDoorView view = ((MagicDoorView) HandlerUtil
					.getActivePart(event));
			view.reset();
		}
		return null;
	}
}

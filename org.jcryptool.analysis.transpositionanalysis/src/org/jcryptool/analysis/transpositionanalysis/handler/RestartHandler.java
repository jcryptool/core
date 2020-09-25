// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.analysis.transpositionanalysis.views.TranspAnalysisView;

/**
 * This handler starts a new analysis.
 *
 * @author Thorben Groos
 * @version 0.9.5
 */
public class RestartHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (HandlerUtil.getActivePart(event) instanceof TranspAnalysisView) {
			TranspAnalysisView view = ((TranspAnalysisView) HandlerUtil.getActivePart(event));
			view.resetClick();
		}

		return null;
	}
}
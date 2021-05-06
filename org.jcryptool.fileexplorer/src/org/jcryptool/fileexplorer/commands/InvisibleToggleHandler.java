//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.fileexplorer.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.fileexplorer.views.FileExplorerView;

/**
 *
 *
 * @author Simon L
 */
public class InvisibleToggleHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			Widget w = ((Event) event.getTrigger()).widget;

			boolean showInvisibleFiles = false;
			if (w instanceof MenuItem) {
				showInvisibleFiles = ((MenuItem) w).getSelection();
			} else if (w instanceof ToolItem) {
				showInvisibleFiles = ((ToolItem) w).getSelection();
			} else {
				LogUtil.logError("Invisibile file show toggle: Couldn't determine widget type");
			}

			((FileExplorerView) HandlerUtil.getActivePart(event)).setHideInvisible(!showInvisibleFiles);
		} catch (ClassCastException e) {
			LogUtil.logError("Cast exception when retrieving toggle invisible file display state.", e); //$NON-NLS-1$
		}

		return null;
	}
}

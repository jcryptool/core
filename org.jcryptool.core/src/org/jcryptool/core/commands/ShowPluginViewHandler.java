// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.jcryptool.core.CorePlugin;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * Shows the selected view. In case of <b>Visualizations</b> or <b>Games</b> the view will be shown in maximized state.
 * 
 * @author mwalthart
 * @author Holger Friedrich (support for Commands, new class based on ShowPluginViewAction)
 * @version 0.6.1
 */
public class ShowPluginViewHandler extends AbstractHandler {
    private String viewId;
    private String name;

    public ShowPluginViewHandler(String viewId, String name) {
        super();
        this.viewId = viewId;
        this.name = name;
    }

    public String getText() {
        return name;
    }
    
    public void setText(String name) {
        this.name = name;
    }

    @Override
	public boolean isEnabled() {
        return true;
    }

    @Override
	public Object execute(ExecutionEvent event) {
        try {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            page.showView(viewId);
            
            // This was used to start all visualizations or games in a maximized window.
            // This causes Problem when using the "Restore" icons in the JCT. They don't work anymore.
//            if (viewId.contains("org.jcryptool.games") || viewId.contains("org.jcryptool.visual")) { //$NON-NLS-1$ //$NON-NLS-2$
//                page.setPartState(page.getActivePartReference(), IWorkbenchPage.STATE_MAXIMIZED);
//            }
        } catch (WorkbenchException ex) {
            LogUtil.logError(CorePlugin.PLUGIN_ID, ex);
        }
        return(null);
    }
}

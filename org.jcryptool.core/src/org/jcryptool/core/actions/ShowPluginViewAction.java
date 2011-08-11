// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.jcryptool.core.CorePlugin;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * Shows the selected view. In case of <b>Visualizations</b> or <b>Games</b> the view will be shown in maximized state.
 * 
 * @author mwalthart
 * @version 0.6.0
 */
public class ShowPluginViewAction extends Action {
    private String viewId;
    private String name;

    public ShowPluginViewAction(String viewId, String name) {
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

    public boolean isEnabled() {
        return true;
    }

    public void run() {
        try {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            page.showView(viewId);
            
            if (viewId.contains("org.jcryptool.games") || viewId.contains("org.jcryptool.visual")) { //$NON-NLS-1$ //$NON-NLS-2$
                page.setPartState(page.getActivePartReference(), IWorkbenchPage.STATE_MAXIMIZED);
            }
        } catch (WorkbenchException ex) {
            LogUtil.logError(CorePlugin.PLUGIN_ID, ex);
        }
    }

    public void runWithEvent(Event event) {
        run();
    }
}

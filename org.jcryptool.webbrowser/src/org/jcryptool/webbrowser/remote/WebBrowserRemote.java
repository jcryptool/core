//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.webbrowser.remote;

import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.webbrowser.BrowserPlugin;
import org.jcryptool.webbrowser.ui.BrowserView;

/**
 * Implementation of a class to control the web browser view.
 *
 * @version 0.5.0
 * @author mwalthart
 */
public class WebBrowserRemote {
    private static final String VIEW_ID = "org.jcryptool.webbrowser.browser"; //$NON-NLS-1$

    /**
     * Opens the given URL in the web browser.
     *
     * @param url the URL to open
     */
    public void openUrl(String url) {
        BrowserView.openUrl(url);
    }

    /**
     * Opens a web browser or brings it to front.
     */
    public void showBrowser() {
        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(VIEW_ID);
        } catch (Exception ex) {
            LogUtil.logError(BrowserPlugin.PLUGIN_ID, ex);
        }
    }

    /**
     * Hides an opened web browser.
     */
    public void hideBrowser() {
        try {
            IViewReference[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getActivePage().getViewReferences();
            for (IViewReference view : views) {
                if (view.getId().equalsIgnoreCase(VIEW_ID)) {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(view);
                    return;
                }
            }
        } catch (Exception ex) {
            LogUtil.logError(BrowserPlugin.PLUGIN_ID, ex);
        }
    }
}

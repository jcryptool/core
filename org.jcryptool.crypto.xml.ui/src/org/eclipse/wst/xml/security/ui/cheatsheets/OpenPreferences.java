/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.cheatsheets;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;

/**
 * <p>Cheat Sheet action. Opens the preferences of the current workspace and jumps directly to the
 * general <b>XML Security</b> node located in the <b>XML</b> category.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class OpenPreferences extends Action implements ICheatSheetAction {
    /**
     * Opens the preferences dialog and selects the general <i>XML Security</i> node.
     *
     * @param params Parameters for the action
     * @param cheatSheetManager The Cheat Sheet manager
     */
    public void run(final String[] params, final ICheatSheetManager cheatSheetManager) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        PreferenceManager preferenceManager = workbench.getPreferenceManager();
        if (preferenceManager != null) {
            PreferenceDialog dialog = new PreferenceDialog(workbench.getActiveWorkbenchWindow()
                    .getShell(), preferenceManager);
            dialog.setSelectedNode("org.eclipse.wst.xml.security.ui.preferences.XmlSecurity");
            dialog.create();
            dialog.open();
        }
    }
}

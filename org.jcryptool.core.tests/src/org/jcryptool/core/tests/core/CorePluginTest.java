// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.tests.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.jcryptool.core.CorePlugin;
import org.junit.Test;

/**
 * Tests for the JCrypTool Core plug-in and runtime.
 *
 * @author Dominik Schadow
 * @version 0.9.4
 */
public class CorePluginTest {
    private static final String CORE_PLUGIN_ID = "org.jcryptool.core";

    @Test
    public void checkPluginID() {
        assertEquals(CORE_PLUGIN_ID, CorePlugin.getDefault().toString());
    }

    @Test
    public void testViews() {
        IWorkbenchWindow activeWorkbenchWindow = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
        IViewReference[] viewReferences = activePage.getViewReferences();

        assertEquals(5, viewReferences.length);

        assertEquals("org.jcryptool.fileexplorer.views.FileExplorerView", viewReferences[0].getId());
        assertEquals("File Explorer", viewReferences[0].getPartName());

        assertEquals("org.jcryptool.actions.views.ActionView", viewReferences[1].getId());
        assertEquals("Actions", viewReferences[1].getPartName());

        assertEquals("org.jcryptool.core.views.AlgorithmView", viewReferences[2].getId());
        assertEquals("Crypto Explorer", viewReferences[2].getPartName());

        assertEquals("org.eclipse.help.ui.HelpView", viewReferences[3].getId());
        assertEquals("Help", viewReferences[3].getPartName());

        assertEquals("org.eclipse.ui.internal.introview", viewReferences[4].getId());
        assertEquals("Welcome", viewReferences[4].getPartName());

        String[] shortcuts = activePage.getShowViewShortcuts();
        assertEquals(6, shortcuts.length);
        assertEquals("org.eclipse.ui.console.ConsoleView", shortcuts[0]);
        assertEquals("org.jcryptool.core.views.AlgorithmView", shortcuts[1]);
        assertEquals("org.jcryptool.fileexplorer.views.FileExplorerView", shortcuts[2]);
        assertEquals("org.eclipse.help.ui.HelpView", shortcuts[3]);
        assertEquals("org.jcryptool.actions.views.ActionView", shortcuts[4]);
        assertEquals("org.jcryptool.webbrowser.browser", shortcuts[5]);
    }

    @Test
    public void testPerspectives() {
        IWorkbenchWindow activeWorkbenchWindow = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();

        IPerspectiveDescriptor perspective = activePage.getPerspective();
        assertEquals("org.jcryptool.core.perspective", perspective.getId());
        assertEquals("Default", perspective.getLabel());

        String[] shortcuts = activePage.getPerspectiveShortcuts();
        assertEquals(1, shortcuts.length);
        assertEquals("org.jcryptool.crypto.flexiprovider.ui.perspective.FlexiProviderPerspective", shortcuts[0]);
    }

    @Test
    public void testEditors() {
        IWorkbenchWindow activeWorkbenchWindow = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
        IEditorRegistry registry = activeWorkbenchWindow.getWorkbench().getEditorRegistry();

        assertEquals("org.jcryptool.editor.text.editor.JCTTextEditor", registry.getDefaultEditor("test.txt").getId());
        assertEquals("org.jcryptool.editors.hex.HexEditor", registry.getDefaultEditor("*.*").getId());
    }

    @Test
    public void testAddNewView() {
        IWorkbenchWindow activeWorkbenchWindow = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();

        int viewCountBefore = activePage.getViewReferences().length;
        assertEquals(5, viewCountBefore);

        try {
            activePage.showView("org.eclipse.ui.console.ConsoleView");
        } catch (PartInitException ex) {
            fail(ex.getMessage());
        }

        int viewCountAfter = activePage.getViewReferences().length;
        assertTrue(viewCountBefore == viewCountAfter - 1);
    }
}

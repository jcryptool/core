// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;

/**
 * The Perspective class generates an initial page layout and visible action set for a page.
 *
 * @author mwalthart
 *
 * @version 0.6.0
 */
public class Perspective implements IPerspectiveFactory {
    /** The default perspective ID (JCrypTool). */
    public static final String PERSPECTIVE_ID = "org.jcryptool.core.perspective"; //$NON-NLS-1$

    /**
     * Add views to the default perspective using <b>org.eclipse.ui.perspectiveExtensions</b> in
     * plugin.xml.
     */
    @Override
	public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();

        IPlaceholderFolderLayout flGames = layout.createPlaceholderFolder(
                "org.jcryptool.games", IPageLayout.BOTTOM, 0.5f, editorArea); //$NON-NLS-1$
        flGames.addPlaceholder("org.jcryptool.games.*"); //$NON-NLS-1$

        IPlaceholderFolderLayout flVisual = layout.createPlaceholderFolder(
                "org.jcryptool.visual", IPageLayout.BOTTOM, 0.5f, editorArea); //$NON-NLS-1$
        flVisual.addPlaceholder("org.jcryptool.visual.*"); //$NON-NLS-1$

        IPlaceholderFolderLayout flAnalysis = layout.createPlaceholderFolder(
                "org.jcryptool.analysis", IPageLayout.BOTTOM, 0.5f, editorArea); //$NON-NLS-1$
        flAnalysis.addPlaceholder("org.jcryptool.analysis.*"); //$NON-NLS-1$
    }
    
}

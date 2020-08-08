//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.ui.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;

/**
 * The standard FlexiProvider perspective.
 *
 * @author tkern
 * @version 0.6.0
 */
public class FlexiProviderPerspective implements IPerspectiveFactory {
	
	/**
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
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

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jcryptool.core.util.images.ImageService;

/**
 * The activator class controls the plug-in life cycle
 */
public class TranspositionAnalysisPlugin extends AbstractUIPlugin {
	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.jcryptool.analysis.transpositionanalysis"; //$NON-NLS-1$

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		reg.put("keyboardInputIcon", ImageService.getImage(PLUGIN_ID, "icons/keys.png"));
		reg.put("fileInputIcon", ImageService.ICON_FILE);
	}
}
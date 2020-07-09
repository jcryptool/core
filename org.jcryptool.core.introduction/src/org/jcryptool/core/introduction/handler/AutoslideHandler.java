// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.introduction.handler;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;
import org.jcryptool.core.introduction.views.AlgorithmInstruction;
import org.jcryptool.core.introduction.views.IntroductionPlugin;
import org.jcryptool.core.util.images.ImageService;

/**
 * This handler activates the automatic switching of images in the top right
 * corner (the plugin toolbar) of the plugin.
 * 
 * @author Thorben Groos thorben.groos@web.de
 *
 */
public class AutoslideHandler extends AbstractHandler implements IElementUpdater {
	
	public static boolean autoSlide = true;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (HandlerUtil.getActivePart(event) instanceof AlgorithmInstruction) {
			AlgorithmInstruction view = ((AlgorithmInstruction) HandlerUtil.getActivePart(event));
			// Invert the autoSlide variable in this class and the AlgorithmInstruction class
			// after the user pressed the icon.
			autoSlide = !view.getAutoSlide();
			view.setAutoSlide(autoSlide);
			
			// Trigger the updateElement function below.
			ICommandService commands = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
			commands.refreshElements("org.jcryptool.core.introduction.autoSlideCommand", null); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public void updateElement(UIElement element, Map parameters) {
		// This method changes the icon in the toolbar and the tooltip.
		// The initial tooltip text is the files OSGI-INF/I10n/bundle_de.porperties and bundle.properties.
		if (autoSlide) {
			element.setIcon(ImageService.getImageDescriptor(IntroductionPlugin.PLUGIN_ID, "icons/animationenabled.png")); //$NON-NLS-1$
			element.setTooltip(Messages.AutoslideHandler_disable);
		} else {
			element.setIcon(ImageService.getImageDescriptor(IntroductionPlugin.PLUGIN_ID, "icons/animationdisabled.png")); //$NON-NLS-1$
			element.setTooltip(Messages.AutoslideHandler_enable);
		}
	}

}

package org.jcryptool.visual.ECDH.handlers;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.visual.ECDH.ECDHPlugin;
import org.jcryptool.visual.ECDH.Messages;

/**
 * Handler, that takes care of changing the icon in the top right corner
 * thats indicating whether the animation is shown or not.
 * @author Thorben Groos
 *
 */
public class ShowAnimationHandler extends AbstractHandler implements IElementUpdater {
	
	public static boolean showAnimation = true;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Switch whether the animation should be shown or not.
		showAnimation = !showAnimation;
		ICommandService commands = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		commands.refreshElements("org.jcryptool.visual.ecdh.showAnimationCommand", null); //$NON-NLS-1$
		return null;
	}

	@Override
	public void updateElement(UIElement element, Map parameters) {
		if (showAnimation) {
			element.setIcon(ImageService.getImageDescriptor(ECDHPlugin.PLUGIN_ID, "icons/animationenabled.png"));
			element.setTooltip(Messages.getString("ShowAnimationHandler.animation_enabled"));
		} else {
			element.setIcon(ImageService.getImageDescriptor(ECDHPlugin.PLUGIN_ID, "icons/animationdisabled.png"));
			element.setTooltip(Messages.getString("ShowAnimationHandler.animation_disabled"));
		}
	}
	

	

}

package org.jcryptool.visual.rainbow;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class RainbowPlugin extends AbstractUIPlugin {

	  /** The plug-in ID. */
    public static final String PLUGIN_ID = "org.jcryptool.visual.rainbow"; //$NON-NLS-1$

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path
     * 
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

}

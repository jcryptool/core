package org.jcryptool.crypto.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CryptoUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jcryptool.crypto.ui"; //$NON-NLS-1$

	// The shared instance
	private static CryptoUIPlugin plugin;

	public static final String FILE_INPUT_ICON = "fileInputIcon";

	public static final String KEYBOARD_INPUT_ICON = "keyboardInputIcon";
	
	/**
	 * The constructor
	 */
	public CryptoUIPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		SWTResourceManager.dispose();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static CryptoUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		ImageDescriptor imageDescriptorKeys = getImageDescriptor("icons/keys.png");
		reg.put(KEYBOARD_INPUT_ICON, imageDescriptorKeys.createImage());
		ImageDescriptor imageDescriptorFile = getImageDescriptor("icons/file.png");
		reg.put(FILE_INPUT_ICON, imageDescriptorFile.createImage());
	}
	
}

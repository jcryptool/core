package org.jcryptool.visual.verifiablesecretsharing;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class VerifiableSecretSharingPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jcryptool.visual.verifiablesecretsharing"; //$NON-NLS-1$

	// The shared instance
	private static VerifiableSecretSharingPlugin plugin;
	
    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;

        super.stop(context);
    }
	
	/**
	 * The constructor
	 */
	public VerifiableSecretSharingPlugin() {
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static VerifiableSecretSharingPlugin getDefault() {
		return plugin;
	}
}

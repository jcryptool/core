package org.jcryptool.core.help;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class HelpPlugin extends AbstractUIPlugin {

	private static HelpPlugin instance;

	public HelpPlugin() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		HelpPlugin.instance = this;
	}
	
	public static HelpPlugin getInstance() {
		return instance;
	}
	
}

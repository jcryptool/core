package org.jcryptool.commands.ui.eclipseconsole;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.commands.ui.eclipseconsole.messages"; //$NON-NLS-1$
	public static String CommandsUiStartup_consolename;
	public static String CommandsUiStartup_eval_error;
	public static String CommandsUiStartup_prompt;
	public static String CommandsUiStartup_welcome;
	public static String CommandsUiStartup_welcome_tip;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

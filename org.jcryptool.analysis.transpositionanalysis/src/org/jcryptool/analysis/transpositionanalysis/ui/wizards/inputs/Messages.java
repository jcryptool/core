package org.jcryptool.analysis.transpositionanalysis.ui.wizards.inputs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.analysis.transpositionanalysis.ui.wizards.inputs.messages"; //$NON-NLS-1$
	public static String TextInputWithSource_editor;
	public static String TextInputWithSource_file;
	public static String TextInputWithSource_manualInput;
	public static String TextInputWithSource_manualInput_source;
	public static String TextInputWithSource_notopenedanymore;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

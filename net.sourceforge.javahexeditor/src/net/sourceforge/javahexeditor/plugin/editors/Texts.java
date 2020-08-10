package net.sourceforge.javahexeditor.plugin.editors;

import org.eclipse.osgi.util.NLS;

public class Texts extends NLS {
	private static final String BUNDLE_NAME = "net.sourceforge.javahexeditor.plugin.editors.Texts"; //$NON-NLS-1$
	public static String HexEditor_unsaved;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Texts.class);
	}

	private Texts() {
	}
}

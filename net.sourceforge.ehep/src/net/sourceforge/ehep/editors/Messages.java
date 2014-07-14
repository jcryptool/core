package net.sourceforge.ehep.editors;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "net.sourceforge.ehep.editors.messages"; //$NON-NLS-1$
	public static String HexEditor_12;
	public static String HexEditor_6;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

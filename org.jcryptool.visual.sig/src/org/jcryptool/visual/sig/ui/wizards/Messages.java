package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.osgi.util.NLS;

/**
 * Holds the messages for the signature visualization (for the 2 wizards)
 *
 */
public class Messages {
	//Name of the files that contain the string values
	private static final String BUNDLE_NAME = "org.jcryptool.visual.sig.messages";
	//Create strings for all the values
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	
	private Messages() {
	}

}

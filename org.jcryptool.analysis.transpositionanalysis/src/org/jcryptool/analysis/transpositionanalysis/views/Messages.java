package org.jcryptool.analysis.transpositionanalysis.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.analysis.transpositionanalysis.views.messages"; //$NON-NLS-1$
	public static String TranspAnalysisView_resetDialogQuestion;
	public static String TranspAnalysisView_resetDialogTitle;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

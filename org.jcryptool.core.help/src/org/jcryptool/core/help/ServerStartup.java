package org.jcryptool.core.help;

import java.net.URI;

import org.eclipse.ui.IStartup;

public class ServerStartup implements IStartup {

	@Override
	public void earlyStartup() {
		try {
			System.out.println("Starting server...");
			JCTJS_Server.getInstance().start();
			System.out.println("Started server on port " + JCTJS_Server.getInstance().getPort());
			java.awt.Desktop.getDesktop().browse(URI.create((JCTJS_Server.getInstance().makeUrlStringFor("./javascript/test.txt"))));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not start JCTJS server. The help system may malfunction. Please report this bug: http://github.com/jcryptool/core/issues. Thanks.");
		}
	}

}

package org.jcryptool.core.help;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.about.InstallationPage;

public class InstallationPage2 extends InstallationPage {

	public InstallationPage2() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		new Label(parent, SWT.NONE).setText("Custom about page!");

	}

}

package org.jcryptool.visual.dsa.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.dsa.DSAData;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.ChooseKPage;

public class UniqueKeyWizard extends Wizard {

	private static final String TITLE = "Enter unique parameter.";
	private final DSAData data;

	public UniqueKeyWizard(final DSAData data) {
		this.data = data;
		this.setWindowTitle(TITLE);
	}

	@Override
	public void addPages() {
		addPage(new ChooseKPage(data));
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}

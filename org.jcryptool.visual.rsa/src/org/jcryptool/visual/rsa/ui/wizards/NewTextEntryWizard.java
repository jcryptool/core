package org.jcryptool.visual.rsa.ui.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.jcryptool.crypto.ui.textblockloader.TextAsNumbersLoaderWizard;
import org.jcryptool.visual.rsa.Action;
import org.jcryptool.visual.rsa.RSAData;

public class NewTextEntryWizard extends TextAsNumbersLoaderWizard {

	private RSAData data;

	public NewTextEntryWizard(int maxNumber, RSAData data, boolean onlyNumbersInput) {
		super(maxNumber, onlyNumbersInput);
		this.data = data;
	}
	public NewTextEntryWizard(int maxNumber, RSAData data) {
		this(maxNumber, data, onlyNumbersLoadingData(data));
	}
	
	private static boolean onlyNumbersLoadingData(RSAData data2) {
		return (data2.getAction() == Action.DecryptAction);
	}


}

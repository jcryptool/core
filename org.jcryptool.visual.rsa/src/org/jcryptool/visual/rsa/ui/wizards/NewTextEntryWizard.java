package org.jcryptool.visual.rsa.ui.wizards;

import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.crypto.ui.textblockloader.TextAsNumbersLoaderWizard;
import org.jcryptool.crypto.ui.textblockloader.conversion.ConversionStringToBlocks;
import org.jcryptool.crypto.ui.textblockloader.conversion.NumbersToBlocksConversion;
import org.jcryptool.visual.rsa.Action;
import org.jcryptool.visual.rsa.RSAData;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.EnterCiphertextPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.EnterPlaintextPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.EnterSignaturePage;

public class NewTextEntryWizard extends TextAsNumbersLoaderWizard {

	private RSAData data;
	private int verificationStep;

	public NewTextEntryWizard(RSAData data, int verificationStep) {
		super(extractMaxNuimberFromData(data), onlyNumbersLoadingData(data, verificationStep));
		this.data = data;
		//TODO: initialize wizard titles and descriptions accordingly
		initializeWizard(data, verificationStep);
	}
	private void initializeWizard(RSAData data2, int verificationStep2) {
		// TODO Auto-generated method stub
		
	}
	private static int extractMaxNuimberFromData(RSAData data) {
		return data.getN().intValue();
	}
	
	private static boolean onlyNumbersLoadingData(RSAData data2, int verificationStep) {
		return (data2.getAction() == Action.DecryptAction) || (verificationStep == 2 && data2.getAction() == Action.VerifyAction);
	}
	
	@Override
	public final boolean performFinish() {
		return true;
	}
	public ConversionStringToBlocks getSTBConversionUsed() {
		if(getDataInputMethod().equals(TextAsNumbersLoaderWizard.METHOD_TEXT_BASED)) {
			return getSTBConversion();
		}
		return null;
	}


}

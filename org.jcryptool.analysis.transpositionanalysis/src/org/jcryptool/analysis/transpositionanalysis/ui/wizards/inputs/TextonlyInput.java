package org.jcryptool.analysis.transpositionanalysis.ui.wizards.inputs;

import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.TextfieldInput;

public abstract class TextonlyInput extends TextfieldInput<String> {

	@Override
	protected InputVerificationResult verifyUserChange() {
		return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
	}

	@Override
	public String readContent() {
		return getTextfield().getText();
	}

	@Override
	protected String getDefaultContent() {
		return "";
	}

	@Override
	public String getName() {
		return "Text";
	}
}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
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
		return ""; //$NON-NLS-1$
	}

	@Override
	public String getName() {
		return "Text"; //$NON-NLS-1$
	}
}

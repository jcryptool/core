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
package org.jcryptool.crypto.classic.xor.algorithm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfiguration;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfigurationWithKey;

/**
 * @author simlei
 *
 */
public class XORConfiguration extends ClassicAlgorithmConfiguration {

	/**
	 * true: textual key input; false: vernam (keyfile) operation.
	 */
	private boolean keyMethod;
	private String keyText;
	private String keyFileName;

	public XORConfiguration(boolean encryptMode, AbstractAlphabet plaintextAlpha,
			boolean filterNonalpha, TransformData preOpTransformData, boolean keyMethod, String keyText, String keyFileName) {
		super(encryptMode, Messages.XORConfiguration_0, plaintextAlpha, filterNonalpha, preOpTransformData);
		this.keyMethod = keyMethod;
		this.keyText = keyText;
		this.keyFileName = keyFileName;
	}

	@Override
	public Composite displayAlgorithmParameters(Composite parent, IEditorPart editor) {
		//TODO: check for GridLayout in parent
		Composite main = super.displayAlgorithmParameters(parent, editor);
		
		if(keyMethod) { // textual key
			Label descr = new Label(main, SWT.NONE);
			descr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
					false, 1, 1));
			descr.setText(Messages.XORConfiguration_1);

			Control keyDisplay = ClassicAlgorithmConfigurationWithKey.generateKeyDisplay(main, editor, keyText);
		} else {
			Label descr = new Label(main, SWT.NONE);
			descr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			descr.setText(Messages.XORConfiguration_2);
			
			Control keyDisplay = ClassicAlgorithmConfigurationWithKey.generateKeyDisplay(main, editor, keyFileName);
		}
		
		return main;
	}

	public boolean isKeyMethod() {
		return keyMethod;
	}

	public String getKeyText() {
		return keyText;
	}

	public String getKeyFileName() {
		return keyFileName;
	}

	public void setKeyMethod(boolean keyMethod) {
		this.keyMethod = keyMethod;
	}

	public void setKeyText(String keyText) {
		this.keyText = keyText;
	}

	public void setKeyFileName(String keyFileName) {
		this.keyFileName = keyFileName;
	}

}

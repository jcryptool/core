//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.xml.io;

import org.jdom.Element;

@SuppressWarnings("serial")
public class InputSignatureElement extends Element {

	private String input;
	private String signature;
	
	public InputSignatureElement(String input, String signature) {
		super("InputSignature"); //$NON-NLS-1$
		if (input != null) {
			setAttribute(Messages.InputSignatureElement_0, input);	
		} else {
			setAttribute(Messages.InputSignatureElement_1, Messages.InputSignatureElement_2);
		}
		input = getAttributeValue("input"); //$NON-NLS-1$
		if (signature != null) {
			setAttribute(Messages.InputSignatureElement_3, signature);	
		} else {
			setAttribute(Messages.InputSignatureElement_4, Messages.InputSignatureElement_5);
		}
		signature = getAttributeValue("signature"); //$NON-NLS-1$
	}
	
	public InputSignatureElement(Element inputSignatureElement) {
		super("InputSignature"); //$NON-NLS-1$
		setAttribute("input", inputSignatureElement.getAttributeValue("input")); //$NON-NLS-1$ //$NON-NLS-2$
		input = getAttributeValue("input"); //$NON-NLS-1$
		setAttribute("signature", inputSignatureElement.getAttributeValue("signature")); //$NON-NLS-1$ //$NON-NLS-2$
		signature = getAttributeValue("signature"); //$NON-NLS-1$
	}
	
	public String getInput() {
		return input;
	}
	
	public String getSignature() {
		return signature;
	}
	
}

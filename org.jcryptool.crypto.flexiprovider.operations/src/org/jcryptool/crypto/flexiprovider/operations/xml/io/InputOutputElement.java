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
public class InputOutputElement extends Element {

	private String input;
	private String output;
	
	public InputOutputElement(String input, String output) {
		super("InputOutput"); //$NON-NLS-1$
		if (input != null) {
			setAttribute(Messages.InputOutputElement_0, input);	
		} else {
			setAttribute(Messages.InputOutputElement_1, Messages.InputOutputElement_2);
		}
		input = getAttributeValue("input"); //$NON-NLS-1$
		if (output != null) {
			setAttribute(Messages.InputOutputElement_3, output);	
		} else { 
			setAttribute(Messages.InputOutputElement_4, Messages.InputOutputElement_5);
		}
		output = getAttributeValue("output"); //$NON-NLS-1$
	}
	
	public InputOutputElement(Element inputOutputElement) {
		super("InputOutput"); //$NON-NLS-1$
		setAttribute("input", inputOutputElement.getAttributeValue("input")); //$NON-NLS-1$ //$NON-NLS-2$
		input = getAttributeValue("input"); //$NON-NLS-1$
		setAttribute("output", inputOutputElement.getAttributeValue("output")); //$NON-NLS-1$ //$NON-NLS-2$
		output = getAttributeValue("output"); //$NON-NLS-1$
	}
	
	public String getInput() {
		return input;
	}
	
	public String getOutput() {
		return output;
	}
	
}

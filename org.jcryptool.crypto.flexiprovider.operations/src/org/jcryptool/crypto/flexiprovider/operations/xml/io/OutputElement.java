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
public class OutputElement extends Element {

	private String output;
	
	public OutputElement(String output) {
		super("Output"); //$NON-NLS-1$
		if (output != null) {
			setAttribute(Messages.OutputElement_0, output);
		} else { 
			setAttribute(Messages.OutputElement_1, Messages.OutputElement_2);
		}
		output = getAttributeValue("output"); //$NON-NLS-1$
	}
	
	public OutputElement(Element outputElement) {
		super("Output"); //$NON-NLS-1$
		setAttribute("output", outputElement.getAttributeValue("output")); //$NON-NLS-1$ //$NON-NLS-2$
		output = getAttributeValue("output"); //$NON-NLS-1$
	}
	
	public String getOutput() {
		return output;
	}
	
}

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
package org.jcryptool.crypto.flexiprovider.operations.xml.algorithms;

import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.SecureRandomDescriptor;
import org.jdom.Element;

@SuppressWarnings("serial")
public class SecureRandomDescriptorElement extends AlgorithmDescriptorElement {

	private String lengthAttribute;
	
	public SecureRandomDescriptorElement(SecureRandomDescriptor descriptor) {
		super(descriptor);
		super.setName("SecureRandomDescriptor"); //$NON-NLS-1$
		setAttribute("length", String.valueOf(descriptor.getLength())); //$NON-NLS-1$
		lengthAttribute = getAttributeValue("length");		 //$NON-NLS-1$
	}

	public SecureRandomDescriptorElement(Element secureRandomDescriptorElement) {
		super(secureRandomDescriptorElement);
		super.setName("SecureRandomDescriptor"); //$NON-NLS-1$
		setAttribute("length", secureRandomDescriptorElement.getAttributeValue("length")); //$NON-NLS-1$ //$NON-NLS-2$
		lengthAttribute = getAttributeValue("length");  //$NON-NLS-1$
	}
			
	private int getLength() {
		return Integer.valueOf(lengthAttribute);
	}
	
	public AlgorithmDescriptor getDescriptor() {
		return new SecureRandomDescriptor(super.getAlgorithmName(), getLength());
	}
	
}

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
import org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.paramspecs.AlgorithmParameterSpecElement;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;
import org.jdom.Element;

import de.flexiprovider.api.parameters.AlgorithmParameterSpec;

@SuppressWarnings("serial")
public class AlgorithmDescriptorElement extends Element {

	private String nameAttribute;
	private String typeAttribute;
	private AlgorithmParameterSpec algorithmParameterSpec;
	
	protected AlgorithmDescriptorElement() {		
	}
	
	public AlgorithmDescriptorElement(AlgorithmDescriptor descriptor) {
		super("AlgorithmDescriptor"); //$NON-NLS-1$
		setAttribute("name", descriptor.getAlgorithmName()); //$NON-NLS-1$
		nameAttribute = getAttributeValue("name"); //$NON-NLS-1$
		
		setAttribute("type", descriptor.getType().getName()); //$NON-NLS-1$
		typeAttribute = getAttributeValue("type"); //$NON-NLS-1$
		
		if (descriptor.getAlgorithmParameterSpec() != null) {
			addContent(new AlgorithmParameterSpecElement(descriptor.getAlgorithmName(), descriptor.getAlgorithmParameterSpec()));
			algorithmParameterSpec = descriptor.getAlgorithmParameterSpec();
		}
	}
		
	public AlgorithmDescriptorElement(Element element) {
		super("AlgorithmDescriptor");		 //$NON-NLS-1$
		setAttribute("name", element.getAttributeValue("name")); //$NON-NLS-1$ //$NON-NLS-2$
		nameAttribute = getAttributeValue("name"); //$NON-NLS-1$
		
		setAttribute("type", element.getAttributeValue("type")); //$NON-NLS-1$ //$NON-NLS-2$
		typeAttribute = getAttributeValue("type"); //$NON-NLS-1$
		
		if (element.getChild("AlgorithmParameterSpec") != null) { //$NON-NLS-1$
			AlgorithmParameterSpecElement parameterSpecElement = new AlgorithmParameterSpecElement(element.getChild("AlgorithmParameterSpec")); //$NON-NLS-1$
			algorithmParameterSpec = parameterSpecElement.getAlgorithmParameterSpec(); 
		}		
	}
	
	public AlgorithmDescriptor getDescriptor() {
		return new AlgorithmDescriptor(getAlgorithmName(), getType(), getAlgorithmParameterSpec());
	}
	
	protected String getAlgorithmName() {
		return nameAttribute;
	}
	
	protected RegistryType getType() {
		return RegistryType.getTypeForName(typeAttribute);
	}
	
	protected AlgorithmParameterSpec getAlgorithmParameterSpec() {
		return algorithmParameterSpec;
	}
	
}

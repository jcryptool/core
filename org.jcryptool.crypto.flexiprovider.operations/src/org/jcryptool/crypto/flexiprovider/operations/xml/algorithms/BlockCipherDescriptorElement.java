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
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.BlockCipherDescriptor;
import org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.paramspecs.ModeParameterSpecElement;
import org.jdom.Element;

import de.flexiprovider.common.mode.ModeParameterSpec;

@SuppressWarnings("serial")
public class BlockCipherDescriptorElement extends AlgorithmDescriptorElement {

	private String modeAttribute;
	private String paddingAttribute;
	private ModeParameterSpec modeParameterSpec;
	
	public BlockCipherDescriptorElement(BlockCipherDescriptor descriptor) {
		super(descriptor);
		super.setName("BlockCipherDescriptor"); //$NON-NLS-1$
		setAttribute("mode", descriptor.getMode()); //$NON-NLS-1$
		modeAttribute = getAttributeValue("mode"); //$NON-NLS-1$
		setAttribute("padding", descriptor.getPadding()); //$NON-NLS-1$
		paddingAttribute = getAttributeValue("padding"); //$NON-NLS-1$
		if (descriptor.getModeParameters() != null) {
			addContent(new ModeParameterSpecElement(descriptor.getModeParameters()));
			modeParameterSpec = descriptor.getModeParameters(); 
		}
	}
	
	public BlockCipherDescriptorElement(Element blockCipherDescriptorElement) {
		super(blockCipherDescriptorElement);
		super.setName("BlockCipherDescriptor"); //$NON-NLS-1$
		setAttribute("mode", blockCipherDescriptorElement.getAttributeValue("mode")); //$NON-NLS-1$ //$NON-NLS-2$
		modeAttribute = getAttributeValue("mode"); //$NON-NLS-1$
		setAttribute("padding", blockCipherDescriptorElement.getAttributeValue("padding")); //$NON-NLS-1$ //$NON-NLS-2$
		paddingAttribute = getAttributeValue("padding"); //$NON-NLS-1$
		if (blockCipherDescriptorElement.getChild("ModeParameterSpec") != null) { //$NON-NLS-1$
			ModeParameterSpecElement modeParameterSpecElement = new ModeParameterSpecElement(blockCipherDescriptorElement.getChild("ModeParameterSpec")); //$NON-NLS-1$
			modeParameterSpec = modeParameterSpecElement.getModeParameterSpec();
		}
	}
	
	private String getMode() {
		return modeAttribute;
	}
	
	private String getPadding() {
		return paddingAttribute;
	}
	
	private ModeParameterSpec getModeParameterSpec() {
		return modeParameterSpec;
	}
	
	public AlgorithmDescriptor getDescriptor() {
		return new BlockCipherDescriptor(super.getAlgorithmName(),
				getMode(),
				getPadding(),
				getModeParameterSpec(),
				super.getAlgorithmParameterSpec()
		);
	}
	
	
	
}

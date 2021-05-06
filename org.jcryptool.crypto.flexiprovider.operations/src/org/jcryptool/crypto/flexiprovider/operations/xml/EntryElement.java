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
package org.jcryptool.crypto.flexiprovider.operations.xml;

import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.BlockCipherDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.SecureRandomDescriptor;
import org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.AlgorithmDescriptorElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.BlockCipherDescriptorElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.SecureRandomDescriptorElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.io.InputOutputElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.io.InputSignatureElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.io.OutputElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.keys.KeyElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.ops.OperationElement;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;
import org.jdom.Element;


@SuppressWarnings("serial")
class EntryElement extends Element {

	private AlgorithmDescriptor descriptor;
	
	protected EntryElement(IFlexiProviderOperation entry) {
		super(Messages.EntryElement_0);
		this.setAttribute("name", entry.getEntryName()); //$NON-NLS-1$
		this.setAttribute("timestamp", String.valueOf(entry.getTimestamp())); //$NON-NLS-1$
		descriptor = entry.getAlgorithmDescriptor();
	
		// algorithm
		if (descriptor instanceof SecureRandomDescriptor) {
			addContent(new SecureRandomDescriptorElement((SecureRandomDescriptor)descriptor));
		} else if (descriptor instanceof BlockCipherDescriptor) {
			addContent(new BlockCipherDescriptorElement((BlockCipherDescriptor)descriptor));
		} else {
			// AlgorithmDescriptor
			addContent(new AlgorithmDescriptorElement(descriptor));
		}
		
		// key
		if (entry.getKeyStoreAlias() != null) {
			addContent(new KeyElement(entry.getKeyStoreAlias()));
		}
		
		// i/o
		if (descriptor.getType().equals(RegistryType.SIGNATURE)) {
			// input/signature
			addContent(new InputSignatureElement(entry.getInput(), entry.getSignature()));
		} else if (descriptor.getType().equals(RegistryType.SECURE_RANDOM)) {
			// output
			addContent(new OutputElement(entry.getOutput()));
		} else {
			// input/output
			addContent(new InputOutputElement(entry.getInput(), entry.getOutput()));
		}
		
		// operation
		if (entry.getOperation() != null) {
			addContent(new OperationElement(entry.getOperation()));
		}
		
	}
	
}

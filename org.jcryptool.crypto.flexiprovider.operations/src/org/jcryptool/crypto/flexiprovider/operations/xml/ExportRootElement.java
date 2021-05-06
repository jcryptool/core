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
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.EntryNode;
import org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.AlgorithmDescriptorElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.BlockCipherDescriptorElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.SecureRandomDescriptorElement;
import org.jdom.Element;

@SuppressWarnings("serial")
public class ExportRootElement extends Element {

	public ExportRootElement(IFlexiProviderOperation entry) {
		super("ExportedEntry"); //$NON-NLS-1$
		setAttribute("version", "0.2.0"); //$NON-NLS-1$ //$NON-NLS-2$
		setAttribute("name", entry.getEntryName()); //$NON-NLS-1$
		setAttribute("timestamp", String.valueOf(entry.getTimestamp())); //$NON-NLS-1$
		AlgorithmDescriptor descriptor = entry.getAlgorithmDescriptor(); 
		if (descriptor instanceof SecureRandomDescriptor) {
			addContent(new SecureRandomDescriptorElement((SecureRandomDescriptor)descriptor));
		} else if (descriptor instanceof BlockCipherDescriptor) {
			addContent(new BlockCipherDescriptorElement((BlockCipherDescriptor)descriptor));
		} else {
			addContent(new AlgorithmDescriptorElement((AlgorithmDescriptor)descriptor));
		}
	}
	
	public ExportRootElement(Element exportRootElement) {
		super("ExportedEntry"); //$NON-NLS-1$
		setAttribute("version", exportRootElement.getAttributeValue("version")); //$NON-NLS-1$ //$NON-NLS-2$
		setAttribute("name", exportRootElement.getAttributeValue("name")); //$NON-NLS-1$ //$NON-NLS-2$
		setAttribute("timestamp", exportRootElement.getAttributeValue("timestamp")); //$NON-NLS-1$ //$NON-NLS-2$
		if (exportRootElement.getChild("AlgorithmDescriptor") != null) { //$NON-NLS-1$
			addContent((Element)exportRootElement.getChild("AlgorithmDescriptor").clone());	 //$NON-NLS-1$
		} else if (exportRootElement.getChild("SecureRandomDescriptor") != null) { //$NON-NLS-1$
			addContent((Element)exportRootElement.getChild("SecureRandomDescriptor").clone());	 //$NON-NLS-1$
		} else if (exportRootElement.getChild("BlockCipherDescriptor") != null) { //$NON-NLS-1$
			addContent((Element)exportRootElement.getChild("BlockCipherDescriptor").clone());	 //$NON-NLS-1$
		}		
	}
	
	public EntryNode getEntryNode() {
		String name = getAttributeValue("name"); //$NON-NLS-1$
		long timestamp = Long.valueOf(getAttributeValue("timestamp")); //$NON-NLS-1$
		// algorithm
		AlgorithmDescriptor descriptor = null;
		if (getChild("AlgorithmDescriptor") != null) { //$NON-NLS-1$
			AlgorithmDescriptorElement descriptorElement = new AlgorithmDescriptorElement(getChild("AlgorithmDescriptor")); //$NON-NLS-1$
			descriptor = descriptorElement.getDescriptor();
		} else if (getChild("SecureRandomDescriptor") != null) { //$NON-NLS-1$
			SecureRandomDescriptorElement descriptorElement = new SecureRandomDescriptorElement(getChild("SecureRandomDescriptor")); //$NON-NLS-1$
			descriptor = descriptorElement.getDescriptor();
		} else if (getChild("BlockCipherDescriptor") != null) { //$NON-NLS-1$
			BlockCipherDescriptorElement descriptorElement = new BlockCipherDescriptorElement(getChild("BlockCipherDescriptor")); //$NON-NLS-1$
			descriptor = descriptorElement.getDescriptor();
		}
		return new EntryNode(name, timestamp, descriptor);		
	}
	
}

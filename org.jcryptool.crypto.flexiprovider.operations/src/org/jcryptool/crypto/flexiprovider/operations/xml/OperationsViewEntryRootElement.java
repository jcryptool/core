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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.EntryNode;
import org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.AlgorithmDescriptorElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.BlockCipherDescriptorElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.SecureRandomDescriptorElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.io.InputOutputElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.io.InputSignatureElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.io.OutputElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.keys.KeyElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.ops.OperationElement;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jdom.Element;

@SuppressWarnings("serial")
public class OperationsViewEntryRootElement extends Element {

	public OperationsViewEntryRootElement() {
		super("OperationsViewEntries"); //$NON-NLS-1$
		this.setAttribute("version", "0.2.0"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@SuppressWarnings("unchecked")
	public OperationsViewEntryRootElement(Element rootElement) {
		super("OperationsViewEntries"); //$NON-NLS-1$
		Iterator<Element> it = rootElement.getChildren("Entry").iterator(); //$NON-NLS-1$
		while (it.hasNext()) {
			addContent((Element)it.next().clone());
		}
	}
	
	public void addEntry(IFlexiProviderOperation entry) {
		this.addContent(new EntryElement(entry));
	}
	
	@SuppressWarnings("unchecked")
	public List<EntryNode> getEntryNodes() {
		List<EntryNode> nodes = new ArrayList<EntryNode>();
		Iterator<Element> it = getChildren("Entry").iterator(); //$NON-NLS-1$
		while (it.hasNext()) {
			Element element = it.next();
			String name = element.getAttributeValue("name"); //$NON-NLS-1$
			long timestamp = Long.valueOf(element.getAttributeValue("timestamp")); //$NON-NLS-1$
			
			// algorithm
			AlgorithmDescriptor descriptor = null;
			if (element.getChild("AlgorithmDescriptor") != null) { //$NON-NLS-1$
				AlgorithmDescriptorElement descriptorElement = new AlgorithmDescriptorElement(element.getChild("AlgorithmDescriptor")); //$NON-NLS-1$
				descriptor = descriptorElement.getDescriptor();
			} else if (element.getChild("SecureRandomDescriptor") != null) { //$NON-NLS-1$
				SecureRandomDescriptorElement descriptorElement = new SecureRandomDescriptorElement(element.getChild("SecureRandomDescriptor")); //$NON-NLS-1$
				descriptor = descriptorElement.getDescriptor();
			} else if (element.getChild("BlockCipherDescriptor") != null) { //$NON-NLS-1$
				BlockCipherDescriptorElement descriptorElement = new BlockCipherDescriptorElement(element.getChild("BlockCipherDescriptor")); //$NON-NLS-1$
				descriptor = descriptorElement.getDescriptor();
			}
			
			EntryNode entryNode = new EntryNode(name, timestamp, descriptor);
			nodes.add(entryNode);
			
			// supplemental information below
			
			// key
			KeyStoreAlias alias = null;
			if (element.getChild("Key") != null) { //$NON-NLS-1$
				KeyElement keyElement = new KeyElement(element.getChild("Key")); //$NON-NLS-1$
				alias = keyElement.getAlias();
				entryNode.setKeyStoreAlias(alias);
			}
			
			// operation
			OperationType type = null;
			if (element.getChild("Operation") != null) { //$NON-NLS-1$
				OperationElement opsElement = new OperationElement(element.getChild("Operation")); //$NON-NLS-1$
				type = opsElement.getOperationType();
				if (!type.equals(OperationType.UNKNOWN)) {
					entryNode.setOperation(type);	
				}				
			}			
			
			// i/o
			String input = null;
			String output = null;
			String signature = null;
			if (element.getChild("Output") != null) { //$NON-NLS-1$
				OutputElement outElement = new OutputElement(element.getChild("Output")); //$NON-NLS-1$
				output = outElement.getOutput();
				entryNode.setOutput(output);
			} else if (element.getChild("InputOutput") != null) { //$NON-NLS-1$
				InputOutputElement inOutElement = new InputOutputElement(element.getChild("InputOutput")); //$NON-NLS-1$
				input = inOutElement.getInput();
				output = inOutElement.getOutput();
				entryNode.setInput(input);
				entryNode.setOutput(output);
			} else if (element.getChild("InputSignature") != null) { //$NON-NLS-1$
				InputSignatureElement inSigElement = new InputSignatureElement(element.getChild("InputSignature")); //$NON-NLS-1$
				input = inSigElement.getInput();
				signature = inSigElement.getSignature();
				entryNode.setInput(input);
				entryNode.setSignature(signature);
			}			

		}
		return nodes;
	}
	
}

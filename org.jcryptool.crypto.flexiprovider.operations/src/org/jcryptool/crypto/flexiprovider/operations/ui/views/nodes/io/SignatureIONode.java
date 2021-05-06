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
package org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io;

public class SignatureIONode extends IONode {

	private InputNode inputNode;
	private SignatureNode signatureNode;
	
	public SignatureIONode() {
		super(Messages.SignatureIONode_0);
		inputNode = new InputNode();
		this.addChild(inputNode);
		signatureNode = new SignatureNode();
		this.addChild(signatureNode);
	}
	
	public void setInput(String input) {
		inputNode.setInput(input);
	}
	
	public String getInput() {
		return inputNode.getInput();
	}
	
	public void setSignature(String signature) {
		signatureNode.setSignature(signature);
	}
	
	public String getSignature() {
		return signatureNode.getSignature();
	}
	
}

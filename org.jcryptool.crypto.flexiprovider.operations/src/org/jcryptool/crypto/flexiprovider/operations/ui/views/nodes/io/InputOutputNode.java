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

public class InputOutputNode extends IONode {

	private InputNode inputNode;
	private OutputNode outputNode;
	
	public InputOutputNode() {
		super(Messages.InputOutputNode_0);
		inputNode = new InputNode();
		this.addChild(inputNode);
		outputNode = new OutputNode();
		this.addChild(outputNode);
	}
	
	public void setInput(String input) {
		inputNode.setInput(input);
	}
	
	public String getInput() {
		return inputNode.getInput();
	}
	
	public void setOutput(String output) {
		outputNode.setOutput(output);
	}
	
	public String getOutput() {
		return outputNode.getOutput();
	}
	
}

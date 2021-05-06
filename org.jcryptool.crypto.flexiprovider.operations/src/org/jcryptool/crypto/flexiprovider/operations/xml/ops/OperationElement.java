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
package org.jcryptool.crypto.flexiprovider.operations.xml.ops;

import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jdom.Element;

@SuppressWarnings("serial")
public class OperationElement extends Element {

	private OperationType type;
	
	public OperationElement(OperationType type) {
		super("Operation"); //$NON-NLS-1$
		setAttribute("type", type.getTypeName()); //$NON-NLS-1$
		this.type = type;
	}
	
	public OperationElement(Element operationElement) {
		super("Operation"); //$NON-NLS-1$
		setAttribute("type", operationElement.getAttributeValue("type")); //$NON-NLS-1$ //$NON-NLS-2$
		type = OperationType.getTypeForName(getAttributeValue("type")); //$NON-NLS-1$
	}
	
	public OperationType getOperationType() {
		return type;
	}
	
}

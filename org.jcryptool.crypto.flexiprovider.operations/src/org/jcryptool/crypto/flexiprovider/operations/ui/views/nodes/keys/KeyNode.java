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
package org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.keys;

import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;

public abstract class KeyNode extends TreeNode {

	public KeyNode(String name) {
		super(name);
	}
	
	public abstract void setAlias(IKeyStoreAlias alias);

	public abstract IKeyStoreAlias getAlias();

}

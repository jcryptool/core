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
package org.jcryptool.crypto.flexiprovider.operations.xml.keys;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jdom.Element;

@SuppressWarnings("serial")
public class KeyElement extends Element {

	public KeyElement(IKeyStoreAlias alias) {
		super(Messages.KeyElement_0);
		setText(alias.getAliasString());
	}
	
	public KeyElement(Element keyElement) {
		super(Messages.KeyElement_1);
		setText(keyElement.getText());
	}

	public KeyStoreAlias getAlias() {
		return new KeyStoreAlias(getText());
	}
	
}

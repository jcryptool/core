//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.descriptors.interfaces;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

public interface IContactDescriptor {

	public String getName();
	
	public void addSecretKey(KeyStoreAlias alias);
	public void addCertificate(KeyStoreAlias alias);
	public void addKeyPair(KeyStoreAlias privateKey, KeyStoreAlias publicKey);
	
	public void removeSecretKey(KeyStoreAlias alias);
	public void removeCertificate(KeyStoreAlias alias);
	public void removeKeyPair(KeyStoreAlias privateKey);
}

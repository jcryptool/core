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

import org.jcryptool.crypto.keystore.keys.KeyType;

public interface INewEntryDescriptor {

	public String getContactName();
	
	public String getPassword();
	
	public KeyType getKeyStoreEntryType();

	public String getDisplayedName();
	
	public String getAlgorithmName();
	
	public String getProvider();
	
	public int getKeyLength();
	
}

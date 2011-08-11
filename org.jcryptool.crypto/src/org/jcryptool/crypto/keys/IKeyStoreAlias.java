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
package org.jcryptool.crypto.keys;

public interface IKeyStoreAlias {
	
	public String getClassName();
	
	public String getContactName();
	
	public String getEncodedContactName();
	
	public boolean isValid();
	
	public KeyType getKeyStoreEntryType();
	
	public String getOperation();
	
	public int getKeyLength();
	
	public String getHashValue();

	public String getAliasString();
	
}

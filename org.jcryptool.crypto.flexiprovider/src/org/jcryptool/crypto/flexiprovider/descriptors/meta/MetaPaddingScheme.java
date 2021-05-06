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
package org.jcryptool.crypto.flexiprovider.descriptors.meta;

import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaPaddingScheme;

public class MetaPaddingScheme implements IMetaPaddingScheme {

	private String className;
	private String name;
	String id;
	
	public MetaPaddingScheme(String className, String id, String name) {
		this.className = className;
		this.name = name;
		this.id = id;
	}
	
	public int compareTo(IMetaPaddingScheme paddingScheme) {
		return getID().compareTo(paddingScheme.getID());
	}

	public String getClassName() {
		return className;
	}
	
	public String getID() {
		return id;
	}

	public String getPaddingSchemeName() {
		return name;
	}

}

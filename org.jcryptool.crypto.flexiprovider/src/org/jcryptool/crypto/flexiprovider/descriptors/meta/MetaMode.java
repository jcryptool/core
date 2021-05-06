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

import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaMode;

public class MetaMode implements IMetaMode {

	private String className;
	private String name;
	private String id;
	private String paramSpecClassName;

	public MetaMode(String className, String id, String name) {
		this.className = className;
		this.id = id;
		this.name = name;
	}

	public int compareTo(IMetaMode mode) {
		return getID().compareTo(mode.getID());
	}

	public String getClassName() {
		return className;
	}

	public String getDescription() {
		return name;
	}

	public String getID() {
		return id;
	}

	public String getParameterSpec() {
		return paramSpecClassName;
	}

	public void setParameterSpec(String parameterSpecClassName) {
		this.paramSpecClassName = parameterSpecClassName;
	}

}

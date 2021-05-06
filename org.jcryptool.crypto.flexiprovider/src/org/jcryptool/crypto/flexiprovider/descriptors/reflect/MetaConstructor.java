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
package org.jcryptool.crypto.flexiprovider.descriptors.reflect;

import java.util.ArrayList;
import java.util.List;

import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaConstructor;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaParameter;


public class MetaConstructor implements IMetaConstructor {

	private String className;
	private List<IMetaParameter> params = new ArrayList<IMetaParameter>(1);
	
	public MetaConstructor(String className, List<IMetaParameter> params) {
		this.className = className;
		this.params.addAll(params);
	}
	
	public String getClassName() {
		return className;
	}
	
	public List<IMetaParameter> getParameters() {
		return params;
	}
	
}

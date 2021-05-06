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
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaSpec;


public class MetaSpec implements IMetaSpec {

	private String className;
	private List<IMetaConstructor> constructors = new ArrayList<IMetaConstructor>(1);
	
	public MetaSpec(String className, List<IMetaConstructor> constructors) {
		this.className = className;
		this.constructors.addAll(constructors);
	}
	
	public String getClassName() {
		return className;
	}
	
	public List<IMetaConstructor> getMetaConstructors() {
		return constructors;
	}
	
}

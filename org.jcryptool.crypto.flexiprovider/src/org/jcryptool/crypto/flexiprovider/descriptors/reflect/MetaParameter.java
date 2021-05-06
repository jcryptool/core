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

import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaParameter;


public class MetaParameter implements IMetaParameter {

	private String type;
	private String name;
	private String description;
	private List<IMetaParameter> subParameters;
	
	public MetaParameter(String type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public MetaParameter(String type, String name, String description) {
		this.type = type;
		this.name = name;
		this.description = description;
	}
	
	public void addParameter(IMetaParameter parameter) {
		if (subParameters == null) {
			subParameters = new ArrayList<IMetaParameter>();
		}
		subParameters.add(parameter);
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getName() {
		return name;
	}

	public List<IMetaParameter> getSubParameters() {
		return subParameters;
	}

	public String getType() {
		return type;
	}
	
}

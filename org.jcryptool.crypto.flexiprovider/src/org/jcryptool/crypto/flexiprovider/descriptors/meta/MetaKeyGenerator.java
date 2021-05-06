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

import java.util.Iterator;
import java.util.List;

import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaKeyGenerator;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaLength;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaOID;


public class MetaKeyGenerator implements IMetaKeyGenerator {

	private String className;
	private String parameterSpecClassName;
	private IMetaOID oid;
	private List<String> names;
	private IMetaLength lengths;
	
	private String paramGenParamSpecClassName;
	
	private String paramGenClassName;

	private boolean parameterSpecDisabled = false;
	
	public MetaKeyGenerator(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public IMetaLength getLengths() {
		return lengths;
	}

	public List<String> getNames() {
		return names;
	}

	public IMetaOID getOID() {
		return oid;
	}

	public String getParameterGeneratorClassName() {
		return paramGenClassName;
	}

	public String getParameterSpecClassName() {
		return parameterSpecClassName;
	}

	public String getParamGenParameterSpecClassName() {
		return paramGenParamSpecClassName;
	}
	
	public boolean isNamed(String name) {
		Iterator<String> it = names.iterator();
		while (it.hasNext()) {
			String current = it.next();
			if (current.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isParameterSpecDisabled() {
		return parameterSpecDisabled;
	}
	
	public void setLengths(IMetaLength lengths) {
		this.lengths = lengths;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}


	public void setOID(IMetaOID oid) {
		this.oid = oid;
	}

	public void setParameterGeneratorClassName(String className) {
		this.paramGenClassName = className;
	}
	
	public void setParameterSpecClassName(String className) {
		this.parameterSpecClassName = className;
	}
	
	public void setParameterSpecDisabled(boolean disabled) {
		this.parameterSpecDisabled = disabled;
	}

	public void setParamGenParameterSpecClassName(String className) {
		this.paramGenParamSpecClassName = className;
	}
	
}

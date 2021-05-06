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
package org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces;

import java.util.List;

public interface IMetaEntry {

	public String getClassName();

	public List<String> getNames();

	public IMetaOID getOID();

	public String getParameterGeneratorClassName();

	public String getParameterSpecClassName();

	public String getParamGenParameterSpecClassName();

	public boolean isNamed(String name);

	public boolean isParameterSpecDisabled();

	public void setOID(IMetaOID oid);

	public void setParameterGeneratorClassName(String className);

	public void setParameterSpecClassName(String className);

	public void setParameterSpecDisabled(boolean disabled);

	public void setParamGenParameterSpecClassName(String className);

}

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

public interface IMetaLength {

	public int getDefaultLength();
	public List<Integer> getLengths();
	public int getLowerBound();
	public int getUpperBound();
	public void setBounds(int lowerBound, int upperBound);
	public void setDefaultLength(int defaultLength);
	public void setLengths(List<Integer> lengths);
	
}

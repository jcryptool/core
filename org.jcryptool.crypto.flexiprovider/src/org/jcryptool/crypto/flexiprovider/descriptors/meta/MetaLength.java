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

import java.util.List;

import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaLength;


public class MetaLength implements IMetaLength {

	private int defaultLength;
	private List<Integer> lengths;
	private int lowerBound;
	private int upperBound;
	
	public MetaLength(int defaultLength) {
		this.defaultLength = defaultLength;
	}
	
	public int getDefaultLength() {
		return defaultLength;
	}

	public List<Integer> getLengths() {
		return lengths;
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public void setBounds(int lowerBound, int upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public void setDefaultLength(int defaultLength) {
		this.defaultLength = defaultLength;
	}

	public void setLengths(List<Integer> lengths) {
		this.lengths = lengths;
	}

}

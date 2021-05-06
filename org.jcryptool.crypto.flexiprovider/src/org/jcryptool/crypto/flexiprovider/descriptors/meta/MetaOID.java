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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaOID;



public class MetaOID implements IMetaOID {
	
	private static final String DOT = "."; //$NON-NLS-1$
	private List<Integer> oidsegments = new ArrayList<Integer>(7);
	String oid;
	
	public MetaOID(String oid) {
		this.oid = oid;
		StringTokenizer tokenizer = new StringTokenizer(oid, DOT);
		while (tokenizer.hasMoreTokens()) {
			oidsegments.add(Integer.valueOf(tokenizer.nextToken()));		
		}
	}
	
	public int compareTo(IMetaOID oid) {
		return getStringOID().compareTo(oid.getStringOID());
	}
	
	public boolean equals(Object o) {
		if (o instanceof MetaOID) {
			return getStringOID().equals( ((MetaOID)o).getStringOID() );
		} else {
			return false;	
		}	
	}
	
	public String getStringOID() {
		return oid;
	}

	public String toString() {
		return oid;
	}
	
}

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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaOID;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;


public class MetaAlgorithm implements IMetaAlgorithm, Comparable<IMetaAlgorithm> {

	private RegistryType type;
	
	private IMetaOID oid;
	
	private String providerName;

	private Map<String, String> names = new HashMap<String, String>();	

	private Map<String, String> standardParams = new HashMap<String, String>();

	private String algorithmClassName;
	private String algorithmParamSpecClassName;
	private String keyGenClassName;
	
	private int defaultBlockLength;
	private List<Integer> blockLengths;
	
	private String paramGenParamSpecClassName;
	
	private String paramGenClassName;
	
	private boolean parameterSpecDisabled = false;
	
	private String blockCipherName;
	
	private IMetaOID blockCipherOID;
	
	private String blockCipherMode;

	public MetaAlgorithm(RegistryType type, IMetaOID oid, List<String> names, String algorithmClassName) {
		this.type = type;
		this.oid = oid;
		Iterator<String> it = names.iterator();
		while (it.hasNext()) {
			addName(it.next());
		}
		this.algorithmClassName = algorithmClassName;
	}
	
	public MetaAlgorithm(RegistryType type, IMetaOID oid, String algorithmClassName) {
		this.type = type;
		this.algorithmClassName = algorithmClassName;
		this.oid = oid;
	}	

	public MetaAlgorithm(RegistryType type, String name, String algorithmClassName) {
		this.type = type;
		this.algorithmClassName = algorithmClassName;
		synchronized(names) {
			names.put(name, name);	
		}		
	}

	public void addName(String name) {
		synchronized(names) {
			names.put(name, name);	
		}				
	}

	public void addStandardParams(String params) {
		synchronized(standardParams) {
			standardParams.put(params, params);
		}
	}

	public int compareTo(IMetaAlgorithm algorithm) {
		if (getOID() != null && algorithm.getOID() != null) {
			// sort according to the names
			return getOID().compareTo(algorithm.getOID());	
		} else if (getOID() != null || algorithm.getOID() != null) {
			// if only one oid is null, make sure that null is considered "LARGER"
			if (getOID() == null) {
				return 1;
			} else {
				return -1;
			}
		} else {
			// normal ordering of the oids
			return getName().compareTo(algorithm.getName());
		}		
	}

	public boolean equals(Object o) {
		if (o instanceof MetaAlgorithm) {
			if (getOID() != null) {
				return getOID().toString().equals(((MetaAlgorithm)o).getOID().toString());	
			} else {
				if (names.size() != ((MetaAlgorithm)o).getNameCount()  ) {
					return false;
				} else {
					// name comparision
					return Arrays.equals(getNamesArray(), ((MetaAlgorithm)o).getNamesArray() );			
				}
			}						
		} else {
			return false;
		}
	}

	public String getBlockCipherMode() {
		return blockCipherMode;
	}

	public String getBlockCipherName() {
		return blockCipherName;
	}

	public IMetaOID getBlockCipherOID() {
		return blockCipherOID;
	}
	
	public List<Integer> getBlockLengths() {
		return blockLengths;
	}
	
	public String getClassName() {
		return algorithmClassName;
	}

	public int getDefaultBlockLength() {
		return defaultBlockLength;
	}

	public String getKeyGeneratorClassName() {
		return keyGenClassName;
	}

	public String getName() {
		synchronized(names) {
			Iterator<String> it = names.keySet().iterator();
			while (it.hasNext()) {
				return it.next();
			}
		}		
		return null;
	}

	public int getNameCount() {
		return names.size();
	}
	
	public List<String> getNames() {
		synchronized(names) {
			List<String> sorted = new ArrayList<String>(names.values());
			Collections.sort(sorted);
			return sorted;
		}		
	}
	
	private String[] getNamesArray() {
		String[] result = new String[names.size()];
		List<String> list = new ArrayList<String>(names.values());
		Collections.sort(list);		
		result = list.toArray(result);
		return result;
	}
	
	public IMetaOID getOID() {
		return oid;
	}
	
	public String getParameterGeneratorClassName() {
		return paramGenClassName;
	}

	public String getParameterSpecClassName() {
		return algorithmParamSpecClassName;
	}

	public String getParamGenParameterSpecClassName() {
		return paramGenParamSpecClassName;
	}

	public List<String> getStandardParams() {
		return new ArrayList<String>(standardParams.values());
	}
	public RegistryType getType() {
		return type;
	}
	
	public boolean isNamed(String name) {
		synchronized(names) {
			if (names.containsKey(name)) {
				return true;
			} else {
				return false;
			}	
		}		
	}

	public boolean isParameterSpecDisabled() {
		return parameterSpecDisabled;
	}

	public void merge(IMetaAlgorithm algorithm) {
		if (this.oid == null) {
			this.oid = algorithm.getOID();
		}
		Iterator<String> it = algorithm.getNames().iterator();
		String tmp;
		while (it.hasNext()) {
			tmp = it.next();
			synchronized(names) {
				names.put(tmp, tmp);	
			}					
		}
	}

	public void setBlockCipherMode(String mode) {
		this.blockCipherMode = mode;
	}

	public void setBlockCipherName(String name) {
		this.blockCipherName = name;
	}
	
	public void setBlockCipherOID(IMetaOID oid) {
		this.blockCipherOID = oid;
	}

	public void setBlockLengths(List<Integer> blockLengths) {
		this.blockLengths = blockLengths;
	}
	
	public void setDefaultBlockLength(int blockLength) {
		this.defaultBlockLength = blockLength;
	}
	
	public void setKeyGeneratorClassName(String keyGenClassName) {
		this.keyGenClassName = keyGenClassName;
	}
	public void setName(String name) {
		synchronized(names) {
			names.put(name, name);	
		}		
	}

	public void setOID(IMetaOID oid) {
		this.oid = oid;
	}

	public void setParameterGeneratorClassName(String className) {
		this.paramGenClassName = className;
	}

	public void setParameterSpecClassName(String className) {
		this.algorithmParamSpecClassName = className;
	}

	
	public void setParameterSpecDisabled(boolean disabled) {
		this.parameterSpecDisabled = disabled;
	}

	public void setParamGenParameterSpecClassName(String className) {
		this.paramGenParamSpecClassName = className;
	}

	
	public String toString() {
		String result = "\n"; //$NON-NLS-1$
		result = result + "OID: " + oid; //$NON-NLS-1$
		if (providerName != null) {
			result = result + "; ----- Provider: " + providerName + " (Type: "+type+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			result = result + "; ----- <NO PROVIDER>" + " (Type: "+type+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		if (algorithmClassName != null) result = result + "\n\tClassName: " + algorithmClassName;		 //$NON-NLS-1$
		synchronized(names) {
			if (names.size() > 0) {
				result = result + "\n\tNames: "; //$NON-NLS-1$
				List<String> sorted = new ArrayList<String>(names.values());
				Collections.sort(sorted);
				Iterator<String> it = sorted.iterator();
				while (it.hasNext()) {
					result = result + it.next() + ", "; //$NON-NLS-1$
				}
			}			
		}
		if (algorithmParamSpecClassName != null) {
			result = result + "\n\tAlgorithmParamSpec: " + algorithmParamSpecClassName; //$NON-NLS-1$
		}
		synchronized(standardParams) {
			if (standardParams.size() > 0) {
				result = result + "\n\tStandard Params: "; //$NON-NLS-1$
				List<String> sorted = new ArrayList<String>(standardParams.values());
				Collections.sort(sorted);
				Iterator<String> it = sorted.iterator();
				while (it.hasNext()) {
					result = result + it.next() + ", "; //$NON-NLS-1$
				}	
			}			
		}
		return result+"\n"; //$NON-NLS-1$
	}
	
}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
/**
 * 
 */
package org.jcryptool.crypto.keystore.ui.wizards;

import org.eclipse.core.runtime.IPath;

/**
 * @author tkern
 *
 */
public class NewKeyStoreDescriptor implements INewKeyStoreDescriptor {

	private String name;
	private IPath path;
	
	public NewKeyStoreDescriptor(String name, IPath path) {
		this.name = name;
		this.path = path;
	}
	
	/**
	 * @see org.jcryptool.crypto.keystore.ui.wizards.INewKeyStoreDescriptor#getKeyStoreName()
	 */
	public String getKeyStoreName() {
		return name;
	}

	/**
	 * @see org.jcryptool.crypto.keystore.ui.wizards.INewKeyStoreDescriptor#getKeyStorePath()
	 */
	public IPath getKeyStorePath() {
		return path;
	}

}

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
package org.jcryptool.crypto.flexiprovider.types;

import de.flexiprovider.api.Registry;

public enum RegistryType {

	ASYMMETRIC_BLOCK_CIPHER(Registry.ASYMMETRIC_BLOCK_CIPHER),
	ASYMMETRIC_HYBRID_CIPHER(Registry.ASYMMETRIC_HYBRID_CIPHER),
	BLOCK_CIPHER(Registry.BLOCK_CIPHER),
	MODE(Registry.MODE),
	PADDING_SCHEME(Registry.PADDING_SCHEME),
	CIPHER(Registry.CIPHER),
	MAC(Registry.MAC),
	MESSAGE_DIGEST(Registry.MESSAGE_DIGEST),
	SECURE_RANDOM(Registry.SECURE_RANDOM),
	SIGNATURE(Registry.SIGNATURE),
	ALG_PARAM_SPEC(Registry.ALG_PARAM_SPEC),
	ALG_PARAMS(Registry.ALG_PARAMS),
	ALG_PARAM_GENERATOR(Registry.ALG_PARAM_GENERATOR),
	SECRET_KEY_GENERATOR(Registry.SECRET_KEY_GENERATOR),
	KEY_PAIR_GENERATOR(Registry.KEY_PAIR_GENERATOR),
	SECRET_KEY_FACTORY(Registry.SECRET_KEY_FACTORY),
	KEY_FACTORY(Registry.KEY_FACTORY),
	KEY_DERIVATION(Registry.KEY_DERIVATION),
	KEY_AGREEMENT(Registry.KEY_AGREEMENT);

	public static RegistryType getTypeForName(String name) {
		return Enum.valueOf(RegistryType.class, name);
	}

	private int type;

	private RegistryType(int type) {
		this.type = type;
	}

	public String getName() {
		return name();
	}

	public int getTypeNumber() {
		return type;
	}
	
	public String toString() {
		return name() + " of type " + type; //$NON-NLS-1$
	}

}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.descriptors.interfaces;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.ui.views.nodes.Contact;

public interface IContactDescriptor {
	
    Contact getContact();

    void addSecretKey(KeyStoreAlias alias);

    void addCertificate(KeyStoreAlias alias);

    void addKeyPair(KeyStoreAlias privateKey, KeyStoreAlias publicKey);

    void removeSecretKey(KeyStoreAlias alias);

    void removeCertificate(KeyStoreAlias alias);

    void removeKeyPair(KeyStoreAlias privateKey);
}

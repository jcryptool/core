// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.descriptors.interfaces;

import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.ui.views.nodes.Contact;

public interface IContactDescriptor {

    Contact getContact();

    void addSecretKey(IKeyStoreAlias alias);

    void addCertificate(IKeyStoreAlias alias);

    void addKeyPair(IKeyStoreAlias privateKey, IKeyStoreAlias publicKey);

    void removeSecretKey(IKeyStoreAlias alias);

    void removeCertificate(IKeyStoreAlias alias);

    void removeKeyPair(IKeyStoreAlias privateKey);
}

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

import java.security.PrivateKey;
import java.security.PublicKey;

public interface INewKeyPairDescriptor extends INewEntryDescriptor {
    PrivateKey getPrivateKey();

    PublicKey getPublicKey();
}

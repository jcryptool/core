// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.modern.sha3.jh;


/**
 * The action class of JH.
 *
 * @author Michael Starzer
 */
public class JHAction {
    public byte[] run(int outputSize, String str) {
        JHAlgorithm JH;
        JH = new JHAlgorithm(str.getBytes());
        return JH.hash(outputSize);
    }
}

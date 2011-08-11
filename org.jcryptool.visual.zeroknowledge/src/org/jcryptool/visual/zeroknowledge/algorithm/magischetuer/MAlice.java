// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.algorithm.magischetuer;

/**
 * Klasse für eine Person, die das Geheimnis kennt.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class MAlice extends MPerson {

    /**
     * erstellt ein Object mit dem Namen "Alice"
     */
    public MAlice() {
        super("Alice"); //$NON-NLS-1$
    }

    /**
     * setzt das Geheimnis neu
     * 
     * @param code Code, das gesetzt werden soll
     */
    public void setCode(int[] code) {
        this.code = code;
        notifyChanged();
    }
}

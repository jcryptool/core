// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.dataobject.modern.symmetric;

import org.jcryptool.core.operations.dataobject.modern.ModernDataObject;

/**
 * The implementation of the IDataObject interface for the Lfsr algorithm.
 * 
 * @version 0.1
 * 
 */
public class LfsrDataObject extends ModernDataObject {

    private boolean[] seed;
    private boolean[] tapSettings;

    public boolean[] getSeed() {
        return seed;
    }

    public void setSeed(boolean[] seed) {
        this.seed = seed;
    }

    public boolean[] getTapSettings() {
        return tapSettings;
    }

    public void setTapSettings(boolean[] tapSettings) {
        this.tapSettings = tapSettings;
    }
}

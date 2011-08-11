// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.caesar.algorithm;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.crypto.classic.caesar.CaesarPlugin;

/**
 * The CaesarAlgorithm extends the AbstractClassicAlgorithm2.
 *
 * @see org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm2.
 *
 * @author amro
 * @version 0.5
 */
public class CaesarAlgorithm extends AbstractClassicAlgorithm {
    
	public static final CaesarAlgorithmSpecification specification = new CaesarAlgorithmSpecification();
	
	/**
     * Constructor The specific engine of the algorithm is assigned.
     *
     */
    public CaesarAlgorithm() {
        engine = new CaesarEngine();
    }

    /**
     * This method takes the key data as a char array and generates from it the
     * algorithm key as int array
     *
     * @param keyData the key data
     * @return the generated key as int array
     */
    protected int[] generateKey(char[] keyData) {
        int[] tmp = null;
        try {
            int value = (alphaConv.getAlphaIndex(keyData[0]) + 1) % alphaConv.getAlphaLength();

            tmp = new int[1];

            tmp[0] = value;
        } catch (Exception e) {
            LogUtil.logError(CaesarPlugin.PLUGIN_ID, "Exception while generating a key", e, true);
        }

        return tmp;
    }

    @Override
    public String getAlgorithmName() {
        return "Caesar"; //$NON-NLS-1$
    }
}

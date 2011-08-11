// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.xor.algorithm;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.crypto.classic.xor.XorPlugin;

public class XorAlgorithm extends AbstractClassicAlgorithm {
	
	public static final XorAlgorithmSpecification specification = new XorAlgorithmSpecification();
	
	public XorAlgorithm() {
        engine = new XorEngine();
    }

    @Override
    protected int[] generateKey(char[] keyData) {
        try {
            return alphaConv.charArrayToIntArray(alphaConv.filterNonAlphaChars((keyData)));
        } catch (Exception e) {
            LogUtil.logError(XorPlugin.PLUGIN_ID, "Exception while filtering an currentAlphabet", e, true);
            return new int[0];
        }
    }

    @Override
    public String getAlgorithmName() {
        return "XOR"; //$NON-NLS-1$
    }

}

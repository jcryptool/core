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
package org.jcryptool.crypto.classic.substitution.algorithm;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.crypto.classic.substitution.SubstitutionPlugin;

public class SubstitutionAlgorithm extends AbstractClassicAlgorithm {
    
	public static final SubstitutionAlgorithmSpecification specification = new SubstitutionAlgorithmSpecification();
	
	/**
     * Constructor The specific engine of the algorithm is assigned.
     *
     */
    public SubstitutionAlgorithm() {
        engine = new SubstitutionEngine();

    }

    @Override
    protected int[] generateKey(char[] keyData) {

        try {
            return alphaConv.charArrayToIntArray(alphaConv.filterNonAlphaChars((keyData)));
        } catch (Exception e) {
            LogUtil.logError(SubstitutionPlugin.PLUGIN_ID, "Exception while filtering an currentAlphabet", e, true);
            return null;
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Substitution"; //$NON-NLS-1$
    }

}

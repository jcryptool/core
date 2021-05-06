// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.paramspecs;

import java.io.IOException;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.operations.FlexiProviderOperationsPlugin;
import org.jdom.Element;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidParameterSpecException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;
import de.flexiprovider.api.parameters.AlgorithmParameters;

@SuppressWarnings("serial")
public class AlgorithmParameterSpecElement extends Element {
    private AlgorithmParameterSpec algorithmParameterSpec;

    public AlgorithmParameterSpecElement(String algorithmName, AlgorithmParameterSpec spec) {
        super("AlgorithmParameterSpec"); //$NON-NLS-1$
        setAttribute("class", spec.getClass().getName()); //$NON-NLS-1$
        setAttribute("algorithm", algorithmName); //$NON-NLS-1$
        setAlgorithmParameters(algorithmName, spec);
        algorithmParameterSpec = spec;
    }

    public AlgorithmParameterSpecElement(Element algorithmParameterSpecElement) {
        super("AlgorithmParameterSpec"); //$NON-NLS-1$
        setAttribute("class", algorithmParameterSpecElement.getAttributeValue("class")); //$NON-NLS-1$ //$NON-NLS-2$
        setAttribute("algorithm", algorithmParameterSpecElement.getAttributeValue("algorithm")); //$NON-NLS-1$ //$NON-NLS-2$
        setText(algorithmParameterSpecElement.getText());
        algorithmParameterSpec = getAlgorithmParameterSpec(getAttributeValue("algorithm"), //$NON-NLS-1$
                algorithmParameterSpecElement.getText());
    }

    public AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return algorithmParameterSpec;
    }

    private AlgorithmParameterSpec getAlgorithmParameterSpec(String algorithmName, String encoded) {
        if (encoded != null) {
            LogUtil.logInfo("getAlgorithmParameterSpec"); //$NON-NLS-1$
            try {
                byte[] encodedParams = Base64Coder.decode(encoded);
                AlgorithmParameters params = Registry.getAlgParams(algorithmName);
                params.init(encodedParams);
                return params.getParameterSpec(Registry.getAlgParamSpecClass(algorithmName));
            } catch (IOException e) {
                LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "IOException while decoding AlgorithmParameterParameters", e, true);
            } catch (NoSuchAlgorithmException e) {
                LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "NoSuchAlgorithmException while decoding AlgorithmParameterParameters", e, true);
            } catch (InvalidParameterSpecException e) {
                LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "InvalidParameterSpecException while decoding AlgorithmParameterParameters", e, true);
            }
        }
        return null;
    }

    private void setAlgorithmParameters(String algorithmName, AlgorithmParameterSpec spec) {
        if (spec != null) {
            try {
                AlgorithmParameters params = Registry.getAlgParams(algorithmName);
                params.init(spec);
                setText(String.valueOf(Base64Coder.encode(params.getEncoded())));
            } catch (IOException e) {
                LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "IOException while encoding AlgorithmParameterParameters", e, true);
            } catch (NoSuchAlgorithmException e) {
                LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "NoSuchAlgorithmException while encoding AlgorithmParameterParameters", e, true);
            } catch (InvalidParameterSpecException e) {
                LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "InvalidParameterSpecException while encoding AlgorithmParameterParameters", e, true);
            }
        }
    }

}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
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
import de.flexiprovider.common.mode.ModeParameterSpec;

@SuppressWarnings("serial")
public class ModeParameterSpecElement extends Element {
	private ModeParameterSpec modeParameterSpec;

	public ModeParameterSpecElement(ModeParameterSpec spec) {
		super("ModeParameterSpec"); //$NON-NLS-1$
		setModeParameters(spec);
		modeParameterSpec = spec;
	}

	public ModeParameterSpecElement(Element modeParameterSpecElement) {
		super("ModeParameterSpec"); //$NON-NLS-1$
		modeParameterSpec = getModeParameterSpec(modeParameterSpecElement.getText());
	}

	public ModeParameterSpec getModeParameterSpec() {
		return modeParameterSpec;
	}

	private ModeParameterSpec getModeParameterSpec(String encoded) {
		if (encoded != null) {
			LogUtil.logInfo("getModeParameterSpec"); //$NON-NLS-1$

			try {
				byte[] encodedParams = Base64Coder.decode(encoded);
				AlgorithmParameters params = Registry.getAlgParams("Mode"); //$NON-NLS-1$
				params.init(encodedParams);
				return (ModeParameterSpec)params.getParameterSpec(ModeParameterSpec.class);
			} catch (IOException e) {
				LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "IOException while decoding ModeParameters", e, false);
			} catch (NoSuchAlgorithmException e) {
				LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "NoSuchAlgorithmException while decoding ModeParameters", e, true);
			} catch (InvalidParameterSpecException e) {
				LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "InvalidParameterSpecException while decoding ModeParameters", e, true);
			}
		}
		return null;
	}

	private void setModeParameters(AlgorithmParameterSpec modeSpec) {
		if (modeSpec != null) {
			try {
				AlgorithmParameters params = Registry.getAlgParams("Mode"); //$NON-NLS-1$
				params.init(modeSpec);
				setText(String.valueOf(Base64Coder.encode(params.getEncoded())));
			} catch (IOException e) {
				LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "IOException while encoding ModeParameters", e, true);
			} catch (NoSuchAlgorithmException e) {
				LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "NoSuchAlgorithmException while encoding ModeParameters", e, true);
			} catch (InvalidParameterSpecException e) {
				LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID,
                        "InvalidParameterSpecException while encoding ModeParameters", e, true);
			}
		}
	}

}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
/**
 *
 */
package de.flexiprovider;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.providers.AbstractProviderController;

import de.flexiprovider.core.FlexiCoreProvider;
import de.flexiprovider.ec.FlexiECProvider;
import de.flexiprovider.nf.FlexiNFProvider;
import de.flexiprovider.pqc.FlexiPQCProvider;

/**
 * Serves as the focal point for adressing the FlexiProvider library.<br>
 * Attempts at putting the FlexiProvider library as the priority 1-4 providers for JCrypTool.
 * 
 * @author tkern
 *
 */
public class FlexiProviderController extends AbstractProviderController {
	
	/**
	 * Empty no-args constructor.
	 */
	public FlexiProviderController() {
	}

	/**
	 * Adds the four distinctive FlexiProvider cryptographic providers as the top priority providers for the platform.
	 * 
	 * @see org.jcryptool.core.operations.providers.AbstractProviderController#addProviders()
	 */
	@Override
	public List<String> addProviders() {
		List<String> providers = new ArrayList<String>(4);

		Provider flexiCore = new FlexiCoreProvider();
		flexiCore.remove("SecureRandom.BBS");
		flexiCore.remove("SecureRandom.BBSRandom");
		flexiCore.remove("Alg.Alias.SecureRandom.BBSRandom");
		Security.addProvider(flexiCore);
		providers.add(flexiCore.getName() + AbstractProviderController.SEPARATOR + flexiCore.getInfo());
		LogUtil.logInfo("Security Provider '" + flexiCore.getName() + "' added.");

		Provider flexiEC = new FlexiECProvider();
        flexiEC.remove("SecureRandom.ECPRNG");
        Security.addProvider(flexiEC);
		providers.add(flexiEC.getName() + AbstractProviderController.SEPARATOR + flexiEC.getInfo());
		LogUtil.logInfo("Security Provider '" + flexiEC.getName() + "' added.");

		Provider flexiPQC = new FlexiPQCProvider();
		Security.addProvider(flexiPQC);
		providers.add(flexiPQC.getName() + AbstractProviderController.SEPARATOR + flexiPQC.getInfo());
		LogUtil.logInfo("Security Provider '" + flexiPQC.getName() + "' added.");

		Provider flexiNF = new FlexiNFProvider();
		Security.addProvider(flexiNF);
		providers.add(flexiNF.getName() + AbstractProviderController.SEPARATOR + flexiNF.getInfo());
		LogUtil.logInfo("Security Provider '" + flexiNF.getName() + "' added.");
		
		return providers;
	}

}

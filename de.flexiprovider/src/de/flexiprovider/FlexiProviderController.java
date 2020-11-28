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
import java.util.LinkedList;
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
	
	public static final FlexiNFProvider FLEXI_NF_PROVIDER = new FlexiNFProvider();
	public static final FlexiPQCProvider FLEXI_PQC_PROVIDER = new FlexiPQCProvider();
	public static final FlexiECProvider FLEXI_EC_PROVIDER = new FlexiECProvider();
	public static final FlexiCoreProvider FLEXICORE_PROVIDER = new FlexiCoreProvider();
	private static final Provider[] FLEXI_PROVIDERS = new Provider[] {FlexiProviderController.FLEXICORE_PROVIDER, FLEXI_EC_PROVIDER, FLEXI_PQC_PROVIDER, FLEXI_NF_PROVIDER};
	
	static {
		FLEXICORE_PROVIDER.remove("SecureRandom.BBS");
		FLEXICORE_PROVIDER.remove("SecureRandom.BBSRandom");
		FLEXICORE_PROVIDER.remove("Alg.Alias.SecureRandom.BBSRandom");
        FLEXI_EC_PROVIDER.remove("SecureRandom.ECPRNG");
	}

	/**
	 * Empty no-args constructor.
	 */
	public FlexiProviderController() {
	}

	// these get cached before any of JCT's providers are set, so they can be repositioned (before or after) according to the current needs
	private List<Provider> defaultProviders = null;
	
	private void cacheDefaultProviders() {
		if (defaultProviders == null) {
			defaultProviders = new LinkedList<>();
			for (Provider p : Security.getProviders()) {
				defaultProviders.add(p);
			}
		}
	}


	
	@Override
	public void setProviders__sunPromoted() {
		LogUtil.logInfo(FlexiProviderPlugin.PLUGIN_ID, "promoting sun security providers in FlexiProviderController");
		cacheDefaultProviders();
		for(Provider p: Security.getProviders()) {
			Security.removeProvider(p.getName());
		}
		// add the sun providers first
		for (Provider provider: defaultProviders) {
//			LogUtil.logInfo(FlexiProviderPlugin.PLUGIN_ID, "adding Provider: " + provider.getName());
			Security.addProvider(provider);
		}
		// add the Flexiproviders after
		for (Provider provider : FLEXI_PROVIDERS) {
//			LogUtil.logInfo(FlexiProviderPlugin.PLUGIN_ID, "adding Provider: " + provider.getName());
			Security.addProvider(provider);
		}
		System.out.println("Sun cryptoprovider prioritized");
	}
	@Override
	public void setProviders__flexiPromoted() {
//		LogUtil.logInfo(FlexiProviderPlugin.PLUGIN_ID, "promoting flexi security providers in FlexiProviderController");
		cacheDefaultProviders();
		for(Provider p: Security.getProviders()) {
			Security.removeProvider(p.getName());
		}
		// add the Flexiproviders first
		for (Provider provider : FLEXI_PROVIDERS) {
//			LogUtil.logInfo(FlexiProviderPlugin.PLUGIN_ID, "adding Provider: " + provider.getName());
			Security.addProvider(provider);
		}

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		// add the sun providers after
		for (Provider provider: defaultProviders) {
//			LogUtil.logInfo(FlexiProviderPlugin.PLUGIN_ID, "adding Provider: " + provider.getName());
			Security.addProvider(provider);
		}
//		System.out.println("FlexiProvider cryptoprovider prioritized");
	}
	
	/**
	 * Adds the four distinctive FlexiProvider cryptographic providers as the top priority providers for the platform.
	 * 
	 * @see org.jcryptool.core.operations.providers.AbstractProviderController#addProviders()
	 */
	@Override
	public List<String> addProviders() {
		List<String> providers = new ArrayList<String>(4);

		providers.add(FlexiProviderController.FLEXICORE_PROVIDER.getName() + AbstractProviderController.SEPARATOR + FlexiProviderController.FLEXICORE_PROVIDER.getInfo());
		providers.add(FLEXI_EC_PROVIDER.getName() + AbstractProviderController.SEPARATOR + FLEXI_EC_PROVIDER.getInfo());
		providers.add(FLEXI_PQC_PROVIDER.getName() + AbstractProviderController.SEPARATOR + FLEXI_PQC_PROVIDER.getInfo());
		providers.add(FLEXI_NF_PROVIDER.getName() + AbstractProviderController.SEPARATOR + FLEXI_NF_PROVIDER.getInfo());

		setProviders__sunPromoted();
		LogUtil.logInfo(FlexiProviderPlugin.PLUGIN_ID, "CURRENT PROVIDERS: ----");
		for (Provider p: Security.getProviders()) {
			LogUtil.logInfo(FlexiProviderPlugin.PLUGIN_ID, "- " + p.getName());
		}
		LogUtil.logInfo(FlexiProviderPlugin.PLUGIN_ID, "END: CURRENT PROVIDERS: ----");
		return providers;
	}


	public static void reset_crypto_providers_generic() {
		// TODO Auto-generated method stub
		
	}

}

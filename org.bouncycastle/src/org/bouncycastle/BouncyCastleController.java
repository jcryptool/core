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
package org.bouncycastle;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.providers.AbstractProviderController;

/**
 * @author tkern
 *
 */
public class BouncyCastleController extends AbstractProviderController {
	
	public BouncyCastleController() {}

	/**
	 * @see org.jcryptool.core.operations.providers.AbstractProviderController#addProviders()</br>
	 * This method adds the bouncycastle as crypto provider to the list of available crypto providers.
	 */
	@Override
	public List<String> addProviders() {
		List<String> providers = new ArrayList<String>(1);

		Provider bc = new BouncyCastleProvider();
		Security.addProvider(bc);
		providers.add(bc.getName() + AbstractProviderController.SEPARATOR + bc.getInfo());
		LogUtil.logInfo("Security Provider '" + bc.getName() + "' added.");
		
		return providers;
	}

	@Override
	public void setProviders__sunPromoted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProviders__flexiPromoted() {
		// TODO Auto-generated method stub
		
	}

}

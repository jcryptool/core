// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.engines.listener;

import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEngine;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEngineFactory;
import org.jcryptool.crypto.flexiprovider.operations.engines.ICheckOperationListener;

/**
 * @author Anatoli Barski
 */
public class CheckOperationListener implements ICheckOperationListener {

	@Override
	public boolean checkOperation(IFlexiProviderOperation operation) {
		FlexiProviderEngine engine = FlexiProviderEngineFactory.createEngine(operation.getRegistryType());
		return engine.init(operation) != null;
	}
}

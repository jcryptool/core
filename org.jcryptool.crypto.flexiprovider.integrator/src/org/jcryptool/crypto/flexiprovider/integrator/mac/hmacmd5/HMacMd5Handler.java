// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.integrator.mac.hmacmd5;

import org.jcryptool.crypto.flexiprovider.integrator.IntegratorHandler;

/**
 * Provides specifications for the DummyAction
 * @author mwalthart
 * @author Holger Friedrich (support for Commands, additional class based on HMacMd5Action)
 *
 */
public class HMacMd5Handler extends IntegratorHandler {
	@Override
	protected String getFlexiProviderAlgorithmName() {return "HmacMD5";} //$NON-NLS-1$
	@Override
	protected String getReadableAlgorithmName() {return "HMacMD5";} //$NON-NLS-1$
	@Override
	protected String getShowKey() {return "HmacMD5";} //$NON-NLS-1$
}
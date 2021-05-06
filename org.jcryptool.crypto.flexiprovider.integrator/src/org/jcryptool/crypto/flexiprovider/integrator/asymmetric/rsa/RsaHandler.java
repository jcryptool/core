// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.integrator.asymmetric.rsa;

import org.jcryptool.crypto.flexiprovider.integrator.IntegratorHandler;

/**
 * Provides specifications for the DummyAction
 * @author mwalthart
 * @author Holger Friedrich (support for Commands, based on RsaAction)
 *
 */
public class RsaHandler extends IntegratorHandler {
	@Override
	protected String getFlexiProviderAlgorithmName() {return "RSA_PKCS1_v1_5";} //$NON-NLS-1$
	@Override
	protected String getReadableAlgorithmName() {return "RSA";} //$NON-NLS-1$
	@Override
	protected String getShowKey() {return "RSA";} //$NON-NLS-1$
}
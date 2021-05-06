// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.providers;

import java.security.Provider;
import java.util.List;

public abstract class AbstractProviderController {

    public static final String SEPARATOR = ";"; //$NON-NLS-1$

    public abstract List<String> addProviders();
    
	public abstract void setProviders__sunPromoted();
	public abstract void setProviders__flexiPromoted();

}

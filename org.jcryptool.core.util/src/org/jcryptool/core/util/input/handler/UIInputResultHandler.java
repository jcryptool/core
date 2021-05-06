// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.input.handler;

import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;

/**
 * Interface for handling the InputVerificationResults that come from UIInputs. For example, register the implementing
 * class as Observer to the AbstractUIInput and direct all InputVerificationResults to
 * {@link #handleVerificationResultMsg(InputVerificationResult)}.
 * 
 * @author Simon L
 */
public interface UIInputResultHandler {

    /**
     * incoming verification results should be directed here.
     * 
     * @param result the InputVerificationResult of a AbstractUIInput
     * @param result the AbstractUIInput where the result came from
     */
    public void handleVerificationResultMsg(AbstractUIInput<?> origin, InputVerificationResult result);

}

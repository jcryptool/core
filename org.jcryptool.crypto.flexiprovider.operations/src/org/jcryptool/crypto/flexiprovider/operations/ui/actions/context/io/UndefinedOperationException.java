// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----

package org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io;

/**
 * An exception type thrown when the operation type has not been selected yet
 * (used by the code determining whether to use a SAVE or an OPEN dialog)
 * 
 * @author Holger Friedrich
 *
 */
public class UndefinedOperationException extends Exception {

	/** Added because there was an Eclipse warning about the missing serialVersionUID */
	private static final long serialVersionUID = 4518623688780444043L;

	public UndefinedOperationException() {
		super();
	}

	public UndefinedOperationException(String arg0) {
		super(arg0);
	}

	public UndefinedOperationException(Throwable arg0) {
		super(arg0);
	}

	public UndefinedOperationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UndefinedOperationException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}

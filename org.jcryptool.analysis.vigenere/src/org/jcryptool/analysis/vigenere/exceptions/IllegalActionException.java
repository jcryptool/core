/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.exceptions;

/**
 * This exception is thrown whenever the actions of user cannot be processed.
 * 
 * @author Ronny Wolf
 * @version 0.0.1, 2010/07/11
 */
public class IllegalActionException extends Exception {
    /**
     * The required identification number for serialization.
     */
    private static final long serialVersionUID = -536945539070337772L;

    /**
     * Constructs an <code>IllegalActionException</code>.
     */
    public IllegalActionException() {
        super();
    }

    /**
     * Constructs an <code>IllegalActionException</code> with the specified,
     * detailed message.
     * 
     * @param message
     *            the detail message.
     */
    public IllegalActionException(final String message) {
        super(message);
    }
}

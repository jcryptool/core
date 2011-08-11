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
 * This exception is thrown whenever it is not possible to access any kind of
 * content.
 * 
 * @author Ronny Wolf
 * @version 0.0.1, 2010/07/06
 */
public class NoContentException extends Exception {

    /**
     * The required identification number for serialization.
     */
    private static final long serialVersionUID = 6429833284373456707L;

    /**
     * Constructs an <code>NoContentException</code>.
     */
    public NoContentException() {
        super();
    }

    /**
     * Constructs an <code>NoContentException</code> with the specified,
     * detailed message.
     * 
     * @param message
     *            the detail message.
     */
    public NoContentException(final String message) {
        super(message);
    }
}

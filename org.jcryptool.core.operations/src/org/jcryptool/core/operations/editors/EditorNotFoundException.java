// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.operations.editors;

/**
 * Exception is thrown when {@link EditorsManager} is not able to access an active workbench window or page.
 * 
 * @author Ronny Wolf
 * @version 0.0.1, 2010/07/16
 */
public class EditorNotFoundException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 4364119999916988838L;

    /**
     * Constructs an <code>EditorNotFoundException</code>.
     */
    public EditorNotFoundException() {
        super();
    }

    /**
     * Constructs an <code>EditorNotFoundException</code> with the specified, detailed message.
     * 
     * @param message the detail message.
     */
    public EditorNotFoundException(final String message) {
        super(message);
    }
}

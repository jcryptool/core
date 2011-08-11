//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.util.input;

import org.eclipse.jface.dialogs.IMessageProvider;

/**
 * Contains the results of an input verification. the main indicator is
 * {@link #isValid()}, because this is the most important thing to know
 * about a verification. Furthermore, there can be error messages or warnings,
 * or no comments at all (see {@link #getMessage()} and {@link #getMessageType()}).
 * 
 * @author Simon L
 */
public abstract class InputVerificationResult {
	
	public static final String RESULT_TYPE_DEFAULT = "DEFAULT";

	public static final InputVerificationResult DEFAULT_RESULT_EVERYTHING_OK = new InputVerificationResult() {
		public int getMessageType() { return InputVerificationResult.NONE; }
		public String getMessage() {return "";} //$NON-NLS-1$
		public boolean isValid() {return true;}
		public boolean isStandaloneMessage() {return false;}
		public String toString() {return "OK";} //$NON-NLS-1$
	};
	
	public static final int WARNING = IMessageProvider.WARNING;
    public static final int ERROR = IMessageProvider.ERROR;
    public static final int INFORMATION = IMessageProvider.INFORMATION;
    public static final int NONE = IMessageProvider.NONE;
    
    /**
     * The message displayed in the wizard title bar
     */
    public abstract String getMessage();
    
    /**
     * The message type
     */
    public abstract int getMessageType();

    public Object getResultType() {
    	return RESULT_TYPE_DEFAULT;
    }
    
    /**
     * @return whether the message is a piece of text that
     * has to be inserted into a pattern of text in a 'usual way'
     * or whether it has to be displayed single and unchanged. 
     */
    public abstract boolean isStandaloneMessage();
	
	/**
	 * @return whether the validation was successful in general or not
	 */
	public abstract boolean isValid();
	
}

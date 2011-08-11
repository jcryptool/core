//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.model.ui.wizard.util;

import org.eclipse.jface.dialogs.IMessageProvider;

public interface MWizardMessage {
    public static final int WARNING = IMessageProvider.WARNING;
    public static final int ERROR = IMessageProvider.ERROR;
    public static final int INFORMATION = IMessageProvider.INFORMATION;
    public static final int NONE = IMessageProvider.NONE;
    
    /**
     * The message displayed in the wizard title bar
     */
    public String getMessage();
    
    /**
     * The message type
     */
    public int getMessageType();
    
    /**
     * @return whether the message is a piece of text that
     * has to be inserted into a pattern of text in a 'susual way'
     * or whether it has to be displayed single and unchanged. 
     */
    public boolean isStandaloneMessage();
}
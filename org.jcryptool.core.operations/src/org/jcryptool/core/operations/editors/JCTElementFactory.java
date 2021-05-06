// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.editors;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.util.PathEditorInput;

/**
 * Restores editors after a workbench restart.
 * 
 * @author t-kern
 * 
 */
public class JCTElementFactory implements IElementFactory {
    /** The factory ID */
    public static final String ID = "org.jcryptool.core.operations.service.JCTElementFactory"; //$NON-NLS-1$

    /**
     * @see org.eclipse.ui.IElementFactory#createElement(org.eclipse.ui.IMemento)
     */
    public IAdaptable createElement(IMemento memento) {
        String path = memento.getString("path"); //$NON-NLS-1$
        LogUtil.logInfo("path: " + path); //$NON-NLS-1$
        return new PathEditorInput(path);
    }

}

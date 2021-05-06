// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.commands.ui.eclipseconsole.console;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.ui.console.IOConsoleInputStream;
import org.jcryptool.commands.ui.CommandsUIPlugin;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 *
 *
 * @author Simon L
 */
public class IOConsoleInputStreamWrapper extends InputStream {

    private IOConsoleInputStream stream;

    public IOConsoleInputStreamWrapper(IOConsoleInputStream stream) {
        this.stream = stream;
    }

    /*
     * (non-Javadoc)
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException {
        try {
            if (stream.available() > 0) {
                return stream.read();
            } else
                return -1;
        } catch (IOException e) {
            if (!e.getMessage().equals("Input Stream Closed")) { //$NON-NLS-1$
                LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e);
                return -1;
            } else
                throw e;
        }
    }

    @Override
    public int available() throws IOException {
        try {
            return stream.available();
        } catch (IOException e) {
            if (!e.getMessage().equals("Input Stream Closed")) { //$NON-NLS-1$
                LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e);
                return -1;
            } else
                throw e;
        }
    }

}

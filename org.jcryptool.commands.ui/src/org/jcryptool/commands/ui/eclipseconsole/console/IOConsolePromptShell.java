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

import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.jcryptool.commands.ui.CommandsUIPlugin;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 *
 *
 * @author Simon L
 */
public class IOConsolePromptShell extends IOConsoleShell {

    protected static final String DEFAULT_PROMPT = "> "; //$NON-NLS-1$

    public IOConsolePromptShell(IOConsole console, boolean autoStartMonitoring) {
        super(console, autoStartMonitoring);

        IOConsoleOutputStream o = console.newOutputStream();
        try {
            sendPrompt(o);
        } catch (IOException e) {
            LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e);
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e);
            }
        }
    }

    public IOConsolePromptShell(IOConsole console) {
        this(console, false);
    }

    /**
     * Sends the promt to the console. This method is called after every linebreak before which was user-input.
     *
     * @param o the output stream. Does not need to be closed.
     * @throws IOException
     */
    protected void sendPrompt(IOConsoleOutputStream o) throws IOException {
        o.write(DEFAULT_PROMPT);
    }

    @Override
    protected void returnDetected() {
        super.returnDetected();
        IOConsoleOutputStream o = console.newOutputStream();
        try {
            sendPrompt(o);
        } catch (IOException e) {
            LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e);
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e);
            }
        }
    }

}

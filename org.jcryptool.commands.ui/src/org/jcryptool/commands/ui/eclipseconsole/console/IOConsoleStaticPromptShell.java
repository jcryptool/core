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
public class IOConsoleStaticPromptShell extends IOConsolePromptShell {

    private String prompt;

    public IOConsoleStaticPromptShell(IOConsole console, boolean autoStartMonitoring, String prompt) {
        super(console, autoStartMonitoring);
        this.prompt = prompt;

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

    public IOConsoleStaticPromptShell(IOConsole console, String prompt) {
        this(console, false, prompt);
    }

    @Override
    protected void sendPrompt(IOConsoleOutputStream o) throws IOException {
        if (prompt != null)
            o.write(prompt);
    }

}

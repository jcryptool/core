// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.commands.core;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.jcryptool.commands.core.api.IllegalCommandException;

/**
 * CommandFactory and Factory via loadExtensions()
 *
 * @author paw
 *
 */
public class ProxiedCommand implements Command {
    private static final String SYNTAX_EXPT_DEFAULT_VALUE = ""; //$NON-NLS-1$
    protected static final String COMMAND_NAME_ATTRIBUTE = "name"; //$NON-NLS-1$
    private static final String COMMAND_SYNTAX_ATTRIBUTE = "syntax"; //$NON-NLS-1$
    private static final String DESCRIPTION_ATTRIBUTE = "description"; //$NON-NLS-1$
    private static final String COMMAND_IMPL = "commandImpl"; //$NON-NLS-1$

    protected String commandName = ""; //$NON-NLS-1$

    protected IConfigurationElement configurationElement = null;
    private Command proxiedObject = null;

    ProxiedCommand(IConfigurationElement configurationElement) {
        this.configurationElement = configurationElement;
        this.commandName = getAttribute(COMMAND_NAME_ATTRIBUTE, null);
    }

    public Options createOptions() {
        return getProxiedObject().createOptions();
    }

    public void execute(CommandLine commandLine) throws IllegalCommandException {
        getProxiedObject().execute(commandLine);
    }

    public String getResult() {
        return getProxiedObject().getResult();
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandSyntax() {
        if (getAttribute(COMMAND_SYNTAX_ATTRIBUTE, SYNTAX_EXPT_DEFAULT_VALUE).equals(SYNTAX_EXPT_DEFAULT_VALUE)) {
            return getSyntaxTerm(this.getCommandName(), this.createOptions());
        }

        return getAttribute(COMMAND_SYNTAX_ATTRIBUTE, null);

    }

    public String getDescription() {
        return getAttribute(DESCRIPTION_ATTRIBUTE, SYNTAX_EXPT_DEFAULT_VALUE); //$NON-NLS-1$
    }

    private Command getProxiedObject() {
        try {
            if (proxiedObject == null) {
                proxiedObject = (Command) configurationElement.createExecutableExtension(COMMAND_IMPL);
                proxiedObject.setCommandName(getCommandName());
                proxiedObject.setCommandSyntax(getCommandSyntax());
                proxiedObject.setDescription(getDescription());
            }
            return proxiedObject;
        } catch (CoreException e) {
            throw new RuntimeException("Couldn't load Proxied Object " //$NON-NLS-1$
                    + configurationElement.getAttribute(COMMAND_IMPL), e);
        }
    }

    /**
     * Generates a syntax term for a command
     *
     * @param commandName the name of the command
     * @param options the options of this command
     * @return
     */
    private static String getSyntaxTerm(String commandName, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        StringWriter writer = new StringWriter();
        formatter.printUsage(new PrintWriter(writer), Command.CONSOLE_WIDTH, commandName, options);
        return writer.toString().replaceAll("\\Ausage:?( )*", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$
    }

    protected String getAttribute(String name, String defaultValue) {
        String value = configurationElement.getAttribute(name);
        if (value != null) {
            return value;
        }
        if (defaultValue != null) {
            return defaultValue;
        }
        throw new IllegalArgumentException("Missing Attribute: " + name); //$NON-NLS-1$
    }

    public final void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public final void setDescription(String description) {
    }

    public final void setCommandSyntax(String commandSyntax) {
    }
}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.commands.core;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jcryptool.commands.core.api.IllegalCommandException;

/**
 * JCrpTool=> "CommandName" -Options "Result"
 */
public abstract interface Command {
	
	public static final int CONSOLE_WIDTH = 120;
	
    public String getDescription();

    public String getCommandName();

    public String getCommandSyntax();

    public void setCommandName(String commandName);

    public void setDescription(String description);

    public void setCommandSyntax(String commandSyntax);

    /**
     * Gets called after createOptions()
     * Use the information of commandLine to evaluate the input and run the algorithm.
     * Store the result of algorithm for the getResult() function.
     * @param commandLine
     * @throws IllegalCommandException
     */
    public void execute(CommandLine commandLine) throws IllegalCommandException;

    /**
     * Gets called after execute(..)
     * 
     * @return result of algorithm
     */
    public String getResult();

    /**
     * Options shouldn't be held by Object. Only generate and forget.
     *
     * @return
     */
    public Options createOptions();
}

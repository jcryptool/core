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

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jcryptool.commands.core.ExtendedHelpCommand.Example;
import org.jcryptool.commands.core.api.AbstractCommand;

public class HELP_Command extends AbstractCommand {
    private String result;

    public Options createOptions() {
        return HelpCommand.createStaticOptions();
    }

    public void execute(CommandLine commandLine) {
        StringBuilder result = new StringBuilder();
        try {
            if (commandLine.hasOption(HelpCommand.EXAMPLES_SHORT_OPT)) {
                if (commandLine.hasOption(HelpCommand.COMMANDLIST_SHORT_OPT)) {
                    String mask = Messages.HelpCommand_chooseOnlyOneOptionHint;
                    throw new ParseException(String.format(mask, HelpCommand.EXAMPLES_SHORT_OPT,
                            HelpCommand.COMMANDLIST_SHORT_OPT));
                }

                if (commandLine.getArgs().length == 1) {
                    String commandName = commandLine.getArgs()[0];
                    Command command = HelpCommand.getCommands().get(commandName);

                    List<Example> examples = HelpCommand.getExamples(commandName, command);
                    if (examples.size() != 0) {
                        result.append(HelpCommand.getExampleString(examples, commandName));
                    } else {
                        throw new ParseException(Messages.HelpCommand_noExampleSupport);
                    }

                } else {
                    throw new ParseException(Messages.HelpCommand_tooManyArgs);
                }
            } else if (commandLine.hasOption(HelpCommand.COMMANDLIST_SHORT_OPT)) {
                if (commandLine.getArgs().length == 0) {
                    for (Command command : CommandFactory.loadUniqueExtensions()) {
                        HelpCommand.printCommand(result, command);
                    }
                } else {
                    throw new ParseException(Messages.HelpCommand_tooManyArgs);
                }
            } else if (commandLine.getArgs().length == 1) {
                String commandName = commandLine.getArgs()[0];
                Command command = HelpCommand.getCommands().get(commandName);

                result.append(HelpCommand.getDetailedHelpFor(commandName, command,
                        HelpCommand.reverseCommandline(this.getCommandName(), commandLine)));
            } else if (commandLine.getArgs().length == 0) {
                result.append(HelpCommand.getGeneralHelptext());
            } else {
                throw new ParseException(Messages.HelpCommand_tooManyArgsSyntaxhelpRef);
            }
        } catch (ParseException e) {
            result.append(e.getMessage());
        }
        this.result = result.toString();
    }

    public String getResult() {
        return result;
    }
}

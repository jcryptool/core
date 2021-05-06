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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.osgi.util.NLS;
import org.jcryptool.commands.core.ExtendedHelpCommand.Example;
import org.jcryptool.commands.core.api.AbstractCommand;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;

public class HelpCommand extends AbstractCommand {
    private static final String REF_FURTHER_INFORMATION_ONLINEHELP = Messages.HelpCommand_2;
    static final String COMMANDLIST_SHORT_OPT = "l"; //$NON-NLS-1$
    static final String EXAMPLES_SHORT_OPT = "x"; //$NON-NLS-1$
    static final String COMMANDLIST_LONG_OPT = "commandlist"; //$NON-NLS-1$
    static final String EXAMPLES_LONG_OPT = "examples"; //$NON-NLS-1$
    private static final String ARGUMENT_DESCRIPTIONS = Messages.HelpCommand_argdescriptionsLabel;
    private final static String SYNTAX_LABEL = Messages.HelpCommand_syntaxLabel;

    private static HashMap<String, Command> commands;
    private String result;

    /**
     * @return the help text, that argumentless calls of the help command show.
     */
    public static String getGeneralHelptext() {
        return InputStreamToString(openResourceStream(Messages.HelpCommand_helpfile));
    }

    @SuppressWarnings("static-access")
    public static Options createStaticOptions() {
        Options options = new Options();

        options.addOption(OptionBuilder.withLongOpt(EXAMPLES_LONG_OPT).hasArg(false) //$NON-NLS-1$
        .isRequired(false).withDescription( //$NON-NLS-1$
                Messages.HelpCommand_show_examples_label).create(EXAMPLES_SHORT_OPT));

        options.addOption(OptionBuilder.withLongOpt(COMMANDLIST_LONG_OPT).hasArg(false) //$NON-NLS-1$
        .isRequired(false).withDescription(Messages.HelpCommand_listcommandsLabel).create(COMMANDLIST_SHORT_OPT));

        return options;
    }

    public Options createOptions() {
        return createStaticOptions();
    }

    public static synchronized HashMap<String, Command> getCommands() {
        if (commands == null) {
            commands = new HashMap<String, Command>();
            for (Command command : CommandFactory.loadExtensions()) {
                commands.put(command.getCommandName(), command);
            }
        }
        return commands;
    }

    public void execute(CommandLine commandLine) {
        StringBuilder result = new StringBuilder();
        try {
            if (commandLine.hasOption(EXAMPLES_SHORT_OPT)) {
                if (commandLine.hasOption(COMMANDLIST_SHORT_OPT)) {
                    String mask = Messages.HelpCommand_chooseOnlyOneOptionHint;
                    throw new ParseException(String.format(mask, EXAMPLES_SHORT_OPT, COMMANDLIST_SHORT_OPT));
                }

                if (commandLine.getArgs().length == 1) {
                    String commandName = commandLine.getArgs()[0];
                    Command command = getCommands().get(commandName);

                    List<Example> examples = getExamples(commandName, command);
                    if (examples.size() != 0) {
                        result.append(getExampleString(examples, commandName));
                    } else {
                        throw new ParseException(Messages.HelpCommand_noExampleSupport);
                    }

                } else {
                    if (commandLine.getArgs().length > 1)
                        throw new ParseException(Messages.HelpCommand_tooManyArgs);
                    if (commandLine.getArgs().length < 1)
                        throw new ParseException(Messages.HelpCommand_tooFewArgs);
                }
            } else if (commandLine.hasOption(COMMANDLIST_SHORT_OPT)) {
                if (commandLine.getArgs().length == 0) {
                    for (Command command : CommandFactory.loadUniqueExtensions()) {
                        printCommand(result, command);
                    }
                } else {
                    throw new ParseException(Messages.HelpCommand_tooManyArgs);
                }
            } else if (commandLine.getArgs().length == 1) {
                String commandName = commandLine.getArgs()[0];
                Command command = getCommands().get(commandName);

                result.append(getShortHelpFor(commandName, command,
                        reverseCommandline(this.getCommandName(), commandLine)));
            } else if (commandLine.getArgs().length == 0) {
                result.append(getGeneralHelptext());
            } else {
                throw new ParseException(Messages.HelpCommand_tooManyArgsSyntaxhelpRef);
            }
        } catch (ParseException e) {
            result.append(e.getMessage());
        }
        this.result = result.toString();
    }

    private static String makeLeerString(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(" "); //$NON-NLS-1$
        }
        return builder.toString();
    }

    /**
     * creates a String for output which contains the formatted examples.
     *
     * @param examples the examples
     * @param nameOfCommand the name of the command the examples are for (specify the name that was specified in the
     *        command line)
     */
    static String getExampleString(Collection<Example> examples, String nameOfCommand) {
        int maxlength = 0;
        for (Example e : examples)
            maxlength = Math.max(maxlength, e.exampleCmdLine.length());

        StringBuilder builder = new StringBuilder();
        Iterator<Example> it = examples.iterator();

        if (it.hasNext()) {
            Example example = it.next();
            int lengthDiff = maxlength - example.exampleCmdLine.length();
            builder.append("'" + makeConcreteExampleString(example.exampleCmdLine, nameOfCommand) + "'" + makeLeerString(lengthDiff) + " -> " + example.explanation); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        while (it.hasNext()) {
            Example example = it.next();
            int lengthDiff = maxlength - example.exampleCmdLine.length();
            builder.append("\n" + "'" + makeConcreteExampleString(example.exampleCmdLine, nameOfCommand) + "'" + makeLeerString(lengthDiff) + " -> " + example.explanation); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }
        return builder.toString();
    }

    /**
     * returns a command line example where the placeholder for the command name has been filled in
     *
     * @param exampleCmdLine
     */
    private static String makeConcreteExampleString(String exampleCmdLine, String commandName) {
        return exampleCmdLine.replaceFirst("#commandname#", commandName); //$NON-NLS-1$
    }

    /**
     * returns the syntax term for a command
     *
     * @param command the command
     * @return
     */
    private static String getSyntaxTerm(Command command) {
        return command.getCommandSyntax();
    }

    /**
     * Generates an argument table for a command
     *
     * @param options the options of this command
     * @return
     */
    private static String getGeneratedArglist(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        StringWriter writer = new StringWriter();
        formatter.printOptions(new PrintWriter(writer), CONSOLE_WIDTH, options, 4, 8);
        return writer.toString();
    }

    /**
     * This function returns the help for calls of this pattern: HELP cmd ?? cmd cmd HELP cmd ?? cmd -HELP cmd --HELP
     *
     * @param commandName
     * @param command
     * @return
     */
    public static String getDetailedHelpFor(String commandName, Command command, String commandCallLine) {
        if (command != null) {
            List<ExtendedHelpCommand.Example> examples = getExamples(commandName, command);
            Set<String> aliases = getAliases(commandName, command);

            String description = command.getDescription();
            String syntax = getSyntaxTerm(command);
            String argtable = getGeneratedArglist(command.createOptions());

            StringBuilder maskBuilder = new StringBuilder();
            List<String> maskArgs = new LinkedList<String>();

            // Description, Syntax, argtable [, examples] [, aliases]

            maskBuilder.append("%s"); //$NON-NLS-1$
            maskArgs.add(description);
            maskBuilder.append("\n" + SYNTAX_LABEL + "%s"); //$NON-NLS-1$ //$NON-NLS-2$
            maskArgs.add(syntax);
            maskBuilder.append("\n" + ARGUMENT_DESCRIPTIONS + "\n%s"); //$NON-NLS-1$ //$NON-NLS-2$
            maskArgs.add(argtable);
            if (examples.size() != 0) {
                String examplesRef = getRefStringToExamples(commandName);
                maskBuilder.append("\n%s"); //$NON-NLS-1$
                maskArgs.add(examplesRef);
            }
            if (aliases.size() != 0) {
                String aliasesRef = getRefForAliases(aliases);
                maskBuilder.append("\n%s"); //$NON-NLS-1$
                maskArgs.add(aliasesRef);
            }
            maskBuilder.append("\n" + REF_FURTHER_INFORMATION_ONLINEHELP); //$NON-NLS-1$

            Object[] maskArgsArray = maskArgs.toArray();
            return String.format(maskBuilder.toString(), maskArgsArray);
        } else {
            return NLS.bind(Messages.HelpCommand_1, commandName);
        }
    }

    /**
     * This function returns the help for calls of this pattern: help cmd ? cmd cmd help cmd ? cmd -help cmd --help
     *
     * @param commandName
     * @param command
     * @return
     */
    public static String getShortHelpFor(String commandName, Command command, String commandCallLine) {
        if (command != null) {
            String description = command.getDescription();
            String syntax = getSyntaxTerm(command);
            String detailedHelpRef = getRefStringToDetailedHelp(commandName, command, commandCallLine);
            List<ExtendedHelpCommand.Example> examples = getExamples(commandName, command);

            StringBuilder maskBuilder = new StringBuilder();
            List<String> maskArgs = new LinkedList<String>();

            // Description, Syntax [, examples], LongHelpReference

            maskBuilder.append("%s"); //$NON-NLS-1$
            maskArgs.add(description);
            maskBuilder.append("\n" + SYNTAX_LABEL + "%s"); //$NON-NLS-1$ //$NON-NLS-2$
            maskArgs.add(syntax);
            if (examples.size() != 0) {
                String examplesRef = getRefStringToExamples(commandName);
                maskBuilder.append("\n%s"); //$NON-NLS-1$
                maskArgs.add(examplesRef);
            }
            maskBuilder.append("\n%s"); //$NON-NLS-1$
            maskArgs.add(detailedHelpRef);

            maskBuilder.append("\n" + REF_FURTHER_INFORMATION_ONLINEHELP); //$NON-NLS-1$

            Object[] maskArgsArray = maskArgs.toArray();
            return String.format(maskBuilder.toString(), maskArgsArray);
        } else {
            return NLS.bind(Messages.HelpCommand_1, commandName);
        }
    }

    /**
     * returns the help that appaers when a command was called with a bad combination of options/arguments
     *
     * @param commandName the name of the command
     * @param command the command object
     * @param commandCallLine the original line that called the command
     * @param e the exception that was thrown from the parsing process
     */
    public static Object getHelpForBadCommandCall(String commandName, Command command, String commandCallLine,
            ParseException e) {

        String syntax = getSyntaxTerm(command);
        String helpRef = getRefStringToFurtherHelpBadCommandCall(commandName, command, commandCallLine, e);

        StringBuilder maskBuilder = new StringBuilder();
        List<String> maskArgs = new LinkedList<String>();

        // Description, Syntax, argtable [, examples] [, aliases]

        maskBuilder.append("%s"); //$NON-NLS-1$
        maskArgs.add(e.getLocalizedMessage());
        maskBuilder.append("\n" + SYNTAX_LABEL + "%s"); //$NON-NLS-1$ //$NON-NLS-2$
        maskArgs.add(syntax);
        maskBuilder.append("\n%s"); //$NON-NLS-1$
        maskArgs.add(helpRef);

        Object[] maskArgsArray = maskArgs.toArray();
        return String.format(maskBuilder.toString(), maskArgsArray);
    }

    /**
     * returns reference (as help text) to the other names of the command. Assumes, that there are aliases, actually
     *
     * @param commandName the name of the command that was used in this help request
     * @param aliases the command object
     * @throws IllegalArgumentException if there are no aliases.
     */
    private static String getRefForAliases(Set<String> aliases) {
        if (aliases.size() == 0)
            throw new IllegalArgumentException("No aliases found"); //$NON-NLS-1$

        StringBuilder builder = new StringBuilder();
        builder.append(Messages.HelpCommand_aliasesArePrefix);

        Iterator<String> iterator = aliases.iterator();
        if (aliases.size() >= 1) {
            builder.append(iterator.next() + "'"); //$NON-NLS-1$
        }
        if (aliases.size() > 1) {
            while (iterator.hasNext()) {
                builder.append(", '"); //$NON-NLS-1$
                builder.append(iterator.next() + "'"); //$NON-NLS-1$
            }
        }

        builder.append("."); //$NON-NLS-1$
        return builder.toString();
    }

    /**
     * Returns the reference to more help that appears in the help that appears when a command was called with a bad
     * combination of options/arguments
     */
    private static String getRefStringToFurtherHelpBadCommandCall(String commandName, Command command,
            String commandCallLine, ParseException e) {
        StringBuilder maskBuilder = new StringBuilder();
        List<String> maskArgs = new LinkedList<String>();

        List<Example> examples = getExamples(commandName, command);

        maskBuilder.append(Messages.HelpCommand_refstring1BadCommand);
        maskArgs.add(commandName);
        if (examples.size() != 0) {
            String examplesRef = getRefStringToExamples(commandName);
            maskBuilder.append("\n%s"); //$NON-NLS-1$
            maskArgs.add(examplesRef);
        }

        Object[] maskArgsArray = maskArgs.toArray();
        return String.format(maskBuilder.toString(), maskArgsArray);
    }

    /**
     * Returns the part of the help that points out how to reach examples for the usage of the command. Assumes that
     * there are actually examples.
     *
     * @param commandName the name of the command
     */
    private static String getRefStringToExamples(String commandName) {
        String mask = Messages.HelpCommand_refstringExamples;
        String exampleCommandLine = "help -" + EXAMPLES_SHORT_OPT + " " + commandName; //$NON-NLS-1$ //$NON-NLS-2$

        return String.format(mask, exampleCommandLine);
    }

    /**
     * returns the String that references the possibility to show a more detailed help output.
     *
     * @param commandName the name of the command
     * @param command the command object
     * @param commandCallLine the command line that evoked the help output in which this reference has to appaer.
     */
    private static String getRefStringToDetailedHelp(String commandName, Command command, String commandCallLine) {
        String detailedHelpCmdline = getDetailedHelpCmdlineFromShortHelpCmdline(commandCallLine);
        String mask = Messages.HelpCommand_detailedHelpRefstring;
        return String.format(mask, detailedHelpCmdline);
    }

    /**
     * derives the command line to show the detailed help from a command line that evoked the display of the short help.
     *
     * @param commandCallLine the command line for the short help
     */
    private static String getDetailedHelpCmdlineFromShortHelpCmdline(String commandCallLine) {
        /*
         * possible commandCallLines: help cmd ? cmd cmd help cmd ? cmd -help cmd --help
         */

        // Seeking outer help call pattern first
        String outerHelpPattern = "(help)|\\?.*"; //$NON-NLS-1$
        if (commandCallLine.matches(outerHelpPattern)) {
            try {
                String cmdname = commandCallLine.trim().split(" ")[0]; //$NON-NLS-1$
                String cmdtail = commandCallLine.substring(cmdname.length());

                cmdname = cmdname.replaceFirst("\\Ahelp", "HELP"); //$NON-NLS-1$ //$NON-NLS-2$
                cmdname = cmdname.replaceFirst("\\A\\?", "??"); //$NON-NLS-1$ //$NON-NLS-2$

                return cmdname + cmdtail;
            } catch (IllegalStateException e) {
                LogUtil.logError(e);
                return null;
            }
        } else {
            String commandLine = String.copyValueOf(commandCallLine.toCharArray());
            commandLine = commandLine.replaceFirst("help", "HELP"); //$NON-NLS-1$ //$NON-NLS-2$
            commandLine = commandLine.replaceFirst("\\?", "??"); //$NON-NLS-1$ //$NON-NLS-2$

            if (commandLine.equals(commandCallLine)) {
                LogUtil.logError("Error in generating reference commandline for detailed help."); //$NON-NLS-1$
            }
            return commandLine;
        }

    }

    /**
     * Returns examples for a command, if it has any, and if not, returns an empty List
     */
    static List<Example> getExamples(String commandName, Command command) {
        return command instanceof ExtendedHelpCommand ? ((ExtendedHelpCommand) command).getExamples()
                : new LinkedList<ExtendedHelpCommand.Example>();
    }

    /**
     * Returns aliases for a command, if it has any, and if not, returns an empty List. Aliases are, in this case, all
     * names for the command that are NOT equal to the given command name.
     */
    private static Set<String> getAliases(String commandName, Command command) {
        Set<String> aliases = new HashSet<String>();
        if (command instanceof ExtendedHelpCommand) {
            aliases.add(((ExtendedHelpCommand) command).getOriginalCommandName());
            aliases.addAll(((ExtendedHelpCommand) command).getAliases());
            aliases.remove(commandName);
        } else {
            aliases.add(command.getCommandName());
        }
        return aliases;

    }

    /**
     * Recovers the command line form of which a CommandLine object could've originated from. Assumes, that all options
     * have been stated in short form.
     *
     * @param commandName the name of the command
     * @param cmdLine the CommandLine object to parse
     * @return a command line, which would parse to a CommandLine object equal to the given one.
     */
    public static String reverseCommandline(String commandName, CommandLine cmdLine) {
        StringBuilder builder = new StringBuilder();
        builder.append(commandName);
        for (String arg : cmdLine.getArgs()) {
            builder.append(" " + arg); //$NON-NLS-1$
        }

        for (Option option : cmdLine.getOptions()) {
            builder.append(" -" + option.getOpt()); //$NON-NLS-1$
            for (int i = 0; i < option.getArgs(); i++) {
                builder.append(" " + (option.getValue(i).contains(" ") ? "\"" + option.getValue(i) + "\"" : option.getValue(i))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            }
        }

        return builder.toString();
    }

    static void printCommand(StringBuilder result, Command command) {
        result.append(command.getCommandName());
        result.append(" -> "); //$NON-NLS-1$
        result.append(command.getDescription());
        result.append("\n"); //$NON-NLS-1$
    }

    public String getResult() {
        return result;
    }

    /**
     * opens a resource file stream
     *
     * @param filename the file path
     * @return the inputStream containing the file's content
     */
    private static InputStream openResourceStream(final String filename) {
        try {
            URL installURL = CommandsCorePlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
            URL url = new URL(installURL, filename);
            return (url.openStream());
        } catch (MalformedURLException e) {
            LogUtil.logError(CommandsCorePlugin.PLUGIN_ID, e);
        } catch (IOException e) {
            LogUtil.logError(CommandsCorePlugin.PLUGIN_ID, e);
        }
        return null;
    }

    /**
     * reads the current value from an input stream
     *
     * @param in the input stream
     */
    private static String InputStreamToString(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, IConstants.UTF8_ENCODING));
        } catch (UnsupportedEncodingException ex) {
            reader = new BufferedReader(new InputStreamReader(in));
            LogUtil.logError(CommandsCorePlugin.PLUGIN_ID, ex);
        }

        StringBuffer myStrBuf = new StringBuffer();
        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = reader.read()) != -1) {
                myStrBuf.append(String.valueOf((char) charOut));
            }
        } catch (IOException e) {
            LogUtil.logError(CommandsCorePlugin.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }
}

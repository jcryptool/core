// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.commands.core.evaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.eclipse.osgi.util.NLS;
import org.jcryptool.commands.core.Command;
import org.jcryptool.commands.core.CommandFactory;
import org.jcryptool.commands.core.CommandsCorePlugin;
import org.jcryptool.commands.core.HelpCommand;
import org.jcryptool.commands.core.api.IllegalCommandException;
import org.jcryptool.core.logging.utils.LogUtil;

public class CommandEvaluator {

    public enum ResultType {
        /**
         * An error that remains unspecified occured
         */
        ERROR_OTHER(10),
        /**
         * A syntax error occured (like, not all required options were found)
         */
        ERROR_SYNTAX(5),
        /**
         * Some Parameters didn't meet the specification of the algorithm behind the command
         */
        ALGORITHM_SPECIFICATION_VIOLATION(4),
        /**
         * No errors detected
         */
        OK(0);

        private int severityOfFailure;

        ResultType(int severityOfFailure) {
            this.severityOfFailure = severityOfFailure;
        }

        /**
         * @return true, if, and only if, the computation of the command string finished without impairing the quality
         *         of the result. Intended to return true even when minor warnings occur (like, an algorithm key is "a",
         *         which causes a warning that this won't really encrypt a message, but since this is a valid input,
         *         this method returns true).
         */
        public boolean isResultOfCompleteComputation() {
            if (this == OK) {
                return true;
            }
            return false;
        }

        public int getSeverityOfFailure() {
            return severityOfFailure;
        }

        public static ResultType getLeastSevereResultType() {
            return OK;
        }
    }

    public static class EvaluationResult {
        public EvaluationResult(String result, ResultType returnType) {
            super();
            this.result = result;
            this.returnType = returnType;
        }

        private String result;
        private ResultType returnType;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public ResultType getReturnType() {
            return returnType;
        }

        public void setReturnType(ResultType returnType) {
            this.returnType = returnType;
        }
    }

    private Map<String, Command> commands;

    public CommandEvaluator() {
        commands = new HashMap<String, Command>();
        for (Command command : CommandFactory.loadExtensions()) {
            commands.put(command.getCommandName(), command);
        }
    }

    public EvaluationResult evaluateMultiline(String[] multilineString) {
        StringBuilder builder = new StringBuilder();
        ResultType mostSevereResultType = ResultType.getLeastSevereResultType();
        String errorMask1 = Messages.CommandEvaluator_2;
        try {
            EvaluationResult result = evaluate(multilineString[0]);
            if (result.getReturnType().getSeverityOfFailure() > mostSevereResultType.getSeverityOfFailure()) {
                mostSevereResultType = result.getReturnType();
            }
            if (result.getReturnType().isResultOfCompleteComputation()) {
                builder.append(result.getResult());
            } else {
                builder.append(String.format("\n" + errorMask1, multilineString[0], result.result)); //$NON-NLS-1$
                return new EvaluationResult(builder.toString(), mostSevereResultType);
            }
            for (int i = 1; i < multilineString.length; i++) {
                result = evaluate(multilineString[i]);
                if (result.getReturnType().getSeverityOfFailure() > mostSevereResultType.getSeverityOfFailure()) {
                    mostSevereResultType = result.getReturnType();
                }
                if (result.getReturnType().isResultOfCompleteComputation()) {
                    builder.append("\n" + result.getResult()); //$NON-NLS-1$
                } else {
                    builder.append(String.format("\n" + errorMask1, multilineString[0], "\n" + result.result)); //$NON-NLS-1$ //$NON-NLS-2$
                    return new EvaluationResult(builder.toString(), mostSevereResultType);
                }
            }

        } catch (ParseException ex) {
            // real syntax errors were already caught in the singleline-evaulation method
            LogUtil.logError(CommandsCorePlugin.PLUGIN_ID, ex);
        }

        return new EvaluationResult(builder.toString(), mostSevereResultType);
    }

    public CommandEvaluator.EvaluationResult evaluate(String commandString) throws ParseException {
        if (commandString.contains("\n")) { //$NON-NLS-1$
            String[] splitCommandStrings = commandString.split("((\\r\\n)|(\r)|(\n))"); //$NON-NLS-1$
            return evaluateMultiline(splitCommandStrings);
        }

        int commandEnd = commandString.indexOf(" "); //$NON-NLS-1$
        commandEnd = commandEnd > 0 ? commandEnd : commandString.length();
        String commandName = commandString.substring(0, commandEnd);

        String cleanCommandLine = commandString.trim();
        String commandArgs = commandString.substring(commandEnd).trim(); //$NON-NLS-1$

        StringBuilder result = new StringBuilder(); //$NON-NLS-1$
        Command command = commands.get(commandName);
        if (command != null) {

            CommandLine commandLine = null;

            if (isCallForShortHelp(cleanCommandLine)) {
                result.append(HelpCommand.getShortHelpFor(commandName, command, cleanCommandLine));
            } else if (isCallForDetailedHelp(cleanCommandLine)) {
                result.append(HelpCommand.getDetailedHelpFor(commandName, command, cleanCommandLine));
            } else

                try {
                    String[] args = splitArgs(commandArgs);
                    BasicParser parser = new BasicParser();
                    commandLine = parser.parse(command.createOptions(), args);
                    command.execute(commandLine);
                    result.append(command.getResult());
                } catch (IllegalCommandException e) {
                    result.append(e.getMessage());
                } catch (RuntimeException e) {
                    result.append(Messages.CommandEvaluator_1);
                    result.append(e.getLocalizedMessage());
                } catch (ParseException e) {
                    // TODO only if it is a "help anormality"
                    result.append(HelpCommand.getHelpForBadCommandCall(commandName, command, cleanCommandLine, e));
                    return new EvaluationResult(result.toString(), ResultType.ERROR_SYNTAX);
                }

        } else {
            result.append(NLS.bind(Messages.CommandEvaluator_0, commandName));
        }
        return new EvaluationResult(result.toString(), ResultType.OK);
    }

    private boolean isCallForDetailedHelp(String cleanCommandLine) {
        String matcher1 = "[^ \\t\\n\\r]+ ((HELP)|(\\?\\?)|(-HELP)|(--HELP))"; //$NON-NLS-1$
        if (cleanCommandLine.matches(matcher1)) {
            return true;
        }
        return false;
    }

    private static boolean isCallForShortHelp(String cleanCommandLine) {
        /*
         * cmd help cmd ? cmd -help cmd --help
         */
        String matcher1 = "[^ \\t\\n\\r]+ ((help)|\\?|(-help)|(--help))"; //$NON-NLS-1$
        if (cleanCommandLine.matches(matcher1)) {
            return true;
        }
        return false;
    }

    /**
     * Sprits command Arg String into its parts. Needs to parse text cause of " " text grouping. \" escapes " for text
     * usage.
     *
     * @param commandArgs
     * @return
     * @throws ParseException
     */
    private String[] splitArgs(String commandArgs) throws ParseException {
        ArrayList<String> args = new ArrayList<String>();
        StringBuilder arg = new StringBuilder();
        boolean stringMode = false;
        boolean escapeMode = false;
        for (int i = 0; i < commandArgs.length(); i++) {
            char actualChar = commandArgs.charAt(i);
            if (Character.isSpaceChar(actualChar)) {
                escapeMode = false;
                if (stringMode) {
                    arg.append(actualChar);
                } else {
                    args.add(arg.toString());
                    arg.delete(0, arg.length());
                }
            } else if (actualChar == '"') {
                if (escapeMode) {
                    arg.deleteCharAt(arg.length() - 1);
                    arg.append(actualChar);
                    escapeMode = false;
                } else {
                    stringMode = !stringMode;
                }
            } else if (actualChar == '\\') {
                arg.append(actualChar);
                escapeMode = true;
            } else {
                escapeMode = false;
                arg.append(actualChar);
            }
        }
        if (arg.length() > 0) {
            args.add(arg.toString());
        }

        if (stringMode) {
            throw new ParseException(
                    "Illegal use of \". It needs to be escaped with \\\" or used as a pair that encloses text like \"hello world\""); //$NON-NLS-1$
        }

        return args.toArray(new String[args.size()]);
    }

}

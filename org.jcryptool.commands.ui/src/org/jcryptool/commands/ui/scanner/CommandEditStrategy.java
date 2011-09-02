//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.commands.ui.scanner;

import org.apache.commons.cli.ParseException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.jcryptool.commands.core.evaluator.CommandEvaluator;

final class CommandEditStrategy implements IAutoEditStrategy {
    private CommandEvaluator evaluator;

    public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
        try {
            IRegion line = document.getLineInformationOfOffset(command.offset);
            int lineNumber = document.getLineOfOffset(command.offset);
            int positionInLine = command.offset - line.getOffset();
            boolean allowedEdit = lineNumber == document.getNumberOfLines() - 2
                    && "JCrypTool=> ".length() <= positionInLine; //$NON-NLS-1$
            if (allowedEdit) {
                command.text = replaceUnwantedSigns(command.text);
                command.text = expandNewLineToNewCommand(command.text, document);
            } else {
                command.doit = false;
                command.text = ""; //$NON-NLS-1$
                command.offset = document.getLength() - 1;
                command.length = 0;
                command.shiftsCaret = false;
            }
        } catch (BadLocationException e) {
            throw new RuntimeException("AutoEditing in Console went wrong", e); //$NON-NLS-1$
        } catch (ParseException e) {
            command.text = e.getLocalizedMessage();
        }
    }

    private String expandNewLineToNewCommand(String text, IDocument document)
            throws ParseException, BadLocationException {
        if (text.matches("[\\n\\r]")) { //$NON-NLS-1$
            // TODO Text Pastes mit mehreren commands muessen gehandhabt werden..
            IRegion region = document.getLineInformation(document.getNumberOfLines() - 2);
            String commandString = document.get(region.getOffset(), region.getLength());
            commandString = commandString.replace("JCrypTool=> ", ""); //$NON-NLS-1$ //$NON-NLS-2$

            StringBuilder result = new StringBuilder();
            result.append("\n" + getEvaluator().evaluate(commandString).getResult()+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
            result.append("\nJCrypTool=> "); //$NON-NLS-1$

            return result.toString();
        } else {
            return text;
        }
    }

    private CommandEvaluator getEvaluator() {
        if (evaluator == null) {
            evaluator = new CommandEvaluator();
        }
        return evaluator;
    }

    private String replaceUnwantedSigns(String text) {
        return text.replaceAll("[\\t\\f\\a\\e\\x0B]", " "); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
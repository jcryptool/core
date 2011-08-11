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

import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class ConsoleConfiguration extends SourceViewerConfiguration {

    private ConsoleCommandScanner commandScanner;
    private DefaultScanner defaultScanner;
    private DefaultEditStrategy defaultEditStrategy;
    private CommandEditStrategy commandEditStrategy;

    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
        return new String[] {IDocument.DEFAULT_CONTENT_TYPE,
                ConsolePartitionScanner.CONSOLE_COMMAND};
    }

    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();

        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getConsoleCommandScanner());
        reconciler.setDamager(dr, ConsolePartitionScanner.CONSOLE_COMMAND);
        reconciler.setRepairer(dr, ConsolePartitionScanner.CONSOLE_COMMAND);

        dr = new DefaultDamagerRepairer(getDefaultScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

        return reconciler;
    }

    @Override
    public String[] getIndentPrefixes(ISourceViewer sourceViewer, String contentType) {
        return super.getIndentPrefixesForTab(1);
    }

    @Override
    public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
        if (contentType.equals(IDocument.DEFAULT_CONTENT_TYPE)) {
            return new IAutoEditStrategy[] {getDefaultEditStrategy()};
        } else if (contentType.equals(ConsolePartitionScanner.CONSOLE_COMMAND)) {
            return new IAutoEditStrategy[] {getCommandEditStrategy()};
        }
        return null;
    }

    private ITokenScanner getDefaultScanner() {
        if (defaultScanner == null) {
            defaultScanner = new DefaultScanner();

            TextAttribute textAttribute = new TextAttribute(defaultScanner.getDefaultColor());
            Token token = new Token(textAttribute);
            defaultScanner.setDefaultReturnToken(token);
        }
        return defaultScanner;
    }

    private ITokenScanner getConsoleCommandScanner() {
        if (commandScanner == null) {
            commandScanner = new ConsoleCommandScanner();

            TextAttribute textAttribute = new TextAttribute(commandScanner.getDefaultColor());
            Token token = new Token(textAttribute);
            commandScanner.setDefaultReturnToken(token);
        }
        return commandScanner;
    }

    public DefaultEditStrategy getDefaultEditStrategy() {
        if (defaultEditStrategy == null) {
            defaultEditStrategy = new DefaultEditStrategy();
        }
        return defaultEditStrategy;
    }

    public CommandEditStrategy getCommandEditStrategy() {
        if (commandEditStrategy == null) {
            commandEditStrategy = new CommandEditStrategy();
        }
        return commandEditStrategy;
    }
}

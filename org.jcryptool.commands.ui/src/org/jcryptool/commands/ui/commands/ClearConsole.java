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
package org.jcryptool.commands.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.commands.ui.scanner.ConsolePartitionScanner;
import org.jcryptool.commands.ui.scanner.DebugPartitioner;
import org.jcryptool.commands.ui.views.ConsoleView;

/**
 * Clears the content of the <b>Console View</b>.
 *
 * @author Dominik Schadow
 * @version 0.6.0
 */
public class ClearConsole extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ConsoleView view = (ConsoleView) HandlerUtil.getActivePart(event);
        SourceViewer viewer = view.getViewer();

        Document document = new Document(Messages.ClearConsole_1);
        IDocumentPartitioner partitioner = new DebugPartitioner(new ConsolePartitionScanner(),
                new String[] {ConsolePartitionScanner.CONSOLE_COMMAND});
        viewer.setDocument(document);

        partitioner.connect(document);
        document.setDocumentPartitioner(partitioner);

        return null;
    }
}

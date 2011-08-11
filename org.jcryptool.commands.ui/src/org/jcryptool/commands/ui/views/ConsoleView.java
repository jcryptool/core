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
package org.jcryptool.commands.ui.views;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.commands.ui.scanner.ConsoleConfiguration;
import org.jcryptool.commands.ui.scanner.ConsolePartitionScanner;
import org.jcryptool.commands.ui.scanner.DebugPartitioner;

public class ConsoleView extends ViewPart {
    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "org.jcryptool.commands.ui.views.ConsoleView"; //$NON-NLS-1$

    private SourceViewer viewer;
    private Document document;

    /**
     * The constructor.
     */
    public ConsoleView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        viewer = new SourceViewer(parent, new VerticalRuler(12), SWT.BORDER | SWT.V_SCROLL);
        viewer.getTextWidget().setFont(JFaceResources.getFontRegistry().get("org.eclipse.jface.textfont")); //$NON-NLS-1$
        viewer.configure(new ConsoleConfiguration());

        document = new Document(Messages.ConsoleView_1);
        IDocumentPartitioner partitioner = new DebugPartitioner(new ConsolePartitionScanner(),
                new String[] {ConsolePartitionScanner.CONSOLE_COMMAND});
        viewer.setDocument(document);

        partitioner.connect(document);
        document.setDocumentPartitioner(partitioner);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "org.jcryptool.commands.ui.consoleView"); //$NON-NLS-1$

        hookActionBar();
        hookContextMenu();
    }

    private void hookActionBar() {
        IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
        mgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        getViewSite().getActionBars().updateActionBars();
    }

    private void hookContextMenu() {
        MenuManager manager = new MenuManager();
        manager.setRemoveAllWhenShown(true);

        Menu menu = manager.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu("org.jcryptool.commands.popup", manager, viewer); //$NON-NLS-1$
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    public SourceViewer getViewer(){
        return viewer;
    }
}
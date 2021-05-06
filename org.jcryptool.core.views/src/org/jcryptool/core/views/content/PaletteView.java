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
package org.jcryptool.core.views.content;

import java.util.HashMap;

import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jcryptool.core.views.AlgorithmView;
import org.jcryptool.core.views.ISearchable;
import org.jcryptool.core.views.content.palette.AlgorithmPaletteViewer;
import org.jcryptool.core.views.content.palette.ViewProviderPaletteViewer;

/**
 * Provides a folder layout for the algorithm extension point
 *
 * @author mwalthart
 *
 */
public class PaletteView {

    private HashMap<String, PaletteViewer> viewers;
    private HashMap<String, CTabItem> items;
    private CTabFolder folder;

    public PaletteView(Composite parent) {
        folder = new CTabFolder(parent, SWT.BOTTOM);
        viewers = new HashMap<String, PaletteViewer>();
        items = new HashMap<String, CTabItem>();

        // algorithm folder
        viewers.put(AlgorithmView.MENU_TEXT_ALGORITHM, new AlgorithmPaletteViewer(folder));
        items.put(AlgorithmView.MENU_TEXT_ALGORITHM, new CTabItem(folder, SWT.BORDER | SWT.BOTTOM));
        items.get(AlgorithmView.MENU_TEXT_ALGORITHM).setControl(
                viewers.get(AlgorithmView.MENU_TEXT_ALGORITHM).getControl());
        items.get(AlgorithmView.MENU_TEXT_ALGORITHM).setText(AlgorithmView.MENU_TEXT_ALGORITHM);

        // analysis folder
        viewers.put(AlgorithmView.MENU_TEXT_ANALYSIS, new ViewProviderPaletteViewer(
                "org.jcryptool.core.operations.analysis", folder)); //$NON-NLS-1$
        items.put(AlgorithmView.MENU_TEXT_ANALYSIS, new CTabItem(folder, SWT.BORDER | SWT.BOTTOM));
        items.get(AlgorithmView.MENU_TEXT_ANALYSIS).setControl(
                viewers.get(AlgorithmView.MENU_TEXT_ANALYSIS).getControl());
        items.get(AlgorithmView.MENU_TEXT_ANALYSIS).setText(AlgorithmView.MENU_TEXT_ANALYSIS);

        // visuals folder
        viewers.put(AlgorithmView.MENU_TEXT_VISUALS, new ViewProviderPaletteViewer(
                "org.jcryptool.core.operations.visuals", folder)); //$NON-NLS-1$
        items.put(AlgorithmView.MENU_TEXT_VISUALS, new CTabItem(folder, SWT.BORDER | SWT.BOTTOM));
        items.get(AlgorithmView.MENU_TEXT_VISUALS).setControl(
                viewers.get(AlgorithmView.MENU_TEXT_VISUALS).getControl());
        items.get(AlgorithmView.MENU_TEXT_VISUALS).setText(AlgorithmView.MENU_TEXT_VISUALS);

        // games folder
        viewers.put(AlgorithmView.MENU_TEXT_GAMES, new ViewProviderPaletteViewer(
                "org.jcryptool.core.operations.games", folder)); //$NON-NLS-1$
        items.put(AlgorithmView.MENU_TEXT_GAMES, new CTabItem(folder, SWT.BORDER | SWT.BOTTOM));
        items.get(AlgorithmView.MENU_TEXT_GAMES).setControl(
                viewers.get(AlgorithmView.MENU_TEXT_GAMES).getControl());
        items.get(AlgorithmView.MENU_TEXT_GAMES).setText(AlgorithmView.MENU_TEXT_GAMES);

        // add the listener for the tab change event
        folder.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                AlgorithmView.showTab(((CTabItem) e.item).getText());
            }
        });
    }

    /**
     * Returns the control
     *
     * @return the control
     */
    public Control getControl() {
        return folder;
    }

    /**
     * Filters the displayed items due to a string (case insensitive)
     *
     * @param needle the string to filter
     */
    public void setFilter(String needle) {
        for (PaletteViewer viewer : viewers.values())
            ((ISearchable) viewer).search(needle);
    }

    /**
     * shows the tab with the given name at the viewer
     *
     * @param name the name of the tab to show
     */
    public void showTab(String name) {
        if (folder.getSelection() == null || !folder.getSelection().getText().equals(name)) {
            folder.setSelection(items.get(name));
            folder.layout();
        }
    }
}

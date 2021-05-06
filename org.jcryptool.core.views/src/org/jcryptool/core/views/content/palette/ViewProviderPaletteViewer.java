//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.views.content.palette;

import java.util.Collections;
import java.util.TreeMap;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.editparts.PaletteEditPart;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.core.views.AlgorithmView;
import org.jcryptool.core.views.ISearchable;
import org.jcryptool.core.views.ViewsPlugin;
import org.jcryptool.core.views.content.TreeView;

/**
 * A palette viewer for the ViewProviders (analysis, visuals, games).
 *
 * @author mwalthart
 * @author Holger Friedrich (support for Commands)
 * @version 0.9.2
 */
public class ViewProviderPaletteViewer extends PaletteViewer implements ISearchable {
    private String extensionPointId;
    private PaletteRoot invisibleRoot = new PaletteRoot();
    private PaletteViewer viewer = this;
    private AbstractHandler doubleClickHandler;
    private String search;

    /**
     * creates a palette viewer
     *
     * @param extensionPointId the extension point id of the target
     * @param parent the parent composite
     */
    public ViewProviderPaletteViewer(String extensionPointId, Composite parent) {
        super();
        this.extensionPointId = extensionPointId;

        createTree(new String[] {""}); //$NON-NLS-1$
        createControl(parent);
        setPaletteRoot(invisibleRoot);
        makeAndAssignActions();
    }

    /**
     * Creates the actions and assigns them to the viewers double click listener.
     */
    private void makeAndAssignActions() {
        doubleClickHandler = new AbstractHandler() {
            public Object execute(ExecutionEvent event) {
                final ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);

                Object obj = ((IStructuredSelection) viewer.getSelection()).getFirstElement();

                if (obj instanceof PaletteEditPart) {
                    PaletteEditPart part = (PaletteEditPart) obj;
                    IConfigurationElement[] elements = Platform.getExtensionRegistry()
                            .getExtensionPoint(extensionPointId).getConfigurationElements();
                    for (IConfigurationElement element : elements) {
                        if (element.getAttribute("name").equals(( //$NON-NLS-1$
                                (PaletteEntry) part.getModel()).getLabel())) {
                            Command command = commandService.getCommand(element.getAttribute("viewId")); //$NON-NLS-1$
                            try {
                            	return command.executeWithChecks(event);
                            } catch(Exception ex) {
                            	LogUtil.logError(ViewsPlugin.PLUGIN_ID, ex);
                            	return(null);
                            }
                        }
                    }
                }
                return(null);
            }
        };

        viewer.getControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(final MouseEvent e) {
                if (e.button == 1) { // only left button double clicks
                    final IHandlerService handlerService = (IHandlerService)PlatformUI.getWorkbench().getService(IHandlerService.class);
                    IEvaluationContext evaluationContext = handlerService.createContextSnapshot(true);
                    ExecutionEvent event = new ExecutionEvent(null, Collections.EMPTY_MAP, null, evaluationContext);

                    try {
                		doubleClickHandler.execute(event); // run assigned action
                	} catch (ExecutionException ex) {
                		LogUtil.logError(ViewsPlugin.PLUGIN_ID, ex);
                	}
                }
            }

            @Override
            public void mouseDown(final MouseEvent e) {
                IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                Object obj = selection.getFirstElement();

                if (obj instanceof PaletteEditPart) {
                    AlgorithmView.showContextHelp(extensionPointId, ((PaletteEntry) ((PaletteEditPart) obj).getModel()).getLabel());
                    viewer.getControl().setFocus();
                    viewer.setSelection(selection);
                }
            }
        });
    }

    /**
     * Creates a tree from the extension point structure.
     *
     * @param needles the search string to filter the elements
     */
    private void createTree(String[] needles) {
        invisibleRoot = new PaletteRoot();
        TreeMap<String, PaletteEntry> sortList = new TreeMap<String, PaletteEntry>();

        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
                extensionPointId);

        String label = ""; //$NON-NLS-1$
        if (extensionPoint.getLabel().equals("analysis")) { //$NON-NLS-1$
            label = AlgorithmView.MENU_TEXT_ANALYSIS;
        } else if (extensionPoint.getLabel().equals("games")) { //$NON-NLS-1$
            label = AlgorithmView.MENU_TEXT_GAMES;
        } else if (extensionPoint.getLabel().equals("visuals")) { //$NON-NLS-1$
            label = AlgorithmView.MENU_TEXT_VISUALS;
        }

        PaletteDrawer category = new PaletteDrawer(label);
        category.setSmallIcon(ImageService.getImageDescriptor(ViewsPlugin.PLUGIN_ID, TreeView.ICON_FOLDER));
        category.setLargeIcon(ImageService.getImageDescriptor(ViewsPlugin.PLUGIN_ID, TreeView.ICON_FOLDER));

        invisibleRoot.add(category);

        IConfigurationElement[] elements = extensionPoint.getConfigurationElements();
        for (IConfigurationElement element : elements) {
            String name = element.getAttribute("name"); //$NON-NLS-1$

            // filter
            boolean show = true;
            for (String needle : needles) {
                if (!needle.equals("") && !name.toLowerCase().matches(".*" + needle.toLowerCase() + ".*")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    show = false;
            }
            if (show) {
                SelectionToolEntry paletteEntry = new SelectionToolEntry(element
                        .getAttribute("name"), ""); //$NON-NLS-1$ //$NON-NLS-2$
                // Get the icon of the view from the extension point.
                if (element.getAttribute("icon") != null) {
                	String icon = element.getAttribute("icon");
                	paletteEntry.setSmallIcon(ImageService.createIconFromURL(icon));
                	paletteEntry.setLargeIcon(ImageService.createIconFromURL(icon));
                } else {
                    paletteEntry.setSmallIcon(ImageService.IMAGEDESCRIPTOR_PERSPECTIVE_STANDARD);
                    paletteEntry.setLargeIcon(ImageService.IMAGEDESCRIPTOR_PERSPECTIVE_STANDARD);

                }
                paletteEntry.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
                paletteEntry.setType(""); //$NON-NLS-1$

                sortList.put(paletteEntry.getLabel(), paletteEntry);
            }
        }

        // attach items to the category
        for (PaletteEntry entry : sortList.values()) {
            category.add(entry);
        }
    }

    /**
     * sets the search string for the viewer
     *
     * @see ISearchable
     */
    public void search(String needle) {
        search = needle;
        invisibleRoot = new PaletteRoot();
        createTree(needle.split(" ")); //$NON-NLS-1$
        setPaletteRoot(invisibleRoot);
    }

    /**
     * returns the current search string of the viewer
     *
     * @see ISearchable
     */
    public String getCurrentSearch() {
        if (search == null)
            return ""; //$NON-NLS-1$
        return search;
    }
}

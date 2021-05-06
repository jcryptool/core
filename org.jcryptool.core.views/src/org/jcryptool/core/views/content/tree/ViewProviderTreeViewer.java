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
package org.jcryptool.core.views.content.tree;

import java.util.Collections;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.views.AlgorithmView;
import org.jcryptool.core.views.ISearchable;
import org.jcryptool.core.views.ViewsPlugin;
import org.jcryptool.core.views.content.structure.NameSorter;
import org.jcryptool.core.views.content.structure.TreeObject;
import org.jcryptool.core.views.content.structure.TreeParent;
import org.jcryptool.core.views.content.structure.ViewContentProvider;
import org.jcryptool.core.views.content.structure.ViewLabelProvider;

/**
 * A tree viewer for the ViewProviders (Analysis, Visuals, Games)
 *
 * @author mwalthart
 * @author Holger Friedrich (support for Commands)
 * @version 0.9.2
 */
public class ViewProviderTreeViewer extends TreeViewer implements ISearchable {
    private String extensionPointId;
    private TreeParent invisibleRoot = new TreeParent(""); //$NON-NLS-1$
    protected TreeViewer viewer = this;
    protected AbstractHandler doubleClickHandler;
    private String search;

    /**
     * creates a tree viewer
     *
     * @param extensionPointId the extension point id of the target
     * @param parent the parent composite
     */
    public ViewProviderTreeViewer(String extensionPointId, Composite parent) {
        super(parent);
        this.extensionPointId = extensionPointId;
        init();
    }

    /**
     * creates a tree viewer
     *
     * @param extensionPointId the extension point id of the target
     * @param tree the source tree
     */
    public ViewProviderTreeViewer(String extensionPointId, Tree tree) {
        super(tree);
        this.extensionPointId = extensionPointId;
        init();
    }

    /**
     * creates a tree viewer
     *
     * @param extensionPointId the extension point id of the target
     * @param parent the parent composite
     * @param style the style of the viewer
     */
    public ViewProviderTreeViewer(String extensionPointId, Composite parent, int style) {
        super(parent, style);
        this.extensionPointId = extensionPointId;
        init();
    }

    /**
     * initializes the viewer with the extension point data
     */
    private void init() {
        createTree(new String[] {""}); //$NON-NLS-1$

        setContentProvider(new ViewContentProvider());
        setLabelProvider(new ViewLabelProvider());
        setComparator(new NameSorter());
        setInput(invisibleRoot);

        expandToLevel(invisibleRoot, 2); // expand all

        makeAndAssignActions();
    }

    /**
     * creates the actions and assigns them to the viewers double click listener
     */
    private void makeAndAssignActions() {
        doubleClickHandler = new AbstractHandler() {
            public Object execute(ExecutionEvent event) {
                final ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);

                TreeObject treeObject = (TreeObject) ((IStructuredSelection) viewer.getSelection()).getFirstElement();

                IConfigurationElement[] elements = Platform.getExtensionRegistry()
                        .getExtensionPoint(extensionPointId).getConfigurationElements();
                for (IConfigurationElement element : elements) {
                    if (element.getAttribute("name").equals(treeObject.getName())) { //$NON-NLS-1$
                    	Command command = commandService.getCommand(element.getAttribute("viewId")); //$NON-NLS-1$
                    	try {
                    		return command.executeWithChecks(event);
                    	} catch(Exception ex) {
                    		LogUtil.logError(ViewsPlugin.PLUGIN_ID, ex);
                    	}
                    }
                }
                return(null);
            }
        };

        addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(final DoubleClickEvent event) {
                Object obj = ((IStructuredSelection) viewer.getSelection()).getFirstElement();

                if (obj instanceof TreeParent) {
                    if (viewer.getTree().getSelection()[0].getExpanded()) {
                        viewer.collapseToLevel(obj, 1);
                    } else {
                        viewer.expandToLevel(obj, 1);
                    }
                } else if (obj instanceof TreeObject) {
                	try {
                        final IHandlerService handlerService = (IHandlerService)PlatformUI.getWorkbench().getService(IHandlerService.class);
                        IEvaluationContext evaluationContext = handlerService.createContextSnapshot(true);
                        ExecutionEvent executionEvent = new ExecutionEvent(null, Collections.EMPTY_MAP, null, evaluationContext);

                        doubleClickHandler.execute(executionEvent); // run assigned action
                	} catch(ExecutionException ex) {
                		LogUtil.logError(ViewsPlugin.PLUGIN_ID, ex);
                	}
                }
            }
        });

        addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(final SelectionChangedEvent event) {
                Object treeObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();

                if (treeObject instanceof TreeParent) {
                    PlatformUI.getWorkbench().getHelpSystem().displayHelp(ViewsPlugin.PLUGIN_ID + ".algorithmsView"); //$NON-NLS-1$
                    getControl().setFocus();
                } else if (treeObject instanceof TreeObject) {
                    AlgorithmView.showContextHelp(extensionPointId, ((TreeObject) treeObject).getName());
                    getControl().setFocus();
                }
            }
        });
    }

    /**
     * creates a tree from the extension point structure
     *
     * @param needles the search string to filter the elements
     */
    private void createTree(String[] needles) {
        invisibleRoot = new TreeParent(""); //$NON-NLS-1$
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

        TreeParent category = new TreeParent(label);
        invisibleRoot.addChild(category);

        for (IConfigurationElement element : extensionPoint.getConfigurationElements()) {
            String name = element.getAttribute("name"); //$NON-NLS-1$

            // filter
            boolean show = true;
            for (String needle : needles) {
                if (!needle.equals("") && !name.toLowerCase().matches(".*" + needle.toLowerCase() + ".*")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    show = false;
            }

            if (show) // add element
                category.addChild(new TreeObject(element.getAttribute("name"))); //$NON-NLS-1$
        }
    }

    /**
     * sets the search string of the viewer
     *
     * @see ISearchable
     */
    public void search(String needle) {
        search = needle;
        createTree(needle.split(" ")); //$NON-NLS-1$
        setInput(invisibleRoot);
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

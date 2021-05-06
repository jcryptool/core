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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.jcryptool.core.ApplicationActionBarAdvisor;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.CommandInfo;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.ShadowAlgorithmHandler;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.core.views.AlgorithmView;
import org.jcryptool.core.views.ISearchable;
import org.jcryptool.core.views.ViewsPlugin;
import org.jcryptool.core.views.content.structure.NameSorter;
import org.jcryptool.core.views.content.structure.TreeObject;
import org.jcryptool.core.views.content.structure.TreeParent;
import org.jcryptool.core.views.content.structure.ViewContentProvider;
import org.jcryptool.core.views.content.structure.ViewLabelProvider;

/**
 * A tree viewer for the algorithm extension point
 *
 * @author mwalthart
 * @author Holger Friedrich (support for Commands)
 * @version 0.9.3
 */
public class AlgorithmTreeViewer extends TreeViewer implements ISearchable {
    private TreeViewer viewer = this;
    private TreeParent invisibleRoot;
    private AbstractHandler doubleClickHandler;
    private ArrayList<CommandInfo> algorithmList = new ArrayList<CommandInfo>();
    private String search;
    protected String extensionPointId = "org.jcryptool.core.operations.algorithms"; //$NON-NLS-1$

    /**
     * creates a tree viewer
     *
     * @param parent the parent composite
     */
    public AlgorithmTreeViewer(Composite parent) {
        super(parent);
        init();
    }

    /**
     * creates a tree viewer
     *
     * @param tree the source tree
     */
    public AlgorithmTreeViewer(Tree tree) {
        super(tree);
        init();
    }

    /**
     * creates a tree viewer
     *
     * @param parent the parent composite
     * @param style the style of the viewer
     */
    public AlgorithmTreeViewer(Composite parent, int style) {
        super(parent, style);
        init();
    }

    /**
     * initializes the tree viewer with the algorithm extension point
     */
    private void init() {
        loadAlgorithms();
        createTree(new String[] {""}); //$NON-NLS-1$

        setContentProvider(new ViewContentProvider());
        setLabelProvider(new ViewLabelProvider());
        setComparator(new NameSorter());
        setInput(invisibleRoot);
        expandToLevel(invisibleRoot, 2); // expand all

        setupDragAndDrop();
        makeAndAssignActions();
    }

    /**
     * enables the drag'n'drop functionality
     */
    private void setupDragAndDrop() {
        viewer.addDragSupport(DND.DROP_MOVE, new Transfer[] {TextTransfer.getInstance()},
            new DragSourceAdapter() {
                public void dragStart(DragSourceEvent event) {
                    Object obj = ((IStructuredSelection) viewer.getSelection()).getFirstElement();

                    if (obj instanceof TreeParent) {// only allow drag&drop for algorithm and not for categories
                        event.doit = false;
                    } else if (obj instanceof TreeObject) {
                        // random number generators have no drag&drop
                        if (((TreeObject) obj).getParent().getName().equals(org.jcryptool.core.Messages.applicationActionBarAdvisor_Menu_Algorithms_PRNG))
                            event.doit = false;
                    }
                }

                public void dragSetData(DragSourceEvent event) {
                    event.data = viewer.getTree().getSelection()[0].getText();
                }
            });

        viewer.addDropSupport(DND.DROP_MOVE, new Transfer[] {FileTransfer.getInstance()},
            new DropTargetAdapter() {
                public void dragOver(DropTargetEvent event) {
                    if (event.item.getData() instanceof TreeParent) {
                        event.feedback = DND.DROP_NONE;
                        return;
                    } else if (event.item.getData() instanceof TreeObject) {
                        if (((TreeItem) event.item).getParentItem().getText().equalsIgnoreCase(
                                org.jcryptool.core.Messages.applicationActionBarAdvisor_Menu_Algorithms_PRNG)) {
                            event.feedback = DND.DROP_NONE;
                        }

                        return;
                    }
                }

                public void drop(DropTargetEvent event) {
                    if (event.item instanceof TreeItem) {
                        String url = ((String[]) event.data)[0];

                        if (openFile(url))
                            AlgorithmView.doAction(((TreeItem) event.item).getText());
                    }
                }
            });
    }

    /**
     * loads the algorithms from the extension point
     */
    private void loadAlgorithms() {
        for (CommandInfo info : OperationsPlugin.getDefault().getAlgorithmsManager().getShadowAlgorithmCommands()) {
            if (!algorithmList.contains(info)) {
                algorithmList.add(info);
            }
        }
    }

    /**
     * creates a tree for the algorithm structure
     *
     * @param needles the search string to filter the algorithms
     */
    private void createTree(String[] needles) {
        HashMap<String, TreeParent> types = new HashMap<String, TreeParent>();

        Iterator<CommandInfo> it = algorithmList.iterator();
        CommandInfo info = null;

        while (it.hasNext()) {
            info = it.next();

			String text = "";
			String type = "";
			boolean isFlexiProviderAlgorithm = false;
			
			ShadowAlgorithmHandler handler = (ShadowAlgorithmHandler)info.getHandler();
			text = handler.getText();
			type = handler.getType();
			isFlexiProviderAlgorithm = handler.isFlexiProviderAlgorithm();
            
            // filter
            boolean show = true;
            for (String needle : needles) {
                if (!text.toLowerCase().matches(".*" + needle.toLowerCase() + ".*")) //$NON-NLS-1$ //$NON-NLS-2$
                    show = false;
            }

            if (show) {
                // Create Category
                if (types.get(type) == null) {
                    // translate
                    String translatedType = ApplicationActionBarAdvisor.getTypeTranslation(type);
                    types.put(type, new TreeParent(translatedType));
                }

                // Add element
                TreeObject object = new TreeObject(text);
                if (isFlexiProviderAlgorithm)
                    object.setIsFlexiProviderAlgorithm();
                types.get(type).addChild(object);
            }
        }
        ArrayList<TreeParent> parents = new ArrayList<TreeParent>(types.values());

        // attach categories to root element
        invisibleRoot = new TreeParent(""); //$NON-NLS-1$
        Iterator<TreeParent> parentIterator2 = parents.iterator();
        while (parentIterator2.hasNext()) {
            invisibleRoot.addChild(parentIterator2.next());
        }
    }

    /**
     * creates the actions according to the algorithm extension point and assigns them to the
     * viewers double click listener
     */
    private void makeAndAssignActions() {
        doubleClickHandler = new AbstractHandler() {
            public Object execute(ExecutionEvent event) {
                TreeObject treeObject = (TreeObject) ((IStructuredSelection) viewer.getSelection()).getFirstElement();

                IEditorReference[] editorReferences = PlatformUI.getWorkbench()
                        .getActiveWorkbenchWindow().getActivePage().getEditorReferences();
                if (editorReferences.length == 0
                        && (!treeObject.getParent().getName().equals(org.jcryptool.core.Messages.applicationActionBarAdvisor_Menu_Algorithms_PRNG))) {
                    AlgorithmView.showMessage(Messages.AlgorithmView_warning_message_no_active_editor);
                } else {
                    final ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);

                    Iterator<CommandInfo> it9 = algorithmList.iterator();
                    CommandInfo commandInfo = null;
                    while (it9.hasNext()) {
                        commandInfo = it9.next();
                        ShadowAlgorithmHandler handler = (ShadowAlgorithmHandler)commandInfo.getHandler();
                        String commandId = commandInfo.getCommandId();
                        if(commandId != null && treeObject.getName().equals(handler.getText())) {
                        	Command command = commandService.getCommand(commandId);
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
     * returns the current search string of the viewer
     *
     * @see ISearchable
     */
    public String getCurrentSearch() {
        if (search == null)
            return ""; //$NON-NLS-1$
        return search;
    }

    /**
     * sets the viewers search string
     *
     * @see ISearchable
     */
    public void search(String needle) {
        search = needle;

        createTree(needle.split(" ")); //$NON-NLS-1$
        setInput(invisibleRoot);
        expandAll();
    }

    private void showErrorDialog(String title, String message) {
        MessageDialog.openError(Display.getCurrent().getActiveShell(), title, message);
    }

    public boolean openFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            showErrorDialog(Messages.AlgorithmTreeViewer_error, Messages.AlgorithmTreeViewer_1);
            return false;
        }
        if (!file.canRead()) {
            showErrorDialog(Messages.AlgorithmTreeViewer_error, Messages.AlgorithmTreeViewer_2);
            return false;
        }
        if (file.isDirectory()) {
            showErrorDialog(Messages.AlgorithmTreeViewer_error, Messages.AlgorithmTreeViewer_3);
            return false;
        }

        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
            page.openEditor(new PathEditorInput(new Path(path)), IOperationsConstants.ID_HEX_EDITOR);
        } catch (PartInitException ex) {
            LogUtil.logError(ex);

            return false;
        }
        return true;
    }
}

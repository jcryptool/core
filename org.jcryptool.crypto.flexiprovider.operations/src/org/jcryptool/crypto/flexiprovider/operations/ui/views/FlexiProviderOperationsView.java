// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.ui.views;

import java.awt.MouseInfo;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorInputTransfer;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.crypto.flexiprovider.operations.FlexiProviderOperationsPlugin;
import org.jcryptool.crypto.flexiprovider.operations.OperationsManager;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.RemoveAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.RemoveKeyAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.RenameAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.SelectInputFileAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.SelectOutputFileAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.SelectSignatureAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.SetInputEditorAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.SetOutputEditorAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.ops.DecryptAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.ops.EncryptAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu.ExecuteOperationAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu.ExportOperationAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu.ImportOperationAction;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.IOperationChangedListener;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.ISelectedOperationListener;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.EntryNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io.IONode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io.InputNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io.OutputNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io.SignatureNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.keys.KeyNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.ops.OperationsNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.providers.FlexiProviderOperationsViewContentProvider;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.providers.FlexiProviderOperationsViewLabelProvider;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;
import org.jcryptool.crypto.flexiprovider.ui.nodes.ITreeNode;
import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;

public class FlexiProviderOperationsView extends ViewPart implements Listener, ISelectedOperationListener,
        IOperationChangedListener {

    private TreeViewer viewer;

    private Action doubleClickAction;

    // menu
    private Action importAction;
    private Action exportAction;
    private Action executeAction;

    // context
    private Action renameAction;
    private Action removeAction;
    private Action removeKeyAction;
    private Action selectInputFileAction;
    private Action setInputEditorAction;
    private Action selectOutputFileAction;
    private Action setOutputEditorAction;
    private Action selectSignatureOutputAction;
    private Action encryptAction;
    private Action decryptAction;

    private final int dropOps = DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_LINK | DND.DROP_MOVE;
    private final Transfer[] keyTransfers = new Transfer[] {TextTransfer.getInstance()};
    private final Transfer[] editorTransfers = new Transfer[] {EditorInputTransfer.getInstance()};

    private Label currentEntryLabel;

    /**
     * The constructor.
     */
    public FlexiProviderOperationsView() {
        OperationsManager.getInstance().addOperationChangedListener(this);
    }

    private void registerActions() {
        // menu
        importAction = new ImportOperationAction();
        exportAction = new ExportOperationAction(this);
        executeAction = new ExecuteOperationAction(this);
        // context
        renameAction = new RenameAction(this);
        removeAction = new RemoveAction(this);
        removeKeyAction = new RemoveKeyAction(this);
        selectInputFileAction = new SelectInputFileAction(this);
        setInputEditorAction = new SetInputEditorAction(this);
        selectOutputFileAction = new SelectOutputFileAction(this);
        setOutputEditorAction = new SetOutputEditorAction(this);
        selectSignatureOutputAction = new SelectSignatureAction(this);
        encryptAction = new EncryptAction(this);
        decryptAction = new DecryptAction(this);
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    @Override
    public void createPartControl(Composite parent) {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        // gridData.grabExcessVerticalSpace = true;
        currentEntryLabel = new Label(parent, SWT.NULL);
        currentEntryLabel.setLayoutData(gridData);
        currentEntryLabel.setText(Messages.FlexiProviderOperationsView_0);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        getSite().setSelectionProvider(viewer);
        viewer.setContentProvider(new FlexiProviderOperationsViewContentProvider(this, viewer));
        viewer.setLabelProvider(new FlexiProviderOperationsViewLabelProvider());
        viewer.setInput(getViewSite());
        viewer.getTree().setLayoutData(gridData);
        viewer.getControl().addListener(SWT.Selection, this);
        viewer.addDragSupport(dropOps, editorTransfers, new EditorDragListener(viewer));
        viewer.addDropSupport(dropOps, keyTransfers, new KeyDropListener());
        parent.setLayout(new GridLayout());
        registerActions();
        hookContextMenu();
        hookActions();
        // hookDoubleClickAction();
        contributeToActionBars();

        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(parent, FlexiProviderOperationsPlugin.PLUGIN_ID + ".OperationsView"); //$NON-NLS-1$
    }

    /**
     * Adds a listener, which will fold or unfold the nodes.
     */
    private void hookActions() {
        doubleClickAction = new Action() {
            private ToolTip keyTipViewer;

			@Override
            public void run() {
                ISelection selection = viewer.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();

                if (obj instanceof TreeNode) {
                    if (viewer.getTree().getSelection()[0].getExpanded()) {
                        viewer.collapseToLevel(obj, 1);
                    } else {
                        viewer.expandToLevel(obj, 1);
                    }
                } else if (obj instanceof OperationsNode) {
                    // OperationsManager.getInstance().algorithmCalled(((AlgorithmNode) obj).getAlgorithm());
                }
                
                if(obj instanceof KeyNode) {
                	if(keyTipViewer != null && ! keyTipViewer.isDisposed()) keyTipViewer.dispose();
                	keyTipViewer = new ToolTip(viewer.getControl().getShell(), SWT.BALLOON);
                	keyTipViewer.setMessage(Messages.FlexiProviderOperationsView_keystore_hint);
                	keyTipViewer.setAutoHide(true);
                	keyTipViewer.setLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
                	keyTipViewer.setVisible(true);
                }
                
                if(obj instanceof IONode ||
                		obj instanceof InputNode ||
                		obj instanceof SignatureNode) {
//                	Event e = new Event();
//                	e.widget = viewer.getTree();
//                	e.x = viewer.getTree().toControl(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y).x;
//                	e.y = viewer.getTree().toControl(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y).y;
//                	e.button = 3;
//                	e.stateMask = 0;
//                	e.count = 1;
//                	MouseEvent mE = new MouseEvent(e);
//                	viewer.getTree().notifyListeners(SWT.MouseDown, e);
                	viewer.getControl().getMenu().setVisible(true);
                	viewer.getControl().getMenu().setEnabled(true);
                }
                
                if(obj instanceof OperationsNode) {
                	if (obj instanceof TreeNode) {
                        currentEntryNode = getCurrentEntryNode((TreeNode) obj);
                        if(displayOperationContextMenu(currentEntryNode)) {
                        	viewer.getControl().getMenu().setVisible(true);
                        	viewer.getControl().getMenu().setEnabled(true);
                        } else {
                        	if(keyTipViewer != null && ! keyTipViewer.isDisposed()) keyTipViewer.dispose();
                        	keyTipViewer = new ToolTip(viewer.getControl().getShell(), SWT.BALLOON);
                        	keyTipViewer.setMessage(Messages.FlexiProviderOperationsView_keystore_hint_operations);
                        	keyTipViewer.setAutoHide(true);
                        	keyTipViewer.setLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
                        	keyTipViewer.setVisible(true);
                        }
                    }
                }
            }
        };

        viewer.getControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(final MouseEvent e) {
                if (e.button == 1) { // only left button double clicks
                    doubleClickAction.run(); // run assigned action
                }
            }
        });
    }
    
    // private IExtendedOperationDescriptor currentOperation;

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                if (viewer.getTree().getSelection().length > 0) {
                    Object selection = viewer.getTree().getSelection()[0].getData();

                    if (selection instanceof TreeNode) {
                        currentEntryNode = getCurrentEntryNode((TreeNode) selection);
                    }

                    if (selection instanceof EntryNode) {
                        fillEntryNodeContextMenu(manager);
                    } else if (selection instanceof OperationsNode) {
                        if (displayOperationContextMenu(currentEntryNode)) {
                            fillEncryptDecryptNodeContextMenu(manager);
                        }
                    } else if (selection instanceof InputNode) {
                        fillInputContextMenu(manager);
                    } else if (selection instanceof OutputNode) {
                        fillOutputContextMenu(manager);
                    } else if (selection instanceof SignatureNode) {
                        fillSignatureOutputContextMenu(manager);
                    } else if (selection instanceof KeyNode) {
                        fillKeyNodeContextMenu(manager);
                    }
                }
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu("org.jcryptool.crypto.flexiprovider.operations.popup", menuMgr, viewer);
    }

    protected boolean displayOperationContextMenu(EntryNode currentEntryNode) {
		return currentEntryNode.getRegistryType().equals(RegistryType.BLOCK_CIPHER)
        || currentEntryNode.getRegistryType().equals(RegistryType.CIPHER);
	}

	private void fillEntryNodeContextMenu(IMenuManager manager) {
        manager.add(renameAction);
        manager.add(removeAction);
    }

    private void fillEncryptDecryptNodeContextMenu(IMenuManager manager) {
        manager.add(encryptAction);
        manager.add(decryptAction);
    }

    private void fillKeyNodeContextMenu(IMenuManager manager) {
        manager.add(removeKeyAction);
    }

    private void fillInputContextMenu(IMenuManager manager) {
        manager.add(setInputEditorAction);
        manager.add(selectInputFileAction);
    }

    private void fillOutputContextMenu(IMenuManager manager) {
        manager.add(setOutputEditorAction);
        manager.add(selectOutputFileAction);
    }

    private void fillSignatureOutputContextMenu(IMenuManager manager) {
        manager.add(selectSignatureOutputAction);
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(executeAction);
        manager.add(new Separator());
        manager.add(importAction);
        manager.add(exportAction);
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    @Override
    public void dispose() {
        OperationsManager.getInstance().saveXML();
        OperationsManager.getInstance().removeOperationChangedListener(this);
        super.dispose();
    }

    public void handleEvent(Event event) {
        if (event.widget instanceof Tree && ((Tree) event.widget).getSelection().length > 0) {
            Object obj = ((Tree) event.widget).getSelection()[0].getData();
            if (obj instanceof TreeNode) {
                EntryNode tmp = getCurrentEntryNode((TreeNode) obj);
                if (tmp != null) {
                    currentEntryNode = tmp;
                    currentEntryLabel.setText(NLS.bind(Messages.FlexiProviderOperationsView_2,
                            currentEntryNode.getEntryName()));
                }
            }
        }
    }

    private EntryNode currentEntryNode;

    public EntryNode getFlexiProviderOperation() {
        return currentEntryNode;
    }

    private EntryNode getCurrentEntryNode(ITreeNode node) {
        if (node instanceof EntryNode) {
            return (EntryNode) node;
        } else {
            return getCurrentEntryNode(node.getParent());
        }
    }

    public void addOperation() {
    }

    public void removeOperation() {
        currentEntryNode = null;
        currentEntryLabel.setText(Messages.FlexiProviderOperationsView_3);
    }

    public void update(TreeNode updated) {
    }
}

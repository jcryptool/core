// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.ui.views;

import java.awt.MouseInfo;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.part.EditorInputTransfer;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.IServiceLocator;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.operations.FlexiProviderOperationsPlugin;
import org.jcryptool.crypto.flexiprovider.operations.OperationsManager;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.RemoveHandler;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.RemoveKeyHandler;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.RenameHandler;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.SelectInputFileHandler;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.SelectOutputFileHandler;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.SelectSignatureHandler;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.SetInputEditorHandler;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.SetOutputEditorHandler;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.ops.DecryptHandler;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.ops.EncryptHandler;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu.ExecuteOperationHandler;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu.ExportOperationHandler;
import org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu.ImportOperationHandler;
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

public class FlexiProviderOperationsView extends ViewPart implements Listener, ISelectedOperationListener {

    private TreeViewer viewer;

    private AbstractHandler doubleClickHandler;

    // menu
    private final static String importCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.import";
    private final static String exportCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.export";
    private final static String executeCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.execute";

    private AbstractHandler importHandler;
    private AbstractHandler exportHandler;
    private AbstractHandler executeHandler;

    // context
    private final static String renameCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.rename"; 
    private final static String removeCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.remove"; 
    private final static String removeKeyCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.removeKey"; 
    
    private AbstractHandler renameHandler;
    private AbstractHandler removeHandler;
    private AbstractHandler removeKeyHandler;

    private final static String selectInputFileCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.selectInputFile";
    private final static String setInputEditorCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.setInputEditor";
    private final static String selectOutputFileCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.selectOutputFile";
    private final static String setOutputEditorCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.setOutputEditor";
    private final static String selectSignatureOutputCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.selectSignatureOutput";
    
    private AbstractHandler selectInputFileHandler;
    private AbstractHandler setInputEditorHandler;
    private AbstractHandler selectOutputFileHandler;
    private AbstractHandler setOutputEditorHandler;
    private AbstractHandler selectSignatureOutputHandler;

    private final static String encryptCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.encrypt"; 
    private final static String decryptCommandId = "org.jcryptool.crypto.flexiprovider.operations.commands.decrypt"; 

    private AbstractHandler encryptHandler;
    private AbstractHandler decryptHandler;

    private final int dropOps = DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_LINK | DND.DROP_MOVE;
    private final Transfer[] keyTransfers = new Transfer[] { TextTransfer.getInstance() };
    private final Transfer[] editorTransfers = new Transfer[] { EditorInputTransfer.getInstance() };

    private Label currentEntryLabel;

    private ICommandService commandService;
    private Category autogeneratedCategory;
    private IServiceLocator serviceLocator;

	private FlexiProviderOperationsViewContentProvider contentprovider;
    
    /**
     * The constructor.
     */
    public FlexiProviderOperationsView() {
    	FlexiProviderOperationsPlugin.lastOperationsView = this;
//         OperationsManager.getInstance().addOperationChangedListener(this);
    }

    private void defineCommand(final String commandId, final String name, final AbstractHandler handler) {
        Command command = commandService.getCommand(commandId);
        command.define(name, null, autogeneratedCategory);
        command.setHandler(handler);
    }

    private void defineAllCommands() {
        commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
        autogeneratedCategory = commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID);

    	// menu
        importHandler = new ImportOperationHandler();
        exportHandler = new ExportOperationHandler(this);
        executeHandler = new ExecuteOperationHandler(this);

        defineCommand(importCommandId,
        	org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu.Messages.ImportOperationAction_0,
        	importHandler);
        defineCommand(exportCommandId,
        	org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu.Messages.ExportOperationAction_0,
        	exportHandler);
        defineCommand(executeCommandId,
        	org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu.Messages.ExecuteOperationAction_0,
        	executeHandler);
        
        // context
        renameHandler = new RenameHandler(this);
        removeHandler = new RemoveHandler(this);
        removeKeyHandler = new RemoveKeyHandler(this);

        defineCommand(renameCommandId,
        	org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.Messages.RenameHandler_0,
        	renameHandler);
        defineCommand(removeCommandId,
        	org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.Messages.RemoveHandler_0,
        	removeHandler);
        defineCommand(removeKeyCommandId,
        	org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.Messages.RemoveKeyHandler_0,
        	removeKeyHandler);
        
        selectInputFileHandler = new SelectInputFileHandler(this);
        setInputEditorHandler = new SetInputEditorHandler(this);
        selectOutputFileHandler = new SelectOutputFileHandler(this);
        setOutputEditorHandler = new SetOutputEditorHandler(this);
        selectSignatureOutputHandler = new SelectSignatureHandler(this);

        defineCommand(selectInputFileCommandId,
        	org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.Messages.SelectInputFileAction_0,
        	selectInputFileHandler);
        defineCommand(setInputEditorCommandId,
            org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.Messages.SetInputEditorAction_0,
            setInputEditorHandler);
        defineCommand(selectOutputFileCommandId,
            org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.Messages.SelectOutputFileAction_0,
            selectOutputFileHandler);
        defineCommand(setOutputEditorCommandId,
            org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.Messages.SetOutputEditorAction_0,
            setOutputEditorHandler);
        defineCommand(selectSignatureOutputCommandId,
            org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io.Messages.SelectSignatureAction_0,
            selectSignatureOutputHandler);

        encryptHandler = new EncryptHandler(this);
        decryptHandler = new DecryptHandler(this);

        defineCommand(encryptCommandId,
        	org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.ops.Messages.EncryptAction_0,
        	encryptHandler);
        defineCommand(decryptCommandId,
        	org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.ops.Messages.DecryptAction_0,
        	decryptHandler);
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
        this.contentprovider = new FlexiProviderOperationsViewContentProvider(this, viewer);
        viewer.setContentProvider(contentprovider);
        viewer.setLabelProvider(new FlexiProviderOperationsViewLabelProvider());
        viewer.setInput(getViewSite());
        viewer.getTree().setLayoutData(gridData);
        viewer.getControl().addListener(SWT.Selection, this);
        viewer.addDragSupport(dropOps, editorTransfers, new EditorDragListener(viewer));
        viewer.addDropSupport(dropOps, keyTransfers, new KeyDropListener());
        parent.setLayout(new GridLayout());
        defineAllCommands();
        serviceLocator = PlatformUI.getWorkbench();
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
        doubleClickHandler = new AbstractHandler() {
            private ToolTip keyTipViewer;

            @Override
            public Object execute(ExecutionEvent event) {
                ISelection selection = viewer.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();

                if (obj instanceof TreeNode) {
                    if (viewer.getTree().getSelection()[0].getExpanded()) {
                        viewer.collapseToLevel(obj, 1);
                    } else {
                        viewer.expandToLevel(obj, 1);
                    }
                } else if (obj instanceof OperationsNode) {
                    // OperationsManager.getInstance().algorithmCalled(((AlgorithmNode)
                    // obj).getAlgorithm());
                }

                if (obj instanceof KeyNode) {
                    if (keyTipViewer != null && !keyTipViewer.isDisposed())
                        keyTipViewer.dispose();
                    keyTipViewer = new ToolTip(viewer.getControl().getShell(), SWT.BALLOON);
                    keyTipViewer.setMessage(Messages.FlexiProviderOperationsView_keystore_hint);
                    keyTipViewer.setAutoHide(true);
                    keyTipViewer.setLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo()
                            .getLocation().y);
                    keyTipViewer.setVisible(true);
                }

                if (obj instanceof IONode || obj instanceof InputNode || obj instanceof SignatureNode) {
                    // Event e = new Event();
                    // e.widget = viewer.getTree();
                    // e.x = viewer.getTree().toControl(MouseInfo.getPointerInfo().getLocation().x,
                    // MouseInfo.getPointerInfo().getLocation().y).x;
                    // e.y = viewer.getTree().toControl(MouseInfo.getPointerInfo().getLocation().x,
                    // MouseInfo.getPointerInfo().getLocation().y).y;
                    // e.button = 3;
                    // e.stateMask = 0;
                    // e.count = 1;
                    // MouseEvent mE = new MouseEvent(e);
                    // viewer.getTree().notifyListeners(SWT.MouseDown, e);
                    viewer.getControl().getMenu().setVisible(true);
                    viewer.getControl().getMenu().setEnabled(true);
                }

                if (obj instanceof OperationsNode) {
                    if (obj instanceof TreeNode) {
                        currentEntryNode = getCurrentEntryNode((TreeNode) obj);
                        if (displayOperationContextMenu(currentEntryNode)) {
                            viewer.getControl().getMenu().setVisible(true);
                            viewer.getControl().getMenu().setEnabled(true);
                        } else {
                            if (keyTipViewer != null && !keyTipViewer.isDisposed())
                                keyTipViewer.dispose();
                            keyTipViewer = new ToolTip(viewer.getControl().getShell(), SWT.BALLOON);
                            keyTipViewer.setMessage(Messages.FlexiProviderOperationsView_keystore_hint_operations);
                            keyTipViewer.setAutoHide(true);
                            keyTipViewer.setLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo
                                    .getPointerInfo().getLocation().y);
                            keyTipViewer.setVisible(true);
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
                	try {
                		doubleClickHandler.execute(null); // run assigned action
                	} catch(Exception ex) {
                		LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, ex);
                	}
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
                || currentEntryNode.getRegistryType().equals(RegistryType.CIPHER)
                || currentEntryNode.getRegistryType().equals(RegistryType.ASYMMETRIC_HYBRID_CIPHER)
                || currentEntryNode.getRegistryType().equals(RegistryType.ASYMMETRIC_BLOCK_CIPHER);
    }

    private void addContributionItem(IContributionManager manager, final String commandId,
    	final ImageDescriptor icon, final String tooltip)
    {
    	CommandContributionItemParameter param = new CommandContributionItemParameter(serviceLocator,
    		null, commandId, SWT.PUSH);
    	if(icon != null)
    		param.icon = icon;
    	if(tooltip != null && !tooltip.equals(""))
    		param.tooltip = tooltip;
    	CommandContributionItem item = new CommandContributionItem(param);
    	manager.add(item);
    }
    
    private void fillEntryNodeContextMenu(IMenuManager manager) {
        addContributionItem(manager, renameCommandId, null, null);
        addContributionItem(manager, removeCommandId, ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/cancel.png"), null);
    }

    private void fillEncryptDecryptNodeContextMenu(IMenuManager manager) {
    	addContributionItem(manager, encryptCommandId, null, null);
    	addContributionItem(manager, decryptCommandId, null, null);
    }

    private void fillKeyNodeContextMenu(IMenuManager manager) {
    	addContributionItem(manager, removeKeyCommandId, ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/cancel.png"), null);
    }

    private void fillInputContextMenu(IMenuManager manager) {
    	addContributionItem(manager, setInputEditorCommandId, null, null);
    	addContributionItem(manager, selectInputFileCommandId, null, null);
    }

    private void fillOutputContextMenu(IMenuManager manager) {
    	addContributionItem(manager, setOutputEditorCommandId, null, null);
    	addContributionItem(manager, selectOutputFileCommandId, null, null);
    }

    private void fillSignatureOutputContextMenu(IMenuManager manager) {
    	addContributionItem(manager, selectSignatureOutputCommandId, null, null);
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        addContributionItem(manager, executeCommandId, ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/start.gif"),
        	org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu.Messages.ExecuteOperationAction_1);

        manager.add(new Separator());

        addContributionItem(manager, importCommandId, ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/import.gif"),
        	org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu.Messages.ImportOperationAction_1);
        addContributionItem(manager, exportCommandId, ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/export.gif"),
        	org.jcryptool.crypto.flexiprovider.operations.ui.actions.menu.Messages.ExportOperationAction_1);
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
//        OperationsManager.getInstance().removeOperationChangedListener(this);
        FlexiProviderOperationsPlugin.lastOperationsView = null;
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
    	if(currentEntryNode == null && viewer.getTree().getItemCount() == 1)
    	{
    		// return(viewer.getTree().getItem(0));	// Duh.  That's a TreeItem, not a node...
    		Object obj = ((FlexiProviderOperationsViewContentProvider)viewer.getContentProvider()).getElements(getViewSite())[0];
    		if(obj instanceof EntryNode)
    			return (EntryNode)obj;
    	}
        return currentEntryNode;
    }

    private EntryNode getCurrentEntryNode(ITreeNode node) {
        if (node instanceof EntryNode) {
            return (EntryNode) node;
        } else {
            return getCurrentEntryNode(node.getParent());
        }
    }


    public Optional<AlgorithmDescriptor> descriptorToShow = Optional.empty();

    public void addOperation() {
    	this.contentprovider.addOperation();
    }

    public void removeOperation() {
        currentEntryNode = null;
        currentEntryLabel.setText(Messages.FlexiProviderOperationsView_3);
        this.contentprovider.removeOperation();
    }

    public void update(TreeNode updated) {
    	this.contentprovider.update(updated);
    }


	public void selectThis(AlgorithmDescriptor descriptor) {
		FlexiProviderOperationsViewContentProvider content = (FlexiProviderOperationsViewContentProvider)viewer.getContentProvider();
		this.descriptorToShow = Optional.of(descriptor);
        if (descriptorToShow.isPresent()) {
			Object[] items = content.getChildren(content.getInvisibleRoot());
			LinkedList<Object> reverseitems = new LinkedList<Object>();
			for (Object obj: items) {
				reverseitems.add(0, obj);
			}
			
			Optional<Object> toSelect = Optional.empty();
			for (Object item : reverseitems) {
				if (item instanceof EntryNode) {
					EntryNode node = (EntryNode) item;
					AlgorithmDescriptor itemDescr = node.getAlgorithmDescriptor();
					if (itemDescr.getAlgorithmName().equals(descriptor.getAlgorithmName())) {
						toSelect = Optional.of(item);
						break;
					} else {
					}
				}
			}
			if (toSelect.isPresent()) {
				viewer.setSelection(new TreeSelection(new TreePath(new Object[] {toSelect.get()})));
			}
			descriptorToShow = Optional.empty();
		}
	}

	public void showBubble(String string) {
		if (! viewer.getControl().isVisible()) {
			
			Control control = viewer.getControl();
			Shell shell = viewer.getControl().getShell();
			final ToolTip tip = new ToolTip(shell, SWT.BALLOON);
			tip.setMessage(string);
			tip.setVisible(true);
			tip.setLocation(control.toDisplay(control.getSize().x, 0));
			tip.setAutoHide(true);
		} else {
			viewer.getControl().addPaintListener(new PaintListener() {
				private boolean doneThis = false;
				@Override
				public void paintControl(PaintEvent e) {
					if (doneThis) {
						return;
					} else {
						doneThis = true;
					}
					Control control = viewer.getControl();
					Shell shell = viewer.getControl().getShell();
					final ToolTip tip = new ToolTip(shell, SWT.BALLOON);
					tip.setMessage(string);
					tip.setVisible(true);
					tip.setLocation(control.toDisplay(control.getSize().x, 0));
					tip.setAutoHide(true);
					
				}
			});
		}
	}
}

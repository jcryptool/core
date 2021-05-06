// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.fileexplorer.views;

import java.io.File;

import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.State;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.ui.workbench.renderers.swt.HandledContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.views.AlgorithmView;
import org.jcryptool.fileexplorer.commands.CryptoHandler;
import org.jcryptool.fileexplorer.popup.contributions.CryptoContributionItem;

/**
 * A view providing files and directories like a normal file system explorer.
 *
 * @author mwalthart
 * @author Dominik Schadow
 * @version 0.6.0
 */
@SuppressWarnings("restriction")
public class FileExplorerView extends ViewPart {
    private static final String MEMENTOKEY_INVISIBLE_FILES_HIDDEN = "invisibleFilesHidden";
    private TreeViewer viewer;
    private IHandler cryptoHandler;
    private CryptoContributionItem cryptoContributionItem;
    public static final String EDITOR_ID_HEX = IOperationsConstants.ID_HEX_EDITOR; // $NON-NLS-1$
    /** The system clipboard. */
    private Clipboard clipboard = null;

    private FileExplorerContentProvider contentProvider;
    protected boolean menuButtonInitialized = false;
    private boolean hideInvisible;

    /**
     * Creates the view.
     */
    @Override
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
        getSite().setSelectionProvider(viewer);
        contentProvider = new FileExplorerContentProvider();
        contentProvider.setHideInvisible(isHideInvisible());
        viewer.setContentProvider(contentProvider);
        viewer.setLabelProvider(new FileExplorerLabelProvider());
        viewer.setInput(File.listRoots()); // list all drives
        viewer.setAutoExpandLevel(0);

        viewer.setComparator(new FileExplorerViewerComparator());

        expandToLevel(DirectoryService.getUserHomeDir());

        initCopyAndPaste();
        createContributions();
        createContextMenu();
        hookDoubleClickAction();
        setupDragNDrop();

        IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
        configureToolBar(mgr);
        getViewSite().getActionBars().updateActionBars();

        PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),
                "org.jcryptool.fileexplorer.fileExplorerView"); //$NON-NLS-1$
    }

    private void setupDragNDrop() {
        viewer.addDragSupport(DND.DROP_MOVE, new Transfer[] { FileTransfer.getInstance() }, new DragSourceAdapter() {
            public void dragSetData(DragSourceEvent event) {
                TreeItem[] selection = viewer.getTree().getSelection();
                event.data = new String[] { ((IFileStore) selection[0].getData()).toString() };
            }

            public void dragStart(DragSourceEvent event) {
                TreeItem[] selection = viewer.getTree().getSelection();
                if (selection.length > 0 && selection[0].getItemCount() == 0) {
                    event.doit = true;
                    event.data = ((IFileStore) selection[0].getData()).toString();
                } else {
                    event.doit = false;
                }
            };
        });

        viewer.addDropSupport(DND.DROP_MOVE, new Transfer[] { TextTransfer.getInstance() }, new DropTargetAdapter() {
            public void dragOver(DropTargetEvent event) {
                if (event.item != null && event.item.getData() instanceof IFileStore) {
                    String url = ((IFileStore) event.item.getData()).toString();

                    File file = new File(url);
                    if (file.isDirectory()) {
                        event.feedback = DND.DROP_NONE;
                        return;
                    }
                }
            }

            public void drop(DropTargetEvent event) {
                String url = ((IFileStore) event.item.getData()).toString();
                String algorithm = (String) event.data;

                File file = new File(url);
                if (file.isDirectory()) {
                    event.feedback = DND.DROP_NONE;
                    return;
                }
                if (openFile(url))
                    AlgorithmView.doAction(algorithm);
            }
        });
    }

    /**
     * Expands the tree to the given element. Used protocol is <code>file</code> since this view is
     * only able to display the local file system. In case the given element does not exist (any
     * more) the root directory is expanded as fallback. After expanding the given element is
     * selected in the tree.
     *
     * @param element The element to expand
     */
    public void expandToLevel(String element) {
        try {
            viewer.expandToLevel(EFS.getStore(new File(element).toURI()), 1);
            if (viewer.getExpandedElements().length > 0) {
                viewer.setSelection(
                        new StructuredSelection(viewer.getExpandedElements()[viewer.getExpandedElements().length - 1]));
            }
        } catch (Exception ex) {
            viewer.getTree().getItem(0).setExpanded(true);
            viewer.expandToLevel(viewer.getTree().getItem(0).getData(), 1);
        }
    }

    private void configureToolBar(IToolBarManager mgr) {
        mgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /**
     * Initializes the copy and paste functionality.
     */
    private void initCopyAndPaste() {
        clipboard = new Clipboard(getSite().getShell().getDisplay());
    }

    public Clipboard getClipboard() {
        return clipboard;
    }

    public TreeViewer getViewer() {
        return viewer;
    }

    private void createContributions() {
        cryptoHandler = new CryptoHandler();
        cryptoContributionItem = new CryptoContributionItem(this, cryptoHandler);
    }

    private void createContextMenu() {
        MenuManager manager = new MenuManager();
        manager.setRemoveAllWhenShown(true);

        manager.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager m) {
                fillContextMenu(m);
            }
        });

        Menu menu = manager.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu("org.jcryptool.fileexplorer.popup", manager, viewer); //$NON-NLS-1$
    }

    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                ISelection selection = viewer.getSelection();

                if (selection instanceof IStructuredSelection) {
                    IFileStore fs = (IFileStore) ((IStructuredSelection) selection).getFirstElement();

                    if (fs != null) {
                        if (!fs.fetchInfo().isDirectory()) {
                            openFile(fs.toURI().getPath());
                        } else {
                            if (viewer.getTree().getSelection()[0].getExpanded()) {
                                viewer.collapseToLevel(((IStructuredSelection) event.getSelection()).getFirstElement(),
                                        1);
                            } else {
                                viewer.expandToLevel(((IStructuredSelection) event.getSelection()).getFirstElement(),
                                        1);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Disposes the view, frees all system resources.
     */
    public void dispose() {
        if (clipboard != null) {
            clipboard.dispose();
            clipboard = null;
        }
        super.dispose();
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(cryptoContributionItem);
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    private void showErrorDialog(String message) {
        MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.FileExplorerView_error, message);
    }

    public void refresh() {
        Object[] expandedElements = viewer.getExpandedElements();
        viewer.refresh();
        for (Object element : expandedElements) {
            viewer.setExpandedState(element, true);
        }
    }

    public boolean openFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            showErrorDialog(Messages.FileExplorerView_2);
            return false;
        }
        if (!file.canRead()) {
            showErrorDialog(Messages.FileExplorerView_3);
            return false;
        }
        if (file.isDirectory()) {
            showErrorDialog(Messages.FileExplorerView_4);
            return false;
        }

        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
            page.openEditor(new PathEditorInput(new Path(path)), EDITOR_ID_HEX, true, IWorkbenchPage.MATCH_NONE);
        } catch (PartInitException ex) {
            LogUtil.logError(ex);
            return false;
        }
        return true;
    }

    public boolean isHideInvisible() {
        return hideInvisible;
    }

    public void setHideInvisible(boolean hideInvisible) {
        this.hideInvisible = hideInvisible;
        if (contentProvider != null)
            contentProvider.setHideInvisible(hideInvisible);

        if (viewer != null) {
            Object[] expandState = viewer.getExpandedElements();

            viewer.refresh();

            viewer.setExpandedElements(expandState);
        }
    }

    @Override
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);

        if (memento != null && memento.getBoolean(MEMENTOKEY_INVISIBLE_FILES_HIDDEN) != null) {
            boolean invis_files_hidden = memento.getBoolean(MEMENTOKEY_INVISIBLE_FILES_HIDDEN);
            this.setHideInvisible(invis_files_hidden);
        } else {
            setHideInvisible(getDefaultForHideInvisibleFiles());
        }

        getViewSite().getActionBars().getMenuManager().addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                if (!menuButtonInitialized) {
                    try {
                        IContributionItem[] items = getViewSite().getActionBars().getMenuManager().getItems();
                        for (IContributionItem item : items) {
                            if ("org.jcryptool.fileexplorer.invisibleToggle".equals(item.getId())) {
                                if (item instanceof CommandContributionItem) {
                                    CommandContributionItem invisibleToggle = (CommandContributionItem) item;
                                    State state = invisibleToggle.getCommand().getCommand()
                                            .getState(RegistryToggleState.STATE_ID);
                                    if (state == null)
                                        throw new UnsupportedOperationException(
                                                "The 'show invisible files' command does not have a toggle state"); //$NON-NLS-1$
                                    else if (!(state.getValue() instanceof Boolean))
                                        throw new UnsupportedOperationException(
                                                "The 'show invisible files' command's toggle state doesn't contain a boolean value"); //$NON-NLS-1$
                                    else {
                                        state.setValue(!isHideInvisible());
                                    }
                                } else if (item instanceof HandledContributionItem) {
                                    HandledContributionItem invisibleToggle = (HandledContributionItem) item;
                                    // The MCommand's element ID seems to be the command ID
                                    // Knowing the command ID, we can retrieve the Command (not
                                    // MCommand!)
                                    // from the ECommandService
                                    String commandId = invisibleToggle.getModel().getCommand().getElementId();
                                    ECommandService commandService = (ECommandService) PlatformUI.getWorkbench()
                                            .getService(ECommandService.class);
                                    State state = commandService.getCommand(commandId)
                                            .getState(RegistryToggleState.STATE_ID);
                                    if (state == null)
                                        throw new UnsupportedOperationException(
                                                "The 'show invisible files' command does not have a toggle state"); //$NON-NLS-1$
                                    else if (!(state.getValue() instanceof Boolean))
                                        throw new UnsupportedOperationException(
                                                "The 'show invisible files' command's toggle state doesn't contain a boolean value"); //$NON-NLS-1$
                                    else {
                                        state.setValue(!isHideInvisible());
                                    }
                                }
                            }
                        }

                        menuButtonInitialized = true;
                    } catch (UnsupportedOperationException e) {
                        LogUtil.logError(e.getMessage());
                    }
                }
            }
        });
    }

    private boolean getDefaultForHideInvisibleFiles() {
        return true;
    }

    @Override
    public void saveState(IMemento memento) {
        super.saveState(memento);
        memento.putBoolean(MEMENTOKEY_INVISIBLE_FILES_HIDDEN, isHideInvisible());
    }
}

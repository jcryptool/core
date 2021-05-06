// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.ui.views;

import java.util.List;

import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.actions.core.registry.ActionCascadeService;
import org.jcryptool.actions.core.types.ActionCascade;
import org.jcryptool.actions.core.types.ActionItem;
import org.jcryptool.actions.ui.ActionsUIPlugin;
import org.jcryptool.actions.ui.preferences.PreferenceConstants;
import org.jcryptool.actions.ui.views.provider.ActionLabelProvider;
import org.jcryptool.core.util.directories.DirectoryService;

/**
 * The <b>Actions view</b> is able to track all cryptographic changes applied to a selected editor
 * input.
 *
 * @author Dominik Schadow
 * @version 0.9.3
 */
public class ActionView extends ViewPart implements IListChangeListener<Object> {
    public static final String ID = "org.jcryptool.actions.views.ActionView"; //$NON-NLS-1$
    private static final Color colorLightShadow =
            Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
    private final String CRLF = System.getProperty("line.separator"); //$NON-NLS-1$

    private TableViewer viewer;
    private StyledText detailTextfield;
    private SashForm control;

    private TableViewerColumn filenameColumn;

    private String importPath = null;

    @Override
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);

        if (memento == null) {
            setFallbackPath();
        } else {
            importPath = memento.getString("path"); //$NON-NLS-1$

            if (importPath == null || "".equals(importPath)) { //$NON-NLS-1$
                setFallbackPath();
            }
        }
    }

    private void setFallbackPath() {
        importPath = DirectoryService.getUserHomeDir();
    }

    @Override
    public void saveState(IMemento memento) {
        memento.putString("path", importPath); //$NON-NLS-1$

        super.saveState(memento);
    }

    private void createActionTable(Composite parent) {
        // Creating a table viewer
        viewer =
                new TableViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
                        | SWT.FULL_SELECTION);

        viewer.getTable().setLinesVisible(true);
        viewer.getTable().setHeaderVisible(true);

        TableViewerColumn column = new TableViewerColumn(viewer, SWT.LEFT);
        column.getColumn().setText(Messages.ActionView_5);
        column.getColumn().setToolTipText(Messages.ActionView_6);
        column.getColumn().setWidth(50);
        column.getColumn().setMoveable(true);

        column = new TableViewerColumn(viewer, SWT.LEFT);
        column.getColumn().setText(Messages.ActionView_0);
        column.getColumn().setToolTipText(Messages.ActionView_1);
        column.getColumn().setWidth(80);
        column.getColumn().setMoveable(true);

        filenameColumn = new TableViewerColumn(viewer, SWT.LEFT);
        filenameColumn.getColumn().setText(Messages.ActionView_2);
        filenameColumn.getColumn().setToolTipText(Messages.ActionView_3);
        filenameColumn.getColumn().setMoveable(true);
        setFilenameVisibility();

        viewer.setContentProvider(new ObservableListContentProvider());
        viewer.setLabelProvider(new ActionLabelProvider());

        viewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
			public void doubleClick(DoubleClickEvent e) {
            }
        });

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
			public void selectionChanged(SelectionChangedEvent e) {
                ActionItem action =
                        (ActionItem) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
                if (action != null) {
                    detailTextfield.setText(action.getDetails());
                    if (detailTextfield.getLineCount() > 0) {
                        detailTextfield.setLineBackground(0, 1, colorLightShadow);
                        StyleRange styleRange = new StyleRange();
                        styleRange.start = 0;
                        styleRange.length = detailTextfield.getText().indexOf(CRLF);
                        styleRange.fontStyle = SWT.BOLD;
                        detailTextfield.setStyleRange(styleRange);
                        detailTextfield.setTabs(20);

                        int i = 0;
                        int l = 0;
                        while (i >= 0) {
                            i = detailTextfield.getText().indexOf(CRLF, i + 1);
                            l = detailTextfield.getText().indexOf(":", i + 1) - i; //$NON-NLS-1$
                            if (l > 0 && i >= 0) {
                                styleRange.start = i;
                                styleRange.length = l;
                                detailTextfield.setStyleRange(styleRange);
                            }

                        }
                    }
                } else {
                    detailTextfield.setText(""); //$NON-NLS-1$
                }
            }
        });

        viewer.setInput(ActionCascadeService.getInstance().observeActionItems());

        ActionCascadeService.getInstance().observeActionItems().addListChangeListener(this);

        getSite().setSelectionProvider(viewer);
    }

    public void setFilenameVisibility() {
        IPreferenceStore store = ActionsUIPlugin.getDefault().getPreferenceStore();
        setFilenameVisible(store.getBoolean(PreferenceConstants.P_SHOW_FILENAMES));
    }

    public void setFilenameVisible(final boolean visible) {
        if (visible) {
            filenameColumn.getColumn().setWidth(90);
            filenameColumn.getColumn().setResizable(true);
        } else {
            filenameColumn.getColumn().setWidth(0);
            filenameColumn.getColumn().setResizable(false);
        }
    }

    @Override
    public void createPartControl(Composite parent) {
        control = new SashForm(parent, SWT.VERTICAL);

        createActionTable(control);
        detailTextfield =
                new StyledText(control, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        detailTextfield.setMargins(10, 10, 10, 10);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),
                "org.jcryptool.actions.ui.actionView"); //$NON-NLS-1$

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
        getSite().registerContextMenu("org.jcryptool.actions.popup", manager, viewer); //$NON-NLS-1$
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    public ActionCascade getActionCascade() {
        return ActionCascadeService.getInstance().getCurrentActionCascade();
    }

    public void refresh() {
        viewer.refresh();
    }

    public boolean hasContent() {
        return viewer.getTable().getItemCount() > 0 ? true : false;
    }

    public TableViewer getViewer() {
        return this.viewer;
    }

    public void setImportPath(final String importPath) {
        this.importPath = importPath;
    }

    public String getImportPath() {
        return importPath;
    }

	@Override
	public void handleListChange(ListChangeEvent<?> event) {

		// list changes are either additions or removals

		ListDiffEntry[] listDiffs = event.diff.getDifferences();
		for (ListDiffEntry<?> listDiffEntry : listDiffs) {
			if(listDiffEntry.isAddition()) handleAddition();
			else handleRemoval();
		}
	}

	private void selectFirstActionItem() {
		List<ActionItem> actionItems =  ActionCascadeService.getInstance().getActionItems();
        if(actionItems != null && actionItems.size() > 0)
        {
            ActionItem firstItem = actionItems.get(0);
            viewer.setSelection(new StructuredSelection(firstItem), true);
        }
	}

	private void handleAddition() {
		selectFirstActionItem();
	}

	private void handleRemoval() {
		selectFirstActionItem();
	}
}

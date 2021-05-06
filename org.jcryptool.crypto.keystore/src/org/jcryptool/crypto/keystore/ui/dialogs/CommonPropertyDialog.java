// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.wb.swt.layout.grouplayout.LayoutStyle;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.ContentProviderFactory;
import org.jcryptool.crypto.keystore.ui.views.nodes.TreeNode;

/**
 * @author Anatoli Barski
 * 
 */
public class CommonPropertyDialog extends TitleAreaDialog {
    private IStructuredContentProvider contentProvider;
    protected TreeNode treeNode;
    private Table table;

    public CommonPropertyDialog(Shell parentShell, TreeNode treeNode) {
        super(parentShell);
        this.treeNode = treeNode;
        this.setHelpAvailable(false);
    }

    public IStructuredContentProvider getContentProvider() {
        if (contentProvider == null) {
            contentProvider = ContentProviderFactory.create(treeNode);
        }
        return contentProvider;
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {

        Composite area = (Composite) super.createDialogArea(parent);
        this.setMessage(Messages.getString("CommonPropertyDialog.0")); //$NON-NLS-1$

        Composite composite = new Composite(area, SWT.NONE);
        TableColumnLayout tclComposite = new TableColumnLayout(true);
        composite.setLayout(tclComposite);

        TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL);
        // Copy values automatical to the clipboard
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
			public void selectionChanged(SelectionChangedEvent event) {
                StructuredSelection selection = (StructuredSelection) event.getSelectionProvider().getSelection();
                TableEntry tableEntry = (TableEntry) selection.getFirstElement();
                final Clipboard cb = new Clipboard(Display.getCurrent());
                cb.setContents(new Object[] { tableEntry.getValue() }, new Transfer[] { TextTransfer.getInstance() });
            }
        });
        table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableViewerColumn tableViewerNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        tableViewerNameColumn.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                TableEntry entry = (TableEntry) element;
                return entry == null ? "" : entry.getName(); //$NON-NLS-1$
            }
        });
        TableColumn tblclmnNameColumn = tableViewerNameColumn.getColumn();
        tclComposite.setColumnData(tblclmnNameColumn, new ColumnWeightData(1, 2));
        tblclmnNameColumn.setText(Messages.getString("AbstractKeyDialog.tblclmnNameColumn.text")); //$NON-NLS-1$

        TableViewerColumn tableViewerValueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        tableViewerValueColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public Image getImage(Object element) {
                return null;
            }

            @Override
            public String getText(Object element) {
                TableEntry entry = (TableEntry) element;
                return entry == null ? "" : entry.getValue(); //$NON-NLS-1$
            }
        });
        TableColumn tblclmnValueColumn = tableViewerValueColumn.getColumn();
        tclComposite.setColumnData(tblclmnValueColumn, new ColumnWeightData(2, 2));
        tblclmnValueColumn.setText(Messages.getString("AbstractKeyDialog.tblclmnValueColumn.text")); //$NON-NLS-1$
        tableViewer.setContentProvider(getContentProvider());

        tableViewer.setInput(treeNode);

        Label lblTableLabel = new Label(area, SWT.NONE);
        lblTableLabel.setText(Messages.getString("AbstractKeyDialog.lblTableLabel.text")); //$NON-NLS-1$
        GroupLayout glArea = new GroupLayout(area);
        glArea.setHorizontalGroup(glArea.createParallelGroup(GroupLayout.LEADING).add(
                glArea.createSequentialGroup()
                        .addContainerGap()
                        .add(glArea.createParallelGroup(GroupLayout.LEADING).add(lblTableLabel)
                                .add(composite, GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)).addContainerGap()));
        glArea.setVerticalGroup(glArea.createParallelGroup(GroupLayout.LEADING).add(
                glArea.createSequentialGroup().addContainerGap().add(lblTableLabel)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(composite, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE).addContainerGap()));
        area.setLayout(glArea);

        return area;
    }
    

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CLOSE_LABEL, true);
    }
    
    @Override
    protected boolean isResizable() {
    	return true;
    }

}

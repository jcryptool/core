/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.verify;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * <p>The comparator for the <b>XML Signatures</b> view. It is possible to
 * sort the signature data by any column in ascending or descending order.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public abstract class SignatureComparator extends ViewerComparator {
    public static final int ASC = 1;
    public static final int NONE = 0;
    public static final int DESC = -1;

    private int direction = 0;

    private TableViewerColumn column;

    private ColumnViewer viewer;

    public SignatureComparator(ColumnViewer viewer, TableViewerColumn column) {
        this.column = column;
        this.viewer = viewer;
        this.column.getColumn().addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (SignatureComparator.this.viewer.getComparator() != null) {
                    if (SignatureComparator.this.viewer.getComparator() == SignatureComparator.this) {
                        int tdirection = SignatureComparator.this.direction;

                        if (tdirection == ASC) {
                            setSorter(SignatureComparator.this, DESC);
                        } else if (tdirection == DESC) {
                            setSorter(SignatureComparator.this, NONE);
                        }
                    } else {
                        setSorter(SignatureComparator.this, ASC);
                    }
                } else {
                    setSorter(SignatureComparator.this, ASC);
                }
            }
        });
    }

    public void setSorter(SignatureComparator sorter, int direction) {
        if (direction == NONE) {
            column.getColumn().getParent().setSortColumn(null);
            column.getColumn().getParent().setSortDirection(SWT.NONE);
            viewer.setComparator(null);
        } else {
            column.getColumn().getParent().setSortColumn(column.getColumn());
            sorter.direction = direction;

            if (direction == ASC) {
                column.getColumn().getParent().setSortDirection(SWT.UP);
            } else {
                column.getColumn().getParent().setSortDirection(SWT.DOWN);
            }

            if (viewer.getComparator() == sorter) {
                viewer.refresh();
            } else {
                viewer.setComparator(sorter);
            }

        }
    }

    public int compare(Viewer viewer, Object e1, Object e2) {
        return direction * doCompare(viewer, e1, e2);
    }

    protected abstract int doCompare(Viewer viewer, Object e1, Object e2);
}

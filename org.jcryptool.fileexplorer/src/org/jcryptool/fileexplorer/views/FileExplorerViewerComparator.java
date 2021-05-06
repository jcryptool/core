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

import java.util.Comparator;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 *
 *
 * @author Simon L
 */
public class FileExplorerViewerComparator extends ViewerComparator {

    @Override
    public int category(Object element) {
        if (element instanceof IFileStore) {
            IFileStore fileElement = (IFileStore) element;
            try {
                if (fileElement.toLocalFile(0, null).isDirectory()) {
                    return 0;
                }
                return 1;
            } catch (CoreException e) {
                // maybe some files cannot be read, like Boot sector files. No need to worry about
                // it
            }
        }
        return Integer.MAX_VALUE;
    }

    @Override
    protected Comparator<? super String> getComparator() {
        return new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return ((String) o1).compareToIgnoreCase((String) o2);
            }
        };
    }
}

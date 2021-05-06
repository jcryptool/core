// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.dialogs.contentproviders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Anatoli Barski
 * 
 */
public class CommonContentProvider implements IStructuredContentProvider {

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public Object[] getElements(Object inputElement) {
        return null;
    }

    /*
     * helper for merging 2 arrays in a single array
     * @returns array starting with elements from array1 and finishing with elements from array2
     */
    protected Object[] merge(Object[] array1, Object[] array2) {
        List<Object> list = new ArrayList<Object>();
        if (array1 != null)
            list.addAll(Arrays.asList(array1));
        if (array2 != null)
            list.addAll(Arrays.asList(array2));
        return list.toArray();
    }

}

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sphincs.ui;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.jcryptool.visual.sphincs.algorithm.Node;

/**
 * Class to define the linker of the Nodes
 * 
 * @author Philipp Guggenberger
 *
 */
public class ZestNodeContentProvider extends ArrayContentProvider implements IGraphEntityContentProvider {

    @Override
    public Object[] getConnectedTo(Object entity) {
        if (entity instanceof Node) {
            Node node = (Node) entity;
            return node.getConnectedTo().toArray();
        }

        throw new RuntimeException("Type not supported"); //$NON-NLS-1$
    }
}

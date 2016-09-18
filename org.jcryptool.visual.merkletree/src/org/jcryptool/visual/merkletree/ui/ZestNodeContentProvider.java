package org.jcryptool.visual.merkletree.ui;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.jcryptool.visual.merkletree.algorithm.Node;

/**
 * Class to define the linker of the Nodes
 * 
 * @author Kevin Muehlboeck
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

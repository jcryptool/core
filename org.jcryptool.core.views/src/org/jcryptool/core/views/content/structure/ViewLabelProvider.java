//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.views.content.structure;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jcryptool.core.views.ViewsPlugin;
import org.jcryptool.core.views.content.TreeView;

/**
 * LabelPrivder for the TreeViewer
 *
 * @author mwalthart
 */
public class ViewLabelProvider extends LabelProvider {

    /**
     * returns the label for the given object
     */
    public String getText(Object obj) {
        return obj.toString();
    }

    /**
     * returns the icon for the given object
     */
    public Image getImage(Object obj) {
        // inner node
        if (obj instanceof TreeParent) {
            return AbstractUIPlugin.imageDescriptorFromPlugin(ViewsPlugin.PLUGIN_ID,
                    TreeView.ICON_FOLDER).createImage();
        }

        // leaf from JCrypTool
        if (((TreeObject) obj).isFlexiProviderAlgorithm()) {
            return AbstractUIPlugin.imageDescriptorFromPlugin(ViewsPlugin.PLUGIN_ID,
                    TreeView.ICON_ITEM_FLEXI).createImage();
        }
        // leaf from FlexiProvider
        return AbstractUIPlugin.imageDescriptorFromPlugin(ViewsPlugin.PLUGIN_ID,
                TreeView.ICON_ITEM_JCT).createImage();
    }
}

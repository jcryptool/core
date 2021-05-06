//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
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
import org.jcryptool.core.util.images.ImageService;
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
     * FIXME: Does not return the correct image.
     * returns the icon for the given object
     */
    public Image getImage(Object obj) {
        // inner node
        if (obj instanceof TreeParent) {
            return ImageService.getImage(ViewsPlugin.PLUGIN_ID, TreeView.ICON_FOLDER);
        }

        // leaf from JCrypTool
        if (((TreeObject) obj).isFlexiProviderAlgorithm()) {
            return ImageService.ICON_PERSPECTIVE_ALGORITHM;
        }
        // leaf from FlexiProvider
        return ImageService.ICON_PERSPECTIVE_STANDARD;
    }
}

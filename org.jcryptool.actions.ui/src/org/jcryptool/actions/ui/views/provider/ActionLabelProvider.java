// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.ui.views.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jcryptool.actions.core.types.ActionItem;
import org.jcryptool.actions.core.utils.Constants;
import org.jcryptool.actions.ui.ActionsUIPlugin;
import org.jcryptool.core.util.images.ImageService;

/**
 * Label provider for the <b>Actions view</b>.
 *
 * @author Dominik Schadow
 * @version 0.9.7
 */
public class ActionLabelProvider extends LabelProvider implements ITableLabelProvider {
    @Override
	public String getColumnText(Object obj, int index) {
        ActionItem item = (ActionItem) obj;

        switch (index) {
            case 1:
            	return item.getActionName();
            case 2:
            	return item.getFilename();
            default:
                return null;
        }
    }

    @Override
	public Image getColumnImage(Object obj, int index) {
    	ActionItem item = (ActionItem) obj;

        switch (index) {
            case 0:
            	if (item.getActionType() != null && item.getActionType().equals(Constants.ACTIONS_DECRYPT)){
            		return ImageService.getImage(ActionsUIPlugin.PLUGIN_ID, "icons/decrypt.gif");
            	} else if (item.getActionType() != null &&  item.getActionType().equals(Constants.ACTIONS_ENCRYPT)){
            		return ImageService.getImage(ActionsUIPlugin.PLUGIN_ID, "icons/encrypt.gif");
            	} else if (item.getActionType() != null &&  item.getActionType().equals(Constants.ACTIONS_SIGN)){
            		return ImageService.getImage(ActionsUIPlugin.PLUGIN_ID, "icons/sign.gif");
            	} else if (item.getActionType() != null &&  item.getActionType().equals(Constants.ACTIONS_VERIFY)){
            		return ImageService.getImage(ActionsUIPlugin.PLUGIN_ID, "icons/verify.gif");
            	}
            default:
                return null;
        }
    }
}

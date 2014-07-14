// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
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

/**
 * Label provider for the <b>Actions view</b>.
 *
 * @author Dominik Schadow
 * @version 0.9.7
 */
public class ActionLabelProvider extends LabelProvider implements ITableLabelProvider {
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

    public Image getColumnImage(Object obj, int index) {
    	ActionItem item = (ActionItem) obj;

        switch (index) {
            case 0:
            	if (item.getActionType() != null && item.getActionType().equals(Constants.ACTIONS_DECRYPT)){
            		return ActionsUIPlugin.getImageDescriptor("icons/decrypt.gif").createImage(); //$NON-NLS-1$;
            	} else if (item.getActionType() != null &&  item.getActionType().equals(Constants.ACTIONS_ENCRYPT)){
            		return ActionsUIPlugin.getImageDescriptor("icons/encrypt.gif").createImage(); //$NON-NLS-1$;;
            	} else if (item.getActionType() != null &&  item.getActionType().equals(Constants.ACTIONS_SIGN)){
            		return ActionsUIPlugin.getImageDescriptor("icons/sign.gif").createImage(); //$NON-NLS-1$;;
            	} else if (item.getActionType() != null &&  item.getActionType().equals(Constants.ACTIONS_VERIFY)){
            		return ActionsUIPlugin.getImageDescriptor("icons/verify.gif").createImage(); //$NON-NLS-1$;;
            	}
            default:
                return null;
        }
    }
}

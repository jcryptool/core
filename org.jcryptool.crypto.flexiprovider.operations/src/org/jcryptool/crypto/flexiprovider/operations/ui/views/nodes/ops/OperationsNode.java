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
package org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.ops;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.flexiprovider.operations.FlexiProviderOperationsPlugin;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;

public class OperationsNode extends TreeNode {

	private OperationType type = OperationType.UNKNOWN;

	public OperationsNode() {
		super(Messages.OperationsNode_0);
	}

	public void setOperation(OperationType type) {
		this.type = type;
		if (OperationType.UNKNOWN.equals(type)) {
			super.setName(Messages.OperationsNode_1);
		} else {
			super.setName(NLS.bind(Messages.OperationsNode_2, this.type.getTypeName()));
		}
	}

	public OperationType getOperation() {
		return type;
	}

	public ImageDescriptor getImageDescriptor() {
		return ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/start.gif");
	}

}

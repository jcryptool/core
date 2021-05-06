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
package org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io;

import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.flexiprovider.operations.FlexiProviderOperationsPlugin;
import org.jcryptool.crypto.flexiprovider.operations.OperationsManager;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.IOperationChangedListener;
import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;

public class InputNode extends TreeNode {

	private String input;

	public InputNode() {
		super(Messages.InputNode_0);

		setInput(Messages.InputNode_2);
	}

	public void setInput(String input) {
		this.input = input;
		setName(NLS.bind(Messages.InputNode_1, this.input));
		Iterator<IOperationChangedListener> it = OperationsManager.getInstance().getOperationChangedListeners();
		while (it.hasNext()) {
			it.next().update(this);
		}
	}

	public String getInput() {
		return input;
	}

	public ImageDescriptor getImageDescriptor() {
		return ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/folder_inbox.png");
	}

}

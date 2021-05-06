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

public class OutputNode extends IONode {

	private String output;

	public OutputNode() {
		super(Messages.OutputNode_0);

		setOutput(Messages.OutputNode_2);
	}

	public void setOutput(String output) {
		this.output = output;
		setName(NLS.bind(Messages.OutputNode_1, this.output));
		Iterator<IOperationChangedListener> it = OperationsManager.getInstance().getOperationChangedListeners();
		while (it.hasNext()) {
			it.next().update(this);
		}
	}

	public String getOutput() {
		return output;
	}

	public ImageDescriptor getImageDescriptor() {
		return ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/folder_outbox.png");
	}

}

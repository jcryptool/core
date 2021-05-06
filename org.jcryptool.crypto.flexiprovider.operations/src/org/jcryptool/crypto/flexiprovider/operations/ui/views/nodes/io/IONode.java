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

import org.eclipse.jface.resource.ImageDescriptor;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.flexiprovider.operations.FlexiProviderOperationsPlugin;
import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;

public abstract class IONode extends TreeNode {

	public IONode(String name) {
		super(name);
	}
	
	public ImageDescriptor getImageDescriptor() {
		return ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/find.png");
	}

}

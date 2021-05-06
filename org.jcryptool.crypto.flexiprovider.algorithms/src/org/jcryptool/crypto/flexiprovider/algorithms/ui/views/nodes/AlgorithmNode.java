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
package org.jcryptool.crypto.flexiprovider.algorithms.ui.views.nodes;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;



public class AlgorithmNode extends TreeNode {

	private IMetaAlgorithm algorithm;

	public AlgorithmNode(IMetaAlgorithm algorithm) {
		super(algorithm.getName());
		if (algorithm.getOID() != null) {
			super.setName(algorithm.getName() + " (OID: " + algorithm.getOID().toString() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		this.algorithm = algorithm;
	}

	public IMetaAlgorithm getAlgorithm() {
		return algorithm;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.createFromImage(
				PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT)
		);
	}

}

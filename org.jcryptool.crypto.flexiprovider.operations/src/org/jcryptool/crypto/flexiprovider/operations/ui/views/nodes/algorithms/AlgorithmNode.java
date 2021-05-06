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
package org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.algorithms;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.BlockCipherDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.SecureRandomDescriptor;
import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;

public class AlgorithmNode extends TreeNode {

	public AlgorithmNode(AlgorithmDescriptor descriptor) {
		super(NLS.bind(Messages.AlgorithmNode_0, descriptor.getAlgorithmName()));
		if (descriptor instanceof BlockCipherDescriptor) {
			this.addChild(new ModeNode( ((BlockCipherDescriptor)descriptor).getMode() ));
			this.addChild(new PaddingNode( ((BlockCipherDescriptor)descriptor).getPadding() ));
		} else if (descriptor instanceof SecureRandomDescriptor) {
			this.addChild(new LengthNode( ((SecureRandomDescriptor)descriptor).getLength() ));
		} else {
			// AlgorithmNode
		}
	}

	public ImageDescriptor getImageDescriptor() {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT);
	}


	private class LengthNode extends TreeNode {

		public LengthNode(int length) {
			super(NLS.bind(Messages.AlgorithmNode_1, length));
		}

		public ImageDescriptor getImageDescriptor() {
			return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT);
		}

	}

	private class ModeNode extends TreeNode {

		public ModeNode(String name) {
			super(NLS.bind(Messages.AlgorithmNode_3, name));
		}

		public ImageDescriptor getImageDescriptor() {
			return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT);
		}

	}

	private class PaddingNode extends TreeNode {

		public PaddingNode(String name) {
			super(NLS.bind(Messages.AlgorithmNode_4, name));
		}

		public ImageDescriptor getImageDescriptor() {
			return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
}

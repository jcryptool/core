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
package org.jcryptool.crypto.flexiprovider.algorithms.ui.views.providers;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.views.nodes.AlgorithmNode;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.views.nodes.CategoryNode;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.views.nodes.FolderNode;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.ui.nodes.ITreeNode;
import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;



public class FlexiProviderAlgorithmsViewContentProvider implements
IStructuredContentProvider, ITreeContentProvider {

	public static ITreeNode _invisibleRoot;
	private static ViewPart _viewPart;

	public FlexiProviderAlgorithmsViewContentProvider(ViewPart viewPart) {
		_viewPart = viewPart;
	}

	public void initialize() {
		_invisibleRoot = new TreeNode("INVISIBLE_ROOT"); //$NON-NLS-1$
		_invisibleRoot.addChild(init(Messages.FlexiProviderAlgorithmsViewContentProvider_0, AlgorithmsXMLManager.getInstance().getAsymmetricBlockCiphers()));
		_invisibleRoot.addChild(init(Messages.FlexiProviderAlgorithmsViewContentProvider_1, AlgorithmsXMLManager.getInstance().getAsymmetricHybridCiphers()));
		_invisibleRoot.addChild(init(Messages.FlexiProviderAlgorithmsViewContentProvider_2, AlgorithmsXMLManager.getInstance().getBlockCiphers()));
		_invisibleRoot.addChild(init(Messages.FlexiProviderAlgorithmsViewContentProvider_3, AlgorithmsXMLManager.getInstance().getCiphers()));
		_invisibleRoot.addChild(init(Messages.FlexiProviderAlgorithmsViewContentProvider_4, AlgorithmsXMLManager.getInstance().getMacs()));
		_invisibleRoot.addChild(init(Messages.FlexiProviderAlgorithmsViewContentProvider_5, AlgorithmsXMLManager.getInstance().getMessageDigests()));
		_invisibleRoot.addChild(init(Messages.FlexiProviderAlgorithmsViewContentProvider_6, AlgorithmsXMLManager.getInstance().getSecureRandoms()));
		_invisibleRoot.addChild(init(Messages.FlexiProviderAlgorithmsViewContentProvider_7, AlgorithmsXMLManager.getInstance().getSignatures()));
	}
	
	private static CategoryNode init(String label, List<IMetaAlgorithm> algorithms) {
		CategoryNode category = new CategoryNode(label);

		// should be sorted already. just to be on the save side.
		Collections.sort(algorithms);
		
		// top level entries
		for (IMetaAlgorithm meta : algorithms) {
			if (!meta.getClassName().contains("$")) { //$NON-NLS-1$
				category.addChild(new AlgorithmNode(meta));
			}
		}
		
		// sub level entries
		for (IMetaAlgorithm meta : algorithms) {
			if (meta.getClassName().contains("$")) { //$NON-NLS-1$
				String primaryPrefix = meta.getClassName().substring(0, meta.getClassName().indexOf("$")); //$NON-NLS-1$
				if (containsClassName(primaryPrefix, algorithms)) {
					// recursive entries
					buildAlgorithmHierarchy(category, meta);	
				} else {
					// folder entries
					String prefix = meta.getClassName().substring(meta.getClassName().lastIndexOf(".")+1, meta.getClassName().indexOf("$"));					 //$NON-NLS-1$ //$NON-NLS-2$
					ITreeNode folder = new FolderNode(prefix);
					for (IMetaAlgorithm meta2 : algorithms) {
						if (meta2.getClassName().contains("$")) { //$NON-NLS-1$
							if (meta2.getClassName().contains("."+prefix)) { //$NON-NLS-1$
								folder.addChild(new AlgorithmNode(meta2));
							}
						}
					}
					category.addChild(folder);	
				}
				
			}
		}		
		return category;
	}
	
	private static boolean containsClassName(String className, List<IMetaAlgorithm> algorithms) {
		for (IMetaAlgorithm meta : algorithms) {
			if (className.equals(meta.getClassName())) return true;
		}
		return false;
	}
	
	private static void buildAlgorithmHierarchy(ITreeNode parent, IMetaAlgorithm child) {
		Object[] children = parent.getChildrenArray();
		for (Object obj : children) {
			if (obj instanceof AlgorithmNode) {
				ITreeNode tmp = (ITreeNode)obj;
				if (tmp.hasChildren()) {
					buildAlgorithmHierarchy(tmp, child);
				} 
				if (tmp instanceof AlgorithmNode) {
					String currentClassName = ((AlgorithmNode)tmp).getAlgorithm().getClassName();
					if (child.getClassName().startsWith(currentClassName)) {
						if (!child.getClassName().substring(currentClassName.length()+1, child.getClassName().length()).contains("$")) { //$NON-NLS-1$
							// direct heir
							((AlgorithmNode)tmp).addChild(new AlgorithmNode(child));
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public Object[] getElements(Object object) {
		if (object.equals(_viewPart.getViewSite())) {
			if (_invisibleRoot==null) {
//				initializeOLD();
				initialize();
			}
			return getChildren(_invisibleRoot);
		}
		return getChildren(object);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getChildren(Object object) {
		if (object instanceof TreeNode) {
			return ((TreeNode)object).getChildrenArray();
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object object) {
		if (object instanceof TreeNode) {
			return ((TreeNode)object).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object object) {
		if (object instanceof TreeNode) {
			return ((TreeNode)object).hasChildren();
		}
		return false;
	}

}

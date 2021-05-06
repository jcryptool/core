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
package org.jcryptool.fileexplorer.views.factories;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * Adapterfactory to fill the <b>File Explorer</b> view with the
 * active file systems content.
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class FileExplorerAdapterFactory implements IAdapterFactory {
	private IWorkbenchAdapter fileAdapter = new IWorkbenchAdapter() {
		public Object[] getChildren(Object object) {
			try {
				IFileStore store = (IFileStore) object;
				if (store.fetchInfo().isDirectory()) {
					return store.childStores(EFS.NONE, null);

				}
			} catch (Exception ignore) { }

			return new IFileStore[0];
		}

		public ImageDescriptor getImageDescriptor(Object object) {
			return ImageFactory.getIconForFile((IFileStore) object);
		}

		public String getLabel(Object object) {
			return ((IFileStore) object).getName();
		}

		public Object getParent(Object object) {
			return ((IFileStore) object).getParent();
		}
	};

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof IFileStore && adapterType == IWorkbenchAdapter.class) {
			return fileAdapter;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Class[] getAdapterList() {
		return new Class[] {IWorkbenchAdapter.class};
	}
}

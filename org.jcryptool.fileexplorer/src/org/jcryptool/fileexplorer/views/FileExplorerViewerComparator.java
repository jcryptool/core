//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.fileexplorer.views;

import java.util.Comparator;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 * 
 * 
 * @author Simon L
 */
public class FileExplorerViewerComparator extends ViewerComparator {
	
	@Override
	public int category(Object element) {
		if(element instanceof IFileStore) {
			IFileStore fileElement = (IFileStore) element;
			try {
				if(fileElement.toLocalFile(0, null).isDirectory()) {
					return 0;
				}
				return 1;
			} catch (CoreException e) {
				// maybe some files cannot be read, like Boot sector files. No need to worry about this imo.
//				LogUtil.logError("Error at retrieving local file information for sorting in File Explorer.");
			}
		}
		return Integer.MAX_VALUE;
	}
	
	@SuppressWarnings("unchecked")
	protected Comparator<Object> getSuperComparator() {
		return super.getComparator();
	}
	
	@Override
	protected Comparator<?> getComparator() {
		return new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				if(o1 instanceof String && o2 instanceof String) {
					return ((String)o1).compareToIgnoreCase((String)o2);
				} else {
					return getSuperComparator().compare(o1, o2);
				}
			}
		};
	}


//	public int category(IFileStore element) {
//		try {
//			if(element.toLocalFile(EFS.CACHE, null).isDirectory()) return 0;
//			return 1;
//		} catch (CoreException e) {
//			LogUtil.logError("Error at retrieving local file information for sorting in File Explorer.");
//		}
//		
//		return super.category(element);
//	}
//
//	@Override
//	public int compare(Viewer viewer, Object e1, Object e2) {
//		
//		if(! (e1 instanceof IFileStore && e2 instanceof IFileStore)) {
//			return super.compare(viewer, e1, e2);
//		}
//		
//		IFileStore e1File = (IFileStore) e1;
//		IFileStore e2File = (IFileStore) e2;
//		
//		int cat1 = category(e1File);
//        int cat2 = category(e2File);
//
//        if (cat1 != cat2) {
//			return cat1 - cat2;
//		}
//    	
//        String name1;
//        String name2;
//
//        if (viewer == null || !(viewer instanceof ContentViewer)) {
//            name1 = e1.toString();
//            name2 = e2.toString();
//        } else {
//            IBaseLabelProvider prov = ((ContentViewer) viewer)
//                    .getLabelProvider();
//            if (prov instanceof ILabelProvider) {
//                ILabelProvider lprov = (ILabelProvider) prov;
//                name1 = lprov.getText(e1);
//                name2 = lprov.getText(e2);
//            } else {
//                name1 = e1.toString();
//                name2 = e2.toString();
//            }
//        }
//        if (name1 == null) {
//			name1 = "";//$NON-NLS-1$
//		}
//        if (name2 == null) {
//			name2 = "";//$NON-NLS-1$
//		}
//
//        // use the comparator to compare the strings
//        return getComparator().compare(name1, name2);
//		
//	}

	
}

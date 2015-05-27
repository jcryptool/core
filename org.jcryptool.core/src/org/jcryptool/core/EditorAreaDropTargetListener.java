// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2015 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.commands.FileOpener;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.util.PathEditorInput;

/**
 * @author Holger Friedrich
 *
 */
public class EditorAreaDropTargetListener implements DropTargetListener {

    private static final String TEXT_EDITOR = "org.jcryptool.editor.text.editor.JCTTextEditor"; //$NON-NLS-1$
    private static final String HEX_EDITOR = "net.sourceforge.ehep.editors.HexEditor"; //$NON-NLS-1$
    private String getEditorId(final String osString) {
        final IEditorDescriptor descriptor = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(osString);

        if (descriptor != null) {
            return descriptor.getId();
        } else {
            // no file association; opening the file with the hex editor
            return HEX_EDITOR;
        }
    }

	/**
	 * 
	 */
	public EditorAreaDropTargetListener() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragEnter(DropTargetEvent event) {
		if(FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
			if(event.detail == DND.DROP_DEFAULT) {
				event.detail = DND.DROP_COPY;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragLeave(DropTargetEvent event) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragOver(DropTargetEvent event) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void drop(DropTargetEvent event) {
		// TODO Auto-generated method stub
		if(FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
			String[] filenames = (String [])event.data;
			for(String filename:  filenames) {
				FileOpener.open(filename);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dropAccept(DropTargetEvent event) {
		if(!FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
			// * <p>The application can veto the drop by setting the <code>event.detail</code> field to 
			// * <code>DND.DROP_NONE</code>.</p>
			event.detail = DND.DROP_NONE;
		}
	}

}

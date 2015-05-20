/**
 * 
 */
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
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.util.PathEditorInput;

/**
 * @author frh
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
		System.out.println("dragEnter() called");
		if(FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
			System.out.println("we are looking at a valid FileTransfer");
			System.out.println("event.detail == " + event.detail);
			System.out.println("event.feedback == " + event.feedback);
			if(event.detail == DND.DROP_DEFAULT) {
				event.detail = DND.DROP_COPY;
			}
		} else {
			System.out.println("this does not seem to be a valid FileTransfer");
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragLeave(DropTargetEvent event) {
		System.out.println("dragLeave() called");
		System.out.println("event.detail == " + event.detail);
		System.out.println("event.feedback == " + event.feedback);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		// TODO Auto-generated method stub
		System.out.println("dragOperationChanged() called");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragOver(DropTargetEvent event) {
		System.out.println("dragOver() called");
		System.out.println("event.detail == " + event.detail);
		System.out.println("event.feedback == " + event.feedback);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void drop(DropTargetEvent event) {
		// TODO Auto-generated method stub
		System.out.println("drop() called");
		if(FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
			System.out.println("now we should handle dropping the file");
			String[] filenames = (String [])event.data;
			for(String filename:  filenames)
			{
			final IPath path = new Path(filename);
            final String editorId = getEditorId(path.toOSString());

            if (editorId != null) {
                try {
                    if (editorId.equals(TEXT_EDITOR)) {
                        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new PathEditorInput(path.toOSString()), editorId, true,
                                IWorkbenchPage.MATCH_NONE);
                    } else if (editorId.equals(HEX_EDITOR)) {
                    	PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new PathEditorInput(path.toOSString()), editorId, true,
                                IWorkbenchPage.MATCH_NONE);
                    }
                } catch (final PartInitException ex) {
                    MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), 
                    		"could not open", // Messages.OpenFileAction_title_could_not_open,
                            "could not open");  // NLS.bind(Messages.OpenFileAction_message_could_not_open, editorId));
                    LogUtil.logError(ex);
                }
            } else { // no editor is associated
                MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                        "could not open", // Messages.OpenFileAction_title_could_not_open,
                        "assign editor");  // NLS.bind(Messages.OpenFileAction_message_assign_editor, path.getFileExtension()));
            }
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dropAccept(DropTargetEvent event) {
		System.out.println("dropAccept() called");
		if(!FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
			// * <p>The application can veto the drop by setting the <code>event.detail</code> field to 
			// * <code>DND.DROP_NONE</code>.</p>
			event.detail = DND.DROP_NONE;
		}
	}

}

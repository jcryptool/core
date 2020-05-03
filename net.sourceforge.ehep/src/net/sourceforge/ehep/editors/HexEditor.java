/*==========================================================================
 * 
 * HexEditor.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.10 $
 * $Date: 2012/11/06 16:45:23 $
 * $Name:  $
 * 
 * Created on 10-Nov-2003
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.editors;

import java.io.File;
import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.part.EditorPart;
import org.jcryptool.core.operations.util.PathEditorInput;

import net.sourceforge.ehep.EhepPlugin;
import net.sourceforge.ehep.core.FileManager;
import net.sourceforge.ehep.gui.HexEditorControl;

/**
 * The main EHEP plug-in editor part
 *
 * @author Marcel Palko alias Randallco
 * @author randallco@users.sourceforge.net
 */
public class HexEditor extends EditorPart {
	private HexEditorControl hexEditorControl = null;
	private FileManager fileManager = null;
//	private IFile iFile = null;
	private File ioFile = null;
	private boolean isDirty = false;
	
	public HexEditor() {
		super();
	}
	
	/**
	 * @see org.eclipse.ui.IEditorPart#doSave(IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		IEditorInput input = getEditorInput();
		File ioFileTmp = null;
		EhepPlugin.log("Saving input type: " + input.getClass().getName()); //$NON-NLS-1$
		
		if (input instanceof IPathEditorInput) {
			//
			// Input file is outside the Eclipse Workspace
			//
			IPathEditorInput pathEditorInput = (IPathEditorInput) input;
			IPath path = pathEditorInput.getPath();
			ioFileTmp = path.toFile();
			EhepPlugin.log("Saving exernal file: " + ioFileTmp.getAbsolutePath()); //$NON-NLS-1$
		} 
		else {
			//
			// ERROR - Unhandled input type
			//
			EhepPlugin.log("Unhandled input type!"); //$NON-NLS-1$
		} // else

		fileManager.saveFile(this, hexEditorControl.getHexTable(), ioFileTmp, monitor, false);
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#doSaveAs()
	 */
	public void doSaveAs() {
		performSaveAs(getProgressMonitor());
	}

	/**
	 * @param progressMonitor the progress monitor to be used
	 */
	protected void performSaveAs(IProgressMonitor progressMonitor) {
		Shell shell = getSite().getShell();
		IEditorInput input = getEditorInput();
		
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		
		String stringOriginal = null;
		if (input instanceof IPathEditorInput) {
			stringOriginal = ((IPathEditorInput) input).getPath().toFile().getName();
		} 
		if (stringOriginal != null) {
			//
			// External file
			//
			dialog.setFileName(stringOriginal);
		} else {
			//
			// Shouldn't occur - ERROR
			//
			EhepPlugin.log("ERROR in performSaveAs()!"); //$NON-NLS-1$
			String message = MessageFormat.format(Messages.HexEditor_6, (Object[]) null);
			ErrorDialog errorDialog = new ErrorDialog(shell, message, message, null, ErrorDialog.CANCEL);
			errorDialog.open();
			return;
		} // else
		
		String path = dialog.open();
		if (path == null) {
			if (progressMonitor != null)
				progressMonitor.setCanceled(true);
			return;
		}
			
		IPath filePath = new Path(path);
			
		IWorkspace workspace = EhepPlugin.getWorkspace();
		IFile file = workspace.getRoot().getFile(filePath);
		final IEditorInput newInput = new PathEditorInput(filePath);
		boolean success = false;
		
		fileManager.saveFile(this, hexEditorControl.getHexTable(), filePath.toFile(), progressMonitor, true);
		try {
			file.refreshLocal(IResource.DEPTH_ONE, progressMonitor);
		} catch (CoreException e) {
			EhepPlugin.log(e);
		}

		success = true;

		if (success) {
			setInput(newInput);
		}
		
		if (progressMonitor != null) {
			progressMonitor.setCanceled(!success);
		}
	}
	
	/**
	 * Returns the progress monitor related to this editor.
	 * @return the progress monitor related to this editor
	 */
	protected IProgressMonitor getProgressMonitor() {
		IProgressMonitor pm = null;
		
		IStatusLineManager manager = getStatusLineManager();
		if (manager != null)
			pm = manager.getProgressMonitor();
			
		return pm != null ? pm : new NullProgressMonitor();
	}

	/**
	 * Returns the status line manager of this editor.
	 * @return the status line manager of this editor
	 */
	private IStatusLineManager getStatusLineManager() {
		IEditorActionBarContributor contributor= getEditorSite().getActionBarContributor();		
		if (!(contributor instanceof EditorActionBarContributor))
			return null;
			
		IActionBars actionBars= ((EditorActionBarContributor) contributor).getActionBars();
		if (actionBars == null)
			return null;
			
		return actionBars.getStatusLineManager();
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#init(IEditorSite, IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input)
		throws PartInitException {
			setSite(site);
			setInput(input);
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#isDirty()
	 */
	public boolean isDirty() {
		return isDirty;
	}

	/**
	 * Sets the "dirty" flag.
	 * @param isDirty true if the file has been modified otherwise false
	 */
	public void setDirty(boolean isDirty) {
		//
		// Set internal "dirty" flag
		//
		this.isDirty = isDirty;
		//
		// Fire the "property change" event to change file's status within Eclipse IDE
		//
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	/**
	 * Gets the file read-only flag value
	 * @return true if the file is read-only otherwise false
	 */
	public boolean isReadOnly() {
		return (ioFile != null) ? !ioFile.canWrite() : true;
	}
	
	/**
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(Composite)
	 */
	public void createPartControl(Composite parent) {
		//
		// Make an instance of the main plugin GUI control
		//
		hexEditorControl = new HexEditorControl(this, parent);

		//
		// Make an instance of the file manager
		//
		fileManager = new FileManager(parent, ioFile);
		
		//
		// Load file into the table
		//
		if (!fileManager.loadFile(hexEditorControl.getHexTable()))
			ioFile = null;
		
		//
		// Adjust the panel size with data table
		//
		hexEditorControl.getHexTable().pack();
		
		//
		// Refresh GUI, set cursor position
		//
		hexEditorControl.getHexTable().setSelection(0);
		if (hexEditorControl.getHexTable().getItemCount() > 0)
			hexEditorControl.getCursor().setSelection(0, 1);
		hexEditorControl.updateStatusPanel();
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		hexEditorControl.setFocus();
	}

	/**
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	public void setInput(IEditorInput input) {
		super.setInput(input);
		String fileName = null;
		boolean isReadOnly = false;

		EhepPlugin.log("Input type: " + input.getClass().getName()); //$NON-NLS-1$
		
		if (input instanceof IPathEditorInput) {
			//
			// Input file is outside the Eclipse Workspace
			//
			IPathEditorInput pathEditorInput = (IPathEditorInput) input;
			IPath path = pathEditorInput.getPath();
			ioFile = path.toFile();
			fileName = ioFile.getName();
			isReadOnly = !ioFile.canWrite();
			EhepPlugin.log("Opening exernal file: " + ioFile.getAbsolutePath()); //$NON-NLS-1$
		} 
		else {
			//
			// ERROR - Unhandled input type
			//
			EhepPlugin.log("Unhandled input type!"); //$NON-NLS-1$
		} // else
		
		if (fileName != null) {
			setPartName(fileName + ((isReadOnly) ? Messages.HexEditor_12: "")); //$NON-NLS-2$
		} // if
	}

	/**
	 * Called when the editor is to be disposed
	 */	
	public void dispose() {
		hexEditorControl.dispose();
		hexEditorControl = null;
		super.dispose();
		System.gc();
	}

	/**
	 * 
	 */
	public void undo() {
		hexEditorControl.undo();
		setDirty(hexEditorControl.canUndo());
	}

	/**
	 * 
	 */
	public void redo() {
		hexEditorControl.redo();
		setDirty(hexEditorControl.canUndo());
	}
	
	public HexEditorControl getControl() {
		return hexEditorControl;
	}
}

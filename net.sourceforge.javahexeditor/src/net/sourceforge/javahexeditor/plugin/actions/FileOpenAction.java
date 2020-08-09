package net.sourceforge.javahexeditor.plugin.actions;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import net.sourceforge.javahexeditor.plugin.editors.HexEditor;

public final class FileOpenAction implements IObjectActionDelegate {

	private File[] files;
	private IStructuredSelection currentSelection;

	/**
	 * Creation is public.
	 */
	public FileOpenAction() {
		super();
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	@Override
	public void run(IAction action) {

		if (!isEnabled()) {
			throw new IllegalStateException("Action is not enabled");
		}

		for (int i = 0; i < files.length; i++) {

			if (files[i] == null) {
				continue;
			}

			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(files[i].getPath()));

			if (file == null) {
				continue;
			}
			IEditorInput editorInput = new FileEditorInput(file);

			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();
			try {
				page.openEditor(editorInput, HexEditor.ID, true,
						org.eclipse.ui.IWorkbenchPage.MATCH_INPUT | org.eclipse.ui.IWorkbenchPage.MATCH_ID);

			} catch (PartInitException ex) {
				throw new RuntimeException(ex);
			}

		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		currentSelection = selection instanceof IStructuredSelection ? (IStructuredSelection) selection : null;
		action.setEnabled(isEnabled());
	}

	private boolean isEnabled() {
		boolean enabled = false;
		if (currentSelection != null) {
			Object[] selectedObjects = currentSelection.toArray();
			files = new File[selectedObjects.length];
			for (int i = 0; i < selectedObjects.length; i++) {
				File file = getResource(selectedObjects[i]);
				if (file != null && file.isFile()) {
					files[i] = file;
					enabled = true;
				}
			}
		} else {
			files = null;
		}
		return enabled;
	}

	private File getResource(Object object) {
		if (object instanceof IResource) {
			return ((IResource) object).getLocation().toFile();
		}
		if (object instanceof File) {
			return (File) object;
		}
		if (object instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) object;

			IResource resource = adaptable.getAdapter(IResource.class);
			if (resource != null) {
				return resource.getLocation().toFile();
			}
			File file = adaptable.getAdapter(File.class);
			if (file != null) {
				return file;
			}
		}
		return null;
	}

}

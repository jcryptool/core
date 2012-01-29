package net.sourceforge.javahexeditor.actions;

import net.sourceforge.javahexeditor.plugin.editors.BinaryEditor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;

public class NewFileAction implements IWorkbenchWindowActionDelegate {


	@SuppressWarnings("unused")
	private IWorkbenchWindow window;

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}


	/**
	 * creates a new window with the sample imput of JCrypTool
	 */
	public void run(IAction action) {
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			page.openEditor(AbstractEditorService.createTemporaryFile(), BinaryEditor.ID);
		} catch (Exception e) {
            LogUtil.logError(e);
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {}

	public void dispose() {}

}

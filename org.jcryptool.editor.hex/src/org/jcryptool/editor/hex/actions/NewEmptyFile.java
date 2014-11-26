package org.jcryptool.editor.hex.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.editor.hex.HexEditorConstants;

public class NewEmptyFile extends AbstractHandler {

	/**
	 * creates a new window with the sample input of JCrypTool
	 */
	public Object execute(ExecutionEvent event) {
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			page.openEditor(AbstractEditorService.createTemporaryEmptyFile(), HexEditorConstants.EditorID);
		} catch (Exception e) {
            LogUtil.logError(e);
		}
		return null;
	}
}

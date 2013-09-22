package org.jcryptool.crypto.classic.model.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.crypto.classic.model.ClassicCryptoModelPlugin;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfiguration;
import org.jcryptool.crypto.classic.model.algorithmconfig.ui.ClassicAlgorithmConfigViewer;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ClassicAlgorithmViewConfigHandler extends AbstractHandler {
	private static final String DIALOG_TITLE = Messages.ClassicAlgorithmViewConfigHandler_0;

	/**
	 * The constructor.
	 */
	public ClassicAlgorithmViewConfigHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if(EditorsManager.getInstance().isEditorOpen()) {
    		IEditorPart currentEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    		if(currentEditor != null) {
    			ClassicAlgorithmConfiguration config = ClassicAlgorithmConfiguration.getAlgorithmConfigForEditor(currentEditor);
    			
    			if(config != null) {
    				showConfiguration(currentEditor, config);
    			} else {
    				handleNoConfigToShow();
    			}
    		} else {
    			handleNoActiveEditor();
    		}
        } else {
        	handleNoEditorOpen();
        }
		return null;
	}
	
    private void showConfiguration(IEditorPart currentEditor, ClassicAlgorithmConfiguration config) {
    	try {
			Display display = Display.getDefault();
			ClassicAlgorithmConfigViewer shell = new ClassicAlgorithmConfigViewer(
					display, currentEditor, config);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception ex) {
		    LogUtil.logError(ex);
		}
	}

	private void handleNoActiveEditor() {
    	//TODO: better handling
    	String msg = Messages.ClassicAlgorithmViewConfigHandler_1;
		LogUtil.logInfo(ClassicCryptoModelPlugin.PLUGIN_ID, msg);
		MessageDialog.openInformation(new Shell(PlatformUI.getWorkbench().getDisplay()), DIALOG_TITLE, msg);
	}

	private void handleNoEditorOpen() {
    	//TODO: better handling
    	String msg = Messages.ClassicAlgorithmViewConfigHandler_2;
		LogUtil.logInfo(ClassicCryptoModelPlugin.PLUGIN_ID, msg);
		MessageDialog.openInformation(new Shell(PlatformUI.getWorkbench().getDisplay()), DIALOG_TITLE, msg);
	}

	private void handleNoConfigToShow() {
		String msg = Messages.ClassicAlgorithmViewConfigHandler_3;
		LogUtil.logInfo(ClassicCryptoModelPlugin.PLUGIN_ID, msg);
		MessageDialog.openInformation(new Shell(PlatformUI.getWorkbench().getDisplay()), DIALOG_TITLE, msg);
	}
}

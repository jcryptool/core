package net.sourceforge.javahexeditor;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.util.PathEditorInput;

/**
 * The OpenInTextAction class takes an input of an active hex editor, shuts the hex editor down and opens it with the
 * text editor.
 * 
 * @author mwalthart
 * @author Holger Friedrich (now extending AbstractHandler in order to use Commands instead of Actions)
 * @version 0.2
 */
public class OpenInTexteditor {
	
	    /** The active editor. */
	    private static IEditorPart editor;
	    /** Active workbench page. */
	    private static IWorkbenchPage page;

	    /**
	     * Sets the active editor for the delegate.
	     * 
	     * @param action the action proxy that handles presentation portion of the action
	     * @param targetEditor the new editor target
	     */
	    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
	        editor = targetEditor;
	        if (editor != null) {
	            page = editor.getSite().getPage();
	        }
	    }

	    /**
	     * Creates the editor input for the hex editor
	     * 
	     * @param absolutePath the absolute file path
	     * @return the created editor input
	     */
	    private static IEditorInput createEditorInput(String absolutePath) {
	        return new PathEditorInput(absolutePath);
	    }

	    /**
	     * Performs this action.
	     * 
	     * @param action the action proxy that handles the presentation portion of the action
	     */
	    
	    public static void changeEditor() {
	    	page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	    	editor = page.getActiveEditor();
	    	if(editor == null) {
	    		return;
	    	}
	    	
	        IPathEditorInput originalInput = (IPathEditorInput) editor.getEditorInput();
	        IEditorInput input = createEditorInput(originalInput.getPath().toString());

	        // check if text editor plug-in is loaded
	        if (Platform.getBundle(IOperationsConstants.ID_TEXT_EDITOR_PLUGIN) != null) {
	            try {
	                page.closeEditor(editor, true);
	                page.openEditor(input, IOperationsConstants.ID_TEXT_EDITOR, true);
	            } catch (PartInitException e) {
	                MessageDialog.openWarning(new Shell(Display.getCurrent()), Texts.OpenInTexteditor_warning_title,
	                        Texts.OpenInTexteditor_warning_message);
	            }
	        } else {
	            MessageDialog.openError(new Shell(Display.getCurrent()), Texts.OpenInTexteditor_error_title,
	                    Texts.OpenInTexteditor_error_Message);
	        }
	        return;
	    }
	}

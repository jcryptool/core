package org.jcryptool.actions.ui.handler;

import java.io.File;
import java.net.URL;
import java.security.InvalidParameterException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.actions.core.registry.ActionCascadeService;
import org.jcryptool.actions.core.utils.ImportUtils;
import org.jcryptool.actions.ui.ActionsUIPlugin;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * @author Anatoli Barski
 *
 */
public class ImportSampleHandler extends AbstractHandler {
	 
	String paramName = "";

    /* (non-Javadoc)
     * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	String sampleFilename = event.getParameter("sampleFilename");
    	if(sampleFilename == null)
    	{
    		throw new InvalidParameterException("command parameter sampleFilename is required");
    	}
    	
    	URL bundleUrl = null;
    	File sampleFile = null;

        try {
        	bundleUrl =
                    FileLocator.toFileURL((ActionsUIPlugin.getDefault().getBundle().getEntry("/"))); //$NON-NLS-1$
            sampleFile = new File(bundleUrl.getFile()
                    + "templates" + File.separatorChar //$NON-NLS-1$
                    + sampleFilename); //$NON-NLS-1$
            
            MessageDialog.openWarning(HandlerUtil.getActiveShell(event), "TEST", sampleFile.getAbsolutePath());
            
        } catch (Exception ex) {
            LogUtil.logError("Error loading sample file " + sampleFilename + " from plugin.", ex); //$NON-NLS-1$
        }
        
    	ActionCascadeService service = ActionCascadeService.getInstance();

    	if (service.getCurrentActionCascade() != null && service.getCurrentActionCascade().getSize()>0){
            boolean confirmImport = MessageDialog
                    .openConfirm(HandlerUtil.getActiveShell(event), Messages.ImportHandler_0,
                            Messages.ImportHandler_1);
            if (!confirmImport) {
                return null;
            }
        }
    	
    	
        String filename = sampleFile.getAbsolutePath();
        

        if (filename != null && filename.length() > 0) {
            
            ImportUtils importUtil = new ImportUtils(filename);
            boolean isValid = importUtil.validateActionCascade();

            if (isValid) {
            	service.setCurrentActionCascade(importUtil.createActionCascade());
            } else {
                MessageDialog.openInformation(HandlerUtil.getActiveShell(event), Messages.ImportHandler_2,
                Messages.ImportHandler_3);
            }
        }

        return null;
    }

}

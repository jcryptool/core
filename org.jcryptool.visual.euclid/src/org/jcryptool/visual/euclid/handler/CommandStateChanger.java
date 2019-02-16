package org.jcryptool.visual.euclid.handler;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;
import org.jcryptool.visual.euclid.handler.CommandState.State;
import org.jcryptool.visual.euclid.handler.CommandState.Variable;

/**
 * 
 * @author Thorben Groos
 *
 */
public class CommandStateChanger {
	
	public CommandStateChanger() {
		
	}
	
	public void changeCommandState(Variable variable, State state) {
		ISourceProviderService sourceProviderService = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getService(ISourceProviderService.class);
		
        CommandState commandStateService = (CommandState) sourceProviderService
        		.getSourceProvider(CommandState.variableVal(variable));
        

        switch (variable) {
		case CSV:
	        if (state == State.ENABLED) 
	        	commandStateService.setCSVExportEnabled();
	        if (state == State.DISABLED)
	        	commandStateService.setCSVExportDisabled();
			break;
		case PDF:
	        if (state == State.ENABLED) 
	        	commandStateService.setPDFExportEnabled();
	        if (state == State.DISABLED)
	        	commandStateService.setPDFExportDisabled();
			break;
		case TEX:
	        if (state == State.ENABLED) 
	        	commandStateService.setTEXExportEnabled();
	        if (state == State.DISABLED)
	        	commandStateService.setTEXExportDisabled();
			break;
		default:
			break;

		}
        
//        if (state == State.ENABLED) {
//        	commandStateService.setCSVExportEnabled();
//        	commandStateService.setPDFExportEnabled();
//        	commandStateService.setTEXExportEnabled();
//        } else if (state == State.DISABLED) {
//        	commandStateService.setCSVExportDisabled();
//        	commandStateService.setPDFExportDisabled();
//        	commandStateService.setTEXExportDisabled();
//        } else if (state == State.CSV_ENABLED) {
//        	commandStateService.setCSVExportEnabled();
//        } else if (state == State.CSV_DISABLED) {
//        	commandStateService.setCSVExportDisabled();
//        } else if (state == State.PDF_ENABLED) {
//        	commandStateService.setPDFExportEnabled();
//        } else if (state == State.PDF_DISABLED) {
//        	commandStateService.setPDFExportDisabled();
//        } else if (state == State.TEX_ENABLED) {
//        	commandStateService.setTEXExportEnabled();
//        } else if (state == State.TEX_DISABLED) {
//        	commandStateService.setTEXExportDisabled();
//        }
	}

}

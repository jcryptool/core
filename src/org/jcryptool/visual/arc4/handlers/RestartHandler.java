package org.jcryptool.visual.arc4.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.arc4.ARC4View;

/**
 * Make the connection to the restart handler and invoke the restart method of the view This is for
 * the restart button provided by JCrypTool that can be found in the tab-bar of the plug-in in the
 * upper right corner. It has nothing to do with the button that allows you to reset the step
 * counter to zero
 * 
 * @author Luca Rupp
 */
public class RestartHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof ARC4View) {
            ARC4View view = ((ARC4View) HandlerUtil.getActivePart(event));
            view.reset();
        }
        return null;
    }
}
package org.jcryptool.visual.wots.files;



import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.wots.WotsView;

public class RestartHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof WotsView) {
                WotsView view = ((WotsView) HandlerUtil.getActivePart(event));
                
                view.reset();
        }

        return null;
    }
}

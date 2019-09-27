package org.jcryptool.visual.sphincsplus;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.sphincsplus.ui.SphincsPlusView;

public class RestartCommand extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof SphincsPlusView) {
            SphincsPlusView view = ((SphincsPlusView) HandlerUtil.getActivePart(event));

            view.restartSphincsPlusView();
        }

        return null;
    }
}

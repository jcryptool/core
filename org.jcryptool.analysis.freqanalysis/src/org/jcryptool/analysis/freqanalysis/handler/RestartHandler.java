package org.jcryptool.analysis.freqanalysis.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.analysis.freqanalysis.views.FreqAnalysisView;

public class RestartHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof FreqAnalysisView) {
        	FreqAnalysisView view = ((FreqAnalysisView) HandlerUtil.getActivePart(event));
                view.resetClick();
        }

        return null;
    }
}

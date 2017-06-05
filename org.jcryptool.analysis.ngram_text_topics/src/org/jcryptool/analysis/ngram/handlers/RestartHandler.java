package org.jcryptool.analysis.ngram.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.analysis.ngram.views.NgramView;


public class RestartHandler extends AbstractHandler 
{
    public Object execute(ExecutionEvent event) throws ExecutionException 
    {
        if (HandlerUtil.getActivePart(event) instanceof NgramView) 
        {
        	NgramView view = ((NgramView) HandlerUtil.getActivePart(event));
            view.reset();
        }
        return null;
    }
}

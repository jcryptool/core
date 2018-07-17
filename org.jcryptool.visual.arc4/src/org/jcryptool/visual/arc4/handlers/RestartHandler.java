//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
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
    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof ARC4View) {
            ARC4View view = ((ARC4View) HandlerUtil.getActivePart(event));
            view.reset();
        }
        return null;
    }
}
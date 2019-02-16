//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.games.numbershark.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.numbershark.views.NumberSharkView;

/**
 * This handler provides a hint for the next step.
 * 
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class HintHandler extends AbstractHandler {
    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof NumberSharkView) {
            NumberSharkView view = ((NumberSharkView) HandlerUtil.getActivePart(event));

            String msg;
            int gameNMax = view.getNumberOfFields();

            int hint = view.getHint();
            int style = SWT.YES | SWT.NO;
            if (hint > 0) {
                msg = NLS.bind(Messages.HintHandler_0, new Object[] { hint, gameNMax / 2 });
            } else {
                msg = NLS.bind(Messages.HintHandler_1, new Object[] { gameNMax / 2 });
                style = SWT.OK;
            }

            MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION | style);
            mb.setText(Messages.HintHandler_2);
            mb.setMessage(msg);
            int answer = mb.open();
            if (answer == SWT.YES) {
                view.deactivateNumber(hint);
            }
        }

        return null;
    }

}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.numbershark.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.numbershark.views.Number;
import org.jcryptool.games.numbershark.views.NumberSharkView;

/**
 * This handler provides a hint for the next step.
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class HintHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof NumberSharkView) {
            NumberSharkView view = ((NumberSharkView) HandlerUtil.getActivePart(event));

            String msg;
            int gameNMax = view.getNumberOfFields();
            boolean[] activeNumbers = view.getActiveNumbers();
            Number[] numNum = view.getNumNum();

            int hint = calculateHint(gameNMax, activeNumbers, numNum);

            if (hint > 0) {
                msg = NLS.bind(Messages.HintHandler_0, new Object[] {hint, gameNMax / 2});
            } else {
                msg = NLS.bind(Messages.HintHandler_1, new Object[] {gameNMax / 2});
            }

            MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
            mb.setText(Messages.HintHandler_2);
            mb.setMessage(msg);
            mb.open();
        }

        return null;
    }

    /**
     * Calculate number for the hint.
     *
     * @param gameNMax
     * @param activeNumbers
     * @param numNum
     * @return
     */
    private int calculateHint(int gameNMax, boolean[] activeNumbers, Number[] numNum) {
        for (int i = gameNMax; i > gameNMax / 2; i--) {
            if (activeNumbers[i - 1]) {
                int counter = 0;

                for (int j = 0; j < numNum[i - 1].getDivisors().size(); j++) {
                    int n = numNum[i - 1].getDivisors().get(j);
                    if (activeNumbers[n - 1]) {
                        counter++;
                    }

                }
                if (counter == 1) {
                    return i;
                }

            }
        }

        return 0;
    }
}

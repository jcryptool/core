// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.verifiablesecretsharing.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.visual.verifiablesecretsharing.views.VerifiableSecretSharingView;

/**
 * This handler starts a new game.
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class RestartHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof VerifiableSecretSharingView) {
                VerifiableSecretSharingView view = ((VerifiableSecretSharingView) HandlerUtil.getActivePart(event));
                
                view.restartVerifiableSecretSharing();
        }

        return null;
    }
}

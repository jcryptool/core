/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.wst.xml.security.core.verify.VerificationResult;
import org.eclipse.wst.xml.security.ui.verify.Verification;

/**
 * <p>Shows additional properties in a popup dialog of the selected XML Signature
 * in the XML Signatures view.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class ShowPropertiesCommand extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Object o = ((IStructuredSelection) HandlerUtil.getCurrentSelection(event)).getFirstElement();

        if (o instanceof VerificationResult) {
            Verification.showVerificationResult((VerificationResult) o);
        }

        return null;
    }
}

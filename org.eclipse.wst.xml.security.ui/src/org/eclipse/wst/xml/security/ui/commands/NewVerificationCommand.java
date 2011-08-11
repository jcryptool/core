/*******************************************************************************
 * Copyright (c) 2011 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.commands;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.xml.security.core.verify.VerificationResult;
import org.eclipse.wst.xml.security.core.verify.VerifyDocument;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.verify.SignatureView;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithmAction;
import org.jcryptool.core.operations.dataobject.IDataObject;

/**
 * <p>
 * Command used to show the <b>XML Signatures</b> view of the XML Security Tools to verify all XML Signatures contained
 * in the selected XML document.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class NewVerificationCommand extends AbstractAlgorithmAction {
    /** The data to sign. */
    private InputStream data = null;

    private void createVerification() {
        VerifyDocument verify = new VerifyDocument();
        ArrayList<VerificationResult> results = new ArrayList<VerificationResult>();

        try {
            data = getActiveEditorInputStream();

            results = verify.verify(data);

            if (results.isEmpty()) {
                MessageDialog.openInformation(getActiveWorkbenchWindow().getShell(), Messages.NewVerificationCommand_0,
                        Messages.NewVerificationCommand_1);
            }

            // show results
            IViewPart vp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView(SignatureView.ID);

            if (vp instanceof SignatureView) {
                ((SignatureView) vp).setInput(results);
            }
        } catch (XMLSignatureException ex) {
            LogUtil.logError(XSTUIPlugin.getId(), Messages.NewVerificationCommand_2, ex, true);
        } catch (KeyResolverException ex) {
            LogUtil.logError(XSTUIPlugin.getId(), Messages.NewVerificationCommand_3, ex, true);
        } catch (Exception ex) {
            LogUtil.logError(XSTUIPlugin.getId(), Messages.NewVerificationCommand_4, ex, true);
        }
    }

    @Override
    public void run() {
        run(null);
    }

    @Override
    public void run(IDataObject dataobject) {
        createVerification();
    }
}

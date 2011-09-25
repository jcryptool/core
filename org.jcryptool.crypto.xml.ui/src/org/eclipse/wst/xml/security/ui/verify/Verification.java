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
package org.eclipse.wst.xml.security.ui.verify;

import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.resolver.implementations.ResolverFragment;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.xml.security.core.verify.VerificationResult;
import org.eclipse.wst.xml.security.ui.dialogs.VerificationDialog;

/**
 * <p>Prepares and shows the verification dialog.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public final class Verification {
    /**
     * Private constructor to avoid instantiation.
     */
    private Verification() {
    }

    /**
     * Shows a dialog with the verification result. The displayed image depends on the
     * verification result.
     *
     * @param result VerificationResult object
     */
    public static void showVerificationResult(final VerificationResult result) {
        XMLSignature signature = result.getSignature();

        ResolverFragment fragmentResolver = new ResolverFragment();

        if (signature != null) {
            signature.addResourceResolver(fragmentResolver);

            try {
                X509Certificate cert = signature.getKeyInfo().getX509Certificate();
                PublicKey pk = signature.getKeyInfo().getPublicKey();
                String additionalInfo = ""; //$NON-NLS-1$

                if (cert != null) {
                    additionalInfo = "Certificate Type: " + cert.getType(); //$NON-NLS-1$
                    additionalInfo += ", Certificate Version: " + cert.getVersion(); //$NON-NLS-1$
                    additionalInfo += ", Certificate Algorithm: " + cert.getSigAlgName(); //$NON-NLS-1$
                    additionalInfo += ", Certificate Algorithm ID: " + cert.getSigAlgOID(); //$NON-NLS-1$
                } else if (pk != null) {
                    additionalInfo = "Public Key Format: " + pk.getFormat(); //$NON-NLS-1$
                    additionalInfo += ", Public Key Algorithm: " + pk.getAlgorithm(); //$NON-NLS-1$
                }

                if (VerificationResult.VALID.equals(result.getStatus())) { //$NON-NLS-1$
                    showVerificationDialog(result.getStatus(), NLS.bind(Messages.validSignature,
                            new Object[] {result.getId(), additionalInfo}),
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
                } else if (VerificationResult.INVALID.equals(result.getStatus())) { //$NON-NLS-1$
                    showVerificationDialog(result.getStatus(), NLS.bind(Messages.invalidSignature,
                            new Object[] {result.getId(), additionalInfo}),
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
                } else {
                    showVerificationDialog(result.getStatus(), NLS.bind(Messages.unknownSignature,
                            new Object[] {result.getId(), additionalInfo}),
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
                }
            } catch (KeyResolverException ex) {
                MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        Messages.verificationImpossible, Messages.impossibleToGetKeyInformation);
            }
        } else {
            showVerificationDialog(result.getStatus(), NLS.bind(Messages.unknownSignature,
                    new Object[] {result.getId(), ""}), //$NON-NLS-1$
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        }
    }

    /**
     * Shows an information popup window with a message.
     *
     * @param status The status of the signature
     * @param message The message
     * @param shell The parent shell
     */
    protected static void showVerificationDialog(final String status, final String message, final Shell shell) {
        VerificationDialog dialog = new VerificationDialog(shell, Messages.title, message, status);
        dialog.open();
    }
}

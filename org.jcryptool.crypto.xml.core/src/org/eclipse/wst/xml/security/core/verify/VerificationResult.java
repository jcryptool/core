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
package org.eclipse.wst.xml.security.core.verify;

import org.apache.xml.security.signature.XMLSignature;
import org.eclipse.osgi.util.NLS;

/**
 * <p>The verification result container stores the result of the signature verifications.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class VerificationResult {
    /** Identifier for a signature with invalid status. */
    public static final String INVALID = "invalid"; //$NON-NLS-1$
    /** Identifier for a signature with valid status. */
    public static final String VALID = "valid"; //$NON-NLS-1$
    /** Identifier for a signature with unknown status. */
    public static final String UNKNOWN = "unknown"; //$NON-NLS-1$
    /** The signature id. */
    private String signatureId;
    /** The signature status. */
    private String signatureStatus;
    /** The certificate type. */
    private String certificateType;
    /** The certificate algorithm. */
    private String certificateAlgorithm;
    /** The XML signature. */
    private XMLSignature signature;

    /**
     * VerificationResult Constructor.
     *
     * @param status The signature status file
     * @param id The signature id
     * @param type The certificate type
     * @param algorithm The certificate algorithm
     * @param xmlSignature The XML signature
     */
    public VerificationResult(final String status, final String id, final String type,
        final String algorithm, final XMLSignature xmlSignature) {
        signatureStatus = status;
        signatureId = id;
        certificateType = type;
        certificateAlgorithm = algorithm;
        signature = xmlSignature;
    }

    /**
     * Returns the signature id.
     *
     * @return The signature id
     */
    public String getId() {
        return signatureId;
    }

    /**
     * Returns the signature status.
     *
     * @return The signature status
     */
    public String getStatus() {
        return signatureStatus;
    }

    /**
     * Returns the XML signature.
     *
     * @return The XML signature
     */
    public XMLSignature getSignature() {
        return signature;
    }

    /**
     * Returns the certificate algorithm.
     *
     * @return The certificate algorithm
     */
    public String getAlgorithm() {
        return certificateAlgorithm;
    }

    /**
     * Returns the certificate type.
     *
     * @return The certificate type
     */
    public String getType() {
        return certificateType;
    }

    /**
     * Converts a VerificationResult item into a readable String format.
     *
     * @return A readable version of the VerificationResult
     */
    public String resultToReadableString() {
        String resultString = NLS.bind(Messages.verificationResult,
                new Object[] {getId(), getStatus(), getType(), getAlgorithm()});

        return resultString;
    }
}

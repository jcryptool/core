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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.eclipse.wst.xml.security.core.utils.IGlobals;
import org.eclipse.wst.xml.security.core.utils.SignatureNamespaceContext;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <p>Verifies exactly one XML Signature with the included <code>KeyInfo</code> element or
 * <i>public key</i> identified by the unique signature id. The verification result is returned as a
 * VerificationResult object, which contains all available information about this signature (like
 * type and algorithm) and its status.</p>
 *
 * <p>A <code>KeyInfo</code> element is always required to verify a signature with the XML Security Tools.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class VerifySignature {
    /**
     * Searches for the signature with the given id in the XML document and verifies this signature.
     * An exception returns an unknown signature state, as well as a signature id which doesn't
     * exist in the XML document.
     *
     * @param signatureFileName Path and filename of the XML document
     * @param signatureId The id of the signature to verify
     * @return Result of verification
     * @throws Exception to indicate any exceptional condition
     */
    public ArrayList<VerificationResult> verify(final String signatureFileName, final String signatureId)
        throws Exception {
        ArrayList<VerificationResult> signatures = new ArrayList<VerificationResult>();
        XMLSignature signature = null;
        KeyInfo keyInfo = null;
        boolean schemaValidate = false;
        final String signatureSchemaFile = "data/xmldsig-core-schema.xsd"; //$NON-NLS-1$
        File file = new File(signatureFileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        if (schemaValidate) {
            dbf.setAttribute(IGlobals.SCHEMA, Boolean.TRUE);
            dbf.setAttribute(IGlobals.DOM, Boolean.TRUE);
            dbf.setValidating(true);
            dbf.setAttribute(IGlobals.SAX, Boolean.TRUE);
        }

        dbf.setNamespaceAware(true);
        dbf.setAttribute(IGlobals.SAX_NAMESPACES, Boolean.TRUE);

        if (schemaValidate) {
            dbf.setAttribute(IGlobals.EXTERNAL_SCHEMA_LOC,
                    org.apache.xml.security.utils.Constants.SignatureSpecNS + " " //$NON-NLS-1$
                            + signatureSchemaFile);
        }

        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(new org.apache.xml.security.utils.IgnoreAllErrorHandler());

        if (schemaValidate) {
            db.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(final String publicId, final String systemId)
                    throws SAXException {
                    if (systemId.endsWith("xmldsig-core-schema.xsd")) { //$NON-NLS-1$
                        try {
                            return new InputSource(new FileInputStream(signatureSchemaFile));
                        } catch (FileNotFoundException ex) {
                            throw new SAXException(ex);
                        }
                    }
                    return null;
                }
            });
        }
        XPath xpath = XPathFactory.newInstance().newXPath();
        NamespaceContext ns = new SignatureNamespaceContext();
        InputSource inputSource = new InputSource(new java.io.FileInputStream(file));
        xpath.setNamespaceContext(ns);
        Element signatureElement = (Element) xpath.evaluate("//ds:Signature[@Id='" + signatureId + "']", inputSource, XPathConstants.NODE);
        String status = "";
        String type = "", algorithm = ""; //$NON-NLS-1$ //$NON-NLS-2$

        try {
            if (signatureElement != null) { // document contains Signature element
                signature = new XMLSignature(signatureElement, file.toURI().toString());
                keyInfo = signature.getKeyInfo();

                if (keyInfo != null) { // signature contains KeyInfo element
                    X509Certificate cert = keyInfo.getX509Certificate();
                    PublicKey pk = keyInfo.getPublicKey();

                    if (cert != null) { // X509 certificate
                        type = cert.getType();
                        algorithm = cert.getSigAlgName();
                        if (signature.checkSignatureValue(cert)) {
                            status = VerificationResult.VALID;
                        } else {
                            status = VerificationResult.INVALID;
                        }
                    } else if (pk != null) { // public key
                        type = ""; //$NON-NLS-1$
                        algorithm = pk.getAlgorithm();
                        if (signature.checkSignatureValue(pk)) {
                            status = VerificationResult.VALID;
                        } else {
                            status = VerificationResult.INVALID;
                        }
                    } else { // neither certificate nor public key
                        status = VerificationResult.UNKNOWN;
                    }
                } else { // no KeyInfo element
                    status = VerificationResult.UNKNOWN;
                }
                signatures.add(new VerificationResult(status, signatureId, type, algorithm,
                        signature));
            } else {
                signatures.add(new VerificationResult(VerificationResult.UNKNOWN, signatureId, type, algorithm,
                        signature));
            }
        } catch (Exception ex) {
            signatures.add(new VerificationResult(VerificationResult.UNKNOWN, signatureId, type, algorithm, signature));
            Utils.logError(ex, "Error during single signature verification"); //$NON-NLS-1$
        }

        return signatures;
    }
}

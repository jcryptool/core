/*******************************************************************************
 * Copyright (c) 2008 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.utils;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * <p>XML Signature namespace implementation.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class SignatureNamespaceContext implements NamespaceContext {
    /**
     * Returns the namespace URI for the given prefix.
     *
     * @param prefix The namespace prefix
     * @return The namespace URI
     */
    public String getNamespaceURI(final String prefix) {
        if (prefix == null) {
            throw new NullPointerException("Null prefix");
        } else if ("ds".equals(prefix)) {
            return "http://www.w3.org/2000/09/xmldsig#";
        } else if ("xml".equals(prefix)) {
            return XMLConstants.XML_NS_URI;
        }
        return XMLConstants.NULL_NS_URI;

    }

    /**
     * This method isn't necessary for XPath processing.
     *
     * @param uri The namespace URI
     * @return The namespace prefix
     */
    public String getPrefix(final String uri) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method isn't necessary for XPath processing.
     *
     * @param uri The namespace URI
     * @return The namespace prefixes
     */
    public Iterator<?> getPrefixes(final String uri) {
        throw new UnsupportedOperationException();
    }
}

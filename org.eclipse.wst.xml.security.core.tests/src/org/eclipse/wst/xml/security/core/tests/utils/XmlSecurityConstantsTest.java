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
package org.eclipse.wst.xml.security.core.tests.utils;

import junit.framework.TestCase;

import org.eclipse.wst.xml.security.core.utils.XmlSecurityConstants;

/**
 * <p>JUnit tests for the XmlSecurityConstants class.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class XmlSecurityConstantsTest extends TestCase {
    /**
     * Test for the getSignatureAlgorithm method to get the default name for a selected signature
     * algorithm.
     */
    public void testGetSignatureAlgorithm() {
        assertEquals("http://www.w3.org/2000/09/xmldsig#dsa-sha1", XmlSecurityConstants
                .getSignatureAlgorithm("DSA with SHA 1 (DSS)"));
        assertNull(null, XmlSecurityConstants.getSignatureAlgorithm("dummy"));
    }

    /**
     * Test for the getMessageDigestAlgorithm method to get the default name for a selected message
     * digest algorithm.
     */
    public void testGetMessageDigestAlgorithm() {
        assertEquals("http://www.w3.org/2001/04/xmlenc#sha256", XmlSecurityConstants
                .getMessageDigestAlgorithm("SHA 256"));
        assertNull(null, XmlSecurityConstants.getMessageDigestAlgorithm("dummy"));
    }

    /**
     * Test for the getCanonicalizationAlgorithm method to get the default name for a selected
     * canonicalization algorithm.
     */
    public void testGetCanonicalizationAlgorithm() {
        assertEquals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments", XmlSecurityConstants
                .getCanonicalizationAlgorithm("Exclusive with comments"));
        assertNull(null, XmlSecurityConstants.getCanonicalizationAlgorithm("dummy"));
    }

    /**
     * Test for the getTransformationAlgorithm method to get the default name for a selected
     * transformation algorithm.
     */
    public void testGetTransformationAlgorithm() {
        assertEquals("http://www.w3.org/2002/06/xmldsig-filter2", XmlSecurityConstants
                .getTransformationAlgorithm("XPath 2 Filter"));
        assertNull(null, XmlSecurityConstants.getTransformationAlgorithm("dummy"));
    }

    /**
     * Test for the getEncryptionAlgorithm method to get the default name for a selected encryption
     * algorithm.
     */
    public void testGetEncryptionAlgorithm() {
        assertEquals("http://www.w3.org/2001/04/xmlenc#aes256-cbc", XmlSecurityConstants
                .getEncryptionAlgorithm("AES 256"));
        assertNull(null, XmlSecurityConstants.getEncryptionAlgorithm("dummy"));
    }

    /**
     * Test for the getKeyCipherAlgorithm method to get the default name for a selected key cipher
     * algorithm.
     */
    public void testGetKeyCipherAlgorithm() {
        assertEquals("http://www.w3.org/2001/04/xmlenc#kw-tripledes", XmlSecurityConstants
                .getKeyCipherAlgorithm("Triple DES Key Wrap"));
        assertNull(null, XmlSecurityConstants.getKeyCipherAlgorithm("dummy"));
    }

    /**
     * Test for the getKeyAlgoritm method to get the default name for a selected key algorithm.
     */
    public void testGetKeyAlgoritm() {
        assertEquals("DESede", XmlSecurityConstants.getKeyAlgoritm("Triple DES"));
        assertNull(null, XmlSecurityConstants.getKeyAlgoritm("dummy"));
    }
}

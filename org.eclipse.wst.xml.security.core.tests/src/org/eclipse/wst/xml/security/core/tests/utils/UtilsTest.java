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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.wst.xml.security.core.tests.XMLSecurityToolsCoreTestPlugin;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.w3c.dom.Document;

/**
 * <p>JUnit tests for the Utils class.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class UtilsTest extends TestCase {
    /** The XML document. */
    private Document xml = null;
    /** The ID XML document. */
    private Document idXml = null;
    /** The selection in the XML document. */
    private Document selection = null;

    /**
     * Set up method. Sets up the XML document used in these test cases.
     *
     * @throws Exception
     */
    public void setUp() throws Exception {
        xml = Utils.parse(new File(XMLSecurityToolsCoreTestPlugin.getTestFileLocation("resources/FirstSteps.xml")));
        idXml = Utils.parse(new File(XMLSecurityToolsCoreTestPlugin.getTestFileLocation("resources/ids.xml")));
    }

    /**
     * Test for the XPath method to determine the XPath to a selected element and to a selected
     * element content.
     *
     * @throws Exception
     */
    public void testGetUniqueXPathToNode() throws Exception {
        selection = Utils.parse("<Quantity>1</Quantity>");
        assertEquals("Invoice/InvoiceLine[2]/Quantity[1]", Utils.getUniqueXPathToNode(xml, selection));
        selection = Utils.parse("<PaymentMeans><PayeeFinancialAccount><ID>07044961</ID>"
                + "<Name>The Specialists Company</Name><AccountTypeCode>Credit</AccountTypeCode>"
                + "<FinancialInstitutionBranch><ID>776631</ID><Institution>LOYDGB852</Institution>"
                + "</FinancialInstitutionBranch></PayeeFinancialAccount></PaymentMeans>");
        assertEquals("Invoice/PaymentMeans[1]", Utils.getUniqueXPathToNode(xml, selection));
        selection = Utils.parse("<xmlsectempelement>458746</xmlsectempelement>");
        assertEquals("Invoice/BuyerParty[1]/ID[1]/text()", Utils.getUniqueXPathToNode(xml,
                selection));
    }

    /**
     * Test for the ID method to determine all IDs in the XML document based on the type signature
     * or encryption.
     */
    public void testGetIds() throws Exception {
        InputStream idStream = new FileInputStream(new File(XMLSecurityToolsCoreTestPlugin.getTestFileLocation("resources/ids.xml")));
        String[] idsEncryption = {"Use first encryption id", "myEncryptionA", "myEncryptionB"};
        assertEquals(idsEncryption.length, Utils.getIds(idStream, "encryption").length);
        idStream = new FileInputStream(new File(XMLSecurityToolsCoreTestPlugin.getTestFileLocation("resources/ids.xml")));
        String[] idsSignature = {"Use first signature id", "mySignature"};
        assertEquals(idsSignature.length, Utils.getIds(idStream, "signature").length);
    }

    /**
     * Test for the ID method to determine all IDs in the XML document.
     */
    public void testGetAllIds() throws Exception {
        String[] ids = {"myEncryptionA", "myEncryptionB", "mySignature"};
        assertEquals(ids.length, Utils.getAllIds(idXml).length);
    }

    /**
     * Test for the XPath validation method to validate XPath expressions entered by the user.
     */
    public void testValidateXPath() {
        assertEquals("attribute", Utils.validateXPath(xml, "Invoice/TotalAmount/@currencyID"));
        assertEquals("single", Utils.validateXPath(xml, "Invoice/InvoiceLine[1]/Item/BasePrice[@currencyID='EUR']"));
        assertEquals("multiple", Utils.validateXPath(xml, "Invoice/InvoiceLine"));
        assertEquals("none", Utils.validateXPath(xml, "Invoice/Line"));
    }

    /**
     * Test for signature and encryption id validation.
     */
    public void testValidateId() {
        assertEquals(true, Utils.validateId("valid"));
        assertEquals(false, Utils.validateId("aa<324 "));
        assertEquals(false, Utils.validateId(">"));
        assertEquals(false, Utils.validateId("\""));
        assertEquals(false, Utils.validateId("'"));
        assertEquals(false, Utils.validateId("&"));
        assertEquals(false, Utils.validateId(" "));
        assertEquals(true, Utils.validateId("äöüß$%/()"));
    }

    /**
     * Test for id uniqueness.
     */
    public void testCheckIdUniqueness() {
        String[] ids = {"myID", "myid", "aaa", "bbb", "x", "öäü"};
        assertEquals(true, Utils.ensureIdIsUnique("aa", ids));
        assertEquals(true, Utils.ensureIdIsUnique("mySignatureID", ids));
        assertEquals(true, Utils.ensureIdIsUnique("äöüß", ids));
        assertEquals(true, Utils.ensureIdIsUnique("myId", ids));
        assertEquals(false, Utils.ensureIdIsUnique("myid", ids));
        assertEquals(false, Utils.ensureIdIsUnique("öäü", ids));
        assertEquals(false, Utils.ensureIdIsUnique("x", ids));
        assertEquals(false, Utils.ensureIdIsUnique("bbb", ids));
    }
}

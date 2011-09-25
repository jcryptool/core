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
package org.eclipse.wst.xml.security.core.tests.sign;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.xml.security.utils.XMLUtils;
import org.eclipse.wst.xml.security.core.cryptography.Keystore;
import org.eclipse.wst.xml.security.core.sign.CreateSignature;
import org.eclipse.wst.xml.security.core.sign.Signature;
import org.eclipse.wst.xml.security.core.tests.XMLSecurityToolsCoreTestPlugin;
import org.eclipse.wst.xml.security.core.utils.IGlobals;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.eclipse.wst.xml.security.core.verify.VerificationResult;
import org.eclipse.wst.xml.security.core.verify.VerifySignature;
import org.w3c.dom.Document;

/**
 * <p>JUnit test class for {@link org.eclipse.wst.xml.security.core.sign.CreateSignature}.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class CreateSignatureTest extends TestCase {
    /** The signature model used in these tests. */
    private Signature signature = null;
    /** The signature id. */
    private static final String SIGNATURE_ID = "JUnitTest";
    /** The temporary file containing the signature. */
    private static final String SIGNED_FILE_NAME = "result.xml";
    private static final String KEYSTORE_PATH = "resources/sample_keystore.jks";
    private static final String KEYSTORE_PASSWORD = "sampleKeystore";
    private static final String KEY_ALIAS = "sampleKey";
    private static final String KEY_PASSWORD = "sampleKey";

    /**
     * Sets up the Apache XML Security API and prepares the SignatureWizard object.
     *
     * @throws Exception to indicate any exceptional condition
     */
    protected void setUp() throws Exception {
        org.apache.xml.security.Init.init();

        Keystore sampleKeyStore = new Keystore(XMLSecurityToolsCoreTestPlugin.getTestFileLocation(KEYSTORE_PATH), KEYSTORE_PASSWORD,
                IGlobals.KEYSTORE_TYPE);
        sampleKeyStore.load();

        signature = new Signature();
        signature.setBsp(false);
        signature.setLaunchEncryptionWizard(false);
        signature.setCanonicalizationAlgorithm("Exclusive with comments");
        signature.setKeyName(KEY_ALIAS);
        signature.setKeyPassword(KEY_PASSWORD.toCharArray());
        signature.setDetachedFile(null);
        signature.setDocument(Utils.parse(new File(XMLSecurityToolsCoreTestPlugin.getTestFileLocation("resources/FirstSteps.xml"))));
        signature.setKeystore(sampleKeyStore);
        signature.setKeystorePassword(KEYSTORE_PASSWORD.toCharArray());
        signature.setMessageDigestAlgorithm("SHA 1");
        signature.setResource("document");
        signature.setSignatureAlgorithm("DSA with SHA 1 (DSS)");
        signature.setSignatureId(SIGNATURE_ID);
        signature.setSignatureProperties(null);
        signature.setSignatureType("enveloped");
        signature.setTransformationAlgorithm("None");
        signature.setXpath(null);
    }

    /**
     * Kills the SignatureWizard object and deletes the signed XML document.
     *
     * @throws Exception to indicate any exceptional condition
     */
    public void tearDown() throws Exception {
        signature = null;

        try {
            File file = new File(XMLSecurityToolsCoreTestPlugin.getTestFileLocation(SIGNED_FILE_NAME));
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ex) {
            ;
        }
    }

    /**
     * Test method for
     * {@link org.eclipse.wst.xml.security.core.sign.CreateSignature#sign(org.eclipse.wst.xml.security.core.sign.Signature, org.eclipse.jface.text.ITextSelection)}
     *
     */
    public void testSign() {
        CreateSignature sign = new CreateSignature();
        VerifySignature verify = new VerifySignature();
        Document result = null;
        try {
            result = sign.sign(signature, null, null);

            String signedFilename = XMLSecurityToolsCoreTestPlugin.getTestFileLocation(SIGNED_FILE_NAME);

            FileOutputStream fos = new FileOutputStream(signedFilename);
            if (result != null) {
                XMLUtils.outputDOM(result, fos);
            }
            fos.flush();
            fos.close();

            ArrayList<VerificationResult> signatures = verify
                    .verify(signedFilename, SIGNATURE_ID);
            assertEquals("valid", (signatures.get(0)).getStatus());

            signatures = verify.verify(signedFilename, "wrongID");
            assertEquals("unknown", (signatures.get(0)).getStatus());
        } catch (Exception ex) {
            fail(ex.getLocalizedMessage());
        }
    }
}

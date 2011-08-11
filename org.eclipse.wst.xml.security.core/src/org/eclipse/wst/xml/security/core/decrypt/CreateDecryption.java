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
package org.eclipse.wst.xml.security.core.decrypt;

import java.io.File;
import java.net.URL;

import javax.crypto.SecretKey;

import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.utils.EncryptionConstants;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.xml.security.core.cryptography.Keystore;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.jcryptool.core.operations.algorithm.modern.AbstractModernAlgorithm;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>Decrypts the XML document (fragment) based on the user settings in the XML Decryption Wizard or
 * stored in the preferences (Quick Decryption). The correct key file used for encryption, the used
 * key file algorithm and the encryption id are needed.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class CreateDecryption extends AbstractModernAlgorithm {
    /** The encrypted XML file. */
    private File xmlDocument = null;
    /** The secret key used for encryption. */
    private Keystore keystore = null;
    /** The encryption id. */
    private String encryptionId = null;
    /** The keys name. */
    private String keyName = "";
    /** The encryption algorithm name used in the EncryptionMethod element. */
    private String algorithm = "";

    /**
     * <p>Decrypts the selected document based on the settings in the <i>XML Decryption wizard</i>.</p>
     *
     * <p>Finally the encrypted data in the XML document is replaced with the decrypted content and the
     * decrypted XML document is returned to the action class. If the <i>SecretKey</i> is invalid or
     * an error occurs, the encrypted XML document will be returned.</p>
     *
     * @param decryption Decryption object with all the settings from the wizard
     * @param monitor Progress monitor indicating the decryption progress
     * @return The decrypted XML document
     * @throws Exception to indicate any exceptional condition
     */
    public Document decrypt(final Decryption decryption, IProgressMonitor monitor) throws Exception {
        loadSettings(decryption);

        Document doc = Utils.parse(xmlDocument);

        monitor.worked(1);

        NodeList encryptedElements = doc.getElementsByTagNameNS(
                EncryptionConstants.EncryptionSpecNS, EncryptionConstants._TAG_ENCRYPTEDDATA);

        int position = 0;

        monitor.worked(1);

        if (encryptionId != null && !encryptionId.equals("Use first encryption")) {
            // look for the EncryptedData element with the given Id attribute
            for (int i = 0, length = encryptedElements.getLength(); i < length; i++) {
            	Element current = (Element) encryptedElements.item(i);
            	String id = current.getAttribute(EncryptionConstants._ATT_ID);
                if (id != null && id.equals(encryptionId)) {
                    position = i;
                    break;
                }
            }
        }

        monitor.worked(1);

        Element encryptedElement = (Element) encryptedElements.item(position);
        if (encryptedElement == null) {
        	throw new Exception("No XML element EncryptedData was found in the selected XML document");
        }

        monitor.worked(1);

        keystore.load();

        SecretKey key = keystore.getSecretKey(decryption.getKeyName(), decryption.getKeyPassword());

        if (key == null) {
        	throw new Exception("Key " + keyName + " not found in keystore " + keystore);
        }

        monitor.worked(1);

        XMLCipher xmlCipher = XMLCipher.getInstance();
        xmlCipher.init(XMLCipher.DECRYPT_MODE, key);

        boolean envelopingEncryption = true;

        NodeList references = encryptedElement.getElementsByTagNameNS(
                EncryptionConstants.EncryptionSpecNS, EncryptionConstants._TAG_CIPHERREFERENCE);
        if (references.getLength() > 0) {
            envelopingEncryption = false;
        }

        monitor.worked(1);

        algorithm = xmlCipher.getEncryptedData().getEncryptionMethod().getAlgorithm();

        if (envelopingEncryption) {
            xmlCipher.doFinal(doc, encryptedElement);
        } else {
            if (references.getLength() == 1) {
                Node reference = references.item(0);
                Node uri = reference.getAttributes().getNamedItem(EncryptionConstants._ATT_URI);
                String detachedDocument = uri.getNodeValue();

                if (!detachedDocument.equals("")) {
                    URL url = new URL(detachedDocument);
                    File detachedFile = new File(url.getFile());

                    if (detachedFile.exists()) {
                        Document detachedDoc = Utils.parse(detachedFile);
                        EncryptedData ed = xmlCipher.loadEncryptedData(detachedDoc, detachedDoc
                                .getDocumentElement());
                        encryptedElement = xmlCipher.martial(doc, ed);
                        xmlCipher.doFinal(doc, encryptedElement);
                    }
                }
            }
        }

        monitor.worked(1);

        return doc;
    }

    /**
     * Loads the settings from the <i>XML Decryption Wizard</i> out of the
     * <code>Decryption</code> object into different member variables.
     *
     * @param decryption Contains all user settings
     * @throws Exception to indicate any exceptional condition
     */
    private void loadSettings(final Decryption decryption) throws Exception {
        xmlDocument = new File(decryption.getFile());
        keystore = decryption.getKeystore();
        encryptionId = decryption.getEncryptionId();
        keyName = decryption.getKeyName();
    }

    @Override
    public IDataObject getDataObject() {
        return null;
    }

    @Override
    public IDataObject execute() {
        return null;
    }

    @Override
    public String getAlgorithmName() {
        return algorithm;
    }
}

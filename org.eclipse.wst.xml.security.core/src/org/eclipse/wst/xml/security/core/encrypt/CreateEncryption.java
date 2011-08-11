/*******************************************************************************
 * Copyright (c) 2011 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.encrypt;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;

import javax.crypto.SecretKey;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.xml.security.encryption.CipherData;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.EncryptionMethod;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.utils.EncryptionConstants;
import org.apache.xml.security.utils.XMLUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.xml.security.core.cryptography.Keystore;
import org.eclipse.wst.xml.security.core.utils.SignatureNamespaceContext;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.eclipse.wst.xml.security.core.utils.XmlSecurityConstants;
import org.jcryptool.core.operations.algorithm.modern.AbstractModernAlgorithm;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <p>
 * Encrypts the XML document (fragment) based on the user settings in the <i>XML Encryption
 * Wizard</i>.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class CreateEncryption extends AbstractModernAlgorithm {
    /** The XML file to encrypt. */
    private Document doc = null;
    /** The detached XML file to encrypt. */
    private File detachedFile = null;
    /** The detached XML document to encrypt. */
    private Document detachedDoc = null;
    /** True encrypts only the element content, false encrypts everything (default). */
    private boolean encryptContentOnly;
    /** The encryption type. */
    private String encryptionType = null;
    /** The Java Keystore. */
    private Keystore keystore = null;
    /** The resource to encrypt. */
    private String resource = null;
    /** XPath to encrypt (can be null). */
    private String expression = null;
    /** Optional encryption id. */
    private String encryptionId = null;
    /** The encryption algorithm name used in the EncryptionMethod element. */
    private String algorithm = null;
    /** The key cipher algorithm. */
    private String keyCipherAlgorithm = null;
    /** The name of the key. */
    private String keyName = "";

    /**
     * Encrypts the document selected in an Eclipse view (like navigator or package explorer) or in
     * an opened editor based on the chosen settings in the <i>XML Encryption Wizard</i> or stored
     * in the preferences.
     *
     * @param encryption Encryption object with all the settings from the wizard
     * @param selection The selected text in the editor
     * @param monitor Progress monitor indicating the encryption progress
     * @return Document The XML document containing the encryption
     * @throws Exception to indicate any exceptional condition
     */
    public Document encrypt(Encryption encryption, String selection, IProgressMonitor monitor)
            throws Exception {
        Document encrypted = null;

        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }

        monitor.worked(1);

        loadSettings(encryption, selection);
        keystore.load();

        SecretKey key = keystore.getSecretKey(encryption.getKeyName(), encryption.getKeyPassword());

        if (key == null) {
            throw new Exception("Key " + keyName + " not found in keystore " + keystore);
        }

        monitor.worked(1);

        if (encryptionType.equalsIgnoreCase("enveloping")) {
            encrypted = envelopingEncryption(selection, key);
        } else if (encryptionType.equalsIgnoreCase("detached")) {
            encrypted = detachedEncryption(key);
        }

        monitor.worked(1);

        return encrypted;
    }

    /**
     * Loads the settings from the <i>XML Encryption Wizard</i> out of the
     * <code>EncryptionWizard</code> object into different member variables.
     *
     * @param encryption Contains all user settings
     * @param selection A possibly existing text selection
     * @throws Exception to indicate any exceptional condition
     */
    private void loadSettings(Encryption encryption, String selection) throws Exception {
        doc = encryption.getDocument();
        encryptionType = encryption.getEncryptionType();
        encryptContentOnly = encryption.getContent();
        keystore = encryption.getKeystore();
        keyName = encryption.getKeyName();
        resource = encryption.getResource();
        expression = encryption.getXpath();
        detachedFile = encryption.getFileDetached();

        if (detachedFile != null) {
            detachedDoc = Utils.parse(detachedFile);
        }

        if (null != encryption.getEncryptionId()) {
            encryptionId = encryption.getEncryptionId();
        }

        // get the constant names for all algorithms
        algorithm =
                XmlSecurityConstants.getEncryptionAlgorithm(encryption.getEncryptionAlgorithm());
        keyCipherAlgorithm =
                XmlSecurityConstants.getKeyCipherAlgorithm(encryption.getKeyWrapAlogrithm());
    }

    /**
     * <p>
     * Generates an enveloping encryption. Distinguishes between encrypting the whole document,
     * selected element(s) or element content and an XPath expression to an element to encrypt.
     * </p>
     *
     * <p>
     * A <code>CipherValue</code> element is created inside the current XML document which contains
     * the encrypted data.
     * </p>
     *
     * @param file The XML document to encrypt
     * @param selection The text selection
     * @param secretKey The data encryption key
     * @return The encrypted XML document
     * @throws Exception to indicate any exceptional condition
     */
    private Document envelopingEncryption(String selection, Key secretKey) throws Exception {
        Element root = doc.getDocumentElement();

        XMLCipher keyCipher = XMLCipher.getInstance(keyCipherAlgorithm);
        keyCipher.init(XMLCipher.WRAP_MODE, secretKey);

        XMLCipher xmlCipher = XMLCipher.getInstance(algorithm);
        xmlCipher.init(XMLCipher.ENCRYPT_MODE, secretKey);

        EncryptedKey encryptedKey = keyCipher.encryptKey(doc, secretKey);
        EncryptedData encryptedData = xmlCipher.getEncryptedData();

        KeyInfo keyInfo = new KeyInfo(doc);
        keyInfo.addKeyName(keyName);
        keyInfo.add(encryptedKey);

        encryptedData.setKeyInfo(keyInfo);

        if (!"".equals(encryptionId)) {
            encryptedData.setId(encryptionId);
        }

        if ("document".equalsIgnoreCase(resource)) {
            xmlCipher.doFinal(doc, root, encryptContentOnly);
        } else if ("selection".equalsIgnoreCase(resource)) {
            Document selectionDoc = Utils.parse(selection);
            String tempXPath = Utils.getUniqueXPathToNode(doc, selectionDoc);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NamespaceContext ns = new SignatureNamespaceContext();
            xpath.setNamespaceContext(ns);
            Element selectedElement = (Element) xpath.evaluate(tempXPath, doc, XPathConstants.NODE);
            xmlCipher.doFinal(doc, selectedElement, encryptContentOnly);
        } else if ("xpath".equalsIgnoreCase(resource)) {
            XPath xpath = XPathFactory.newInstance().newXPath();
            NamespaceContext ns = new SignatureNamespaceContext();
            xpath.setNamespaceContext(ns);
            Element selectedElement =
                    (Element) xpath.evaluate(expression, doc, XPathConstants.NODE);
            xmlCipher.doFinal(doc, selectedElement, encryptContentOnly);
        }

        return doc;
    }

    /**
     * <p>
     * Generates a detached encryption. A detached encryption only encrypts the complete detached
     * XML document.
     * </p>
     *
     * <p>
     * A <code>CipherReference</code> element is appended to the current XML document (called
     * context document) to contain the reference (URI) to the encrypted data in the detached (and
     * totally encrypted) XML document (called source document).
     * </p>
     *
     * @param file The selected XML document to contain the reference to the encrypted document
     * @param secretKey The data encryption key
     * @return The encrypted XML document
     * @throws Exception to indicate any exceptional condition
     */
    private Document detachedEncryption(Key secretKey) throws Exception {
        XMLCipher keyCipher = XMLCipher.getInstance(keyCipherAlgorithm);
        keyCipher.init(XMLCipher.WRAP_MODE, secretKey);

        XMLCipher xmlCipher = XMLCipher.getInstance(algorithm);
        xmlCipher.init(XMLCipher.ENCRYPT_MODE, secretKey);

        if ("document".equalsIgnoreCase(resource)) {
            // update the context XML document
            XMLCipher contextCipher = XMLCipher.getInstance();

            Element contextRoot = doc.getDocumentElement();

            EncryptedKey encryptedKey = keyCipher.encryptKey(doc, secretKey);
            EncryptedData encryptedData =
                    contextCipher.createEncryptedData(CipherData.REFERENCE_TYPE,
                            detachedFile.toURI().toString());
            KeyInfo keyInfo = new KeyInfo(doc);
            keyInfo.addKeyName(keyName);
            keyInfo.add(encryptedKey);
            encryptedData.setKeyInfo(keyInfo);
            EncryptionMethod em = contextCipher.createEncryptionMethod(algorithm);
            encryptedData.setEncryptionMethod(em);

            if (!"".equals(encryptionId)) {
                encryptedData.setId(encryptionId);
            }

            Element contextEncryption = contextCipher.martial(doc, encryptedData);
            contextRoot.appendChild(contextEncryption);

            // encrypt the source XML document
            Document enc = xmlCipher.doFinal(detachedDoc, detachedDoc.getDocumentElement());
            NodeList encrypted =
                    enc.getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS,
                            EncryptionConstants._TAG_CIPHERVALUE);

            String encryptedContent = "";
            if (encrypted.getLength() > 0) {
                encryptedContent = encrypted.item(0).getTextContent();
            }

            if (!"".equals(encryptedContent)) {
                Document encryptedDoc = Utils.createDocument();
                Element docElement = encryptedDoc.createElement("EncryptedDoc");
                docElement.setTextContent(encryptedContent);
                encryptedDoc.appendChild(docElement);

                FileOutputStream fosSource = new FileOutputStream(detachedFile);
                XMLUtils.outputDOM(encryptedDoc, fosSource);

                fosSource.flush();
                fosSource.close();
            }
        }

        return doc;
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

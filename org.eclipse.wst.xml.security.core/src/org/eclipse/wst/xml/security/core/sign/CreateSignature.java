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
package org.eclipse.wst.xml.security.core.sign;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.xml.security.signature.ObjectContainer;
import org.apache.xml.security.signature.SignatureProperties;
import org.apache.xml.security.signature.SignatureProperty;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.transforms.params.XPath2FilterContainer;
import org.apache.xml.security.utils.resolver.implementations.ResolverFragment;
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

/**
 * <p>Creates the XML signature for the selected XML document (fragment) based
 * on the user settings in the <i>XML Signature Wizard</i>.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class CreateSignature extends AbstractModernAlgorithm {
    /** The XML signature. */
    private XMLSignature sig = null;
    /** The XML file to sign. */
    private Document doc = null;
    /** The detached file to sign. */
    private File detachedFile = null;
    /** The base URI. */
    private String baseURI = null;
    /** The resource to sign. */
    private String resource = null;
    /** The XPath expression to sign. */
    private String expression = null;
    /** The signature type. */
    private String signatureType = null;
    /** The Java Keystore. */
    private Keystore keystore = null;
    /** The certificate password. */
    private char[] keyPassword = null;
    /** The certificate name. */
    private String keyName = null;
    /** The optional signature ID. */
    private String signatureId = null;
    /** The message digest algorithm. */
    private String messageDigestAlgorithm = null;
    /** The signature algorithm. */
    private String signatureAlgorithm = null;
    /** The canonicalization algorithm. */
    private String canonicalizationAlgorithm = null;
    /** The transformation algorithm. */
    private String transformationAlgorithm = null;
    /** The text selection in an editor. */
    private String textSelection = null;
    /** Additional signature properties for the XML Signature. */
    private ArrayList<DigitalSignatureProperty> properties = null;

    /**
     * <p>Prepares the signing of the XML document (fragment) selected in an Eclipse view (like navigator or package
     * explorer) or in an opened editor based on the user settings in the <i>XML Signature Wizard</i> or the
     * workspace preferences.</p>
     *
     * <p>Loads the Java Keystore and prepares the private key for the signature process.</p>
     *
     * @param model Signature wizard model with all settings from the wizard
     * @param selection The selected text in the editor
     * @param monitor Progress monitor indicating the signing progress
     * @throws Exception to indicate any exceptional condition
     * @return The signed XML document
     */
    public Document sign(Signature model, String selection, IProgressMonitor monitor)
        throws Exception {
        Document signedDoc = null;

        try {
            if (monitor == null) {
                monitor = new NullProgressMonitor();
            }

            loadSettings(model, selection);

            monitor.worked(1);

            keystore.load();

            monitor.worked(1);

            // Get the private key for signing
            PrivateKey privateKey = (PrivateKey) keystore.getPrivateKey(keyName, keyPassword);

            monitor.worked(1);

            ResolverFragment fragmentResolver = new ResolverFragment();

            sig = new XMLSignature(doc, baseURI, signatureAlgorithm, canonicalizationAlgorithm);
            sig.addResourceResolver(fragmentResolver);

            monitor.worked(1);

            if ("enveloping".equalsIgnoreCase(signatureType)) {
                signedDoc = createEnvelopingSignature(privateKey, doc);
            } else if ("enveloped".equalsIgnoreCase(signatureType)) {
                signedDoc = createEnvelopedSignature(privateKey, doc);
            } else if ("detached".equalsIgnoreCase(signatureType)) {
                signedDoc = createDetachedSignature(privateKey, doc);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            monitor.worked(1);
        }

        return signedDoc;
    }

    /**
     * <p>Loads the user settings from the <i>XML Signature Wizard</i> out of the <code>SignatureWizard</code>
     * object into different member variables.</p>
     *
     * <p>Determines the correct and fully qualified names of the selected algorithms for direct use with the Apache XML
     * Security API.</p>
     *
     * @param signature The SignatureWizard object
     * @param selection A possibly existing text selection
     * @throws Exception to indicate any exceptional condition
     */
    private void loadSettings(Signature signature, String selection) throws Exception {
        doc = signature.getDocument();
        // FIXME determine base URI
        baseURI = "";//xmlFile.toURI().toString();
        resource = signature.getResource();
        expression = null;

        if ("xpath".equalsIgnoreCase(resource)) {
            expression = signature.getXpath();
        } else if ("selection".equalsIgnoreCase(resource)) {
            textSelection = selection;
        }

        signatureType = signature.getSignatureType();

        if ("detached".equalsIgnoreCase(signatureType)) {
            detachedFile = signature.getDetachedFile();
        }

        keystore = signature.getKeystore();
        keyPassword = signature.getKeyPassword();
        keyName = signature.getKeyName();

        if (signature.getSignatureProperties() != null) {
            properties = signature.getSignatureProperties();
        }

        if (signature.getSignatureId() != null) {
            signatureId = signature.getSignatureId();
        }

        messageDigestAlgorithm = XmlSecurityConstants.getMessageDigestAlgorithm(signature.getMessageDigestAlgorithm());
        signatureAlgorithm = XmlSecurityConstants.getSignatureAlgorithm(signature.getSignatureAlgorithm());
        canonicalizationAlgorithm = XmlSecurityConstants.getCanonicalizationAlgorithm(signature
                .getCanonicalizationAlgorithm());
        transformationAlgorithm = XmlSecurityConstants.getTransformationAlgorithm(signature.getTransformationAlgorithm());
    }

    /**
     * <p>Creates a detached signature. The selected XML document is regarded as context document and will contain the
     * reference to the signed XML document (detached document). The detached document won't be changed at all, only its
     * hash value will be calculated.</p>
     *
     * @param privateKey The private key to create the signature
     * @param doc The context XML document which contains the signature
     * @return The signed XML document
     * @throws Exception to indicate any exceptional condition
     */
    private Document createDetachedSignature(PrivateKey privateKey, Document doc) throws Exception {
        Element root = doc.getDocumentElement();
        Transforms transforms = null;
        root.appendChild(sig.getElement());

        if (transformationAlgorithm != null && !"None".equals(transformationAlgorithm)) {
            transforms = new Transforms(doc);
            transforms.addTransform(transformationAlgorithm);
        }

        sig.addDocument(detachedFile.toURI().toString(), transforms, messageDigestAlgorithm);

        addProperties(doc);

        if (!"".equals(signatureId)) {
            sig.setId(signatureId);
        }

        boolean doSign = addCertificate();

        if (doSign) {
            sig.sign(privateKey);
        }

        return doc;
    }

    /**
     * <p>Creates an enveloped signature. Adds the signature element to the current root element, so the signed document
     * (fragment) surrounds the signature.</p>
     *
     * @param privateKey The private key to create the signature
     * @param doc The XML document to sign
     * @return The signed XML document
     * @throws Exception to indicate any exceptional condition
     */
    private Document createEnvelopedSignature(PrivateKey privateKey, Document doc) throws Exception {
        Element root = doc.getDocumentElement();

        if ("document".equalsIgnoreCase(resource)) {
            Transforms transforms = new Transforms(doc);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            if (transformationAlgorithm != null && !"None".equals(transformationAlgorithm)) {
                transforms.addTransform(transformationAlgorithm);
            }
            sig.addDocument("", transforms, messageDigestAlgorithm);
        } else if ("selection".equalsIgnoreCase(resource)) {
            Document selectionDoc = Utils.parse(textSelection);
            String finalXpath = Utils.getUniqueXPathToNode(doc, selectionDoc);

            Transforms transforms = new Transforms(doc);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            transforms.addTransform(Transforms.TRANSFORM_XPATH2FILTER, XPath2FilterContainer.newInstanceIntersect(doc,
                    finalXpath).getElement());
            if (transformationAlgorithm != null && !"None".equals(transformationAlgorithm)) {
                transforms.addTransform(transformationAlgorithm);
            }
            sig.addDocument("", transforms, messageDigestAlgorithm);
        } else if ("xpath".equalsIgnoreCase(resource)) {
            Transforms transforms = new Transforms(doc);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            transforms.addTransform(Transforms.TRANSFORM_XPATH2FILTER, XPath2FilterContainer.newInstanceIntersect(doc,
                    expression).getElement());
            if (transformationAlgorithm != null && !"None".equals(transformationAlgorithm)) {
                transforms.addTransform(transformationAlgorithm);
            }
            sig.addDocument("", transforms, messageDigestAlgorithm);
        }

        root.appendChild(sig.getElement());

        addProperties(doc);

        if (!"".equals(signatureId)) {
            sig.setId(signatureId);
        }

        boolean doSign = addCertificate();

        if (doSign) {
            sig.sign(privateKey);
        }

        return doc;
    }

    /**
     * <p>Creates an enveloping signature. Removes the signed node(s) or text content from its original location
     * and stores it inside the <code>object</code> element in the XML Signature. The nodes name is used
     * as id for the <code>object</code> element.</p>
     *
     * @param privateKey The private key to create the signature
     * @param doc The XML document to sign
     * @return The signed XML document
     * @throws Exception to indicate any exceptional condition
     */
    private Document createEnvelopingSignature(PrivateKey privateKey, Document doc) throws Exception {
        Element root = doc.getDocumentElement();
        ObjectContainer obj = new ObjectContainer(doc);
        String objectId = root.getLocalName();

        if ("document".equalsIgnoreCase(resource)) {
            // TODO insert line break before first element in object
            obj.appendChild(root);
            obj.setId(objectId);

            // TODO insert line break after XML declaration (if one is there)
            doc.appendChild(sig.getElement());
        } else if ("selection".equalsIgnoreCase(resource)) {
            Document selectionDoc = Utils.parse(textSelection);
            String expression = Utils.getUniqueXPathToNode(doc, selectionDoc);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NamespaceContext ns = new SignatureNamespaceContext();
            xpath.setNamespaceContext(ns);
            Element selectedElement = (Element) xpath.evaluate(expression, doc, XPathConstants.NODE);
            objectId = selectedElement.getLocalName();

            // TODO insert line break before first element in object
            obj.appendChild(selectedElement);
            obj.setId(objectId);

            if (!root.isSameNode(selectedElement)) {
                // TODO insert line break before first element
                root.appendChild(sig.getElement());
            } else {
                // TODO insert line break after XML declaration (if one is there)
                doc.appendChild(sig.getElement());
            }
        } else if ("xpath".equalsIgnoreCase(resource)) {
            XPath xpath = XPathFactory.newInstance().newXPath();
            NamespaceContext ns = new SignatureNamespaceContext();
            xpath.setNamespaceContext(ns);
            Element selectedElement = (Element) xpath.evaluate(expression, doc, XPathConstants.NODE);
            objectId = selectedElement.getLocalName();

            // TODO insert line break before first element in object
            obj.appendChild(selectedElement);
            obj.setId(objectId);

            if (!root.isSameNode(selectedElement)) {
                // TODO insert line break before first element
                root.appendChild(sig.getElement());
            } else {
                // TODO insert line break after XML declaration (if one is there)
                doc.appendChild(sig.getElement());
            }
        }

        sig.appendObject(obj);

        Transforms transforms = null;
        if (transformationAlgorithm != null && !"None".equals(transformationAlgorithm)) {
            transforms = new Transforms(doc);
            transforms.addTransform(transformationAlgorithm);
        }

        sig.addDocument("#" + objectId, transforms, messageDigestAlgorithm);

        addProperties(doc);

        if (!"".equals(signatureId)) {
            sig.setId(signatureId);
        }

        boolean doSign = addCertificate();

        if (doSign) {
            sig.sign(privateKey);
        }

        return doc;
    }

    /**
     * <p>Adds the entered signature properties to the signature element. The additional reference per property element
     * causes every property to be signed as well.</p>
     *
     * <p>A signature property is only used if and only if the id and target value are not empty.</p>
     *
     * <p>IDs must be unique in the selected XML document!</p>
     *
     * @param doc The XML document to add the properties to
     * @throws Exception to indicate any exceptional condition
     */
    private void addProperties(Document doc) throws Exception {
        if (properties != null && properties.size() > 0) {
            SignatureProperties props = new SignatureProperties(doc);

            for (int i = 0, size = properties.size(); i < size; i++) {
                if (!"".equals(properties.get(i).getId()) && !"".equals(properties.get(i).getTarget())) {
                    SignatureProperty prop = new SignatureProperty(doc, properties.get(i).getTarget(), properties
                            .get(i).getId());
                    prop.getElement().appendChild(doc.createTextNode("\n " + properties.get(i).getContent() + "\n"));
                    props.addSignatureProperty(prop);
                    sig.addDocument("#" + properties.get(i).getId());
                }
            }

            ObjectContainer object = new ObjectContainer(doc);
            object.appendChild(doc.createTextNode("\n"));
            object.appendChild(props.getElement());
            object.appendChild(doc.createTextNode("\n"));
            sig.appendObject(object);
        }
    }

    /**
     * <p>Adds the certificate and public key information from the keystore. All certificate information is needed for
     * successful verification with the <b>XML Security Tools</b>.</p>
     *
     * <p>Aborts the adding if the certificate expired or is not yet valid.</p>
     *
     * @return Certificate successfully added
     * @throws Exception to indicate any exceptional condition
     */
    private boolean addCertificate() throws Exception {
        boolean addedCertificate = false;
        X509Certificate cert = (X509Certificate) keystore.getCertificate(keyName);

        if (cert != null) {
            try {
                cert.checkValidity();
            } catch (CertificateExpiredException cee) {
                addedCertificate = false;
            } catch (CertificateNotYetValidException cnyve) {
                addedCertificate = false;
            }

            sig.addKeyInfo(cert);
            sig.addKeyInfo(cert.getPublicKey());
            addedCertificate = true;
        }

        return addedCertificate;
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
        return signatureAlgorithm;
    }
}

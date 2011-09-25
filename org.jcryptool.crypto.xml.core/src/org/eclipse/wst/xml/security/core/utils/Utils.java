/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.xml.security.utils.EncryptionConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.xml.security.core.XmlSecurityPlugin;
import org.jcryptool.core.util.constants.IConstants;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * <p>
 * Main utility class with different supporting methods used all over the XML Security Tools.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public final class Utils {
    /** Contains the XPath for every element of an XML document. */
    private static HashSet<String> xpathCollector = new HashSet<String>();
    /** Contains the XPath to the selected element content. */
    private static String xpathExpressionToContent = null;
    private static final String TEMPORARY_ELEMENT_LOCAL_NAME = "xmlsectempelement"; //$NON-NLS-1$
    private static final String ID_ATTRIBUTE = "Id"; //$NON-NLS-1$
    private static final String ID_SEPARATOR = ";"; //$NON-NLS-1$

    /**
     * Utility class, no instance required since all methods are static.
     */
    private Utils() {
    }

    /**
     * Parses the IFile in a W3C document.
     *
     * @param file The IFile to parse
     * @return The parsed XML document
     * @throws IOException during document preparation
     * @throws SAXException during document generation
     * @throws ParserConfigurationException during document builder factory initialization
     */
    public static Document parse(final IFile file) throws SAXException, IOException,
            ParserConfigurationException {
        return prepareDocumentBuilder(true, false).parse(file.getLocationURI().toString());
    }

    /**
     * Parses the File in a W3C document.
     *
     * @param file The file to parse
     * @return The parsed XML document
     * @throws IOException during document preparation
     * @throws SAXException during document generation
     * @throws ParserConfigurationException during document builder factory initialization
     */
    public static Document parse(final File file) throws SAXException, IOException,
            ParserConfigurationException {
        return prepareDocumentBuilder(true, false).parse(file);
    }

    /**
     * Parses the byte array in a W3C document.
     *
     * @param content The byte array to parse
     * @return The parsed XML document
     * @throws IOException during document preparation
     * @throws SAXException during document generation
     * @throws ParserConfigurationException during document builder factory initialization
     */
    public static Document parse(final byte[] content) throws SAXException, IOException,
            ParserConfigurationException {
        return prepareDocumentBuilder(true, false).parse(new ByteArrayInputStream(content));
    }

    /**
     * Parses the InputStream in a W3C document.
     *
     * @param content The InputStream to parse
     * @return The parsed XML document
     * @throws IOException during document preparation
     * @throws SAXException during document generation
     * @throws ParserConfigurationException during document builder factory initialization
     */
    public static Document parse(final InputStream content) throws SAXException, IOException,
            ParserConfigurationException {
        return prepareDocumentBuilder(true, false).parse(content);
    }

    /**
     * Parses the String in a W3C document. Adds a temporary root element
     * <code>xmlsectempelement</code> if the String only consists of character data (element
     * content) and doesn't start/end with &lt; and &gt;.
     *
     * @param content The String to parse
     * @return The parsed XML document
     * @throws IOException during document preparation
     * @throws SAXException during document generation
     * @throws ParserConfigurationException during document builder factory initialization
     */
    public static Document parse(String content) throws SAXException, IOException,
            ParserConfigurationException {
        StringBuffer xml = new StringBuffer();

        if (!content.startsWith("<") && !content.endsWith(">")) { //$NON-NLS-1$ //$NON-NLS-2$
            xml.append("<" + TEMPORARY_ELEMENT_LOCAL_NAME + ">"); //$NON-NLS-1$ //$NON-NLS-2$
            xml.append(content);
            xml.append("</" + TEMPORARY_ELEMENT_LOCAL_NAME + ">"); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            xml.append(content);
        }

        return prepareDocumentBuilder(true, false).parse(
                new InputSource(new StringReader(xml.toString())));
    }

    /**
     * Creates a new W3C document.
     *
     * @return The new XML document
     * @throws ParserConfigurationException during document builder factory initialization
     */
    public static Document createDocument() throws ParserConfigurationException {
        return prepareDocumentBuilder(true, false).newDocument();
    }

    /**
     * Prepares a <code>DocumentBuilder</code> to parse XML documents.
     *
     * @param namespaceAware Is DocumentBuilderFactory namespace aware
     * @param validating is DocumentBuilderFactory validating
     * @return The DocumentBuilder
     * @throws ParserConfigurationException during document builder factory initialization
     */
    private static DocumentBuilder prepareDocumentBuilder(boolean namespaceAware, boolean validating)
            throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(namespaceAware);
        dbf.setValidating(validating);

        return dbf.newDocumentBuilder();
    }

    /**
     * Collects all IDs (signature or encryption, based on the type) in the given XML document and
     * returns them in a String array.
     *
     * @param content The XML document containing the IDs to search for
     * @param type Indicates signature or encryption id search
     * @return All IDs in a String array
     */
    public static String[] getIds(final InputStream content, final String type) {
        StringBuilder ids = new StringBuilder();
        ids.append(Messages.bind(Messages.Utils_0, type));

        try {
            Document doc = parse(content);
            String current = null;
            NodeList nodes = null;

            if ("encryption".equals(type)) { //$NON-NLS-1$
                nodes =
                        doc.getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS,
                                EncryptionConstants._TAG_ENCRYPTEDDATA);
            } else if ("signature".equals(type)) { //$NON-NLS-1$
                XPath xpath = XPathFactory.newInstance().newXPath();
                NamespaceContext ns = new SignatureNamespaceContext();
                xpath.setNamespaceContext(ns);
                nodes = (NodeList) xpath.evaluate("//ds:Signature", doc, //$NON-NLS-1$
                        XPathConstants.NODESET);
            }

            if (nodes != null) {
                for (int i = 0, length = nodes.getLength(); i < length; i++) {
                    current = ((Element) nodes.item(i)).getAttribute(ID_ATTRIBUTE);

                    if (current != null && current.trim().length() > 0) {
                        ids.append(current);
                        ids.append(ID_SEPARATOR);
                    }
                }
            }
        } catch (SAXParseException ex) {
            // ignore, simply an XML error
        } catch (Exception ex) {
            logError(ex, Messages.errorDuringIdSearch);
        }
        return ids.toString().split(ID_SEPARATOR);
    }

    /**
     * Collects all IDs (no difference between signature or encryption id) in the given XML document
     * and stores them in a String array.
     *
     * @param xml The XML document containing the IDs to search for
     * @return All IDs in a String array
     */
    public static String[] getAllIds(final Document doc) {
        StringBuffer ids = new StringBuffer(""); //$NON-NLS-1$

        try {
            String current = null;

            NodeList encNodes =
                    doc.getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS,
                            EncryptionConstants._TAG_ENCRYPTEDDATA);

            XPath xpath = XPathFactory.newInstance().newXPath();
            NamespaceContext ns = new SignatureNamespaceContext();
            xpath.setNamespaceContext(ns);
            NodeList sigNodes = (NodeList) xpath.evaluate("//ds:Signature", doc, //$NON-NLS-1$
                    XPathConstants.NODESET);

            for (int i = 0, length = encNodes.getLength(); i < length; i++) {
                current = ((Element) encNodes.item(i)).getAttribute(ID_ATTRIBUTE);

                if (current != null && current.trim().length() > 0) {
                    ids.append(current);
                    ids.append(ID_SEPARATOR);
                }
            }

            for (int i = 0, length = sigNodes.getLength(); i < length; i++) {
                current = ((Element) sigNodes.item(i)).getAttribute(ID_ATTRIBUTE);

                if (current != null && current.trim().length() > 0) {
                    ids.append(current);
                    ids.append(ID_SEPARATOR);
                }
            }
        } catch (Exception ex) {
            logError(ex, Messages.errorDuringIdSearch);
        }

        return ids.toString().split(ID_SEPARATOR);
    }

    /**
     * Returns the XPath for every element of the XML document as an <code>Object</code> array. Gets
     * the root element first, then calls the method <code>childNodes</code> to determine all
     * children of the element.
     *
     * @param doc The XML document
     * @return Object array with the XPath expressions for every node
     */
    public static Object[] getCompleteXpath(final Document doc) {
        if (doc == null) {
            return null;
        }

        Node root = doc.getDocumentElement();
        xpathCollector.add(getXPathExpression(root));

        childNodes(root);

        return xpathCollector.toArray();
    }

    /**
     * Determines all child nodes and stores the XPath expression for every node in the HashSet.
     * Only element nodes are added to the HashSet.
     *
     * @param node The node to determine the XPath expression for and possible child nodes
     */
    private static void childNodes(final Node node) {
        NodeList childList = node.getChildNodes();
        Node childNode = null;

        for (int i = 0, length = childList.getLength(); i < length; i++) {
            childNode = childList.item(i);

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                xpathCollector.add(getXPathExpression(childNode));
            }

            childNodes(childNode);
        }
    }

    /**
     * Returns the unique XPath expression for the given node.
     *
     * @param node The node to determine the XPath for
     * @return The XPath expression as string
     */
    private static String getXPathExpression(final Node node) {
        String xpathExpression = node.getNodeName();

        if (node.getParentNode() != null) {
            int index = 0;
            Node prec = node;

            while (prec != null) {
                if (prec.getNodeName().equals(node.getNodeName())) {
                    index++;
                }
                prec = prec.getPreviousSibling();
            }

            if (node.getParentNode() instanceof Document) {
                ; // do nothing
            } else {
                xpathExpression = getXPathExpression(node.getParentNode()) + "/" + xpathExpression //$NON-NLS-1$
                        + "[" + String.valueOf(index) + "]"; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        return xpathExpression;
    }

    /**
     * Determines the unique XPath expression for the selected element or element content in the
     * editor. Selected element content (character data) is indicated by the temporarily added root
     * element <code>xmlsectempelement</code>.
     *
     * @param doc The complete XML document
     * @param selection The selected element or element content as XML document
     * @return The unique XPath expression for the selection
     */
    public static String getUniqueXPathToNode(final Document doc, final Document selection) {
        String xpathExpression = "/"; //$NON-NLS-1$
        if (!selection.getDocumentElement().getLocalName().equals(TEMPORARY_ELEMENT_LOCAL_NAME)) {
            // a node is selected
            String selectionRootElement = selection.getDocumentElement().getLocalName();
            xpathExpression = "//" + selectionRootElement; //$NON-NLS-1$
            NodeList matchingNodes = doc.getElementsByTagName(selectionRootElement);

            if (matchingNodes.getLength() == 1) {
                xpathExpression = getXPathExpression(matchingNodes.item(0));
            } else {
                for (int i = 0; i < matchingNodes.getLength(); i++) {
                    boolean foundNode = true;
                    NodeList selectionChildNodes = selection.getChildNodes();
                    for (int j = 0; j < selectionChildNodes.getLength(); j++) {
                        Node selectionNode = selectionChildNodes.item(j);
                        Node matchingNode = matchingNodes.item(i);
                        if (!matchingNode.toString().equals(selectionNode.toString())) {
                            foundNode = false;
                            break;
                        }
                        if (!matchingNode.getTextContent().trim().equals(
                                selectionNode.getTextContent().trim())) {
                            foundNode = false;
                            break;
                        }
                    }
                    if (foundNode) {
                        xpathExpression = getXPathExpression(matchingNodes.item(i));
                        break;
                    }
                }
            }
        } else { // only element content is selected
            xpathExpressionToContent = null;
            xpathExpression =
                    getXPathToContent(doc.getDocumentElement(),
                            selection.getDocumentElement().getTextContent());
        }

        return xpathExpression;
    }

    /**
     * Determines the XPath expression to the selected character data (element content). The first
     * matching content is used, so this XPath may point to another element content.
     *
     * @param root The root element of the XML document
     * @param selectedContent The selected character data
     * @return The XPath expression to the selected character data
     */
    private static String getXPathToContent(final Node root, final String selectedContent) {
        NodeList childList = root.getChildNodes();
        Node childNode = null;

        for (int i = 0, length = childList.getLength(); i < length
                && xpathExpressionToContent == null; i++) {
            childNode = childList.item(i);
            if (childNode.getNodeType() == Node.TEXT_NODE) {
                if (childNode.getTextContent().equals(selectedContent)) {
                    xpathExpressionToContent = getXPathExpression(childNode.getParentNode());
                }
            }
            getXPathToContent(childNode, selectedContent);
        }

        return xpathExpressionToContent + "/text()"; //$NON-NLS-1$
    }

    /**
     * Validates the XPath expression entered by the user in a wizard. The user can only continue
     * with the entered XPath if <i>single</i> is returned.
     *
     * @param doc The XML document
     * @param xpathExpression The XPath expression
     * @return <i>single</i>, <i>multiple</i>, <i>none</i> or <i>attribute</i> depending on the
     *         entered XPath expression
     */
    public static String validateXPath(final Document doc, final String xpathExpression) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodes =
                    (NodeList) xpath.evaluate(xpathExpression, doc, XPathConstants.NODESET);

            if (nodes.getLength() == 1) {
                if (nodes.item(0).getNodeType() == Node.ATTRIBUTE_NODE) {
                    return "attribute"; //$NON-NLS-1$
                } else if (nodes.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    return "single"; //$NON-NLS-1$
                }
            } else if (nodes.getLength() > 1) {
                return "multiple"; //$NON-NLS-1$
            }

            return "none"; //$NON-NLS-1$
        } catch (Exception ex) {
            return "none"; //$NON-NLS-1$
        }
    }

    /**
     * Returns the XML document parsed as a String. The output String can be pretty printed or not
     * (never pretty print a signed XML document, this will break the signature).
     *
     * @param doc The XML document to convert
     * @param prettyPrint Pretty print the output String
     * @return The Document as a String
     * @exception Exception to indicate any exceptional condition
     */
    public static String docToString(final Document doc, final boolean prettyPrint)
            throws Exception {
        StringWriter writer = new StringWriter();
        boolean indentFallback = false;

        TransformerFactory factory = TransformerFactory.newInstance();
        if (prettyPrint) {
            try {
                factory.setAttribute("indent-number", "4"); //$NON-NLS-1$ //$NON-NLS-2$
            } catch (IllegalArgumentException e) {
                indentFallback = true;
            }
        }

        Transformer transformer = factory.newTransformer();
        Properties props = new Properties();
        props.setProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
        props.setProperty(OutputKeys.STANDALONE, "yes"); //$NON-NLS-1$
        props.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); //$NON-NLS-1$
        if (prettyPrint) {
            props.setProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

            if (indentFallback) {
                props.setProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(4)); //$NON-NLS-1$
            }
        }

        DocumentType type = doc.getDoctype();
        if (type != null) {
            String publicId = type.getPublicId();
            if (publicId != null) {
                props.setProperty(OutputKeys.DOCTYPE_PUBLIC, publicId);
            }
            String systemId = type.getSystemId();
            if (systemId != null) {
                props.setProperty(OutputKeys.DOCTYPE_SYSTEM, systemId);
            }
        }

        transformer.setOutputProperties(props);
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        return writer.toString();
    }

    public static InputStream docToInputStram(final Document doc) throws Exception {
        DOMSource source = new DOMSource(doc);
        StringWriter xmlAsWriter = new StringWriter();
        StreamResult result = new StreamResult(xmlAsWriter);
        TransformerFactory.newInstance().newTransformer().transform(source, result);

        return new ByteArrayInputStream(xmlAsWriter.toString().getBytes(IConstants.UTF8_ENCODING));
    }

    /**
     * Validates the ID (signature ID or encryption ID) entered by the user. An ID containing &lt;,
     * &gt;, &qout;, &apos; or &amp; or whitespace is invalid.
     *
     * @param id The entered ID
     * @return Validity of the ID
     */
    public static boolean validateId(final String id) {
        return Pattern.matches("[^<>&\"\'\\s]+", id); //$NON-NLS-1$
    }

    /**
     * Checks whether the valid ID is unique in the current XML document.
     *
     * @param newId The entered ID
     * @param ids All existing IDs in the XML document
     * @return Uniqueness of the ID
     */
    public static boolean ensureIdIsUnique(final String newId, final String[] ids) {
        boolean uniqueId = true;

        for (String currentId : ids) {
            if (currentId.equals(newId)) {
                uniqueId = false;
            }
        }
        return uniqueId;
    }

    /**
     * Called when there is a text selection and either the XML Signature Wizard or the XML
     * Encryption Wizard is called. If the selection is invalid, the radio button in the wizard is
     * disabled. This method returns always <code>true</code> if only element content (no &gt; or
     * &lt; included) is selected.
     *
     * @param textSelection The text selection as a String value
     * @return true or false which activates or deactivates the selection radio button in the wizard
     */
    public static boolean parseSelection(final String textSelection) {
        if (textSelection == null || textSelection.trim().length() == 0) {
            return false;
        }

        Pattern p = Pattern.compile("[^<>]+"); //$NON-NLS-1$
        Matcher m = p.matcher(textSelection);

        // a tag (or parts of it) are selected
        if (!m.matches()) {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            try {
                XMLReader xmlReader = spf.newSAXParser().getXMLReader();
                xmlReader.setErrorHandler(null);
                xmlReader.parse(new InputSource(new StringReader(textSelection)));
            } catch (IOException ex) {
                logError(ex, "Error during parsing textselection"); //$NON-NLS-1$
                return false;
            } catch (SAXException ex) {
                // ignore, simply an invalid selection
                return false;
            } catch (ParserConfigurationException ex) {
                logError(ex, "Error during parsing textselection"); //$NON-NLS-1$
                return false;
            }

            return true;
        }

        // only element content, no < or > selected, always return true
        return true;
    }

    /**
     * Logs the given error message to the workspace default log file.
     *
     * @param ex The error message to log
     * @param message The error message
     */
    public static void logError(final Exception ex, final String message) {
        IStatus status =
                new Status(IStatus.ERROR,
                        XmlSecurityPlugin.getDefault().getBundle().getSymbolicName(), 0, message,
                        ex);
        XmlSecurityPlugin.getDefault().getLog().log(status);
    }
}

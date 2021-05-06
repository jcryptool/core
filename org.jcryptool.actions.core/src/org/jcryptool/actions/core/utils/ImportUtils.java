// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.core.utils;

import java.io.File;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.core.runtime.FileLocator;
import org.jcryptool.actions.core.ActionsCorePlugin;
import org.jcryptool.actions.core.types.ActionCascade;
import org.jcryptool.actions.core.types.ActionItem;
import org.jcryptool.core.logging.utils.LogUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Utility class with different import helper methods for the <b>Actions view</b> plug-ins.
 *
 * @author Dominik Schadow
 * @version 0.9.3
 */
public class ImportUtils {
    private String filename;

    public ImportUtils(String filename) {
        this.filename = filename;
    }

    public static String getPathFromFile(final String filename) {
        return filename.substring(0, filename.lastIndexOf(File.separator));
    }

    public boolean validateActionCascade() {
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$
        Schema schema = null;
        URL schemaUrl = null;

        try {
            schemaUrl = FileLocator.toFileURL((ActionsCorePlugin.getDefault().getBundle().getEntry("/"))); //$NON-NLS-1$
            File schemaFile = new File(schemaUrl.getFile() + "resources" + File.separatorChar //$NON-NLS-1$
                    + "actioncascade.xsd"); //$NON-NLS-1$
            schema = factory.newSchema(schemaFile);
        } catch (Exception ex) {
            LogUtil.logError("Errors creating XSD-Schema for Actions from URL: " + schemaUrl, ex); //$NON-NLS-1$
            return false;
        }

        try {
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(filename));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public ActionCascade createActionCascade() {
        Document doc;
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = dBuilder.parse(new File(filename));
            doc.getDocumentElement().normalize();
        } catch (Exception ex) {
            LogUtil.logError("XML-Parsing of " + filename + " failed.", ex); //$NON-NLS-1$ //$NON-NLS-2$
            return null;
        }

        ActionCascade ac =
                new ActionCascade(doc.getDocumentElement().getAttributes().getNamedItem("name").getTextContent()); //$NON-NLS-1$
        ac.setSavePasswords(Boolean.parseBoolean(doc.getDocumentElement().getAttributes().getNamedItem("passwords").getTextContent())); //$NON-NLS-1$

        NodeList nList = doc.getElementsByTagName("ActionItem"); //$NON-NLS-1$
        for (int i = 0; i < nList.getLength(); i++) {
            String actionname = ""; //$NON-NLS-1$
            String filename = ""; //$NON-NLS-1$
            String pluginId = ""; //$NON-NLS-1$
            String actionType = ""; //$NON-NLS-1$
            String alphabet = ""; //$NON-NLS-1$
            String dataObjectType = ""; //$NON-NLS-1$
            NodeList values = nList.item(i).getChildNodes();
            for (int j = 0; j < values.getLength(); j++) {
                String nodeName = values.item(j).getNodeName();
                if (nodeName.equals("FileName")) { //$NON-NLS-1$
                    filename = values.item(j).getTextContent();
                } else if (nodeName.equals("ActionName")) { //$NON-NLS-1$
                    actionname = values.item(j).getTextContent();
                    pluginId = values.item(j).getAttributes().getNamedItem("plugin").getTextContent(); //$NON-NLS-1$
                } else if (nodeName.equals("ActionType")) { //$NON-NLS-1$
                    actionType = values.item(j).getTextContent();
                } else if (nodeName.equals("Alphabet")) { //$NON-NLS-1$
                    alphabet = values.item(j).getTextContent();
                }
            }
            ActionItem ai = new ActionItem(filename, actionname);
            ai.setActionType(actionType);
            ai.setPluginId(pluginId);
            ai.setAlphabet(alphabet);

            for (int j = 0; j < values.getLength(); j++) {
                String nodeName = values.item(j).getNodeName();
                if (nodeName.equals("Parameters")) { //$NON-NLS-1$
                    dataObjectType = values.item(j).getAttributes().getNamedItem("dataobjecttype").getTextContent(); //$NON-NLS-1$
                    ai.setDataObjectType(dataObjectType);
                    NodeList params = values.item(j).getChildNodes();

                    for (int m = 0; m < params.getLength(); m++) {
                        NodeList paramAtts = params.item(m).getChildNodes();
                        String key = null;
                        String value = null;
                        for (int n = 0; n < paramAtts.getLength(); n++) {
                            String paramNodeName = paramAtts.item(n).getNodeName();
                            if (paramNodeName.equals("Key")) { //$NON-NLS-1$
                                key = paramAtts.item(n).getTextContent();
                            } else if (paramNodeName.equals("Value")) { //$NON-NLS-1$
                                value = paramAtts.item(n).getTextContent();
                            }
                            ai.addParam(key, value);
                        }
                    }
                }
            }

            ac.addItem(ai);
        }

        return ac;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String filename) {
        this.filename = filename;
    }
}

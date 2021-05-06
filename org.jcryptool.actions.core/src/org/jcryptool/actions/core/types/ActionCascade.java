// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.core.types;

import java.util.ArrayList;
import java.util.Enumeration;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * <p>
 * <code>ActionCascade</code> object class. Contains one to many <code>ActionItem</code> objects. At least the starting
 * ActionItem is always contained in an ActionCascade, so an empty ActionCascade is impossible.
 * </p>
 *
 * <p>
 * An ActionCascade should be named like the initial file name with a suffix <strong>Cascade</strong>.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class ActionCascade {
    private static final String NAMESPACE_URI = "http://www.jcryptool.org/actioncascade"; //$NON-NLS-1$
    private String name;
    private ArrayList<ActionItem> items = null;
    private boolean savePasswords = false;

    public ActionCascade(final String name) {
        this.name = name;

        items = new ArrayList<ActionItem>();
    }

    public String getName() {
        return name;
    }

    public boolean isSavePasswords() {
        return savePasswords;
    }

    public void setSavePasswords(boolean savePasswords) {
        this.savePasswords = savePasswords;
    }

    public ArrayList<ActionItem> addItem(final ActionItem item) {
        items.add(item);
        return items;
    }

    public ArrayList<ActionItem> removeItem(final ActionItem item) {
        items.remove(item);
        return items;
    }

    public void removeItemAt(final int index) {
        if (items.size() < index) {
            return;
        } else {
            items.remove(index);
        }
    }

    public ActionItem getItemAt(final int index) {
        if (items.size() < index) {
            return null;
        } else {
            return items.get(index);
        }
    }

    public ArrayList<ActionItem> getAllItems() {
        return items;
    }

    public int getSize() {
        return items.size();
    }

    public Document toXML() {
        Document doc = new Document();
        Element actionCascade = new Element("ActionCascade"); //$NON-NLS-1$
        actionCascade.setAttribute("name", name); //$NON-NLS-1$
        actionCascade.setAttribute("passwords", Boolean.toString(isSavePasswords())); //$NON-NLS-1$
        actionCascade.setNamespace(Namespace.getNamespace(NAMESPACE_URI));

        Element actionItems = new Element("ActionItems", NAMESPACE_URI); //$NON-NLS-1$ //$NON-NLS-2$

        for (ActionItem item : items) {
            Element actionItem = new Element("ActionItem", NAMESPACE_URI); //$NON-NLS-1$
            Element actionType = new Element("ActionType", NAMESPACE_URI); //$NON-NLS-1$
            actionType.setText(item.getActionType());
            Element fileName = new Element("FileName", NAMESPACE_URI); //$NON-NLS-1$
            fileName.setText(item.getFilename());
            Element actionName = new Element("ActionName", NAMESPACE_URI); //$NON-NLS-1$
            actionName.setText(item.getActionName());
            actionName.setAttribute("plugin", item.getPluginId()); //$NON-NLS-1$
            Element alphabetName = new Element("Alphabet", NAMESPACE_URI); //$NON-NLS-1$
            alphabetName.setText(item.getAlphabet());

            actionItem.addContent(actionType);
            actionItem.addContent(fileName);
            actionItem.addContent(actionName);
            actionItem.addContent(alphabetName);

            Element parameters = new Element("Parameters", NAMESPACE_URI); //$NON-NLS-1$
            parameters.setAttribute("dataobjecttype", item.getDataObjectType()); //$NON-NLS-1$
            Enumeration<String> e = item.getParams().keys();
            while (e.hasMoreElements()) {
                String paramKey = e.nextElement();

                if (!isSavePasswords() && "key password".equals(paramKey)) { //$NON-NLS-1$
                    if (e.hasMoreElements()) {
                        paramKey = e.nextElement();
                    } else {
                        break;
                    }
                }

                String paramValue = item.getParam(paramKey);
                Element parameter = new Element("Parameter", NAMESPACE_URI); //$NON-NLS-1$
                Element key = new Element("Key", NAMESPACE_URI); //$NON-NLS-1$
                key.setText(paramKey);
                Element value = new Element("Value", NAMESPACE_URI); //$NON-NLS-1$
                value.setText(paramValue);
                parameter.addContent(key);
                parameter.addContent(value);
                parameters.addContent(parameter);
            }
            actionItem.addContent(parameters);
            actionItems.addContent(actionItem);
        }

        actionCascade.addContent(actionItems);

        doc.setRootElement(actionCascade);
        return doc;
    }

    public String toString() {
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        return outputter.outputString(toXML());
    }
}

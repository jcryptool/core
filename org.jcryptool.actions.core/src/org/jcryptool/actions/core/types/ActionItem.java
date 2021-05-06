// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.core.types;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * An <code>ActionItem</code> represents one item in an action cascade. Each <code>ActionItem</code>
 * must provide a human readable action name, a file name on which the action is applied to and a
 * position in the action cascade.
 *
 * @author Dominik Schadow
 * @version 0.6.0
 */
public class ActionItem {
    private static final String CRLF = System.getProperty("line.separator"); //$NON-NLS-1$
    /** The executed actions name. */
    private String actionName;
    /** The filename of the created file. */
    private String filename;
    /** The plug-in ID of the plug-in that executed the action, maybe empty. */
    private String pluginId = ""; //$NON-NLS-1$
    /** The class of the used data object. */
    private String dataObjectType = ""; //$NON-NLS-1$
    /** The type of the action. */
    private String actionType;
    /** The name of the alphabet. */
    private String alphabet;
    /** Table containing defined parameters. */
    private Hashtable<String, String> params;

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(final String actionType) {
        this.actionType = actionType;
    }

    public ActionItem(final String filename, final String actionName) {
        this.filename = filename;
        this.actionName = actionName;
        this.params = new Hashtable<String, String>();
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(final String actionName) {
        this.actionName = actionName;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String filename) {
        this.filename = filename;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(final String pluginId) {
        this.pluginId = pluginId;
    }

    public void addParam(final String key, final String value) {
        if (value != null && key != null) {
            this.params.put(key, value);
        }
    }

    public String getParam(final String key) {
        return (String) this.params.get(key);
    }

    public Hashtable<String, String> getParams() {
        return params;
    }

    public void setParams(Hashtable<String, String> params) {
        this.params = params;
    }

    public String getDataObjectType() {
        return dataObjectType;
    }

    public void setDataObjectType(String dataObjectType) {
        this.dataObjectType = dataObjectType;
    }

    public String getDetails() {
        String detailText = getActionName() + CRLF;
        StringBuffer sbuf = new StringBuffer(detailText);
        if (getAlphabet() != null) {
            sbuf.append(getAlphabet() + CRLF);
        }

        Enumeration<String> keys = getParams().keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            sbuf.append(key + ":\t " + getParams().get(key) + CRLF); //$NON-NLS-1$
        }
        return sbuf.toString();
    }
}

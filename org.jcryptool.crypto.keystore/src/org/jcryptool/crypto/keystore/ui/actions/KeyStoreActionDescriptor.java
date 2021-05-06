// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.actions;

public class KeyStoreActionDescriptor implements IKeyStoreActionDescriptor {

    private String text;
    private String toolTipText;
    private String type;
    private String extensionUID;
    private String icon;
    private String id;

    public KeyStoreActionDescriptor(String extensionUID, String id, String type, String text, String toolTipText,
            String icon) {
        this.extensionUID = extensionUID;
        this.id = id;
        this.type = type;
        this.text = text;
        this.toolTipText = toolTipText;
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public String getToolTipText() {
        return toolTipText;
    }

    public String getType() {
        return type;
    }

    public String getExtensionUID() {
        return extensionUID;
    }

    public String getIcon() {
        return icon;
    }

    public String getID() {
        return id;
    }

}

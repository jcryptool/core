/*******************************************************************************
 * Copyright (c) 2008 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.sign;

/**
 * <p>Creates a new signature property in the <i>XML Signature Wizard</i>.
 * Each property has an id, a target and a content.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class DigitalSignatureProperty {
    /** The id of the signature property. */
    private String id;
    /** The target of the signature property. */
    private String target;
    /** The content of the signature property. */
    private String content;

    /**
     * Constructor of the signature property.
     *
     * @param newId The id for the property
     * @param newTarget The target for the property
     * @param newContent The content for the property
     */
    public DigitalSignatureProperty(final String newId, final String newTarget, final String newContent) {
        id = newId;
        target = newTarget;
        content = newContent;
    }

    /**
     * Returns the id of the selected property.
     *
     * @return The signature property id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the target of the selected property.
     *
     * @return The signature property target
     */
    public String getTarget() {
        return target;
    }

    /**
     * Returns the content of the selected property.
     *
     * @return The signature property content
     */
    public String getContent() {
        return content;
    }
}

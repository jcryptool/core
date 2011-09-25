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
package org.eclipse.wst.xml.security.ui.sign;

/**
 * <p>Editable table item for the signature property table. Every column - <i>ID,
 * Target, Content</i> is editable and has an empty default value.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class EditableTableItem {
    /** Signature property id. */
    private String id;
    /** Signature property target. */
    private String target;
    /** Signature property content. */
    private String content;

    /**
     * Constructor for a new row.
     *
     * @param newId The signature property id
     * @param newTarget The signature property target
     * @param newContent The signature property content
     */
    public EditableTableItem(final String newId, final String newTarget, final String newContent) {
        id = newId;
        target = newTarget;
        content = newContent;
    }

    /**
     * Returns the signature property ID.
     *
     * @return The ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the signature property ID.
     *
     * @param id The ID
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Returns the signature property target.
     *
     * @return The target
     */
    public String getTarget() {
        return target;
    }

    /**
     * Sets the signature property target.
     *
     * @param target The target
     */
    public void setTarget(final String target) {
        this.target = target;
    }

    /**
     * Returns the signature property content.
     *
     * @return The content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the signature property content.
     *
     * @param content The content
     */
    public void setContent(final String content) {
        this.content = content;
    }
}

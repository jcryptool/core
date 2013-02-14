/*******************************************************************************
 * Copyright (c) 203 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.jcryptool.crypto.xml.core.utils;

/**
 * <p>
 * This utility class defines global variables and their values for the XML Security Tools.
 * </p>
 * 
 * @author Dominik Schadow
 * @version 1.0.0
 */
public interface IGlobals {
    /** Maximum number of characters of signature and encryption ID. */
    int ID_LIMIT = 20;
    /** Maximum key name size. */
    int KEY_NAME_MAX_SIZE = 20;
    /** Maximum key password size. */
    int KEY_PASSWORD_MAX_SIZE = 20;
    /** Group numerator in wizards. */
    int GROUP_NUMERATOR = 100;
    /** Default margin for GUI elements. */
    int MARGIN = 10;
    /** Default large width for combo boxes. */
    int COMBO_LARGE_WIDTH = 200;
    /** Default margin for combo boxes. */
    int COMBO_MARGIN = 20;
    /** Small width for textfields. */
    int SHORT_TEXT_WIDTH = 200;
    /** Medium width for textfields. */
    int MEDIUM_TEXT_WIDTH = 225;
    /** Schema URI. */
    String SCHEMA = "http://apache.org/xml/features/validation/schema";
    /** Defer node expansion URI. */
    String DOM = "http://apache.org/xml/features/dom/defer-node-expansion";
    /** SAX validation URI. */
    String SAX = "http://xml.org/sax/features/validation";
    /** SAX namespaces URI. */
    String SAX_NAMESPACES = "http://xml.org/sax/features/namespaces";
    /** External schema location URI. */
    String EXTERNAL_SCHEMA_LOC = "http://apache.org/xml/properties/schema/external-schemaLocation";
}

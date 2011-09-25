/*******************************************************************************
 * Copyright (c) 2010 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.utils;

/**
 * <p>
 * This utility class defines global variables and their values for the XML Security Tools.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public interface IGlobals {
    /** Maximum number of characters of signature and encryption ID. */
    int ID_LIMIT = 20;
    /** Maximum number of characters of each key information (like OU). */
    int KEY_DATA_LIMIT = 50;
    /** Minimum key name size. */
    int KEY_NAME_MIN_SIZE = 4;
    /** Maximum key name size. */
    int KEY_NAME_MAX_SIZE = 20;
    /** Minimum keystore password size. */
    int KEYSTORE_PASSWORD_MIN_SIZE = 6;
    /** Maximum keystore password size. */
    int KEYSTORE_PASSWORD_MAX_SIZE = 20;
    /** Minimum key password size. */
    int KEY_PASSWORD_MIN_SIZE = 6;
    /** Maximum key password size. */
    int KEY_PASSWORD_MAX_SIZE = 20;
    /** Group numerator in wizards. */
    int GROUP_NUMERATOR = 100;
    /** Default margin for GUI elements. */
    int MARGIN = 10;
    /** Large margin for GUI elements. */
    int LARGE_MARGIN = 10;
    /** Default width for buttons. */
    int BUTTON_WIDTH = 60;
    /** Large width for buttons. */
    int LARGE_BUTTON_WIDTH = 100;
    /** Default width for combo boxes. */
    int COMBO_WIDTH = 150;
    /** Default large width for combo boxes. */
    int COMBO_LARGE_WIDTH = 200;
    /** Default margin for combo boxes. */
    int COMBO_MARGIN = 20;
    /** Small width for textfields. */
    int SHORT_TEXT_WIDTH = 200;
    /** Medium width for textfields. */
    int MEDIUM_TEXT_WIDTH = 225;
    /** Large width for textfields. */
    int LARGE_TEXT_WIDTH = 300;
    /** Default extension for a JKS file. */
    String KEYSTORE_EXTENSION = ".jks";
    /** Default extension name for Keystore dialog. */
    String[] KEY_STORE_EXTENSION_NAME = {"Java Keystore (*.jks)", "All Files (*.*)"};
    /** Default extension for Keystore dialog. */
    String[] KEY_STORE_EXTENSION = {"*.jks", "*.*"};
    /** Default extensions for detached file dialog. */
    String[] DETACHED_FILE_EXTENSION = {"*.xml"};
    /** Default extension names for detached file dialog. */
    String[] DETACHED_FILE_EXTENSION_NAME = {"XML document (*.xml)"};
    /**
     * The Java Keystore type, JCEKS is required, since JKS (the Java default) does not support secret keys without a
     * certificate.
     */
    String KEYSTORE_TYPE = "JCEKS";
    String CERTIFICATE_TYPE = "X.509";
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

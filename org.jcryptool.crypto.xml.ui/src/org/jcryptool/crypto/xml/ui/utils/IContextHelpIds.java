/*******************************************************************************
 * Copyright (c) 2013 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.jcryptool.crypto.xml.ui.utils;

/**
 * <p>This interfaces defines all context help ids for the <b>XML Security</b> plug-in. Context
 * help file is included in the <b>org.jcryptool.crypto.xml.help</b> plug-in.</p>
 *
 * @author Dominik Schadow
 * @version 1.0.0
 */
public interface IContextHelpIds {
    String PREFIX = "org.jcryptool.crypto.xml.help.";
    String SIGNATURE_VIEW = PREFIX + "signature_view";
    String PREFERENCES_GENERAL = PREFIX + "preferences_general";
    String PREFERENCES_SIGNATURE = PREFIX + "preferences_signature";
    String PREFERENCES_ENCRYPTION = PREFIX + "preferences_encryption";
    String XPATH_DIALOG = PREFIX + "xpath_dialog";
    String WIZARD_DECRYPTION_RESOURCE = PREFIX + "wizard_decryption_resource";
    String WIZARD_DECRYPTION_KEY = PREFIX + "wizard_decryption_key";
    String WIZARD_ENCRYPTION_RESOURCE = PREFIX + "wizard_encryption_resource";
    String WIZARD_ENCRYPTION_KEY = PREFIX + "wizard_encryption_key";
    String WIZARD_ENCRYPTION_ALGORITHMS = PREFIX + "wizard_encryption_algorithms";
    String WIZARD_SIGNATURE_RESOURCE = PREFIX + "wizard_signature_resource";
    String WIZARD_SIGNATURE_KEY = PREFIX + "wizard_signature_key";
    String WIZARD_SIGNATURE_ALGORITHMS = PREFIX + "wizard_signature_algorithms";
}

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
package org.eclipse.wst.xml.security.ui.preferences;

/**
 * <p>
 * This interface contains some preference values for the XML Security Tools preference pages. All other values (like
 * algorithms) are defined in the <code>Algorithms</code> class in the
 * <code>org.eclipse.wst.xml.security.core.utils</code> package.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public interface IPreferenceValues {
    /** Canonicalization types. */
    String[][] CANON_TYPES = { {"&Exclusive", "exclusive"}, {"&Inclusive", "inclusive"}};
    /** Canonicalization targets. */
    String[][] CANON_TARGETS = { {"&Same Document", "internal"}, {"&New Document", "external"}};
    /** Signature types. */
    String[][] SIGNATURE_TYPES = { {"Enveloping", "enveloping"}, {"Enveloped", "enveloped"}};
    /** Encryption types. */
    String[][] ENCRYPTION_TYPES = {{"Enveloping", "enveloping"}};
}

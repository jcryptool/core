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
package org.eclipse.wst.xml.security.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;

/**
 * <p>This class is used to initialize all default preference values of the
 * XML Security Tools.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

  /**
   * Constructor.
   */
  public PreferenceInitializer() {
    super();
  }

  /**
   * Initializes default preference values for the XML Security Tools. This method is called
   * automatically by the preference initializer when the appropriate default preference node is
   * accessed.
   */
  public void initializeDefaultPreferences() {
    IPreferenceStore store = XSTUIPlugin.getDefault().getPreferenceStore();
    // Canonicalization
    store.setDefault(PreferenceConstants.CANON_TYPE, "exclusive");
  }
}

// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.backend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.IKeyStoreConstants;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.ui.actions.IKeyStoreActionDescriptor;
import org.jcryptool.crypto.keystore.ui.actions.KeyStoreActionDescriptor;

/**
 * 
 * @author t-kern
 * 
 */
public class KeyStoreActionManager {
    private static KeyStoreActionManager instance;

    private List<IKeyStoreActionDescriptor> newSecretKeyActions = new ArrayList<IKeyStoreActionDescriptor>();
    private List<IKeyStoreActionDescriptor> newKeyPairActions = new ArrayList<IKeyStoreActionDescriptor>();
    private List<IKeyStoreActionDescriptor> importActions = new ArrayList<IKeyStoreActionDescriptor>();

    private KeyStoreActionManager() {
        loadKeyStoreActions();
    }

    public static synchronized KeyStoreActionManager getInstance() {
        if (instance == null) {
            instance = new KeyStoreActionManager();
        }
        return instance;
    }

    private void loadKeyStoreActions() {
        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(KeyStorePlugin.PLUGIN_ID,
                IKeyStoreConstants.PL_KEYSTORE_COMMANDS);

        IExtension[] extensions = extensionPoint.getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            LogUtil.logInfo("Extension: " + extensions[i].getLabel()); //$NON-NLS-1$
            IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
            for (int j = 0; j < configElements.length; j++) {
                if (configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_TYPE).equals(
                        IKeyStoreConstants.ID_NEW_SYMMETRIC_KEY)) {
                    addNewSymmetricKeyAction(new KeyStoreActionDescriptor(extensions[i].getUniqueIdentifier(),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_ID),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_TYPE),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_TEXT),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_TOOLTIP),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_ICON)));
                } else if (configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_TYPE).equals(
                        IKeyStoreConstants.ID_NEW_KEYPAIR)) {
                    addNewKeyPairAction(new KeyStoreActionDescriptor(extensions[i].getUniqueIdentifier(),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_ID),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_TYPE),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_TEXT),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_TOOLTIP),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_ICON)));
                } else if (configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_TYPE).equals(
                        IKeyStoreConstants.ID_IMPORT)) {
                    addImportAction(new KeyStoreActionDescriptor(extensions[i].getUniqueIdentifier(),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_ID),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_TYPE),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_TEXT),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_TOOLTIP),
                            configElements[j].getAttribute(IKeyStoreConstants.ATT_ACTION_ICON)));
                }
            }
        }
    }

    private void addNewSymmetricKeyAction(IKeyStoreActionDescriptor descriptor) {
        newSecretKeyActions.add(descriptor);
    }

    private void addNewKeyPairAction(IKeyStoreActionDescriptor descriptor) {
        newKeyPairActions.add(descriptor);
    }

    private void addImportAction(IKeyStoreActionDescriptor descriptor) {
        importActions.add(descriptor);
    }

    public Iterator<IKeyStoreActionDescriptor> getNewSymmetricKeyActions() {
        return newSecretKeyActions.iterator();
    }

    public Iterator<IKeyStoreActionDescriptor> getNewKeyPairActions() {
        return newKeyPairActions.iterator();
    }

    public Iterator<IKeyStoreActionDescriptor> getImportActions() {
        return importActions.iterator();
    }
}

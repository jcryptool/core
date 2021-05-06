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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.IKeyStoreConstants;
import org.jcryptool.crypto.keystore.KeyStorePlugin;

public class ShadowKeyStoreHandler extends AbstractHandler {
    private String extensionUID;
    private String id;

    public ShadowKeyStoreHandler(IKeyStoreActionDescriptor descriptor) {
        extensionUID = descriptor.getExtensionUID();
        LogUtil.logInfo("ExtensionUID: " + extensionUID); //$NON-NLS-1$
        id = descriptor.getID();
        // setText(text);
        // setToolTipText(tooltip);
        // setImageDescriptor(KeyStorePlugin.getImageDescriptor(extensionUID, icon));
    }

    public Object execute(final ExecutionEvent event) {
        IExtensionRegistry registry = Platform.getExtensionRegistry();

        IExtensionPoint extensionPoint = registry.getExtensionPoint(KeyStorePlugin.PLUGIN_ID,
                IKeyStoreConstants.PL_KEYSTORE_COMMANDS);

        IExtension extension = extensionPoint.getExtension(extensionUID);
        IConfigurationElement[] configElements = extension.getConfigurationElements();
        for (int i = 0; i < configElements.length; i++) {
            IConfigurationElement element = configElements[i];

            if (element.getName().equals(IKeyStoreConstants.TAG_KEYSTORE_COMMAND)) {
                LogUtil.logInfo("NAME: " + element.getName()); //$NON-NLS-1$
                if (id.equals(element.getAttribute(IKeyStoreConstants.ATT_ACTION_ID))) {
                    try {
                        final AbstractKeyStoreHandler handler = (AbstractKeyStoreHandler) element
                                .createExecutableExtension(IKeyStoreConstants.ATT_HANDLER_CLASS);
                        PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
                            public void run() {
                                try {
									handler.execute(event);
								} catch (ExecutionException e) {
									LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
								}
                            }
                        });
                    } catch (CoreException e) {
                        LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
                                "CoreException while creating an executable extension", e, true);
                    }
                }
            }
        }
        return(null);
    }

}

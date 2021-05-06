// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.engines.messagedigest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.ui.PartInitException;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEngine;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEnginesPlugin;

import de.flexiprovider.api.MessageDigest;
import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;

public class MessageDigestEngine extends FlexiProviderEngine {
    private MessageDigest digest;

    public MessageDigestEngine() {
    }

    @Override
    public KeyObject init(IFlexiProviderOperation operation) {
        LogUtil.logInfo("initializing message digest engine"); //$NON-NLS-1$
        this.operation = operation;
        try {
            digest = Registry.getMessageDigest(operation.getAlgorithmDescriptor().getAlgorithmName());
            initialized = true;
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                    "NoSuchAlgorithmException while initializing a message digest", e, false); //$NON-NLS-1$
        }

        return null;
    }

    @Override
    public void perform(KeyObject usedKey) {
        if (initialized) {
            InputStream inputStream = initInput(operation.getInput());
            OutputStream outputStream = initOutput(operation.getOutput());
            try {
                int i;
                while ((i = inputStream.read()) != -1) {
                    digest.update((byte) i);
                }
                outputStream.write(digest.digest());
                inputStream.close();
                outputStream.close();
                if (operation.getOutput().equals("<Editor>")) { //$NON-NLS-1$
                    EditorsManager.getInstance().openNewHexEditor(
                            AbstractEditorService.createOutputFile(new FileInputStream(new File(getOutputURI()))));
                }
            } catch (IOException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "IOException while performing a message digest", e, false); //$NON-NLS-1$
            } catch (PartInitException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, "Failed to open the Hexeditor", e, true);
            }
        }
    }

}

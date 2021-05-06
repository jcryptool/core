// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.dialogs.contentproviders;

import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.ui.dialogs.TableEntry;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.CertificateNode;

/**
 * @author Anatoli Barski
 * 
 */
public class CertificateContentProvider extends AbstractKeyNodeContentProvider {

    @Override
    public Object[] getElements(Object inputElement) {
        CertificateNode treeNode = (CertificateNode) inputElement;
        IKeyStoreAlias alias = treeNode.getAlias();

        try {
            Certificate cert = KeyStoreManager.getInstance().getCertificate(alias);
            Object[] certElements = { new TableEntry(Messages.CertificateContentProvider_CertType, cert.getType()) };
            return merge(super.getElements(inputElement), certElements);
        } catch (UnrecoverableEntryException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
        }

        return null;
    }
}

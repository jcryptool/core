// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair;

import java.util.ArrayList;
import java.util.List;

import org.jcryptool.crypto.keystore.ui.dialogs.TableEntry;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.CertificateContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.Messages;

import de.flexiprovider.api.exceptions.InvalidKeySpecException;
import de.flexiprovider.api.keys.Key;
import de.flexiprovider.pqc.pflash.PFlashKeyFactory;
import de.flexiprovider.pqc.pflash.PFlashPublicKey;
import de.flexiprovider.pqc.pflash.PFlashPublicKeySpec;

/**
 * @author Anatoli Barski
 * 
 */
public class PFlashPublicKeyContentProvider extends CertificateContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            PFlashPublicKey key = (PFlashPublicKey) inputElement;
            if (key == null)
                return null;

        } catch (ClassCastException e) {
            return null;
        }
        return paramElements;
    }

    @Override
    protected List<TableEntry> getKeySpecElements(Key key) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            PFlashKeyFactory keyFactory = new PFlashKeyFactory();
            PFlashPublicKeySpec keySpec = (PFlashPublicKeySpec) keyFactory.getKeySpec(key, PFlashPublicKeySpec.class);
            if (keySpec == null)
                return null;
            paramElements.add(new TableEntry(Messages.ContentProvider_oidstring, "" + keySpec.getOIDString())); //$NON-NLS-2$

        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
        return paramElements;
    }
}

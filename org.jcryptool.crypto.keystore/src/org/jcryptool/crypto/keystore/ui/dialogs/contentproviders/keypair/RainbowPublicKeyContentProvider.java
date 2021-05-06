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
import de.flexiprovider.pqc.rainbow.RainbowKeyFactory;
import de.flexiprovider.pqc.rainbow.RainbowPublicKey;
import de.flexiprovider.pqc.rainbow.RainbowPublicKeySpec;

/**
 * @author Anatoli Barski
 * 
 */
public class RainbowPublicKeyContentProvider extends CertificateContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            RainbowPublicKey key = (RainbowPublicKey) inputElement;
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
            RainbowKeyFactory keyFactory = new RainbowKeyFactory();
            RainbowPublicKeySpec keySpec = (RainbowPublicKeySpec) keyFactory
                    .getKeySpec(key, RainbowPublicKeySpec.class);
            if (keySpec == null)
                return null;
            paramElements.add(new TableEntry(Messages.ContentProvider_oidstring, "" + keySpec.getOIDString())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_doclength, "" + keySpec.getDocLength())); //$NON-NLS-2$

        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
        return paramElements;
    }
}

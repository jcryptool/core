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
import de.flexiprovider.pqc.ecc.mceliece.McElieceCCA2KeyFactory;
import de.flexiprovider.pqc.ecc.mceliece.McElieceCCA2PublicKey;
import de.flexiprovider.pqc.ecc.mceliece.McElieceCCA2PublicKeySpec;

/**
 * @author Anatoli Barski
 * 
 */
public class McElieceCCA2PublicKeyContentProvider extends CertificateContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            McElieceCCA2PublicKey key = (McElieceCCA2PublicKey) inputElement;
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
            McElieceCCA2KeyFactory keyFactory = new McElieceCCA2KeyFactory();
            McElieceCCA2PublicKeySpec keySpec = (McElieceCCA2PublicKeySpec) keyFactory.getKeySpec(key,
                    McElieceCCA2PublicKeySpec.class);
            if (keySpec == null)
                return null;

            paramElements.add(new TableEntry(Messages.ContentProvider_n, "" + keySpec.getN())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_t, "" + keySpec.getT())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_matrixg, "" + keySpec.getMatrixG())); //$NON-NLS-2$

        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
        return paramElements;
    }
}

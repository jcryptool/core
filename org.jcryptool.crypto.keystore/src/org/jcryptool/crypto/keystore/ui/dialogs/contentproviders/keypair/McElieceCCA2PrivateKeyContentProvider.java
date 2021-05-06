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
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.AbstractKeyNodeContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.Messages;

import de.flexiprovider.api.exceptions.InvalidKeySpecException;
import de.flexiprovider.api.keys.Key;
import de.flexiprovider.pqc.ecc.mceliece.McElieceCCA2KeyFactory;
import de.flexiprovider.pqc.ecc.mceliece.McElieceCCA2PrivateKey;
import de.flexiprovider.pqc.ecc.mceliece.McElieceCCA2PrivateKeySpec;

/**
 * @author Anatoli Barski
 * 
 */
public class McElieceCCA2PrivateKeyContentProvider extends AbstractKeyNodeContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            McElieceCCA2PrivateKey key = (McElieceCCA2PrivateKey) inputElement;
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
            McElieceCCA2PrivateKeySpec keySpec = (McElieceCCA2PrivateKeySpec) keyFactory.getKeySpec(key,
                    McElieceCCA2PrivateKeySpec.class);
            if (keySpec == null)
                return null;

            paramElements.add(new TableEntry(Messages.ContentProvider_k, "" + keySpec.getK())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_n, "" + keySpec.getN())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_field, "" + keySpec.getField())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_goppapoly, "" + keySpec.getGoppaPoly())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_h, "" + keySpec.getH())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_p, "" + keySpec.getP())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_qinv, "" + keySpec.getQInv())); //$NON-NLS-2$

        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
        return paramElements;
    }
}

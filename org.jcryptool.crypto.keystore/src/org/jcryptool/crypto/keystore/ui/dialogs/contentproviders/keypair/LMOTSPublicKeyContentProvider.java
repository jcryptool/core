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

import de.flexiprovider.pqc.ots.lm.LMOTSPublicKey;

/**
 * @author Anatoli Barski
 * 
 */
public class LMOTSPublicKeyContentProvider extends CertificateContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            LMOTSPublicKey key = (LMOTSPublicKey) inputElement;
            if (key == null)
                return null;
            paramElements.add(new TableEntry(Messages.ContentProvider_hashedk, "" + key.getHashedK())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_hashedl, "" + key.getHashedL())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_hashfunc, "" + key.getHashFunction())); //$NON-NLS-2$
        } catch (ClassCastException e) {
            return null;
        }
        return paramElements;
    }
}

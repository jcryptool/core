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

import de.flexiprovider.core.elgamal.ElGamalPublicKey;

/**
 * @author Anatoli Barski
 * 
 */
public class ElGamalPublicKeyContentProvider extends AbstractKeyNodeContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            ElGamalPublicKey key = (ElGamalPublicKey) inputElement;
            if (key == null)
                return null;
            paramElements.add(new TableEntry(Messages.ContentProvider_generator, "" + key.getGenerator())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_modulus, "" + key.getModulus())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_publica, "" + key.getPublicA())); //$NON-NLS-2$

        } catch (ClassCastException e) {
            return null;
        }
        return paramElements;
    }
}

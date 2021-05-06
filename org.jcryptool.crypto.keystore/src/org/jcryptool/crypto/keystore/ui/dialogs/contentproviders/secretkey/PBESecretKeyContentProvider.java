// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.secretkey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jcryptool.crypto.keystore.ui.dialogs.TableEntry;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.AbstractKeyNodeContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.Messages;

import de.flexiprovider.api.exceptions.InvalidKeySpecException;
import de.flexiprovider.api.keys.Key;
import de.flexiprovider.api.keys.SecretKey;
import de.flexiprovider.core.pbe.PBEKeyFactory;
import de.flexiprovider.core.pbe.PBEKeySpec;
import de.flexiprovider.ec.keys.ECSecretKey;

/**
 * @author Anatoli Barski
 * 
 */
public class PBESecretKeyContentProvider extends AbstractKeyNodeContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            ECSecretKey key = (ECSecretKey) inputElement;
            if (key == null)
                return null;

            paramElements.add(new TableEntry(Messages.ContentProvider_s, key.getS().toString()));

        } catch (ClassCastException e) {
            return null;
        }
        return paramElements;
    }

    @Override
    protected List<TableEntry> getKeySpecElements(Key key) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            PBEKeyFactory keyFactory = new PBEKeyFactory();
            PBEKeySpec keySpec = (PBEKeySpec) keyFactory.getKeySpec((SecretKey) key, PBEKeySpec.class);
            if (keySpec == null)
                return null;
            paramElements
                    .add(new TableEntry(Messages.ContentProvider_iterationcount, "" + keySpec.getIterationCount())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_keylength, "" + keySpec.getKeyLength())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_password,
                    "" + Arrays.toString(keySpec.getPassword()))); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_salt, "" + Arrays.toString(keySpec.getSalt()))); //$NON-NLS-2$

        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
        return paramElements;
    }
}

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
import de.flexiprovider.core.dsa.DSAKeyFactory;
import de.flexiprovider.core.dsa.DSAPrivateKeySpec;
import de.flexiprovider.core.dsa.interfaces.DSAKey;
import de.flexiprovider.core.dsa.interfaces.DSAParams;
import de.flexiprovider.core.dsa.interfaces.DSAPrivateKey;

/**
 * @author Anatoli Barski
 * 
 */
public class DSAPrivateKeyContentProvider extends AbstractKeyNodeContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            DSAPrivateKey key = (DSAPrivateKey) inputElement;
            if (key == null)
                return null;
            paramElements.add(new TableEntry(Messages.ContentProvider_valuex, key.getValueX().toString()));

            paramElements.addAll(getParameters(key));
        } catch (ClassCastException e) {
            return null;
        }
        return paramElements;
    }

    @Override
    protected List<TableEntry> getKeySpecElements(Key key) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            DSAKeyFactory keyFactory = new DSAKeyFactory();
            DSAPrivateKeySpec keySpec = (DSAPrivateKeySpec) keyFactory.getKeySpec(key, DSAPrivateKeySpec.class);
            if (keySpec == null)
                return null;
        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
        return paramElements;
    }

    private List<TableEntry> getParameters(DSAKey key) {
        DSAParams params = (DSAParams) key.getParams();
        List<TableEntry> paramElements = new ArrayList<TableEntry>();
        paramElements.add(new TableEntry(Messages.ContentProvider_baseg, "" + params.getBaseG())); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_primep, "" + params.getPrimeP())); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_primeq, "" + params.getPrimeQ())); //$NON-NLS-2$
        return paramElements;
    }
}

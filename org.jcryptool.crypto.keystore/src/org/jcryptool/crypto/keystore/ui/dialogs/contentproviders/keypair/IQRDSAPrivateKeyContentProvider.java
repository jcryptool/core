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
import de.flexiprovider.nf.iq.iqrdsa.IQRDSAKeyFactory;
import de.flexiprovider.nf.iq.iqrdsa.IQRDSAParameterSpec;
import de.flexiprovider.nf.iq.iqrdsa.IQRDSAPrivateKey;
import de.flexiprovider.nf.iq.iqrdsa.IQRDSAPrivateKeySpec;

/**
 * @author Anatoli Barski
 * 
 */
public class IQRDSAPrivateKeyContentProvider extends AbstractKeyNodeContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            IQRDSAPrivateKey key = (IQRDSAPrivateKey) inputElement;
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
            IQRDSAKeyFactory keyFactory = new IQRDSAKeyFactory();
            IQRDSAPrivateKeySpec keySpec = (IQRDSAPrivateKeySpec) keyFactory
                    .getKeySpec(key, IQRDSAPrivateKeySpec.class);
            if (keySpec == null)
                return null;

            paramElements.add(new TableEntry(Messages.ContentProvider_a, "" + keySpec.getA())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_alpha, "" + keySpec.getAlpha())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_gamma, "" + keySpec.getGamma())); //$NON-NLS-2$

            paramElements.addAll(getParameters(keySpec));
        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
        return paramElements;
    }

    private List<TableEntry> getParameters(IQRDSAPrivateKeySpec keySpec) {
        IQRDSAParameterSpec params = (IQRDSAParameterSpec) keySpec.getParams();
        List<TableEntry> paramElements = new ArrayList<TableEntry>();
        paramElements.add(new TableEntry(Messages.ContentProvider_discriminant, "" + params.getDiscriminant())); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_modulus, "" + params.getModulus())); //$NON-NLS-2$
        return paramElements;
    }
}

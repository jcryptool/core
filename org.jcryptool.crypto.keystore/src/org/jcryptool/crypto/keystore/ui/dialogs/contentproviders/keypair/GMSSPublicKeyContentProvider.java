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
import java.util.Arrays;
import java.util.List;

import org.jcryptool.crypto.keystore.ui.dialogs.TableEntry;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.CertificateContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.Messages;

import de.flexiprovider.api.exceptions.InvalidKeySpecException;
import de.flexiprovider.api.keys.Key;
import de.flexiprovider.pqc.hbc.gmss.GMSSKeyFactory;
import de.flexiprovider.pqc.hbc.gmss.GMSSParameterset;
import de.flexiprovider.pqc.hbc.gmss.GMSSPublicKey;
import de.flexiprovider.pqc.hbc.gmss.GMSSPublicKeySpec;

/**
 * @author Anatoli Barski
 * 
 */
public class GMSSPublicKeyContentProvider extends CertificateContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            GMSSPublicKey key = (GMSSPublicKey) inputElement;
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
            GMSSKeyFactory keyFactory = new GMSSKeyFactory();
            GMSSPublicKeySpec keySpec = (GMSSPublicKeySpec) keyFactory.getKeySpec(key, GMSSPublicKeySpec.class);
            if (keySpec == null)
                return null;
            paramElements.add(new TableEntry(Messages.ContentProvider_publickey,
                    "" + Arrays.toString(keySpec.getPublicKey()))); //$NON-NLS-2$
            paramElements.addAll(getParameters(keySpec));
        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
        return paramElements;
    }

    private List<TableEntry> getParameters(GMSSPublicKeySpec keySpec) {

        GMSSParameterset params = (GMSSParameterset) keySpec.getGMSSParameterset();
        List<TableEntry> paramElements = new ArrayList<TableEntry>();
        paramElements.add(new TableEntry(Messages.ContentProvider_numoflayers, "" + params.getNumOfLayers())); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_k, "" + Arrays.toString(params.getK()))); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_heightoftrees,
                "" + Arrays.toString(params.getHeightOfTrees()))); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_winternitzparameter,
                "" + Arrays.toString(params.getWinternitzParameter()))); //$NON-NLS-2$

        return paramElements;
    }
}

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
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.AbstractKeyNodeContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.Messages;

import de.flexiprovider.core.mprsa.MpRSAPrivateKey;

/**
 * @author Anatoli Barski
 * 
 */
public class MpRSAPrivateKeyContentProvider extends AbstractKeyNodeContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            MpRSAPrivateKey key = (MpRSAPrivateKey) inputElement;
            if (key == null)
                return null;

            paramElements.add(new TableEntry(Messages.ContentProvider_crtcoeff, "" + key.getCRTCoeff())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_crtcoefficient, "" + key.getCrtCoefficient())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_d, "" + key.getD())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_dp, "" + key.getDp())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_dq, "" + key.getDq())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_e, "" + key.getE())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_n, "" + key.getN())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_p, "" + key.getP()));
            paramElements.add(new TableEntry(Messages.ContentProvider_q, "" + key.getQ())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_modulus, "" + key.getModulus())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_primeexponentp, "" + key.getPrimeExponentP())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_primeexponentq, "" + key.getPrimeExponentQ())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_primep, "" + key.getPrimeP())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_primeq, "" + key.getPrimeQ())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_privateexponent, "" + key.getPrivateExponent())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_publicexponent, "" + key.getPublicExponent())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_otherprimeinfos,
                    "" + Arrays.toString(key.getOtherPrimeInfo()))); //$NON-NLS-2$

        } catch (ClassCastException e) {
            return null;
        }
        return paramElements;
    }

}

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

import de.flexiprovider.api.exceptions.InvalidKeyException;
import de.flexiprovider.ec.keys.ECPublicKey;
import de.flexiprovider.ec.parameters.CurveParams;

/**
 * @author Anatoli Barski
 * 
 */
public class ECPublicKeyContentProvider extends CertificateContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            ECPublicKey key = (ECPublicKey) inputElement;
            if (key == null)
                return null;
            paramElements.add(new TableEntry(Messages.ContentProvider_w, "" + key.getW())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_encodedw, "" + key.getEncodedW())); //$NON-NLS-2$

            paramElements.addAll(getParameters(key));
        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeyException e) {
            return null;
        }
        return paramElements;
    }

    private List<TableEntry> getParameters(ECPublicKey key) {
        CurveParams params = (CurveParams) key.getParams();
        List<TableEntry> paramElements = new ArrayList<TableEntry>();
        paramElements.add(new TableEntry(Messages.ContentProvider_k, "" + params.getK())); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_r, "" + params.getR())); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_oidstring, "" + params.getOID())); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_ellipticcurve, "" + params.getE())); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_g, "" + params.getG())); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_q, "" + params.getQ())); //$NON-NLS-2$
        return paramElements;
    }
}

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
import de.flexiprovider.core.elgamal.semanticallysecure.SSVElGamalKeyFactory;
import de.flexiprovider.core.elgamal.semanticallysecure.SSVElGamalPrivateKey;
import de.flexiprovider.core.elgamal.semanticallysecure.SSVElGamalPrivateKeySpec;

/**
 * @author Anatoli Barski
 * 
 */
public class SSVElGamalPrivateKeyContentProvider extends AbstractKeyNodeContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            SSVElGamalPrivateKey key = (SSVElGamalPrivateKey) inputElement;
            if (key == null)
                return null;
            paramElements.add(new TableEntry(Messages.ContentProvider_a, "" + key.getA())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_generator, "" + key.getGenerator())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_modulusp, "" + key.getModulusP())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_modulusq, "" + key.getModulusQ())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_publica, "" + key.getPublicA())); //$NON-NLS-2$

        } catch (ClassCastException e) {
            return null;
        }
        return paramElements;
    }

    @Override
    protected List<TableEntry> getKeySpecElements(Key key) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            SSVElGamalKeyFactory keyFactory = new SSVElGamalKeyFactory();
            SSVElGamalPrivateKeySpec keySpec = (SSVElGamalPrivateKeySpec) keyFactory.getKeySpec(key,
                    SSVElGamalPrivateKeySpec.class);
            if (keySpec == null)
                return null;

        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
        return paramElements;
    }
}

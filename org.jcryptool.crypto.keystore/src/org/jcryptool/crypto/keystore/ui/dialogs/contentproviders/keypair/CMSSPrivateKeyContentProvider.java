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

import de.flexiprovider.api.exceptions.InvalidKeySpecException;
import de.flexiprovider.api.keys.Key;
import de.flexiprovider.pqc.hbc.cmss.CMSSKeyFactory;
import de.flexiprovider.pqc.hbc.cmss.CMSSPrivateKey;
import de.flexiprovider.pqc.hbc.cmss.CMSSPrivateKeySpec;

/**
 * @author Anatoli Barski
 * 
 */
public class CMSSPrivateKeyContentProvider extends AbstractKeyNodeContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            CMSSPrivateKey key = (CMSSPrivateKey) inputElement;
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
            CMSSKeyFactory keyFactory = new CMSSKeyFactory();
            CMSSPrivateKeySpec keySpec = (CMSSPrivateKeySpec) keyFactory.getKeySpec(key, CMSSPrivateKeySpec.class);
            if (keySpec == null)
                return null;
            paramElements.add(new TableEntry(Messages.ContentProvider_activesubtree, "" + keySpec.getActiveSubtree())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_heightoftrees, "" + keySpec.getHeightOfTrees())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_indexmain, "" + keySpec.getIndexMain())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_indexsub, "" + keySpec.getIndexSub())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_oidstring, "" + keySpec.getOIDString())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_maintreeotskey,
                    "" + Arrays.toString(keySpec.getMaintreeOTSVerificationKey()))); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_masks, "" + keySpec.getMasks())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_seeds, "" + keySpec.getSeeds())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_subtreerootsig,
                    "" + Arrays.toString(keySpec.getSubtreeRootSig()))); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_authpaths,
                    "" + Arrays.toString(keySpec.getAuthPaths()))); //$NON-NLS-2$

        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
        return paramElements;
    }
}

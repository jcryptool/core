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
import de.flexiprovider.pqc.hbc.gmss.GMSSKeyFactory;
import de.flexiprovider.pqc.hbc.gmss.GMSSParameterset;
import de.flexiprovider.pqc.hbc.gmss.GMSSPrivateKey;
import de.flexiprovider.pqc.hbc.gmss.GMSSPrivateKeySpec;

/**
 * @author Anatoli Barski
 * 
 */
public class GMSSPrivateKeyContentProvider extends AbstractKeyNodeContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            GMSSPrivateKey key = (GMSSPrivateKey) inputElement;
            if (key == null)
                return null;

            paramElements.add(new TableEntry(Messages.ContentProvider_detailedname, Arrays.toString(key.getName())));

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
            GMSSPrivateKeySpec keySpec = (GMSSPrivateKeySpec) keyFactory.getKeySpec(key, GMSSPrivateKeySpec.class);
            if (keySpec == null)
                return null;
            paramElements.add(new TableEntry(Messages.ContentProvider_algnames,
                    "" + Arrays.toString(keySpec.getAlgNames()))); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_curauthpath, "" + keySpec.getCurrentAuthPath())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_curretain, "" + keySpec.getCurrentRetain())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_currootsig, "" + keySpec.getCurrentRootSig())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_curseed, "" + keySpec.getCurrentSeed())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_curstack, "" + keySpec.getCurrentStack())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_curtreehash, "" + keySpec.getCurrentTreehash())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_index, "" + Arrays.toString(keySpec.getIndex()))); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_keep, "" + keySpec.getKeep())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_mintreehash,
                    "" + Arrays.toString(keySpec.getMinTreehash()))); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_nextauthpath, "" + keySpec.getNextAuthPath())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_nextnextleaf,
                    "" + Arrays.toString(keySpec.getNextNextLeaf()))); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_nextnextroot,
                    "" + Arrays.toString(keySpec.getNextNextRoot()))); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_nextnextseed, "" + keySpec.getNextNextSeed())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_nextretain, "" + keySpec.getNextRetain())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_nextroot, "" + keySpec.getNextRoot())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_nextrootsig,
                    "" + Arrays.toString(keySpec.getNextRootSig()))); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_nextstack, "" + keySpec.getNextStack())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_nexttreehash, "" + keySpec.getNextTreehash())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_upperleaf,
                    "" + Arrays.toString(keySpec.getUpperLeaf()))); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_uppertreehashleaf,
                    "" + Arrays.toString(keySpec.getUpperTreehashLeaf()))); //$NON-NLS-2$

            paramElements.addAll(getParameters(keySpec));

        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
        return paramElements;
    }

    private List<TableEntry> getParameters(GMSSPrivateKeySpec keySpec) {

        GMSSParameterset params = (GMSSParameterset) keySpec.getGmssPS();
        List<TableEntry> paramElements = new ArrayList<TableEntry>();
        paramElements.add(new TableEntry(Messages.ContentProvider_numoflayers, "" + params.getNumOfLayers())); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_numoflayers, "" + Arrays.toString(params.getK()))); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_heightoftrees,
                "" + Arrays.toString(params.getHeightOfTrees()))); //$NON-NLS-2$
        paramElements.add(new TableEntry(Messages.ContentProvider_winternitzparameter,
                "" + Arrays.toString(params.getWinternitzParameter()))); //$NON-NLS-2$

        return paramElements;
    }
}

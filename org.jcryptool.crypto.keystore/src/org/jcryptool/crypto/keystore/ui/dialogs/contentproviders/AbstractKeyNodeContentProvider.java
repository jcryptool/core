// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.dialogs.contentproviders;

import java.security.UnrecoverableEntryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.ui.dialogs.TableEntry;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.AbstractKeyNode;

import de.flexiprovider.api.Cipher;
import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidKeyException;
import de.flexiprovider.api.exceptions.InvalidKeySpecException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.keys.Key;
import de.flexiprovider.api.keys.KeyFactory;
import de.flexiprovider.api.keys.KeySpec;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;

/**
 * an abstract class for classes which make use of AbstractKeyNode (e.g. CertificateNode, SecretKeyNode)
 * 
 * @author Anatoli Barski
 * 
 */
public class AbstractKeyNodeContentProvider extends CommonContentProvider {

    protected char[] password = KeyStoreManager.KEY_PASSWORD;

    @Override
    public Object[] getElements(Object inputElement) {

        Object[] elements = super.getElements(inputElement);

        Key key = getKey(inputElement);

        List<TableEntry> aliasElements = getAliasElements(inputElement);
        List<TableEntry> keyElements = getKeyElements(key);
        List<TableEntry> keySpecElements = getKeySpecElements(key);
        List<TableEntry> cipherElements = getCipherElements(key);
        List<TableEntry> algorithmElements = getAlgorithmElements(key);

        List<Object> abstractKeyNodeElements = new ArrayList<Object>();
        if (elements != null)
            abstractKeyNodeElements.addAll(Arrays.asList(elements));
        if (aliasElements != null)
            abstractKeyNodeElements.addAll(aliasElements);
        if (keyElements != null)
            abstractKeyNodeElements.addAll(keyElements);
        if (cipherElements != null)
            abstractKeyNodeElements.addAll(cipherElements);
        if (algorithmElements != null)
            abstractKeyNodeElements.addAll(algorithmElements);
        if (keySpecElements != null)
            abstractKeyNodeElements.addAll(keySpecElements);

        return abstractKeyNodeElements.toArray();
    }

    protected List<TableEntry> getKeySpecElements(Key key) {
        return null;
    }

    protected List<TableEntry> getAlgorithmElements(Object inputElement) {
        return null;
    }

    protected KeySpec getKeySpec(Key key) {
        try {
            KeyFactory keyFactory = Registry.getKeyFactory(key.getAlgorithm());
            KeySpec keySpec = keyFactory.getKeySpec(key, AlgorithmParameterSpec.class);
            return keySpec;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
    }

    private List<TableEntry> getKeyElements(Object inputElement) {
        Key key = (Key) inputElement;
        if (key == null)
            return null;

        List<TableEntry> list = new ArrayList<TableEntry>();

        list.add(new TableEntry(Messages.AbstractKeyNodeContentProvider_Algorithm, key.getAlgorithm()));
        list.add(new TableEntry(Messages.AbstractKeyNodeContentProvider_Format, key.getFormat()));
        String bytesToString = bytesToString(key.getEncoded());
		list.add(new TableEntry(Messages.AbstractKeyNodeContentProvider_Encoded, bytesToString));


        return list;
    }


    private List<TableEntry> getCipherElements(Object inputElement) {

        Key key = (Key) inputElement;
        if (key == null)
            return null;

        List<TableEntry> cipherElements = new ArrayList<TableEntry>();
        try {
            Cipher cipher = Registry.getCipher(key.getAlgorithm());
            if (cipher.getIV() != null)
                cipherElements.add(new TableEntry(Messages.AbstractKeyNodeContentProvider_InitVector, cipher.getIV()
                        .toString()));
            if (cipher.getBlockSize() != 0)
                cipherElements.add(new TableEntry(Messages.AbstractKeyNodeContentProvider_BlockSize, Integer
                        .toString(cipher.getBlockSize())));
            try {
                cipherElements.add(new TableEntry(Messages.AbstractKeyNodeContentProvider_CipherKeySize, Integer
                        .toString(cipher.getKeySize(key))));
            } catch (InvalidKeyException ex) {
                LogUtil.logError(ex);
            }
        } catch (NoSuchAlgorithmException e) {
            return cipherElements;
        }

        return cipherElements;
    }

    private List<TableEntry> getAliasElements(Object inputElement) {
        AbstractKeyNode node = (AbstractKeyNode) inputElement;
        IKeyStoreAlias alias = node.getAlias();
        List<TableEntry> elements = new ArrayList<TableEntry>();
        elements.add(new TableEntry(Messages.AbstractKeyNodeContentProvider_Alias, alias.getAliasString()));
        elements.add(new TableEntry(Messages.AbstractKeyNodeContentProvider_ContactName, alias.getContactName()));
        elements.add(new TableEntry(Messages.AbstractKeyNodeContentProvider_KeyLength, "" + alias.getKeyLength())); //$NON-NLS-2$
        elements.add(new TableEntry(Messages.AbstractKeyNodeContentProvider_Operation, alias.getOperation()));
        elements.add(new TableEntry(Messages.AbstractKeyNodeContentProvider_AsString, alias.toString()));
        return elements;
    }

    /**
     * Tries to retrieve the key from keystore using the default password. If the operation succeeds, the default
     * password will be updated, if it fails, the user have to enter a password into a prompt window.
     */
    protected Key getKey(Object inputElement) {
        AbstractKeyNode abstractKeyNode = (AbstractKeyNode) inputElement;
        IKeyStoreAlias alias = abstractKeyNode.getAlias();

        try {
            return KeyStoreManager.getInstance().getKey(alias, KeyStoreManager.KEY_PASSWORD);
        } catch (UnrecoverableEntryException ex) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "The entered password was not correct.", ex, true);
        } catch (java.security.NoSuchAlgorithmException ex) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "The requested algorithm is not supported.", ex, true);
        }

        return null;
    }

	public static byte[] listToArr(List<Byte> bytes) {
		byte[] result = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); i++) {
			byte b = bytes.get(i);
			result[i] = b;
		}
		return result;
	}

	private static String byteToString(byte b) {
		String hexString = Integer.toString(Byte.toUnsignedInt(b), 16);
		return hexString.length() == 1 ? "0"+hexString : hexString;
	}

	public static String bytesToString(List<Byte> content) {
		return bytesToString(listToArr(content));
	}

	public static String bytesToString(byte[] content) {
		LinkedList<String> result = new LinkedList<String>();
		for (byte b : content) {
			String byteToString = byteToString(b);
			result.add(byteToString);
		}
		String collect = result.stream().collect(Collectors.joining(" "));
		return collect;
	}
}

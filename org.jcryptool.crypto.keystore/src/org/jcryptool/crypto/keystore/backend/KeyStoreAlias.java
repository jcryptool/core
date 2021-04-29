// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.backend;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;

/**
 * EncodedContactName;KeyStoreEntryType;Operation;KeyLength;Provider;HashValue
 * 
 * @author tkern
 * 
 */
public class KeyStoreAlias implements IKeyStoreAlias {
    public static final String EVERYTHING_MATCHER = "everything"; //$NON-NLS-1$
    private static final String SEPARATOR = ";"; //$NON-NLS-1$
    private String encodedContactName = null;
    private KeyType keyType;
    private String opName;
    private int keyLength = -1;
    private boolean valid = false;
    private String hashValue;
    private String className;
    
    
    /**
     * 
     * @param contactName
     * @param keyType
     * @param opName
     * @param keyLength
     * @param hashValue
     * @param className
     */
    public KeyStoreAlias(String contactName, KeyType keyType, String opName, int keyLength, String hashValue,
            String className) {
        this.encodedContactName = encode(contactName);
        this.keyType = keyType;
        this.opName = encode(opName);
        this.keyLength = keyLength;
        this.hashValue = hashValue;
        this.className = encode(className);
        valid = true;
    }

    public KeyStoreAlias(String alias) {
        String[] elements = alias.split(SEPARATOR);
        if (elements.length == 6) {
            encodedContactName = elements[0];
            keyType = translate(elements[1]);
            opName = elements[2];
            keyLength = Integer.valueOf(elements[3]);
            hashValue = elements[4];
            className = elements[5];
            valid = true;
        } else {
            valid = false;
        }
    }

    public String getClassName() {
        return decode(className);
    }

    public String getContactName() {
        return decode(encodedContactName);
    }

    public String getEncodedContactName() {
        return encodedContactName;
    }

    public boolean isValid() {
        return valid;
    }

    public KeyType getKeyStoreEntryType() {
        return keyType;
    }

    public String getOperation() {
        return decode(opName);
    }

    /**
     * Returns whether a given key id/type (e. g. "RSA") matches the operation id (e. g. "RSA (OID: 0.232.4)") <br />
     * For the keyId "everything", this method returns true; <br />
     * dont rely too heavily on this method as it will be removed as soon as abetter alternative is found
     * 
     * @param keyId the key id
     * @return whether the key id/type and the operation id match
     */
    public boolean isOperationMatchingKeyId(String keyId) {
        return isOperationMatchingKeyId(getOperation(), keyId);
    }

    /**
     * Returns whether a given key id/type (e. g. "RSA") matches the operation id (e. g. "RSA (OID: 0.232.4)") <br />
     * For the keyId "everything", this method returns true; <br />
     * dont rely too heavily on this method as it will be removed as soon as abetter alternative is found
     * 
     * @param opId the operation id
     * @param keyId the key id
     * @return whether the key id/type and the operation id match
     */
    public static boolean isOperationMatchingKeyId(String opId, String keyId) {
        if (keyId.equals(EVERYTHING_MATCHER))
            return true; //$NON-NLS-1$
        return opId.matches(keyId + "(.*([^)]*))*"); //$NON-NLS-1$
    }

    public int getKeyLength() {
        return keyLength;
    }

    public String getHashValue() {
        return hashValue;
    }

    private KeyType translate(String type) {
        if (type.equals("public")) { //$NON-NLS-1$
            return KeyType.PUBLICKEY;
        } else if (type.equals("keypair.private")) { //$NON-NLS-1$
            return KeyType.KEYPAIR_PRIVATE_KEY;
        } else if (type.equals("keypair.public")) { //$NON-NLS-1$
            return KeyType.KEYPAIR_PUBLIC_KEY;
        } else if (type.equals("secret")) { //$NON-NLS-1$
            return KeyType.SECRETKEY;
        }
        return KeyType.UNKNOWN;
    }

    @Override
    public String toString() {
        return decode(encodedContactName) + SEPARATOR + keyType.getType() + SEPARATOR + decode(opName) + SEPARATOR
                + keyLength + SEPARATOR + hashValue + SEPARATOR + decode(className);
    }

    public String getAliasString() {
        return encodedContactName + SEPARATOR + keyType.getType() + SEPARATOR + opName + SEPARATOR + keyLength
                + SEPARATOR + hashValue + SEPARATOR + className;
    }

    /**
     * Encodes the given string to ASCII.
     * 
     * @param string The string parameter that will be encoded into ASCII
     * @return The corresponding ASCII value
     */
    private String encode(String string) {
        if (string == null) {
            return ""; //$NON-NLS-1$
        }
        StringBuffer sb = new StringBuffer();
        char c;
        for (int i = 0; i < string.length(); ++i) {
            c = string.charAt(i);
            String hex = Integer.toHexString(c).toUpperCase();
            for (int j = 0; j < 2 - hex.length(); j++) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * Decodes the given ASCII String and returns a 'human readable' version.
     * 
     * @param string The ASCII string
     * @return The 'human readable' version
     */
    private String decode(String string) {
        if (string == null) {
            return ""; //$NON-NLS-1$
        }
        if (string.length() % 2 != 0) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
                    "Cannot decode the given ASCII string, since it is not divisible by two"); //$NON-NLS-1$
            return ""; //$NON-NLS-1$
        }
        try {
            StringBuffer sb = new StringBuffer();
            char c1, c2;
            String c = ""; //$NON-NLS-1$
            for (int i = 0; i < string.length(); i++) {
                c1 = string.charAt(i);
                c2 = string.charAt(++i);
                c = Character.toString(c1) + Character.toString(c2);
                String asc = String.valueOf((char) Integer.parseInt(c, 16));
                sb.append(asc);
            }
            return sb.toString();
        } catch (NumberFormatException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "NumberFormatException while decoding an ASCII string", e, false); //$NON-NLS-1$
            return string;
        }
    }

}

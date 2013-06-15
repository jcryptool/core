// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;
import org.jcryptool.visual.extendedrsa.IdentityManager;
import org.junit.Test;

import de.flexiprovider.core.rsa.RSAPrivateCrtKey;

/**
 * testclass for the class "Identitymanager.java"
 * 
 * @author Christoph Schnepf, Patrick Zillner
 * 
 */
public class IdentityManagerTest {

    public final String ALICE = "Alice Whitehat";
    public final String TESTID = "testID";
    public final String PASSWORD = "1234";
    public final int LENGTH = 1024;

    @Test
    public void testGetInstance() {
        @SuppressWarnings("unused")
        ContactManager cm = ContactManager.getInstance();
    }

    @Test
    public void testCreateIdentity() {
        IdentityManager im = IdentityManager.getInstance();
        im.createIdentity(TESTID, "RSA", PASSWORD, LENGTH);
    }

    @Test
    public void testCreateIdentity_testAlgo() {
        IdentityManager im = IdentityManager.getInstance();
        im.createIdentity(TESTID, "abcd", PASSWORD, LENGTH);
        // i can't expect/throw the exception of the job (z.B. a NoSuchAlgorithmException in an other thread).. so it
        // only leaves a log-entry
    }

    @Test
    public void testCreateIdentity_testnull() {
        IdentityManager im = IdentityManager.getInstance();
        im.createIdentity(null, null, null, 0);
        // i can't expect/throw the exception of the job (z.B. a NoSuchAlgorithmException in an other thread).. so it
        // only leaves a log-entry
    }

    @Test
    public void testCreateMpRSAIdentity() {
        IdentityManager im = IdentityManager.getInstance();
        im.createMpRSAIdentity(ALICE, PASSWORD, LENGTH, 3);
    }

    @Test
    public void testCreateMpRSAIdentity_null() {
        IdentityManager im = IdentityManager.getInstance();
        im.createMpRSAIdentity(null, null, LENGTH, 0);
        // i can't expect/throw the exception of the job (z.B. a NoSuchAlgorithmException in an other thread).. so it
        // only leaves a log-entry
    }

    @Test
    public void testSaveRSAKeyToKeystore() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveRSAKeyToKeystore(TESTID, PASSWORD, null, new BigInteger("35"), new BigInteger("5"), new BigInteger("7"),
                new BigInteger("11"), new BigInteger("11"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveRSAKeyToKeystore_1() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveRSAKeyToKeystore(null, PASSWORD, null, new BigInteger("35"), new BigInteger("5"), new BigInteger("7"),
                new BigInteger("11"), new BigInteger("11"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveRSAKeyToKeystore_2() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveRSAKeyToKeystore(TESTID, null, null, new BigInteger("35"), new BigInteger("5"), new BigInteger("7"),
                new BigInteger("11"), new BigInteger("11"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveRSAKeyToKeystore_3() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveRSAKeyToKeystore(TESTID, PASSWORD, null, BigInteger.ZERO, new BigInteger("5"), new BigInteger("7"),
                new BigInteger("11"), new BigInteger("11"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveRSAKeyToKeystore_4() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveRSAKeyToKeystore(TESTID, PASSWORD, null, new BigInteger("35"), BigInteger.ZERO, new BigInteger("7"),
                new BigInteger("11"), new BigInteger("11"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveRSAKeyToKeystore_5() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveRSAKeyToKeystore(TESTID, PASSWORD, null, new BigInteger("35"), new BigInteger("5"), BigInteger.ZERO,
                new BigInteger("11"), new BigInteger("11"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveRSAKeyToKeystore_6() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveRSAKeyToKeystore(TESTID, PASSWORD, null, new BigInteger("35"), new BigInteger("5"), new BigInteger("7"),
                BigInteger.ZERO, new BigInteger("11"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveRSAKeyToKeystore_7() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveRSAKeyToKeystore(TESTID, PASSWORD, null, new BigInteger("35"), new BigInteger("5"), new BigInteger("7"),
                new BigInteger("11"), BigInteger.ZERO);
    }

    @Test
    public void testSaveMpRSAKeyToKeystore() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveMpRSAKeyToKeystore(TESTID, PASSWORD, null, 3, new BigInteger("385"), new BigInteger("5"),
                new BigInteger("7"), new BigInteger("11"), BigInteger.ZERO, BigInteger.ZERO, new BigInteger("11"),
                new BigInteger("131"));
    }

    public void testSaveMpRSAKeyToKeystore_4primes() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveMpRSAKeyToKeystore(TESTID, PASSWORD, null, 4, new BigInteger("5005"), new BigInteger("5"),
                new BigInteger("7"), new BigInteger("11"), new BigInteger("13"), BigInteger.ZERO, new BigInteger("11"),
                new BigInteger("1091"));
    }

    public void testSaveMpRSAKeyToKeystore_5primes() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveMpRSAKeyToKeystore(TESTID, PASSWORD, null, 5, new BigInteger("85085"), new BigInteger("5"),
                new BigInteger("7"), new BigInteger("11"), new BigInteger("13"), new BigInteger("11"), new BigInteger(
                        "17"), new BigInteger("3491"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveMpRSAKeyToKeystore_invalid1() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveMpRSAKeyToKeystore(TESTID, PASSWORD, null, 4, new BigInteger("385"), new BigInteger("5"),
                new BigInteger("7"), new BigInteger("11"), BigInteger.ZERO, BigInteger.ZERO, new BigInteger("11"),
                new BigInteger("131"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveMpRSAKeyToKeystore_invalid2() {
        IdentityManager im = IdentityManager.getInstance();
        im.saveMpRSAKeyToKeystore(TESTID, PASSWORD, null, 5, new BigInteger("385"), new BigInteger("5"),
                new BigInteger("7"), new BigInteger("11"), BigInteger.ZERO, BigInteger.ZERO, new BigInteger("11"),
                new BigInteger("131"));
    }

    @Test
    public void testGetAsymmetricKeyAlgorithms() {
        IdentityManager im = IdentityManager.getInstance();
        im.getAsymmetricKeyAlgorithms(ALICE);
    }

    @Test
    public void testGetAsymmetricKeyAlgorithms_invalid() {
        IdentityManager im = IdentityManager.getInstance();
        Vector<String> contacts = im.getAsymmetricKeyAlgorithms("testid123");
        assertEquals(0, contacts.size());
    }

    @Test
    public void testCountOwnKeys_0() {
        IdentityManager im = IdentityManager.getInstance();
        int keys = im.countOwnKeys("testid123");
        assertEquals(0, keys);
    }

    public void testCountOwnKeys() {
        IdentityManager im = IdentityManager.getInstance();
        int keys = im.countOwnKeys(ALICE);
        assertTrue(keys > 0);
    }

    @Test
    public void testGetPublicKeys_invalid() {
        IdentityManager im = IdentityManager.getInstance();
        HashMap<String, KeyStoreAlias> keys = im.getPublicKeys("testid123");
        assertEquals(0, keys.size());
    }

    @Test
    public void testGetPublicKeys() {
        IdentityManager im = IdentityManager.getInstance();
        HashMap<String, KeyStoreAlias> keys = im.getPublicKeys(ALICE);
        assertTrue(keys.size() > 0);
    }

    @Test
    public void testGetAttackablePublicKeys() {
        IdentityManager im = IdentityManager.getInstance();
        HashMap<String, KeyStoreAlias> keys = im.getAttackablePublicKeys(TESTID);
        assertTrue(keys.size() > 0);
    }

    @Test
    public void testLoadAllKeysForIdentityAndOtherPublics() {
        IdentityManager im = IdentityManager.getInstance();
        TreeMap<String, KeyStoreAlias> keys = im.loadAllKeysForIdentityAndOtherPublics(TESTID);
        assertTrue(keys.size() > 0);
    }

    @Test
    public void testGetAllRSAPubKeyParameters() {
        IdentityManager im = IdentityManager.getInstance();
        HashMap<String, KeyStoreAlias> keys = im.getPublicKeys(ALICE);
        for (KeyStoreAlias alias : keys.values()) {
            Vector<String> keyFacts = im.getAllRSAPubKeyParameters(alias);
            assertTrue(keyFacts.size() > 0);
        }
    }

    @Test
    public void testGetAllRSAPubKeyParameters_invalid() {
        IdentityManager im = IdentityManager.getInstance();
        HashMap<String, KeyStoreAlias> keys = im.getPublicKeys("asdf1234");
        for (KeyStoreAlias alias : keys.values()) {
            Vector<String> keyFacts = im.getAllRSAPubKeyParameters(alias);
            assertEquals(keyFacts.size(), 0);
        }
    }

    @Test
    public void testGetAllRSAPrivKeyParameters() {
        IdentityManager im = IdentityManager.getInstance();
        HashMap<String, KeyStoreAlias> keys = im.getPrivateKeys(ALICE);
        for (KeyStoreAlias alias : keys.values()) {
            if (alias.getClassName().substring(alias.getClassName().lastIndexOf('.') + 1).equals("RSAPrivateCrtKey")) {
                Vector<String> parameter = im.getAllRSAPrivKeyParameters(alias, PASSWORD);
                assertTrue(parameter.size() > 0);
            }
        }
    }

    // @Test
    // public void testGetAllMpRSAPrivKeyParameters() {
    // IdentityManager im = IdentityManager.getInstance();
    // HashMap<String,KeyStoreAlias> keys = im.getPrivateKeys(ALICE);
    // for (KeyStoreAlias alias : keys.values()){
    // if (alias.getClassName().substring(alias.getClassName().lastIndexOf('.')+1).equals("MpRSAPrivateKey")){
    // Vector<String> parameter = im.getAllMpRSAPrivKeyParameters(alias, PASSWORD);
    // assertTrue(parameter.size() > 0);
    // }
    // }
    // }

    @Test
    public void testGetPublicKeyParameters_invalid() {
        IdentityManager im = IdentityManager.getInstance();
        HashMap<String, KeyStoreAlias> keys = im.getPublicKeys("asdf1234");
        for (KeyStoreAlias alias : keys.values()) {
            Vector<String> keyFacts = im.getAllRSAPubKeyParameters(alias);
            assertEquals(keyFacts.size(), 0);
        }
    }

    @Test
    public void testGetPublicKeyParameters() {
        IdentityManager im = IdentityManager.getInstance();
        HashMap<String, KeyStoreAlias> keys = im.getPublicKeys(ALICE);
        for (KeyStoreAlias alias : keys.values()) {
            Vector<String> keyFacts = im.getAllRSAPubKeyParameters(alias);
            assertTrue(keyFacts.size() > 0);
        }
    }

    @Test
    public void testGetPrivateKeys() {
        IdentityManager im = IdentityManager.getInstance();
        HashMap<String, KeyStoreAlias> keys = im.getPrivateKeys(ALICE);
        assertTrue(keys.size() > 0);
    }

    @Test
    public void testGetPrivateKeys_inv() {
        IdentityManager im = IdentityManager.getInstance();
        HashMap<String, KeyStoreAlias> keys = im.getPrivateKeys("asd123");
        assertEquals(keys.size(), 0);
    }

    @Test
    public void testGetPrivateKeyParametersRSA() {
        IdentityManager im = IdentityManager.getInstance();
        HashMap<String, KeyStoreAlias> keys = im.getPrivateKeys(ALICE);
        for (KeyStoreAlias alias : keys.values()) {
            if (alias.getClassName().substring(alias.getClassName().lastIndexOf(':') + 1).endsWith("RSAPrivateCrtKey")) {
                RSAPrivateCrtKey key = im.getPrivateKey(alias, PASSWORD);
                Vector<BigInteger> keyFacts = im.getPrivateKeyParametersRSA(key);
                assertTrue(keyFacts.size() > 0);
            }
        }
    }

    @Test
    public void testGetPublicForPrivateRSA() {
        IdentityManager im = IdentityManager.getInstance();
        HashMap<String, KeyStoreAlias> keys = im.getPrivateKeys(ALICE);
        for (KeyStoreAlias alias : keys.values()) {
            if (alias.getClassName().substring(alias.getClassName().lastIndexOf(':') + 1).endsWith("RSAPrivateCrtKey")) {
                KeyStoreAlias pubToPriv = im.getPublicForPrivateRSA(alias);
                assertTrue(pubToPriv != null);
            }
        }
    }
}

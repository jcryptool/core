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

import java.util.Enumeration;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.extendedrsa.SecureMessage;
import org.junit.Test;

/**
 * test for the class "SecureMessage.java"
 * 
 * @author Christoph Schnepf, Patrick Zillner
 * 
 */
public class SecureMessageTest {

    public final String ALICE = "Alice Whitehat";

    @Test(expected = IllegalArgumentException.class)
    public void testSecureMessage_null1() {
        @SuppressWarnings("unused")
        SecureMessage message = new SecureMessage(null, 0, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSecureMessage_null2() {
        @SuppressWarnings("unused")
        SecureMessage message = new SecureMessage("asdf", 0, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSecureMessage_null3() {
        @SuppressWarnings("unused")
        SecureMessage message = new SecureMessage("asdf", 1, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSecureMessage_null4() {
        @SuppressWarnings("unused")
        SecureMessage message = new SecureMessage("asdf", 1, "sender", null, null);
    }

    @Test
    public void testSecureMessage_null5() {
        int count = 0;
        KeyStoreAlias alias = null;
        final Enumeration<String> aliases;
        aliases = KeyStoreManager.getInstance().getAliases();
        while (aliases != null && aliases.hasMoreElements()) {
            alias = new KeyStoreAlias(aliases.nextElement());

            if (alias.getContactName().equals(ALICE) && count < 1) {
                count++;
                @SuppressWarnings("unused")
                SecureMessage message = new SecureMessage("asdf", 1, "sender", alias, null);
            }
        }
    }

    @Test
    public void testSecureMessage_getmessage() {
        int count = 0;
        KeyStoreAlias alias = null;
        final Enumeration<String> aliases;
        aliases = KeyStoreManager.getInstance().getAliases();
        while (aliases != null && aliases.hasMoreElements()) {
            alias = new KeyStoreAlias(aliases.nextElement());

            if (alias.getContactName().equals(ALICE) && count < 1) {
                count++;
                SecureMessage message = new SecureMessage("asdf", 1, "sender", alias, null);
                assertEquals("asdf", message.getEncryptedMessage());
            }
        }
    }

    @Test
    public void testSecureMessage_getKeyID() {
        int count = 0;
        KeyStoreAlias alias = null;
        final Enumeration<String> aliases;
        aliases = KeyStoreManager.getInstance().getAliases();
        while (aliases != null && aliases.hasMoreElements()) {
            alias = new KeyStoreAlias(aliases.nextElement());

            if (alias.getContactName().equals(ALICE) && count < 1) {
                count++;
                SecureMessage message = new SecureMessage("asdf", 1, "sender", alias, null);
                assertEquals(1, message.getKeyID());
            }
        }
    }

    @Test
    public void testSecureMessage_getSender() {
        int count = 0;
        KeyStoreAlias alias = null;
        final Enumeration<String> aliases;
        aliases = KeyStoreManager.getInstance().getAliases();
        while (aliases != null && aliases.hasMoreElements()) {
            alias = new KeyStoreAlias(aliases.nextElement());

            if (alias.getContactName().equals(ALICE) && count < 1) {
                count++;
                SecureMessage message = new SecureMessage("asdf", 1, "sender", alias, null);
                assertEquals("sender", message.getSender());
            }
        }
    }

    @Test
    public void testSecureMessage_getRecipient() {
        int count = 0;
        KeyStoreAlias alias = null;
        final Enumeration<String> aliases;
        aliases = KeyStoreManager.getInstance().getAliases();
        while (aliases != null && aliases.hasMoreElements()) {
            alias = new KeyStoreAlias(aliases.nextElement());

            if (alias.getContactName().equals(ALICE) && count < 1) {
                count++;
                SecureMessage message = new SecureMessage("asdf", 1, "sender", alias, null);
                assertEquals(ALICE, message.getRecipient().getContactName());
            }
        }
    }

    @Test
    public void testSecureMessage_getSubject() {
        int count = 0;
        KeyStoreAlias alias = null;
        final Enumeration<String> aliases;
        aliases = KeyStoreManager.getInstance().getAliases();
        while (aliases != null && aliases.hasMoreElements()) {
            alias = new KeyStoreAlias(aliases.nextElement());

            if (alias.getContactName().equals(ALICE) && count < 1) {
                count++;
                SecureMessage message = new SecureMessage("asdf", 1, "sender", alias, "subject");
                assertEquals("subject", message.getSubject());
            }
        }
    }

    @Test
    public void testSecureMessage_getSubject_null() {
        int count = 0;
        KeyStoreAlias alias = null;
        final Enumeration<String> aliases;
        aliases = KeyStoreManager.getInstance().getAliases();
        while (aliases != null && aliases.hasMoreElements()) {
            alias = new KeyStoreAlias(aliases.nextElement());

            if (alias.getContactName().equals(ALICE) && count < 1) {
                count++;
                SecureMessage message = new SecureMessage("asdf", 1, "sender", alias, null);
                assertEquals(null, message.getSubject());
            }
        }
    }
}

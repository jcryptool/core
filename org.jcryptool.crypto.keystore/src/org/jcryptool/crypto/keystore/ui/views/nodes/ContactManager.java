// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.views.nodes;

import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IContactDescriptor;
import org.jcryptool.crypto.keystore.ui.views.interfaces.IKeyStoreListener;

public class ContactManager {
    /** Singleton instance */
    private static ContactManager instance;

    /** All contacts and their respective meta information */
    private Map<String, IContactDescriptor> contacts = Collections
            .synchronizedMap(new HashMap<String, IContactDescriptor>());

    private static List<IKeyStoreListener> listeners = new ArrayList<IKeyStoreListener>();

    private ITreeNode invisibleRoot;

    private ContactManager() {
    }

    public synchronized static ContactManager getInstance() {
        if (instance == null) {
            instance = new ContactManager();
            instance.initTreeModel();
        }
        return instance;
    }

    private void initTreeModel() {
        if (invisibleRoot == null) {
            init();
            invisibleRoot = new TreeNode("INVISIBLE_ROOT"); //$NON-NLS-1$
            for (IContactDescriptor desc : contacts.values()) {
                LogUtil.logInfo("adding: " + desc.getName()); //$NON-NLS-1$
                invisibleRoot.addChild(desc);
            }
            LogUtil.logInfo("children.length: " + invisibleRoot.getChildrenArray().length); //$NON-NLS-1$
        }
    }

    public void addKeyStoreListener(IKeyStoreListener listener) {
        listeners.add(listener);
    }

    public void removeKeyStoreListener(IKeyStoreListener listener) {
        listeners.remove(listener);
    }

    public Iterator<IKeyStoreListener> getKeyStoreListeners() {
        return listeners.iterator();
    }

    public ITreeNode getTreeModel() {
        return invisibleRoot;
    }

    private void init() {
        contacts.clear();
        Enumeration<String> aliases = null;

        try {
            aliases = KeyStoreManager.getInstance().getAliases();
        } catch (KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while accessing the aliases", e, true); //$NON-NLS-1$
        }

        while (aliases != null && aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            LogUtil.logInfo("Adding Entry " + alias); //$NON-NLS-1$
            addEntry(new KeyStoreAlias(alias));
        }
    }

    private void addEntry(KeyStoreAlias alias) {
        LogUtil.logInfo("Adding entry " + alias.getAliasString()); //$NON-NLS-1$

        if (contacts.containsKey(alias.getContactName())) {
            addEntryToContact(contacts.get(alias.getContactName()), alias);
        } else {
            IContactDescriptor contact = new ContactDescriptorNode(alias.getContactName());
            addEntryToContact(contact, alias);
            contacts.put(alias.getContactName(), contact);
        }
    }

    private void addEntryToContact(IContactDescriptor contact, KeyStoreAlias alias) {
        LogUtil.logInfo("Adding entry to contact " + alias.getAliasString()); //$NON-NLS-1$

        if (alias.getKeyStoreEntryType().equals(KeyType.SECRETKEY)) {
            contact.addSecretKey(alias);
        } else if (alias.getKeyStoreEntryType().equals(KeyType.PUBLICKEY)) {
            contact.addCertificate(alias);
        } else if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PRIVATE_KEY)) {
            contact.addKeyPair(alias, null);
        } else if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PUBLIC_KEY)) {
            contact.addKeyPair(null, alias);
        }
    }

    public boolean contactExists(String name) {
        return contacts.containsKey(name);
    }

    public void addContact(IContactDescriptor contact) {
        if (contactExists(contact.getName())) {
            LogUtil.logInfo("Contact name already exists"); //$NON-NLS-1$

            return;
        }

        contacts.put(contact.getName(), contact);
        notifyListeners();
    }

    public void removeContact(String contact) {
        LogUtil.logInfo("Removing contact " + contact); //$NON-NLS-1$

        invisibleRoot.removeChild(contacts.get(contact));
        contacts.remove(contact);
        notifyListeners();
    }

    private void notifyListeners() {
        Iterator<IKeyStoreListener> it = getKeyStoreListeners();
        while (it.hasNext()) {
            it.next().fireKeyStoreModified(invisibleRoot);
        }
    }

    public void removeEntry(KeyStoreAlias alias) {
        LogUtil.logInfo("Removing entry " + alias.getAliasString()); //$NON-NLS-1$

        if (alias.getKeyStoreEntryType().equals(KeyType.SECRETKEY)) {
            LogUtil.logInfo("removing a secret key"); //$NON-NLS-1$
            contacts.get(alias.getContactName()).removeSecretKey(alias);
        } else if (alias.getKeyStoreEntryType().equals(KeyType.PUBLICKEY)) {
            LogUtil.logInfo("removing a certificate"); //$NON-NLS-1$
            contacts.get(alias.getContactName()).removeCertificate(alias);
        } else if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PRIVATE_KEY)) {
            LogUtil.logInfo("removing a key pair"); //$NON-NLS-1$
            contacts.get(alias.getContactName()).removeKeyPair(alias);
        } else if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PUBLIC_KEY)) {
            LogUtil.logInfo("removing a key pair"); //$NON-NLS-1$
            contacts.get(alias.getContactName()).removeKeyPair(alias);
        }
    }

    public void addCertificate(KeyStoreAlias alias) {
        LogUtil.logInfo("Adding certificate " + alias.getAliasString()); //$NON-NLS-1$

        if (contactExists(alias.getContactName())) {
            contacts.get(alias.getContactName()).addCertificate(alias);
        } else {
            IContactDescriptor contact = new ContactDescriptorNode(alias.getContactName());
            contact.addCertificate(alias);
            contacts.put(alias.getContactName(), contact);
            invisibleRoot.addChild(contact);
            notifyListeners();
        }
    }

    public void addKeyPair(KeyStoreAlias privateKey, KeyStoreAlias publicKey) {
        LogUtil.logInfo("Adding key pair " + publicKey.getAliasString()); //$NON-NLS-1$

        if (contactExists(privateKey.getContactName())) {
            contacts.get(privateKey.getContactName()).addKeyPair(privateKey, publicKey);
        } else {
            IContactDescriptor contact = new ContactDescriptorNode(privateKey.getContactName());
            contact.addKeyPair(privateKey, publicKey);
            contacts.put(privateKey.getContactName(), contact);
            invisibleRoot.addChild(contact);
            notifyListeners();
        }
    }

    public void addSecretKey(KeyStoreAlias alias) {
        LogUtil.logInfo("Adding secret key " + alias.getAliasString()); //$NON-NLS-1$

        if (contactExists(alias.getContactName())) {
            contacts.get(alias.getContactName()).addSecretKey(alias);
        } else {
            IContactDescriptor contact = new ContactDescriptorNode(alias.getContactName());
            contact.addSecretKey(alias);
            contacts.put(alias.getContactName(), contact);
            invisibleRoot.addChild(contact);
            notifyListeners();
        }
    }

    public Iterator<IContactDescriptor> getContacts() {
        return contacts.values().iterator();
    }

    public int getContactSize() {
        LogUtil.logInfo("Contacts size is " + contacts.size()); //$NON-NLS-1$
        return contacts.size();
    }
}

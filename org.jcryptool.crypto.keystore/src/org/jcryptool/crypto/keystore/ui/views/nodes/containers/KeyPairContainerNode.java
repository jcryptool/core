//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.views.nodes.containers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.ui.views.interfaces.IKeyStoreListener;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.KeyPairNode;

public class KeyPairContainerNode extends AbstractContainerNode implements IKeyPairContainerNode {

//	List<KeyPairNode> nodes = new ArrayList<KeyPairNode>();
	private Map<String, KeyPairNode> nodes = new HashMap<String, KeyPairNode>();
	
	public KeyPairContainerNode() {
		super(Messages.getString("Label.KeyPairs")); //$NON-NLS-1$
	}
	
	/**
	 * @see org.jcryptool.crypto.keystore.ui.views.nodes.TreeNode#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return KeyStorePlugin.getImageDescriptor("icons/16x16/kgpg_key2.png"); //$NON-NLS-1$
	}

	public void add(KeyStoreAlias alias) {
	}

	public void remove(KeyStoreAlias alias) {
		KeyPairNode child = nodes.get(alias.getHashValue());
		nodes.remove(alias.getHashValue());
		removeChild(child);
		Iterator<IKeyStoreListener> it = ContactManager.getInstance().getKeyStoreListeners();
		while (it.hasNext()) {
			it.next().fireKeyStoreModified(this);
		}
	}

	
	public void addKeyPair(KeyStoreAlias privateKey, KeyStoreAlias publicKey) {
		if (privateKey != null && publicKey != null) {
			KeyPairNode child = new KeyPairNode(privateKey, publicKey);
			setAppropriateNameForNode(child);
			nodes.put(privateKey.getHashValue(), child);
			addChild(child);
		} else if (privateKey != null) {
			if (nodes.containsKey(privateKey.getHashValue())) {
				nodes.get(privateKey.getHashValue()).addPrivateKey(privateKey);
			} else {
				KeyPairNode child = new KeyPairNode(privateKey, null);
				setAppropriateNameForNode(child);
				nodes.put(privateKey.getHashValue(), child);
				addChild(child);
			}			
		} else if (publicKey != null) {
			if (nodes.containsKey(publicKey.getHashValue())) {
				nodes.get(publicKey.getHashValue()).addPublicKey(publicKey);
			} else {
				KeyPairNode child = new KeyPairNode(null, publicKey);
				setAppropriateNameForNode(child);
				nodes.put(publicKey.getHashValue(), child);
				addChild(child);	
			}			
		}		
		Iterator<IKeyStoreListener> it = ContactManager.getInstance().getKeyStoreListeners();
		while (it.hasNext()) {
			it.next().fireKeyStoreModified(this);
		}
	}

	/**
	 * Must be called BEFORE the child is added. appends a number to the name of
	 * the child to make it's name unique.
	 * 
	 * @param child
	 */
	private void setAppropriateNameForNode(KeyPairNode child) {
		if(getChild(child.getName()) != null) {
			while(getChild(child.getName()) != null) {
				child.incNameCounter();
			}
		}
	}
	
}

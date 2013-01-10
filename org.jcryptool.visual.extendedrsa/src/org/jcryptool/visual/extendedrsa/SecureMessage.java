//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * * This class represents a message in the message-queue
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class SecureMessage {
	
	private String encryptedMessage;
	private int keyID;
	private String sender;
	private KeyStoreAlias recipient;
	private String subject;
	private int messageID;
	
	/**
	 * contains a "secured message"
	 * @param encryptedMessage stands for the message itself.
	 * @param keyID represents the internal ID of the used key
	 * @param sender sender
	 * @param recipient recipient
	 * @param subject the subject
	 */
	public SecureMessage(String encryptedMessage, int keyID, String sender, KeyStoreAlias recipient, String subject) {
		if (encryptedMessage != null && keyID > 0 && sender != null && recipient != null){
		this.encryptedMessage = encryptedMessage;
		this.keyID = keyID;
		this.sender = sender;
		this.recipient = recipient;
		this.subject = subject;
		}else{
			throw new IllegalArgumentException("Error: null-values for the encryptedMessage, the sender and the recipient are not allowed! And the keyID has to be greater than 0!");
		}
	}

	public String getEncryptedMessage() {
		return encryptedMessage;
	}

	public void setEncryptedMessage(String encryptedMessage) {
		this.encryptedMessage = encryptedMessage;
	}

	public int getKeyID() {
		return keyID;
	}
	public void setKeyID(int keyID) {
		this.keyID = keyID;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public KeyStoreAlias getRecipient() {
		return recipient;
	}

	public void setRecipient(KeyStoreAlias recipient) {
		this.recipient = recipient;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
}

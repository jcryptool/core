//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa;

/**
 * * This class represents a message in the message-queue
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class SecureMessage {
	
	private byte[] encryptedMessage;
	private int keyID;
	private String sender;
	private String recipient;
	
	public byte[] getEncryptedMessage() {
		return encryptedMessage;
	}
	public void setEncryptedMessage(byte[] encryptedMessage) {
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
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
}

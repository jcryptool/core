// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

/**
 * This is only an extension to the normal "TabFolder" to save the messages somewhere.
 * 
 * @author Christoph Schnepf, Patrick Zillner
 * 
 */
public class ExtendedTabFolder extends TabFolder {

    private Vector<SecureMessage> messageQueue;
    private int messageID;

    public ExtendedTabFolder(Composite parent, int style) {
        super(parent, style);
        messageQueue = new Vector<SecureMessage>();
        messageID = 0;
    }

    public Vector<SecureMessage> getMessageQueue() {
        return messageQueue;
    }

    public void addMessageToQueue(SecureMessage message) {
        messageQueue.add(message);
        message.setMessageID(++messageID);
    }

    public SecureMessage getMessageAtIndex(int index) {
        return messageQueue.elementAt(index);
    }

    public void deleteMessageAtIndex(int index) {
        messageQueue.remove(index);
    }

    @Override
    protected void checkSubclass() {
    }

    public SecureMessage getMessageWithID(int messageID) {
        SecureMessage secM = null;
        for (SecureMessage mess : messageQueue) {
            if (mess.getMessageID() == messageID) {
                secM = mess;
            }
        }
        return secM;
    }

    public void deleteMessageWithID(int messageID) {
        SecureMessage secM = null;
        Iterator<SecureMessage> it = messageQueue.iterator();

        while (it.hasNext()) {
            secM = (SecureMessage) it.next();
            if (secM.getMessageID() == messageID) {
                it.remove();
            }
        }
    }
}

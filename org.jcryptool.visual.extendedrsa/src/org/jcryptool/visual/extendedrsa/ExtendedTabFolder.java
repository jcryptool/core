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

import java.util.Vector;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
/**
 * This is only an extension to the normal "TabFolder" to save the messages somewhere.
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class ExtendedTabFolder extends TabFolder{

	private Vector<SecureMessage> messageQueue;
	
	public ExtendedTabFolder(Composite parent, int style) {
		super(parent, style);
	}

	public Vector<SecureMessage> getMessageQueue() {
		return messageQueue;
	}

	public void setMessageQueue(Vector<SecureMessage> messageQueue) {
		this.messageQueue = messageQueue;
	}
	@Override 
	protected void checkSubclass() { 
	}
}

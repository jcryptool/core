//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.ui.util;

import org.eclipse.swt.widgets.Control;

public class NestedEnableDisableSwitcher extends NestedEnable {

	public boolean alwaysRefresh = false;
	private boolean startState;

	public NestedEnableDisableSwitcher(Control root) {
		super(root);
		startState = root.isEnabled();
	}

	@Override
	public void setEnabled(boolean b) {
		if(alwaysRefresh || getRoot().isEnabled() == startState) {
			reset();
		}
		super.setEnabled(b);
	}
	
}

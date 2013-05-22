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

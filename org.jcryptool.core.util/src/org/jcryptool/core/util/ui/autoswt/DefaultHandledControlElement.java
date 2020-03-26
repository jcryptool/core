package org.jcryptool.core.util.ui.autoswt;

import org.eclipse.swt.widgets.Control;

public class DefaultHandledControlElement implements IHandledUIElement<Control> {
	
	private Object registrationKey;
	private Control rootControl;

	public DefaultHandledControlElement(Control root, Object registrationKey) {
		this.rootControl = root;
		this.registrationKey = registrationKey;
	}

	public DefaultHandledControlElement(Control root) {
		this(root, root);
	}
	
	
	@Override
	public Object getRegistrationKey() {
		return this.registrationKey;
	}
	
	@Override
	public Control getRootControl() {
		return this.rootControl;
	}

}

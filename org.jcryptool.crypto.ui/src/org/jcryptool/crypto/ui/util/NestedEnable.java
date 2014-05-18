package org.jcryptool.crypto.ui.util;

import java.util.IdentityHashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jcryptool.core.logging.utils.LogUtil;

public class NestedEnable {
	
	private Map<Control, Boolean> enabledStates;
	
	private Control root;

	public NestedEnable(Control root) {
		this.root = root;
		reset();
	}
	
	public void reset() {
		this.enabledStates = new IdentityHashMap<Control, Boolean>();
	}
	
	private void recurseEnableOnComposite(Composite c, boolean b) {
		Control[] children = c.getChildren();
		for(Control child: children) {
			if(child != null && !child.isDisposed()) {
				setEnabled(child, b);
			}
		}
	}
	
	private void setEnabled(Control c, boolean b) {
		boolean neverSeenBefore = !enabledStates.containsKey(c);
		boolean ownImplementation = hasOwnEnableImplementation(c);
		
		if(neverSeenBefore) {
			enabledStates.put(c, c.isEnabled());
			
			if(c instanceof Composite && !ownImplementation) {
				Composite composite = (Composite) c;
				recurseEnableOnComposite(composite, b);
			}
			c.setEnabled(b);
		} else {
			boolean stateBeforeEncounteringLastTime = enabledStates.get(c);
			boolean wasAlreadyAsRequiredLastTime = (c.isEnabled() == stateBeforeEncounteringLastTime);
			
			enabledStates.put(c, c.isEnabled());
			
			if(c instanceof Composite && !ownImplementation) {
				Composite composite = (Composite) c;
				recurseEnableOnComposite(composite, b);
			}
			if(!wasAlreadyAsRequiredLastTime) {
				c.setEnabled(b);
			}
		}
	}
	
	private boolean hasOwnEnableImplementation(Control c) {
		try {
			String swtPackagePath = "org.eclipse.swt";
			Class<?> definingClass;
			definingClass = c.getClass().getMethod("setEnabled", boolean.class).getDeclaringClass();
			String implementingClassName = definingClass.getName();
			return !implementingClassName.contains(swtPackagePath);
		} catch (SecurityException ex) {
		    LogUtil.logError(ex);
		} catch (NoSuchMethodException ex) {
		    LogUtil.logError(ex);
		}
		return false;
	}

	public void setEnabled(boolean b) {
		setEnabled(root, b);
	}
	
	public Control getRoot() {
		return root;
	}
	
}

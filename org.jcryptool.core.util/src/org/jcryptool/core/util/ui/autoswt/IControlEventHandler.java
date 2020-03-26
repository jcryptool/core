package org.jcryptool.core.util.ui.autoswt;

import org.eclipse.swt.widgets.Control;

/**
 * interface that may be implemented to handle all sorts of Control lifecycle events
 * 
 * @author simon
 *
 */
public interface IControlEventHandler {
	
	public default void onSWTEvent(LayoutAdvisor advisor, Control c, int swtConstant, Object swtArgument) {
		
	}
	/**
	 * This method is invoked after initialization of a control (may happen multiple times)
	 * 
	 */
	public default void onIninitializationFinished(LayoutAdvisor advisor, Control c) {
	}

}

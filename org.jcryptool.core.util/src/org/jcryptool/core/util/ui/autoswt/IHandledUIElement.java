package org.jcryptool.core.util.ui.autoswt;

import org.eclipse.swt.widgets.Control;

public interface IHandledUIElement<CT extends Control> {

	/**
	 * Sometimes this is a JFace page or ViewPart, etc
	 * Sometimes this is just the root control.
	 * 
	 * @return
	 */
	public Object getRegistrationKey();

	/**
	 * Return the SWT control that is the root of the handled element
	 * @return
	 */
	public CT getRootControl();

}

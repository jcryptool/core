package org.jcryptool.visual.crtverification;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.jcryptool.visual.crtverification.views.CrtVerView;
public class viewController extends CrtVerView {

	public viewController(Composite parent, int style) {
		super(parent, style);
		DateTime date = new DateTime(this, SWT.None);
		
		// TODO Auto-generated constructor stub
	}
	
}

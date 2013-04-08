package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

public class ShowCert implements Views {
	Composite composite;
	Composite left;
	Composite right;
	List lst_certs;
	Label lbl_organization;
	Label lbl_common;
	Label lbl_mail;
	
	public ShowCert(Composite content){
        composite = new Composite(content, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        left = new Composite(composite, SWT.None);
        right = new Composite(composite, SWT.NONE);
        
        lst_certs = new List(left, SWT.BORDER);
        
        lbl_organization = new Label(right, SWT.NONE);
        lbl_common = new Label(right, SWT.NONE);
        lbl_mail = new Label(right, SWT.NONE);
        
        composite.setVisible(false);
        
	}
	
	public void setVisible(boolean visible){
		composite.setVisible(visible);
	}
}

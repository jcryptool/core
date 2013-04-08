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
        
        left = new Composite(composite, SWT.FILL);
	    left.setLayout(new GridLayout(1, false));
        right = new Composite(composite, SWT.NONE);
        right.setLayout(new GridLayout(1, false));
        
        lst_certs = new List(left, SWT.BORDER);
        
        lbl_organization = new Label(right, SWT.NONE);
        lbl_organization.setText("org");
        lbl_common = new Label(right, SWT.NONE);
        lbl_common.setText("common");
        lbl_mail = new Label(right, SWT.NONE);
        lbl_mail.setText("mail");
        composite.setVisible(false);
        
	}
	
	public void dispose(){
		this.composite.dispose();
	}
	
	public void setVisible(boolean visible){
		composite.setVisible(visible);
	}
}

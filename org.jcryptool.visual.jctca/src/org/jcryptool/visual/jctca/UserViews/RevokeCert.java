package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class RevokeCert implements Views {

	Composite composite;
	Combo cmb_certs;
	Button btn_revoke;
	public RevokeCert(Composite content){
		composite = new Composite(content, SWT.None);
		composite.setLayout(new GridLayout(1, true));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		cmb_certs = new Combo(composite, SWT.READ_ONLY | SWT.DROP_DOWN);
		cmb_certs.add("Cert #1");
		cmb_certs.add("cert #2");
		cmb_certs.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_revoke = new Button(composite, SWT.NONE);
		btn_revoke.setText("Revoke Certificate");
		btn_revoke.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		composite.setVisible(false);
	}
	
	public void dispose(){
		composite.dispose();
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		composite.setVisible(visible);
	}
	
	
}

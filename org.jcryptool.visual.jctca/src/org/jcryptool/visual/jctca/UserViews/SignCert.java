package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SignCert implements Views {

	Composite composite;

	public SignCert(Composite content) {
		this.composite = new Composite(content, SWT.BORDER);
		this.composite.setLayout(new GridLayout(1, false));
		GridData gd_comp = new GridData(SWT.FILL, SWT.TOP, true, true);
		composite.setLayoutData(gd_comp);
		
		Button btn_select_file = new Button(composite, SWT.NONE);
		btn_select_file.setText("Datei auswählen...");
		Label lbl_or = new Label(composite, SWT.CENTER);
		lbl_or.setText("oder");
		
		GridData gd_txt = new GridData(SWT.FILL, SWT.FILL, true, true,1,20);
		Text txt_sign = new Text(composite, SWT.LEFT | SWT.MULTI | SWT.BORDER);
		txt_sign.setText("...Text zum Signieren eingeben...");
		txt_sign.setLayoutData(gd_txt);
		
		Combo cmb_priv_key = new Combo(composite, SWT.DROP_DOWN);
		cmb_priv_key.add("Privater Schlüssel #1");
		cmb_priv_key.add("Privater Schlüssel #2");
		cmb_priv_key.add("Privater Schlüssel #3");
		cmb_priv_key.add("Privater Schlüssel #4");
		cmb_priv_key.select(0);
		
		
		
		Button btn_sign = new Button(composite, SWT.NONE);
		btn_sign.setText("Mit ausgewähltem Schlüssel signieren");
		
		
		composite.setVisible(false);
	}

	@Override
	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}
	
	public void dispose(){
		composite.dispose();
	}

}

package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CreateCert implements Views{

	Composite composite;
	
	Label lbl_common;
	Text txt_common;
	
	Label lbl_org;
	Text txt_org;
	
	Label lbl_orgUnit;
	Text txt_orgUnit;
	
	Label lbl_city;
	Text txt_city;
	
	Label lbl_state;
	Text txt_state;
	
	Label lbl_country;
	Text txt_country;
	
	Label lbl_mail;
	Text txt_mail;
	
	Label lbl_proof;
	Button btn_proof;
	
	Label lbl_key;
	Button btn_key;
	
	Label lbl_plain;
	Button btn_send;
	
	Label lbl_plain1;
	Button btn_genKey;
	
	public CreateCert(Composite content){
        composite = new Composite(content, SWT.NONE);
        composite.setLayout(new GridLayout(2, true));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        lbl_common = new Label(composite, SWT.NONE);
        lbl_common.setText("Common Name");    
        txt_common = new Text(composite, SWT.SINGLE | SWT.BORDER);
        txt_common.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_org = new Label(composite, SWT.None);
        lbl_org.setText("Organisation");
        txt_org = new Text(composite, SWT.SINGLE | SWT.BORDER);
        txt_org.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_orgUnit = new Label(composite, SWT.None);
        lbl_orgUnit.setText("Organisational Unit");
        txt_orgUnit = new Text(composite, SWT.SINGLE | SWT.BORDER);
        txt_orgUnit.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_city = new Label(composite, SWT.None);
        lbl_city.setText("City or Locality");
        txt_city = new Text(composite, SWT.SINGLE | SWT.BORDER);
        txt_city.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_state = new Label(composite, SWT.None);
        lbl_state.setText("State or Province");
        txt_state = new Text(composite, SWT.SINGLE | SWT.BORDER);
        txt_state.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_country = new Label(composite, SWT.None);
        lbl_country.setText("Country");
        txt_country = new Text(composite, SWT.SINGLE | SWT.BORDER);
        txt_country.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_mail = new Label(composite, SWT.None);
        lbl_mail.setText("E-Mail");
        txt_mail = new Text(composite, SWT.BORDER | SWT.SINGLE);
        txt_mail.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_proof = new Label(composite, SWT.None);
        lbl_proof.setText("Proof of Identity");
        btn_proof = new Button(composite, SWT.None);
        btn_proof.setText("Select File");
        btn_proof.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_key = new Label(composite, SWT.None);
        lbl_key.setText("Public Key");
        btn_key = new Button(composite, SWT.NONE);
        btn_key.setText("Select extisting public key");
        btn_key.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_plain = new Label(composite, SWT.NONE);
        btn_genKey = new Button(composite, SWT.NONE);
        btn_genKey.setText("Generate a key pair");
        btn_genKey.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_plain1 = new Label(composite, SWT.None);
        btn_send = new Button(composite, SWT.NONE);
        btn_send.setText("Send certificate signing request");
        btn_send.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
                
        composite.setVisible(false);
	}
	
	public void dispose(){
		this.composite.dispose();
	}
	
	public void setVisible(boolean visible){
		this.composite.setVisible(visible);
	}
}

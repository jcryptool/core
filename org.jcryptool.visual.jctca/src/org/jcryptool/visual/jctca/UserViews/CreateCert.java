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
	
	public CreateCert(Composite content){
        composite = new Composite(content, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        lbl_common = new Label(composite, SWT.NONE);
        lbl_common.setText("Common Name");    
        txt_common = new Text(composite, SWT.SINGLE | SWT.BORDER);
        
        lbl_org = new Label(composite, SWT.None);
        lbl_org.setText("Organisation");
        txt_org = new Text(composite, SWT.SINGLE | SWT.BORDER);
        
        lbl_orgUnit = new Label(composite, SWT.None);
        lbl_orgUnit.setText("Organisational Unit");
        txt_orgUnit = new Text(composite, SWT.SINGLE | SWT.BORDER);
        
        lbl_city = new Label(composite, SWT.None);
        lbl_city.setText("City or Locality");
        txt_city = new Text(composite, SWT.SINGLE | SWT.BORDER);
        
        lbl_state = new Label(composite, SWT.None);
        lbl_state.setText("State or Province");
        txt_state = new Text(composite, SWT.SINGLE | SWT.BORDER);
        
        lbl_country = new Label(composite, SWT.None);
        lbl_country.setText("Country");
        txt_country = new Text(composite, SWT.SINGLE | SWT.BORDER);
        
        lbl_mail = new Label(composite, SWT.None);
        lbl_mail.setText("E-Mail");
        txt_mail = new Text(composite, SWT.BORDER | SWT.SINGLE);
        
        lbl_proof = new Label(composite, SWT.None);
        lbl_proof.setText("Proof of Identity");
        btn_proof = new Button(composite, SWT.None);
        btn_proof.setText("Select File");
        
        lbl_key = new Label(composite, SWT.None);
        lbl_key.setText("Public Key");
        btn_key = new Button(composite, SWT.NONE);
        btn_key.setText("Select extisting public key");
         
        lbl_plain = new Label(composite, SWT.None);
        btn_send = new Button(composite, SWT.NONE);
        btn_send.setText("Send certificate signing request");
                
        composite.setVisible(false);
	}
	
	public void dispose(){
		this.composite.dispose();
	}
	
	public void setVisible(boolean visible){
		this.composite.setVisible(visible);
	}
}

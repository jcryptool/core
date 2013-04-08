package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CreateCert implements Views{

	Composite right1;
	
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
        right1 = new Composite(content, SWT.NONE);
        right1.setLayout(new GridLayout(2, false));
        right1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        lbl_common = new Label(right1, SWT.NONE);
        lbl_common.setText("Common Name");    
        txt_common = new Text(right1, SWT.SINGLE | SWT.BORDER);
        
        lbl_org = new Label(right1, SWT.None);
        lbl_org.setText("Organisation");
        txt_org = new Text(right1, SWT.SINGLE | SWT.BORDER);
        
        lbl_orgUnit = new Label(right1, SWT.None);
        lbl_orgUnit.setText("Organisational Unit");
        txt_orgUnit = new Text(right1, SWT.SINGLE | SWT.BORDER);
        
        lbl_city = new Label(right1, SWT.None);
        lbl_city.setText("City or Locality");
        txt_city = new Text(right1, SWT.SINGLE | SWT.BORDER);
        
        lbl_state = new Label(right1, SWT.None);
        lbl_state.setText("State or Province");
        txt_state = new Text(right1, SWT.SINGLE | SWT.BORDER);
        
        lbl_country = new Label(right1, SWT.None);
        lbl_country.setText("Country");
        txt_country = new Text(right1, SWT.SINGLE | SWT.BORDER);
        
        lbl_mail = new Label(right1, SWT.None);
        lbl_mail.setText("E-Mail");
        txt_mail = new Text(right1, SWT.BORDER | SWT.SINGLE);
        
        lbl_proof = new Label(right1, SWT.None);
        lbl_proof.setText("Proof of Identity");
        btn_proof = new Button(right1, SWT.None);
        btn_proof.setText("Select File");
        
        lbl_key = new Label(right1, SWT.None);
        lbl_key.setText("Public Key");
        btn_key = new Button(right1, SWT.NONE);
        btn_key.setText("Select extisting public key");
         
        lbl_plain = new Label(right1, SWT.None);
        btn_send = new Button(right1, SWT.NONE);
        btn_send.setText("Send certificate signing request");
                
        right1.setVisible(false);
	}
	
	public void setVisible(boolean visible){
		this.right1.setVisible(visible);
	}
}

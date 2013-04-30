package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CreateCert implements Views{

	Composite composite;
	
	Label lbl_firstname;
	Text txt_firstname;
	
	Label lbl_lastname;
	Text txt_lastname;
	
	Label lbl_street;
	Text txt_street;
	
	Label lbl_ZIP;
	Text txt_ZIP;
	
	Label lbl_city;
	Text txt_city;
	
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
	
	
	public CreateCert(Composite content, Group exp){
        composite = new Composite(content, SWT.NONE);
        composite.setLayout(new GridLayout(1, true));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, true));

        Group createCertGroup = new Group(composite, SWT.NONE);
		createCertGroup.setLayout(new GridLayout(2, false));
		GridData gd_grp = new GridData(SWT.FILL, SWT.NONE, true, true);
		createCertGroup.setLayoutData(gd_grp);
		createCertGroup.setText(Messages.CreateCert_create_csr_headline);
        
		
        
        lbl_firstname = new Label(createCertGroup, SWT.NONE);
        lbl_firstname.setText(Messages.CreateCert_first_name);    
        txt_firstname = new Text(createCertGroup, SWT.SINGLE | SWT.BORDER);
        txt_firstname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_lastname = new Label(createCertGroup, SWT.None);
        lbl_lastname.setText(Messages.CreateCert_last_name);
        txt_lastname = new Text(createCertGroup, SWT.SINGLE | SWT.BORDER);
        txt_lastname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_street = new Label(createCertGroup, SWT.None);
        lbl_street.setText(Messages.CreateCert_street);
        txt_street = new Text(createCertGroup, SWT.SINGLE | SWT.BORDER);
        txt_street.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_ZIP = new Label(createCertGroup, SWT.None);
        lbl_ZIP.setText(Messages.CreateCert_zip);
        txt_ZIP = new Text(createCertGroup, SWT.SINGLE | SWT.BORDER);
        txt_ZIP.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_city = new Label(createCertGroup, SWT.None);
        lbl_city.setText(Messages.CreateCert_city);
        txt_city = new Text(createCertGroup, SWT.SINGLE | SWT.BORDER);
        txt_city.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_country = new Label(createCertGroup, SWT.None);
        lbl_country.setText(Messages.CreateCert_country);
        txt_country = new Text(createCertGroup, SWT.SINGLE | SWT.BORDER);
        txt_country.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_mail = new Label(createCertGroup, SWT.None);
        lbl_mail.setText(Messages.CreateCert_email);
        txt_mail = new Text(createCertGroup, SWT.BORDER | SWT.SINGLE);
        txt_mail.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_proof = new Label(createCertGroup, SWT.None);
        lbl_proof.setText(Messages.CreateCert_identity_file);
        btn_proof = new Button(createCertGroup, SWT.None);
        btn_proof.setText(Messages.CreateCert_chose_file_btn);
        btn_proof.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        
        lbl_key = new Label(createCertGroup, SWT.None);
        lbl_key.setText(Messages.CreateCert_pub_key);
        btn_key = new Button(createCertGroup, SWT.NONE);
        btn_key.setText(Messages.CreateCert_chose_pub_key_btn);
        btn_key.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_plain = new Label(createCertGroup, SWT.NONE);
        btn_genKey = new Button(createCertGroup, SWT.NONE);
        btn_genKey.setText(Messages.CreateCert_gen_keypair_btn);
        btn_genKey.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
//        lbl_plain1 = new Label(createCertGroup, SWT.None);
        btn_send = new Button(composite, SWT.NONE);
        btn_send.setText(Messages.CreateCert_send_csr_btn);
        btn_send.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
                
        Label lbl_exp = (Label)exp.getChildren()[0];
        lbl_exp.setText(Messages.CreateCert_exp_txt0 +
        		Messages.CreateCert_exp_txt1 +
        		Messages.CreateCert_exp_txt2 +
        		Messages.CreateCert_exp_txt3);
        composite.setVisible(false);
	}
	
	public void dispose(){
		this.composite.dispose();		
	}
	
	public void setVisible(boolean visible){
		this.composite.setVisible(visible);
	}
}

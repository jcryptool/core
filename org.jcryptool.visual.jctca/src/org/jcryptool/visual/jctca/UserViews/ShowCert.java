package org.jcryptool.visual.jctca.UserViews;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;

public class ShowCert implements Views {
	Composite composite;
	Composite left;
	Composite right;
	List lst_certs;
	Label lbl_plain, lbl_plain1, lbl_plain2, lbl_plain3, lbl_plain4, lbl_plain5, lbl_plain6, lbl_plain7, lbl_plain8;
	
	Label lbl_issued_to;
	Label lbl_issued_by;
	
	Label lbl_common;
	Label lbl_value_common;
	Label lbl_common_by;
	Label lbl_value_common_by;

	Label lbl_org;
	Label lbl_value_org;
	Label lbl_org_by;
	Label lbl_value_org_by;

	Label lbl_orgUnit;
	Label lbl_value_orgUnit;
	Label lbl_orgUnit_by;
	Label lbl_value_orgUnit_by;
	
	Label lbl_city;
	Label lbl_value_city;

	Label lbl_state;
	Label lbl_value_state;

	Label lbl_country;
	Label lbl_value_country;

	Label lbl_mail;
	Label lbl_value_mail;
	
	Label lbl_issued_on;
	Label lbl_value_issued_on;
	Label lbl_expired_on;
	Label lbl_value_expired_on;

	Button btn_revoke;
	
	KeyStoreManager ksm = KeyStoreManager.getInstance();
	
	public ShowCert(Composite content, Composite exp) {
		composite = new Composite(content, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_comp = new GridData(SWT.FILL, SWT.TOP, true, true);
		composite.setLayoutData(gd_comp);
		
		Group showCertGroup = new Group(composite, SWT.NONE);
		showCertGroup.setLayout(new GridLayout(2, false));
		GridData gd_grp = new GridData(SWT.FILL, SWT.FILL, true, true);
		showCertGroup.setLayoutData(gd_grp);
		showCertGroup.setText(Messages.ShowCert_manage_certs_headline);
		

		left = new Composite(showCertGroup, SWT.NONE);
		left.setLayout(new GridLayout(1, true));
		left.setLayoutData(new GridData(SWT.NONE,SWT.FILL,false,true));
		right = new Composite(showCertGroup, SWT.NONE);
		right.setLayout(new GridLayout(2, false));

		lst_certs = new List(left, SWT.BORDER);
		lst_certs.setLayoutData(new GridData(SWT.NONE,SWT.FILL,false,true));
		
		//get all public keys from JCT Keystore and iterate over them
		for(KeyStoreAlias ksAlias : ksm.getAllPublicKeys()){
			//for each public key, save the name
			String certListEntry = ksAlias.getContactName() + " (" + ksAlias.getKeyLength() + "bit) ";
			lst_certs.add(certListEntry);
		}
		
		//lst_certs.add(Messages.ShowCert_dummy_cert0);
		//lst_certs.add(Messages.ShowCert_dummy_cert1);
		//lst_certs.add(Messages.ShowCert_dummy_cert2);
		//lst_certs.add(Messages.ShowCert_dummy_cert3);

		lbl_issued_to = new Label(right,SWT.NONE);
		lbl_issued_to.setFont(FontService.getNormalBoldFont());
		lbl_issued_to.setText(Messages.ShowCert_issued_to);
		lbl_plain = new Label(right,SWT.NONE);
		
		lbl_common = new Label(right, SWT.NONE);
		lbl_common.setText(Messages.ShowCert_common_name);
		lbl_value_common = new Label(right, SWT.None);
		lbl_value_common.setText(Messages.ShowCert_dummy_common_name);

		lbl_org = new Label(right, SWT.None);
		lbl_org.setText(Messages.ShowCert_organisation);
		lbl_value_org = new Label(right, SWT.None);
		lbl_value_org.setText(Messages.ShowCert_dummy_organisation);

		lbl_orgUnit = new Label(right, SWT.None);
		lbl_orgUnit.setText(Messages.ShowCert_ou);
		lbl_value_orgUnit= new Label(right, SWT.None);
		lbl_value_orgUnit.setText(Messages.ShowCert_dummy_ou);

		lbl_city = new Label(right, SWT.None);
		lbl_city.setText(Messages.ShowCert_city);
		lbl_value_city= new Label(right, SWT.None);
		lbl_value_city.setText(Messages.ShowCert_dummy_city);

//		lbl_state = new Label(right, SWT.None);
//		lbl_state.setText("Bundesland");
//		lbl_value_state = new Label(right, SWT.None);
//		lbl_value_state.setText("Berlin");

		lbl_country = new Label(right, SWT.None);
		lbl_country.setText(Messages.ShowCert_country);
		lbl_value_country =new Label(right, SWT.None);
		lbl_value_country.setText(Messages.ShowCert_dummy_country);

		lbl_mail = new Label(right, SWT.None);
		lbl_mail.setText(Messages.ShowCert_email);
		lbl_value_mail = new Label(right, SWT.None);
		lbl_value_mail.setText(Messages.ShowCert_dummy_email);
		
		lbl_plain1 = new Label(right,SWT.NONE);
		lbl_plain2 = new Label(right,SWT.NONE);
		lbl_issued_by = new Label(right,SWT.NONE);
		lbl_issued_by.setFont(FontService.getNormalBoldFont());
		lbl_issued_by.setText(Messages.ShowCert_issued_by);
		lbl_plain3 = new Label(right,SWT.NONE);
		
		lbl_common_by = new Label(right, SWT.NONE);
		lbl_common_by.setText(Messages.ShowCert_common_name_issuer);
		lbl_value_common_by = new Label(right, SWT.None);
		lbl_value_common_by.setText(Messages.ShowCert_dummy_cn_issuer);

		lbl_org_by = new Label(right, SWT.None);
		lbl_org_by.setText(Messages.ShowCert_org_issuer);
		lbl_value_org_by = new Label(right, SWT.None);
		lbl_value_org_by.setText(Messages.ShowCert_dummy_org_issuer);

		lbl_orgUnit_by = new Label(right, SWT.None);
		lbl_orgUnit_by.setText(Messages.ShowCert_ou_issuer);
		lbl_value_orgUnit_by= new Label(right, SWT.None);
		lbl_value_orgUnit_by.setText(Messages.ShowCert_dummy_ou_issuer);
		
		lbl_plain4 = new Label(right,SWT.NONE);
		lbl_plain5 = new Label(right,SWT.NONE);
		lbl_issued_by = new Label(right,SWT.NONE);
		lbl_issued_by.setFont(FontService.getNormalBoldFont());
		lbl_issued_by.setText(Messages.ShowCert_validity_period);
		lbl_plain6 = new Label(right,SWT.NONE);
		
		lbl_issued_on = new Label(right, SWT.None);
		lbl_issued_on.setText(Messages.ShowCert_issued_on);
		lbl_value_issued_on = new Label(right, SWT.None);
		lbl_value_issued_on.setText(Messages.ShowCert_dummy_issued_on);
		lbl_expired_on = new Label(right, SWT.None);
		lbl_expired_on.setText(Messages.ShowCert_expires_on);
		lbl_value_expired_on = new Label(right, SWT.None);
		lbl_value_expired_on.setText(Messages.ShowCert_dummy_expires_on);
		
		lbl_plain7 = new Label(right,SWT.NONE);
		lbl_plain8 = new Label(right,SWT.NONE);
		btn_revoke = new Button(right,SWT.NONE);
		btn_revoke.setText(Messages.ShowCert_rev_cert_btn);
		
	    Label lbl_exp = (Label)exp.getChildren()[0];
        lbl_exp.setText(Messages.ShowCert_exp_txt0 +
        		Messages.ShowCert_exp_txt1 +
        		Messages.ShowCert_exp_txt2 +
        		Messages.ShowCert_exp_txt3);
		composite.setVisible(false);
	}
	
	public void dispose() {
		this.composite.dispose();
	}

	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}
}

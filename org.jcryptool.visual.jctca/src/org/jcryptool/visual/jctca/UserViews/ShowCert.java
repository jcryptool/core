package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jcryptool.core.util.fonts.FontService;

public class ShowCert implements Views {
	Composite composite;
	Composite left;
	Composite right;
	List lst_certs;
	Label lbl_plain, lbl_plain1, lbl_plain2, lbl_plain3, lbl_plain4, lbl_plain5, lbl_plain6;
	
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

	public ShowCert(Composite content) {
		composite = new Composite(content, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_comp = new GridData(SWT.FILL, SWT.TOP, true, true);
		composite.setLayoutData(gd_comp);
		
		Group showCertGroup = new Group(composite, SWT.BORDER);
		showCertGroup.setLayout(new GridLayout(2, false));
		GridData gd_grp = new GridData(SWT.FILL, SWT.FILL, true, true);
		showCertGroup.setLayoutData(gd_grp);
		showCertGroup.setText("Zertifikate verwalten");
		

		left = new Composite(showCertGroup, SWT.NONE);
		left.setLayout(new GridLayout(1, true));
		left.setLayoutData(new GridData(SWT.NONE,SWT.FILL,false,true));
		right = new Composite(showCertGroup, SWT.NONE);
		right.setLayout(new GridLayout(2, false));

		lst_certs = new List(left, SWT.BORDER);
		lst_certs.setLayoutData(new GridData(SWT.NONE,SWT.FILL,false,true));
		lst_certs.add("   Zertifikat #1   ");
		lst_certs.add("   Zertifikat #2   ");
		lst_certs.add("   Zertifikat #3   ");
		lst_certs.add("   Zertifikat #4   ");

		lbl_issued_to = new Label(right,SWT.NONE);
		lbl_issued_to.setFont(FontService.getNormalBoldFont());
		lbl_issued_to.setText("Ausgestellt für");
		lbl_plain = new Label(right,SWT.NONE);
		
		lbl_common = new Label(right, SWT.NONE);
		lbl_common.setText("Common Name");
		lbl_value_common = new Label(right, SWT.None);
		lbl_value_common.setText("Erika Musterfrau");

		lbl_org = new Label(right, SWT.None);
		lbl_org.setText("Organisation");
		lbl_value_org = new Label(right, SWT.None);
		lbl_value_org.setText("nicht Teil dieses Zertifikats");

		lbl_orgUnit = new Label(right, SWT.None);
		lbl_orgUnit.setText("Organisationseinheit  ");
		lbl_value_orgUnit= new Label(right, SWT.None);
		lbl_value_orgUnit.setText("nicht Teil dieses Zertifikats");

		lbl_city = new Label(right, SWT.None);
		lbl_city.setText("Ort");
		lbl_value_city= new Label(right, SWT.None);
		lbl_value_city.setText("Berlin");

//		lbl_state = new Label(right, SWT.None);
//		lbl_state.setText("Bundesland");
//		lbl_value_state = new Label(right, SWT.None);
//		lbl_value_state.setText("Berlin");

		lbl_country = new Label(right, SWT.None);
		lbl_country.setText("Land");
		lbl_value_country =new Label(right, SWT.None);
		lbl_value_country.setText("Deutschland");

		lbl_mail = new Label(right, SWT.None);
		lbl_mail.setText("E-Mail");
		lbl_value_mail = new Label(right, SWT.None);
		lbl_value_mail.setText("e.musterfrau@gmail.com");
		
		lbl_plain1 = new Label(right,SWT.NONE);
		lbl_plain2 = new Label(right,SWT.NONE);
		lbl_issued_by = new Label(right,SWT.NONE);
		lbl_issued_by.setFont(FontService.getNormalBoldFont());
		lbl_issued_by.setText("Ausgestellt von");
		lbl_plain3 = new Label(right,SWT.NONE);
		
		lbl_common_by = new Label(right, SWT.NONE);
		lbl_common_by.setText("Common Name");
		lbl_value_common_by = new Label(right, SWT.None);
		lbl_value_common_by.setText("JCryptool Certificate Authority");

		lbl_org_by = new Label(right, SWT.None);
		lbl_org_by.setText("Organisation");
		lbl_value_org_by = new Label(right, SWT.None);
		lbl_value_org_by.setText("JCryptool");

		lbl_orgUnit_by = new Label(right, SWT.None);
		lbl_orgUnit_by.setText("Organisationseinheit  ");
		lbl_value_orgUnit_by= new Label(right, SWT.None);
		lbl_value_orgUnit_by.setText("Certificate Authority");
		
		lbl_plain4 = new Label(right,SWT.NONE);
		lbl_plain5 = new Label(right,SWT.NONE);
		lbl_issued_by = new Label(right,SWT.NONE);
		lbl_issued_by.setFont(FontService.getNormalBoldFont());
		lbl_issued_by.setText("Gültigkeitszeitraum");
		lbl_plain6 = new Label(right,SWT.NONE);
		
		lbl_issued_on = new Label(right, SWT.None);
		lbl_issued_on.setText("Ausgestellt am");
		lbl_value_issued_on = new Label(right, SWT.None);
		lbl_value_issued_on.setText("9/4/13");
		lbl_expired_on = new Label(right, SWT.None);
		lbl_expired_on.setText("Gültig bis");
		lbl_value_expired_on = new Label(right, SWT.None);
		lbl_value_expired_on.setText("10/4/4");
		composite.setVisible(false);
		

	}

	public void dispose() {
		this.composite.dispose();
	}

	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}
}

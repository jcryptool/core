package org.jcryptool.visual.jctca.CertificateViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jcryptool.visual.jctca.Activator;

public class ShowCertReq implements Views {
	Composite composite;
	Composite left;
	Composite right;
	Composite center;
	Composite grp_exp;

	List lst_csr;

	Label lbl_firstname;
	Label lbl_value_firstname;
	Label lbl_lastname;
	Label lbl_value_lastname;
	Label lbl_street;
	Label lbl_value_street;
	Label lbl_ZIP;
	Label lbl_value_ZIP;
	Label lbl_city;
	Label lbl_value_city;
	Label lbl_country;
	Label lbl_value_country;
	Label lbl_mail;
	Label lbl_value_mail;

	Button btn_verify_identity;
	Button btn_reject_csr;

	public ShowCertReq(Composite content, Composite exp) {
		// composite = new Composite(content, SWT.NONE);
		// composite.setLayout(new GridLayout(1, false));
		// GridData gd_comp = new GridData(SWT.FILL, SWT.TOP, true, true);
		// composite.setLayoutData(gd_comp);

		Group showCSRGroup = new Group(content, SWT.NONE);
		showCSRGroup.setLayout(new GridLayout(3, false));
		GridData gd_grp = new GridData(SWT.FILL, SWT.TOP, true, true);
		showCSRGroup.setLayoutData(gd_grp);
		showCSRGroup.setText(Messages.ShowCertReq_csr_headline);

		left = new Composite(showCSRGroup, SWT.NONE);
		left.setLayout(new GridLayout(1, true));
		left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		center = new Composite(showCSRGroup, SWT.NONE);
		center.setLayout(new GridLayout(2, true));
		center.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, true));
		right = new Composite(showCSRGroup, SWT.NONE);
		right.setLayout(new GridLayout(1, false));
		right.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, true));

		lst_csr = new List(left, SWT.BORDER);
		lst_csr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		lst_csr.add(Messages.ShowCertReq_dummy_csr0);
		lst_csr.add(Messages.ShowCertReq_dummy_csr1);
		lst_csr.add(Messages.ShowCertReq_dummy_csr2);
		lst_csr.add(Messages.ShowCertReq_dummy_csr3);

		lbl_firstname = new Label(center, SWT.NONE);
		lbl_firstname.setText(Messages.ShowCertReq_first_name);
		lbl_value_firstname = new Label(center, SWT.NONE);
		lbl_value_firstname.setText(Messages.ShowCertReq_dummy_first_name);

		lbl_lastname = new Label(center, SWT.None);
		lbl_lastname.setText(Messages.ShowCertReq_last_name);
		lbl_value_lastname = new Label(center, SWT.None);
		lbl_value_lastname.setText(Messages.ShowCertReq_dummy_last_name);

		lbl_street = new Label(center, SWT.None);
		lbl_street.setText(Messages.ShowCertReq_street);
		lbl_value_street = new Label(center, SWT.None);
		lbl_value_street.setText(Messages.ShowCertReq_dummy_street);

		lbl_ZIP = new Label(center, SWT.None);
		lbl_ZIP.setText(Messages.ShowCertReq_zip_code);
		lbl_value_ZIP = new Label(center, SWT.None);
		lbl_value_ZIP.setText(Messages.ShowCertReq_dummy_zip_code);

		lbl_city = new Label(center, SWT.None);
		lbl_city.setText(Messages.ShowCertReq_city);
		lbl_value_city = new Label(center, SWT.None);
		lbl_value_city.setText(Messages.ShowCertReq_dummy_city);

		lbl_country = new Label(center, SWT.None);
		lbl_country.setText(Messages.ShowCertReq_country);
		lbl_value_country = new Label(center, SWT.None);
		lbl_value_country.setText(Messages.ShowCertReq_dummy_country);

		lbl_mail = new Label(center, SWT.None);
		lbl_mail.setText(Messages.ShowCertReq_email);
		lbl_value_mail = new Label(center, SWT.None);
		lbl_value_mail.setText(Messages.ShowCertReq_dummy_email);

		btn_verify_identity = new Button(center, SWT.NONE);
		btn_verify_identity.setText(Messages.ShowCertReq_verify_identity);
		btn_verify_identity.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true));
		btn_reject_csr = new Button(center, SWT.NONE);
		btn_reject_csr.setText(Messages.ShowCertReq_csr_deny);
		btn_reject_csr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));

		String ausweis = Messages.ShowCertReq_dummy_ausweis_path;
		Label lbl_img = new Label(right, SWT.NONE);
		lbl_img.setImage(Activator.getImageDescriptor(ausweis).createImage());
		// Label lbl_exp = (Label)exp.getChildren()[0];
		// lbl_exp.setText("Hi, I explain what is going on in Show Certificate Request!");
	}

	@Override
	public void dispose() {
		this.composite.dispose();
	}

	@Override
	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}
}

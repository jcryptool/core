package org.jcryptool.visual.jctca.CertificateViews;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

public class ShowRevReq implements Views {
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

	public ShowRevReq(Composite content) {
		// composite = new Composite(content, SWT.NONE);
		// composite.setLayout(new GridLayout(1, false));
		// GridData gd_comp = new GridData(SWT.FILL, SWT.TOP, true, true);
		// composite.setLayoutData(gd_comp);

		Group showCSRGroup = new Group(content, SWT.NONE);
		showCSRGroup.setLayout(new GridLayout(3, false));
		GridData gd_grp = new GridData(SWT.FILL, SWT.TOP, true, true);
		showCSRGroup.setLayoutData(gd_grp);
		showCSRGroup.setText("CSR verifizieren");

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
		lst_csr.add("   CSR #1   ");
		lst_csr.add("   CSR #2   ");
		lst_csr.add("   CSR #3   ");
		lst_csr.add("   CSR #4   ");

		lbl_firstname = new Label(center, SWT.NONE);
		lbl_firstname.setText("Vornamen");
		lbl_value_firstname = new Label(center, SWT.NONE);
		lbl_value_firstname.setText("Erika");

		lbl_lastname = new Label(center, SWT.None);
		lbl_lastname.setText("Nachname");
		lbl_value_lastname = new Label(center, SWT.None);
		lbl_value_lastname.setText("Mustermann");

		lbl_street = new Label(center, SWT.None);
		lbl_street.setText("Straße");
		lbl_value_street = new Label(center, SWT.None);
		lbl_value_street.setText("Musterstrasse 12");

		lbl_ZIP = new Label(center, SWT.None);
		lbl_ZIP.setText("Postleitzahl");
		lbl_value_ZIP = new Label(center, SWT.None);
		lbl_value_ZIP.setText("13357");

		lbl_city = new Label(center, SWT.None);
		lbl_city.setText("Ort");
		lbl_value_city = new Label(center, SWT.None);
		lbl_value_city.setText("Berlin");

		lbl_country = new Label(center, SWT.None);
		lbl_country.setText("Land");
		lbl_value_country = new Label(center, SWT.None);
		lbl_value_country.setText("Deutschland");

		lbl_mail = new Label(center, SWT.None);
		lbl_mail.setText("E-Mail");
		lbl_value_mail = new Label(center, SWT.None);
		lbl_value_mail.setText("e.mustermann@gmail.com");

		btn_verify_identity = new Button(center, SWT.NONE);
		btn_verify_identity.setText("Identität verifizieren");
		btn_verify_identity.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true));
		btn_reject_csr = new Button(center, SWT.NONE);
		btn_reject_csr.setText("CSR ablehnen");
		btn_reject_csr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));

		Image img_id = new Image(right.getDisplay(),"/home/kerstin/git/minica/org.jcryptool.visual.jctca/src/org/jcryptool/visual/jctca/RegistrarViews/ausweis.jpeg");
		Label lbl_img = new Label(right, SWT.NONE);
		lbl_img.setImage(img_id);
	}

	public void dispose() {
		this.composite.dispose();
	}

	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}
}

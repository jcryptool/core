package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class ShowCert implements Views {
	Composite composite;
	Composite left;
	Composite right;
	List lst_certs;
	Label lbl_common;
	Label lbl_value_common;

	Label lbl_org;
	Label lbl_value_org;

	Label lbl_orgUnit;
	Label lbl_value_orgUnit;

	Label lbl_city;
	Label lbl_value_city;

	Label lbl_state;
	Label lbl_value_state;

	Label lbl_country;
	Label lbl_value_country;

	Label lbl_mail;
	Label lbl_value_mail;

	public ShowCert(Composite content) {
		composite = new Composite(content, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		left = new Composite(composite, SWT.NONE);
		left.setLayout(new GridLayout(1, true));
		left.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		right = new Composite(composite, SWT.NONE);
		right.setLayout(new GridLayout(2, false));

		lst_certs = new List(left, SWT.NONE);
		lst_certs.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		lst_certs.add("   Certificate #1   ");
		lst_certs.add("   Certificate #2   ");
		lst_certs.add("   Certificate #3   ");
		lst_certs.add("   Certificate #4   ");

		lbl_common = new Label(right, SWT.NONE);
		lbl_common.setText("Common Name");
		lbl_value_common = new Label(right, SWT.None);
		lbl_value_common.setText("Erika Musterfrau");

		lbl_org = new Label(right, SWT.None);
		lbl_org.setText("Organisation");
		lbl_value_org = new Label(right, SWT.None);
		lbl_value_org.setText("Musteragentur");

		lbl_orgUnit = new Label(right, SWT.None);
		lbl_orgUnit.setText("Organisational Unit  ");
		lbl_value_orgUnit= new Label(right, SWT.None);
		lbl_value_orgUnit.setText("Musterentwuerfe");

		lbl_city = new Label(right, SWT.None);
		lbl_city.setText("City or Locality");
		lbl_value_city= new Label(right, SWT.None);
		lbl_value_city.setText("Berlin");

		lbl_state = new Label(right, SWT.None);
		lbl_state.setText("State or Province");
		lbl_value_state = new Label(right, SWT.None);
		lbl_value_state.setText("Berlin");

		lbl_country = new Label(right, SWT.None);
		lbl_country.setText("Country");
		lbl_value_country =new Label(right, SWT.None);
		lbl_value_country.setText("Deutschland");

		lbl_mail = new Label(right, SWT.None);
		lbl_mail.setText("E-Mail");
		lbl_value_mail = new Label(right, SWT.None);
		lbl_value_mail.setText("e.musterfrau@gmail.com");
		
		composite.setVisible(false);

	}

	public void dispose() {
		this.composite.dispose();
	}

	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}
}

package org.jcryptool.visual.jctca.RegistrarViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jcryptool.visual.jctca.CertificateClasses.RegistrarCSR;
import org.jcryptool.visual.jctca.listeners.CSRListener;

public class ShowCSR implements Views {
	Composite composite;
	Composite left;
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

	Button btn_forward_csr;
	Button btn_reject_csr;

	public ShowCSR(Composite content, Composite exp) {
		// composite = new Composite(content, SWT.NONE);
		// composite.setLayout(new GridLayout(1, false));
		// GridData gd_comp = new GridData(SWT.FILL, SWT.TOP, true, true);
		// composite.setLayoutData(gd_comp);

		Group showCSRGroup = new Group(content, SWT.NONE);
		showCSRGroup.setLayout(new GridLayout(3, false));
		// set SWT.FILL auf SWT.NONE, um grp nicht vollst. auszufuellen - bild
		// bleibt dann kleiner
		GridData gd_grp = new GridData(SWT.FILL, SWT.FILL, true, true);
		showCSRGroup.setLayoutData(gd_grp);
		showCSRGroup.setText(Messages.ShowCSR_verify_csr_headline);

		left = new Composite(showCSRGroup, SWT.NONE);
		left.setLayout(new GridLayout(1, true));
		left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		center = new Composite(showCSRGroup, SWT.NONE);
		center.setLayout(new GridLayout(2, false));
		center.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		
		
		lst_csr = new List(left, SWT.BORDER);
		GridData lst_ld = new GridData(SWT.FILL, SWT.NONE, true, true);
		lst_ld.minimumWidth = 60; // damit unter Linux die Liste auch korrekt
									// angezeigt wird
		lst_csr.setLayoutData(lst_ld);

		lbl_firstname = new Label(center, SWT.NONE);
		lbl_firstname.setText("Vorname(n):");
		lbl_value_firstname = new Label(center, SWT.FILL);
		GridData lbl_gd = new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1);
		lbl_value_firstname.setText("Erika");
		lbl_value_firstname.setLayoutData(lbl_gd);

		lbl_lastname = new Label(center, SWT.None);
		lbl_lastname.setText("Nachname:");
		lbl_value_lastname = new Label(center, SWT.FILL);
		lbl_value_lastname.setText("Gablermann");
		lbl_value_lastname.setLayoutData(lbl_gd);

		lbl_street = new Label(center, SWT.None);
		lbl_street.setText("Straße:");
		lbl_value_street = new Label(center, SWT.FILL);
		lbl_value_street.setText("Birkenweg 12");
		lbl_value_street.setLayoutData(lbl_gd);

		lbl_ZIP = new Label(center, SWT.None);
		lbl_ZIP.setText("Postleitzahl");
		lbl_value_ZIP = new Label(center, SWT.FILL);
		lbl_value_ZIP.setText("13357");
		lbl_value_ZIP.setLayoutData(lbl_gd);

		lbl_city = new Label(center, SWT.None);
		lbl_city.setText("Stadt");
		lbl_value_city = new Label(center, SWT.FILL);
		lbl_value_city.setText("Berlin");
		lbl_value_city.setLayoutData(lbl_gd);

		lbl_country = new Label(center, SWT.None);
		lbl_country.setText("Land:");
		lbl_value_country = new Label(center, SWT.FILL);
		lbl_value_country.setText("Deutschland");
		lbl_value_country.setLayoutData(lbl_gd);

		lbl_mail = new Label(center, SWT.None);
		lbl_mail.setText("E-Mailadresse");
		lbl_value_mail = new Label(center, SWT.FILL);
		lbl_value_mail.setText("boeseerika@example.com");
		lbl_value_mail.setLayoutData(lbl_gd);

		
		Composite center_btn = new Composite(center, SWT.NONE);
		center_btn.setLayout(new GridLayout(2,true));
		GridData btn_gd = new GridData();
		btn_gd.horizontalSpan=2;
		center_btn.setLayoutData(btn_gd);
		btn_verify_identity = new Button(center_btn, SWT.NONE);
		btn_verify_identity.setText("Identitätsprüfung vornehmen");
		btn_verify_identity.setData(new Integer(0));
		GridData btn_ld = new GridData(SWT.FILL, SWT.NONE, false, true);
		btn_ld.horizontalSpan = 2;
		btn_verify_identity.setLayoutData(btn_ld);

		btn_forward_csr = new Button(center_btn, SWT.NONE);
		btn_forward_csr.setText("CSR an CA weiterleiten");
		btn_forward_csr.setData(new Integer(0));
		btn_forward_csr.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
				true));
		btn_forward_csr.setEnabled(false);

		btn_reject_csr = new Button(center_btn, SWT.NONE);
		btn_reject_csr.setText("CSR ablehnen");
		btn_reject_csr.setData(1);
		btn_reject_csr.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
				true));
		btn_reject_csr.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
				true));
		btn_reject_csr.setEnabled(false);

		CSRListener lst = new CSRListener(lbl_value_firstname,
				lbl_value_lastname, lbl_value_street, lbl_value_ZIP,
				lbl_value_city, lbl_value_country, lbl_value_mail,
				btn_forward_csr, btn_reject_csr, lst_csr);
		lst_csr.addSelectionListener(lst);
		btn_reject_csr.addSelectionListener(lst);
		btn_forward_csr.addSelectionListener(lst);
		VerifyIdentity verify = new VerifyIdentity(Display.getCurrent()
				.getActiveShell(), lst_csr, btn_forward_csr, btn_reject_csr);
		btn_verify_identity.addSelectionListener(verify);
		RegistrarCSR csr = RegistrarCSR.getInstance();
		csr.addCSR("Erika",
				"Gablermann",
				"Birkenweg 12", "13357",
				"Berlin", "Deutschland", "boese-eri@example.com",
				"icons/ausweis.jpeg", null, null);//$NON-NLS-1$
		csr.addCSR("Böser", "Angreiffer", "Blackhatstreet 42", "1337",
				"Bösestadt", "Blackhattonia", "blackhat@example.com",
				"icons/ausweis_blackhat.jpg", null, null);//$NON-NLS-1$

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

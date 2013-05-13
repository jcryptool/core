package org.jcryptool.visual.jctca.UserViews;

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
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.listeners.RevokeButtonListener;
import org.jcryptool.visual.jctca.listeners.UserShowCertsListener;

public class ShowCert implements Views {
	Composite composite;
	Composite left;
	Composite right;
	List lst_certs;
	Label lbl_plain, lbl_plain1, lbl_plain2, lbl_plain3, lbl_plain4,
			lbl_plain5, lbl_plain6, lbl_plain7, lbl_plain8;

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
		left.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));
		right = new Composite(showCertGroup, SWT.NONE);
		right.setLayout(new GridLayout(2, false));
		right.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		lst_certs = new List(left, SWT.BORDER);
		lst_certs.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));

		addRSAAndDSAKeysToDropdown();

		lbl_issued_to = new Label(right, SWT.NONE);
		lbl_issued_to.setFont(FontService.getNormalBoldFont());
		lbl_issued_to.setText(Messages.ShowCert_issued_to);
		lbl_plain = new Label(right, SWT.NONE);

		lbl_common = new Label(right, SWT.NONE);
		lbl_common.setText(Messages.ShowCert_common_name);
		lbl_value_common = new Label(right, SWT.None);

		lbl_org = new Label(right, SWT.None);
		lbl_org.setText(Messages.ShowCert_organisation);
		lbl_value_org = new Label(right, SWT.None);

		lbl_orgUnit = new Label(right, SWT.None);
		lbl_orgUnit.setText(Messages.ShowCert_ou);
		lbl_value_orgUnit = new Label(right, SWT.None);

		lbl_city = new Label(right, SWT.None);
		lbl_city.setText(Messages.ShowCert_city);
		lbl_value_city = new Label(right, SWT.None);

		// lbl_state = new Label(right, SWT.None);
		// lbl_state.setText("Bundesland");
		// lbl_value_state = new Label(right, SWT.None);
		// lbl_value_state.setText("Berlin");

		lbl_country = new Label(right, SWT.None);
		lbl_country.setText(Messages.ShowCert_country);
		lbl_value_country = new Label(right, SWT.None);

		lbl_mail = new Label(right, SWT.None);
		lbl_mail.setText(Messages.ShowCert_email);
		lbl_value_mail = new Label(right, SWT.None);

		lbl_plain1 = new Label(right, SWT.NONE);
		lbl_plain2 = new Label(right, SWT.NONE);
		lbl_issued_by = new Label(right, SWT.NONE);
		lbl_issued_by.setFont(FontService.getNormalBoldFont());
		lbl_issued_by.setText(Messages.ShowCert_issued_by);
		lbl_plain3 = new Label(right, SWT.NONE);

		lbl_common_by = new Label(right, SWT.NONE);
		lbl_common_by.setText(Messages.ShowCert_common_name_issuer);
		lbl_value_common_by = new Label(right, SWT.None);

		lbl_org_by = new Label(right, SWT.None);
		lbl_org_by.setText(Messages.ShowCert_org_issuer);
		lbl_value_org_by = new Label(right, SWT.None);

		lbl_orgUnit_by = new Label(right, SWT.None);
		lbl_orgUnit_by.setText(Messages.ShowCert_ou_issuer);
		lbl_value_orgUnit_by = new Label(right, SWT.None);

		lbl_plain4 = new Label(right, SWT.NONE);
		lbl_plain5 = new Label(right, SWT.NONE);
		lbl_issued_by = new Label(right, SWT.NONE);
		lbl_issued_by.setFont(FontService.getNormalBoldFont());
		lbl_issued_by.setText(Messages.ShowCert_validity_period);
		lbl_plain6 = new Label(right, SWT.NONE);

		lbl_issued_on = new Label(right, SWT.None);
		lbl_issued_on.setText(Messages.ShowCert_issued_on);
		lbl_value_issued_on = new Label(right, SWT.None);
		lbl_expired_on = new Label(right, SWT.None);
		lbl_expired_on.setText(Messages.ShowCert_expires_on);
		lbl_value_expired_on = new Label(right, SWT.None);
		
		btn_revoke = new Button(right, SWT.PUSH);
		btn_revoke.setText("Zertifikat widerrufen");
		GridData revoke_gd = new GridData();
		revoke_gd.horizontalSpan=2;
		btn_revoke.setLayoutData(revoke_gd);
		
		UserShowCertsListener uscListener = new UserShowCertsListener(lbl_value_common, lbl_value_org, lbl_value_orgUnit, lbl_value_city, lbl_value_country, lbl_value_mail, lbl_value_common_by, lbl_value_org_by, lbl_value_orgUnit_by, lbl_value_issued_on, lbl_value_expired_on, btn_revoke);
		lst_certs.addSelectionListener(uscListener);
		
		btn_revoke.addSelectionListener(new RevokeButtonListener());
		
		lbl_plain7 = new Label(right, SWT.NONE);
		lbl_plain8 = new Label(right, SWT.NONE);

		Label lbl_exp = (Label) exp.getChildren()[0];
		lbl_exp.setText(Messages.ShowCert_exp_txt0 + Messages.ShowCert_exp_txt1
				+ Messages.ShowCert_exp_txt2 + Messages.ShowCert_exp_txt3);
		composite.setVisible(false);
	}

	private void addRSAAndDSAKeysToDropdown() {
		KeyStoreManager ksm = KeyStoreManager.getInstance();
		int i = 0;
		for (KeyStoreAlias ksAlias : Util.getAllRSAAndDSAPublicKeys(ksm)) {
			if (Util.isSignedByJCTCA(ksAlias) == false) {
				continue;
			}
			String ListEntry = ksAlias.getContactName() + " (" + ksAlias.getKeyLength() + "bit ";
			if (ksAlias.getOperation().contains("RSA")) {
				ListEntry += "RSA)";
			} else {
				ListEntry += "DSA)";
			}
			lst_certs.add(ListEntry);
			lst_certs.setData(Integer.toString(i), ksAlias);
			i++;
		}
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

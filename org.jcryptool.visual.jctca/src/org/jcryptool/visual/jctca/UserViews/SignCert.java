package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;

public class SignCert implements Views {

	Composite composite;
	Combo cmb_priv_key;

	public SignCert(Composite content, Composite exp) {
		this.composite = new Composite(content, SWT.NONE);
		this.composite.setLayout(new GridLayout(1, false));
		GridData gd_comp = new GridData(SWT.FILL, SWT.TOP, true, true);
		composite.setLayoutData(gd_comp);

		Group signCertGroup = new Group(composite, SWT.NONE);
		signCertGroup.setLayout(new GridLayout(1, false));
		GridData gd_grp = new GridData(SWT.FILL, SWT.TOP, true, true);
		signCertGroup.setLayoutData(gd_grp);
		signCertGroup.setText(Messages.SignCert_sig_headline);

		Button btn_select_file = new Button(signCertGroup, SWT.NONE);
		btn_select_file.setText(Messages.SignCert_get_file_btn);
		Label lbl_or = new Label(signCertGroup, SWT.CENTER);
		lbl_or.setText(Messages.SignCert_or_headline);

		GridData gd_txt = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 20);
		Text txt_sign = new Text(signCertGroup, SWT.LEFT | SWT.MULTI
				| SWT.BORDER);
		txt_sign.setText(Messages.SignCert_dummy_enter_text);
		txt_sign.setLayoutData(gd_txt);

		cmb_priv_key = new Combo(signCertGroup, SWT.DROP_DOWN);
		addRSAAndDSAPrivateKeysToDropdown();
		cmb_priv_key.select(0);

		Button btn_detail = new Button(signCertGroup, SWT.CHECK);
		btn_detail.setText(Messages.SignCert_visualized_sig_btn);

		Button btn_sign = new Button(signCertGroup, SWT.NONE);
		btn_sign.setText(Messages.SignCert_sign_with_key_btn);
		Label lbl_exp = (Label) exp.getChildren()[0];
		lbl_exp.setText(Messages.SignCert_exp_txt0 + Messages.SignCert_exp_txt1);

		composite.setVisible(false);
	}

	private void addRSAAndDSAPrivateKeysToDropdown(){
		KeyStoreManager ksm = KeyStoreManager.getInstance();
		for (KeyStoreAlias ksAlias : Util.getAllRSAAndDSAPublicKeys(ksm)) {
			String ListEntry = ksAlias.getContactName() + " (" + ksAlias.getKeyLength() + "bit ";
			if (ksAlias.getOperation().contains("RSA")) {
				ListEntry += "RSA)";
			} else {
				ListEntry += "DSA)";
			}
			cmb_priv_key.add(ListEntry);
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}

	@Override
	public void dispose() {
		composite.dispose();
	}

}

package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.listeners.SelectFileListener;

public class SignCert implements Views {

	Composite composite;
	Combo cmb_priv_key;
	Label selected_file;

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

		Button btn_select_file = new Button(signCertGroup, SWT.PUSH);
		btn_select_file.setText(Messages.SignCert_get_file_btn);
		btn_select_file.setData("select");
		
		Composite cmp_mini = new Composite(signCertGroup, SWT.NONE);
		cmp_mini.setLayout(new GridLayout(2,false));
		cmp_mini.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,true,1,1));
		
		selected_file = new Label(cmp_mini, SWT.NONE);
		Button btn_deselect_file = new Button(cmp_mini,SWT.NONE);
		btn_deselect_file.setText("Auswahl aufheben");
		btn_deselect_file.setData("deselect");
		btn_deselect_file.setVisible(false);
		
		Label lbl_or = new Label(signCertGroup, SWT.CENTER);
		lbl_or.setText(Messages.SignCert_or_headline);

		GridData gd_txt = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 20);
		Text txt_sign = new Text(signCertGroup, SWT.LEFT | SWT.MULTI
				| SWT.BORDER);
		txt_sign.setText(Messages.SignCert_dummy_enter_text);
		txt_sign.setLayoutData(gd_txt);

		
		btn_select_file.addSelectionListener(new SelectFileListener(selected_file, txt_sign, btn_deselect_file));
		btn_deselect_file.addSelectionListener(new SelectFileListener(selected_file, txt_sign, btn_deselect_file));
		
		cmb_priv_key = new Combo(signCertGroup, SWT.DROP_DOWN);
		cmb_priv_key.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, true));
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
			String listEntry = ksAlias.getContactName() + " (" + ksAlias.getKeyLength() + "bit ";
			if (ksAlias.getOperation().contains("RSA")) {
				listEntry += "RSA";
			} else {
				listEntry += "DSA";
			}
			listEntry+=", Hash: " + ksAlias.getHashValue() + ")";
			
			cmb_priv_key.add(listEntry);
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

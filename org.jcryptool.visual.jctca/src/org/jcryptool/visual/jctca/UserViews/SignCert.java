package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.listeners.SelectFileListener;
import org.jcryptool.visual.jctca.listeners.SigVisPluginOpenListener;

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
		signCertGroup.setText(Messages.SignCert_headline);

		Button btn_select_file = new Button(signCertGroup, SWT.PUSH);
		btn_select_file.setText(Messages.SignCert_btn_chose_file);
		btn_select_file.setData("select");//$NON-NLS-1$
		
		Composite cmp_mini = new Composite(signCertGroup, SWT.NONE);
		cmp_mini.setLayout(new GridLayout(2,false));
		cmp_mini.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,true,1,1));
		
		selected_file = new Label(cmp_mini, SWT.NONE);
		selected_file.setText(""); //$NON-NLS-1$
		Button btn_deselect_file = new Button(cmp_mini,SWT.NONE);
		btn_deselect_file.setText(Messages.SignCert_btn_cancel_file_selection);
		btn_deselect_file.setData("deselect");//$NON-NLS-1$
		btn_deselect_file.setVisible(false);
		
		Label lbl_or = new Label(signCertGroup, SWT.CENTER);
		lbl_or.setText(Messages.SignCert_file_or_text_headline);

		GridData gd_txt = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 20);
		Text txt_sign = new Text(signCertGroup, SWT.LEFT | SWT.MULTI
				| SWT.BORDER);
		txt_sign.setText(Messages.SignCert_textbox_sample_text);
		txt_sign.setLayoutData(gd_txt);

		
		btn_select_file.addSelectionListener(new SelectFileListener(selected_file, txt_sign, btn_deselect_file));
		btn_deselect_file.addSelectionListener(new SelectFileListener(selected_file, txt_sign, btn_deselect_file));
		
		cmb_priv_key = new Combo(signCertGroup, SWT.DROP_DOWN);
		cmb_priv_key.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, true));
		addRSAAndDSAPrivateKeysToDropdown();
		cmb_priv_key.select(0);

		Button btn_detail = new Button(signCertGroup, SWT.CHECK);
		btn_detail.setText(Messages.SignCert_checkbox_show_sigvis);

		Button btn_sign = new Button(signCertGroup, SWT.NONE);
		btn_sign.setText(Messages.SignCert_btn_sign_with_key);
		btn_sign.addSelectionListener(new SigVisPluginOpenListener(btn_detail, selected_file, txt_sign, cmb_priv_key));
			
		Label lbl_exp = (Label) exp.getChildren()[0];
		lbl_exp.setText(Messages.SignCert_explain_text);

		composite.setVisible(false);
	}

	private void addRSAAndDSAPrivateKeysToDropdown(){
		KeyStoreManager ksm = KeyStoreManager.getInstance();
		for (KeyStoreAlias ksAlias : Util.getAllRSAAndDSAPublicKeys(ksm)) {
			String listEntry = ksAlias.getContactName() + Messages.SignCert_8 + ksAlias.getKeyLength() + Messages.SignCert_9;
			if (ksAlias.getOperation().contains(Messages.SignCert_10)) {
				listEntry += Messages.SignCert_11;
			} else {
				listEntry += Messages.SignCert_12;
			}
			listEntry+=Messages.SignCert_13 + ksAlias.getHashValue() + Messages.SignCert_14;
			
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

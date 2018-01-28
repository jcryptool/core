// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.listeners.RadioButtonListener;
import org.jcryptool.visual.jctca.listeners.SelectFileListener;
import org.jcryptool.visual.jctca.listeners.SigVisPluginOpenListener;

public class SignCert implements Views {
    private Color dark_gray = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
    private Composite composite;
    private Combo cmb_priv_key;

    public SignCert(Composite content, Composite exp) {
        composite = new Composite(content, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        GridData gd_comp = new GridData(SWT.FILL, SWT.FILL, true, true);
        composite.setLayoutData(gd_comp);

        Group cmp_mini = new Group(composite, SWT.NONE);
        cmp_mini.setLayout(new GridLayout());
        cmp_mini.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        cmp_mini.setText(Messages.SignCert_chose_method);

        Group selectSthGroup = new Group(composite, SWT.None);
        selectSthGroup.setLayout(new GridLayout(1, false));
        selectSthGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        selectSthGroup.setText(Messages.SignCert_what_to_sign);

        Group signCertGroup = new Group(composite, SWT.NONE);
        signCertGroup.setLayout(new GridLayout(1, false));
        signCertGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        signCertGroup.setText(Messages.SignCert_headline);

        Composite signBtnComp = new Composite(composite, SWT.NONE);
        signBtnComp.setLayout(new GridLayout(1, false));
        signBtnComp.setLayoutData(gd_comp);

        Composite selectSthComp = new Composite(selectSthGroup, SWT.NONE);
        selectSthComp.setLayout(new GridLayout(3, false));
        selectSthComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Button btn_detail = new Button(cmp_mini, SWT.RADIO);
        btn_detail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        btn_detail.setText(Messages.SignCert_checkbox_show_sigvis);
        btn_detail.setData("detail"); //$NON-NLS-1$
        btn_detail.setSelection(true);

        Button btn_non_detail = new Button(cmp_mini, SWT.RADIO);
        btn_non_detail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        btn_non_detail.setText(Messages.SignCert_sign_directly);
        btn_non_detail.setData("detail"); //$NON-NLS-1$

        Label lbl_detail = new Label(cmp_mini, SWT.WRAP);
        lbl_detail.setText(Messages.SignCert_footnote_input_in_signvis);
        lbl_detail.setForeground(dark_gray);
        GridData gd_lbl_detail = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_lbl_detail.widthHint = 300;
        lbl_detail.setLayoutData(gd_lbl_detail);
        
        Button btn_radio_signFile = new Button(selectSthComp, SWT.RADIO);
        btn_radio_signFile.setText(Messages.SignCert_file);
        btn_radio_signFile.setData("file"); //$NON-NLS-1$
        btn_radio_signFile.setEnabled(false);

        Button btn_select_file = new Button(selectSthComp, SWT.PUSH);
        btn_select_file.setText(Messages.SignCert_btn_chose_file);
        btn_select_file.setData("select");//$NON-NLS-1$
        btn_select_file.setEnabled(false);

        Label selected_file = new Label(selectSthComp, SWT.NONE); // selectSthGroup
        selected_file.setText(""); //$NON-NLS-1$
        selected_file.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

        GridData gd_btn = new GridData();
        Button btn_radio_signText = new Button(selectSthComp, SWT.RADIO);
        btn_radio_signText.setText(Messages.SignCert_text);
        btn_radio_signText.setData("text"); //$NON-NLS-1$
        btn_radio_signText.setLayoutData(gd_btn);
        btn_radio_signText.setEnabled(false);

        GridData gd_txt = new GridData(SWT.FILL, SWT.FILL, false, true, 2, 20);
        Text txt_sign = new Text(selectSthComp, SWT.LEFT | SWT.MULTI | SWT.BORDER);
        txt_sign.setText(Messages.SignCert_textbox_sample_text);
        txt_sign.setLayoutData(gd_txt);
        txt_sign.setEnabled(false);
        txt_sign.setForeground(dark_gray);

        btn_radio_signFile.addSelectionListener(new RadioButtonListener(selected_file, txt_sign, btn_select_file));
        btn_radio_signText.addSelectionListener(new RadioButtonListener(selected_file, txt_sign, btn_select_file));

        btn_select_file.addSelectionListener(new SelectFileListener(selected_file, txt_sign));

        btn_non_detail.addSelectionListener(new RadioButtonListener(true, btn_radio_signFile, btn_radio_signText,
                btn_select_file, selected_file, txt_sign, btn_select_file));
        btn_detail.addSelectionListener(new RadioButtonListener(false, btn_radio_signFile, btn_radio_signText,
                btn_select_file, selected_file, txt_sign, btn_select_file));

        cmb_priv_key = new Combo(signCertGroup, SWT.DROP_DOWN);
        cmb_priv_key.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        addRSAPrivateKeysToDropdown();
        cmb_priv_key.select(0);

        Button btn_sign = new Button(signBtnComp, SWT.PUSH);
        btn_sign.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        btn_sign.setText(Messages.SignCert_btn_sign_with_key);
        btn_sign.addSelectionListener(new SigVisPluginOpenListener(btn_detail, selected_file, txt_sign, cmb_priv_key));

        StyledText stl_exp = (StyledText) exp.getChildren()[0];
        stl_exp.setText(Messages.SignCert_explain_text);
        composite.setVisible(false);
    }

    private void addRSAPrivateKeysToDropdown() {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        for (KeyStoreAlias ksAlias : Util.getAllRSAPublicKeys(ksm)) {
            if (Util.isSignedByJCTCA(ksAlias) == false) {
                continue;
            }
            String listEntry = ksAlias.getContactName() + " (" + ksAlias.getKeyLength() + "bit "; //$NON-NLS-1$ //$NON-NLS-2$
            if (ksAlias.getOperation().contains("RSA")) { //$NON-NLS-1$
                listEntry += "RSA"; //$NON-NLS-1$
            }
            listEntry += ", Hash: " + Util.formatHash(ksAlias.getHashValue()) + ")"; //$NON-NLS-1$ //$NON-NLS-2$
            cmb_priv_key.setData(listEntry, ksAlias);
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
//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca.UserViews;

import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.listeners.RevokeButtonListener;
import org.jcryptool.visual.jctca.listeners.UserShowCertsListener;

/**
 * Shows all valid certificates that have been created by the user. Independent of the revocation status.
 * 
 * @author mmacala
 * 
 */
public class ShowCert implements Views {
    private Composite composite;
    private List lst_certs;
    private Button btn_revoke;

    public ShowCert(Composite content, Composite exp) {
        composite = new Composite(content, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        GridData gd_comp = new GridData(SWT.FILL, SWT.FILL, true, true);
        composite.setLayoutData(gd_comp);

        Group showCertGroup = new Group(composite, SWT.NONE);
        showCertGroup.setLayout(new GridLayout(2, false));
        GridData gd_grp = new GridData(SWT.FILL, SWT.FILL, true, true);
        showCertGroup.setLayoutData(gd_grp);
        showCertGroup.setText(Messages.ShowCert_headline);

        Composite left = new Composite(showCertGroup, SWT.NONE);
        left.setLayout(new GridLayout(1, true));
        left.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));
        Composite right = new Composite(showCertGroup, SWT.NONE);
        right.setLayout(new GridLayout(2, false));
        right.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

        lst_certs = new List(left, SWT.BORDER);
        lst_certs.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));

        addRSAAndDSAKeysToDropdown();

        Label lbl_issued_to = new Label(right, SWT.NONE);
        lbl_issued_to.setFont(FontService.getNormalBoldFont());
        lbl_issued_to.setText(Messages.ShowCert_lbl_issued_to);
        new Label(right, SWT.NONE);

        Label lbl_common = new Label(right, SWT.NONE);
        lbl_common.setText(Messages.ShowCert_lbl_subject_cn);
        Label lbl_value_common = new Label(right, SWT.None);

        Label lbl_org = new Label(right, SWT.None);
        lbl_org.setText(Messages.ShowCert_lbl_subject_o);
        Label lbl_value_org = new Label(right, SWT.None);

        Label lbl_orgUnit = new Label(right, SWT.None);
        lbl_orgUnit.setText(Messages.ShowCert_lbl_subject_ou);
        Label lbl_value_orgUnit = new Label(right, SWT.None);

        Label lbl_city = new Label(right, SWT.None);
        lbl_city.setText(Messages.ShowCert_lbl_subject_l);
        Label lbl_value_city = new Label(right, SWT.None);

        Label lbl_country = new Label(right, SWT.None);
        lbl_country.setText(Messages.ShowCert_lbl_subject_c);
        Label lbl_value_country = new Label(right, SWT.None);

        Label lbl_mail = new Label(right, SWT.None);
        lbl_mail.setText(Messages.ShowCert_lbl_subject_e);
        Label lbl_value_mail = new Label(right, SWT.None);

        new Label(right, SWT.NONE);
        new Label(right, SWT.NONE);
        Label lbl_issued_by = new Label(right, SWT.NONE);
        lbl_issued_by.setFont(FontService.getNormalBoldFont());
        lbl_issued_by.setText(Messages.ShowCert_lbl_issued_by);
        new Label(right, SWT.NONE);

        Label lbl_common_by = new Label(right, SWT.NONE);
        lbl_common_by.setText(Messages.ShowCert_lbl_issuer_cn);
        Label lbl_value_common_by = new Label(right, SWT.None);

        Label lbl_org_by = new Label(right, SWT.None);
        lbl_org_by.setText(Messages.ShowCert_lbl_issuer_o);
        Label lbl_value_org_by = new Label(right, SWT.None);

        Label lbl_orgUnit_by = new Label(right, SWT.None);
        lbl_orgUnit_by.setText(Messages.ShowCert_lbl_issuer_ou);
        Label lbl_value_orgUnit_by = new Label(right, SWT.None);

        new Label(right, SWT.NONE);
        new Label(right, SWT.NONE);
        Label lbl_issue_date = new Label(right, SWT.NONE);
        lbl_issue_date.setFont(FontService.getNormalBoldFont());
        lbl_issue_date.setText(Messages.ShowCert_lbl_validity_perios);
        new Label(right, SWT.NONE);

        Label lbl_issued_on = new Label(right, SWT.None);
        lbl_issued_on.setText(Messages.ShowCert_lbl_issued_on);
        Label lbl_value_issued_on = new Label(right, SWT.None);
        Label lbl_expired_on = new Label(right, SWT.None);
        lbl_expired_on.setText(Messages.ShowCert_lbl_expires_on);
        Label lbl_value_expired_on = new Label(right, SWT.None);

        btn_revoke = new Button(right, SWT.PUSH);
        btn_revoke.setText(Messages.ShowCert_btn_revoke_cert);
        GridData revoke_gd = new GridData();
        revoke_gd.horizontalSpan = 2;
        btn_revoke.setLayoutData(revoke_gd);
        btn_revoke.setEnabled(false);
        UserShowCertsListener uscListener = new UserShowCertsListener(lbl_value_common, lbl_value_org,
                lbl_value_orgUnit, lbl_value_city, lbl_value_country, lbl_value_mail, lbl_value_common_by,
                lbl_value_org_by, lbl_value_orgUnit_by, lbl_value_issued_on, lbl_value_expired_on, btn_revoke);
        lst_certs.addSelectionListener(uscListener);

        btn_revoke.addSelectionListener(new RevokeButtonListener());

        new Label(right, SWT.NONE);
        new Label(right, SWT.NONE);

        StyledText stl_exp = (StyledText) exp.getChildren()[0];
        stl_exp.setText(Messages.ShowCert_explain_text);
        composite.setVisible(false);
    }

    private void addRSAAndDSAKeysToDropdown() {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        int i = 0;
        for (KeyStoreAlias ksAlias : Util.getAllRSAPublicKeys(ksm)) {
            if (Util.isSignedByJCTCA(ksAlias) == false) {
                continue;
            }
            Certificate cert = null;
            try {
                cert = ksm.getCertificate(ksAlias);
            } catch (UnrecoverableEntryException e) {
                LogUtil.logError(e);
            } catch (NoSuchAlgorithmException e) {
                LogUtil.logError(e);
            }
            String listEntry = ""; //$NON-NLS-1$
            if (cert instanceof X509Certificate) {
                X509Certificate x509 = (X509Certificate) cert;
                String[] subject = x509.getSubjectX500Principal().toString().split("CN="); //$NON-NLS-1$
                if (subject.length > 1) {
                    listEntry = x509.getSubjectX500Principal().toString().split("CN=")[1].split(",")[0] + " (" + ksAlias.getKeyLength() + "bit "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                } else {
                    listEntry = ksAlias.getContactName() + " (" + ksAlias.getKeyLength() + "bit "; //$NON-NLS-1$ //$NON-NLS-2$
                }
            } else {
                listEntry = ksAlias.getContactName() + " (" + ksAlias.getKeyLength() + "bit "; //$NON-NLS-1$ //$NON-NLS-2$
            }
            if (ksAlias.getOperation().contains("RSA")) { //$NON-NLS-1$
                listEntry += "RSA)"; //$NON-NLS-1$
            } else {
                listEntry += "DSA)"; //$NON-NLS-1$
            }
            lst_certs.add(listEntry);
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

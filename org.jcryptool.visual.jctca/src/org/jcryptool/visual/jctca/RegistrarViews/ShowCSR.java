//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca.RegistrarViews;

import java.util.Calendar;

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

/**
 * the view containing all components in the RA view
 * 
 * @author mmacala
 * 
 */
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
        lbl_firstname.setText(Messages.ShowCSR_lbl_first_name);
        lbl_value_firstname = new Label(center, SWT.FILL);
        GridData lbl_gd = new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1);
        lbl_value_firstname.setText(Messages.ShowCSR_firstname_bad_erika);
        lbl_value_firstname.setLayoutData(lbl_gd);

        lbl_lastname = new Label(center, SWT.None);
        lbl_lastname.setText(Messages.ShowCSR_lbl_last_name);
        lbl_value_lastname = new Label(center, SWT.FILL);
        lbl_value_lastname.setText(Messages.ShowCSR_lastname_bad_erika);
        lbl_value_lastname.setLayoutData(lbl_gd);

        lbl_street = new Label(center, SWT.None);
        lbl_street.setText(Messages.ShowCSR_lbl_street);
        lbl_value_street = new Label(center, SWT.FILL);
        lbl_value_street.setText(Messages.ShowCSR_street_bad_erika);
        lbl_value_street.setLayoutData(lbl_gd);

        lbl_ZIP = new Label(center, SWT.None);
        lbl_ZIP.setText(Messages.ShowCSR_lbl_zip);
        lbl_value_ZIP = new Label(center, SWT.FILL);
        lbl_value_ZIP.setText(Messages.ShowCSR_zip_bad_erika);
        lbl_value_ZIP.setLayoutData(lbl_gd);

        lbl_city = new Label(center, SWT.None);
        lbl_city.setText(Messages.ShowCSR_lbl_city);
        lbl_value_city = new Label(center, SWT.FILL);
        lbl_value_city.setText(Messages.ShowCSR_city_bad_erika);
        lbl_value_city.setLayoutData(lbl_gd);

        lbl_country = new Label(center, SWT.None);
        lbl_country.setText(Messages.ShowCSR_lbl_country);
        lbl_value_country = new Label(center, SWT.FILL);
        lbl_value_country.setText(Messages.ShowCSR_country_bad_erika);
        lbl_value_country.setLayoutData(lbl_gd);

        lbl_mail = new Label(center, SWT.None);
        lbl_mail.setText(Messages.ShowCSR_lbl_email);
        lbl_value_mail = new Label(center, SWT.FILL);
        lbl_value_mail.setText(Messages.ShowCSR_email_bad_erika);
        lbl_value_mail.setLayoutData(lbl_gd);

        Composite center_btn = new Composite(center, SWT.NONE);
        center_btn.setLayout(new GridLayout(2, true));
        GridData btn_gd = new GridData();
        btn_gd.horizontalSpan = 2;
        center_btn.setLayoutData(btn_gd);
        btn_verify_identity = new Button(center_btn, SWT.NONE);
        btn_verify_identity.setText(Messages.ShowCSR_btn_check_identity);
        btn_verify_identity.setData(new Integer(0));
        GridData btn_ld = new GridData(SWT.FILL, SWT.NONE, false, true);
        btn_ld.horizontalSpan = 2;
        btn_verify_identity.setLayoutData(btn_ld);

        btn_forward_csr = new Button(center_btn, SWT.NONE);
        btn_forward_csr.setText(Messages.ShowCSR_btn_csr_to_ca);
        btn_forward_csr.setData(new Integer(0));
        btn_forward_csr.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, true));
        btn_forward_csr.setEnabled(false);

        btn_reject_csr = new Button(center_btn, SWT.NONE);
        btn_reject_csr.setText(Messages.ShowCSR_btn_reject_csr);
        btn_reject_csr.setData(1);
        btn_reject_csr.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, true));
        btn_reject_csr.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, true));
        btn_reject_csr.setEnabled(false);

        CSRListener lst = new CSRListener(lbl_value_firstname, lbl_value_lastname, lbl_value_street, lbl_value_ZIP,
                lbl_value_city, lbl_value_country, lbl_value_mail, btn_forward_csr, btn_reject_csr, lst_csr);
        lst_csr.addSelectionListener(lst);
        btn_reject_csr.addSelectionListener(lst);
        btn_forward_csr.addSelectionListener(lst);
        VerifyIdentity verify = new VerifyIdentity(Display.getCurrent().getActiveShell(), lst_csr, btn_forward_csr,
                btn_reject_csr);
        btn_verify_identity.addSelectionListener(verify);
        RegistrarCSR csr = RegistrarCSR.getInstance();

        // the two standard CSRs that are there to teach the user
        csr.addCSR(Messages.ShowCSR_firstname_bad_erika, Messages.ShowCSR_lastname_bad_erika,
                Messages.ShowCSR_street_bad_erika, Messages.ShowCSR_zip_bad_erika, Messages.ShowCSR_city_bad_erika,
                Messages.ShowCSR_country_bad_erika, Messages.ShowCSR_email_bad_erika,
                "icons/ausweis.jpeg", null, null, Calendar.getInstance().getTime());//$NON-NLS-1$
        csr.addCSR(Messages.ShowCSR_first_name_blackhat, Messages.ShowCSR_last_name_blackhat,
                Messages.ShowCSR_street_black_hat, Messages.ShowCSR_zip_blackhat, Messages.ShowCSR_city_blackhat,
                Messages.ShowCSR_country_blackhat, Messages.ShowCSR_email_blackhat,
                "icons/ausweis_blackhat.jpg", null, null, Calendar.getInstance().getTime());//$NON-NLS-1$

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

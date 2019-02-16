//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.RegistrarCSR;

/**
 * Listener on the components in the RA view
 * 
 * @author mmacala
 * 
 */
public class CSRListener implements SelectionListener {
    private Label first;
    private Label last;
    private Label street;
    private Label zip;
    private Label town;
    private Label country;
    private Label mail;
    private Button btn_forward;
    private Button btn_reject;
    private List csrs;

    /**
     * constructor with all the needed widgets
     * 
     * @param first
     * @param last
     * @param street
     * @param zip
     * @param town
     * @param country
     * @param mail
     * @param btn_forward
     * @param btn_reject
     * @param csrs
     */
    public CSRListener(Label first, Label last, Label street, Label zip, Label town, Label country, Label mail,
            Button btn_forward, Button btn_reject, List csrs) {
        super();
        this.first = first;
        this.last = last;
        this.street = street;
        this.zip = zip;
        this.town = town;
        this.country = country;
        this.mail = mail;
        this.btn_forward = btn_forward;
        this.btn_reject = btn_reject;
        this.csrs = csrs;
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
    }

    @Override
    public void widgetSelected(SelectionEvent arg0) {
        Object src = arg0.getSource();
        int index = csrs.getSelectionIndex();
        RegistrarCSR regCSR = RegistrarCSR.getInstance();
        CSR csr = regCSR.getCSR(index);
        if (src instanceof List && csr != null) {
            this.setLabels(csr);
        } else if (src instanceof Button && csr != null) {
            Button btn = (Button) src;
            Integer data = (Integer) btn.getData();
            if (data.equals(0)) {
                csrs.remove(index);
                regCSR.removeCSR(csr);
                if (csr.getPrivAlias() == null) {
                    Util.showMessageBox(Messages.CSRListener_msgbox_title_fake_csr_sent_to_ca,
                            Messages.CSRListener_msgbox_text_fake_csr_to_ca_sent, SWT.ICON_WARNING);
                    return;
                }
                this.setLabels(new CSR("", "", "", "", "", "", "", "", null, null, null)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
                CertificateCSRR.getInstance().addCSR(csr);
                Util.showMessageBox(Messages.CSRListener_msgbox_title_csr_to_ca_sent,
                        Messages.CSRListener_msgbox_text_csr_to_ca_sent, SWT.ICON_INFORMATION);
            } else if (data.equals(1)) {
                csrs.remove(index);
                regCSR.removeCSR(csr);
                if (csr.getPrivAlias() == null) {
                    Util.showMessageBox(Messages.CSRListener_msgbox_title_fake_csr_rejected,
                            Messages.CSRListener_msgbox_text_fake_csr_rejected, SWT.ICON_INFORMATION);
                }
                this.setLabels(new CSR("", "", "", "", "", "", "", "", null, null, null)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
                if (csr.getPubAlias() != null) {
                    Util.showMessageBox(Messages.CSRListener_msgbox_title_csr_rejected,
                            Messages.CSRListener_msgbox_text_csr_rejected, SWT.ICON_INFORMATION);
                }
            }
        }
        mail.getParent().layout();
    }

    /**
     * sets the labels according to the information provided in the CSR
     * 
     * @param csr
     */
    public void setLabels(CSR csr) {
        first.setText(csr.getFirst());
        last.setText(csr.getLast());
        street.setText(csr.getStreet());
        zip.setText(csr.getZip());
        town.setText(csr.getTown());
        country.setText(csr.getCountry());
        mail.setText(csr.getMail());
        btn_forward.setEnabled(csr.isForwardenabled());
        btn_reject.setEnabled(csr.isRejectenabled());
        mail.getParent().layout();
    }

}

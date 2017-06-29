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
package org.jcryptool.visual.jctca.RegistrarViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.visual.jctca.Activator;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;
import org.jcryptool.visual.jctca.CertificateClasses.RegistrarCSR;

/**
 * the dialog shown to the user when whe wants to verify an identity
 * 
 * @author mmacala
 * 
 */
public class VerifyIdentity extends Dialog implements SelectionListener {
    private Shell parent;

    private Label img;

    private Button btn_true;
    private Button btn_false;
    private Button btn_cancel;
    private List csr;
    private Button forward_csr;
    private Button reject_csr;

    private CSR c;
    private Group grp_exp;
    private Shell shell;

    public VerifyIdentity(Shell parent, List csr, Button forward_csr, Button reject_csr) {
        super(parent, SWT.APPLICATION_MODAL);
        this.parent = parent;

        this.csr = csr;
        this.forward_csr = forward_csr;
        this.reject_csr = reject_csr;
    }

    public String open(CSR c) {
        shell = new Shell(getParent(), SWT.TITLE);
        this.c = c;
        parent = shell;
        shell.setText(getText());
        createContents(shell);
        String proof = c.getProof();
        Image i = null;
        if (proof == null) {
            proof = "icons/ausweis.jpeg";//$NON-NLS-1$
        }
        if (proof.contains("icons\\") || proof.contains("icons/")) {//$NON-NLS-1$ //$NON-NLS-2$
            i = Activator.getImageDescriptor(proof).createImage();
        } else {
            i = new Image(Display.getCurrent(), c.getProof());
        }
        img.setImage(i);
        parent.layout();
        shell.pack();
        shell.open();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return "foo"; //$NON-NLS-1$
    }

    private void createContents(final Shell shell) {
        shell.setLayout(new GridLayout(1, true));
        shell.setText(Messages.VerifyIdentity_title); // zeigt den Titel an

        Composite main = new Composite(shell, SWT.FILL);
        main.setLayout(new GridLayout(4, false));
        Composite btns = new Composite(main, SWT.None);
        btns.setLayout(new GridLayout(3, true));
        GridData gd_btns = new GridData(GridData.FILL_HORIZONTAL);
        gd_btns.horizontalSpan = 4;
        btns.setLayoutData(gd_btns);

        btn_true = new Button(btns, SWT.PUSH);
        btn_true.setText(Messages.VerifyIdentity_btn_mark_identity_correct);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        // data.horizontalSpan = 2;
        btn_true.setLayoutData(data);
        btn_true.addSelectionListener(this);

        btn_false = new Button(btns, SWT.PUSH);
        btn_false.setText(Messages.VerifyIdentity_btn_mark_identity_fake);
        data = new GridData(GridData.FILL_HORIZONTAL);
        // data.horizontalSpan = 2;
        btn_false.setLayoutData(data);
        btn_false.addSelectionListener(this);

        btn_cancel = new Button(btns, SWT.PUSH);
        btn_cancel.setText(Messages.VerifyIdentity_btn_cancel);
        data = new GridData(GridData.FILL_HORIZONTAL);
        btn_cancel.setLayoutData(data);
        btn_cancel.addSelectionListener(this);

        shell.setDefaultButton(btn_cancel);

        data = new GridData(1, 1, true, true);
        data.horizontalSpan = 3;
        img = new Label(main, SWT.FILL);
        img.setLayoutData(data);

        grp_exp = new Group(main, SWT.FILL);
        grp_exp.setText(Messages.VerifyIdentity_grp_explain_headline);
        data = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
        grp_exp.setLayoutData(data);
        grp_exp.setLayout(new GridLayout(1, false));

        Label lbl_exp = new Label(grp_exp, SWT.WRAP);
        lbl_exp.setText(Messages.VerifyIdentity_explain_text);
        data = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
        lbl_exp.setLayoutData(data);
        data.widthHint = 350;

        grp_exp.layout();

    }

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
    }

    @Override
    public void widgetSelected(SelectionEvent arg0) {
        Button btn = (Button) arg0.getSource();
        Integer data = (Integer) btn.getData();

        if (data != null && data.equals(0)) {
            int selected = csr.getSelectionIndex();
            CSR c = RegistrarCSR.getInstance().getCSR(selected);
            if (c != null && (shell == null || !shell.isVisible())) {
                this.open(c);
            }
        } else if (btn.equals(btn_true)) {
            forward_csr.setEnabled(true);
            reject_csr.setEnabled(true);
            c.setForwardenabled(true);
            c.setRejectenabled(true);
            parent.setVisible(false);
        } else if (btn.equals(btn_false)) {
            forward_csr.setEnabled(false);
            reject_csr.setEnabled(true);
            c.setForwardenabled(false);
            c.setRejectenabled(true);
            parent.setVisible(false);
        } else if (btn.equals(btn_cancel)) {
            forward_csr.setEnabled(false);
            reject_csr.setEnabled(false);
            c.setForwardenabled(false);
            c.setRejectenabled(false);
            parent.setVisible(false);

        }

    }
}

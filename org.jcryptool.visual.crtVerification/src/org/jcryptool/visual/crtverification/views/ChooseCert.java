// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crtverification.views;

import java.security.cert.X509Certificate;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.wizard.Wizard;

public class ChooseCert extends Wizard {
    private String name = "Java Keystore";
    private ChooseCertPage page;
    private int certType; // [1] UserCert; [2] Cert; [3] RootCert
    private CrtVerViewComposite composite;

    public ChooseCert(int type, CrtVerViewComposite composite) {
        super();
        this.composite = composite;
        TrayDialog.setDialogHelpAvailable(false);
        setWindowTitle(name);
        certType = type;
    }

    @Override
    public void addPages() {
        page = new ChooseCertPage(name, certType, composite.controller);
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        X509Certificate cert = (X509Certificate) composite.controller.getKsc().getCertificate(page.contact);
        composite.controller.loadCertificate(page, cert, page.contact);
        composite.btnValidate.setFocus();
        return true;
    }

    @Override
    public boolean performCancel() {
        composite.btnValidate.setFocus();
        return true;
    }

}

// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class ChooseCertPage extends WizardPage {
   	private ChooseCertComposite compositeFile;
   	private CrtVerViewController controller;
   	String contact_name;
    int certType; // [1] UserCert; [2] Cert; [3] RootCert
    
    public int getCertType() {
		return certType;
	}


	public void setCertType(int certType) {
		this.certType = certType;
	}
    
    public ChooseCertPage(String pageName, int type, CrtVerViewController controller) {
        super(pageName);
        this.controller = controller;
        certType = type;
        setTitle(pageName);
        setDescription(Messages.ChooseCertPage_description);
    }
    
    @Override
	public void createControl(Composite parent) {
        setPageComplete(false);
        compositeFile = new ChooseCertComposite(parent, SWT.NONE, this, controller);
        setControl(compositeFile);
    }

    /**
     * @return the compositeFile
     */
    public ChooseCertComposite getCompositeFile() {
        return compositeFile;
    }
}

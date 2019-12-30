// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.visual.sigVerification.algorithm.Input;

/**
 * This class contains the page containing the hash wizard composite. It is a part of the hash
 * wizard.
 * 
 * @author Wilfing
 */
public class HashWizardPage extends WizardPage {
    private HashComposite composite;
    private Input input;

    protected HashWizardPage(String pageName, Input input) {
        super(Messages.HashWizard_header);
        this.input = input;
//        setDescription(Messages.HashWizard_header);
        setTitle(Messages.HashWizard_WindowTitle);
    }

    @Override
	public void createControl(Composite parent) {
    	composite = new HashComposite(parent, NONE, input);
        setControl(composite);
        setPageComplete(true);
    }

    /**
     * @return the grpHashes
     */
    public Group getGrpHashes() {
        return composite.getGrpHashes();
    }
}

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
package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * This class contains the page containing the hash wizard composite. It is a part of the hash wizard.
 * 
 * @author Grebe
 */
public class HashWizardPage extends WizardPage {
    private HashComposite composite;

    protected HashWizardPage(String pageName) {
        super(pageName);
        setTitle(Messages.HashWizard_WindowTitle);
    }

    public void createControl(Composite parent) {
        composite = new HashComposite(parent, NONE);
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

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This class contains loads the composite for the first page of the input wizard.
 * 
 * @author Grebe
 */
public class InputWizardPage extends WizardPage {
    private InputComposite composite;
    private boolean enableNext = true;

    public InputWizardPage(String pageName) {
        super(pageName);
        setTitle(Messages.InputWizard_title);
        setDescription(Messages.InputWizard_header);
    }

    public void createControl(Composite parent) {
        composite = new InputComposite(parent, NONE);
        setControl(composite);
        setPageComplete(true);
    }

    public int getRdoSelection() {
        if (composite.getRdoFromEditor().getSelection())
            return 0;
        else
            return 1;
    }

    public boolean canFlipToNextPage() {
        return enableNext;
    }
}

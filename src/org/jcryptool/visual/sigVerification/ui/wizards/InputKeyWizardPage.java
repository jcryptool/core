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
package org.jcryptool.visual.sigVerification.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.visual.sigVerification.ui.wizards.Messages;

/**
 * This class contains loads the composite for the first page of the input wizard.
 * 
 * @author Grebe
 */
public class InputKeyWizardPage extends WizardPage {
    private InputKeyComposite composite;
    private boolean enableNext = true;

    public InputKeyWizardPage(String pageName) {
        super(pageName);
        setTitle(Messages.InputKeyWizard_title);
        setDescription(Messages.InputKeyWizard_header);
    }

    public void createControl(Composite parent) {
        composite = new InputKeyComposite(parent, NONE);
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

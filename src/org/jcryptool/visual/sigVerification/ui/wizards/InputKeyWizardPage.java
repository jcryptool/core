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
import org.jcryptool.visual.sigVerification.algorithm.Input;
import org.jcryptool.visual.sigVerification.algorithm.SigVerification;
import org.jcryptool.visual.sigVerification.ui.wizards.Messages;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * This class contains loads the composite for the first page of the input wizard.
 * 
 * @author Wilf
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
        composite.getRdoFromFile().setBounds(10, 10, 430, 18);
        composite.getRdoFromEditor().setBounds(10, 34, 430, 18);
        composite.getRdoFromKeyStore().setBounds(10, 58, 430, 18);
        setControl(composite);
                
    }

    public int getRdoSelection() {
        if (composite.getRdoFromEditor().getSelection())
            return 0;
        else if(composite.getRdoFromFile().getSelection())
            return 1;
        else
        	return 2;
    }

    public boolean canFlipToNextPage() {
        return enableNext;
    }
}

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

/**
 * This class contains the page containing the editor input composite. It is a part of the Input wizard.
 * 
 * @author Grebe
 */
public class InputKeyEditorWizardPage extends WizardPage {
    private InputKeyEditorComposite compositeEditor;

    public InputKeyEditorWizardPage(String pageName) {
        super(pageName);

        setTitle(Messages.InputKeyEditorWizard_title);
        setDescription(Messages.InputKeyEditorWizard_header);
    }

    @Override
    public void createControl(Composite parent) {
        compositeEditor = new InputKeyEditorComposite(parent, NONE, this);
        compositeEditor.setFocus();
        setControl(compositeEditor);
        setPageComplete(false);
    }
}
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

/**
 * This class contains the page containing the editor input composite. It is a part of the Input wizard.
 * 
 * @author Grebe
 */
public class InputEditorWizardPage extends WizardPage {
    private InputEditorComposite compositeEditor;

    public InputEditorWizardPage(String pageName) {
        super(pageName);
        setTitle(Messages.InputEditorWizard_title);
        setDescription(Messages.InputEditorWizard_header);
    }

    @Override
    public void createControl(Composite parent) {
        compositeEditor = new InputEditorComposite(parent, NONE, this);
        setControl(compositeEditor);
        setPageComplete(false);
    }
    
    @Override
    public void setVisible(boolean visible) {
    	super.setVisible(visible);
    	if (visible)
    		compositeEditor.setInitialFocus();
    }
}
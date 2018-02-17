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
 * This class contains the page containing the file input composite. It is a part of the Input wizard.
 * 
 * @author Grebe
 */
public class InputFileWizardPage extends WizardPage {
    private InputFileComposite compositeFile;

    public InputFileWizardPage(String pageName) {
        super(pageName);
        setTitle(Messages.InputFileWizard_title);
    }

    public void createControl(Composite parent) {
        setPageComplete(false);
        compositeFile = new InputFileComposite(parent, NONE, this);
        setControl(compositeFile);
    }

    /**
     * @return the compositeFile
     */
    public InputFileComposite getCompositeFile() {
        return compositeFile;
    }

    @Override
    public void setVisible(boolean visible) {
    	super.setVisible(visible);
    	if (visible) {
    		compositeFile.setInitialFocus();
    		compositeFile.updateMaxSize();
    		setDescription(Messages.InputFileWizard_header + " " + compositeFile.getMaxSizeInMB() + "MB.");
    	}
    }
}

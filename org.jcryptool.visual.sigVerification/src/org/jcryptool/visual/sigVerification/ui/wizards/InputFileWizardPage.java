// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.visual.sigVerification.ui.wizards.Messages;
import org.jcryptool.visual.sigVerification.algorithm.Input;

/**
 * This class contains the page containing the file input composite. It is a part of the Input
 * wizard.
 * 
 * @author Wilfing
 */
public class InputFileWizardPage extends WizardPage {
    private InputFileComposite compositeFile;
    private Input input;

    public InputFileWizardPage(String pageName, Input input) {
        super(pageName);
        this.input = input;
        setTitle(Messages.InputFileWizard_title);
    }

    public void createControl(Composite parent) {
        setPageComplete(false);
        compositeFile = new InputFileComposite(parent, NONE, this, input);
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
    		compositeFile.updateMaxSize();
    		setDescription(Messages.InputFileWizard_header + " " + compositeFile.getMaxSizeInMB() + "MB.");
    	}
    }
}

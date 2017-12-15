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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * This class contains the GUI elements for first page of the file input wizard.
 * 
 * @author Grebe
 */
public class InputComposite extends Composite {
    private Button rdoFromFile;
    private Button rdoFromEditor;

    public InputComposite(Composite parent, int style) {
        super(parent, style);
        
        setLayout(new GridLayout());

        rdoFromFile = new Button(this, SWT.RADIO);
        rdoFromFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdoFromFile.setText(Messages.InputWizard_rdoFromFile);

        rdoFromEditor = new Button(this, SWT.RADIO);
        rdoFromEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdoFromEditor.setText(Messages.InputWizard_rdoFromEditor);

        rdoFromFile.setSelection(true);
    }

    /**
     * @return the rdoFromFile
     */
    public Button getRdoFromFile() {
        return rdoFromFile;
    }

    /**
     * @return the rdoFromEditor
     */
    public Button getRdoFromEditor() {
        return rdoFromEditor;
    }
}

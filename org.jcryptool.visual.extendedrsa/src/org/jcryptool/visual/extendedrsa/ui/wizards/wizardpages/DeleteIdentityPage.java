// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa.ui.wizards.wizardpages;

import java.util.Vector;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.extendedrsa.ExtendedTabFolder;
import org.jcryptool.visual.extendedrsa.IdentityManager;

/**
 * This is the wizard to delete a new Identity with the button in the visual
 * 
 * @author Christoph Schnepf, Patrick Zillner
 * 
 */
public class DeleteIdentityPage extends WizardPage {

    private Combo selectedIdentity;

    public DeleteIdentityPage(ExtendedTabFolder tabfolder) {
        super(Messages.DeleteIdentityPage_0, Messages.DeleteIdentityPage_1, null);
        setDescription(Messages.DeleteIdentityPage_2);
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        GridLayout grid = new GridLayout(2, false);
        grid.horizontalSpacing = 20;
        grid.verticalSpacing = 10;
        container.setLayout(grid);

        Label lbl_chooseId = new Label(container, SWT.NONE);
        lbl_chooseId.setText(Messages.DeleteIdentityPage_3);

        selectedIdentity = new Combo(container, SWT.READ_ONLY);
        GridData gd_selectedIdentity = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        selectedIdentity.setLayoutData(gd_selectedIdentity);

        Vector<String> ids = IdentityManager.getInstance().getContacts();
        for (String s : ids) {
            if (!s.equals(Messages.DeleteIdentityPage_4) && (!s.equals(Messages.DeleteIdentityPage_5))) {
                selectedIdentity.add(s);
            }
        }

        selectedIdentity.select(0);

        Label hint = new Label(container, SWT.WRAP);
      hint.setFont(FontService.getNormalBoldFont());
      hint.setText(Messages.DeleteIdentityPage_6);
        GridData gd_hint = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        hint.setLayoutData(gd_hint);
    }

    public Combo getSelectedIdentity() {
        return selectedIdentity;
    }
}

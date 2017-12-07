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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.extendedrsa.IdentityManager;
import org.jcryptool.visual.library.Lib;

/**
 * This is the wizardPage for a new Identity.
 * 
 * @author Christoph Schnepf, Patrick Zillner
 * 
 */
public class NewIdentityPage extends WizardPage implements ModifyListener {
    private Text idName;
    private Text idForename;
    private Text idSurname;
    private Text idOrganisation;
    private Text idRegion;
    /** a {@link VerifyListener} instance that makes sure only digits are entered. */
    private static final VerifyListener VL = Lib.getVerifyListener(Lib.CHARACTERS);

    public NewIdentityPage() {
        super(Messages.NewIdentityPage_0, Messages.NewIdentityPage_1, null);
        setDescription(Messages.NewIdentityPage_2);
        setPageComplete(false);
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        GridLayout layout = new GridLayout(3, false);
        layout.horizontalSpacing = 20;
        layout.verticalSpacing = 10;
        container.setLayout(layout);

        Label lblName = new Label(container, SWT.NONE);
        lblName.setText(Messages.NewIdentityPage_3);
        idName = new Text(container, SWT.BORDER);
        GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_text.widthHint = 200;
        idName.setLayoutData(gd_text);
        idName.addModifyListener(this);
        idName.addVerifyListener(VL);
        Label mandatory = new Label(container, SWT.NONE);
        mandatory.setText(Messages.NewIdentityPage_4);

        Label lblVorname = new Label(container, SWT.NONE);
        lblVorname.setText(Messages.NewIdentityPage_5);
        idForename = new Text(container, SWT.BORDER);
        GridData gd_idForename = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gd_idForename.widthHint = 200;
        idForename.setLayoutData(gd_idForename);

        Label lblNachname = new Label(container, SWT.NONE);
        lblNachname.setText(Messages.NewIdentityPage_6);
        idSurname = new Text(container, SWT.BORDER);
        GridData gd_idSurname = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gd_idSurname.widthHint = 200;
        idSurname.setLayoutData(gd_idSurname);
        
        Label lblFirma = new Label(container, SWT.NONE);
        lblFirma.setText(Messages.NewIdentityPage_7);
        idOrganisation = new Text(container, SWT.BORDER);
        GridData gd_txt_idOrganisation = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gd_txt_idOrganisation.widthHint = 200;
        idOrganisation.setLayoutData(gd_txt_idOrganisation);

        Label lblLand = new Label(container, SWT.NONE);
        lblLand.setText(Messages.NewIdentityPage_8);
        idRegion = new Text(container, SWT.BORDER);
        GridData gd_idRegion = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gd_idRegion.widthHint = 200;
        idRegion.setLayoutData(gd_idRegion);

        Label hint = new Label(container, SWT.WRAP);
        hint.setFont(FontService.getNormalBoldFont());
        hint.setText(Messages.NewIdentityPage_9);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        hint.setLayoutData(gd);
    }

    public String getIdName() {
        return idName.getText().toString();
    }

    public String getIdForename() {
        return idForename.getText().toString();
    }

    public String getIdSurname() {
        return idSurname.getText().toString();
    }

    public String getIdOrganisation() {
        return idOrganisation.getText().toString();
    }

    public String getIdRegion() {
        return idRegion.getText().toString();
    }

    @Override
    public void modifyText(ModifyEvent e) {
        if (idName.getText().length() > 0) {
            if (IdentityManager.getInstance().getContacts().contains(idName.getText().toString())) {
                setPageComplete(false);
                setErrorMessage(Messages.NewIdentityPage_10);
            } else {
                setErrorMessage(null);
                setPageComplete(true);
            }
        } else {
            setPageComplete(false);
        }

    }

}

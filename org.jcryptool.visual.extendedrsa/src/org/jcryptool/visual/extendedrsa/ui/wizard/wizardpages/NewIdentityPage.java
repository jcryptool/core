//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa.ui.wizard.wizardpages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

/**
 * This is the wizardPage for a new Identity.
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class NewIdentityPage extends WizardPage implements ModifyListener{
	private Text idName;
	private Text idForename;
	private Text idSurname;
	private Text idOrganisation;
	private Text idRegion;

	public NewIdentityPage() {
		super("Neue Identit\u00e4t", "Neue Identit\u00e4t", null);
        setDescription("Bitte f\u00fcllen Sie die Felder aus und dr\u00fccken Sie 'Fertig stellen' um die Identit\u00e4t zu erstellen.");
        setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 40;
		layout.verticalSpacing = 10;
		container.setLayout(layout);
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name der Identit\u00e4t:");
		
		idName = new Text(container, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_text.widthHint = 155;
		idName.setLayoutData(gd_text);
		idName.addModifyListener(this);
		
		Label lblVorname = new Label(container, SWT.NONE);
		lblVorname.setText("Vorname:");
		
		idForename = new Text(container, SWT.BORDER);
		GridData gd_idForename = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		gd_idForename.widthHint = 155;
		idForename.setLayoutData(gd_idForename);
		
		Label lblNachname = new Label(container, SWT.NONE);
		lblNachname.setText("Nachname:");
		
		idSurname = new Text(container, SWT.BORDER);
		GridData gd_idSurname = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		gd_idSurname.widthHint = 155;
		idSurname.setLayoutData(gd_idSurname);
		
		Label lblFirma = new Label(container, SWT.NONE);
		lblFirma.setText("Firma/Organisation:");
		
		idOrganisation = new Text(container, SWT.BORDER);
		GridData gd_txt_idOrganisation = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		gd_txt_idOrganisation.widthHint = 155;
		idOrganisation.setLayoutData(gd_txt_idOrganisation);
		
		Label lblLand = new Label(container, SWT.NONE);
		lblLand.setText("Land/Region:");
		
		idRegion = new Text(container, SWT.BORDER);
		GridData gd_idRegion = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		gd_idRegion.widthHint = 155;
		idRegion.setLayoutData(gd_idRegion);	
	}

	public Text getIdName() {
		return idName;
	}

	public Text getIdForename() {
		return idForename;
	}

	public Text getIdSurname() {
		return idSurname;
	}

	public Text getIdOrganisation() {
		return idOrganisation;
	}

	public Text getIdRegion() {
		return idRegion;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		if (idName.getText().length() > 0){
			setPageComplete(true);
		}else{
			setPageComplete(false);
		}
		
	}

}

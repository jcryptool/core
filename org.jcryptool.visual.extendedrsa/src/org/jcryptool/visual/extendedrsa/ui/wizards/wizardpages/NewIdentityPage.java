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
package org.jcryptool.visual.extendedrsa.ui.wizards.wizardpages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.extendedrsa.IdentityManager;

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
        setDescription("Bitte f\u00fcllen Sie die Felder aus und dr\u00fccken Sie 'Fertigstellen', um die Identit\u00e4t zu erzeugen.\nIhre Identit\u00e4t wird dann im Schl\u00fcsselspeicher gespeichert.");
        setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 40;
		layout.verticalSpacing = 10;
		container.setLayout(layout);
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name der Identit\u00e4t:");
		idName = new Text(container, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		gd_text.widthHint = 155;
		idName.setLayoutData(gd_text);
		idName.addModifyListener(this);
		Label mandatory = new Label (container, SWT.NONE);
		mandatory.setText("(erforderlich)");
		
		Label lblVorname = new Label(container, SWT.NONE);
		lblVorname.setText("Vorname:");
		idForename = new Text(container, SWT.BORDER);
		GridData gd_idForename = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		gd_idForename.widthHint = 155;
		idForename.setLayoutData(gd_idForename);
		new Label(container, SWT.NONE).setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		
		Label lblNachname = new Label(container, SWT.NONE);
		lblNachname.setText("Nachname:");
		idSurname = new Text(container, SWT.BORDER);
		GridData gd_idSurname = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		gd_idSurname.widthHint = 155;
		idSurname.setLayoutData(gd_idSurname);
		new Label(container, SWT.NONE).setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		
		Label lblFirma = new Label(container, SWT.NONE);
		lblFirma.setText("Firma/Organisation:");
		idOrganisation = new Text(container, SWT.BORDER);
		GridData gd_txt_idOrganisation = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		gd_txt_idOrganisation.widthHint = 155;
		idOrganisation.setLayoutData(gd_txt_idOrganisation);
		new Label(container, SWT.NONE).setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		
		Label lblLand = new Label(container, SWT.NONE);
		lblLand.setText("Land/Region:");
		idRegion = new Text(container, SWT.BORDER);
		GridData gd_idRegion = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		gd_idRegion.widthHint = 155;
		idRegion.setLayoutData(gd_idRegion);
		
		for (int i = 0; i < 4; i++){
			new Label(container, SWT.NONE).setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		}
		
		Label hint = new Label(container, SWT.WRAP);
		hint.setFont(FontService.getNormalBoldFont());
		hint.setText("Achtung: Diese Identit\u00e4t wird erst als Registerkarte in der Visualisierung angezeigt, wenn diese durch den Button „Identit\u00e4ten ein-/ausblenden“ selektiert wurde.");
		GridData gd = new GridData(SWT.LEFT, SWT.LEFT, true, true, 3, 2);
		gd.widthHint = 500;
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
		if (idName.getText().length() > 0){
			if (IdentityManager.getInstance().getContacts().contains(idName.getText().toString())){
				setPageComplete(false);
				setErrorMessage("Achtung: Diese Identit\u00e4t existiert bereits. Bitte einen anderen Namen w\u00e4hlen.");
			}else{
				setErrorMessage(null);
				setPageComplete(true);
			}
		}else{
			setPageComplete(false);
		}
		
	}

}

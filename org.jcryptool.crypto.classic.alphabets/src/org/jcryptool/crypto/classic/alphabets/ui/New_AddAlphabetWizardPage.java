//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.alphabets.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The WizardPage for the AddAlphabet wizard.
 * 
 * @author simlei
 *
 */
public class New_AddAlphabetWizardPage extends WizardPage {

	private Group alphabetCharsGroup;
	private Label charsLabel;
	private Label filterDescriptionLabel;
	private Text charsText;
	private Group alphabetNameGroup;
	private Label nameLabel;
	private Text nameText;
	private Label maxNameCharsLabel;

	/** The name of the new alphabet */
	private String name;
	
	/** The charset of the new alphabet */
	private String chars;
	private CreateAlphabetComposite blockAlphaComposite;
	
	/**
	 * Creates a new instance of AddAlphabetWizardPage.
	 */
	public New_AddAlphabetWizardPage() {
		super("page_name", Messages.getString("AddAlphabetWizardPage.1"), null);	 //$NON-NLS-1$ //$NON-NLS-2$
		setPageComplete(false);
		setDescription(Messages.getString("AddAlphabetWizardPage.2")); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite pageComposite = new Composite(parent, SWT.NULL);	
		pageComposite.setSize(new Point(350, 175));
		pageComposite.setLayout(new GridLayout());
		pageComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		blockAlphaComposite = new CreateAlphabetComposite(pageComposite);
		blockAlphaComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
//		AlphabetSelectorComposite selector1 = new AlphabetSelectorComposite(pageComposite, Mode.COMBO_BOX_WITH_CUSTOM_ALPHABET_BUTTON);
//		selector1.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
//		createSeparator(pageComposite);
		
//		AlphabetSelectorComposite selector2 = new AlphabetSelectorComposite(pageComposite, Mode.SINGLE_COMBO_BOX_ONLY_EXISTING_ALPHABETS);
//		selector2.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
//		createSeparator(pageComposite);
		
//		AlphabetSelectorComposite selector3 = new AlphabetSelectorComposite(pageComposite, Mode.SINGLE_COMBO_BOX_WITH_CUSTOM_ALPHABETS);
//		selector3.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		setControl(pageComposite);		
	}
	
	private void createSeparator(Composite parent) {
		Label s = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		s.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}

	/**
	 * Returns the name of the new alphabet.
	 * 
	 * @return	The name of the new alphabet
	 */
	public String getAlphabetName() {
		return name;
	}
	
	/**
	 * Returns the charset of the new alphabet.
	 * 
	 * @return	The charset of the new alphabet
	 */
	public String getAlphabetCharset() {
		return chars;
	}
	
}

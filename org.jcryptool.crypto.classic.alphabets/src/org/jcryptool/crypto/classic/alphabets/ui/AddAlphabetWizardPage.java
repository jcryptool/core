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
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * The WizardPage for the AddAlphabet wizard.
 * 
 * @author t-kern
 *
 */
public class AddAlphabetWizardPage extends WizardPage implements Listener {

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
	
	/**
	 * Creates a new instance of AddAlphabetWizardPage.
	 */
	public AddAlphabetWizardPage() {
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
		createAlphabetNameGroup(pageComposite);
		createAlphabetCharsGroup(pageComposite);
		setControl(pageComposite);		
	}
	
	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget == nameText) {
			name = nameText.getText();
		} else if (event.widget == charsText) {
			chars = charsText.getText();
		}
		setPageComplete(mayFinish());
	}
	
	/**
	 * Returns <code>true</code>, when at both textfields contain at least one character. 
	 * 
	 * @return	<code>true</code>, when at both textfields contain at least one character
	 */
	private boolean mayFinish() {
		if ( nameText.getText().length() > 0 && charsText.getText().length() > 0 ) {
			return true;
		}
		return false;
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
	
	/**
	 * This method initializes alphabetNameGroup	
	 *
	 */
	private void createAlphabetNameGroup(Composite parent) {
		GridData maxNameCharsLabelGridData = new GridData();
		maxNameCharsLabelGridData.grabExcessHorizontalSpace = true;
		maxNameCharsLabelGridData.grabExcessVerticalSpace = true;
		GridData nameLabelGridData = new GridData();
		nameLabelGridData.grabExcessHorizontalSpace = true;
		nameLabelGridData.grabExcessVerticalSpace = true;
		GridData nameTextGridData = new GridData();
		nameTextGridData.grabExcessHorizontalSpace = true;
		nameTextGridData.horizontalAlignment = GridData.FILL;
		nameTextGridData.verticalAlignment = GridData.CENTER;
		nameTextGridData.grabExcessVerticalSpace = true;
		GridData alphabetNameGroupGridData = new GridData();
		alphabetNameGroupGridData.grabExcessHorizontalSpace = true;
		alphabetNameGroupGridData.verticalAlignment = GridData.FILL;
		alphabetNameGroupGridData.grabExcessVerticalSpace = true;
		alphabetNameGroupGridData.horizontalAlignment = GridData.FILL;
		alphabetNameGroup = new Group(parent, SWT.NONE);
		alphabetNameGroup.setLayout(new GridLayout());
		alphabetNameGroup.setText(Messages.getString("AddAlphabetWizardPage.3")); //$NON-NLS-1$
		alphabetNameGroup.setLayoutData(alphabetNameGroupGridData);
		nameLabel = new Label(alphabetNameGroup, SWT.NONE);
		nameLabel.setText(Messages.getString("AddAlphabetWizardPage.4")); //$NON-NLS-1$
		nameLabel.setLayoutData(nameLabelGridData);
		nameText = new Text(alphabetNameGroup, SWT.BORDER);
		nameText.setLayoutData(nameTextGridData);
		nameText.addListener(SWT.Modify, this);
		
		nameText.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				setErrorMessage(null);
				if (e.character != SWT.BS && e.character != SWT.DEL) {
					if (nameText.getText().length() >= 75) {
						setErrorMessage(Messages.getString("AddAlphabetWizardPage.5")); //$NON-NLS-1$
						e.doit = false;						
					}
				}
			}

		});
		
		maxNameCharsLabel = new Label(alphabetNameGroup, SWT.NONE);
		maxNameCharsLabel.setText(Messages.getString("AddAlphabetWizardPage.6")); //$NON-NLS-1$
		maxNameCharsLabel.setLayoutData(maxNameCharsLabelGridData);
	}

	/**
	 * This method initializes alphabetCharsGroup	
	 *
	 */
	private void createAlphabetCharsGroup(Composite parent) {
		GridData charsLabelGridData = new GridData();
		charsLabelGridData.horizontalAlignment = GridData.FILL;
		charsLabelGridData.grabExcessHorizontalSpace = true;
		charsLabelGridData.grabExcessVerticalSpace = true;
		charsLabelGridData.verticalAlignment = GridData.CENTER;
		GridData charsTextGridData = new GridData();
		charsTextGridData.horizontalAlignment = GridData.FILL;
		charsTextGridData.grabExcessHorizontalSpace = true;
		charsTextGridData.grabExcessVerticalSpace = true;
		charsTextGridData.verticalAlignment = GridData.CENTER;
		GridData filterDescriptionLabelGridData = new GridData();
		filterDescriptionLabelGridData.horizontalAlignment = GridData.FILL;
		filterDescriptionLabelGridData.grabExcessHorizontalSpace = true;
		filterDescriptionLabelGridData.grabExcessVerticalSpace = true;
		filterDescriptionLabelGridData.verticalAlignment = GridData.CENTER;
		GridData alphabetCharsGroupGridData = new GridData();
		alphabetCharsGroupGridData.grabExcessHorizontalSpace = true;
		alphabetCharsGroupGridData.verticalAlignment = GridData.FILL;
		alphabetCharsGroupGridData.grabExcessVerticalSpace = true;
		alphabetCharsGroupGridData.horizontalAlignment = GridData.FILL;
		alphabetCharsGroup = new Group(parent, SWT.NONE);
		alphabetCharsGroup.setLayout(new GridLayout());
		alphabetCharsGroup.setText(Messages.getString("AddAlphabetWizardPage.7")); //$NON-NLS-1$
		alphabetCharsGroup.setLayoutData(alphabetCharsGroupGridData);
		charsLabel = new Label(alphabetCharsGroup, SWT.NONE);
		charsLabel.setText(Messages.getString("AddAlphabetWizardPage.8")); //$NON-NLS-1$
		charsLabel.setLayoutData(charsLabelGridData);
		charsText = new Text(alphabetCharsGroup, SWT.BORDER);
		charsText.setLayoutData(charsTextGridData);
		charsText.addListener(SWT.Modify, this);
		charsText.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				setErrorMessage(null);
				if (e.character != SWT.BS && e.character != SWT.DEL) {
					String val = e.text;
					if (charsText.getText().indexOf(val) != -1) {
						setErrorMessage(Messages.getString("AddAlphabetWizardPage.9")); //$NON-NLS-1$
						e.doit = false;
					}
				}
			}

		});
		
		filterDescriptionLabel = new Label(alphabetCharsGroup, SWT.NONE);
		filterDescriptionLabel.setText(Messages.getString("AddAlphabetWizardPage.10")); //$NON-NLS-1$
		filterDescriptionLabel.setLayoutData(filterDescriptionLabelGridData);
	}
	
}

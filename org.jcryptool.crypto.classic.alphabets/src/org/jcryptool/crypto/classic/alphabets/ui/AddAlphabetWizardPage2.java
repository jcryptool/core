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

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.TextfieldInput;
import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;

/**
 * The WizardPage for the AddAlphabet wizard.
 * 
 * @author simlei
 *
 */
public class AddAlphabetWizardPage2 extends WizardPage {

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
	private ComposeAlphabetComposite compositeAlphaInput;
	private Button btnComposeAlphabetFrom;
	private Button btnEnterEachCharacter;
	private Composite compEnterName;
	private Text text;
	private Composite compEnterCharacters;
	private Composite composite_1;
	private Group compNameData;
	private Group compCharacterData;
	private AlphabetManualInputField textualAlphaInput;
	private AbstractUIInput<AbstractAlphabet> alphabetInput;
	private Composite parentComp;
	private Composite pageComposite;
	private TextfieldInput<String> nameInput;
	
	
	/**
	 * Creates a new instance of AddAlphabetWizardPage.
	 */
	public AddAlphabetWizardPage2() {
		super("page_name", Messages.getString("AddAlphabetWizardPage.1"), null);	 //$NON-NLS-1$ //$NON-NLS-2$
		setPageComplete(false);
		setDescription(Messages.getString("AddAlphabetWizardPage.2")); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		parentComp = parent;
		pageComposite = new Composite(parent, SWT.NULL);	
//		pageComposite.setSize(new Point(350, 175));
		GridLayout gl_pageComposite = new GridLayout();
		gl_pageComposite.numColumns = 2;
		gl_pageComposite.marginBottom = 50;
		pageComposite.setLayout(gl_pageComposite);
		pageComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		{
			compEnterName = new Composite(pageComposite, SWT.NONE);
			compEnterName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			GridLayout gl_compEnterName = new GridLayout(1, false);
			gl_compEnterName.marginHeight = 0;
			gl_compEnterName.marginWidth = 0;
			compEnterName.setLayout(gl_compEnterName);
			{
				compNameData = new Group(compEnterName, SWT.NONE);
				compNameData.setText(Messages.getString("AddAlphabetWizardPage2.0")); //$NON-NLS-1$
				compNameData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				GridLayout gl_composite_2 = new GridLayout(1, false);
				compNameData.setLayout(gl_composite_2);
				{
					text = new Text(compNameData, SWT.BORDER);
					text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					text.setSize(564, 21);
					
					nameInput = new TextfieldInput<String>() {

						@Override
						protected Text getTextfield() {
							return text;
						}

						@Override
						protected InputVerificationResult verifyUserChange() {
							return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
						}

						@Override
						public String readContent() {
							return getTextfield().getText();
						}

						@Override
						protected String getDefaultContent() {
							return ""; //$NON-NLS-1$
						}

						@Override
						public String getName() {
							return "Alphabetname"; //$NON-NLS-1$
						}
					};
				}
			}
		}
		{
			compEnterCharacters = new Composite(pageComposite, SWT.NONE);
			compEnterCharacters.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			GridLayout gl_composite = new GridLayout(1, false);
			gl_composite.marginHeight = 0;
			gl_composite.marginWidth = 0;
			compEnterCharacters.setLayout(gl_composite);
			{
				compCharacterData = new Group(compEnterCharacters, SWT.NONE);
				compCharacterData.setText(Messages.getString("AddAlphabetWizardPage2.lblDefineThe.text")); //$NON-NLS-1$
				compCharacterData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				GridLayout gl_compCharacterData = new GridLayout(1, false);
				compCharacterData.setLayout(gl_compCharacterData);
				{
					composite_1 = new Composite(compCharacterData, SWT.NONE);
					composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					GridLayout gl_composite_1 = new GridLayout(2, false);
					gl_composite_1.marginLeft = 5;
					gl_composite_1.marginWidth = 0;
					gl_composite_1.marginHeight = 0;
					composite_1.setLayout(gl_composite_1);
					{
						btnComposeAlphabetFrom = new Button(composite_1, SWT.RADIO);
						btnComposeAlphabetFrom.setSize(299, 16);
						btnComposeAlphabetFrom.setText(Messages.getString("AddAlphabetWizardPage2.btnComposeAlphabetFrom.text")); //$NON-NLS-1$
						
						btnComposeAlphabetFrom.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								displayBlockInputForm(btnComposeAlphabetFrom.getSelection());
							}
						});
					}
					{
						btnEnterEachCharacter = new Button(composite_1, SWT.RADIO);
						btnEnterEachCharacter.setSize(206, 16);
						btnEnterEachCharacter.setText(Messages.getString("AddAlphabetWizardPage2.btnEnterEachCharacter.text")); //$NON-NLS-1$
						
						btnEnterEachCharacter.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								displayBlockInputForm(btnComposeAlphabetFrom.getSelection());
							}
						});
					}
				}
				{
					textualAlphaInput = new AlphabetManualInputField(compCharacterData, SWT.NONE);
					textualAlphaInput.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
				}
				{
					compositeAlphaInput = new ComposeAlphabetComposite(compCharacterData);
					GridData gd_blockAlphaComposite = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
					compositeAlphaInput.setLayoutData(gd_blockAlphaComposite);
					compositeAlphaInput.layoutRoot = pageComposite; 
					//TODO: das mit der übergebenen layout root für dynamische größenverändernde ui spielereien muss auch irgendwie anders gehen
				}
				
			}
		}
		
		alphabetInput = new AbstractUIInput<AbstractAlphabet>() {

			private boolean compositeInput = true;

			@Override
			protected InputVerificationResult verifyUserChange() {
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}

			@Override
			public AbstractAlphabet readContent() {
				AbstractAlphabet charData = btnComposeAlphabetFrom.getSelection()?
					compositeAlphaInput.getAlphabet():
					textualAlphaInput.getAlphabet();
				
				name = text.getText();
				charData.setName(name);
				return charData;
			}

			@Override
			public void writeContent(AbstractAlphabet content) {
				if(btnComposeAlphabetFrom.getSelection()) {
					//Can't write
				} else {
					textualAlphaInput.writeCharset(content.getCharacterSet());
				}
				text.setText(content.getName());
			}

			@Override
			protected AbstractAlphabet getDefaultContent() {
				AtomAlphabet atomAlphabet = new AtomAlphabet(""); //$NON-NLS-1$
				atomAlphabet.setName(""); //$NON-NLS-1$
				return atomAlphabet;
			}

			@Override
			public String getName() {
				return "Alphabet"; //$NON-NLS-1$
			}
			
			@Override
			protected void saveDefaultRawUserInput() {
				compositeInput = true;
			}
			
			@Override
			protected void saveRawUserInput() {
				compositeInput = btnComposeAlphabetFrom.getSelection();
			}
			
			@Override
			protected void resetUserInput() {
				btnComposeAlphabetFrom.setSelection(compositeInput);
				btnEnterEachCharacter.setSelection(!compositeInput);
				displayBlockInputForm(compositeInput);
				super.resetUserInput();
			}
		};
		
		btnComposeAlphabetFrom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				displayBlockInputForm(btnComposeAlphabetFrom.getSelection());
				alphabetInput.synchronizeWithUserSide();
			}
		});
		btnEnterEachCharacter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				alphabetInput.synchronizeWithUserSide();
			}
		});
		
		textualAlphaInput.getAlphabetInput().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				alphabetInput.synchronizeWithUserSide();
			}
		});
		
		compositeAlphaInput.getAlphabetInput().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				alphabetInput.synchronizeWithUserSide();
			}
		});
		
		nameInput.addObserver(alphabetInput);
		
		alphabetInput.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				setPageComplete(isWizardComplete());
			}
		});

		setPageComplete(isWizardComplete());
		setControl(pageComposite);		
	}
	
	private boolean isWizardComplete() {
		boolean nameComplete =alphabetInput.getContent().getName().length()>0;
		boolean alphabetComplete = alphabetInput.getContent().getCharacterSet().length>0;
		
		return nameComplete && alphabetComplete;
	}
	
	protected void displayBlockInputForm(boolean composite) {
		GridData composLData = (GridData) compositeAlphaInput.getLayoutData();
		GridData textLData = (GridData) textualAlphaInput.getLayoutData();
		
		composLData.exclude = !composite;
		compositeAlphaInput.setVisible(composite);
		textLData.exclude = composite;
		textualAlphaInput.setVisible(!composite);
		
		compCharacterData.layout(new Control[]{compositeAlphaInput, textualAlphaInput});
//		pageComposite.layout();
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
		return alphabetInput.getContent().getName();
	}
	
	/**
	 * Returns the charset of the new alphabet.
	 * 
	 * @return	The charset of the new alphabet
	 */
	public String getAlphabetCharset() {
		return String.valueOf(alphabetInput.getContent().getCharacterSet());
	}
}

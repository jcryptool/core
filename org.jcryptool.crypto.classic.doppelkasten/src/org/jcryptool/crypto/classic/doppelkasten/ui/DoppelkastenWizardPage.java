// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.doppelkasten.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.TextfieldInput;
import org.jcryptool.crypto.classic.doppelkasten.DoppelkastenPlugin;
import org.jcryptool.crypto.classic.doppelkasten.algorithm.DoppelkastenAlgorithmSpecification;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;
import org.jcryptool.crypto.classic.model.ui.wizard.KeyInput;

/**
 * The wizard page for the Autokey-Vigenere wizard.
 *
 * @author SLeischnig
 *
 */
public class DoppelkastenWizardPage extends AbstractClassicCryptoPage {

    private Label key2DescriptionLabel;
    private Text key2Text;
	private TextfieldInput<String> key2Input;

    /**
     * Creates a new instance of CaesarWizardPage.
     */
    public DoppelkastenWizardPage() {
        super(Messages.DoppelkastenWizardPage_doublebox, Messages.DoppelkastenWizardPage_enterkey);
    }



    @Override
	protected void createInputObjects() {
		super.createInputObjects();
		createSecondKeyInput();
	}



	@Override
	protected void createKeyInputObjects() {
		keyInput = new KeyInput<String>() {
			@Override
			public Text getTextfield() {
				return keyText;
			}
			@Override
			protected InputVerificationResult verifyUserChange() {
				return KeyVerificator.verify(getTextfield().getText(), getAlphabetInput().getContent(), getKeyVerificators());
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
				return Messages.DoppelkastenWizardPage_firstkey;
			}
			@SuppressWarnings("rawtypes")
			@Override
			protected void resetExternallyCaused(AbstractUIInput inputWhichCausedThis) {
				String keyNow = getTextfield().getText();
				StringBuilder stringBuilder = new StringBuilder();
				for(int i=0; i<keyNow.length(); i++) {
					if(getAlphabetInput().getContent().contains(keyNow.charAt(i))) {
						stringBuilder.append(keyNow.charAt(i));
					}
				}

				getTextfield().setText(stringBuilder.toString());
				reread(inputWhichCausedThis);
			}
			@Override
			public AbstractAlphabet getAlphabet() {
				return getAlphabetInput().getContent();
			}
		};

		//changes in the currentAlphabet input must be forwarded to the key input for revalidation
		getAlphabetInput().addObserver(keyInput);
	}


	
	private void createSecondKeyInput() {
		key2Input = new KeyInput<String>() {
			@Override
			public Text getTextfield() {
				return key2Text;
			}
			@Override
			protected InputVerificationResult verifyUserChange() {
				return KeyVerificator.verify(getTextfield().getText(), getAlphabetInput().getContent(), getKeyVerificators());
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
				return Messages.DoppelkastenWizardPage_secondkey;
			}
			@SuppressWarnings("rawtypes")
			@Override
			protected void resetExternallyCaused(AbstractUIInput inputWhichCausedThis) {
				String keyNow = getTextfield().getText();
				StringBuilder stringBuilder = new StringBuilder();
				for(int i=0; i<keyNow.length(); i++) {
					if(getAlphabetInput().getContent().contains(keyNow.charAt(i))) {
						stringBuilder.append(keyNow.charAt(i));
					}
				}

				setTextfieldTextExternal(stringBuilder.toString());
				reread(inputWhichCausedThis);
			}
			@Override
			public AbstractAlphabet getAlphabet() {
				return getAlphabetInput().getContent();
			}

		};

		//changes in the currentAlphabet input must be forwarded to the key input for revalidation
		getAlphabetInput().addObserver(key2Input);
	}


	@Override
	protected void createInputVerificationHandler(Shell shell) {
		super.createInputVerificationHandler(shell);
		verificationDisplayHandler.addAsObserverForInput(key2Input);
		verificationDisplayHandler.addInputWidgetMapping(key2Input, key2Text);
	}




	@Override
	protected void addPageObserver() {
		super.addPageObserver();
		key2Input.addObserver(pageObserver);
	}



	@Override
    protected void setHelpAvailable() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), DoppelkastenPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }

    @Override
    protected boolean mayFinish() {
        return super.mayFinish() && (key2Input != null && key2Input.getContent().length() > 0);
    }

    

    @Override
	public String getKey() {
	    return DoppelkastenAlgorithmSpecification.glueKeys(keyInput.getContent(), key2Input.getContent());
	}



	@Override
    /**
     * This method initializes the key input group.
     * Subclasses should override this, if more controls are needed.
     */
    protected void createKeyGroup(Composite parent) {
        keyGroup = new Group(parent, SWT.NONE);

			GridLayout keyGroupGridLayout = new GridLayout();
				keyGroupGridLayout.numColumns = 2;

			GridData keyGroupGridData = new GridData();
				keyGroupGridData.horizontalAlignment = GridData.FILL;
				keyGroupGridData.grabExcessHorizontalSpace = true;
				keyGroupGridData.grabExcessVerticalSpace = false;
				keyGroupGridData.verticalAlignment = SWT.TOP;

			keyGroup.setLayoutData(keyGroupGridData);
			keyGroup.setLayout(keyGroupGridLayout);
			keyGroup.setText(Messages.DoppelkastenWizardPage_keys);

		keyDescriptionLabel = new Label(keyGroup, SWT.NONE);

			GridData keyDescriptionLabelGridData = new GridData();
				keyDescriptionLabelGridData.horizontalAlignment = GridData.FILL;
				keyDescriptionLabelGridData.grabExcessVerticalSpace = true;
//				keyDescriptionLabelGridData.exclude = true;

			keyDescriptionLabel.setText(Messages.DoppelkastenWizardPage_enterfirst);
			keyDescriptionLabel.setLayoutData(keyDescriptionLabelGridData);
//			keyDescriptionLabel.setVisible(false);

		keyText = new Text(keyGroup, SWT.BORDER);

			GridData keyTextGridData = new GridData();
				keyTextGridData.grabExcessHorizontalSpace = true;
				keyTextGridData.horizontalAlignment = GridData.FILL;
				keyTextGridData.verticalAlignment = GridData.CENTER;
				keyTextGridData.grabExcessVerticalSpace = true;

			keyText.setLayoutData(keyTextGridData);
	        keyText.setToolTipText("");

	    key2DescriptionLabel = new Label(keyGroup, SWT.NONE);

			GridData key2DescriptionLabelGridData = new GridData();
				key2DescriptionLabelGridData.horizontalAlignment = GridData.FILL;
				key2DescriptionLabelGridData.grabExcessVerticalSpace = true;
//				key2DescriptionLabelGridData.exclude = true;

			key2DescriptionLabel.setText(Messages.DoppelkastenWizardPage_entersecond);
			key2DescriptionLabel.setLayoutData(key2DescriptionLabelGridData);
//			key2DescriptionLabel.setVisible(false);

		key2Text = new Text(keyGroup, SWT.BORDER);

			GridData key2TextGridData = new GridData();
				key2TextGridData.grabExcessHorizontalSpace = true;
				key2TextGridData.horizontalAlignment = GridData.FILL;
				key2TextGridData.verticalAlignment = GridData.CENTER;
				key2TextGridData.grabExcessVerticalSpace = true;

			key2Text.setLayoutData(key2TextGridData);
	        key2Text.setToolTipText("");

    }
	
//	doublebox -E -t "TEST TEXT" -k AKEY -k2 KEYTWO
	@Override
    protected String generateCommandLineString() {
    	String encDec = operationInput.getContent()?"-E":"-D";
    	String key = "-k " + quoteCmdlineArgIfNecessary(DoppelkastenAlgorithmSpecification.unglueKeys(getKey())[0]);
    	String key2 = "-k2 " + quoteCmdlineArgIfNecessary(DoppelkastenAlgorithmSpecification.unglueKeys(getKey())[1]);
    	
    	String result = "doublebox " + encDec + " -ed " + key + " " + key2;

//    	result += " " + generateAlphabetPartForCommandLine();
    	
    	if(!isNonAlphaFilter()) result += " --noFilter";
    	return result;
    }

}
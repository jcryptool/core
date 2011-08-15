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
package org.jcryptool.crypto.classic.xor.ui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.util.input.ButtonInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;
import org.jcryptool.crypto.classic.xor.XorPlugin;

/**
 * The XorWizardPage for the Xor cipher.
 *
 * @author t-kern
 *
 */
public class XorWizardPage extends AbstractClassicCryptoPage {

	private Button fileRadioButton;

	private Button keyTextRadioButton;
	private FilechooserComposite fileChooser;
	
	private ButtonInput keyMethodInput;

	public XorWizardPage() {
		super(Messages.XOR, Messages.XorWizardPage_orders);
	}
	
	
	
	@Override
	protected void createInputObjects() {
		super.createInputObjects();
		createInputMethodObject();
	}

	private void createInputMethodObject() {
		keyMethodInput = new ButtonInput() {
			@Override
			protected InputVerificationResult verifyUserChange() {
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}
			@Override
			public String getName() {
				return Messages.XorWizardPage_inputname_keymethod;
			}
			@Override
			protected Boolean getDefaultContent() {
				return true;
			}
			@Override
			public Button getButton() {
				return keyTextRadioButton;
			}
			@Override
			public void writeContent(Boolean content) {
				super.writeContent(content);
				fileRadioButton.setSelection(!content);
			}
		};
		setInputFromText(keyMethodInput.getContent());
		keyMethodInput.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				if(arg==null) {
					setInputFromText(keyMethodInput.getContent());
				}
			}
		});
	}
	
	@Override
	protected void createInputVerificationHandler(Shell shell) {
		super.createInputVerificationHandler(shell);
		verificationDisplayHandler.addAsObserverForInput(keyMethodInput);
		verificationDisplayHandler.addInputWidgetMapping(keyMethodInput, keyTextRadioButton);
		
		verificationDisplayHandler.addAsObserverForInput(fileChooser.getFileInput());
		verificationDisplayHandler.addInputWidgetMapping(fileChooser.getFileInput(), fileChooser);
	}

	@Override
	protected void addPageObserver() {
		super.addPageObserver();
		keyMethodInput.addObserver(pageObserver);
		fileChooser.getFileInput().addObserver(pageObserver);
	}

	/**
	 * Enables/disables various ui elements.
	 *
	 * @param inputFromText	The state defining which elements are supposed to be enabled/disabled
	 */
	private void setInputFromText(boolean inputFromText) {
		updateKeyinputForEnabledState(inputFromText);
		if(fileChooser != null) fileChooser.updateForEnabledState(! inputFromText);
	}

	@Override
	/**
	 * Returns <code>true</code>, if the page is complete and the wizard may finish.
	 *
	 * @return	<code>true</code>, if the page is complete and the wizard may finish
	 */
	protected boolean mayFinish() {
		if (encryptButton.getSelection() || decryptButton.getSelection()) {
			if (keyMethodInput.getContent() && keyInput.getContent().length() > 0) {
				return true;
			} else if (keyMethodInput.getContent() && fileChooser.hasValidFile()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the (absolute) name of the selected file
	 *
	 * @return	The (absolute) name of the selected file
	 */
	public String getPathToKeyFile() {
		return fileChooser.getFileInput().getContent();
	}
	
	/**
	 * @return the method of key input - true:textual input; false:vernam file operation
	 */
	public boolean getKeyMethod() {
		return keyMethodInput.getContent();
	}

	@Override
	protected void createKeyGroup(Composite parent) {
		GridData selectedFileLabelGridData = new GridData();
		selectedFileLabelGridData.horizontalSpan = 3;
		selectedFileLabelGridData.horizontalAlignment = GridData.FILL;

		GridData keyTextRadioButtonGridData = new GridData();
		keyTextRadioButtonGridData.grabExcessVerticalSpace = true;
		GridData fileRadioButtonGridData = new GridData();
		fileRadioButtonGridData.horizontalSpan = 1;
		fileRadioButtonGridData.grabExcessVerticalSpace = true;
		GridData openFileButtonGridData = new GridData();
		openFileButtonGridData.horizontalAlignment = GridData.END;
		openFileButtonGridData.grabExcessHorizontalSpace = true;
		openFileButtonGridData.verticalAlignment = GridData.CENTER;
		GridData keyGroupGridData = new GridData();
		keyGroupGridData.horizontalAlignment = GridData.FILL;
		keyGroupGridData.grabExcessHorizontalSpace = true;
		keyGroupGridData.grabExcessVerticalSpace = false;
		keyGroupGridData.verticalAlignment = SWT.TOP;
		GridData keyTextGridData = new GridData();
		keyTextGridData.horizontalAlignment = GridData.FILL;
		keyTextGridData.verticalAlignment = GridData.CENTER;
		keyTextGridData.horizontalSpan = 2;
		keyTextGridData.grabExcessHorizontalSpace = true;
		GridLayout keyGroupGridLayout = new GridLayout();
		keyGroupGridLayout.numColumns = 3;
		keyGroup = new Group(parent, SWT.NONE);
		keyGroup.setText(Messages.XorWizardPage_keys);
		keyGroup.setLayoutData(keyGroupGridData);
		keyGroup.setLayout(keyGroupGridLayout);
		keyTextRadioButton = new Button(keyGroup, SWT.RADIO);
		keyTextRadioButton.setText(Messages.XorWizardPage_manualkeylabel);
		keyTextRadioButton.setLayoutData(keyTextRadioButtonGridData);
		keyText = new Text(keyGroup, SWT.BORDER);
		keyText.setLayoutData(keyTextGridData);
		keyText.setToolTipText("");

		fileRadioButton = new Button(keyGroup, SWT.RADIO);
		fileRadioButton.setText(Messages.XorWizardPage_selectfileforvernam);
		fileRadioButton.setLayoutData(fileRadioButtonGridData);
		fileChooser = new FilechooserComposite(keyGroup, SWT.NONE);
		GridData fileChooserGData = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1);
		fileChooser.setLayoutData(fileChooserGData);
	}
	
	public void updateKeyinputForEnabledState(boolean enabled) {
		Control[] allCtrls = new Control[]{
			keyText,
		};
		for(Control c: allCtrls) {
			c.setEnabled(enabled);
		}
	}

    @Override
    protected void setHelpAvailable() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
                XorPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }
}
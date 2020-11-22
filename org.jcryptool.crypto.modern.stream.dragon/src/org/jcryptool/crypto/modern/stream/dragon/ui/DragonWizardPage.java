//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.modern.stream.dragon.ui;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.modern.stream.dragon.DragonPlugin;

public class DragonWizardPage extends WizardPage implements Listener {

	public enum DisplayOption { OUTPUT_ONLY, OUTPUT_AND_KEYSTREAM, KEYSTREAM_ONLY }

	/** Used to override a listener, since the change is performed on purpose! */
	private boolean CLEARING_FLAG = false;

	private Group keyIVSizeGroup;
	private Button _128BitButton;
	private Button _256BitButton;

	private Group keyValueGroup;
	private Button hexadecimalKeyInputButton;
	private Button binaryKeyInputButton;
	private Text keyText;
	private Label keyDigitCountLabel;

	private Group ivValueGroup;
	private Button hexadecimalIvInputButton;
	private Button binaryIvInputButton;
	private Text ivText;
	private Label ivDigitCountLabel;

	private Group displayOptionsGroup;
	private Button displayOutputOnlyButton;
	private Button displayOutputAndKeystreamButton;
	private Button displayKeystreamOnlyButton;
	private Text keystreamLengthText;

	private boolean is128Bit = true;
	private String keyValue = ""; //$NON-NLS-1$
	private boolean keyFormatIsHexadecimal = true;
	private String ivValue = ""; //$NON-NLS-1$
	private boolean ivFormatIsHexadecimal = true;

	private DisplayOption displayOption = DisplayOption.OUTPUT_ONLY;
	private String keystreamLengthValue = ""; //$NON-NLS-1$

	/** Contains all hexadecimal digits for convenience */
	private ArrayList<String> hexValues = new ArrayList<String>(16);

	/** Contains all decimal digits for convenience */
	private ArrayList<String> decValues = new ArrayList<String>(10);
    private static final int KEY_MAX_VALUE = 1024;

	/**
	 * Creates a new instance of DragonWizardPage.
	 */
	public DragonWizardPage() {
		super(".", "Dragon", null); //$NON-NLS-1$ //$NON-NLS-2$
		setTitle(Messages.DragonWizardPage_0);
		setMessage(Messages.DragonWizardPage_1);
		setupHexadecimalValues();
		setupDecimalValues();
	}

	/**
	 * Sets up an ArrayList with hexadecimal values for convenience.
	 */
	private void setupHexadecimalValues() {
		hexValues.add(0, "0"); hexValues.add(1, "1"); hexValues.add(2, "2"); hexValues.add(3, "3"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		hexValues.add(4, "4"); hexValues.add(5, "5"); hexValues.add(6, "6"); hexValues.add(7, "7"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		hexValues.add(8, "8"); hexValues.add(9, "9"); hexValues.add(10, "A"); hexValues.add(11, "B"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		hexValues.add(12, "C"); hexValues.add(13, "D"); hexValues.add(14, "E"); hexValues.add(15, "F"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	/**
	 * Sets up an ArrayList with decimal values for convenience.
	 */
	private void setupDecimalValues() {
		decValues.add(0, "0"); decValues.add(1, "1"); decValues.add(2, "2");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		decValues.add(3, "3"); decValues.add(4, "4"); decValues.add(5, "5");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		decValues.add(6, "6"); decValues.add(7, "7"); decValues.add(8, "8");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		decValues.add(9, "9"); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite pageComposite = new Composite(parent, SWT.NULL);

		createKeyIvSizeGroup(pageComposite);
		createKeyValueGroup(pageComposite);
		createIvValueGroup(pageComposite);
		createDisplayOptionsGroup(pageComposite);

		_128BitButton.setSelection(true);
		hexadecimalKeyInputButton.setSelection(true);
		hexadecimalIvInputButton.setSelection(true);
		displayOutputOnlyButton.setSelection(true);

		pageComposite.setLayout(new GridLayout());

		setControl(pageComposite);
		setPageComplete(mayFinish());

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), DragonPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
	}

	/**
	 * This method initializes keyIvSizeGroup.
	 *
	 * @param parent
	 */
	private void createKeyIvSizeGroup(Composite parent) {
		GridData _128BitButtonGridData = new GridData();
		_128BitButtonGridData.horizontalAlignment = GridData.FILL;
		_128BitButtonGridData.grabExcessHorizontalSpace = true;
		_128BitButtonGridData.grabExcessVerticalSpace = true;
		_128BitButtonGridData.verticalAlignment = GridData.CENTER;

		GridData _256BitButtonGridData = new GridData();
		_256BitButtonGridData.horizontalAlignment = GridData.FILL;
		_256BitButtonGridData.grabExcessHorizontalSpace = true;
		_256BitButtonGridData.grabExcessVerticalSpace = true;
		_256BitButtonGridData.verticalAlignment = GridData.CENTER;

		GridLayout keyIvSizeGroupGridLayout = new GridLayout();
		keyIvSizeGroupGridLayout.numColumns = 2;

		GridData keyIvSizeGroupGridData = new GridData();
		keyIvSizeGroupGridData.horizontalAlignment = GridData.FILL;
		keyIvSizeGroupGridData.grabExcessHorizontalSpace = true;
		keyIvSizeGroupGridData.grabExcessVerticalSpace = true;
		keyIvSizeGroupGridData.verticalAlignment = GridData.FILL;

		keyIVSizeGroup = new Group(parent, SWT.NONE);
		keyIVSizeGroup.setLayoutData(keyIvSizeGroupGridData);
		keyIVSizeGroup.setLayout(keyIvSizeGroupGridLayout);
		keyIVSizeGroup.setText(Messages.DragonWizardPage_3);

		_128BitButton = new Button(keyIVSizeGroup, SWT.RADIO);
		_128BitButton.setText("128 Bit"); //$NON-NLS-1$
		_128BitButton.setLayoutData(_128BitButtonGridData);
		_128BitButton.addListener(SWT.Selection, this);

		_256BitButton = new Button(keyIVSizeGroup, SWT.RADIO);
		_256BitButton.setText("256 Bit"); //$NON-NLS-1$
		_256BitButton.setLayoutData(_256BitButtonGridData);
		_256BitButton.addListener(SWT.Selection, this);
	}

	/**
	 * This method initializes keyValueGroup.
	 *
	 * @param parent
	 */
	private void createKeyValueGroup(Composite parent) {
		GridData hexadecimalKeyInputButtonGridData = new GridData();
		hexadecimalKeyInputButtonGridData.horizontalAlignment = GridData.FILL;
		hexadecimalKeyInputButtonGridData.grabExcessHorizontalSpace = true;
		hexadecimalKeyInputButtonGridData.grabExcessVerticalSpace = true;
		hexadecimalKeyInputButtonGridData.verticalAlignment = GridData.CENTER;

		GridData binaryKeyInputButtonGridData = new GridData();
		binaryKeyInputButtonGridData.horizontalAlignment = GridData.FILL;
		binaryKeyInputButtonGridData.grabExcessHorizontalSpace = true;
		binaryKeyInputButtonGridData.grabExcessVerticalSpace = true;
		binaryKeyInputButtonGridData.verticalAlignment = GridData.CENTER;

		GridData keyTextGridData = new GridData();
		keyTextGridData.horizontalSpan = 2;
		keyTextGridData.verticalAlignment = GridData.CENTER;
		keyTextGridData.grabExcessHorizontalSpace = true;
		keyTextGridData.horizontalAlignment = GridData.FILL;

		GridData keyDigitCountLabelGridData = new GridData();
		keyDigitCountLabelGridData.horizontalSpan = 2;
		keyDigitCountLabelGridData.horizontalAlignment = GridData.BEGINNING;
		keyDigitCountLabelGridData.grabExcessHorizontalSpace = true;
		keyDigitCountLabelGridData.grabExcessVerticalSpace = false;
		keyDigitCountLabelGridData.verticalAlignment = GridData.CENTER;

		GridLayout keyValueGroupGridLayout = new GridLayout();
		keyValueGroupGridLayout.numColumns = 2;

		GridData keyValueGroupGridData = new GridData();
		keyValueGroupGridData.grabExcessHorizontalSpace = true;
		keyValueGroupGridData.horizontalAlignment = GridData.FILL;
		keyValueGroupGridData.verticalAlignment = GridData.FILL;
		keyValueGroupGridData.grabExcessVerticalSpace = true;

		keyValueGroup = new Group(parent, SWT.NONE);
		keyValueGroup.setText(Messages.DragonWizardPage_4);
		keyValueGroup.setLayout(keyValueGroupGridLayout);
		keyValueGroup.setLayoutData(keyValueGroupGridData);

		hexadecimalKeyInputButton = new Button(keyValueGroup, SWT.RADIO);
		hexadecimalKeyInputButton.setText(Messages.DragonWizardPage_5);
		hexadecimalKeyInputButton.setLayoutData(hexadecimalKeyInputButtonGridData);
		hexadecimalKeyInputButton.addListener(SWT.Selection, this);

		binaryKeyInputButton = new Button(keyValueGroup, SWT.RADIO);
		binaryKeyInputButton.setText(Messages.DragonWizardPage_6);
		binaryKeyInputButton.setLayoutData(binaryKeyInputButtonGridData);
		binaryKeyInputButton.addListener(SWT.Selection, this);

		keyText = new Text(keyValueGroup, SWT.BORDER | SWT.SINGLE);
		keyText.setLayoutData(keyTextGridData);
        StringBuilder temp = new StringBuilder();
		int max = 0;
		if(is128Bit){
			if(keyFormatIsHexadecimal)
				max = 32;
			else
				max = 128;
		}
		else{
			if(keyFormatIsHexadecimal)
				max = 64;
			else
				max = 256;
		}
		for(int i=0; i<max; i++)
			temp.append("0"); //$NON-NLS-1$
		keyText.setText(temp.toString()); //$NON-NLS-1$
		keyText.addListener(SWT.Modify, this);
		keyText.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				if (CLEARING_FLAG)
					return;

				setErrorMessage(null);
				if (e.character != SWT.BS && e.character != SWT.DEL) {
					e.text = e.text.toUpperCase();

					if (keyFormatIsHexadecimal) {
						if ( !hexValues.contains(e.text) ) {
							setErrorMessage(Messages.DragonWizardPage_7);
							e.doit = false;
						} else if ( (keyText.getText().length() + 1) > (is128Bit ? 32 : 64) ) {
							setErrorMessage(Messages.DragonWizardPage_8);
							e.doit = false;
						}
					} else {
						if ((!e.text.equals("0")) && (!e.text.equals("1"))) { //$NON-NLS-1$ //$NON-NLS-2$
							setErrorMessage(Messages.DragonWizardPage_9);
							e.doit = false;
						} else if ( (keyText.getText().length() + 1) > (is128Bit ? 128 : 256) ) {
							setErrorMessage(Messages.DragonWizardPage_8);
							e.doit = false;
						}
					}
				}
			}

		});

		keyDigitCountLabel = new Label(keyValueGroup, SWT.NONE);
		keyDigitCountLabel.setLayoutData(keyDigitCountLabelGridData);
		keyDigitCountLabel.setText(NLS.bind(Messages.DragonWizardPage_11, "32   "));  //$NON-NLS-2$ //$NON-NLS-1$
	}

	/**
	 * This method initializes ivValueGroup.
	 *
	 * @param parent
	 */
	private void createIvValueGroup(Composite parent) {
		GridData hexadecimalIvInputButtonGridData = new GridData();
		hexadecimalIvInputButtonGridData.horizontalAlignment = GridData.FILL;
		hexadecimalIvInputButtonGridData.grabExcessHorizontalSpace = true;
		hexadecimalIvInputButtonGridData.grabExcessVerticalSpace = true;
		hexadecimalIvInputButtonGridData.verticalAlignment = GridData.CENTER;

		GridData binaryIvInputButtonGridData = new GridData();
		binaryIvInputButtonGridData.horizontalAlignment = GridData.FILL;
		binaryIvInputButtonGridData.grabExcessHorizontalSpace = true;
		binaryIvInputButtonGridData.grabExcessVerticalSpace = true;
		binaryIvInputButtonGridData.verticalAlignment = GridData.CENTER;

		GridData ivTextGridData = new GridData();
		ivTextGridData.horizontalSpan = 2;
		ivTextGridData.verticalAlignment = GridData.CENTER;
		ivTextGridData.grabExcessHorizontalSpace = true;
		ivTextGridData.horizontalAlignment = GridData.FILL;

		GridData ivDigitCountLabelGridData = new GridData();
		ivDigitCountLabelGridData.horizontalSpan = 2;
		ivDigitCountLabelGridData.horizontalAlignment = GridData.BEGINNING;
		ivDigitCountLabelGridData.grabExcessHorizontalSpace = true;
		ivDigitCountLabelGridData.grabExcessVerticalSpace = false;
		ivDigitCountLabelGridData.verticalAlignment = GridData.CENTER;

		GridLayout ivValueGroupGridLayout = new GridLayout();
		ivValueGroupGridLayout.numColumns = 2;

		GridData ivValueGroupGridData = new GridData();
		ivValueGroupGridData.grabExcessHorizontalSpace = true;
		ivValueGroupGridData.horizontalAlignment = GridData.FILL;
		ivValueGroupGridData.verticalAlignment = GridData.FILL;
		ivValueGroupGridData.grabExcessVerticalSpace = true;

		ivValueGroup = new Group(parent, SWT.NONE);
		ivValueGroup.setText(Messages.DragonWizardPage_12);
		ivValueGroup.setLayout(ivValueGroupGridLayout);
		ivValueGroup.setLayoutData(ivValueGroupGridData);

		hexadecimalIvInputButton = new Button(ivValueGroup, SWT.RADIO);
		hexadecimalIvInputButton.setText(Messages.DragonWizardPage_5);
		hexadecimalIvInputButton.setLayoutData(hexadecimalIvInputButtonGridData);
		hexadecimalIvInputButton.addListener(SWT.Selection, this);

		binaryIvInputButton = new Button(ivValueGroup, SWT.RADIO);
		binaryIvInputButton.setText(Messages.DragonWizardPage_6);
		binaryIvInputButton.setLayoutData(binaryIvInputButtonGridData);
		binaryIvInputButton.addListener(SWT.Selection, this);

		ivText = new Text(ivValueGroup, SWT.BORDER | SWT.SINGLE);
		ivText.setLayoutData(ivTextGridData);
        StringBuilder temp = new StringBuilder();
		int max = 0;
		if(is128Bit){
			if(ivFormatIsHexadecimal)
				max = 32;
			else
				max = 128;
		}
		else{
			if(ivFormatIsHexadecimal)
				max = 64;
			else
				max = 256;
		}
		for(int i=0; i<max; i++)
			temp.append("0"); //$NON-NLS-1$
		ivText.setText(temp.toString()); //$NON-NLS-1$
		ivText.addListener(SWT.Modify, this);
		ivText.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				if (CLEARING_FLAG) {
					return;
				}
				setErrorMessage(null);
				if (e.character != SWT.BS && e.character != SWT.DEL) {
					e.text = e.text.toUpperCase();

					if (ivFormatIsHexadecimal) {
						if ( !hexValues.contains(e.text) ) {
							setErrorMessage(Messages.DragonWizardPage_7);
							e.doit = false;
						} else if ( (ivText.getText().length() + 1) > (is128Bit ? 32 : 64) ) {
							setErrorMessage(Messages.DragonWizardPage_8);
							e.doit = false;
						}
					} else {
						if ((!e.text.equals("0")) && (!e.text.equals("1"))) { //$NON-NLS-1$ //$NON-NLS-2$
							setErrorMessage(Messages.DragonWizardPage_9);
							e.doit = false;
						} else if ( (ivText.getText().length() + 1) > (is128Bit ? 128 : 256) ) {
							setErrorMessage(Messages.DragonWizardPage_8);
							e.doit = false;
						}
					}
				}
			}

		});

		ivDigitCountLabel = new Label(ivValueGroup, SWT.None);
		ivDigitCountLabel.setLayoutData(ivDigitCountLabelGridData);
		ivDigitCountLabel.setText(NLS.bind(Messages.DragonWizardPage_11, "32   "));  //$NON-NLS-2$ //$NON-NLS-1$
	}

	private void createDisplayOptionsGroup(Composite parent) {
		GridData displayOutputOnlyButtonGridData = new GridData();
		displayOutputOnlyButtonGridData.horizontalAlignment = GridData.FILL;
		displayOutputOnlyButtonGridData.grabExcessHorizontalSpace = false;
		displayOutputOnlyButtonGridData.grabExcessVerticalSpace = true;
		displayOutputOnlyButtonGridData.verticalAlignment = GridData.CENTER;

		GridData displayOutputAndKeystreamButtonGridData = new GridData();
		displayOutputAndKeystreamButtonGridData.horizontalAlignment = GridData.FILL;
		displayOutputAndKeystreamButtonGridData.grabExcessHorizontalSpace = false;
		displayOutputAndKeystreamButtonGridData.grabExcessVerticalSpace = true;
		displayOutputAndKeystreamButtonGridData.verticalAlignment = GridData.CENTER;

		GridData displayKeystreamOnlyButtonGridData = new GridData();
		displayKeystreamOnlyButtonGridData.horizontalAlignment = GridData.FILL;
		displayKeystreamOnlyButtonGridData.grabExcessHorizontalSpace = false;
		displayKeystreamOnlyButtonGridData.grabExcessVerticalSpace = true;
		displayKeystreamOnlyButtonGridData.verticalAlignment = GridData.CENTER;

		GridData keystreamLengthTextGridData = new GridData();
		keystreamLengthTextGridData.verticalAlignment = GridData.CENTER;
		keystreamLengthTextGridData.grabExcessHorizontalSpace = true;
		keystreamLengthTextGridData.horizontalAlignment = GridData.FILL;

		GridLayout displayOptionsGroupGridLayout = new GridLayout();
		displayOptionsGroupGridLayout.numColumns = 1;

		GridData displayOptionsGroupGridData = new GridData();
		displayOptionsGroupGridData.horizontalAlignment = GridData.FILL;
		displayOptionsGroupGridData.grabExcessHorizontalSpace = true;
		displayOptionsGroupGridData.grabExcessVerticalSpace = true;
		displayOptionsGroupGridData.verticalAlignment = GridData.FILL;

		displayOptionsGroup = new Group(parent, SWT.NONE);
		displayOptionsGroup.setLayoutData(displayOptionsGroupGridData);
		displayOptionsGroup.setLayout(displayOptionsGroupGridLayout);
		displayOptionsGroup.setText(Messages.DragonWizardPage_20);

		displayOutputOnlyButton = new Button(displayOptionsGroup, SWT.RADIO);
		displayOutputOnlyButton.setText(Messages.DragonWizardPage_21);
		displayOutputOnlyButton.setLayoutData(displayOutputOnlyButtonGridData);
		displayOutputOnlyButton.addListener(SWT.Selection, this);

		displayOutputAndKeystreamButton = new Button(displayOptionsGroup, SWT.RADIO);
		displayOutputAndKeystreamButton.setText(Messages.DragonWizardPage_22);
		displayOutputAndKeystreamButton.setLayoutData(displayOutputAndKeystreamButtonGridData);
		displayOutputAndKeystreamButton.addListener(SWT.Selection, this);

		displayKeystreamOnlyButton = new Button(displayOptionsGroup, SWT.RADIO);
		displayKeystreamOnlyButton.setText(Messages.DragonWizardPage_23);
		displayKeystreamOnlyButton.setLayoutData(displayKeystreamOnlyButtonGridData);
		displayKeystreamOnlyButton.addListener(SWT.Selection, this);

        keystreamLengthTextGridData = new GridData();
        keystreamLengthTextGridData.verticalAlignment = GridData.CENTER;
        keystreamLengthTextGridData.horizontalIndent = 20;
        keystreamLengthTextGridData.widthHint = 50;

		keystreamLengthText = new Text(displayOptionsGroup, SWT.BORDER | SWT.SINGLE);
		keystreamLengthText.setLayoutData(keystreamLengthTextGridData);
		keystreamLengthText.setText("100"); //$NON-NLS-1$
		keystreamLengthText.addListener(SWT.Modify, this);
		keystreamLengthText.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				if (CLEARING_FLAG) {
					return;
				}
				setErrorMessage(null);
				if (e.character != SWT.BS && e.character != SWT.DEL) {
					if (!decValues.contains(e.text) ) {
						setErrorMessage(Messages.DragonWizardPage_24);
						e.doit = false;
					} else if (keystreamLengthText.getText().length() == 0 && e.text.equals("0")) { //$NON-NLS-1$
                        setErrorMessage(Messages.DragonWizardPage_13);
                        e.doit = false;
                    } else if ((Long.parseLong(keystreamLengthText.getText() + e.text)) > KEY_MAX_VALUE) {
                        setErrorMessage(NLS.bind(Messages.DragonWizardPage_25, KEY_MAX_VALUE));
                        e.doit = false;
                    }
				}
			}

		});
		keystreamLengthText.setEnabled(false);
	}

	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget == _128BitButton) {
			if (!is128Bit) {
				is128Bit = true;
				clearKey();
				clearIV();
			}
		} else if (event.widget == _256BitButton) {
			if (is128Bit) {
				is128Bit = false;
				clearKey();
				clearIV();
			}
		} else if (event.widget == hexadecimalKeyInputButton) {
			if (keyFormatIsHexadecimal == false) {
				keyFormatIsHexadecimal = true;
				clearKey();
			}
		} else if (event.widget == binaryKeyInputButton) {
			if (keyFormatIsHexadecimal) {
				keyFormatIsHexadecimal = false;
				clearKey();
			}
		} else if (event.widget == hexadecimalIvInputButton) {
			if (ivFormatIsHexadecimal == false) {
				ivFormatIsHexadecimal = true;
				clearIV();
			}
		} else if (event.widget == binaryIvInputButton) {
			if (ivFormatIsHexadecimal) {
				ivFormatIsHexadecimal = false;
				clearIV();
			}
		} else if (event.widget == keyText) {
			keyValue = keyText.getText();

			if (keyFormatIsHexadecimal)
				keyDigitCountLabel.setText(Messages.DragonWizardPage_26 + keyValue.length() + " / " + (is128Bit ? "32" : "64") + "\n" +  //$NON-NLS-1$
					Messages.DragonWizardPage_27 + (is128Bit ? "32" : "64"));  //$NON-NLS-1$ //$NON-NLS-2$
			else
				keyDigitCountLabel.setText(Messages.DragonWizardPage_26 + keyValue.length() + " / " + (is128Bit ? "128" : "256") + "\n" +   //$NON-NLS-1$
			        Messages.DragonWizardPage_27 + (is128Bit ? "128" : "256"));   //$NON-NLS-1$ //$NON-NLS-2$
		} else if (event.widget == ivText) {
			ivValue = ivText.getText();

			if (ivFormatIsHexadecimal)
				ivDigitCountLabel.setText(Messages.DragonWizardPage_26 + ivValue.length() + " / " + (is128Bit ? "32" : "64") + "\n" +   //$NON-NLS-1$
			        Messages.DragonWizardPage_27 + (is128Bit ? "32" : "64"));   //$NON-NLS-1$ //$NON-NLS-2$
			else
				ivDigitCountLabel.setText(Messages.DragonWizardPage_26 + ivValue.length() + " / " + (is128Bit ? "128" : "256") + "\n" +   //$NON-NLS-1$
			        Messages.DragonWizardPage_27 + (is128Bit ? "128" : "256"));   //$NON-NLS-1$ //$NON-NLS-2$
		} else if (event.widget == displayOutputOnlyButton) {
			displayOption = DisplayOption.OUTPUT_ONLY;
			clearKeystreamLength();
			keystreamLengthText.setEnabled(false);
		} else if (event.widget == displayOutputAndKeystreamButton) {
			displayOption = DisplayOption.OUTPUT_AND_KEYSTREAM;
			clearKeystreamLength();
			keystreamLengthText.setEnabled(false);
            setErrorMessage(null);
		} else if (event.widget == displayKeystreamOnlyButton) {
			displayOption = DisplayOption.KEYSTREAM_ONLY;
			keystreamLengthText.setEnabled(true);
            setErrorMessage(null);
		} else if (event.widget == keystreamLengthText) {
			keystreamLengthValue = keystreamLengthText.getText();
		}
		setPageComplete(mayFinish());
	}

	private void clearKey() {
		CLEARING_FLAG = true;
		StringBuilder temp = new StringBuilder();
		int max = 0;
		if(is128Bit){
			if(keyFormatIsHexadecimal)
				max = 32;
			else
				max = 128;
		}
		else{
			if(keyFormatIsHexadecimal)
				max = 64;
			else
				max = 256;
		}
		for(int i=0; i<max; i++)
			temp.append("0"); //$NON-NLS-1$
		keyText.setText(temp.toString());
		CLEARING_FLAG = false;
		keyValue = ""; //$NON-NLS-1$
	}

	private void clearIV() {
		CLEARING_FLAG = true;
		StringBuilder temp = new StringBuilder();
		int max = 0;
		if(is128Bit){
			if(ivFormatIsHexadecimal)
				max = 32;
			else
				max = 128;
		}
		else{
			if(ivFormatIsHexadecimal)
				max = 64;
			else
				max = 256;
		}
		for(int i=0; i<max; i++)
			temp.append("0"); //$NON-NLS-1$
		ivText.setText(temp.toString());
		CLEARING_FLAG = false;
		ivValue = ""; //$NON-NLS-1$
	}

	private void clearKeystreamLength() {
		CLEARING_FLAG = true;
		keystreamLengthText.setText("100"); //$NON-NLS-1$
		CLEARING_FLAG = false;
		keystreamLengthValue = "100"; //$NON-NLS-1$
	}

	public boolean getIs128Bit() {
		return is128Bit;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public boolean getKeyFormatIsHexadecimal() {
		return keyFormatIsHexadecimal;
	}

	public String getIvValue() {
		return ivValue;
	}

	public boolean getIvFormatIsHexadecimal() {
		return ivFormatIsHexadecimal;
	}

	public DisplayOption getDisplayOption() {
		return displayOption;
	}

	public String getKeystreamLengthValue() {
		return keystreamLengthValue;
	}

	/**
	 * Returns <code>true</code>, if the page is complete and the wizard may finish.
	 *
	 * @return	<code>true</code>, if the page is complete and the wizard may finish
	 */
	private boolean mayFinish() {
		if ( keyText.getText() != "" && ivText.getText() != "" ) { //$NON-NLS-1$ //$NON-NLS-2$
			if (displayKeystreamOnlyButton.getSelection())
				if (keystreamLengthText.getText() != "") //$NON-NLS-1$
					return true;
				else
					return false;

			return true;
		}
		return false;
	}
}

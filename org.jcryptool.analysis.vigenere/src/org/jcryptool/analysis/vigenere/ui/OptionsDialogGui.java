/* *****************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.analysis.vigenere.interfaces.DataProvider;

public class OptionsDialogGui extends Dialog {
	private FrequencyGui parent;

	private Combo comboReference;
	private Combo comboAlphabet;

	private Button buttonBlocks;

	/**
	 * Constructor for OptionsDialogGUI.
	 * @param container
	 */
	public OptionsDialogGui(final FrequencyGui container) {
		super(container.getShell());
		this.parent = container;
	}

	private void applyPressed() {
		boolean blocks = buttonBlocks.getSelection();
		String alph = comboAlphabet.getText();
		String reftxt = comboReference.getText();

		parent.chancePreview(blocks, reftxt, alph);
	}

	@Override
	protected void cancelPressed() {
		this.parent.cancelOperation();
		this.close();
		super.cancelPressed();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.OptionsDialogGui_dialog_title);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite compositeContain = new Composite(parent, SWT.NONE);
		compositeContain.setLayout(new GridLayout());

		Composite compositeContent = new Composite(compositeContain, SWT.NONE);
		compositeContent.setLayout(new GridLayout(2, false));
		compositeContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		buttonBlocks = new Button(compositeContent, SWT.CHECK);
		buttonBlocks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		buttonBlocks.setText(Messages.OptionsDialogGui_label_blocks);
		buttonBlocks.setSelection(this.parent.isBlocked());

		Label separatorUp = new Label(compositeContent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_separatorUp = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_separatorUp.verticalIndent = 10;
		separatorUp.setLayoutData(gd_separatorUp);

		Label labelReference = new Label(compositeContent, SWT.RIGHT);
		GridData gd_labelReference = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd_labelReference.verticalIndent = 10;
		labelReference.setLayoutData(gd_labelReference);
		labelReference.setText(Messages.OptionsDialogGui_label_reference);

		comboReference = new Combo(compositeContent, SWT.READ_ONLY);
		GridData gd_comboReference = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_comboReference.verticalIndent = 10;
		comboReference.setLayoutData(gd_comboReference);
		comboReference.setItems(DataProvider.getInstance().listReferenceTexts());
		comboReference.select(getIndexReferences());

		Label labelAlphabet = new Label(compositeContent, SWT.RIGHT);
		labelAlphabet.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		labelAlphabet.setText(Messages.OptionsDialogGui_label_alphabet);
		labelAlphabet.setAlignment(SWT.RIGHT);

		comboAlphabet = new Combo(compositeContent, SWT.READ_ONLY);
		comboAlphabet.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		comboAlphabet.setItems(DataProvider.getInstance().getAlphabets());
		comboAlphabet.select(getIndexAlphabets());

		Label separatorMid = new Label(compositeContent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_separatorMid = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_separatorMid.verticalIndent = 10;
		separatorMid.setLayoutData(gd_separatorMid);

		Button buttonPreview = new Button(compositeContent, SWT.PUSH);
		GridData gd_buttonPreview = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);
		gd_buttonPreview.verticalIndent = 10;
		buttonPreview.setLayoutData(gd_buttonPreview);
		buttonPreview.setText(Messages.OptionsDialogGui_button_preview);
		buttonPreview.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				applyPressed();
			}
		});

		Label separatorBottom = new Label(compositeContent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_separatorBottom = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_separatorBottom.verticalIndent = 10;
		separatorBottom.setLayoutData(gd_separatorBottom);

		compositeContain.setSize(compositeContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		return compositeContain;
	}

	private int getIndexAlphabets() {
		String alph = parent.getAlphabetIdent();
		String[] alphs = DataProvider.getInstance().getAlphabets();

		for (int i = 0; i < alphs.length; i++) {
			if (alph.equals(alphs[i])) {
				return i;
			}
		}

		return 0;
	}

	private int getIndexReferences() {
		String ref = parent.getRefTextIdent();
		String[] refs = DataProvider.getInstance().listReferenceTexts();

		for (int i = 0; i < refs.length; i++) {
			if (ref.equals(refs[i])) {
				return i;
			}
		}

		return 0;
	}

	@Override
	protected void okPressed() {
		boolean blocks = buttonBlocks.getSelection();
		String alph = comboAlphabet.getText();
		String reftxt = comboReference.getText();

		parent.chancePreview(blocks, reftxt, alph);
		parent.saveOptions(blocks, reftxt, alph);
		super.okPressed();
	}
}

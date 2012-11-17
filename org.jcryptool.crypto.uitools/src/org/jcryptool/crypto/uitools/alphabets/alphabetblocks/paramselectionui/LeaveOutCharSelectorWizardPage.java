package org.jcryptool.crypto.uitools.alphabets.alphabetblocks.paramselectionui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.crypto.uitools.alphabets.alphabetblocks.BlockAlphabet;
import org.jcryptool.crypto.uitools.alphabets.composite.AtomAlphabet;

public class LeaveOutCharSelectorWizardPage extends WizardPage {
	private Label lblCharacterToExclude;
	private Combo combo;
	private BlockAlphabet alpha;
	private Character selectedChar = null;

	/**
	 * Create the wizard.
	 */
	public LeaveOutCharSelectorWizardPage(BlockAlphabet alpha) {
		super("wizardPage");
		this.alpha = alpha;
		setTitle("Exclude one character from the alphabet block");
		setDescription(String.format("Please select the character to exclude from the %s alphabet block:", alpha.getBlockName()));
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		{
			lblCharacterToExclude = new Label(container, SWT.NONE);
			lblCharacterToExclude.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblCharacterToExclude.setText("Character to exclude: ");
		}
		{
			combo = new Combo(container, SWT.NONE);
			GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			layoutData.minimumWidth = 50;
			combo.setLayoutData(layoutData);
			fillCombo(alpha);
			if(combo.getItemCount() > 0) {
				selectCharAt(0);
			}
			this.setPageComplete(true);
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					selectCharAt(combo.getSelectionIndex());
				}
			});
		}
	}

	private void selectCharAt(int i) {
		combo.select(i);
		selectedChar = alpha.getCharacterSet()[combo.getSelectionIndex()];
	}

	private void fillCombo(BlockAlphabet alpha) {
		for(char c: alpha.getCharacterSet()) {
			String displayString = AtomAlphabet.getPrintableCharRepresentation(c);
			combo.add(displayString);
		}
	}
	
	public char getSelectedChar() {
		return selectedChar;
	}

}

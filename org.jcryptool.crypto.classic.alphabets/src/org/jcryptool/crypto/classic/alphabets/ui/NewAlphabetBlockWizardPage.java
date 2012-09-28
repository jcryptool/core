package org.jcryptool.crypto.classic.alphabets.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class NewAlphabetBlockWizardPage extends WizardPage {

	/**
	 * Create the wizard.
	 */
	public NewAlphabetBlockWizardPage() {
		super(Messages.getString("NewAlphabetBlockWizardPage.0")); //$NON-NLS-1$
		setTitle(Messages.getString("NewAlphabetBlockWizardPage.0")); //$NON-NLS-1$
		setDescription(Messages.getString("NewAlphabetBlockWizardPage.2")); //$NON-NLS-1$
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout());
		setControl(container);
	}

}

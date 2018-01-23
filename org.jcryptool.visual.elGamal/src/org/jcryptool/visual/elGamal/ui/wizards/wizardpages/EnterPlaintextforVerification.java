package org.jcryptool.visual.elGamal.ui.wizards.wizardpages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.library.Lib;

/**
 * The Dialog page for entering the plaintext in the verify tab of the plugin.
 * Main function is to set the text entered in textfield plaintext set as plaintext in the data object.
 * @author Thorben Groos
 *
 */
public class EnterPlaintextforVerification extends WizardPage {

	/** unique pagename to get this page from inside a wizard. */
	private static String PAGENAME = "EnterPlaintextforVerificationPage"; //$NON-NLS-1$

	/** Common data object fore storing the entries. */
	private ElGamalData data;

	/** field for entering the plaintext. */
	private Text plaintext;

	public EnterPlaintextforVerification(ElGamalData data) {
		super(PAGENAME);
		this.data = data;
		setDescription(Messages.EnterPlaintextforVerification_description);
		setTitle(Messages.EnterPlaintextforVerification_enter_plaintext);

	}

	@Override
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());

		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		label.setText(Messages.EnterSignaturePage_textentry);

		plaintext = new Text(composite, SWT.BORDER);
		plaintext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		plaintext.addVerifyListener(Lib.getVerifyListener(Lib.CHARACTERS));

		Button SHA1Checkbox = new Button(composite, SWT.CHECK);
		SHA1Checkbox.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		SHA1Checkbox.setText(Messages.EnterSignaturePage_use_sha1);
		SHA1Checkbox.setToolTipText(Messages.EnterSignaturePage_use_sha1_popup);
		SHA1Checkbox.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				data.setSimpleHash(!SHA1Checkbox.getSelection());
			}
		});

		// fill in old data
		plaintext.setText(data.getPlainText());
		SHA1Checkbox.setSelection(!data.getSimpleHash());

		setControl(composite);
	}

	/**
	 * getter for the contents of the plaintextfield.
	 *
	 * @return the content of the plaintextfield as string or the empty string if
	 *         not set
	 */
	public String getPlaintext() {
		return plaintext.getText();
	}

	/**
	 * getter for the pagename.
	 *
	 * @return the pagename
	 */
	public static String getPagename() {
		return PAGENAME;
	}

}

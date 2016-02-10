package org.jcryptool.crypto.classic.caesar.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CaesarKeyInput extends Composite {

	public Label passwordLabel;
	public Text passwordText;
	public Label shiftLabel;
	public Combo shiftCombo;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CaesarKeyInput(Composite parent, int style) {
		super(parent, style);
		
		GridLayout layout = new GridLayout(4, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		
		this.setLayout(layout);
		
		passwordLabel = new Label(this, SWT.NONE);
		GridData pwdLabelData = new GridData(SWT.FILL, SWT.CENTER, false, false);
		passwordLabel.setLayoutData(pwdLabelData);
		passwordLabel.setText(Messages.CaesarKeyInput_0);
		
		passwordText = new Text(this, SWT.NONE);
		GridData pwdTextData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		pwdTextData.widthHint = 40;
		passwordText.setLayoutData(pwdTextData);
		
		shiftLabel = new Label(this, SWT.NONE);
		GridData shiftLabelData = new GridData(SWT.FILL, SWT.CENTER, false, false);
		shiftLabel.setLayoutData(shiftLabelData);
		shiftLabel.setText(Messages.CaesarKeyInput_1);
		
		shiftCombo = new Combo(this, SWT.NONE);
		GridData shiftComboData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		shiftComboData.widthHint = 50;
		shiftCombo.setLayoutData(shiftComboData);

	}

	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}

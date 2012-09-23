package org.jcryptool.crypto.classic.alphabets.ui.customalphabets.customhistory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

public class CustomAlphabetItem extends Composite {
	private Label lblName;
	private Label lblContent;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CustomAlphabetItem(Composite parent, AbstractAlphabet alpha) {
		super(parent, SWT.BORDER);
		setLayout(new GridLayout(1, false));
		{
			lblName = new Label(this, SWT.NONE);
			lblName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			lblName.setText(alpha.getName());
		}
		{
			lblContent = new Label(this, SWT.WRAP);
			GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			layoutData.widthHint = 300;
			lblContent.setLayoutData(layoutData);
			lblContent.setText(AtomAlphabet.alphabetContentAsString(alpha.getCharacterSet()));
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}

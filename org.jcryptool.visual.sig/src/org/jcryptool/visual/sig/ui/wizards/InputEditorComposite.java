package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

public class InputEditorComposite extends Composite {
	
	// Limit for the length of the text that might be entered into the plaintext field
	private static final int TEXTLIMIT = 150;
	private Text text;

	public InputEditorComposite(Composite parent, int style) {
		super(parent, style);
		
		text = new Text(this, SWT.BORDER | SWT.WRAP);
		text.setBounds(10, 10, 430, 215);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        text.setTextLimit(TEXTLIMIT);
        
        Label lblToSaveThe = new Label(this, SWT.NONE);
        lblToSaveThe.setBounds(10, 231, 167, 15);
        lblToSaveThe.setText("to save the text click finish");
		// TODO Auto-generated constructor stub
	}

}

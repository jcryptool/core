package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

public class InputEditorComposite extends Composite {

	// Limit for the length of the text that might be entered into the plaintext field
	private static final int TEXTLIMIT = 1000;
	private Text text = null;
	private InputEditorWizardPage page;
	

	public InputEditorComposite(Composite parent, int style, InputEditorWizardPage p) {
		super(parent, style);

		text = new Text(this, SWT.BORDER | SWT.WRAP);
		text.setBounds(10, 10, 430, 215);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.setTextLimit(TEXTLIMIT);
		text.setFocus();
		
		Label lblToSaveThe = new Label(this, SWT.NONE);
		lblToSaveThe.setBounds(10, 231, 386, 15);
		lblToSaveThe.setText(Messages.InputEditorWizard_Label);
		
		page = p;

		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (text.getText().length() > 0) {
					page.setPageComplete(true);
					page.canFlipToNextPage();
					org.jcryptool.visual.sig.algorithm.Input.data = text.getText().getBytes();
					page.getWizard().getContainer().updateButtons();
				}
				else {
					page.setPageComplete(false);
					page.getWizard().getContainer().updateButtons();
					page.setErrorMessage("Please enter a text");
				}
			}
		});		
	}//end Constructor

	public String getText() {
		return text.getText();
		
	}

}

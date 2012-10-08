package org.jcryptool.crypto.classic.alphabets.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.TextfieldInput;
import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;

public class AlphabetManualInputField extends Composite {
	private Text text;
	private Label lblYouCanEnter;
	private TextfieldInput<AtomAlphabet> alphabetInput;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AlphabetManualInputField(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		{
			text = new Text(this, SWT.BORDER);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			alphabetInput = new TextfieldInput<AtomAlphabet>() {
				@Override
				protected Text getTextfield() {
					return text;
				}
				@Override
				protected InputVerificationResult verifyUserChange() {
					return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
				}
				@Override
				public AtomAlphabet readContent() {
					char[] chars = AtomAlphabet.parseAlphaContentFromString(getTextfield().getText());
					return new AtomAlphabet(chars);
				}
				@Override
				protected AtomAlphabet getDefaultContent() {
					return new AtomAlphabet(new char[]{});
				}
				@Override
				public String getName() {
					return "Alphabet input"; //$NON-NLS-1$
				}
				@Override
				public void writeContent(AtomAlphabet content) {
					setTextfieldTextExternal(AtomAlphabet.alphabetContentAsString(content.getCharacterSet()));
				}
			};
		}
		{
			lblYouCanEnter = new Label(this, SWT.WRAP);
			lblYouCanEnter.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC)); //$NON-NLS-1$
			lblYouCanEnter.setText(Messages.getString("AlphabetManualInputField.2")); //$NON-NLS-1$
			GridData gd_lblYouCanEnter = new GridData(SWT.FILL, SWT.CENTER, true, false);
			lblYouCanEnter.setLayoutData(gd_lblYouCanEnter);
			gd_lblYouCanEnter.widthHint = 100;
		}
	}
	
	public AtomAlphabet getAlphabet() {
		return alphabetInput.getContent();
	}

	public TextfieldInput<AtomAlphabet> getAlphabetInput() {
		return alphabetInput;
	}
	
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void writeCharset(char[] characterSet) {
		if(text != null && !text.isDisposed()) {
			alphabetInput.setTextfieldTextExternal(AtomAlphabet.alphabetContentAsString(characterSet));
			alphabetInput.synchronizeWithUserSide();
		}
	}

}

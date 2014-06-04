package org.jcryptool.crypto.classic.substitution.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.TextfieldInput;
import org.jcryptool.crypto.classic.model.ui.wizard.KeyInput;
import org.jcryptool.crypto.classic.model.ui.wizard.util.WidgetBubbleUIInputHandler;
import org.jcryptool.crypto.classic.substitution.algorithm.SubstitutionAlgorithm;
import org.jcryptool.crypto.classic.substitution.algorithm.SubstitutionAlgorithmSpecification;
import org.jcryptool.crypto.classic.substitution.algorithm.SubstitutionKey;
import org.jcryptool.crypto.classic.substitution.algorithm.SubstitutionKey.PasswordToKeyMethod;
import org.jcryptool.crypto.classic.substitution.ui.substControls.SubstitutionLetterInputField;
import org.jcryptool.crypto.classic.substitution.ui.substControls.SubstitutionLetterInputField.Mode;

public class SubstitutionKeyEditor extends Composite {
	private static final PasswordToKeyMethod keyMethodAntilexical = new PasswordToKeyMethod(true, true, false);

	private static final int WIDTH_HINT_GLOBAL = 600;

	public static class NoCharacterVerificationResult extends InputVerificationResult {

		private char plaintextChar;

		public NoCharacterVerificationResult(char plaintextChar) {
			this.plaintextChar = plaintextChar;
		}

		@Override
		public String getMessage() {
			return String.format(Messages.SubstitutionKeyEditor_0, String.valueOf(plaintextChar));
		}

		@Override
		public MessageType getMessageType() {
			return MessageType.INFORMATION;
		}

		@Override
		public boolean isStandaloneMessage() {
			return true;
		}

		@Override
		public boolean isValid() {
			return true;
		}

	}

	public static class NoCharRepresentationDetectedVerificationResult extends InputVerificationResult {

		private String textfieldContent;
		private Character parsedContent;
		private char plaintextChar;

		public NoCharRepresentationDetectedVerificationResult(String textfieldContent, Character parsedContent, char plaintextChar) {
			this.textfieldContent = textfieldContent;
			this.parsedContent = parsedContent;
			this.plaintextChar = plaintextChar;
		}

		@Override
		public String getMessage() {
			return String
					.format(Messages.SubstitutionKeyEditor_1,
							textfieldContent, String.valueOf(plaintextChar));
		}

		@Override
		public MessageType getMessageType() {
			return MessageType.WARNING;
		}

		@Override
		public boolean isStandaloneMessage() {
			return true;
		}

		@Override
		public boolean isValid() {
			return false;
		}

	}

	public static class CharNotInAlphabetVerificationResult extends InputVerificationResult {

		private String textfieldContent;
		private Character parsedContent;
		private AbstractAlphabet plaintextAlpha;
		private char plaintextChar;

		public CharNotInAlphabetVerificationResult(String textfieldContent, Character parsedContent,
				AbstractAlphabet plaintextAlpha, char plaintextChar) {
			this.textfieldContent = textfieldContent;
			this.parsedContent = parsedContent;
			this.plaintextAlpha = plaintextAlpha;
			this.plaintextChar = plaintextChar;
		}

		@Override
		public String getMessage() {
			return String.format(Messages.SubstitutionKeyEditor_2, String.valueOf(parsedContent));
		}

		@Override
		public MessageType getMessageType() {
			return MessageType.WARNING;
		}

		@Override
		public boolean isStandaloneMessage() {
			return true;
		}

		@Override
		public boolean isValid() {
			return false;
		}

	}

	private final Color UNDETERMINED_SUBST_COLOR;

	private Text txtPassword;

	private Map<Character, Character> charMapping;
	private Map<Character, TextfieldInput<Character>> inputs;
	private Map<Character, SubstitutionLetterInputField> charInputControls;

	private ScrolledComposite scrolledComposite;

	private Composite scrollCompMain;

	private AbstractAlphabet plaintextAlphabet;

	private Label lblPasswordExplanation;

	private KeyInput<String> passwordInput;

	private boolean complete = false;

	private List<Observer> observers;

	private PasswordToKeyMethod keyCreationMethod;

	private Button btnSet;

	private Button restOfAlphaLexical;

	private Button restOfAlphaAntilexical;

	private SubstitutionAlgorithmSpecification spec;

	private Button btnReset;

	/**
	 * Create the composite.
	 *
	 * @param parent
	 * @param style
	 */
	public SubstitutionKeyEditor(Composite parent, int style, AbstractAlphabet plaintextAlphabet, SubstitutionAlgorithmSpecification spec) {
		super(parent, style);
		this.plaintextAlphabet = plaintextAlphabet;
		this.spec = spec;
		this.inputs = new HashMap<Character, TextfieldInput<Character>>();
		this.charInputControls = new HashMap<Character, SubstitutionLetterInputField>();
		this.observers = new LinkedList<Observer>();
		this.keyCreationMethod = new SubstitutionAlgorithmSpecification().getDefaultKeyCreationMethod();
		charMapping = new HashMap<Character, Character>();
		UNDETERMINED_SUBST_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED);
		createGUI(plaintextAlphabet);
	}

	private void createGUI(final AbstractAlphabet plaintextAlphabet) {
		setLayout(new GridLayout(1, false));

		Label lblHereYouCan = new Label(this, SWT.NONE);
		lblHereYouCan.setText(Messages.SubstitutionKeyEditor_3);

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		layoutData.widthHint = WIDTH_HINT_GLOBAL;
		scrolledComposite.setLayoutData(layoutData);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		scrollCompMain = new Composite(scrolledComposite, SWT.NONE);
		scrollCompMain.setLayout(new GridLayout(plaintextAlphabet.getCharacterSet().length, false));
		scrollCompMain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		scrolledComposite.setContent(scrollCompMain);

		createSubstitutionControls(plaintextAlphabet);
		this.charMapping = readMapping();
		updateUnusedCharsList();

		scrolledComposite.setMinSize(scrollCompMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setText(Messages.SubstitutionKeyEditor_4);

		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_1.setLayout(new GridLayout(1, false));


		txtPassword = new Text(composite_1, SWT.BORDER);
		txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR) {
					setKeyByPassword();
				}
			}
		});

		Composite compSetPassword = new Composite(composite_1, SWT.NONE);
		GridLayout compSetPasswordLayout = new GridLayout(4, false);
		compSetPasswordLayout.marginWidth = 0;
		compSetPasswordLayout.marginHeight = 0;
		compSetPassword.setLayout(compSetPasswordLayout);
		GridData compSetPasswordLData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		compSetPassword.setLayoutData(compSetPasswordLData);

		btnSet = new Button(compSetPassword, SWT.NONE);
		btnSet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		btnSet.setText(Messages.SubstitutionKeyEditor_5);
		btnSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setKeyByPassword();
			}
		});

		Label restOfAlphaLabel = new Label(compSetPassword, SWT.NONE);
		restOfAlphaLabel.setText(Messages.SubstitutionKeyEditor_6);

		Composite compRestDirection = new Composite(compSetPassword, SWT.NONE);
		GridLayout compRestDirectionLayout = new GridLayout(1, false);
		compRestDirectionLayout.marginWidth = 0;
		compRestDirectionLayout.marginHeight = 0;
		compRestDirection.setLayout(compRestDirectionLayout);
		GridData compRestDirectionLData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 2);
		compRestDirection.setLayoutData(compRestDirectionLData);

		restOfAlphaLexical = new Button(compRestDirection, SWT.RADIO);
		restOfAlphaLexical.setText(Messages.SubstitutionKeyEditor_8);
		boolean lexicalBtnSelection = !spec.getDefaultKeyCreationMethod().equals(keyMethodAntilexical);
		restOfAlphaLexical.setSelection(
				lexicalBtnSelection
				);

		restOfAlphaAntilexical = new Button(compRestDirection, SWT.RADIO);
		restOfAlphaAntilexical.setText(Messages.SubstitutionKeyEditor_9);
		restOfAlphaAntilexical.setSelection(
				!lexicalBtnSelection
				);


		btnReset = new Button(compSetPassword, SWT.NONE);
		btnReset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		btnReset.setText(Messages.SubstitutionKeyEditor_10);
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setKeyByPassword("", new PasswordToKeyMethod(true, true, true)); //$NON-NLS-1$
				passwordInput.writeContent(""); //$NON-NLS-1$
				passwordInput.synchronizeWithUserSide();
			}
		});

		Composite compKeyExplanation = new Composite(composite_1, SWT.NONE);
		GridLayout compKeyExplanationLayout = new GridLayout(2, false);
		compKeyExplanationLayout.marginWidth = 0;
		compKeyExplanationLayout.marginHeight = 0;
		compKeyExplanation.setLayout(compKeyExplanationLayout);
		GridData compKeyExplanationLayoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		compKeyExplanationLayoutData.horizontalSpan = 2;
		compKeyExplanation.setLayoutData(compKeyExplanationLayoutData);

		Label lblInfoImg = new Label(compKeyExplanation, SWT.NONE);
		lblInfoImg.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		//TODO: activate image
		Image infoImg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK);
		lblInfoImg.setImage(infoImg);
//		lblInfoImg.setText("(i)");


		lblPasswordExplanation = new Label(compKeyExplanation, SWT.WRAP);
		GridData lblPasswordExplanationLData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		lblPasswordExplanationLData.widthHint = (int) (WIDTH_HINT_GLOBAL * 0.9);
		lblPasswordExplanation.setLayoutData(lblPasswordExplanationLData);
		lblPasswordExplanation.setText(Messages.SubstitutionKeyEditor_7);

		passwordInput = new KeyInput<String>() {

			@Override
			public AbstractAlphabet getAlphabet() {
				return plaintextAlphabet;
			}

			@Override
			public Text getTextfield() {
				return txtPassword;
			}

			@Override
			protected InputVerificationResult verifyUserChange() {
				return KeyVerificator.verify(getTextfield().getText(), plaintextAlphabet, SubstitutionAlgorithm.specification.getKeyVerificators());
			}

			@Override
			public String readContent() {
				return getTextfield().getText();
			}

			@Override
			protected String getDefaultContent() {
				return ""; //$NON-NLS-1$
			}

			@Override
			public String getName() {
				return "password"; //$NON-NLS-1$
			}
		};
		passwordInput.writeContent(passwordInput.getContent());
		WidgetBubbleUIInputHandler inputHandler = new WidgetBubbleUIInputHandler(getShell()) {
			@Override
			public Control mapInputToWidget(AbstractUIInput<?> input) {
				if (input.equals(passwordInput)) {
                    return passwordInput.getTextfield();
                }
                return super.mapInputToWidget(input);
			}
		};
		passwordInput.addObserver(inputHandler);

		complete = checkMappingComplete();
	}

	private void setKeyByPassword() {
		if(!txtPassword.getText().equals("")) { //$NON-NLS-1$
			setKeyByPassword(passwordInput.getContent(), getKeyCreationMethod());
		}
	}

	protected void setKeyByPassword(String text, PasswordToKeyMethod passwordToKeyMethod) {
		SubstitutionKey keyToSet = passwordToKeyMethod.createKey(text, plaintextAlphabet);

		setCharMappingExternal(keyToSet.getSubstitutions());
	}

	private void updateUnusedCharsList() {
		List<Character> usedChars = new LinkedList<Character>();
		for(Character c: this.charMapping.values()) if(c!=null) usedChars.add(c);

		for(SubstitutionLetterInputField ctrl: this.charInputControls.values()) {
			ctrl.setCharactersInUse(usedChars);
		}
	}

	private Map<Character, Character> readMapping() {
		HashMap<Character, Character> result = new HashMap<Character, Character>();
		for(char c: plaintextAlphabet.getCharacterSet()) {
			TextfieldInput<Character> input = inputs.get(Character.valueOf(c));
			result.put(c, input.getContent());
		}
		return result;
	}

	private void createSubstitutionControls(AbstractAlphabet plaintextAlphabet) {
		char[] characterSet = plaintextAlphabet.getCharacterSet();
		for (int i = 0; i < characterSet.length; i++) {
			char c = characterSet[i];
			Composite substAtomControl = createSingleSubstitutionControl(scrollCompMain, c, i);
		}
	}

	private Composite createSingleSubstitutionControl(Composite scrollCompMain2, final char plaintextChar, final int posInAlpha) {
		Composite comp = new Composite(scrollCompMain2, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		comp.setLayout(layout);
		GridData layoutData = new GridData();
		comp.setLayoutData(layoutData);

		Label plainChar = new Label(comp, SWT.NONE);
		plainChar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		plainChar.setText(AbstractAlphabet.getPrintableCharRepresentation(plaintextChar));

		Label arrow = new Label(comp, SWT.NONE);
		arrow.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		arrow.setText(String.valueOf((char) 8595));

		final SubstitutionLetterInputField characterInputControl = new SubstitutionLetterInputField(comp, Mode.VERTICAL_POPUP, plaintextAlphabet);
		charInputControls.put(plaintextChar, characterInputControl);

		final TextfieldInput<Character> input = characterInputControl.getCharInput();
		input.writeContent(plaintextChar);
		input.synchronizeWithUserSide();
		inputs.put(plaintextChar, input);

		input.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				if(arg==null) {
					SubstitutionKeyEditor.this.charMapping = readMapping();
					updateUnusedCharsList();
					if (input.getContent() == null) {
					} else {
						// look if this substitution exists already
						for (TextfieldInput<Character> otherInput : inputs.values()) {
							if (otherInput != null && otherInput != input) {
								if (input.getContent() != null && otherInput.getContent() != null
										&& otherInput.getContent().equals(input.getContent())) {
									//reset the conflicting input to "undetermined"
									otherInput.writeContent(null);
									otherInput.synchronizeWithUserSide();
								}
							}
						}
					}

					setTextfieldSubstitutionUndetermined(characterInputControl, input.getContent() == null);

					complete = checkMappingComplete();
					for(Observer obs: SubstitutionKeyEditor.this.observers) {
						obs.update(null, null);
					}
				}
			}
		});

		return comp;
	}

	private void setTextfieldSubstitutionUndetermined(SubstitutionLetterInputField characterInputControls, boolean undetermined) {
		Color colorToSet = undetermined ? UNDETERMINED_SUBST_COLOR : null;
		characterInputControls.setTextfieldBorderColor(colorToSet);
	}

//	private boolean isTextfieldSetToUndetermined(TextfieldInput<?> input) {
//		Text textfield = input.getTextfield();
//		return textfield.getBackground().equals(UNDETERMINED_SUBST_COLOR);
//	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Returns the selected character mapping (even if cancelled).
	 * To check, whether the mapping is complete, call {@link #isCompleteData()}.<br />
	 * all characters which have not had a substitution character set, have "null" mapped to them.<br /><br />
	 * This map contains all alphabet characters as keys. If {@link #isCompleteData()} returns true, all alphabet characters
	 * have a unique, non-null mapping.<br /><br />
	 * Please note, that the method {@link #wasFinished()} returns whether the dialog was finished or cancelled.
	 *
	 * @return the selected character mapping
	 */
	public Map<Character, Character> getCharMapping() {
		return charMapping;
	}

	/**
	 * @return whether the selected character mapping is complete
	 */
	public boolean isCompleteData() {
		return complete;
	}

	private boolean checkMappingComplete() {
		for(Map.Entry<Character, Character> entry: getCharMapping().entrySet()) {
			if(entry.getValue() == null) return false;
		}
		return true;
	}

	public void setKeyCreationMethod(SubstitutionKey.PasswordToKeyMethod method) {
		this.keyCreationMethod = method;
	}

	public AbstractAlphabet getAlphabet() {
		return plaintextAlphabet;
	}

	public String getPassword() {
		return passwordInput.getContent();
	}

	public PasswordToKeyMethod getKeyCreationMethod() {
		if(restOfAlphaLexical.getSelection()) {
			return new SubstitutionKey.PasswordToKeyMethod(true, true, true);
		} else {
			return new SubstitutionKey.PasswordToKeyMethod(true, true, false);
		}
	}

	/**
	 * populates the substitutions with a password like it would happen when using the password text field.
	 *
	 * @param password the password
	 */
	public void setPasswordExternal(String password, boolean apply) {
		passwordInput.writeContent(""); //$NON-NLS-1$
		passwordInput.synchronizeWithUserSide();
		for(char c: password.toCharArray()) {
			passwordInput.writeContent(passwordInput.getContent()+c);
			passwordInput.synchronizeWithUserSide();

		}

		if(apply) {
			setKeyByPassword(passwordInput.getContent(), getKeyCreationMethod());
		}
	}

	/**
	 * @param o an observer which will be notified when the character mapping changes by editing through this composite.<br />
	 * Note, that both arguments of the {@link Observer#update(Observable, Object)} method will be null;
	 * frequent updates are possible.
	 */
	public void addObserver(Observer o) {
		this.observers.add(o);
	}

	public void setCharMappingExternal(Map<Character, Character> mapping) {
		for(Character plaintextChar: inputs.keySet()) {
			TextfieldInput<Character> input = inputs.get(plaintextChar);
			input.writeContent(mapping.get(plaintextChar));
			input.synchronizeWithUserSide();
		}
	}
}

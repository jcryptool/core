//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.substitution.ui.modules.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.analysis.substitution.ui.modules.utils.SubstitutionLetterInputField.Mode;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.util.input.TextfieldInput;

public class SubstitutionKeyEditor extends Composite {
	private final Color UNDETERMINED_SUBST_COLOR;

	private Map<Character, Character> charMapping;
	private Map<Character, TextfieldInput<Character>> inputs;
	private Map<Character, SubstitutionLetterInputField> charInputControls;

	private ScrolledComposite scrolledComposite;

	private Composite scrollCompMain;

	private AbstractAlphabet plaintextAlphabet;

	private boolean complete = false;

	private List<Observer> observers;



	private LinkedList<Character> plaintextChars;



	private boolean displayUndefinedChars = false;



	private boolean showIllustrativeMapper;

	/**
	 * Create the composite.
	 *
	 * @param parent
	 * @param style
	 */
	public SubstitutionKeyEditor(Composite parent, int style, AbstractAlphabet plaintextAlphabet, boolean showIllustrativeMapper, List<Character> subset) {
		super(parent, style);
		this.showIllustrativeMapper = showIllustrativeMapper;
		this.plaintextChars = new LinkedList<Character>(AbstractAlphabet.calcAlphaConjunction(plaintextAlphabet.asList(), subset));
		this.plaintextAlphabet = plaintextAlphabet;
		this.inputs = new HashMap<Character, TextfieldInput<Character>>();
		this.charInputControls = new HashMap<Character, SubstitutionLetterInputField>();
		this.observers = new LinkedList<Observer>();
		charMapping = new HashMap<Character, Character>();
		UNDETERMINED_SUBST_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED);
		createGUI(plaintextAlphabet);
	}

	/**
	 * Create the composite.
	 *
	 * @param parent
	 * @param style
	 */
	public SubstitutionKeyEditor(Composite parent, int style, AbstractAlphabet plaintextAlphabet) {
		this(parent, style, plaintextAlphabet, true, plaintextAlphabet.asList());
	}

	public void setDisplayUndefinedChars(boolean displayUndefinedChars) {
		this.displayUndefinedChars = displayUndefinedChars;
	}



	private void createGUI(final AbstractAlphabet plaintextAlphabet) {
		setLayout(new GridLayout(1, false));

//		Label lblHereYouCan = new Label(this, SWT.NONE);
//		lblHereYouCan.setText(Messages.SubstitutionKeyEditor_0);

		Composite composite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		scrolledComposite = new ScrolledComposite(composite, this.getStyle() | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
//		layoutData.widthHint = (int) Math.round(WIDTH_HINT_GLOBAL * ((double)plaintextChars.size() / (double)plaintextAlphabet.getCharacterSet().length) );
		scrolledComposite.setLayoutData(layoutData);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		scrollCompMain = new Composite(scrolledComposite, SWT.NONE);
		scrollCompMain.setLayout(new GridLayout(1, false));
		scrollCompMain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		scrolledComposite.setContent(scrollCompMain);

		createSubstitutionControls(plaintextAlphabet);
		this.charMapping = readMapping();
		updateUnusedCharsList();

		scrolledComposite.setMinSize(scrollCompMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		complete = checkMappingComplete();
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
		for(Character c: plaintextChars) {
			TextfieldInput<Character> input = inputs.get(Character.valueOf(c));
			result.put(c, input.getContent());
		}
		return result;
	}

	private void createSubstitutionControls(AbstractAlphabet plaintextAlphabet) {
		Composite centerComp = new Composite(scrollCompMain, SWT.NONE);
		GridLayout layout = new GridLayout(plaintextChars.size()+(showIllustrativeMapper?2:0), false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		centerComp.setLayout(layout);
		centerComp.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

		if(showIllustrativeMapper) {
			Composite compIllustration = new Composite(centerComp, SWT.NONE);
			GridLayout layoutIllustration = new GridLayout(1, false);
			compIllustration.setLayout(layoutIllustration);
			GridData layoutData = new GridData();
			compIllustration.setLayoutData(layoutData);

			Label plainChar = new Label(compIllustration, SWT.NONE);
			plainChar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
			plainChar.setText(Messages.SubstitutionKeyEditor_1);

			Label arrow = new Label(compIllustration, SWT.NONE);
			arrow.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
			arrow.setText(String.valueOf((char) 8595));

			final IllustrationSubstitutionLetterInputField characterInputControl = new IllustrationSubstitutionLetterInputField(compIllustration, plaintextAlphabet);

			//spaceholder
			Label placeholder = new Label(centerComp, SWT.NONE);
			GridData layoutDataPlaceHolder = new GridData();
			layoutDataPlaceHolder.widthHint = 15;
			placeholder.setLayoutData(layoutDataPlaceHolder);
		}

		for (int i = 0; i < plaintextChars.size(); i++) {
			char c = plaintextChars.get(i);
			Composite substAtomControl = createSingleSubstitutionControl(centerComp, c, i);
		}
	}

	private Composite createSingleSubstitutionControl(Composite parent, final char plaintextChar, final int posInAlpha) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		comp.setLayout(layout);
		GridData layoutData = new GridData();
		comp.setLayoutData(layoutData);

		Label plainChar = new Label(comp, SWT.NONE);
		plainChar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		plainChar.setText(AbstractAlphabet.getPrintableCharRepresentation(plaintextChar));
		plainChar.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL));

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
		if(displayUndefinedChars) characterInputControls.setTextfieldBorderColor(colorToSet);
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

	public AbstractAlphabet getAlphabet() {
		return plaintextAlphabet;
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

	public void setSingleSubstitution(Character key, Character value) {
		if(getAlphabet().contains(value) && plaintextChars.contains(key)) {
			TextfieldInput<Character> textfieldInput = inputs.get(key);
			textfieldInput.writeContent(value);
			textfieldInput.synchronizeWithUserSide();
		}
	}
}

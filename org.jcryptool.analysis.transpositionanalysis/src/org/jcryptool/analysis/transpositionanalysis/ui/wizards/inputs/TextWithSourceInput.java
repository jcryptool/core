//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.ui.wizards.inputs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorReference;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorUtils;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.InputVerificationResult.MessageType;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;
import org.jcryptool.crypto.ui.textsource.TextSourceType;

public abstract class TextWithSourceInput extends AbstractUIInput<TextInputWithSource> {

	private static final double THRESHHOLD_DELETE_DISCONNECT_TEXT_PERCENTILE = 0.85;

	private static final String NOBALLOON_RESULTTYPE = "NOBALLOON"; //$NON-NLS-1$

	private Integer lastTextLength;
	private Double lastPercentageOfTextSelected;
	private Point lastSelection;

	TextInputWithSource userinputSource = null;

	public TextWithSourceInput(List<IEditorReference> editors) {
		super();

		getTextFieldForTextInput().addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				monitorSelectionPercentageAndTextLength(getTextFieldForTextInput().getSelection(),
						getTextFieldForTextInput().getText().length());
			}

			@Override
			public void mouseDown(MouseEvent e) {
				monitorSelectionPercentageAndTextLength(getTextFieldForTextInput().getSelection(),
						getTextFieldForTextInput().getText().length());
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		getTextFieldForTextInput().addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				monitorSelectionPercentageAndTextLength(getTextFieldForTextInput().getSelection(),
						getTextFieldForTextInput().getText().length());
			}

			@Override
			public void keyPressed(KeyEvent e) {
				monitorSelectionPercentageAndTextLength(getTextFieldForTextInput().getSelection(),
						getTextFieldForTextInput().getText().length());
			}
		});

		getTextOnlyInput().addObserver(this);
	}

	@Override
	protected InputVerificationResult verifyUserChange() {
		if (getFileRadioButton().getSelection() && getSelectedFile() == null) {
			// msg not meant to be displayed
			return InputVerificationResult.generateIVR(false, "no file was selected", MessageType.INFORMATION, false, //$NON-NLS-1$
					NOBALLOON_RESULTTYPE);
		}
		if (getFileRadioButton().getSelection() && !getSelectedFile().exists()) {
			// msg not meant to be displayed
			return InputVerificationResult.generateIVR(false, "input file does not exist", MessageType.INFORMATION, //$NON-NLS-1$
					false);
		}
		if (getBtnJctEditorOption().getSelection() && getEditorsNotNecessarilyFresh().size() == 0) {
			// msg not meant to be displayed
			return InputVerificationResult.generateIVR(false, "no editors are available", MessageType.INFORMATION, //$NON-NLS-1$
					false, NOBALLOON_RESULTTYPE);
		}
		if (getBtnJctEditorOption().getSelection() && getComboEditors().getSelectionIndex() < 0) {
			// should never appear
			return InputVerificationResult.generateIVR(true, "no editor selected", MessageType.INFORMATION, false, //$NON-NLS-1$
					NOBALLOON_RESULTTYPE);
		}
		return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
	}

	@Override
	public TextInputWithSource readContent() {
		if (getBtnOwninput().getSelection()) {
			String text = getTextOnlyInput().getContent();
			if (!getContent().getSourceType().equals(TextSourceType.USERINPUT)) {
				userinputSource = getContent();
			}
			return new TextInputWithSource(text, userinputSource);
		} else if (getFileRadioButton().getSelection()) {
			try {
				// reset user input origin
				userinputSource = null;
				return new TextInputWithSource(getTextFromFile(getSelectedFile()), getSelectedFile());
			} catch (FileNotFoundException ex) {
				// should not happen since existence of file has been
				// tested
				LogUtil.logError(ex);
				return null;
			}
		} else if (getBtnJctEditorOption().getSelection()) {
			// reset user input origin
			userinputSource = null;
			if (getComboEditors().getSelectionIndex() < 0) {
				IEditorReference bestEditor = getBestEditorReference();
				return new TextInputWithSource(EditorUtils.retrieveTextForEditor(bestEditor), bestEditor);
			} else {
				IEditorReference currentlySelectedEditor = getSelectedEditor();
				return new TextInputWithSource(EditorUtils.retrieveTextForEditor(currentlySelectedEditor),
						currentlySelectedEditor);
			}
		} else {
			throw new RuntimeException("Not all input method cases covered at reading input text!"); //$NON-NLS-1$
		}
	}

	@Override
	public void writeContent(TextInputWithSource content) {
		if (content.getSourceType().equals(TextSourceType.FILE)) {
			setUIState(content, true);
		} else if (content.getSourceType().equals(TextSourceType.JCTEDITOR)) {
			setUIState(content, true);
		} else if (content.getSourceType().equals(TextSourceType.USERINPUT)) {
			setUIState(content, true);
		} else {
			throw new RuntimeException("not all cases covered in writeContent"); //$NON-NLS-1$
		}
	}

	@Override
	protected TextInputWithSource getDefaultContent() {
		if (getInitialTextObject() != null) { // preset from out of the wizard
			// exists
			if (!isEditorAvailable()) {
				if (getInitialTextObject().getSourceType().equals(TextSourceType.JCTEDITOR)) {
					// mh... has to load text from editor, but it is not
					// available. what to do?
					// just default.
					return new TextInputWithSource(getInitialTextObject().getText());
				} else {
					return getInitialTextObject();
				}
			} else {
				return getInitialTextObject();
			}
		} else { // no preset from out of the wizard
			if (!isEditorAvailable()) {
				return new TextInputWithSource(""); //$NON-NLS-1$
			} else {
				IEditorReference bestEditor = getBestEditorReference();
				return new TextInputWithSource(EditorUtils.retrieveTextForEditor(bestEditor), bestEditor);
			}
		}
	}

	@Override
	public String getName() {
		return "Text"; //$NON-NLS-1$
	}

	protected abstract Button getFileRadioButton();

	protected abstract Button getBtnJctEditorOption();

	protected abstract Button getBtnOwninput();

	protected abstract File getSelectedFile();

	protected abstract Combo getComboEditors();

	protected abstract Text getTextFieldForTextInput();

	protected abstract IEditorReference getSelectedEditor();

	protected abstract void setUIState(TextInputWithSource content, boolean b);

	protected abstract TextInputWithSource getInitialTextObject();

	protected abstract List<IEditorReference> getEditorsNotNecessarilyFresh();

	private static IEditorReference getBestEditorReference() {
		List<IEditorReference> editorReferences = EditorsManager.getInstance().getEditorReferences();
		if (editorReferences.size() > 0) {
			IEditorReference activeEditor = EditorsManager.getInstance().getActiveEditorReference();
			if (activeEditor != null) {
				return activeEditor;
			} else {
				return editorReferences.get(0);
			}
		} else {
			return null;
		}
	}

	private static String getTextFromFile(File f) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(f);
		return EditorUtils.inputStreamToString(fis);
	}

	private boolean isEditorAvailable() {
		return getEditorsNotNecessarilyFresh().size() > 0;
	}

	protected void monitorSelectionPercentageAndTextLength(Point selection, int textLength) {
		if (lastSelection == null) {
			lastSelection = new Point(0, 0);
		}
		if (lastPercentageOfTextSelected == null) {
			lastPercentageOfTextSelected = 0.0;
		}
		if (lastTextLength == null) {
			lastTextLength = textLength;
		}

		double percentageOfTextSelected = textLength == 0 ? -1
				: Double.valueOf(((double) Math.abs(selection.x - selection.y) / (double) textLength));
		double percentageOfLastTextLengthStillAvailable = Double
				.valueOf(((double) textLength / (double) lastTextLength));
		if (percentageOfTextSelected == -1) {
			// special case: text length is now zero.
			markDisconnectFromPreviousText();
		} else {
			if (lastPercentageOfTextSelected > THRESHHOLD_DELETE_DISCONNECT_TEXT_PERCENTILE) {
				if (percentageOfTextSelected < 0.05 && percentageOfLastTextLengthStillAvailable <= (1
						- (THRESHHOLD_DELETE_DISCONNECT_TEXT_PERCENTILE - 0.05))) {
					// seems like the big selected text got deleted --
					// disconnect
					markDisconnectFromPreviousText();
				} else if (Math.abs(selection.y - selection.x) == 0 && (lastTextLength != textLength)) {
					// paste detected
					// TODO: length comparison of texts should not be indicator of text change
					// (could be accidentally the same length but still text replacement)
					markDisconnectFromPreviousText();
				}
			}
		}

		lastSelection = selection;
		lastPercentageOfTextSelected = percentageOfTextSelected;
		lastTextLength = textLength;
	}

	private void markDisconnectFromPreviousText() {
		userinputSource = null;
		synchronizeWithUserSide();
	}

	protected abstract AbstractUIInput<String> getTextOnlyInput();

}

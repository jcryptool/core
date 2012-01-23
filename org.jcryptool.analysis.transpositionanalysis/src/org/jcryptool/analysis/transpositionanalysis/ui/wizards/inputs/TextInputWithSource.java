package org.jcryptool.analysis.transpositionanalysis.ui.wizards.inputs;

import java.io.File;

import org.eclipse.ui.IEditorReference;

/**
 * Data structure for delivering text and referencing the source of it. Very
 * related to {@link TextSourceType}
 * 
 * @author Simon L
 * 
 */
public class TextInputWithSource {
	private TextSourceType sourceType;
	public File file;
	public IEditorReference editorReference;
	private String text;
	public TextInputWithSource userInputOrigin;

	private TextInputWithSource(String text, TextSourceType sourceType) {
		super();
		this.text = text;
		this.sourceType = sourceType;
	}
	
	/**
	 * Source type will be {@link TextSourceType#USERINPUT}
	 * 
	 * @param text
	 *            the userinput text
	 */
	public TextInputWithSource(String text) {
		this(text, TextSourceType.USERINPUT);
	}

	public TextInputWithSource(String text, TextInputWithSource userInputOrigin) {
		this(text, TextSourceType.USERINPUT);
		this.userInputOrigin = userInputOrigin;
	}

	public TextInputWithSource(String text, File file) {
		this(text, TextSourceType.FILE);
		this.file = file;
	}

	public TextInputWithSource(String text, IEditorReference editorReference) {
		this(text, TextSourceType.JCTEDITOR);
		this.editorReference = editorReference;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TextSourceType getSourceType() {
		return sourceType;
	}

	public boolean equals(TextInputWithSource o2) {
		return getText().equals(o2.getText()) && getSourceType().equals(o2.getSourceType())
			&& (file == null ? (o2.file == null) : (file.equals(o2.file)))
			&& (editorReference == null ? (o2.editorReference == null) : (editorReference.equals(o2.editorReference)))
			&& (userInputOrigin == null ? (o2.userInputOrigin == null) : (userInputOrigin.equals(o2.userInputOrigin)));
	}

	@Override
	public String toString() {
		if (getSourceType().equals(TextSourceType.USERINPUT)) {
			if (userInputOrigin == null) {
				return "manual input";
			} else {
				return "manual input; Source: " + userInputOrigin.toString();
			}
		} else if (getSourceType().equals(TextSourceType.JCTEDITOR)) {
			return "Editor: " + editorReference == null ? (editorReference.getTitle()+" (not opened anymore)") : editorReference.getTitle();
		} else if (getSourceType().equals(TextSourceType.FILE)) {
			return "File: " + file.getName();
		} else {
			return super.toString();
		}
	}

}

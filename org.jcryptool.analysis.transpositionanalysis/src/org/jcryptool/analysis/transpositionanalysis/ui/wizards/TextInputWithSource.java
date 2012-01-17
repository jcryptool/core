package org.jcryptool.analysis.transpositionanalysis.ui.wizards;

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
			&& (editorReference == null ? (o2.editorReference == null) : (editorReference.equals(o2.editorReference)));
	}

}

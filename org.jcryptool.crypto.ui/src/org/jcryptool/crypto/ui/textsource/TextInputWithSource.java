//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.ui.textsource;

import java.io.File;

import org.eclipse.equinox.internal.transforms.TransformedBundleEntry;
import org.eclipse.ui.IEditorReference;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;

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
	private TransformData transformation = null;
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
		if (this.transformation == null) {
			return this.text;
		}
		Transform transform = new Transform();
		transform.setMyTransform(this.transformation);
		String transformed = transform.transformText(this.text);
		return transformed;
	}

	public void setTransform(TransformData data) {
		this.transformation = data;
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
				return Messages.TextInputWithSource_manualInput;
			} else {
				return Messages.TextInputWithSource_manualInput_source + userInputOrigin.toString();
			}
		} else if (getSourceType().equals(TextSourceType.JCTEDITOR)) {
			return Messages.TextInputWithSource_editor + editorReference == null ? (editorReference.getTitle()+Messages.TextInputWithSource_notopenedanymore) : editorReference.getTitle();
		} else if (getSourceType().equals(TextSourceType.FILE)) {
			return Messages.TextInputWithSource_file + file.getName();
		} else {
			return super.toString();
		}
	}

	public TransformData getTransform() {
		return this.transformation;
	}

}

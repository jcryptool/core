//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.freqanalysis.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.jcryptool.analysis.freqanalysis.FreqAnalysisPlugin;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.constants.IConstants;

/**
 * @author Anatoli Barski
 *
 */
public abstract class AbstractAnalysisUI extends Composite {

	protected Composite composite0;
	protected Button button;
	protected Button button0;
	protected Button button1;

	protected String text;
	protected CustomFreqCanvas myGraph;
	protected int myOffset, myLength;
	protected Button button3;
	protected Spinner spinner2;
	protected Spinner spinner1;

	AbstractAnalysisUI(final Composite parent, final int style) {
		super(parent, style);
	}

	/**
	 * checks, whether an editor is opened or not.
	 */
	protected boolean checkEditor() {
		InputStream stream = EditorsManager.getInstance().getActiveEditorContentInputStream();
		if (stream == null) {
			MessageDialog.openInformation(getShell(), Messages.AbstractAnalysisUI_0, Messages.AbstractAnalysisUI_1);
			return false;
		}
		return true;
	}

	/**
	 * get the text from an opened editor
	 */
	protected String getEditorText() {
		String text = ""; //$NON-NLS-1$
		InputStream stream = EditorsManager.getInstance().getActiveEditorContentInputStream();
		text = InputStreamToString(stream);
		return text;
	}

	/**
	 * reads the current value from an input stream
	 *
	 * @param in the input stream
	 */
	protected String InputStreamToString(InputStream in) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, IConstants.UTF8_ENCODING));
		} catch (UnsupportedEncodingException e1) {
			LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e1);
		}

		StringBuffer myStrBuf = new StringBuffer();
		int charOut = 0;
		String output = ""; //$NON-NLS-1$
		try {
			while ((charOut = reader.read()) != -1) {
				myStrBuf.append(String.valueOf((char) charOut));
			}
		} catch (IOException e) {
			LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e);
		}
		output = myStrBuf.toString();
		return output;
	}

	protected void recalcGraph() {
		if (text == null) {
			if (checkEditor()) {
				text = getEditorText();
			} else {
				return;
			}
		}
		if (text != null) {
			setFinalVigParameters();
			analyze();
			myGraph.redraw();
		}
	}

	protected abstract void analyze();

	/**
	 * takes the input control's values and sets the final analysis parameters
	 */
	private void setFinalVigParameters() {
		myLength = 1;
		if (!button3.getSelection()) {
			myLength = spinner1.getSelection();
		}
		myOffset = 0;
		if (!button3.getSelection()) {
			myOffset = spinner2.getSelection();
		}
	}
}

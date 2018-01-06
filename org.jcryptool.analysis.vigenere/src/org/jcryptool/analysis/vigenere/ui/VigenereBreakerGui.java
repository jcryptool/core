/* *****************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorReference;
import org.jcryptool.analysis.vigenere.exceptions.IllegalInputException;
import org.jcryptool.analysis.vigenere.exceptions.NoContentException;
import org.jcryptool.analysis.vigenere.interfaces.DataProvider;
import org.jcryptool.analysis.vigenere.views.VigenereBreakerView;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;

public class VigenereBreakerGui extends ContentDelegator {
	protected static final String COURIER = "Courier New"; //$NON-NLS-1$
	private Content content;
	private String chiffre;
	private String edtitle;
	private int passlength;
	private String phrase;
	private VigenereBreakerView vigenereBreakerView;

	public VigenereBreakerGui(Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	@Override
	protected void backFrequency() {
		content.dispose();
		content = new FrequencyGui(this, chiffre, passlength, edtitle);
		((FrequencyGui) content).showCompletePass(phrase);
		this.layout(new Control[] { content });
	}

	@Override
	protected void backFrequency(String chiff, int length, String phrase) {
		passlength = length;
		content.dispose();
		content = new FrequencyGui(this, chiff, length, edtitle);
		((FrequencyGui) content).showCompletePass(phrase);
		this.layout(new Control[] { content });
	}

	@Override
	protected void backFriedman(final int length) {
		content.dispose();
		content = new FriedmanGui(this, chiffre, edtitle);
		((FriedmanGui) content).reset(length);
		this.layout(new Control[] { content });
	}

	@Override
	protected void backSummary() {
		content.dispose();
		content = new SummaryGui(this, SWT.NONE);
		this.layout(new Control[] { content });
	}

	private String filterChiffre(String chiff) {
		TransformData filter = TransformData.fromString(TransformData.BLANKS_LABEL);
		filter.setUnmodified();
		filter.setAlphabetTransformationON(true);
		return Transform.transformText(chiff, filter);
	}

	public VigenereBreakerView getVigenereBreakerView() {
		return vigenereBreakerView;
	}

	/**
	 * Initializes the first GUI window.
	 */
	private void initGUI() {
		this.setLayout(new GridLayout());
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		content = new SummaryGui(this, SWT.NONE);
		this.layout(new Control[] { content });
	}

	public void setView(VigenereBreakerView vigenereBreakerView) {
		this.vigenereBreakerView = vigenereBreakerView;
	}

	@Override
	protected void toDecrypt(final String chiff, final String plain, final String password, final int passlength) {
		phrase = password;
		content.dispose();
		content = new DecryptionGui(this, plain, chiffre, password, passlength, edtitle);
		this.layout(new Control[] { content });
	}

	@Override
	protected void toFrequency(int length) {
		passlength = length;
		content.dispose();
		content = new FrequencyGui(this, chiffre, length, edtitle);
		this.layout(new Control[] { content });
	}

	@Override
	protected void toFriedman(final IEditorReference selection) {
		try {
			chiffre = filterChiffre(DataProvider.getInstance().getEditorContent(selection));
			edtitle = selection.getTitle();
			content.dispose();
			content = new FriedmanGui(this, chiffre, selection);
			this.layout(new Control[] { content });
		} catch (IllegalInputException iiEx) {
			MessageBox box = new MessageBox(getShell(), SWT.ICON_ERROR);
			String message = Messages.VigenereBreakerGui_mbox_open;
			box.setText(Messages.VigenereGlobal_mbox_warning);
			box.setMessage(message);
			box.open();
		} catch (NoContentException noEx) {
			MessageBox box = new MessageBox(getShell(), SWT.ICON_INFORMATION);
			String message = Messages.VigenereBreakerGui_mbox_empty;
			box.setText(Messages.VigenereGlobal_mbox_info);
			box.setMessage(message);
			box.open();
		}
	}

	@Override
	protected void toQuick(final IEditorReference selection) {
		try {
			edtitle = selection.getTitle();
			chiffre = filterChiffre(DataProvider.getInstance().getEditorContent(selection));
			content.dispose();
			content = new QuickDecryptGui(this, chiffre, selection);
			this.layout(new Control[] { content });
		} catch (IllegalInputException iiEx) {
			MessageBox box = new MessageBox(getShell(), SWT.ICON_ERROR);
			String message = Messages.VigenereBreakerGui_mbox_open;
			box.setText(Messages.VigenereGlobal_mbox_warning);
			box.setMessage(message);
			box.open();
		} catch (NoContentException noEx) {
			MessageBox box = new MessageBox(getShell(), SWT.ICON_INFORMATION);
			String message = Messages.VigenereBreakerGui_mbox_empty;
			box.setText(Messages.VigenereGlobal_mbox_info);
			box.setMessage(message);
			box.open();
		}
	}
}

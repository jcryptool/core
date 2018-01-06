/* *****************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * ***************************************************************************
 */
package org.jcryptool.analysis.vigenere.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.vigenere.exceptions.IllegalActionException;
import org.jcryptool.analysis.vigenere.interfaces.DataProvider;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class DecryptionGui extends Content {
	private static String removeRecurrences(String password) {
		for (int i = 1; i <= Math.floor(password.length() / 2); i++) {
			String begin = password.substring(0, i);
			String tail = password.substring(i);
			while (tail.length() >= i) {
				if (tail.substring(0, i).equals(begin)) {
					tail = (tail.length() == i ? "" : tail.substring(i));
				} else {
					break;
				}
			}
			if (tail.length() == 0) {
				return begin;
			}
		}
		return password;
	}
	final private String pltext;
	final private String chtext;
	final private int length;
	final private String phrase;

	final private String edtitle;

	private Composite cselection;
	private Label labelOne;
	private Label labelTwo;
	private Label labelThree;
	private Label labelSeparator;
	private Label labelChiffre;

	private Label labelPlain;
	private Text textPlain;

	private Text textChiffre;
	private Button buttonBack;
	private Button buttonEnd;
	private Label labelLength;
	private Text textLength;
	private Group groupResult;
	private Label labelPassword;
	private Text textPassword;

	private Composite compositeButtons;

	/**
	 * Constructor for DecryptionGui.
	 * 
	 * @param parent
	 * @param plain
	 * @param chiffre
	 * @param password
	 * @param lenght
	 * @param title
	 */
	public DecryptionGui(final ContentDelegator parent, final String plain, final String chiffre, final String password,
			final int lenght, final String title) {
		super(parent, SWT.NONE);

		if (565 < chiffre.length()) {
			this.chtext = chiffre.substring(0, 564);
		} else {
			this.chtext = chiffre;
		}

		if (565 < plain.length()) {
			this.pltext = plain.substring(0, 564);
		} else {
			this.pltext = plain;
		}

		this.phrase = removeRecurrences(password);
		this.length = phrase.length();
		this.edtitle = title;

		initGUI();
	}

	private void back() {
		ContentDelegator del = (ContentDelegator) getParent();
		del.backFrequency();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		try {
			FontData coudat = new FontData(VigenereBreakerGui.COURIER, 10, SWT.NORMAL);
			Font coufont = new Font(getDisplay(), coudat);

			this.setLayout(new GridLayout());
			this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			cselection = new Composite(this, SWT.NONE);
			cselection.setLayout(new GridLayout(3, false));
			cselection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			labelOne = new Label(cselection, SWT.BORDER | SWT.CENTER);
			labelOne.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			labelOne.setText(Messages.VigenereGlobal_navi_friedman);
			labelOne.setFont(FontService.getLargeFont());
			labelOne.setForeground(ColorService.GRAY);

			labelTwo = new Label(cselection, SWT.BORDER | SWT.CENTER);
			labelTwo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			labelTwo.setText(Messages.VigenereGlobal_navi_frequency);
			labelTwo.setFont(FontService.getLargeFont());
			labelTwo.setForeground(ColorService.GRAY);

			labelThree = new Label(cselection, SWT.BORDER | SWT.CENTER);
			labelThree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			labelThree.setText(Messages.VigenereGlobal_navi_decryption);
			labelThree.setFont(FontService.getLargeFont());

			groupResult = new Group(this, SWT.NONE);
			groupResult.setLayout(new GridLayout(2, false));
			groupResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			groupResult.setText(Messages.DecryptionGui_group_results + " " + edtitle);

			labelLength = new Label(groupResult, SWT.NONE);
			labelLength.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));
			labelLength.setText(Messages.DecryptionGui_label_length);

			textLength = new Text(groupResult, SWT.BORDER);
			textLength.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			textLength.setEditable(false);
			textLength.setText(String.valueOf(length));

			labelPassword = new Label(groupResult, SWT.NONE);
			labelPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));
			labelPassword.setText(Messages.DecryptionGui_label_password);

			textPassword = new Text(groupResult, SWT.BORDER);
			textPassword.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			textPassword.setEditable(false);
			textPassword.setText(phrase);

			labelChiffre = new Label(this, SWT.NONE);
			labelChiffre.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			labelChiffre.setText(Messages.DecryptionGui_label_chiffre);

			textChiffre = new Text(this, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
			GridData gd_tchiffre = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd_tchiffre.widthHint = 600;
			gd_tchiffre.heightHint = 150;
			gd_tchiffre.minimumHeight = 150;
			textChiffre.setLayoutData(gd_tchiffre);
			textChiffre.setText(chtext);
			textChiffre.setEditable(false);
			textChiffre.setFont(coufont);

			labelPlain = new Label(this, SWT.NONE);
			labelPlain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			labelPlain.setText(Messages.DecryptionGui_label_plain);

			textPlain = new Text(this, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
			GridData gd_tplain = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd_tplain.widthHint = 600;
			gd_tplain.heightHint = 150;
			gd_tplain.minimumHeight = 150;
			textPlain.setLayoutData(gd_tplain);
			textPlain.setText(pltext);
			textPlain.setEditable(false);
			textPlain.setFont(coufont);

			labelSeparator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
			labelSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			compositeButtons = new Composite(this, SWT.NONE);
			compositeButtons.setLayout(new GridLayout(2, true));
			compositeButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			buttonBack = new Button(compositeButtons, SWT.PUSH);
			buttonBack.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
			buttonBack.setText(Messages.DecryptionGui_button_back);
			buttonBack.setToolTipText(Messages.DecryptionGui_ttip_back);
			buttonBack.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					back();
				}
			});

			buttonEnd = new Button(compositeButtons, SWT.PUSH);
			buttonEnd.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));
			buttonEnd.setText(Messages.DecryptionGui_button_finish);
			buttonEnd.setToolTipText(Messages.DecryptionGui_ttip_finish);
			buttonEnd.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					openEditor();
				}
			});
			this.layout();
		} catch (Exception ex) {
			LogUtil.logError(ex);
		}
	}

	private void openEditor() {
		try {
			DataProvider.getInstance().openEditor(pltext);
			buttonEnd.setEnabled(false);
		} catch (IllegalActionException iaEx) {
			// just in case. not my fault though.
			String message = Messages.DecryptionGui_mbox_open;
			MessageBox box = new MessageBox(getShell(), SWT.ICON_INFORMATION);
			box.setText(Messages.VigenereGlobal_mbox_info);
			box.setMessage(message);
			box.open();
		}
	}
}

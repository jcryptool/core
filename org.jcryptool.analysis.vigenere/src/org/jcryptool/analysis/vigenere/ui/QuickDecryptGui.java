/* *****************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * ***************************************************************************
 */
package org.jcryptool.analysis.vigenere.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
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
import org.eclipse.ui.IEditorReference;
import org.jcryptool.analysis.vigenere.exceptions.IllegalActionException;
import org.jcryptool.analysis.vigenere.exceptions.IllegalInputException;
import org.jcryptool.analysis.vigenere.exceptions.NoContentException;
import org.jcryptool.analysis.vigenere.interfaces.DataProvider;
import org.jcryptool.core.logging.utils.LogUtil;
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
public class QuickDecryptGui extends Content {
	private FriedmanContainer friedcon;
	private FrequencyContainer freqcon;
	private String chiffre;
	private int length;
	private String plain;
	private String phrase;
	final private String edtitle;

	private Button buttonFrequency;
	private Group groupResult;
	private Button buttonFriedman;

	private Composite cselection;

	private Label labelOne;
	private Label labelTwo;
	private Label labelThree;
	private Label lableSeperator;
	private Label lableChiffre;
	private Label labelLength;
	private Label labelPassword;
	private Text textPassword;
	private Text textlength;
	private Label lablePlain;

	private Text textPlain;
	private Text textChiffre;

	private Button buttonBack;
	private Button buttonEnd;
	private Composite buttonsComposite;
	private Composite textComposite;

	public QuickDecryptGui(final ContentDelegator parent, final String chiffre, final String title) {
		super(parent, SWT.NONE);
		this.edtitle = title;
		prepare(chiffre);
		initGUI();
	}

	public QuickDecryptGui(final ContentDelegator parent, final String chiffre, final IEditorReference edRef) {
		super(parent, SWT.NONE);
		this.edtitle = edRef.getTitle();
		prepare(chiffre);
		initGUI();
	}

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

	private void prepare(final String chiffre) {
		try {
			this.chiffre = chiffre;
			// get length and password.
			friedcon = new FriedmanContainer(this, chiffre);
			length = friedcon.findKeyLength(chiffre);
			freqcon = new FrequencyContainer(chiffre, length);
			phrase = removeRecurrences(freqcon.guessPass());
			length = phrase.length();
			// decrypt.
			plain = freqcon.decryptAll(phrase);

			if (565 < chiffre.length()) {
				this.chiffre = chiffre.substring(0, 564);
			}

			if (565 < plain.length()) {
				this.plain = plain.substring(0, 564);
			}
		} catch (NoContentException ncEx) {
			String message = Messages.FrequencyGui_mbox_missing;
			MessageBox box = new MessageBox(null, SWT.ICON_WARNING);
			box.setText(Messages.VigenereGlobal_mbox_warning);
			box.setMessage(message);
			box.open();
		} catch (IllegalInputException iiEx) {
			String message = Messages.FrequencyGui_mbox_incomplete;
			MessageBox box = new MessageBox(getShell(), SWT.ICON_INFORMATION);
			box.setText(Messages.VigenereGlobal_mbox_info);
			box.setMessage(message);
			box.open();
		}

	}

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
			labelOne.setCursor(new Cursor(getDisplay(), SWT.CURSOR_HAND));
			labelOne.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					showFriedman();
				}
			});

			labelTwo = new Label(cselection, SWT.BORDER | SWT.CENTER);
			labelTwo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			labelTwo.setText(Messages.VigenereGlobal_navi_frequency);
			labelTwo.setFont(FontService.getLargeFont());
			labelTwo.setCursor(new Cursor(getDisplay(), SWT.CURSOR_HAND));
			labelTwo.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					showFrequency();
				}
			});

			labelThree = new Label(cselection, SWT.BORDER | SWT.CENTER);
			labelThree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			labelThree.setText(Messages.VigenereGlobal_navi_decryption);
			labelThree.setFont(FontService.getLargeFont());

			groupResult = new Group(this, SWT.NONE);
			groupResult.setLayout(new GridLayout(3, false));
			groupResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			groupResult.setText(Messages.QuickDecrypGui_group_results + edtitle);

			labelLength = new Label(groupResult, SWT.NONE);
			labelLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			labelLength.setText(Messages.QuickDecrypGui_label_length);

			textlength = new Text(groupResult, SWT.BORDER);
			textlength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			textlength.setEditable(false);
			textlength.setText(String.valueOf(length));

			buttonFriedman = new Button(groupResult, SWT.PUSH);
			buttonFriedman.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
			buttonFriedman.setText(Messages.QuickDecrypGui_button_friedman);
			buttonFriedman.setToolTipText(Messages.QuickDecrypGui_ttip_friedman);
			buttonFriedman.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					showFriedman();
				}
			});

			labelPassword = new Label(groupResult, SWT.NONE);
			labelPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			labelPassword.setText(Messages.QuickDecrypGui_label_password);

			textPassword = new Text(groupResult, SWT.BORDER);
			textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			textPassword.setEditable(false);
			textPassword.setText(phrase);

			buttonFrequency = new Button(groupResult, SWT.PUSH);
			buttonFrequency.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			buttonFrequency.setText(Messages.QuickDecrypGui_button_frequency);
			buttonFrequency.setToolTipText(Messages.QuickDecrypGui_ttip_frequency);
			buttonFrequency.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					showFrequency();
				}
			});

			textComposite = new Composite(this, SWT.NONE);
			textComposite.setLayout(new GridLayout());
			textComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			// Label Geheimtext
			lableChiffre = new Label(textComposite, SWT.NONE);
			lableChiffre.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			lableChiffre.setText(Messages.QuickDecrypGui_label_chiffre);

			// Gehimtext Text
			textChiffre = new Text(textComposite, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
			GridData gd_tchiffre = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd_tchiffre.widthHint = 600;
			gd_tchiffre.heightHint = 150;
			gd_tchiffre.minimumHeight = 150;
			textChiffre.setLayoutData(gd_tchiffre);
			textChiffre.setEditable(false);
			textChiffre.setText(chiffre);
			textChiffre.setFont(coufont);

			lablePlain = new Label(textComposite, SWT.NONE);
			GridData gd_lplain = new GridData(SWT.FILL, SWT.FILL, true, false);
			gd_lplain.verticalIndent = 30;
			lablePlain.setLayoutData(gd_lplain);
			lablePlain.setText(Messages.QuickDecrypGui_label_plain);

			textPlain = new Text(textComposite, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
			GridData gd_tplain = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd_tplain.widthHint = 600;
			gd_tplain.heightHint = 150;
			gd_tplain.minimumHeight = 150;
			textPlain.setLayoutData(gd_tplain);
			textPlain.setEditable(false);
			textPlain.setText(plain);
			textPlain.setFont(coufont);

			lableSeperator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
			lableSeperator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			buttonsComposite = new Composite(this, SWT.NONE);
			buttonsComposite.setLayout(new GridLayout(2, true));
			buttonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			buttonBack = new Button(buttonsComposite, SWT.PUSH);
			buttonBack.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
			buttonBack.setText(Messages.QuickDecrypGui_button_back);
			buttonBack.setToolTipText(Messages.QuickDecrypGui_ttip_back);
			buttonBack.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					back();
				}
			});

			buttonEnd = new Button(buttonsComposite, SWT.PUSH | SWT.CENTER);
			buttonEnd.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));
			buttonEnd.setText(Messages.QuickDecrypGui_button_finish);
			buttonEnd.setToolTipText(Messages.QuickDecrypGui_ttip_finish);
			buttonEnd.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					openEditor();
				}
			});

			layout();
			pack();
		} catch (Exception ex) {
			LogUtil.logError(ex);
		}
	}

	private void openEditor() {
		try {
			DataProvider.getInstance().openEditor(plain);
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

	private void back() {
		ContentDelegator del = (ContentDelegator) getParent();
		del.backSummary();
	}

	private void showFriedman() {
		ContentDelegator del = (ContentDelegator) getParent();
		del.backFriedman(length);
	}

	private void showFrequency() {
		try {
			check(length);
			ContentDelegator del = (ContentDelegator) getParent();
			del.backFrequency(chiffre, length, phrase);
		} catch (IllegalInputException iiEx) {
			MessageBox box = new MessageBox(getShell(), SWT.ICON_INFORMATION);
			String message = Messages.FriedmanGui_mbox_tally;
			box.setText(Messages.VigenereGlobal_mbox_info);
			box.setMessage(message);
			box.open();
		}
	}

	private void check(final int length) throws IllegalInputException {
		if (PasswordContainer.TALLY < length) {
			throw new IllegalInputException("Only " //$NON-NLS-1$
					+ PasswordContainer.TALLY + " characters allowed!"); //$NON-NLS-1$
		}
	}
}

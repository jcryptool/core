/* *****************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors All rights reserved. This program and the accompanying materials
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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.vigenere.exceptions.IllegalInputException;
import org.jcryptool.analysis.vigenere.exceptions.NoContentException;
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
public class FrequencyGui extends Content {
	// graphical elements.
	private Composite cselection;
	Composite cgraph;
	private Composite cphrase;
	private Label lpreview;

	private Label lsone;
	private Label lstwo;
	private Label lsthree;

	private Text thelp;
	private Text tsample;

	private Button bshift;
	private Button bdecrypt;
	private Button boptions;
	private Button bnext;
	private Button bback;

	// container for frequency graph.
	private FrequencyContainer container;

	// chiffre text and length of pass phrase
	final private String chiffre;
	final private String edtitle;
	private int plength;
	private Label llength;
	private Composite mainComposite;
	private Label labelSeparator;
	private Composite buttonComposite;

	/**
	 * Constructor for the Frquency GUI.
	 * 
	 * @param parent
	 *            The parent composite in which the control should be displayed.
	 *            Parent composite must use GridLayout.
	 * @param chiffre
	 * @param passlength
	 * @param title
	 *            The title of the used Editor.
	 */
	public FrequencyGui(ContentDelegator parent, final String chiffre, final int passlength, final String title) {
		super(parent, SWT.NONE);
		this.chiffre = chiffre;
		this.edtitle = title;
		this.plength = passlength;
		initGUI();
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
			GridLayout gl_cselection = new GridLayout(3, false);
			gl_cselection.marginHeight = 0;
			gl_cselection.marginWidth = 0;
			cselection.setLayout(gl_cselection);
			cselection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			lsone = new Label(cselection, SWT.BORDER | SWT.CENTER);
			lsone.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			lsone.setText(Messages.VigenereGlobal_navi_friedman);
			lsone.setFont(FontService.getLargeFont());
			lsone.setForeground(ColorService.GRAY);

			lstwo = new Label(cselection, SWT.BORDER | SWT.CENTER);
			lstwo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			lstwo.setText(Messages.VigenereGlobal_navi_frequency);
			lstwo.setFont(FontService.getLargeFont());

			lsthree = new Label(cselection, SWT.BORDER | SWT.CENTER);
			lsthree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			lsthree.setText(Messages.VigenereGlobal_navi_decryption);
			lsthree.setFont(FontService.getLargeFont());
			lsthree.setForeground(ColorService.GRAY);

			mainComposite = new Composite(this, SWT.NONE);
			mainComposite.setLayout(new GridLayout(2, true));
			mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			lpreview = new Label(mainComposite, SWT.NONE);
			lpreview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
			lpreview.setText(Messages.FrequencyGui_label_preview1 + " " + edtitle);

			tsample = new Text(mainComposite, SWT.READ_ONLY | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
			GridData gd_tsample = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
			gd_tsample.widthHint = 600;
			gd_tsample.heightHint = 150;
			tsample.setLayoutData(gd_tsample);
			tsample.setFont(coufont);

			cgraph = new Composite(mainComposite, SWT.NONE);
			GridLayout gl_cgraph = new GridLayout();
			gl_cgraph.marginHeight = 0;
			gl_cgraph.marginWidth = 0;
			cgraph.setLayout(gl_cgraph);
			GridData gd_cgraph = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
			gd_cgraph.widthHint = 600;
			gd_cgraph.minimumHeight = 250;
			cgraph.setLayoutData(gd_cgraph);

			thelp = new Text(mainComposite, SWT.MULTI | SWT.WRAP);
			thelp.setText(String.format(Messages.FrequencyGui_text_help, plength) + Messages.FrequencyGui_0);
			thelp.setFont(FontService.getSmallFont());
			thelp.setEditable(false);
			GridData gd_thelp = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
			gd_thelp.widthHint = 300;
			thelp.setLayoutData(gd_thelp);

			llength = new Label(mainComposite, SWT.NONE);
			llength.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			llength.setText(Messages.FrequencyGui_label_length + " " + String.valueOf(plength));
			
			cphrase = new Composite(mainComposite, SWT.NONE);
			cphrase.setLayout(new RowLayout());
			cphrase.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			// Seperator
			labelSeparator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
			labelSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			buttonComposite = new Composite(this, SWT.NONE);
			buttonComposite.setLayout(new GridLayout(5, false));
			buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			bback = new Button(buttonComposite, SWT.PUSH);
			bback.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
			bback.setText(Messages.FrequencyGui_button_back);
			bback.setToolTipText(Messages.FrequencyGui_ttip_back);
			bback.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					back();
				}
			});

			bshift = new Button(buttonComposite, SWT.PUSH);
			bshift.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
			bshift.setText(Messages.FrequencyGui_button_shift);
			bshift.setToolTipText(Messages.FrequencyGui_ttip_shift);
			bshift.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					shift();
				}
			});

			bdecrypt = new Button(buttonComposite, SWT.PUSH);
			bdecrypt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
			bdecrypt.setText(Messages.FrequencyGui_button_decrypt);
			bdecrypt.setToolTipText(Messages.FrequencyGui_ttip_decrypt);
			bdecrypt.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					decrypt();
				}
			});

			boptions = new Button(buttonComposite, SWT.PUSH);
			boptions.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
			boptions.setText(Messages.FrequencyGui_button_options);
			boptions.setToolTipText(Messages.FrequencyGui_ttip_options);
			boptions.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					showOptions();
				}
			});

			bnext = new Button(buttonComposite, SWT.PUSH);
			bnext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
			bnext.setText(Messages.FrequencyGui_button_next);
			bnext.setToolTipText(Messages.FrequencyGui_ttip_next);
			bnext.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					next();
				}
			});

			prepare();
			this.layout();
		} catch (Exception ex) {
			LogUtil.logError(ex);
		}
	}

	private void shift() {
		try {
			int index = container.setCharacter();
			tsample.setText(container.decrypt(index));

			if (container.isFirst()) {
				lpreview.setText(Messages.FrequencyGui_label_preview2 + edtitle);
			}

			if (container.isDone()) {
				lpreview.setText(Messages.FrequencyGui_label_preview3 + edtitle);
			}
		} catch (IllegalInputException iiEx) {
			// can't be reached anymore. plug-in automatically highlights
			// an element. first character is default. exception is relict
			// from earlier implementation.
		}
	}

	private void prepare() {
		try {
			this.container = new FrequencyContainer(this.chiffre, this.plength);

			boolean a = container.isBlocked();
			String b = container.cutText(chiffre);
			String c = container.formatPreview(a, plength, b);

			this.tsample.setText(c);

			container.initGraph(cgraph);
			container.activateComparator(container.getReferenceText(),
					DataProvider.getInstance().getAlphabet(container.getAlphabetIdent()));
			container.enableButtons(cphrase);
			container.show();
			this.layout();
		} catch (NoContentException ncEx) {
			// not my fault. just in case.
			String message = Messages.FrequencyGui_mbox_missing;
			MessageBox box = new MessageBox(null, SWT.ICON_WARNING);
			box.setText(Messages.VigenereGlobal_mbox_info);
			box.setMessage(message);
			box.open();
		}
	}

	private void showOptions() {
		OptionsDialogGui option = new OptionsDialogGui(this);
		option.open();
	}

	protected void chancePreview(final boolean blocks, final String reftext, final String alphabet) {
		String sample = container.getSampleText();
		String s = container.formatPreview(blocks, plength, sample);

		this.tsample.setText(s);

		container.preview(blocks, reftext, alphabet);
	}

	protected void saveOptions(final boolean blocks, final String reftext, final String alphabet) {
		container.save(blocks, reftext, alphabet);
	}

	protected void cancelOperation() {
		String sample = container.getSampleText();
		String s = container.formatPreview(container.isBlocked(), plength, sample);

		tsample.setText(s);
		container.restore();
	}

	protected void setPreview(final String text) {
		this.tsample.setText(text);
	}

	protected boolean isBlocked() {
		return container.isBlocked();
	}

	public String getAlphabetIdent() {
		return container.getAlphabetIdent();
	}

	public String getRefTextIdent() {
		return container.getRefTextIdent();
	}

	private void next() {
		try {
			String pass = container.getPasswort();
			String plain = container.decryptAll();
			ContentDelegator del = (ContentDelegator) getParent();
			del.toDecrypt(chiffre, plain, pass, plength);
		} catch (IllegalInputException iiEx) {
			String message = String.format(Messages.FrequencyGui_mbox_incomplete, container.getFoundCharCount(),
					container.getPasswordLength());
			MessageBox box = new MessageBox(getShell(), SWT.ICON_INFORMATION);
			box.setText(Messages.VigenereGlobal_mbox_info);
			box.setMessage(message);
			box.open();
		}
	}

	private void back() {
		ContentDelegator del = (ContentDelegator) getParent();
		del.backFriedman(plength);
	}

	private void decrypt() {
		String guess = container.guessPass();
		PasswordDialogGui option = new PasswordDialogGui(this, guess);
		option.open();
	}

	protected void showCompletePass(final String in) {
		String plain = container.showCompletePass(in);
		tsample.setText(plain);
		lpreview.setText(Messages.FrequencyGui_label_preview3 + edtitle);
	}
}

/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorReference;
import org.jcryptool.analysis.vigenere.exceptions.IllegalInputException;
import org.jcryptool.analysis.vigenere.exceptions.NoContentException;
import org.jcryptool.analysis.vigenere.interfaces.DataProvider;
import org.jcryptool.analysis.vigenere.views.VigenereBreakerView;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;

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

    private void initGUI() {
        try {
            FormLayout thisLayout = new FormLayout();
            setLayout(thisLayout);
            setSize(800, 620);

            getParent().setFont(FontService.getNormalFont());

            content = new SummaryGui(this, SWT.NONE);

            this.layout();
        } catch (Exception ex) {
            LogUtil.logError(ex);
        }
    }

    @Override
    protected void toFriedman(final IEditorReference selection) {
        try {
            chiffre = DataProvider.getInstance().getEditorContent(selection);
            edtitle = selection.getTitle();
            content.dispose();
            content = new FriedmanGui(this, chiffre, selection);
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
    protected void toFrequency(int length) {
        passlength = length;
        content.dispose();
        content = new FrequencyGui(this, chiffre, length, edtitle);
    }

    @Override
    protected void toDecrypt(final String chiff, final String plain,
            final String password, final int passlength) {
        phrase = password;
        content.dispose();
        content = new DecryptionGui(this, plain, chiffre, password, passlength,
                edtitle);
    }

    @Override
    protected void toQuick(final IEditorReference selection) {
        try {
            edtitle = selection.getTitle();
            chiffre = DataProvider.getInstance().getEditorContent(selection);
            content.dispose();
            content = new QuickDecryptGui(this, chiffre, selection);
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
    protected void backSummary() {
        content.dispose();
        content = new SummaryGui(this, SWT.NONE);
        getVigenereBreakerView().scrollToTop();
    }

    @Override
    protected void backFriedman(final int length) {
        content.dispose();
        content = new FriedmanGui(this, chiffre, edtitle);
        ((FriedmanGui) content).reset(length);
        getVigenereBreakerView().scrollToTop();
    }

    @Override
    protected void backFrequency() {
        content.dispose();
        content = new FrequencyGui(this, chiffre, passlength, edtitle);
        ((FrequencyGui) content).showCompletePass(phrase);
        getVigenereBreakerView().scrollToTop();
    }

    @Override
    protected void backFrequency(String chiff, int length, String phrase) {
        passlength = length;
        content.dispose();
        content = new FrequencyGui(this, chiff, length, edtitle);
        ((FrequencyGui) content).showCompletePass(phrase);
        getVigenereBreakerView().scrollToTop();
    }

	public void setView(VigenereBreakerView vigenereBreakerView) {
		this.vigenereBreakerView = vigenereBreakerView;
	}

	public VigenereBreakerView getVigenereBreakerView() {
		return vigenereBreakerView;
	}
}

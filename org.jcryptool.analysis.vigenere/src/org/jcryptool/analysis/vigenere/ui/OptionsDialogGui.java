/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.analysis.vigenere.interfaces.DataProvider;

public class OptionsDialogGui extends Dialog {
    private FrequencyGui parent;

    private Combo creference;
    private Combo calphabet;

    private Button bblocks;

    public OptionsDialogGui(final FrequencyGui container) {
        super(container.getShell());
        this.parent = container;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.OptionsDialogGui_dialog_title);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        GridLayout parentLayout = new GridLayout();
        parentLayout.makeColumnsEqualWidth = true;
        parent.setLayout(parentLayout);
        Composite ccontain = new Composite(parent, SWT.NONE);
        GridLayout ccontainLayout = new GridLayout();
        ccontainLayout.makeColumnsEqualWidth = true;
        GridData ccontainLData = new GridData();
        ccontainLData.widthHint = 460;
        ccontainLData.heightHint = 205;
        ccontain.setLayoutData(ccontainLData);
        ccontain.setLayout(ccontainLayout);
        {
            Composite ccontent = new Composite(ccontain, SWT.NONE);
            FormLayout ccontentLayout = new FormLayout();
            GridData ccontentLData = new GridData();
            ccontentLData.widthHint = 450;
            ccontentLData.heightHint = 205;
            ccontent.setLayoutData(ccontentLData);
            ccontent.setLayout(ccontentLayout);
            {
                Button bapply = new Button(ccontent, SWT.PUSH | SWT.CENTER);
                FormData bapplyLData = new FormData();
                bapplyLData.left = new FormAttachment(0, 1000, 340);
                bapplyLData.top = new FormAttachment(0, 1000, 160);
                bapplyLData.width = 90;
                bapplyLData.height = 25;
                bapply.setLayoutData(bapplyLData);
                bapply.setText(Messages.OptionsDialogGui_button_preview);
                bapply.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent event) {
                        applyPressed();
                    }
                });
            }
            {
                Label sepup = new Label(ccontent, SWT.SEPARATOR
                        | SWT.HORIZONTAL);
                FormData sepobenLData = new FormData();
                sepobenLData.left = new FormAttachment(0, 1000, 20);
                sepobenLData.top = new FormAttachment(0, 1000, 43);
                sepobenLData.width = 410;
                sepobenLData.height = 10;
                sepup.setLayoutData(sepobenLData);
            }
            {
                calphabet = new Combo(ccontent, SWT.READ_ONLY);
                FormData calphabetLData = new FormData();
                calphabetLData.left = new FormAttachment(0, 1000, 200);
                calphabetLData.top = new FormAttachment(0, 1000, 105);
                calphabetLData.width = 204;
                calphabetLData.height = 23;
                calphabet.setLayoutData(calphabetLData);
                calphabet.setItems(DataProvider.getInstance().getAlphabets());
                calphabet.select(getIndexAlphabets());
            }
            {
                creference = new Combo(ccontent, SWT.READ_ONLY);
                FormData creferenceLData = new FormData();
                creferenceLData.left = new FormAttachment(0, 1000, 200);
                creferenceLData.top = new FormAttachment(0, 1000, 63);
                creferenceLData.width = 203;
                creferenceLData.height = 23;
                creference.setLayoutData(creferenceLData);
                creference.setItems(DataProvider.getInstance()
                        .listReferenceTexts());
                creference.select(getIndexReferences());
            }
            {
                Label lalphabet = new Label(ccontent, SWT.NONE);
                FormData lalphabetLData = new FormData();
                lalphabetLData.left = new FormAttachment(0, 1000, 20);
                lalphabetLData.top = new FormAttachment(0, 1000, 105);
                lalphabetLData.width = 160;
                lalphabetLData.height = 23;
                lalphabet.setLayoutData(lalphabetLData);
                lalphabet.setText(Messages.OptionsDialogGui_label_alphabet);
                lalphabet.setAlignment(SWT.RIGHT);
            }
            {
                Label lreference = new Label(ccontent, SWT.NONE);
                FormData lreferenceLData = new FormData();
                lreferenceLData.left = new FormAttachment(0, 1000, 20);
                lreferenceLData.top = new FormAttachment(0, 1000, 63);
                lreferenceLData.width = 160;
                lreferenceLData.height = 23;
                lreference.setLayoutData(lreferenceLData);
                lreference.setText(Messages.OptionsDialogGui_label_reference);
                lreference.setAlignment(SWT.RIGHT);
            }
            {
                bblocks = new Button(ccontent, SWT.CHECK | SWT.LEFT);
                FormData bblocksLData = new FormData();
                bblocksLData.left = new FormAttachment(0, 1000, 20);
                bblocksLData.top = new FormAttachment(0, 1000, 10);
                bblocksLData.width = 410;
                bblocksLData.height = 23;
                bblocks.setLayoutData(bblocksLData);
                bblocks.setText(Messages.OptionsDialogGui_label_blocks);
                bblocks.setSelection(this.parent.isBlocked());
            }
            {
                Label sepmid = new Label(ccontent, SWT.SEPARATOR
                        | SWT.HORIZONTAL);
                FormData sepmidLData = new FormData();
                sepmidLData.left = new FormAttachment(0, 1000, 20);
                sepmidLData.top = new FormAttachment(0, 1000, 143);
                sepmidLData.width = 410;
                sepmidLData.height = 10;
                sepmid.setLayoutData(sepmidLData);
            }
            {
                Label sepbut = new Label(ccontent, SWT.SEPARATOR
                        | SWT.HORIZONTAL);
                FormData sepbutLData = new FormData();
                sepbutLData.left = new FormAttachment(0, 1000, 20);
                sepbutLData.top = new FormAttachment(0, 1000, 196);
                sepbutLData.width = 410;
                sepbutLData.height = 10;
                sepbut.setLayoutData(sepbutLData);
            }
            {

            }
        }

        return ccontain;
    }

    @Override
    protected void okPressed() {
        boolean blocks = bblocks.getSelection();
        String alph = calphabet.getText();
        String reftxt = creference.getText();

        parent.chancePreview(blocks, reftxt, alph);
        parent.saveOptions(blocks, reftxt, alph);
        super.okPressed();
    }

    @Override
    protected void cancelPressed() {
        this.parent.cancelOperation();
        this.close();
        super.cancelPressed();
    }

    private void applyPressed() {
        boolean blocks = bblocks.getSelection();
        String alph = calphabet.getText();
        String reftxt = creference.getText();

        parent.chancePreview(blocks, reftxt, alph);
    }

    private int getIndexAlphabets() {
        String alph = parent.getAlphabetIdent();
        String[] alphs = DataProvider.getInstance().getAlphabets();

        for (int i = 0; i < alphs.length; i++) {
            if (alph.equals(alphs[i])) {
                return i;
            }
        }

        return 0;
    }

    private int getIndexReferences() {
        String ref = parent.getRefTextIdent();
        String[] refs = DataProvider.getInstance().listReferenceTexts();

        for (int i = 0; i < refs.length; i++) {
            if (ref.equals(refs[i])) {
                return i;
            }
        }

        return 0;
    }
}

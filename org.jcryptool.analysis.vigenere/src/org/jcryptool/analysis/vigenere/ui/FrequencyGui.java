/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * ***************************************************************************
 */
package org.jcryptool.analysis.vigenere.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.vigenere.exceptions.IllegalInputException;
import org.jcryptool.analysis.vigenere.exceptions.NoContentException;
import org.jcryptool.analysis.vigenere.interfaces.DataProvider;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation, company or business for any purpose whatever) then
 * you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of
 * Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO
 * JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class FrequencyGui extends Content {
    // graphical elements.
    private Composite cselection;
    private Composite cvariable;
    private Composite cgraph;
    private Composite cphrase;
    private Label lpreview;

    private Label lsone;
    private Label lstwo;
    private Label lsthree;
    private Label lsep;
    private Label lsepend;

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

    // private String passguess;

    public FrequencyGui(ContentDelegator parent, final String chiffre, final int passlength, final String title) {
        super(parent, SWT.NONE);
        this.chiffre = chiffre;
        this.edtitle = title;
        this.plength = passlength;
        initGUI();
    }

    private void initGUI() {
        try {
            FontData coudat = new FontData(VigenereBreakerGui.COURIER, 10, SWT.NORMAL);
            Font coufont = new Font(getDisplay(), coudat);

            FormLayout thisLayout = new FormLayout();
            this.setSize(780, 620);
            this.setLayout(thisLayout);
            {
                cselection = new Composite(this, SWT.NONE);
                FormLayout cselectionLayout = new FormLayout();
                FormData cselectionLData = new FormData();
                cselectionLData.left = new FormAttachment(0, 1000, 10);
                cselectionLData.top = new FormAttachment(0, 1000, 0);
                cselectionLData.width = 760;
                cselectionLData.height = 30;
                cselection.setLayoutData(cselectionLData);
                cselection.setLayout(cselectionLayout);
                {
                    lsep = new Label(cselection, SWT.SEPARATOR | SWT.HORIZONTAL);
                    FormData lsepLData = new FormData();
                    lsepLData.left = new FormAttachment(0, 1000, 0);
                    lsepLData.top = new FormAttachment(0, 1000, 26);
                    lsepLData.width = 760;
                    lsepLData.height = 5;
                    lsep.setLayoutData(lsepLData);
                }
                {
                    lsthree = new Label(cselection, SWT.BORDER | SWT.CENTER);
                    FormData lsthreeLData = new FormData();
                    lsthreeLData.left = new FormAttachment(0, 1000, 480);
                    lsthreeLData.top = new FormAttachment(0, 1000, 0);
                    lsthreeLData.width = 238;
                    lsthreeLData.height = 24;
                    lsthree.setLayoutData(lsthreeLData);
                    lsthree.setText(Messages.VigenereGlobal_navi_decryption);
                    lsthree.setFont(FontService.getLargeFont());
                    lsthree.setEnabled(false);
                }
                {
                    lstwo = new Label(cselection, SWT.BORDER | SWT.CENTER);
                    FormData lstwoLData = new FormData();
                    lstwoLData.left = new FormAttachment(0, 1000, 240);
                    lstwoLData.top = new FormAttachment(0, 1000, 0);
                    lstwoLData.width = 238;
                    lstwoLData.height = 24;
                    lstwo.setLayoutData(lstwoLData);
                    lstwo.setText(Messages.VigenereGlobal_navi_frequency);
                    lstwo.setFont(FontService.getLargeFont());
                }
                {
                    lsone = new Label(cselection, SWT.BORDER | SWT.CENTER);
                    FormData lsoneLData = new FormData();
                    lsoneLData.left = new FormAttachment(0, 1000, 0);
                    lsoneLData.top = new FormAttachment(0, 1000, 0);
                    lsoneLData.width = 238;
                    lsoneLData.height = 24;
                    lsone.setLayoutData(lsoneLData);
                    lsone.setText(Messages.VigenereGlobal_navi_friedman);
                    lsone.setFont(FontService.getLargeFont());
                    lsone.setEnabled(false);
                }
            }
            {
                cvariable = new Composite(this, SWT.NONE);
                FormLayout composite3Layout = new FormLayout();
                FormData composite3LData = new FormData();
                composite3LData.left = new FormAttachment(0, 1000, 10);
                composite3LData.top = new FormAttachment(0, 1000, 30);
                composite3LData.width = 760;
                composite3LData.height = 580;
                cvariable.setLayoutData(composite3LData);
                cvariable.setLayout(composite3Layout);
                {
                    lpreview = new Label(cvariable, SWT.NONE);
                    FormData lpreviewLData = new FormData();
                    lpreviewLData.left = new FormAttachment(0, 1000, 0);
                    lpreviewLData.top = new FormAttachment(0, 1000, 10);
                    lpreviewLData.width = 760;
                    lpreviewLData.height = 15;
                    lpreview.setLayoutData(lpreviewLData);
                    lpreview.setText(Messages.FrequencyGui_label_preview1 + edtitle);
                    lpreview.setFont(FontService.getNormalFont());
                }

                {
                    thelp = new Text(cvariable, SWT.MULTI | SWT.WRAP);
                    FormData tdescrLData = new FormData();
                    tdescrLData.left = new FormAttachment(0, 1000, 0);
                    tdescrLData.top = new FormAttachment(0, 1000, 380);
                    tdescrLData.width = 234;
                    // tdescrLData.height = 145;
                    thelp.setLayoutData(tdescrLData);
                    thelp.setText(String.format(Messages.FrequencyGui_text_help, plength)
                            + "\n\nDurch Klick auf \"Bestimmen\" wird ein Lösungsvorschlag automatisch errechnet.");
                    thelp.setEditable(false);
                    thelp.setEnabled(false);
                    thelp.setFont(FontService.getNormalFont());
                }
                {
                    lsepend = new Label(cvariable, SWT.SEPARATOR | SWT.HORIZONTAL);
                    FormData label1LData = new FormData();
                    label1LData.left = new FormAttachment(0, 1000, 0);
                    label1LData.top = new FormAttachment(thelp, 5);
                    label1LData.width = 760;
                    label1LData.height = 4;
                    lsepend.setLayoutData(label1LData);
                }
                {
                    llength = new Label(cvariable, SWT.NONE);
                    FormData llengthLData = new FormData();
                    llengthLData.left = new FormAttachment(0, 1000, 260);
                    llengthLData.top = new FormAttachment(0, 1000, 400);
                    llengthLData.width = 200;
                    llengthLData.height = 15;
                    llength.setLayoutData(llengthLData);
                    String l = Messages.FrequencyGui_label_length;
                    llength.setText(l + String.valueOf(plength));
                    llength.setFont(FontService.getNormalFont());
                }
                {
                    tsample = new Text(cvariable, SWT.MULTI | SWT.WRAP | SWT.BORDER);
                    FormData text1LData = new FormData();
                    text1LData.left = new FormAttachment(0, 1000, 0);
                    text1LData.top = new FormAttachment(0, 1000, 25);
                    text1LData.width = 752;
                    text1LData.height = 84;
                    tsample.setLayoutData(text1LData);
                    tsample.setFont(coufont);
                    tsample.setEditable(false);
                    tsample.setEnabled(false);
                    tsample.setBackground(new Color(getDisplay(), 250, 250, 250));
                }
                {
                    cgraph = new Composite(cvariable, SWT.NONE);
                    GridLayout composite4Layout = new GridLayout();
                    composite4Layout.makeColumnsEqualWidth = true;
                    FormData composite4LData = new FormData();
                    composite4LData.left = new FormAttachment(0, 1000, 0);
                    composite4LData.top = new FormAttachment(0, 1000, 120);
                    composite4LData.width = 760;
                    composite4LData.height = 260;
                    cgraph.setLayoutData(composite4LData);
                    cgraph.setLayout(composite4Layout);
                }
                {
                    cphrase = new Composite(cvariable, SWT.NONE);
                    FormLayout composite6Layout = new FormLayout();
                    FormData composite6LData = new FormData();
                    composite6LData.left = new FormAttachment(0, 1000, 250);
                    composite6LData.top = new FormAttachment(0, 1000, 422);
                    composite6LData.width = 510;
                    composite6LData.height = 100;
                    cphrase.setLayoutData(composite6LData);
                    cphrase.setLayout(composite6Layout);
                }
                {
                    bshift = new Button(cvariable, SWT.PUSH | SWT.CENTER);
                    FormData bfriedexecLData = new FormData();
                    bfriedexecLData.left = new FormAttachment(0, 1000, 260);
                    bfriedexecLData.top = new FormAttachment(lsepend, 5);
                    bfriedexecLData.height = 25;
                    bshift.setLayoutData(bfriedexecLData);
                    bshift.setText(Messages.FrequencyGui_button_shift);
                    bshift.setFont(FontService.getNormalFont());
                    bshift.setToolTipText(Messages.FrequencyGui_ttip_shift);
                    bshift.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent event) {
                            shift();
                        }
                    });
                }
                {
                    bdecrypt = new Button(cvariable, SWT.PUSH | SWT.CENTER);
                    FormData button1LData = new FormData();
                    button1LData.left = new FormAttachment(bshift, 5);
                    button1LData.top = new FormAttachment(lsepend, 5);
                    button1LData.height = 25;
                    bdecrypt.setLayoutData(button1LData);
                    bdecrypt.setText(Messages.FrequencyGui_button_decrypt);
                    bdecrypt.setFont(FontService.getNormalFont());
                    bdecrypt.setToolTipText(Messages.FrequencyGui_ttip_decrypt);
                    bdecrypt.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                            decrypt();
                        }
                    });
                }
                {
                    bback = new Button(cvariable, SWT.PUSH | SWT.CENTER);
                    FormData bbackLData = new FormData();
                    bbackLData.left = new FormAttachment(0, 1000, 0);
                    bbackLData.top = new FormAttachment(lsepend, 5);
                    bbackLData.height = 25;
                    bbackLData.width = 90;
                    bback.setLayoutData(bbackLData);
                    bback.setText(Messages.FrequencyGui_button_back);
                    bback.setFont(FontService.getNormalFont());
                    bback.setToolTipText(Messages.FrequencyGui_ttip_back);
                    bback.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent event) {
                            back();
                        }
                    });
                }
                {
                    bnext = new Button(cvariable, SWT.PUSH | SWT.CENTER);
                    FormData button1LData = new FormData();
                    button1LData.left = new FormAttachment(0, 1000, 670);
                    button1LData.top = new FormAttachment(lsepend, 5);
                    button1LData.height = 25;
                    button1LData.width = 90;
                    bnext.setLayoutData(button1LData);
                    bnext.setText(Messages.FrequencyGui_button_next);
                    bnext.setFont(FontService.getNormalFont());
                    bnext.setToolTipText(Messages.FrequencyGui_ttip_next);
                    bnext.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent event) {
                            next();
                        }
                    });
                }
                {
                    boptions = new Button(cvariable, SWT.PUSH | SWT.CENTER);
                    FormData boptionsLData = new FormData();
                    boptionsLData.left = new FormAttachment(0, 1000, 570);
                    boptionsLData.top = new FormAttachment(lsepend, 5);
                    boptionsLData.height = 25;
                    boptionsLData.width = 90;
                    boptions.setLayoutData(boptionsLData);
                    boptions.setText(Messages.FrequencyGui_button_options);
                    boptions.setFont(FontService.getNormalFont());
                    boptions.setToolTipText(Messages.FrequencyGui_ttip_options);
                    boptions.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent event) {
                            showOptions();
                        }
                    });
                }
            }

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

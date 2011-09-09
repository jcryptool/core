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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.vigenere.exceptions.IllegalActionException;
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
public class DecryptionGui extends Content {
    final private String pltext;
    final private String chtext;
    final private int length;
    final private String phrase;
    final private String edtitle;

    private Composite cselection;
    private Composite cvariable;

    private Label lsone;
    private Label lstwo;
    private Label lsthree;
    private Label lsep;
    private Label lsepend;
    private Label lchiffre;
    private Label lplain;

    private Text tplain;
    private Text tchiffre;

    private Button bback;
    private Button bend;
    private Label llength;
    private Text tlenth;
    private Group gresult;
    private Label lpass;
    private Text tpass;

    public DecryptionGui(final ContentDelegator parent, final String plain, final String chiffre,
            final String password, final int lenght, final String title) {
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
                    lstwo.setEnabled(false);
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
                FormLayout cvariableLayout = new FormLayout();
                FormData cvariableLData = new FormData();
                cvariableLData.left = new FormAttachment(0, 1000, 10);
                cvariableLData.top = new FormAttachment(0, 1000, 30);
                cvariableLData.width = 760;
                cvariableLData.height = 560;
                cvariable.setLayoutData(cvariableLData);
                cvariable.setLayout(cvariableLayout);
                {
                    bback = new Button(cvariable, SWT.PUSH | SWT.CENTER);
                    FormData button1LData = new FormData();
                    button1LData.left = new FormAttachment(0, 1000, 0);
                    button1LData.top = new FormAttachment(0, 1000, 435);
                    button1LData.width = 90;
                    button1LData.height = 25;
                    bback.setLayoutData(button1LData);
                    bback.setText(Messages.DecryptionGui_button_back);
                    bback.setFont(FontService.getNormalFont());
                    bback.setToolTipText(Messages.DecryptionGui_ttip_back);
                    bback.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                            back();
                        }
                    });
                }
                {
                    FormData lsependLData = new FormData();
                    lsependLData.left = new FormAttachment(0, 1000, 0);
                    lsependLData.top = new FormAttachment(0, 1000, 420);
                    lsependLData.width = 760;
                    lsependLData.height = 2;
                    lsepend = new Label(cvariable, SWT.SEPARATOR | SWT.HORIZONTAL);
                    lsepend.setLayoutData(lsependLData);
                }
                {
                    bend = new Button(cvariable, SWT.PUSH | SWT.CENTER);
                    FormData bendLData = new FormData();
                    bendLData.left = new FormAttachment(0, 1000, 670);
                    bendLData.top = new FormAttachment(0, 1000, 435);
                    bendLData.width = 90;
                    bendLData.height = 25;
                    bend.setLayoutData(bendLData);
                    bend.setText(Messages.DecryptionGui_button_finish);
                    bend.setFont(FontService.getNormalFont());
                    bend.setToolTipText(Messages.DecryptionGui_ttip_finish);
                    bend.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent event) {
                            openEditor();
                        }
                    });
                }
                {
                    lplain = new Label(cvariable, SWT.NONE);
                    FormData label2LData = new FormData();
                    label2LData.left = new FormAttachment(0, 1000, 0);
                    label2LData.top = new FormAttachment(0, 1000, 285);
                    label2LData.width = 100;
                    label2LData.height = 15;
                    lplain.setLayoutData(label2LData);
                    lplain.setText(Messages.DecryptionGui_label_plain);
                    lplain.setFont(FontService.getNormalFont());
                }
                {
                    lchiffre = new Label(cvariable, SWT.NONE);
                    FormData label1LData = new FormData();
                    label1LData.left = new FormAttachment(0, 1000, 0);
                    label1LData.top = new FormAttachment(0, 1000, 155);
                    label1LData.width = 100;
                    label1LData.height = 15;
                    lchiffre.setLayoutData(label1LData);
                    lchiffre.setText(Messages.DecryptionGui_label_chiffre);
                    lchiffre.setFont(FontService.getNormalFont());
                }
                {
                    tchiffre = new Text(cvariable, SWT.MULTI | SWT.WRAP | SWT.BORDER);
                    FormData text2LData = new FormData();
                    text2LData.left = new FormAttachment(0, 1000, 0);
                    text2LData.top = new FormAttachment(0, 1000, 170);
                    text2LData.width = 752;
                    text2LData.height = 99;
                    tchiffre.setLayoutData(text2LData);
                    tchiffre.setText(chtext);
                    tchiffre.setEnabled(false);
                    tchiffre.setEditable(false);
                    tchiffre.setFont(coufont);
                    tchiffre.setBackground(new Color(getDisplay(), 250, 250, 250));
                }
                {
                    tplain = new Text(cvariable, SWT.MULTI | SWT.WRAP | SWT.BORDER);
                    FormData text1LData = new FormData();
                    text1LData.left = new FormAttachment(0, 1000, 0);
                    text1LData.top = new FormAttachment(0, 1000, 300);
                    text1LData.width = 752;
                    text1LData.height = 99;
                    tplain.setLayoutData(text1LData);
                    tplain.setText(pltext);
                    tplain.setEditable(false);
                    tplain.setEnabled(false);
                    tplain.setFont(coufont);
                    tplain.setBackground(new Color(getDisplay(), 250, 250, 250));
                }
                {
                    gresult = new Group(cvariable, SWT.NONE);
                    FormLayout gresultLayout = new FormLayout();
                    gresult.setLayout(gresultLayout);
                    FormData gresultLData = new FormData();
                    gresultLData.left = new FormAttachment(0, 1000, 0);
                    gresultLData.top = new FormAttachment(0, 1000, 10);
                    gresultLData.width = 754;
                    gresultLData.height = 88;
                    gresult.setLayoutData(gresultLData);
                    gresult.setText(Messages.DecryptionGui_group_results + edtitle);
                    gresult.setFont(FontService.getNormalFont());
                    {
                        tpass = new Text(gresult, SWT.NONE | SWT.BORDER);
                        FormData tpassLData = new FormData();
                        tpassLData.left = new FormAttachment(0, 1000, 167);
                        tpassLData.top = new FormAttachment(0, 1000, 50);
                        tpassLData.width = 128;
                        tpassLData.height = 15;
                        tpass.setLayoutData(tpassLData);
                        tpass.setEditable(false);
                        tpass.setEnabled(false);
                        tpass.setText(phrase);
                        tpass.setFont(FontService.getNormalFont());
                    }
                    {
                        lpass = new Label(gresult, SWT.NONE);
                        FormData lpassLData = new FormData();
                        lpassLData.left = new FormAttachment(0, 1000, 17);
                        lpassLData.top = new FormAttachment(0, 1000, 53);
                        lpassLData.width = 120;
                        lpassLData.height = 15;
                        lpass.setLayoutData(lpassLData);
                        lpass.setText(Messages.DecryptionGui_label_password);
                        lpass.setFont(FontService.getNormalFont());
                        lpass.setAlignment(SWT.RIGHT);
                    }
                    {
                        tlenth = new Text(gresult, SWT.NONE | SWT.BORDER);
                        FormData tlenthLData = new FormData();
                        tlenthLData.left = new FormAttachment(0, 1000, 167);
                        tlenthLData.top = new FormAttachment(0, 1000, 15);
                        tlenthLData.width = 128;
                        tlenthLData.height = 15;
                        tlenth.setLayoutData(tlenthLData);
                        tlenth.setEditable(false);
                        tlenth.setEnabled(false);
                        tlenth.setFont(FontService.getNormalFont());
                        tlenth.setText(String.valueOf(length));
                    }
                    {
                        llength = new Label(gresult, SWT.NONE);
                        FormData llengthLData = new FormData();
                        llengthLData.left = new FormAttachment(0, 1000, 17);
                        llengthLData.top = new FormAttachment(0, 1000, 18);
                        llengthLData.width = 120;
                        llengthLData.height = 15;
                        llength.setLayoutData(llengthLData);
                        llength.setText(Messages.DecryptionGui_label_length);
                        llength.setFont(FontService.getNormalFont());
                        llength.setAlignment(SWT.RIGHT);
                    }
                }
            }
            this.layout();
        } catch (Exception ex) {
            LogUtil.logError(ex);
        }
    }

    private void openEditor() {
        try {
            DataProvider.getInstance().openEditor(pltext);
            bend.setEnabled(false);
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
        del.backFrequency();
    }
}

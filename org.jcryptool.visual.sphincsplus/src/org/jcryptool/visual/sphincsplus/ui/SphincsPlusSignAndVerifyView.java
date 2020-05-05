// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sphincsplus.ui;

import java.util.Base64;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.visual.sphincsplus.EnvironmentParameters;
import org.jcryptool.visual.sphincsplus.SphincsPlus;
import org.jcryptool.visual.sphincsplus.algorithm.Utils;
import org.jcryptool.visual.sphincsplus.interfaces.ISphincsPlus;

/**
 * 
 * @author Sebastian Ranftl
 *
 */
public class SphincsPlusSignAndVerifyView extends Composite {

    private Composite messageComposite;
    private Composite signatureComposite;
	private Composite characterEncodingComposite;
	private Composite valueComposite;

    private GridLayout messageCompositeLayout;
    private GridLayout signatureCompositeLayout;

    private GridData text_inputMessageLayout;
    private GridData text_info_inputMessageLayout;

    public static Group messageGroup;
    private Group infoGroup;
    private Group signButtonGroup;
    private Group valueGroup;
    private Group descriptionGroup;

    public static Text text_inputMessage;
    public static Text text_info_inputMessage;
    private Text text_r;
    private Text text_fors;
    private Text text_ht;
    private Text lbl_status;
    private Text label_r;
    private Text label_fors;
    private Text label_ht;
    private Text text_CharacterEncoding;
    private StyledText text_sig_description;

    private Button btn_verify;
    private Button btn_sign;
    private Button btn_base64;
    private Button btn_hex;

    public static boolean message_signed = false;
    private static boolean signature_hex_selected = false;
    private static boolean verified = false;

    private String randomize_hex = new String();
    private String randomize_base = new String();
    private String ht_hex = new String();
    private String ht_base = new String();
    private String fors_hex = new String();
    private String fors_base = new String();

    private ISphincsPlus sphincs = new SphincsPlus();

    private byte[] sig;
    private byte[] message;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public SphincsPlusSignAndVerifyView(Composite parent, final int style, SphincsPlusView sphincsPlusView) {
        super(parent, style);

        setLayout(new GridLayout());

        createBody();
    }

    private void createBody() {
        final Composite bodyComposite = new Composite(this, SWT.NONE);
        bodyComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout gl_bodyComposite = new GridLayout();
        gl_bodyComposite.marginWidth = 0;
        gl_bodyComposite.marginHeight = 0;
        bodyComposite.setLayout(gl_bodyComposite);

        createMessageBody(bodyComposite);
        createSignatureBody(bodyComposite);
    }

    private void createMessageBody(Composite parent) {
        messageComposite = new Composite(parent, SWT.NONE);
        messageComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        messageCompositeLayout = new GridLayout(2, true);
        messageCompositeLayout.marginWidth = 0;
        messageCompositeLayout.marginHeight = 0;
        messageComposite.setLayout(messageCompositeLayout);
        messageComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        createMessageGroup(messageComposite);
        createInfoGroup(messageComposite);

    }

    private void createMessageGroup(Composite parent) {

        messageGroup = new Group(parent, SWT.NONE);
        messageGroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        messageGroup.setLayout(new GridLayout());
        messageGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        text_inputMessage = new Text(messageGroup, SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI | SWT.WRAP);
        text_inputMessageLayout = new GridData(SWT.FILL, SWT.TOP, true, true);
        text_inputMessageLayout.minimumHeight = 120;
        text_inputMessageLayout.heightHint = 120;
        text_inputMessageLayout.minimumWidth = 125;
        text_inputMessageLayout.widthHint = 125;
        text_inputMessage.setLayoutData(text_inputMessageLayout);
        text_inputMessage.setText(Messages.SphincsPlusSignAndVerifyView_text_inputMessage);

        messageGroup.setText(
                Messages.SphincsPlusSignAndVerifyView_messageGroup_part_1 + text_inputMessage.getText().length() + "; "
                        + Messages.SphincsPlusSignAndVerifyView_messageGroup_part_2
                        + SphincsPlusParameterView.min_message_length + ")");

        // Disables the sign button if no message is given in the text field
        text_inputMessage.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {

                messageGroup.setText(
                        Messages.SphincsPlusSignAndVerifyView_messageGroup_part_1 + text_inputMessage.getText().length()
                                + "; " + Messages.SphincsPlusSignAndVerifyView_messageGroup_part_2
                                + SphincsPlusParameterView.min_message_length + ")");

                lbl_status.setBackground(ColorConstants.orange);
                lbl_status.setText(Messages.SphincsPlusSignAndVerifyView_lbl_status_text_changed);

                if (text_inputMessage.getText().length() >= SphincsPlusParameterView.min_message_length) {
                    btn_sign.setEnabled(true);

                } else {

                    btn_sign.setEnabled(false);
                }

            }
        });
    }

    private void createInfoGroup(Composite parent) {

        infoGroup = new Group(parent, SWT.NONE);
        infoGroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        infoGroup.setLayout(new GridLayout());
        infoGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        infoGroup.setText(Messages.SphincsPlusSignAndVerifyView_infoGroup);

        text_info_inputMessage = new Text(infoGroup, SWT.V_SCROLL | SWT.READ_ONLY | SWT.WRAP);
        text_info_inputMessage.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        text_info_inputMessageLayout = new GridData(SWT.FILL, SWT.FILL, true, true);
        text_info_inputMessageLayout.minimumHeight = 120;
        text_info_inputMessageLayout.heightHint = 120;
        text_info_inputMessageLayout.widthHint = 125;
        text_info_inputMessageLayout.minimumWidth = 125;
        text_info_inputMessage.setLayoutData(text_info_inputMessageLayout);
        text_info_inputMessage.setText(Messages.SphincsPlusSignAndVerifyView_text_info_inputMessage_part_1
                + SphincsPlusParameterView.min_message_length
                + Messages.SphincsPlusSignAndVerifyView_text_info_inputMessage_part_2);
    }

    private void createSignatureBody(Composite parent) {
        signatureComposite = new Composite(parent, SWT.NONE);
        signatureComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        signatureCompositeLayout = new GridLayout();
        signatureCompositeLayout.marginWidth = 0;
        signatureCompositeLayout.marginHeight = 0;
        signatureComposite.setLayout(signatureCompositeLayout);
        signatureComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createSignButtonGroup(signatureComposite);
        createDescriptionGroup(signatureComposite);
        createValueGroup(signatureComposite);
    }

    private void createSignButtonGroup(Composite parent) {
        signButtonGroup = new Group(parent, SWT.NONE);
        signButtonGroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        signButtonGroup.setLayout(new GridLayout(3, true));
        signButtonGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        signButtonGroup.setText(Messages.SphincsPlusSignAndVerifyView_signButtonGroup);

        btn_sign = new Button(signButtonGroup, SWT.NONE);
        btn_sign.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        btn_sign.setText(Messages.SphincsPlusSignAndVerifyView_btn_sign);

        btn_verify = new Button(signButtonGroup, SWT.NONE);
        btn_verify.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        btn_verify.setText(Messages.SphincsPlusSignAndVerifyView_btn_verify);

        lbl_status = new Text(signButtonGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW | SWT.CENTER);
        lbl_status.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        lbl_status.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

        btn_sign.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                message = (text_inputMessage.getText()).getBytes();

                if (SphincsPlusParameterView.key_generate_pressed) {

                    message_signed = true;

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            sig = sphincs.spx_sign(message);
                        }
                    };

                    new Thread(runnable).start();
                    BusyIndicator.showWhile(Display.getCurrent(), runnable);

                    int n = EnvironmentParameters.n;
                    int k = EnvironmentParameters.k;
                    int a = EnvironmentParameters.a;

                    byte[] r = new byte[n];
                    byte[] sig_fors_bytes = new byte[k * (a + 1) * n];
                    byte[] sig_ht_bytes = new byte[sig.length - r.length - sig_fors_bytes.length];

                    System.arraycopy(sig, 0, r, 0, n);
                    System.arraycopy(sig, r.length, sig_fors_bytes, 0, sig_fors_bytes.length);
                    System.arraycopy(sig, r.length + sig_fors_bytes.length, sig_ht_bytes, 0, sig_ht_bytes.length);

                    randomize_hex = Utils.bytesToHex(r);
                    ht_hex = Utils.bytesToHex(sig_ht_bytes);
                    fors_hex = Utils.bytesToHex(sig_fors_bytes);
                    randomize_base = Base64.getEncoder().encodeToString(r);
                    ht_base = Base64.getEncoder().encodeToString(sig_ht_bytes);
                    fors_base = Base64.getEncoder().encodeToString(sig_fors_bytes);

                    if (signature_hex_selected) {

                        text_r.setText(randomize_hex);
                        text_fors.setText(fors_hex);
                        text_ht.setText(ht_hex);

                    } else {

                        text_r.setText(randomize_base);
                        text_fors.setText(fors_base);
                        text_ht.setText(ht_base);

                    }

                    lbl_status.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
                    lbl_status.setText(Messages.SphincsPlusSignAndVerifyView_lbl_status_signed);

                    label_r.setText("R" + " (" + r.length + " bytes)");
                    label_fors.setText("SIG FORS" + " (" + sig_fors_bytes.length + " bytes)");
                    label_ht.setText("SIG HT" + " (" + sig_ht_bytes.length + " bytes)");

                } else {

                    MessageBox messageBoxx = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                    messageBoxx.setMessage(Messages.SphincsPlusSignAndVerifyView_no_key_message);
                    messageBoxx.setText(Messages.SphincsPlusSignAndVerifyView_no_key_info);
                    messageBoxx.open();
                }

            }
        });

        btn_verify.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                if (message_signed == true) {

                    message = (text_inputMessage.getText()).getBytes();

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            verified = sphincs.spx_verify(message, sig);
                        }
                    };

                    new Thread(runnable).start();
                    BusyIndicator.showWhile(Display.getCurrent(), runnable);

                    if (verified) {

                        lbl_status.setBackground(ColorConstants.lightGreen);
                        lbl_status.setText(Messages.SphincsPlusSignAndVerifyView_lbl_status_verified);

                    } else {

                        lbl_status.setBackground(ColorConstants.orange);
                        lbl_status.setText(Messages.SphincsPlusSignAndVerifyView_lbl_status_verification_failed);
                    }

                } else {

                    MessageBox messageBoxx = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                    messageBoxx.setMessage(Messages.SphincsPlusSignAndVerifyView_sign_first_message);
                    messageBoxx.setText(Messages.SphincsPlusSignAndVerifyView_sign_first_info);
                    messageBoxx.open();
                }
            }
        });
    }

    private void createDescriptionGroup(Composite parent) {
        descriptionGroup = new Group(parent, SWT.NONE);
        descriptionGroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        descriptionGroup.setLayout(new GridLayout(1, true));
        GridData gd_descriptionGroup = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_descriptionGroup.minimumWidth = 250;
        gd_descriptionGroup.widthHint = 250;
        descriptionGroup.setLayoutData(gd_descriptionGroup);
        descriptionGroup.setText(Messages.SphincsPlusSignAndVerifyView_descriptionGroup);

        text_sig_description = new StyledText(descriptionGroup, SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        GridData text_data = new GridData(SWT.FILL, SWT.FILL, true, true);
        text_data.heightHint = 180;
        text_sig_description.setText(Messages.SphincsPlusSignAndVerifyView_text_sig_description);
        text_sig_description.setLayoutData(text_data);

    }
    


    private void createValueGroup(Composite parent) {
        valueGroup = new Group(parent, SWT.NONE);
        valueGroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        GridLayout gl_valueGroup = new GridLayout(1, false);
        gl_valueGroup.marginWidth = 0;
        gl_valueGroup.marginHeight = 0;
        valueGroup.setLayout(gl_valueGroup);
        valueGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        valueGroup.setText(Messages.SphincsPlusSignAndVerifyView_valueGroup);
        
        characterEncodingComposite = new Composite(valueGroup, SWT.NONE);
        characterEncodingComposite.setLayout(new GridLayout(3, false));
        characterEncodingComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        characterEncodingComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        
        text_CharacterEncoding = new Text(characterEncodingComposite, SWT.NONE);
        text_CharacterEncoding.setText(Messages.SphincsPlusSignAndVerifyView_encodingButtonGroup);
        text_CharacterEncoding.setEditable(false);
        text_CharacterEncoding.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        
        btn_hex = new Button(characterEncodingComposite, SWT.RADIO);
        btn_hex.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
        btn_hex.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        btn_hex.setText("Hex");

        btn_base64 = new Button(characterEncodingComposite, SWT.RADIO);
        btn_base64.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
        btn_base64.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        btn_base64.setText("Base64");

        // Set default selection
        btn_hex.setSelection(true);
        signature_hex_selected = true;

        // If button selection changes
        btn_base64.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                Button source = (Button) e.getSource();

                if (source.getSelection()) {
                    signature_hex_selected = false;
                    text_r.setText(randomize_base);
                    text_fors.setText(fors_base);
                    text_ht.setText(ht_base);
                }
            }
        });

        btn_hex.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                Button source = (Button) e.getSource();

                if (source.getSelection()) {
                    signature_hex_selected = true;
                    text_r.setText(randomize_hex);
                    text_fors.setText(fors_hex);
                    text_ht.setText(ht_hex);
                }
            }
        });
        
        valueComposite = new Composite(valueGroup, SWT.NONE);
        valueComposite.setLayout(new GridLayout(2, false));
        valueComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        valueComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

        label_r = new Text(valueComposite, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        GridData gd_label_r = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd_label_r.minimumWidth = 200;
        gd_label_r.widthHint = 200;
        label_r.setLayoutData(gd_label_r);
        label_r.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_r.setText("R (0 bytes)");

        text_r = new Text(valueComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        GridData gd_text_r = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_text_r.minimumHeight = 50;
        gd_text_r.heightHint = 50;
        text_r.setLayoutData(gd_text_r);

        label_fors = new Text(valueComposite, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        GridData gd_label_fors = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd_label_fors.minimumWidth = 200;
        gd_label_fors.widthHint = 200;
        label_fors.setLayoutData(gd_label_fors);
        label_fors.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_fors.setText("SIG FORS (0 bytes)");

        text_fors = new Text(valueComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        GridData gd_text_fors = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd_text_fors.minimumHeight = 50;
        gd_text_fors.heightHint = 50;
        text_fors.setLayoutData(gd_text_fors);

        label_ht = new Text(valueComposite, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        GridData gd_label_ht = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd_label_ht.minimumWidth = 200;
        gd_label_ht.widthHint = 200;
        label_ht.setLayoutData(gd_label_ht);
        label_ht.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_ht.setText("SIG HT (0 bytes)");

        text_ht = new Text(valueComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        GridData gd_text_ht = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd_text_ht.minimumHeight = 50;
        gd_text_ht.heightHint = 50;
        text_ht.setLayoutData(gd_text_ht);

    }
}

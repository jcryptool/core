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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.visual.sphincsplus.EnvironmentParameters;
import org.jcryptool.visual.sphincsplus.HashFunctionType;
import org.jcryptool.visual.sphincsplus.SphincsPlus;
import org.jcryptool.visual.sphincsplus.algorithm.Hypertree;
import org.jcryptool.visual.sphincsplus.algorithm.Key;
import org.jcryptool.visual.sphincsplus.algorithm.Utils;
import org.jcryptool.visual.sphincsplus.interfaces.ISphincsPlus;

/**
 * 
 * @author Sebastian Ranftl
 *
 */
public class SphincsPlusParameterView extends Composite {

    private Composite parameterComposite;
    private Composite keysComposite;

    private GridLayout parameterCompositeLayout;
    private GridLayout keysCompositeLayout;

    private Group parameterGroup;
    private Group descriptionGroup;
    private Group KeyGroup;
    // private Group publicKeyGroup;

    private Text label_security_level;
    private Text label_n;
    private Text label_h;
    private Text label_d;
    private Text label_log_t;
    private Text label_k;
    private Text label_w;
    private Text label_bitsec;
    private Text label_sec_level;
    private Text label_sig_bytes;
    private Text label_SKseed;
    private Text label_SKprf;
    private Text label_PKseed;
    private Text label_PKroot;
    // private Text label_pk_PKseed;
    // private Text label_pk_PKroot;
    private Text headLabel;
    private Text label_randomize;
    private StyledText text_description;
    private Text text_SKseed;
    private Text text_PKseed;
    private Text text_SKprf;
    private Text text_PKroot;
    private Text headDescription;
    private Text text_n;
    private Text text_h;
    private Text text_d;
    private Text text_log_t;
    private Text text_k;
    private Text text_w;
    private Text text_bitsec;
    private Text text_sec_level;
    private Text text_sig_bytes;
    private Text label_sk_SKseed_char_count;
    private Text label_sk_SKprf_char_count;
    private Text label_sk_PKseed_char_count;
    private Text label_sk_PKroot_char_count;
    private Text label_characters;

    private Button btn_generateKeys;
    private Button btnCheckButton_parameter;
    private Button btnCheckButton_keys;
    private Button btn_hex;
    private Button btn_base64;

    private Combo combo_parameters;
    private Combo combo_hashfunction;
    private Combo combo_randomize;

    private final int[] sphincs_128s = { 16, 64, 8, 15, 10, 16, 133, 1, 8080 };
    private final int[] sphincs_128f = { 16, 60, 20, 9, 30, 16, 128, 1, 16976 };
    private final int[] sphincs_192s = { 24, 64, 8, 16, 14, 16, 196, 3, 17064 };
    private final int[] sphincs_192f = { 24, 66, 22, 8, 33, 16, 194, 3, 35664 };
    private final int[] sphincs_256s = { 32, 64, 8, 14, 22, 16, 255, 5, 29792 };
    private final int[] sphincs_256f = { 32, 68, 17, 10, 30, 16, 254, 5, 49216 };

    private int[] mode = new int[9];
    private int key_size_chars;
    public static int min_message_length;

    public static boolean key_generate_pressed = false;
    public static boolean keys_check_box_selected = false;
    public static boolean key_hex_selected = false;

    private String sk_seed_hex = new String();
    private String pk_seed_hex = new String();
    private String sk_seed_base = new String();
    private String pk_seed_base = new String();
    private String sk_prf_hex = new String();
    private String sk_prf_base = new String();
    private String pk_root_hex = new String();
    private String pk_root_base = new String();

    private ISphincsPlus spx = new SphincsPlus();
    private Key key = Key.getInstance();

    VerifyListener keys_inputValidationListener;
    VerifyListener param_inputValidationListener;
    ModifyListener keys_changedListener;

    /**
     * Creates the main composite.
     * 
     * @param parent
     * @param style
     */
    public SphincsPlusParameterView(final Composite parent, final int style, SphincsPlusView sphincsPlusView) {
        super(parent, style);

        setLayout(new GridLayout());

        createHead();
        createBody();
    }

    /**
     * Creates the Head of the Tab, including Title and description.
     */
    private void createHead() {
    	TitleAndDescriptionComposite tadComposite = new TitleAndDescriptionComposite(this);
    	tadComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    	tadComposite.setTitle(Messages.SphincsPlusParameterView_headLabel);
    	tadComposite.setDescription(Messages.SphincsPlusParameterView_headDescription);
    }

    /**
     * Creates the Body, containing Parameter and Key composites.
     */
    private void createBody() {
        final Composite bodyComposite = new Composite(this, SWT.NONE);
        bodyComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout gl_bodyComposite = new GridLayout();
        gl_bodyComposite.marginWidth = 0;
        gl_bodyComposite.marginHeight = 0;
        bodyComposite.setLayout(gl_bodyComposite);

        createParameterBody(bodyComposite);
        createKeyBody(bodyComposite);
    }

    /**
     * Creates the parameter body, including parameter and description group.
     */
    private void createParameterBody(Composite parent) {
        parameterComposite = new Composite(parent, SWT.NONE);
        parameterComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        parameterCompositeLayout = new GridLayout(2, false);
        parameterCompositeLayout.marginWidth = 0;
        parameterCompositeLayout.marginHeight = 0;
        parameterComposite.setLayout(parameterCompositeLayout);
        parameterComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createParameterGroup(parameterComposite);
        createParameterDescriptionGroup(parameterComposite);

    }

    /**
     * Creates the parameter group with all labels and buttons.
     * 
     * @param parent
     */
    private void createParameterGroup(Composite parent) {
        parameterGroup = new Group(parent, SWT.NONE);
        parameterGroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        parameterGroup.setLayout(new GridLayout(3, false));
        GridData gd_parameterGroup = new GridData(SWT.FILL, SWT.FILL, false, true);
        parameterGroup.setLayoutData(gd_parameterGroup);
        parameterGroup.setText(Messages.SphincsPlusParameterView_parameterGroup);

        label_security_level = new Text(parameterGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_security_level.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_security_level.setText(Messages.SphincsPlusParameterView_label_security_level);

        combo_parameters = new Combo(parameterGroup, SWT.BORDER | SWT.READ_ONLY);
        combo_parameters.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        combo_parameters.add("SPHINCS+-128s");
        combo_parameters.add("SPHINCS+-128f");
        combo_parameters.add("SPHINCS+-192s");
        combo_parameters.add("SPHINCS+-192f");
        combo_parameters.add("SPHINCS+-256s");
        combo_parameters.add("SPHINCS+-256f");

        combo_hashfunction = new Combo(parameterGroup, SWT.BORDER | SWT.READ_ONLY);
        combo_hashfunction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        combo_hashfunction.add("SHA-256");
        combo_hashfunction.add("SHAKE256");

        label_n = new Text(parameterGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_n.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_n.setText("n");

        text_n = new Text(parameterGroup, SWT.BORDER | SWT.READ_ONLY);
        text_n.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        text_n.setText(String.valueOf(sphincs_128s[0]));

        label_h = new Text(parameterGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_h.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_h.setText("h");

        text_h = new Text(parameterGroup, SWT.BORDER | SWT.READ_ONLY);
        text_h.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        text_h.setText(String.valueOf(sphincs_128s[1]));

        label_d = new Text(parameterGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_d.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_d.setText("d");

        text_d = new Text(parameterGroup, SWT.BORDER | SWT.READ_ONLY);
        text_d.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        text_d.setText(String.valueOf(sphincs_128s[2]));

        label_log_t = new Text(parameterGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_log_t.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_log_t.setText("log(t)");

        text_log_t = new Text(parameterGroup, SWT.BORDER | SWT.READ_ONLY);
        text_log_t.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        text_log_t.setText(String.valueOf(sphincs_128s[3]));

        label_k = new Text(parameterGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_k.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_k.setText("k");

        text_k = new Text(parameterGroup, SWT.BORDER | SWT.READ_ONLY);
        text_k.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        text_k.setText(String.valueOf(sphincs_128s[4]));

        label_w = new Text(parameterGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_w.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_w.setText("w");

        text_w = new Text(parameterGroup, SWT.BORDER | SWT.READ_ONLY);
        text_w.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        text_w.setText(String.valueOf(sphincs_128s[5]));

        label_bitsec = new Text(parameterGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_bitsec.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_bitsec.setText("bitsec");

        text_bitsec = new Text(parameterGroup, SWT.BORDER | SWT.READ_ONLY);
        text_bitsec.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        text_bitsec.setText(String.valueOf(sphincs_128s[6]));

        label_sec_level = new Text(parameterGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_sec_level.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_sec_level.setText("sec level");

        text_sec_level = new Text(parameterGroup, SWT.BORDER | SWT.READ_ONLY);
        text_sec_level.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        text_sec_level.setText(String.valueOf(sphincs_128s[7]));

        label_sig_bytes = new Text(parameterGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_sig_bytes.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_sig_bytes.setText("sig length");

        text_sig_bytes = new Text(parameterGroup, SWT.BORDER | SWT.READ_ONLY);
        text_sig_bytes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        text_sig_bytes.setText(String.valueOf(sphincs_128s[8]));

        label_randomize = new Text(parameterGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_randomize.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        GridData gd_label_randomize = new GridData();
        label_randomize.setText("RANDOMIZE");
        gd_label_randomize.widthHint = 150;
        label_randomize.setLayoutData(gd_label_randomize);

        combo_randomize = new Combo(parameterGroup, SWT.BORDER | SWT.READ_ONLY);
        combo_randomize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        combo_randomize.add(Messages.SphincsPlusParameterView_combo_randomize_yes);
        combo_randomize.add(Messages.SphincsPlusParameterView_combo_randomize_no);
        
        //Dirty but fast. A spacer that forces the btnCheckButton_parameter to start in the second column.
        new Label(parameterGroup, SWT.NONE);

        btnCheckButton_parameter = new Button(parameterGroup, SWT.CHECK);
        btnCheckButton_parameter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        btnCheckButton_parameter.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        btnCheckButton_parameter.setText(Messages.SphincsPlusParameterView_btnCheckButton_parameter);

        btn_generateKeys = new Button(parameterGroup, SWT.NONE);
        btn_generateKeys.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        btn_generateKeys.setText(Messages.SphincsPlusParameterView_btn_generateKeys_generate);

        // Set Parameters to default
        combo_parameters.setEnabled(true);
        combo_parameters.select(0);
        System.arraycopy(sphincs_128s, 0, mode, 0, sphincs_128s.length);
        key_size_chars = (mode[0] * 2);
        min_message_length = (int) Math.ceil(((double) mode[3] * mode[4]) / 8);

        combo_hashfunction.setEnabled(true);
        combo_hashfunction.select(0);
        EnvironmentParameters.function = HashFunctionType.SHA_256;

        combo_randomize.setEnabled(true);
        combo_randomize.select(0);
        EnvironmentParameters.RANDOMIZE = true;

        // If Parameters not default
        combo_parameters.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                switch (combo_parameters.getSelectionIndex()) {
                case 0:
                    System.arraycopy(sphincs_128s, 0, mode, 0, sphincs_128s.length);
                    break;
                case 1:
                    System.arraycopy(sphincs_128f, 0, mode, 0, sphincs_128f.length);
                    break;
                case 2:
                    System.arraycopy(sphincs_192s, 0, mode, 0, sphincs_192s.length);
                    break;
                case 3:
                    System.arraycopy(sphincs_192f, 0, mode, 0, sphincs_192f.length);
                    break;
                case 4:
                    System.arraycopy(sphincs_256s, 0, mode, 0, sphincs_256s.length);
                    break;
                case 5:
                    System.arraycopy(sphincs_256f, 0, mode, 0, sphincs_256f.length);
                    break;
                }

                text_n.setText(String.valueOf(mode[0]));
                text_h.setText(String.valueOf(mode[1]));
                text_d.setText(String.valueOf(mode[2]));
                text_log_t.setText(String.valueOf(mode[3]));
                text_k.setText(String.valueOf(mode[4]));
                text_w.setText(String.valueOf(mode[5]));
                text_bitsec.setText(String.valueOf(mode[6]));
                text_sec_level.setText(String.valueOf(mode[7]));
                text_sig_bytes.setText(String.valueOf(mode[8]));
            }
        });

        // If parameters not default
        combo_hashfunction.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                switch (combo_hashfunction.getSelectionIndex()) {
                case 0:
                    EnvironmentParameters.function = HashFunctionType.SHA_256;
                    break;
                case 1:
                    EnvironmentParameters.function = HashFunctionType.SHAKE_256;
                    break;
                }
            }
        });

        // If parameters not default
        combo_randomize.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                switch (combo_randomize.getSelectionIndex()) {
                case 0:
                    EnvironmentParameters.RANDOMIZE = true;
                    break;
                case 1:
                    EnvironmentParameters.RANDOMIZE = false;
                    break;
                }
            }
        });

        // Check if parameter check button is selected or not.
        btnCheckButton_parameter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                if (btnCheckButton_parameter.getSelection()) {

                    MessageBox messageBoxx = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                    messageBoxx
                            .setMessage(Messages.SphincsPlusParameterView_messageBox_enter_parameter_manually_message);
                    messageBoxx.setText(Messages.SphincsPlusParameterView_messageBox_enter_parameter_manually_info);
                    messageBoxx.open();

                    ////////////////////////////////////////////////////
                    ////// Here starts the user input validation ///////
                    ////////////////////////////////////////////////////

                    param_inputValidationListener = new VerifyListener() {

                        @Override
                        public void verifyText(VerifyEvent e) {
                            String string = e.text;
                            char[] chars = new char[string.length()];
                            string.getChars(0, chars.length, chars, 0);
                            for (int i = 0; i < chars.length; i++) {
                                if (!('0' <= chars[i] && chars[i] <= '9')) {
                                    e.doit = false;
                                    return;
                                }
                            }
                        }
                    };

                    text_n.setEditable(true);
                    text_n.addVerifyListener(param_inputValidationListener);

                    text_h.setEditable(true);
                    text_h.addVerifyListener(param_inputValidationListener);

                    text_d.setEditable(true);
                    text_d.addVerifyListener(param_inputValidationListener);

                    text_log_t.setEditable(true);
                    text_log_t.addVerifyListener(param_inputValidationListener);

                    text_k.setEditable(true);
                    text_k.addVerifyListener(param_inputValidationListener);

                    text_w.setEditable(true);
                    text_w.addVerifyListener(param_inputValidationListener);

                    //////////////////////////////////////////////////
                    ////// Here ends the user input validation ///////
                    //////////////////////////////////////////////////

                    text_bitsec.setText("-");
                    text_sec_level.setText("-");
                    text_sig_bytes.setText("-");

                } else {

                    text_n.setEditable(false);
                    text_n.removeVerifyListener(param_inputValidationListener);

                    text_h.setEditable(false);
                    text_h.removeVerifyListener(param_inputValidationListener);

                    text_d.setEditable(false);
                    text_d.removeVerifyListener(param_inputValidationListener);

                    text_log_t.setEditable(false);
                    text_log_t.removeVerifyListener(param_inputValidationListener);

                    text_k.setEditable(false);
                    text_k.removeVerifyListener(param_inputValidationListener);

                    text_w.setEditable(false);
                    text_w.removeVerifyListener(param_inputValidationListener);

                    text_n.setText(String.valueOf(mode[0]));
                    text_h.setText(String.valueOf(mode[1]));
                    text_d.setText(String.valueOf(mode[2]));
                    text_log_t.setText(String.valueOf(mode[3]));
                    text_k.setText(String.valueOf(mode[4]));
                    text_w.setText(String.valueOf(mode[5]));
                    text_bitsec.setText(String.valueOf(mode[6]));
                    text_sec_level.setText(String.valueOf(mode[7]));
                    text_sig_bytes.setText(String.valueOf(mode[8]));
                }
            }
        });

        // Actions when pressing the generate keys button.
        btn_generateKeys.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {

                ////////////////////////////////////////////////////////////////////////
                ////////// If one of the parameter text fields is left empty ///////////
                ////////////////////////////////////////////////////////////////////////

                if (text_n.getText().isEmpty() || text_h.getText().isEmpty() || text_d.getText().isEmpty()
                        || text_log_t.getText().isEmpty() || text_k.getText().isEmpty() || text_w.getText().isEmpty()) {
                    MessageBox messageBoxx = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                    messageBoxx.setMessage(Messages.SphincsPlusParameterView_messageBox_parameter_empty_message);
                    messageBoxx.setText(Messages.SphincsPlusParameterView_messageBox_parameter_empty_info);
                    messageBoxx.open();
                }

                if (text_n.getText().isEmpty()) {
                    text_n.setText(String.valueOf(mode[0]));
                    EnvironmentParameters.n = Integer.parseInt(text_n.getText());
                } else {
                    EnvironmentParameters.n = Integer.parseInt(text_n.getText());
                }

                if (text_h.getText().isEmpty()) {
                    text_h.setText(String.valueOf(mode[1]));
                    EnvironmentParameters.h = Integer.parseInt(text_h.getText());
                } else {
                    EnvironmentParameters.h = Integer.parseInt(text_h.getText());
                }

                if (text_d.getText().isEmpty()) {
                    text_d.setText(String.valueOf(mode[2]));
                    EnvironmentParameters.d = Integer.parseInt(text_d.getText());
                } else {
                    EnvironmentParameters.d = Integer.parseInt(text_d.getText());
                }

                if (text_log_t.getText().isEmpty()) {
                    text_log_t.setText(String.valueOf(mode[3]));
                    EnvironmentParameters.a = Integer.parseInt(text_log_t.getText());
                } else {
                    EnvironmentParameters.a = Integer.parseInt(text_log_t.getText());
                }

                if (text_k.getText().isEmpty()) {
                    text_k.setText(String.valueOf(mode[4]));
                    EnvironmentParameters.k = Integer.parseInt(text_k.getText());
                } else {
                    EnvironmentParameters.k = Integer.parseInt(text_k.getText());
                }

                if (text_w.getText().isEmpty()) {
                    text_w.setText(String.valueOf(mode[5]));
                    EnvironmentParameters.w = Integer.parseInt(text_w.getText());
                } else {
                    EnvironmentParameters.w = Integer.parseInt(text_w.getText());
                }

                // If the key generation button isn't pressed,
                // a message appears in sign and verify
                key_generate_pressed = true;
                key_size_chars = (EnvironmentParameters.n * 2);

                min_message_length = (int) Math.ceil(((double) EnvironmentParameters.a * EnvironmentParameters.k) / 8);

                SphincsPlusSignAndVerifyView.text_info_inputMessage.setText(
                        Messages.SphincsPlusSignAndVerifyView_text_info_inputMessage_part_1 + min_message_length
                                + Messages.SphincsPlusSignAndVerifyView_text_info_inputMessage_part_2);

                SphincsPlusSignAndVerifyView.messageGroup
                        .setText(Messages.SphincsPlusSignAndVerifyView_messageGroup_part_1
                                + SphincsPlusSignAndVerifyView.text_inputMessage.getText().length() + "; "
                                + Messages.SphincsPlusSignAndVerifyView_messageGroup_part_2 + min_message_length + ")");

                // Keys input and initialization
                // If the "enter keys manually" check box is selected
                if (keys_check_box_selected) {

                    // If one key is empty or the entered size is wrong
                    if (text_SKseed.getText().length() < key_size_chars) {

                        MessageBox messageBoxx = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                        messageBoxx.setMessage(Messages.SphincsPlusParameterView_messageBox_no_sk_seed_entered_message);
                        messageBoxx.setText(Messages.SphincsPlusParameterView_messageBox_no_keys_entered_info);
                        messageBoxx.open();

                    } else if (text_PKseed.getText().length() < key_size_chars) {

                        MessageBox messageBoxx = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                        messageBoxx.setMessage(Messages.SphincsPlusParameterView_messageBox_no_pk_seed_entered_message);
                        messageBoxx.setText(Messages.SphincsPlusParameterView_messageBox_no_keys_entered_info);
                        messageBoxx.open();

                    } else if (text_SKprf.getText().length() < key_size_chars) {

                        MessageBox messageBoxx = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                        messageBoxx.setMessage(Messages.SphincsPlusParameterView_messageBox_no_sk_prf_entered_message);
                        messageBoxx.setText(Messages.SphincsPlusParameterView_messageBox_no_keys_entered_info);
                        messageBoxx.open();

                    } else if (text_PKroot.getText().length() < key_size_chars) {

                        MessageBox messageBoxx = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                        messageBoxx.setMessage(Messages.SphincsPlusParameterView_messageBox_no_pk_root_entered_message);
                        messageBoxx.setText(Messages.SphincsPlusParameterView_messageBox_no_keys_entered_info);
                        messageBoxx.open();

                        // If all keys are entered
                    } else {

                        byte[] sk_seed_entered;
                        byte[] pk_seed_entered;
                        byte[] sk_prf_entered;
                        byte[] pk_root_entered;

                        // get the entered seed values and convert it to bytes
                        sk_seed_entered = (text_SKseed.getText()).getBytes();
                        pk_seed_entered = (text_PKseed.getText()).getBytes();
                        sk_prf_entered = (text_SKprf.getText().getBytes());

                        // set the seed values in the Key instance
                        key.setSkseed(sk_seed_entered);
                        key.setPkseed(pk_seed_entered);
                        key.setSkprf(sk_prf_entered);

                        // calculate PK.root
                        pk_root_entered = new Hypertree().ht_PKgen(sk_seed_entered, pk_seed_entered);
                        pk_root_hex = Utils.bytesToHex(pk_root_entered);

                        // set PK.root in the Key instance
                        key.setPkroot(pk_root_entered);

                        // set PK.root in text field
                        text_PKroot.setText(pk_root_hex);

                        btn_generateKeys.setEnabled(false);

                        MessageBox messageBoxx = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                        messageBoxx.setMessage(
                                Messages.SphincsPlusParameterView_messageBox_continue_in_second_tab_message);
                        messageBoxx.setText(Messages.SphincsPlusParameterView_messageBox_continue_in_second_tab_info);
                        messageBoxx.open();
                    }

                } else {

                    spx.spx_keygen();

                    // Byte arrays for the keys
                    byte[] sk_seed_calculated;
                    byte[] pk_seed_calculated;
                    byte[] sk_prf_calculated;
                    byte[] pk_root_calculated;

                    // Get the values for the keys from keygen()
                    sk_seed_calculated = key.getSkseed();
                    pk_seed_calculated = key.getPkseed();
                    sk_prf_calculated = key.getSkprf();
                    pk_root_calculated = key.getPkroot();

                    // Convert the keys to hex and base
                    sk_seed_hex = Utils.bytesToHex(sk_seed_calculated);
                    sk_prf_hex = Utils.bytesToHex(sk_prf_calculated);
                    pk_seed_hex = Utils.bytesToHex(pk_seed_calculated);
                    pk_root_hex = Utils.bytesToHex(pk_root_calculated);

                    sk_seed_base = Base64.getEncoder().encodeToString(sk_seed_calculated);
                    sk_prf_base = Base64.getEncoder().encodeToString(sk_prf_calculated);
                    pk_seed_base = Base64.getEncoder().encodeToString(pk_seed_calculated);
                    pk_root_base = Base64.getEncoder().encodeToString(pk_root_calculated);

                    // If hex encoding is selected
                    if (key_hex_selected) {

                        text_SKprf.setText(sk_prf_hex);
                        text_PKroot.setText(pk_root_hex);
                        text_SKseed.setText(sk_seed_hex);
                        text_PKseed.setText(pk_seed_hex);

                        // If base encoding is selected
                    } else {

                        text_SKprf.setText(sk_prf_base);
                        text_PKroot.setText(pk_root_base);
                        text_SKseed.setText(sk_seed_base);
                        text_PKseed.setText(pk_seed_base);
                    }

                    MessageBox messageBoxx = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                    messageBoxx.setMessage(Messages.SphincsPlusParameterView_messageBox_continue_in_second_tab_message_2);
                    messageBoxx.setText(Messages.SphincsPlusParameterView_messageBox_continue_in_second_tab_info);
                    messageBoxx.open();
                }

                label_sk_SKseed_char_count.setText(text_SKseed.getText().length() + "/" + key_size_chars);
                label_sk_PKseed_char_count.setText(text_PKseed.getText().length() + "/" + key_size_chars);
                label_sk_SKprf_char_count.setText(text_SKprf.getText().length() + "/" + key_size_chars);
                label_sk_PKroot_char_count.setText(text_PKroot.getText().length() + "/" + key_size_chars);

            }

        });
    }

    /**
     * Creates the description group with the text description.
     * 
     * @param parent
     */
    private void createParameterDescriptionGroup(Composite parent) {
        descriptionGroup = new Group(parent, SWT.NONE);
        descriptionGroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        descriptionGroup.setLayout(new GridLayout(1, true));
        GridData gd_descriptionGroup = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd_descriptionGroup.minimumWidth = 250;
        gd_descriptionGroup.widthHint = 250;
        descriptionGroup.setLayoutData(gd_descriptionGroup);
        descriptionGroup.setText(Messages.SphincsPlusParameterView_descriptionGroup);

        text_description = new StyledText(descriptionGroup, SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        text_description.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        text_description.setAlwaysShowScrollBars(false);
        GridData text_data = new GridData(SWT.FILL, SWT.FILL, true, true);
        text_data.heightHint = 400;
        text_description.setText(Messages.SphincsPlusParameterView_parameterDescription);
        text_description.setLayoutData(text_data);

    }

    /**
     * Creates the Key body containing the secret and public key group.
     * 
     * @param parent
     */
    private void createKeyBody(Composite parent) {

        keysComposite = new Composite(parent, SWT.NONE);
        keysComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        keysCompositeLayout = new GridLayout();
        keysCompositeLayout.marginWidth = 0;
        keysCompositeLayout.marginHeight = 0;
        keysComposite.setLayout(keysCompositeLayout);
        keysComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createKeyGroup(keysComposite);
        // createPublicKeyGroup(keysComposite);
    }

    /**
     * Creates the secret key group containing all components of the secret key.
     * 
     * @param parent
     */
    private void createKeyGroup(Composite parent) {

        KeyGroup = new Group(parent, SWT.NONE);
        KeyGroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        KeyGroup.setLayout(new GridLayout(4, false));
        KeyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        KeyGroup.setText(Messages.SphincsPlusParameterView_secretKeyGroup);

        btn_hex = new Button(KeyGroup, SWT.RADIO);
        btn_hex.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        btn_hex.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        btn_hex.setText("Hex");

        btn_base64 = new Button(KeyGroup, SWT.RADIO);
        btn_base64.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btn_base64.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        btn_base64.setText("Base64");

        btnCheckButton_keys = new Button(KeyGroup, SWT.CHECK);
        btnCheckButton_keys.setLayoutData(new GridData(SWT.BORDER, SWT.FILL, true, false, 1, 1));
        btnCheckButton_keys.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        btnCheckButton_keys.setText(Messages.SphincsPlusParameterView_btnCheckButton_keys);

        label_characters = new Text(KeyGroup, SWT.NONE);
        label_characters.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        label_characters.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_characters.setText(Messages.SphincsPlusParameterView_label_characters);

        label_SKseed = new Text(KeyGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_SKseed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        label_SKseed.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_SKseed.setText("SK.seed");

        text_SKseed = new Text(KeyGroup, SWT.BORDER | SWT.READ_ONLY);
        text_SKseed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        label_sk_SKseed_char_count = new Text(KeyGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_sk_SKseed_char_count.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        label_sk_SKseed_char_count.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_sk_SKseed_char_count.setText(text_SKseed.getText().length() + "/" + key_size_chars);

        label_SKprf = new Text(KeyGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_SKprf.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        label_SKprf.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_SKprf.setText("SK.prf");

        text_SKprf = new Text(KeyGroup, SWT.BORDER | SWT.READ_ONLY);
        text_SKprf.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        label_sk_SKprf_char_count = new Text(KeyGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_sk_SKprf_char_count.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        label_sk_SKprf_char_count.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_sk_SKprf_char_count.setText(text_SKprf.getText().length() + "/" + key_size_chars);

        label_PKseed = new Text(KeyGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_PKseed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        label_PKseed.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_PKseed.setText("PK.seed");

        text_PKseed = new Text(KeyGroup, SWT.BORDER | SWT.READ_ONLY);
        text_PKseed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        label_sk_PKseed_char_count = new Text(KeyGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_sk_PKseed_char_count.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        label_sk_PKseed_char_count.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_sk_PKseed_char_count.setText(text_PKseed.getText().length() + "/" + key_size_chars);

        label_PKroot = new Text(KeyGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_PKroot.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        label_PKroot.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_PKroot.setText("PK.root");

        text_PKroot = new Text(KeyGroup, SWT.BORDER | SWT.READ_ONLY);
        text_PKroot.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        label_sk_PKroot_char_count = new Text(KeyGroup, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        label_sk_PKroot_char_count.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        label_sk_PKroot_char_count.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label_sk_PKroot_char_count.setText(text_PKroot.getText().length() + "/" + key_size_chars);

        // Check if parameter check button is selected or not.
        btnCheckButton_keys.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                if (btnCheckButton_keys.getSelection()) {

                    if (btn_base64.getSelection()) {

                        btn_base64.setSelection(false);
                        btn_hex.setSelection(true);

                        key_hex_selected = true;

                        text_SKseed.setText(sk_seed_hex);
                        text_PKseed.setText(pk_seed_hex);
                        text_SKprf.setText(sk_prf_hex);
                        text_PKroot.setText(pk_root_hex);

                    }

                    // If there are already keys generated and nothing has changed
                    if (key_generate_pressed) {

                        btn_generateKeys.setEnabled(false);
                    }

                    // Shows the actual amount of entered characters
                    text_SKseed.addListener(SWT.Modify, new Listener() {

                        @Override
                        public void handleEvent(Event e) {
                            text_SKseed.setTextLimit(key_size_chars);
                            label_sk_SKseed_char_count.setText(text_SKseed.getText().length() + "/" + key_size_chars);
                        }
                    });

                    text_PKseed.addListener(SWT.Modify, new Listener() {

                        @Override
                        public void handleEvent(Event e) {
                            text_PKseed.setTextLimit(key_size_chars);
                            label_sk_PKseed_char_count.setText(text_PKseed.getText().length() + "/" + key_size_chars);
                        }
                    });

                    text_SKprf.addListener(SWT.Modify, new Listener() {

                        @Override
                        public void handleEvent(Event e) {
                            text_SKprf.setTextLimit(key_size_chars);
                            label_sk_SKprf_char_count.setText(text_SKprf.getText().length() + "/" + key_size_chars);
                        }
                    });

                    text_PKroot.addListener(SWT.Modify, new Listener() {

                        @Override
                        public void handleEvent(Event e) {
                            text_PKroot.setTextLimit(key_size_chars);
                            label_sk_PKroot_char_count.setText(text_PKroot.getText().length() + "/" + key_size_chars);
                        }
                    });

                    ////////////////////////////////////////////////////
                    ////// Here starts the user input validation ///////
                    ////////////////////////////////////////////////////

                    keys_inputValidationListener = new VerifyListener() {
                        @Override
                        public void verifyText(VerifyEvent e) {
                            String string = e.text;
                            char[] chars = new char[string.length()];
                            string.getChars(0, chars.length, chars, 0);
                            for (int i = 0; i < chars.length; i++) {
                                if (!(('0' <= chars[i] && chars[i] <= '9') || ('A' <= chars[i] && chars[i] <= 'F')
                                        || ('a' <= chars[i] && chars[i] <= 'f'))) {
                                    e.doit = false;
                                    return;
                                }
                            }
                        }
                    };

                    text_SKseed.setEditable(true);
                    text_SKseed.addVerifyListener(keys_inputValidationListener);

                    text_PKseed.setEditable(true);
                    text_PKseed.addVerifyListener(keys_inputValidationListener);

                    text_SKprf.setEditable(true);
                    text_SKprf.addVerifyListener(keys_inputValidationListener);

                    //////////////////////////////////////////////////
                    ////// Here ends the user input validation ///////
                    //////////////////////////////////////////////////

                    // Enables set_key_button if keys has changed
                    keys_changedListener = new ModifyListener() {
                        @Override
                        public void modifyText(ModifyEvent e) {
                            key_generate_pressed = false;
                            btn_generateKeys.setEnabled(true);
                        }
                    };

                    text_SKseed.addModifyListener(keys_changedListener);
                    text_PKseed.addModifyListener(keys_changedListener);
                    text_SKprf.addModifyListener(keys_changedListener);

                    btn_hex.setEnabled(false);
                    btn_base64.setEnabled(false);

                    btn_generateKeys.setText(Messages.SphincsPlusParameterView_btn_generateKeys_set);

                    keys_check_box_selected = true;

                } else {

                    text_SKseed.setEditable(false);
                    text_SKseed.removeVerifyListener(keys_inputValidationListener);
                    text_SKseed.removeModifyListener(keys_changedListener);

                    text_PKseed.setEditable(false);
                    text_PKseed.removeVerifyListener(keys_inputValidationListener);
                    text_PKseed.removeModifyListener(keys_changedListener);

                    text_SKprf.setEditable(false);
                    text_SKprf.removeVerifyListener(keys_inputValidationListener);
                    text_SKprf.removeModifyListener(keys_changedListener);

                    btn_hex.setEnabled(true);
                    btn_base64.setEnabled(true);

                    btn_generateKeys.setText(Messages.SphincsPlusParameterView_btn_generateKeys_generate);

                    keys_check_box_selected = false;

                    btn_generateKeys.setEnabled(true);

                }
            }
        });

        // Set default selection
        btn_hex.setSelection(true);
        key_hex_selected = true;

        // If button selection changes
        btn_base64.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                Button source = (Button) e.getSource();

                if (source.getSelection()) {
                    key_hex_selected = false;
                    text_SKprf.setText(sk_prf_base);
                    text_PKroot.setText(pk_root_base);
                    text_SKseed.setText(sk_seed_base);
                    text_PKseed.setText(pk_seed_base);
                }
            }
        });

        btn_hex.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                Button source = (Button) e.getSource();

                if (source.getSelection()) {
                    key_hex_selected = true;
                    text_SKprf.setText(sk_prf_hex);
                    text_PKroot.setText(pk_root_hex);
                    text_SKseed.setText(sk_seed_hex);
                    text_PKseed.setText(pk_seed_hex);
                }
            }
        });

    }
}

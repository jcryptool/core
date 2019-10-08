package org.jcryptool.visual.sphincsplus.ui;

import org.eclipse.osgi.util.NLS;

/**
 * This class defines variables, which are used in the GUI. The values of this variables are defined
 * in the messages.properties and messages_de.properties. This is an easy way to change text printed
 * in the GUI, without doing anything in the actual code.
 * 
 * @author Sebastian Ranftl
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.visual.sphincsplus.ui";

    /**
     * SphincsPlusView
     */
    // Tab descriptions
    public static String SphincsPlusTab_1;
    public static String SphincsPlusTab_2;
    public static String SphincsPlusTab_3;
    public static String SphincsPlusTab_4;

    /**
     * SphincsPlusParameterView
     */
    // Title description
    public static String SphincsPlusParameterView_headLabel;
    public static String SphincsPlusParameterView_headDescription;

    // Group Titles
    public static String SphincsPlusParameterView_parameterGroup;
    public static String SphincsPlusParameterView_descriptionGroup;
    public static String SphincsPlusParameterView_secretKeyGroup;
    public static String SphincsPlusParameterView_publicKeyGroup;

    // Label Titles
    public static String SphincsPlusParameterView_label_security_level;
    public static String SphincsPlusParameterView_label_characters;

    // Button Titles
    public static String SphincsPlusParameterView_btn_generateKeys_set;
    public static String SphincsPlusParameterView_btn_generateKeys_generate;
    public static String SphincsPlusParameterView_btnCheckButton_parameter;
    public static String SphincsPlusParameterView_btnCheckButton_keys;
    public static String SphincsPlusParameterView_btnCheckButton_randomize;

    // Message Boxes
    public static String SphincsPlusParameterView_messageBox_enter_parameter_manually_message;
    public static String SphincsPlusParameterView_messageBox_enter_parameter_manually_info;
    public static String SphincsPlusParameterView_messageBox_no_sk_seed_entered_message;
    public static String SphincsPlusParameterView_messageBox_no_pk_seed_entered_message;
    public static String SphincsPlusParameterView_messageBox_no_sk_prf_entered_message;
    public static String SphincsPlusParameterView_messageBox_no_pk_root_entered_message;
    public static String SphincsPlusParameterView_messageBox_no_keys_entered_info;
    public static String SphincsPlusParameterView_messageBox_parameter_empty_message;
    public static String SphincsPlusParameterView_messageBox_parameter_empty_info;
    public static String SphincsPlusParameterView_messageBox_continue_in_second_tab_message;
    public static String SphincsPlusParameterView_messageBox_continue_in_second_tab_message_2;
    public static String SphincsPlusParameterView_messageBox_continue_in_second_tab_info;
    public static String SphincsPlusParameterView_messageBox_switch_to_hex_message;
    public static String SphincsPlusParameterView_messageBox_switch_to_hex_info;

    // Parameter description
    public static String SphincsPlusParameterView_parameterDescription;

    // Combo Boxes
    public static String SphincsPlusParameterView_combo_randomize_yes;
    public static String SphincsPlusParameterView_combo_randomize_no;

    /**
     * SphincsPlusSignAndVerifyView
     */
    // Group Titles
    public static String SphincsPlusSignAndVerifyView_messageGroup_part_1;
    public static String SphincsPlusSignAndVerifyView_messageGroup_part_2;
    public static String SphincsPlusSignAndVerifyView_signButtonGroup;
    public static String SphincsPlusSignAndVerifyView_encodingButtonGroup;
    public static String SphincsPlusSignAndVerifyView_valueGroup;
    public static String SphincsPlusSignAndVerifyView_descriptionGroup;
    public static String SphincsPlusSignAndVerifyView_infoGroup;

    // Text Titles
    public static String SphincsPlusSignAndVerifyView_text_inputMessage;
    public static String SphincsPlusSignAndVerifyView_text_sig_description;

    // Button Titles
    public static String SphincsPlusSignAndVerifyView_btn_sign;
    public static String SphincsPlusSignAndVerifyView_btn_verify;

    // Label Titles
    public static String SphincsPlusSignAndVerifyView_lbl_status_signed;
    public static String SphincsPlusSignAndVerifyView_lbl_status_verified;
    public static String SphincsPlusSignAndVerifyView_lbl_status_verification_failed;
    public static String SphincsPlusSignAndVerifyView_lbl_status_text_changed;

    // Message Boxes
    public static String SphincsPlusSignAndVerifyView_no_key_message;
    public static String SphincsPlusSignAndVerifyView_no_key_info;
    public static String SphincsPlusSignAndVerifyView_sign_first_message;
    public static String SphincsPlusSignAndVerifyView_sign_first_info;
    
    // Descriptions
    public static String SphincsPlusSignAndVerifyView_text_info_inputMessage_part_1;
    public static String SphincsPlusSignAndVerifyView_text_info_inputMessage_part_2;

    /**
     * SphincsPlusTreeView
     */
    // Label Titles
    public static String SphincsPlusTreeView_descLabel;

    // Collapsed Part Title
    public static String SphincsPlusTreeView_collapsedPart_expanded;
    public static String SphincsPlusTreeView_collapsedPart_collapsed;

    // Description Text
    public static String SphincsPlusTreeView_descText_general;
    public static String SphincsPlusTreeView_descText_root_node_d;
    public static String SphincsPlusTreeView_descText_root_node_XMSS;

    /**
     * SphincsPlusForsView
     */
    // Label Titles
    public static String SphincsPlusForsView_descLabel;

    // Collapsed Part Title
    public static String SphincsPlusForsView_collapsedPart_expanded;
    public static String SphincsPlusForsView_collapsedPart_collapsed;

    // Descriptions
    public static String SphincsPlusForsView_descText_general;
    public static String SphincsPlusForsView_description_rootNode;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME + ".messages", Messages.class);
    }

    private Messages() {
    }
}

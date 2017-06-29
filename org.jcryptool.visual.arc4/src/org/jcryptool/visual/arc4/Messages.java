//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.arc4;

import org.eclipse.osgi.util.NLS;

/**
 * This class defines names of string constants that are the texts of the plug-in; the actual texts
 * are found in "messages_de.properties" and "messages_en.properties"
 * 
 * @author Luca Rupp
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.visual.arc4.messages";

    public static String VariablesVisualLabelI;

    public static String VariablesVisualLabelJ;

    public static String VariablesVisualLabelStep;

    public static String WizardPageInvalidHexDigit;

    public static String WizardPageInputLimitPart1;

    public static String WizardPageInputLimitPart2;

    public static String InstructionVisualLabel;

    public static String InstructionVisualButton;

    public static String VectorVisualGroup;

    public static String VectorVisualTool;

    public static String InstructionVisualGroup;

    public static String InstructionVisualTool;

    public static String InstructionVisualInputTool;

    public static String InstructionVisualButtonTool;

    public static String VariablesVisualGroup;

    public static String VariablesVisualTool;

    public static String DatavectorVisualKEYGroup;

    public static String DatavectorVisualKEYTool;

    public static String DatavectorVisualENCGroup;

    public static String DatavectorVisualENCTool;

    public static String DatavectorVisualPLAINGroup;

    public static String DatavectorVisualPLAINTool;

    public static String DatavectorVisualRANDGroup;

    public static String DatavectorVisualRANDTool;

    public static String DatavectorVisualKEYButton;

    public static String DatavectorVisualPLAINButton;

    public static String DatavectorVisualKEYWizard;

    public static String DatavectorVisualPLAINWizard;

    public static String DatavectorVisualKEYButtonTool;

    public static String DatavectorVisualPLAINButtonTool;

    public static String WizardPageDescriptionKey;

    public static String WizardPageDescriptionPlain;

    public static String WizardPageToLong;

    public static String PluginDescription;

    public static String InstuctionVisualFinish;

    public static String InstructionVisualFinishTool;

    public static String PluginDescriptionCaption;

    public static String InstructionVisualRYESText;

    public static String InstructionVisualRYESTool;

    public static String InstructionVisualRNOText;

    public static String InstructionVisualRNOTool;

    public static String CompositeResetText;

    public static String CompositeResetTool;

    public static String CompositeSettingsText;

    public static String CompositeSettingsTool;

    public static String CompositeAlgFinishTitle;

    public static String CompositeAlgFinishText;

    public static String CompositeSettingsRandText;

    public static String CompositeSettingsRandTool;

    public static String VariablesVisualDec;

    public static String VariablesVisualHex;

    public static String WizardPageInputFull;
    
    public static String VariableW;
    
    public static String CompositeWTool;
    
    public static String CompositeARC4;
    
    public static String CompositeARC4Tool;
    
    public static String CompositeSPRITZ;
    
    public static String CompositeSPRITZTool;
    
    public static String AlgSelectionBoxText;
    
    public static String AlgSelectionBoxTool;
    
    public static String CopyToClipboard;
    
    public static String CopyToClipboardTool;
    
    public static String WizardPageHexNotification;
    
    public static String KeySelectionWizardHeading;
    
    public static String PlainSelectionWizardHeading;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }

}
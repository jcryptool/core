// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.kleptography.ui;

import java.math.BigInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.kleptography.ui.KleptoView.PrimeGenSetting;

public class RSAKeyView extends Composite {

    private KleptoView kleptoView;

    private Group gTexts;
    private Group gKeyGen;
    private Group gAdditionalNumbers;
    private Group gStandardNumbers;
    private Group gMessage;
    private Group gCipher;
    private Group gDecrypt;
    private StyledText textP;
    private StyledText textQ;
    private StyledText textN;
    private StyledText textE;
    private StyledText textD;
    private Text tMessage;
    private Text tCiphertext;
    private Text tDecrypt;
    private Combo tBits;
    private Label labelP;
    private Label labelQ;
    private Label labelN;
    private Label labelE;
    private Label labelD;
    private Label lBits;
    private Button bCalcN;
    private Button bGenE;
    private Button bCalcD;
    private Button bEncrypt;
    private Button bDecrypt;
    private Button bResetIDAndIndex;
    private StyledText tID;
    private StyledText tIndex;
    private Label lID;
    private Label lIndex;
    private Button bGenSeed;
    private StyledText tSeed;
    private Label lSeed;
    private Button bGenPrimes;
    private Composite cExtraButton;
    private Composite cExtraLabel1;
    private Composite cExtraLabel2;
    private Composite cExtraText1;
    private Composite cExtraText2;
    private Button bGenAll;
    private Composite cHeader;
    private Label lTitle;
    private StyledText stGeneralDescription;
    private Composite cDescription;
    private Label lDescTitle;
    private StyledText stSpecificDescription;
    private Button bGenAttacker;
    private Label lAttackerN;
    private Label lAttackerE;
    private StyledText tAttackerN;
    private StyledText tAttackerE;
    private Label lEncryptedP;
    private StyledText tEncryptedP;
    private Composite cRadix;
    private Button radioBinary;
    private Button radioDecimal;
    private Button radioHex;
    private Integer displayRadix;
    private Button bSave;
    private Label lNPrime;
    private StyledText tNPrime;
    /** Prevents the bit length error dialog from appearing twice and causing problems. */
    private boolean dialogFlag = false;
    private Combo cAttack;
    private Label lMethod;
    private Group gSettings;
	private Label lBlankE;
	private Button bResetE;
    private Label lBitsBinary;

    /**
     * Sets the bit count to be used for generating keys. If using a SETUP attack and the input is less than 32, it will
     * be set to 32. (SETUP requires at least 16 bits for the attack's keys, meaning 32 for the cryptosystem.) If the
     * input is less than 16, it will be set to 16. If the input is not a multiple of 16, round down.
     *
     * @param bits The desired bit count.
     */
    public void setBits(int bits) {
        if (kleptoView.currentSetting == PrimeGenSetting.SETUP && bits < 20) {
            displayInputError(1);
            tBits.setText("20"); //$NON-NLS-1$
        } else if (kleptoView.currentSetting == PrimeGenSetting.SETUP && bits % 4 != 0) {
            displayInputError(2);
            tBits.setText(Integer.toString(bits - bits % 4));
        } else if (bits < 8) {
            displayInputError(3);
            tBits.setText("8"); //$NON-NLS-1$
        } else if (bits % 2 != 0) {
            displayInputError(4);
            tBits.setText(Integer.toString(bits - bits % 2));
        } else {
            tBits.setText(Integer.toString(bits));
        }
        // If the local setting is different from the main code setting, save the new
        // value and reset everything. We need to make sure that the value actually
        // changed, otherwise it would be annoying for the user for everything
        // to reset for no reason.
        if (getBits() != kleptoView.klepto.functions.getBitCount()) {
            kleptoView.currentStep = 1;
            kleptoView.klepto.functions.setBitCount(getBits());
            kleptoView.klepto.rsa.setInitSETUP(false);
            updateEnabled();
            kleptoView.attackView.clearAttackDisplay();
            highlightEncryptedP();
            kleptoView.klepto.rsa.setInitFixed(false);
            kleptoView.klepto.attack.resetFixedTextsSaved();
            updateDescription();
        }
    }

    /**
     * Displays an error message if the input bit count is invalid.
     *
     * @param errorNum The particular error message to display.
     */
    private void displayInputError(int errorNum) {
        // This flag is necessary to prevent an ugly situation. If SETUP is selected and
        // user tries to change to a bit count less than 20, this function can get called
        // twice, and the second call will crash and/or cause a severe memory crunch.
        if (dialogFlag)
            return;
        dialogFlag = true;
        MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
        mb.setText(Messages.RSAKeyView_Bit_Length_Input_Error_Title);
        switch (errorNum) {
            case 1:
                mb.setMessage(Messages.RSAKeyView_Bit_Length_SETUP_Minimum);
                break;
            case 2:
                mb.setMessage(Messages.RSAKeyView_Bit_Length_SETUP_Divisibility);
                break;
            case 3:
                mb.setMessage(Messages.RSAKeyView_Bit_Length_Minimum);
                break;
            case 4:
                mb.setMessage(Messages.RSAKeyView_Bit_Length_Divisibility);
                break;
            default:
                mb.setMessage(Messages.RSAKeyView_Bit_Length_Generic_Error);
        }
        mb.open();
        dialogFlag = false;
    }

    /**
     * Sets the bit count to be used for generating keys. Accepts input as a string but converts it if possible. If the
     * string is not numeric, it will reset to the last stored value.
     *
     * @param textBits The desired bit count (as a string).
     */
    public void setBits(String textBits) {
        try {
            setBits(Integer.parseInt(textBits.trim()));
        } catch (NumberFormatException nfe) {
            setBits(kleptoView.klepto.functions.getBitCount());
        }
    }

    /**
     * Retrieves the stored bit count as an integer. If the text field is not numeric, it will return the last stored
     * value.
     *
     * @return The stored bit count.
     */
    public int getBits() {
        try {
            return Integer.parseInt(tBits.getText().trim());
        } catch (NumberFormatException nfe) {
            return kleptoView.klepto.functions.getBitCount();
        }
    }

    /**
     * Retrieves the stored bit count as a string. It first tries to convert the text field to an integer and then to a
     * string, so if the value is not numeric, it will return the last stored value.
     *
     * @return The stored bit count (as a string)
     */
    public String getBitsAsString() {
        return Integer.toString(getBits());
    }

    public void setDisplayRadix(Integer displayRadix) {
        this.displayRadix = displayRadix;
    }

    public Integer getDisplayRadix() {
        return displayRadix;
    }

    /**
     * Constructor. Sets up the whole GUI page, initializes what is visible and accessible, and sets default values.
     *
     * @param parent The parent composite (the Encryption tab).
     * @param kleptoView A reference to the kleptoView driver class.
     */
    public RSAKeyView(final Composite parent, final int style, KleptoView kleptoView) {
        // Set up the basic appearance.
        super(parent, style);
        setLayout(new GridLayout(3, true));
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // Save a reference to the driver class and initialize the sequencing.
        this.kleptoView = kleptoView;
        kleptoView.currentSetting = PrimeGenSetting.HONEST;
        kleptoView.currentStep = 1;

        // Set up the controls.
        setUpHeader(this);
        setUpKeyGen(this);
        setUpDescription(this);

        setUpTexts(this);
        setUpListeners();

        // Set up the visibility, editability, and enabled states.
        setUpEditability();
        updateEnabled();
        updateVisibility();

        // Set default values.
        setBits(kleptoView.klepto.functions.getBitCount());
        updateDescription();
        setDefaultE();

        // Set up the tab order for the tab. The key idea is to ignore the Header and Description.
        this.setTabList(new Control[] {gKeyGen, gTexts});
    }

    /**
     * Sets the default value of E.
     */
    private void setDefaultE() {
        kleptoView.klepto.rsa.setDefaultE();
        textE.setText(kleptoView.klepto.rsa.getE().toString(getDisplayRadix()));
    }

    /**
     * Sets up the header with a description of the plugin.
     *
     * @param localParent The parent control of the header (the Key tab).
     */
    private void setUpHeader(Composite localParent) {
        cHeader = new Composite(localParent, SWT.NONE);
        cHeader.setBackground(KleptoView.WHITE);
        GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
        gd2.minimumWidth = 300;
        gd2.widthHint = 300;
        cHeader.setLayoutData(gd2);
        cHeader.setLayout(new GridLayout());

        lTitle = new Label(cHeader, SWT.NONE);
        lTitle.setFont(FontService.getHeaderFont());
        lTitle.setBackground(KleptoView.WHITE);
        lTitle.setText(Messages.RSAKeyView_Title);

        stGeneralDescription = new StyledText(cHeader, SWT.READ_ONLY | SWT.WRAP);
        stGeneralDescription.setText(Messages.RSAKeyView_Gen_Desc);
        GridData gd = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
        stGeneralDescription.setLayoutData(gd);
    }

    /**
     * Sets up the key generation section (which contains the buttons and cryptosystem numbers).
     *
     * @param localParent The parent control of the key generation section (the Key tab).
     */
    private void setUpKeyGen(Composite localParent) {
        // Define the main control.
        gKeyGen = new Group(localParent, SWT.NONE);
        gKeyGen.setLayout(new GridLayout());
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        gKeyGen.setLayoutData(gd);
        gKeyGen.setText(Messages.RSAKeyView_Keygen);

        gSettings = new Group(gKeyGen, SWT.NONE);
        gSettings.setLayout(new GridLayout(4, false));
        gSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        gSettings.setText(Messages.RSAKeyView_Settings);
        setUpSettings(gSettings);

        gAdditionalNumbers = new Group(gKeyGen, SWT.NONE);
        gAdditionalNumbers.setLayout(new GridLayout(2, false));
        GridData gdNumbers = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gAdditionalNumbers.setLayoutData(gdNumbers);
        gAdditionalNumbers.setText(Messages.RSAKeyView_Additional_Values);
        setUpAdditionalNumbers(gAdditionalNumbers);

        gStandardNumbers = new Group(gKeyGen, SWT.NONE);
        gStandardNumbers.setLayout(new GridLayout(5, false));
        GridData gdButtons = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gStandardNumbers.setLayoutData(gdButtons);
        gStandardNumbers.setText(Messages.RSAKeyView_Cryptosystem_Values);
        setUpStandardNumbers(gStandardNumbers);
    }

    /**
     * Sets up the settings section.
     *
     * @param localParent The parent control of the settings section (the keygen group).
     */
    private void setUpSettings(Composite localParent) {
        lMethod = new Label(localParent, SWT.LEFT);
        lMethod.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        lMethod.setText(Messages.RSAKeyView_Method);

        cAttack = new Combo(localParent, SWT.READ_ONLY | SWT.DROP_DOWN);
        cAttack.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
        // Set the list of attack methods and default to the first, honest key generation.
        String items2[] = {Messages.RSAKeyView_Honest, Messages.RSAKeyView_FixedP, Messages.RSAKeyView_PRF,
                Messages.RSAKeyView_PRG, Messages.RSAKeyView_SETUP};
        cAttack.setItems(items2);
        cAttack.select(0);

        // Define the display radix composite.
        cRadix = new Composite(localParent, SWT.BORDER);
        cRadix.setLayout(new GridLayout(3, false));
        cRadix.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        // Set up the radix buttons.
        setUpRadixButtons(cRadix);

        // Set up the bit count controls.
        lBits = new Label(localParent, SWT.None);
        lBits.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        lBits.setText(Messages.RSAKeyView_Bit_Length);

        tBits = new Combo(localParent, SWT.BORDER);
        GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        // gd.minimumWidth = 32;
        // gd.widthHint = 32;
        tBits.setLayoutData(gd);
        // Set the default items of the list.
        String items[] = {"16", "32", "64", "128", "256"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        tBits.setItems(items);

        lBitsBinary = new Label(localParent, SWT.NONE);
        lBitsBinary.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        lBitsBinary.setText(Messages.RSAKeyView_in_decimal);
    }

    /**
     * Sets up the display radix radio buttons at the top of the numbers group.
     *
     * @param localParent The parent control of the radix buttons (the settings group).
     */
    private void setUpRadixButtons(Composite localParent) {
        radioBinary = new Button(localParent, SWT.RADIO);
        radioBinary.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        radioBinary.setText(Messages.RSAKeyView_Binary);
        radioDecimal = new Button(localParent, SWT.RADIO);
        radioDecimal.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        radioDecimal.setText(Messages.RSAKeyView_Decimal);
        radioHex = new Button(localParent, SWT.RADIO);
        radioHex.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        radioHex.setText(Messages.RSAKeyView_Hex);
        // Default to hex.
        radioHex.setSelection(true);
        setDisplayRadix(16);
    }

    /**
     * Sets up the additional cryptosystem buttons and numbers.
     *
     * @param localParent The parent control of the additional numbers group (the keygen group).
     */
    private void setUpAdditionalNumbers(Composite localParent) {
        // Set up the extra button(s).
        cExtraButton = new Composite(localParent, SWT.NONE);
        cExtraButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
        cExtraButton.setLayout(new StackLayout());
        setUpSpecialButtons(cExtraButton);

        // Set up the spaces for extra labels and text.
        cExtraLabel1 = new Composite(localParent, SWT.NONE);
        cExtraLabel1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        cExtraLabel1.setLayout(new StackLayout());
        setUpExtraLabel1(cExtraLabel1);

        cExtraText1 = new Composite(localParent, SWT.NONE);
        cExtraText1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        cExtraText1.setLayout(new StackLayout());
        setUpExtraText1(cExtraText1);

        cExtraLabel2 = new Composite(localParent, SWT.NONE);
        cExtraLabel2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        cExtraLabel2.setLayout(new StackLayout());
        setUpExtraLabel2(cExtraLabel2);

        cExtraText2 = new Composite(localParent, SWT.NONE);
        cExtraText2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        cExtraText2.setLayout(new StackLayout());
        setUpExtraText2(cExtraText2);

        // Set up the specific extra controls for the SETUP attack (otherwise invisible).
        lEncryptedP = new Label(localParent, SWT.None);
        lEncryptedP.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        lEncryptedP.setText(Messages.RSAKeyView_EncryptedP);
        tEncryptedP = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        tEncryptedP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        tEncryptedP.setMargins(4, 0, 4, 0);

        lNPrime = new Label(localParent, SWT.None);
        lNPrime.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        lNPrime.setText(Messages.RSAKeyView_N_Prime);
        tNPrime = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        tNPrime.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        tNPrime.setMargins(4, 0, 4, 0);
    }

    /**
     * Sets up the special/extra button(s). This is based on a StackLayout to show one (or none) of three buttons at a
     * time.
     *
     * @param localParent The parent control of the special buttons (the extra button composite).
     */
    private void setUpSpecialButtons(Composite localParent) {
        bResetIDAndIndex = new Button(localParent, SWT.PUSH);
        bResetIDAndIndex.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        bResetIDAndIndex.setText(Messages.RSAKeyView_Reset_ID_Index);

        bGenSeed = new Button(localParent, SWT.PUSH);
        bGenSeed.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        bGenSeed.setText(Messages.RSAKeyView_Gen_New_Seed);

        bGenAttacker = new Button(localParent, SWT.PUSH);
        bGenAttacker.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        bGenAttacker.setText(Messages.RSAKeyView_Gen_Attacker_Keys);
    }

    /**
     * Sets up the first extra label (designed for a StackLayout).
     *
     * @param localParent The parent control of the extra label (the additional numbers group).
     */
    private void setUpExtraLabel1(Composite localParent) {
        lID = new Label(localParent, SWT.None);
        lID.setText(Messages.RSAKeyView_ID);
        lSeed = new Label(localParent, SWT.None);
        lSeed.setText(Messages.RSAKeyView_Seed);
        lAttackerN = new Label(localParent, SWT.None);
        lAttackerN.setText(Messages.RSAKeyView_AttackerN);
    }

    /**
     * Sets up the first extra text (designed for a StackLayout).
     *
     * @param localParent The parent control of the extra text (the additional numbers group).
     */
    private void setUpExtraText1(Composite localParent) {
        tID = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        tID.setMargins(4, 0, 4, 0);
        tSeed = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        tSeed.setMargins(4, 0, 4, 0);
        tAttackerN = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        tAttackerN.setMargins(4, 0, 4, 0);
    }

    /**
     * Sets up the second extra label (designed for a StackLayout).
     *
     * @param localParent The parent control of the extra label (the additional numbers group).
     */
    private void setUpExtraLabel2(Composite localParent) {
        lIndex = new Label(localParent, SWT.None);
        lIndex.setText(Messages.RSAKeyView_Index);
        lAttackerE = new Label(localParent, SWT.None);
        lAttackerE.setText(Messages.RSAKeyView_AttackerE);
    }

    /**
     * Sets up the second extra text (designed for a StackLayout).
     *
     * @param localParent The parent control of the extra text (the additional numbers group).
     */
    private void setUpExtraText2(Composite localParent) {
        tIndex = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        tIndex.setMargins(4, 0, 4, 0);
        tAttackerE = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        tAttackerE.setMargins(4, 0, 4, 0);
    }

    /**
     * Sets up the standard cryptosystem numbers.
     *
     * @param localParent The parent control of the standard numberss (the keygen group).
     */
    private void setUpStandardNumbers(Composite localParent) {
        bGenAll = new Button(localParent, SWT.PUSH);
        bGenAll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 5, 1));
        bGenAll.setText(Messages.RSAKeyView_GenAll);

        bGenPrimes = new Button(localParent, SWT.PUSH);
        bGenPrimes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
        bGenPrimes.setText(Messages.RSAKeyView_Gen_Primes);

        labelP = new Label(localParent, SWT.None);
        labelP.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 2, 1));
        labelP.setText(Messages.RSAKeyView_P);

        labelQ = new Label(localParent, SWT.None);
        labelQ.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 2, 1));
        labelQ.setText(Messages.RSAKeyView_Q);

        textP = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        textP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
        textP.setMargins(4, 0, 4, 0);

        textQ = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        textQ.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
        textQ.setMargins(4, 0, 4, 0);

        bCalcN = new Button(localParent, SWT.PUSH);
        bCalcN.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
        bCalcN.setText(Messages.RSAKeyView_CalcN);

        labelN = new Label(localParent, SWT.None);
        labelN.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 4, 1));
        labelN.setText(Messages.RSAKeyView_N);

        textN = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        textN.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
        textN.setMargins(4, 0, 4, 0);

        lBlankE = new Label(localParent, SWT.None);
        lBlankE.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));

        labelE = new Label(localParent, SWT.None);
        labelE.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 2, 1));
        labelE.setText(Messages.RSAKeyView_E);

        // These buttons mess everything up. Can't figure it out.
        bGenE = new Button(localParent, SWT.PUSH);
        bGenE.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
        bGenE.setText(Messages.RSAKeyView_GenE);

        bResetE = new Button(localParent, SWT.PUSH);
        bResetE.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
        bResetE.setText(Messages.RSAKeyView_ResetE);

        textE = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        textE.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
        textE.setMargins(4, 0, 4, 0);

        bCalcD = new Button(localParent, SWT.PUSH);
        bCalcD.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
        bCalcD.setText(Messages.RSAKeyView_CalcD);

        labelD = new Label(localParent, SWT.None);
        labelD.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 4, 1));
        labelD.setText(Messages.RSAKeyView_D);

        textD = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        textD.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
        textD.setMargins(4, 0, 4, 0);
    }

    /**
     * Sets up the description of the specific key generation algorithm.
     *
     * @param localParent The parent control of the description (the keygen tab).
     */
    private void setUpDescription(Composite localParent) {
        cDescription = new Composite(localParent, SWT.NONE);
        cDescription.setBackground(KleptoView.WHITE);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd.minimumHeight = 300;
        gd.heightHint = 300;
        gd.minimumWidth = 10;
        gd.widthHint = 10;
        cDescription.setLayoutData(gd);
        cDescription.setLayout(new GridLayout(1, true));

        // The text here depends on the algorithm chosen - hence initialize it to be blank.
        lDescTitle = new Label(cDescription, SWT.NONE);
        lDescTitle.setFont(FontService.getHeaderFont());
        lDescTitle.setBackground(KleptoView.WHITE);
        lDescTitle.setText(""); //$NON-NLS-1$
        lDescTitle.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        // The text here depends on the current step - hence initialize it to be blank.
        stSpecificDescription = new StyledText(cDescription, SWT.READ_ONLY | SWT.WRAP);
        stSpecificDescription.setText(""); //$NON-NLS-1$
        // stSpecificDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd.minimumHeight = 300;
        gd.heightHint = 300;
        gd.minimumWidth = 10;
        gd.widthHint = 10;
        stSpecificDescription.setLayoutData(gd);
    }

    /**
     * Sets up the plaintext and ciphertext controls at the bottom.
     *
     * @param localParent The parent control of the text section (the Key tab).
     */
    private void setUpTexts(Composite localParent) {
        // Set up the group to contain all the texts.
        gTexts = new Group(localParent, SWT.None);
        gTexts.setLayout(new GridLayout(3, true));
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        gd.minimumHeight = 10;
        gd.heightHint = 10;
        gTexts.setLayoutData(gd);
        gTexts.setText(Messages.RSAKeyView_Texts);

        // Define the subgroups (plaintext/message, ciphertext, deciphered text)
        gMessage = new Group(gTexts, SWT.NONE);
        gMessage.setLayout(new GridLayout(1, true));
        // GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        // gd2.minimumHeight = 100;
        // gd2.heightHint = 150;
        gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd.minimumHeight = 10;
        gd.heightHint = 10;
        gMessage.setLayoutData(gd);
        // gMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        gMessage.setText(Messages.RSAKeyView_Plaintext);

        gCipher = new Group(gTexts, SWT.NONE);
        gCipher.setLayout(new GridLayout(2, false));
        gCipher.setLayoutData(gd);
        // gCipher.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        gCipher.setText(Messages.RSAKeyView_Ciphertext);

        gDecrypt = new Group(gTexts, SWT.NONE);
        gDecrypt.setLayout(new GridLayout(1, true));
        gDecrypt.setLayoutData(gd);
        // gDecrypt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        gDecrypt.setText(Messages.RSAKeyView_Decrypted);

        // Set up the actual text fields and associated buttons.
        tMessage = new Text(gMessage, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        tMessage.setLayoutData(gd);
        // tMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        bEncrypt = new Button(gMessage, SWT.PUSH);
        bEncrypt.setText(Messages.RSAKeyView_Encrypt);

        tCiphertext = new Text(gCipher, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gd.minimumHeight = 10;
        gd.heightHint = 10;
        tCiphertext.setLayoutData(gd);
        // tCipher.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        bDecrypt = new Button(gCipher, SWT.PUSH);
        bDecrypt.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 1, 1));
        bDecrypt.setText(Messages.RSAKeyView_Decrypt);
        bSave = new Button(gCipher, SWT.PUSH);
        bSave.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 1, 1));
        bSave.setText(Messages.RSAKeyView_Save);

        tDecrypt = new Text(gDecrypt, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd.minimumHeight = 10;
        gd.heightHint = 10;
        tDecrypt.setLayoutData(gd);
        // tDecrypt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    }

    /**
     * Generates the primes P and Q according to the selected algorithm.
     */
    private void genPrimes() {
        switch (kleptoView.currentSetting) {
            case HONEST:
                kleptoView.klepto.rsa.genHonestPrimes();
                break;
            case FIXED:
                kleptoView.klepto.rsa.genFixedPRandomQ();
                break;
            case PRF:
                kleptoView.klepto.rsa.genPseudoRandomFunctionP();
                // Update index with new value.
                tIndex.setText(kleptoView.klepto.rsa.getIndex().toString(getDisplayRadix()));
                break;
            case PRG:
                kleptoView.klepto.rsa.genPseudoRandomGeneratorP();
                // Update seed with new value.
                tSeed.setText(kleptoView.klepto.rsa.getSeed().toString(getDisplayRadix()));
                break;
            case SETUP:
                kleptoView.klepto.rsa.genSetupPrimes();
                // Update the encrypted P and N' with their new values.
                tEncryptedP.setText(kleptoView.klepto.rsa.getEncryptedP().toString(getDisplayRadix()));
                tNPrime.setText(kleptoView.klepto.rsa.getNPrime().toString(getDisplayRadix()));
                break;
        }
        highlightEncryptedP();
        textP.setText(kleptoView.klepto.rsa.getP().toString(getDisplayRadix()));
        textQ.setText(kleptoView.klepto.rsa.getQ().toString(getDisplayRadix()));
    }

    /**
     * Calculates N and Phi from P and Q.
     */
    private void calcN() {
        kleptoView.klepto.rsa.calculateN();
        kleptoView.klepto.rsa.calculatePhi();
        textN.setText(kleptoView.klepto.rsa.getN().toString(getDisplayRadix()));
        highlightEncryptedP();
    }

    /**
     * When using the SETUP attack and displaying in binary or hex, highlight the encrypted P and its representation in
     * N and N'.
     */
    private void highlightEncryptedP() {
        // For highlighting to actually work, we have to make sure that you can actually
        // see the identical parts, which means only certain radixes will work.
        // The radix must be a factor of two, unless the bitCount is not divisible by 8.
        // It must be divisible by 4 for the algorithm to work anyway, so in that case
        // only a radix of 2 or 4 would work, and I didn't include a quaternary option.
        // We must also be at least step 3 (primes generated).
        // Create a StyleRange for the appropriate length, but don't color it yet.
        StyleRange styleRange = new StyleRange();
        styleRange.start = 0;
        styleRange.length = tEncryptedP.getCharCount();
        if (kleptoView.currentSetting == PrimeGenSetting.SETUP
                && ((kleptoView.klepto.functions.getBitCount() % 8 == 0 && kleptoView.klepto.functions
                        .checkMultipleOfTwo(getDisplayRadix())) || getDisplayRadix() == 2 || getDisplayRadix() == 4)
                && kleptoView.currentStep >= 3) {
            // Color the StyleRange yellow and set it to the Encrypted P and N'.
            styleRange.background = KleptoView.YELLOW;
            tEncryptedP.setStyleRange(styleRange);
            tNPrime.setStyleRange(styleRange);
            // First check if textN has text in it. If the user has gone back some steps,
            // such that N was previously highlighted but now no longer should be,
            // redefine the StyleRange to gray. Otherwise keep it yellow, but in either
            // case, apply it to textN. Note that the layered conditionals prevent
            // a bug if the user starts by hitting GenAll for SETUP.
            if (textN.getCharCount() > tEncryptedP.getCharCount()) {
                if (kleptoView.currentStep <= 3) {
                    styleRange = new StyleRange();
                    styleRange.start = 0;
                    styleRange.length = tEncryptedP.getCharCount();
                    styleRange.background = KleptoView.BACKGROUND_GRAY;
                }
                textN.setStyleRange(styleRange);
            }
            kleptoView.attackView.highlightEncryptedP(true);
        } else {
            // Use the StyleRange for the text fields more or less just to
            // clear out the previous highlighting.
            styleRange.background = KleptoView.BACKGROUND_GRAY;
            tEncryptedP.setStyleRange(styleRange);
            // This conditional is required to prevent an error when not using SETUP.
            if (tNPrime.getCharCount() > tEncryptedP.getCharCount())
                tNPrime.setStyleRange(styleRange);
            if (textN.getCharCount() > tEncryptedP.getCharCount())
                textN.setStyleRange(styleRange);
            kleptoView.attackView.highlightEncryptedP(false);
        }
    }

    /**
     * Generates the public key exponent E using Phi.
     */
    private void genRandomE() {
        kleptoView.klepto.rsa.generateRandomE();
        textE.setText(kleptoView.klepto.rsa.getE().toString(getDisplayRadix()));
    }

    /**
     * Calculates the private key exponent using N and Phi.
     */
    private void calcD() {
        kleptoView.klepto.rsa.calculateD();
        textD.setText(kleptoView.klepto.rsa.getD().toString(getDisplayRadix()));
    }

    /**
     * Updates the display of all the numbers according to the currently selected radix.
     */
    private void updateNumbers() {
        textP.setText(kleptoView.klepto.rsa.getP().toString(getDisplayRadix()));
        textQ.setText(kleptoView.klepto.rsa.getQ().toString(getDisplayRadix()));
        textN.setText(kleptoView.klepto.rsa.getN().toString(getDisplayRadix()));
        textE.setText(kleptoView.klepto.rsa.getE().toString(getDisplayRadix()));
        textD.setText(kleptoView.klepto.rsa.getD().toString(getDisplayRadix()));
        tID.setText(kleptoView.klepto.rsa.getID().toString(getDisplayRadix()));
        tIndex.setText(kleptoView.klepto.rsa.getIndex().toString(getDisplayRadix()));
        tSeed.setText(kleptoView.klepto.rsa.getSeed().toString(getDisplayRadix()));
        tAttackerN.setText(kleptoView.klepto.rsa.getAttackerN().toString(getDisplayRadix()));
        tAttackerE.setText(kleptoView.klepto.rsa.getAttackerE().toString(getDisplayRadix()));
        tEncryptedP.setText(kleptoView.klepto.rsa.getEncryptedP().toString(getDisplayRadix()));
        BigInteger nPrime = kleptoView.klepto.rsa.getNPrime();
        if(nPrime!=null)
        tNPrime.setText(nPrime.toString(getDisplayRadix()));
    }

    /**
     * Disables the editability of all text fields that are generated or calculated.
     */
    private void setUpEditability() {
        textN.setEditable(false);
        textN.setBackground(KleptoView.BACKGROUND_GRAY);
        textD.setEditable(false);
        textD.setBackground(KleptoView.BACKGROUND_GRAY);
        tIndex.setEditable(false);
        tIndex.setBackground(KleptoView.BACKGROUND_GRAY);
        tAttackerN.setEditable(false);
        tAttackerN.setBackground(KleptoView.BACKGROUND_GRAY);
        tAttackerE.setEditable(false);
        tAttackerE.setBackground(KleptoView.BACKGROUND_GRAY);
        tEncryptedP.setEditable(false);
        tEncryptedP.setBackground(KleptoView.BACKGROUND_GRAY);
        tNPrime.setEditable(false);
        tNPrime.setBackground(KleptoView.BACKGROUND_GRAY);
        tCiphertext.setEditable(false);
        tCiphertext.setBackground(KleptoView.BACKGROUND_GRAY);
        tDecrypt.setEditable(false);
        tDecrypt.setBackground(KleptoView.BACKGROUND_GRAY);
    }

    /**
     * Enables or disables the text fields and buttons as a set. Since the encryption process is linear, going back to
     * an earlier step means that all steps thereafter must redone in sequence. Instead of outright clearing the old
     * values, we simply disable them and turn them gray. This allows the previous value to remain for the sake of
     * comparison, but to make sure it is obvious that the value is out of date. For the fields that allow manual entry,
     * we must also set their editability state and the foreground (text) color. P and Q actually do get cleared if
     * starting from scratch.
     */
    private void updateEnabled() {
        // If starting at the beginning, clear P and Q. This is designed to prevent
        // confusion if the user has entered something and then changed the attack method.
        if (kleptoView.currentStep <= 2) {
            textP.setText(""); //$NON-NLS-1$
            textQ.setText(""); //$NON-NLS-1$
        }
        // The first step is the most complicated. This is the check to enable GenPrimes.
        // Honest and Fixed P start out with GenPrimes enabled. PRG/PRG/SETUP require an additional
        // step first and thus we must check if they have been initialized.
        if (kleptoView.currentSetting == PrimeGenSetting.HONEST || kleptoView.currentSetting == PrimeGenSetting.FIXED
                || (kleptoView.currentSetting == PrimeGenSetting.PRF && kleptoView.klepto.rsa.getInitPRF())
                || (kleptoView.currentSetting == PrimeGenSetting.PRG && kleptoView.klepto.rsa.getInitPRG())
                || (kleptoView.currentSetting == PrimeGenSetting.SETUP && kleptoView.klepto.rsa.getInitSETUP())
                || kleptoView.currentStep >= 2) {
            bGenPrimes.setEnabled(true);
            bGenAll.setEnabled(true);
            // Honest and Fixed P allow total control over P and thus it must be enabled as well.
            if (kleptoView.currentSetting == PrimeGenSetting.HONEST
                    || kleptoView.currentSetting == PrimeGenSetting.FIXED) {
                textP.setEditable(true);
                textP.setBackground(KleptoView.WHITE);
            } else {
                textP.setEditable(false);
                textP.setBackground(KleptoView.BACKGROUND_GRAY);
            }
            textP.setEnabled(true);
            textP.setForeground(KleptoView.BLACK);
            // Everything but SETUP allows Q to be manually edited.
            if (kleptoView.currentSetting != PrimeGenSetting.SETUP) {
                textQ.setEditable(true);
                textQ.setBackground(KleptoView.WHITE);
            } else {
                textQ.setEditable(false);
                textQ.setBackground(KleptoView.BACKGROUND_GRAY);
            }
            textQ.setEnabled(true);
            textQ.setForeground(KleptoView.BLACK);
            // PRF and SETUP need to allow their respective special controls.
            if (kleptoView.currentSetting == PrimeGenSetting.PRF) {
                tIndex.setEnabled(true);
                tIndex.setForeground(KleptoView.BLACK);
            } else if (kleptoView.currentSetting == PrimeGenSetting.SETUP) {
                tAttackerN.setEnabled(true);
                tAttackerN.setForeground(KleptoView.BLACK);
                tAttackerE.setEnabled(true);
                tAttackerE.setForeground(KleptoView.BLACK);
            }
        }
        // Default condition for PRF/PRG/SETUP is to disable practically everything.
        else {
            bGenPrimes.setEnabled(false);
            bGenAll.setEnabled(false);
            textP.setEnabled(false);
            textP.setEditable(false);
            textP.setBackground(KleptoView.BACKGROUND_GRAY);
            textP.setForeground(KleptoView.FOREGROUND_GRAY);
            textQ.setEnabled(false);
            textQ.setEditable(false);
            textQ.setBackground(KleptoView.BACKGROUND_GRAY);
            textQ.setForeground(KleptoView.BACKGROUND_GRAY);
            tIndex.setEnabled(false);
            tIndex.setForeground(KleptoView.BACKGROUND_GRAY);
            tAttackerN.setEnabled(false);
            tAttackerN.setForeground(KleptoView.BACKGROUND_GRAY);
            tAttackerE.setEnabled(false);
            tAttackerE.setForeground(KleptoView.BACKGROUND_GRAY);
        }
        // PQ generated, enable CalcN.
        if (kleptoView.currentStep >= 3) {
            bCalcN.setEnabled(true);
            tEncryptedP.setForeground(KleptoView.BLACK);
            tNPrime.setForeground(KleptoView.BLACK);
            tEncryptedP.setEnabled(true);
            tNPrime.setEnabled(true);
        } else {
            bCalcN.setEnabled(false);
            tEncryptedP.setForeground(KleptoView.FOREGROUND_GRAY);
            tNPrime.setForeground(KleptoView.FOREGROUND_GRAY);
            tEncryptedP.setEnabled(false);
            tNPrime.setEnabled(false);
        }
        // N calculated, enable GenE and GenD (in case the user accepts the default) and manual entry.
        // Note that from here on out we have to be careful with Fixed P. We have to catch the special
        // case were the user is creating a second key and ciphertext to save.
        if (kleptoView.currentStep >= 4
                && (kleptoView.currentSetting != PrimeGenSetting.FIXED || kleptoView.currentStep <= 9 || kleptoView.currentStep >= 11)) {
            bGenE.setEnabled(true);
            bResetE.setEnabled(true);
            bCalcD.setEnabled(true);
            textN.setForeground(KleptoView.BLACK);
            textN.setEnabled(true);
            textE.setEditable(true);
            textE.setBackground(KleptoView.WHITE);
            textE.setForeground(KleptoView.BLACK);
            textE.setEnabled(true);
        } else {
            bGenE.setEnabled(false);
            bResetE.setEnabled(false);
            bCalcD.setEnabled(false);
            textN.setForeground(KleptoView.FOREGROUND_GRAY);
            textN.setEnabled(false);
            textE.setEditable(false);
            textE.setBackground(KleptoView.BACKGROUND_GRAY);
            textE.setForeground(KleptoView.FOREGROUND_GRAY);
            textE.setEnabled(false);
        }
        // Step 5 offers just about nothing new, since D was already enabled.
        // Setp 6 enables D. The message field is already enabled by default.
        if (kleptoView.currentStep >= 6
                && (kleptoView.currentSetting != PrimeGenSetting.FIXED || kleptoView.currentStep <= 9 || kleptoView.currentStep >= 13)) {
            textD.setEnabled(true);
            textD.setForeground(KleptoView.BLACK);
        } else {
            textD.setEnabled(false);
            textD.setForeground(KleptoView.FOREGROUND_GRAY);
        }
        // Text entered, enable the decrypt button.
        if (kleptoView.currentStep >= 7
                && (kleptoView.currentSetting != PrimeGenSetting.FIXED || kleptoView.currentStep <= 9 || kleptoView.currentStep >= 14)) {
            bEncrypt.setEnabled(true);
        } else {
            bEncrypt.setEnabled(false);
        }
        // Encrypted, enable decryption and saving.
        if (kleptoView.currentStep >= 8
                && (kleptoView.currentSetting != PrimeGenSetting.FIXED || kleptoView.currentStep <= 9 || kleptoView.currentStep >= 15)) {
            tCiphertext.setForeground(KleptoView.BLACK);
            tCiphertext.setEnabled(true);
            bDecrypt.setEnabled(true);
            bSave.setEnabled(true);
        } else {
            tCiphertext.setForeground(KleptoView.FOREGROUND_GRAY);
            tCiphertext.setEnabled(false);
            bDecrypt.setEnabled(false);
            bSave.setEnabled(false);
        }
        // Decrypted.
        if (kleptoView.currentStep >= 9
                && (kleptoView.currentSetting != PrimeGenSetting.FIXED || kleptoView.currentStep <= 9 || kleptoView.currentStep >= 16)) {
            tDecrypt.setForeground(KleptoView.BLACK);
            tDecrypt.setEnabled(true);
        } else {
            tDecrypt.setForeground(KleptoView.FOREGROUND_GRAY);
            tDecrypt.setEnabled(false);
        }
    }

    /**
     * Controls the visibility of the buttons and fields for the special things required for each attack. This uses a
     * lot of checks against the currentSetting, which is possibly a bit inefficient, but copying all of the various
     * setVisible() calls is far uglier.
     */
    private void updateVisibility() {
        // For the first button/field, we use a StackLayout component to switch what is present.
        // If none of the settings are used, though, nothing should be visible.
        cExtraButton.setVisible(kleptoView.currentSetting == PrimeGenSetting.PRF
                || kleptoView.currentSetting == PrimeGenSetting.PRG
                || kleptoView.currentSetting == PrimeGenSetting.SETUP);
        cExtraLabel1.setVisible(kleptoView.currentSetting == PrimeGenSetting.PRF
                || kleptoView.currentSetting == PrimeGenSetting.PRG
                || kleptoView.currentSetting == PrimeGenSetting.SETUP);
        cExtraText1.setVisible(kleptoView.currentSetting == PrimeGenSetting.PRF
                || kleptoView.currentSetting == PrimeGenSetting.PRG
                || kleptoView.currentSetting == PrimeGenSetting.SETUP);
        // Same idea for the second, except that the PRG doesn't use it.
        cExtraLabel2.setVisible(kleptoView.currentSetting == PrimeGenSetting.PRF
                || kleptoView.currentSetting == PrimeGenSetting.SETUP);
        cExtraText2.setVisible(kleptoView.currentSetting == PrimeGenSetting.PRF
                || kleptoView.currentSetting == PrimeGenSetting.SETUP);
        bSave.setVisible(kleptoView.currentSetting == PrimeGenSetting.FIXED
                || kleptoView.currentSetting == PrimeGenSetting.SETUP);
        // Show on top the appropriate button/fields for the current setting.
        if (kleptoView.currentSetting == PrimeGenSetting.PRF) {
            ((StackLayout) cExtraButton.getLayout()).topControl = bResetIDAndIndex;
            ((StackLayout) cExtraLabel1.getLayout()).topControl = lID;
            ((StackLayout) cExtraText1.getLayout()).topControl = tID;
            ((StackLayout) cExtraLabel2.getLayout()).topControl = lIndex;
            ((StackLayout) cExtraText2.getLayout()).topControl = tIndex;
        } else if (kleptoView.currentSetting == PrimeGenSetting.PRG) {
            ((StackLayout) cExtraButton.getLayout()).topControl = bGenSeed;
            ((StackLayout) cExtraLabel1.getLayout()).topControl = lSeed;
            ((StackLayout) cExtraText1.getLayout()).topControl = tSeed;
        } else if (kleptoView.currentSetting == PrimeGenSetting.SETUP) {
            ((StackLayout) cExtraButton.getLayout()).topControl = bGenAttacker;
            ((StackLayout) cExtraLabel1.getLayout()).topControl = lAttackerN;
            ((StackLayout) cExtraText1.getLayout()).topControl = tAttackerN;
            ((StackLayout) cExtraLabel2.getLayout()).topControl = lAttackerE;
            ((StackLayout) cExtraText2.getLayout()).topControl = tAttackerE;
        }
        // Refresh the components.
        cExtraButton.layout();
        cExtraLabel1.layout();
        cExtraText1.layout();
        cExtraLabel2.layout();
        cExtraText2.layout();
        // The extra fields for the SETUP can simply be set to visible or invisible without complication.
        tEncryptedP.setVisible(kleptoView.currentSetting == PrimeGenSetting.SETUP);
        lEncryptedP.setVisible(kleptoView.currentSetting == PrimeGenSetting.SETUP);
        tNPrime.setVisible(kleptoView.currentSetting == PrimeGenSetting.SETUP);
        lNPrime.setVisible(kleptoView.currentSetting == PrimeGenSetting.SETUP);
    }

    /**
     * Basic message display. Used mostly for invalid input warnings.
     *
     * @param title The title of the dialog box.
     * @param message The message text.
     */
    private void showDialog(String title, String message) {
        MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
        mb.setText(title);
        mb.setMessage(message);
        mb.open();
    }

    /**
     * Controller class for changing attack modes. Called whenever the user selects the attack mode dropdown. This sets
     * up the display and the resets the background variables.
     */
    private void changeAttackMode() {
        // Start over at step 1.
        kleptoView.currentStep = 1;
        // In each case, first check that the user didn't just reselect the same mode.
        // If it is different, change the setting appropriately and take care of the
        // tab visibility. InitFixed and FixedTextsSaved must be reset if not using Fixed P.
        switch (cAttack.getSelectionIndex()) {
            case 0:
                if (kleptoView.currentSetting != PrimeGenSetting.HONEST) {
                    kleptoView.currentSetting = PrimeGenSetting.HONEST;
                    kleptoView.setAttackTabVisibility(false);
                    kleptoView.klepto.rsa.setInitFixed(false);
                    kleptoView.klepto.attack.resetFixedTextsSaved();
                }
                break;
            case 1:
                if (kleptoView.currentSetting != PrimeGenSetting.FIXED) {
                    kleptoView.currentSetting = PrimeGenSetting.FIXED;
                    kleptoView.setAttackTabVisibility(true);
                    // Leave initFixed and FixedTextsSaved as they are.
                    // Only set them to false if one of the other radio buttons has been selected.
                }
                break;
            case 2:
                if (kleptoView.currentSetting != PrimeGenSetting.PRF) {
                    kleptoView.currentSetting = PrimeGenSetting.PRF;
                    kleptoView.setAttackTabVisibility(false);
                    kleptoView.klepto.rsa.setInitFixed(false);
                    kleptoView.klepto.attack.resetFixedTextsSaved();
                }
                break;
            case 3:
                if (kleptoView.currentSetting != PrimeGenSetting.PRG) {
                    kleptoView.currentSetting = PrimeGenSetting.PRG;
                    kleptoView.setAttackTabVisibility(false);
                    kleptoView.klepto.rsa.setInitFixed(false);
                    kleptoView.klepto.attack.resetFixedTextsSaved();
                }
                break;
            case 4:
                if (kleptoView.currentSetting != PrimeGenSetting.SETUP) {
                    kleptoView.currentSetting = PrimeGenSetting.SETUP;
                    kleptoView.setAttackTabVisibility(true);
                    kleptoView.klepto.rsa.setInitFixed(false);
                    kleptoView.klepto.attack.resetFixedTextsSaved();
                    // When SETUP is selected, the bit count must be at least 20 and divisible by 4.
                    // This check is implemented in the setBits() routine, so it can be
                    // invoked just by sending in the current value.
                    // Also, if the bit count is > 256, we should warn the user that it may take
                    // a while to generate the primes; this check is also in setBits().
                    setBits(getBits());
                }
                break;
        }
        // General display stuff: resetting all the various sections.
        updateEnabled();
        kleptoView.attackView.clearAttackDisplay();
        highlightEncryptedP();
        updateVisibility();
        kleptoView.attackView.updateVisibility();
        updateDescription();
    }

    /**
     * Does all the display work for generating the primes. Called when the corresponding button is pressed.
     */
    private void generatePrimes() {
        // If using PRF, try to read the ID. If the user has entered his or her own, but
        // it is for some reason invalid, then show an error message and break.
        if (kleptoView.currentSetting == PrimeGenSetting.PRF) {
            BigInteger id = BigInteger.ZERO;
            try {
                id = new BigInteger(tID.getText(), getDisplayRadix());
            } catch (NumberFormatException nfe) {
                showDialog(Messages.RSAKeyView_Invalid_Input, Messages.RSAKeyView_Invalid_ID);
                return;
            }
            kleptoView.klepto.rsa.setID(id);
        }
        // If using PRG, try to read the seed. If the user has entered his or her own, but
        // it is for some reason invalid, then show an error message and break.
        else if (kleptoView.currentSetting == PrimeGenSetting.PRG) {
            BigInteger seed = BigInteger.ZERO;
            try {
                seed = new BigInteger(tSeed.getText(), getDisplayRadix());
            } catch (NumberFormatException nfe) {
                showDialog(Messages.RSAKeyView_Invalid_Input, Messages.RSAKeyView_Invaild_Seed);
                return;
            }
            kleptoView.klepto.rsa.setSeed(seed);
        }
        // If using Fixed P and the first set has already been saved, then the step needs to
        // be advanced to a yet higher level. Otherwise, advance to 3.
        if (kleptoView.currentSetting == PrimeGenSetting.FIXED && kleptoView.currentStep >= 9) {
            kleptoView.currentStep = 10;
        } else {
            kleptoView.currentStep = 3;
        }
        // Actually generate the primes and do the rest of the disply work.
        genPrimes();
        updateEnabled();
        kleptoView.attackView.clearAttackDisplay();
        updateDescription();
    }

    /**
     * If the user types something in the prime number fields, we have to reset the step back to that level and update
     * the display accordingly.
     */
    private void primeEntered() {
        if (kleptoView.currentSetting == PrimeGenSetting.FIXED && kleptoView.currentStep >= 9) {
            kleptoView.currentStep = 10;
        } else {
            kleptoView.currentStep = 3;
        }
        updateEnabled();
        kleptoView.attackView.clearAttackDisplay();
        updateDescription();
    }

    /**
     * Does all the display work for calculating N. Called when the corresponding button is pressed.
     */
    private void calculateN() {
        // First try to read the primes in, in case the user entered them manually.
        // If something is wrong, display a message and break.
        BigInteger p = BigInteger.ZERO;
        BigInteger q = BigInteger.ZERO;
        try {
            p = new BigInteger(textP.getText(), getDisplayRadix());
        } catch (NumberFormatException nfe) {
            showDialog(Messages.RSAKeyView_Invalid_Input, Messages.RSAKeyView_Invalid_P);
            return;
        }
        try {
            q = new BigInteger(textQ.getText(), getDisplayRadix());
        } catch (NumberFormatException nfe) {
            showDialog(Messages.RSAKeyView_Invalid_Input, Messages.RSAKeyView_Invalid_Q);
            return;
        }
        // Check that every condition for the primes is met:
        // they must actually be prime, be exactly half the length of the given bit length,
        // and not equal to each other.
        if (!kleptoView.klepto.rsa.verifyPrime(p)) {
            showDialog(Messages.RSAKeyView_Invalid_Input, Messages.RSAKeyView_Invaild_P_Prime);
        } else if (!kleptoView.klepto.rsa.verifyHalfBitLength(p)) {
            showDialog(Messages.RSAKeyView_Invalid_Input, Messages.RSAKeyView_Invalid_P_Length + getBits() / 2
                    + Messages.RSAKeyView_Invalid_Length);
        } else if (!kleptoView.klepto.rsa.verifyPrime(q)) {
            showDialog(Messages.RSAKeyView_Invalid_Input, Messages.RSAKeyView_Invalid_Q_Prime);
        } else if (!kleptoView.klepto.rsa.verifyHalfBitLength(q)) {
            showDialog(Messages.RSAKeyView_Invalid_Input, Messages.RSAKeyView_Invalid_Q_Length + getBits() / 2
                    + Messages.RSAKeyView_Invalid_Length);
        } else if (!kleptoView.klepto.rsa.verifyNotEqual(p, q)) {
            showDialog(Messages.RSAKeyView_Invalid_Input, Messages.RSAKeyView_Invalid_Equal);
        } else {
            // If everything is good, save the primes and update the step.
            kleptoView.klepto.rsa.setP(p);
            kleptoView.klepto.rsa.setQ(q);
            kleptoView.klepto.rsa.setInitFixed(true);
            if (kleptoView.currentSetting == PrimeGenSetting.FIXED && kleptoView.currentStep >= 10) {
                kleptoView.currentStep = 11;
            } else {
                kleptoView.currentStep = 4;
            }
            // Actually calculate N and update the display.
            calcN();
            updateEnabled();
            kleptoView.attackView.clearAttackDisplay();
            updateDescription();
            // If the N is smaller than whatever E was previously set to, reset E.
            if (kleptoView.klepto.rsa.getE().compareTo(kleptoView.klepto.rsa.getN()) >= 0) {
                kleptoView.klepto.rsa.setDefaultE();
                textE.setText(kleptoView.klepto.rsa.getE().toString(getDisplayRadix()));
            }
        }
    }

    /**
     * Does all the display work for generating E. Called when the corresponding button is pressed.
     */
    private void generateE() {
        // Update the step.
        if (kleptoView.currentSetting == PrimeGenSetting.FIXED && kleptoView.currentStep >= 11) {
            kleptoView.currentStep = 12;
        } else {
            kleptoView.currentStep = 5;
        }
        // Actually generate E and then update the display.
        genRandomE();
        updateEnabled();
        kleptoView.attackView.clearAttackDisplay();
        updateDescription();
    }

    /**
     * Resets the E value to the default (65537 or 17 if N < 65537).
     */
    private void resetE() {
        // Update the step.
        if (kleptoView.currentSetting == PrimeGenSetting.FIXED && kleptoView.currentStep >= 11) {
            kleptoView.currentStep = 12;
        } else {
            kleptoView.currentStep = 5;
        }
        // Actually reset E and then update the display.
    	kleptoView.klepto.rsa.setDefaultE();
        textE.setText(kleptoView.klepto.rsa.getE().toString(getDisplayRadix()));
        updateEnabled();
        kleptoView.attackView.clearAttackDisplay();
        updateDescription();
    }

    /**
     * If the user types something in the E field, we have to reset the step back to that level and update the display
     * accordingly.
     */
    private void eEntered() {
        if (kleptoView.currentSetting == PrimeGenSetting.FIXED && kleptoView.currentStep >= 11) {
            kleptoView.currentStep = 12;
        } else {
            kleptoView.currentStep = 5;
        }
        updateEnabled();
        kleptoView.attackView.clearAttackDisplay();
        updateDescription();
    }

    /**
     * Does all the display work for calculating D. Called when the corresponding button is pressed.
     */
    private void calculateD() {
        // First try to read E in, in case the user entered it manually.
        // If something is wrong, display a message and break.
        BigInteger e = BigInteger.ZERO;
        try {
            e = new BigInteger(textE.getText(), getDisplayRadix());
        } catch (NumberFormatException nfe) {
            showDialog(Messages.RSAKeyView_Invalid_Input, Messages.RSAKeyView_Invalid_E);
            return;
        }
        // Make sure that E meets the necessary criteria: it must be greater than 1 but
        // less than Phi, and relatively prime to Phi.
        if (!kleptoView.klepto.rsa.verifyESize(e)) {
            showDialog(Messages.RSAKeyView_Invalid_Input, Messages.RSAKeyView_Invalid_E_Size
                    + kleptoView.klepto.rsa.getPhi().toString(getDisplayRadix()) + Messages.RSAKeyView_Invalid_E_Size2);
        } else if (!kleptoView.klepto.rsa.verifyERelativePrime(e)) {
            showDialog(Messages.RSAKeyView_Invalid_Input, Messages.RSAKeyView_Invalid_E_Coprime
                    + kleptoView.klepto.rsa.getPhi().toString(getDisplayRadix())
                    + Messages.RSAKeyView_Invalid_E_Coprime2);
        } else {
            // If all is wll, save E and update the step.
            kleptoView.klepto.rsa.setE(e);
            // This checks >= 11 instead of 12 since 12 is changing E and that is usually
            // actually left alone and skipped.
            if (kleptoView.currentSetting == PrimeGenSetting.FIXED && kleptoView.currentStep >= 11) {
                kleptoView.currentStep = 13;
            } else {
                kleptoView.currentStep = 6;
            }
            // If there is already text in the message field, bump the step up another level.
            if (tMessage.getText().trim().length() > 0)
                kleptoView.currentStep++;
            // Actually calculate D and update the display.
            calcD();
            updateEnabled();
            kleptoView.attackView.clearAttackDisplay();
            updateDescription();
        }
    }

    /**
     * Generate/calculate everything all at once.
     */
    private void generateAll() {
        // Jump the step all the way ahead to the message entry.
        if (kleptoView.currentSetting == PrimeGenSetting.FIXED && kleptoView.currentStep >= 9) {
            kleptoView.currentStep = 13;
        } else {
            kleptoView.currentStep = 6;
        }
        // And if there is already text entered in the message field, bump it up another step.
        if (tMessage.getText().trim().length() > 0)
            kleptoView.currentStep++;
        // Do all the main generation/calculations in one foul swoop. Only reset E if necessary.
        genPrimes();
        calcN();
        if (!kleptoView.klepto.rsa.verifyESize(kleptoView.klepto.rsa.getE())
                || !kleptoView.klepto.rsa.verifyERelativePrime(kleptoView.klepto.rsa.getE())) {
            kleptoView.klepto.rsa.setDefaultE();
            textE.setText(kleptoView.klepto.rsa.getE().toString(getDisplayRadix()));
        }
        calcD();
        // Update the display.
        updateEnabled();
        kleptoView.attackView.clearAttackDisplay();
        updateDescription();
    }

    /**
     * The radix radio buttons control how the numbers are displayed. This is called when one of the radio buttons is
     * selected. If the value has actually changed, save it, update the numbers, and highlight if necessary.
     *
     * @param newValue The newly selected radix.
     */
    private void changeRadix(int newValue) {
        if (getDisplayRadix() != newValue) {
            setDisplayRadix(newValue);
            updateNumbers();
            kleptoView.attackView.updateNumbers();
            highlightEncryptedP();
        }
    }

    /**
     * When the user enters/changes a message, the step must be advanced or regressed as necessary to that level and the
     * display updated appropriately. If the user encrypts some text and then changes the plaintext, things get
     * confusing and/or screwy if the user then tries to decrypt, so it's better to just disable the decrypt button
     * until the new plaintext is encrypted, which automatically happens by setting the step as is done here.
     */
    private void messageChanged() {
        if (kleptoView.currentStep >= 6) {
            if (kleptoView.currentSetting == PrimeGenSetting.FIXED && kleptoView.currentStep >= 13) {
                kleptoView.currentStep = 14;
            } else {
                kleptoView.currentStep = 7;
            }
            updateEnabled();
            kleptoView.attackView.clearAttackDisplay();
            updateDescription();
        }
    }

    /**
     * Does all the display work for encrypting the plaintext. Called when the corresponding button is pressed.
     */
    private void encryptMessage() {
        // Update the step.
        if (kleptoView.currentSetting == PrimeGenSetting.FIXED && kleptoView.currentStep >= 13) {
            kleptoView.currentStep = 15;
        } else {
            kleptoView.currentStep = 8;
        }
        // If the plaintext isn't blank, save it, encrypt it, and update the display.
        if (tMessage.getText().length() > 0) {
            kleptoView.klepto.rsa.setMessage(tMessage.getText());
            kleptoView.klepto.rsa.encrypt();
            tCiphertext.setText(kleptoView.klepto.rsa.getCipherHex());
            updateEnabled();
            kleptoView.attackView.clearAttackDisplay();
            updateDescription();
        }
    }

    /**
     * Controls all the work done to save the public key and ciphertext.
     */
    private void savePublicData() {
        // Update the step.
        if (kleptoView.currentSetting == PrimeGenSetting.FIXED && kleptoView.currentStep >= 15) {
            kleptoView.currentStep = 16;
        } else {
            kleptoView.currentStep = 9;
        }
        // Save the text and update the displays on the attack side.
        if (kleptoView.currentSetting == PrimeGenSetting.FIXED) {
            kleptoView.attackView.saveCipherTextFixed();
            kleptoView.attackView.updateEnabled();
            kleptoView.attackView.updateDescriptionFixed();
        } else if (kleptoView.currentSetting == PrimeGenSetting.SETUP) {
            kleptoView.attackView.saveCipherTextSETUP();
            kleptoView.attackView.updateEnabled();
            kleptoView.attackView.updateDescriptionSETUP();
            // Make this call just for the sake of the highlighting on the attack tab.
            highlightEncryptedP();
        }
        // Update the description text.
        updateDescription();
    }

    /**
     * Does all the display work for decrypting the ciphertext.
     */
    private void decryptCiphertext() {
        // Update the step (if necessary).
        if (kleptoView.currentStep < 9)
            kleptoView.currentStep = 9;
        // Actually decrypt the ciphertext.
        kleptoView.klepto.rsa.decrypt();
        // Update display.
        tDecrypt.setText(kleptoView.klepto.rsa.getDeciphered());
        updateEnabled();
    }

    /**
     * Does all the display work for resetting the ID and index.
     */
    private void resetIDAndIndex() {
        // Update the step.
        kleptoView.currentStep = 2;
        // Generate a new ID, reset the index, and set initPRF to true.
        kleptoView.klepto.rsa.genNewID();
        kleptoView.klepto.rsa.resetIndex();
        kleptoView.klepto.rsa.setInitPRF(true);
        // Update the display.
        tID.setText(kleptoView.klepto.rsa.getID().toString(getDisplayRadix()));
        tIndex.setText(kleptoView.klepto.rsa.getIndex().toString(getDisplayRadix()));
        updateEnabled();
        updateDescription();
    }

    /**
     * If the user types something in the ID field, we have to reset the step back to that level and update the display
     * accordingly.
     */
    private void idEntered() {
        // Update the step.
        kleptoView.currentStep = 2;
        // Generate a new ID, reset the index, and set initPRF to true.
        kleptoView.klepto.rsa.resetIndex();
        kleptoView.klepto.rsa.setInitPRF(true);
        // Update the display.
        tIndex.setText(kleptoView.klepto.rsa.getIndex().toString(getDisplayRadix()));
        updateEnabled();
        updateDescription();
    }

    /**
     * Does all the display work for generating a new seed.
     */
    private void generateSeed() {
        // Update the step.
        kleptoView.currentStep = 2;
        // Generate a new seed and set initPRG to true.
        kleptoView.klepto.rsa.genNewSeed();
        kleptoView.klepto.rsa.setInitPRG(true);
        // Update the display.
        tSeed.setText(kleptoView.klepto.rsa.getSeed().toString(getDisplayRadix()));
        updateEnabled();
        updateDescription();
    }

    /**
     * If the user types something in the seed field, we have to reset the step back to that level and update the
     * display accordingly.
     */
    private void seedEntered() {
        // Update the step.
        kleptoView.currentStep = 2;
        // Generate a new seed and set initPRG to true.
        kleptoView.klepto.rsa.setInitPRG(true);
        // Update the display.
        updateEnabled();
        updateDescription();
    }

    /**
     * Does all the display work for generating the attacker's keys.
     */
    private void generateAttackerKeys() {
        // Update the step.
        kleptoView.currentStep = 2;
        // Generate new attacker keys and set initSTEUP to true.
        kleptoView.klepto.rsa.genAttackerKeys();
        kleptoView.klepto.rsa.setInitSETUP(true);
        // Update the display.
        tAttackerN.setText(kleptoView.klepto.rsa.getAttackerN().toString(getDisplayRadix()));
        tAttackerE.setText(kleptoView.klepto.rsa.getAttackerE().toString(getDisplayRadix()));
        updateEnabled();
        kleptoView.attackView.clearAttackDisplay();
        highlightEncryptedP();
        updateDescription();
    }

    /**
     * Changes the text of the description to reflect the current setting and step. For each setting, there is a linear
     * sequence of steps. They are added one after the other depending on where the user is at. See the notes for
     * KleptoView.currentStep for more information on what each internal step means. The external steps (i.e. what the
     * user sees) are somewhat arbitrarily based on what steps are important for the user.
     */
    private void updateDescription() {
    	// Figure out where the first colon appears. This will determine how much of each
    	// line should be in bold. (In English, it's the first seven ("Step 1:") and
    	// in German the first ten ("Schritt 1:").)
		int boldlength = Messages.RSAKeyView_Honest_Step1.indexOf(":") + 1;

        // We need enough style counts to make each step numerator bold and the last step
        // highlighted. Start by using the current internal step count as a base.
        int styleCount = kleptoView.currentStep;

        // Since many internal steps do not correspond to external steps as far the
        // description text that the user sees goes, we must subtract steps as appropriate.
        if (styleCount >= 14 && kleptoView.currentSetting == PrimeGenSetting.FIXED)
            styleCount--;
        if (styleCount >= 12 && kleptoView.currentSetting == PrimeGenSetting.FIXED)
            styleCount--;
        if (styleCount >= 11 && kleptoView.currentSetting == PrimeGenSetting.FIXED)
            styleCount--;
        if (styleCount >= 10 && kleptoView.currentSetting == PrimeGenSetting.FIXED)
            styleCount--;
        if (styleCount >= 7)
            styleCount--;
        if (styleCount >= 5)
            styleCount--;
        if (styleCount >= 2
                && (kleptoView.currentSetting == PrimeGenSetting.HONEST || kleptoView.currentSetting == PrimeGenSetting.FIXED))
            styleCount--;

        // Add one more for the highlighting of the most recent text.
        styleCount++;
        // Set up each of the style ranges for the step numerators
        // and add them all to an array.
        // Each must be bold and have a length of seven for English or ten for German.
        StyleRange[] styles = new StyleRange[styleCount];
        for (int i = 0; i < styles.length - 1; i++) {
            StyleRange sr = new StyleRange();
            sr.length = boldlength; // 7 in English, 10 in German
            sr.fontStyle = SWT.BOLD;
            styles[i] = sr;
        }

        // After this, boldlength will be used to determine how much space in each
        // line should be ignored to do the highlighting correctly. Specifically,
        // boldlength will be subtracted from the line's length to get the highlighted
        // length. (The space after the colon should be ignored, hence adding one.)
        // highlightoffset marks where to start the highlighting and is two greater
        // to account for the line breaks.
        boldlength++;
        int highlightoffset = boldlength + 2;

        // Start the description and set the current styleRange count to zero.
        StringBuilder sbDescription = new StringBuilder(100);
        int srCount = 0;
        // Create the highlighted styleRange and save it as the last entry of the array.
        StyleRange sr = new StyleRange();
        sr.start = boldlength; // 8 in English, 11 in German
        sr.background = KleptoView.YELLOW;
        styles[styles.length - 1] = sr;

        switch (kleptoView.currentSetting) {
            case HONEST:
                // Set the title.
                lDescTitle.setText(Messages.RSAKeyView_Honest_Title);
                if (kleptoView.currentStep >= 1) {
                    // The first step is a bit different than the rest.
                    // The first bold styleRange must start at zero. The length and font
                    // have been already defined; it's already good to go.
                    styles[srCount].start = 0;
                    // Then define the length of the highlighted styleRange to be the length
                    // of the step 1 text minus the boldface step numerator.
                    styles[styles.length - 1].length = Messages.RSAKeyView_Honest_Step1.length() - boldlength;
                    // Add the text of step 1 to the description.
                    sbDescription.append(Messages.RSAKeyView_Honest_Step1);
                }
                if (kleptoView.currentStep >= 3) {
                	// The normal pattern is as follows. First move the next bold style
                	// into position. Adding two accounts for the line breaks.
                    styles[++srCount].start = sbDescription.length() + 2;
                    // Define the start of the highlighted styleRange; it will be the current
                    // length plus the offset to skip over the line breaks and step numerator.
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    // Define the highlighted length; it will be the length of the line minus
                    // the bold step part.
                    styles[styles.length - 1].length = Messages.RSAKeyView_Honest_Step2.length() - boldlength;
                    // Add the line breaks and the actual line of text.
                    sbDescription.append("\n\n" + Messages.RSAKeyView_Honest_Step2); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 4) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_Honest_Step3.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_Honest_Step3); //$NON-NLS-1$
                }
//                if (kleptoView.currentStep >= 5) {
//                    styles[++srCount].start = strDescription.length() + 2;
//                    styles[styles.length - 1].start = strDescription.length() + 10;
//                    styles[styles.length - 1].length = Messages.RSAKeyView_Honest_Step4.length() - 8;
//                    strDescription += "\n\n" + Messages.RSAKeyView_Honest_Step4; //$NON-NLS-1$
//                }
                if (kleptoView.currentStep >= 6) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_Honest_Step4.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_Honest_Step4); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 8) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_Honest_Step5.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_Honest_Step5); //$NON-NLS-1$
                }
                break;
            case FIXED:
                lDescTitle.setText(Messages.RSAKeyView_Fixed_Title);
                if (kleptoView.currentStep >= 1) {
                    styles[srCount].start = 0;
                    styles[styles.length - 1].length = Messages.RSAKeyView_Fixed_Step1.length() - boldlength;
                    sbDescription.append(Messages.RSAKeyView_Fixed_Step1);
                }
                if (kleptoView.currentStep >= 3) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_Fixed_Step2.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_Fixed_Step2); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 4) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_Fixed_Step3.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_Fixed_Step3); //$NON-NLS-1$
                }
//                if (kleptoView.currentStep >= 5) {
//                    styles[++srCount].start = strDescription.length() + 2;
//                    styles[styles.length - 1].start = strDescription.length() + 10;
//                    styles[styles.length - 1].length = Messages.RSAKeyView_Fixed_Step4.length() - 8;
//                    strDescription += "\n\n" + Messages.RSAKeyView_Fixed_Step4; //$NON-NLS-1$
//                }
                if (kleptoView.currentStep >= 6) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_Fixed_Step4.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_Fixed_Step4); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 8) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_Fixed_Step5.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_Fixed_Step5); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 9) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_Fixed_Step6.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_Fixed_Step6); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 13) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_Fixed_Step7.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_Fixed_Step7); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 15) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_Fixed_Step8.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_Fixed_Step8); //$NON-NLS-1$
                }
                break;
            case PRF:
                lDescTitle.setText(Messages.RSAKeyView_PRF_Title);
                if (kleptoView.currentStep >= 1) {
                    styles[srCount].start = 0;
                    styles[styles.length - 1].length = Messages.RSAKeyView_PRF_Step1.length() - boldlength;
                    sbDescription.append(Messages.RSAKeyView_PRF_Step1);
                }
                if (kleptoView.currentStep >= 2) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_PRF_Step2.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_PRF_Step2); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 3) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_PRF_Step3.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_PRF_Step3); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 4) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_PRF_Step4.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_PRF_Step4); //$NON-NLS-1$
                }
//                if (kleptoView.currentStep >= 5) {
//                    styles[++srCount].start = strDescription.length() + 2;
//                    styles[styles.length - 1].start = strDescription.length() + 10;
//                    styles[styles.length - 1].length = Messages.RSAKeyView_PRF_Step5.length() - 8;
//                    strDescription += "\n\n" + Messages.RSAKeyView_PRF_Step5; //$NON-NLS-1$
//                }
                if (kleptoView.currentStep >= 6) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_PRF_Step5.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_PRF_Step5); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 8) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_PRF_Step6.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_PRF_Step6); //$NON-NLS-1$
                }
                break;
            case PRG:
                lDescTitle.setText(Messages.RSAKeyView_PRG_Title);
                if (kleptoView.currentStep >= 1) {
                    styles[srCount].start = 0;
                    styles[styles.length - 1].length = Messages.RSAKeyView_PRG_Step1.length() - boldlength;
                    sbDescription.append(Messages.RSAKeyView_PRG_Step1);
                }
                if (kleptoView.currentStep >= 2) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_PRG_Step2.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_PRG_Step2); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 3) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_PRG_Step3.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_PRG_Step3); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 4) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_PRG_Step4.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_PRG_Step4); //$NON-NLS-1$
                }
//                if (kleptoView.currentStep >= 5) {
//                    styles[++srCount].start = strDescription.length() + 2;
//                    styles[styles.length - 1].start = strDescription.length() + 10;
//                    styles[styles.length - 1].length = Messages.RSAKeyView_PRG_Step5.length() - 8;
//                    strDescription += "\n\n" + Messages.RSAKeyView_PRG_Step5; //$NON-NLS-1$
//                }
                if (kleptoView.currentStep >= 6) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_PRG_Step5.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_PRG_Step5); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 8) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_PRG_Step6.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_PRG_Step6); //$NON-NLS-1$
                }
                break;
            case SETUP:
                lDescTitle.setText(Messages.RSAKeyView_SETUP_Title);
                if (kleptoView.currentStep >= 1) {
                    styles[srCount].start = 0;
                    styles[styles.length - 1].length = Messages.RSAKeyView_SETUP_Step1.length() - boldlength;
                    sbDescription.append(Messages.RSAKeyView_SETUP_Step1);
                }
                if (kleptoView.currentStep >= 2) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_SETUP_Step2.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_SETUP_Step2); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 3) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_SETUP_Step3.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_SETUP_Step3); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 4) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_SETUP_Step4.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_SETUP_Step4); //$NON-NLS-1$
                }
//                if (kleptoView.currentStep >= 5) {
//                    styles[++srCount].start = strDescription.length() + 2;
//                    styles[styles.length - 1].start = strDescription.length() + 10;
//                    styles[styles.length - 1].length = Messages.RSAKeyView_SETUP_Step5.length() - 8;
//                    strDescription += "\n\n" + Messages.RSAKeyView_SETUP_Step5; //$NON-NLS-1$
//                }
                if (kleptoView.currentStep >= 6) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_SETUP_Step5.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_SETUP_Step5); //$NON-NLS-1$
                }
                if (kleptoView.currentStep >= 8) {
                    styles[++srCount].start = sbDescription.length() + 2;
                    styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
                    styles[styles.length - 1].length = Messages.RSAKeyView_SETUP_Step6.length() - boldlength;
                    sbDescription.append("\n\n" + Messages.RSAKeyView_SETUP_Step6); //$NON-NLS-1$
                }
                break;
        }
        // Set the entirety of the built-up text as the description and apply the
        // entire array of styleRanges.
        stSpecificDescription.setText(sbDescription.toString());
        stSpecificDescription.setStyleRanges(styles);
    }

    /**
     * Set up the listeners for the buttons and fields to accept user input. In most cases, the enabled and visible
     * buttons and text fields are updated appropriately, calculations are done in other classes, and the results are
     * displayed.
     */
    private void setUpListeners() {
        // Save the bitCount value. setBits() does all the checking and updating.
        // The focusGained() part has to be there but it will do nothing.
        tBits.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
                // Save the local change.
                setBits(getBitsAsString());
            }

            public void focusGained(FocusEvent e) {
                // do nothing.
            }
        });

        // Save the bitCount value. setBits() does all the checking and updating.
        // This is activated when the user selects an entry from the dropdown or hits enter.
        tBits.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                // Save the local change.
                setBits(getBitsAsString());
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        cAttack.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                changeAttackMode();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        bGenPrimes.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                generatePrimes();
            }
        });

        textP.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                primeEntered();
//                if(e.keyCode == '\r') {
//                	textQ.setFocus();
//                }
            }

            public void keyPressed(KeyEvent e) {
                // Do nothing.
            }
        });

        textQ.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                primeEntered();
            }

            public void keyPressed(KeyEvent e) {
                // Do nothing.
            }
        });

        bCalcN.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent se) {
                calculateN();
            }
        });

        bGenE.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                generateE();
            }
        });

        bResetE.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                resetE();
            }
        });

        textE.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                eEntered();
            }

            public void keyPressed(KeyEvent e) {
                // Do nothing.
            }
        });

        bCalcD.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent se) {
                calculateD();
            }
        });

        bGenAll.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                generateAll();
            }
        });

        radioBinary.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                changeRadix(2);
            }
        });

        radioDecimal.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                changeRadix(10);
            }
        });

        radioHex.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                changeRadix(16);
            }
        });

        tMessage.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                messageChanged();
            }
        });

        bEncrypt.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                encryptMessage();
            }
        });

        bSave.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                savePublicData();
            }
        });

        bDecrypt.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                decryptCiphertext();
            }
        });

        bResetIDAndIndex.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                resetIDAndIndex();
            }
        });

        tID.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                idEntered();
            }

            public void keyPressed(KeyEvent e) {
                // Do nothing.
            }
        });

        bGenSeed.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                generateSeed();
            }
        });

        tSeed.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                seedEntered();
            }

            public void keyPressed(KeyEvent e) {
                // Do nothing.
            }
        });

        bGenAttacker.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                generateAttackerKeys();
            }
        });

        textP.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
                    e.doit = true;
                }
            }
        });

        textQ.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
                    e.doit = true;
                }
            }
        });

        textE.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
                    e.doit = true;
                }
            }
        });

        tID.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
                    e.doit = true;
                }
            }
        });

        tSeed.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
                    e.doit = true;
                }
            }
        });

        tMessage.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
                    e.doit = true;
                }
            }
        });

        tCiphertext.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
                    e.doit = true;
                }
            }
        });

    }

} // end RSAKeyView class
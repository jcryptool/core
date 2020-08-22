//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2015, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.modern.stream.arc4.ui;

import java.util.ArrayList;
import java.util.Random;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.modern.stream.arc4.Arc4Plugin;

/**
 * the page we interact with.
 * 
 * @author David
 *
 */
public class Arc4WizardPage extends WizardPage implements Listener {

    /**
     * HelperForLabelUnderKeyInput.
     */
    boolean Helper = false;

    /**
     * w for spritz.
     */
    int w = 3;

    /**
     * Group for key.
     */
    private Group keyIVSizeGroup;

    /**
     * encrypt button.
     */
    private Button encrypt;

    /**
     * decrypt button.
     */
    private Button decrypt;

    /**
     * combo for byte dropdown.
     */
    private Combo dropdown;

    /**
     * type of inputs at dropdown.
     */
    private String inputs[] = { "128 Byte", "192 Byte", "256 Byte", Messages.Arc4Page_generate_null };

    /**
     * group for the algorithm choosing.
     */
    private Group algoGroup;

    /**
     * arc4 or.
     */
    private Button arc4;

    /**
     * Spritz.
     */
    private Button spritz;

    /**
     * dropdown list for Spritz w.
     */
    private Combo dropDownSpritz;

    /**
     * if its Arc4 it is true here.
     */
    private boolean isArc4 = true;

    /**
     * if he wants a random number true here.
     */
    private boolean isRand = true;

    /**
     * Clearing Flag is used also if the programm says it wont be used....
     */
    // private boolean CLEARING_FLAG = false;

    /**
     * keyGroup.
     */
    private Group keyValueGroup;

    /**
     * Button for hex input.
     */
    private Button hexadecimalKeyInputButton;

    /**
     * button for binary input.
     */
    private Button binaryKeyInputButton;

    /**
     * button for random Button.
     */
    private Button randomButton;

    /**
     * Button for zero.
     */
    private Button zeroButton;

    /**
     * Key Text input field.
     */
    private Text keyText;

    /**
     * Counter for KeyDigits.
     */
    private Label keyDigitCountLabel;

    /**
     * The key input.
     */
    private String keyValue = ""; //$NON-NLS-1$

    /**
     * If Key is Hex.
     */
    private boolean keyFormatIsHexadecimal = true;

    /**
     * array list for Hex testing.
     */
    private ArrayList<String> hexValues = new ArrayList<String>(16);

	private Composite pageComposite;

    /**
     * create Wizard Page set Title and Message; setup the Hexadecimal values.
     */
    public Arc4WizardPage() {
        super(".", "ARC4/Spritz", null); //$NON-NLS-1$ //$NON-NLS-2$
        setTitle(Messages.Arc4_name);
        setMessage(Messages.Arc4Page_message);
        setupHexadecimalValues();

    }

    /**
     * Sets up an ArrayList with hexadecimal values.
     */
    private void setupHexadecimalValues() {
        hexValues.add(0, "0");
        hexValues.add(1, "1");
        hexValues.add(2, "2");
        hexValues.add(3, "3");
        hexValues.add(4, "4");
        hexValues.add(5, "5");
        hexValues.add(6, "6");
        hexValues.add(7, "7");
        hexValues.add(8, "8");
        hexValues.add(9, "9");
        hexValues.add(10, "A");
        hexValues.add(11, "B");
        hexValues.add(12, "C");
        hexValues.add(13, "D");
        hexValues.add(14, "E");
        hexValues.add(15, "F");
    }

    /**
     *
     * creating the whole wizard.
     *
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        pageComposite = new Composite(parent, SWT.NULL);
        pageComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        encrypt_decrypt(pageComposite);
        arc4_spritz(pageComposite);
        createKeyValueGroup(pageComposite);

        encrypt.setSelection(true);
        arc4.setSelection(true);
        randomButton.setSelection(true);

        hexadecimalKeyInputButton.setSelection(true);

        pageComposite.setLayout(new GridLayout());

        setControl(pageComposite);
        setPageComplete(mayFinish());

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), Arc4Plugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$

    }

    /**
     *
     * the two controll buttons to choose between arc4 and spritz
     *
     * @param parent
     */
    private void arc4_spritz(Composite parent) {
        GridData _arc4 = new GridData();
        _arc4.horizontalAlignment = GridData.FILL;
        _arc4.grabExcessHorizontalSpace = true;
        _arc4.grabExcessVerticalSpace = true;
        _arc4.verticalAlignment = GridData.CENTER;

        GridData _spritz = new GridData();
        _spritz.horizontalAlignment = GridData.FILL;
        _spritz.grabExcessHorizontalSpace = true;
        _spritz.grabExcessVerticalSpace = true;
        _spritz.verticalAlignment = GridData.CENTER;

        GridData _dspritz = new GridData();
        _dspritz.horizontalAlignment = GridData.FILL;
        _dspritz.grabExcessHorizontalSpace = true;
        _dspritz.grabExcessVerticalSpace = true;
        _dspritz.verticalAlignment = GridData.CENTER;

        GridData _wspritz = new GridData();
        _wspritz.horizontalAlignment = GridData.FILL;
        _wspritz.grabExcessHorizontalSpace = true;
        _wspritz.grabExcessVerticalSpace = true;
        _wspritz.verticalAlignment = GridData.CENTER;

        GridLayout chooseGridLayout = new GridLayout();
        chooseGridLayout.numColumns = 6;

        GridData GroupGridDataChoose = new GridData();
        GroupGridDataChoose.horizontalAlignment = GridData.FILL;
        GroupGridDataChoose.grabExcessHorizontalSpace = true;
        GroupGridDataChoose.grabExcessVerticalSpace = true;
        GroupGridDataChoose.verticalAlignment = GridData.FILL;

        algoGroup = new Group(parent, SWT.NONE);
        algoGroup.setLayoutData(GroupGridDataChoose);
        algoGroup.setLayout(chooseGridLayout);
        algoGroup.setText(Messages.Arc4Page_algo);

        arc4 = new Button(algoGroup, SWT.RADIO);
        arc4.setLayoutData(_arc4);
        arc4.addListener(SWT.Selection, this);
        arc4.setText(Messages.Arc4Page_arc4);
        arc4.addListener(SWT.Selection, this);

        Label space = new Label(algoGroup, SWT.NULL);
        space.setLayoutData(_spritz);

        Label space2 = new Label(algoGroup, SWT.NULL);
        space2.setLayoutData(_spritz);

        spritz = new Button(algoGroup, SWT.RADIO);
        spritz.setLayoutData(_spritz);
        spritz.addListener(SWT.Selection, this);
        spritz.setText(Messages.Arc4Page_spritz);
        spritz.addListener(SWT.Selection, this);

        Label w = new Label(algoGroup, SWT.NULL);
        w.setLayoutData(_spritz);
        w.setText("            w:");

        dropDownSpritz = new Combo(algoGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
        dropDownSpritz.setLayoutData(_dspritz);
        dropDownSpritz.addListener(SWT.Selection, this);

        String[] values = new String[128];
        for (int a = 0; a < 128; a++) {
            values[a] = Integer.toString(2 * a + 1);
        }
        dropDownSpritz.setItems(values);
        dropDownSpritz.select(1);
        dropDownSpritz.setEnabled(false);

    }

    /**
     *
     * encrypt decrypt group.
     * <p>
     * completly usless but the user should see that it does the same if it decrypt or encrypts, i.
     * don't use the imput made here anywhere
     *
     * @param parent
     */
    private void encrypt_decrypt(Composite parent) {
        GridData _encrypt = new GridData();
        _encrypt.horizontalAlignment = GridData.FILL;
        _encrypt.grabExcessHorizontalSpace = true;
        _encrypt.grabExcessVerticalSpace = true;
        _encrypt.verticalAlignment = GridData.CENTER;

        GridData _decrypt = new GridData();
        _decrypt.horizontalAlignment = GridData.FILL;
        _decrypt.grabExcessHorizontalSpace = true;
        _decrypt.grabExcessVerticalSpace = true;
        _decrypt.verticalAlignment = GridData.CENTER;

        GridLayout CryptSizeGroupGridLayout = new GridLayout();
        CryptSizeGroupGridLayout.numColumns = 2;
        CryptSizeGroupGridLayout.makeColumnsEqualWidth = true;

        GridData GroupGridData = new GridData();
        GroupGridData.horizontalAlignment = GridData.FILL;
        GroupGridData.grabExcessHorizontalSpace = true;
        GroupGridData.grabExcessVerticalSpace = true;
        GroupGridData.verticalAlignment = GridData.FILL;

        keyIVSizeGroup = new Group(parent, SWT.NONE);
        keyIVSizeGroup.setLayoutData(GroupGridData);
        keyIVSizeGroup.setLayout(CryptSizeGroupGridLayout);
        keyIVSizeGroup.setText(Messages.Arc4Page_crypt);

        encrypt = new Button(keyIVSizeGroup, SWT.RADIO);
        encrypt.setLayoutData(_encrypt);
        encrypt.addListener(SWT.Selection, this);
        encrypt.setText(Messages.Arc4Page_decrypt);

        decrypt = new Button(keyIVSizeGroup, SWT.RADIO);
        decrypt.setLayoutData(_decrypt);
        decrypt.addListener(SWT.Selection, this);
        decrypt.setText(Messages.Arc4Page_encrypt);
    }

    /**
     * This methode creats the whole key section (may look a little bit confusing).
     * <p>
     * key input, label, controll buttons, dropdown,...
     *
     * @param parent
     */
    private void createKeyValueGroup(Composite parent) {
        GridData hexadecimalKeyInputButtonGridData = new GridData();
        hexadecimalKeyInputButtonGridData.horizontalAlignment = GridData.FILL;
        hexadecimalKeyInputButtonGridData.grabExcessHorizontalSpace = true;
        hexadecimalKeyInputButtonGridData.grabExcessVerticalSpace = true;
        hexadecimalKeyInputButtonGridData.verticalAlignment = GridData.CENTER;

        GridData binaryKeyInputButtonGridData = new GridData();
        binaryKeyInputButtonGridData.horizontalAlignment = GridData.FILL;
        binaryKeyInputButtonGridData.grabExcessHorizontalSpace = true;
        binaryKeyInputButtonGridData.grabExcessVerticalSpace = true;
        binaryKeyInputButtonGridData.verticalAlignment = GridData.CENTER;

        GridData randInputButtonGridData = new GridData();
        randInputButtonGridData.horizontalAlignment = GridData.FILL;
        randInputButtonGridData.grabExcessHorizontalSpace = true;
        randInputButtonGridData.grabExcessVerticalSpace = true;
        randInputButtonGridData.verticalAlignment = GridData.CENTER;

        GridData keyTextGridData = new GridData();
        keyTextGridData.horizontalSpan = 4;
        keyTextGridData.verticalAlignment = GridData.CENTER;
        keyTextGridData.grabExcessHorizontalSpace = true;
        keyTextGridData.horizontalAlignment = GridData.FILL;

        GridData randzero = new GridData();
        randzero.horizontalSpan = 1;
        randzero.verticalAlignment = GridData.CENTER;
        randzero.grabExcessHorizontalSpace = true;
        randzero.horizontalAlignment = GridData.FILL;
        randzero.grabExcessVerticalSpace = true;

        GridData keyDigitCountLabelGridData = new GridData();
        keyDigitCountLabelGridData.horizontalSpan = 2;
        keyDigitCountLabelGridData.horizontalAlignment = GridData.FILL;
        keyDigitCountLabelGridData.grabExcessHorizontalSpace = true;
        keyDigitCountLabelGridData.grabExcessVerticalSpace = false;
        keyDigitCountLabelGridData.verticalAlignment = GridData.CENTER;
        keyDigitCountLabelGridData.heightHint = 30;
        keyDigitCountLabelGridData.widthHint = 300;

        GridLayout keyValueGroupGridLayout = new GridLayout();
        keyValueGroupGridLayout.numColumns = 1;
        keyValueGroupGridLayout.makeColumnsEqualWidth = true;

        GridLayout parentLayout = new GridLayout();
        parentLayout.numColumns = 4;
        parentLayout.makeColumnsEqualWidth = true;

        GridLayout minorGridLayout = new GridLayout();
        minorGridLayout.numColumns = 1;
        minorGridLayout.makeColumnsEqualWidth = true;

        GridLayout minorGridLayout2 = new GridLayout();
        minorGridLayout2.numColumns = 1;
        minorGridLayout2.makeColumnsEqualWidth = true;

        GridData keyValueGroupGridData = new GridData();
        keyValueGroupGridData.grabExcessHorizontalSpace = true;
        keyValueGroupGridData.horizontalAlignment = GridData.FILL;
        keyValueGroupGridData.verticalAlignment = GridData.FILL;
        keyValueGroupGridData.grabExcessVerticalSpace = true;

        GridData comperGridData = new GridData();
        comperGridData.grabExcessHorizontalSpace = true;
        comperGridData.horizontalAlignment = GridData.FILL;
        comperGridData.verticalAlignment = GridData.FILL;
        comperGridData.grabExcessVerticalSpace = true;

        keyValueGroup = new Group(parent, SWT.NONE);
        keyValueGroup.setText(Messages.Arc4Page_key);
        keyValueGroup.setLayout(keyValueGroupGridLayout);
        keyValueGroup.setLayoutData(keyValueGroupGridData);

        Composite comper = new Composite(keyValueGroup, SWT.NONE);
        comper.setLayout(parentLayout);
        comper.setLayoutData(comperGridData);

        Composite comp2 = new Composite(comper, SWT.NONE);
        comp2.setLayout(minorGridLayout);

        Label g = new Label(comper, SWT.NULL);
        g.setText(" ");

        Composite comp = new Composite(comper, SWT.NONE);
        comp.setLayout(minorGridLayout2);
        comp.setLayoutData(randzero);

        hexadecimalKeyInputButton = new Button(comp2, SWT.RADIO);
        hexadecimalKeyInputButton.setText(Messages.Arc4Page_hex);
        hexadecimalKeyInputButton.setLayoutData(hexadecimalKeyInputButtonGridData);
        hexadecimalKeyInputButton.addListener(SWT.Selection, this);

        randomButton = new Button(comp, SWT.RADIO);
        randomButton.setText(Messages.Arc4Page_rand_key);
        randomButton.setLayoutData(randInputButtonGridData);
        randomButton.addListener(SWT.Selection, this);

        binaryKeyInputButton = new Button(comp2, SWT.RADIO);
        binaryKeyInputButton.setText(Messages.Arc4Page_bin);
        binaryKeyInputButton.setLayoutData(binaryKeyInputButtonGridData);
        binaryKeyInputButton.addListener(SWT.Selection, this);

        dropdown = new Combo(comper, SWT.DROP_DOWN | SWT.READ_ONLY);
        dropdown.setItems(inputs);
        dropdown.select(3);
        dropdown.addListener(SWT.Selection, this);
        dropdown.setText(Messages.Arc4Page_generate_null);

        zeroButton = new Button(comp, SWT.RADIO);
        zeroButton.setText(Messages.Arc4Page_zero_key);
        zeroButton.setLayoutData(randInputButtonGridData);
        zeroButton.addListener(SWT.Selection, this);

        keyText = new Text(comper, SWT.BORDER | SWT.SINGLE);
        keyText.setLayoutData(keyTextGridData);
        StringBuilder temp = new StringBuilder();
        int max = 0;

        if (keyFormatIsHexadecimal) {
            max = 512;
        } else {
            max = 2048;
        }
        for (int i = 0; i < max; i++) {
            temp.append("0"); //$NON-NLS-1$
        }
        keyText.addListener(SWT.Modify, this);
        // performs one part of the input verification a secound part will be
        // done in the "global" event handler
        keyText.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                if (keyFormatIsHexadecimal) {
                    if ((keyText.getText().length() + 1) > 512 && e.character != SWT.BS && e.character != SWT.DEL
                            && e.keyCode != 0) {
                        setErrorMessage(Messages.Arc4Page_enough);
                        e.doit = false;
                    }
                } else {
                    if ((keyText.getText().length() + 1) > 2048 && e.character != SWT.BS && e.character != SWT.DEL
                            && e.keyCode != 0) {
                        setErrorMessage(Messages.Arc4Page_enough);
                        e.doit = false;
                    }
                }
            }
        });

        keyDigitCountLabel = new Label(comper, SWT.NONE);
        keyDigitCountLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        keyDigitCountLabel.setText(Messages.Arc4Page_max2_hex + " 512" + "\n "); //$NON-NLS-2$ //$NON-NLS-1$
        
    }

    /**
     * Retunrs the key.
     *
     * @return -> key as String
     */
    public String getKeyValue() {
        return keyValue;
    }

    /**
     * find out if the input was Hexadecimal or binary.
     * 
     * @return -> true for hex
     */
    public boolean getKeyFormatIsHexadecimal() {
        return keyFormatIsHexadecimal;
    }

    /**
     * returns which algo to use Spritz or Arc4.
     *
     * @return -> true for Arc4, false for Spritz
     */
    public boolean getAlgo() {
        return isArc4;
    }

    /**
     * 
     * @return w for Spritz
     */
    public int freeW() {
        return this.w;
    }

    /**
     * event handler, for all the event's definied.
     */
    @Override
    public void handleEvent(Event event) {

        if (event.widget == hexadecimalKeyInputButton) {
            if (keyFormatIsHexadecimal == false) {
                keyFormatIsHexadecimal = true;
                keyDigitCountLabel.setText(Messages.Arc4Page_max2_hex + " 512");
                clearKey();
            }
        } else if (event.widget == binaryKeyInputButton) {
            if (keyFormatIsHexadecimal) {
                keyFormatIsHexadecimal = false;
                keyDigitCountLabel.setText(Messages.Arc4Page_max2_bin + " 2048");
                clearKey();
            }
        } else if (event.widget == arc4) {
            if (isArc4 == false) {
                isArc4 = true;
                dropDownSpritz.setEnabled(false);
            }

        } else if (event.widget == spritz) {
            if (isArc4) {
                isArc4 = false;
                dropDownSpritz.setEnabled(true);
            }
        } else if (event.widget == keyText) {

            Point t = keyText.getSelection();

            keyValue = keyText.getText();

            if (keyValue.length() != 0 || Helper == true) {
                Helper = true;
                int change = 0;
                // CLEARING_FLAG = true;
                if (keyFormatIsHexadecimal) {
                    if (keyValue.replaceAll("[^A-Fa-f0-9]", "").length() != keyValue.length()) {
                        keyText.setText(keyValue.replaceAll("[^A-Fa-f0-9]", ""));
                        change = 1;
                    }

                    if (keyValue.length() > 512) {
                        keyValue = keyValue.substring(0, 512);
                        keyText.setText(keyValue);
                        keyText.setText(keyValue.replaceAll("[^A-Fa-f0-9]", ""));
                        change = 2;
                    }
                    keyDigitCountLabel.setText(Messages.Arc4Page_amount_hex + " " + keyValue.length() + "\n" + //$NON-NLS-1$
                            Messages.Arc4Page_max2_hex + " 512"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    if (keyValue.replaceAll("[^0-1]", "").length() != keyValue.length()) {
                        keyText.setText(keyValue.replaceAll("[^0-1]", ""));
                        change = 1;
                    }
                    if (keyValue.length() > 2048) {
                        keyValue = keyValue.substring(0, 2048);
                        keyText.setText(keyValue.replaceAll("[^0-1]", ""));
                        keyText.setText(keyValue);
                        change = 2;

                    }
                    keyDigitCountLabel.setText(Messages.Arc4Page_amount_bin + " " + keyValue.length() + "\n" + //$NON-NLS-1$
                            Messages.Arc4Page_max2_bin + " 2048"); //$NON-NLS-1$ //$NON-NLS-2$

                }
                if (change == 1 && keyFormatIsHexadecimal) {
                    setErrorMessage(Messages.Arc4Page_only_hex);
                } else if (change == 1 && !keyFormatIsHexadecimal) {
                    setErrorMessage(Messages.Arc4Page_only_bin);
                } else if (change == 2) {
                    setErrorMessage(Messages.Arc4Page_enough);
                } else {
                    setErrorMessage(null);
                }

                if (t.x < keyText.getText().length() + 1) {
                    keyText.setSelection(t.x);
                    if (change != 0) {
                        keyText.setSelection(t.x - 1);
                    }
                } else {
                    keyText.setSelection(keyText.getText().length());
                }
            }

            // CLEARING_FLAG = false;
            keyValue = keyText.getText();

        } else if (event.widget == dropDownSpritz) {

            this.w = Integer.parseInt(dropDownSpritz.getText());

        } else if (event.widget == dropdown) {
            if (dropdown.getSelectionIndex() == 0) {
                String t = "0";
                if (isRand) {
                    if (keyFormatIsHexadecimal) {
                        for (int i = 0; i < 255; i++) {
                            t += rand_string_hex();
                        }
                    } else {
                        for (int i = 0; i < 1023; i++) {
                            t += rand_string_bin();
                        }
                    }
                } else {
                    if (keyFormatIsHexadecimal) {
                        for (int i = 0; i < 255; i++) {
                            t += "0";
                        }
                    } else {
                        for (int i = 0; i < 1023; i++) {
                            t += "0";
                        }
                    }
                }
                // CLEARING_FLAG = true;
                keyText.setText(t);
                // CLEARING_FLAG = false;
            }
            if (dropdown.getSelectionIndex() == 1) {
                String t = "0";

                if (isRand) {
                    if (keyFormatIsHexadecimal) {
                        for (int i = 0; i < 383; i++) {
                            t += rand_string_hex();
                        }
                    } else {
                        for (int i = 0; i < 1535; i++) {
                            t += rand_string_bin();
                        }
                    }
                } else {
                    if (keyFormatIsHexadecimal) {
                        for (int i = 0; i < 383; i++) {
                            t += "0";
                        }
                    } else {
                        for (int i = 0; i < 1535; i++) {
                            t += "0";
                        }
                    }
                }
                // CLEARING_FLAG = true;
                keyText.setText(t);
                // CLEARING_FLAG = false;
            }
            if (dropdown.getSelectionIndex() == 2) {
                String t = "0";

                if (isRand) {
                    if (keyFormatIsHexadecimal) {
                        for (int i = 0; i < 511; i++) {
                            t += rand_string_hex();
                        }
                    } else {
                        for (int i = 0; i < 2047; i++) {
                            t += rand_string_bin();
                        }
                    }
                } else {
                    if (keyFormatIsHexadecimal) {
                        for (int i = 0; i < 511; i++) {
                            t += "0";
                        }
                    } else {
                        for (int i = 0; i < 2047; i++) {
                            t += "0";
                        }
                    }
                }
                // CLEARING_FLAG = true;
                keyText.setText(t);
                // CLEARING_FLAG = false;
            }
            if (dropdown.getSelectionIndex() == 3) {

                // CLEARING_FLAG = true;
                keyText.setText("");
                // CLEARING_FLAG = false;
            }

        } else if (event.widget == zeroButton) {
            isRand = false;
        } else if (event.widget == randomButton) {
            isRand = true;
        }
        setPageComplete(mayFinish());
        this.pageComposite.layout(new Control[] {keyDigitCountLabel});
//         this.getShell().pack();
    }

    /**
     * generates a random hex-char;
     * <p>
     * <b>It's not a save PRG!!</b>
     *
     * @return -> string of one char as hex value
     */
    private String rand_string_hex() {
        String t = "";
        Random rand = new Random();
        int n = rand.nextInt();
        n = n % 16;
        if (n < 0) {
            n = n * -1;
        }

        switch (n) {
        case 0:
            t += "0";
            break;
        case 1:
            t += "1";
            break;
        case 2:
            t += "2";
            break;
        case 3:
            t += "3";
            break;
        case 4:
            t += "4";
            break;
        case 5:
            t += "5";
            break;
        case 6:
            t += "6";
            break;
        case 7:
            t += "7";
            break;
        case 8:
            t += "8";
            break;
        case 9:
            t += "9";
            break;
        case 10:
            t += "a";
            break;
        case 11:
            t += "b";
            break;
        case 12:
            t += "c";
            break;
        case 13:
            t += "d";
            break;
        case 14:
            t += "e";
            break;
        case 15:
            t += "f";
            break;
        }
        return t;

    }

    /**
     *
     * random generator for a binary char.
     * <p>
     * <b>It's not a save PRG!!</b>
     *
     * @return -> char of 1 or 0
     */
    private String rand_string_bin() {
        String t = "";
        Random rand = new Random();
        int n = rand.nextInt();
        n = n % 2;
        if (n < 0) {
            n = n * -1;
        }

        switch (n) {
        case 0:
            t += "0";
            break;
        case 1:
            t += "1";
            break;
        }

        return t;

    }

    /**
     * changes the hex to binary input and other way round when you change the Control buttons.
     */
    private void clearKey() {
        // CLEARING_FLAG = true;

        if (keyFormatIsHexadecimal) {
            keyText.setText(BinStringToHexString(keyValue));
        } else {
            keyText.setText(hexToBin(keyValue));
        }

        // CLEARING_FLAG = false;

    }

    /**
     * Returns <code>true</code>, if the page is complete and the wizard may finish.
     * <p>
     *
     * <ul>
     * <li>arc4 has to have 5 byte</li>
     * <li>no nibbles are allowed</li>
     * </ul>
     *
     * @return <code>true</code>, if the page is complete and the wizard may finish
     */
    private boolean mayFinish() {
        if (keyFormatIsHexadecimal) {
            if (keyText.getText().length() >= 10 && keyText.getText().length() % 2 == 0) {

                return true;
            } else {
                return false;
            }
        } else if (keyText.getText().length() >= 40 && keyText.getText().length() % 8 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * converting a String of binaries (0 and 1) to a hex string this will be converted in the next.
     * methode to a byte arrray
     *
     * @param key -> the key string
     * @return -> byte array
     */
    private String BinStringToHexString(String key) {
        String t = "";

        while (key.length() % 4 != 0) {
            key = "0" + key;
        }

        int[] a = new int[4];
        for (int i = 0; i < key.length(); i = i + 4) {
            if (key.charAt(i) == 48) {
                a[0] = 0;
            } else {
                a[0] = 1;
            }
            if (key.charAt(i + 1) == 48) {
                a[1] = 0;
            } else {
                a[1] = 1;
            }
            if (key.charAt(i + 2) == 48) {
                a[2] = 0;
            } else {
                a[2] = 1;
            }
            if (key.charAt(i + 3) == 48) {
                a[3] = 0;
            } else {
                a[3] = 1;
            }

            int calc = 0;

            if (a[0] == 1) {
                calc += 8;
            }

            if (a[1] == 1) {
                calc += 4;
            }

            if (a[2] == 1) {
                calc += 2;
            }

            if (a[3] == 1) {
                calc += 1;
            }

            switch (calc) {
            case 0:
                t += "0";
                break;
            case 1:
                t += "1";
                break;
            case 2:
                t += "2";
                break;
            case 3:
                t += "3";
                break;
            case 4:
                t += "4";
                break;
            case 5:
                t += "5";
                break;
            case 6:
                t += "6";
                break;
            case 7:
                t += "7";
                break;
            case 8:
                t += "8";
                break;
            case 9:
                t += "9";
                break;
            case 10:
                t += "a";
                break;
            case 11:
                t += "b";
                break;
            case 12:
                t += "c";
                break;
            case 13:
                t += "d";
                break;
            case 14:
                t += "e";
                break;
            case 15:
                t += "f";
                break;
            }

            calc = 0;

        }

        return t.toUpperCase();
    }

    /**
     * hex string to binary string; if the input is invalid it's not corrected in this method.
     *
     * @param hex -> string of hex values
     * @return String of a given hex value to binary
     */
    private String hexToBin(String hex) {
        String t = "";

        hex = hex.toUpperCase();

        for (int i = 1; i <= hex.length(); i++) {
            char tester = hex.charAt(i - 1);

            if (tester == '0') {
                t += "0000";
            }
            if (tester == '1') {
                t += "0001";
            }
            if (tester == '2') {
                t += "0010";
            }
            if (tester == '3') {
                t += "0011";
            }
            if (tester == '4') {
                t += "0100";
            }
            if (tester == '5') {
                t += "0101";
            }
            if (tester == '6') {
                t += "0110";
            }
            if (tester == '7') {
                t += "0111";
            }
            if (tester == '8') {
                t += "1000";
            }
            if (tester == '9') {
                t += "1001";
            }
            if (tester == 'A') {
                t += "1010";
            }
            if (tester == 'B') {
                t += "1011";
            }
            if (tester == 'C') {
                t += "1100";
            }
            if (tester == 'D') {
                t += "1101";
            }
            if (tester == 'E') {
                t += "1110";
            }
            if (tester == 'F') {
                t += "1111";
            }

        }

        return t;

    }
}
